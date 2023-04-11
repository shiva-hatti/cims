package com.iris.dto;

import java.io.Serializable;

public class UploadChannelDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1400910828258032232L;

	private Long uploadChannelId;

	private String uploadChannelDesc;

	private String status;

	public Long getUploadChannelId() {
		return uploadChannelId;
	}

	public void setUploadChannelId(Long uploadChannelId) {
		this.uploadChannelId = uploadChannelId;
	}

	public String getUploadChannelDesc() {
		return uploadChannelDesc;
	}

	public void setUploadChannelDesc(String uploadChannelDesc) {
		this.uploadChannelDesc = uploadChannelDesc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
