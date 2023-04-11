package com.iris.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.iris.util.Validations;

/**
 * @author Nitin Sonawane
 * @version 1.0
 */
@Entity
@Table(name = "TBL_RETURN_GROUP_MAPPING")
@JsonInclude(Include.NON_NULL)
public class ReturnGroupMapping implements Serializable {

	private static final long serialVersionUID = 5717195175755935032L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RETURN_GROUP_MAP_ID")
	private Long returnGroupMapId;

	@Column(name = "DEFAULT_GROUP_NAME")
	private String defaultGroupName;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY_FK")
	private UserMaster createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_MODIFIED_BY_FK")
	private UserMaster userModify;

	@Column(name = "LAST_MODIFIED_ON")
	private Date lastModifiedOn;

	@Column(name = "LAST_UPDATE_ON")
	private Date lastUpdateOn;

	@OneToMany(mappedBy = "returnGroupMapIdFk")
	private Set<ReturnGroupLabelMapping> rtnGroupLblSet;

	@Transient
	private int singleLanguage;

	@Transient
	private Long roleIdKey;

	@Column(name = "ORDER_NO")
	private Float orderNo;

	@Transient
	private List<Return> returnList;

	@Transient
	private Long returnCount;

	@Transient
	private String allottedReturns;

	@Column(name = "IS_CROSS_VALIDATION")
	private Boolean isCrossValidation;

	public ReturnGroupMapping() {

	}

	public ReturnGroupMapping(Long returnGroupMapId, String createdBy, Date createdOn, String userModify, Date lastModifiedOn, String returnGroupName, Boolean isActive, Long returnCount) {
		this.returnGroupMapId = returnGroupMapId;
		UserMaster userMaster = new UserMaster();
		userMaster.setUserName(createdBy);
		this.createdBy = userMaster;
		this.createdOn = createdOn;
		UserMaster userMaster1 = new UserMaster();
		userMaster1.setUserName(userModify);
		this.userModify = userMaster1;
		this.lastModifiedOn = lastModifiedOn;
		this.defaultGroupName = returnGroupName;
		this.isActive = isActive;
		this.returnCount = returnCount;
	}

	public ReturnGroupMapping(Long returnGroupMapId, String returnGroupName) {
		this.returnGroupMapId = returnGroupMapId;
		this.defaultGroupName = returnGroupName;
	}

	/**
	 * @return the returnCount
	 */
	public Long getReturnCount() {
		return returnCount;
	}

	/**
	 * @param returnCount the returnCount to set
	 */
	public void setReturnCount(Long returnCount) {
		this.returnCount = returnCount;
	}

	public List<Return> getReturnList() {
		return returnList;
	}

	public void setReturnList(List<Return> returnList) {
		this.returnList = returnList;
	}

	/**
	 * @return the returnGroupMapId
	 */
	public Long getReturnGroupMapId() {
		return returnGroupMapId;
	}

	/**
	 * @param returnGroupMapId the returnGroupMapId to set
	 */
	public void setReturnGroupMapId(Long returnGroupMapId) {
		this.returnGroupMapId = returnGroupMapId;
	}

	/**
	 * @return the defaultGroupName
	 */
	public String getDefaultGroupName() {
		return defaultGroupName;
	}

	/**
	 * @param defaultGroupName the defaultGroupName to set
	 */
	public void setDefaultGroupName(String defaultGroupName) {
		this.defaultGroupName = Validations.trimInput(defaultGroupName);
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

	public int getSingleLanguage() {
		return singleLanguage;
	}

	public void setSingleLanguage(int singleLanguage) {
		this.singleLanguage = singleLanguage;
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
	 * @return the rtnGroupLblSet
	 */
	public Set<ReturnGroupLabelMapping> getRtnGroupLblSet() {
		return rtnGroupLblSet;
	}

	/**
	 * @param rtnGroupLblSet the rtnGroupLblSet to set
	 */
	public void setRtnGroupLblSet(Set<ReturnGroupLabelMapping> rtnGroupLblSet) {
		this.rtnGroupLblSet = rtnGroupLblSet;
	}

	/**
	 * @return the orderNo
	 */
	public Float getOrderNo() {
		return orderNo;
	}

	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(Float orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * @return the allottedReturns
	 */
	public String getAllottedReturns() {
		return allottedReturns;
	}

	/**
	 * @param allottedReturns the allottedReturns to set
	 */
	public void setAllottedReturns(String allottedReturns) {
		this.allottedReturns = allottedReturns;
	}

	public Boolean getIsCrossValidation() {
		return isCrossValidation;
	}

	public void setIsCrossValidation(Boolean isCrossValidation) {
		this.isCrossValidation = isCrossValidation;
	}

	public Date getLastUpdateOn() {
		return lastUpdateOn;
	}

	public void setLastUpdateOn(Date lastUpdateOn) {
		this.lastUpdateOn = lastUpdateOn;
	}

	@Override
	public String toString() {
		return "ReturnGroupMapping [returnGroupMapId=" + returnGroupMapId + ", defaultGroupName=" + defaultGroupName + ", isActive=" + isActive + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", userModify=" + userModify + ", lastModifiedOn=" + lastModifiedOn + ", lastUpdateOn=" + lastUpdateOn + ", rtnGroupLblSet=" + rtnGroupLblSet + ", singleLanguage=" + singleLanguage + ", roleIdKey=" + roleIdKey + ", orderNo=" + orderNo + ", returnList=" + returnList + ", returnCount=" + returnCount + ", allottedReturns=" + allottedReturns + ", isCrossValidation=" + isCrossValidation + "]";
	}

	//	/**
	//	 * @return the returnSet
	//	 */
	//	public Set<Return> getReturnSet() {
	//		return returnSet;
	//	}
	//
	//	/**
	//	 * @param returnSet the returnSet to set
	//	 */
	//	public void setReturnSet(Set<Return> returnSet) {
	//		this.returnSet = returnSet;
	//	}

}