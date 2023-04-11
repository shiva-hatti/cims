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

/**
 * @author pmohite
 * @version 1.0
 * @date 25/02/2021
 */
@Entity
@Table(name = "TBL_RETURN_SEC_VERSION_MAP")
public class ReturnSectionVersionMap implements Serializable {

	private static final long serialVersionUID = -6374846772336929625L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RETURN_SEC_VERSION_ID")
	private Integer returnSecVersionMapId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TAXONOMY_ID_FK")
	private ReturnTemplate taxonomyIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_SEC_ID_FK")
	private ReturnSectionMap returnSecIdFk;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY_FK")
	private UserMaster createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFIED_BY_FK")
	private UserMaster userModify;

	@Column(name = "MODIFIED_ON")
	private Date lastModifiedOn;

	public Integer getReturnSecVersionMapId() {
		return returnSecVersionMapId;
	}

	public void setReturnSecVersionMapId(Integer returnSecVersionMapId) {
		this.returnSecVersionMapId = returnSecVersionMapId;
	}

	public ReturnTemplate getTaxonomyIdFk() {
		return taxonomyIdFk;
	}

	public void setTaxonomyIdFk(ReturnTemplate taxonomyIdFk) {
		this.taxonomyIdFk = taxonomyIdFk;
	}

	public ReturnSectionMap getReturnSecIdFk() {
		return returnSecIdFk;
	}

	public void setReturnSecIdFk(ReturnSectionMap returnSecIdFk) {
		this.returnSecIdFk = returnSecIdFk;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
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

	public UserMaster getUserModify() {
		return userModify;
	}

	public void setUserModify(UserMaster userModify) {
		this.userModify = userModify;
	}

	public Date getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

}