/**
 * 
 */
package com.iris.sdmx.status.entity;

import java.io.Serializable;
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

import com.iris.model.FileDetails;
import com.iris.model.UserMaster;

/**
 * @author apagaria
 *
 */
@Entity
@Table(name = "TBL_SDMX_FILE_ACTIVITY_LOG")
public class SdmxFileActivityLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "FILE_ACTIVITY_ID_PK")
	private Long fileActivityLogId;

	@Column(name = "JOB_PROCESSING_ID")
	private String jobProcessingId;

	@Column(name = "OTHER_DETAILS")
	private String otherDetails;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SDMX_PROCESS_DETAIL_ID_FK")
	private SdmxProcessDetailEntity sdmxProcessDetailIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILE_DETAILS_ID_FK")
	private FileDetails fileDetailsIdFk;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PROCESS_START_TIME")
	private Date processStartTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PROCESS_END_TIME")
	private Date processEndTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	private UserMaster createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "IS_SUCCESS")
	private Boolean isSuccess;

	/**
	 * @return the fileActivityLogId
	 */
	public Long getFileActivityLogId() {
		return fileActivityLogId;
	}

	/**
	 * @param fileActivityLogId the fileActivityLogId to set
	 */
	public void setFileActivityLogId(Long fileActivityLogId) {
		this.fileActivityLogId = fileActivityLogId;
	}

	/**
	 * @return the jobProcessingId
	 */
	public String getJobProcessingId() {
		return jobProcessingId;
	}

	/**
	 * @param jobProcessingId the jobProcessingId to set
	 */
	public void setJobProcessingId(String jobProcessingId) {
		this.jobProcessingId = jobProcessingId;
	}

	/**
	 * @return the otherDetails
	 */
	public String getOtherDetails() {
		return otherDetails;
	}

	/**
	 * @param otherDetails the otherDetails to set
	 */
	public void setOtherDetails(String otherDetails) {
		this.otherDetails = otherDetails;
	}

	/**
	 * @return the sdmxProcessDetailIdFk
	 */
	public SdmxProcessDetailEntity getSdmxProcessDetailIdFk() {
		return sdmxProcessDetailIdFk;
	}

	/**
	 * @param sdmxProcessDetailIdFk the sdmxProcessDetailIdFk to set
	 */
	public void setSdmxProcessDetailIdFk(SdmxProcessDetailEntity sdmxProcessDetailIdFk) {
		this.sdmxProcessDetailIdFk = sdmxProcessDetailIdFk;
	}

	/**
	 * @return the fileDetailsIdFk
	 */
	public FileDetails getFileDetailsIdFk() {
		return fileDetailsIdFk;
	}

	/**
	 * @param fileDetailsIdFk the fileDetailsIdFk to set
	 */
	public void setFileDetailsIdFk(FileDetails fileDetailsIdFk) {
		this.fileDetailsIdFk = fileDetailsIdFk;
	}

	/**
	 * @return the processStartTime
	 */
	public Date getProcessStartTime() {
		return processStartTime;
	}

	/**
	 * @param processStartTime the processStartTime to set
	 */
	public void setProcessStartTime(Date processStartTime) {
		this.processStartTime = processStartTime;
	}

	/**
	 * @return the processEndTime
	 */
	public Date getProcessEndTime() {
		return processEndTime;
	}

	/**
	 * @param processEndTime the processEndTime to set
	 */
	public void setProcessEndTime(Date processEndTime) {
		this.processEndTime = processEndTime;
	}

	/**
	 * @return the createdBy
	 */
	public UserMaster getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(UserMaster createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the isSuccess
	 */
	public Boolean getIsSuccess() {
		return isSuccess;
	}

	/**
	 * @param isSuccess the isSuccess to set
	 */
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "SdmxFileActivityLog [fileActivityLogId=" + fileActivityLogId + ", jobProcessingId=" + jobProcessingId + ", otherDetails=" + otherDetails + ", sdmxProcessDetailIdFk=" + sdmxProcessDetailIdFk + ", fileDetailsIdFk=" + fileDetailsIdFk + ", processStartTime=" + processStartTime + ", processEndTime=" + processEndTime + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", isSuccess=" + isSuccess + "]";
	}

}
