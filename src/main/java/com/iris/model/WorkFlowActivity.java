package com.iris.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name = "TBL_WORKFLOW_ACTIVITY")
@JsonInclude(Include.NON_NULL)
public class WorkFlowActivity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -990953598975382583L;

	@Id
	@Column(name = "ACTIVITY_ID")
	private Long activityId;

	@Column(name = "ACTIVITY_LABEL")
	private String activityDesc;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "APPLICABLE_FOR_ENTITY")
	private Boolean isApplicableForEntity;

	@Column(name = "APPLICABLE_FOR_DEPT")
	private Boolean isApplicableForDept;

	@OneToMany(mappedBy = "workFlowActivity")
	private List<UserRoleActivityMap> userRoleActivityMapping;

	/**
	 * @return the userRoleActivityMapping
	 */
	public List<UserRoleActivityMap> getUserRoleActivityMapping() {
		return userRoleActivityMapping;
	}

	/**
	 * @param userRoleActivityMapping the userRoleActivityMapping to set
	 */
	public void setUserRoleActivityMapping(List<UserRoleActivityMap> userRoleActivityMapping) {
		this.userRoleActivityMapping = userRoleActivityMapping;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public String getActivityDesc() {
		return activityDesc;
	}

	public void setActivityDesc(String activityDesc) {
		this.activityDesc = activityDesc;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsApplicableForEntity() {
		return isApplicableForEntity;
	}

	public void setIsApplicableForEntity(Boolean isApplicableForEntity) {
		this.isApplicableForEntity = isApplicableForEntity;
	}

	public Boolean getIsApplicableForDept() {
		return isApplicableForDept;
	}

	public void setIsApplicableForDept(Boolean isApplicableForDept) {
		this.isApplicableForDept = isApplicableForDept;
	}

}