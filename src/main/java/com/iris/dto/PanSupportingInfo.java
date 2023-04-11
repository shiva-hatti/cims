package com.iris.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class PanSupportingInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1570291556336009002L;
	private String refId;
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

	/**
	 * @return the refId
	 */
	public String getRefId() {
		return refId;
	}

	/**
	 * @param refId the refId to set
	 */
	public void setRefId(String refId) {
		this.refId = refId;
	}
}
