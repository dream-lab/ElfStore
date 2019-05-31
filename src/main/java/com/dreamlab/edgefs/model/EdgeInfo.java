package com.dreamlab.edgefs.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;

public class EdgeInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2761322668253801577L;
	
	//nodeId , nodeIp , port and reliability[only for now]
	//should be populated when the Edge registers with the Fog
	public short nodeId;
	public String nodeIp;
	public int port;
	//stats should be set as part of Edge registration process
	private LocalEdgeStats stats;
	private long lastHeartBeatTime;
	private long lastUpdatedTime = Long.MIN_VALUE;
	private int missedHeartbeats;
	//this is used so that there is no contention between 
	//various edges, only contention will be when received
	//heartbeat from edge and checking for missing
	//heartbeats (is there any other scenario for contention??)
	//Currently not using coarse grained locking
//	private Lock lock = new ReentrantLock();
	
	//if this Edge is active or not
	//A for Active, D for Dead, R for Recovery
	//R status is set when the Edge device is dead
	//and Fog starts recovering microbatches present
	//on this Edge
	private transient String status = "A";
	
	public EdgeInfo() {
		
	}

	public EdgeInfo(short nodeId, String nodeIp, int port,  int reliability) {
		super();
		this.nodeId = nodeId;
		this.nodeIp = nodeIp;
		this.port = port;
		this.stats = new LocalEdgeStats(reliability);
	}
	
	public EdgeInfo(short nodeId, int rel, long storage) {
		super();
		this.nodeId = nodeId;
		this.stats = new LocalEdgeStats(storage, rel);
	}
	
	public static Comparator<EdgeInfo> storageComparator = new Comparator<EdgeInfo>() {

		@Override
		public int compare(EdgeInfo l1, EdgeInfo l2) {
			return l1.getStats().getStorage().compareTo(l2.getStats().getStorage());
		}
	};
	
	public static Comparator<EdgeInfo> reliabilityComparator = new Comparator<EdgeInfo>() {

		@Override
		public int compare(EdgeInfo l1, EdgeInfo l2) {
			return l1.getStats().getReliability().compareTo(l2.getStats().getReliability());
		}
	};

	public short getNodeId() {
		return nodeId;
	}

	public void setNodeId(short nodeId) {
		this.nodeId = nodeId;
	}

	public java.lang.String getNodeIp() {
		return nodeIp;
	}

	public void setNodeIp(java.lang.String nodeIp) {
		this.nodeIp = nodeIp;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public LocalEdgeStats getStats() {
		return stats;
	}

	public void setStats(LocalEdgeStats stats) {
		this.stats = stats;
	}

	public long getLastHeartBeatTime() {
		return lastHeartBeatTime;
	}

	public void setLastHeartBeatTime(long lastHeartBeatTime) {
		this.lastHeartBeatTime = lastHeartBeatTime;
	}

	public long getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(long lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public int getMissedHeartbeats() {
		return missedHeartbeats;
	}

	public void setMissedHeartbeats(int missedHeartbeats) {
		this.missedHeartbeats = missedHeartbeats;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	/*public boolean acquireLock() {
		return lock.tryLock();
	}
	
	public void releaseLock() {
		lock.unlock();
	}*/
	
	@Override
	public String toString() {
		return "EdgeInfo [nodeId=" + nodeId + ", nodeIp=" + nodeIp + ", port=" + port + "]";
	}

	public static List<LocalEdgeStats> minMedianMax(Map<Short, EdgeInfo> localEdgeConfMap,
			Set<Short> noStorageEdges) {
		List<LocalEdgeStats> list = new ArrayList<>();
		//the list will contain 3 entries for LocalEdgeStats
		//entry-1: minimum storage, minimum reliability
		//entry-2: median storage, median reliability
		//entry-3: maximum storage, maximum reliability
		LocalEdgeStats l1 = new LocalEdgeStats(), l2 = new LocalEdgeStats(), l3 = new LocalEdgeStats();
		
		int numEntries = 0;
		List<EdgeInfo> values = new ArrayList<>();
		for(Short key : localEdgeConfMap.keySet()) {
			EdgeInfo edgeInfo = localEdgeConfMap.get(key);
			if(edgeInfo.getStatus().equals("A") && !noStorageEdges.contains(key)) {
				values.add(edgeInfo);
				numEntries += 1;
			}
		}
		
		if(numEntries == 0) {
			//no active edge devices
			return list;
		}
			
		
		values.sort(storageComparator);
		l1.setStorage(values.get(0).getStats().getStorage());
		l2.setStorage(values.get((numEntries - 1)/2).getStats().getStorage());
		l3.setStorage(values.get(numEntries-1).getStats().getStorage());
		
		values.sort(reliabilityComparator);
		l1.setReliability(values.get(0).getStats().getReliability());
		l2.setReliability(values.get(numEntries/2).getStats().getReliability());
		l3.setReliability(values.get(numEntries-1).getStats().getReliability());
		
		list.add(l1);
		list.add(l2);
		list.add(l3);
		return list;
	}
	
}
