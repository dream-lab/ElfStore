package com.dreamlab.edgefs.management;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamMetadata {

	private long startTime;
	private long endTime = Long.MAX_VALUE;
	private double reliability;
	private int minReplica;
	private int maxReplica;
	//for future extension purposes
	private Map<String, Object> properties = new HashMap<>();
	
	public StreamMetadata() {
		minReplica =1;
		maxReplica =3;
		reliability = 95;
	}
	
	public StreamMetadata(long timeStamp,int argMin,int argMax){
		startTime = timeStamp;
		minReplica = argMin;
		maxReplica = argMax;
		reliability = 95;
	}
	
	public boolean isStreamActive() {
		if(this.endTime == Long.MAX_VALUE)
			return true;
		return false;
	}
	
	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getMinReplica() {
		return minReplica;
	}

	public void setMinReplica(int minReplica) {
		this.minReplica = minReplica;
	}

	public int getMaxReplica() {
		return maxReplica;
	}

	public void setMaxReplica(int maxReplica) {
		this.maxReplica = maxReplica;
	}

	public double getReliability() {
		return reliability;
	}

	public void setReliability(double reliability) {
		this.reliability = reliability;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
}
