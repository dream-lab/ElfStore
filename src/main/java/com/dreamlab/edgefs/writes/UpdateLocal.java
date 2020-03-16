package com.dreamlab.edgefs.writes;

import java.nio.ByteBuffer;
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
import com.dreamlab.edgefs.thrift.NodeInfoData;
import com.dreamlab.edgefs.thrift.WriteResponse;

public class UpdateLocal {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateLocal.class);

	public WriteResponse updateBlock(long mbId, Metadata mbMetadata, ByteBuffer mbData, String clientId,
			boolean updateMetaFlag, boolean updateDataFlag, Fog fog) {

		WriteResponse wrResponse = new WriteResponse();
		wrResponse.setStatus(Constants.FAILURE);

		boolean writeData = false;
		long updateStartTime = System.nanoTime();
		Map<Short, Byte> edgeMap = fog.getMbIDLocationMap().get(mbId);
		if (edgeMap != null) {
			for (Short edgeId : edgeMap.keySet()) {
				if (writeData) {
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
							LOGGER.info("MicrobatchId : " + mbId + ", read, endTime=" + System.currentTimeMillis()
									+ ",status=0");
							continue;
						}
						TProtocol protocol = new TBinaryProtocol(transport);
						EdgeService.Client edgeClient = new EdgeService.Client(protocol);
						try {
							wrResponse = edgeClient.update(mbId, mbMetadata, mbData);
							writeData = true;

							/** TODO : Increment the version here 
							 * Uncomment and check these once
							 * **/
							short version = 0;

//							if (fog.getMbIDLocationMap().containsKey(mbMetadata.getMbId())) {
//								version = fog.getMbIdVersionMap().get(mbId);
//								version = (short) (version + (short) 1);
//								LOGGER.info("DEBUG : The version of the updated block is " + version);
//								fog.getMbIdVersionMap().put(mbMetadata.getMbId(), version);
//							}

							byte[] fogBFilter = fog.getPersonalBloomFilter();
							byte[] fogDynamicBFilter = fog.getPersonalDynamicFilter();
							byte[] edgeBFilter = fog.getEdgeBloomFilters().get(edgeInfo.getNodeId());
							FogServiceHandler.updateFogAndEdgeBloomFilters(Constants.MICROBATCH_METADATA_ID,
									String.valueOf(mbMetadata.getMbId()), fogBFilter, edgeBFilter);
							FogServiceHandler.updateDymanicBloomFilter(mbMetadata.getMbId() + "", version + "",
									fogDynamicBFilter);

						} catch (TException e) {
							LOGGER.info("Error while writing microbatch from edge : " + edgeInfo);
							e.printStackTrace();
						} finally {
							transport.close();
						}
					}
				}
			}
		} /** end of the if condition edgeMap != null **/
		long updateEndTime = System.nanoTime();

		return wrResponse;
	}
}
