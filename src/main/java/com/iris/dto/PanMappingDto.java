package com.iris.dto;

import java.io.Serializable;
import java.util.Date;

import com.iris.model.PanMaster;
import com.iris.model.UserMaster;

public class PanMappingDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6473124537179910111L;

	private Long panMappingId;

	private Long dummyPanId;

	private String dummyPanNumber;

	private String dummyPanBorrowerName;

	private String actualPanBorrowerName;

	private Long actualPanId;

	private String actualPanNumber;

	private String createdBy;

	private Long createdOn;

	private Long requestedByEntityId;

	private String comment;

	private String approvedByUserName;

	private Long approvedOn;

	private Boolean isActive;

	private String status;

	private String entityListUsedForFilling;

	private String entityCode;

	private Integer approvedByFk;

	private String[] panList;

	public String[] getPanList() {
		return panList;
	}

	public void setPanList(String[] panList) {
		this.panList = panList;
	}

	public Integer getApprovedByFk() {
		return approvedByFk;
	}

	public void setApprovedByFk(Integer approvedByFk) {
		this.approvedByFk = approvedByFk;
	}

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	public Long getPanMappingId() {
		return panMappingId;
	}

	public void setPanMappingId(Long panMappingId) {
		this.panMappingId = panMappingId;
	}

	public Long getDummyPanId() {
		return dummyPanId;
	}

	public void setDummyPanId(Long dummyPanId) {
		this.dummyPanId = dummyPanId;
	}

	public String getDummyPanNumber() {
		return dummyPanNumber;
	}

	public void setDummyPanNumber(String dummyPanNumber) {
		this.dummyPanNumber = dummyPanNumber;
	}

	public Long getActualPanId() {
		return actualPanId;
	}

	public void setActualPanId(Long actualPanId) {
		this.actualPanId = actualPanId;
	}

	public String getActualPanNumber() {
		return actualPanNumber;
	}

	public void setActualPanNumber(String actualPanNumber) {
		this.actualPanNumber = actualPanNumber;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}

	public Long getRequestedByEntityId() {
		return requestedByEntityId;
	}

	public void setRequestedByEntityId(Long requestedByEntityId) {
		this.requestedByEntityId = requestedByEntityId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getApprovedByUserName() {
		return approvedByUserName;
	}

	public void setApprovedByUserName(String approvedByUserName) {
		this.approvedByUserName = approvedByUserName;
	}

	public Long getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(Long approvedOn) {
		this.approvedOn = approvedOn;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEntityListUsedForFilling() {
		return entityListUsedForFilling;
	}

	public void setEntityListUsedForFilling(String entityListUsedForFilling) {
		this.entityListUsedForFilling = entityListUsedForFilling;
	}

	public String getDummyPanBorrowerName() {
		return dummyPanBorrowerName;
	}

	public void setDummyPanBorrowerName(String dummyPanBorrowerName) {
		this.dummyPanBorrowerName = dummyPanBorrowerName;
	}

	public String getActualPanBorrowerName() {
		return actualPanBorrowerName;
	}

	public void setActualPanBorrowerName(String actualPanBorrowerName) {
		this.actualPanBorrowerName = actualPanBorrowerName;
	}

	@Override
	public String toString() {
		return "PanMappingDto [panMappingId=" + panMappingId + ", dummyPanId=" + dummyPanId + ", dummyPanNumber=" + dummyPanNumber + ", actualPanId=" + actualPanId + ", actualPanNumber=" + actualPanNumber + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", requestedByEntityId=" + requestedByEntityId + ", comment=" + comment + ", approvedByUserName=" + approvedByUserName + ", approvedOn=" + approvedOn + ", isActive=" + isActive + ", status=" + status + ", entityListUsedForFilling=" + entityListUsedForFilling + "]";
	}

}
