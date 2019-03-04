package com.dreamlab.edgefs.model;

public class NodeInfo {
	
	private String nodeIP;
	private short nodeID;
	private int port;
	private short buddyPoolId;
	
	public NodeInfo() {
		
	}
	
	public NodeInfo(String nodeIp, short nodeId, int port) {
		super();
		this.nodeID = nodeId;
		this.nodeIP = nodeIp;
		this.port = port;
	}
	
	public NodeInfo(String nodeIP, short nodeID, int port, short poolId) {
		super();
		this.nodeIP = nodeIP;
		this.nodeID = nodeID;
		this.port = port;
		this.buddyPoolId = poolId;
	}

	
	/*Getter and setter methods */
	public String getNodeIP() {
		return nodeIP;
	}

	public void setNodeIP(String argNodeIP) {
		nodeIP = argNodeIP;
	}

	public short getNodeID() {
		return nodeID;
	}

	public void setNodeID(short argNodeID) {
		nodeID = argNodeID;
	}

	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}


	public short getBuddyPoolId() {
		return buddyPoolId;
	}


	public void setBuddyPoolId(short buddyPoolId) {
		this.buddyPoolId = buddyPoolId;
	}


	@Override
	public String toString() {
		return "NodeInfo [NodeIP=" + nodeIP + ", NodeID=" + nodeID + ", Port=" + port + ", PoolId=" + buddyPoolId + "]";
	}
	
	

}
