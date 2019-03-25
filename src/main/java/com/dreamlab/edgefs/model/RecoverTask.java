package com.dreamlab.edgefs.model;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamlab.edgefs.misc.Constants;
import com.dreamlab.edgefs.servicehandler.FogServiceHandler;
import com.dreamlab.edgefs.thrift.FindReplica;
import com.dreamlab.edgefs.thrift.FogService;
import com.dreamlab.edgefs.thrift.NodeInfoData;
import com.dreamlab.edgefs.thrift.ReadReplica;
import com.dreamlab.edgefs.thrift.WritableFogData;
import com.dreamlab.edgefs.thrift.WritePreference;

public class RecoverTask implements Comparable<RecoverTask>, Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(RecoverTask.class);
	
	private Integer reliability;
	private String microbatchId;
	private FogServiceHandler handler;
	
	public RecoverTask() {
		
	}
	
	public RecoverTask(Integer reliability, String microbatchId, FogServiceHandler handler) {
		super();
		this.reliability = reliability;
		this.microbatchId = microbatchId;
		this.handler = handler;
	}

	public Integer getReliability() {
		return reliability;
	}

	public String getMicrobatchId() {
		return microbatchId;
	}

	@Override
	public int compareTo(RecoverTask task) {
		return task.getReliability().compareTo(this.reliability);
	}

	@Override
	public void run() {
		Short edgeId = handler.getFog().getMbIDLocationMap().get(microbatchId);
		if(edgeId == null)
			return;
		EdgeInfo edgeInfo = handler.getFog().getLocalEdgesMap().get(edgeId);
		if(edgeInfo == null)
			return;
		int edgeReliability = getReliability();
		String microBatchId = getMicrobatchId();
		LOGGER.info("Recovery for microbatchId : " + microBatchId + " belonging to EdgeId: " + edgeId
		+ " starts at " + System.currentTimeMillis());
		List<FindReplica> currentReplicas = new ArrayList<>();
		try {
			currentReplicas = handler.find(microBatchId, true, true, null);
		} catch (TException e) {
			LOGGER.error("Error while finding replicas for data recovery : " + e);
			e.printStackTrace();
			return;
		}
		ReadReplica read = null;
		for (FindReplica r : currentReplicas) {
			NodeInfoData node = r.getNode();
			TTransport transport = new TFramedTransport(new TSocket(node.getNodeIP(), node.getPort()));
			try {
				transport.open();
			} catch (TTransportException e) {
				LOGGER.error("Unable to contact for recovery : " + e);
				e.printStackTrace();
				continue;
			}
			TProtocol protocol = new TBinaryProtocol(transport);
			FogService.Client fogClient = new FogService.Client(protocol);
			try {
				read = fogClient.read(microBatchId, true);
				LOGGER.info("Write complete for recovery");
				break;
			} catch (TException e) {
				LOGGER.error("Error while reading data during recovery : " + e);
				e.printStackTrace();
				continue;
			}
		}
		List<WritableFogData> newReplicas = new ArrayList<>();
		if (read != null) {
			long datalength = read.getData().length;
			//identifyreplicas take MB size of datalength
			datalength = datalength/(1024 * 1024);
//			newReplicas = handler.identifyReplicas(datalength, null, (double) (edgeReliability * 1.0) / 100, 1, 2);
			newReplicas = handler.identifyReplicas(datalength, true, (double) (edgeReliability * 1.0) / 100, 1, 2);
		}
		if (read != null && read.getStatus() == Constants.SUCCESS) {
			LOGGER.info("Found new replicas for writing");
			for (WritableFogData fogData : newReplicas) {
				NodeInfoData node = fogData.getNode();
				TTransport transport = new TFramedTransport(new TSocket(node.getNodeIP(), node.getPort()));
				try {
					transport.open();
				} catch (TTransportException e) {
					//write failure not accounted
					transport.close();
					LOGGER.error("Unable to contact for writing while recovery : " + e);
					e.printStackTrace();
					continue;
				}
				TProtocol protocol = new TBinaryProtocol(transport);
				FogService.Client fogClient = new FogService.Client(protocol);
				try {
					byte[] data = read.getData();
					ByteBuffer buffer = ByteBuffer.allocate(data.length);
					buffer.put(data, 0, data.length);
					buffer.flip();
					fogClient.write(read.getMetadata(), buffer, WritePreference.HHH);
					
				} catch (TException e) {
					LOGGER.error("Error while reading data during recovery : " + e);
					e.printStackTrace();
					continue;
				} finally {
					transport.close();
				}
			}
			LOGGER.info("Successfully recovered microbatch : " + microBatchId 
					+ " lost from edgeId: " + edgeId);
			LOGGER.info("Recovery for microbatchId : " + microBatchId + " belonging to EdgeId: " + edgeId
					+ " ends at " + System.currentTimeMillis());
			
			//remove the microbatch from the list of microbatches the edge has
			//fetch edge from MbIdLocation map and remove this microbatchId
			//Since the microbatchId is removed from the list, make sure to not
			//use iterator while adding this microbatchId as we are removing here
			//and adding in Checker (ConcurrentModificationException)
			List<String> list = handler.getFog().getEdgeMicrobatchMap().get(edgeId);
			list.remove(microBatchId);
			if (list.size() == 0) {
				LOGGER.info("All microbatches recovered for EdgeId: " + edgeId + " at " + System.currentTimeMillis());
			}
		}
		
	}

}
