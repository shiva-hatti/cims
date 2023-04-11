package com.iris.sdmx.codelist.bean;

import java.io.Serializable;
import java.util.List;

public class CodelListMasterRequestBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8988013267117699753L;

	private Long userId;

	private Long roleId;

	private Boolean isApprovedRecord;

	private String clCode;

	private String clVersion;

	private boolean fetchClMasterRecord = false;

	private String clValueCode;

	private Long clId;

	private List<CodelListMasterRequestBean> codelListMasterRequestBean;

	private String agencyMasterCode;

	/**
	 * @return the clCode
	 */
	public String getClCode() {
		return clCode;
	}

	/**
	 * @param clCode the clCode to set
	 */
	public void setClCode(String clCode) {
		this.clCode = clCode;
	}

	/**
	 * @return the clVersion
	 */
	public String getClVersion() {
		return clVersion;
	}

	/**
	 * @param clVersion the clVersion to set
	 */
	public void setClVersion(String clVersion) {
		this.clVersion = clVersion;
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
	 * @return the isApprovedRecord
	 */
	public Boolean getIsApprovedRecord() {
		return isApprovedRecord;
	}

	/**
	 * @param isApprovedRecord the isApprovedRecord to set
	 */
	public void setIsApprovedRecord(Boolean isApprovedRecord) {
		this.isApprovedRecord = isApprovedRecord;
	}

	public Boolean getFetchClMasterRecord() {
		return fetchClMasterRecord;
	}

	public void setFetchClMasterRecord(Boolean fetchClMasterRecord) {
		this.fetchClMasterRecord = fetchClMasterRecord;
	}

	public List<CodelListMasterRequestBean> getCodelListMasterRequestBean() {
		return codelListMasterRequestBean;
	}

	public void setCodelListMasterRequestBean(List<CodelListMasterRequestBean> codelListMasterRequestBean) {
		this.codelListMasterRequestBean = codelListMasterRequestBean;
	}

	public String getClValueCode() {
		return clValueCode;
	}

	public void setClValueCode(String clValueCode) {
		this.clValueCode = clValueCode;
	}

	public Long getClId() {
		return clId;
	}

	public void setClId(Long clId) {
		this.clId = clId;
	}

	/**
	 * @return the agencyMasterCode
	 */
	public String getAgencyMasterCode() {
		return agencyMasterCode;
	}

	/**
	 * @param agencyMasterCode the agencyMasterCode to set
	 */
	public void setAgencyMasterCode(String agencyMasterCode) {
		this.agencyMasterCode = agencyMasterCode;
	}

}
