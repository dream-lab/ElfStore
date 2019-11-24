package com.dreamlab.edgefs.iface;

import java.util.List;
import java.util.Set;

import com.dreamlab.edgefs.model.FindConditionTuple;

public interface FindQueryConditionIFace {

	public Set<Long> evaluateFindQuery(List<FindConditionTuple<String,String>> findQuery);
}
