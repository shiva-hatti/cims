package com.iris.util.constant;

public enum FraudMasterEnum {

	CLOSED("C"), DEACTIVATED("D"), HOLD("H"), RESUME("R"), SADP("SADP"), HIVE("HIVE");

	private String activityType;

	private FraudMasterEnum(String activityType) {
		this.activityType = activityType;
	}

	/**
	 * @return the moduleCode
	 */
	public String getConstantVal() {
		return activityType;
	}

}