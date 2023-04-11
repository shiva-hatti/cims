package com.iris.dto;

import java.io.Serializable;
import java.util.Date;

import com.iris.model.FilingStatus;
import com.iris.model.Frequency;
import com.iris.model.UploadChannel;

public class FileDetailsDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -679611934008224502L;

	private Long id;

	private String fileName;

	private Long fileCreationTimeInNumber;

	private String status;

	private String fileType;

	private UploadChannel uploadChannelIdFk;

	private Long size;

	private String fileMimeType;

	private String returnCode;

	private String entityCode;

	private Boolean isActive;

	private Long reportingPeriodStartDateInLong;

	private Long reportingPeriodEndDateInLong;

	private String userName;

	private Boolean processingFlag;

	private String userEmailId;

	private Boolean nillable;

	private String supportiveDocName;

	private String nullabelComments;

	private String supportiveDocType;

	private Long entityId;

	private Long channelId;

	private Long userId;

	private Long fileCopyingStartTimeInLong;

	private Long fileCopyingEndTimeInLong;

	private String reasonOfNotProcessed;

	private FilingStatus fileStatus;

	private Long creationDateInLong;

	private String filingStatus;

	private int filingStatusId;

	private Long uploadId;

	private String ifscCode;

	private String uploadInstanceFileName;

	private String endDate;

	private Date endDate_DateFrmt;

	private Date reportingPeriodEndDate;

	private Date reportingPeriodStartDate;

	private String frequency;

	private Frequency frequencyIdFk;

	private String attachedFileName;

	private String returnName;

	private String entityName;

	private String activityTrackerJson;

	private Long unlockRequestId;

	private Long revisionRequestId;

	private Long prevUploadId;

	private String returnPropertyVal;

	private String templateVersionNo;

	private int noOfErrors;

	private int noOfWarnings;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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
	 * @return the fileCreationTimeInNumber
	 */
	public Long getFileCreationTimeInNumber() {
		return fileCreationTimeInNumber;
	}

	/**
	 * @param fileCreationTimeInNumber the fileCreationTimeInNumber to set
	 */
	public void setFileCreationTimeInNumber(Long fileCreationTimeInNumber) {
		this.fileCreationTimeInNumber = fileCreationTimeInNumber;
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
	 * @return the uploadChannelIdFk
	 */
	public UploadChannel getUploadChannelIdFk() {
		return uploadChannelIdFk;
	}

	/**
	 * @param uploadChannelIdFk the uploadChannelIdFk to set
	 */
	public void setUploadChannelIdFk(UploadChannel uploadChannelIdFk) {
		this.uploadChannelIdFk = uploadChannelIdFk;
	}

	/**
	 * @return the size
	 */
	public Long getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(Long size) {
		this.size = size;
	}

	/**
	 * @return the fileMimeType
	 */
	public String getFileMimeType() {
		return fileMimeType;
	}

	/**
	 * @param fileMimeType the fileMimeType to set
	 */
	public void setFileMimeType(String fileMimeType) {
		this.fileMimeType = fileMimeType;
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
	 * @return the reportingPeriodStartDateInLong
	 */
	public Long getReportingPeriodStartDateInLong() {
		return reportingPeriodStartDateInLong;
	}

	/**
	 * @param reportingPeriodStartDateInLong the reportingPeriodStartDateInLong to set
	 */
	public void setReportingPeriodStartDateInLong(Long reportingPeriodStartDateInLong) {
		this.reportingPeriodStartDateInLong = reportingPeriodStartDateInLong;
	}

	/**
	 * @return the reportingPeriodEndDateInLong
	 */
	public Long getReportingPeriodEndDateInLong() {
		return reportingPeriodEndDateInLong;
	}

	/**
	 * @param reportingPeriodEndDateInLong the reportingPeriodEndDateInLong to set
	 */
	public void setReportingPeriodEndDateInLong(Long reportingPeriodEndDateInLong) {
		this.reportingPeriodEndDateInLong = reportingPeriodEndDateInLong;
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
	 * @return the processingFlag
	 */
	public Boolean getProcessingFlag() {
		return processingFlag;
	}

	/**
	 * @param processingFlag the processingFlag to set
	 */
	public void setProcessingFlag(Boolean processingFlag) {
		this.processingFlag = processingFlag;
	}

	/**
	 * @return the userEmailId
	 */
	public String getUserEmailId() {
		return userEmailId;
	}

	/**
	 * @param userEmailId the userEmailId to set
	 */
	public void setUserEmailId(String userEmailId) {
		this.userEmailId = userEmailId;
	}

	/**
	 * @return the nillable
	 */
	public Boolean getNillable() {
		return nillable;
	}

	/**
	 * @param nillable the nillable to set
	 */
	public void setNillable(Boolean nillable) {
		this.nillable = nillable;
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
	 * @return the nullabelComments
	 */
	public String getNullabelComments() {
		return nullabelComments;
	}

	/**
	 * @param nullabelComments the nullabelComments to set
	 */
	public void setNullabelComments(String nullabelComments) {
		this.nullabelComments = nullabelComments;
	}

	/**
	 * @return the supportiveDocType
	 */
	public String getSupportiveDocType() {
		return supportiveDocType;
	}

	/**
	 * @param supportiveDocType the supportiveDocType to set
	 */
	public void setSupportiveDocType(String supportiveDocType) {
		this.supportiveDocType = supportiveDocType;
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
	 * @return the channelId
	 */
	public Long getChannelId() {
		return channelId;
	}

	/**
	 * @param channelId the channelId to set
	 */
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
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
	 * @return the fileCopyingStartTimeInLong
	 */
	public Long getFileCopyingStartTimeInLong() {
		return fileCopyingStartTimeInLong;
	}

	/**
	 * @param fileCopyingStartTimeInLong the fileCopyingStartTimeInLong to set
	 */
	public void setFileCopyingStartTimeInLong(Long fileCopyingStartTimeInLong) {
		this.fileCopyingStartTimeInLong = fileCopyingStartTimeInLong;
	}

	/**
	 * @return the fileCopyingEndTimeInLong
	 */
	public Long getFileCopyingEndTimeInLong() {
		return fileCopyingEndTimeInLong;
	}

	/**
	 * @param fileCopyingEndTimeInLong the fileCopyingEndTimeInLong to set
	 */
	public void setFileCopyingEndTimeInLong(Long fileCopyingEndTimeInLong) {
		this.fileCopyingEndTimeInLong = fileCopyingEndTimeInLong;
	}

	/**
	 * @return the reasonOfNotProcessed
	 */
	public String getReasonOfNotProcessed() {
		return reasonOfNotProcessed;
	}

	/**
	 * @param reasonOfNotProcessed the reasonOfNotProcessed to set
	 */
	public void setReasonOfNotProcessed(String reasonOfNotProcessed) {
		this.reasonOfNotProcessed = reasonOfNotProcessed;
	}

	/**
	 * @return the fileStatus
	 */
	public FilingStatus getFileStatus() {
		return fileStatus;
	}

	/**
	 * @param fileStatus the fileStatus to set
	 */
	public void setFileStatus(FilingStatus fileStatus) {
		this.fileStatus = fileStatus;
	}

	/**
	 * @return the creationDateInLong
	 */
	public Long getCreationDateInLong() {
		return creationDateInLong;
	}

	/**
	 * @param creationDateInLong the creationDateInLong to set
	 */
	public void setCreationDateInLong(Long creationDateInLong) {
		this.creationDateInLong = creationDateInLong;
	}

	/**
	 * @return the filingStatus
	 */
	public String getFilingStatus() {
		return filingStatus;
	}

	/**
	 * @param filingStatus the filingStatus to set
	 */
	public void setFilingStatus(String filingStatus) {
		this.filingStatus = filingStatus;
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
	 * @return the ifscCode
	 */
	public String getIfscCode() {
		return ifscCode;
	}

	/**
	 * @param ifscCode the ifscCode to set
	 */
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	/**
	 * @return the uploadInstanceFileName
	 */
	public String getUploadInstanceFileName() {
		return uploadInstanceFileName;
	}

	/**
	 * @param uploadInstanceFileName the uploadInstanceFileName to set
	 */
	public void setUploadInstanceFileName(String uploadInstanceFileName) {
		this.uploadInstanceFileName = uploadInstanceFileName;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the endDate_DateFrmt
	 */
	public Date getEndDate_DateFrmt() {
		return endDate_DateFrmt;
	}

	/**
	 * @param endDate_DateFrmt the endDate_DateFrmt to set
	 */
	public void setEndDate_DateFrmt(Date endDate_DateFrmt) {
		this.endDate_DateFrmt = endDate_DateFrmt;
	}

	/**
	 * @return the reportingPeriodEndDate
	 */
	public Date getReportingPeriodEndDate() {
		return reportingPeriodEndDate;
	}

	/**
	 * @param reportingPeriodEndDate the reportingPeriodEndDate to set
	 */
	public void setReportingPeriodEndDate(Date reportingPeriodEndDate) {
		this.reportingPeriodEndDate = reportingPeriodEndDate;
	}

	/**
	 * @return the reportingPeriodStartDate
	 */
	public Date getReportingPeriodStartDate() {
		return reportingPeriodStartDate;
	}

	/**
	 * @param reportingPeriodStartDate the reportingPeriodStartDate to set
	 */
	public void setReportingPeriodStartDate(Date reportingPeriodStartDate) {
		this.reportingPeriodStartDate = reportingPeriodStartDate;
	}

	/**
	 * @return the frequency
	 */
	public String getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	/**
	 * @return the frequencyIdFk
	 */
	public Frequency getFrequencyIdFk() {
		return frequencyIdFk;
	}

	/**
	 * @param frequencyIdFk the frequencyIdFk to set
	 */
	public void setFrequencyIdFk(Frequency frequencyIdFk) {
		this.frequencyIdFk = frequencyIdFk;
	}

	/**
	 * @return the attachedFileName
	 */
	public String getAttachedFileName() {
		return attachedFileName;
	}

	/**
	 * @param attachedFileName the attachedFileName to set
	 */
	public void setAttachedFileName(String attachedFileName) {
		this.attachedFileName = attachedFileName;
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
	 * @return the activityTrackerJson
	 */
	public String getActivityTrackerJson() {
		return activityTrackerJson;
	}

	/**
	 * @param activityTrackerJson the activityTrackerJson to set
	 */
	public void setActivityTrackerJson(String activityTrackerJson) {
		this.activityTrackerJson = activityTrackerJson;
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
	 * @return the revisionRequestId
	 */
	public Long getRevisionRequestId() {
		return revisionRequestId;
	}

	/**
	 * @param revisionRequestId the revisionRequestId to set
	 */
	public void setRevisionRequestId(Long revisionRequestId) {
		this.revisionRequestId = revisionRequestId;
	}

	/**
	 * @return the prevUploadId
	 */
	public Long getPrevUploadId() {
		return prevUploadId;
	}

	/**
	 * @param prevUploadId the prevUploadId to set
	 */
	public void setPrevUploadId(Long prevUploadId) {
		this.prevUploadId = prevUploadId;
	}

	/**
	 * @return the returnPropertyVal
	 */
	public String getReturnPropertyVal() {
		return returnPropertyVal;
	}

	/**
	 * @param returnPropertyVal the returnPropertyVal to set
	 */
	public void setReturnPropertyVal(String returnPropertyVal) {
		this.returnPropertyVal = returnPropertyVal;
	}

	/**
	 * @return the templateVersionNo
	 */
	public String getTemplateVersionNo() {
		return templateVersionNo;
	}

	/**
	 * @param templateVersionNo the templateVersionNo to set
	 */
	public void setTemplateVersionNo(String templateVersionNo) {
		this.templateVersionNo = templateVersionNo;
	}

	/**
	 * @return the noOfErrors
	 */
	public int getNoOfErrors() {
		return noOfErrors;
	}

	/**
	 * @param noOfErrors the noOfErrors to set
	 */
	public void setNoOfErrors(int noOfErrors) {
		this.noOfErrors = noOfErrors;
	}

	/**
	 * @return the noOfWarnings
	 */
	public int getNoOfWarnings() {
		return noOfWarnings;
	}

	/**
	 * @param noOfWarnings the noOfWarnings to set
	 */
	public void setNoOfWarnings(int noOfWarnings) {
		this.noOfWarnings = noOfWarnings;
	}

}
