package com.iris.dto;

import java.io.Serializable;

public class FilingApprovalNotificationBean implements Serializable {

	private static final long serialVersionUID = 6392093557904018092L;

	private Long activityId;

	private Long userRoleId;

	private Long roleTypeId;

	private Long userId;

	private String userName;

	private Long entityId;

	private String entiryCode;

	private String entityName;

	public FilingApprovalNotificationBean(Long activityId, Long userRoleId, Long roleTypeId, Long userId, String userName, Long entityId, String entiryCode, String entityName) {
		this.activityId = activityId;
		this.userRoleId = userRoleId;
		this.roleTypeId = roleTypeId;
		this.userId = userId;
		this.userName = userName;
		this.entityId = entityId;
		this.entiryCode = entiryCode;
		this.entityName = entityName;
	}

	/**
	 * @return the activityId
	 */
	public Long getActivityId() {
		return activityId;
	}

	/**
	 * @param activityId the activityId to set
	 */
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	/**
	 * @return the userRoleId
	 */
	public Long getUserRoleId() {
		return userRoleId;
	}

	/**
	 * @param userRoleId the userRoleId to set
	 */
	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	/**
	 * @return the roleTypeId
	 */
	public Long getRoleTypeId() {
		return roleTypeId;
	}

	/**
	 * @param roleTypeId the roleTypeId to set
	 */
	public void setRoleTypeId(Long roleTypeId) {
		this.roleTypeId = roleTypeId;
	}

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
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the entityId
	 */
	public Long getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return the entiryCode
	 */
	public String getEntiryCode() {
		return entiryCode;
	}

	/**
	 * @param entiryCode the entiryCode to set
	 */
	public void setEntiryCode(String entiryCode) {
		this.entiryCode = entiryCode;
	}

	/**
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * @param entityName the entityName to set
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

}
