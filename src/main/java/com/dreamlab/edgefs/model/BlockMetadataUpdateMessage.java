package com.dreamlab.edgefs.model;

/**
 * Just an easy way to separate the failure from the success cases for
 * stream metadata update operations. The success ones will have a code
 * of > 0 while failure ones will have < 0
 *
 */
public enum BlockMetadataUpdateMessage {

	FAIL_NULL_METADATA("Null metadata not allowed", (byte) -6),
	FAIL_NOT_EXISTS("The fog doesn't have the stream", (byte) -5),
	FAIL_NOT_OWNER("Update can happen only at the Fog where the stream was created", (byte) -4),
	//this means that the client has no lock currently. This happens as the client was not 
	//the last holder of the lock so it has to make an open() call to get the lock
	FAIL_NO_LOCK("Block writes are allowed only when holding the stream lock", (byte) -3),
	//this means that lease time of the lock has exceeded the hard lease time but no other
	//client has acquired the lock, so renew can be invoked
	FAIL_LEASE_EXPIRED("Lease expired but renew can happen", (byte) -2),
	FAIL_BLOCK_NUMBER_EXISTS("This block number already exists", (byte) -1),
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
