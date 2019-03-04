package com.dreamlab.edgefs.model;

import java.util.List;

import com.dreamlab.edgefs.controlplane.CoarseGrainedStats;

public class BuddyHeartbeatData {

	private NodeInfo nodeInfo;
	private byte[] consolidatedBloomFilter;
	//adding a stream level bloomfilter
	private byte[] streamBloomFilter;
	private byte[] buddyStats;
	private List<FogStats> neighborStats;
	
	public BuddyHeartbeatData() {
		
	}

	public BuddyHeartbeatData(NodeInfo nodeInfo, byte[] consolidatedBloomFilter, byte[] streamBloomFilter,
			byte[] bStats, List<FogStats> neighborStats) {
		super();
		this.nodeInfo = nodeInfo;
		this.consolidatedBloomFilter = consolidatedBloomFilter;
		this.streamBloomFilter = streamBloomFilter;
		this.buddyStats = bStats;
		this.neighborStats = neighborStats;
	}

	public NodeInfo getNodeInfo() {
		return nodeInfo;
	}

	public void setNodeInfo(NodeInfo nodeInfo) {
		this.nodeInfo = nodeInfo;
	}

	public byte[] getConsolidatedBloomFilter() {
		return consolidatedBloomFilter;
	}

	public void setConsolidatedBloomFilter(byte[] consolidatedBloomFilter) {
		this.consolidatedBloomFilter = consolidatedBloomFilter;
	}

	public byte[] getStreamBloomFilter() {
		return streamBloomFilter;
	}

	public void setStreamBloomFilter(byte[] streamBloomFilter) {
		this.streamBloomFilter = streamBloomFilter;
	}

	public byte[] getBuddyStats() {
		return buddyStats;
	}

	public void setBuddyStats(byte[] buddyStats) {
		this.buddyStats = buddyStats;
	}

	public List<FogStats> getNeighborStats() {
		return neighborStats;
	}

	public void setNeighborStats(List<FogStats> neighborStats) {
		this.neighborStats = neighborStats;
	}
	
}
