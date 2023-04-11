package com.iris.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * @author sajadhav
 */
@Entity
@Table(name = "TBL_RETURN_APPROVAL_DTL")
public class ReturnApprovalDetail implements Serializable {

	private static final long serialVersionUID = -616268373665749513L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RETURN_APPROVAL_ID_FK")
	private Long returnApprovalDetailId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_UPLOAD_ID_FK")
	private ReturnsUploadDetails returnUploadDetails;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACTIVITY_ID_FK")
	private WorkFlowActivity workFlowActivity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APPROVAL_USER_ID_FK")
	private UserMaster approvedRejectedBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APPROVAL_ROLE_ID_FK")
	private UserRole userRole;

	@Column(name = "APPROVED_ON")
	private Date approvedRejectedOn;

	@Column(name = "WF_STEP")
	private int workflowStep;

	@Column(name = "IS_COMPLETE")
	private boolean isComplete;

	@Column(name = "COMMENT")
	private String comment;

	@Column(name = "REVIEW_STATUS")
	private String reviewStatus;

	@Column(name = "CREATION_TIME")
	private Date creationTime;

	@Transient
	private int filingStatusId;

	@Transient
	private Long uploadId;

	@Transient
	private Long returnId;

	@Transient
	private Long entityId;

	@Transient
	private String entityCode;

	@Transient
	private Long userRoleId;

	@Transient
	private Long userId;

	@Transient
	private String userName;

	@Transient
	private Long previousUploadId;

	@Transient
	private Long workflowId;

	@Transient
	private String uploadChannelDesc;

	@Transient
	private Long uploadChannelId;

	@Transient
	private String fileType;

	@Transient
	private String instanceFile;

	@Transient
	private String fileName;

	@Transient
	private String attachedFile;

	@Transient
	private String supportiveDocName;

	@Transient
	private String status;

	@Transient
	private String frequencyName;

	@Transient
	private Date endDate;

	@Transient
	private Date startDate;

	@Transient
	private String returnCode;

	@Transient
	private Long unlockRequestId;

	@Transient
	private Integer returnPropertyValue;

	@Transient
	private String returnName;

	@Transient
	private String entityName;

	@Transient
	private Date uploadedDate;

	@Transient
	private Long taxonomyId;

	public ReturnApprovalDetail() {

	}

	public ReturnApprovalDetail(Long uploadId, Long returnApprovalDetailId, Long returnId, Long entityId, String entityCode, Integer filingStatusId, Long userRoleId, Long userId, String userName, Long previousUploadId, Long workflowId, Integer workflowStep, String uploadChannelDesc, Long uploadChannelId, String fileType, String instanceFile, String fileName, String attachedFile, String supportiveDocName, String status, String frequencyName, Date endDate, Date startDate, String returnCode, Long unlockRequestId, Integer returnPropertyValue, String returnName, String entityName, Date uploadedDate, Long taxonomyId) {
		this.uploadId = uploadId;
		this.returnApprovalDetailId = returnApprovalDetailId;
		this.returnId = returnId;
		this.entityId = entityId;
		this.entityCode = entityCode;
		this.filingStatusId = filingStatusId;
		this.userRoleId = userRoleId;
		this.userId = userId;
		this.userName = userName;
		this.previousUploadId = previousUploadId;
		this.workflowId = workflowId;
		this.workflowStep = workflowStep;
		this.uploadChannelDesc = uploadChannelDesc;
		this.uploadChannelId = uploadChannelId;
		this.fileType = fileType;
		this.instanceFile = instanceFile;
		this.fileName = fileName;
		this.attachedFile = attachedFile;
		this.supportiveDocName = supportiveDocName;
		this.status = status;
		this.frequencyName = frequencyName;
		this.endDate = endDate;
		this.startDate = startDate;
		this.returnCode = returnCode;
		this.unlockRequestId = unlockRequestId;
		this.returnPropertyValue = returnPropertyValue;
		this.returnName = returnName;
		this.entityName = entityName;
		this.uploadedDate = uploadedDate;
		this.taxonomyId = taxonomyId;
	}

	/**
	 * @return the userRole
	 */
	public UserRole getUserRole() {
		return userRole;
	}

	/**
	 * @param userRole the userRole to set
	 */
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	/**
	 * @return the filingStatusId
	 */
	public int getFilingStatusId() {
		return filingStatusId;
	}

	/**
	 * @param filingStatusId the filingStatusId to set
	 */
	public void setFilingStatusId(int filingStatusId) {
		this.filingStatusId = filingStatusId;
	}

	/**
	 * @return the returnUploadDetails
	 */
	public ReturnsUploadDetails getReturnUploadDetails() {
		return returnUploadDetails;
	}

	/**
	 * @param returnUploadDetails the returnUploadDetails to set
	 */
	public void setReturnUploadDetails(ReturnsUploadDetails returnUploadDetails) {
		this.returnUploadDetails = returnUploadDetails;
	}

	/**
	 * @return the returnApprovalDetailId
	 */
	public Long getReturnApprovalDetailId() {
		return returnApprovalDetailId;
	}

	/**
	 * @param returnApprovalDetailId the returnApprovalDetailId to set
	 */
	public void setReturnApprovalDetailId(Long returnApprovalDetailId) {
		this.returnApprovalDetailId = returnApprovalDetailId;
	}

	/**
	 * @return the workFlowActivity
	 */
	public WorkFlowActivity getWorkFlowActivity() {
		return workFlowActivity;
	}

	/**
	 * @param workFlowActivity the workFlowActivity to set
	 */
	public void setWorkFlowActivity(WorkFlowActivity workFlowActivity) {
		this.workFlowActivity = workFlowActivity;
	}

	/**
	 * @return the approvedRejectedBy
	 */
	public UserMaster getApprovedRejectedBy() {
		return approvedRejectedBy;
	}

	/**
	 * @param approvedRejectedBy the approvedRejectedBy to set
	 */
	public void setApprovedRejectedBy(UserMaster approvedRejectedBy) {
		this.approvedRejectedBy = approvedRejectedBy;
	}

	/**
	 * @return the approvedRejectedOn
	 */
	public Date getApprovedRejectedOn() {
		return approvedRejectedOn;
	}

	/**
	 * @param approvedRejectedOn the approvedRejectedOn to set
	 */
	public void setApprovedRejectedOn(Date approvedRejectedOn) {
		this.approvedRejectedOn = approvedRejectedOn;
	}

	/**
	 * @return the workflowStep
	 */
	public int getWorkflowStep() {
		return workflowStep;
	}

	/**
	 * @param workflowStep the workflowStep to set
	 */
	public void setWorkflowStep(int workflowStep) {
		this.workflowStep = workflowStep;
	}

	/**
	 * @return the isComplete
	 */
	public boolean isComplete() {
		return isComplete;
	}

	/**
	 * @param isComplete the isComplete to set
	 */
	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the reviewStatus
	 */
	public String getReviewStatus() {
		return reviewStatus;
	}

	/**
	 * @param reviewStatus the reviewStatus to set
	 */
	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	/**
	 * @return the creationTime
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * @param creationTime the creationTime to set
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * @return the uploadId
	 */
	public Long getUploadId() {
		return uploadId;
	}

	/**
	 * @param uploadId the uploadId to set
	 */
	public void setUploadId(Long uploadId) {
		this.uploadId = uploadId;
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

	/**
	 * @return the entityId
	 */
	public Long getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
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
	 * @return the userRoleId
	 */
	public Long getUserRoleId() {
		return userRoleId;
	}

	/**
	 * @param userRoleId the userRoleId to set
	 */
	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
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
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the previousUploadId
	 */
	public Long getPreviousUploadId() {
		return previousUploadId;
	}

	/**
	 * @param previousUploadId the previousUploadId to set
	 */
	public void setPreviousUploadId(Long previousUploadId) {
		this.previousUploadId = previousUploadId;
	}

	/**
	 * @return the workflowId
	 */
	public Long getWorkflowId() {
		return workflowId;
	}

	/**
	 * @param workflowId the workflowId to set
	 */
	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}

	/**
	 * @return the uploadChannelDesc
	 */
	public String getUploadChannelDesc() {
		return uploadChannelDesc;
	}

	/**
	 * @param uploadChannelDesc the uploadChannelDesc to set
	 */
	public void setUploadChannelDesc(String uploadChannelDesc) {
		this.uploadChannelDesc = uploadChannelDesc;
	}

	/**
	 * @return the uploadChannelId
	 */
	public Long getUploadChannelId() {
		return uploadChannelId;
	}

	/**
	 * @param uploadChannelId the uploadChannelId to set
	 */
	public void setUploadChannelId(Long uploadChannelId) {
		this.uploadChannelId = uploadChannelId;
	}

	/**
	 * @return the fileType
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * @return the instanceFile
	 */
	public String getInstanceFile() {
		return instanceFile;
	}

	/**
	 * @param instanceFile the instanceFile to set
	 */
	public void setInstanceFile(String instanceFile) {
		this.instanceFile = instanceFile;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the attachedFile
	 */
	public String getAttachedFile() {
		return attachedFile;
	}

	/**
	 * @param attachedFile the attachedFile to set
	 */
	public void setAttachedFile(String attachedFile) {
		this.attachedFile = attachedFile;
	}

	/**
	 * @return the supportiveDocName
	 */
	public String getSupportiveDocName() {
		return supportiveDocName;
	}

	/**
	 * @param supportiveDocName the supportiveDocName to set
	 */
	public void setSupportiveDocName(String supportiveDocName) {
		this.supportiveDocName = supportiveDocName;
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
		this.status = status;
	}

	/**
	 * @return the frequencyName
	 */
	public String getFrequencyName() {
		return frequencyName;
	}

	/**
	 * @param frequencyName the frequencyName to set
	 */
	public void setFrequencyName(String frequencyName) {
		this.frequencyName = frequencyName;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
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

	/**
	 * @return the unlockRequestId
	 */
	public Long getUnlockRequestId() {
		return unlockRequestId;
	}

	/**
	 * @param unlockRequestId the unlockRequestId to set
	 */
	public void setUnlockRequestId(Long unlockRequestId) {
		this.unlockRequestId = unlockRequestId;
	}

	/**
	 * @return the returnPropertyValue
	 */
	public Integer getReturnPropertyValue() {
		return returnPropertyValue;
	}

	/**
	 * @param returnPropertyValue the returnPropertyValue to set
	 */
	public void setReturnPropertyValue(Integer returnPropertyValue) {
		this.returnPropertyValue = returnPropertyValue;
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
	 * @return the uploadedDate
	 */
	public Date getUploadedDate() {
		return uploadedDate;
	}

	/**
	 * @param uploadedDate the uploadedDate to set
	 */
	public void setUploadedDate(Date uploadedDate) {
		this.uploadedDate = uploadedDate;
	}

	/**
	 * @return the taxonomyId
	 */
	public Long getTaxonomyId() {
		return taxonomyId;
	}

	/**
	 * @param taxonomyId the taxonomyId to set
	 */
	public void setTaxonomyId(Long taxonomyId) {
		this.taxonomyId = taxonomyId;
	}

}