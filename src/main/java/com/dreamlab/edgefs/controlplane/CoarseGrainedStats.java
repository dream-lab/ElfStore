package com.dreamlab.edgefs.controlplane;

import java.io.Serializable;

import com.dreamlab.edgefs.misc.Constants;

//this information for the Fog contains s1, s2, s3 followed by
//r1, r2, r3 followed by a, b, c, d. Each is represented by a
//byte and this information is exchanged by the Fog with other
//Fogs (not going into detail on if it is sent only to its neighbors
// + buddies OR every other Fog in the system
public class CoarseGrainedStats implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4055326585281246401L;
	
	//for s1, s2 and s3 , we can keep the last bit as an indicator
	//for the unit of storage used. For instance 0 for byte, 1 for
	//kB, 2 for MB, 3 for GB. To represent in this way, 2 bits are 
	//needed for depicting the unit of storage. We will represent
	//these two bits as the most significant bits in the storage byte
	//Using some form of encoding as used in interaction between Edge
	//and Fog can be used here
	//12 bytes are used for s1,s2,s3,r1,r2,r3,a,b,c,d
	//removed the 2 bits for FogId as I am sending those 2 bits as i16
	//in MessagePayload, check FogServices.thrift
	private byte[] info = new byte[Constants.STATS_BYTES];
	
	public CoarseGrainedStats() {
		
	}
	
	public CoarseGrainedStats(byte[] b) {
		this();
		this.info = b;
	}

	public byte[] getInfo() {
		return info;
	}

	public void setInfo(byte[] info) {
		this.info = info;
	}
	
	public String toString() {
		for(int i=0;i<10;i++) {
			System.out.println(": "+info[i]);
		}
		return "Hello";
		
	}
	
}
