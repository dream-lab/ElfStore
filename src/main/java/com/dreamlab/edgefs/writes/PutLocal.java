package com.dreamlab.edgefs.writes;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
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

import com.dreamlab.edgefs.servicehandler.FogServiceHandler;

import com.dreamlab.edgefs.thrift.EdgeService;
import com.dreamlab.edgefs.thrift.Metadata;
import com.dreamlab.edgefs.thrift.WritePreference;
import com.dreamlab.edgefs.thrift.WriteResponse;

public class PutLocal {

	private static final Logger LOGGER = LoggerFactory.getLogger(PutLocal.class);

	/**
	 * Put logic for normal
	 * 
	 * @param mbMetadata
	 * @param version
	 * @param data
	 * @param preference
	 * @param metaKeyValueMap
	 * @param parentFog
	 * @param clientId
	 * @param fog
	 * @return
	 */
	public WriteResponse putData(Metadata mbMetadata, short version, ByteBuffer data, WritePreference preference,
			Map<String, List<String>> metaKeyValueMap, String clientId, Fog fog) {

		WriteResponse wrResponse = new WriteResponse();
		wrResponse.setStatus(Constants.FAILURE);

		/** Set the version of the block to zero **/
		version = 0;

		LOGGER.info("MicrobatchId : " + mbMetadata.getMbId() + ", putNext, startTime=" + System.currentTimeMillis());
		Map<Short, Byte> duplicateHolders = fog.getMbIDLocationMap().get(mbMetadata.getMbId());
		/**
		 * we pass the duplicateHolders while identifying local replica because it might
		 * happen that during recovery, we may choose an edge that already has the
		 * micro-batch present so we should pick a different edge to make sure there is
		 * proper replication
		 **/
		LOGGER.info("[DATA-SIZE] The size of the data is " + data.capacity() + " preference is " + preference);
		EdgeInfo localEdge = FogServiceHandler.identifyLocalReplica(data.capacity(), preference, mbMetadata.getMbId());
		
		long putStartSessionTime = System.nanoTime();
		if (localEdge == null) {
			LOGGER.info("No suitable edge present");
			return wrResponse;
		}

		TTransport transport = new TFramedTransport(new TSocket(localEdge.getNodeIp(), localEdge.getPort()));
		try {
			transport.open();
		} catch (TTransportException e) {
			transport.close();
			LOGGER.error("Unable to contact edge device : " + localEdge);
			e.printStackTrace();
			return wrResponse;
		}

		TProtocol protocol = new TBinaryProtocol(transport);
		EdgeService.Client edgeClient = new EdgeService.Client(protocol);
		try {
			wrResponse = edgeClient.write(mbMetadata.getMbId(), mbMetadata, data);
		} catch (TException e) {
			LOGGER.info("Error while writing microbatch to edge : " + localEdge);
			e.printStackTrace();
			return wrResponse;
		} finally {
			transport.close();

			/** Update the remote fog's local index **/
			FogServiceHandler.updateMicrobatchLocalInfo(mbMetadata, data, localEdge, metaKeyValueMap);
//			fog.getMbIdParentFogMap().put(mbMetadata.getMbId(), parentFog); TODO : Check why was parentFog being stored
			LOGGER.info("MicrobatchId : " + mbMetadata.getMbId() + ", putNext, endTime=" + System.currentTimeMillis());

		}
		
		long putEndSessionTime = System.nanoTime();	
		return wrResponse;
	}
	
	public WriteResponse putDataEdge(Metadata mbMetadata, short version, ByteBuffer data, WritePreference preference,
			Map<String, List<String>> metaKeyValueMap, String clientId, Fog fog, EdgeInfo localEdge) {
		WriteResponse wrResponse = new WriteResponse();
		wrResponse.setStatus(Constants.FAILURE);
		LOGGER.info("[PUT DATA EDGE] for the local fog put case");

		/** Set the version of the block to zero **/
		version = 0;

		LOGGER.info("MicrobatchId : " + mbMetadata.getMbId() + ", putNext, startTime=" + System.currentTimeMillis());
		TTransport transport = new TFramedTransport(new TSocket(localEdge.getNodeIp(), localEdge.getPort()));
		try {
			transport.open();
		} catch (TTransportException e) {
			transport.close();
			LOGGER.error("Unable to contact edge device : " + localEdge);
			e.printStackTrace();
			return wrResponse;
		}

		TProtocol protocol = new TBinaryProtocol(transport);
		EdgeService.Client edgeClient = new EdgeService.Client(protocol);
		long putStartTime= System.nanoTime();
		try {
			wrResponse = edgeClient.write(mbMetadata.getMbId(), mbMetadata, data);
		} catch (TException e) {
			LOGGER.info("Error while writing microbatch to edge : " + localEdge);
			e.printStackTrace();
			return wrResponse;
		} finally {
			transport.close();

			/** Update the remote fog's local index **/
			FogServiceHandler.updateMicrobatchLocalInfo(mbMetadata, data, localEdge, metaKeyValueMap);
//			fog.getMbIdParentFogMap().put(mbMetadata.getMbId(), parentFog); TODO : Check why was parentFog being stored
			LOGGER.info("MicrobatchId : " + mbMetadata.getMbId() + ", putNext, endTime=" + System.currentTimeMillis());

		}
		long putEndTime = System.nanoTime();

		return wrResponse;
	}

}
