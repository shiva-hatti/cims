package com.iris.ebr.metadata.flow.entity;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.iris.rbrToEbr.entity.EbrRbrFlowMaster;

/**
 * @author sdhone
 */
@Entity
@Table(name = "TBL_CTL_EBR_METADATA_FLOW")
public class EbrMetadataFlow implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = -1988794732691555997L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "METADATA_CONTROL_PK")
	private Long metadataControlPk;

	@Column(name = "METADATA_JOB_ID")
	private String metadataJobId;

	@Column(name = "METADATA_SOURCE_ENTITY_NAME")
	private String metadataSourceEntityName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "METADATA_OFFSET_DATE")
	private Date metadataOffsetDate;

	@Column(name = "FLOW_ID_FK")
	private int flowIdfk;

	@Column(name = "FLOW_NAME")
	private String flowName;

	@Column(name = "TASK_NAME")
	private String taskName;

	@Column(name = "SEQUENCE")
	private int sequence;

	@Column(name = "PRIORITY")
	private int priority;

	@Column(name = "STATUS")
	private int status;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	public EbrMetadataFlow() {

	}

	public Long getMetadataControlPk() {
		return metadataControlPk;
	}

	public void setMetadataControlPk(Long metadataControlPk) {
		this.metadataControlPk = metadataControlPk;
	}

	public String getMetadataJobId() {
		return metadataJobId;
	}

	public void setMetadataJobId(String metadataJobId) {
		this.metadataJobId = metadataJobId;
	}

	public String getMetadataSourceEntityName() {
		return metadataSourceEntityName;
	}

	public void setMetadataSourceEntityName(String metadataSourceEntityName) {
		this.metadataSourceEntityName = metadataSourceEntityName;
	}

	public Date getMetadataOffsetDate() {
		return metadataOffsetDate;
	}

	public void setMetadataOffsetDate(Date metadataOffsetDate) {
		this.metadataOffsetDate = metadataOffsetDate;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @return the flowIdfk
	 */
	public int getFlowIdfk() {
		return flowIdfk;
	}

	/**
	 * @param flowIdfk the flowIdfk to set
	 */
	public void setFlowIdfk(int flowIdfk) {
		this.flowIdfk = flowIdfk;
	}

}
