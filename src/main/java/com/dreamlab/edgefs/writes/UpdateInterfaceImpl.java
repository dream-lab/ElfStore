package com.dreamlab.edgefs.writes;

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

import com.dreamlab.edgefs.controlplane.Fog;
import com.dreamlab.edgefs.iface.UpdateInterface;
import com.dreamlab.edgefs.misc.Constants;
import com.dreamlab.edgefs.thrift.FindReplica;
import com.dreamlab.edgefs.thrift.FogService;
import com.dreamlab.edgefs.thrift.Metadata;
import com.dreamlab.edgefs.thrift.NodeInfoData;
import com.dreamlab.edgefs.thrift.WriteResponse;

public class UpdateInterfaceImpl implements UpdateInterface {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateInterfaceImpl.class);
	private List<FindReplica> writeLocations = null;
	private WriteResponse wrResponse = new WriteResponse();
	private boolean fogHintCaptured = false;
	private NodeInfoData fogHint = null;

	public void setWriteLocation(List<FindReplica> writeLocations) {
		if (null != writeLocations) {
			this.writeLocations = writeLocations;
		} else {
			this.writeLocations = new ArrayList<FindReplica>();
		}
		LOGGER.info("[UPDATE-BLOCK] Write locations are " + writeLocations);
	}

	@Override
	public WriteResponse updateBlock(long mbId, Metadata mbMetadata, ByteBuffer mbData, String clientId,
			boolean updateMetaFlag, boolean updateBlockFlag, Fog fog) {

		WriteResponse wrResponse = new WriteResponse();
		wrResponse.setStatus(Constants.FAILURE);

		if (mbMetadata == null) {
			LOGGER.error("No metadata supplied while writing");
			return wrResponse;
		}

		wrResponse = updateBlockAll(mbId, mbMetadata, mbData, clientId, updateMetaFlag, updateBlockFlag, fog);

		return wrResponse;
	}

	private WriteResponse updateBlockAll(long mbId, Metadata mbMetadata, ByteBuffer mbData, String clientId,
			boolean updateMetaFlag, boolean updateBlockFlag, Fog fog) {

		wrResponse = new WriteResponse(Constants.FAILURE);
		fogHintCaptured = false;
		fogHint = null;

		List<Runnable> myRunnableList = new ArrayList<Runnable>();

		/** This has to be multi-threaded **/
		for (FindReplica fogWriteLocation : writeLocations) {

			if (fogWriteLocation.getNode().nodeId == fog.getMyFogInfo().getNodeID()) {

				Runnable myRunnable = new Runnable() {

					@Override
					public void run() {
						UpdateLocal updateObj = new UpdateLocal();
						wrResponse = updateObj.updateBlock(mbId, mbMetadata, mbData, clientId, updateMetaFlag,
								updateBlockFlag, fog);
						fogHint = fogWriteLocation.getNode();
						fogHintCaptured = true;
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
						}

						TProtocol protocol = new TBinaryProtocol(transport);
						FogService.Client fogClient = new FogService.Client(protocol);

						try {
							wrResponse = fogClient.updateBlockAndMeta(mbId, mbMetadata, mbData, clientId,
									updateMetaFlag, updateBlockFlag);
						} catch (TException e) {
							LOGGER.info("Error while writing microbatch to Fog : " + fogWriteLocation.getNode().nodeId);
							e.printStackTrace();
						} finally {
							transport.close();
						}

						if (false == fogHintCaptured) {
							fogHint = fogWriteLocation.getNode();
							fogHintCaptured = true;
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

		LOGGER.info("[UPDATE BLOCK] Parallel updates done");
		return wrResponse;
	}

}