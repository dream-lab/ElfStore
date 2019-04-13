package com.dreamlab.edgefs.model;

import java.io.Serializable;

public class FogInfo extends NodeInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2403644503061543976L;
	
	private double reliability;
	
	public FogInfo() {
		
	}
	
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
	
	@Override
	public String toString() {
		return "FogInfo [poolId=" + getBuddyPoolId() + "nodeId=" + getNodeID() + 
				"nodeIp=" + getNodeIP() + "]";
	}


}
