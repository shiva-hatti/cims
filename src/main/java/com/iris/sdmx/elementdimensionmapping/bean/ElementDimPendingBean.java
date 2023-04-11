package com.iris.sdmx.elementdimensionmapping.bean;

import java.io.Serializable;

public class ElementDimPendingBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long userId;

	private Long roleId;

	private String langCode;

	private String dsdCode;

	private String elementVersion;

	private Boolean isPending;

	private String agencyCode;

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
	 * @return the dsdCode
	 */
	public String getDsdCode() {
		return dsdCode;
	}

	/**
	 * @param dsdCode the dsdCode to set
	 */
	public void setDsdCode(String dsdCode) {
		this.dsdCode = dsdCode;
	}

	/**
	 * @return the elementVersion
	 */
	public String getElementVersion() {
		return elementVersion;
	}

	/**
	 * @param elementVersion the elementVersion to set
	 */
	public void setElementVersion(String elementVersion) {
		this.elementVersion = elementVersion;
	}

	/**
	 * @return the isPending
	 */
	public Boolean getIsPending() {
		return isPending;
	}

	/**
	 * @param isPending the isPending to set
	 */
	public void setIsPending(Boolean isPending) {
		this.isPending = isPending;
	}

	/**
	 * @return the agencyCode
	 */
	public String getAgencyCode() {
		return agencyCode;
	}

	/**
	 * @param agencyCode the agencyCode to set
	 */
	public void setAgencyCode(String agencyCode) {
		this.agencyCode = agencyCode;
	}

}
