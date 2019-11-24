package com.dreamlab.edgefs.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.dreamlab.edgefs.thrift.FindQueryCondition;

public class FindQueryStructure {

	private List<List<FindConditionTuple<String, String>>> findQueryStruct = null;
	private Iterator<List<FindConditionTuple<String, String>>> conditionIter = null;

	public FindQueryStructure(List<List<FindQueryCondition>> findQueryList) {
		findQueryStruct = createFindQueryStruct(findQueryList);
		conditionIter = findQueryStruct.iterator();
	}

	private List<List<FindConditionTuple<String, String>>> createFindQueryStruct(
			List<List<FindQueryCondition>> andQueryList) {

		if (null == andQueryList) {
			andQueryList = new ArrayList<List<FindQueryCondition>>();
		}

		List<List<FindConditionTuple<String, String>>> andConditionList = new ArrayList<List<FindConditionTuple<String, String>>>();

		/** Iterate over the conditions of the AND list **/
		for (int i = 0; i < andQueryList.size(); i++) {

			List<FindQueryCondition> orConditionList = andQueryList.get(i);
			if (null == orConditionList || 0 == orConditionList.size())
				continue;

			/** Iterate over the OR condition **/
			List<FindConditionTuple<String, String>> orConditionTupleList = new ArrayList<FindConditionTuple<String, String>>();
			for (int j = 0; j < orConditionList.size(); j++) {

				FindConditionTuple<String, String> orConditionTuple = new FindConditionTuple<String, String>(
						orConditionList.get(j).getKey(), orConditionList.get(j).getValue());

				orConditionTupleList.add(orConditionTuple);
			}

			andConditionList.add(orConditionTupleList);
		}

		return andConditionList;
	}

	public Iterator<List<FindConditionTuple<String, String>>> getIterator() {
		return conditionIter;
	}

	@Override
	public String toString() {
		return "FindQueryStructure [findQueryStruct=" + findQueryStruct + ", conditionIter=" + conditionIter + "]";
	}

}
