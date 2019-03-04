package com.dreamlab.edgefs.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class LocalEdgeStats {

	private Long storage;
	private Integer reliability;
	
	public LocalEdgeStats() {
		
	}
	
	public LocalEdgeStats(Integer reliability) {
		super();
		this.reliability = reliability;
	}

	public LocalEdgeStats(long storage, int reliability) {
		super();
		this.storage = storage;
		this.reliability = reliability;
	}

	public Long getStorage() {
		return storage;
	}

	public void setStorage(long storage) {
		this.storage = storage;
	}

	public Integer getReliability() {
		return reliability;
	}

	public void setReliability(int reliability) {
		this.reliability = reliability;
	}
	
}
