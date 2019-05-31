package com.dreamlab.edgefs.model;

public enum StreamLeaseRenewalCode {

	FAIL_NOT_EXISTS((byte) -4),
	FAIL_NOT_OWNER((byte) -3),
	FAIL_LOCK_ACQUIRED((byte) -2),
	FAIL_LEASE_EXPIRED((byte) -1),
	SUCCESS((byte) 1);
	
	private byte code;
	
	private StreamLeaseRenewalCode(byte c) {
		this.code = c;
	}
	
	public byte getCode() {
		return code;
	}
	
}
