package com.dreamlab.edgefs.management;

/**
 * 
 * @author swamiji
 *
 */

public class MicrobatchMetadata {
	
	private String streamId;
	private String timeStamp;
	
	public MicrobatchMetadata(String argStreamId, String argTimeStamp) {
		streamId = argStreamId;
		timeStamp = argTimeStamp;
	}

	public String getTimeStamp() {
		return timeStamp;
	}
	
	public String getStreamId() {
		return streamId;
	}
}
