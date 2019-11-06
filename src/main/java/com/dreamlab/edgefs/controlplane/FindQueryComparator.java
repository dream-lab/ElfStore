package com.dreamlab.edgefs.controlplane;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dreamlab.edgefs.iface.FindQueryComparatorIFace;
import com.dreamlab.edgefs.thrift.MatchPreference;

public class FindQueryComparator implements FindQueryComparatorIFace {

	@Override
	public Set<Long> performMatching(Map<String, String> metaKeyValueMap, Map<String, List<Long>> metaToMbidMap,
			MatchPreference matchpref) {
		Set<Long> mbidSet = new HashSet<Long>();

		switch (matchpref) {
		case OR:
			mbidSet = orMatching(metaKeyValueMap, metaToMbidMap, matchpref);
			break;

		case AND:
			mbidSet = andMatching(metaKeyValueMap, metaToMbidMap, matchpref);
			break;
		default:
			mbidSet = andMatching(metaKeyValueMap, metaToMbidMap, matchpref);
			break;

		}
		return mbidSet;
	}

	/**
	 * 
	 * @param metaKeyValueMap
	 * @param metaToMbidMap
	 * @param matchpref
	 * @return
	 */
	private Set<Long> andMatching(Map<String, String> metaKeyValueMap, Map<String, List<Long>> metaToMbidMap,
			MatchPreference matchpref) {
		Set<Long> mbidSet = new HashSet<Long>();
		boolean firstPass = true;

		Iterator<Map.Entry<String, String>> itr = metaKeyValueMap.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, String> entry = itr.next();
			// formulate the searchKey for setMetaMbIdMap
			String searchKey = entry.getKey() + ":" + entry.getValue();
			List<Long> matchingMbIds = metaToMbidMap.get(searchKey);
			if (matchingMbIds == null) {
				matchingMbIds = new ArrayList<Long>();
			}

			if (firstPass) {
				if (matchingMbIds != null)
					mbidSet.addAll(matchingMbIds);
				firstPass = false;
			} else {
				if (matchingMbIds != null)
					mbidSet.retainAll(matchingMbIds);
			}
		}

		return mbidSet;
	}

	/**
	 * 
	 * @param metaKeyValueMap
	 * @param metaToMbidMap
	 * @param matchpref
	 * @return
	 */
	private Set<Long> orMatching(Map<String, String> metaKeyValueMap, Map<String, List<Long>> metaToMbidMap,
			MatchPreference matchpref) {
		Set<Long> mbidSet = new HashSet<Long>();

		Iterator<Map.Entry<String, String>> itr = metaKeyValueMap.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, String> entry = itr.next();
			// formulate the searchKey for setMetaMbIdMap
			String searchKey = entry.getKey() + ":" + entry.getValue();
			List<Long> matchingMbIds = metaToMbidMap.get(searchKey);
			if (matchingMbIds == null) {
				matchingMbIds = new ArrayList<Long>();
			}

			if (matchingMbIds != null)
				mbidSet.addAll(matchingMbIds);
		}

		return mbidSet;
	}
}
