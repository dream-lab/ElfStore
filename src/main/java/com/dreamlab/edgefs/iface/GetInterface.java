package com.dreamlab.edgefs.iface;

import com.dreamlab.edgefs.controlplane.Fog;
import com.dreamlab.edgefs.thrift.ReadReplica;

public interface GetInterface {
	
	/** Read the data which is present locally, from an Edge**/
	public ReadReplica readLocal(long microbatchId, boolean fetchMetadata, String compFormat, long uncompSize,
			String clientId,Fog fog);
	
	/** Read the data remotely from a different Fog **/
	public ReadReplica readRemote(long microbatchId, boolean fetchMetadata, String compFormat, long uncompSize,
			String clientId, String fogIp, int fogPort);
}
