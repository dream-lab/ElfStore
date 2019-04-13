package com.dreamlab.edgefs.controlplane;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamlab.edgefs.misc.Constants;
import com.dreamlab.edgefs.model.CheckerTask;
import com.dreamlab.edgefs.model.FogExchangeInfo;
import com.dreamlab.edgefs.model.FogInfo;
import com.dreamlab.edgefs.model.NeighborInfo;
import com.dreamlab.edgefs.model.NodeInfo;
import com.dreamlab.edgefs.servicehandler.FogServiceHandler;
import com.dreamlab.edgefs.thrift.FogInfoData;
import com.dreamlab.edgefs.thrift.FogService;
import com.dreamlab.edgefs.thrift.MessagePayload;
import com.dreamlab.edgefs.thrift.NeighborCount;
import com.dreamlab.edgefs.thrift.NeighborInfoData;
import com.dreamlab.edgefs.thrift.NodeInfoData;
import com.dreamlab.edgefs.thrift.TwoPhaseCommitResponse;

/**
 * TODO:: Why in the thrift definition, poolId is not part of NodeInfoData.
 * Include if it is essential
 *
 */
public class FogServer {

	public static FogServiceHandler fogHandler;
	public static FogService.Processor eventProcessor;
	public static String referrerFogIp = null;
	public static int referrerPort = 0;
	// setting for exponential backoff
	public static int SLEEP_TIME = 5000;
	public static int SLEEP_TIME_MAX = 50000;
	public static int MULTIPLICATION_FACTOR = 2;

	public static Properties properties = new Properties();

	public static Properties getProperties() {
		return properties;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(FogServer.class);

	public static void main(String[] args) {

		LOGGER.info("Reading properties file....");
		try {
			properties.load(FogServer.class.getResourceAsStream("/system.properties"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		String fogIp = args[0];
		int serverPort = Integer.parseInt(args[1]);
		short buddyPoolId = Short.parseShort(args[2]);
		short fogId = Short.parseShort(args[3]);
		float reliability = Float.parseFloat(args[4]);
		//this is a new argument added. This can be zero or nonzero
		//A zero indicates that we are starting fresh and no deserialization is needed
		//A nonzero means we need to deserialize the Fog state
		int restoreState = Integer.parseInt(args[5]);

		boolean newFog = false;
		
		//commenting the part when a Fog want to join a pool
		//currently serving through a static configuration only
/*
		// Assuming when the node wants to join a pool, the argument
		// for buddyPoolId is not used
		if (args.length > 5) { *//** This means a Fog is trying to join a Pool **//*
			referrerFogIp = args[5];
			referrerPort = Integer.parseInt(args[6]);
			newFog = true;
		}
*/
		/** set the kmax to 5 in the constants file **/
		FogHolder self = new FogHolder(new Fog(fogIp, fogId, serverPort, buddyPoolId, reliability));
		self.getFog().setkMax(Constants.KMAX);
		//set kMin as well if you want to as these fields will be transient
		//so in case we restart a Fog instance, we won't be able to recover them
		
		// set the disk watermark for Edge
		if (properties.containsKey(Constants.EDGE_DISK_WATERMARK)) {
			self.getFog().setEdgeDiskWatermark(Long.parseLong(properties.getProperty(Constants.EDGE_DISK_WATERMARK)));
		} else {
			// set some absolute constant as the watermark
			self.getFog().setEdgeDiskWatermark(Constants.CONSTANT_DISK_WATERMARK_EDGE);
		}

		try {
			fogHandler = new FogServiceHandler(self.getFog());
			eventProcessor = new FogService.Processor(fogHandler);

			Runnable fogRunnable = new Runnable() {

				@Override
				public void run() {
					bootstrapFog(eventProcessor, serverPort);
				}
			};
			
			Thread t1 = new Thread(fogRunnable);
			t1.start();
			
			if(restoreState == 0) {
				populateBuddiesAndNeighborsFromClusterConf(Constants.CONF_PATH, self.getFog());
			} else {
				self.setFog(Fog.deserializeInstance());
			}

			Runnable buddyHeartBeat = new Runnable() {

				@Override
				public void run() {
					int buddyHeartbeatTime = Integer
							.parseInt(properties.getProperty(Constants.BUDDY_HEARTBEAT_INTERVAL));
					int buddyBFHeartbeats = Integer
							.parseInt(properties.getProperty(Constants.BUDDY_BLOOM_SEND_HEARTBEATS));
					int buddyStatsHeartbeats = Integer
							.parseInt(properties.getProperty(Constants.BUDDY_STATS_SEND_HEARTBEATS));
					int bootstrapHeartbeats = Integer
							.parseInt(properties.getProperty(Constants.GLOBAL_BOOTSTRAP_HEARTBEATS));
					int buddyForceBFHeartbeats = Integer
							.parseInt(properties.getProperty(Constants.BUDDY_BLOOM_FORCE_SEND_HEARTBEATS));
					int buddyForceStatsHeartbeats = Integer
							.parseInt(properties.getProperty(Constants.BUDDY_STATS_FORCE_SEND_HEARTBEATS));
					long count = 0;
					// check if to send bloomfilter
					boolean bloomFilterSend = false, forceBFSend = false;
					// check if to send stats
					boolean statsSend = false, forceStatsSend = false;

					// allow for initial bootstrap
					// uncomment for deployment if needed
					/*
					 * try { Thread.sleep(bootstrapHeartbeats * buddyHeartbeatTime * 1000); } catch
					 * (InterruptedException e) { e.printStackTrace(); }
					 */
					while (true) {
						// this will enable initial bootstrapping as well as allow periodic heartbeats
						try {
							Thread.sleep(buddyHeartbeatTime * 1000);
							count += 1;
							if (count % buddyBFHeartbeats == 0) {
								bloomFilterSend = true;
							}
							if (count % buddyStatsHeartbeats == 0) {
								statsSend = true;
							}
							if (count % buddyForceBFHeartbeats == 0) {
								forceBFSend = true;
							}
							if (count % buddyForceStatsHeartbeats == 0) {
								forceStatsSend = true;
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						// LOGGER.info("Sending heartbeat to my buddy");
						self.getFog().sendHeartbeatBuddies(bloomFilterSend, forceBFSend, statsSend, forceStatsSend);
						bloomFilterSend = false;
						statsSend = false;
						forceBFSend = false;
						forceStatsSend = false;
					}
				}
			};

			// When we read the cluster.conf, we start sending subscribe
			// requests to other nodes. So once binding of a Fog node to
			// a port is done, wait for few seconds and then start sending
			// subscribe requests

			//again a portion which is related to joining an existing pool, commenting it
			/*if (((referrerFogIp == null) || (referrerFogIp.isEmpty()))) {
				populateBuddiesAndNeighborsFromClusterConf(Constants.CONF_PATH, self);
			} else {
				*//** NodeX is trying to join **//*
				LOGGER.info("New Node joining ");
			}*/
			
			if (newFog) {
				/** Attempt to join a pool **/
				String result = requestToJoinBuddyPool(self.getFog(), referrerFogIp, referrerPort);
				LOGGER.info("Joined Pool Successfully " + result);
			}

			Thread t2 = new Thread(buddyHeartBeat);
			t2.start();

			// A separate runnable for subscribed Fog devices
			Runnable subscriberHeartBeat = new Runnable() {

				@Override
				public void run() {
					int subscriberHeartbeatTime = Integer
							.parseInt(properties.getProperty(Constants.SUBS_HEARTBEAT_INTERVAL));
					int sendBFHeartbeats = Integer
							.parseInt(properties.getProperty(Constants.SUBS_BLOOM_SEND_HEARTBEATS));
					int sendStatsHeartbeats = Integer
							.parseInt(properties.getProperty(Constants.SUBS_STATS_SEND_HEARTBEATS));
					int bootstrapHeartbeats = Integer
							.parseInt(properties.getProperty(Constants.GLOBAL_BOOTSTRAP_HEARTBEATS));
					int forceSendBFHeartbeats = Integer
							.parseInt(properties.getProperty(Constants.SUBS_BLOOM_FORCE_SEND_HEARTBEATS));
					int forceStatsHeartbeats = Integer
							.parseInt(properties.getProperty(Constants.SUBS_STATS_FORCE_SEND_HEARTBEATS));
					long count = 0;
					// check if to send bloomfilter
					boolean bloomFilterSend = false, forceBFSend = false;
					// check if to send bloomfilter
					boolean statsSend = false, forceStatsSend = false;

					// allow for initial bootstrap
					// uncomment for deployment
					/*
					 * try { Thread.sleep(bootstrapHeartbeats * subscriberHeartbeatTime * 1000); }
					 * catch (InterruptedException e) { e.printStackTrace(); }
					 */
					while (true) {
						try {
							Thread.sleep(subscriberHeartbeatTime * 1000);
							count += 1;
							if (count % sendBFHeartbeats == 0) {
								bloomFilterSend = true;
							}
							if (count % sendStatsHeartbeats == 0) {
								statsSend = true;
							}
							if (count % forceSendBFHeartbeats == 0) {
								forceBFSend = true;
							}
							if (count % forceStatsHeartbeats == 0) {
								forceStatsSend = true;
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						self.getFog().sendHeartbeatSubscribers(bloomFilterSend, forceBFSend, 
								statsSend, forceStatsSend);
						bloomFilterSend = false;
						statsSend = false;
						forceBFSend = false;
						forceStatsSend = false;
					}
				}
			};

			Thread t3 = new Thread(subscriberHeartBeat);
			t3.start();

			// for calculating local stats
			Runnable localStatsCalculator = new Runnable() {

				@Override
				public void run() {
					int edgeHeartbeatInterval = Integer
							.parseInt(properties.getProperty(Constants.EDGE_HEARTBEAT_INTERVAL));
					int localUpdateHeartbeats = Integer
							.parseInt(properties.getProperty(Constants.LOCAL_STATS_CALC_HEARTBEATS));
					int localUpdateWindow = edgeHeartbeatInterval * localUpdateHeartbeats;
					int maxMissingHeartbeats = Integer
							.parseInt(properties.getProperty(Constants.EDGE_MISS_HEARTBEATS_MAX));
					while (true) {
						try {
							// assuming the supplied value is in seconds
							Thread.sleep(localUpdateWindow * 1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						// missing heartbeats should be updated for the Edge
						// should we do as part of local stats calculation or
						// do periodically via another thread
						self.getFog().updateMissingHeartBeats(edgeHeartbeatInterval * 1000, maxMissingHeartbeats);
						self.getFog().localStatsCalculate();
					}
				}
			};

			// for calculating global stats
			Runnable globalStatsCalculator = new Runnable() {

				@Override
				public void run() {
					int buddyHeartbeatTime = Integer
							.parseInt(properties.getProperty(Constants.BUDDY_HEARTBEAT_INTERVAL));
					int globalUpdateHeartbeats = Integer
							.parseInt(properties.getProperty(Constants.GLOBAL_CALC_HEARTBEATS));
					int globalUpdateWindow = buddyHeartbeatTime * globalUpdateHeartbeats;
					while (true) {
						try {
							// assuming the supplied value is in seconds
							Thread.sleep(globalUpdateWindow * 1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						self.getFog().globalStatsCalculate();
					}
				}
			};

			Thread t4 = new Thread(localStatsCalculator);
			t4.start();

			Thread t5 = new Thread(globalStatsCalculator);
			t5.start();

			// place the to be recovered microbatches in this queue
			BlockingQueue<Runnable> queue = new PriorityBlockingQueue<Runnable>();
			ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 10000, TimeUnit.MILLISECONDS, queue);
			// CheckerTask checker = new CheckerTask(self, queue, fogHandler);
			CheckerTask checker = new CheckerTask(self.getFog(), executor, fogHandler, queue);

			Thread t6 = new Thread(checker);
			t6.start();

			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param nodeX
	 *            The joining Node
	 * @return YES if successfully joined, NO otherwise
	 */
	private static String requestToJoinBuddyPool(Fog fog, String referrerIP, int referrerPort) {
		TwoPhaseCommitResponse result = new TwoPhaseCommitResponse(Constants.STATUS_NO);
		boolean joinedPool = false;
		TTransport transport = null;
		try {
			transport = new TFramedTransport(new TSocket(referrerIP, referrerPort));
			transport.open();

			TProtocol protocol = new TBinaryProtocol(transport);
			FogService.Client fogClient = new FogService.Client(protocol);

			FogInfoData incomingNode = new FogInfoData();
			incomingNode.setReliability(fog.getMyFogInfo().getReliability());

			NodeInfoData nodeInfo = new NodeInfoData();
			nodeInfo.setNodeId(fog.getMyFogInfo().getNodeID());
			nodeInfo.setNodeIP(fog.getMyFogInfo().getNodeIP());
			nodeInfo.setPort(fog.getMyFogInfo().getPort());

			incomingNode.setNodeInstance(nodeInfo);

			List<NeighborInfoData> candidatePools = fogClient.joinCluster(incomingNode, (short) 1, (short) 2);// pmin,pmax

			for (NeighborInfoData candidate : candidatePools) {

				transport = new TFramedTransport(
						new TSocket(candidate.getNodeInstance().getNodeIP(), candidate.getNodeInstance().getPort()));
				transport.open();
				protocol = new TBinaryProtocol(transport);
				fogClient = new FogService.Client(protocol);

				while (true) {
					FogInfo nodeX = fog.getMyFogInfo();
					result = fogClient.joinPool(
							new FogInfoData(new NodeInfoData(nodeX.getNodeID(), nodeX.getNodeIP(), nodeX.getPort()),
									nodeX.getReliability())); /** 2 phase commit protocol here **/
					if (result.getResponseType().equals(Constants.STATUS_YES)) {
						/*
						 * Once pool is joined, get the buddies first and then neighbors. For buddies,
						 * contact a node from the pool, contact the referrer to give a node from that
						 * pool or get one from the cluster.conf. For neighbors, run the neighbor
						 * distribution algorithm. Once all this is done, start the thread for
						 * exchanging heartbeats with buddies and neighbors
						 */
						short destinationPool = result.getBuddyPoolId();
						fog.setBuddyPoolId(destinationPool);
						// the return value of getPoolMember can be null.
						// TODO::No null return value
						NodeInfoData poolMember = fogClient.getPoolMember(destinationPool);
						getBuddyPoolMembers(fog, poolMember);
						// this node must send each of its buddies a message so that
						// they add this node as their buddy, using MessageType BUDDY_JOIN
						addSelfInBuddies(fog);
						joinedPool = true;
						// Neighbor distribution comes here
						int baseSleepTime = SLEEP_TIME;
						while (!acquireNeighborsFromBuddies(fog)) {
							// procedure not completed mainly because of unable to acquire
							// all locks, sleep with exponential backoff handling
							Thread.sleep(baseSleepTime);
							if (baseSleepTime < SLEEP_TIME_MAX
									&& baseSleepTime * MULTIPLICATION_FACTOR < SLEEP_TIME_MAX) {
								baseSleepTime *= MULTIPLICATION_FACTOR;
							} else {
								baseSleepTime = SLEEP_TIME_MAX;
							}
						}
						// Successfully acquired neighbors , lets release the locks now
						releaseBuddyLocks(fog);
						break;
						// Now we start the heartbeat mechanism in the main
					} else {
						// sleep for some time if unable to join pool, exponential backoff maybe needed
						Thread.sleep(5000);
					}
				}

				if (joinedPool == true)
					break;

			} /** Iterating over the candidate pool, until it successfully joins a pool **/

		} catch (TException e) {
			if (transport != null) {
				transport.close();
			}
			LOGGER.info("In RequestToJoinBuddyPool");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return result.getResponseType();
	}

	private static void releaseBuddyLocks(Fog fog) {
		Collection<FogInfo> buddies = fog.getBuddyMap().values();
		for (FogInfo buddy : buddies) {
			TTransport transport = new TFramedTransport(new TSocket(buddy.getNodeIP(), buddy.getPort()));
			try {
				transport.open();
			} catch (TTransportException e) {
				transport.close();
				e.printStackTrace();
			}
			TProtocol protocol = new TBinaryProtocol(transport);
			FogService.Client fogClient = new FogService.Client(protocol);
			try {
				fogClient.nodeJoiningComplete();
			} catch (TTransportException e) {
				// If we are done with acquiring neighbors and about to release
				// locks, then if a buddy is down, go ahead and complete the
				// process. This handling may vary across different methods but
				// for lock release this is fine.
				LOGGER.info("In releaseBuddyLocks");
				e.printStackTrace();
			} catch (TException e) {
				LOGGER.info("In releaseBuddyLocks");
				e.printStackTrace();
			} finally {
				transport.close();
			}
		}
	}

	private static boolean acquireNeighborsFromBuddies(Fog fog) {
		List<NeighborCount> responses = new ArrayList<>();
		Collection<FogInfo> buddies = fog.getBuddyMap().values();
		for (FogInfo buddy : buddies) {
			TTransport transport = new TFramedTransport(new TSocket(buddy.getNodeIP(), buddy.getPort()));
			try {
				transport.open();
			} catch (TTransportException e) {
				transport.close();
				e.printStackTrace();
				return false;
			}
			TProtocol protocol = new TBinaryProtocol(transport);
			FogService.Client fogClient = new FogService.Client(protocol);
			try {
				NeighborCount neighborCountPerPool = fogClient.getNeighborCountPerPool();
				// the method name is a thrift generated one, so maybe remove the is from
				// thrift interface definition
				if (!neighborCountPerPool.isIsLockAcquired()) {
					// unable to acquire lock
					return false;
				}
				responses.add(neighborCountPerPool);
			} catch (TException e) {
				LOGGER.info("In acquireNeighborsFromBuddies");
				e.printStackTrace();
			} finally {
				transport.close();
			}
		}
		// now we need to do the 2D matrix magic here
		findNeighborDistribution(fog, responses);
		return true;
	}

	private static void findNeighborDistribution(Fog fog, List<NeighborCount> responses) {
		int numBuddies = responses.size();
		int distinctPools = 0;
		Set<Short> poolIds = new HashSet<>();
		for (NeighborCount nCount : responses) {
			Map<Short, Short> nCountPerPool = nCount.getNeighborCountPerPool();
			poolIds.addAll(nCountPerPool.keySet());
		}
		distinctPools = poolIds.size();
		// for the below matrix, we need proper indexing of the buddies and
		// the buddypools. Since we have the list of NeighborCount, we have a
		// proper order of buddies which we will use. To get a proper order of
		// pools, we create a list out of poolIds which is a set to get a fixed
		// order of pools in our matrix
		List<Short> poolList = new ArrayList<>(poolIds);
		int[][] matrix = new int[numBuddies][distinctPools];
		// Column constraint : At least kmin buddies should have neighbors
		// from each buddy pool
		int[] distinct = new int[distinctPools];
		// Row constraint : neighbors must be equally distributed ideally
		// Each buddy should have between 0.5 to 1.5 of the ideal neighbor count
		int[] rowSum = new int[numBuddies];
		for (int buddyIdx = 0; buddyIdx < numBuddies; buddyIdx++) {
			NeighborCount current = responses.get(buddyIdx);
			Map<Short, Short> nCountPerPool = current.getNeighborCountPerPool();
			for (int poolIdx = 0; poolIdx < distinctPools; poolIdx++) {
				if (nCountPerPool.containsKey(poolList.get(poolIdx))) {
					matrix[buddyIdx][poolIdx] = nCountPerPool.get(poolList.get(poolIdx));
				} else {
					matrix[buddyIdx][poolIdx] = 0;
				}
			}
		}
		// populating distinct
		for (int j = 0; j < distinctPools; j++) {
			int count = 0;
			for (int i = 0; i < numBuddies; i++) {
				if (matrix[i][j] > 0)
					count++;
			}
			distinct[j] = count;
		}

		// populating rowSum
		for (int i = 0; i < numBuddies; i++) {
			int sum = 0;
			for (int j = 0; j < distinctPools; j++) {
				sum += matrix[i][j];
			}
			rowSum[i] = sum;
		}

		// start the algorithm to come up with a proper distribution
		// and constraint satisfaction
		// How to find the total number of nodes(N) in the system which is
		// required as part of the row constraint. Ok in the NeighborInfo class
		// we have a poolSize present, so from a single neighbor we have the size
		// of the pool and total size is sum of poolsize of distinct pools
		Map<Short, Short> poolSizeMap = fog.getPoolSizeMap();
		int N = numBuddies; // total nodes in the system
		for (Short poolId : poolSizeMap.keySet()) {
			N += poolSizeMap.get(poolId);
		}
		// double idealNeighbors = (N+1)/(responses.size() * 1.0);
		double idealNeighbors = (N - numBuddies) / ((1 + numBuddies) * 1.0);

		// key is the node index in List<NeighborCount> and value is another map with
		// key
		// as the poolId and value
		// is the count of neighbors to request from this pool from the choosen buddy
		Map<Integer, Map<Short, Short>> myNeighborMap = new HashMap<>();
		int myNeighborCount = 0;
		// START::First pass
		for (int j = 0; j < distinctPools; j++) {
			Distribution dist = new Distribution();
			for (int i = 0; i < numBuddies; i++) {
				if (rowSum[i] >= idealNeighbors) {
					dist.candidates.add(i);
					dist.diffs.add(rowSum[i] - idealNeighbors);
					dist.totalDiff += (rowSum[i] - idealNeighbors);
				}
			}
			int sample = dist.sample();
			if (matrix[sample][j] > 0) {
				if (!myNeighborMap.containsKey(sample)) {
					myNeighborMap.put(sample, new HashMap<>());
				}
				myNeighborMap.get(sample).put((short) poolList.get(j), (short) matrix[sample][j]);
				myNeighborCount += matrix[sample][j];
				// there is no change in distinct[j] as we take all from one and
				// give to the new joining node
				rowSum[sample] -= matrix[sample][j];
			}

			if (myNeighborCount > 0.5 * idealNeighbors)
				break;
		}
		// END::First pass

		if (myNeighborCount <= 0.5 * idealNeighbors) {
			// START::Second pass
		}

		Collection<NeighborInfo> neighbors = fog.getNeighborsMap().values();
		for (Integer buddyIdx : myNeighborMap.keySet()) {
			NeighborCount nCount = responses.get(buddyIdx);
			NodeInfoData nodeInfoData = nCount.getNodeInfoData();
			Map<Short, Short> requestMap = myNeighborMap.get(buddyIdx);
			TTransport transport = new TFramedTransport(new TSocket(nodeInfoData.getNodeIP(), nodeInfoData.getPort()));
			try {
				transport.open();
			} catch (TTransportException e) {
				transport.close();
				e.printStackTrace();
				return;
			}
			TProtocol protocol = new TBinaryProtocol(transport);
			FogService.Client fogClient = new FogService.Client(protocol);
			try {
				List<NeighborInfoData> receivedNeighbors = fogClient.requestNeighbors(requestMap);
				for (NeighborInfoData nInfoData : receivedNeighbors) {
					neighbors.add(new NeighborInfo(
							new NodeInfo(nInfoData.getNodeInstance().getNodeIP(),
									nInfoData.getNodeInstance().getNodeId(), nInfoData.getNodeInstance().getPort(),
									nInfoData.getBuddyPoolId()),
							nInfoData.getBuddyPoolId(), nInfoData.getPool_reliability(), nInfoData.getPool_size()));
				}
				// neighbors are added to the joining node, now subscribe call
				// should be made to the neighbor so it will add this node as
				// one of its subscribers. Also it will be beneficial for this
				// node to keep the identity of the buddy from which it got a
				// a specific neighbor so that it can contact the neighbor to
				// replace the older subscription with this node.
			} catch (TException e) {
				LOGGER.info("In findNeighborDistribution");
				e.printStackTrace();
			} finally {
				transport.close();
			}
		}

	}

	static class Distribution {
		List<Double> diffs = new ArrayList<>();
		List<Integer> candidates = new ArrayList<>();
		double totalDiff = 0;
		Random random = new Random();

		public Distribution() {

		}

		public int sample() {
			double value = random.nextDouble() * totalDiff;
			int idx = 0;
			for (; value > 0; idx++) {
				value -= diffs.get(idx);
			}
			return candidates.get(idx - 1);
		}
	}

	private static void addSelfInBuddies(Fog fog) {
		NodeInfoData nodeInfoData = new NodeInfoData(fog.getMyFogInfo().getNodeID(), fog.getMyFogInfo().getNodeIP(),
				fog.getMyFogInfo().getPort());
		// based on the messagetype is your payload decided
		// this is the case for node joining, so we need FogInfo object as a whole
		// so only thing needed to be passed is the reliability (we have NodeInfo thing
		// going separately)
		// so that each buddy node can calculate the pool reliability
		MessagePayload payload = new MessagePayload();
		payload.setReliability(fog.getMyFogInfo().getReliability());

		Collection<FogInfo> buddies = fog.getBuddyMap().values();
		for (FogInfo fInfo : buddies) {
			TTransport transport = new TFramedTransport(new TSocket(fInfo.getNodeIP(), fInfo.getPort()));
			try {
				transport.open();
			} catch (TTransportException e) {
				transport.close();
				e.printStackTrace();
				return;
			}
			TProtocol protocol = new TBinaryProtocol(transport);
			FogService.Client fogClient = new FogService.Client(protocol);
			try {
				// fogClient.heartBeat(MessageType.BUDDY_JOIN, nodeInfoData, payload);
				// fogClient.heartBeat(payload);
			} /*
				 * catch (TException e) { LOGGER.info("In addSelfInBuddies");
				 * e.printStackTrace(); }
				 */ finally {

			}
		}
	}

	private static void getBuddyPoolMembers(Fog fog, NodeInfoData poolMember) {
		Map<Short, FogInfo> buddyMap = new HashMap<>();
		TTransport transport = new TFramedTransport(new TSocket(poolMember.getNodeIP(), poolMember.getPort()));
		try {
			transport.open();
		} catch (TTransportException e) {
			transport.close();
			e.printStackTrace();
			return;
		}
		TProtocol protocol = new TBinaryProtocol(transport);
		FogService.Client fogClient = new FogService.Client(protocol);
		try {
			List<FogInfoData> buddyPoolMembers = fogClient.getBuddyPoolMembers();
			for (FogInfoData fData : buddyPoolMembers) {
				buddyMap.put(fData.getNodeInstance().getNodeId(),
						new FogInfo(fData.getNodeInstance().getNodeIP(), fData.getNodeInstance().getNodeId(),
								fData.getNodeInstance().getPort(), fog.getBuddyPoolId(), fData.getReliability()));
			}
			fog.setBuddyMap(buddyMap);
		} catch (TException e) {
			LOGGER.info("In getBuddyPoolMembers");
			e.printStackTrace();
		} finally {
			transport.close();
		}
	}

	/**
	 * 
	 * @param fileName
	 *            clusterConf File
	 * @param self
	 */
	private static void populateBuddiesAndNeighborsFromClusterConf(String fileName, Fog self) {
		List<String> lines = Collections.emptyList();
		try {
			lines = Files.readAllLines(Paths.get(fileName));
		} catch (IOException ex) {
			LOGGER.error("IOException : ", ex);
			ex.printStackTrace();
		}
		Iterator<String> it = lines.iterator();
		while (it.hasNext()) {
			String[] arr = it.next().split(",");
			FogInfo fogInfo = new FogInfo(arr[0], Short.parseShort(arr[3]), Integer.parseInt(arr[1]),
					Short.parseShort(arr[2]), Float.parseFloat(arr[4].trim()));
			LOGGER.info(arr[5]);
			String neighborConfig = arr[5].substring(1, arr[5].length() - 1);
			if (self.getMyFogInfo().getBuddyPoolId() == fogInfo.getBuddyPoolId()
					&& self.getMyFogInfo().getNodeID() == fogInfo.getNodeID()) {
				Map<Short, NeighborInfo> neighborsMap = self.getNeighborsMap();
				Map<Short, FogExchangeInfo> neighborExchangeInfoMap = self.getNeighborExchangeInfo();
				Map<Short, Short> poolSizeMap = self.getPoolSizeMap();
				String[] array = neighborConfig.split("_");
				if (!array[0].equals("")) {

					for (String n : array) {
						String[] split = n.split(":");
						NeighborInfo neighborInfo = new NeighborInfo();
						neighborInfo.setNode(new NodeInfo(split[2], Short.parseShort(split[1]),
								Integer.parseInt(split[3]), Short.parseShort(split[0])));
						neighborInfo.setBuddyPoolId(Short.parseShort(split[0]));
						// need to set the poolReliability as well to help in node joining, figure it
						// out
						// neighborInfo.setPoolReliability(0.99);

						// poolSize helps in knowing the total number of nodes in the system
						// which is needed for the neighbor distribution
						// a Fog must store this information which is exchanged in heartbeats
						// with its neighbors
						// neighborInfo.setPoolSize(1);
						neighborsMap.put(neighborInfo.getNode().getNodeID(), neighborInfo);
						neighborExchangeInfoMap.put(neighborInfo.getNode().getNodeID(),
								new FogExchangeInfo(neighborInfo.getNode()));
						if (!poolSizeMap.containsKey(neighborInfo.getBuddyPoolId())) {
							poolSizeMap.put(neighborInfo.getBuddyPoolId(), (short) 0);
						}
						poolSizeMap.put(neighborInfo.getBuddyPoolId(),
								(short) (poolSizeMap.get(neighborInfo.getBuddyPoolId()) + 1));
					}
				}

				// TODO::Subscribe call missing
				sendSubscribeRequests(self);

			} else if (self.getMyFogInfo().getBuddyPoolId() != fogInfo.getBuddyPoolId()) {
				continue;
			} else {
				Map<Short, FogInfo> buddyMap = self.getBuddyMap();
				buddyMap.put(fogInfo.getNodeID(), fogInfo);
				Map<Short, FogExchangeInfo> buddyExchangeInfo = self.getBuddyExchangeInfo();
				buddyExchangeInfo.put(fogInfo.getNodeID(), new FogExchangeInfo(fogInfo));
			}
		}
		self.setPoolSize((short) (self.getBuddyMap().size() + 1));
	}

	private static void sendSubscribeRequests(Fog self) {
		FogInfo selfInfo = self.getMyFogInfo();
		NodeInfoData nodeInfoData = new NodeInfoData(selfInfo.getNodeID(), selfInfo.getNodeIP(), selfInfo.getPort());
		Collection<NeighborInfo> neighbors = self.getNeighborsMap().values();
		for (NeighborInfo nInfo : neighbors) {
			LOGGER.info("Sending subscribe request to fogId : {}", nInfo.getNode().getNodeID());
			while (true) {
				TTransport transport = null;
				try {
					transport = new TFramedTransport(
							new TSocket(nInfo.getNode().getNodeIP(), nInfo.getNode().getPort()));
					transport.open();
				} catch (TTransportException e) {
					transport.close();
					e.printStackTrace();
					// the other device might not be up, so an error
					// What to do when the device will not come up ??
					// One way is to have a timeout after which we stop
					// retrying and remove the device from neighbors list
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						LOGGER.info("Sleep Interrupted !!!!");
						e1.printStackTrace();
					}
					continue;
				}
				TProtocol protocol = new TBinaryProtocol(transport);
				FogService.Client fogClient = new FogService.Client(protocol);
				try {
					boolean subscribed = fogClient.subscribe(nodeInfoData);
					if (subscribed)
						break;
				} catch (TException e) {
					e.printStackTrace();
				} finally {
					if (transport != null) {
						transport.close();
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param eventProcessor
	 * @param serverPort
	 */
	public static void bootstrapFog(FogService.Processor eventProcessor, int serverPort) {
		try {
			TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(serverPort);
			/*TServer server = new TNonblockingServer(
					new TNonblockingServer.Args(serverTransport).processor(eventProcessor));*/
			TThreadedSelectorServer.Args serverArgs = new TThreadedSelectorServer.Args(serverTransport);
			//20 threads for processing requests
			serverArgs.executorService(Executors.newFixedThreadPool(20));
			serverArgs.processor(eventProcessor);
			TThreadedSelectorServer server = new TThreadedSelectorServer(serverArgs);
			LOGGER.info("Starting the Fog Server.. ");
			server.serve();
			LOGGER.info("Closed the connection Thrift Server");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
