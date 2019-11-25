package com.dreamlab.edgefs.iface;

import java.util.Map;

import com.dreamlab.edgefs.controlplane.Fog;
import com.dreamlab.edgefs.thrift.MatchPreference;

public interface PropagateFindQueryIFace {

	public Map<Long, String> getMbIdStreamIdMatchMapFromNeighbors(Map<String, String> metaKeyValueMap,
			Map<Long, String> currentMbIdStreamIdMap, MatchPreference matchpreference, Fog fog);

	public Map<Long, String> getMbIdStreamIdMatchMapFromBuddies(Map<String, String> metaKeyValueMap,
			Map<Long, String> currentMbIdStreamIdMap, MatchPreference matchpreference, Fog fog);
}
