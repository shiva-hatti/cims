package com.iris.model;

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
import javax.persistence.Transient;

@Entity
@Table(name = "TBL_SCHEDULER_LOG")
public class SchedulerLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7128441402719550173L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCHEDULER_ID_FK")
	private Scheduler schedulerIdFk;

	@Column(name = "SCHEDULER_STARTED_TIME")
	private Date schedulerStartedTime;

	@Column(name = "SCHEDULER_STOPPED_TIME")
	private Date schedulerStoppedTime;

	@Column(name = "TAKEN_RECORDS_COUNT")
	private Long takedRecordsCount;

	@Column(name = "SUCCESSFULLY_PROCESSED_COUNT")
	private Long successfullyProcessedCount;

	@Column(name = "FAILED_RECORD_COUNT")
	private Long failedRecordCount;

	@Transient
	private Long schedulerStartedTimeInLong;

	@Transient
	private Long schedulerStoppedTimeInLong;

	@Column(name = "IP_ADDRESS")
	private String ipAddress;

	@Column(name = "IS_RUNNING")
	private Boolean isRunning;

	@Column(name = "PROCESSING_INFO")
	private String processingInfo;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the schedulerIdFk
	 */
	public Scheduler getSchedulerIdFk() {
		return schedulerIdFk;
	}

	/**
	 * @param schedulerIdFk the schedulerIdFk to set
	 */
	public void setSchedulerIdFk(Scheduler schedulerIdFk) {
		this.schedulerIdFk = schedulerIdFk;
	}

	/**
	 * @return the schedulerStartedTime
	 */
	public Date getSchedulerStartedTime() {
		return schedulerStartedTime;
	}

	/**
	 * @param schedulerStartedTime the schedulerStartedTime to set
	 */
	public void setSchedulerStartedTime(Date schedulerStartedTime) {
		this.schedulerStartedTime = schedulerStartedTime;
	}

	/**
	 * @return the schedulerStoppedTime
	 */
	public Date getSchedulerStoppedTime() {
		return schedulerStoppedTime;
	}

	/**
	 * @param schedulerStoppedTime the schedulerStoppedTime to set
	 */
	public void setSchedulerStoppedTime(Date schedulerStoppedTime) {
		this.schedulerStoppedTime = schedulerStoppedTime;
	}

	/**
	 * @return the takedRecordsCount
	 */
	public Long getTakedRecordsCount() {
		return takedRecordsCount;
	}

	/**
	 * @param takedRecordsCount the takedRecordsCount to set
	 */
	public void setTakedRecordsCount(Long takedRecordsCount) {
		this.takedRecordsCount = takedRecordsCount;
	}

	/**
	 * @return the successfullyProcessedCount
	 */
	public Long getSuccessfullyProcessedCount() {
		return successfullyProcessedCount;
	}

	/**
	 * @param successfullyProcessedCount the successfullyProcessedCount to set
	 */
	public void setSuccessfullyProcessedCount(Long successfullyProcessedCount) {
		this.successfullyProcessedCount = successfullyProcessedCount;
	}

	/**
	 * @return the schedulerStartedTimeInLong
	 */
	public Long getSchedulerStartedTimeInLong() {
		return schedulerStartedTimeInLong;
	}

	/**
	 * @param schedulerStartedTimeInLong the schedulerStartedTimeInLong to set
	 */
	public void setSchedulerStartedTimeInLong(Long schedulerStartedTimeInLong) {
		this.schedulerStartedTimeInLong = schedulerStartedTimeInLong;
	}

	/**
	 * @return the schedulerStoppedTimeInLong
	 */
	public Long getSchedulerStoppedTimeInLong() {
		return schedulerStoppedTimeInLong;
	}

	/**
	 * @param schedulerStoppedTimeInLong the schedulerStoppedTimeInLong to set
	 */
	public void setSchedulerStoppedTimeInLong(Long schedulerStoppedTimeInLong) {
		this.schedulerStoppedTimeInLong = schedulerStoppedTimeInLong;
	}

	public Long getFailedRecordCount() {
		return failedRecordCount;
	}

	public void setFailedRecordCount(Long failedRecordCount) {
		this.failedRecordCount = failedRecordCount;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Boolean getIsRunning() {
		return isRunning;
	}

	public void setIsRunning(Boolean isRunning) {
		this.isRunning = isRunning;
	}

	public String getProcessingInfo() {
		return processingInfo;
	}

	public void setProcessingInfo(String processingInfo) {
		this.processingInfo = processingInfo;
	}

}
