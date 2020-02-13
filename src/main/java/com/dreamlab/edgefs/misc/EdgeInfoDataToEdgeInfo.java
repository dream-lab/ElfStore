package com.dreamlab.edgefs.misc;

import com.dreamlab.edgefs.model.EdgeInfo;
import com.dreamlab.edgefs.thrift.EdgeInfoData;

public class EdgeInfoDataToEdgeInfo {
	public static EdgeInfo convert(EdgeInfoData edgeInfoData) {
		EdgeInfo myEdgeInfo = new EdgeInfo(edgeInfoData.nodeId, edgeInfoData.nodeIp, edgeInfoData.port, edgeInfoData.reliability);
		return myEdgeInfo;
	}
}
