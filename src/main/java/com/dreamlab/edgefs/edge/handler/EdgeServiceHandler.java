package com.dreamlab.edgefs.edge.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import org.apache.commons.io.FileUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamlab.edgefs.edge.model.Edge;
import com.dreamlab.edgefs.misc.Constants;
import com.dreamlab.edgefs.thrift.EdgeService;
import com.dreamlab.edgefs.thrift.Metadata;
import com.dreamlab.edgefs.thrift.ReadReplica;
import com.dreamlab.edgefs.thrift.WriteResponse;

public class EdgeServiceHandler implements EdgeService.Iface {

	private static final Logger LOGGER = LoggerFactory.getLogger(EdgeServiceHandler.class);

	private Edge edge;

	public EdgeServiceHandler(Edge edge) {
		super();
		this.edge = edge;
	}

	@Override
//	public byte write(String mbId, Metadata mbMetadata, ByteBuffer mbData) throws TException {
	public WriteResponse write(String mbId, Metadata mbMetadata, ByteBuffer mbData) throws TException {
		WriteResponse wrResponse = new WriteResponse();
		wrResponse.setStatus(Constants.FAILURE);
		if (mbId != null && mbMetadata != null && mbData != null) {
			try {
				LOGGER.info(
						"MicrobatchId : " + mbMetadata.getMbId() + ", write, startTime=" + System.currentTimeMillis());

				// data
				File myFile = new File(edge.getDatapath() + "/" + mbId + ".data");
				FileUtils.writeByteArrayToFile(myFile, mbData.array());
				
				int mbSize = mbData.array().length/(1000 * 1000);
				edge.setStorage(edge.getStorage() - mbSize);

				// Metadata
				File metaFile = new File(edge.getDatapath() + "/" + mbId + ".meta");
				FileOutputStream foStream = new FileOutputStream(metaFile);
				ObjectOutputStream objStream = new ObjectOutputStream(foStream);

				objStream.writeObject(mbMetadata);
				objStream.close();
				foStream.close();

				LOGGER.info(
						"MicrobatchId : " + mbMetadata.getMbId() + ", write, endTime=" + System.currentTimeMillis());

				wrResponse.setStatus(Constants.SUCCESS);
				wrResponse.setReliability(edge.getReliability());
			} catch (IOException e) {
				LOGGER.error("Error while writing the microbatch " + e);
				e.printStackTrace();
			}
		}
		return wrResponse;
	}

	@Override
	public ReadReplica read(String mbId, byte fetchMetadata) throws TException {
		ReadReplica replica = new ReadReplica();
		replica.setStatus(Constants.FAILURE);
		File mbFile = new File(edge.getDatapath() + "/" + mbId + ".data");
		try {
			byte[] byteArray = FileUtils.readFileToByteArray(mbFile);
			if (byteArray != null) {
				replica.setData(byteArray);
			} else {
				return replica;
			}
			if (fetchMetadata == 1) {
				File metaFile = new File(edge.getDatapath() + "/" + mbId + ".meta");
				FileInputStream fiStream = new FileInputStream(metaFile);
				ObjectInputStream objStream = new ObjectInputStream(fiStream);

				try {
					Metadata mbMetadata = (Metadata) objStream.readObject();
					replica.setMetadata(mbMetadata);
				} catch (ClassNotFoundException e) {
					LOGGER.error("Microbatch metadata different from the expected format, not sending it");
					e.printStackTrace();
					return replica;
				} finally {
					objStream.close();
					fiStream.close();
				}
			}
		} catch (IOException e) {
			LOGGER.error("Error while reading the microbatchId : " + mbId);
			e.printStackTrace();
			return replica;
		}
		replica.setStatus(Constants.SUCCESS);
		return replica;
	}
	
	@Override
	public ReadReplica getMetadata(String mbId) throws TException {
		ReadReplica replica = new ReadReplica();
		replica.setStatus(Constants.FAILURE);
		File metaFile = new File(edge.getDatapath() + "/" + mbId + ".meta");
		try {
			FileInputStream fiStream = new FileInputStream(metaFile);
			ObjectInputStream objStream = new ObjectInputStream(fiStream);
			try {
				Metadata mbMetadata = (Metadata) objStream.readObject();
				replica.setMetadata(mbMetadata);
			} catch (ClassNotFoundException e) {
				LOGGER.error("Microbatch metadata different from the expected format, not sending it");
				e.printStackTrace();
				return replica;
			} finally {
				objStream.close();
				fiStream.close();
			}
		} catch (IOException ex) {
			LOGGER.error("Error while reading metadata for the microbatchId : " + mbId);
			ex.printStackTrace();
			return replica;
		}
		replica.setStatus(Constants.SUCCESS);
		return replica;
	}

	@Override
	public void zip() throws TException {

	}

	@Override
	public void pong() throws TException {

	}

	@Override
	public byte insert(ByteBuffer data) throws TException {
		return 0;
	}

	@Override
	public int add(int num1, int num2) throws TException {
		return 0;
	}

}
