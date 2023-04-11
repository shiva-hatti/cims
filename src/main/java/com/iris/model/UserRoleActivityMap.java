package com.iris.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_USER_ROLE_ACTIVITY_MAPPING")
public class UserRoleActivityMap implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3694100260375504774L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ROLE_ACTIVITY_MAPPING_ID")
	private Long userRoleActivityMapId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACTIVITY_ID_FK")
	private WorkFlowActivity workFlowActivity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ROLE__ID_FK")
	private UserRole role;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	private UserMaster createdBy;

	@Column(name = "LAST_MODIFIED_ON")
	private Date lastmMdifiedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_MODIFIED_BY")
	private UserMaster lastModifiedBy;

	public Long getUserRoleActivityMapId() {
		return userRoleActivityMapId;
	}

	public void setUserRoleActivityMapId(Long userRoleActivityMapId) {
		this.userRoleActivityMapId = userRoleActivityMapId;
	}

	public WorkFlowActivity getWorkFlowActivity() {
		return workFlowActivity;
	}

	public void setWorkFlowActivity(WorkFlowActivity workFlowActivity) {
		this.workFlowActivity = workFlowActivity;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public UserMaster getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserMaster createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastmMdifiedOn() {
		return lastmMdifiedOn;
	}

	public void setLastmMdifiedOn(Date lastmMdifiedOn) {
		this.lastmMdifiedOn = lastmMdifiedOn;
	}

	public UserMaster getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(UserMaster lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

}
