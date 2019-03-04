package com.dreamlab.edgefs.model;

public class FogInfo extends NodeInfo {
	
	@Override
	public String toString() {
		return "FogInfo [poolId=" + getBuddyPoolId() + "nodeId=" + getNodeID() + 
				"nodeIp=" + getNodeIP() + "]";
	}

	private double reliability;
	
	public FogInfo(String nodeIP, short nodeID, int port, short poolId) {
		super(nodeIP, nodeID, port, poolId);
	}

	public FogInfo(String nodeIP, short nodeID, int port, short poolId, double reliability) {
		super(nodeIP, nodeID, port, poolId);			
		this.reliability = reliability;
	}

	public double getReliability() {
		return reliability;
	}

	public void setReliability(double reliability) {
		this.reliability = reliability;
	}

}
