package com.dreamlab.edgefs.misc;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamlab.edgefs.controlplane.Fog;
import com.dreamlab.edgefs.model.StreamMetadataComparator;
import com.dreamlab.edgefs.thrift.ByteTypeStreamMetadata;
import com.dreamlab.edgefs.thrift.DoubleTypeStreamMetadata;
import com.dreamlab.edgefs.thrift.SQueryRequest;

public class StaticStreamMetaComparator {

	private static final Logger LOGGER = LoggerFactory.getLogger(StreamMetadataComparator.class);
	private static final String FIELD_VALUE = "value";
	private static final String FIELD_UPDATABLE = "updatable";
	private static final String FIELD_VERSION = "version";
	private static final String FIELD_OWNER = "owner";
	private static final String FIELD_ENDTIME = "endTime";
	private static final String FIELD_OTHER_PROPS = "otherProperties";

	public static Set<String> retrieveStreamId(SQueryRequest squery, Fog fog) {

		/** Set of stream ids **/
		Set<String> myStreamIdSet = new HashSet<String>(fog.getStreamMbIdMap().keySet()) ;
		LOGGER.info("BEFORE : The total streams registered are "+myStreamIdSet.toString());

		Field[] fields = SQueryRequest.class.getDeclaredFields();
		for (Field fd : fields) {

			try {
				if (fd.getName().startsWith("_"))
					continue;
				// no need to match the static fields
				if (Modifier.isStatic(fd.getModifiers()))
					continue;

				Object myObj = fd.get(squery);
				if (myObj == null) {
					continue;
				}
				Field valueField = fd.getType().getDeclaredField(FIELD_VALUE);

				/** Min replica **/
				if (fd.getName().compareToIgnoreCase(("minReplica")) == 0) {

					String myStr = valueField.get(myObj) + "";
					Integer myInt = Integer.valueOf(myStr);
					Set<String> streamIdSet = fog.getStreamMetaStreamIdMap().get(myStr);
					myStreamIdSet.retainAll(streamIdSet);
				}

				/** Reliability **/
				if (fd.getName().compareToIgnoreCase(("reliability")) == 0) {

					String myStr = valueField.get(myObj) + "";
					Set<String> streamIdSet = fog.getStreamMetaStreamIdMap().get(myStr);
					myStreamIdSet.retainAll(streamIdSet);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		LOGGER.info("AFTER : The mystream id set is " + myStreamIdSet.toString());
		return myStreamIdSet;
	}

	public static void main(String[] args) {
		SQueryRequest myReq = new SQueryRequest();
		myReq.setMinReplica(new ByteTypeStreamMetadata((byte) 2, false));
		myReq.setReliability(new DoubleTypeStreamMetadata(0.99, false));

		Fog myFog = new Fog();
		myFog.setStreamMbIdMap(new HashMap<String, Set<Long>>());
		myFog.getStreamMbIdMap().put("streamid1", null);
		myFog.getStreamMbIdMap().put("streamid2", null);
		myFog.getStreamMbIdMap().put("streamid3", null);

		ArrayList<String> streamIdList = new ArrayList<String>();
		streamIdList.add("streamid3");
		streamIdList.add("streamid2");

		myFog.setStreamMetaStreamIdMap(new ConcurrentHashMap<String, Set<String>>());
		myFog.getStreamMetaStreamIdMap().put(""+2, new HashSet<String>(streamIdList));
		
		streamIdList.clear();
		streamIdList.add("streamid3");
		streamIdList.add("streamid1");
		myFog.getStreamMetaStreamIdMap().put("0.99", new HashSet<String>(streamIdList));

		retrieveStreamId(myReq, myFog);
	}

}
