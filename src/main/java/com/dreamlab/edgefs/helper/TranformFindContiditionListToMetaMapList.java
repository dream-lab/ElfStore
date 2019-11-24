package com.dreamlab.edgefs.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dreamlab.edgefs.model.FindConditionTuple;

public class TranformFindContiditionListToMetaMapList {

	public static List<Map<String, String>> transformListToMap(List<FindConditionTuple<String,String>> orConditionList) {

		List<Map<String, String>> myMetaMapList = new ArrayList<Map<String, String>>();
		
		if(null != orConditionList) {			
			for(FindConditionTuple<String,String> condition : orConditionList) {
				Map<String, String> metaMap = new HashMap<String, String>();
				metaMap.put(condition.getKey(), condition.getValue());
				myMetaMapList.add(metaMap);
			}			
		}

		return myMetaMapList;
	}
}
