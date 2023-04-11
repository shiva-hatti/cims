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
 * @author Nitin Sonawane.
 * @version 1.0
 */
@Entity
@Table(name = "TBL_RETURN_GROUP_LABEL_MAP")
public class ReturnGroupLabelMapping implements Serializable {

	private static final long serialVersionUID = -7056562698740303796L;

	@Id
	//	@SequenceGenerator(name = "RETURN_GROUP_LBL_ID_GENERATOR", sequenceName = "TBL_RETURN_GROUP_LABEL_MAP_SEQ", allocationSize = 1)
	//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RETURN_GROUP_LBL_ID_GENERATOR")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RETURN_GROUP_LABEL_MAP_ID")
	private Long returnGroupLabelMapId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_GROUP_MAP_ID_FK")
	private ReturnGroupMapping returnGroupMapIdFk;

	@Column(name = "GROUP_LABEL")
	private String groupLabel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LANG_ID_FK")
	private LanguageMaster langIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_MODIFIED_BY_FK")
	private UserMaster userModify;

	@Column(name = "LAST_MODIFIED_ON")
	private Date lastModifiedOn;

	@Column(name = "LAST_UPDATE_ON")
	private Date lastUpdateOn;

	@Transient
	private Long roleIdKey;

	public ReturnGroupLabelMapping() {

	}

	public ReturnGroupLabelMapping(Long returnGroupLabelMapId, String groupLabel) {
		this.returnGroupLabelMapId = returnGroupLabelMapId;
		this.groupLabel = groupLabel;
	}

	public ReturnGroupLabelMapping(Long returnGroupLabelMapId, String groupLabel, Long returnGroupMapId, String defaultGroupName, Boolean isActive, Long createdBy, String createdUserName, Date createdOn, Long modifyBy, String modifyUserName, Date modifyOn, String languageName) {
		this.returnGroupLabelMapId = returnGroupLabelMapId;
		this.groupLabel = groupLabel;
		ReturnGroupMapping returnGroupMapping = new ReturnGroupMapping(returnGroupMapId, defaultGroupName);
		returnGroupMapping.setIsActive(isActive);
		UserMaster usr = new UserMaster(createdBy, createdUserName);
		returnGroupMapping.setCreatedBy(usr);
		returnGroupMapping.setCreatedOn(createdOn);
		this.returnGroupMapIdFk = returnGroupMapping;
		UserMaster usr1 = new UserMaster(modifyBy, modifyUserName);
		this.userModify = usr1;
		this.lastUpdateOn = modifyOn;
		LanguageMaster languageMaster = new LanguageMaster();
		languageMaster.setLanguageName(languageName);
		this.langIdFk = languageMaster;
	}

	/**
	 * @return the returnGroupLabelMapId
	 */
	public Long getReturnGroupLabelMapId() {
		return returnGroupLabelMapId;
	}

	/**
	 * @param returnGroupLabelMapId the returnGroupLabelMapId to set
	 */
	public void setReturnGroupLabelMapId(Long returnGroupLabelMapId) {
		this.returnGroupLabelMapId = returnGroupLabelMapId;
	}

	/**
	 * @return the returnGroupMapIdFk
	 */
	public ReturnGroupMapping getReturnGroupMapIdFk() {
		return returnGroupMapIdFk;
	}

	/**
	 * @param returnGroupMapIdFk the returnGroupMapIdFk to set
	 */
	public void setReturnGroupMapIdFk(ReturnGroupMapping returnGroupMapIdFk) {
		this.returnGroupMapIdFk = returnGroupMapIdFk;
	}

	/**
	 * @return the groupLabel
	 */
	public String getGroupLabel() {
		return groupLabel;
	}

	/**
	 * @param groupLabel the groupLabel to set
	 */
	public void setGroupLabel(String groupLabel) {
		this.groupLabel = Validations.trimInput(groupLabel);
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