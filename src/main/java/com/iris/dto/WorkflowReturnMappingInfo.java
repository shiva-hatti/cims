package com.iris.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.iris.model.Regulator;
import com.iris.model.Return;
import com.iris.model.UploadChannel;
import com.iris.model.UserMaster;
import com.iris.model.WorkFlowMasterBean;

public class WorkflowReturnMappingInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -590148225478538000L;
	private Long workflowId;
	private Boolean active;
	private Date lastUpdated;
	private WorkFlowMasterBean workFlowMaster;
	private Return returnIdFk;
	private UploadChannel channelIdFk;
	private UserMaster lastUpdatedBy;
	private Long langId;
	private String langCode;
	private Regulator regulator;
	private List<WorkflowReturnMappingInfo> returnChannelList;
	private boolean statusFlag;
	private boolean uploadChannel;
	private boolean webChannel;
	private boolean emailChannel;
	private boolean apiChannel;
	private boolean stsChannel;
	private List<WorkflowReturnMappingInfo> activeReturnChannelList;
	private List<Long> returnIds;

	/**
	 * @return the langCode
	 */
	public String getLangCode() {
		return langCode;
	}

	/**
	 * @param langCode the langCode to set
	 */
	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * @return the workFlowMaster
	 */
	public WorkFlowMasterBean getWorkFlowMaster() {
		return workFlowMaster;
	}

	/**
	 * @param workFlowMaster the workFlowMaster to set
	 */
	public void setWorkFlowMaster(WorkFlowMasterBean workFlowMaster) {
		this.workFlowMaster = workFlowMaster;
	}

	/**
	 * @return the langId
	 */
	public Long getLangId() {
		return langId;
	}

	/**
	 * @param langId the langId to set
	 */
	public void setLangId(Long langId) {
		this.langId = langId;
	}

	/**
	 * @return the regulator
	 */
	public Regulator getRegulator() {
		return regulator;
	}

	/**
	 * @param regulator the regulator to set
	 */
	public void setRegulator(Regulator regulator) {
		this.regulator = regulator;
	}

	/**
	 * @return the returnChannelList
	 */
	public List<WorkflowReturnMappingInfo> getReturnChannelList() {
		return returnChannelList;
	}

	/**
	 * @param returnChannelList the returnChannelList to set
	 */
	public void setReturnChannelList(List<WorkflowReturnMappingInfo> returnChannelList) {
		this.returnChannelList = returnChannelList;
	}

	/**
	 * @return the returnIdFk
	 */
	public Return getReturnIdFk() {
		return returnIdFk;
	}

	/**
	 * @param returnIdFk the returnIdFk to set
	 */
	public void setReturnIdFk(Return returnIdFk) {
		this.returnIdFk = returnIdFk;
	}

	/**
	 * @return the channelIdFk
	 */
	public UploadChannel getChannelIdFk() {
		return channelIdFk;
	}

	/**
	 * @param channelIdFk the channelIdFk to set
	 */
	public void setChannelIdFk(UploadChannel channelIdFk) {
		this.channelIdFk = channelIdFk;
	}

	/**
	 * @return the lastUpdatedBy
	 */
	public UserMaster getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	/**
	 * @param lastUpdatedBy the lastUpdatedBy to set
	 */
	public void setLastUpdatedBy(UserMaster lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	/**
	 * @return the statusFlag
	 */
	public boolean isStatusFlag() {
		return statusFlag;
	}

	/**
	 * @param statusFlag the statusFlag to set
	 */
	public void setStatusFlag(boolean statusFlag) {
		this.statusFlag = statusFlag;
	}

	/**
	 * @return the uploadChannel
	 */
	public boolean isUploadChannel() {
		return uploadChannel;
	}

	/**
	 * @param uploadChannel the uploadChannel to set
	 */
	public void setUploadChannel(boolean uploadChannel) {
		this.uploadChannel = uploadChannel;
	}

	/**
	 * @return the webChannel
	 */
	public boolean isWebChannel() {
		return webChannel;
	}

	/**
	 * @param webChannel the webChannel to set
	 */
	public void setWebChannel(boolean webChannel) {
		this.webChannel = webChannel;
	}

	/**
	 * @return the emailChannel
	 */
	public boolean isEmailChannel() {
		return emailChannel;
	}

	/**
	 * @param emailChannel the emailChannel to set
	 */
	public void setEmailChannel(boolean emailChannel) {
		this.emailChannel = emailChannel;
	}

	/**
	 * @return the apiChannel
	 */
	public boolean isApiChannel() {
		return apiChannel;
	}

	/**
	 * @param apiChannel the apiChannel to set
	 */
	public void setApiChannel(boolean apiChannel) {
		this.apiChannel = apiChannel;
	}

	/**
	 * @return the stsChannel
	 */
	public boolean isStsChannel() {
		return stsChannel;
	}

	/**
	 * @param stsChannel the stsChannel to set
	 */
	public void setStsChannel(boolean stsChannel) {
		this.stsChannel = stsChannel;
	}

	/**
	 * @return the activeReturnChannelList
	 */
	public List<WorkflowReturnMappingInfo> getActiveReturnChannelList() {
		return activeReturnChannelList;
	}

	/**
	 * @param activeReturnChannelList the activeReturnChannelList to set
	 */
	public void setActiveReturnChannelList(List<WorkflowReturnMappingInfo> activeReturnChannelList) {
		this.activeReturnChannelList = activeReturnChannelList;
	}

	/**
	 * @return the returnIds
	 */
	public List<Long> getReturnIds() {
		return returnIds;
	}

	/**
	 * @param returnIds the returnIds to set
	 */
	public void setReturnIds(List<Long> returnIds) {
		this.returnIds = returnIds;
	}

}