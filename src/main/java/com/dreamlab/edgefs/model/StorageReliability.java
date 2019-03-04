package com.dreamlab.edgefs.model;

import java.util.HashMap;
import java.util.Map;

public enum StorageReliability {

	LL(0), //a
	HL(1), //b
	LH(2), //c
	HH(3);  //d
	
	private static Map<Integer, StorageReliability> lookupMap = new HashMap<>();
	static {
		lookupMap.put(0, StorageReliability.LL);
		lookupMap.put(1, StorageReliability.HL);
		lookupMap.put(2, StorageReliability.LH);
		lookupMap.put(3, StorageReliability.HH);
	}
	
	private int index;
	
	StorageReliability(int idx) {
		this.index = idx;
	}
	
	public int getIndex() {
		return index;
	}
	
	public static StorageReliability getStorageReliability(int val) {
		//need exception handling
		return lookupMap.get(val);
	}
	
}
