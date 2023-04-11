package com.iris.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author psheke
 * @date 03/12/2020
 */
public class EntityAuditorMapping implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 273164011354976606L;
	private Long entityAuditorMapId;
	private Long returnId;
	private String returnName;
	private Boolean upload = false;
	private Boolean view = false;
	private Boolean review = false;
	private String associationStatus;
	private Boolean isActive;
	private String tenureStartDate;
	private String tenureEndDate;
	private boolean updateFlag;
	private Long createdByUserId;
	private Long modifiedByUserId;
	private Long auditorId;
	private Long auditFirmId;
	private String auditFirmName;
	private String auditorName;
	private String email;
	private Long entityId;
	private String entityName;
	private String entityCode;
	private String returnCode;
	private String requestedByUserName;
	private String requestedDate;
	private String approvedByUserName;
	private String approvedOn;
	private String rejectComment;
	private Integer adminStatusId;
	private String associatedToUserName;
	private String auditorICAIMemNumber;

	public Integer getAdminStatusId() {
		return adminStatusId;
	}

	public void setAdminStatusId(Integer adminStatusId) {
		this.adminStatusId = adminStatusId;
	}

	/**
	 * @return the tenureStartDate
	 */
	public String getTenureStartDate() {
		return tenureStartDate;
	}

	/**
	 * @param tenureStartDate the tenureStartDate to set
	 */
	public void setTenureStartDate(String tenureStartDate) {
		this.tenureStartDate = tenureStartDate;
	}

	/**
	 * @return the tenureEndDate
	 */
	public String getTenureEndDate() {
		return tenureEndDate;
	}

	/**
	 * @param tenureEndDate the tenureEndDate to set
	 */
	public void setTenureEndDate(String tenureEndDate) {
		this.tenureEndDate = tenureEndDate;
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
	 * @return the associationStatus
	 */
	public String getAssociationStatus() {
		return associationStatus;
	}

	/**
	 * @param associationStatus the associationStatus to set
	 */
	public void setAssociationStatus(String associationStatus) {
		this.associationStatus = associationStatus;
	}

	/**
	 * @return the returnName
	 */
	public String getReturnName() {
		return returnName;
	}

	/**
	 * @param returnName the returnName to set
	 */
	public void setReturnName(String returnName) {
		this.returnName = returnName;
	}

	/**
	 * @return the upload
	 */
	public Boolean getUpload() {
		return upload;
	}

	/**
	 * @param upload the upload to set
	 */
	public void setUpload(Boolean upload) {
		this.upload = upload;
	}

	/**
	 * @return the view
	 */
	public Boolean getView() {
		return view;
	}

	/**
	 * @param view the view to set
	 */
	public void setView(Boolean view) {
		this.view = view;
	}

	/**
	 * @return the review
	 */
	public Boolean getReview() {
		return review;
	}

	/**
	 * @param review the review to set
	 */
	public void setReview(Boolean review) {
		this.review = review;
	}

	/**
	 * @return the returnId
	 */
	public Long getReturnId() {
		return returnId;
	}

	/**
	 * @param returnId the returnId to set
	 */
	public void setReturnId(Long returnId) {
		this.returnId = returnId;
	}

	public Long getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(Long auditorId) {
		this.auditorId = auditorId;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public boolean isUpdateFlag() {
		return updateFlag;
	}

	public void setUpdateFlag(boolean updateFlag) {
		this.updateFlag = updateFlag;
	}

	public Long getModifiedByUserId() {
		return modifiedByUserId;
	}

	public void setModifiedByUserId(Long modifiedByUserId) {
		this.modifiedByUserId = modifiedByUserId;
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

	/**
	 * @return the entityCode
	 */
	public String getEntityCode() {
		return entityCode;
	}

	/**
	 * @param entityCode the entityCode to set
	 */
	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	/**
	 * @return the returnCode
	 */
	public String getReturnCode() {
		return returnCode;
	}

	/**
	 * @param returnCode the returnCode to set
	 */
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public Long getEntityAuditorMapId() {
		return entityAuditorMapId;
	}

	public void setEntityAuditorMapId(Long entityAuditorMapId) {
		this.entityAuditorMapId = entityAuditorMapId;
	}

	public Long getCreatedByUserId() {
		return createdByUserId;
	}

	public void setCreatedByUserId(Long createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	public String getRequestedByUserName() {
		return requestedByUserName;
	}

	public void setRequestedByUserName(String requestedByUserName) {
		this.requestedByUserName = requestedByUserName;
	}

	public String getApprovedByUserName() {
		return approvedByUserName;
	}

	public void setApprovedByUserName(String approvedByUserName) {
		this.approvedByUserName = approvedByUserName;
	}

	public String getRejectComment() {
		return rejectComment;
	}

	public void setRejectComment(String rejectComment) {
		this.rejectComment = rejectComment;
	}

	public String getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(String approvedOn) {
		this.approvedOn = approvedOn;
	}

	public String getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(String requestedDate) {
		this.requestedDate = requestedDate;
	}

	public String getAssociatedToUserName() {
		return associatedToUserName;
	}

	public void setAssociatedToUserName(String associatedToUserName) {
		this.associatedToUserName = associatedToUserName;
	}

	/**
	 * @return the auditFirmId
	 */
	public Long getAuditFirmId() {
		return auditFirmId;
	}

	/**
	 * @param auditFirmId the auditFirmId to set
	 */
	public void setAuditFirmId(Long auditFirmId) {
		this.auditFirmId = auditFirmId;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the auditFirmName
	 */
	public String getAuditFirmName() {
		return auditFirmName;
	}

	/**
	 * @param auditFirmName the auditFirmName to set
	 */
	public void setAuditFirmName(String auditFirmName) {
		this.auditFirmName = auditFirmName;
	}

	/**
	 * @return the auditorName
	 */
	public String getAuditorName() {
		return auditorName;
	}

	/**
	 * @param auditorName the auditorName to set
	 */
	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	/**
	 * @return the auditorICAIMemNumber
	 */
	public String getAuditorICAIMemNumber() {
		return auditorICAIMemNumber;
	}

	/**
	 * @param auditorICAIMemNumber the auditorICAIMemNumber to set
	 */
	public void setAuditorICAIMemNumber(String auditorICAIMemNumber) {
		this.auditorICAIMemNumber = auditorICAIMemNumber;
	}

}
