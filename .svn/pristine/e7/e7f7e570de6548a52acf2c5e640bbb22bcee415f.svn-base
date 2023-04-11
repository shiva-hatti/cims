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
 * This is NationalityLabel bean class
 * 
 * @author nsasane
 * @date 09/12/15
 * @version 1.0
 */
@Entity
@Table(name = "TBL_NATIONALITY_LABEL")
public class NationalityLabel implements Serializable {

	private static final long serialVersionUID = 3719612438413019839L;

	@Id
	//	@SequenceGenerator(name = "NATIONALITY_LBL_ID_GENERATOR", sequenceName = "TBL_NATIONALITY_LABEL_SEQ", allocationSize = 1)
	//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="NATIONALITY_LBL_ID_GENERATOR")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NATIONALITY_LABEL_ID")
	private long nationalityLblId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NATIONALITY_ID_FK")
	private Nationality nationalityIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LANG_ID_FK")
	private LanguageMaster langIdFk;

	@Column(name = "NATIONALITY_LABEL")
	private String nationalityLabel;

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
	 * @return the nationalityLblId
	 */
	public long getNationalityLblId() {
		return nationalityLblId;
	}

	/**
	 * @param nationalityLblId the nationalityLblId to set
	 */
	public void setNationalityLblId(long nationalityLblId) {
		this.nationalityLblId = nationalityLblId;
	}

	/**
	 * @return the nationalityIdFk
	 */
	public Nationality getNationalityIdFk() {
		return nationalityIdFk;
	}

	/**
	 * @param nationalityIdFk the nationalityIdFk to set
	 */
	public void setNationalityIdFk(Nationality nationalityIdFk) {
		this.nationalityIdFk = nationalityIdFk;
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
	 * @return the nationalityLabel
	 */
	public String getNationalityLabel() {
		return nationalityLabel;
	}

	/**
	 * @param nationalityLabel the nationalityLabel to set
	 */
	public void setNationalityLabel(String nationalityLabel) {
		this.nationalityLabel = Validations.trimInput(nationalityLabel);
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
