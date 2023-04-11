package com.iris.ebr.business.technical.metadata.entity;

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

import com.iris.model.UserMaster;

@Table(name = "TBL_SDMX_TECH_METADATA_PROCESS")
@Entity
public class TechMetadatProcess implements Serializable {

	/**
	 * @author sdhone
	 */
	private static final long serialVersionUID = -3244372879969106205L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SDMX_TECH_METADATA_PROCESS_ID")
	private Long techMetadataProcessId;

	@Column(name = "TECH_METADATA_UPLOADED_ON")
	private Date techMetadataUploadedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TECH_METADATA_CREATED_BY_FK")
	private UserMaster techMetadataCreatedBy;

	@Column(name = "TECH_METADATA_FILE_NAME")
	private String techMetadataFileName;

	@Column(name = "TECH_METADATA_INSERT_START")
	private Date techMetadataInsertStart;

	@Column(name = "TECH_METADATA_INSERT_END")
	private Date techMetadataInsertEnd;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "INSERT_STATUS")
	private Boolean insertStatus;

	/**
	 * @return the techMetadataProcessId
	 */
	public Long getTechMetadataProcessId() {
		return techMetadataProcessId;
	}

	/**
	 * @param techMetadataProcessId the techMetadataProcessId to set
	 */
	public void setTechMetadataProcessId(Long techMetadataProcessId) {
		this.techMetadataProcessId = techMetadataProcessId;
	}

	/**
	 * @return the techMetadataUploadedOn
	 */
	public Date getTechMetadataUploadedOn() {
		return techMetadataUploadedOn;
	}

	/**
	 * @param techMetadataUploadedOn the techMetadataUploadedOn to set
	 */
	public void setTechMetadataUploadedOn(Date techMetadataUploadedOn) {
		this.techMetadataUploadedOn = techMetadataUploadedOn;
	}

	/**
	 * @return the techMetadataCreatedBy
	 */
	public UserMaster getTechMetadataCreatedBy() {
		return techMetadataCreatedBy;
	}

	/**
	 * @param techMetadataCreatedBy the techMetadataCreatedBy to set
	 */
	public void setTechMetadataCreatedBy(UserMaster techMetadataCreatedBy) {
		this.techMetadataCreatedBy = techMetadataCreatedBy;
	}

	/**
	 * @return the techMetadataFileName
	 */
	public String getTechMetadataFileName() {
		return techMetadataFileName;
	}

	/**
	 * @param techMetadataFileName the techMetadataFileName to set
	 */
	public void setTechMetadataFileName(String techMetadataFileName) {
		this.techMetadataFileName = techMetadataFileName;
	}

	/**
	 * @return the techMetadataInsertStart
	 */
	public Date getTechMetadataInsertStart() {
		return techMetadataInsertStart;
	}

	/**
	 * @param techMetadataInsertStart the techMetadataInsertStart to set
	 */
	public void setTechMetadataInsertStart(Date techMetadataInsertStart) {
		this.techMetadataInsertStart = techMetadataInsertStart;
	}

	/**
	 * @return the techMetadataInsertEnd
	 */
	public Date getTechMetadataInsertEnd() {
		return techMetadataInsertEnd;
	}

	/**
	 * @param techMetadataInsertEnd the techMetadataInsertEnd to set
	 */
	public void setTechMetadataInsertEnd(Date techMetadataInsertEnd) {
		this.techMetadataInsertEnd = techMetadataInsertEnd;
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
	 * @return the insertStatus
	 */
	public Boolean getInsertStatus() {
		return insertStatus;
	}

	/**
	 * @param insertStatus the insertStatus to set
	 */
	public void setInsertStatus(Boolean insertStatus) {
		this.insertStatus = insertStatus;
	}

}
