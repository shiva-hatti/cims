package com.iris.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_NSDL_PAN_VERF")
public class NSDLPanVerif implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5848941653287681950L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NSDL_PAN_VERF_ID")
	private Long nsdlPanVerifId;

	@Column(name = "ACTUAL_PAN")
	private String actualPanNumber;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "SUPPORTING_INFO")
	private String supportingInfo;

	@Column(name = "MODULE_NAME")
	private String moduleName;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "NSDL_VERIFIED_ON")
	private Date nsdlVerifiedOn;

	@Column(name = "NSDL_RESPONSE")
	private String nsdlResponse;

	@Column(name = "SUB_TASK_STATUS")
	private Boolean subTaskStatus;

	public Long getNsdlPanVerifId() {
		return nsdlPanVerifId;
	}

	public void setNsdlPanVerifId(Long nsdlPanVerifId) {
		this.nsdlPanVerifId = nsdlPanVerifId;
	}

	public String getActualPanNumber() {
		return actualPanNumber;
	}

	public void setActualPanNumber(String actualPanNumber) {
		this.actualPanNumber = actualPanNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSupportingInfo() {
		return supportingInfo;
	}

	public void setSupportingInfo(String supportingInfo) {
		this.supportingInfo = supportingInfo;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
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

	public Date getNsdlVerifiedOn() {
		return nsdlVerifiedOn;
	}

	public void setNsdlVerifiedOn(Date nsdlVerifiedOn) {
		this.nsdlVerifiedOn = nsdlVerifiedOn;
	}

	public String getNsdlResponse() {
		return nsdlResponse;
	}

	public void setNsdlResponse(String nsdlResponse) {
		this.nsdlResponse = nsdlResponse;
	}

	public Boolean getSubTaskStatus() {
		return subTaskStatus;
	}

	public void setSubTaskStatus(Boolean subTaskStatus) {
		this.subTaskStatus = subTaskStatus;
	}

}
