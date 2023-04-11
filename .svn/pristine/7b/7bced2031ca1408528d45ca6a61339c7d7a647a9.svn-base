/**
 * 
 */
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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Siddique
 *
 */
@Entity
@Table(name = "TBL_PAN_MASTER_BULK")
@JsonInclude(Include.NON_NULL)
public class PanMasterBulk implements Serializable {

	private static final long serialVersionUID = 4015126415539400978L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "FILE_NAME")
	private String fileName;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY_FK")
	private UserMaster createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "TOTAL_RECORDS")
	private Long totalRecords;

	@Column(name = "NUM_OF_SUCCESSFUL")
	private Long numOfSuccessfull;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID_FK")
	private PanStatus statusId;

	@Column(name = "IS_PROCESSED")
	private Boolean isProcessed;

	@Column(name = "PROCESS_START_TIME")
	private Date processStartTime;

	@Column(name = "PROCESS_END_TIME")
	private Date processEndTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENTITY_ID_FK")
	private EntityBean entityBean;

	@Transient
	private String filePath;

	@Column(name = "ORIGINAL_FILE_NAME")
	private String originalFileName;

	@Transient
	private Long createdOnLong;

	@Transient
	private Long processStartTimeInLong;

	@Transient
	private Long processEndTimeInLong;

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

	public Long getCreatedOnLong() {
		return createdOnLong;
	}

	public void setCreatedOnLong(Long createdOnLong) {
		this.createdOnLong = createdOnLong;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return the entityBean
	 */
	public EntityBean getEntityBean() {
		return entityBean;
	}

	/**
	 * @param entityBean the entityBean to set
	 */
	public void setEntityBean(EntityBean entityBean) {
		this.entityBean = entityBean;
	}

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
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	/**
	 * @return the createdBy
	 */
	public UserMaster getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(UserMaster createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the totalRecords
	 */
	public Long getTotalRecords() {
		return totalRecords;
	}

	/**
	 * @param totalRecords the totalRecords to set
	 */
	public void setTotalRecords(Long totalRecords) {
		this.totalRecords = totalRecords;
	}

	/**
	 * @return the numOfSuccessfull
	 */
	public Long getNumOfSuccessfull() {
		return numOfSuccessfull;
	}

	/**
	 * @param numOfSuccessfull the numOfSuccessfull to set
	 */
	public void setNumOfSuccessfull(Long numOfSuccessfull) {
		this.numOfSuccessfull = numOfSuccessfull;
	}

	/**
	 * @return the statusId
	 */
	public PanStatus getStatusId() {
		return statusId;
	}

	/**
	 * @param statusId the statusId to set
	 */
	public void setStatusId(PanStatus statusId) {
		this.statusId = statusId;
	}

	/**
	 * @return the isProcessed
	 */
	public Boolean getIsProcessed() {
		return isProcessed;
	}

	/**
	 * @param isProcessed the isProcessed to set
	 */
	public void setIsProcessed(Boolean isProcessed) {
		this.isProcessed = isProcessed;
	}

	/**
	 * @return the processStartTime
	 */
	public Date getProcessStartTime() {
		return processStartTime;
	}

	/**
	 * @param processStartTime the processStartTime to set
	 */
	public void setProcessStartTime(Date processStartTime) {
		this.processStartTime = processStartTime;
	}

	/**
	 * @return the processEndTime
	 */
	public Date getProcessEndTime() {
		return processEndTime;
	}

	/**
	 * @param processEndTime the processEndTime to set
	 */
	public void setProcessEndTime(Date processEndTime) {
		this.processEndTime = processEndTime;
	}

}
