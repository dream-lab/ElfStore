package com.dreamlab.edgefs.servicehandler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
import com.dreamlab.edgefs.controlplane.GlobalReplicaAllocation;
import com.dreamlab.edgefs.misc.BloomFilter;
import com.dreamlab.edgefs.misc.BuddyDataExchangeFormat;
import com.dreamlab.edgefs.misc.Constants;
import com.dreamlab.edgefs.misc.GlobalStatsHandler;
import com.dreamlab.edgefs.misc.NeighborDataExchangeFormat;
import com.dreamlab.edgefs.model.BuddyHeartbeatData;
import com.dreamlab.edgefs.model.EdgeInfo;
import com.dreamlab.edgefs.model.FogExchangeInfo;
import com.dreamlab.edgefs.model.FogInfo;
import com.dreamlab.edgefs.model.FogStats;
import com.dreamlab.edgefs.model.LocalEdgeStats;
import com.dreamlab.edgefs.model.NeighborHeartbeatData;
//import com.dreamlab.edgefs.model.LocalEdgeStats;
import com.dreamlab.edgefs.model.NeighborInfo;
import com.dreamlab.edgefs.model.NodeInfo;
import com.dreamlab.edgefs.model.StorageReliability;
import com.dreamlab.edgefs.thrift.BuddyPayload;
import com.dreamlab.edgefs.thrift.EdgeInfoData;
import com.dreamlab.edgefs.thrift.EdgePayload;
import com.dreamlab.edgefs.thrift.EdgeService;
import com.dreamlab.edgefs.thrift.FindReplica;
//import com.dreamlab.edgefs.model.StorageUnit;
import com.dreamlab.edgefs.thrift.FogInfoData;
import com.dreamlab.edgefs.thrift.FogService;
import com.dreamlab.edgefs.thrift.Metadata;
import com.dreamlab.edgefs.thrift.NeighborCount;
import com.dreamlab.edgefs.thrift.NeighborInfoData;
import com.dreamlab.edgefs.thrift.NeighborPayload;
import com.dreamlab.edgefs.thrift.NodeInfoData;
import com.dreamlab.edgefs.thrift.NodeInfoPrimary;
import com.dreamlab.edgefs.thrift.NodeInfoPrimaryTypeStreamMetadata;
import com.dreamlab.edgefs.thrift.QueryReplica;
import com.dreamlab.edgefs.thrift.ReadReplica;
import com.dreamlab.edgefs.thrift.StreamMetadata;
import com.dreamlab.edgefs.thrift.StreamMetadataInfo;
import com.dreamlab.edgefs.thrift.TwoPhaseCommitRequest;
import com.dreamlab.edgefs.thrift.TwoPhaseCommitResponse;
import com.dreamlab.edgefs.thrift.TwoPhasePreCommitRequest;
import com.dreamlab.edgefs.thrift.TwoPhasePreCommitResponse;
import com.dreamlab.edgefs.thrift.WritableFogData;
import com.dreamlab.edgefs.thrift.WritePreference;
import com.dreamlab.edgefs.thrift.WriteResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author master Request: Multiple Node Request state maintenance (persist in a
 *         file? or a HashMap?) Persisting in Hashmap so far
 */
public class FogServiceHandler implements FogService.Iface {

	private static final Logger LOGGER = LoggerFactory.getLogger(FogServiceHandler.class);
	
	private Fog fog;
	private volatile boolean lock = false;
	private int delK = 0; // need to make this synchronized
	private HashMap<Short, String> nodeJoiningStateMap = null; // This is to maintain state per each node which is
	
	//this lock is specifically used when metadata related to the microbatches
	//held by an edge is updated. To be more specific, this is used to serialize
	//access to mbIDLocationMap since it might happen that concurrent writes for a
	//single microbatch to different edges of a single Fog is happening and in that
	//case when we create entry in the map, we have a check if that microbatchId is present
	//and if not, we initialize it with value being a new empty set. We don't want to lose
	//any updates so fine grained locking is necessary and sufficient
	private final Lock edgeMicrobatchLock = new ReentrantLock();

	public FogServiceHandler(Fog fog) {
		super();
		this.fog = fog;
		nodeJoiningStateMap = new HashMap<Short, String>();
	}

	public FogServiceHandler() {

	}

	/** Getters and setters **/
	public Fog getFog() {
		return fog;
	}

	public void setFog(Fog fog) {
		this.fog = fog;
	}

	public boolean isLock() {
		return lock;
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}

	public int getDelK() {
		return delK;
	}

	/** Synchronized because multiple fogs might try to join **/
	public synchronized void setDelK(int delK) {
		this.delK = delK;
	}

	/** Getters and setters end **/

	/**
	 * 
	 * @param NodeX
	 * @return
	 * @throws TException
	 */
	@Override
	public String bootstrapFog(FogInfoData NodeX) throws TException {
		LOGGER.info("NODE X infor " + NodeX.toString());
		return "SUCCESS";
	}

	private FogStats convertBytesToStats(byte[] coarseGrainedStats) {
		FogStats stats = new FogStats();
		if (coarseGrainedStats.length < 10) {
			// this is incorrect, maybe log an exception and check whats wrong
			return null;
		}
		stats.setMinStorage(Constants.interpretByteAsLong(coarseGrainedStats[0]));
		stats.setMedianStorage(Constants.interpretByteAsLong(coarseGrainedStats[1]));
		stats.setMaxStorage(Constants.interpretByteAsLong(coarseGrainedStats[2]));
		stats.setMinReliability(coarseGrainedStats[3]);
		stats.setMedianReliability(coarseGrainedStats[4]);
		stats.setMaxReliability(coarseGrainedStats[5]);
		stats.setA(coarseGrainedStats[6]);
		stats.setB(coarseGrainedStats[7]);
		stats.setC(coarseGrainedStats[8]);
		stats.setD(coarseGrainedStats[9]);
		return stats;
	}

	/**
	 * The joining node requests the Fog Referrer to send it candidates of buddy
	 * pool between pmin and pmax
	 * 
	 * @param NodeX the joining node
	 * @param pmin  the lower bound of buddy pool candidates
	 * @param pmax  the upper bound of buddy pool candidates
	 * @return List<Neighbor> back to the joining client fog
	 * @throws TException
	 */
	@Override
	public List<NeighborInfoData> joinCluster(FogInfoData NodeX, short pmin, short pmax) throws TException {

		/** Check the local neighbor data **/
		List<NeighborInfoData> candidatePool = new ArrayList<NeighborInfoData>();

		Map<Short, Short> myNeighborMap = new HashMap<Short, Short>();
		int count = 0;
		for (NeighborInfo neighbor : getFog().getNeighborsMap().values()) {

			/** Use only distinct buddypoolId **/
			if (!myNeighborMap.containsKey(neighbor.getBuddyPoolId()) && neighbor.getPoolSize() < getFog().getkMax()
					&& count < pmax) { // check that the pool size constraint is not breached, and count hasn't
										// breached pmax

				myNeighborMap.put(neighbor.getBuddyPoolId(), (short) 1);

				NeighborInfoData neighborData = new NeighborInfoData();
				neighborData.setBuddyPoolId(neighbor.getBuddyPoolId());
				neighborData.setPool_reliability(neighbor.getPoolReliability());
				neighborData.setPool_size(neighbor.getPoolSize());

				NodeInfoData nodeInfo = new NodeInfoData();
				nodeInfo.setNodeId(neighbor.getNode().getNodeID());
				nodeInfo.setNodeIP(neighbor.getNode().getNodeIP());
				nodeInfo.setPort(neighbor.getNode().getPort());
				neighborData.setNodeInstance(nodeInfo);

				candidatePool.add(neighborData);
				count++;
			}
		}

		if (myNeighborMap.keySet().size() > pmin) {
			// return the list of neighborinfo
			return candidatePool;
		} else { // check buddies to give their candidate Pool
			for (FogInfo buddy : getFog().getBuddyMap().values()) {

				List<NeighborInfoData> candidatePoolReturnedByBuddies = null;

				TTransport transport = new TFramedTransport(new TSocket("127.0.0.1", buddy.getPort()));
				transport.open();

				TProtocol protocol = new TBinaryProtocol(transport);
				FogService.Client fogClient = new FogService.Client(protocol);
				candidatePoolReturnedByBuddies = fogClient.getCandidatePool(NodeX.getReliability(), pmin); // Joining
																											// node's
				
				//this code is not currently in use, fix it when used
				transport.close();																	// reliability
																											// and the
																											// pmin
																											// should be
																											// sent

				for (NeighborInfoData neighborData : candidatePoolReturnedByBuddies) {
					if (!myNeighborMap.containsKey(neighborData.getBuddyPoolId())) {
						myNeighborMap.put(neighborData.getBuddyPoolId(), (short) 1);
						candidatePool.add(neighborData);
					}
				}

			}
		}

		return candidatePool;
	}

	/**
	 * This call is made by a referrer fog to its buddy, because the candidate pools
	 * size were less than pmin So the called on buddy will send the candidates in
	 * its buddy pool back to the referrer's fog
	 * 
	 * @param reliability Reliability of the joining fog
	 * @param pmin        the minimum number of pools needed
	 * @return List<Neighbor> back to the referrer fog
	 * @throws TException
	 */
	@Override
	public List<NeighborInfoData> getCandidatePool(double reliability, short pmin) throws TException {

		List<NeighborInfoData> candidatePool = new ArrayList<NeighborInfoData>();

		int count = 0;
		Map<Short, Short> myNeighborMap = new HashMap<Short, Short>();
		for (NeighborInfo neighbor : getFog().getNeighborsMap().values()) {

			/** Use only distinct buddypoolId **/
			if (!myNeighborMap.containsKey(neighbor.getBuddyPoolId()) && neighbor.getPoolSize() < getFog().getkMax()
					&& count < pmin) { // check that the pool size constraint is not breached, and the pmin count is
										// not yet reached

				myNeighborMap.put(neighbor.getBuddyPoolId(), (short) 1);

				NeighborInfoData neighborData = new NeighborInfoData();
				neighborData.setBuddyPoolId(neighbor.getBuddyPoolId());
				neighborData.setPool_reliability(neighbor.getPoolReliability());
				neighborData.setPool_size(neighbor.getPoolSize());

				NodeInfoData nodeInfo = new NodeInfoData();
				nodeInfo.setNodeId(neighbor.getNode().getNodeID());
				nodeInfo.setNodeIP(neighbor.getNode().getNodeIP());
				nodeInfo.setPort(neighbor.getNode().getPort());
				neighborData.setNodeInstance(nodeInfo);

				candidatePool.add(neighborData);

				count++;
			}
		}

		return candidatePool;
	}

	/**
	 * 
	 * @param NodeX The joining Node
	 * @return YES OR NO, NO means failed to join, YES means NODEX joined the pool
	 * @throws TException missing feature : Unable to communicate to buddy, timeout
	 *                    occurs, move on and make a decision missing feature :
	 *                    Maintain state until the decision is communicated
	 */
	@Override
	public TwoPhaseCommitResponse joinPool(FogInfoData NodeX) {
		//This is not in use now, will fix transport issues when used
		
		LOGGER.info("The incoming NodeX " + NodeX.toString());
		Collection<FogInfo> myBuddyList = fog.getBuddyMap().values();
		int counter = 0;

		// PHASE 1 : PRE-COMMIT
		LOGGER.info("Starting PHASE 1");

		try {
			for (FogInfo fogInfo : myBuddyList) {
				TTransport transport = new TFramedTransport(new TSocket("127.0.0.1", fogInfo.getPort()));
				transport.open();

				TProtocol protocol = new TBinaryProtocol(transport);
				FogService.Client fogClient = new FogService.Client(protocol);

				TwoPhasePreCommitRequest preCommitReq = new TwoPhasePreCommitRequest();
				preCommitReq.setCoordinatorId(fog.getMyFogInfo().getNodeID());
				preCommitReq.setJoiningNode(NodeX.getNodeInstance());
				preCommitReq.setRequestType(Constants.TWO_PHASE_PRE_COMMIT);

				TwoPhasePreCommitResponse response = fogClient.initiate2PhasePreCommit(preCommitReq);
				LOGGER.info("The response is " + response.getResponseType());

				if (response.getResponseType().equals(Constants.STATUS_NO))
					return new TwoPhaseCommitResponse(Constants.STATUS_NO);
				else
					counter++;
			}

		} catch (TException e) {
			e.printStackTrace();
		} finally {
			
		}

		// PHASE 2: starts
		if (counter == myBuddyList.size()) {

			LOGGER.info("Starting PHASE 2");
			counter = 0;
			// PHASE 2 : COMMIT
			for (FogInfo fogInfo : myBuddyList) {
				TTransport transport = new TFramedTransport(new TSocket("localhost", fogInfo.getPort()));
				try {
					transport.open();

					TProtocol protocol = new TBinaryProtocol(transport);
					FogService.Client fogClient = new FogService.Client(protocol);

					TwoPhaseCommitRequest commitReq = new TwoPhaseCommitRequest();
					commitReq.setCoordinatorId(fog.getMyFogInfo().getNodeID());
					commitReq.setNodeId(NodeX.getNodeInstance().getNodeId());
					commitReq.setRequestType(Constants.TWO_PHASE_COMMIT);

					TwoPhaseCommitResponse response = fogClient.initiate2PhaseCommit(commitReq);
					if (response.getResponseType().equals(Constants.STATUS_NO))
						return new TwoPhaseCommitResponse(Constants.STATUS_NO);
					else
						counter++;

				} catch (TException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (counter == myBuddyList.size()) {
				return new TwoPhaseCommitResponse(Constants.STATUS_YES);
			}
		} // end if , check for sufficient phase 1 responses
		return new TwoPhaseCommitResponse(Constants.STATUS_NO); // PHASE 1 insufficient responses
	}

	/**
	 * Pre-Commit phase in 2PC, checks if node size is breached before admitting any
	 * fog node
	 * 
	 * @param preCommitRequest
	 * @return TwoPhasePreCommitResponse
	 * @throws TException
	 */
	@Override
	public TwoPhasePreCommitResponse initiate2PhasePreCommit(TwoPhasePreCommitRequest preCommitRequest) {

		LOGGER.info("in 2 phase pre commit : the kmax is " + fog.getkMax());
		TwoPhasePreCommitResponse response = new TwoPhasePreCommitResponse();
		response.setResponseType(Constants.STATUS_NO);

		if (preCommitRequest.requestType.equals(Constants.TWO_PHASE_PRE_COMMIT)) {
			// TODO: CHANGE this fog.getBuddyList().size() to getK
			if ((fog.getK() + delK) >= fog.getkMax()) { // Check if it has exceeded kmax

				LOGGER.info("pool size exceeded " + (fog.getK() + delK));

				response.setResponseType(Constants.STATUS_NO);
			} else {

				setDelK(delK + 1); // increment the delK by 1, synchronized call
				LOGGER.info("pool size is " + (fog.getK() + delK) + " MAX size is " + fog.getkMax());
				response.setResponseType(Constants.STATUS_YES);

				String stateAndJoinNode = Constants.TWO_PHASE_PRE_COMMIT;
				nodeJoiningStateMap.put(preCommitRequest.joiningNode.getNodeId(), stateAndJoinNode); // eg: NodeID =>
																										// PRE_COMMIT
			}
		}
		return response;

	}

	/**
	 * 2 Phase commit : Phase 2,
	 * 
	 * @param commitRequest
	 * @return STATUS_YES if already in PRE_COMMIT for the joining NODE, else return
	 *         STATUS_NO
	 * @throws TException
	 */
	// TODO: Optimistic sending of YES message for the nodes which go down after
	// saying YES in the PRE_COMMIT PHASE
	@Override
	public TwoPhaseCommitResponse initiate2PhaseCommit(TwoPhaseCommitRequest commitRequest) {

		LOGGER.info("In 2 phase commit...");
		TwoPhaseCommitResponse response = new TwoPhaseCommitResponse();
		response.setResponseType(Constants.STATUS_NO);

		if (commitRequest.getRequestType().equals(Constants.TWO_PHASE_COMMIT)) {
			// Check for all the conditions, "statefulness"
			if (nodeJoiningStateMap.containsKey(commitRequest.getNodeId())) { // Check if the COMMIT REQUEST IS COMING
																				// from the same joining Node

				String prevState = nodeJoiningStateMap.get(commitRequest.getNodeId());
				LOGGER.info("Previous agreed state " + prevState);
				if (prevState.equals(Constants.TWO_PHASE_PRE_COMMIT)) {
					response.setResponseType(Constants.STATUS_YES);
					fog.setK(fog.getK() + 1); // Increment the pool size by 1
					setDelK(delK - 1); // reduce del by 1
				}

			}
		}
		return response;
	}

	@Override
	public NodeInfoData getPoolMember(short buddyPoolId) throws TException {
		if (fog.getBuddyPoolId() == buddyPoolId) {
			return new NodeInfoData(fog.getMyFogInfo().getNodeID(), fog.getMyFogInfo().getNodeIP(),
					fog.getMyFogInfo().getPort());
		}
		Collection<NeighborInfo> neighbors = fog.getNeighborsMap().values();
		for (NeighborInfo neighbor : neighbors) {
			if (neighbor.getBuddyPoolId() == buddyPoolId) {
				NodeInfo nodeInfo = neighbor.getNode();
				return new NodeInfoData(nodeInfo.getNodeID(), nodeInfo.getNodeIP(), nodeInfo.getPort());
			}
		}
		return null;
	}

	@Override
	public List<FogInfoData> getBuddyPoolMembers() throws TException {
		List<FogInfoData> members = new ArrayList<>();
		for (FogInfo fogInfo : fog.getBuddyMap().values()) {
			members.add(new FogInfoData(new NodeInfoData(fogInfo.getNodeID(), fogInfo.getNodeIP(), fogInfo.getPort()),
					fogInfo.getReliability()));
		}
		// add self information
		members.add(new FogInfoData(new NodeInfoData(fog.getMyFogInfo().getNodeID(), fog.getMyFogInfo().getNodeIP(),
				fog.getMyFogInfo().getPort()), fog.getMyFogInfo().getReliability()));
		return members;
	}

	@Override
	public NeighborCount getNeighborCountPerPool() throws TException {
		NeighborCount neighborCount = new NeighborCount(false);
		if (!lock) {
			synchronized (fog) {
				if (!lock) {
					lock = true;
				} else {
					// unable to acquire lock
					return neighborCount;
				}
			}
		} else {
			return neighborCount;
		}
		Map<Short, Short> neighborCountMap = new HashMap<>();
		Collection<NeighborInfo> neighbors = fog.getNeighborsMap().values();
		for (NeighborInfo nInfo : neighbors) {
			short nPoolId = nInfo.getBuddyPoolId();
			if (!neighborCountMap.containsKey(nPoolId)) {
				neighborCountMap.put(nPoolId, (short) 0);
			}
			neighborCountMap.put(nPoolId, (short) (neighborCountMap.get(nPoolId) + 1));
		}
		neighborCount.setIsLockAcquired(true);
		neighborCount.setNeighborCountPerPool(neighborCountMap);
		FogInfo myFogInfo = fog.getMyFogInfo();
		neighborCount
				.setNodeInfoData(new NodeInfoData(myFogInfo.getNodeID(), myFogInfo.getNodeIP(), myFogInfo.getPort()));
		return neighborCount;
	}

	@Override
	public List<NeighborInfoData> requestNeighbors(Map<Short, Short> requestMap) throws TException {
		List<NeighborInfoData> givenNeighbors = new ArrayList<>();
		Iterator<NeighborInfo> iter = fog.getNeighborsMap().values().iterator();
		while (iter.hasNext()) {
			NeighborInfo next = iter.next();
			if (requestMap.containsKey(next.getBuddyPoolId())) {
				NodeInfo node = next.getNode();
				givenNeighbors
						.add(new NeighborInfoData(new NodeInfoData(node.getNodeID(), node.getNodeIP(), node.getPort()),
								next.getBuddyPoolId(), next.getPoolReliability(), next.getPoolSize()));
				if (requestMap.get(next.getBuddyPoolId()) == 1) {
					requestMap.remove(next.getBuddyPoolId());
				} else {
					requestMap.put(next.getBuddyPoolId(), (short) (requestMap.get(next.getBuddyPoolId()) - 1));
				}
				iter.remove();
			}
		}
		return givenNeighbors;
	}

	@Override
	public void nodeJoiningComplete() throws TException {
		// this method is called to release the lock once a joining node
		// completes the process
		if (lock) {
			synchronized (fog) {
				if (lock) {
					lock = false;
				}
			}
		}
	}

	/**
	 * This will perform the selection of replicas for a given metadata
	 * 
	 * @param dataLength
	 * It is called from {@link #getWriteLocations} wherein we receive the encoded
	 * value of storage space as a byte and send to this method the datalength in MB
	 */
//	public List<WritableFogData> identifyReplicas(long dataLength, EdgeInfoData edge,double expectedReliability,int minReplica,int maxReplica) {
	//new field microbatchId added to make sure that even though two edges on a Fog can contain the
	//same microbatch, it should never happen that same microbatch is replicated on same edge neither
	//during normal writes (via localWritesInProgress) nor during recovery (via mbIDLocationMap)
	public List<WritableFogData> identifyReplicas(String mbId, long dataLength, boolean isEdge,double expectedReliability,int minReplica,int maxReplica) {
		
		LOGGER.info("the data length requested is "+dataLength);

		GlobalStatsHandler globalStatsHandler = new GlobalStatsHandler(fog.getFogUpdateMap(),
				fog.getCoarseGrainedStats(), fog.getMyFogInfo().getNodeID());

		List<NodeInfoData> replicasToWrite = new ArrayList<NodeInfoData>();//
		List<WritableFogData> fogsToWrite = new ArrayList<WritableFogData>();

		GlobalReplicaAllocation replicaAlloc = new GlobalReplicaAllocation();

		String pref = "HHL";
		double reliability = 0.0;
		
		/*if (edge != null && fog.getLocalEdgesMap().containsKey(edge.getNodeId())) {
			Short edgeId = edge.getNodeId();
			LOGGER.info("The storage for this " + fog.getLocalEdgesMap().get(edgeId).getStats().getStorage());

			if (fog.getLocalEdgesMap().get(edgeId).getNodeIp().equals(edge.getNodeIp())
					&& fog.getLocalEdgesMap().get(edgeId).getStatus().equals("A")
					&& fog.getLocalEdgesMap().get(edgeId).getStats().getStorage() >= Constants.DISK_WATERMARK
					&& fog.getLocalEdgesMap().get(edgeId).getStats().getStorage() > dataLength) {
				NodeInfoData edgeData = new NodeInfoData(edge.getNodeId(), edge.getNodeIp(), edge.getPort());
				replicaAlloc.setReliability((double) edge.getReliability() / 100.0);
				LOGGER.info("The reliability is set to " + replicaAlloc.getReliability());
				reliability = replicaAlloc.getReliability();
				replicasToWrite.add(edgeData);
			} else {
				FogStats selfStats = FogStats.createInstance(fog.getCoarseGrainedStats().getInfo());
				// lets try to pick a higher reliability edge first
				EdgeInfo chosenEdge = getHighReliabilityEdge(selfStats, dataLength * 1024 * 1024, null);
				if (chosenEdge == null) {
					chosenEdge = getLowReliabilityEdge(selfStats, dataLength * 1024 * 1024, null);
				}
				if (chosenEdge == null) {
					LOGGER.info("Unable to pick A local edge");
				} else {
					NodeInfoData edgeData = new NodeInfoData(chosenEdge.getNodeId(), chosenEdge.getNodeIp(),
							chosenEdge.getPort());
					replicaAlloc.setReliability((double) chosenEdge.getStats().getReliability() / 100.0);
					LOGGER.info("The reliability is set to " + replicaAlloc.getReliability());
					reliability = replicaAlloc.getReliability();
					replicasToWrite.add(edgeData);
				}

			}
		}
*/
		
		//the logic to pick local replica is changed to this piece only
		FogStats selfStats = FogStats.createInstance(fog.getCoarseGrainedStats().getInfo());
		// lets try to pick a higher reliability edge first
		EdgeInfo chosenEdge = getHighReliabilityEdge(selfStats, dataLength * 1024 * 1024, 
				fog.getMbIDLocationMap().get(mbId), mbId);
		if (chosenEdge == null) {
			chosenEdge = getLowReliabilityEdge(selfStats, dataLength * 1024 * 1024, 
				fog.getMbIDLocationMap().get(mbId), mbId);
		}
		if (chosenEdge == null) {
			LOGGER.info("Unable to pick A local edge");
		} else {
			NodeInfoData edgeData = new NodeInfoData(chosenEdge.getNodeId(), chosenEdge.getNodeIp(),
					chosenEdge.getPort());
			replicaAlloc.setReliability((double) chosenEdge.getStats().getReliability() / 100.0);
			LOGGER.info("The reliability is set to " + replicaAlloc.getReliability());
			reliability = replicaAlloc.getReliability();
			replicasToWrite.add(edgeData);
			//this is for the local write, so update the localEdgeWritesInProgress
			fog.getLocalEdgeWritesInProgress().put(mbId, chosenEdge.getNodeId());
		}
		
		/**
		 * Get all the stream related metadata such as reliability , replica count and
		 * so on, which was set during registration
		 **/
		if(replicasToWrite.size()==1) {
			LOGGER.info("here ");
			
			WritableFogData localWritable = new WritableFogData();
			
			FogInfo fogInfo  = fog.getMyFogInfo();
			NodeInfoData fogNodeInfo = new NodeInfoData(fogInfo.getNodeID(), fogInfo.getNodeIP(), fogInfo.getPort());
			
			EdgeInfoData myEdgeInfo = new EdgeInfoData();
			myEdgeInfo.setNodeId(replicasToWrite.get(0).getNodeId());
			myEdgeInfo.setNodeIp(replicasToWrite.get(0).getNodeIP());
			myEdgeInfo.setPort(replicasToWrite.get(0).getPort());
			
			localWritable.setReliability(reliability);
			localWritable.setNode(fogNodeInfo);
			localWritable.setEdgeInfo(myEdgeInfo);
			
			
			if(pref.equals("HHL")) {
				localWritable.setPreference(WritePreference.HHL);
			}else if(pref.equals("HLH")) {
				localWritable.setPreference(WritePreference.HLH);
			}else if(pref.equals("HHH")) {
				localWritable.setPreference(WritePreference.HHH);
			}else if(pref.equals("HLL")) {
				localWritable.setPreference(WritePreference.HLL);
			}else if(pref.equals("LHH")) {
				localWritable.setPreference(WritePreference.LHH);
			}else if(pref.equals("LHL")) {
				localWritable.setPreference(WritePreference.LHL);
			}else if(pref.equals("LLH")) {
				localWritable.setPreference(WritePreference.LLH);
			}else {
				localWritable.setPreference(WritePreference.LLL);
			}
			
			
			LOGGER.info("The fog data being sent "+fogsToWrite.toString());
			
			fogsToWrite.add(localWritable);
			
		}		
		
//		StreamMetadata streamMD = fog.getStreamToStreamMetadata().get(streamId); 
		LOGGER.info("The edge distribution map " + fog.getEdgeDistributionMap().toString());
		LOGGER.info("The fog distribution map " + fog.getStorageFogMap().toString());

		//TODO: REMOVE HARD CODING
		List<NodeInfo> allotedFogReplicas = replicaAlloc.identifyReplicas(fog.getEdgeDistributionMap(), /**updated here **/
				fog.getStorageFogMap(), fog.getFogUpdateMap(), expectedReliability, minReplica- replicasToWrite.size(), maxReplica - replicasToWrite.size()); // needed reliability, minReplica and
																			// MaxReplica

		Map<Short, List<String>> fogChoiceList = replicaAlloc.getFogChoiceList();
		LOGGER.info("The choices made are " + fogChoiceList.toString());
		replicaAlloc.prepareChoiceList();

		LOGGER.info("The choice list is " + replicaAlloc.getFogChoiceList().toString());		
		LOGGER.info("The replica choices finals "+replicasToWrite.toString());
		
		List<String> preferenceList = replicaAlloc.getChoiceList();
		List<NodeInfo> nodesChosen = replicaAlloc.getFogsChosen();
		List<Double> distribution = replicaAlloc.getReliabilityContribution();
		
		replicaAlloc.prepareChoiceList();
		fogChoiceList = replicaAlloc.getFogChoiceList();
		
		LOGGER.info("The choices made "+preferenceList.toString());
		LOGGER.info("The nodes chosen "+nodesChosen.toString());
		LOGGER.info("The reliability distribution "+distribution.toString());
			
		for(int i =0;i<preferenceList.size();i++) {
			
			WritableFogData myFogData = new WritableFogData();
			
			myFogData.setReliability(distribution.get(i));
			String preference = preferenceList.get(i);
			
			if(preference.equals("HHL")) {
				myFogData.setPreference(WritePreference.HHL);
			}else if(preference.equals("HLH")) {
				myFogData.setPreference(WritePreference.HLH);
			}else if(preference.equals("HHH")) {
				myFogData.setPreference(WritePreference.HHH);
			}else if(preference.equals("HLL")) {
				myFogData.setPreference(WritePreference.HLL);
			}else if(preference.equals("LHH")) {
				myFogData.setPreference(WritePreference.LHH);
			}else if(preference.equals("LHL")) {
				myFogData.setPreference(WritePreference.LHL);
			}else if(preference.equals("LLH")) {
				myFogData.setPreference(WritePreference.LLH);
			}else {
				myFogData.setPreference(WritePreference.LLL);
			}
			
			NodeInfoData fogNodeInfo = new NodeInfoData();
			
			fogNodeInfo.setNodeId(nodesChosen.get(i).getNodeID());
			fogNodeInfo.setNodeIP(nodesChosen.get(i).getNodeIP());
			fogNodeInfo.setPort(nodesChosen.get(i).getPort());
			
			LOGGER.info("the fog info that I am about to send "+fogNodeInfo);
			
			myFogData.setNode(fogNodeInfo);
			
			LOGGER.info("The payload about to send "+myFogData.toString());
			
			fogsToWrite.add(myFogData);
			
			Short nodeIdChosen = nodesChosen.get(i).getNodeID();
			for(int j=0;j<fogChoiceList.get(nodeIdChosen).size()-1;j++){
				fogsToWrite.add(myFogData);
			}
		}

//		for (NodeInfo allotedFog : allotedFogReplicas) {
//			NodeInfoData myFog = new NodeInfoData(allotedFog.getNodeID(), allotedFog.getNodeIP(), allotedFog.getPort());
//			replicasToWrite.add(myFog);
//		}

		return fogsToWrite;
	}

	@Override
	public boolean subscribe(NodeInfoData nodeInfoData) throws TException {
		NodeInfo nodeInfo = new NodeInfo();
		nodeInfo.setNodeID(nodeInfoData.getNodeId());
		nodeInfo.setNodeIP(nodeInfoData.getNodeIP());
		nodeInfo.setPort(nodeInfoData.getPort());
		fog.getSubscribedMap().put(nodeInfoData.getNodeId(), nodeInfo);
		LOGGER.info("Subscribed to fog with Id : " + nodeInfoData.getNodeId());
		return true;
	}

	/** Bloom filter searches from the consolidated neighbor buddy bloom filter **/
	public NodeInfo checkConsolidatedNeighborBloomFilter(String microbatchID) {

		NodeInfo destFog = new NodeInfo();

		/** Check the consolidated structure **/
		/** If not present return false **/

		return destFog;
	}

	/**
	 * The regular Edge Heart beat,every message will be payloaded in this heart
	 * beat
	 */
	@Override
	public boolean edgeHeartBeats(EdgePayload edgePayload) throws TException {
		if (edgePayload != null) {
			LOGGER.info("Got Edge Heart beat from edgeId : " + edgePayload.getEdgeId());
			long currentTime = System.currentTimeMillis();
			Short edgeId = edgePayload.getEdgeId();
			EdgeInfo edgeInfo = fog.getLocalEdgesMap().get(edgeId);
			if (edgeInfo == null)
				return false;
			edgeInfo.setLastHeartBeatTime(currentTime);
			edgeInfo.setMissedHeartbeats(0);
			// we may need to update the status of the EdgeInfo in case
			// it was D and starts sending heartbeats
			// there are some issues open regarding how to deal with edge
			// coming back, need more clarity on those
			if (edgePayload.isSetEncodedStorage()) {
				long freeSpace = Constants.interpretByteAsLong(edgePayload.getEncodedStorage());
				edgeInfo.getStats().setStorage(freeSpace);
				// the freeSpace may be less than the watermark, add this edge to
				// the set of noStorage devices
//				if (freeSpace <= fog.getEdgeDiskWatermark()) {
				if (freeSpace <= Constants.DISK_WATERMARK) {
					LOGGER.info("The EdgeId : {} has lower storage than the disk watermark, will not"
							+ " be considered in the local and global stats", edgeId);
					fog.getNoStorageEdges().add(edgeId);
				}
				edgeInfo.setLastUpdatedTime(currentTime);
				fog.setMostRecentEdgeUpdate(System.currentTimeMillis());
			}
			return true;
		}
		return false;
	}

	/**
	 * Device Management API: This is called once at the joining of Edge Should
	 * update the local Edges
	 * TODO::Check killed edge joining again
	 */
	@Override
	public byte edgeJoin(EdgeInfoData edgeInfoData) throws TException {

		LOGGER.info("the incoming edge is registered " + edgeInfoData.toString());
		Short nodeId = edgeInfoData.getNodeId();

		EdgeInfo edgeInfo = new EdgeInfo();
		edgeInfo.setLastHeartBeatTime(System.currentTimeMillis()); /** Set timestamp **/
		edgeInfo.setNodeId(nodeId);
		edgeInfo.setNodeIp(edgeInfoData.getNodeIp());
		edgeInfo.setPort(edgeInfoData.getPort());

		LocalEdgeStats edgeStats = new LocalEdgeStats();
		edgeStats.setReliability(edgeInfoData.getReliability());
		edgeStats.setStorage(Constants.interpretByteAsLong(edgeInfoData.getStorage()));

		edgeInfo.setStats(edgeStats);
		/** The edge is added to the local edge map **/ // No need to synchronize here
		fog.getLocalEdgesMap().put(nodeId, edgeInfo);
		fog.setMostRecentEdgeUpdate(System.currentTimeMillis());

		// a newly joining edge should trigger local stats calculation
		fog.setMostRecentEdgeUpdate(System.currentTimeMillis());
		// add this edge to map containing edge to list of microbatch mapping
		fog.getEdgeMicrobatchMap().put(nodeId, new HashSet<>());
		
		//as a fix to issue #25, we need to maintain an individual bloom filter for
		//each edge, so create an entry in the edgeBloomFilters of the Fog while joining
		fog.getEdgeBloomFilters().put(edgeInfoData.getNodeId(), new byte[Constants.BLOOM_FILTER_BYTES]);

		// please set a consistent value of return values, keep success fixed as 0 or 1
		return 1;
	}
	
	/**
	 * unregister the stream ID, this is the call from the edge client which is
	 * producing the stream
	 * 
	 * On the other end, it will delete the local directory mount.
	 */
	@Override
	public byte terminate(String streamId) throws TException {

//		fog.getStreamIDEdgeIDMap().remove(streamId);

		return 0;
	}

	//registerStream is equivalent of the create()
	@Override
	public byte registerStream(String streamId, StreamMetadata metadata) throws TException {
		if (metadata != null) {
			//set the owner of the stream to this Fog
			NodeInfoPrimaryTypeStreamMetadata ownerFog = new NodeInfoPrimaryTypeStreamMetadata(
					new NodeInfoPrimary(fog.getMyFogInfo().getNodeIP(), fog.getMyFogInfo().getPort()),
					false);
			metadata.setOwner(ownerFog);
			StreamMetadataInfo metadataInfo = new StreamMetadataInfo();
			metadataInfo.setStreamMetadata(metadata);
			metadataInfo.setCached(false);
			fog.getStreamMetadata().put(streamId, metadataInfo);
		}
		
		// once stream is registered, initialize the set of microbatches for the stream
		fog.getStreamMbIdMap().put(streamId, new HashSet<>());
		// TODO:create directory in the edge if don't want a flat namespace
		updateStreamBloomFilter(streamId, metadata);
		return Constants.SUCCESS;
	}
	
	private void updateStreamBloomFilter(String streamId, StreamMetadata streamMetadata) {
		//for every key present in the streamMetadata along with the streamId, update
		//the personal stream bloomfilter. Currently search is not supported on top of
		//the stream metadata, to do that a similar thing like microbatch needs to be done
		byte[] streamBloomFilter = fog.getPersonalStreamBFilter();
		BloomFilter.storeEntry(Constants.STREAM_METADATA_ID, streamId, streamBloomFilter);
		BloomFilter.storeEntry(Constants.STREAM_METADATA_START_TIME, String.valueOf(
				streamMetadata.getStartTime().getValue()), streamBloomFilter);
		//others can also be hashed and stored in BF but since search is supported on top of
		//streamId only to fetch the StreamMetadata, for now this will do
		
		//At this point, the personal stream bloomfilter is updated which should be sent
		//in the next time window to the subscribers as well as the buddies(consolidated)
		fog.setMostRecentSelfBFUpdate(System.currentTimeMillis());
		//this is done only to trigger the sending of consolidated bloomfilters
		//to the buddies
		fog.setMostRecentNeighborBFUpdate(System.currentTimeMillis());
	}

	/**
	 * Maybe not needed right now
	 */
	@Override
	public String intentToWrite(byte clientId) throws TException {

		return null;
	}

	/**
	 * This method is called by the client to write the data
	 * 
	 * returns the List of IP addresses of Fog for the client
	 */
	/*
	 * @Override public List<NodeInfoData> write(byte dataLength,byte nodeId) throws
	 * TException {
	 * 
	 * LOGGER.info("Got a write request "+nodeId);
	 * 
	 * List<NodeInfoData> fogLocations = identifyReplicas(dataLength,(short)
	 * nodeId);
	 * 
	 * LOGGER.info("Here print "+fogLocations.toString());
	 * 
	 * return fogLocations; }
	 */

	/**
	 * At the finish of the write, the micro batchId and the metadata should be
	 * updated
	 **/
	/*
	 * public byte insertMetadata(Metadata mbMetadata) throws TException {
	 * 
	 *//** For each incoming metadata, map the microbatch ID to the edge **/
	/*
	 * fog.getMbIDLocationMap().put(mbMetadata.getMbId(), mbMetadata.getEdgeId());
	 * //Updated an entry for micro batch
	 * 
	 * //to ease recovery, all microbatches stored by an edge are also
	 * 
	 * String metaData = mbMetadata.getTimestamp(); //time stamp is the only
	 * metadata Map<String, List<String>> metadataMicrobacthIDList =
	 * fog.getMetaMbIdMap();
	 * 
	 *//** For each incoming metadata, map the metadata to micro batch ID **//*
																				 * if(fog.getMetaMbIdMap().containsKey(
																				 * metaData)) {
																				 * fog.getMetaMbIdMap().get(metaData).
																				 * add(mbMetadata.getMbId()); //for each
																				 * time stamp add the micro batche ID
																				 * }else {
																				 * fog.getMetaMbIdMap().get(metaData).
																				 * add(metaData); }
																				 * 
																				 * return Constants.SUCCESS; }
																				 */

	/**
	 * We are not supporting this feature as of now
	 */
	@Override
	public List<NodeInfoData> writeNext(String sessionId, Metadata mbData, byte dataLength) throws TException {
		return null;
	}
	
	/** 
	 * selfInfo is the client information
	 */
	@Override
	public List<FindReplica> find(String microbatchId, boolean checkNeighbors, boolean checkBuddies,
			EdgeInfoData selfInfo) throws TException {
		LOGGER.info("MicrobatchId : " + microbatchId + ", find, startTime=" +
				System.currentTimeMillis());
		List<FindReplica> replicas = new ArrayList<>();
		if (fog.getMbIDLocationMap().containsKey(microbatchId)) {
			// Short edgeId = fog.getMbIDLocationMap().get(microbatchId);
			NodeInfoData nodeInfo = new NodeInfoData(fog.getMyFogInfo().getNodeID(), fog.getMyFogInfo().getNodeIP(),
					fog.getMyFogInfo().getPort());
			Map<Short, Byte> edgeMap = fog.getMbIDLocationMap().get(microbatchId);
			if (edgeMap != null) {
				for (Short edgeId : edgeMap.keySet()) {
					if (edgeId != null && fog.getLocalEdgesMap().containsKey(edgeId)
							&& fog.getLocalEdgesMap().get(edgeId).getStatus().equals("A")) {
						FindReplica localReplica = new FindReplica();
						localReplica.setNode(nodeInfo);
						replicas.add(localReplica);
						if (selfInfo != null && fog.getLocalEdgesMap().containsKey(selfInfo.getNodeId())) {
							EdgeInfo edgeInfo = fog.getLocalEdgesMap().get(edgeId);
							if (edgeInfo != null) {
								// dummy values of reliability and storage are passed
								EdgeInfoData edgeInfoData = new EdgeInfoData(edgeInfo.getNodeId(), edgeInfo.getNodeIp(),
										edgeInfo.getPort(), (byte) 0, (byte) 0);
								localReplica.setEdgeInfo(edgeInfoData);
							}
						} else {
							// this else means that either the client is not an edge or its an edge
							// but not reporting to this Fog, so there is no point in giving it the
							// local edge information since the client cannot talk to the edge directly
							break;
						}
					}
				}
			}
		}
		
		if(checkNeighbors) {
			List<FindReplica> nReplicas = getFromNeighbors(Constants.MICROBATCH_METADATA_ID, 
					microbatchId, selfInfo);
			if(nReplicas != null) {
				replicas.addAll(nReplicas);
			}
		}
		
		if(checkBuddies) {
			List<FindReplica> bReplicas = getFromBuddies(Constants.MICROBATCH_METADATA_ID, 
					microbatchId, selfInfo);
			if(bReplicas != null) {
				replicas.addAll(bReplicas);
			}
		}
		LOGGER.info("MicrobatchId : " + microbatchId + ", find, endTime=" +
				System.currentTimeMillis());
		return replicas;
	}

	@Override
	public ReadReplica read(String microbatchId, boolean fetchMetadata) throws TException {
		ReadReplica data = new ReadReplica();
		data.setStatus(Constants.FAILURE);
		
		LOGGER.info("MicrobatchId : " + microbatchId + ", read, startTime=" +
				System.currentTimeMillis());
		
		//Short edgeId = fog.getMbIDLocationMap().get(microbatchId);
		
		//since now we may have multiple edges on a single Fog holding the
		//copy of a single microbatch, don't fail-fast in case the request
		//is not served at a single edge or the edge contacted is down. Try
		//till either a success is returned or all edges have been tried.
		boolean readData = false;
		Map<Short, Byte> edgeMap = fog.getMbIDLocationMap().get(microbatchId);
		if (edgeMap != null) {
			for (Short edgeId : edgeMap.keySet()) {
				if (readData) {
					break;
				}
				if (edgeId != null) {
					EdgeInfo edgeInfo = fog.getLocalEdgesMap().get(edgeId);
					if (edgeInfo != null && edgeInfo.getStatus().equals("A")) {
						TTransport transport = new TFramedTransport(
								new TSocket(edgeInfo.getNodeIp(), edgeInfo.getPort()));
						try {
							transport.open();
						} catch (TTransportException e) {
							transport.close();
							LOGGER.info("Unable to contact edge device : " + edgeInfo);
							e.printStackTrace();
							LOGGER.info("MicrobatchId : " + microbatchId + ", read, endTime="
									+ System.currentTimeMillis() + ",status=0");
							continue;
						}
						TProtocol protocol = new TBinaryProtocol(transport);
						EdgeService.Client edgeClient = new EdgeService.Client(protocol);
						try {
							if (fetchMetadata) {
								data = edgeClient.read(microbatchId, (byte) 1);
							} else {
								data = edgeClient.read(microbatchId, (byte) 0);
							}
							readData = true;
						} catch (TException e) {
							LOGGER.info("Error while reading microbatch from edge : " + edgeInfo);
							e.printStackTrace();
						} finally {
							transport.close();
						}
					}
				}
			}
		}
		LOGGER.info("MicrobatchId : " + microbatchId + ", read, endTime=" +
				System.currentTimeMillis() + ",status=" + data.getStatus());
		data.setStatus(Constants.SUCCESS);
		return data;
	}
	
	private List<FindReplica> getFromNeighbors(String searchKey, String searchValue, EdgeInfoData selfInfo) {
		List<FindReplica> replicas = new ArrayList<>();
		Map<Short, FogExchangeInfo> neighborExchangeInfo = fog.getNeighborExchangeInfo();
		for (Entry<Short, FogExchangeInfo> entry : neighborExchangeInfo.entrySet()) {
			FogExchangeInfo nInfo = entry.getValue();
			if (nInfo != null) {
				byte[] bloomFilter = nInfo.getBloomFilterUpdates();
				if (BloomFilter.search(searchKey, searchValue, bloomFilter)) {
					// match with BloomFilter, now contact the node to see if data present or not
					NeighborInfo neighbor = fog.getNeighborsMap().get(entry.getKey());
					List<FindReplica> nReplicas = fetchDataFromOtherFog(neighbor.getNode().getNodeIP(),
							neighbor.getNode().getPort(), searchValue, false, false, selfInfo);
					if (nReplicas != null) {
						replicas.addAll(nReplicas);
					}
				}
			}
		}
		return replicas;
	}

	private List<FindReplica> getFromBuddies(String searchKey, String searchValue, 
			EdgeInfoData selfInfo) {
		List<FindReplica> replicas = new ArrayList<>();
		Map<Short, FogExchangeInfo> buddyExchangeInfo = fog.getBuddyExchangeInfo();
		for (Entry<Short, FogExchangeInfo> entry : buddyExchangeInfo.entrySet()) {
			FogExchangeInfo buddyInfo = entry.getValue();
			if (buddyInfo != null) {
				byte[] consolidateBFilter = buddyInfo.getBloomFilterUpdates();
				if (BloomFilter.search(searchKey, searchValue, consolidateBFilter)) {
					// match with BloomFilter, now contact the node to see if data present or not
					FogInfo buddy = fog.getBuddyMap().get(entry.getKey());
					List<FindReplica> bReplicas = fetchDataFromOtherFog(buddy.getNodeIP(), buddy.getPort(), 
							searchValue, true, false, selfInfo);
					if (bReplicas != null) {
						replicas.addAll(bReplicas);
					}
				}
			}
		}
		return replicas;
	}

	private List<FindReplica> fetchDataFromOtherFog(String ip, int port, String searchValue, 
			boolean checkNeighbors, boolean checkBuddies, EdgeInfoData edgeInfo) {
		List<FindReplica> replicas = new ArrayList<>();
		TTransport transport = new TFramedTransport(new TSocket(ip, port));
		try {
			transport.open();
		} catch (TTransportException e) {
			transport.close();
			LOGGER.error("Error while fetching information from other fogs");
			e.printStackTrace();
			return replicas;
		}
		TProtocol protocol = new TBinaryProtocol(transport);
		FogService.Client fogClient = new FogService.Client(protocol);
		try {
			List<FindReplica> rList = fogClient.find(searchValue, checkNeighbors, checkBuddies, edgeInfo); 
			if(rList != null)
				replicas.addAll(rList);
		} catch (TException e) {
			LOGGER.error("Error in finding replicas from neighbor " + ip);
			e.printStackTrace();
		} finally {
			transport.close();
		}
		return replicas;
	}
	
	@Override
	public ReadReplica getMeta(String microbatchId, boolean checkNeighbors, boolean checkBuddies) 
			throws TException {
		LOGGER.info("MicrobatchId : " + microbatchId + ", getMeta, startTime=" +
				System.currentTimeMillis());
		ReadReplica replica = new ReadReplica();
		replica.setStatus(Constants.FAILURE);
		if (fog.getMbIDLocationMap().containsKey(microbatchId)) {
			// Short edgeId = fog.getMbIDLocationMap().get(microbatchId);
			Map<Short, Byte> edgeMap = fog.getMbIDLocationMap().get(microbatchId);
			if (edgeMap != null) {
				for (Short edgeId : edgeMap.keySet()) {
					if (edgeId != null && fog.getLocalEdgesMap().containsKey(edgeId)
							&& fog.getLocalEdgesMap().get(edgeId).getStatus().equals("A")) {
						boolean noError = true;
						EdgeInfo edgeInfo = fog.getLocalEdgesMap().get(edgeId);
						TTransport transport = new TFramedTransport(
								new TSocket(edgeInfo.getNodeIp(), edgeInfo.getPort()));
						try {
							transport.open();
						} catch (TTransportException e) {
							transport.close();
							LOGGER.info("Unable to contact edge device : " + edgeInfo);
							e.printStackTrace();
							noError = false;
						}

						if (noError) {
							TProtocol protocol = new TBinaryProtocol(transport);
							EdgeService.Client edgeClient = new EdgeService.Client(protocol);
							try {
								replica = edgeClient.getMetadata(microbatchId);
							} catch (TException e) {
								LOGGER.info("Error while fetching metadata of microbatch {} from edge : {} ",
										microbatchId, edgeInfo);
								e.printStackTrace();
								noError = false;
							} finally {
								transport.close();
							}
						}
						if (noError) {
							LOGGER.info("MicrobatchId : " + microbatchId + ", getMeta, endTime="
									+ System.currentTimeMillis());
							return replica;
						}
					}
				}
			}
		}
		
		if(checkNeighbors) {
			replica = getMetadataFromNeighbors(Constants.MICROBATCH_METADATA_ID, microbatchId);
			if(replica.getStatus() == Constants.SUCCESS) {
				LOGGER.info("MicrobatchId : " + microbatchId + ", getMeta, endTime=" +
						System.currentTimeMillis());
				return replica;
			}
		}
		
		if(checkBuddies) {
			replica = getMetadataFromBuddies(Constants.MICROBATCH_METADATA_ID, microbatchId);
		}
		
		LOGGER.info("MicrobatchId : " + microbatchId + ", getMeta, endTime=" +
				System.currentTimeMillis());
		return replica;
	}
	
	private ReadReplica getMetadataFromNeighbors(String searchKey, String searchValue) {
		ReadReplica replica = new ReadReplica();
		replica.setStatus(Constants.FAILURE);
		Map<Short, FogExchangeInfo> neighborExchangeInfo = fog.getNeighborExchangeInfo();
		for (Entry<Short, FogExchangeInfo> entry : neighborExchangeInfo.entrySet()) {
			FogExchangeInfo nInfo = entry.getValue();
			if (nInfo != null) {
				byte[] bloomFilter = nInfo.getBloomFilterUpdates();
				if (BloomFilter.search(searchKey, searchValue, bloomFilter)) {
					// match with BloomFilter, now contact the node to see if data present or not
					NeighborInfo neighbor = fog.getNeighborsMap().get(entry.getKey());
					replica = fetchMetadataFromOtherFog(neighbor.getNode().getNodeIP(), 
							neighbor.getNode().getPort(), searchValue, false, false);
					if(replica.getStatus() == Constants.SUCCESS)
						return replica;
				}
			}
		}
		return replica;
	}

	private ReadReplica getMetadataFromBuddies(String searchKey, String searchValue) {
		ReadReplica replica = new ReadReplica();
		replica.setStatus(Constants.FAILURE);
		Map<Short, FogExchangeInfo> buddyExchangeInfo = fog.getBuddyExchangeInfo();
		for (Entry<Short, FogExchangeInfo> entry : buddyExchangeInfo.entrySet()) {
			FogExchangeInfo buddyInfo = entry.getValue();
			if (buddyInfo != null) {
				byte[] consolidateBFilter = buddyInfo.getBloomFilterUpdates();
				if (BloomFilter.search(searchKey, searchValue, consolidateBFilter)) {
					// match with BloomFilter, now contact the node to see if data present or not
					FogInfo buddy = fog.getBuddyMap().get(entry.getKey());
					replica = fetchMetadataFromOtherFog(buddy.getNodeIP(), buddy.getPort(), 
							searchValue, true, false);
					if(replica.getStatus() == Constants.SUCCESS)
						return replica;
				}
			}
		}
		return replica;
	}
	
	private ReadReplica fetchMetadataFromOtherFog(String ip, int port, String searchValue, 
			boolean checkNeighbors, boolean checkBuddies) {
		ReadReplica replica = new ReadReplica();
		replica.setStatus(Constants.FAILURE);
		TTransport transport = new TFramedTransport(new TSocket(ip, port));
		try {
			transport.open();
		} catch (TTransportException e) {
			transport.close();
			LOGGER.error("Error while fetching metadata information from other fog");
			e.printStackTrace();
			return replica;
		}
		TProtocol protocol = new TBinaryProtocol(transport);
		FogService.Client fogClient = new FogService.Client(protocol);
		try {
			replica = fogClient.getMeta(searchValue, checkNeighbors, checkBuddies); 
		} catch (TException e) {
			LOGGER.error("Error in finding replicas from neighbor " + ip);
			e.printStackTrace();
		} finally {
			transport.close();
		}
		return replica;
	}

	
	@Override
	public QueryReplica findUsingQuery(String metadataKey, String metadataValue, boolean checkNeighbors,
			boolean checkBuddies) throws TException {
		QueryReplica response = new QueryReplica();
		response.setMatchingNodes(new HashMap<>());
		Map<String, List<NodeInfoData>> matchingNodes = response.getMatchingNodes();
		String searchKey = metadataKey + ":" + metadataValue;
		List<String> microBatchIdList = fog.getMetaMbIdMap().get(searchKey);
		if(microBatchIdList != null) {
			NodeInfoData nodeInfo = new NodeInfoData(fog.getMyFogInfo().getNodeID(), 
					fog.getMyFogInfo().getNodeIP(), fog.getMyFogInfo().getPort());
			for(String mbId : microBatchIdList) {
				matchingNodes.put(mbId, new ArrayList<>());
				matchingNodes.get(mbId).add(nodeInfo);
			}
		}
		
		if(checkNeighbors) {
			getMatchListFromNeighbors(metadataKey, metadataValue, response);
		}
		
		if(checkBuddies) {
			getMatchListFromBuddies(metadataKey, metadataValue, response);
		}
		return response;
	}

	private void getMatchListFromNeighbors(String searchKey, String searchValue, 
			QueryReplica currentState) {
		Map<String, List<NodeInfoData>> matchingNodes = currentState.getMatchingNodes();
		Map<Short, FogExchangeInfo> neighborExchangeInfo = fog.getNeighborExchangeInfo();
		for (Entry<Short, FogExchangeInfo> entry : neighborExchangeInfo.entrySet()) {
			FogExchangeInfo nInfo = entry.getValue();
			if (nInfo != null) {
				byte[] bloomFilter = nInfo.getBloomFilterUpdates();
				if (BloomFilter.search(searchKey, searchValue, bloomFilter)) {
					// match with BloomFilter, now contact the node to see if data present or not
					NeighborInfo neighbor = fog.getNeighborsMap().get(entry.getKey());
					QueryReplica nReplica = fetchDataListFromOtherFog(neighbor.getNode().getNodeIP(),
							neighbor.getNode().getPort(), searchKey, searchValue, false, false);
					if(nReplica != null) {
						Map<String, List<NodeInfoData>> nMatchingNodes = nReplica.getMatchingNodes();
						for(String mbId : nMatchingNodes.keySet()) {
							if(matchingNodes.containsKey(mbId)) {
								matchingNodes.get(mbId).addAll(nMatchingNodes.get(mbId));
							} else {
								matchingNodes.put(mbId, nMatchingNodes.get(mbId));
							}
						}
					}
				}
			}
		}
	}

	private void getMatchListFromBuddies(String searchKey, String searchValue,
			QueryReplica currentState) {
		Map<String, List<NodeInfoData>> matchingNodes = currentState.getMatchingNodes();
		Map<Short, FogExchangeInfo> buddyExchangeInfo = fog.getBuddyExchangeInfo();
		for (Entry<Short, FogExchangeInfo> entry : buddyExchangeInfo.entrySet()) {
			FogExchangeInfo buddyInfo = entry.getValue();
			if (buddyInfo != null) {
				byte[] consolidateBFilter = buddyInfo.getBloomFilterUpdates();
				if (BloomFilter.search(searchKey, searchValue, consolidateBFilter)) {
					// match with BloomFilter, now contact the node to see if data present or not
					FogInfo buddy = fog.getBuddyMap().get(entry.getKey());
					QueryReplica bReplica = fetchDataListFromOtherFog(buddy.getNodeIP(), buddy.getPort(), 
							searchKey, searchValue, true, false);
					if (bReplica != null) {
						Map<String, List<NodeInfoData>> bMatchingNodes = bReplica.getMatchingNodes();
						for(String mbId : bMatchingNodes.keySet()) {
							if(matchingNodes.containsKey(mbId)) {
								matchingNodes.get(mbId).addAll(bMatchingNodes.get(mbId));
							} else {
								matchingNodes.put(mbId, bMatchingNodes.get(mbId));
							}
						}
					}
				}
			}
		}
	}

	private QueryReplica fetchDataListFromOtherFog(String ip, int port, String searchKey, 
			String searchValue, boolean checkNeighbors, boolean checkBuddies) {
		QueryReplica replica = new QueryReplica();
		replica.setMatchingNodes(new HashMap<>());
		TTransport transport = new TFramedTransport(new TSocket(ip, port));
		try {
			transport.open();
		} catch (TTransportException e) {
			transport.close();
			LOGGER.error("Error while connecting to Fog ip : " + ip, e);
			e.printStackTrace();
			return replica;
		}
		TProtocol protocol = new TBinaryProtocol(transport);
		FogService.Client fogClient = new FogService.Client(protocol);
		try {
			replica = fogClient.findUsingQuery(searchKey, searchValue, checkNeighbors, checkBuddies);
		} catch (TException e) {
			LOGGER.error("Error while querying data from Fog ip : " + ip, e);
			e.printStackTrace();
		} finally {
			transport.close();
		}
		return replica;
	}

	/**
	 * Find all the micro batch ids whose metadata matches the given metadata query
	 * the call is returned to the client
	 */
	@Override
	public ByteBuffer findNext(String microbatchId) throws TException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * this may not be needed now, see comment in {@link #getWriteLocations method}
	 * Update the arguments required with additional edgeId and call the method
	 * updateMicrobatchLocalInfo to update metadata
	 * updateMicrobatchLocalInfo(Metadata mbMetadata, EdgeInfo edgeInfo)
	 * TODO:: This is invoked by the Edge client in case of a local write, this 
	 * client should calculate the MD5 checksum and pass that checksum in the metadata 
	 */
	@Override
	public byte insertMetadata(Metadata mbMetadata, EdgeInfoData edgeInfoData) throws TException {
		LOGGER.info("MicrobatchId : " + mbMetadata.getMbId() + ", insertMetadata, startTime=" +
				System.currentTimeMillis());
		EdgeInfo edgeInfo = new EdgeInfo();
		edgeInfo.setNodeId(edgeInfoData.getNodeId());
		
		LOGGER.info(" updated here by Sheshadri ");
		/**This is the method which the client should call if it writes to the edge by itself **/
		//we don't have the data in this call, so the client should send
		//the MD5 checksum as part of the Metadata itself
		if(!mbMetadata.isSetChecksum()) {
			//throw error or do something to make the client know of the error
		}
		updateMicrobatchLocalInfo(mbMetadata, null, edgeInfo);
		
		//this call comes from a local write and once it completes, we can remove
		//this mbId from the localWritesInProgess map which is a way to prevent
		//duplicate writes of a microbatch to the same edge
		fog.getLocalEdgeWritesInProgress().remove(mbMetadata.getMbId());
		
		LOGGER.info("MicrobatchId : " + mbMetadata.getMbId() + ", insertMetadata, endTime=" +
				System.currentTimeMillis());
		return Constants.SUCCESS;
	}
	

	/**
	 * the blackListedFogs will be null on initial client request but in case some
	 * returned Fog is not able to serve the request, then client again requests
	 * some Fog with the id of that Fog so that it is not returned again
	 */
	@Override
	/*public List<WritableFogData> getWriteLocations(byte dataLength, Metadata metadata,
			List<Short> blackListedFogs, EdgeInfoData clientEdgeInfo) throws TException {*/
	public List<WritableFogData> getWriteLocations(byte dataLength, Metadata metadata,
			List<Short> blackListedFogs, boolean isEdge) throws TException {
		// WritableFogData contains the information of node to contact, the type of the
		// edge
		// device such as HHL,HLH, etc(WritePreference), the reliability of the node
		// returned
		// which will be minReliability in case **L and medianReliability in case of **H
		// so that
		// client can request another Fog in case that Fog was not able to server the
		// request
		
		//first thing to do is to find the StreamMetadata for the incoming request
		//via the streamId present in the metadata. Each Fog has a stream bloomfilter
		//as well which maintains the information about which streams got registered
		//with it. This bloomfilter is also propagated in a similar way as the other
		//bloomfilter. StreamMetadata is necessary as we want to know the reliability
		//and max and min replica which is provided by the client at the stream level
		LOGGER.info("Fetching the locations for write operation for microbatchId : " + metadata.getMbId());
		LOGGER.info("MicrobatchId : " + metadata.getMbId() + ", getWriteLocations, startTime=" +
		System.currentTimeMillis());
		StreamMetadataInfo strMetadata = getStreamMetadata(metadata.getStreamId(), true, true);
		if(strMetadata == null) {
			LOGGER.info("Unable to locate the stream metadata for streamId : " + metadata.getStreamId());
			return null;
		}
		//stream metadata not null, lets cache for future requests
		fog.getStreamMetadata().put(metadata.getStreamId(), strMetadata);
		
		long decodedLength = Constants.interpretByteAsLong(dataLength);
//		double expectedReliability = strMetadata.getReliability();
		double expectedReliability = strMetadata.getStreamMetadata().getReliability().getValue();
		int minReplica = strMetadata.getStreamMetadata().getMinReplica().getValue();
		int maxReplica = strMetadata.getStreamMetadata().getMaxReplica().getValue();
		LOGGER.info("MicrobatchId : " + metadata.getMbId() + ", identifyReplicas, startTime=" +
				System.currentTimeMillis());
//		List<WritableFogData> fogLocations = identifyReplicas(decodedLength, clientEdgeInfo,expectedReliability, strMetadata.getMinReplica(), strMetadata.getMaxReplica());
		List<WritableFogData> fogLocations = identifyReplicas(metadata.getMbId(), decodedLength, isEdge,expectedReliability, minReplica, maxReplica);
		LOGGER.info("MicrobatchId : " + metadata.getMbId() + ", identifyReplicas, endTime=" +
				System.currentTimeMillis());
		
		LOGGER.info("MicrobatchId : " + metadata.getMbId() + ", getWriteLocations, endTime=" +
				System.currentTimeMillis());

		return fogLocations;
	}
	
	@Override
	public StreamMetadataInfo getStreamMetadata(String streamId, boolean checkNeighbors, boolean checkBuddies)
			throws TException {

		StreamMetadataInfo metadata = null;
		if (fog.getStreamMetadata().containsKey(streamId))
			return fog.getStreamMetadata().get(streamId);
		if (checkNeighbors) {
			metadata = getStreamFromNeighbors(Constants.STREAM_METADATA_ID, streamId);
			if(metadata != null)
				return metadata;
		}

		if (checkBuddies) {
			metadata = getStreamFromBuddies(Constants.STREAM_METADATA_ID, streamId);
			if(metadata != null)
				return metadata;
		}

		return metadata;
	}

	private StreamMetadataInfo getStreamFromNeighbors(String searchKey, String searchValue) {
		Map<Short, FogExchangeInfo> neighborExchangeInfo = fog.getNeighborExchangeInfo();
		for (Entry<Short, FogExchangeInfo> entry : neighborExchangeInfo.entrySet()) {
			FogExchangeInfo nInfo = entry.getValue();
			if (nInfo != null) {
				byte[] bloomFilter = nInfo.getStreamBFilterUpdates();
				if (BloomFilter.search(searchKey, searchValue, bloomFilter)) {
					// match with BloomFilter, now contact the node to see if data present or not
					NeighborInfo neighbor = fog.getNeighborsMap().get(entry.getKey());
					StreamMetadataInfo metadata = fetchStreamFromOtherFog(neighbor.getNode().getNodeIP(),
							neighbor.getNode().getPort(), searchKey, searchValue, false, false);
					if (metadata != null)
						return metadata;
				}
			}
		}
		return null;
	}

	private StreamMetadataInfo getStreamFromBuddies(String searchKey, String searchValue) {
		Map<Short, FogExchangeInfo> buddyExchangeInfo = fog.getBuddyExchangeInfo();
		for (Entry<Short, FogExchangeInfo> entry : buddyExchangeInfo.entrySet()) {
			FogExchangeInfo buddyInfo = entry.getValue();
			if (buddyInfo != null) {
				byte[] consolidateBFilter = buddyInfo.getStreamBFilterUpdates();
				if (BloomFilter.search(searchKey, searchValue, consolidateBFilter)) {
					// match with BloomFilter, now contact the node to see if data present or not
					FogInfo buddy = fog.getBuddyMap().get(entry.getKey());
					StreamMetadataInfo metadata = fetchStreamFromOtherFog(buddy.getNodeIP(), buddy.getPort(), 
							searchKey, searchValue, true, false);
					if (metadata != null) {
						return metadata;
					}
				}
			}
		}
		return null;
	}
	
	private StreamMetadataInfo fetchStreamFromOtherFog(String nodeIP, int port, String searchKey,
			String searchValue, boolean checkNeighbors, boolean checkBuddies) {
		StreamMetadataInfo metadata = null;
		TTransport transport = new TFramedTransport(new TSocket(nodeIP, port));
		try {
			transport.open();
		} catch (TTransportException e) {
			transport.close();
			e.printStackTrace();
			return metadata;
		}
		TProtocol protocol = new TBinaryProtocol(transport);
		FogService.Client fogClient = new FogService.Client(protocol);
		try {
			metadata = fogClient.getStreamMetadata(searchValue, checkNeighbors, checkBuddies);
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			transport.close();
		}
		return metadata;
	}


	@Override
//	public byte write(Metadata mbMetadata, ByteBuffer data, WritePreference preference) throws TException {
	public WriteResponse write(Metadata mbMetadata, ByteBuffer data, WritePreference preference) throws TException {
		// select a local edge based on the preference given
		// become a client with that edge server and send the write request
		// if successful, the persist the metadata and update the various maps
		// and return true else return false
		
		WriteResponse wrResponse = new WriteResponse();
		wrResponse.setStatus(Constants.FAILURE);
		//it may happen that multiple copies of the same microbatch 
		//will be written to the edges within a single Fog, so we need
		//to make sure that we should not pick the same edge again
		LOGGER.info("MicrobatchId : " + mbMetadata.getMbId() + ", write, startTime=" +
				System.currentTimeMillis());
		//Short duplicateHolderEdgeId = fog.getMbIDLocationMap().get(mbMetadata.getMbId());
		Map<Short, Byte> duplicateHolders = fog.getMbIDLocationMap().get(mbMetadata.getMbId());
		//we pass the duplicateHolders while identifying local replica because it might
		//happen that during recovery, we may choose an edge that already has the microbatch
		//present so we should pick a different edge to make sure there is proper replication
		EdgeInfo localEdge = identifyLocalReplica(data.capacity(), preference, duplicateHolders,
				mbMetadata.getMbId());
		if(localEdge == null) {
			LOGGER.info("No suitable edge present");
			return wrResponse;
		}
		//the microbatchId is contained within the metadata so check for safety
		if(mbMetadata == null) {
			LOGGER.error("No metadata supplied while writing");
			return wrResponse;
		}
		TTransport transport = new TFramedTransport(new TSocket(localEdge.getNodeIp(), localEdge.getPort()));
		try {
			transport.open();
		} catch (TTransportException e) {
			transport.close();
			LOGGER.error("Unable to contact edge device : " + localEdge);
			e.printStackTrace();
			return wrResponse;
		}
		
		TProtocol protocol = new TBinaryProtocol(transport);
		EdgeService.Client edgeClient = new EdgeService.Client(protocol);
		try {
			wrResponse = edgeClient.write(mbMetadata.getMbId(), mbMetadata, data);
		} catch (TException e) {
			LOGGER.info("Error while writing microbatch to edge : " + localEdge);
			e.printStackTrace();
			return wrResponse;
		} finally {
			transport.close();
		}
		updateMicrobatchLocalInfo(mbMetadata, data, localEdge);
		LOGGER.info("MicrobatchId : " + mbMetadata.getMbId() + ", write, endTime=" +
				System.currentTimeMillis());
		//make sure that edge reliability is set correctly when the various edges
		//are started as we are returning the WriteResponse returned directly 
		//from the Edge
		return wrResponse;
	}
	
	private void updateMicrobatchLocalInfo(Metadata mbMetadata, ByteBuffer data, EdgeInfo edgeInfo) {
		//microbatch to edgeId mapping
		//fog.getMbIDLocationMap().put(mbMetadata.getMbId(), edgeInfo.getNodeId());
		edgeMicrobatchLock.lock();
		if(!fog.getMbIDLocationMap().containsKey(mbMetadata.getMbId())) {
			fog.getMbIDLocationMap().put(mbMetadata.getMbId(), new ConcurrentHashMap<>());
		}
		edgeMicrobatchLock.unlock();
		Map<Short, Byte> edgeMap = fog.getMbIDLocationMap().get(mbMetadata.getMbId());
		//value in this map is some dummy value
		edgeMap.put(edgeInfo.getNodeId(), (byte) 1);
		
		//streamId to set of microbatchId mapping
		//CONCURRENT WRITES:: What happens when multiple clients are issuing write
		//requests to a single Fog and if mbSet was null (which is not since we 
		//initialized it during registerStream), then there comes the possibility of
		//concurrent writers finding the set to be null and making their change and 
		//then putting the changed set back on the map. This will cause lost updates
		//as only the last write will be visible to the subsequent writers.
		//If writers only make their change in the set and no put the set again on 
		//the map, that should be sufficient as the reference is modified and no 
		//as the reference is modified and no updates to the set will be missed
		Set<String> mbSet = fog.getStreamMbIdMap().get(mbMetadata.getStreamId());
		if(mbSet == null) {
			//notice concurrent modification can come here but currently
			//we are nowhere using an iterator for scanning it
			mbSet = new HashSet<>();
		}
		mbSet.add(mbMetadata.getMbId());
		//commented due to above CONCURRENT WRITES comment
		//fog.getStreamMbIdMap().put(mbMetadata.getStreamId(), mbSet);
		
		//edge to list of microbatchId for recovery purposes
		//the null case will again not come as we have initialized it on edgeJoin()
		Set<String> edgeMBList = fog.getEdgeMicrobatchMap().get(edgeInfo.getNodeId());
		if(edgeMBList == null) {
			edgeMBList = new HashSet<>();
		}
		edgeMBList.add(mbMetadata.getMbId());
		//commented due to above CONCURRENT WRITES comment
		//fog.getEdgeMicrobatchMap().put(edgeInfo.getNodeId(), edgeMBList);
		
		//metadata 'key:value' to the microbatchId map
		//may need to check for the concurrent writes scenario
		updateMetadataMap(mbMetadata, edgeInfo);
		
		//update the edge bloomfilter as well as fog's personal bloomfilter
		//may need to check for the concurrent writes scenario
		updateBloomFilters(mbMetadata, edgeInfo);
		
		//add the MD5 checksum for the block of data as well
		//Note:: This call can come from the write() or insertMetadata()
		//The case of write is fine as it has the data from which the checksum can
		//be computed however insertMetadata() doesn't have the data as it comes
		//from a local edge and makes a separate metadata call. This edge client
		//should compute the MD5 checksum on its own and send it as a field in the
		//Metadata object passed
		if(data != null) {
			computeMD5Checksum(data, mbMetadata);
		}
		
		//put the Metadata object into the map, this is a new addition
		fog.getBlockMetadata().put(mbMetadata.getMbId(), mbMetadata);
		
		//update which stream the microbatch belongs to
		fog.getMicroBatchToStream().put(mbMetadata.getMbId(), mbMetadata.getStreamId());
		
		fog.setMostRecentSelfBFUpdate(System.currentTimeMillis());
		fog.setMostRecentNeighborBFUpdate(System.currentTimeMillis());
	}

	private void computeMD5Checksum(ByteBuffer data, Metadata mbMetadata) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] arr = data.array();
			for (int i = 0; i < arr.length; i += 1024) {
				if (arr.length - i <= 1023) {
					md.update(arr, i, arr.length - i);
				} else {
					md.update(arr, i, 1024);
				}
			}
			StringBuilder checkSum = new StringBuilder();
			for (byte b : md.digest()) {
				checkSum.append(String.format("%02x", b));
			}
			mbMetadata.setChecksum(checkSum.toString());
			LOGGER.info("The MD5 checksum for the block {} is : {}", mbMetadata.getMbId(), 
					mbMetadata.getChecksum());
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("The algorithm for messagedigest is not present");
			e.printStackTrace();
		}
	}
	
	//personal bloomfilter is sent to subscribers and also in consolidated
	//form to neighbors. Its aim is to purely facilitate microbatch searches
	//based on microbatch metadata (though streamId can also be used)
	private void updateBloomFilters(Metadata mbMetadata, EdgeInfo edgeInfo) {
		byte[] fogBFilter = fog.getPersonalBloomFilter();
		byte[] edgeBFilter = fog.getEdgeBloomFilters().get(edgeInfo.getNodeId());
		updateFogAndEdgeBloomFilters(Constants.MICROBATCH_METADATA_ID, mbMetadata.getMbId(),
				fogBFilter, edgeBFilter);
		updateFogAndEdgeBloomFilters(Constants.STREAM_METADATA_ID, mbMetadata.getStreamId(), 
				fogBFilter, edgeBFilter);
		updateFogAndEdgeBloomFilters(Constants.MICROBATCH_METADATA_TIMESTAMP, String.valueOf(
				mbMetadata.getTimestamp()), fogBFilter, edgeBFilter);
		String properties = mbMetadata.getProperties();
		if(properties != null) {
			//assuming properties is a map
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> otherProps = new HashMap<>();
			try {
				otherProps = mapper.readValue(properties, Map.class);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			for(Entry<String, Object> entry : otherProps.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if(key != null && value != null) {
					updateFogAndEdgeBloomFilters(key, String.valueOf(value), fogBFilter, edgeBFilter);
				}
			}
		}
	}
	
	private void updateFogAndEdgeBloomFilters(String key, String value, 
			byte[] fogBFilter, byte[] edgeBFilter) {
		BloomFilter.storeEntry(key, value, fogBFilter);
		BloomFilter.storeEntry(key, value, edgeBFilter);
	}

	private void updateMetadataMap(Metadata mbMetadata, EdgeInfo edgeInfo) {
		//for every key:value present in the mbMetadata, we need to store that
		//in the map to faciliate find
		//no need to insert mbId in this map as there is a separate map for it
		String searchKey = Constants.MICROBATCH_METADATA_TIMESTAMP + ":" + mbMetadata.getTimestamp();
		checkAndInsertEntry(searchKey, mbMetadata.getMbId());
		//no need to do for streamId as we have a separate map which provides the 
		//functionality to get list of microbatches given a streamId i.e. streamMbIdMap
		
		//LATEST: To support query using key value pair instead of a microbatchId, either
		//update the logic for findUsingQuery to check if the field being searched is present
		//in other local maintained data structures or make a generic method which supports 
		//every query serviced from here which is what the updated logic is doing now by adding
		//fields are present elsewhere as well
		searchKey = Constants.MICROBATCH_METADATA_ID + ":" + mbMetadata.getMbId();
		checkAndInsertEntry(searchKey, mbMetadata.getMbId());
		searchKey = Constants.STREAM_METADATA_ID + ":" + mbMetadata.getStreamId();
		checkAndInsertEntry(searchKey, mbMetadata.getMbId());
		
		String properties = mbMetadata.getProperties();
		if(properties != null) {
			//assuming properties is a map
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> otherProps = new HashMap<>();
			try {
				otherProps = mapper.readValue(properties, Map.class);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			for(Entry<String, Object> entry : otherProps.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if(key != null && value != null) {
					searchKey = key + ":" + String.valueOf(value);
					checkAndInsertEntry(searchKey, mbMetadata.getMbId());
				}
			}
		}
	}
	
	private void checkAndInsertEntry(String searchKey, String mbId) {
		Map<String, List<String>> metaToMBIdListMap = fog.getMetaToMBIdListMap();
		List<String> list = metaToMBIdListMap.get(searchKey);
		if(list == null)
			list = new ArrayList<>();
		list.add(mbId);
		metaToMBIdListMap.put(searchKey, list);
	}

	private EdgeInfo identifyLocalReplica(int dataLength, WritePreference preference,
			Map<Short, Byte> duplicateHolders, String mbId) {
		FogStats selfStats = FogStats.createInstance(fog.getCoarseGrainedStats().getInfo());
		EdgeInfo edgeInfo = null;
		if(preference == WritePreference.HHL || preference == WritePreference.HLL ||
				preference == WritePreference.LHL || preference == WritePreference.LLL) {
			edgeInfo = getLowReliabilityEdge(selfStats, dataLength, duplicateHolders, mbId);
		} else {
			edgeInfo = getHighReliabilityEdge(selfStats, dataLength, duplicateHolders, mbId);
		}
		return edgeInfo;
	}
	
	//assuming dataLength is in bytes
	private EdgeInfo getHighReliabilityEdge(FogStats selfStats, long dataLength,
			Map<Short, Byte> duplicateHolders, String mbId) {
		Map<Short, EdgeInfo> localEdges = fog.getLocalEdgesMap();
		Map<StorageReliability, List<Short>> localEdgeMapping = fog.getLocalEdgeMapping();
		Set<Short> visitedEdges = new HashSet<>();
		Random random = new Random();

		List<Short> list = localEdgeMapping.get(StorageReliability.HH);
		List<Short> iterList = null; // this will be used for iteration

		if (list != null && !list.isEmpty()) {
			if ((duplicateHolders == null || duplicateHolders.isEmpty()) && 
					!fog.getLocalEdgeWritesInProgress().containsKey(mbId)) {
				iterList = list;
			} else {
				iterList = new ArrayList<>(list);
				for(Short edgeId : duplicateHolders.keySet()) {
					if(list.contains(edgeId)) {
						iterList.remove((Object) edgeId);
					}
				}
				if(fog.getLocalEdgeWritesInProgress().containsKey(mbId)) {
					iterList.remove((Object) fog.getLocalEdgeWritesInProgress().get(mbId));
				}
			}
			
			int size = iterList.size();
			int visitedCount = 0;
			while (visitedCount < size) {
				int idx = random.nextInt(size);
				if (visitedEdges.contains(idx))
					continue;
				EdgeInfo edgeInfo = localEdges.get(iterList.get(idx));
				if (edgeInfo.getStatus().equals("A") && 
						edgeInfo.getStats().getStorage() >= Constants.DISK_WATERMARK &&
						edgeInfo.getStats().getStorage() > (dataLength/(1024*1024))) {
					return edgeInfo;
				} else {
					visitedEdges.add((short) idx);
					visitedCount += 1;
				}
			}
		}

		list = localEdgeMapping.get(StorageReliability.LH);
		if (list != null && !list.isEmpty()) {
			if ((duplicateHolders == null || duplicateHolders.isEmpty()) && 
					!fog.getLocalEdgeWritesInProgress().containsKey(mbId)) {
				iterList = list;
			} else {
				iterList = new ArrayList<>(list);
				for(Short edgeId : duplicateHolders.keySet()) {
					if(list.contains(edgeId)) {
						iterList.remove((Object) edgeId);
					}
				}
				if(fog.getLocalEdgeWritesInProgress().containsKey(mbId)) {
					iterList.remove((Object) fog.getLocalEdgeWritesInProgress().get(mbId));
				}
			}
			
			int size = iterList.size();
			int visitedCount = 0;
			while (visitedCount < size) {
				int idx = random.nextInt(size);
				if (visitedEdges.contains(idx))
					continue;
				EdgeInfo edgeInfo = localEdges.get(iterList.get(idx));
				if (edgeInfo.getStatus().equals("A") && 
						edgeInfo.getStats().getStorage() >= Constants.DISK_WATERMARK &&
						edgeInfo.getStats().getStorage() > (dataLength/(1024*1024))) {
					return edgeInfo;
				} else {
					visitedEdges.add((short) idx);
					visitedCount += 1;
				}
			}
		}
		return null;
	}

	//assuming dataLength is in bytes
	private EdgeInfo getLowReliabilityEdge(FogStats selfStats, long dataLength,
			Map<Short, Byte> duplicateHolders, String mbId) {
		Map<Short, EdgeInfo> localEdges = fog.getLocalEdgesMap();
		Map<StorageReliability, List<Short>> localEdgeMapping = fog.getLocalEdgeMapping();
		Set<Short> visitedEdges = new HashSet<>();
		Random random = new Random();

		List<Short> list = localEdgeMapping.get(StorageReliability.HL);
		List<Short> iterList = null;

		if (list != null && !list.isEmpty()) {
			if ((duplicateHolders == null || duplicateHolders.isEmpty()) && 
					!fog.getLocalEdgeWritesInProgress().containsKey(mbId)) {
				iterList = list;
			} else {
				iterList = new ArrayList<>(list);
				for(Short edgeId : duplicateHolders.keySet()) {
					if(list.contains(edgeId)) {
						iterList.remove((Object) edgeId);
					}
				}
				if(fog.getLocalEdgeWritesInProgress().containsKey(mbId)) {
					iterList.remove((Object) fog.getLocalEdgeWritesInProgress().get(mbId));
				}
			}
			
			int size = iterList.size();
			int visitedCount = 0;
			while (visitedCount < size) {
				int idx = random.nextInt(size);
				if (visitedEdges.contains(idx))
					continue;
				EdgeInfo edgeInfo = localEdges.get(iterList.get(idx));
				if (edgeInfo.getStatus().equals("A") && 
						edgeInfo.getStats().getStorage() >= Constants.DISK_WATERMARK && 
						edgeInfo.getStats().getStorage() > (dataLength/(1024*1024))) {
					return edgeInfo;
				} else {
					visitedEdges.add((short) idx);
					visitedCount += 1;
				}
			}
		}

		list = localEdgeMapping.get(StorageReliability.LL);
		if (list != null && !list.isEmpty()) {
			if ((duplicateHolders == null || duplicateHolders.isEmpty()) && 
					!fog.getLocalEdgeWritesInProgress().containsKey(mbId)) {
				iterList = list;
			} else {
				iterList = new ArrayList<>(list);
				for(Short edgeId : duplicateHolders.keySet()) {
					if(list.contains(edgeId)) {
						iterList.remove((Object) edgeId);
					}
				}
				if(fog.getLocalEdgeWritesInProgress().containsKey(mbId)) {
					iterList.remove((Object) fog.getLocalEdgeWritesInProgress().get(mbId));
				}
			}
			
			int size = iterList.size();
			int visitedCount = 0;
			while (visitedCount < size) {
				int idx = random.nextInt(size);
				if (visitedEdges.contains(idx))
					continue;
				EdgeInfo edgeInfo = localEdges.get(iterList.get(idx));
				if (edgeInfo.getStatus().equals("A") && 
						edgeInfo.getStats().getStorage() >= Constants.DISK_WATERMARK && 
						edgeInfo.getStats().getStorage() > (dataLength/(1024*1024))) {
					return edgeInfo;
				} else {
					visitedEdges.add((short) idx);
					visitedCount += 1;
				}
			}
		}
		return null;
	}
	/**
	 * Graceful exit, need to unregister the streamID
	 */
	@Override
	public byte edgeLeave(EdgeInfoData edgeInfoData) throws TException {
		// an edge leaving should trigger local stats calculation
		fog.setMostRecentEdgeUpdate(System.currentTimeMillis());
		return 0;
	}

	@Override
	public void neighborHeartBeat(NeighborPayload payload) throws TException {
		NeighborHeartbeatData data = NeighborDataExchangeFormat.decodeData(payload);
		long currentTime = System.currentTimeMillis();
		short nodeId = data.getNeighborInfo().getNode().getNodeID();
		LOGGER.info("Received heartbeat from neighbor : " + nodeId);

		if (!fog.getNeighborsMap().containsKey(nodeId)) {
			NeighborInfo nInfo = data.getNeighborInfo();
			fog.getNeighborsMap().put(nodeId, nInfo);
			fog.getNeighborExchangeInfo().put(nodeId, new FogExchangeInfo(nInfo.getNode()));
		}

		// make sure that every neighbor is present in the global stats map
		if (!fog.getFogUpdateMap().containsKey(nodeId)) {
			FogStats neighorStats = new FogStats();
			neighorStats.setNodeInfo(data.getNeighborInfo().getNode());
			fog.getFogUpdateMap().put(nodeId, neighorStats);
		}

		FogExchangeInfo nInfo = fog.getNeighborExchangeInfo().get(nodeId);
		nInfo.setLastHeartBeatTime(currentTime);
		if (data.getBloomFilterUpdates() != null) {
			nInfo.setLastUpdatedBFTime(currentTime);
			nInfo.setBloomFilterUpdates(data.getBloomFilterUpdates());
			fog.setMostRecentNeighborBFUpdate(System.currentTimeMillis());
		}
		
		if(data.getStreamBFilter() != null) {
			nInfo.setStreamBFilterUpdates(data.getStreamBFilter());
			fog.setMostRecentNeighborBFUpdate(System.currentTimeMillis());
		}

		if (data.getNeighborStats() != null) {
			nInfo.setLastUpdatedStatsTime(currentTime);
			fog.getFogUpdateMap().put(nodeId, data.getNeighborStats());
			fog.setMostRecentNeighborStatsUpdate(System.currentTimeMillis());
			fog.setMostRecentFogStatsUpdate(System.currentTimeMillis());
		}
	}

	@Override
	public void buddyHeartBeat(BuddyPayload payload) throws TException {
		BuddyHeartbeatData data = BuddyDataExchangeFormat.decodeData(payload);
		boolean anyStatsUpdate = false;
		NodeInfo buddyInfo = data.getNodeInfo();
		short buddyId = buddyInfo.getNodeID();
		LOGGER.info("Received heartbeat from buddy : " + buddyInfo);
		byte[] buddyStats = data.getBuddyStats();
		List<FogStats> buddyNeighborStats = data.getNeighborStats();
		byte[] consolidatedBloomFilter = data.getConsolidatedBloomFilter();
		byte[] consolidatedStreamBFilter = data.getStreamBloomFilter();
		long currentTime = System.currentTimeMillis();
		if (!fog.getBuddyMap().containsKey(buddyId)) {
			FogInfo fogInfo = new FogInfo(buddyInfo.getNodeIP(), buddyInfo.getNodeID(), buddyInfo.getPort(),
					buddyInfo.getBuddyPoolId());
			fog.getBuddyMap().put(buddyId, fogInfo);
			fog.getBuddyExchangeInfo().put(buddyId, new FogExchangeInfo(fogInfo));
		}
		FogExchangeInfo buddyExchangeInfo = fog.getBuddyExchangeInfo().get(buddyId);
		buddyExchangeInfo.setLastHeartBeatTime(currentTime);
		if (consolidatedBloomFilter != null) {
			buddyExchangeInfo.setLastUpdatedBFTime(currentTime);
			buddyExchangeInfo.setBloomFilterUpdates(consolidatedBloomFilter);
		}
		if(consolidatedStreamBFilter != null) {
			buddyExchangeInfo.setStreamBFilterUpdates(consolidatedStreamBFilter);
		}
		if (buddyStats != null) {
			buddyExchangeInfo.setLastUpdatedStatsTime(currentTime);
			FogStats stats = FogStats.createInstance(buddyStats, buddyInfo);
			fog.getFogUpdateMap().put(buddyId, stats);
			anyStatsUpdate = true;
		}
		if (buddyNeighborStats != null && !buddyNeighborStats.isEmpty()) {
			for (FogStats stats : buddyNeighborStats) {
				fog.getFogUpdateMap().put(stats.getNodeInfo().getNodeID(), stats);
			}
			anyStatsUpdate = true;
		}

		if (anyStatsUpdate) {
			fog.setMostRecentFogStatsUpdate(System.currentTimeMillis());
		}

	}

	@Override
	public byte serializeState() throws TException {
		LOGGER.info("The serialization started at {}", System.currentTimeMillis());
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(Constants.SERIALIZATION_FILE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(fog);
			oos.close();
			fos.close();
		} catch (IOException ex) {
			LOGGER.error("Error while serializing state");
			LOGGER.error("The error is ", ex);
			return Constants.FAILURE;
		}
		LOGGER.info("The serialization completed at {}", System.currentTimeMillis());
		return Constants.SUCCESS;
	}
	
}