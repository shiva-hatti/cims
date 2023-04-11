/**
 * 
 */
package com.iris.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.iris.util.AESV2;
import com.iris.util.Validations;
import com.iris.util.constant.GeneralConstants;

/**
 * @author sajadhav
 *
 */
/**
 * @author sajadhav
 *
 */
public class FilingHistoryDto implements Serializable {

	private static Logger LOGGER = LogManager.getLogger(FilingHistoryDto.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 207498958145721529L;

	private Long fileDetailsId;

	private String fileDetailsFilingStaus;

	private String filingStaus;

	private Integer fileDetailsFilingStatusId;

	private Integer filingStatusId;

	private String uploadChannelDesc;

	private String fileDetailsReturnCode;

	private String returnCode;

	private Long returnId;

	private String returnlabel;

	private String fileDetailsIfscCode;

	private String ifscCode;

	private Long entityId;

	private String entityName;

	private Long startDateInLong;

	private Long endDateInLong;

	private Long fileDetailsfrequencyId;

	private Long frequencyId;

	private String frequencyCode;

	private String frequencyName;

	private String fileType;

	private String originalFileName;

	private String modifiledFileName;

	private Long size;

	private String reasonOfNotProcessed;

	private Long prevUploadId;

	private Long revisionRequestId;

	private Long unlockRequestId;

	private Long formVersionId;

	private Integer returnPropertyValId;

	private Integer noOfError;

	private Integer noOfWarning;

	private String fileMimeType;

	private Long uploadedDateInLong;

	private String fileDetailsEntityCode;

	private String entityCode;

	private Long filingNo;

	private Long fileDetailsReportingStartDateInLong;

	private Long fileDetailsReportingEndDateInLong;

	private String fileDetailsSupportiveDocName;

	private String fileDetailsSupportiveDocType;

	private String supportiveDocName;

	private String returnPropertyValDesc;

	private String uploadedBy;

	private String userEmailId;

	private Boolean ebrFiling;

	private String formVersionNo;

	private Long lastUpdatedOn;

	private Boolean nilFiling;

	public FilingHistoryDto(Long fileDetailsId, String fileDetailsFilingStaus, String filingStaus, String uploadChannelDesc, String fileDetailsReturnCode, String returnCode, Long returnId, String returnlabel, String fileDetailsIfscCode, String ifscCode, Long entityId, String fileDetailsEntityCode, String entityCode, String entityName, Date startDate, Date endDate, Long fileDetailsfrequencyId, Long frequencyId, String frequencyCode, String frequencyName, String fileType, String originalFileName, String modifiledFileName, Long size, String reasonOfNotProcessed, Long prevUploadId, Long revisionRequestId, Long unlockRequestId, Long formVersionId, String formVersionNo, Integer returnPropertyValId, String returnPropertyValDesc, Integer noOfError, Integer noOfWarning, String fileMimeType, Date uploadedDate, Long filingNo, Date fileDetailsReportingStartDate, Date fileDetailsReportingEndDate, Integer fileDetailsFilingStatusId, Integer filingStatusId, String fileDetailsSupportiveDocName, String fileDetailsSupportiveDocType, String supportiveDocName, String uploadedBy, String userEmailId, Date busValProcessEndTime, Date metaValProcessEndTime, Date docPushEndTime, Date etlEndTime, Date targetJobEndTime, Boolean nilFiling) {
		this.fileDetailsId = fileDetailsId;
		this.fileDetailsFilingStaus = fileDetailsFilingStaus;
		this.filingStaus = filingStaus;
		this.uploadChannelDesc = uploadChannelDesc;
		this.fileDetailsReturnCode = fileDetailsReturnCode;
		this.returnCode = returnCode;
		this.returnId = returnId;
		this.returnlabel = returnlabel;
		this.fileDetailsIfscCode = fileDetailsIfscCode;
		this.ifscCode = ifscCode;
		this.entityId = entityId;
		this.entityName = entityName;
		if (startDate != null) {
			this.startDateInLong = startDate.getTime();
		}
		if (endDate != null) {
			this.endDateInLong = endDate.getTime();
		}
		if (uploadedDate != null) {
			this.uploadedDateInLong = uploadedDate.getTime();
		}
		this.fileDetailsfrequencyId = fileDetailsfrequencyId;
		this.frequencyId = frequencyId;
		this.frequencyCode = frequencyCode;
		this.frequencyName = frequencyName;
		this.fileType = fileType;
		this.originalFileName = originalFileName;
		this.modifiledFileName = modifiledFileName;
		this.size = size;
		this.reasonOfNotProcessed = reasonOfNotProcessed;
		this.prevUploadId = prevUploadId;
		this.revisionRequestId = revisionRequestId;
		this.unlockRequestId = unlockRequestId;
		this.formVersionId = formVersionId;
		this.returnPropertyValId = returnPropertyValId;
		this.noOfError = noOfError;
		this.noOfWarning = noOfWarning;
		this.fileMimeType = fileMimeType;
		this.filingNo = filingNo;
		this.fileDetailsEntityCode = fileDetailsEntityCode;
		this.entityCode = entityCode;

		if (fileDetailsReportingEndDate != null) {
			this.fileDetailsReportingEndDateInLong = fileDetailsReportingEndDate.getTime();
		}
		if (fileDetailsReportingStartDate != null) {
			this.fileDetailsReportingStartDateInLong = fileDetailsReportingStartDate.getTime();
		}

		this.fileDetailsSupportiveDocName = fileDetailsSupportiveDocName;
		this.fileDetailsSupportiveDocType = fileDetailsSupportiveDocType;
		this.supportiveDocName = supportiveDocName;
		this.returnPropertyValDesc = returnPropertyValDesc;
		this.fileDetailsFilingStatusId = fileDetailsFilingStatusId;
		this.filingStatusId = filingStatusId;
		this.userEmailId = userEmailId;
		if (!Validations.isEmpty(uploadedBy)) {
			try {
				this.uploadedBy = AESV2.getInstance().decrypt(uploadedBy);
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
			}
		}

		this.formVersionNo = formVersionNo;
		this.nilFiling = nilFiling;
		if (targetJobEndTime != null) {
			this.setLastUpdatedOn(targetJobEndTime.getTime());
		} else if (etlEndTime != null) {
			this.setLastUpdatedOn(etlEndTime.getTime());
		} else if (docPushEndTime != null) {
			this.setLastUpdatedOn(docPushEndTime.getTime());
		} else if (busValProcessEndTime != null) {
			this.setLastUpdatedOn(busValProcessEndTime.getTime());
		} else if (metaValProcessEndTime != null) {
			this.setLastUpdatedOn(metaValProcessEndTime.getTime());
		} else if (uploadedDate != null) {
			this.setLastUpdatedOn(uploadedDate.getTime());
		}
	}

	public FilingHistoryDto(String filingStaus, String returnCode, Long returnId, String returnlabel, String ifscCode, Long entityId, String entityCode, String entityName, Date startDate, Date endDate, Long frequencyId, String frequencyCode, String frequencyName, String modifiledFileName, Long prevUploadId, Long revisionRequestId, Long unlockRequestId, Integer returnPropertyValId, String returnPropertyValDesc, Integer noOfError, Integer noOfWarning, Date uploadedDate, Long filingNo, Integer filingStatusId, String supportiveDocName, String uploadedBy, Boolean ebrFiling) {
		this.filingStaus = filingStaus;
		this.returnCode = returnCode;
		this.returnId = returnId;
		this.returnlabel = returnlabel;
		this.ifscCode = ifscCode;
		this.entityId = entityId;
		this.entityName = entityName;
		if (startDate != null) {
			this.startDateInLong = startDate.getTime();
		}
		if (endDate != null) {
			this.endDateInLong = endDate.getTime();

		}
		if (uploadedDate != null) {
			this.uploadedDateInLong = uploadedDate.getTime();
			this.setLastUpdatedOn(uploadedDate.getTime());

		}
		this.frequencyId = frequencyId;
		this.frequencyCode = frequencyCode;
		this.frequencyName = frequencyName;
		this.modifiledFileName = modifiledFileName;
		this.prevUploadId = prevUploadId;
		this.revisionRequestId = revisionRequestId;
		this.unlockRequestId = unlockRequestId;
		this.returnPropertyValId = returnPropertyValId;
		this.noOfError = noOfError;
		this.noOfWarning = noOfWarning;
		this.filingNo = filingNo;
		this.entityCode = entityCode;
		this.supportiveDocName = supportiveDocName;
		this.returnPropertyValDesc = returnPropertyValDesc;
		this.filingStatusId = filingStatusId;
		this.ebrFiling = ebrFiling;
		if (!Validations.isEmpty(uploadedBy)) {
			try {
				this.uploadedBy = AESV2.getInstance().decrypt(uploadedBy);
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
			}
		}
	}

	/**
	 * @return the nilFiling
	 */
	public Boolean getNilFiling() {
		return nilFiling;
	}

	/**
	 * @param nilFiling the nilFiling to set
	 */
	public void setNilFiling(Boolean nilFiling) {
		this.nilFiling = nilFiling;
	}

	/**
	 * @return the formVersionNo
	 */
	public String getFormVersionNo() {
		return formVersionNo;
	}

	/**
	 * @param formVersionNo the formVersionNo to set
	 */
	public void setFormVersionNo(String formVersionNo) {
		this.formVersionNo = formVersionNo;
	}

	/**
	 * @return the fileDetailsSupportiveDocName
	 */
	public String getFileDetailsSupportiveDocName() {
		return fileDetailsSupportiveDocName;
	}

	/**
	 * @param fileDetailsSupportiveDocName the fileDetailsSupportiveDocName to set
	 */
	public void setFileDetailsSupportiveDocName(String fileDetailsSupportiveDocName) {
		this.fileDetailsSupportiveDocName = fileDetailsSupportiveDocName;
	}

	/**
	 * @return the fileDetailsSupportiveDocType
	 */
	public String getFileDetailsSupportiveDocType() {
		return fileDetailsSupportiveDocType;
	}

	/**
	 * @param fileDetailsSupportiveDocType the fileDetailsSupportiveDocType to set
	 */
	public void setFileDetailsSupportiveDocType(String fileDetailsSupportiveDocType) {
		this.fileDetailsSupportiveDocType = fileDetailsSupportiveDocType;
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
	 * @return the returnPropertyValDesc
	 */
	public String getReturnPropertyValDesc() {
		return returnPropertyValDesc;
	}

	/**
	 * @param returnPropertyValDesc the returnPropertyValDesc to set
	 */
	public void setReturnPropertyValDesc(String returnPropertyValDesc) {
		this.returnPropertyValDesc = returnPropertyValDesc;
	}

	/**
	 * @return the fileDetailsId
	 */
	public Long getFileDetailsId() {
		return fileDetailsId;
	}

	/**
	 * @param fileDetailsId the fileDetailsId to set
	 */
	public void setFileDetailsId(Long fileDetailsId) {
		this.fileDetailsId = fileDetailsId;
	}

	/**
	 * @return the filingStaus
	 */
	public String getFilingStaus() {
		return filingStaus;
	}

	/**
	 * @param filingStaus the filingStaus to set
	 */
	public void setFilingStaus(String filingStaus) {
		this.filingStaus = filingStaus;
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
	 * @return the fileDetailsReturnCode
	 */
	public String getFileDetailsReturnCode() {
		return fileDetailsReturnCode;
	}

	/**
	 * @param fileDetailsReturnCode the fileDetailsReturnCode to set
	 */
	public void setFileDetailsReturnCode(String fileDetailsReturnCode) {
		this.fileDetailsReturnCode = fileDetailsReturnCode;
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
	 * @return the returnlabel
	 */
	public String getReturnlabel() {
		return returnlabel;
	}

	/**
	 * @param returnlabel the returnlabel to set
	 */
	public void setReturnlabel(String returnlabel) {
		this.returnlabel = returnlabel;
	}

	/**
	 * @return the fileDetailsIfscCode
	 */
	public String getFileDetailsIfscCode() {
		return fileDetailsIfscCode;
	}

	/**
	 * @param fileDetailsIfscCode the fileDetailsIfscCode to set
	 */
	public void setFileDetailsIfscCode(String fileDetailsIfscCode) {
		this.fileDetailsIfscCode = fileDetailsIfscCode;
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
	 * @return the fileDetailsfrequencyId
	 */
	public Long getFileDetailsfrequencyId() {
		return fileDetailsfrequencyId;
	}

	/**
	 * @param fileDetailsfrequencyId the fileDetailsfrequencyId to set
	 */
	public void setFileDetailsfrequencyId(Long fileDetailsfrequencyId) {
		this.fileDetailsfrequencyId = fileDetailsfrequencyId;
	}

	/**
	 * @return the frequencyId
	 */
	public Long getFrequencyId() {
		return frequencyId;
	}

	/**
	 * @param frequencyId the frequencyId to set
	 */
	public void setFrequencyId(Long frequencyId) {
		this.frequencyId = frequencyId;
	}

	/**
	 * @return the frequencyCode
	 */
	public String getFrequencyCode() {
		return frequencyCode;
	}

	/**
	 * @param frequencyCode the frequencyCode to set
	 */
	public void setFrequencyCode(String frequencyCode) {
		this.frequencyCode = frequencyCode;
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
	 * @return the originalFileName
	 */
	public String getOriginalFileName() {
		return originalFileName;
	}

	/**
	 * @param originalFileName the originalFileName to set
	 */
	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	/**
	 * @return the modifiledFileName
	 */
	public String getModifiledFileName() {
		return modifiledFileName;
	}

	/**
	 * @param modifiledFileName the modifiledFileName to set
	 */
	public void setModifiledFileName(String modifiledFileName) {
		this.modifiledFileName = modifiledFileName;
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
	 * @return the formVersionId
	 */
	public Long getFormVersionId() {
		return formVersionId;
	}

	/**
	 * @param formVersionId the formVersionId to set
	 */
	public void setFormVersionId(Long formVersionId) {
		this.formVersionId = formVersionId;
	}

	/**
	 * @return the returnPropertyValId
	 */
	public Integer getReturnPropertyValId() {
		return returnPropertyValId;
	}

	/**
	 * @param returnPropertyValId the returnPropertyValId to set
	 */
	public void setReturnPropertyValId(Integer returnPropertyValId) {
		this.returnPropertyValId = returnPropertyValId;
	}

	/**
	 * @return the noOfError
	 */
	public Integer getNoOfError() {
		return noOfError;
	}

	/**
	 * @param noOfError the noOfError to set
	 */
	public void setNoOfError(Integer noOfError) {
		this.noOfError = noOfError;
	}

	/**
	 * @return the noOfWarning
	 */
	public Integer getNoOfWarning() {
		return noOfWarning;
	}

	/**
	 * @param noOfWarning the noOfWarning to set
	 */
	public void setNoOfWarning(Integer noOfWarning) {
		this.noOfWarning = noOfWarning;
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
	 * @return the fileDetailsEntityCode
	 */
	public String getFileDetailsEntityCode() {
		return fileDetailsEntityCode;
	}

	/**
	 * @param fileDetailsEntityCode the fileDetailsEntityCode to set
	 */
	public void setFileDetailsEntityCode(String fileDetailsEntityCode) {
		this.fileDetailsEntityCode = fileDetailsEntityCode;
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
	 * @return the filingNo
	 */
	public Long getFilingNo() {
		return filingNo;
	}

	/**
	 * @param filingNo the filingNo to set
	 */
	public void setFilingNo(Long filingNo) {
		this.filingNo = filingNo;
	}

	/**
	 * @return the uploadedBy
	 */
	public String getUploadedBy() {
		return uploadedBy;
	}

	/**
	 * @param uploadedBy the uploadedBy to set
	 */
	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
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
	 * @return the fileDetailsFilingStaus
	 */
	public String getFileDetailsFilingStaus() {
		return fileDetailsFilingStaus;
	}

	/**
	 * @param fileDetailsFilingStaus the fileDetailsFilingStaus to set
	 */
	public void setFileDetailsFilingStaus(String fileDetailsFilingStaus) {
		this.fileDetailsFilingStaus = fileDetailsFilingStaus;
	}

	/**
	 * @return the fileDetailsFilingStatusId
	 */
	public Integer getFileDetailsFilingStatusId() {
		return fileDetailsFilingStatusId;
	}

	/**
	 * @param fileDetailsFilingStatusId the fileDetailsFilingStatusId to set
	 */
	public void setFileDetailsFilingStatusId(Integer fileDetailsFilingStatusId) {
		this.fileDetailsFilingStatusId = fileDetailsFilingStatusId;
	}

	/**
	 * @return the filingStatusId
	 */
	public Integer getFilingStatusId() {
		return filingStatusId;
	}

	/**
	 * @param filingStatusId the filingStatusId to set
	 */
	public void setFilingStatusId(Integer filingStatusId) {
		this.filingStatusId = filingStatusId;
	}

	/**
	 * @return the ebrFiling
	 */
	public Boolean getEbrFiling() {
		return ebrFiling;
	}

	/**
	 * @param ebrFiling the ebrFiling to set
	 */
	public void setEbrFiling(Boolean ebrFiling) {
		this.ebrFiling = ebrFiling;
	}

	/**
	 * @return the startDateInLong
	 */
	public Long getStartDateInLong() {
		return startDateInLong;
	}

	/**
	 * @param startDateInLong the startDateInLong to set
	 */
	public void setStartDateInLong(Long startDateInLong) {
		this.startDateInLong = startDateInLong;
	}

	/**
	 * @return the endDateInLong
	 */
	public Long getEndDateInLong() {
		return endDateInLong;
	}

	/**
	 * @param endDateInLong the endDateInLong to set
	 */
	public void setEndDateInLong(Long endDateInLong) {
		this.endDateInLong = endDateInLong;
	}

	/**
	 * @return the uploadedDateInLong
	 */
	public Long getUploadedDateInLong() {
		return uploadedDateInLong;
	}

	/**
	 * @param uploadedDateInLong the uploadedDateInLong to set
	 */
	public void setUploadedDateInLong(Long uploadedDateInLong) {
		this.uploadedDateInLong = uploadedDateInLong;
	}

	/**
	 * @return the fileDetailsReportingStartDateInLong
	 */
	public Long getFileDetailsReportingStartDateInLong() {
		return fileDetailsReportingStartDateInLong;
	}

	/**
	 * @param fileDetailsReportingStartDateInLong the fileDetailsReportingStartDateInLong to set
	 */
	public void setFileDetailsReportingStartDateInLong(Long fileDetailsReportingStartDateInLong) {
		this.fileDetailsReportingStartDateInLong = fileDetailsReportingStartDateInLong;
	}

	/**
	 * @return the fileDetailsReportingEndDateInLong
	 */
	public Long getFileDetailsReportingEndDateInLong() {
		return fileDetailsReportingEndDateInLong;
	}

	/**
	 * @param fileDetailsReportingEndDateInLong the fileDetailsReportingEndDateInLong to set
	 */
	public void setFileDetailsReportingEndDateInLong(Long fileDetailsReportingEndDateInLong) {
		this.fileDetailsReportingEndDateInLong = fileDetailsReportingEndDateInLong;
	}

	/**
	 * @return the lastUpdatedOn
	 */
	public Long getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	/**
	 * @param lastUpdatedOn the lastUpdatedOn to set
	 */
	public void setLastUpdatedOn(Long lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

}
