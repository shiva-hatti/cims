package com.iris.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This is the ReturnRegulatorMapping bean class with Hibernate mapping.
 * 
 * @author pmohite
 */
@Entity
@Table(name = "TBL_RETURN_REGULATOR_MAPPING")
@JsonInclude(Include.NON_NULL)
public class ReturnRegulatorMapping implements Serializable {

	private static final long serialVersionUID = 8262415709009662785L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RETURN_REGULATOR_ID")
	private Long returnRegulatorId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_ID_FK")
	private Return returnIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REGULATOR_ID_FK")
	private Regulator regulatorIdFk;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	private UserMaster createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_MODIFIED_BY")
	private UserMaster updatedBy;

	@Column(name = "LAST_MODIFIED_ON")
	private Date updatedOn;

	@Column(name = "EMAIL_IDs")
	private String emailIds;

	@Transient
	private List<Long> returnIdArray;

	@Transient
	private String emailIdString;

	@Transient
	private int returnArrs;

	@Transient
	private Map<String, String> returnEmailIdStringMap;

	@Transient
	private Long roleId;

	@Transient
	private String langCode;

	public ReturnRegulatorMapping() {

	}

	/**
	 * @return the returnRegulatorId
	 */
	public Long getReturnRegulatorId() {
		return returnRegulatorId;
	}

	/**
	 * @param returnRegulatorId the returnRegulatorId to set
	 */
	public void setReturnRegulatorId(Long returnRegulatorId) {
		this.returnRegulatorId = returnRegulatorId;
	}

	/**
	 * @return the returnIdFk
	 */
	public Return getReturnIdFk() {
		return returnIdFk;
	}

	/**
	 * @param returnIdFk the returnIdFk to set
	 */
	public void setReturnIdFk(Return returnIdFk) {
		this.returnIdFk = returnIdFk;
	}

	/**
	 * @return the regulatorIdFk
	 */
	public Regulator getRegulatorIdFk() {
		return regulatorIdFk;
	}

	/**
	 * @param regulatorIdFk the regulatorIdFk to set
	 */
	public void setRegulatorIdFk(Regulator regulatorIdFk) {
		this.regulatorIdFk = regulatorIdFk;
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
	 * @return the emailIds
	 */
	public String getEmailIds() {
		return emailIds;
	}

	/**
	 * @param emailIds the emailIds to set
	 */
	public void setEmailIds(String emailIds) {
		this.emailIds = emailIds;
	}

	/**
	 * @return the emailIdString
	 */
	public String getEmailIdString() {
		return emailIdString;
	}

	/**
	 * @param emailIdString the emailIdString to set
	 */
	public void setEmailIdString(String emailIdString) {
		this.emailIdString = emailIdString;
	}

	/**
	 * @return the updatedBy
	 */
	public UserMaster getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(UserMaster updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the updatedOn
	 */
	public Date getUpdatedOn() {
		return updatedOn;
	}

	/**
	 * @param updatedOn the updatedOn to set
	 */
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	/**
	 * @return the returnArrs
	 */
	public int getReturnArrs() {
		return returnArrs;
	}

	/**
	 * @param returnArrs the returnArrs to set
	 */
	public void setReturnArrs(int returnArrs) {
		this.returnArrs = returnArrs;
	}

	/**
	 * @return the returnEmailIdStringMap
	 */
	public Map<String, String> getReturnEmailIdStringMap() {
		return returnEmailIdStringMap;
	}

	/**
	 * @param returnEmailIdStringMap the returnEmailIdStringMap to set
	 */
	public void setReturnEmailIdStringMap(Map<String, String> returnEmailIdStringMap) {
		this.returnEmailIdStringMap = returnEmailIdStringMap;
	}

	/**
	 * @return the returnIdArray
	 */
	public List<Long> getReturnIdArray() {
		return returnIdArray;
	}

	/**
	 * @param returnIdArray the returnIdArray to set
	 */
	public void setReturnIdArray(List<Long> returnIdArray) {
		this.returnIdArray = returnIdArray;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

}