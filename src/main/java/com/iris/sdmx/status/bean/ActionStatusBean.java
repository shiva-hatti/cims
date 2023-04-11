package com.iris.sdmx.status.bean;

import java.io.Serializable;

import com.iris.sdmx.util.SdmxValidations;

public class ActionStatusBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4719707412211028973L;

	private Long actionId;

	private String actionName;

	/**
	 * @return the actionId
	 */
	public Long getActionId() {
		return actionId;
	}

	/**
	 * @param actionId the actionId to set
	 */
	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}

	/**
	 * @return the actionName
	 */
	public String getActionName() {
		return actionName;
	}

	/**
	 * @param actionName the actionName to set
	 */
	public void setActionName(String actionName) {
		this.actionName = SdmxValidations.trimInput(actionName);
	}

}