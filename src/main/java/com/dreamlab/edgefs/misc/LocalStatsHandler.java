package com.dreamlab.edgefs.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamlab.edgefs.controlplane.CoarseGrainedStats;
import com.dreamlab.edgefs.model.EdgeInfo;
import com.dreamlab.edgefs.model.LocalEdgeStats;
import com.dreamlab.edgefs.model.StorageReliability;

public class LocalStatsHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocalStatsHandler.class);
	
	private Map<Short, EdgeInfo> localEdgesMap;
	private CoarseGrainedStats coarseGrainedStats;
	private Map<StorageReliability, List<Short>> localEdgeMapping;
	private Set<Short> noStorageEdges;

	public LocalStatsHandler() {
		
	}

	public LocalStatsHandler(Map<Short, EdgeInfo> localEdgeStats, CoarseGrainedStats coarseGrainedStats,
			Map<StorageReliability, List<Short>> edgeMapping, Set<Short> blacklistedEdges) {
		super();
		this.localEdgesMap = localEdgeStats;
		this.coarseGrainedStats = coarseGrainedStats;
		this.localEdgeMapping = edgeMapping;
		this.noStorageEdges = blacklistedEdges;
	}

	public Map<Short, EdgeInfo> getLocalEdgeStats() {
		return localEdgesMap;
	}

	public void setLocalEdgeStats(Map<Short, EdgeInfo> localEdgeStats) {
		this.localEdgesMap = localEdgeStats;
	}

	public CoarseGrainedStats getCoarseGrainedStats() {
		return coarseGrainedStats;
	}

	public void setCoarseGrainedStats(CoarseGrainedStats coarseGrainedStats) {
		this.coarseGrainedStats = coarseGrainedStats;
	}

	public Map<StorageReliability, List<Short>> getLocalEdgeMapping() {
		return localEdgeMapping;
	}

	public void setLocalEdgeMapping(Map<StorageReliability, List<Short>> localEdgeMapping) {
		this.localEdgeMapping = localEdgeMapping;
	}
	
	//returning the median reliability and median storage
	//using this value for test cases
	public LocalEdgeStats computeLocalEdgeStats() {
		if (localEdgesMap == null || localEdgesMap.isEmpty()) {
			return null;
		}
		//this return three instances of LocalEdgeStats
		//first has min storage and min reliability
		//second has median storage and median reliability
		//third has max storage and max reliability
		List<LocalEdgeStats> stats = EdgeInfo.minMedianMax(localEdgesMap, noStorageEdges);
		if(stats == null || stats.isEmpty()) {
			//this means no devices are now active, we have come from
			//some active devices to no active devices
			populateMetrics(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
			return null;
		}
		
		LOGGER.info("MinS : " + stats.get(0).getStorage() 
				+ ", MinR : " + stats.get(0).getReliability());
		LOGGER.info("MedianS : " + stats.get(1).getStorage() 
				+ ", MedianR : " + stats.get(1).getReliability());
		LOGGER.info("MaxS : " + stats.get(2).getStorage() 
				+ ", MaxR : " + stats.get(2).getReliability());
		findDistribution(stats);
		return stats.get(1);
	}

	private void findDistribution(List<LocalEdgeStats> stats) {
		//Take care of special case when minStorage == maxStorage
		//Then start with equally distributing b+d between a+c
		//as all have the same storage space
		long minStorage = stats.get(0).getStorage();
		double minReliability = stats.get(0).getReliability();
		long maxStorage = stats.get(2).getStorage();
		double maxReliability = stats.get(2).getReliability();
		
		//this contains only active edges
		int numEdges = 0;
		
		int a = 0, b = 0, c = 0, d = 0;
		long medianStorage = stats.get(1).getStorage();
		double medianReliability = stats.get(1).getReliability();
		
		for(Short key : localEdgesMap.keySet()) {
			EdgeInfo edgeInfo = localEdgesMap.get(key);
			if(!edgeInfo.getStatus().equals("A") || noStorageEdges.contains(key))
				continue;
			numEdges += 1;
			LocalEdgeStats current = edgeInfo.getStats();
			if(current.getStorage() >= medianStorage) {
				if(current.getReliability() >= medianReliability) {
					d += 1;
				} else {
					b += 1;
				}
			} else {
				if(current.getReliability() >= medianReliability) {
					c += 1;
				} else {
					a += 1;
				}
			}
		}
		//at this point, we partitioned based on storage basis,
		//so (a+c) = (b+d) holds but (a+b) = (c+d) might not.
		//we will try to balance this property as well
		int diff = Math.abs((a+b) - (c+d));
		if(diff == 0 || ((numEdges & 1) != 0 && diff == 1)) {
			//we are done , no need to shift reliability median
			//populate metrics
			populateMetrics(minStorage, medianStorage, maxStorage, minReliability, medianReliability,
					maxReliability, a, b, c, d);
		} else {
			List<EdgeInfo> list = new ArrayList<>(localEdgesMap.values());
			//Collections.sort(list, LocalEdgeStats.doubleComparator);
			if((a+b) > (c+d)) {
				//Collections.reverse(list);
				//we need to shift median reliability to a lower value
				double windowSize = (medianReliability - minReliability)/Constants.RELIABILITY_SAMPLE_BUCKETS;
				//windowSize cannot be 0 in this case because if medianReliability
				//and minReliability were equal, then (a+b) would be 0 and certainly
				//less than (c+d)
				
				//we need to take as equal as possible from a and b (i.e. low or high storage)
				int maxFromLoworHigh = diff/2 + (((diff & 1) != 0)?1:0);
				int lowTaken = 0, highTaken = 0;
				boolean isDone = false;
				while(medianReliability >= minReliability) {
					Iterator<EdgeInfo> it = list.iterator();
					while(it.hasNext()) {
						EdgeInfo edgeInfo = it.next();
						if(!edgeInfo.getStatus().equals("A") || 
								noStorageEdges.contains(edgeInfo.getNodeId())) {
							continue;
						}
						LocalEdgeStats next = edgeInfo.getStats();
						if(next.getReliability() >= medianReliability - windowSize && 
								next.getReliability() < medianReliability) {
							if(next.getStorage() >= medianStorage && highTaken < maxFromLoworHigh) {
								b -= 1;
								d += 1;
								highTaken += 1;
								int diff1 = (a+b) - (c+d);
								if(diff1 == 0 || ((numEdges & 1) != 0 && diff1 == 1)) {
									//done, populate metrics
									populateMetrics(minStorage, medianStorage, maxStorage, minReliability,
											medianReliability, maxReliability, a, b, c, d);
									isDone = true;
									break;
								}
							}
							if(next.getStorage() < medianStorage && lowTaken < maxFromLoworHigh) {
								a -= 1;
								c += 1;
								lowTaken += 1;
								int diff1 = (a+b) - (c+d);
								if(diff1 == 0 || ((numEdges & 1) != 0 && diff1 == 1)) {
									//done, populate metrics
									populateMetrics(minStorage, medianStorage, maxStorage, minReliability,
											medianReliability, maxReliability, a, b, c, d);
									isDone = true;
									break;
								}
							}
						}
					}
					medianReliability = medianReliability - windowSize;
				}
				if(!isDone) {
					//unable to achieve the goal with given config but reached
					//the end, find the best effort solution
					
					//this correction to medianReliability is necessary as we are out of
					//the while loop
					medianReliability = medianReliability + windowSize;
					//populate metrics
					populateMetrics(minStorage, medianStorage, maxStorage, minReliability, medianReliability,
							maxReliability, a, b, c, d);
				}
			} else {
				//we need to shift median reliability to a lower value
				double windowSize = (maxReliability - medianReliability)/Constants.RELIABILITY_SAMPLE_BUCKETS;
				//it might happen that if medianReliability and maxReliability are same
				//so no shifting in medianReliability will make while stuck in an infinite loop
				boolean shiftMedian = true;
				boolean isDone = false;
				if(windowSize == 0) {
					populateMetrics(minStorage, medianStorage, maxStorage, minReliability, medianReliability,
							maxReliability, a, b, c, d);
					isDone = true;
					shiftMedian = false;
				}
				//we need to take as equal as possible from c and d (i.e. low or high storage)
				int maxFromLoworHigh = diff/2 + (((diff & 1) != 0)?1:0);
				int lowTaken = 0, highTaken = 0;
				while(shiftMedian && medianReliability <= maxReliability) {
					Iterator<EdgeInfo> it = list.iterator();
					while(it.hasNext()) {
						EdgeInfo edgeInfo = it.next();
						if(!edgeInfo.getStatus().equals("A") || 
								noStorageEdges.contains(edgeInfo.getNodeId()) ){
							continue;
						}
						LocalEdgeStats next = edgeInfo.getStats();
						if(next.getReliability() < medianReliability + windowSize && 
								next.getReliability() >= medianReliability) {
							if(next.getStorage() >= medianStorage && highTaken < maxFromLoworHigh) {
								d -= 1;
								b += 1;
								highTaken += 1;
								int diff1 = (c+d) - (a+b);
								if(diff1 == 0 || ((numEdges & 1) != 0 && diff1 == 1)) {
									//done, populate metrics
									populateMetrics(minStorage, medianStorage, maxStorage, minReliability,
											medianReliability, maxReliability, a, b, c, d);
									isDone = true;
									break;
								}
							}
							if(next.getStorage() < medianStorage && lowTaken < maxFromLoworHigh) {
								c -= 1;
								a += 1;
								lowTaken += 1;
								int diff1 = (c+d) - (a+b);
								if(diff1 == 0 || ((numEdges & 1) != 0 && diff1 == 1)) {
									//done, populate metrics
									populateMetrics(minStorage, medianStorage, maxStorage, minReliability,
											medianReliability, maxReliability, a, b, c, d);
									isDone = true;
									break;
								}
							}
						}
					}
					medianReliability = medianReliability + windowSize;
				}
				if(!isDone) {
					//unable to achieve the goal with given config but reached
					//the end, find the best effort solution
					
					//this correction to medianReliability is necessary as we are out of
					//the while loop
					medianReliability = medianReliability - windowSize;
					populateMetrics(minStorage, medianStorage, maxStorage, minReliability, medianReliability,
							maxReliability, a, b, c, d);
				}
			}
		}
	}
	
	private void populateMetrics(long minStorage, long medianStorage, long maxStorage, double minReliability,
			double medianReliability, double maxReliability, int a, int b, int c, int d) {
		//first populating fine-grained metrics for this Fog
		LOGGER.info("New Median Reliability : " + minReliability);
		//the dead edge devices (status "D") or the devices with low storage should be 
		//removed from the localEdgeMapping map
		for(Short key : localEdgesMap.keySet()) {
			EdgeInfo edgeInfo = localEdgesMap.get(key);
			if(!edgeInfo.getStatus().equals("A") || noStorageEdges.contains(key)) {
				//this edge should not be considered for further writes
				removeEdgeFromLocalMapping(key);
				continue;
			}
			LocalEdgeStats current = edgeInfo.getStats();
			if(current.getStorage() >= medianStorage) {
				if(current.getReliability() >= medianReliability) {
					if(localEdgeMapping.get(StorageReliability.HH) == null)
						localEdgeMapping.put(StorageReliability.HH, new ArrayList<>());
					StorageReliability previousState = checkEdgePreviousStatus(key);
					if(previousState != null && previousState != StorageReliability.HH) {
						localEdgeMapping.get(previousState).remove(key);
					}
					localEdgeMapping.get(StorageReliability.HH).add(key);
				} else {
					if(localEdgeMapping.get(StorageReliability.HL) == null)
						localEdgeMapping.put(StorageReliability.HL, new ArrayList<>());
					StorageReliability previousState = checkEdgePreviousStatus(key);
					if(previousState != null && previousState != StorageReliability.HL) {
						localEdgeMapping.get(previousState).remove(key);
					}
					localEdgeMapping.get(StorageReliability.HL).add(key);
				}
			} else {
				if(current.getReliability() >= medianReliability) {
					if(localEdgeMapping.get(StorageReliability.LH) == null)
						localEdgeMapping.put(StorageReliability.LH, new ArrayList<>());
					StorageReliability previousState = checkEdgePreviousStatus(key);
					if(previousState != null && previousState != StorageReliability.LH) {
						localEdgeMapping.get(previousState).remove(key);
					}
					localEdgeMapping.get(StorageReliability.LH).add(key);
				} else {
					if(localEdgeMapping.get(StorageReliability.LL) == null)
						localEdgeMapping.put(StorageReliability.LL, new ArrayList<>());
					StorageReliability previousState = checkEdgePreviousStatus(key);
					if(previousState != null && previousState != StorageReliability.LL) {
						localEdgeMapping.get(previousState).remove(key);
					}
					localEdgeMapping.get(StorageReliability.LL).add(key);
				}
			}
		}
		
		//now lets populate the coarse-grained metrics
		populateCoarseGrainedStats(minStorage, medianStorage, maxStorage, minReliability, medianReliability,
				maxReliability, a, b, c, d);
	}
	
	private void removeEdgeFromLocalMapping(Short key) {
		StorageReliability localState = checkEdgePreviousStatus(key);
		if(localState == null)
			return;
		List<Short> list = localEdgeMapping.get(localState);
		//removing from the list will be fine as in other threads we
		//are randomly picking an edge from a category for writes and
		//not using iterator. The iterator of our list is fail-fast , so
		//if there is a need to iterate over the list, don't use iterator
		list.remove(key);
	}

	private void populateCoarseGrainedStats(long minStorage, long medianStorage, long maxStorage, 
			double minReliability, double medianReliability, double maxReliability, int a, int b, int c, int d) {
		byte[] info = coarseGrainedStats.getInfo();
		info[0] = Constants.encodeLongAsByte(minStorage);
		info[1] = Constants.encodeLongAsByte(medianStorage);
		info[2] = Constants.encodeLongAsByte(maxStorage);
		info[3] = (byte) ((int)minReliability);
		info[4] = (byte) ((int)medianReliability);
		info[5] = (byte) ((int)maxReliability);
		info[6] = (byte) a;
		info[7] = (byte) b;
		info[8] = (byte) c;
		info[9] = (byte) d;
	}

	private StorageReliability checkEdgePreviousStatus(Short key) {
		if(localEdgeMapping.get(StorageReliability.HH) != null && 
				localEdgeMapping.get(StorageReliability.HH).contains(key)) {
			return StorageReliability.HH;
		} else if(localEdgeMapping.get(StorageReliability.HL) != null && 
				localEdgeMapping.get(StorageReliability.HL).contains(key)) {
			return StorageReliability.HL;
		} else if(localEdgeMapping.get(StorageReliability.LH) != null && 
				localEdgeMapping.get(StorageReliability.LH).contains(key)) {
			return StorageReliability.LH;
		} else if(localEdgeMapping.get(StorageReliability.LL) != null && 
				localEdgeMapping.get(StorageReliability.LL).contains(key)) {
			return StorageReliability.LL;
		}
		return null;
	}
	
}
