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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Bhavana Thakare Date 17-12-21
 */

@Entity
@Table(name = "TBL_XBRL_TAXONOMY")
@JsonInclude(Include.NON_NULL)

public class XbrlTaxonomy implements Serializable {
	private static final long serialVersionUID = 3350182948955797332L;
	@Id
	@Column(name = "XBRL_TAXONOMY_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int xbrltaxonomyId;

	@Column(name = "IS_ACTIVE")
	private Boolean active;

	@Column(name = "VERSION_NUMBER")
	private String versionNumber;

	@Column(name = "VERSION_DESC")
	private String versionDesc;

	@Column(name = "VALID_FROM_DATE")
	private Date validFromDate;

	@Column(name = "FILE_NAME")
	private String fileName;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY_FK")
	private UserMaster createdByFk;

	public int getXbrltaxonomyId() {
		return xbrltaxonomyId;
	}

	public void setXbrltaxonomyId(int xbrltaxonomyId) {
		this.xbrltaxonomyId = xbrltaxonomyId;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getVersionDesc() {
		return versionDesc;
	}

	public void setVersionDesc(String versionDesc) {
		this.versionDesc = versionDesc;
	}

	public Date getValidFromDate() {
		return validFromDate;
	}

	public void setValidFromDate(Date validFromDate) {
		this.validFromDate = validFromDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public UserMaster getCreatedByFk() {
		return createdByFk;
	}

	public void setCreatedByFk(UserMaster createdByFk) {
		this.createdByFk = createdByFk;
	}

}
