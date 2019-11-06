package com.dreamlab.edgefs.iface;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dreamlab.edgefs.thrift.MatchPreference;

public interface FindQueryComparatorIFace {
	
public Set<Long> performMatching(Map<String, String> metaKeyValueMap,Map<String, List<Long>> metaToMbidMap, MatchPreference matchpref);

}
