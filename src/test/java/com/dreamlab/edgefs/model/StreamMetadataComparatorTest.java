package com.dreamlab.edgefs.model;

import org.junit.Assert;
import org.junit.Test;

import com.dreamlab.edgefs.thrift.ByteTypeStreamMetadata;
import com.dreamlab.edgefs.thrift.DoubleTypeStreamMetadata;
import com.dreamlab.edgefs.thrift.I32TypeStreamMetadata;
import com.dreamlab.edgefs.thrift.I64TypeStreamMetadata;
import com.dreamlab.edgefs.thrift.NodeInfoPrimary;
import com.dreamlab.edgefs.thrift.NodeInfoPrimaryTypeStreamMetadata;
import com.dreamlab.edgefs.thrift.StreamMetadata;

public class StreamMetadataComparatorTest {

	@Test
	public void successTest() {
		StreamMetadata metadata1 = new StreamMetadata();
		metadata1.setStreamId("test");
		long sTime = System.currentTimeMillis();
		metadata1.setStartTime(new I64TypeStreamMetadata(sTime, false));
		metadata1.setReliability(new DoubleTypeStreamMetadata(0.9, true));
		metadata1.setMinReplica(new ByteTypeStreamMetadata((byte) 2, false));
		metadata1.setMaxReplica(new ByteTypeStreamMetadata((byte) 4, true));
		metadata1.setVersion(new I32TypeStreamMetadata(0, true));
		metadata1.setOwner(new NodeInfoPrimaryTypeStreamMetadata(new NodeInfoPrimary(
				"localhost", 9090), false));
		
		StreamMetadata metadata2 = new StreamMetadata();
		metadata2.setStreamId("test");
		metadata2.setStartTime(new I64TypeStreamMetadata(sTime, false));
		metadata2.setReliability(new DoubleTypeStreamMetadata(0.91, true));
		metadata2.setMinReplica(new ByteTypeStreamMetadata((byte) 2, false));
		metadata2.setMaxReplica(new ByteTypeStreamMetadata((byte) 5, true));
		metadata2.setVersion(new I32TypeStreamMetadata(0, true));
		metadata2.setOwner(new NodeInfoPrimaryTypeStreamMetadata(new NodeInfoPrimary(
				"localhost", 9090), false));
		
		boolean result = StreamMetadataComparator.compare(metadata1, metadata2);
		Assert.assertEquals(true, result);
	}
	
	@Test
	public void failureTest() {
		StreamMetadata metadata1 = new StreamMetadata();
		metadata1.setStreamId("test");
		long sTime = System.currentTimeMillis();
		metadata1.setStartTime(new I64TypeStreamMetadata(sTime, false));
		metadata1.setReliability(new DoubleTypeStreamMetadata(0.9, true));
		metadata1.setMinReplica(new ByteTypeStreamMetadata((byte) 2, false));
		metadata1.setMaxReplica(new ByteTypeStreamMetadata((byte) 4, true));
		metadata1.setVersion(new I32TypeStreamMetadata(0, true));
		metadata1.setOwner(new NodeInfoPrimaryTypeStreamMetadata(new NodeInfoPrimary(
				"localhost", 9090), false));
		
		StreamMetadata metadata2 = new StreamMetadata();
		metadata2.setStreamId("test");
		metadata2.setStartTime(new I64TypeStreamMetadata(sTime, false));
		metadata2.setReliability(new DoubleTypeStreamMetadata(0.91, true));
		metadata2.setMinReplica(new ByteTypeStreamMetadata((byte) 3, false));
		metadata2.setMaxReplica(new ByteTypeStreamMetadata((byte) 4, true));
		metadata2.setVersion(new I32TypeStreamMetadata(0, true));
		metadata2.setOwner(new NodeInfoPrimaryTypeStreamMetadata(new NodeInfoPrimary(
				"localhost", 9090), false));
		
		boolean result = StreamMetadataComparator.compare(metadata1, metadata2);
		Assert.assertEquals(false, result);
	}
	
}
