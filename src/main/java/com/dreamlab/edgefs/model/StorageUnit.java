package com.dreamlab.edgefs.model;

import java.util.HashMap;
import java.util.Map;

//this is how we represent the data
public enum StorageUnit {

	BYTE(0),
	KB(1),
	MB(2),
	GB(3);
	
	private static Map<Integer, StorageUnit> lookupTable = new HashMap<>();
	static {
		lookupTable.put(0, StorageUnit.BYTE);
		lookupTable.put(1, StorageUnit.KB);
		lookupTable.put(2, StorageUnit.MB);
		lookupTable.put(3, StorageUnit.GB);
	}
	
	private int value;
	
	StorageUnit(int v) {
		this.value = v;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public static StorageUnit getUnit(int val) {
		//need exception handling
		return lookupTable.get(val);
	}
	
}
