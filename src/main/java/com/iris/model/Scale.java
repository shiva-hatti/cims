package com.iris.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.iris.util.UtilMaster;

@Entity
@Table(name = "TBL_SCALE")
public class Scale implements Serializable {

	private static final long serialVersionUID = -3356186136870134894L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SCALE_ID")
	private Integer scaleId;

	@Column(name = "SCALE_NAME")
	private String scaleName;

	@Column(name = "SCALE_VALUE")
	private String scaleValue;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "LAST_MODIFIED_ON")
	private Date lastModifiedOn;

	@Column(name = "LAST_UPDATED_ON")
	private Date lastUpdatedOn;

	@Column(name = "STORED_UNIT_SK")
	private String storedUnitSk;

	@Column(name = "SCALE_NAME_WEBFORM")
	private String scaleNameWebform;

	public Integer getScaleId() {
		return scaleId;
	}

	public void setScaleId(Integer scaleId) {
		this.scaleId = scaleId;
	}

	public String getScaleName() {
		return scaleName;
	}

	public void setScaleName(String scaleName) {
		this.scaleName = UtilMaster.trimInput(scaleName);
	}

	public String getScaleValue() {
		return scaleValue;
	}

	public void setScaleValue(String scaleValue) {
		this.scaleValue = UtilMaster.trimInput(scaleValue);
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public String getStoredUnitSk() {
		return storedUnitSk;
	}

	public void setStoredUnitSk(String storedUnitSk) {
		this.storedUnitSk = UtilMaster.trimInput(storedUnitSk);
	}

	public String getScaleNameWebform() {
		return scaleNameWebform;
	}

	public void setScaleNameWebform(String scaleNameWebform) {
		this.scaleNameWebform = UtilMaster.trimInput(scaleNameWebform);
	}

}