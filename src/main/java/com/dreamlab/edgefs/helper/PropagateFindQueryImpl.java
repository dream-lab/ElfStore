package com.dreamlab.edgefs.helper;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamlab.edgefs.controlplane.Fog;
import com.dreamlab.edgefs.iface.PropagateFindQueryIFace;
import com.dreamlab.edgefs.misc.BloomFilter;
import com.dreamlab.edgefs.model.FogExchangeInfo;
import com.dreamlab.edgefs.model.FogInfo;
import com.dreamlab.edgefs.model.NeighborInfo;
import com.dreamlab.edgefs.thrift.FogService;
import com.dreamlab.edgefs.thrift.MatchPreference;

public class PropagateFindQueryImpl implements PropagateFindQueryIFace {

	private static final Logger LOGGER = LoggerFactory.getLogger(PropagateFindQueryImpl.class);

	@Override
	public Map<Long, String> getMbIdStreamIdMatchMapFromNeighbors(Map<String, String> metaKeyValueMap,
			Map<Long, String> currentMbIdStreamIdMap, MatchPreference matchpreference, Fog fog) {

		if (matchpreference != MatchPreference.AND && matchpreference != MatchPreference.OR)
			matchpreference = MatchPreference.AND;

		currentMbIdStreamIdMap = neighborLogic(metaKeyValueMap, currentMbIdStreamIdMap, matchpreference, fog);

		return currentMbIdStreamIdMap;
	}

	@Override
	public Map<Long, String> getMbIdStreamIdMatchMapFromBuddies(Map<String, String> metaKeyValueMap,
			Map<Long, String> currentMbIdStreamIdMap, MatchPreference matchpreference, Fog fog) {

		if (matchpreference != MatchPreference.AND && matchpreference != MatchPreference.OR)
			matchpreference = MatchPreference.AND;

		currentMbIdStreamIdMap = buddyLogic(metaKeyValueMap, currentMbIdStreamIdMap, matchpreference, fog);

		return currentMbIdStreamIdMap;
	}

	private Map<Long, String> neighborLogic(Map<String, String> metaKeyValueMap,
			Map<Long, String> currentMbIdStreamIdMap, MatchPreference matchpreference, Fog fog) {

		Map<Short, FogExchangeInfo> neighborExchangeInfo = fog.getNeighborExchangeInfo();

		if (MatchPreference.AND == matchpreference) {
			LOGGER.info("[neighbor logic] AND as the matchpreference => "+matchpreference);
			for (Entry<Short, FogExchangeInfo> entry : neighborExchangeInfo.entrySet()) {
				FogExchangeInfo nInfo = entry.getValue();
				if (nInfo != null) {
					byte[] bloomFilter = nInfo.getDynamicBloomFilter();

					// Iteration needed; based on number of properties passed for condition checking
					// in the map
					boolean allSatisfiedFlag = true;
					Iterator<Map.Entry<String, String>> itr = metaKeyValueMap.entrySet().iterator();
					while (itr.hasNext()) {
						Map.Entry<String, String> entryMeta = itr.next();

						if (BloomFilter.search(entryMeta.getKey(), entryMeta.getValue(), bloomFilter))
							continue;
						else {
							// i.e at least one of the property does not match in the blocks present under
							// this neighbor
							allSatisfiedFlag = false;
							break;
						}
					}
					if (allSatisfiedFlag) {
						// i.e all the metadata key value pairs are present
						NeighborInfo neighbor = fog.getNeighborsMap().get(entry.getKey());
						Map<Long, String> nMatchingMbIdStreamIdMap = fetchMbIdStreamIdMapFromOtherFog(
								neighbor.getNode().getNodeIP(), neighbor.getNode().getPort(), metaKeyValueMap, false,
								false, matchpreference);
						// Take a union of nMatchingMbIdStreamIdMap and currentMbIdStreamIdMap
						for (Long mbid : nMatchingMbIdStreamIdMap.keySet()) {
							if (!currentMbIdStreamIdMap.containsKey(mbid))
								currentMbIdStreamIdMap.put(mbid, nMatchingMbIdStreamIdMap.get(mbid));
						}
					}

				}
			}
		} else if (MatchPreference.OR == matchpreference) {
			LOGGER.info("[neighbor logic] OR as the matchpreference => "+matchpreference);
			for (Entry<Short, FogExchangeInfo> entry : neighborExchangeInfo.entrySet()) {
				FogExchangeInfo nInfo = entry.getValue();
				if (nInfo != null) {
					byte[] bloomFilter = nInfo.getDynamicBloomFilter();

					// Iteration needed; based on number of properties passed for condition checking
					// in the map
					boolean orSatisfiedFlag = false;
					Iterator<Map.Entry<String, String>> itr = metaKeyValueMap.entrySet().iterator();
					while (itr.hasNext()) {
						Map.Entry<String, String> entryMeta = itr.next();

						if (BloomFilter.search(entryMeta.getKey(), entryMeta.getValue(), bloomFilter)) {
							orSatisfiedFlag = true;
							break;
						}
					}
					if (orSatisfiedFlag) {
						// i.e all the metadata key value pairs are present
						NeighborInfo neighbor = fog.getNeighborsMap().get(entry.getKey());
						Map<Long, String> nMatchingMbIdStreamIdMap = fetchMbIdStreamIdMapFromOtherFog(
								neighbor.getNode().getNodeIP(), neighbor.getNode().getPort(), metaKeyValueMap, false,
								false, matchpreference);
						// Take a union of nMatchingMbIdStreamIdMap and currentMbIdStreamIdMap
						for (Long mbid : nMatchingMbIdStreamIdMap.keySet()) {
							if (!currentMbIdStreamIdMap.containsKey(mbid))
								currentMbIdStreamIdMap.put(mbid, nMatchingMbIdStreamIdMap.get(mbid));
						}
					}

				}
			}
		}

		return currentMbIdStreamIdMap;
	}

	private Map<Long, String> buddyLogic(Map<String, String> metaKeyValueMap, Map<Long, String> currentMbIdStreamIdMap,
			MatchPreference matchpreference, Fog fog) {

		Map<Short, FogExchangeInfo> buddyExchangeInfo = fog.getBuddyExchangeInfo();

		if (MatchPreference.AND == matchpreference) {
			LOGGER.info("[buddy logic] AND as the matchpreference => "+matchpreference);
			for (Entry<Short, FogExchangeInfo> entry : buddyExchangeInfo.entrySet()) {
				FogExchangeInfo buddyInfo = entry.getValue();
				if (buddyInfo != null) {
					byte[] bloomFilter = buddyInfo.getDynamicBloomFilter();
					// Iteration needed; based on number of properties passed for condition checking
					// in the map
					boolean allSatisfiedFlag = true;
					Iterator<Map.Entry<String, String>> itr = metaKeyValueMap.entrySet().iterator();
					while (itr.hasNext()) {
						Map.Entry<String, String> entryMeta = itr.next();
						if (BloomFilter.search(entryMeta.getKey(), entryMeta.getValue(), bloomFilter))
							continue;
						else {
							// i.e at least one of the perperty does not match in the blocks present under
							// this buddy
							allSatisfiedFlag = false;
							break;
						}
					}
					if (allSatisfiedFlag) {
						// i.e all the metadata key value pairs are present
						FogInfo buddy = fog.getBuddyMap().get(entry.getKey());
						Map<Long, String> bMatchingMbIdStreamIdMap = fetchMbIdStreamIdMapFromOtherFog(buddy.getNodeIP(),
								buddy.getPort(), metaKeyValueMap, true, false, matchpreference);
						// Take a union of nMatchingMbIdStreamIdMap and currentMbIdStreamIdMap
						for (Long mbid : bMatchingMbIdStreamIdMap.keySet()) {
							if (!currentMbIdStreamIdMap.containsKey(mbid))
								currentMbIdStreamIdMap.put(mbid, bMatchingMbIdStreamIdMap.get(mbid));
						}
					}

				}
			}
		} else if (MatchPreference.OR == matchpreference) {
			
			LOGGER.info("[buddy logic] OR as the matchpreference => "+matchpreference);
			for (Entry<Short, FogExchangeInfo> entry : buddyExchangeInfo.entrySet()) {
				FogExchangeInfo buddyInfo = entry.getValue();
				if (buddyInfo != null) {
					byte[] bloomFilter = buddyInfo.getDynamicBloomFilter();
					// Iteration needed; based on number of properties passed for condition checking
					// in the map
					boolean orSatisfiedFlag = false;
					Iterator<Map.Entry<String, String>> itr = metaKeyValueMap.entrySet().iterator();
					while (itr.hasNext()) {
						Map.Entry<String, String> entryMeta = itr.next();
						if (BloomFilter.search(entryMeta.getKey(), entryMeta.getValue(), bloomFilter)) {
							orSatisfiedFlag = true;
							break;
						}
						
					}
					if (orSatisfiedFlag) {
						// i.e all the metadata key value pairs are present
						FogInfo buddy = fog.getBuddyMap().get(entry.getKey());
						Map<Long, String> bMatchingMbIdStreamIdMap = fetchMbIdStreamIdMapFromOtherFog(buddy.getNodeIP(),
								buddy.getPort(), metaKeyValueMap, true, false, matchpreference);
						// Take a union of nMatchingMbIdStreamIdMap and currentMbIdStreamIdMap
						for (Long mbid : bMatchingMbIdStreamIdMap.keySet()) {
							if (!currentMbIdStreamIdMap.containsKey(mbid))
								currentMbIdStreamIdMap.put(mbid, bMatchingMbIdStreamIdMap.get(mbid));
						}
					}

				}
			}
		}

		return currentMbIdStreamIdMap;
	}

	private Map<Long, String> fetchMbIdStreamIdMapFromOtherFog(String nodeIP, int port,
			Map<String, String> metaKeyValueMap, boolean checkNeighbors, boolean checkBuddies,
			MatchPreference matchpreference) {

		Map<Long, String> otherMbIdStreamIdMap = new ConcurrentHashMap<>();
		TTransport transport = new TFramedTransport(new TSocket(nodeIP, port));
		try {
			transport.open();
		} catch (TTransportException e) {
			transport.close();
			LOGGER.error("Error while connecting to Fog ip : " + nodeIP, e);
			e.printStackTrace();
			return otherMbIdStreamIdMap;
		}
		TProtocol protocol = new TBinaryProtocol(transport);
		FogService.Client fogClient = new FogService.Client(protocol);
		try {
			otherMbIdStreamIdMap = fogClient.findBlockUsingQuery(metaKeyValueMap, checkNeighbors, checkBuddies,
					matchpreference);
		} catch (TException e) {
			LOGGER.error("Error while querying data from Fog ip : " + nodeIP, e);
			e.printStackTrace();
		} finally {
			transport.close();
		}
		return otherMbIdStreamIdMap;

	}

}
