package com.dreamlab.edgefs.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamlab.edgefs.model.FogStats;
import com.dreamlab.edgefs.model.StorageReliability;
import com.dreamlab.edgefs.servicehandler.FogServiceHandler;

/**
 * 
 * @author swamiji
 *
 */
public class GlobalFogContributions {

	private double rGlobalMax, rGlobalMedian, rGlobalMin, sGlobalMax, sGlobalMedian, sGlobalMin;
	private int LL, LH, HH, HL;
	private static final Logger LOGGER = LoggerFactory.getLogger(FogServiceHandler.class);

	/**
	 * 
	 * @param stats The global stats, initializing the min,median,max storage and
	 *              reliability
	 */
	public GlobalFogContributions(FogStats stats) {
		// set this here
		rGlobalMax = stats.getMaxReliability();
		rGlobalMedian = stats.getMedianReliability();
		rGlobalMin = stats.getMinReliability();

		sGlobalMax = stats.getMaxStorage();
		sGlobalMedian = stats.getMedianStorage();
		sGlobalMin = stats.getMinStorage();

		LOGGER.info("Stats here " + stats.toString());

		LL = 0;
		LH = 0;
		HH = 0;
		HL = 0;
	}

	/**
	 * 
	 * @param globalInfo Set of Fogs using which we need to prepare a
	 *                   globalAllocationMap
	 * @return HashMap<Short,HashMap<StorageReliability,Short>> => FogID1 => HH => 3
	 *         HL => 4 FogID2 => HH => 2 HL => 4
	 */
	public HashMap<StorageReliability, Short> createGlobalMapForEdge(Map<Short, FogStats> globalInfo) {

		/**
		 * The fog stat has the global max, min (reliability and storage, global median
		 * for storage and reliability
		 **/

		LOGGER.info(" global info here " + globalInfo.toString());
		HashMap<Short, HashMap<StorageReliability, Short>> globalAllocationMap = new HashMap<Short, HashMap<StorageReliability, Short>>();

		for (Short fogId : globalInfo.keySet()) {
			FogStats singleFog = globalInfo.get(fogId);

			HashMap<StorageReliability, Short> aMap = computeContributionOfFogToGlobalMap(singleFog.getA(),
					singleFog.getMinStorage(), singleFog.getMedianStorage(), singleFog.getMinReliability(),
					singleFog.getMedianReliability());
			HashMap<StorageReliability, Short> bMap = computeContributionOfFogToGlobalMap(singleFog.getB(),
					singleFog.getMedianStorage(), singleFog.getMaxStorage(), singleFog.getMinReliability(),
					singleFog.getMedianReliability());
			HashMap<StorageReliability, Short> cMap = computeContributionOfFogToGlobalMap(singleFog.getC(),
					singleFog.getMinStorage(), singleFog.getMedianStorage(), singleFog.getMedianReliability(),
					singleFog.getMaxReliability());
			HashMap<StorageReliability, Short> dMap = computeContributionOfFogToGlobalMap(singleFog.getD(),
					singleFog.getMedianStorage(), singleFog.getMaxStorage(), singleFog.getMedianReliability(),
					singleFog.getMaxReliability());

			HashMap<StorageReliability, Short> fogLevelAllocationMap = combineValuesForAFog(aMap, bMap, cMap, dMap);
			globalAllocationMap.put(fogId, fogLevelAllocationMap);

		}

		HashMap<StorageReliability, Short> globalEdgeDistribution = new HashMap<StorageReliability, Short>();
		globalEdgeDistribution.put(StorageReliability.HH, (short) HH);
		globalEdgeDistribution.put(StorageReliability.HL, (short) HL);
		globalEdgeDistribution.put(StorageReliability.LL, (short) LL);
		globalEdgeDistribution.put(StorageReliability.LH, (short) LH);

		return globalEdgeDistribution;
	}

	/**
	 * 
	 * @param a the number of edges in the LL Quadrant
	 * @param b the number of edges in the HL Quadrant
	 * @param c the number of edges in the LH Quadrant
	 * @param d the number of edges in the HH Quadrant
	 * @return the Storage Reliability quadrant for maximum edges of these four
	 */
	public StorageReliability greatestOfFour(int a, int b, int c, int d) {
		StorageReliability choice = StorageReliability.HH;
		LOGGER.info("The edges counts are " + a + " : " + b + " : " + c + " : " + d);

		int max1 = (a > b) ? a : b;
		int max2 = (c > d) ? c : d;

		int max = (max1 > max2) ? max1 : max1;

		if (max == d) {
			choice = StorageReliability.HH;
		} else if (max == b) {
			choice = StorageReliability.HL;
		} else if (max == c) {
			choice = StorageReliability.LH;
		} else {
			choice = StorageReliability.LL;
		}

		LOGGER.info("The max choice is " + choice + " the max number of edges are " + max);
		return choice;
	}

	/**
	 * 
	 * @param globalInfo , the set of all fogs and their 10 bytes
	 * @return a storageReliability , Fog Map. HH => Fog1, Fog2 HL => Fog3,
	 */
	public HashMap<StorageReliability, List<Short>> createStorageFogMap(Map<Short, FogStats> globalInfo) {

		LOGGER.info("The update Fog map sent is " + globalInfo.toString());

		HashMap<StorageReliability, List<Short>> storageFogMap = new HashMap<StorageReliability, List<Short>>();
		storageFogMap.put(StorageReliability.HH, new ArrayList<Short>());
		storageFogMap.put(StorageReliability.HL, new ArrayList<Short>());
		storageFogMap.put(StorageReliability.LL, new ArrayList<Short>());
		storageFogMap.put(StorageReliability.LH, new ArrayList<Short>());

		LOGGER.info(
				"The global storage median, " + sGlobalMedian + " the global reliability median " + rGlobalMedian);

		for (Short fogId : globalInfo.keySet()) {

			FogStats fogStat = globalInfo.get(fogId);

			LOGGER.info("The local storage median, " + fogStat.getMedianStorage()
					+ " the global reliability median " + fogStat.getMedianReliability());

			double fogMedStorage = fogStat.getMedianStorage();
			double fogMedRel = fogStat.getMedianReliability();

			if ((fogMedStorage <= sGlobalMedian) && (fogMedRel < rGlobalMedian)) {
				if (fogMedStorage == sGlobalMedian) {
					ArrayList<Short> resArray = (ArrayList<Short>) storageFogMap.get(StorageReliability.HL);
					resArray.add(fogId);
					storageFogMap.put(StorageReliability.HL, resArray);
				} else {
					ArrayList<Short> resArray = (ArrayList<Short>) storageFogMap.get(StorageReliability.LL);
					resArray.add(fogId);
					storageFogMap.put(StorageReliability.LL, resArray);
				}

			} else if ((fogMedStorage < sGlobalMedian) && (fogMedRel >= rGlobalMedian)) {
				if (fogMedStorage >= rGlobalMedian) {
					ArrayList<Short> resArray = (ArrayList<Short>) storageFogMap.get(StorageReliability.LH);
					resArray.add(fogId);
					storageFogMap.put(StorageReliability.LH, resArray);
				}

			} else if ((fogMedStorage >= sGlobalMedian) && (fogMedRel > rGlobalMedian)) {
				if (fogMedStorage >= sGlobalMedian) {
					ArrayList<Short> resArray = (ArrayList<Short>) storageFogMap.get(StorageReliability.HH);
					resArray.add(fogId);
					storageFogMap.put(StorageReliability.HH, resArray);
				}

			} else if ((fogMedStorage > sGlobalMedian) && (fogMedRel <= rGlobalMedian)) {
				if (fogMedStorage == rGlobalMedian) {
					ArrayList<Short> resArray = (ArrayList<Short>) storageFogMap.get(StorageReliability.HH);
					resArray.add(fogId);
					storageFogMap.put(StorageReliability.HH, resArray);
				} else {
					ArrayList<Short> resArray = (ArrayList<Short>) storageFogMap.get(StorageReliability.HL);
					resArray.add(fogId);
					storageFogMap.put(StorageReliability.HL, resArray);
				}

			} else {
				LOGGER.info(
						"Storage Median and Reliability median of local fog co-incides with global Fog hence allotting randomly ");

				StorageReliability choice = greatestOfFour(fogStat.getA(), fogStat.getB(), fogStat.getC(),
						fogStat.getD());
				LOGGER.info("the greatest contribution is coming from the quadrant " + choice);
				ArrayList<Short> resArray = (ArrayList<Short>) storageFogMap.get(choice);
				resArray.add(fogId);
				storageFogMap.put(choice, resArray);

			}

			LOGGER.info("Storage Fog map " + storageFogMap.toString());
		}

		return storageFogMap;
	}

	/**
	 * Combiner for the singly allocated values of a fog
	 * 
	 * @param aMap a's contribution to all 4, LH,LL,HL,HH
	 * @param bMap b's contribution to all 4, LH,LL,HL,HH
	 * @param cMap c's contribution to all 4, LH,LL,HL,HH
	 * @param dMap d's contribution to all 4, LH,LL,HL,HH
	 * @return
	 */
	private HashMap<StorageReliability, Short> combineValuesForAFog(HashMap<StorageReliability, Short> aMap,
			HashMap<StorageReliability, Short> bMap, HashMap<StorageReliability, Short> cMap,
			HashMap<StorageReliability, Short> dMap) {

		HashMap<StorageReliability, Short> combinedMap = new HashMap<StorageReliability, Short>();

		for (StorageReliability storageRel : aMap.keySet()) {

			if (!combinedMap.containsKey(storageRel)) {
				combinedMap.put(storageRel, aMap.get(storageRel));
			} else {
				combinedMap.put(storageRel, (short) (combinedMap.get(storageRel) + aMap.get(storageRel)));
			}
		}

		for (StorageReliability storageRel : bMap.keySet()) {

			if (!combinedMap.containsKey(storageRel)) {
				combinedMap.put(storageRel, bMap.get(storageRel));
			} else {
				combinedMap.put(storageRel, (short) (combinedMap.get(storageRel) + bMap.get(storageRel)));
			}
		}

		for (StorageReliability storageRel : cMap.keySet()) {

			if (!combinedMap.containsKey(storageRel)) {
				combinedMap.put(storageRel, cMap.get(storageRel));
			} else {
				combinedMap.put(storageRel, (short) (combinedMap.get(storageRel) + cMap.get(storageRel)));
			}
		}

		for (StorageReliability storageRel : dMap.keySet()) {

			if (!combinedMap.containsKey(storageRel)) {
				combinedMap.put(storageRel, dMap.get(storageRel));
			} else {
				combinedMap.put(storageRel, (short) (combinedMap.get(storageRel) + dMap.get(storageRel)));
			}
		}

		return combinedMap;
	}

	/**
	 * calculate the contribution of a single cell eg:a to different areas of global
	 * map
	 **/
	public HashMap<StorageReliability, Short> computeContributionOfFogToGlobalMap(int a, double sMin, double sMax,
			double rMin, double rMax) {

		LOGGER.info("a " + a + " smin " + sMin + " smax " + sMax + " rMin" + rMin + " raMax" + rMax);
		double area = (sMax - sMin) * (rMax - rMin);
		LOGGER.info("area is " + area);

		HashMap<StorageReliability, Short> quadrantValueMap = new HashMap<StorageReliability, Short>();

		if (area == 0) { /** Special cases **/
			/**
			 * can be due to => smax = smin => rmax = rmin => smax = smin = rmax = rmin *
			 */

			short contributingEdges1 = 0;
			short contributingEdges2 = 0;
			short contributingEdges3 = 0;
			short contributingEdges4 = 0;

			/** all 4 values are equal **/
			if (sMax == sMin && rMax == rMin) {
				for (int i = 0; i < a; i++) {
					if (i % 2 == 0)
						contributingEdges1++;
					else
						contributingEdges2++;
				}

				HH = HH + contributingEdges1;
				HL = HL + contributingEdges2;
				quadrantValueMap.put(StorageReliability.HH, (short) HH);
				quadrantValueMap.put(StorageReliability.HL, (short) HL);

			} else if (sMax == sMin && sMax >= sGlobalMedian) { /**
																 * A single vertical line of reliability, falling right
																 * to the global median
																 **/
				/** the line is above the reliability median **/
				if(rMin>=rGlobalMedian) {
					LOGGER.info("The case which was causing negative allocations "+" make all allocations of a to the quadrant it falls in ");
					HH = HH + a;					
				}else if(rMax<=rGlobalMedian) { /** The line is below the reliability median **/
					HL = HL + a;
					LOGGER.info("The case which was causing negative allocations "+" make all allocations of a to the quadrant it falls in ");
				}
				else {
					double low = (rGlobalMedian - rMin) / (rMax - rMin);
					contributingEdges2 = (short) (a * low);
					contributingEdges1 = (short) (a - contributingEdges2);
					HL = HL + contributingEdges2;
					HH = HH + contributingEdges1;

					LOGGER.info("1st quad intersecting");
					LOGGER.info(
							"Contributing edfges 1 " + contributingEdges1 + "Contributing edfges 2 " + contributingEdges2);
	
				}			
				
				quadrantValueMap.put(StorageReliability.HL, (short) HL);
				quadrantValueMap.put(StorageReliability.HH, (short) HH);

			} else if (sMax == sMin && sMax < sGlobalMedian) { /**
																 * A single vertical line of reliability, falling left
																 * to the global median
																 **/
				/** the line is above the reliability median **/
				if(rMin>=rGlobalMedian) {
					LOGGER.info("The case which was causing negative allocations "+" make all allocations of a to the quadrant it falls in ");
					LH = LH + a;					
				}else if(rMax<=rGlobalMedian) { /** The line is below the reliability median **/
					LL = LL + a;
					LOGGER.info("The case which was causing negative allocations "+" make all allocations of a to the quadrant it falls in ");
				}else {
					double low = (rGlobalMedian - rMin) / (rMax - rMin);
					contributingEdges2 = (short) (a * low);
					contributingEdges1 = (short) (a - contributingEdges2);
					LL = LL + contributingEdges2;
					LH = LH + contributingEdges1;

					LOGGER.info("2nd quad intersecting ");
					LOGGER.info(
							"Contributing edfges 1 " + contributingEdges1 + "Contributing edfges 2 " + contributingEdges2);
					
				}
				
				quadrantValueMap.put(StorageReliability.LL, (short) LL);
				quadrantValueMap.put(StorageReliability.LH, (short) LH);

			} else if (rMax == rMin
					&& rMax >= rGlobalMedian) { /** A single horizontal line of storage above global median **/
				
				if(sMax <= sGlobalMedian) {
					LH = LH + a;
				}else if(sMin >= sGlobalMedian) {
					HH = HH + a;
				}else {
					double low = (sGlobalMedian - sMin) / (sMax - sMin);
					contributingEdges3 = (short) (a * low);
					contributingEdges4 = (short) (a - contributingEdges3);
					LH = LH + contributingEdges3;
					HH = HH + contributingEdges4;

					LOGGER.info("3rd quad intersecting");
					LOGGER.info(
							"Contributing edfges 1 " + contributingEdges1 + "Contributing edfges 2 " + contributingEdges2);
	
				}
			
				quadrantValueMap.put(StorageReliability.LH, (short) LH);
				quadrantValueMap.put(StorageReliability.HH, (short) HH);

			} else { /** A single horizontal line of storage below global median **/
				
				if(sMax <= sGlobalMedian) {
					LL = LL + a;
				}else if(sMin >= sGlobalMedian ) {
					HL = HL + a;
				}else {
					double low = (sGlobalMedian - sMin) / (sMax - sMin);
					contributingEdges3 = (short) (a * low);
					contributingEdges4 = (short) (a - contributingEdges3);
					LL = LL + contributingEdges3;
					HL = HL + contributingEdges4;

					LOGGER.info("4th quad intersecting");
					LOGGER.info(
							"Contributing edfges 1 " + contributingEdges1 + "Contributing edfges 2 " + contributingEdges2);
				}

				quadrantValueMap.put(StorageReliability.LL, (short) LL);
				quadrantValueMap.put(StorageReliability.HL, (short) HL);
			}

			return quadrantValueMap;
		}

		/** Single Square, Single Quadrant **/
		if (rMin >= rGlobalMedian && sMax <= sGlobalMedian) {
			quadrantValueMap.put(StorageReliability.LH, new Short((short) a));
			LH = LH + a;
		} else if (rMax <= rGlobalMedian && sMax <= sGlobalMedian) {
			quadrantValueMap.put(StorageReliability.LL, new Short((short) a));
			LL = LL + a;
		} else if (rMax <= rGlobalMedian && sMin >= sGlobalMedian) {
			quadrantValueMap.put(StorageReliability.HL, new Short((short) a));
			HL = HL + a;
		} else if (rMin >= rGlobalMedian && sMin >= sGlobalMedian) {
			quadrantValueMap.put(StorageReliability.HH, new Short((short) a));
			HH = HH + a;
		} /** Single Square, Double Quadrant **/
		else if (sMax <= sGlobalMedian && (rMin < rGlobalMedian && rMax > rGlobalMedian)) { // check this

			double LH_area = ((rMax - rGlobalMedian) * (sMax - sMin)) / (area); // LOW storage high reliability area
			short contributingEdges1 = (short) ((short) a * LH_area);

			double LL_area = ((rGlobalMedian - rMin) * (sMax - sMin)) / (area); // Low storage low reliability area
			short contributingEdges2 = (short) Math.floor(((short) a * LL_area));

			if ((contributingEdges1 + contributingEdges2) < a) {

				short deficit = (short) (a - (contributingEdges1 + contributingEdges2));
				LOGGER.info("deficit node exists in LH LL quadrant config");
				/** Randomly allot the remaining edges between both quadrants **/
				Random rand = new Random();
				int randIndex = 0;
				for (int i = 0; i < deficit; i++) {
					randIndex = rand.nextInt(2);

					if (randIndex == 0)
						contributingEdges1 = (short) (contributingEdges1 + 1);
					else
						contributingEdges2 = (short) (contributingEdges2 + 1);
				}
			}

			quadrantValueMap.put(StorageReliability.LH, new Short(contributingEdges1));
			quadrantValueMap.put(StorageReliability.LL, new Short(contributingEdges2));
			LH = LH + contributingEdges1;
			LL = LL + contributingEdges2;

		} else if (rMax <= rGlobalMedian && sMin < sGlobalMedian && sMax > sGlobalMedian) {

			double LL_area = ((rMax - rMin) * (sGlobalMedian - sMin)) / (area); // LL area
			short contributingEdges1 = (short) ((short) a * LL_area);

			double HL_area = ((rMax - rMin) * (sMax - sGlobalMedian)) / (area); // HL area
			short contributingEdges2 = (short) Math.floor(((short) a * HL_area));

			short deficit = 0;
			if ((contributingEdges1 + contributingEdges2) < a) {
				deficit = (short) (a - (contributingEdges1 + contributingEdges2));
				LOGGER.info("deficit node exists in LLHL quadrant config "+deficit);

				/** Randomly allot the remaining edges between both quadrants **/
				Random rand = new Random();
				int randIndex = 0;
				for (int i = 0; i < deficit; i++) {
					randIndex = rand.nextInt(2);

					if (randIndex == 0)
						contributingEdges1 = (short) (contributingEdges1 + 1);
					else
						contributingEdges2 = (short) (contributingEdges2 + 1);
				}
			}

			quadrantValueMap.put(StorageReliability.LL, new Short(contributingEdges1));
			quadrantValueMap.put(StorageReliability.HL, new Short(contributingEdges2));

			LL = LL + contributingEdges1;
			HL = HL + contributingEdges2;

		} else if (sMin >= sGlobalMedian && rMin < rGlobalMedian && rMax > rGlobalMedian) {

			LOGGER.info("sMin is " + sMin + " S2 is " + sGlobalMedian + " rMin is " + rMin + " " + " R2 is "
					+ rGlobalMedian + " rMax is " + rMax);
			double HH_area = ((rMax - rGlobalMedian) * (sMax - sMin)) / (area);// HH
			short contributingEdges1 = (short) ((short) a * HH_area);

			double HL_area = ((rGlobalMedian - rMin) * (sMax - sMin)) / (area);// HL
			short contributingEdges2 = (short) Math.floor(((short) a * HL_area));

			short deficit = 0;
			if ((contributingEdges1 + contributingEdges2) <= a) {
				deficit = (short) (a - (contributingEdges1 + contributingEdges2));
				LOGGER.info("deficit node exists in HH HL quadrant config "+deficit);
				
				/** Randomly allot the remaining edges between both quadrants **/
				Random rand = new Random();
				int randIndex = 0;
				for (int i = 0; i < deficit; i++) {
					randIndex = rand.nextInt(2);

					if (randIndex == 0)
						contributingEdges1 = (short) (contributingEdges1 + 1);
					else
						contributingEdges2 = (short) (contributingEdges2 + 1);
				}
			}

			quadrantValueMap.put(StorageReliability.HH, new Short(contributingEdges1));
			quadrantValueMap.put(StorageReliability.HL, new Short(contributingEdges2));

			HH = HH + contributingEdges1;
			HL = HL + contributingEdges2;

		} else if (rMin >= rGlobalMedian && sMin < sGlobalMedian && sMax > sGlobalMedian) {

			double LH_area = ((rMax - rMin) * (sGlobalMedian - sMin)) / (area);
			short contributingEdges1 = (short) ((short) a * LH_area);

			double HH_area = ((rMax - rMin) * (sMax - sGlobalMedian)) / (area);
			short contributingEdges2 = (short) Math.floor(((short) a * HH_area));

			short deficit = 0;
			if ((contributingEdges1 + contributingEdges2) < a) {
				deficit = (short) (a - (contributingEdges1 + contributingEdges2));
				
				LOGGER.info("deficit node exists in LH HH quadrant config "+deficit);

				/** Randomly allot the remaining edges between both quadrants **/
				Random rand = new Random();
				int randIndex = 0;

				for (int i = 0; i < deficit; i++) {
					randIndex = rand.nextInt(2);

					if (randIndex == 0)
						contributingEdges1 = (short) (contributingEdges1 + 1);
					else
						contributingEdges2 = (short) (contributingEdges2 + 1);
				}
			}

			quadrantValueMap.put(StorageReliability.LH, new Short(contributingEdges1));
			quadrantValueMap.put(StorageReliability.HH, new Short(contributingEdges2));

			LH = LH + contributingEdges1;
			HH = HH + contributingEdges2;

		} /** Single Square, Four Quadrant **/
		else {

			double LH_area = (rMax - rGlobalMedian) * (sGlobalMedian - sMin) / area; // LH
			short contributingEdges1 = (short) (a * LH_area);

			double LL_area = (rGlobalMedian - rMin) * (sGlobalMedian - sMin) / area; // LL
			short contributingEdges2 = (short) (a * LL_area);

			double HL_area = (sMax - sGlobalMedian) * (rGlobalMedian - rMin) / area; // HL area
			short contributingEdges3 = (short) (a * HL_area);

			double HH_area = (sMax - sGlobalMedian) * (rMax - rGlobalMedian) / area; // HH area
			short contributingEdges4 = (short) (a * HH_area);

			if ((contributingEdges1 + contributingEdges2 + contributingEdges3 + contributingEdges4) < a) {
				short deficit = (short) (a
						- (contributingEdges1 + contributingEdges2 + contributingEdges3 + contributingEdges4));
				LOGGER.info("The deficit (in 4th Quad) is " + deficit);
				/** Fair distribution of the Remaining edges **/
				Random rand = new Random();
				int randIndex = 0;
				for (int i = 0; i < deficit; i++) {

					randIndex = rand.nextInt(4);// generates number randomly with 0 inclusive 4 exclusive

					if (randIndex == 0)
						contributingEdges1 = (short) (contributingEdges1 + 1);
					else if (randIndex == 1)
						contributingEdges2 = (short) (contributingEdges2 + 1);
					else if (randIndex == 2)
						contributingEdges3 = (short) (contributingEdges3 + 1);
					else
						contributingEdges4 = (short) (contributingEdges4 + 1);

				}
			}

			quadrantValueMap.put(StorageReliability.LH, new Short(contributingEdges1));
			quadrantValueMap.put(StorageReliability.LL, new Short(contributingEdges2));
			quadrantValueMap.put(StorageReliability.HL, new Short(contributingEdges3));
			quadrantValueMap.put(StorageReliability.HH, new Short(contributingEdges4));
			LOGGER.info("sMin is " + sMin + " S2 is " + sGlobalMedian + " rMin is " + rMin + " " + " R2 is "
					+ rGlobalMedian + " rMax is " + rMax);

			LH = LH + contributingEdges1;
			LL = LL + contributingEdges2;
			HL = HL + contributingEdges3;
			HH = HH + contributingEdges4;

		}

		return quadrantValueMap;
	}

}
