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

	FAIL_NOT_EXISTS("The fog doesn't have the stream", -4),
	FAIL_NOT_OWNER("Update can happen only at the Fog where the stream was created", -3),
	FAIL_SEMANTIC("Some fields are not updatadable", -2),
	FAIL_VERSION_MISMATCH("Stream updates work only with the latest version", -1),
	SUCCESS("Stream metadata updated successfully", 1);
	
	private static final Map<Integer, StreamMetadataUpdateMessage> lookup
				= new HashMap<>();
	
	static {
		for(StreamMetadataUpdateMessage updateMessage : EnumSet.allOf(StreamMetadataUpdateMessage.class)) {
			lookup.put(updateMessage.getCode(), updateMessage);
		}
	}
	
	private String message;
	private int code;
	
	private StreamMetadataUpdateMessage(String msg, int c) {
		this.message = msg;
		this.code = c;
	}
	
	public String getMessage() {
		return message;
	}

	public int getCode() {
		return code;
	}
	
	public static StreamMetadataUpdateMessage getInstance(int code) {
		if(lookup.containsKey(code)) {
			return lookup.get(code);
		}
		return null;
	}
	
}
