/**
 * 
 */
package com.iris.model;

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

/**
 * @author akhandagale
 *
 */
@Entity
@Table(name = "TBL_ETL_AUDIT_LOG")
public class ETLAuditLog implements Serializable {

	private static final long serialVersionUID = -8273258327301690259L;

	@Id
	@Column(name = "AUDIT_LOG_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long auditId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPLOAD_ID_FK")
	private ReturnsUploadDetails returnUploadDetails;

	@Column(name = "FILE_SIZE")
	private String fileSize;

	@Column(name = "FILE_CHECKSUM")
	private String fileChecksum;

	@Column(name = "IS_NIL_FILING")
	private boolean nillable;

	@Column(name = "DOC_PATH")
	private String docPath;

	@Column(name = "STATUS")
	private Long status;

	@Column(name = "DOC_PUSH_END_TIME")
	private Date docPushEndTime;

	@Column(name = "ETL_END_TIME")
	private Date etlEndTime;

	@Column(name = "TARGET_JOB_END_TIME")
	private Date targetJobEndTime;

	/**
	 * @return the auditId
	 */
	public Long getAuditId() {
		return auditId;
	}

	/**
	 * @param auditId the auditId to set
	 */
	public void setAuditId(Long auditId) {
		this.auditId = auditId;
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
	 * @return the fileSize
	 */
	public String getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * @return the fileChecksum
	 */
	public String getFileChecksum() {
		return fileChecksum;
	}

	/**
	 * @param fileChecksum the fileChecksum to set
	 */
	public void setFileChecksum(String fileChecksum) {
		this.fileChecksum = fileChecksum;
	}

	/**
	 * @return the nillable
	 */
	public boolean isNillable() {
		return nillable;
	}

	/**
	 * @param nillable the nillable to set
	 */
	public void setNillable(boolean nillable) {
		this.nillable = nillable;
	}

	/**
	 * @return the docPath
	 */
	public String getDocPath() {
		return docPath;
	}

	/**
	 * @param docPath the docPath to set
	 */
	public void setDocPath(String docPath) {
		this.docPath = docPath;
	}

	/**
	 * @return the status
	 */
	public Long getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Long status) {
		this.status = status;
	}

	/**
	 * @return the docPushEndTime
	 */
	public Date getDocPushEndTime() {
		return docPushEndTime;
	}

	/**
	 * @param docPushEndTime the docPushEndTime to set
	 */
	public void setDocPushEndTime(Date docPushEndTime) {
		this.docPushEndTime = docPushEndTime;
	}

	/**
	 * @return the etlEndTime
	 */
	public Date getEtlEndTime() {
		return etlEndTime;
	}

	/**
	 * @param etlEndTime the etlEndTime to set
	 */
	public void setEtlEndTime(Date etlEndTime) {
		this.etlEndTime = etlEndTime;
	}

	/**
	 * @return the targetJobEndTime
	 */
	public Date getTargetJobEndTime() {
		return targetJobEndTime;
	}

	/**
	 * @param targetJobEndTime the targetJobEndTime to set
	 */
	public void setTargetJobEndTime(Date targetJobEndTime) {
		this.targetJobEndTime = targetJobEndTime;
	}
}
