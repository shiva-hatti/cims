package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "TBL_SCHEDULER")
public class Scheduler implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7102114167901761694L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SCHEDULER_ID")
	private Long schedulerId;

	@Column(name = "SCHEDULER_NAME")
	private String schedulerName;

	@Transient
	private Boolean isRunning;

	@Column(name = "RECORDS_TO_BE_PROCESSED")
	private Long recordsToBeProcessed;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "SCHEDULER_CODE")
	private String schedulerCode;

	/**
	 * @return the schedulerCode
	 */
	public String getSchedulerCode() {
		return schedulerCode;
	}

	/**
	 * @param schedulerCode the schedulerCode to set
	 */
	public void setSchedulerCode(String schedulerCode) {
		this.schedulerCode = schedulerCode;
	}

	/**
	 * @return the schedulerId
	 */
	public Long getSchedulerId() {
		return schedulerId;
	}

	/**
	 * @param schedulerId the schedulerId to set
	 */
	public void setSchedulerId(Long schedulerId) {
		this.schedulerId = schedulerId;
	}

	/**
	 * @return the schedulerName
	 */
	public String getSchedulerName() {
		return schedulerName;
	}

	/**
	 * @param schedulerName the schedulerName to set
	 */
	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}

	/**
	 * @return the isRunning
	 */
	public Boolean getIsRunning() {
		return isRunning;
	}

	/**
	 * @param isRunning the isRunning to set
	 */
	public void setIsRunning(Boolean isRunning) {
		this.isRunning = isRunning;
	}

	/**
	 * @return the recordsToBeProcessed
	 */
	public Long getRecordsToBeProcessed() {
		return recordsToBeProcessed;
	}

	/**
	 * @param recordsToBeProcessed the recordsToBeProcessed to set
	 */
	public void setRecordsToBeProcessed(Long recordsToBeProcessed) {
		this.recordsToBeProcessed = recordsToBeProcessed;
	}

	/**
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}
