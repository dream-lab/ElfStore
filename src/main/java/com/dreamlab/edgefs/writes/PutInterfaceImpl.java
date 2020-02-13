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
import com.dreamlab.edgefs.iface.PutInterface;
import com.dreamlab.edgefs.misc.Constants;
import com.dreamlab.edgefs.misc.EdgeInfoDataToEdgeInfo;
import com.dreamlab.edgefs.servicehandler.FogServiceHandler;
import com.dreamlab.edgefs.thrift.FogService;
import com.dreamlab.edgefs.thrift.Metadata;
import com.dreamlab.edgefs.thrift.NodeInfoData;

import com.dreamlab.edgefs.thrift.WritableFogData;
import com.dreamlab.edgefs.thrift.WritePreference;
import com.dreamlab.edgefs.thrift.WriteResponse;

public class PutInterfaceImpl implements PutInterface {

	private List<WritableFogData> writeLocations = null;
	private static final Logger LOGGER = LoggerFactory.getLogger(PutInterfaceImpl.class);
	private NodeInfoData firstFog = new NodeInfoData();
	private WriteResponse wrResponse = new WriteResponse();
	private boolean firstWrite = false;

	public void setWriteLocation(List<WritableFogData> writeLocations) {
		this.writeLocations = writeLocations;
	}

	public NodeInfoData getFogHint() {
		return firstFog;
	}

	@Override
	public WriteResponse putNext(Metadata mbMetadata, short version, ByteBuffer data,
			Map<String, List<String>> metaKeyValueMap,String clientId, Fog fog) {

		WriteResponse wrResponse = new WriteResponse();
		wrResponse.setStatus(Constants.FAILURE);

		if (mbMetadata == null) {
			LOGGER.error("No metadata supplied while writing");
			return wrResponse;
		}

		wrResponse = putNextAll(mbMetadata, version, data, metaKeyValueMap,clientId, fog);

		return wrResponse;
	}

	private WriteResponse putNextAll(Metadata mbMetadata, short version, ByteBuffer data,
			Map<String, List<String>> metaKeyValueMap,String clientId, Fog fog) {

		LOGGER.info("[IMPL] got a call to putNextAll");
		wrResponse.setStatus(Constants.FAILURE);

		if (writeLocations == null) {
			return wrResponse;
		}

		List<Runnable> myRunnableList = new ArrayList<Runnable>();

		/** This has to be multi-threaded **/
		for (WritableFogData fogWriteLocation : writeLocations) {

			if (fogWriteLocation.getNode().nodeId == fog.getMyFogInfo().getNodeID()) {

				Runnable myRunnable = new Runnable() {

					@Override
					public void run() {
						/** Case of local fog **/
						PutLocal putLocalReplica = new PutLocal();
						wrResponse = putLocalReplica.putDataEdge(mbMetadata, version, data,
								fogWriteLocation.getPreference(), metaKeyValueMap, clientId, fog,
								EdgeInfoDataToEdgeInfo.convert(fogWriteLocation.getEdgeInfo()));

						if (false == firstWrite) {
							firstFog.nodeId = fog.getMyFogInfo().getNodeID();
							firstFog.NodeIP = fog.getMyFogInfo().getNodeIP();
							firstFog.port = fog.getMyFogInfo().getPort();

							firstWrite = true;
						}
					}
				};

				myRunnableList.add(myRunnable);

			} else {

				Runnable myRunnable = new Runnable() {
					@Override
					public void run() {
						/** Make a remote api call to other Fog for performing write **/
						TTransport transport = new TFramedTransport(new TSocket(fogWriteLocation.getNode().getNodeIP(),
								fogWriteLocation.getNode().getPort()));
						try {
							transport.open();
						} catch (TTransportException e) {
							transport.close();
							LOGGER.error("Unable to contact Fog device : " + fogWriteLocation.getNode().nodeId);
							e.printStackTrace();
							return;
						}

						TProtocol protocol = new TBinaryProtocol(transport);
						FogService.Client fogClient = new FogService.Client(protocol);
						try {
							wrResponse = fogClient.putData(mbMetadata, version, data, fogWriteLocation.getPreference(),
									metaKeyValueMap, clientId);
						} catch (TException e) {
							LOGGER.info("Error while writing microbatch to Fog : " + fogWriteLocation.getNode().nodeId);
							e.printStackTrace();
							return;
						} finally {
							transport.close();
						}

						if (false == firstWrite) {
							firstFog.nodeId = fog.getMyFogInfo().getNodeID();
							firstFog.NodeIP = fog.getMyFogInfo().getNodeIP();
							firstFog.port = fog.getMyFogInfo().getPort();

							firstWrite = true;
						}
					}
				};

				myRunnableList.add(myRunnable);

			}
		}

		/** Add all the runnables **/
		List<Thread> myThreadsList = new ArrayList<Thread>();
		for (int i = 0; i < myRunnableList.size(); i++) {
			Thread newThread = new Thread(myRunnableList.get(i));
			myThreadsList.add(newThread);
		}

		/** Start the threads **/
		for (int i = 0; i < myThreadsList.size(); i++) {
			myThreadsList.get(i).start();
		}

		/** Wait for the threads **/
		for (int i = 0; i < myThreadsList.size(); i++) {
			try {
				myThreadsList.get(i).join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		LOGGER.info("[PUT BLOCK] Parallel puts done");

		return wrResponse;
	}

}
