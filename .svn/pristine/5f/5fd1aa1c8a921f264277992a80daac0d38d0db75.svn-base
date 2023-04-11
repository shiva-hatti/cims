/**
 * 
 */
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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.iris.util.Validations;

/**
 * @author vpathak
 * @version 2.0
 * @date 4-dec-2014
 *
 */
@Entity
@Table(name = "TBL_USER_ROLE_LABEL")
public class UserRoleLabel implements Serializable {

	private static final long serialVersionUID = -8235309715872869862L;
	@Id
	//	@SequenceGenerator(name = "USER_ROLE_LABEL_ID_GENERATOR", sequenceName = "TBL_USER_ROLE_LABEL_SEQ", allocationSize = 1)
	//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_ROLE_LABEL_ID_GENERATOR")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ROLE_LABEL_ID")
	private Long userRoleLabelId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ROLE_ID_FK")
	private UserRole userRoleIdFk;

	@Column(name = "USER_ROLE_LABEL")
	private String userRoleLabel;

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
	 * @return the userRoleLabelId
	 */
	public Long getUserRoleLabelId() {
		return userRoleLabelId;
	}

	/**
	 * @param userRoleLabelId the userRoleLabelId to set
	 */
	public void setUserRoleLabelId(Long userRoleLabelId) {
		this.userRoleLabelId = userRoleLabelId;
	}

	/**
	 * @return the userRoleIdFk
	 */
	public UserRole getUserRoleIdFk() {
		return userRoleIdFk;
	}

	/**
	 * @param userRoleIdFk the userRoleIdFk to set
	 */
	public void setUserRoleIdFk(UserRole userRoleIdFk) {
		this.userRoleIdFk = userRoleIdFk;
	}

	/**
	 * @return the userRoleLabel
	 */
	public String getUserRoleLabel() {
		return userRoleLabel;
	}

	/**
	 * @param userRoleLabel the userRoleLabel to set
	 */
	public void setUserRoleLabel(String userRoleLabel) {
		this.userRoleLabel = Validations.trimInput(userRoleLabel);
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
