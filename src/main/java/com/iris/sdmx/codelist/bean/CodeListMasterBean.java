/**
 * 
 */
package com.iris.sdmx.codelist.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.iris.dto.UserDto;
import com.iris.sdmx.dimesnsion.bean.DimensionMasterBean;

/**
 * @author sajadhav
 *
 */
@JsonInclude(Include.NON_DEFAULT)
public class CodeListMasterBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -881827443261884221L;

	private Long clId;

	private String clCode;

	private String clLable;

	private String clVersion;

	private String clDesc;

	private Boolean isActive;

	private UserDto createdBy;

	private Long createdOnInLong;

	private List<CodeListValuesBean> codeListValues;

	//	private String action;

	private int actionId;

	private Long userId;

	private Long roleId;

	private String langCode;

	private Boolean isApproved;

	private String comment;

	private List<CodeListMasterBean> codeListMasterBeans;

	private List<DimensionMasterBean> dimensionMasterBeans;

	private Date lastUpdatedOn;

	private String agencyMasterCode;

	private Long agencyId;

	private Boolean addApproval;

	private Boolean editApproval;

	/**
	 * @return the isApproved
	 */
	public Boolean getIsApproved() {
		return isApproved;
	}

	/**
	 * @param isApproved the isApproved to set
	 */
	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	//	/**
	//	 * @return the action
	//	 */
	//	public String getAction() {
	//		return action;
	//	}
	//
	//	/**
	//	 * @param action the action to set
	//	 */
	//	public void setAction(String action) {
	//		this.action = action;
	//	}

	/**
	 * @return the clId
	 */
	public Long getClId() {
		return clId;
	}

	/**
	 * @param clId the clId to set
	 */
	public void setClId(Long clId) {
		this.clId = clId;
	}

	/**
	 * @return the clCode
	 */
	public String getClCode() {
		return clCode;
	}

	/**
	 * @param clCode the clCode to set
	 */
	public void setClCode(String clCode) {
		this.clCode = clCode;
	}

	/**
	 * @return the clLable
	 */
	public String getClLable() {
		return clLable;
	}

	/**
	 * @param clLable the clLable to set
	 */
	public void setClLable(String clLable) {
		this.clLable = clLable;
	}

	/**
	 * @return the clVersion
	 */
	public String getClVersion() {
		return clVersion;
	}

	/**
	 * @param clVersion the clVersion to set
	 */
	public void setClVersion(String clVersion) {
		this.clVersion = clVersion;
	}

	/**
	 * @return the clDesc
	 */
	public String getClDesc() {
		return clDesc;
	}

	/**
	 * @param clDesc the clDesc to set
	 */
	public void setClDesc(String clDesc) {
		this.clDesc = clDesc;
	}

	/**
	 * @return the createdBy
	 */
	public UserDto getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(UserDto createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the codeListValues
	 */
	public List<CodeListValuesBean> getCodeListValues() {
		return codeListValues;
	}

	/**
	 * @param codeListValues the codeListValues to set
	 */
	public void setCodeListValues(List<CodeListValuesBean> codeListValues) {
		this.codeListValues = codeListValues;
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
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the roleId
	 */
	public Long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the langCode
	 */
	public String getLangCode() {
		return langCode;
	}

	/**
	 * @param langCode the langCode to set
	 */
	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	/**
	 * @return the createdOnInLong
	 */
	public Long getCreatedOnInLong() {
		return createdOnInLong;
	}

	/**
	 * @param createdOnInLong the createdOnInLong to set
	 */
	public void setCreatedOnInLong(Long createdOnInLong) {
		this.createdOnInLong = createdOnInLong;
	}

	/**
	 * @return the actionId
	 */
	public int getActionId() {
		return actionId;
	}

	/**
	 * @param actionId the actionId to set
	 */
	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	/**
	 * @return the codeListMasterBeans
	 */
	public List<CodeListMasterBean> getCodeListMasterBeans() {
		return codeListMasterBeans;
	}

	/**
	 * @param codeListMasterBeans the codeListMasterBeans to set
	 */
	public void setCodeListMasterBeans(List<CodeListMasterBean> codeListMasterBeans) {
		this.codeListMasterBeans = codeListMasterBeans;
	}

	public List<DimensionMasterBean> getDimensionMasterBeans() {
		return dimensionMasterBeans;
	}

	public void setDimensionMasterBeans(List<DimensionMasterBean> dimensionMasterBeans) {
		this.dimensionMasterBeans = dimensionMasterBeans;
	}

	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	/**
	 * @return the agencyMasterCode
	 */
	public String getAgencyMasterCode() {
		return agencyMasterCode;
	}

	/**
	 * @param agencyMasterCode the agencyMasterCode to set
	 */
	public void setAgencyMasterCode(String agencyMasterCode) {
		this.agencyMasterCode = agencyMasterCode;
	}

	/**
	 * @return the addApproval
	 */
	public Boolean getAddApproval() {
		return addApproval;
	}

	/**
	 * @param addApproval the addApproval to set
	 */
	public void setAddApproval(Boolean addApproval) {
		this.addApproval = addApproval;
	}

	/**
	 * @return the editApproval
	 */
	public Boolean getEditApproval() {
		return editApproval;
	}

	/**
	 * @param editApproval the editApproval to set
	 */
	public void setEditApproval(Boolean editApproval) {
		this.editApproval = editApproval;
	}

	/**
	 * @return the agencyId
	 */
	public Long getAgencyId() {
		return agencyId;
	}

	/**
	 * @param agencyId the agencyId to set
	 */
	public void setAgencyId(Long agencyId) {
		this.agencyId = agencyId;
	}

}
