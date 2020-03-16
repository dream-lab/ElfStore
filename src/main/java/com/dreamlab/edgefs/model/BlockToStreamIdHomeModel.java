package com.dreamlab.edgefs.model;

import com.dreamlab.edgefs.thrift.NodeInfoData;

public class BlockToStreamIdHomeModel {
	private String streamid;
	private NodeInfoData nodeInfoData;

	public BlockToStreamIdHomeModel(String streamid, NodeInfoData nodeInfoData) {
		super();
		this.streamid = streamid;
		this.nodeInfoData = nodeInfoData;
	}

	public String getStreamid() {
		return streamid;
	}

	public void setStreamid(String streamid) {
		this.streamid = streamid;
	}

	public NodeInfoData getNodeInfoData() {
		return nodeInfoData;
	}

	public void setNodeInfoData(NodeInfoData nodeInfoData) {
		this.nodeInfoData = nodeInfoData;
	}

}