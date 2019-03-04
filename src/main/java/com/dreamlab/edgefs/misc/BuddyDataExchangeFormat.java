package com.dreamlab.edgefs.misc;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dreamlab.edgefs.model.BuddyHeartbeatData;
import com.dreamlab.edgefs.model.FogStats;
import com.dreamlab.edgefs.model.NodeInfo;
import com.dreamlab.edgefs.thrift.BuddyPayload;

public class BuddyDataExchangeFormat {

	/**
	 * The buddy data exchange format is 
	 * ---------+----------+---------+---------+----------+--------------+......+-----------+----------+
	 * 	Count(1) {NodeId(2)   Ip(4)    Port(2)  ~PoolId(2)  ~Stats(10)}            ~BF(20)     ~BF(20)
	 * ---------+----------+---------+---------+----------+--------------+......+-----------+----------+
	 * NOTE START
	 * A ~ before a field means the field may be present or not. To indicate this the first
	 * byte will be 0 if no bytes are present for that field or 1 followed by the actual
	 * number of bytes for the specific field. We are intentionally not adding another byte 
	 * to indicate the length of the field as we know the number of bytes to read for a 
	 * specific field.
	 * NOTE END
	 * The first byte is the count of the number of neighbors whose information a Fog is going
	 * to send to its buddies. This may not be equal to number of neighbors the Fog has since 
	 * we are sending stats of only those neighbors whose information has changed since the last
	 * time we sent the stats to buddies. The information of the Fog is always present in terms 
	 * of its NodeId(2 bytes), Ip(4 bytes, create four splits) , Port(2), PoolId(keeping this as 
	 * optional, if it is present, the next byte will be 1 and subsequent 2 bytes indicate the 
	 * poolId, else the next byte will be 0 and we directly move to the Stats. For Stats, self
	 * stats are present only if there is a change in its own stats. Next follows the information
	 * of the neighbors who have updates in a similar pattern. In the end, we have a bloomfilter
	 * which is only present in case of a change since the last time it was sent to the buddies.
	 * Now there is another bloom filter in the end to accomodate for the stream level search taking
	 * place
	 * 
	 */
	
	//we may need to resize the buffer array as we may have
	//a large number of neighbors and sending their updated
	//state may require expanding the buffer
	private static int INITIAL_BUFFER_SIZE = 512;
	
	//this is the maximum size that a single Fog (self or its neighbor)
	//has in the data format, leaving the bloomfilter which is appended
	//in the end. This is used to resize the byte array while encoding
	private static int MAX_FOG_INFO_SIZE = 22;

	// length is not added for the fields as the size of a field
	// is precisely known, there is an ip address which is a string
	// but we are encoding it as four bytes
	public static ByteBuffer encodeData(NodeInfo selfInfo, byte[] bloomFilter,
			byte[] streamBFilter, byte[] selfStats, List<FogStats> neighborStats) {
		byte[] buffer = new byte[INITIAL_BUFFER_SIZE];
		//Here I am starting with index 1 as I will add the count of neighbors
		//with updated state (BF/Stats) by keeping track of the count and add
		//at the very end
		int idx = 1;
		short nodeId = selfInfo.getNodeID();
		buffer[idx++] = (byte) (nodeId >> 8);
		buffer[idx++] = (byte) (nodeId & 0xFF);
		String[] splits = selfInfo.getNodeIP().split("\\.");
		// individual bytes can be more than 128 till 255
		// so handle properly
		for (int i = 0; i < 4; i++) {
			short s = Short.parseShort(splits[i]);
			buffer[idx++] = (byte) (s + Byte.MIN_VALUE);
		}
		int port = selfInfo.getPort();
		// this is fine as port numbers range from 0 to 65535
		short formattedPort = (short) (port + Short.MIN_VALUE);
		buffer[idx++] = (byte) (formattedPort >> 8);
		buffer[idx++] = (byte) (formattedPort & 0xFF);
		
		//since this is the first element which is a buddy
		//no need to attach the poolId. For neighbors, its
		//we can do with or without poolId
		buffer[idx++] = 0;

		// if there was no change in the local stats of a Fog, no stats
		// will be sent. This is made sure by sending null for localStats
		if (selfStats == null) {
			buffer[idx++] = 0;
		} else {
			buffer[idx++] = 1;
			System.arraycopy(selfStats, 0, buffer, idx, selfStats.length);
			idx += selfStats.length;
		}
		
		int countNeighborUpdates = 0;
		if(neighborStats != null && !neighborStats.isEmpty()) {
			countNeighborUpdates = neighborStats.size();
			for(FogStats nStat : neighborStats) {
				buffer = resizeBufferIfNeeded(buffer, idx, MAX_FOG_INFO_SIZE);
				NodeInfo nInfo = nStat.getNodeInfo();
				nodeId = nInfo.getNodeID();
				buffer[idx++] = (byte) (nodeId >> 8);
				buffer[idx++] = (byte) (nodeId & 0xFF);
				
				splits = nInfo.getNodeIP().split("\\.");
				for (int i = 0; i < 4; i++) {
					short s = Short.parseShort(splits[i]);
					buffer[idx++] = (byte) (s + Byte.MIN_VALUE);
				}
				port = nInfo.getPort();
				formattedPort = (short) (port + Short.MIN_VALUE);
				buffer[idx++] = (byte) (formattedPort >> 8);
				buffer[idx++] = (byte) (formattedPort & 0xFF);
				
				//poolId can be attached for neighbors
				//attaching it for now
				buffer[idx++] = 1;
				short poolId = nInfo.getBuddyPoolId();
				buffer[idx++] = (byte) (poolId >> 8);
				buffer[idx++] = (byte) (poolId & 0xFF);
				
				//this neighbor stat is updated so it should be sent
				buffer[idx++] = 1;
				byte[] myStats = FogStats.getBytes(nStat);
				System.arraycopy(myStats, 0, buffer, idx, myStats.length);
				idx += myStats.length;
			}
		}

		// if there was no change in consolidate bloomfilter of a Fog, no
		// update will be sent. A null value indicates this
		buffer = resizeBufferIfNeeded(buffer, idx, Constants.BLOOM_FILTER_BYTES);
		if (bloomFilter == null) {
			buffer[idx++] = 0;
		} else {
			buffer[idx++] = 1;
			System.arraycopy(bloomFilter, 0, buffer, idx, bloomFilter.length);
			idx += bloomFilter.length;
		}
		
		//adding the stream level bloomfilter
		buffer = resizeBufferIfNeeded(buffer, idx, Constants.BLOOM_FILTER_BYTES);
		if (streamBFilter == null) {
			buffer[idx++] = 0;
		} else {
			buffer[idx++] = 1;
			System.arraycopy(streamBFilter, 0, buffer, idx, streamBFilter.length);
			idx += streamBFilter.length;
		}
		
		//TODO:: add count of updated neighbors at index 0
		buffer[0] = (byte) countNeighborUpdates;
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(idx);
		byteBuffer.put(buffer, 0, idx);
		// do not forget to flip
		byteBuffer.flip();
		return byteBuffer;
	}

	private static byte[] resizeBufferIfNeeded(byte[] buffer, int idx, int itemSize) {
		if(buffer.length - idx < itemSize) {
			System.out.println("Resizing");
			return Arrays.copyOf(buffer, buffer.length + INITIAL_BUFFER_SIZE);
		}
		return buffer;
	}

	// in case while encoding, length bytes are added, during decoding skip
	// those bytes by incrementing idx
	public static BuddyHeartbeatData decodeData(BuddyPayload payload) {
		byte[] arr = payload.getPayload();
		int countNeighborUpdates = arr[0];
		int idx = 1;
		byte[] buddyStats = null, neighborStats = null, bloomFilter = null, streamBFilter = null;
		short nodeId = (short) ((arr[idx++] << 8) | (arr[idx++] & 0xFF));
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			builder.append(Short.toString((short) (arr[idx++] - Byte.MIN_VALUE)));
			builder.append(".");
		}
		String ip = builder.substring(0, builder.length() - 1);
		int port = ((arr[idx++] << 8) | (arr[idx++] & 0xFF)) - Short.MIN_VALUE;
		idx++; //buddy is not sending its poolId but we have kept the format consistent
		//if stats of the buddy are updated
		if(arr[idx++] == 1) {
			buddyStats = new byte[Constants.STATS_BYTES];
			System.arraycopy(arr, idx, buddyStats, 0, buddyStats.length);
			idx += buddyStats.length;
		}
		
		NodeInfo buddyInfo = new NodeInfo(ip, nodeId, port);
		List<FogStats> statsList = new ArrayList<>();
		for(int i = 0; i < countNeighborUpdates; i++) {
			nodeId = (short) ((arr[idx++] << 8) | (arr[idx++] & 0xFF));
			builder = new StringBuilder();
			for (int j = 0; j < 4; j++) {
				builder.append(Short.toString((short) (arr[idx++] - Byte.MIN_VALUE)));
				builder.append(".");
			}
			ip = builder.substring(0, builder.length() - 1);
			port = ((arr[idx++] << 8) | (arr[idx++] & 0xFF)) - Short.MIN_VALUE;
			short poolId = 0; // no value given
			if (arr[idx++] == 1) {
				// poolId supplied
				poolId = (short) ((arr[idx++] << 8) | (arr[idx++] & 0xFF));
			}
			if (arr[idx++] == 1) {
				neighborStats = new byte[Constants.STATS_BYTES];
				System.arraycopy(arr, idx, neighborStats, 0, neighborStats.length);
				idx += neighborStats.length;
				FogStats statsInstance = FogStats.createInstance(neighborStats);
				//this line is crucial since we want total system knowledge at a 
				//Fog, so when a node receives heartbeat from its buddy which contains
				//info about buddy and its neighbors about their stats and id,ip,port
				//In presence of buddy's neighbors ip,port information, this Fog can
				//contact directly and server read and write requests to anyone
				statsInstance.setNodeInfo(new NodeInfo(ip, nodeId, port, poolId));
				statsList.add(statsInstance);
			}
		}
		
		// bloomfilter updates
		if (arr[idx++] == 1) {
			bloomFilter = new byte[Constants.BLOOM_FILTER_BYTES];
			System.arraycopy(arr, idx, bloomFilter, 0, bloomFilter.length);
			idx += bloomFilter.length;
		}
		
		//stream level bloomfilter updates
		if (arr[idx++] == 1) {
			streamBFilter = new byte[Constants.BLOOM_FILTER_BYTES];
			System.arraycopy(arr, idx, streamBFilter, 0, streamBFilter.length);
			idx += streamBFilter.length;
		}
		
		BuddyHeartbeatData data = new BuddyHeartbeatData(buddyInfo, bloomFilter, streamBFilter,
				buddyStats, statsList);
		return data;
	}
}
