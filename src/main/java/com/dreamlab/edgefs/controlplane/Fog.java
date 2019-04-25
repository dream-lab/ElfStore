package com.dreamlab.edgefs.controlplane;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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

import com.dreamlab.edgefs.misc.BuddyDataExchangeFormat;
import com.dreamlab.edgefs.misc.Constants;
import com.dreamlab.edgefs.misc.GlobalStatsHandler;
import com.dreamlab.edgefs.misc.LocalStatsHandler;
import com.dreamlab.edgefs.misc.NeighborDataExchangeFormat;
import com.dreamlab.edgefs.model.EdgeInfo;
import com.dreamlab.edgefs.model.FogExchangeInfo;
import com.dreamlab.edgefs.model.FogInfo;
import com.dreamlab.edgefs.model.FogStats;
import com.dreamlab.edgefs.model.LocalEdgeStats;
import com.dreamlab.edgefs.model.NeighborInfo;
import com.dreamlab.edgefs.model.NodeInfo;
import com.dreamlab.edgefs.model.StorageReliability;
import com.dreamlab.edgefs.thrift.BuddyPayload;
import com.dreamlab.edgefs.thrift.FogService;
import com.dreamlab.edgefs.thrift.Metadata;
import com.dreamlab.edgefs.thrift.NeighborPayload;
import com.dreamlab.edgefs.thrift.StreamMetadataInfo;

public class Fog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5150041291949158338L;

	/**
	 * This and the other classes participating will be implementing the Serializable
	 * interface, three types of fields are marked transient. First are those which can
	 * be reconstructed when the FogServer is restarted. Second are those
	 * which should be calculated from the beginning of the start of the FogServer such as
	 * the various fields maintaing the last time an update was sent. Third are those which
	 * are not currently used such as sessionClientMap
	 */
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FogServer.class);
	
	/*************************** Fog Class members *****************************/
	private FogInfo myFogInfo;
	//private float poolReliability;
	private transient int kMin,kMax;
	private transient int k;
	
	private int edgeDiskWatermark;
	
	//this is for the microbatch search
	private byte[] personalBloomFilter = new byte[Constants.BLOOM_FILTER_BYTES];
	
	//An attempt at fixing issue #25
	private Map<Short, byte[]> edgeBloomFilters = new HashMap<>();
	
	//this is for the stream search
	private byte[] personalStreamBFilter = new byte[Constants.BLOOM_FILTER_BYTES];
	
	// For now, I am keeping a single variable to check if bloomfilters are to be
	// sent or not. There is no stream level or microbatch level most recent update
	// demarcation here. A change in any will trigger a send of both
	// time of most recent update to my personal bloomfilter
	// update this whenever a local write happens, also update
	// mostRecentNeighborBFUpdate as well
	private transient long mostRecentSelfBFUpdate = Long.MIN_VALUE;
	
	// this is the last time I sent an update to my subscribers
	// compare this with my most recent update to personal bloomfilter
	// and send if needed
	private transient long lastpersonalBFSent = Long.MIN_VALUE;
	
	
	//self entry can be placed in this map, this doesn't contain stats
	private Map<Short, FogExchangeInfo> neighborExchangeInfo = new ConcurrentHashMap<>();
	
	//buddylevel bloomfilters contain the consolidated bloomfilter
	//of the buddy and all its neighbors and the rest values are for
	//the buddy such as lastheartbeat and etc
	private Map<Short, FogExchangeInfo> buddyExchangeInfo = new ConcurrentHashMap<>();
	
	
	//update this when a neighbor gives updated information about bloomfilters
	//include self if there is a change in local bloomfilter as well
	//(useful for Bbloom)
	private transient long mostRecentNeighborBFUpdate = Long.MIN_VALUE;
	
	//time when last (self + neighbor) bloomfilter was sent
	//(useful for Bbloom)
	private transient long lastNeighborBFSent = Long.MIN_VALUE;
	
	
	/*
	 * TODO::Add indexing logic here for metadata based search
	 */
	
	
	private Map<Short, EdgeInfo> localEdgesMap = new ConcurrentHashMap<>();
	//for every Edge managed by a Fog, maintain a,b,c,d values
	//where a is the edgecount for LL, b for HL, c for LH, d for HH
	//where first is storage and second is reliability parameter
	
	
	private CoarseGrainedStats coarseGrainedStats = new CoarseGrainedStats();
	
	//time of most recent local stats calculation
	private transient long lastLocalUpdatedTime = Long.MIN_VALUE;
	
	//local stats are sent only to subscribers
	private transient long lastLocalStatsSent = Long.MIN_VALUE;
	
	//update this when an Edge gives updated disk utilization
	private transient long mostRecentEdgeUpdate = Long.MIN_VALUE;
	
	
	//information at the level of each Fog
	//value is the set of ids of the edge this Fog controls falling
	//under a particular category namely HL,LH, etc.
	//read and write can be concurrent as client requests to get a local edge
	//and local stats computation is also in progress
	private Map<StorageReliability, List<Short>> localEdgeMapping = new ConcurrentHashMap<>();
	
	//a set can also be maintained here for Edges who have crossed
	//their WATERMARK REQUIREMENT
	//We might be putting, getting or removing from it, make sure 
	//the iterator is not used as it is fail-fast with ConcurrentModificationException
	private Set<Short> noStorageEdges = new HashSet<>();
	
	
	//Optimization : When sending stats info to your buddies, only send
	//those that have changed. Before sending, check the neighborExchangeInfo
	//to see if the neighbor's including self lastUpdatedStatsTime is more 
	//recent than the lastNeighborStatsSent and only send the entries which
	//satisfy the constraint
	
	/*****************************UPDATE VALUE TIME INTERVALS**********************************/
	//update this when a neighbor gives updated information about stats
	//include self if there is a change in local stats as well
	//For self, need to keep the more recent of the neighbor stats and
	//the lastLocalUpdatedTime (useful for Bstats)
	private transient long mostRecentNeighborStatsUpdate = Long.MIN_VALUE;

	//time when last (self + neighbor) stats was sent
	//(useful for Bstats)
	private transient long lastNeighborStatsSent = Long.MIN_VALUE;
	
	
	//this is our global stats
	//this will contain self + neighbors entry as well as 
	//for every buddy, the buddy and its neighbors stats
	private Map<Short, FogStats> fogUpdateMap = new ConcurrentHashMap<>();
	
	//update this when a Fog sends updated stats, this can be a neighbor
	//or a buddy. This is to check whether to compute global stats or not
	//If this time is more recent than the lastGlobalStatsUpdatedTime, we
	//need to recompute the globalStats
	private transient long mostRecentFogStatsUpdate = Long.MIN_VALUE;
	
	// this is set when we last computed the global stats
	private transient long lastGlobalStatsUpdatedTime = Long.MIN_VALUE;
	/****************************************************************************/
	
	
	//maintain global information in terms of storage and reliability
	//for all Fogs
	//key is essentially the quadrant and value map contains fogId as the 
	//key and the Fog's contribution towards the quadrant as the value
	//private Map<StorageReliability, Map<Short, Short>> globalStats = new HashMap<>();
	
	
	/************************ Maps related to buddies and neighbors ***************************/
	//this Fog will be sending its consolidated (local + neighbor)
	//bloomfilters to its buddies and personal to its neighbors
	//commented the set part to go with the map so that an id based
	//lookup can be supported on most of the entities, similar thing
	//done with neighbors and buddies
	private Map<Short, NodeInfo> subscribedMap = new ConcurrentHashMap<>();
	
	private Map<Short, NeighborInfo> neighborsMap = new ConcurrentHashMap<>();
	
	private Map<Short, FogInfo> buddyMap = new ConcurrentHashMap<>();
	
	
	//is this needed, this is present as part of myFogInfo
	private short buddyPoolId;
	//need to maintain the poolSize so that when this Fog is sending
	//heartbeats to its neighbors, size of the whole system at any time
	//can be calculated using similar information from its buddies
	private transient short poolSize;
	//an approximate poolSize can be calculated based on heartbeats from
	//neighbors and buddies. From neighbors, get their poolsize but all 
	//pools might not be covered with local neighbors. From the buddies,
	//get their poolSizeMap to get total picture.
	private transient Map<Short, Short> poolSizeMap = new HashMap<>();
	
	/********************************************************************************************/
	
	//SWAMIJI to address:please use interface on the declaration side
	// May not be useful right now
	private Map<Short,HashMap<StorageReliability,Short>> globalAllocationMap = new ConcurrentHashMap<Short, HashMap<StorageReliability,Short>>();
	
	//SWAMIJI to address:please use interface on the declaration side
	//map to make final allocations 
	//A Map of Storage Reliability to Fog Devices
	private Map<StorageReliability, List<Short>> storageFogMap = new ConcurrentHashMap<StorageReliability, List<Short>>();
	
	private Map<StorageReliability, Short> edgeDistributionMap = new ConcurrentHashMap<StorageReliability, Short>();

	

	//the locks are not used currently, so commenting for now
/*
	//this to update most recent Edge update
	//used to properly update mostRecentEdgeUpdate
	private final Lock edgeLock = new ReentrantLock();
	
	//used to properly update mostRecentNeighborStatsUpdate
	private final Lock neighborStatsLock = new ReentrantLock();
	
	//used to properly update mostRecentNeighborBFUpdate
	private final Lock neighborBloomLock = new ReentrantLock();
	
	//if any Fog updates their stats, global stats will be recalculated
	//in the next window
	//used to properly update mostRecentFogStatsUpdate
	private final Lock globalStatsLock = new ReentrantLock();
	*/
	
	/************************Metadata querying********************************************/
	/**
	 * For all these maps, check if any map is iterated while other threads can do a put or
	 * remove operation over the map. If yes, replace with the ConcurrentHashMap.
	 */
	//This is to maintain a session between Client and a transaction
	private transient Map<Short,String> sessionClientMap = new HashMap<Short, String>(); //needs to be cleared out on every timeout
	
	//This is to maintian a previous allocation that I made for a particular write for a session
	private transient Map<String, List<NodeInfo>> sessionLocations = new HashMap<String, List<NodeInfo>>();
	
	//This is used to have a mapping between micro-batchId to the EdgeID
	//private Map<String,Short> mbIDLocationMap = new ConcurrentHashMap<>();
	
	//IMPORTANT::it might happen that same microbatchId maybe present on multiple edges
	//of a single Fog, then it will create a lot of trouble since we need to
	//make sure that we are not writing the same microbatch to the same edge
	//as writes are concurrent and also while recovery, we should not write 
	//the microbatch to an edge already having a replica of it
	
	//IMPL_NOTE::the issue with keeping a set of shorts is it can throw ConcurrentModificationException
	//as we are updating as well as iterating at the same time. However updates are done
	//at data writes and iterations during reads and these two are separate in time but 
	//still for keeping the separation clean, resorting to a map with implementation
	//a ConcurrentHashMap. Java doesn't have a ConcurrentHashSet and such a feature in
	//use is anyways backed by a ConcurrentHashMap
	private Map<String,Map<Short,Byte>> mbIDLocationMap = new ConcurrentHashMap<>();
	
	//the stream level metadata is stored but not used for searching. This is stored
	//when the stream is first registered
	private Map<String, StreamMetadataInfo> streamMetadata = new ConcurrentHashMap<>();
	
	//This is used to have a mapping between StreamID and a set of Microbatches
	private Map<String, Set<String>> streamMbIdMap =  new ConcurrentHashMap<>();
	
	//This is used to have a mapping between metadata values and micro batches
	//Sumit:The microbatch metadata containing key value pairs
	//is stored in this map using key:value as the map key. The value is a list of microbatches
	//which are managed by this Fog. Multiple metadata key value pairs are placed as different
	//keys in this map and each has a different list instantiated for it, reason being there can
	//be microbatches which have a common set of key value pairs but not the overall metadata
	//which requires us to store a different list for each metadata value
	private Map<String, List<String>> metaToMBIdListMap = new ConcurrentHashMap<>(); 
	
	//this map stores for every edge the list of microbatchIds it stores. This is useful
	//for recovery when an edge device dies and the Fog on learning this needs to start
	//the recovery thereby becoming a client in the process to get the lost data from 
	//other replicas
	private Map<Short, Set<String>> edgeMicrobatchMap = new ConcurrentHashMap<>();
	
	private Map<String, String> microBatchToStream = new ConcurrentHashMap<>();
	
	private Map<String, Metadata> blockMetadata = new ConcurrentHashMap<>();
	
	//CONCURRENT WRITES::during replica identification phase, we first fetch a local edge
	//to the contacted Fog and then move to our algorithm similar to a finite state machine
	//to pick next devices (Fog) to write to. During FSM phase, we pick a Fog only once but 
	//there is a possibility of getting a local edge and then the same Fog in the FSM phase
	//so we need to make sure that during a remote write case, a different edge is picked.
	//During recovery, this case is less likely to happen since we can then use mbIDLocationMap
	//to not pick duplicate edges but during writing this can happen as client is making
	//parallel writes and we need to make sure that a microbatch is strictly replicated on
	//different edges. So during replica identification, for the local edge case, we insert
	//the microbatchId in the set and check it during the remote write to make sure the replica
	//identified by Fog for writing is different from the one picked earlier. Also when the
	//insertMetadata request comes for the local edge write, we remove this microbatchId from
	//the set since mbIDLocationMap can do the job now in case of recovery
	private transient Map<String, Short> localEdgeWritesInProgress = new ConcurrentHashMap<>();
	
	//this is the system-wide cache invalidation time for cached stream metadata
	//transient as it can read from the system.properties during startup
	private transient int streamMetaCacheInvalidation;
	
	//stream soft and hard lease times
	private transient int streamSoftLease;
	
	private transient int streamHardLease;
	
	private transient boolean isReplicaCachingEnabled;
	
	private transient int replicaCachingTime;
	
	/****************************************************************************/
	
	public Fog() {
		
	}
	
	/**
	 * Constructor
	 * @param IP
	 * @param ID
	 * @param reliability
	 */
	public Fog(String IP, short ID, int port, short poolId, float reliability) {
		this.myFogInfo = new FogInfo(IP, ID, port, poolId, reliability);
		this.buddyPoolId = poolId;
	}
	
	/*****************************UTILITIES******************************************/
	@Override
	public String toString() {
		return "Fog [myFogInfo=" + myFogInfo.toString() +" Reliability is " + myFogInfo
				+ ", neighbors=" + neighborsMap + "]";
	}
	/*********************************************************************************/
	
	public Map<String, String> getMicroBatchToStream() {
		return microBatchToStream;
	}

	public void setMicroBatchToStream(Map<String, String> microBatchToStream) {
		this.microBatchToStream = microBatchToStream;
	}

	public Map<StorageReliability, Short> getEdgeDistributionMap() {
		return edgeDistributionMap;
	}
	
	public Map<StorageReliability, List<Short>> getStorageFogMap() {
		return storageFogMap;
	}

	public void setStorageFogMap(HashMap<StorageReliability, List<Short>> storageFogMap) {
		this.storageFogMap = storageFogMap;
	}

	/*public Map<String, Short> getMbIDLocationMap() {
		return mbIDLocationMap;
	}

	public void setMbIDLocationMap(Map<String, Short> mbIDLocationMap) {
		this.mbIDLocationMap = mbIDLocationMap;
	}*/
	
	public Map<String, Map<Short, Byte>> getMbIDLocationMap() {
		return mbIDLocationMap;
	}

	public void setMbIDLocationMap(Map<String, Map<Short, Byte>> mbIDLocationMap) {
		this.mbIDLocationMap = mbIDLocationMap;
	}

	public Map<String, Set<String>> getStreamMbIdMap() {
		return streamMbIdMap;
	}

	public void setStreamMbIdMap(Map<String, Set<String>> streamMbIdMap) {
		this.streamMbIdMap = streamMbIdMap;
	}

	/**
	 * 
	 * @return a map with metadata being the key and List of micro-batch ids being the values
	 */
	public Map<String, List<String>> getMetaMbIdMap() {
		return metaToMBIdListMap;
	}

	public void setMetaMbIdMap(HashMap<String, List<String>> metaMbIdMap) {
		this.metaToMBIdListMap = metaMbIdMap;
	}
	
	public FogInfo getMyFogInfo() {
		return myFogInfo;
	}

	public void setMyFogInfo(FogInfo myFogInfo) {
		this.myFogInfo = myFogInfo;
	}
	
	public int getkMin() {
		return kMin;
	}

	public void setkMin(int kMin) {
		this.kMin = kMin;
	}
	
	public int getkMax() {
		return kMax;
	}

	public void setkMax(int kMax) {
		this.kMax = kMax;
	}
	
	public int getK() {
		return k;
	}

	public void setK(int size) {
		this.k = size;
	}

	public CoarseGrainedStats getCoarseGrainedStats() {
		return coarseGrainedStats;
	}

	public void setCoarseGrainedStats(CoarseGrainedStats coarseGrainedStats) {
		this.coarseGrainedStats = coarseGrainedStats;
	}

	public void setBuddyPoolId(short argbuddyPoolID) {
		buddyPoolId = argbuddyPoolID;
	}
	
	public Map<Short, Short> getPoolSizeMap() {
		return poolSizeMap;
	}

	public void setPoolSizeMap(Map<Short, Short> poolSizeMap) {
		this.poolSizeMap = poolSizeMap;
	}

	public short getBuddyPoolId() {
		return buddyPoolId;
	}

	public short getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(short poolSize) {
		this.poolSize = poolSize;
	}
	
	public Map<Short, EdgeInfo> getLocalEdgesMap() {
		return localEdgesMap;
	}

	public void setLocalEdgesMap(Map<Short, EdgeInfo> localEdgesMap) {
		this.localEdgesMap = localEdgesMap;
	}
	
	public long getLastLocalUpdatedTime() {
		return lastLocalUpdatedTime;
	}

	public void setLastLocalUpdatedTime(long lastLocalUpdatedTime) {
		this.lastLocalUpdatedTime = lastLocalUpdatedTime;
	}

	public long getLastLocalStatsSent() {
		return lastLocalStatsSent;
	}

	public void setLastLocalStatsSent(long lastLocalStatsSent) {
		this.lastLocalStatsSent = lastLocalStatsSent;
	}

	public long getLastGlobalStatsUpdatedTime() {
		return lastGlobalStatsUpdatedTime;
	}

	public void setLastGlobalStatsUpdatedTime(long lastGlobalUpdatedTime) {
		this.lastGlobalStatsUpdatedTime = lastGlobalUpdatedTime;
	}

	public byte[] getPersonalBloomFilter() {
		return personalBloomFilter;
	}

	public void setPersonalBloomFilter(byte[] personalBloomFilter) {
		this.personalBloomFilter = personalBloomFilter;
	}


	public Map<Short, byte[]> getEdgeBloomFilters() {
		return edgeBloomFilters;
	}

	public void setEdgeBloomFilters(Map<Short, byte[]> edgeBloomFilters) {
		this.edgeBloomFilters = edgeBloomFilters;
	}

	public byte[] getPersonalStreamBFilter() {
		return personalStreamBFilter;
	}

	public void setPersonalStreamBFilter(byte[] personalStreamBFilter) {
		this.personalStreamBFilter = personalStreamBFilter;
	}

	public int getEdgeDiskWatermark() {
		return edgeDiskWatermark;
	}

	public void setEdgeDiskWatermark(int edgeDiskWatermark) {
		this.edgeDiskWatermark = edgeDiskWatermark;
	}

	public Map<Short, FogStats> getFogUpdateMap() {
		return fogUpdateMap;
	}

	public void setFogUpdateMap(Map<Short, FogStats> fogUpdateMap) {
		this.fogUpdateMap = fogUpdateMap;
	}

	public Set<Short> getNoStorageEdges() {
		return noStorageEdges;
	}

	public void setNoStorageEdges(Set<Short> noStorageEdges) {
		this.noStorageEdges = noStorageEdges;
	}

	public Map<Short, NodeInfo> getSubscribedMap() {
		return subscribedMap;
	}

	public void setSubscribedMap(Map<Short, NodeInfo> subscribedMap) {
		this.subscribedMap = subscribedMap;
	}

	public Map<Short, NeighborInfo> getNeighborsMap() {
		return neighborsMap;
	}

	public void setNeighborsMap(Map<Short, NeighborInfo> neighborsMap) {
		this.neighborsMap = neighborsMap;
	}

	public Map<Short, FogInfo> getBuddyMap() {
		return buddyMap;
	}

	public void setBuddyMap(Map<Short, FogInfo> buddyMap) {
		this.buddyMap = buddyMap;
	}

	public Map<StorageReliability, List<Short>> getLocalEdgeMapping() {
		return localEdgeMapping;
	}

	public void setLocalEdgeMapping(Map<StorageReliability, List<Short>> localEdgeMapping) {
		this.localEdgeMapping = localEdgeMapping;
	}
	
	public long getMostRecentEdgeUpdate() {
		return mostRecentEdgeUpdate;
	}

	public void setMostRecentEdgeUpdate(long mostRecentEdgeUpdate) {
		this.mostRecentEdgeUpdate = mostRecentEdgeUpdate;
	}
	
	public Map<Short, FogExchangeInfo> getBuddyExchangeInfo() {
		return buddyExchangeInfo;
	}

	public void setBuddyExchangeInfo(Map<Short, FogExchangeInfo> buddyExchangeInfo) {
		this.buddyExchangeInfo = buddyExchangeInfo;
	}

	public long getMostRecentSelfBFUpdate() {
		return mostRecentSelfBFUpdate;
	}

	public void setMostRecentSelfBFUpdate(long mostRecentSelfBFUpdate) {
		this.mostRecentSelfBFUpdate = mostRecentSelfBFUpdate;
	}

	public long getLastpersonalBFSent() {
		return lastpersonalBFSent;
	}

	public void setLastpersonalBFSent(long lastpersonalBFSent) {
		this.lastpersonalBFSent = lastpersonalBFSent;
	}

	public Map<Short, FogExchangeInfo> getNeighborExchangeInfo() {
		return neighborExchangeInfo;
	}

	public void setNeighborExchangeInfo(Map<Short, FogExchangeInfo> neighborExchangeInfo) {
		this.neighborExchangeInfo = neighborExchangeInfo;
	}

	public long getMostRecentNeighborBFUpdate() {
		return mostRecentNeighborBFUpdate;
	}

	public void setMostRecentNeighborBFUpdate(long mostRecentNeighborBFUpdate) {
		this.mostRecentNeighborBFUpdate = mostRecentNeighborBFUpdate;
	}

	public long getLastNeighborBFSent() {
		return lastNeighborBFSent;
	}

	public void setLastNeighborBFSent(long lastNeighborBFSent) {
		this.lastNeighborBFSent = lastNeighborBFSent;
	}

	public long getMostRecentNeighborStatsUpdate() {
		return mostRecentNeighborStatsUpdate;
	}

	public void setMostRecentNeighborStatsUpdate(long mostRecentNeighborStatsUpdate) {
		this.mostRecentNeighborStatsUpdate = mostRecentNeighborStatsUpdate;
	}

	public long getLastNeighborStatsSent() {
		return lastNeighborStatsSent;
	}

	public void setLastNeighborStatsSent(long lastNeighborStatsSent) {
		this.lastNeighborStatsSent = lastNeighborStatsSent;
	}
	
	public long getMostRecentFogStatsUpdate() {
		return mostRecentFogStatsUpdate;
	}

	public void setMostRecentFogStatsUpdate(long mostRecentFogStatsUpdate) {
		this.mostRecentFogStatsUpdate = mostRecentFogStatsUpdate;
	}

	public Map<String, StreamMetadataInfo> getStreamMetadata() {
		return streamMetadata;
	}

	public void setStreamMetadata(Map<String, StreamMetadataInfo> streamMetadata) {
		this.streamMetadata = streamMetadata;
	}

	public Map<Short, Set<String>> getEdgeMicrobatchMap() {
		return edgeMicrobatchMap;
	}

	public void setEdgeMicrobatchMap(Map<Short, Set<String>> edgeMicrobatchMap) {
		this.edgeMicrobatchMap = edgeMicrobatchMap;
	}

	public Map<String, List<String>> getMetaToMBIdListMap() {
		return metaToMBIdListMap;
	}

	public void setMetaToMBIdListMap(Map<String, List<String>> metaToMBIdListMap) {
		this.metaToMBIdListMap = metaToMBIdListMap;
	}

	public Map<String, Metadata> getBlockMetadata() {
		return blockMetadata;
	}

	public void setBlockMetadata(Map<String, Metadata> blockMetadata) {
		this.blockMetadata = blockMetadata;
	}
	
	public Map<String, Short> getLocalEdgeWritesInProgress() {
		return localEdgeWritesInProgress;
	}

	public void setLocalEdgeWritesInProgress(Map<String, Short> localEdgeWritesInProgress) {
		this.localEdgeWritesInProgress = localEdgeWritesInProgress;
	}
	
	public int getStreamMetaCacheInvalidation() {
		return streamMetaCacheInvalidation;
	}

	public void setStreamMetaCacheInvalidation(int streamMetaCacheInvalidation) {
		this.streamMetaCacheInvalidation = streamMetaCacheInvalidation;
	}
	
	public int getStreamSoftLease() {
		return streamSoftLease;
	}

	public void setStreamSoftLease(int streamSoftLease) {
		this.streamSoftLease = streamSoftLease;
	}

	public int getStreamHardLease() {
		return streamHardLease;
	}

	public void setStreamHardLease(int streamHardLease) {
		this.streamHardLease = streamHardLease;
	}
	
	public boolean isReplicaCachingEnabled() {
		return isReplicaCachingEnabled;
	}

	public void setReplicaCachingEnabled(boolean isReplicaCachingEnabled) {
		this.isReplicaCachingEnabled = isReplicaCachingEnabled;
	}

	public int getReplicaCachingTime() {
		return replicaCachingTime;
	}

	public void setReplicaCachingTime(int replicaCachingTime) {
		this.replicaCachingTime = replicaCachingTime;
	}

	//called when the thread wakes up after a fixed window to check
	//any recent updates in this window
	public void localStatsCalculate() {
		LOGGER.info("Inside localStatsCalculate()");
		if (localEdgesMap == null || localEdgesMap.isEmpty()) {
			return;
		}
		if (getMostRecentEdgeUpdate() >= getLastLocalUpdatedTime()) {
			LocalStatsHandler lHandler = new LocalStatsHandler(localEdgesMap, coarseGrainedStats,
					localEdgeMapping, noStorageEdges);
			lHandler.computeLocalEdgeStats();
			setLastLocalUpdatedTime(System.currentTimeMillis());
			setMostRecentFogStatsUpdate(System.currentTimeMillis());
			
			LOGGER.info("coarse grained stats "+coarseGrainedStats.toString());
		} else {
			LOGGER.info("No changes since the last local stats update, going to sleep now");
		}
	}

	/** retun model clas with maps **/
	public void globalStatsCalculate() {
		if(getMostRecentFogStatsUpdate() >= getLastGlobalStatsUpdatedTime() ||
				getLastLocalUpdatedTime() >= getLastGlobalStatsUpdatedTime()) {
			GlobalStatsHandler handler = new GlobalStatsHandler(fogUpdateMap, coarseGrainedStats, 
					myFogInfo.getNodeID());
			FogStats medianStats = handler.computeTotalInfomation(globalAllocationMap, storageFogMap);			
			setLastGlobalStatsUpdatedTime(System.currentTimeMillis());
			
			/** The value returned by the Global Stats handler is set here **/
			storageFogMap = handler.getGlobalFogDistribution();
			LOGGER.info("storage fog map is"+storageFogMap.toString());
			edgeDistributionMap = handler.getGlobalEdgeDistribution();
			LOGGER.info("edge distribution map is"+edgeDistributionMap.toString());
		}
	}
	
	//This will go, only here as it is used for some unit testing
	public LocalEdgeStats computeLocalInformation() {
		LocalStatsHandler lHandler = new LocalStatsHandler(localEdgesMap, 
				coarseGrainedStats, localEdgeMapping, noStorageEdges);
		LocalEdgeStats lStats = lHandler.computeLocalEdgeStats();
		setLastLocalUpdatedTime(System.currentTimeMillis());
		//change in local Fog state in terms of 10 bytes has an effect
		//on the global state as well, so check for varying the global
		//updated time as well
		return lStats;
	}

	
	public void computeGlobalStats() {
		//Assumption is the fogUpdateMap doesn't contain its own entry
		//so while calculating global medians, we are adding -1 as key
		//to indicate self and removing once work is done
		GlobalStatsHandler handler = new GlobalStatsHandler(fogUpdateMap, coarseGrainedStats, myFogInfo.getNodeID());
		FogStats medianStats = handler.computeTotalInfomation(globalAllocationMap, storageFogMap);
		//SWAMIJI to come into picture now 
	}
	
	/*public void updateMissingHeartBeats(long edgeHeartbeatInterval, int maxMissingHeartbeats) {
		long currentTime = System.currentTimeMillis();
		List<Short> removeList = new ArrayList<>();
		for (Short edgeId : localEdgesMap.keySet()) {
			EdgeInfo edgeInfo = localEdgesMap.get(edgeId);
			int missHeatbeats = (int) ((currentTime - edgeInfo.getLastHeartBeatTime()) / edgeHeartbeatInterval);
			if (missHeatbeats >= maxMissingHeartbeats) {
				removeList.add(edgeId);
			} else {
				boolean acquired = false;
				try {
					acquired = edgeInfo.acquireLock();
					if (!acquired) {
						// this means another thread acquired the lock
						// means the Edge would have sent the heartbeat
						// so we can skip updating the missing heartbeats
						LOGGER.info("Another thread acquired lock, no need to update missing heartbeats");
						continue;
					}
					edgeInfo.setMissedHeartbeats(missHeatbeats);
				} finally {
					if (acquired) {
						edgeInfo.releaseLock();
					}
				}
			}
		}
		//remove the edges now
		for (Short edgeId : removeList) {
			EdgeInfo edgeInfo = localEdgesMap.get(edgeId);
			//try to acquire lock on the object, if failed means
			//heartbeat received so don't remove the object else
			//check the condition for missingheartbeats and decide
			boolean acquired = false;
			try {
				acquired = edgeInfo.acquireLock();
				if(!acquired) {
					//update happening currently, no need to remove
					continue;
				} else {
					if(edgeInfo.getMissedHeartbeats() >= maxMissingHeartbeats) {
						//reverse condition is this edge was updated after addition
						// to the removalList so don't remove it
						localEdgesMap.remove(edgeId);
					}
				}
			} finally {
				if(acquired) {
					edgeInfo.releaseLock();
				}
			}
		}
	}*/

	public void updateMissingHeartBeats(long edgeHeartbeatInterval, int maxMissingHeartbeats) {
		long currentTime = System.currentTimeMillis();
		boolean newUpdates = false;
		for (Short edgeId : localEdgesMap.keySet()) {
			EdgeInfo edgeInfo = localEdgesMap.get(edgeId);
			// only add to updateList if the edge device was active previously
			if (edgeInfo.getStatus().equals("A")) {
				int missHeatbeats = (int) ((currentTime - edgeInfo.getLastHeartBeatTime()) / edgeHeartbeatInterval);
				LOGGER.info("The current time is "+currentTime);
				LOGGER.info("The last update was "+edgeInfo.getLastHeartBeatTime());
				
				if (missHeatbeats >= maxMissingHeartbeats) {
					LOGGER.info("The missed heart beats are "+missHeatbeats);
					LOGGER.info("EdgeId : {} now not usable for puts or reads anymore", edgeInfo.getNodeId());
					edgeInfo.setStatus("D");
					newUpdates = true;
				}
			}
		}

		// setting of edge device status to 'D' is also an edge update which
		// should involve computing the local stats so that updated 10 bytes
		// are sent to the subscribers and buddies
		if (newUpdates) {
			//some edge device died, so the bloom filter of the fog should
			//be updated to reflect the change. Also we should update the 
			//most recent time of self bloom filter update to send the update
			//when the timer hits for sending heartbeats
			updatePersonalBloomFilter();
			setMostRecentSelfBFUpdate(System.currentTimeMillis());
			setMostRecentEdgeUpdate(System.currentTimeMillis());
		}
	}
	
	private void updatePersonalBloomFilter() {
		byte[] fogBFilter = new byte[Constants.BLOOM_FILTER_BYTES];
		for(Map.Entry<Short, EdgeInfo> entry : localEdgesMap.entrySet()) {
			if(entry.getValue().getStatus().equals("A")) {
				byte[] edgeBFilter = getEdgeBloomFilters().get(entry.getKey());
				for(int i = 0; i < edgeBFilter.length; i++) {
					fogBFilter[i] = (byte) (fogBFilter[i] | edgeBFilter[i]);
				}
			}
		}
		setPersonalBloomFilter(fogBFilter);
	}

	public void sendHeartbeatBuddies(boolean sendBF, boolean forceSendBF, 
			boolean sendStats, boolean forceSendStats) {
		Collection<FogInfo> buddies = buddyMap.values();
		byte[] selfStats = null, consolidatedBFilter = null, consolidatedStreamBF = null;
		List<FogStats> updatedStats = new ArrayList<>();
		//if forced to send the items, then send it else check if you have to send
		//and there is a more recent change than the last time you sent the items
		if (forceSendBF || (sendBF && (getMostRecentNeighborBFUpdate() >= getLastNeighborBFSent()
				|| getMostRecentSelfBFUpdate() >= getLastNeighborBFSent()))) {
			// create consolidated bloomfilter of neighbors and self
			LOGGER.info("Sending consolidated bloomfilter updates to my buddies");
			List<byte[]> filters = createConsolidatedBloomFilter();
			consolidatedBFilter = filters.get(0);
			consolidatedStreamBF = filters.get(1);
			setLastNeighborBFSent(System.currentTimeMillis());
		}
		if (forceSendStats || (sendStats && (getMostRecentNeighborStatsUpdate() >= getLastNeighborStatsSent()
				|| getLastLocalUpdatedTime() >= getLastNeighborStatsSent()))) {
			LOGGER.info("Sending self and my neighbor stats updates to my buddies");
			for (Entry<Short, FogExchangeInfo> entry : neighborExchangeInfo.entrySet()) {
				if (entry.getValue().getLastUpdatedStatsTime() >= getLastNeighborStatsSent()) {
					updatedStats.add(getFogUpdateMap().get(entry.getKey()));
				}
			}
			if (getLastLocalUpdatedTime() >= getLastNeighborStatsSent()) {
				selfStats = coarseGrainedStats.getInfo();
			}
			setLastNeighborStatsSent(System.currentTimeMillis());
		}
		
		BuddyPayload payload = new BuddyPayload();
		payload.setPayload(BuddyDataExchangeFormat.encodeData(getMyFogInfo(), consolidatedBFilter,
				consolidatedStreamBF, selfStats, updatedStats));
		
		for (FogInfo fInfo : buddies) {
			LOGGER.info("Sending heartbeat to buddy : " + fInfo);
			TTransport transport = new TFramedTransport(new TSocket(fInfo.getNodeIP(), fInfo.getPort()));
			try {
				transport.open();
			} catch (TTransportException e) {
				transport.close();
				e.printStackTrace();
				continue;
			}
			TProtocol protocol = new TBinaryProtocol(transport);
			FogService.Client fogClient = new FogService.Client(protocol);
			try {
				fogClient.buddyHeartBeat(payload);
			} catch (TException e) {
				e.printStackTrace();
			} finally {
				transport.close();
			}
		}
	}
	
	private List<byte[]> createConsolidatedBloomFilter() {
		List<byte[]> consolidatedList = new ArrayList<>();
		byte[] bfArray = new byte[Constants.BLOOM_FILTER_BYTES];
		byte[] streamBFArray = new byte[Constants.BLOOM_FILTER_BYTES];
		Map<Short, FogExchangeInfo> nExchangeInfo = getNeighborExchangeInfo();
		for (Entry<Short, FogExchangeInfo> entry : nExchangeInfo.entrySet()) {
			if (entry.getValue() != null) {
				byte[] neighborBF = entry.getValue().getBloomFilterUpdates();
				byte[] neighborStreamBF = entry.getValue().getStreamBFilterUpdates();
				for (int i = 0; i < neighborBF.length; i++) {
					bfArray[i] = (byte) (bfArray[i] | neighborBF[i]);
					streamBFArray[i] = (byte) (streamBFArray[i] | neighborStreamBF[i]);
				}
			}
		}
		// add self info in it as well
		for (int i = 0; i < personalBloomFilter.length; i++) {
			bfArray[i] = (byte) (bfArray[i] | personalBloomFilter[i]);
			streamBFArray[i] = (byte) (streamBFArray[i] | personalStreamBFilter[i]); 
		}
		consolidatedList.add(bfArray);
		consolidatedList.add(streamBFArray);
		return consolidatedList;
	}
	
	
	public void sendHeartbeatSubscribers(boolean sendBF, boolean forceSendBF,
			boolean sendStats, boolean forceSendStats) {
		Collection<NodeInfo> values = subscribedMap.values();
		byte[] bloomFilter = null, streamBFilter = null;
		CoarseGrainedStats localStats = null;
		if(forceSendBF || (sendBF && getMostRecentSelfBFUpdate() >= getLastpersonalBFSent())) {
			LOGGER.info("Sending bloomfilter updates to my subscribers");
			bloomFilter = personalBloomFilter;
			streamBFilter = personalStreamBFilter;
			setLastpersonalBFSent(System.currentTimeMillis());
		}
		if(forceSendStats || (sendStats && getLastLocalUpdatedTime() >= getLastLocalStatsSent())) {
			LOGGER.info("Sending local stats updates to my subscribers");
			localStats = coarseGrainedStats;
			setLastLocalStatsSent(System.currentTimeMillis());
		}
		
		//prepare the payload
		NeighborPayload payload = new NeighborPayload();
		payload.setPayload(NeighborDataExchangeFormat.encodeData(getMyFogInfo(), bloomFilter,
				streamBFilter, localStats));
		
		for (NodeInfo nInfo : values) {
			LOGGER.info("Sending heartbeat to subscriber : " + nInfo);
			TTransport transport = new TFramedTransport(new TSocket(nInfo.getNodeIP(),
					nInfo.getPort()));
			try {
				transport.open();
			} catch (TTransportException e) {
				transport.close();
				e.printStackTrace();
				continue;
			}
			TProtocol protocol = new TBinaryProtocol(transport);
			FogService.Client fogClient = new FogService.Client(protocol);
			try {
				fogClient.neighborHeartBeat(payload);
			} catch (TException e) {
				e.printStackTrace();
			} finally {
				transport.close();
			}
		}
	}
	
	public static Fog deserializeInstance() {
		LOGGER.info("The deserialization started at {}", System.currentTimeMillis());
		Fog fogInput = null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream(Constants.SERIALIZATION_FILE);
			ois = new ObjectInputStream(fis);
			fogInput = (Fog) ois.readObject();
		} catch (IOException | ClassNotFoundException ex) {
			LOGGER.error("Error while deserializing instance");
			LOGGER.error("The error is ", ex);
		}
		LOGGER.info("The deserialization completed at {}", System.currentTimeMillis());
		return fogInput;
	}

}
