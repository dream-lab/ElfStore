package com.dreamlab.edgefs.model;

public class NeighborHeartbeatData {

	private NeighborInfo neighborInfo;
	private byte[] bloomFilterUpdates;
	//stream level metadata
	private byte[] streamBFilter;
	private FogStats neighborStats;
	
	public NeighborHeartbeatData() {
		
	}

	public NeighborHeartbeatData(NeighborInfo neighborInfo, byte[] bloomFilterUpdates,
			byte[] streamBFilter, FogStats neighborStats) {
		super();
		this.neighborInfo = neighborInfo;
		this.bloomFilterUpdates = bloomFilterUpdates;
		this.streamBFilter = streamBFilter;
		this.neighborStats = neighborStats;
	}

	public NeighborInfo getNeighborInfo() {
		return neighborInfo;
	}

	public void setNeighborInfo(NeighborInfo neighborInfo) {
		this.neighborInfo = neighborInfo;
	}

	public byte[] getBloomFilterUpdates() {
		return bloomFilterUpdates;
	}

	public void setBloomFilterUpdates(byte[] bloomFilterUpdates) {
		this.bloomFilterUpdates = bloomFilterUpdates;
	}

	public byte[] getStreamBFilter() {
		return streamBFilter;
	}

	public void setStreamBFilter(byte[] streamBFilter) {
		this.streamBFilter = streamBFilter;
	}

	public FogStats getNeighborStats() {
		return neighborStats;
	}

	public void setNeighborStats(FogStats neighborStats) {
		this.neighborStats = neighborStats;
	}
}
