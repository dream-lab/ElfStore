package com.dreamlab.edgefs.misc;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import com.dreamlab.edgefs.controlplane.CoarseGrainedStats;
import com.dreamlab.edgefs.model.FogInfo;
import com.dreamlab.edgefs.model.FogStats;
import com.dreamlab.edgefs.model.NeighborHeartbeatData;
import com.dreamlab.edgefs.model.NeighborInfo;
import com.dreamlab.edgefs.model.NodeInfo;
import com.dreamlab.edgefs.thrift.NeighborPayload;

/**
 * This is the common payload used by a Fog device to send
 * heartbeats to its subscribers which is also received by
 * a Fog device from its neighbors.
 *
 */
public class NeighborDataExchangeFormat {

	/**
	 * The subscriber data exchange format is
	 * -----------+----------+----------+-----------+------------+------------+------------+
	 *  NodeId(2)     Ip(4)    Port(2)    ~PoolId(2)  ~Stats(10)    ~BF(20)      ~BF(20)
	 * -----------+----------+----------+-----------+------------+------------+------------+
	 * NOTE START
	 * A ~ before a field means the field may be present or not. To indicate this the first
	 * byte will be 0 if no bytes are present for that field or 1 followed by the actual
	 * number of bytes for the specific field. We are intentionally not adding another byte 
	 * to indicate the length of the field as we know the number of bytes to read for a 
	 * specific field.
	 * NOTE END
	 * 
	 * For more explanation, {@link BuddyDataExchangeFormat}
	 */
	
	//we will never be crossing this limit when sending 
	//data to subscribers but this will play an important
	//when buddy sends heartbeats
	private static int INITIAL_BUFFER_SIZE = 100;
	
	//length is not added for the fields as the size of a field
	//is precisely known, there is an ip address which is a string
	//but it is sent as four bytes
	public static ByteBuffer encodeData(NodeInfo selfInfo, byte[] bloomFilter, byte[] streamBFilter,
			CoarseGrainedStats localStats) {
		byte[] buffer = new byte[INITIAL_BUFFER_SIZE];
		int idx = 0;
		//we have taken short as the nodeId as we assume nodeId
		//range starts from 1 and will go till 32767 (Short.MAX_VALUE)
		//so just shift bits and we are fine
		short nodeId = selfInfo.getNodeID();
		buffer[idx++] = (byte) (nodeId >> 8);
		buffer[idx++] = (byte) (nodeId & 0xFF);
		String[] splits = selfInfo.getNodeIP().split("\\.");
		//individual bytes can be more than 128 till 255
		//so handle properly
		for(int i = 0; i < 4; i++) {
			short s = Short.parseShort(splits[i]);
			buffer[idx++] = (byte) (s + Byte.MIN_VALUE);
		}
		int port = selfInfo.getPort();
		//this is fine as port numbers range from 0 to 65535
		short formattedPort = (short) (port + Short.MIN_VALUE);
		buffer[idx++] = (byte) (formattedPort >> 8);
		buffer[idx++] = (byte) (formattedPort & 0xFF);
		
		//if poolId to be attached, then add a 1 byte as well
		//I am adding it for now
		buffer[idx++] = 1;
		short poolId = selfInfo.getBuddyPoolId();
		//make sure that this poolId is set when bootstrapping system
		buffer[idx++] = (byte) (poolId >> 8);
		buffer[idx++] = (byte) (poolId & 0xFF);
		
		//if there was no change in the local stats of a Fog, no stats
		//will be sent. This is made sure by sending null for localStats
		if(localStats == null) {
			buffer[idx++] = 0;
		} else {
			buffer[idx++] = 1;
			byte[] myStats = localStats.getInfo();
			System.arraycopy(myStats, 0, buffer, idx, myStats.length);
			idx += myStats.length;
		}
		
		//if there was no change in personal bloomfilter of a Fog, no
		//update will be sent. A null value indicates this
		if(bloomFilter == null) {
			buffer[idx++] = 0;
		} else {
			buffer[idx++] = 1;
			System.arraycopy(bloomFilter, 0, buffer, idx, bloomFilter.length);
			idx += bloomFilter.length;
		}
		
		//stream level metadata
		if(streamBFilter == null) {
			buffer[idx++] = 0;
		} else {
			buffer[idx++] = 1;
			System.arraycopy(streamBFilter, 0, buffer, idx, streamBFilter.length);
			idx += streamBFilter.length;
		}
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(idx);
		byteBuffer.put(buffer, 0, idx);
		//do not forget to flip
		byteBuffer.flip();
		return byteBuffer;
	}
	
	//in case while encoding, length bytes are added, during decoding skip
	//those bytes by incrementing idx
	public static NeighborHeartbeatData decodeData(NeighborPayload payload) {
		byte[] arr = payload.getPayload();
		int idx = 0;
		byte[] stats = null, bloomFilter = null, streamBFilter = null;
		short nodeId = (short) ((arr[idx++] << 8) | (arr[idx++] & 0xFF));
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < 4; i++) {
			builder.append(Short.toString((short) (arr[idx++] - Byte.MIN_VALUE)));
			builder.append(".");
		}
		String ip = builder.substring(0, builder.length()-1);
		int port = ((arr[idx++] << 8) | (arr[idx++] & 0xFF)) - Short.MIN_VALUE;
		short poolId = 0; //no value given
		if(arr[idx++] == 1) {
			//poolId supplied
			poolId = (short) ((arr[idx++] << 8) | (arr[idx++] & 0xFF));
		}
		if(arr[idx++] == 1) {
			//stats are updated
			stats = new byte[Constants.STATS_BYTES];
			System.arraycopy(arr, idx, stats, 0, stats.length);
			idx += stats.length;
		}
		if(arr[idx++] == 1) {
			//bloomfilter updates
			bloomFilter = new byte[Constants.BLOOM_FILTER_BYTES];
			System.arraycopy(arr, idx, bloomFilter, 0, bloomFilter.length);
			idx += bloomFilter.length;
		}
		
		//stream level bloomfilter
		if(arr[idx++] == 1) {
			streamBFilter = new byte[Constants.BLOOM_FILTER_BYTES];
			System.arraycopy(arr, idx, streamBFilter, 0, streamBFilter.length);
			idx += streamBFilter.length;
		}
		
		//currently poolSize and poolReliability are not sent but they can be added in
		//the future for proper device management
		NeighborInfo nInfo = new NeighborInfo(new NodeInfo(ip, nodeId, port, poolId));
		FogStats fogStats = null;
		if (stats != null) {
			fogStats = FogStats.createInstance(stats, nInfo.getNode());
		}
		return new NeighborHeartbeatData(nInfo, bloomFilter, streamBFilter, fogStats);
	}
	
	
}
