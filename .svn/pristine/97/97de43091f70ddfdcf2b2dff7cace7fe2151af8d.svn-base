package com.iris.sdmx.status.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.iris.sdmx.util.SdmxValidations;

@Entity
@Table(name = "TBL_ADMIN_STATUS")
public class AdminStatus implements Serializable {

	private static final long serialVersionUID = -1562045721461083189L;

	@Id
	@Column(name = "ADMIN_STATUS_ID")
	private Long adminStatusId;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "STATUS_TECH_CODE")
	private String statusTechCode;

	@Column(name = "STATUS_CODE")
	private String statusCode;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	public AdminStatus(Long adminStatusId) {
		this.adminStatusId = adminStatusId;
	}

	public AdminStatus() {
		super();
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