package com.iris.sdmx.status.bean;

import java.io.Serializable;

import com.iris.sdmx.util.SdmxValidations;

public class AdminStatusBean implements Serializable {

	private static final long serialVersionUID = -1562045721461083189L;

	private Long adminStatusId;

	private String status;

	private String statusTechCode;

	private String statusCode;

	private Boolean isActive;

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
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = SdmxValidations.trimInput(status);
	}

	/**
	 * @return the statusTechCode
	 */
	public String getStatusTechCode() {
		return statusTechCode;
	}

	/**
	 * @param statusTechCode the statusTechCode to set
	 */
	public void setStatusTechCode(String statusTechCode) {
		this.statusTechCode = SdmxValidations.trimInput(statusTechCode);
	}

	/**
	 * @return the statusCode
	 */
	public String getStatusCode() {
		return statusCode;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = SdmxValidations.trimInput(statusCode);
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
}