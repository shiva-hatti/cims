package com.iris.sdmx.agency.master.bean;

import java.io.Serializable;

public class SdmxAgencyMasterBean implements Serializable, Comparable<SdmxAgencyMasterBean> {

	/**
	 * @author sdhone
	 */
	private static final long serialVersionUID = -3421914435394252408L;

	private Long agencyMasterId;
	private String agencyMasterCode;
	private String agencyMasterLabel;
	private Boolean isActive;
	private String agencyFusionName;

	/**
	 * 
	 */
	public SdmxAgencyMasterBean() {
	}

	/**
	 * @param agencyMasterCode
	 * @param agencyMasterLabel
	 */
	public SdmxAgencyMasterBean(String agencyMasterCode, String agencyMasterLabel) {
		this.agencyMasterCode = agencyMasterCode;
		this.agencyMasterLabel = agencyMasterLabel;
	}

	/**
	 * @return the agencyMasterId
	 */
	public Long getAgencyMasterId() {
		return agencyMasterId;
	}

	/**
	 * @param agencyMasterId the agencyMasterId to set
	 */
	public void setAgencyMasterId(Long agencyMasterId) {
		this.agencyMasterId = agencyMasterId;
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

	/**
	 * @return the agencyMasterLabel
	 */
	public String getAgencyMasterLabel() {
		return agencyMasterLabel;
	}

	/**
	 * @param agencyMasterLabel the agencyMasterLabel to set
	 */
	public void setAgencyMasterLabel(String agencyMasterLabel) {
		this.agencyMasterLabel = agencyMasterLabel;
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

	@Override
	public int compareTo(SdmxAgencyMasterBean o) {
		// TODO Auto-generated method stub
		return this.agencyMasterLabel.compareTo(o.agencyMasterLabel);
	}

	/**
	 * @return the agencyFusionName
	 */
	public String getAgencyFusionName() {
		return agencyFusionName;
	}

	/**
	 * @param agencyFusionName the agencyFusionName to set
	 */
	public void setAgencyFusionName(String agencyFusionName) {
		this.agencyFusionName = agencyFusionName;
	}

}
