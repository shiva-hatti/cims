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
 * This is the MENU LABEL bean class with Hibernate mapping.
 * 
 * @author pippar
 * @date 11/06/2015
 *
 * @author GDeepakRao
 * @date 28/06/2015
 * @version 1.1
 */
@Entity
@Table(name = "TBL_MENU_LABEL")
public class MenuLabel implements Serializable {

	private static final long serialVersionUID = -602972493747403758L;

	@Id
	//	@SequenceGenerator(name = "MENU_LABEL_ID_GENERATOR", sequenceName = "TBL_MENU_LABEL_SEQ", allocationSize = 1)
	//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MENU_LABEL_ID_GENERATOR")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MENU_LABEL_ID")
	private Long menuId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MENU_ID_FK")
	private Menu menuIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LANGUAGE_ID_FK")
	private LanguageMaster languageIdFk;

	@Column(name = "MENU_LABEL")
	private String menuLabel;

	@Column(name = "MENU_LABEL_DETAILS")
	private String menuLabelDetail;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_APPROVED_BY_FK")
	private UserMaster approvedBy;

	@Column(name = "LAST_APPROVED_ON")
	private Date approvedOn;

	@Column(name = "LAST_UPDATE_ON")
	private Date lastUpdatedOn;

	@Transient
	private Long roleIdKey;

	@Column(name = "IS_BULK_UPLOAD")
	private Boolean isBulkUpload;

	/**
	 * @return the menuLabelDetail
	 */
	public String getMenuLabelDetail() {
		return menuLabelDetail;
	}

	/**
	 * @param menuLabelDetail the menuLabelDetail to set
	 */
	public void setMenuLabelDetail(String menuLabelDetail) {
		this.menuLabelDetail = Validations.trimInput(menuLabelDetail);
	}

	/**
	 * @return the menuId
	 */
	public Long getMenuId() {
		return menuId;
	}

	/**
	 * @param menuId the menuId to set
	 */
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	/**
	 * @return the menuIdFk
	 */
	public Menu getMenuIdFk() {
		return menuIdFk;
	}

	/**
	 * @param menuIdFk the menuIdFk to set
	 */
	public void setMenuIdFk(Menu menuIdFk) {
		this.menuIdFk = menuIdFk;
	}

	/**
	 * @return the languageIdFk
	 */
	public LanguageMaster getLanguageIdFk() {
		return languageIdFk;
	}

	/**
	 * @param languageIdFk the languageIdFk to set
	 */
	public void setLanguageIdFk(LanguageMaster languageIdFk) {
		this.languageIdFk = languageIdFk;
	}

	/**
	 * @return the menuLabel
	 */
	public String getMenuLabel() {
		return menuLabel;
	}

	/**
	 * @param menuLabel the menuLabel to set
	 */
	public void setMenuLabel(String menuLabel) {
		this.menuLabel = Validations.trimInput(menuLabel);
	}

	/**
	 * @return the modifiedOn
	 */
	public Date getModifiedOn() {
		return modifiedOn;
	}

	/**
	 * @param modifiedOn the modifiedOn to set
	 */
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
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
	 * @return the approvedBy
	 */
	public UserMaster getApprovedBy() {
		return approvedBy;
	}

	/**
	 * @param approvedBy the approvedBy to set
	 */
	public void setApprovedBy(UserMaster approvedBy) {
		this.approvedBy = approvedBy;
	}

	/**
	 * @return the approvedOn
	 */
	public Date getApprovedOn() {
		return approvedOn;
	}

	/**
	 * @param approvedOn the approvedOn to set
	 */
	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
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
	 * @return the lastUpdatedOn
	 */
	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	/**
	 * @param lastUpdatedOn the lastUpdatedOn to set
	 */
	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
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

	/**
	 * @return the isBulkUpload
	 */
	public Boolean getIsBulkUpload() {
		return isBulkUpload;
	}

	/**
	 * @param isBulkUpload the isBulkUpload to set
	 */
	public void setIsBulkUpload(Boolean isBulkUpload) {
		this.isBulkUpload = isBulkUpload;
	}

}