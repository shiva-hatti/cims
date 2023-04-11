package com.iris.rbrToEbr.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author vjadhav
 *
 */
@Entity
@Table(name = "TBL_CTL_EBR_RBR_FLOW_MASTER")
@JsonInclude(Include.NON_NULL)
public class EbrRbrFlowMaster implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5695715998083430214L;
	@Id
	@Column(name = "FLOW_ID")
	private int flowId;

	@Column(name = "FLOW_NAME")
	private String flowName;

	@Column(name = "TASK_NAME")
	private String taskName;

	@Column(name = "SEQUENCE")
	private int sequence;

	@Column(name = "PRIORITY")
	private int priority;

	@Column(name = "TASK_DESCRIPTION")
	private String taskDesc;

	/**
	 * 
	 */
	public EbrRbrFlowMaster() {
	}

	public EbrRbrFlowMaster(int flowId, String flowName, String taskName, int sequence, int priority) {

		this.flowId = flowId;
		this.flowName = flowName;
		this.taskName = taskName;
		this.sequence = sequence;
		this.priority = priority;
	}

	/**
	 * @return the flowId
	 */
	public int getFlowId() {
		return flowId;
	}

	/**
	 * @param flowId the flowId to set
	 */
	public void setFlowId(int flowId) {
		this.flowId = flowId;
	}

	/**
	 * @return the flowName
	 */
	public String getFlowName() {
		return flowName;
	}

	/**
	 * @param flowName the flowName to set
	 */
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	/**
	 * @return the taskName
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * @param taskName the taskName to set
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/**
	 * @return the sequence
	 */
	public int getSequence() {
		return sequence;
	}

	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return the taskDesc
	 */
	public String getTaskDesc() {
		return taskDesc;
	}

	/**
	 * @param taskDesc the taskDesc to set
	 */
	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

}
