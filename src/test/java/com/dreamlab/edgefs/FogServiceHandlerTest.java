package com.dreamlab.edgefs;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.thrift.TException;
import org.junit.Test;

import com.dreamlab.edgefs.controlplane.Fog;
import com.dreamlab.edgefs.misc.Constants;
import com.dreamlab.edgefs.model.EdgeInfo;
import com.dreamlab.edgefs.servicehandler.FogServiceHandler;
import com.dreamlab.edgefs.thrift.MetadataResponse;

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
			MetadataResponse response = myServiceHandler.getMetadataByBlockid(123, "127.0.0.1", 9090, "127.0.0.1", 8000, new ArrayList<String>());
			System.out.println("The error response is "+response.getErrorResponse());
			assertEquals(Constants.FAILURE, response.getStatus());
		} catch(TException e) {
			e.printStackTrace();
		}
		
	}
}
