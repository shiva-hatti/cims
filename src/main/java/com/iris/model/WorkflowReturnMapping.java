package com.iris.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "TBL_WORKFLOW_RETURN_MAPPING")
@JsonInclude(Include.NON_NULL)
public class WorkflowReturnMapping implements Serializable {

	private static final long serialVersionUID = -590148225478538000L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long workflowId;

	@Column(name = "IS_ACTIVE")
	private Boolean active;

	@Column(name = "LAST_UPDATED_ON")
	private Date lastUpdated;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WORKFLOW_MASTER_ID_FK")
	private WorkFlowMasterBean workFlowMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_ID_FK")
	private Return returnIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CHANNEL_ID_FK")
	private UploadChannel channelIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_UPDATED_BY")
	private UserMaster lastUpdatedBy;

	@Transient
	private Long langId;

	@Transient
	private Regulator regulator;

	@Transient
	private List<WorkflowReturnMapping> returnChannelList;

	@Transient
	private boolean statusFlag;

	@Transient
	private boolean uploadChannel;

	@Transient
	private boolean webChannel;

	@Transient
	private boolean emailChannel;

	@Transient
	private boolean apiChannel;

	@Transient
	private boolean stsChannel;

	@Transient
	private List<WorkflowReturnMapping> activeReturnChannelList;

	public WorkflowReturnMapping(Long workflowId, Long workflowMasterId, Long returnId, Long uploadChannelId, String workflowName) {
		this.workflowId = workflowId;
		this.workFlowMaster = new WorkFlowMasterBean();
		this.workFlowMaster.setWorkflowId(workflowMasterId);
		this.workFlowMaster.setWorkFlowName(workflowName);

		this.returnIdFk = new Return();
		this.returnIdFk.setReturnId(returnId);

		this.channelIdFk = new UploadChannel();
		this.channelIdFk.setUploadChannelId(uploadChannelId);
	}

	public WorkflowReturnMapping() {
		// TODO Auto-generated constructor stub
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
	public List<WorkflowReturnMapping> getReturnChannelList() {
		return returnChannelList;
	}

	/**
	 * @param returnChannelList the returnChannelList to set
	 */
	public void setReturnChannelList(List<WorkflowReturnMapping> returnChannelList) {
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
	public List<WorkflowReturnMapping> getActiveReturnChannelList() {
		return activeReturnChannelList;
	}

	/**
	 * @param activeReturnChannelList the activeReturnChannelList to set
	 */
	public void setActiveReturnChannelList(List<WorkflowReturnMapping> activeReturnChannelList) {
		this.activeReturnChannelList = activeReturnChannelList;
	}
}