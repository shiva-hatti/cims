/**
 * 
 */
package com.iris.dto;

import java.io.Serializable;

/**
 * @author Siddique
 *
 */
public class GroupMasterBulkUploadDto implements Serializable {

	private static final long serialVersionUID = -4040569581268817266L;

	private Long id;

	private String fileName;

	private Boolean isActive;

	private Long createdBy;

	private Long createdOn;

	private Long totalRecords;

	private Long numberOfSuccessfull;

	private Long status;

	private Boolean isProcessed;

	private Long processStartTime;

	private Long processEndTime;

	private Long entityId;

	private String entityCode;

	private String originalFileName;

	private String statusDesc;

	private String userName;

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}

	public Long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public Long getNumberOfSuccessfull() {
		return numberOfSuccessfull;
	}

	public void setNumberOfSuccessfull(Long numberOfSuccessfull) {
		this.numberOfSuccessfull = numberOfSuccessfull;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Boolean getIsProcessed() {
		return isProcessed;
	}

	public void setIsProcessed(Boolean isProcessed) {
		this.isProcessed = isProcessed;
	}

	public Long getProcessStartTime() {
		return processStartTime;
	}

	public void setProcessStartTime(Long processStartTime) {
		this.processStartTime = processStartTime;
	}

	public Long getProcessEndTime() {
		return processEndTime;
	}

	public void setProcessEndTime(Long processEndTime) {
		this.processEndTime = processEndTime;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

}
