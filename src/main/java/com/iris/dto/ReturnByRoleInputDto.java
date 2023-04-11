/**
 * 
 */
package com.iris.dto;

import java.io.Serializable;

/**
 * @author Sagar Jadhav
 *
 */
public class ReturnByRoleInputDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3946363657684345901L;

	private String langCode;

	private Boolean isActive;

	private Long userId;

	private Long roleId;

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
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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
	 *
	 */
	@Override
	public String toString() {
		return "ReturnByRoleInputDto [langCode=" + langCode + ", isActive=" + isActive + ", userId=" + userId + ", roleId=" + roleId + "]";
	}

}
