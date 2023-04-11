package com.iris.ebr.rbr.error.bean;

import java.io.Serializable;
import java.util.Date;

public class CtlEbrRbrErrorMasterBean implements Serializable {

	/**
	 * @author sdhone
	 */
	private static final long serialVersionUID = -4079710830720488843L;

	private Long errorId;
	private String errorCode;
	private String errorType;
	private String errorDescription;
	private String errorDisplayText;
	private String errorNotificationFlag;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private String errorMsg;

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

	/**
	 * @return the errorMsg
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

	/**
	 * @param errorMsg the errorMsg to set
	 */
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
