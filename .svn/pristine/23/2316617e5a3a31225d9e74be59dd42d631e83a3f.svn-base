package com.iris.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.iris.util.Validations;

/**
 * This class is used for time format information.
 * 
 * @author Suman Kumar
 * @version 1.0
 */
@Entity
@Table(name = "TBL_TIME_FORMAT")
public class TimeFormat implements Serializable {

	private static final long serialVersionUID = -2536871430124849962L;

	@Id
	@Column(name = "TIME_FORMAT_ID")
	private Long timeFrmtId;

	@Column(name = "TIME_FORMAT")
	private String timeFrmt;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFIED_BY_FK")
	private UserMaster modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	/**
	 * @return the timeFrmtId
	 */
	public Long getTimeFrmtId() {
		return timeFrmtId;
	}

	/**
	 * @param timeFrmtId the timeFrmtId to set
	 */
	public void setTimeFrmtId(Long timeFrmtId) {
		this.timeFrmtId = timeFrmtId;
	}

	/**
	 * @return the timeFrmt
	 */
	public String getTimeFrmt() {
		return timeFrmt;
	}

	/**
	 * @param timeFrmt the timeFrmt to set
	 */
	public void setTimeFrmt(String timeFrmt) {
		this.timeFrmt = Validations.trimInput(timeFrmt);
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
	 * @return the modifiedBy
	 */
	public UserMaster getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(UserMaster modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the modifiedDate
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}