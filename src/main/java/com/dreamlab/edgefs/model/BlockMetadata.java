package com.dreamlab.edgefs.model;

import java.util.HashMap;
import java.util.Map;

/**
 * This class maintains the internal data structures that a Fog maintains
 * on a per stream basis to allow the locking of a stream for writing by
 * a client with proper lease time limits (via system-wide properties) and
 * maintaining important information regarding the client holding the lock
 * and with respect to block sequence number written to the stream
 *
 */
public class BlockMetadata {
 
	//maintenance on a per stream basis
	private String streamId;
	//lock will be set to the clientId which is currently doing
	//block appends else it will be null or some stale clientId
	//whose lease has expired. Currently not doing eager lock cleanup
	//, only when a new client comes to get lock is when we check
	//that the lock can be given to some other client
	private String lock;
	//this is the duration for which the lease is valid
	//after this duration, a renewal is needed
	private int leaseDuration;
	//this is the time when the lease was given
	private long leaseStartTime;
	//this is the session secret between the Fog and the client
	private String sessionSecret;
	//the starting blockId for the stream, must be >= 0
	//this is set when the stream is registered
	private long startBlockId;
	//the lastBlockId written to the stream, must be >= 0
	//setting to -1 for initialization purpose
	private long lastBlockId = -1;
	//this is for verification purposes by matching the MD5
	//checkSum of the block
	//private List<String> blockMD5List = new ArrayList<>();
	//TYPE_CHANGE::previously used a list of MD5 checksum to verify the 
	//integrity of a block, however understanding was that blockIds in
	//a stream will be continuous. Relaxing this constraint for the time
	//being, so to allow O(1) time for block verification, we use a
	//map with key as blockId and value as the checksum.
	private Map<Long, String> blockMD5Map = new HashMap<>();
	
	public BlockMetadata() {
		
	}
	
	public BlockMetadata(String streamId, long startSeqNum) {
		super();
		this.streamId = streamId;
		this.startBlockId = startSeqNum;
	}

	public String getStreamId() {
		return streamId;
	}

	public void setStreamId(String streamId) {
		this.streamId = streamId;
	}

	public String getLock() {
		return lock;
	}

	public void setLock(String lock) {
		this.lock = lock;
	}

	public int getLeaseDuration() {
		return leaseDuration;
	}

	public void setLeaseDuration(int leaseDuration) {
		this.leaseDuration = leaseDuration;
	}

	public long getLeaseStartTime() {
		return leaseStartTime;
	}

	public void setLeaseStartTime(long leaseStartTime) {
		this.leaseStartTime = leaseStartTime;
	}

	public String getSessionSecret() {
		return sessionSecret;
	}

	public void setSessionSecret(String sessionSecret) {
		this.sessionSecret = sessionSecret;
	}

	public long getStartBlockId() {
		return startBlockId;
	}

	public void setStartBlockId(long startBlockId) {
		this.startBlockId = startBlockId;
	}

	public long getLastBlockId() {
		return lastBlockId;
	}

	public void setLastBlockId(long lastBlockId) {
		this.lastBlockId = lastBlockId;
	}

	public Map<Long, String> getBlockMD5Map() {
		return blockMD5Map;
	}

	public void setBlockMD5Map(Map<Long, String> blockMD5Map) {
		this.blockMD5Map = blockMD5Map;
	}
	
}
