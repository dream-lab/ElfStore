package com.dreamlab.edgefs;

import java.nio.ByteBuffer;

import org.junit.Test;

import com.dreamlab.edgefs.controlplane.CoarseGrainedStats;
import com.dreamlab.edgefs.misc.NeighborDataExchangeFormat;
import com.dreamlab.edgefs.model.FogInfo;
import com.dreamlab.edgefs.model.NeighborHeartbeatData;
import com.dreamlab.edgefs.thrift.NeighborPayload;

public class NeighborDataTest {

	@Test
	public void encodeDecodeTest() {
		FogInfo fInfo = new FogInfo("127.11.255.0", (short)32767, 32896, (short)0, 0.99);
		byte[] BF = new byte[16];
		byte[] info = new byte[10];
		info[2] = 127;
		info[5] = 75;
		info[6] = 10;
		info[9] = 5;
		CoarseGrainedStats stats = new CoarseGrainedStats(info);
		/*ByteBuffer buffer = NeighborDataExchangeFormat.encodeData(fInfo, BF, stats);
		byte[] arr = buffer.array();
		for(byte b : arr) {
			System.out.println(b);
		}
		System.out.println("DECODING");
		buffer.flip();
		NeighborPayload payload = new NeighborPayload(buffer);
		NeighborHeartbeatData data = NeighborDataExchangeFormat.decodeData(payload);
		System.out.println(data.getNeighborStats());
		System.out.println(data.getNeighborInfo().getNode());*/
	}
	
}
