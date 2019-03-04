package com.dreamlab.edgefs.model;

public class NeighborInfo {
	private NodeInfo node;
	//this poolId information is present in the NodeInfo as well
	//Need to sort out this mess
	private short buddyPoolId;
	private double poolReliability;
	private short poolSize;
	
	public NeighborInfo() {
	}

	public NeighborInfo(NodeInfo node, short buddyPoolId, double poolReliability, short poolSize) {
		super();
		this.node = node;
		this.buddyPoolId = buddyPoolId;
		this.poolReliability = poolReliability;
		this.poolSize = poolSize;
	}
	
	public NeighborInfo(NodeInfo nodeInfo) {
		super();
		this.node = nodeInfo;
	}

	public NodeInfo getNode() {
		return node;
	}

	public void setNode(NodeInfo node) {
		this.node = node;
	}

	public short getBuddyPoolId() {
		return buddyPoolId;
	}

	public void setBuddyPoolId(short buddyPoolId) {
		this.buddyPoolId = buddyPoolId;
	}

	public double getPoolReliability() {
		return poolReliability;
	}

	public void setPoolReliability(double poolReliability) {
		this.poolReliability = poolReliability;
	}

	public short getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(short poolSize) {
		this.poolSize = poolSize;
	}
}
