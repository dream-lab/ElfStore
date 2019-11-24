package com.dreamlab.edgefs.misc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TransformMapListToMapVal {

	public static <T> Map<T, T> transform(Map<T, List<T>> genericMap){
		Map<T,T> myMap = new HashMap<T, T>();
		
		if(genericMap == null) {
			return myMap;
		}
		
		Iterator<Entry<T, List<T>>> iter = genericMap.entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry<T, List<T>> entry = iter.next();
			T key = entry.getKey();
			List<T> valueList = entry.getValue();
			
		}
		
		return myMap;	
	}
}
