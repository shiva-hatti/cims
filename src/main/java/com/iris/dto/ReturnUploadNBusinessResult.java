/**
 * 
 */
package com.iris.dto;

/**
 * @author apagaria
 *
 */
public class ReturnUploadNBusinessResult {

	private Long uploadId;

	private int fillingStatusId;

	private String errorCode;

	private String errorMsg;

	private Boolean status;

	/**
	 * @return the returnUploadId
	 */
	public Long getUploadId() {
		return uploadId;
	}

	/**
	 * @param returnUploadId the returnUploadId to set
	 */
	public void setUploadId(Long returnUploadId) {
		this.uploadId = returnUploadId;
	}

	/**
	 * @return the fillingStatusId
	 */
	public int getFillingStatusId() {
		return fillingStatusId;
	}

	/**
	 * @param fillingStatusId the fillingStatusId to set
	 */
	public void setFillingStatusId(int fillingStatusId) {
		this.fillingStatusId = fillingStatusId;
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

	/**
	 * @return the status
	 */
	public Boolean getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ReturnUploadResultBean [returnUploadId=" + uploadId + ", fillingStatusId=" + fillingStatusId + ", errorCode=" + errorCode + ", errorMsg=" + errorMsg + ", status=" + status + "]";
	}

}
