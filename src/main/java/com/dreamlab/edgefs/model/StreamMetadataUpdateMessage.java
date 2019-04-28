package com.dreamlab.edgefs.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Just an easy way to separate the failure from the success cases for
 * stream metadata update operations. The success ones will have a code
 * of > 0 while failure ones will have < 0
 *
 */
public enum StreamMetadataUpdateMessage {

	FAIL_NULL("Null StreamMetadata not allowed", (byte) -5),
	FAIL_NOT_EXISTS("The fog doesn't have the stream", (byte) -4),
	FAIL_NOT_OWNER("Update/Open can happen only at the Fog where the stream was created", (byte) -3),
	FAIL_SEMANTIC("Some fields are not updatadable", (byte) -2),
	FAIL_VERSION_MISMATCH("Stream updates work only with the latest version", (byte) -1),
	SUCCESS("Stream metadata updated successfully", (byte) 1);
	
	private static final Map<Byte, StreamMetadataUpdateMessage> lookup
				= new HashMap<>();
	
	static {
		for(StreamMetadataUpdateMessage updateMessage : EnumSet.allOf(StreamMetadataUpdateMessage.class)) {
			lookup.put(updateMessage.getCode(), updateMessage);
		}
	}
	
	private String message;
	private byte code;
	
	private StreamMetadataUpdateMessage(String msg, byte c) {
		this.message = msg;
		this.code = c;
	}
	
	public String getMessage() {
		return message;
	}

	public byte getCode() {
		return code;
	}
	
	public static StreamMetadataUpdateMessage getInstance(byte code) {
		if(lookup.containsKey(code)) {
			return lookup.get(code);
		}
		return null;
	}
	
}
