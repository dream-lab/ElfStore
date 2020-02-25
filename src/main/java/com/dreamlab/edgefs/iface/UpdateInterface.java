package com.dreamlab.edgefs.iface;

import java.nio.ByteBuffer;

import com.dreamlab.edgefs.controlplane.Fog;
import com.dreamlab.edgefs.thrift.Metadata;
import com.dreamlab.edgefs.thrift.WriteResponse;

public interface UpdateInterface {

	public WriteResponse updateBlock(long mbId, Metadata mbMetadata, ByteBuffer mbData, String clientId,
			boolean updateMetaFlag, boolean updateDataFlag, Fog fog);

	/**
	 * public WriteResponse updateBlockCausal(long mbId, Metadata mbMetadata,
	 * ByteBuffer mbData, SessionLog sessionLogInstance, String clientId,
	 * PutPreference putpref, Fog fog);
	 **/
}
