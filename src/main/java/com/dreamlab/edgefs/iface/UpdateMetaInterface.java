package com.dreamlab.edgefs.iface;

import java.util.Map;

import com.dreamlab.edgefs.controlplane.Fog;
import com.dreamlab.edgefs.thrift.WriteResponse;

public interface UpdateMetaInterface {

	public WriteResponse updateMetadataForBlock(long blockId, Map<String, String> metaKeyValueMap, String clientId,
			Fog fog);

	/**
	 * public WriteResponse updateMetadataForBlockCausal(long blockId, Map<String,
	 * String> metaKeyValueMap, SessionLog sessionLogInstance, String clientId,
	 * PutPreference putpref, Fog fog);
	 **/
}
