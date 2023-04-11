package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This is the entity class for the table TBL_WORKFLOW_ACTIVITY.
 * 
 * @author sanjayv
 * @date 16/03/2020
 */
@Entity
@Table(name = "TBL_ACTIVITY_APPLICABLE_MENU")
@JsonInclude(Include.NON_NULL)
public class ActivityApplicableMenu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -990953598975382583L;

	@Id
	@Column(name = "ACTIVITY_APPLICABLE_MENU_ID")
	private Long activityApplicableMenuId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACTIVITY_ID_FK")
	private WorkFlowActivity activityIdFk;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "MENU_ID_FK")
	private Long menuIdFk;

	public Long getActivityApplicableMenuId() {
		return activityApplicableMenuId;
	}

	public void setActivityApplicableMenuId(Long activityApplicableMenuId) {
		this.activityApplicableMenuId = activityApplicableMenuId;
	}

	public WorkFlowActivity getActivityIdFk() {
		return activityIdFk;
	}

	public void setActivityIdFk(WorkFlowActivity activityIdFk) {
		this.activityIdFk = activityIdFk;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Long getMenuIdFk() {
		return menuIdFk;
	}

	public void setMenuIdFk(Long menuIdFk) {
		this.menuIdFk = menuIdFk;
	}

}