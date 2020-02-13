package com.dreamlab.edgefs.reads;

import com.dreamlab.edgefs.controlplane.Fog;
import com.dreamlab.edgefs.iface.GetInterface;
import com.dreamlab.edgefs.thrift.NodeInfoData;
import com.dreamlab.edgefs.thrift.ReadReplica;

public class GetInterfaceImpl implements GetInterface {
	
	private NodeInfoData currFogData = new NodeInfoData();
	
	public GetInterfaceImpl(){
		currFogData.setNodeId((short)1);
		currFogData.setNodeIP("127.0.0.1");
		currFogData.setPort(9000);
	}

	@Override
	public ReadReplica readLocal(long microbatchId, boolean fetchMetadata, String compFormat, long uncompSize,
			String clientId, Fog fog) {
		GetLocal getObj = new GetLocal();
		ReadReplica result = getObj.readLocal(microbatchId, fetchMetadata, compFormat, uncompSize, clientId, fog);
		return result;
	}

	@Override
	public ReadReplica readRemote(long microbatchId, boolean fetchMetadata, String compFormat, long uncompSize,
			String clientId, String fogIp, int fogPort) {
		GetRemote getObj = new GetRemote();
		ReadReplica result = getObj.readRemote(microbatchId, fetchMetadata, compFormat, uncompSize, clientId, fogIp,
				fogPort, currFogData);
		return result;
	}
}