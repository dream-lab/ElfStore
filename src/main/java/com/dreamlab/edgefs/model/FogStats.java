package com.dreamlab.edgefs.model;

import java.io.Serializable;

import com.dreamlab.edgefs.misc.Constants;

public class FogStats implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7069595468981854614L;
	
	private long minStorage;
	private long medianStorage;
	private long maxStorage;
	private int minReliability;
	private int medianReliability;
	private int maxReliability;
	private int a; //LL
	private int b; //HL
	private int c; //LH
	private int d; //HH
	
	//attaching a NodeInfo object as well to identify whose info
	//this will be set initially and subsequent updates only change
	//the other ten values
	//this info is needed for stats as a Fog has knowledge of its
	//buddies, neighbors and subscribers but not total knowledge, so
	//keeping this is important since write may be served from a Fog
	//outside of this Fog's knowledge
	private NodeInfo nodeInfo;
	
	public FogStats() {
		
	}

	public FogStats(long minS, long medianS, long maxS, int minR, int medianR, int maxR) {
		super();
		this.minStorage = minS;
		this.medianStorage = medianS;
		this.maxStorage = maxS;
		this.minReliability = minR;
		this.medianReliability = medianR;
		this.maxReliability = maxR;
	}

	public FogStats(long minStorage, long medianStorage, long maxStorage, int minReliability, int medianReliability,
			int maxReliability, int a, int b, int c, int d) {
		super();
		this.minStorage = minStorage;
		this.medianStorage = medianStorage;
		this.maxStorage = maxStorage;
		this.minReliability = minReliability;
		this.medianReliability = medianReliability;
		this.maxReliability = maxReliability;
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	public long getMinStorage() {
		return minStorage;
	}

	public void setMinStorage(long minStorage) {
		this.minStorage = minStorage;
	}

	public long getMedianStorage() {
		return medianStorage;
	}

	public void setMedianStorage(long medianStorage) {
		this.medianStorage = medianStorage;
	}

	public long getMaxStorage() {
		return maxStorage;
	}

	public void setMaxStorage(long maxStorage) {
		this.maxStorage = maxStorage;
	}

	public int getMinReliability() {
		return minReliability;
	}

	public void setMinReliability(int minReliability) {
		this.minReliability = minReliability;
	}

	public int getMedianReliability() {
		return medianReliability;
	}

	public void setMedianReliability(int medianReliability) {
		this.medianReliability = medianReliability;
	}

	public int getMaxReliability() {
		return maxReliability;
	}

	public void setMaxReliability(int maxReliability) {
		this.maxReliability = maxReliability;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

	public int getD() {
		return d;
	}

	public void setD(int d) {
		this.d = d;
	}

	public NodeInfo getNodeInfo() {
		return nodeInfo;
	}

	public void setNodeInfo(NodeInfo nodeInfo) {
		this.nodeInfo = nodeInfo;
	}
	
	public static FogStats createInstance(byte[] arr) {
		FogStats stats = new FogStats();
		if(arr.length < Constants.STATS_BYTES) {
			//TODO::need exception handling
			System.out.println("The array must contain 10 bytes for constructing FogStats instance");
			return null;
		} else {
			stats.setMinStorage(Constants.interpretByteAsLong(arr[0]));
			stats.setMedianStorage(Constants.interpretByteAsLong(arr[1]));
			stats.setMaxStorage(Constants.interpretByteAsLong(arr[2]));
			stats.setMinReliability(arr[3]);
			stats.setMedianReliability(arr[4]);
			stats.setMaxReliability(arr[5]);
			stats.setA(arr[6]);
			stats.setB(arr[7]);
			stats.setC(arr[8]);
			stats.setD(arr[9]);
		}
		return stats;
	}
	
	public static FogStats createInstance(byte[] arr, NodeInfo nodeInfo) {
		FogStats stats = createInstance(arr);
		stats.setNodeInfo(nodeInfo);
		return stats;
	}
	
	public static byte[] getBytes(FogStats fogStats) {
		if(fogStats == null)
			return null;
		byte[] stats = new byte[Constants.STATS_BYTES];
		stats[0] = Constants.encodeLongAsByte(fogStats.getMinStorage());
		stats[1] = Constants.encodeLongAsByte(fogStats.getMedianStorage());
		stats[2] = Constants.encodeLongAsByte(fogStats.getMaxStorage());
		stats[3] = (byte) fogStats.getMinReliability();
		stats[4] = (byte) fogStats.getMedianReliability();
		stats[5] = (byte) fogStats.getMaxReliability();
		stats[6] = (byte) fogStats.getA();
		stats[7] = (byte) fogStats.getB();
		stats[8] = (byte) fogStats.getC();
		stats[9] = (byte) fogStats.getD();
		return stats;
	}

	@Override
	public String toString() {
		return "FogStats [minStorage=" + minStorage + ", medianStorage=" + medianStorage + ", maxStorage=" + maxStorage
				+ ", minReliability=" + minReliability + ", medianReliability=" + medianReliability
				+ ", maxReliability=" + maxReliability + ", a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + "]";
	}

}
