package com.iris.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * @author psawant
 * @version 1.0
 * @date 04/10/2017
 */
@Entity
@Table(name = "TBL_DYNAMIC_ELE_COL")
public class DynamicEleCol implements Serializable {

	private static final long serialVersionUID = 1931795753971143694L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DYNAMIC_ELE_COL_ID")
	private Integer dynamicEleColId;

	@Column(name = "IS_COLUMN_TYPE")
	private Integer isColumnType;

	@Column(name = "FIELD_NAME")
	private String fieldName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FIELD_TYPE_ID_FK")
	private DynamicFieldType fieldTypeIdfK;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY_FK")
	private UserMaster createdByFk;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFIED_BY_FK")
	private UserMaster modifiedByFk;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

	@Column(name = "DYNAMIC_URL")
	private String dynamicUrl;

	@Column(name = "DEFAULT_FIELD_NAME")
	private String defaultFieldName;

	@Column(name = "LAST_UPDATE_ON")
	private Date lastUpdateOn;

	@Column(name = "MIN_ELEMENT_SIZE")
	private Long minEleSize;

	@Column(name = "MAX_ELEMENT_SIZE")
	private Long maxEleSize;

	@Column(name = "FILE_TYPE")
	private String fileType;

	@Column(name = "FILE_SIZE")
	private Long fileSize;

	public Integer getDynamicEleColId() {
		return dynamicEleColId;
	}

	public void setDynamicEleColId(Integer dynamicEleColId) {
		this.dynamicEleColId = dynamicEleColId;
	}

	public Integer getIsColumnType() {
		return isColumnType;
	}

	public void setIsColumnType(Integer isColumnType) {
		this.isColumnType = isColumnType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public DynamicFieldType getFieldTypeIdfK() {
		return fieldTypeIdfK;
	}

	public void setFieldTypeIdfK(DynamicFieldType fieldTypeIdfK) {
		this.fieldTypeIdfK = fieldTypeIdfK;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public UserMaster getCreatedByFk() {
		return createdByFk;
	}

	public void setCreatedByFk(UserMaster createdByFk) {
		this.createdByFk = createdByFk;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public UserMaster getModifiedByFk() {
		return modifiedByFk;
	}

	public void setModifiedByFk(UserMaster modifiedByFk) {
		this.modifiedByFk = modifiedByFk;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getDynamicUrl() {
		return dynamicUrl;
	}

	public void setDynamicUrl(String dynamicUrl) {
		this.dynamicUrl = dynamicUrl;
	}

	public String getDefaultFieldName() {
		return defaultFieldName;
	}

	public void setDefaultFieldName(String defaultFieldName) {
		this.defaultFieldName = defaultFieldName;
	}

	public Date getLastUpdateOn() {
		return lastUpdateOn;
	}

	public void setLastUpdateOn(Date lastUpdateOn) {
		this.lastUpdateOn = lastUpdateOn;
	}

	public Long getMinEleSize() {
		return minEleSize;
	}

	public void setMinEleSize(Long minEleSize) {
		this.minEleSize = minEleSize;
	}

	public Long getMaxEleSize() {
		return maxEleSize;
	}

	public void setMaxEleSize(Long maxEleSize) {
		this.maxEleSize = maxEleSize;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

}