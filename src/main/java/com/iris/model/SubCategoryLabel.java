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

import com.iris.util.Validations;

/**
 * This bean class for Sub-Category label Mngt
 * 
 * @author nsasane
 * @date 3/12/15 @ version 1.0
 */

@Entity
@Table(name = "TBL_SUB_CATEGORY_LABEL")
public class SubCategoryLabel implements Serializable {

	private static final long serialVersionUID = -4543460259158627811L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SUB_CATEGORY_LABEL_ID")
	private Long subCatLabelId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUB_CATEGORY_ID_FK")
	private SubCategory subCatIdFk;

	@Column(name = "SUB_CATEGORY_LABEL")
	private String subCategoryLabel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LANG_ID_FK")
	private LanguageMaster langIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_MODIFIED_BY_FK")
	private UserMaster userModify;

	@Column(name = "LAST_MODIFIED_ON")
	private Date lastModifiedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_APPROVED_BY_FK")
	private UserMaster lastApprovedBy;

	@Column(name = "LAST_APPROVED_ON")
	private Date lastApprovedOn;

	@Column(name = "LAST_UPDATE_ON")
	private Date lastUpdateOn;

	@Transient
	private Long roleIdKey;

	/**
	 * @return the subCatLabelId
	 */
	public Long getSubCatLabelId() {
		return subCatLabelId;
	}

	/**
	 * @param subCatLabelId the subCatLabelId to set
	 */
	public void setSubCatLabelId(Long subCatLabelId) {
		this.subCatLabelId = subCatLabelId;
	}

	/**
	 * @return the subCatIdFk
	 */
	public SubCategory getSubCatIdFk() {
		return subCatIdFk;
	}

	/**
	 * @param subCatIdFk the subCatIdFk to set
	 */
	public void setSubCatIdFk(SubCategory subCatIdFk) {
		this.subCatIdFk = subCatIdFk;
	}

	/**
	 * @return the subCategoryLabel
	 */
	public String getSubCategoryLabel() {
		return subCategoryLabel;
	}

	/**
	 * @param subCategoryLabel the subCategoryLabel to set
	 */
	public void setSubCategoryLabel(String subCategoryLabel) {
		this.subCategoryLabel = Validations.trimInput(subCategoryLabel);
	}

	/**
	 * @return the langIdFk
	 */
	public LanguageMaster getLangIdFk() {
		return langIdFk;
	}

	/**
	 * @param langIdFk the langIdFk to set
	 */
	public void setLangIdFk(LanguageMaster langIdFk) {
		this.langIdFk = langIdFk;
	}

	/**
	 * @return the userModify
	 */
	public UserMaster getUserModify() {
		return userModify;
	}

	/**
	 * @param userModify the userModify to set
	 */
	public void setUserModify(UserMaster userModify) {
		this.userModify = userModify;
	}

	/**
	 * @return the lastModifiedOn
	 */
	public Date getLastModifiedOn() {
		return lastModifiedOn;
	}

	/**
	 * @param lastModifiedOn the lastModifiedOn to set
	 */
	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	/**
	 * @return the lastApprovedBy
	 */
	public UserMaster getLastApprovedBy() {
		return lastApprovedBy;
	}

	/**
	 * @param lastApprovedBy the lastApprovedBy to set
	 */
	public void setLastApprovedBy(UserMaster lastApprovedBy) {
		this.lastApprovedBy = lastApprovedBy;
	}

	/**
	 * @return the lastApprovedOn
	 */
	public Date getLastApprovedOn() {
		return lastApprovedOn;
	}

	/**
	 * @param lastApprovedOn the lastApprovedOn to set
	 */
	public void setLastApprovedOn(Date lastApprovedOn) {
		this.lastApprovedOn = lastApprovedOn;
	}

	/**
	 * @return the lastUpdateOn
	 */
	public Date getLastUpdateOn() {
		return lastUpdateOn;
	}

	/**
	 * @param lastUpdateOn the lastUpdateOn to set
	 */
	public void setLastUpdateOn(Date lastUpdateOn) {
		this.lastUpdateOn = lastUpdateOn;
	}

	/**
	 * @return the roleIdKey
	 */
	public Long getRoleIdKey() {
		return roleIdKey;
	}

	/**
	 * @param roleIdKey the roleIdKey to set
	 */
	public void setRoleIdKey(Long roleIdKey) {
		this.roleIdKey = roleIdKey;
	}
}
