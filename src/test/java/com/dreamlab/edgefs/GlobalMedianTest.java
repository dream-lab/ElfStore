package com.dreamlab.edgefs;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.dreamlab.edgefs.controlplane.CoarseGrainedStats;
import com.dreamlab.edgefs.misc.Constants;
import com.dreamlab.edgefs.misc.GlobalStatsHandler;
import com.dreamlab.edgefs.model.FogStats;

public class GlobalMedianTest {

	private GlobalStatsHandler handler = null;
	
	@Before
	public void setUp() {
		FogStats f1 = new FogStats(2, 2000, 20000, 55, 62, 80, 7,5,5,7);
		FogStats f2 = new FogStats(10000, 15000, 40000, 70, 90, 99, 0,9,0,0);
		FogStats f3 = new FogStats(2000, 18000, 60000, 72, 85, 95, 3,3,3,4);
		Map<Short, FogStats> map = new HashMap<>();
		map.put((short) 1, f1);
		map.put((short) 2, f2);
		map.put((short) 3, f3);
		byte[] arr = new byte[10];
		arr[0] = 2;
		arr[1] = 19;
		arr[2] = 72;
		arr[3] = 65;
		arr[4] = 75;
		arr[5] = 97;
		arr[6] = 7;
		arr[7] = 5;
		arr[8] = 5;
		arr[9] = 7;
		CoarseGrainedStats stats = new CoarseGrainedStats(arr);
		handler = new GlobalStatsHandler(map, stats, (short) 1);
	}
	
	@Test
	public void medianTest() {
//		FogStats stats = handler.computeTotalInfomation();
//		System.out.println("Median Storage : " + stats.getMedianStorage());
//		System.out.println("Median Reliability : " + stats.getMedianReliability());
//		handler.computeTotalInfomation();
	}
	
	@Test
	public void sanityTest() {
		byte[] b = new byte[Constants.STATS_BYTES];
		b[0] = 62;
		b[1] = 62;
		b[2] = 63;
		b[3] = 60;
		b[4] = 70;
		b[5] = 80;
		b[6] = 0;
		b[7] = 1;
		b[8] = 0;
		b[9] = 1;
		FogStats fogStats = new FogStats(9100, 9100, 9100, 80, 80, 80, 1, 2, 3, 2);
		FogStats fogStats1 = new FogStats(9030, 9030, 9030, 80, 80, 80, 1, 1, 2, 2);
		Map<Short, FogStats> otherStats = new HashMap<>();
		otherStats.put((short) 2, fogStats);
		otherStats.put((short) 3, fogStats1);
		GlobalStatsHandler handler = new GlobalStatsHandler(otherStats, new CoarseGrainedStats(b), (short)1);
		handler.computeTotalInfomation(null, null);
		
	}
	
}
