package com.dreamlab.edgefs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.junit.Before;
import org.junit.Test;

import com.dreamlab.edgefs.controlplane.Fog;
import com.dreamlab.edgefs.model.EdgeInfo;
import com.dreamlab.edgefs.model.LocalEdgeStats;
import com.dreamlab.edgefs.model.StorageReliability;

public class LocalStatsHandlerTest {

	private Fog fog = new Fog();

	private Random random = new Random();

	private int edgeCount = 100;

	private long minS = 1; // 1MB
	private long maxS = 64000;
	private double minR = 3;
	private double maxR = 97;

	@Before
	public void setUp() {

	}

	@Test
	public void checkLocalStatsTest() {
		for (int i = 0; i < edgeCount; i++) {
			double s = random.nextDouble();
			double r = random.nextDouble();
			long storage = (long) (minS + (maxS - minS) * s);
			int rel = (int) (minR + (maxR - minR) * r);
			fog.getLocalEdgesMap().put((short) i, new EdgeInfo((short) i, rel, storage));
		}
		LocalEdgeStats medians = fog.computeLocalInformation();
		if (medians == null)
			return;
		long medianS = medians.getStorage();
		double medianR = medians.getReliability();
		Map<StorageReliability, List<Short>> fineGrainedStats = fog.getLocalEdgeMapping();
		for (StorageReliability sr : fineGrainedStats.keySet()) {
			List<Short> set = fineGrainedStats.get(sr);
			if (set == null)
				continue;
			System.out.println("StorageReliability " + sr + " -> " + set.size());
		}
		System.out.println("Median Storage : " + medians.getStorage());
		int count = 0;
		// just to make sure that count is properly distributed among edges
		for (StorageReliability sr : fineGrainedStats.keySet()) {
			List<Short> list = fineGrainedStats.get(sr);
			if (list != null)
				for (Short edgeId : list) {
					if (checkSetContainment(sr, edgeId, medianS, medianR))
						count += 1;
				}
		}

		if (count == edgeCount) {
			System.out.println("CORRECT");
		} else {
			System.out.println("INCORRECT");
		}
		fog.setLocalEdgesMap(new HashMap<>());
	}
	
	@Test
	public void checkLocalStatsPoissonTest() {
		//generating a Poisson distribution
		double meanStorage = 17500;
		double meanReliability = 62;
		PoissonDistribution storageDist = new PoissonDistribution(meanStorage);
		PoissonDistribution reliabilityDist = new PoissonDistribution(meanReliability);
		for(int i = 0; i < edgeCount; i++) {
			int rel = reliabilityDist.sample();
			int storage = storageDist.sample();
			fog.getLocalEdgesMap().put((short) i, new EdgeInfo((short) i, rel, storage));
		}
		LocalEdgeStats medians = fog.computeLocalInformation();
		if (medians == null)
			return;
		long medianS = medians.getStorage();
		double medianR = medians.getReliability();
		Map<StorageReliability, List<Short>> fineGrainedStats = fog.getLocalEdgeMapping();
		for (StorageReliability sr : fineGrainedStats.keySet()) {
			List<Short> list = fineGrainedStats.get(sr);
			if (list == null)
				continue;
			System.out.println("StorageReliability " + sr + " -> " + list.size());
		}
		System.out.println("Median Storage : " + medians.getStorage());
		int count = 0;
		// just to make sure that count is properly distributed among edges
		for (StorageReliability sr : fineGrainedStats.keySet()) {
			List<Short> list = fineGrainedStats.get(sr);
			if (list != null)
				for (Short edgeId : list) {
					if (checkSetContainment(sr, edgeId, medianS, medianR))
						count += 1;
				}
		}
		System.out.println("Count is : " + count);
		if (count == edgeCount) {
			System.out.println("CORRECT");
		} else {
			System.out.println("INCORRECT");
		}
		fog.setLocalEdgesMap(new HashMap<>());

	}

	private boolean checkSetContainment(StorageReliability sr, Short edgeId, long medianS, double medianR) {
		EdgeInfo edgeInfo = fog.getLocalEdgesMap().get(edgeId);
		if (sr == StorageReliability.HH) {
			if (edgeInfo.getStats().getStorage() >= medianS && edgeInfo.getStats().getReliability() >= medianR)
				return true;
		} else if (sr == StorageReliability.HL) {
			if (edgeInfo.getStats().getStorage() >= medianS && edgeInfo.getStats().getReliability() < medianR)
				return true;
		} else if (sr == StorageReliability.LH) {
			if (edgeInfo.getStats().getStorage() < medianS && edgeInfo.getStats().getReliability() >= medianR)
				return true;
		} else {
			if (edgeInfo.getStats().getStorage() < medianS && edgeInfo.getStats().getReliability() < medianR)
				return true;
		}
		LocalEdgeStats lStats = fog.getLocalEdgesMap().get(edgeId).getStats();
		System.out.println("Error in : " + sr + " , Storage : " + lStats.getStorage() 
		+ " , Reliablity : " + lStats.getReliability());
		return false;
	}

}
