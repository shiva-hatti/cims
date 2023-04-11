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

@Entity
@Table(name = "TBL_RETURN_SEC_VERSION_MAP")
public class WebformVersionMap implements Serializable {

	private static final long serialVersionUID = -8863619948092575245L;

	@Id
	@Column(name = "RETURN_SEC_VERSION_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer retSecVersionMap;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TAXONOMY_ID_FK")
	private ReturnTemplate taxonomyIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_SEC_ID_FK")
	private ReturnSectionMap retSecMap;

	@Column(name = "IS_ACTIVE")
	private boolean isActive;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY_FK")
	private UserMaster createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFIED_BY_FK")
	private UserMaster modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

	public Integer getRetSecVersionMap() {
		return retSecVersionMap;
	}

	public void setRetSecVersionMap(Integer retSecVersionMap) {
		this.retSecVersionMap = retSecVersionMap;
	}

	public ReturnTemplate getTaxonomyIdFk() {
		return taxonomyIdFk;
	}

	public void setTaxonomyIdFk(ReturnTemplate taxonomyIdFk) {
		this.taxonomyIdFk = taxonomyIdFk;
	}

	public ReturnSectionMap getRetSecMap() {
		return retSecMap;
	}

	public void setRetSecMap(ReturnSectionMap retSecMap) {
		this.retSecMap = retSecMap;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public UserMaster getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserMaster createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public UserMaster getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(UserMaster modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

}
