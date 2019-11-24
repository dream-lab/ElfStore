package com.dreamlab.edgefs.model;

public class FindConditionTuple<T,V> {
	
	private final T key;
	private final V value;
	private String operator;
	
	public FindConditionTuple(T key, V value) {
		this.key = key;
		this.value = value;
	}
	
	public T getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}
	
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
}
