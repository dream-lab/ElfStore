package com.dreamlab.edgefs.iface;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import com.dreamlab.edgefs.controlplane.Fog;
import com.dreamlab.edgefs.thrift.Metadata;
import com.dreamlab.edgefs.thrift.WriteResponse;

public interface PutInterface {
	
	public WriteResponse putNext(Metadata mbMetadata, short version, ByteBuffer data, 
			Map<String, List<String>> metaKeyValueMap,String clientId, Fog fog);
	
	/** public WriteResponse putNextCausal(Metadata mbMetadata, short version, ByteBuffer data, WritePreference preference,
			Map<String, String> metaKeyValueMap, NodeInfoData parentFog, String clientid, SessionLog sessionLogInstance,
			PutPreference putpref, Fog fog); **/
}
