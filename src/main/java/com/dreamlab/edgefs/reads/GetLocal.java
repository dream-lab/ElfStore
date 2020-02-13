package com.dreamlab.edgefs.reads;

import java.util.Map;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamlab.edgefs.controlplane.Fog;
import com.dreamlab.edgefs.misc.Constants;
import com.dreamlab.edgefs.model.EdgeInfo;
import com.dreamlab.edgefs.thrift.EdgeService;
import com.dreamlab.edgefs.thrift.ReadReplica;

public class GetLocal {

	private static final Logger LOGGER = LoggerFactory.getLogger(GetLocal.class);

	public ReadReplica readLocal(long microbatchId, boolean fetchMetadata, String compFormat, long uncompSize,
			String clientId, Fog fog) {
		ReadReplica data = new ReadReplica();
		data.setStatus(Constants.FAILURE);

		boolean readData = false;
		Map<Short, Byte> edgeMap = fog.getMbIDLocationMap().get(microbatchId);
		if (edgeMap != null) {
			for (Short edgeId : edgeMap.keySet()) {
				if (readData) {
					break;
				}
				if (edgeId != null) {
					EdgeInfo edgeInfo = fog.getLocalEdgesMap().get(edgeId);
					if (edgeInfo != null && edgeInfo.getStatus().equals("A")) {
						TTransport transport = new TFramedTransport(
								new TSocket(edgeInfo.getNodeIp(), edgeInfo.getPort()));
						try {
							transport.open();
						} catch (TTransportException e) {
							transport.close();
							LOGGER.info("Unable to contact edge device : " + edgeInfo);
							e.printStackTrace();
							LOGGER.info("MicrobatchId : " + microbatchId + ", read, endTime="
									+ System.currentTimeMillis() + ",status=0");
							continue;
						}
						TProtocol protocol = new TBinaryProtocol(transport);
						EdgeService.Client edgeClient = new EdgeService.Client(protocol);
						try {

							if (fetchMetadata) {
								data = edgeClient.read(microbatchId, (byte) 1, compFormat, uncompSize);
							} else {
								data = edgeClient.read(microbatchId, (byte) 0, compFormat, uncompSize);
							}

							readData = true;
						} catch (TException e) {
							LOGGER.info("Error while reading microbatch from edge : " + edgeInfo);
							e.printStackTrace();
						} finally {
							transport.close();
							LOGGER.info("DEBUG : released lock for microbatch id " + microbatchId);
						}
					}
				}
			}
		}
		LOGGER.info("MicrobatchId : " + microbatchId + ", read, endTime=" + System.currentTimeMillis() + ",status="
				+ data.getStatus());
		data.setStatus(Constants.SUCCESS);

		return data;
	}
}
