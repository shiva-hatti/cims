package com.iris.dto;

import java.io.Serializable;

public class FilingPanNSDL implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 588112083789764900L;
	private Long nsdlPanVerifId;
	private Integer filingPanId;
	private String status;
	private Long uploadId;

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

	public Long getNsdlPanVerifId() {
		return nsdlPanVerifId;
	}

	public void setNsdlPanVerifId(Long nsdlPanVerifId) {
		this.nsdlPanVerifId = nsdlPanVerifId;
	}

	public Integer getFilingPanId() {
		return filingPanId;
	}

	public void setFilingPanId(Integer filingPanId) {
		this.filingPanId = filingPanId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
