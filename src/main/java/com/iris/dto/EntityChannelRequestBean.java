package com.iris.dto;

import java.io.Serializable;
import java.util.List;

import com.iris.model.FilingStatus;
import com.iris.model.UploadChannel;

public class EntityChannelRequestBean implements Serializable {

	private static final long serialVersionUID = 335819880966920789L;

	private List<Long> entityIds;

	private List<FilingStatus> filingStatusList;

	private List<UploadChannel> uploadChannelList;

	/**
	 * @return the filingStatusList
	 */
	public List<FilingStatus> getFilingStatusList() {
		return filingStatusList;
	}

	/**
	 * @param filingStatusList the filingStatusList to set
	 */
	public void setFilingStatusList(List<FilingStatus> filingStatusList) {
		this.filingStatusList = filingStatusList;
	}

	/**
	 * @return the entityIds
	 */
	public List<Long> getEntityIds() {
		return entityIds;
	}

	/**
	 * @param entityIds the entityIds to set
	 */
	public void setEntityIds(List<Long> entityIds) {
		this.entityIds = entityIds;
	}

	/**
	 * @return the uploadChannelList
	 */
	public List<UploadChannel> getUploadChannelList() {
		return uploadChannelList;
	}

	/**
	 * @param uploadChannelList the uploadChannelList to set
	 */
	public void setUploadChannelList(List<UploadChannel> uploadChannelList) {
		this.uploadChannelList = uploadChannelList;
	}

}