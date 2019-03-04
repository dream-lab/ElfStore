package com.dreamlab.edgefs;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;
import com.dreamlab.edgefs.misc.GlobalFogContributions;
import com.dreamlab.edgefs.model.FogStats;
import com.dreamlab.edgefs.model.StorageReliability;

public class GlobalFogContributionsTest {

	@Test
	public void testFogContributionSingleQuadrant() {
		FogStats myStats = new FogStats(20, 40, 80, 60, 80, 90);
		
		GlobalFogContributions globalFogContrib = new GlobalFogContributions(myStats);
		
		int a = 40;
		/** This is for the 1st quadrant check **/
		int sMin=30,sMax=35,rMin=82,rMax = 87;
		HashMap<StorageReliability, Short> res = globalFogContrib.computeContributionOfFogToGlobalMap(a, sMin,sMax,rMin,rMax); //smin
		assertEquals(40, (int)res.get(StorageReliability.LH)); //LH quadrant
		
		sMin=30; sMax=35; rMin=71;rMax = 76;
		res = globalFogContrib.computeContributionOfFogToGlobalMap(a, sMin,sMax,rMin,rMax);
		assertEquals(40, (int)res.get(StorageReliability.LL)); //LH quadrant
		
		sMin=40; sMax=45; rMin=50;rMax = 59;
		res = globalFogContrib.computeContributionOfFogToGlobalMap(a, sMin,sMax,rMin,rMax);
		assertEquals(40, (int)res.get(StorageReliability.HL)); //LH quadrant
		
		sMin=45; sMax=50; rMin=82;rMax = 87;
		res = globalFogContrib.computeContributionOfFogToGlobalMap(a, sMin,sMax,rMin,rMax);
		assertEquals(40, (int)res.get(StorageReliability.HH)); //LH quadrant
		
		
	}	
	
	@Test
	public void testComputeContributionDoubleQuadrant() {
		
		FogStats myStats = new FogStats(20, 40, 80, 60, 80, 90);
		
		GlobalFogContributions globalFogContrib = new GlobalFogContributions(myStats);
		
		int a = 40;

		int sMin=30,sMax=35,rMin=82,rMax = 87;
		
		/** This is for the 2nd quadrant check **/
		sMin=30; sMax=35; rMin=70;rMax = 85;
		HashMap<StorageReliability, Short> res = globalFogContrib.computeContributionOfFogToGlobalMap(a, sMin,sMax,rMin,rMax);
		assertEquals(13, (int)res.get(StorageReliability.LH)); 
		assertEquals(27, (int)res.get(StorageReliability.LL)); //this should be 27		
		
		sMin=30; sMax=50; rMin=40;rMax = 60;
		res = globalFogContrib.computeContributionOfFogToGlobalMap(a, sMin,sMax,rMin,rMax);
		assertEquals(20, (int)res.get(StorageReliability.LL)); 
		assertEquals(20, (int)res.get(StorageReliability.HL));
		
		a=40;sMin=50; sMax=70; rMin=75;rMax = 81;
		res = globalFogContrib.computeContributionOfFogToGlobalMap(a, sMin,sMax,rMin,rMax);
		assertEquals(34, (int)res.get(StorageReliability.HL)); 
		assertEquals(6, (int)res.get(StorageReliability.HH));

		a=13;sMin=30; sMax=50; rMin=80;rMax = 85;
		res = globalFogContrib.computeContributionOfFogToGlobalMap(a, sMin,sMax,rMin,rMax);
		assertEquals(6, (int)res.get(StorageReliability.LH)); 
		assertEquals(7, (int)res.get(StorageReliability.HH));
		
		/** This test is to check if all the edge devices are allotted **/
		a=1;sMin=50; sMax=70; rMin=75;rMax = 81;
		res = globalFogContrib.computeContributionOfFogToGlobalMap(a, sMin,sMax,rMin,rMax);
		short res1 = res.get(StorageReliability.HL);
		short res2 = res.get(StorageReliability.HH);
		assertEquals(1, (short) res1+res2);		
		
	}
	
	@Test
	public void testComputeContribution() {
		FogStats myStats = new FogStats(20, 40, 80, 60, 80, 90);
		
		GlobalFogContributions globalFogContrib = new GlobalFogContributions(myStats);
		
		int a = 40;

		int sMin=30,sMax=35,rMin=70,rMax = 80;
		
		/** This is for the 3rd quadrant check **/
		sMin=30; sMax=50; rMin=70;rMax = 90;
		HashMap<StorageReliability, Short> res = globalFogContrib.computeContributionOfFogToGlobalMap(a, sMin,sMax,rMin,rMax);
		assertEquals(10, (int)res.get(StorageReliability.LH)); 
		assertEquals(10, (int)res.get(StorageReliability.LL));
		assertEquals(10, (int)res.get(StorageReliability.HL)); 
		assertEquals(10, (int)res.get(StorageReliability.HH)); 
		
		a=2;sMin=33; sMax=51; rMin=73;rMax = 87;
		res = globalFogContrib.computeContributionOfFogToGlobalMap(a, sMin,sMax,rMin,rMax);
		short res1 = (short) ((int)res.get(StorageReliability.LH)); 
		short res2 = (short) ((int)res.get(StorageReliability.LL));
		short res3 = (short) ((int)res.get(StorageReliability.HL)); 
		short res4 = (short) ((int)res.get(StorageReliability.HH));
		
		System.out.println("res1 "+res1+" res2 "+res2+ " res3 "+res3+ " res4 "+ res4);
		
		System.out.println("Total devies "+(res1+res2+res3+res4));
		assertEquals(2, (short) res1+res2+res3+res4);	
	}
	

}
