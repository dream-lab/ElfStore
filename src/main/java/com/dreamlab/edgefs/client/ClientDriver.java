package com.dreamlab.edgefs.client;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import com.dreamlab.edgefs.thrift.FogInfoData;
import com.dreamlab.edgefs.thrift.FogService;
import com.dreamlab.edgefs.thrift.NodeInfoData;

public class ClientDriver {

	public static void main(String[] args) {
		try {
			TTransport transport;
			transport = new TSocket("localhost", 9009);
			transport.open();

			TProtocol protocol = new TBinaryProtocol(transport);
			FogService.Client fogClient = new FogService.Client(protocol);
			
			NodeInfoData nodeX = new NodeInfoData();
			nodeX.setNodeId((short)1);
			nodeX.setNodeIP("10.0.0.1");
			
			FogInfoData fogX = new FogInfoData();
			fogX.setNodeInstance(nodeX);
			fogX.setReliability(0.99f);
			
			String result = fogClient.bootstrapFog(fogX);
			System.out.println("Result is "+result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
