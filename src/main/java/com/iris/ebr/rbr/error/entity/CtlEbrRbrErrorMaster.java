package com.iris.ebr.rbr.error.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author sdhone
 */
@Entity
@Table(name = "TBL_CTL_EBR_RBR_ERROR_MASTER")
public class CtlEbrRbrErrorMaster implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4732220558348179050L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ERROR_ID")
	private Long errorId;

	@Column(name = "ERROR_CODE")
	private String errorCode;

	@Column(name = "ERROR_TYPE")
	private String errorType;

	@Column(name = "ERROR_DESCRIPTION")
	private String errorDescription;

	@Column(name = "ERROR_DISPLAY_TEXT")
	private String errorDisplayText;

	@Column(name = "ERROR_NOTIFICATION_FLAG")
	private String errorNotificationFlag;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	/**
	 * @return the errorId
	 */
	public Long getErrorId() {
		return errorId;
	}

	/**
	 * @param errorId the errorId to set
	 */
	public void setErrorId(Long errorId) {
		this.errorId = errorId;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorType
	 */
	public String getErrorType() {
		return errorType;
	}

	/**
	 * @param errorType the errorType to set
	 */
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	/**
	 * @return the errorDescription
	 */
	public String getErrorDescription() {
		return errorDescription;
	}

	/**
	 * @param errorDescription the errorDescription to set
	 */
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	/**
	 * @return the errorDisplayText
	 */
	public String getErrorDisplayText() {
		return errorDisplayText;
	}

	/**
	 * @param errorDisplayText the errorDisplayText to set
	 */
	public void setErrorDisplayText(String errorDisplayText) {
		this.errorDisplayText = errorDisplayText;
	}

	/**
	 * @return the errorNotificationFlag
	 */
	public String getErrorNotificationFlag() {
		return errorNotificationFlag;
	}

	/**
	 * @param errorNotificationFlag the errorNotificationFlag to set
	 */
	public void setErrorNotificationFlag(String errorNotificationFlag) {
		this.errorNotificationFlag = errorNotificationFlag;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the modifiedDate
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
