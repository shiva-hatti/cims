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
 * @author Nitin Sonawane Date: 17/04/2018
 */
@Entity
@Table(name = "TBL_RETURN_GROUP_LBL_MOD")
public class ReturnGroupLblMod implements Serializable {

	private static final long serialVersionUID = 8150801831605670759L;

	@Id
	//	@SequenceGenerator(name = "RETURN_GROUP_LBL_MOD_ID_GENERATOR", sequenceName = "TBL_RETURN_GROUP_LBL_MOD_SEQ", allocationSize = 1)
	//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RETURN_GROUP_LBL_MOD_ID_GENERATOR")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RETURN_GROUP_LBL_MOD_ID")
	private Long returngroupLblModId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_GROUP_LBL_FK")
	private ReturnGroupLabelMapping rtnGroupLabel;

	@Column(name = "RETURN_GROUP_LBL_NAME")
	private String rtnGroupLabelName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LANG_ID_FK")
	private LanguageMaster langIdFk;

	@Column(name = "ACTION_ID_FK")
	private Integer actionIdFK;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFIED_BY_FK")
	private UserMaster userModify;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

	public ReturnGroupLblMod() {

	}

	public ReturnGroupLblMod(Long returngroupLblModId, String rtnGroupLabelName, Integer actionIdFK, String createdUserName, Date createdOn, Boolean isActive, String userModify, Date lastModifiedOn, String languageName) {
		this.returngroupLblModId = returngroupLblModId;
		UserMaster userMaster1 = new UserMaster();
		userMaster1.setUserName(userModify);
		this.userModify = userMaster1;
		this.modifiedOn = lastModifiedOn;
		this.rtnGroupLabelName = rtnGroupLabelName;
		this.actionIdFK = actionIdFK;
		ReturnGroupLabelMapping returnGroupLabelMapping = new ReturnGroupLabelMapping();
		ReturnGroupMapping returnGroupMapping = new ReturnGroupMapping();
		UserMaster usr = new UserMaster();
		usr.setUserName(createdUserName);
		returnGroupMapping.setCreatedBy(usr);
		returnGroupMapping.setCreatedOn(createdOn);
		returnGroupMapping.setIsActive(isActive);
		returnGroupLabelMapping.setReturnGroupMapIdFk(returnGroupMapping);
		this.rtnGroupLabel = returnGroupLabelMapping;
		LanguageMaster languageMaster = new LanguageMaster();
		languageMaster.setLanguageName(languageName);
		this.langIdFk = languageMaster;
	}

	/**
	 * @return the returngroupLblModId
	 */
	public Long getReturngroupLblModId() {
		return returngroupLblModId;
	}

	/**
	 * @param returngroupLblModId the returngroupLblModId to set
	 */
	public void setReturngroupLblModId(Long returngroupLblModId) {
		this.returngroupLblModId = returngroupLblModId;
	}

	/**
	 * @return the rtnGroupLabel
	 */
	public ReturnGroupLabelMapping getRtnGroupLabel() {
		return rtnGroupLabel;
	}

	/**
	 * @param rtnGroupLabel the rtnGroupLabel to set
	 */
	public void setRtnGroupLabel(ReturnGroupLabelMapping rtnGroupLabel) {
		this.rtnGroupLabel = rtnGroupLabel;
	}

	/**
	 * @return the rtnGroupLabelName
	 */
	public String getRtnGroupLabelName() {
		return rtnGroupLabelName;
	}

	/**
	 * @param rtnGroupLabelName the rtnGroupLabelName to set
	 */
	public void setRtnGroupLabelName(String rtnGroupLabelName) {
		this.rtnGroupLabelName = rtnGroupLabelName;
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
	 * @return the actionIdFK
	 */
	public Integer getActionIdFK() {
		return actionIdFK;
	}

	/**
	 * @param actionIdFK the actionIdFK to set
	 */
	public void setActionIdFK(Integer actionIdFK) {
		this.actionIdFK = actionIdFK;
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
}
