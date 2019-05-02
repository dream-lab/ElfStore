package com.dreamlab.edgefs.edge.model;

import java.io.File;

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
import com.dreamlab.edgefs.misc.EdgeConstants;
import com.dreamlab.edgefs.thrift.EdgeInfoData;
import com.dreamlab.edgefs.thrift.EdgePayload;
import com.dreamlab.edgefs.thrift.FogService;

public class Edge {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Edge.class);

	private short nodeId;
	private String ip;
	private int port;
	private byte reliability;
	private String fogIp;
	private int fogPort;
	// the path where data is stored
	private String datapath;
	// where logs are going
	private String baseLog;

	private long lastStatsSentTime = Long.MIN_VALUE;
	
	private long storage = 16000;
	
	public Edge() {
		super();
	}

	public Edge(short nodeId, String ip, int port, byte reliability, String fogIp, int fogPort, String datapath,
			String baseLog) {
		super();
		this.nodeId = nodeId;
		this.ip = ip;
		this.port = port;
		this.reliability = reliability;
		this.fogIp = fogIp;
		this.fogPort = fogPort;
		this.datapath = datapath;
		this.baseLog = baseLog;
	}

	public long getStorage() {
		return storage;
	}

	public void setStorage(long storage) {
		this.storage = storage;
	}

	public short getNodeId() {
		return nodeId;
	}

	public void setNodeId(short nodeId) {
		this.nodeId = nodeId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public byte getReliability() {
		return reliability;
	}

	public void setReliability(byte reliability) {
		this.reliability = reliability;
	}

	public String getFogIp() {
		return fogIp;
	}

	public void setFogIp(String fogIp) {
		this.fogIp = fogIp;
	}

	public int getFogPort() {
		return fogPort;
	}

	public void setFogPort(int fogPort) {
		this.fogPort = fogPort;
	}

	public String getDatapath() {
		return datapath;
	}

	public void setDatapath(String datapath) {
		this.datapath = datapath;
	}

	public String getBaseLog() {
		return baseLog;
	}

	public void setBaseLog(String baseLog) {
		this.baseLog = baseLog;
	}

	public long getLastStatsSentTime() {
		return lastStatsSentTime;
	}

	public void setLastStatsSentTime(long lastStatsSentTime) {
		this.lastStatsSentTime = lastStatsSentTime;
	}
	
	public void registerEdgeToFog() {
		//create payload first
		EdgeInfoData edgeInfoData = new EdgeInfoData(nodeId, ip, port, reliability, getSystemFreeSpaceEncoded());
		TTransport transport = new TFramedTransport(new TSocket(fogIp, fogPort));
		try {
			transport.open();
		} catch (TTransportException e) {
			transport.close();
			LOGGER.error("Error opening connection to FogIp : {} and port : {}", fogIp, fogPort);
			e.printStackTrace();
			return;
		}
		TProtocol protocol = new TBinaryProtocol(transport);
		FogService.Client fogClient = new FogService.Client(protocol);
		try {
			fogClient.edgeJoin(edgeInfoData);
		}  catch (TException e) {
			LOGGER.error("Error in joining to FogIp : {} and port : {}" + fogIp, fogPort);
			e.printStackTrace();
		} finally {
			transport.close();
		}
	}

	public void sendHeartbeatToFog() {
		//create the payload first
		EdgePayload payload = createEdgePayload();
		TTransport transport = new TFramedTransport(new TSocket(fogIp, fogPort));
		try {
			transport.open();
		} catch (TTransportException e) {
			transport.close();
			LOGGER.error("Error opening connection to FogIp : {} and port : {}", fogIp, fogPort);
			e.printStackTrace();
			return;
		}
		TProtocol protocol = new TBinaryProtocol(transport);
		FogService.Client fogClient = new FogService.Client(protocol);
		try {
			LOGGER.info("Sending heartbeat to FogIp : {} and port : {}", fogIp, fogPort);
			boolean edgeHeartBeat = fogClient.edgeHeartBeats(payload);
			if(edgeHeartBeat) {
				LOGGER.info("Received heartbeat response from FogIp : {} and port : {}", fogIp, fogPort);
			}
		}  catch (TException e) {
			LOGGER.error("Error in sending heartbeats to FogIp : {} and port : {}" + fogIp, fogPort);
			e.printStackTrace();
		} finally {
			transport.close();
		}
	}
	
	private EdgePayload createEdgePayload() {
		EdgePayload payload = new EdgePayload();
		payload.setEdgeId(nodeId);
//		long currentTime = System.currentTimeMillis();
//		if(currentTime - lastStatsSentTime >= (EdgeConstants.STORAGE_SENT_TIME*1000)) {
		payload.setEncodedStorage(getSystemFreeSpaceEncoded());
//			setLastStatsSentTime(currentTime);
//		}
		return payload;
	}

	private byte getSystemFreeSpaceEncoded()
	{
		File myFile = new File("/");
		long freeSpace = myFile.getFreeSpace()/(1024 * 1024);
		LOGGER.info("The total free space available is (in MB) " + freeSpace);
		byte encoded = Constants.encodeLongAsByte(freeSpace);
		LOGGER.info("The encoded space is " + encoded);
//		byte encoded = Constants.encodeLongAsByte(getStorage());
		return encoded;
	}
}
