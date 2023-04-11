package com.iris.sdmx.status.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.iris.sdmx.util.SdmxValidations;

@Entity
@Table(name = "TBL_ACTION")
public class ActionStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6695990184449061077L;

	@Id
	@Column(name = "ACTION_ID")
	private Long actionId;

	@Column(name = "ACTION_NAME")
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