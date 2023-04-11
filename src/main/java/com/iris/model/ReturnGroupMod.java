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

/**
 * This is the ReturngroupMod bean class with Hibernate mapping.
 * 
 * @author Nitin Sonawane. Date : 17/04/2018. Version:1.0.
 */

@Entity
@Table(name = "TBL_RETURN_GROUP_MOD")
public class ReturnGroupMod implements Serializable {
	private static final long serialVersionUID = 5830811260683526090L;

	@Id
	//	@SequenceGenerator(name = "RETURN_GROUP_MOD_ID_GENERATOR", sequenceName = "TBL_RETURN_GROUP_MOD_SEQ", allocationSize = 1)
	//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RETURN_GROUP_MOD_ID_GENERATOR")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RETURN_GROUP_MOD_ID")
	private Long returnGroupModId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_GROUP_MAP_FK")
	private ReturnGroupMapping returnGroupMap;

	@Column(name = "RETURN_GROUP_NAME")
	private String returnGroupName;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "ACTION_ID_FK")
	private Integer actionIdFK;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFIED_BY_FK")
	private UserMaster userModify;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

	@Column(name = "RETURNS_MAPPED")
	private String returnsMapped;

	@Transient
	private int singleLanguage;

	public ReturnGroupMod() {

	}

	public ReturnGroupMod(Long returnGroupModId, String returnGroupName, Integer actionIdFK, String returnsMapped, String userCreated, Date createdOn, String userModify, Date lastModifiedOn, Boolean isActive) {
		this.returnGroupModId = returnGroupModId;
		UserMaster userMaster1 = new UserMaster();
		userMaster1.setUserName(userModify);
		this.userModify = userMaster1;
		this.modifiedOn = lastModifiedOn;
		this.returnGroupName = returnGroupName;
		this.actionIdFK = actionIdFK;
		this.isActive = isActive;
		this.returnsMapped = returnsMapped;
		ReturnGroupMapping returnGroupMapping = new ReturnGroupMapping();
		UserMaster usr = new UserMaster();
		usr.setUserName(userCreated);
		returnGroupMapping.setCreatedBy(usr);
		returnGroupMapping.setCreatedOn(createdOn);
		returnGroupMapping.setIsActive(isActive);
	}

	/**
	 * @return the returnGroupModId
	 */
	public Long getReturnGroupModId() {
		return returnGroupModId;
	}

	/**
	 * @return the returnGroupMap
	 */
	public ReturnGroupMapping getReturnGroupMap() {
		return returnGroupMap;
	}

	/**
	 * @param returnGroupMap the returnGroupMap to set
	 */
	public void setReturnGroupMap(ReturnGroupMapping returnGroupMap) {
		this.returnGroupMap = returnGroupMap;
	}

	/**
	 * @return the returnGroupName
	 */
	public String getReturnGroupName() {
		return returnGroupName;
	}

	/**
	 * @param returnGroupName the returnGroupName to set
	 */
	public void setReturnGroupName(String returnGroupName) {
		this.returnGroupName = returnGroupName;
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

	public Integer getActionIdFK() {
		return actionIdFK;
	}

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

	/**
	 * @return the singleLanguage
	 */
	public int getSingleLanguage() {
		return singleLanguage;
	}

	/**
	 * @param singleLanguage the singleLanguage to set
	 */
	public void setSingleLanguage(int singleLanguage) {
		this.singleLanguage = singleLanguage;
	}

	/**
	 * @param returnGroupModId the returnGroupModId to set
	 */
	public void setReturnGroupModId(Long returnGroupModId) {
		this.returnGroupModId = returnGroupModId;
	}

	public String getReturnsMapped() {
		return returnsMapped;
	}

	public void setReturnsMapped(String returnsMapped) {
		this.returnsMapped = returnsMapped;
	}

}
