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

/**
 * @author Sanjayv
 * @version 1.0
 */
@Entity
@Table(name = "TBL_USER_ROLE_MODIFIED")
public class UserRoleModified implements Serializable {

	private static final long serialVersionUID = -8530115559013996649L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ROLE_MODIFIED_ID")
	private Long userRoleModifiedId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ROLE_ID_FK")
	private UserRole userRoleIdFk;

	@Column(name = "REJECT_COMMENT")
	private String rejectComment;

	@Column(name = "ADMIN_STATUS_ID_FK")
	private Integer adminStatusId_FK;

	@Column(name = "ACTION_ID_FK")
	private Integer actionId_FK;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFIED_BY_FK")
	private UserMaster userModify;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APPROVED_BY_FK")
	private UserMaster approvedBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY_FK")
	private UserMaster createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "LAST_UPDATE_ON")
	private Date lastUpdateOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_ROLE_FK")
	private UserRole createdByRole;

	@Column(name = "APPROVED_ON")
	private Date approvedOn;

	@Column(name = "USER_ROLE_DETAILS")
	private String roleDetailsJson;

	public Long getUserRoleModifiedId() {
		return userRoleModifiedId;
	}

	public void setUserRoleModifiedId(Long userRoleModifiedId) {
		this.userRoleModifiedId = userRoleModifiedId;
	}

	public UserRole getUserRoleIdFk() {
		return userRoleIdFk;
	}

	public void setUserRoleIdFk(UserRole userRoleIdFk) {
		this.userRoleIdFk = userRoleIdFk;
	}

	public String getRejectComment() {
		return rejectComment;
	}

	public void setRejectComment(String rejectComment) {
		this.rejectComment = rejectComment;
	}

	public Integer getAdminStatusId_FK() {
		return adminStatusId_FK;
	}

	public void setAdminStatusId_FK(Integer adminStatusId_FK) {
		this.adminStatusId_FK = adminStatusId_FK;
	}

	public Integer getActionId_FK() {
		return actionId_FK;
	}

	public void setActionId_FK(Integer actionId_FK) {
		this.actionId_FK = actionId_FK;
	}

	public UserMaster getUserModify() {
		return userModify;
	}

	public void setUserModify(UserMaster userModify) {
		this.userModify = userModify;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public UserMaster getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(UserMaster approvedBy) {
		this.approvedBy = approvedBy;
	}

	public UserMaster getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserMaster createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getLastUpdateOn() {
		return lastUpdateOn;
	}

	public void setLastUpdateOn(Date lastUpdateOn) {
		this.lastUpdateOn = lastUpdateOn;
	}

	public UserRole getCreatedByRole() {
		return createdByRole;
	}

	public void setCreatedByRole(UserRole createdByRole) {
		this.createdByRole = createdByRole;
	}

	public Date getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}

	public String getRoleDetailsJson() {
		return roleDetailsJson;
	}

	public void setRoleDetailsJson(String roleDetailsJson) {
		this.roleDetailsJson = roleDetailsJson;
	}

}