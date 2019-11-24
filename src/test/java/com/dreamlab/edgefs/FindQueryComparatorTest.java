package com.dreamlab.edgefs;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.dreamlab.edgefs.controlplane.FindQueryComparatorImpl;
import com.dreamlab.edgefs.thrift.MatchPreference;

public class FindQueryComparatorTest {

	@Test
	public void performMatchingTest() {

		FindQueryComparatorImpl myComp = new FindQueryComparatorImpl();
		Map<String, List<Long>> metaToMbidMap = new HashMap<String, List<Long>>();
		
		ArrayList<Long> myArrayList1 = new ArrayList<Long>();		
		myArrayList1.add((long) 123);
		myArrayList1.add((long) 124);
		myArrayList1.add((long) 125);
		metaToMbidMap.put("1:dream",myArrayList1);
		
		ArrayList<Long> myArrayList2 = new ArrayList<Long>();		
		myArrayList2.add((long) 123);
		myArrayList2.add((long) 125);
		metaToMbidMap.put("2:lab",myArrayList2);
		
		ArrayList<Long> myArrayList3 = new ArrayList<Long>();		
		myArrayList3.add((long) 125);
		metaToMbidMap.put("3:iisc",myArrayList3);
		
		Map<String, String> metaKeyValueMap = new HashMap<String, String>();
		metaKeyValueMap.put("1","dream");
		
		/** Testing AND and OR Test cases for key which satisfies all the metadata **/
		Set<Long> resultSet = myComp.performMatching(metaKeyValueMap, metaToMbidMap, MatchPreference.AND);
		assertEquals(3, resultSet.size());
		resultSet = myComp.performMatching(metaKeyValueMap, metaToMbidMap, MatchPreference.OR);
		assertEquals(3, resultSet.size());
		
		metaKeyValueMap.put("2","lab");
		resultSet = myComp.performMatching(metaKeyValueMap, metaToMbidMap, MatchPreference.AND);
		assertEquals(2, resultSet.size());
		resultSet = myComp.performMatching(metaKeyValueMap, metaToMbidMap, MatchPreference.OR);
		assertEquals(3, resultSet.size());
		
		metaKeyValueMap.put("3","iisc");
		resultSet = myComp.performMatching(metaKeyValueMap, metaToMbidMap, MatchPreference.AND);
		assertEquals(1, resultSet.size());
		resultSet = myComp.performMatching(metaKeyValueMap, metaToMbidMap, MatchPreference.OR);
		assertEquals(3, resultSet.size());
		
		metaKeyValueMap.put("4","blr");
		resultSet = myComp.performMatching(metaKeyValueMap, metaToMbidMap, MatchPreference.AND);
		assertEquals(0, resultSet.size());
		resultSet = myComp.performMatching(metaKeyValueMap, metaToMbidMap, MatchPreference.OR);
		assertEquals(3, resultSet.size());
		
		/** NONE defaults to AND **/
		metaKeyValueMap.remove("4");
		resultSet = myComp.performMatching(metaKeyValueMap, metaToMbidMap, MatchPreference.NONE);
		assertEquals(1, resultSet.size());
		
	}
}
