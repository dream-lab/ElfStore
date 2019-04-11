package com.dreamlab.edgefs.model;

import com.dreamlab.edgefs.misc.Constants;

//this class is only to maintain the exchange info
//between the Fog devices a given Fog talks to namely its
//buddies and its neighbors. This should be kept separate
//from FogStats as we also maintain a global table for stats
//from all Fogs, so having this info embedded in FogInfo is
//not correct since this info is only for buddies and neighbors
//and not for everyone while FogStats is for everyone
public class FogExchangeInfo {

	//this will be individual for neighbors and (buddy + its neighbors)
	//for buddies, taking 160 bit bloom filter
	private byte[] bloomFilterUpdates = new byte[Constants.BLOOM_FILTER_BYTES];
	
	//this is similar to the above structure but the above structure handles 
	//microbatch level metadata while this is for stream level metadata
	private byte[] streamBFilterUpdates = new byte[Constants.BLOOM_FILTER_BYTES];
	
	//time of last heartbeat
	private long lastHeartBeatTime;
	//most recent bloomfilter update time
	private long lastUpdatedBFTime = Long.MIN_VALUE;
	//most recent stats update time
	private long lastUpdatedStatsTime = Long.MIN_VALUE;
	//number of missed heartbeats
	private int missedHeartbeats;
	
	//bloomfilter updates and heartbeats are from buddies and
	//neighbors and Fog has info about both of them available,
	//so maintaining this is not necessary. However FogStats needs
	//to maintain this for everyone (check the comment there)
	private NodeInfo nodeInfo;
	
	public FogExchangeInfo() {
		
	}
	
	public FogExchangeInfo(byte[] bloomFilter) {
		super();
		this.bloomFilterUpdates = bloomFilter;
	}
	
	public FogExchangeInfo(NodeInfo nodeInfo) {
		super();
		this.nodeInfo = nodeInfo;
	}

	public byte[] getBloomFilterUpdates() {
		return bloomFilterUpdates;
	}

	public void setBloomFilterUpdates(byte[] bloomFilterUpdates) {
		this.bloomFilterUpdates = bloomFilterUpdates;
	}

	public byte[] getStreamBFilterUpdates() {
		return streamBFilterUpdates;
	}

	public void setStreamBFilterUpdates(byte[] streamBFilterUpdates) {
		this.streamBFilterUpdates = streamBFilterUpdates;
	}

	public long getLastHeartBeatTime() {
		return lastHeartBeatTime;
	}

	public void setLastHeartBeatTime(long lastHeartBeatTime) {
		this.lastHeartBeatTime = lastHeartBeatTime;
	}

	public long getLastUpdatedBFTime() {
		return lastUpdatedBFTime;
	}

	public void setLastUpdatedBFTime(long lastUpdatedBFTime) {
		this.lastUpdatedBFTime = lastUpdatedBFTime;
	}

	public long getLastUpdatedStatsTime() {
		return lastUpdatedStatsTime;
	}

	public void setLastUpdatedStatsTime(long lastUpdatedStatsTime) {
		this.lastUpdatedStatsTime = lastUpdatedStatsTime;
	}

	public int getMissedHeartbeats() {
		return missedHeartbeats;
	}

	public void setMissedHeartbeats(int missedHeartbeats) {
		this.missedHeartbeats = missedHeartbeats;
	}
	
}
