package com.iris.sdmx.userMangement.bean;

import java.io.Serializable;

public class ApprovalInputBean implements Serializable {

	private static final long serialVersionUID = 3036612451587128357L;

	private Long userId;

	private Long roleId;

	private String langCode;

	private Boolean isDeptAdmin;

	private Boolean isMainDept;

	private String deptCode;

	private Long actionId;

	private Long adminStatusId;

	private Long modTablePkId;

	private Long masterTablePkId;

	private String comments;

	private Long deptId;

	private String elementOwnerDeptCode;

	private Long elementId;

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the roleId
	 */
	public Long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the langCode
	 */
	public String getLangCode() {
		return langCode;
	}

	/**
	 * @param langCode the langCode to set
	 */
	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	/**
	 * @return the isDeptAdmin
	 */
	public Boolean getIsDeptAdmin() {
		return isDeptAdmin;
	}

	/**
	 * @param isDeptAdmin the isDeptAdmin to set
	 */
	public void setIsDeptAdmin(Boolean isDeptAdmin) {
		this.isDeptAdmin = isDeptAdmin;
	}

	/**
	 * @return the isMainDept
	 */
	public Boolean getIsMainDept() {
		return isMainDept;
	}

	/**
	 * @param isMainDept the isMainDept to set
	 */
	public void setIsMainDept(Boolean isMainDept) {
		this.isMainDept = isMainDept;
	}

	/**
	 * @return the deptCode
	 */
	public String getDeptCode() {
		return deptCode;
	}

	/**
	 * @param deptCode the deptCode to set
	 */
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

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
	 * @return the adminStatusId
	 */
	public Long getAdminStatusId() {
		return adminStatusId;
	}

	/**
	 * @param adminStatusId the adminStatusId to set
	 */
	public void setAdminStatusId(Long adminStatusId) {
		this.adminStatusId = adminStatusId;
	}

	/**
	 * @return the modTablePkId
	 */
	public Long getModTablePkId() {
		return modTablePkId;
	}

	/**
	 * @param modTablePkId the modTablePkId to set
	 */
	public void setModTablePkId(Long modTablePkId) {
		this.modTablePkId = modTablePkId;
	}

	/**
	 * @return the masterTablePkId
	 */
	public Long getMasterTablePkId() {
		return masterTablePkId;
	}

	/**
	 * @param masterTablePkId the masterTablePkId to set
	 */
	public void setMasterTablePkId(Long masterTablePkId) {
		this.masterTablePkId = masterTablePkId;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the deptId
	 */
	public Long getDeptId() {
		return deptId;
	}

	/**
	 * @param deptId the deptId to set
	 */
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	/**
	 * @return the elementOwnerDeptCode
	 */
	public String getElementOwnerDeptCode() {
		return elementOwnerDeptCode;
	}

	/**
	 * @param elementOwnerDeptCode the elementOwnerDeptCode to set
	 */
	public void setElementOwnerDeptCode(String elementOwnerDeptCode) {
		this.elementOwnerDeptCode = elementOwnerDeptCode;
	}

	/**
	 * @return the elementId
	 */
	public Long getElementId() {
		return elementId;
	}

	/**
	 * @param elementId the elementId to set
	 */
	public void setElementId(Long elementId) {
		this.elementId = elementId;
	}

}
