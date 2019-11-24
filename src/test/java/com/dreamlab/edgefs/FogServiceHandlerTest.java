package com.dreamlab.edgefs;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.thrift.TException;
import org.junit.Test;

import com.dreamlab.edgefs.controlplane.Fog;
import com.dreamlab.edgefs.misc.Constants;
import com.dreamlab.edgefs.model.EdgeInfo;
import com.dreamlab.edgefs.servicehandler.FogServiceHandler;
import com.dreamlab.edgefs.thrift.EdgeInfoData;
import com.dreamlab.edgefs.thrift.FindBlockQueryResponse;
import com.dreamlab.edgefs.thrift.FindQueryCondition;
import com.dreamlab.edgefs.thrift.FindReplica;
import com.dreamlab.edgefs.thrift.MatchPreference;
import com.dreamlab.edgefs.thrift.MetadataResponse;
import com.dreamlab.edgefs.thrift.ReplicaCount;

public class FogServiceHandlerTest {

	@Test
	public void getMetadataByBlockidTest() {
		FogServiceHandler myServiceHandler =  new FogServiceHandler();
		Fog myFog = new Fog("127.0.0.1",(short) 1	, 9090, (short)1, 0.90f);		
	
		Map<Short, Byte> myEdgeMap = new HashMap<Short, Byte>();
		myEdgeMap.put((short)11 , (byte)1);		
		myFog.getMbIDLocationMap().put((long)123, myEdgeMap);
		myFog.getLocalEdgesMap().put((short)11, new EdgeInfo((short)11, "127.0.0.1", 8000, (byte)80));
		
		myServiceHandler.setFog(myFog);
		
		/** Test for fog ip and port not being matched **/
		try {
			MetadataResponse response = myServiceHandler.getMetadataByBlockid(123, "127.0.0.1", 8000, "127.0.0.1", 8000, new ArrayList<String>());
			System.out.println("The error response is "+response.getErrorResponse());
			assertEquals(Constants.FAILURE, response.getStatus());
		} catch (TException e) {
			e.printStackTrace();
		}
		
		/** Fog IP,port match, Test for microbatch id not being present in the system **/
		try {
			MetadataResponse response = myServiceHandler.getMetadataByBlockid(123, "127.0.0.1", 990, "127.0.0.1", 8000, new ArrayList<String>());
			System.out.println("The error response is "+response.getErrorResponse());
			assertEquals(Constants.FAILURE, response.getStatus());
		} catch(TException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void findFastTest() {
		FogServiceHandler myServiceHandler =  new FogServiceHandler();
		Fog myFog = new Fog("127.0.0.1",(short) 1	, 9090, (short)1, 0.90f);		
	
		Map<Short, Byte> myEdgeMap = new HashMap<Short, Byte>();
		myEdgeMap.put((short)11 , (byte)1);		
		myFog.getMbIDLocationMap().put((long)123, myEdgeMap);
		myFog.getMbIDLocationMap().put((long)124, myEdgeMap);
		myFog.getMbIDLocationMap().put((long)125, myEdgeMap);
		
		myFog.getLocalEdgesMap().put((short)11, new EdgeInfo((short)11, "127.0.0.1", 8000, (byte)80));		
		myServiceHandler.setFog(myFog);
		
		List<Long> microbatchIdList = new ArrayList<Long>();
		microbatchIdList.add((long) 123);
		microbatchIdList.add((long) 124);
		microbatchIdList.add((long) 125);
		
		EdgeInfoData selfInfo = new EdgeInfoData((short)11, "127.0.0.1", 8000, (byte)80, (byte)60);
		
		/** Passing a null microbatchlist **/
		try {
			Map<Long,List<FindReplica>> findFastResult = myServiceHandler.findFast(null, true, true, selfInfo);
			assertEquals(0, findFastResult.size());
			System.out.println("findFastResult => "+findFastResult.toString());
		}catch(TException e) {
			e.printStackTrace();
		}
		
		/** Test for the edges being matched and all blocks being present in the local fog **/
		try {
			Map<Long,List<FindReplica>> findFastResult = myServiceHandler.findFast(microbatchIdList, true, true, selfInfo);
			assertEquals(3, findFastResult.size());
			System.out.println("findFastResult => "+findFastResult.toString());
		}catch(TException e) {
			e.printStackTrace();
		}
		
		selfInfo = new EdgeInfoData((short)12, "127.0.0.1", 8000, (byte)80, (byte)60);
		/** Test for the edges being matched and all blocks not being present in the local fog **/		
		try {
			Map<Long,List<FindReplica>> findFastResult = myServiceHandler.findFast(microbatchIdList, true, true, selfInfo);
			assertEquals(3, findFastResult.size());
			System.out.println("findFastResult => "+findFastResult.toString());
		}catch(TException e) {
			e.printStackTrace();
		}	
	}
	
	@Test
	public void findBlockUsingQueryTest() {
		FogServiceHandler myServiceHandler =  new FogServiceHandler();
		Fog myFog = new Fog("127.0.0.1",(short) 1	, 9090, (short)1, 0.90f);		
	
		Map<Short, Byte> myEdgeMap = new HashMap<Short, Byte>();
		myEdgeMap.put((short)11 , (byte)1);		
		myFog.getMbIDLocationMap().put((long)123, myEdgeMap);
		myFog.getMbIDLocationMap().put((long)124, myEdgeMap);
		myFog.getMbIDLocationMap().put((long)125, myEdgeMap);
		
		/** Prepare the stream ids **/
		myFog.getMbIdToStreamIdMap().put((long)123, "stream1");
		myFog.getMbIdToStreamIdMap().put((long)124, "stream2");
		myFog.getMbIdToStreamIdMap().put((long)125, "stream3");
		
		/** Prepare the metadata key value map **/
		HashMap<String, String> metaKeyValueMap = new HashMap<String, String>();
		metaKeyValueMap.put("1","dream");
		metaKeyValueMap.put("2","lab");
		metaKeyValueMap.put("3","iisc");
		
		List<Long> microbatchIdList1 = new ArrayList<Long>();
		microbatchIdList1.add((long) 123);
		microbatchIdList1.add((long) 124);
		microbatchIdList1.add((long) 125);
		
		myFog.getMetaToMBIdListMap().put("1:dream", microbatchIdList1);
		
		List<Long> microbatchIdList2 = new ArrayList<Long>();
		microbatchIdList2.add((long) 123);		
		microbatchIdList2.add((long) 125);
		
		myFog.getMetaToMBIdListMap().put("2:lab", microbatchIdList2);
		
		List<Long> microbatchIdList3 = new ArrayList<Long>();				
		microbatchIdList3.add((long) 125);
		
		myFog.getMetaToMBIdListMap().put("3:iisc", microbatchIdList3);
		
		myServiceHandler.setFog(myFog);
		
		Map<Long, String> findBlockQueryResponseMap = myServiceHandler.findBlockUsingQuery(metaKeyValueMap, false, false, MatchPreference.AND);
		System.out.println(findBlockQueryResponseMap);
		assertEquals(findBlockQueryResponseMap.containsKey((long)125), true);
	}
	
	public List<List<FindQueryCondition>> getFindQueryObject(){
		List<List<FindQueryCondition>> myFindQueryList = new ArrayList<List<FindQueryCondition>>();
		
		List<FindQueryCondition> orList1 = new ArrayList<FindQueryCondition>();
		List<FindQueryCondition> orList2 = new ArrayList<FindQueryCondition>();
		List<FindQueryCondition> orList3 = new ArrayList<FindQueryCondition>();
		
		FindQueryCondition condition1 = new FindQueryCondition("1", "cds");
		FindQueryCondition condition2 = new FindQueryCondition("2", "cds");
		FindQueryCondition condition3 = new FindQueryCondition("2", "dream");
		FindQueryCondition condition4 = new FindQueryCondition("3", "cds");
		FindQueryCondition condition5 = new FindQueryCondition("3", "dream");
		FindQueryCondition condition6 = new FindQueryCondition("3", "iisc");
		
		orList1.add(condition1);
		orList2.add(condition2);orList2.add(condition3);
		orList3.add(condition4);orList3.add(condition5);orList3.add(condition6);
		
		myFindQueryList.add(orList1);myFindQueryList.add(orList2);myFindQueryList.add(orList3);
		return myFindQueryList;
	}
	
	@Test
	public void findBlocksAndLocationsWithQueryTest() {
		FogServiceHandler myServiceHandler =  new FogServiceHandler();
		Fog myFog = new Fog("127.0.0.1",(short) 1	, 9090, (short)1, 0.90f);		
	
		Map<Short, Byte> myEdgeMap = new HashMap<Short, Byte>();
		myEdgeMap.put((short)11 , (byte)1);		
		myFog.getMbIDLocationMap().put((long)123, myEdgeMap);
		myFog.getMbIDLocationMap().put((long)124, myEdgeMap);
		myFog.getMbIDLocationMap().put((long)125, myEdgeMap);
		
		/** Add the local edge to the fog local edges map **/
		myFog.getLocalEdgesMap().put((short)11, new EdgeInfo((short)11, "127.0.0.1", 8000, (byte)80));		
		
		/** Prepare the stream ids **/
		myFog.getMbIdToStreamIdMap().put((long)123, "stream1");
		myFog.getMbIdToStreamIdMap().put((long)124, "stream2");
		myFog.getMbIdToStreamIdMap().put((long)125, "stream3");
		
		/** Prepare the metadata key value map **/
		HashMap<String, String> metaKeyValueMap = new HashMap<String, String>();
		metaKeyValueMap.put("1","cds");
		metaKeyValueMap.put("2","dream");
		metaKeyValueMap.put("3","iisc");
		
		List<Long> microbatchIdList1 = new ArrayList<Long>();
		microbatchIdList1.add((long) 123);
		microbatchIdList1.add((long) 124);
		microbatchIdList1.add((long) 125);
		
		myFog.getMetaToMBIdListMap().put("1:cds", microbatchIdList1);
		
		List<Long> microbatchIdList2 = new ArrayList<Long>();
		microbatchIdList2.add((long) 123);		
		microbatchIdList2.add((long) 125);
		
		myFog.getMetaToMBIdListMap().put("2:dream", microbatchIdList2);
		
		List<Long> microbatchIdList3 = new ArrayList<Long>();				
		microbatchIdList3.add((long) 125);
		
		myFog.getMetaToMBIdListMap().put("3:iisc", microbatchIdList3);
		
		myServiceHandler.setFog(myFog);
		
		Map<Long, String> findBlockQueryResponseMap = myServiceHandler.findBlockUsingQuery(metaKeyValueMap, false, false, MatchPreference.AND);
		System.out.println(findBlockQueryResponseMap);
		assertEquals(findBlockQueryResponseMap.containsKey((long)125), true);
		
		/******** CASE 1 : POSITIVE CASES ********/
		/** Test for no replica location NONE **/
		List<List<FindQueryCondition>> queryCondition = getFindQueryObject();
		EdgeInfoData edgeInfo = new EdgeInfoData((short)11, "127.0.0.1", 8000, (byte)80, (byte)60);
		try {
			System.out.println("FindBlockResultMap => "+metaKeyValueMap);
			FindBlockQueryResponse response = myServiceHandler.findBlocksAndLocationsWithQuery(metaKeyValueMap, false, false,queryCondition , ReplicaCount.NONE, edgeInfo);
			assertEquals(3, response.getFindBlockQueryResultMapSize());
			assertEquals(response.getFindBlockQueryResultMap().containsKey((long)125), true);
			
			System.out.println("The response is => "+response);
		} catch (TException e) {
			e.printStackTrace();
		}
		
		/** Test for SINGLE replica location **/
		edgeInfo = new EdgeInfoData((short)11, "127.0.0.1", 8000, (byte)80, (byte)60);
		try {
			FindBlockQueryResponse response = myServiceHandler.findBlocksAndLocationsWithQuery(metaKeyValueMap, false, false,queryCondition , ReplicaCount.ONE, edgeInfo);
			assertEquals(3, response.getFindBlockQueryResultMapSize());
			assertEquals(response.getFindBlockQueryResultMap().containsKey((long)125), true);
			
			System.out.println("The response is => "+response);
		} catch (TException e) {
			e.printStackTrace();
		}
		
		/******** CASE 2 : NEGATIVE CASES ********/
		myFog.getMetaToMBIdListMap().remove("3:iisc");
		myFog.getMetaToMBIdListMap().put("3:cds",microbatchIdList2);
		edgeInfo = new EdgeInfoData((short)11, "127.0.0.1", 8000, (byte)80, (byte)60);
		try {
			FindBlockQueryResponse response = myServiceHandler.findBlocksAndLocationsWithQuery(metaKeyValueMap, false, false,queryCondition , ReplicaCount.NONE, edgeInfo);
			System.out.println("The response is => "+response);
			assertEquals(0, response.getFindBlockQueryResultMapSize());
			assertEquals(false, response.getFindBlockQueryResultMap().containsKey((long)125));			
		} catch (TException e) {
			e.printStackTrace();
		}		
	}
}
