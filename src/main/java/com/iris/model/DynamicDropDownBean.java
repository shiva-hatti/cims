package com.iris.model;

import com.iris.util.Validations;

public class DynamicDropDownBean {

	private Long key;
	private String value;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = Validations.trimInput(value);
	}

}