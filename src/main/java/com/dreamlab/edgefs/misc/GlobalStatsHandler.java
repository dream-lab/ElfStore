package com.dreamlab.edgefs.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamlab.edgefs.controlplane.CoarseGrainedStats;
import com.dreamlab.edgefs.model.FogStats;
import com.dreamlab.edgefs.model.StorageReliability;

public class GlobalStatsHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalStatsHandler.class);
	
	private Map<Short, FogStats> globalInfo;
	private CoarseGrainedStats localInfo;
	private short selfId;
	private Map<StorageReliability, List<Short>> globalFogDistribution; /**Based on Median **/
	private Map<StorageReliability, Short> globalEdgeDistribution;

	public GlobalStatsHandler() {
		globalFogDistribution = new ConcurrentHashMap<>();
		globalEdgeDistribution = new ConcurrentHashMap<>();
	}

	public GlobalStatsHandler(Map<Short, FogStats> globalInfo, CoarseGrainedStats localInfo, short fogId) {
		super();
		this.globalInfo = globalInfo;
		this.localInfo = localInfo;
		this.selfId = fogId;
		globalFogDistribution = null;
		globalEdgeDistribution = null;
	}

	public Map<Short, FogStats> getGlobalInfo() {
		return globalInfo;
	}

	public void setGlobalInfo(Map<Short, FogStats> globalInfo) {
		this.globalInfo = globalInfo;
	}

	public CoarseGrainedStats getLocalInfo() {
		return localInfo;
	}

	public void setLocalInfo(CoarseGrainedStats localInfo) {
		this.localInfo = localInfo;
	}

	public short getSelfId() {
		return selfId;
	}

	public void setSelfId(short selfId) {
		this.selfId = selfId;
	}

	public FogStats computeTotalInfomation(Map<Short,HashMap<StorageReliability,Short>> globalAllocationMap,Map<StorageReliability, List<Short>> storageFogMap) {
		FogStats stats = computeGlobalMinMax();

		// For handling local info as well, adding an instance of FogStats
		// to the globalInfo map with a key of -1 and will remove once this
		// handling is done
		FogStats selfStats = prepareSelfStats(localInfo); 
		globalInfo.put(selfId, selfStats);
		
		long storageMedian = 0;
		int reliabilityMedian = 0;
		//checking if this device is the only device in the system
		//in that case, no need to compute the global medians and
		//just return the local medians
		if (globalInfo.size() == 1) {
			// I am the only Fog in the system
			storageMedian = selfStats.getMedianStorage();
			reliabilityMedian = selfStats.getMedianReliability();
		} else {
			storageMedian = computeGlobalStorageMedian(stats.getMinStorage(), stats.getMaxStorage());
			reliabilityMedian = computeGlobalReliabilityMedian(stats.getMinReliability(), stats.getMaxReliability());
		}
		

		stats.setMedianStorage(storageMedian);
		stats.setMedianReliability(reliabilityMedian);
		// now find out the contribution of each Fog to the
		// four quadrants by iterating over it
		computeFogContributions(stats, globalAllocationMap, storageFogMap);

		// now that we are done calculating global median storage
		// and reliability, remove the selfId key from globalInfo map
		globalInfo.remove(selfId);

		// using FogStats only as a placeholder to return multiple values
		// i.e. median storage & reliability. The other fields should not
		// be used
		FogStats medianStats = new FogStats();
		medianStats.setMedianStorage(storageMedian);
		medianStats.setMedianReliability(reliabilityMedian);
		return medianStats;
	}

	// stats has global values
	private void computeFogContributions(FogStats stats,Map<Short,HashMap<StorageReliability,Short>> globalAllocationMap, Map<StorageReliability, List<Short>> storageFogMap) {
		// use the map for the Fogs
		GlobalFogContributions globalContribution = new GlobalFogContributions(stats);
		
		LOGGER.info("global info stats "+globalInfo.toString());

		globalEdgeDistribution = globalContribution.createGlobalMapForEdge(globalInfo); 
		globalFogDistribution = globalContribution.createStorageFogMap(globalInfo);
		
		LOGGER.info("elfstore GLOBAL EDGE : " +globalEdgeDistribution);
		LOGGER.info("elfstore GLOBAL FOG : " +globalFogDistribution);
		LOGGER.info("elfstore the final output is "+globalAllocationMap.toString());
	}
	
	/**
	 * 
	 * @return the global fog edge distribution
	 */
	public Map<StorageReliability, List<Short>> getGlobalFogDistribution() {
		return globalFogDistribution;
	}
	
	/**
	 * 
	 * @return the global edge distribution
	 */
	public Map<StorageReliability, Short> getGlobalEdgeDistribution() {
		return globalEdgeDistribution;
	}

	public long computeGlobalStorageMedian(long minStorage, long maxStorage) {
		// If minStorage and maxStorage are equal, then its a tough spot

		// the space between min and max (storage or reliability) is split into
		// window and for every Fog in the system, we accumulate the count of Fog
		// devices that fall within this window
		// using double for the count per window globally, when finding contribution
		// of each Fog towards global matrix, we should integer there
		if(maxStorage == minStorage)
			return minStorage;
		double[] countPerWindow = new double[Constants.GLOBAL_INFO_SAMPLE_BUCKETS];
		//casting via double is necessary else ALL HELL BREAKS LOOSE
		double wdwSize = (double)(maxStorage - minStorage) / Constants.GLOBAL_INFO_SAMPLE_BUCKETS;
		for (Short fogId : globalInfo.keySet()) {
			FogStats current = globalInfo.get(fogId);
			long currentMin = current.getMinStorage();
			long currentMedian = current.getMedianStorage();
			long currentMax = current.getMaxStorage();
			
			//an extremely dangerous corner case
			if (currentMin == currentMax) {
				if (currentMin == minStorage) {
					countPerWindow[0] += current.getA() + current.getB() + current.getC() + current.getD();
					continue;
				}
				if (currentMin == maxStorage) {
					countPerWindow[Constants.GLOBAL_INFO_SAMPLE_BUCKETS - 1] += current.getA() + current.getB()
							+ current.getC() + current.getD();
					continue;
				}
			}

			double windowStart = minStorage * 1.0;
			double windowEnd = minStorage + wdwSize;
			int windowNum = 1;
			//since window size now is properly casted to double, the second condition 
			//is not needed but still keeping it as a reminder
			while (windowStart < maxStorage && windowNum <= Constants.GLOBAL_INFO_SAMPLE_BUCKETS) {
				if (currentMax < windowStart)
					break;
				while (currentMin >= windowEnd) {
					windowStart += wdwSize;
					windowEnd += wdwSize;
					windowNum += 1;
				}
				if (currentMin >= windowStart && currentMin < windowEnd) {
					// this is the first window with contribution from this Fog
					if (currentMax <= windowEnd) {
						// total contribution to this window
						countPerWindow[windowNum - 1] += current.getA() + current.getB() + current.getC()
								+ current.getD();
						break;
					} else if (currentMedian <= windowEnd) {
						countPerWindow[windowNum - 1] += current.getA() + current.getC();
						countPerWindow[windowNum
								- 1] += (double) ((current.getB() + current.getD()) * (windowEnd - currentMedian))
										/ (currentMax - currentMedian);
					} else {
						countPerWindow[windowNum
								- 1] += (double) ((current.getA() + current.getC()) * (windowEnd - currentMin))
										/ (currentMedian - currentMin);
					}
				} else {
					if (currentMedian >= windowStart) {
						if (currentMedian >= windowEnd) {
							countPerWindow[windowNum - 1] += (double) ((current.getA() + current.getC()) * wdwSize)
									/ (currentMedian - currentMin);
							if(currentMax == windowEnd) {
								countPerWindow[windowNum - 1] += current.getB() + current.getD();
								break;
							}
						} else {
							countPerWindow[windowNum
									- 1] += (double) ((current.getA() + current.getC()) * (currentMedian - windowStart))
											/ (currentMedian - currentMin);
							if (currentMax > windowEnd) {
								countPerWindow[windowNum - 1] += (double) ((current.getB() + current.getD())
										* (windowEnd - currentMedian)) / (currentMax - currentMedian);
							} else {
								countPerWindow[windowNum - 1] += current.getB() + current.getD();
								break;
							}
						}
					} else {
						if (currentMax >= windowStart) {
							if (currentMax >= windowEnd) {
								countPerWindow[windowNum - 1] += (double) ((current.getB() + current.getD()) * wdwSize)
										/ (currentMax - currentMedian);
							} else {
								countPerWindow[windowNum - 1] += (double) ((current.getB() + current.getD())
										* (currentMax - windowStart)) / (currentMax - currentMedian);
								break;
							}
						} else {
							// this is an unreachable area
						}
					}
				}
				windowStart += wdwSize;
				windowEnd += wdwSize;
				windowNum += 1;
			}
		}
		
		/*LOGGER.info("Storage array");
		for(int i = 0; i < Constants.GLOBAL_INFO_SAMPLE_BUCKETS; i++)
			System.out.print(countPerWindow[i] + ",");
		LOGGER.info();*/
		
		//we will divide totalSum into 2 parts to find the correct bucket
		//and the displacement within the bucket to estimate the global median
		double[] rollingSum = new double[Constants.GLOBAL_INFO_SAMPLE_BUCKETS];
		rollingSum[0] = countPerWindow[0];
		for (int i = 1; i < rollingSum.length; i++) {
			rollingSum[i] = rollingSum[i - 1] + countPerWindow[i];
		}
		double totalSum = rollingSum[Constants.GLOBAL_INFO_SAMPLE_BUCKETS - 1];
		LOGGER.info("Number of edges contributing to global storage median is : " + totalSum);
		
		double medianValue = totalSum/2;
		int medianIdx = Arrays.binarySearch(rollingSum, medianValue);
		if (medianIdx < 0) {
			// check the doc of binarySearch to understand clearly
			medianIdx = (-medianIdx) - 1;
		}
		LOGGER.info("Global Storage Median present at index : " + medianIdx);
		
		double medianWindowLength = countPerWindow[medianIdx];
		double countTillMedianWindow = 0;
		if(medianIdx != 0) {
			countTillMedianWindow = rollingSum[medianIdx-1];
		}
		//this is the number of buckets where our median is located
		double numBuckets = (medianIdx + ((medianValue - countTillMedianWindow) / medianWindowLength));
		double medianStorage = minStorage + numBuckets * wdwSize;
		return (long) medianStorage;
	}

	private FogStats prepareSelfStats(CoarseGrainedStats selfInfo) {
		byte[] info = selfInfo.getInfo();
		FogStats selfStats = new FogStats();
		selfStats.setMinStorage(Constants.interpretByteAsLong(info[0]));
		selfStats.setMedianStorage(Constants.interpretByteAsLong(info[1]));
		selfStats.setMaxStorage(Constants.interpretByteAsLong(info[2]));
		selfStats.setMinReliability(info[3]);
		selfStats.setMedianReliability(info[4]);
		selfStats.setMaxReliability(info[5]);
		selfStats.setA(info[6]);
		selfStats.setB(info[7]);
		selfStats.setC(info[8]);
		selfStats.setD(info[9]);
		return selfStats;
	}

	private int computeGlobalReliabilityMedian(int minReliability, int maxReliability) {
		// If minReliability and maxReliability are equal, then its a tough spot

		// the space between min and max (storage or reliability) is split into
		// window and for every Fog in the system, we accumulate the count of Fog
		// devices that fall within this window
		// using double for the count per window globally, when finding contribution
		// of each Fog towards global matrix, we should integer there
		if(maxReliability == minReliability)
			return minReliability;
		double[] countPerWindow = new double[Constants.GLOBAL_INFO_SAMPLE_BUCKETS];
		//casting via double is necessary else ALL HELL BREAKS LOOSE
		double wdwSize = (double)(maxReliability - minReliability) / Constants.GLOBAL_INFO_SAMPLE_BUCKETS;

		for (Short fogId : globalInfo.keySet()) {
			FogStats current = globalInfo.get(fogId);
			int currentMin = current.getMinReliability();
			int currentMedian = current.getMedianReliability();
			int currentMax = current.getMaxReliability();
			
			//an extremely dangerous corner case
			if (currentMin == currentMax) {
				if (currentMin == minReliability) {
					countPerWindow[0] += current.getA() + current.getB() + current.getC() + current.getD();
					continue;
				}
				if (currentMin == maxReliability) {
					countPerWindow[Constants.GLOBAL_INFO_SAMPLE_BUCKETS - 1] += current.getA() + current.getB()
							+ current.getC() + current.getD();
					continue;
				}
			}

			double windowStart = minReliability * 1.0;
			double windowEnd = minReliability + wdwSize;
			int windowNum = 1;
			//since window size now is properly casted to double, the second condition 
			//is not needed but still keeping it as a reminder
			while (windowStart < maxReliability && windowNum <= Constants.GLOBAL_INFO_SAMPLE_BUCKETS) {
				if (currentMax < windowStart)
					break;
				while (currentMin >= windowEnd) {
					windowStart += wdwSize;
					windowEnd += wdwSize;
					windowNum += 1;
				}
				if (currentMin >= windowStart && currentMin < windowEnd) {
					// this is the first window with contribution from this Fog
					if (currentMax <= windowEnd) {
						// total contribution to this window
						countPerWindow[windowNum - 1] += current.getA() + current.getB() + current.getC()
								+ current.getD();
						break;
					} else if (currentMedian <= windowEnd) {
						countPerWindow[windowNum - 1] += current.getA() + current.getB();
						countPerWindow[windowNum
								- 1] += (double) ((current.getC() + current.getD()) * (windowEnd - currentMedian))
										/ (currentMax - currentMedian);
					} else {
						countPerWindow[windowNum
								- 1] += (double) ((current.getA() + current.getB()) * (windowEnd - currentMin))
										/ (currentMedian - currentMin);
					}
				} else {
					if (currentMedian >= windowStart) {
						if (currentMedian >= windowEnd) {
							countPerWindow[windowNum - 1] += (double) ((current.getA() + current.getB()) * wdwSize)
									/ (currentMedian - currentMin);
							if(currentMax == windowEnd) {
								countPerWindow[windowNum - 1] += current.getC() + current.getD();
								break;
							}
						} else {
							countPerWindow[windowNum
									- 1] += (double) ((current.getA() + current.getB()) * (currentMedian - windowStart))
											/ (currentMedian - currentMin);
							if (currentMax > windowEnd) {
								countPerWindow[windowNum - 1] += (double) ((current.getC() + current.getD())
										* (windowEnd - currentMedian)) / (currentMax - currentMedian);
							} else {
								countPerWindow[windowNum - 1] += current.getC() + current.getD();
								break;
							}
						}
					} else {
						if (currentMax >= windowStart) {
							if (currentMax >= windowEnd) {
								countPerWindow[windowNum - 1] += (double) ((current.getC() + current.getD()) * wdwSize)
										/ (currentMax - currentMedian);
							} else {
								countPerWindow[windowNum - 1] += (double) ((current.getC() + current.getD())
										* (currentMax - windowStart)) / (currentMax - currentMedian);
								break;
							}
						} else {
							// this is an unreachable area
						}
					}
				}
				windowStart += wdwSize;
				windowEnd += wdwSize;
				windowNum += 1;
			}
		}
			/*LOGGER.info("Reliability array");
			for(int i = 0; i < Constants.GLOBAL_INFO_SAMPLE_BUCKETS; i++)
				System.out.print(countPerWindow[i] + ",");
			LOGGER.info();*/
		
			//we will divide totalSum into 2 parts to find the correct bucket
			//and the displacement within the bucket to estimate the global median
			double[] rollingSum = new double[Constants.GLOBAL_INFO_SAMPLE_BUCKETS];
			rollingSum[0] = countPerWindow[0];
			for(int i = 1; i < rollingSum.length; i++) {
				rollingSum[i] = rollingSum[i-1] + countPerWindow[i];
			}
			double totalSum = rollingSum[Constants.GLOBAL_INFO_SAMPLE_BUCKETS - 1];
			LOGGER.info("Number of edges contributing to global reliability median is : " + totalSum);
			
			double medianValue = totalSum/2;
			
			int medianIdx = Arrays.binarySearch(rollingSum, medianValue);
			if(medianIdx < 0) {
				//check the doc of binarySearch to understand clearly
				medianIdx = (-medianIdx) - 1;
			}
			LOGGER.info("Global Storage Median present at index : " + medianIdx);
			
			double medianWindowLength = countPerWindow[medianIdx];
			double countTillMedianWindow = 0;
			if(medianIdx != 0) {
				countTillMedianWindow = rollingSum[medianIdx-1];
			}
			//this is the number of buckets where our median is located
			double numBuckets = (medianIdx + ((medianValue - countTillMedianWindow) / medianWindowLength));
			
			double medianReliability = minReliability + numBuckets * wdwSize;
			return (int)medianReliability;
	}

	private FogStats computeGlobalMinMax() {
		List<Long> storageMin = new ArrayList<>();
		List<Long> storageMax = new ArrayList<>();
		List<Integer> reliabilityMin = new ArrayList<>();
		List<Integer> reliabilityMax = new ArrayList<>();
		for (Short fogId : globalInfo.keySet()) {
			FogStats current = globalInfo.get(fogId);
			storageMin.add(current.getMinStorage());
			storageMax.add(current.getMaxStorage());
			reliabilityMin.add(current.getMinReliability());
			reliabilityMax.add(current.getMaxReliability());
		}
		// include this device as well
		byte[] localStats = localInfo.getInfo();
		// 1st byte is minStorage
		storageMin.add(Constants.interpretByteAsLong(localStats[0]));
		// 3rd byte is maxStorage
		storageMax.add(Constants.interpretByteAsLong(localStats[2]));
		// 4th byte is minReliability
		reliabilityMin.add((int) localStats[3]);
		// 6th byte is maxReliability
		reliabilityMax.add((int) localStats[5]);

		Collections.sort(storageMin);
		Collections.sort(storageMax, Collections.reverseOrder());
		Collections.sort(reliabilityMin);
		Collections.sort(reliabilityMax, Collections.reverseOrder());

		// this is used as a placeholder for the four values to be returned
		FogStats globalStats = new FogStats();
		globalStats.setMinStorage(storageMin.get(0));
		globalStats.setMaxStorage(storageMax.get(0));
		globalStats.setMinReliability(reliabilityMin.get(0));
		globalStats.setMaxReliability(reliabilityMax.get(0));
		return globalStats;
	}

}
