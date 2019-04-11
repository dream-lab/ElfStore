package com.dreamlab.edgefs.controlplane;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamlab.edgefs.model.FogStats;
import com.dreamlab.edgefs.model.NodeInfo;
import com.dreamlab.edgefs.model.StorageReliability;

/**
 * 
 * @author swamiji
 *
 */

public class GlobalReplicaAllocation {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalReplicaAllocation.class);

	private double achievedReliability = 0;
	private List<String> choiceList = new ArrayList<String>();
	private List<NodeInfo> fogsChosen = new ArrayList<NodeInfo>();
	private List<Double> reliabilityContribution = new ArrayList<Double>();
	private List<Integer> buddyPoolId = new ArrayList<Integer>();
	private int replicaCount = 0;
	private Map<Short, NodeInfo> fogSet = new ConcurrentHashMap<Short, NodeInfo>();
	private Map<Short, List<String>> fogChoiceList = new ConcurrentHashMap<Short, List<String>>();

	/**
	 * We will return the above Map, it has FogId, list of choices for that fog ID
	 **/

	/**
	 * 
	 * @param globalStorageMap    : a,b,c,d for edges
	 * @param storageFogMap       : a,b,c, d for Fogs
	 * @param fogUpdateMap        : local stats for Fog level
	 * @param expectedReliability : the constraint to meet (hard constraint)
	 * @param minReplica          : The min number of replicas needed (hard
	 *                            constraint)
	 * @param maxReplica          : the number of replicas beyond which no more
	 *                            allocation takes place
	 * @return The set of Fog Ids
	 */
	public List<NodeInfo> identifyReplicas(Map<StorageReliability, Short> globalStorageMap,
			Map<StorageReliability, List<Short>> storageFogMap, Map<Short, FogStats> fogUpdateMap,
			double expectedReliability, int minReplica, int maxReplica) {

		LOGGER.info("Min replica is " + minReplica + " max replica is" + maxReplica+ " the expected reliability " + expectedReliability);

		LOGGER.info("Identify Replicas Function ");
		LOGGER.info("elfstore the maps are globalStorageMap " + globalStorageMap.toString() + " storage fog map"
				+ storageFogMap.toString() + " fogupdate map " + fogUpdateMap.toString());

		/** sub choice 0 sent **/
		List<NodeInfo> fogInfo = identifyReplicasWithChoice(globalStorageMap, storageFogMap, fogUpdateMap,
				expectedReliability, 0, minReplica, maxReplica);

		LOGGER.info("After sub choice 0 " + achievedReliability + " : " + expectedReliability + " the replica count is "
				+ replicaCount);

		if (achievedReliability >= expectedReliability && replicaCount >= minReplica) {
			
			LOGGER.info("SUCCESS CASE 1st case achievedReliability" + achievedReliability + " : " + expectedReliability + " the replica count is "
					+ replicaCount);
			return fogInfo; /** SUCCESS CASE **/
		} else {

			/** Allocate only HH and LL from now on, which is subchoice 1 **/
			fogInfo.addAll(identifyReplicasWithChoice(globalStorageMap, storageFogMap, fogUpdateMap,
					expectedReliability, 1, minReplica, maxReplica));

		}
		LOGGER.info("After sub choice 1 " + achievedReliability + " : " + expectedReliability + " the replica count is "
				+ replicaCount);

		/** still reliability not achieved **/
		if (achievedReliability >= expectedReliability && replicaCount >= minReplica) {
			LOGGER.info("SUCCESS CASE 2nd case achievedReliability" + achievedReliability + " : " + expectedReliability + " the replica count is "
					+ replicaCount);
			return fogInfo;/** SUCCESS **/
		}
		else {
			fogInfo.addAll(allocateFromLHandLLReplicas(globalStorageMap, storageFogMap, fogUpdateMap,
					expectedReliability, 0, minReplica, maxReplica));
			
			LOGGER.info("(LH and LL) After sub choice 1 " + achievedReliability + " : " + expectedReliability + " the replica count is "
					+ replicaCount);

			if (achievedReliability >= expectedReliability && replicaCount >= minReplica) {
				LOGGER.info("SUCCESS CASE 3rd case achievedReliability" + achievedReliability + " : " + expectedReliability + " the replica count is "
						+ replicaCount);
				return fogInfo;
			}

			fogInfo.addAll(allocateFromLHandLLReplicas(globalStorageMap, storageFogMap, fogUpdateMap,
					expectedReliability, 1, minReplica, maxReplica));
			
			LOGGER.info("(LH and LL) After sub choice 1 " + achievedReliability + " : " + expectedReliability + " the replica count is "
					+ replicaCount);
		}
		LOGGER.info("After LHH ,LLH, LHL, LLL " + achievedReliability + " : " + expectedReliability
				+ " the replica count is " + replicaCount);

		if (achievedReliability >= expectedReliability && replicaCount >= minReplica) {
			
			LOGGER.info("SUCCESS CASE 4th case (LH and LL) After sub choice 0 & 1 " + achievedReliability + " : " + expectedReliability + " the replica count is "
					+ replicaCount);
			
			return fogInfo;/** SUCCESS **/
		} else {
			/**
			 * This case will help us allocate multiple edges from the same fog
			 */
			LOGGER.info("Finally came here to choose remaining replicas");
			chooseRemainingEdges(expectedReliability, fogUpdateMap, minReplica, maxReplica);
		}

		LOGGER.info("After exhausting all options reliability is " + achievedReliability + " Expected reliability is "
				+ expectedReliability);

		return fogInfo;

	}

	/**
	 * return the chosen reliability
	 */
	public List<Double> getReliabilityContribution() {
		return reliabilityContribution;
	}

	/**
	 * set the achieved reliability
	 */
	public void setReliability(double argAchieved) {
		achievedReliability = argAchieved;
	}

	public double getReliability() {
		return achievedReliability;
	}

	/**
	 * 
	 * @return The Map which contains fog and the count and their preferences
	 */
	public Map<Short, List<String>> getFogChoiceList() {
		return fogChoiceList;
	}

	/**
	 * 
	 * @return Chosen Fog
	 */
	public List<NodeInfo> getFogsChosen() {
		return fogsChosen;
	}

	/**
	 * 
	 * @return the value of strings like LLH, HHL etc
	 */
	public List<String> getChoiceList() {
		return choiceList;
	}

	/**
	 * This structure will be accessed outside
	 */
	public void prepareChoiceList() {

		if (fogChoiceList.size() == fogSet.size()) {
			LOGGER.info("Values already computed ");
			return;
		}

		int i = 0;
		/** else prepare the list **/
		for (NodeInfo fogNode : fogsChosen) {

			List<String> myChoiceList = new ArrayList<String>();

			if (i < choiceList.size()) {
				myChoiceList.add(choiceList.get(i));
				fogChoiceList.put(fogNode.getNodeID(), myChoiceList);
			}

			i++; // **IMP **/
		}

	}

	/**
	 * Returns an extra edge from the existing Fog devices, useful when not enough
	 * fogs are there
	 * 
	 * @param expectedReliability : the reliability demanded by the stream
	 * @param fogUpdateMap        : the local stats of the Fog
	 */
	public void chooseRemainingEdges(double expectedReliability, Map<Short, FogStats> fogUpdateMap, int minReplica,
			int maxReplica) {

		LOGGER.info("We came to the remaining edges place ");
		LOGGER.info("The fogs chosen are " + fogsChosen.toString());
		LOGGER.info("The string choices are " + choiceList.toString());
		int i = 0;
		int remainingOptions = 0;
		for (NodeInfo fogNode : fogsChosen) {

			if (fogUpdateMap.containsKey(fogNode.getNodeID()) == false)
				continue;

			LOGGER.info("fogNode is" + fogNode.toString());
			List<String> myChoiceList = new ArrayList<String>();
			myChoiceList.add(choiceList.get(i));
			fogChoiceList.put(fogNode.getNodeID(), myChoiceList);

			if (choiceList.get(i).equals("HHL")) {
				/** -1 because we already have an allocation in this **/
				remainingOptions += fogUpdateMap.get(fogNode.getNodeID()).getA()
						+ fogUpdateMap.get(fogNode.getNodeID()).getB() - 1;

			} else if (choiceList.get(i).equals("HLH")) {
				/** -1 because we already have an allocation in this **/
				remainingOptions += fogUpdateMap.get(fogNode.getNodeID()).getC()
						+ fogUpdateMap.get(fogNode.getNodeID()).getD() - 1;

			} else if (choiceList.get(i).equals("HHH")) {
				/** -1 because we already have an allocation in this **/
				remainingOptions += fogUpdateMap.get(fogNode.getNodeID()).getC()
						+ fogUpdateMap.get(fogNode.getNodeID()).getD() - 1;

			} else if (choiceList.get(i).equals("HLL")) {
				/** -1 because we already have an allocation in this **/
				remainingOptions += fogUpdateMap.get(fogNode.getNodeID()).getA()
						+ fogUpdateMap.get(fogNode.getNodeID()).getB() - 1;

			} else if (choiceList.get(i).equals("LHL")) {
				remainingOptions += fogUpdateMap.get(fogNode.getNodeID()).getA()
						+ fogUpdateMap.get(fogNode.getNodeID()).getB() - 1;

			} else if (choiceList.get(i).equals("LHH")) {
				remainingOptions += fogUpdateMap.get(fogNode.getNodeID()).getC()
						+ fogUpdateMap.get(fogNode.getNodeID()).getD() - 1;

			} else if (choiceList.get(i).equals("LLH")) {
				remainingOptions += fogUpdateMap.get(fogNode.getNodeID()).getC()
						+ fogUpdateMap.get(fogNode.getNodeID()).getD() - 1;

			} else {
				remainingOptions += fogUpdateMap.get(fogNode.getNodeID()).getA()
						+ fogUpdateMap.get(fogNode.getNodeID()).getB() - 1;

			}

			i++; // **IMP **/
		}

		LOGGER.info("The remaining options are " + remainingOptions);
		LOGGER.info("the choice size list " + choiceList.size());

		int totalChoices = choiceList.size();
		i = 0; // reset i, the and contition is to break the loop
		while (i < totalChoices) {

			if (replicaCount >= (maxReplica))
				break;

			NodeInfo fogNode = fogsChosen.get(i);

			if (fogUpdateMap.containsKey(fogNode.getNodeID()) == false)
				continue;

			LOGGER.info("fog node is " + fogNode);
			int alreadyAlloted = fogChoiceList.get(fogNode.getNodeID()).size();

			if (choiceList.get(i).equals("HHL")) {

				int possible = fogUpdateMap.get(fogNode.getNodeID()).getA()
						+ fogUpdateMap.get(fogNode.getNodeID()).getB() - alreadyAlloted;

				while (possible > 0 && (achievedReliability < expectedReliability) || (replicaCount < minReplica)) {
					fogChoiceList.get(fogNode.getNodeID()).add("HHL");

					double edgeReliability = fogUpdateMap.get(fogNode.getNodeID()).getMinReliability() / 100.0;
					achievedReliability = achievedReliability == 0 ? edgeReliability
							: ((1 - (1 - achievedReliability) * (1 - edgeReliability)));
					reliabilityContribution.add(edgeReliability);
					possible--;
					replicaCount++;
				}

			} else if (choiceList.get(i).equals("HLH")) {

				int possible = fogUpdateMap.get(fogNode.getNodeID()).getC()
						+ fogUpdateMap.get(fogNode.getNodeID()).getD() - alreadyAlloted;
				while (possible > 0 && (achievedReliability < expectedReliability) || (replicaCount < minReplica)) {
					fogChoiceList.get(fogNode.getNodeID()).add("HLH");

					double edgeReliability = fogUpdateMap.get(fogNode.getNodeID()).getMedianReliability() / 100.0;
					achievedReliability = achievedReliability == 0 ? edgeReliability
							: ((1 - (1 - achievedReliability) * (1 - edgeReliability)));
					reliabilityContribution.add(edgeReliability);
					possible--;
					replicaCount++;
				}

			} else if (choiceList.get(i).equals("HHH")) {

				int possible = fogUpdateMap.get(fogNode.getNodeID()).getC()
						+ fogUpdateMap.get(fogNode.getNodeID()).getD() - alreadyAlloted;

				while (possible > 0 && (achievedReliability < expectedReliability) || (replicaCount < minReplica)) {
					fogChoiceList.get(fogNode.getNodeID()).add("HHH");

					double edgeReliability = fogUpdateMap.get(fogNode.getNodeID()).getMedianReliability() / 100.0;
					achievedReliability = achievedReliability == 0 ? edgeReliability
							: ((1 - (1 - achievedReliability) * (1 - edgeReliability)));
					reliabilityContribution.add(edgeReliability);
					possible--;
					replicaCount++;
				}

			} else if (choiceList.get(i).equals("HLL")) {

				int possible = fogUpdateMap.get(fogNode.getNodeID()).getA()
						+ fogUpdateMap.get(fogNode.getNodeID()).getB() - alreadyAlloted;

				while (possible > 0 && (achievedReliability < expectedReliability) || (replicaCount < minReplica)) {
					fogChoiceList.get(fogNode.getNodeID()).add("HLL");

					double edgeReliability = fogUpdateMap.get(fogNode.getNodeID()).getMinReliability() / 100.0;
					achievedReliability = achievedReliability == 0 ? edgeReliability
							: ((1 - (1 - achievedReliability) * (1 - edgeReliability)));
					reliabilityContribution.add(edgeReliability);
					possible--;
					replicaCount++;
				}
			} else if (choiceList.get(i).equals("LHH")) {
				int possible = fogUpdateMap.get(fogNode.getNodeID()).getA()
						+ fogUpdateMap.get(fogNode.getNodeID()).getB() - alreadyAlloted;

				while (possible > 0 && (achievedReliability < expectedReliability) || (replicaCount < minReplica)) {
					fogChoiceList.get(fogNode.getNodeID()).add("LHH");

					double edgeReliability = fogUpdateMap.get(fogNode.getNodeID()).getMinReliability() / 100.0;
					achievedReliability = achievedReliability == 0 ? edgeReliability
							: ((1 - (1 - achievedReliability) * (1 - edgeReliability)));
					reliabilityContribution.add(edgeReliability);
					possible--;
					replicaCount++;
				}

			} else if (choiceList.get(i).equals("LHL")) {
				int possible = fogUpdateMap.get(fogNode.getNodeID()).getC()
						+ fogUpdateMap.get(fogNode.getNodeID()).getD() - alreadyAlloted;

				while (possible > 0 && (achievedReliability < expectedReliability) || (replicaCount < minReplica)) {
					fogChoiceList.get(fogNode.getNodeID()).add("LHL");

					double edgeReliability = fogUpdateMap.get(fogNode.getNodeID()).getMedianReliability() / 100.0;
					achievedReliability = achievedReliability == 0 ? edgeReliability
							: ((1 - (1 - achievedReliability) * (1 - edgeReliability)));
					reliabilityContribution.add(edgeReliability);
					possible--;
					replicaCount++;
				}

			} else if (choiceList.get(i).equals("LLH")) {
				int possible = fogUpdateMap.get(fogNode.getNodeID()).getC()
						+ fogUpdateMap.get(fogNode.getNodeID()).getD() - alreadyAlloted;

				while (possible > 0 && (achievedReliability < expectedReliability) || (replicaCount < minReplica)) {
					fogChoiceList.get(fogNode.getNodeID()).add("LLH");

					double edgeReliability = fogUpdateMap.get(fogNode.getNodeID()).getMedianReliability() / 100.0;
					achievedReliability = achievedReliability == 0 ? edgeReliability
							: ((1 - (1 - achievedReliability) * (1 - edgeReliability)));
					reliabilityContribution.add(edgeReliability);
					possible--;
					replicaCount++;
				}
			} else {
				int possible = fogUpdateMap.get(fogNode.getNodeID()).getA()
						+ fogUpdateMap.get(fogNode.getNodeID()).getB() - alreadyAlloted;

				while (possible > 0 && (achievedReliability < expectedReliability) || (replicaCount < minReplica)) {
					fogChoiceList.get(fogNode.getNodeID()).add("LLL");

					double edgeReliability = fogUpdateMap.get(fogNode.getNodeID()).getMinReliability() / 100.0;
					achievedReliability = achievedReliability == 0 ? edgeReliability
							: ((1 - (1 - achievedReliability) * (1 - edgeReliability)));
					reliabilityContribution.add(edgeReliability);
					possible--;
					replicaCount++;
				}
			}

			i++; // IMP
		}
	}

	/**
	 * 
	 * @return returns the choice made during picking of edge
	 */
	public List<String> choiceList() {
		return choiceList;
	}

	/**
	 * 
	 * @param globalStorageMap    : a,b,c,d for edges
	 * @param storageFogMap       : a,b,c, d for Fogs
	 * @param fogUpdateMap        : local stats for Fog level
	 * @param expectedReliability : the constraint to meet
	 * @param subChoice           : this is to for options like HH and LL if the HL
	 *                            and LH fail to allocate
	 * @param replicaConstraints  : the number of replicas beyond which no more
	 *                            allocation takes place
	 * @return The set of Fog Ids
	 */
	private List<NodeInfo> identifyReplicasWithChoice(Map<StorageReliability, Short> globalStorageMap,
			Map<StorageReliability, List<Short>> storageFogMap, Map<Short, FogStats> fogUpdateMap,
			double expectedReliability, int subchoice, int minReplica, int maxReplica) {

		LOGGER.info("The subchoice made is " + subchoice);
		int choice = 0;
		boolean allocationMade = false;
		List<NodeInfo> fogList = new ArrayList<NodeInfo>();
		int quadrantMiss = 0; /** To ensure safe exit when no nodes are present **/

		while ((achievedReliability < expectedReliability) || ((replicaCount < minReplica))) {

			if (replicaCount >= (maxReplica)) /** This is an important decision **/
				break;

			LOGGER.info("in the tank");

			allocationMade = false;
			quadrantMiss = 0; /** This is important **/
			/************************************************************
			 * HH *
			 ************************************************************/
			/** Attempt to make allocation in HHL * */
			if (choice == 0 && allocationMade == false) { // HH High Storage High Reliability Fog

				if (storageFogMap.containsKey(StorageReliability.HH)
						&& globalStorageMap.containsKey(StorageReliability.HH)
						&& globalStorageMap.get(StorageReliability.HH) > 0) {

					int randomFogRange = storageFogMap.get(StorageReliability.HH)
							.size(); /** Set of Fogs Ids in the HH region **/
					allocationMade = false; /** Reset the allocation **/

					/** This is to see that random numbers are not repeated **/
					Set<Short> fogIdRandomIndex = new LinkedHashSet<Short>();

					/**
					 * First while loop is to check for he high storage range for all Fogs, the 'b'
					 * part of the square c d a 'b'
					 * 
					 **/
					int breakLoop = 0;
					while (allocationMade == false && breakLoop < randomFogRange) {

						LOGGER.info("HHL..1st");

						Short fogId = storageFogMap.get(StorageReliability.HH)
								.get(new Random().nextInt(randomFogRange));

						if (fogIdRandomIndex.contains(fogId) == false && fogSet
								.containsKey(fogId) == false) { /** So that we don't choose a fog already chosen **/
							
							LOGGER.info("choice test Fog is " + fogId);
							breakLoop++;
							fogIdRandomIndex.add(fogId); /** To make sure this value is not used again **/

							if (fogUpdateMap.containsKey(fogId) == false || (buddyPoolId.contains((int) fogSet.get(fogId).getBuddyPoolId()))) /** This is important **/
								continue;

							FogStats fStat = fogUpdateMap.get(fogId);
							LOGGER.info("This executes if fogId is present " + fogId);

							switch (subchoice) {
							case 0:
								if (fStat.getB() > 0) { /** This is the HL allocation in the local Fog **/

									allocationMade = true;
									choice = 1;
									replicaCount = replicaCount + 1;
									double reliability = fStat.getMinReliability() / 100.0;
									achievedReliability = (achievedReliability == 0) ? (reliability)
											: (1 - ((1 - achievedReliability)
													* (1 - reliability))); /** Multiply With min reliability **/
									fogList.add(fStat.getNodeInfo());
									fogSet.put(fogId, fStat.getNodeInfo());
									choiceList.add("HHL");
									fogsChosen.add(fStat.getNodeInfo());
									buddyPoolId.add((int) fogSet.get(fogId).getBuddyPoolId());
									reliabilityContribution.add(reliability);
									LOGGER.info("Choice made HHL" + fStat.toString()+ " from buddy pool "+fogSet.get(fogId).getBuddyPoolId());
									LOGGER.info("Achieved reliability is " + achievedReliability);
								}
								break;

							case 1:
								if (fStat.getD() > 0) { /** This is the HH allocation in the local Fog **/

									allocationMade = true;
									choice = 1;
									replicaCount = replicaCount + 1;
									double reliability = fStat.getMedianReliability() / 100.0;
									achievedReliability = (achievedReliability == 0) ? (reliability)
											: (1 - ((1 - achievedReliability)
													* (1 - reliability))); /** Multiply With median reliability **/
									fogList.add(fStat.getNodeInfo());
									fogSet.put(fogId, fStat.getNodeInfo());
									choiceList.add("HHH");
									fogsChosen.add(fStat.getNodeInfo());
									buddyPoolId.add((int) fogSet.get(fogId).getBuddyPoolId());
									reliabilityContribution.add(reliability);
									LOGGER.info("Choice made HHH" + fStat.toString()+ "from buddy pool "+fogSet.get(fogId).getBuddyPoolId());
									LOGGER.info("Achieved reliability is " + achievedReliability);
								}
								break;

							default:
								LOGGER.info("wrong Choice sent ");
								break;

							}

						} /** End of the choice while loop **/
						/**
						 * Logic to break the loop
						 */
						if (fogIdRandomIndex.contains(fogId) == false) {
							breakLoop++;
							fogIdRandomIndex.add(fogId);
							LOGGER.info("Added fogid to fogset to break the loop in LH");
						}
					}

					fogIdRandomIndex.clear();
					/**
					 * Second while loop is to check for the low storage range for all Fogs, the 'a'
					 * part of the square c d 'a' b
					 * 
					 **/
					LOGGER.info("Random range is " + randomFogRange);
					breakLoop = 0;
					while (allocationMade == false && breakLoop < randomFogRange) {

						LOGGER.info("HHL..2nd");

						Short fogId = storageFogMap.get(StorageReliability.HH)
								.get(new Random().nextInt(randomFogRange));

						if (fogIdRandomIndex.contains(fogId) == false && fogSet.containsKey(
								fogId) == false) { /** So that we don't choose a fog already chosen before **/

							breakLoop++;
							fogIdRandomIndex.add(fogId);
							
							if (fogUpdateMap.containsKey(fogId) == false || (buddyPoolId.contains((int) fogSet.get(fogId).getBuddyPoolId()))) /** This is important **/
								continue;

							FogStats fStat = fogUpdateMap.get(fogId);

							switch (subchoice) {
							case 0:
								if (fStat.getA() > 0) { /** This is the HL allocation in the local Fog **/

									allocationMade = true;
									choice = 1;
									replicaCount = replicaCount + 1;
									double reliability = fStat.getMinReliability() / 100.0;
									achievedReliability = (achievedReliability == 0) ? (reliability)
											: (1 - ((1 - achievedReliability)
													* (1 - reliability))); /** Multiply With min reliability **/
									fogList.add(fStat.getNodeInfo());
									fogSet.put(fogId, fStat.getNodeInfo()); /**/
									choiceList.add("HHL");
									fogsChosen.add(fStat.getNodeInfo());
									buddyPoolId.add((int) fogSet.get(fogId).getBuddyPoolId());
									reliabilityContribution.add(reliability);
									LOGGER.info("Choice made HHL" + fStat.toString()+ " from buddy pool "+fogSet.get(fogId).getBuddyPoolId());
									LOGGER.info("Achieved reliability is " + achievedReliability);
								}
								break;

							case 1:
								if (fStat.getC() > 0) { /** This is the HH allocation in the local Fog **/

									allocationMade = true;
									choice = 1;
									replicaCount = replicaCount + 1;
									double reliability = fStat.getMedianReliability() / 100.0;
									achievedReliability = (achievedReliability == 0) ? (reliability)
											: (1 - ((1 - achievedReliability)
													* (1 - reliability))); /** Multiply With median reliability **/
									fogList.add(fStat.getNodeInfo());
									fogSet.put(fogId, fStat.getNodeInfo());/**/
									choiceList.add("HHH");
									fogsChosen.add(fStat.getNodeInfo());
									buddyPoolId.add((int) fogSet.get(fogId).getBuddyPoolId());
									reliabilityContribution.add(reliability);
									LOGGER.info("Choice made HHH" + fStat.toString()+ " from buddy pool "+fogSet.get(fogId).getBuddyPoolId());
									LOGGER.info("Achieved reliability is " + achievedReliability);
								}
								break;

							default:
								LOGGER.info("Wrong subchoice sent");
							}

						}

						/**
						 * Logic to break the loop
						 */
						if (fogIdRandomIndex.contains(fogId) == false) {
							breakLoop++;
							fogIdRandomIndex.add(fogId);
							LOGGER.info("Added fogid to fogset to break the loop in LH");
						}
					}

				} else {/** size() >0 check end **/
					quadrantMiss = 1;
					choice = 1;
					LOGGER.info("No nodes to allocate in HH");
				}

				if (allocationMade == false) {
					quadrantMiss = 1;
					choice = 1; /** This is incremented in case the if condition fails **/
					LOGGER.info("No allocations were made in HH");
				}

				LOGGER.info("Choice is " + choice);

			} /** End if for HH **/

			/************************************************************
			 * HL *
			 ************************************************************/
			/** Attempt to make allocation in HL* */
			if (choice == 1 && allocationMade == false) {// HL High Storage, Low Reliability. Fog
				LOGGER.info("Entering HL");

				if (storageFogMap.containsKey(StorageReliability.HL)
						&& globalStorageMap.containsKey(StorageReliability.HL)
						&& globalStorageMap.get(StorageReliability.HL) > 0) {

					int randomFogRange = storageFogMap.get(StorageReliability.HL).size();
					allocationMade = false;

					/** This is to see that random numbers are not repeated **/
					Set<Short> fogIdRandomIndex = new LinkedHashSet<Short>();
					LOGGER.info("HL size is " + globalStorageMap.get(StorageReliability.HL));
					LOGGER.info("Random range in HL is " + randomFogRange);

					/**
					 * First while loop is to check for he high storage range for all Fogs, the 'd'
					 * part of the square c 'd' a b
					 * 
					 **/
					int breakLoop = 0;
					while (allocationMade == false && breakLoop < randomFogRange) {

						LOGGER.info("HLH... 1st");

						Short fogId = storageFogMap.get(StorageReliability.HL)
								.get(new Random().nextInt(randomFogRange));
						LOGGER.info("Fog id is " + fogId);

						if (fogIdRandomIndex.contains(fogId) == false && fogSet.containsKey(
								fogId) == false) { /** So that we don't choose a Fog already chosen before **/

							fogIdRandomIndex.add(fogId);
							breakLoop++;
							
							if (fogUpdateMap.containsKey(fogId) == false || (buddyPoolId.contains((int) fogSet.get(fogId).getBuddyPoolId()))) /** This is important **/
								continue;
							FogStats fStat = fogUpdateMap.get(fogId);

							LOGGER.info("Fog id is " + fogId + " fogStats is " + fStat.toString());
							LOGGER.info("Sub choice is " + subchoice);
							LOGGER.info("D is " + fStat.getD());
							LOGGER.info("B is " + fStat.getB());

							switch (subchoice) {
							case 0:
								if (fStat.getD() > 0) { /** This is the LH in the Fog **/

									allocationMade = true;
									choice = 0;
									replicaCount = replicaCount + 1;
									double reliability = fStat.getMedianReliability() / 100.0;
									achievedReliability = (achievedReliability == 0) ? (reliability)
											: (1 - ((1 - achievedReliability)
													* (1 - reliability)));/** Multiply With median reliability **/
									fogList.add(fStat.getNodeInfo());
									fogSet.put(fogId, fStat.getNodeInfo());
									choiceList.add("HLH");
									fogsChosen.add(fStat.getNodeInfo());
									buddyPoolId.add((int) fogSet.get(fogId).getBuddyPoolId());
									reliabilityContribution.add(reliability);
									LOGGER.info("Choice made HLH" + fStat.toString()+ " from buddy pool "+fogSet.get(fogId).getBuddyPoolId());
									LOGGER.info("Achieved reliability is " + achievedReliability);
								}
								break;

							case 1:
								if (fStat.getB() > 0) { /** This is the LL in the Fog **/

									allocationMade = true;
									choice = 0;
									replicaCount = replicaCount + 1;
									double reliability = fStat.getMinReliability() / 100.0;
									achievedReliability = (achievedReliability == 0) ? (reliability)
											: (1 - ((1 - achievedReliability)
													* (1 - reliability)));/** Multiply With min reliability **/
									fogList.add(fStat.getNodeInfo());
									fogSet.put(fogId, fStat.getNodeInfo());
									choiceList.add("HLL");
									fogsChosen.add(fStat.getNodeInfo());
									buddyPoolId.add((int) fogSet.get(fogId).getBuddyPoolId());
									reliabilityContribution.add(reliability);
									LOGGER.info("Choice made HLL" + fStat.toString()+ " from buddy pool "+fogSet.get(fogId).getBuddyPoolId());
									LOGGER.info("Achieved reliability is " + achievedReliability);
								}
								break;

							default:

								break;
							}

						}

						/**
						 * Logic to break the loop
						 */
						if (fogIdRandomIndex.contains(fogId) == false) {
							breakLoop++;
							fogIdRandomIndex.add(fogId);
							LOGGER.info("Added fogid to fogset to break the loop in LH");
						}
					}

					fogIdRandomIndex.clear();
					/**
					 * Second while loop is to check for the low storage range for all Fogs, the 'c'
					 * part of the square 'c' d a b
					 * 
					 **/
					breakLoop = 0;
					LOGGER.info("Random Fog Range " + randomFogRange);
					while (allocationMade == false && breakLoop < randomFogRange) { /**
																					 * the second condition is to check
																					 * that we've covered all options
																					 **/

						LOGGER.info("HLH...2nd");

						Short fogId = storageFogMap.get(StorageReliability.HL)
								.get(new Random().nextInt(randomFogRange));

						if (fogIdRandomIndex.contains(fogId) == false && fogSet.containsKey(
								fogId) == false) {/** So that we don't choose a Fog already chosen before **/

							breakLoop++;
							if (fogUpdateMap.containsKey(fogId) == false || (buddyPoolId.contains((int) fogSet.get(fogId).getBuddyPoolId()))) /** This is important **/
								continue;

							FogStats fStat = fogUpdateMap.get(fogId);

							LOGGER.info("Fog id is " + fogId + " fogStats is " + fStat.toString());
							LOGGER.info("Sub choice is " + subchoice);
							LOGGER.info("C is " + fStat.getC());
							LOGGER.info("A is " + fStat.getA());

							switch (subchoice) {

							case 0:
								if (fStat.getC() > 0) { /** This is the LH in the Fog **/

									fogIdRandomIndex.add(fogId);
									allocationMade = true;
									choice = 0;
									replicaCount = replicaCount + 1;
									double reliability = fStat.getMedianReliability() / 100.0;
									achievedReliability = (achievedReliability == 0) ? (reliability)
											: (1 - ((1 - achievedReliability)
													* (1 - reliability)));/** Multiply With median reliability **/
									fogList.add(fStat.getNodeInfo());
									fogSet.put(fogId, fStat.getNodeInfo());
									choiceList.add("HLH");
									fogsChosen.add(fStat.getNodeInfo());
									buddyPoolId.add((int) fogSet.get(fogId).getBuddyPoolId());
									reliabilityContribution.add(reliability);
									LOGGER.info("Choice made HLH" + fStat.toString()+ " from buddy pool "+fogSet.get(fogId).getBuddyPoolId());
									LOGGER.info("Achieved reliability is " + achievedReliability);
								}
								break;

							case 1:
								if (fStat.getA() > 0) { /** This is the LL in the Fog **/

									fogIdRandomIndex.add(fogId);
									allocationMade = true;
									choice = 0;
									replicaCount = replicaCount + 1;
									double reliability = fStat.getMinReliability() / 100.0;
									achievedReliability = (achievedReliability == 0) ? (reliability)
											: (1 - ((1 - achievedReliability)
													* (1 - reliability)));/** Multiply With min reliability **/
									fogList.add(fStat.getNodeInfo());
									fogSet.put(fogId, fStat.getNodeInfo());
									choiceList.add("HLL");
									fogsChosen.add(fStat.getNodeInfo());
									buddyPoolId.add((int) fogSet.get(fogId).getBuddyPoolId());
									reliabilityContribution.add(reliability);
									LOGGER.info("Choice made HLL" + fStat.toString()+ " from buddy pool "+fogSet.get(fogId).getBuddyPoolId());
									LOGGER.info("Achieved reliability is " + achievedReliability);
								}
								break;

							default:
								LOGGER.info("wrong choice sent ");

								break;
							}

						}

						/**
						 * Logic to break the loop
						 */
						if (fogIdRandomIndex.contains(fogId) == false) {
							breakLoop++;
							fogIdRandomIndex.add(fogId);
							LOGGER.info("Added fogid to fogset to break the loop in LH");
						}
					}

				} /** size>0 check if end **/
				else {
					choice = 0;
					quadrantMiss = quadrantMiss + 1;
					LOGGER.info("No edges to allocate in HL");
				}

				/** If No allocation was made then move to the next quadrant **/
				if (allocationMade == false) {
					choice = 0;
					quadrantMiss = quadrantMiss + 1;
					LOGGER.info("Allocation was not made in HL");
				}

			} /** End If for HL **/

			if (quadrantMiss >= 2) {
				LOGGER.info("Can't allocate here");
				break;
			}

		} /** While Achieved reliability is not met **/

		return fogList;
	}

	/**
	 * Returns a list of replicas from LH and LL based on the same logic for HH and
	 * HL regions. (hi Fog, hi edge)
	 * 
	 * @param achievedReliability : reliability achieved so far
	 * @return
	 */
	public List<NodeInfo> allocateFromLHandLLReplicas(Map<StorageReliability, Short> globalStorageMap,
			Map<StorageReliability, List<Short>> storageFogMap, Map<Short, FogStats> fogUpdateMap,
			double expectedReliability, int subchoice, int minReplica, int maxReplica) {

		LOGGER.info("In the same logic but, the other half The subchoice made is " + subchoice);
		int choice = 0;
		boolean allocationMade = false;
		List<NodeInfo> fogList = new ArrayList<NodeInfo>();
		int quadrantMiss = 0; /** To ensure safe exit when no nodes are present **/

		while ((achievedReliability < expectedReliability) || ((replicaCount < minReplica))) {

			if (replicaCount >= (maxReplica))
				break;

			LOGGER.info("in the tank second time");

			allocationMade = false;
			quadrantMiss = 0; /** This is important **/
			/************************************************************
			 * LH *
			 ************************************************************/
			/** Attempt to make allocation in LHL * */
			if (choice == 0 && allocationMade == false) { // LH Low Storage High Reliability Fog

				if (storageFogMap.containsKey(StorageReliability.LH)
						&& globalStorageMap.containsKey(StorageReliability.LH)
						&& globalStorageMap.get(StorageReliability.LH) > 0) {

					int randomFogRange = storageFogMap.get(StorageReliability.LH)
							.size(); /** Set of Fogs Ids in the HH region **/
					allocationMade = false; /** Reset the allocation **/

					/** This is to see that random numbers are not repeated **/
					Set<Short> fogIdRandomIndex = new LinkedHashSet<Short>();

					/**
					 * First while loop is to check for he high storage range for all Fogs, the 'b'
					 * part of the square c d a 'b'
					 * 
					 **/
					int breakLoop = 0;
					while (allocationMade == false && breakLoop < randomFogRange) {

						LOGGER.info("LHL..1st");

						Short fogId = storageFogMap.get(StorageReliability.LH)
								.get(new Random().nextInt(randomFogRange));

						if (fogIdRandomIndex.contains(fogId) == false && fogSet
								.containsKey(fogId) == false) { /** So that we don't choose a fog already chosen **/

							LOGGER.info("2nd half choice test Fog is " + fogId);
							breakLoop++;
							fogIdRandomIndex.add(fogId); /** To make sure this value is not used again **/

							if (fogUpdateMap.containsKey(fogId) == false || (buddyPoolId.contains((int) fogSet.get(fogId).getBuddyPoolId()))) /** This is important **/
								continue;

							FogStats fStat = fogUpdateMap.get(fogId);
							LOGGER.info("This executes if fogId is present " + fogId);

							switch (subchoice) {
							case 0:
								if (fStat.getB() > 0) { /** This is the HL allocation in the local Fog **/

									allocationMade = true;
									choice = 1;
									replicaCount = replicaCount + 1;
									double reliability = fStat.getMinReliability() / 100.0;
									achievedReliability = (achievedReliability == 0) ? (reliability)
											: (1 - ((1 - achievedReliability)
													* (1 - reliability))); /** Multiply With min reliability **/
									fogList.add(fStat.getNodeInfo());
									fogSet.put(fogId, fStat.getNodeInfo());
									choiceList.add("LHL");
									fogsChosen.add(fStat.getNodeInfo());
									buddyPoolId.add((int) fogSet.get(fogId).getBuddyPoolId());
									reliabilityContribution.add(reliability);
									LOGGER.info("Choice made LHL" + fStat.toString()+ " from buddy pool "+fogSet.get(fogId).getBuddyPoolId());
									LOGGER.info("Achieved reliability is " + achievedReliability);
								}
								break;

							case 1:
								if (fStat.getD() > 0) { /** This is the HH allocation in the local Fog **/

									allocationMade = true;
									choice = 1;
									replicaCount = replicaCount + 1;
									double reliability = fStat.getMedianReliability() / 100.0;
									achievedReliability = (achievedReliability == 0) ? (reliability)
											: (1 - ((1 - achievedReliability)
													* (1 - reliability))); /** Multiply With median reliability **/
									fogList.add(fStat.getNodeInfo());
									fogSet.put(fogId, fStat.getNodeInfo());
									choiceList.add("LHH");
									fogsChosen.add(fStat.getNodeInfo());
									buddyPoolId.add((int) fogSet.get(fogId).getBuddyPoolId());
									reliabilityContribution.add(reliability);
									LOGGER.info("Choice made LHH" + fStat.toString()+ " from buddy pool "+fogSet.get(fogId).getBuddyPoolId());
									LOGGER.info("Achieved reliability is " + achievedReliability);
								}
								break;

							default:
								LOGGER.info("wrong Choice sent ");
								break;

							}

						} /** End of the choice while loop **/
						/**
						 * Logic to break the loop
						 */
						if (fogIdRandomIndex.contains(fogId) == false) {
							breakLoop++;
							fogIdRandomIndex.add(fogId);
							LOGGER.info("Added fogid to fogset to break the loop in LH");
						}
					}

					fogIdRandomIndex.clear();
					/**
					 * Second while loop is to check for the low storage range for all Fogs, the 'a'
					 * part of the square c d 'a' b
					 * 
					 **/
					LOGGER.info("Random range is " + randomFogRange);
					breakLoop = 0;
					while (allocationMade == false && breakLoop < randomFogRange) {

						LOGGER.info("LHL..2nd");

						Short fogId = storageFogMap.get(StorageReliability.LH)
								.get(new Random().nextInt(randomFogRange));

						if (fogIdRandomIndex.contains(fogId) == false && fogSet.containsKey(
								fogId) == false) { /** So that we don't choose a fog already chosen before **/

							breakLoop++;
							fogIdRandomIndex.add(fogId);
							if (fogUpdateMap.containsKey(fogId) == false || (buddyPoolId.contains((int) fogSet.get(fogId).getBuddyPoolId()))) /** This is important **/
								continue;

							FogStats fStat = fogUpdateMap.get(fogId);

							switch (subchoice) {
							case 0:
								if (fStat.getA() > 0) { /** This is the HL allocation in the local Fog **/

									allocationMade = true;
									choice = 1;
									replicaCount = replicaCount + 1;
									double reliability = fStat.getMinReliability() / 100.0;
									achievedReliability = (achievedReliability == 0) ? (reliability)
											: (1 - ((1 - achievedReliability)
													* (1 - reliability))); /** Multiply With min reliability **/
									fogList.add(fStat.getNodeInfo());
									fogSet.put(fogId, fStat.getNodeInfo()); /**/
									choiceList.add("LHL");
									fogsChosen.add(fStat.getNodeInfo());
									buddyPoolId.add((int) fogSet.get(fogId).getBuddyPoolId());
									reliabilityContribution.add(reliability);
									LOGGER.info("Choice made HHL" + fStat.toString()+ " from buddy pool "+fogSet.get(fogId).getBuddyPoolId());
									LOGGER.info("Achieved reliability is " + achievedReliability);
								}
								break;

							case 1:
								if (fStat.getC() > 0) { /** This is the HH allocation in the local Fog **/

									allocationMade = true;
									choice = 1;
									replicaCount = replicaCount + 1;
									double reliability = fStat.getMedianReliability() / 100.0;
									achievedReliability = (achievedReliability == 0) ? (reliability)
											: (1 - ((1 - achievedReliability)
													* (1 - reliability))); /** Multiply With median reliability **/
									fogList.add(fStat.getNodeInfo());
									fogSet.put(fogId, fStat.getNodeInfo());/**/
									choiceList.add("LHH");
									fogsChosen.add(fStat.getNodeInfo());
									buddyPoolId.add((int) fogSet.get(fogId).getBuddyPoolId());
									reliabilityContribution.add(reliability);
									LOGGER.info("Choice made LHH" + fStat.toString()+ " from buddy pool "+fogSet.get(fogId).getBuddyPoolId());
									LOGGER.info("Achieved reliability is " + achievedReliability);
								}
								break;

							default:
								LOGGER.info("Wrong subchoice sent");
							}

						}

						/**
						 * Logic to break the loop
						 */
						if (fogIdRandomIndex.contains(fogId) == false) {
							breakLoop++;
							fogIdRandomIndex.add(fogId);
							LOGGER.info("Added fogid to fogset to break the loop in LH");
						}
					}

				} else {/** size() >0 check end **/
					quadrantMiss = 1;
					choice = 1;
					LOGGER.info("No nodes to allocate in LHH");
				}

				if (allocationMade == false) {
					quadrantMiss = 1;
					choice = 1; /** This is incremented in case the if condition fails **/
					LOGGER.info("No allocations were made in LHH");
				}

				LOGGER.info("Choice is " + choice);

			} /** End if for HH **/

			/************************************************************
			 * LL *
			 ************************************************************/
			/** Attempt to make allocation in LL* */
			if (choice == 1 && allocationMade == false) {// HL High Storage, Low Reliability. Fog
				LOGGER.info("Entering LHL");

				if (storageFogMap.containsKey(StorageReliability.LL)
						&& globalStorageMap.containsKey(StorageReliability.LL)
						&& globalStorageMap.get(StorageReliability.LL) > 0) {

					int randomFogRange = storageFogMap.get(StorageReliability.LL).size();
					allocationMade = false;

					/** This is to see that random numbers are not repeated **/
					Set<Short> fogIdRandomIndex = new LinkedHashSet<Short>();
					LOGGER.info("LL size is " + globalStorageMap.get(StorageReliability.LL));
					LOGGER.info("Random range in LL is " + randomFogRange);

					/**
					 * First while loop is to check for he high storage range for all Fogs, the 'd'
					 * part of the square c 'd' a b
					 * 
					 **/
					int breakLoop = 0;
					while (allocationMade == false && breakLoop < randomFogRange) {

						LOGGER.info("LLH... 1st");

						Short fogId = storageFogMap.get(StorageReliability.LL)
								.get(new Random().nextInt(randomFogRange));
						LOGGER.info("Fog id is " + fogId);

						if (fogIdRandomIndex.contains(fogId) == false && fogSet.containsKey(
								fogId) == false) { /** So that we don't choose a Fog already chosen before **/

							fogIdRandomIndex.add(fogId);
							breakLoop++;
							if (fogUpdateMap.containsKey(fogId) == false || (buddyPoolId.contains((int) fogSet.get(fogId).getBuddyPoolId()))) /** This is important **/
								continue;
							FogStats fStat = fogUpdateMap.get(fogId);

							LOGGER.info("Fog id is " + fogId + " fogStats is " + fStat.toString());
							LOGGER.info("Sub choice is " + subchoice);
							LOGGER.info("D is " + fStat.getD());
							LOGGER.info("B is " + fStat.getB());

							switch (subchoice) {
							case 0:
								if (fStat.getD() > 0) { /** This is the LH in the Fog **/

									allocationMade = true;
									choice = 0;
									replicaCount = replicaCount + 1;
									double reliability = fStat.getMedianReliability() / 100.0;
									achievedReliability = (achievedReliability == 0) ? (reliability)
											: (1 - ((1 - achievedReliability)
													* (1 - reliability)));/** Multiply With median reliability **/
									fogList.add(fStat.getNodeInfo());
									fogSet.put(fogId, fStat.getNodeInfo());
									choiceList.add("LLH");
									fogsChosen.add(fStat.getNodeInfo());
									buddyPoolId.add((int) fogSet.get(fogId).getBuddyPoolId());
									reliabilityContribution.add(reliability);
									LOGGER.info("Choice made LLH" + fStat.toString()+ " from buddy pool "+fogSet.get(fogId).getBuddyPoolId());									
									LOGGER.info("Achieved reliability is " + achievedReliability);
								}
								break;

							case 1:
								if (fStat.getB() > 0) { /** This is the LL in the Fog **/

									allocationMade = true;
									choice = 0;
									replicaCount = replicaCount + 1;
									double reliability = fStat.getMinReliability() / 100.0;
									achievedReliability = (achievedReliability == 0) ? (reliability)
											: (1 - ((1 - achievedReliability)
													* (1 - reliability)));/** Multiply With min reliability **/
									fogList.add(fStat.getNodeInfo());
									fogSet.put(fogId, fStat.getNodeInfo());
									choiceList.add("LLL");
									fogsChosen.add(fStat.getNodeInfo());
									buddyPoolId.add((int) fogSet.get(fogId).getBuddyPoolId());
									reliabilityContribution.add(reliability);
									LOGGER.info("Choice made LLL" + fStat.toString()+ " from buddy pool "+fogSet.get(fogId).getBuddyPoolId());
									LOGGER.info("Achieved reliability is " + achievedReliability);									
								}
								break;

							default:

								break;
							}

						}

						/**
						 * Logic to break the loop
						 */
						if (fogIdRandomIndex.contains(fogId) == false) {
							breakLoop++;
							fogIdRandomIndex.add(fogId);
							LOGGER.info("Added fogid to fogset to break the loop in LH");
						}
					}

					fogIdRandomIndex.clear();
					/**
					 * Second while loop is to check for the low storage range for all Fogs, the 'c'
					 * part of the square 'c' d a b
					 * 
					 **/
					breakLoop = 0;
					LOGGER.info("Random Fog Range " + randomFogRange);
					while (allocationMade == false && breakLoop < randomFogRange) { /**
																					 * the second condition is to check
																					 * that we've covered all options
																					 **/

						LOGGER.info("LLH...2nd");

						Short fogId = storageFogMap.get(StorageReliability.LL)
								.get(new Random().nextInt(randomFogRange));

						if (fogIdRandomIndex.contains(fogId) == false && fogSet.containsKey(
								fogId) == false) {/** So that we don't choose a Fog already chosen before **/

							breakLoop++;
							if (fogUpdateMap.containsKey(fogId) == false || (buddyPoolId.contains((int) fogSet.get(fogId).getBuddyPoolId()))) /** This is important **/
								continue;

							FogStats fStat = fogUpdateMap.get(fogId);

							LOGGER.info("Fog id is " + fogId + " fogStats is " + fStat.toString());
							LOGGER.info("Sub choice is " + subchoice);
							LOGGER.info("C is " + fStat.getC());
							LOGGER.info("A is " + fStat.getA());

							switch (subchoice) {

							case 0:
								if (fStat.getC() > 0) { /** This is the LH in the Fog **/

									fogIdRandomIndex.add(fogId);
									allocationMade = true;
									choice = 0;
									replicaCount = replicaCount + 1;
									double reliability = fStat.getMedianReliability() / 100.0;
									achievedReliability = (achievedReliability == 0) ? (reliability)
											: (1 - ((1 - achievedReliability)
													* (1 - reliability)));/** Multiply With median reliability **/
									fogList.add(fStat.getNodeInfo());
									fogSet.put(fogId, fStat.getNodeInfo());
									choiceList.add("LLH");
									fogsChosen.add(fStat.getNodeInfo());
									buddyPoolId.add((int) fogSet.get(fogId).getBuddyPoolId());
									reliabilityContribution.add(reliability);
									LOGGER.info("Choice made LLH" + fStat.toString()+ " from buddy pool "+fogSet.get(fogId).getBuddyPoolId());
									LOGGER.info("Achieved reliability is " + achievedReliability);									
								}
								break;

							case 1:
								if (fStat.getA() > 0) { /** This is the LL in the Fog **/

									fogIdRandomIndex.add(fogId);
									allocationMade = true;
									choice = 0;
									replicaCount = replicaCount + 1;
									double reliability = fStat.getMinReliability() / 100.0;
									achievedReliability = (achievedReliability == 0) ? (reliability)
											: (1 - ((1 - achievedReliability)
													* (1 - reliability)));/** Multiply With min reliability **/
									fogList.add(fStat.getNodeInfo());
									fogSet.put(fogId, fStat.getNodeInfo());
									choiceList.add("LLL");
									fogsChosen.add(fStat.getNodeInfo());
									buddyPoolId.add((int) fogSet.get(fogId).getBuddyPoolId());
									reliabilityContribution.add(reliability);
									LOGGER.info("Choice made LLL" + fStat.toString()+ " from buddy pool "+fogSet.get(fogId).getBuddyPoolId());
									LOGGER.info("Achieved reliability is " + achievedReliability);									
								}
								break;

							default:
								LOGGER.info("wrong choice sent ");

								break;
							}

						}

						/**
						 * Logic to break the loop
						 */
						if (fogIdRandomIndex.contains(fogId) == false) {
							breakLoop++;
							fogIdRandomIndex.add(fogId);
							LOGGER.info("Added fogid to fogset to break the loop in LH");
						}
					}

				} /** size>0 check if end **/
				else {
					choice = 0;
					quadrantMiss = quadrantMiss + 1;
					LOGGER.info("No edges to allocate in HL");
				}

				/** If No allocation was made then move to the next quadrant **/
				if (allocationMade == false) {
					choice = 0;
					quadrantMiss = quadrantMiss + 1;
					LOGGER.info("Allocation was not made in HL");
				}

			} /** End If for HL **/

			if (quadrantMiss >= 2) {
				LOGGER.info("Can't allocate here");
				break;
			}

		} /** While Achieved reliability is not met **/

		return fogList;

	}

}
