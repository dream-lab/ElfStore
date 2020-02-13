package com.dreamlab.edgefs.reads;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamlab.edgefs.thrift.FogService;
import com.dreamlab.edgefs.thrift.NodeInfoData;
import com.dreamlab.edgefs.thrift.ReadReplica;

public class GetRemote {

	private static final Logger LOGGER = LoggerFactory.getLogger(GetRemote.class);

	public ReadReplica readRemote(long microbatchId, boolean fetchMetadata, String compFormat, long uncompSize,
			String clientId, String fogIp, int fogPort, NodeInfoData currFog) {

		ReadReplica readReplica = null;

		TTransport transport = new TFramedTransport(new TSocket(fogIp, fogPort));
		try {
			transport.open();
		} catch (TTransportException e) {
			transport.close();
			LOGGER.info("Unable to contact edge device : " + fogIp + " : " + fogPort);
			e.printStackTrace();
			LOGGER.info(
					"MicrobatchId : " + microbatchId + ", read, endTime=" + System.currentTimeMillis() + ",status=0");
			return readReplica;
		}
		TProtocol protocol = new TBinaryProtocol(transport);
		FogService.Client fogClient = new FogService.Client(protocol);
		try {

			if (fetchMetadata) {
				readReplica = fogClient.get(microbatchId, fetchMetadata, compFormat, uncompSize, clientId);
			} else {
				readReplica = fogClient.get(microbatchId, fetchMetadata, compFormat, uncompSize, clientId);
			}
		} catch (TException e) {
			LOGGER.info("Error while reading from Fog : " + fogIp + " : " + fogPort);
			e.printStackTrace();
		} finally {
			transport.close();
			LOGGER.info("DEBUG : released lock for microbatch id " + microbatchId);
		}

		return readReplica;
	}

}
