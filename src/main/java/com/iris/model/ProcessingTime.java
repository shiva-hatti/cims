package com.iris.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
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

/**
 * @author sajadhav
 *
 */
@Entity
@Table(name = "TBL_PROCESSING_TIME")
@JsonInclude(Include.NON_NULL)
public class ProcessingTime implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PROCESSING_TIME_ID")
	private Long processingTimeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPLOAD_ID_FK")
	private ReturnsUploadDetails returnUploadDetails;

	@ManyToOne(cascade = { CascadeType.DETACH })
	@JoinColumn(name = "PROCESS_TYPE_ID_FK")
	private ProcessTypeMaster processTypeMaster;

	@Column(name = "PROCESS_START_TIME")
	private Date processStartTime;

	@Transient
	private Long processStartTimeInLong;

	@Column(name = "PROCESS_END_TIME")
	private Date processEndTime;

	@Transient
	private Long processEndTimeInLong;

	@Column(name = "SERVER_IP_ADDRESS")
	private String serverIpAddress;

	@Transient
	private boolean status;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public ProcessTypeMaster getProcessTypeMaster() {
		return processTypeMaster;
	}

	public void setProcessTypeMaster(ProcessTypeMaster processTypeMaster) {
		this.processTypeMaster = processTypeMaster;
	}

	public Long getProcessStartTimeInLong() {
		return processStartTimeInLong;
	}

	public void setProcessStartTimeInLong(Long processStartTimeInLong) {
		this.processStartTimeInLong = processStartTimeInLong;
	}

	public Long getProcessEndTimeInLong() {
		return processEndTimeInLong;
	}

	public void setProcessEndTimeInLong(Long processEndTimeInLong) {
		this.processEndTimeInLong = processEndTimeInLong;
	}

	public Long getProcessingTimeId() {
		return processingTimeId;
	}

	public void setProcessingTimeId(Long processingTimeId) {
		this.processingTimeId = processingTimeId;
	}

	public ReturnsUploadDetails getReturnUploadDetails() {
		return returnUploadDetails;
	}

	public void setReturnUploadDetails(ReturnsUploadDetails returnUploadDetails) {
		this.returnUploadDetails = returnUploadDetails;
	}

	public Date getProcessStartTime() {
		return processStartTime;
	}

	public void setProcessStartTime(Date processStartTime) {
		this.processStartTime = processStartTime;
	}

	public Date getProcessEndTime() {
		return processEndTime;
	}

	public void setProcessEndTime(Date processEndTime) {
		this.processEndTime = processEndTime;
	}

	public String getServerIpAddress() {
		return serverIpAddress;
	}

	public void setServerIpAddress(String serverIpAddress) {
		this.serverIpAddress = serverIpAddress;
	}
}
