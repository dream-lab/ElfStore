package com.dreamlab.edgefs.model;

public enum StreamLeaseRenewalCode {

	FAIL_NOT_EXISTS((byte) -3),
	FAIL_NOT_OWNER((byte) -2),
	FAIL_LOCK_ACQUIRED((byte) -1),
	SUCCESS((byte) 1);
	
	private byte code;
	
	private StreamLeaseRenewalCode(byte c) {
		this.code = c;
	}
	
	public byte getCode() {
		return code;
	}
	
}
