package com.dreamlab.edgefs.model;

/**
 * Just an easy way to separate the failure from the success cases for
 * stream metadata update operations. The success ones will have a code
 * of > 0 while failure ones will have < 0
 *
 */
public enum BlockMetadataUpdateMessage {

	FAIL_NULL_METADATA("Null metadata not allowed", (byte) -4),
	FAIL_NOT_EXISTS("The fog doesn't have the stream", (byte) -3),
	FAIL_NOT_OWNER("Update can happen only at the Fog where the stream was created", (byte) -2),
	FAIL_NO_LEASE("Block writes are allowed only when holding the stream lock", (byte) -1),
	SUCCESS("Successfully updated block metadata", (byte) 1);
	
	private String message;
	private byte code;
	
	private BlockMetadataUpdateMessage(String msg, byte c) {
		this.message = msg;
		this.code = c;
	}
	
	public String getMessage() {
		return message;
	}

	public byte getCode() {
		return code;
	}
	
}
