package com.iris.util.constant;

public enum EmailPlaceholderConstants {

	// Add/Edit Category
	CATEGORY_NAME_KEY("#CATEGORY_NAME"),
	CATEGORY_CODE_KEY("#CATEGORY_CODE"),

	// Add/Edit Entity
	ENTITY_NAME_KEY("ENTITY_NAME"),
	ENTITY_CODE_KEY("#ENTITY_CODE"),
	IFSC_CODE_KEY("#IFSC_CODE"),

	// Add/Edit Regulator
	REGUALTOR_NAME_KEY("#REG_NAME"),
	REGUALTOR_CODE_KEY("#REG_CODE"),
	REGUALTOR_STATUS_KEY("#REG_STATUS"),

	// Common for all
	USER_NAME("#USER_NAME"),
	MODIFIED_BY("#MODIFIED_BY"),
	ACTION_KEY("#ACTION"),
	STATUS_KEY("#STATUS"),
	ACTIVITY_BY_KEY("#CREATED_BY"),
	RETURN_NAME_KEY("RETURN_NAME"),
	REF_END_DATE_KEY("REF_END_DATE");

	private String name;

	private EmailPlaceholderConstants(String name) {
		this.name = name;
	}

	public String getConstantVal() {
		return name;
	}

}