package com.iris.sdmx.element.bean;

import java.io.Serializable;
import java.util.Date;

public class ElementListBean implements Serializable, Comparable<ElementListBean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6748594183033402666L;

	private Long elementId;

	private String dsdCode;

	private String elementLabel;

	private String elementVer;

	private Long classificationTypeId;

	private String classificationName;

	private Long dependencyTypeId;

	private String dependencyTypeName;

	private Long flowTypeId;

	private String flowTypeName;

	private Long natureTypeId;

	private String natureTypeName;

	private Long sectorTypeId;

	private String sectorTypeName;

	private String elementDesc;

	private Long elementParentId;

	private String parentElementVer;

	private String parentElementDsdCode;

	private Boolean isActive;

	private Date lastUpdatedOn;

	private Long createdBy;

	private String createdByUserName;

	private Date createdOn;

	private Long modifyBy;

	private Date modifyOn;

	private String modifyByUserName;

	private Long ownerDepartmentId;

	private String ownerDepartmentCode;

	private String ownerDepartmentName;

	/**
	 * @return the elementId
	 */
	public Long getElementId() {
		return elementId;
	}

	/**
	 * @param elementId the elementId to set
	 */
	public void setElementId(Long elementId) {
		this.elementId = elementId;
	}

	/**
	 * @return the dsdCode
	 */
	public String getDsdCode() {
		return dsdCode;
	}

	/**
	 * @param dsdCode the dsdCode to set
	 */
	public void setDsdCode(String dsdCode) {
		this.dsdCode = dsdCode;
	}

	/**
	 * @return the elementLabel
	 */
	public String getElementLabel() {
		return elementLabel;
	}

	/**
	 * @param elementLabel the elementLabel to set
	 */
	public void setElementLabel(String elementLabel) {
		this.elementLabel = elementLabel;
	}

	/**
	 * @return the elementVer
	 */
	public String getElementVer() {
		return elementVer;
	}

	/**
	 * @param elementVer the elementVer to set
	 */
	public void setElementVer(String elementVer) {
		this.elementVer = elementVer;
	}

	/**
	 * @return the classificationTypeId
	 */
	public Long getClassificationTypeId() {
		return classificationTypeId;
	}

	/**
	 * @param classificationTypeId the classificationTypeId to set
	 */
	public void setClassificationTypeId(Long classificationTypeId) {
		this.classificationTypeId = classificationTypeId;
	}

	/**
	 * @return the classificationName
	 */
	public String getClassificationName() {
		return classificationName;
	}

	/**
	 * @param classificationName the classificationName to set
	 */
	public void setClassificationName(String classificationName) {
		this.classificationName = classificationName;
	}

	/**
	 * @return the dependencyTypeId
	 */
	public Long getDependencyTypeId() {
		return dependencyTypeId;
	}

	/**
	 * @param dependencyTypeId the dependencyTypeId to set
	 */
	public void setDependencyTypeId(Long dependencyTypeId) {
		this.dependencyTypeId = dependencyTypeId;
	}

	/**
	 * @return the dependencyTypeName
	 */
	public String getDependencyTypeName() {
		return dependencyTypeName;
	}

	/**
	 * @param dependencyTypeName the dependencyTypeName to set
	 */
	public void setDependencyTypeName(String dependencyTypeName) {
		this.dependencyTypeName = dependencyTypeName;
	}

	/**
	 * @return the flowTypeId
	 */
	public Long getFlowTypeId() {
		return flowTypeId;
	}

	/**
	 * @param flowTypeId the flowTypeId to set
	 */
	public void setFlowTypeId(Long flowTypeId) {
		this.flowTypeId = flowTypeId;
	}

	/**
	 * @return the flowTypeName
	 */
	public String getFlowTypeName() {
		return flowTypeName;
	}

	/**
	 * @param flowTypeName the flowTypeName to set
	 */
	public void setFlowTypeName(String flowTypeName) {
		this.flowTypeName = flowTypeName;
	}

	/**
	 * @return the natureTypeId
	 */
	public Long getNatureTypeId() {
		return natureTypeId;
	}

	/**
	 * @param natureTypeId the natureTypeId to set
	 */
	public void setNatureTypeId(Long natureTypeId) {
		this.natureTypeId = natureTypeId;
	}

	/**
	 * @return the natureTypeName
	 */
	public String getNatureTypeName() {
		return natureTypeName;
	}

	/**
	 * @param natureTypeName the natureTypeName to set
	 */
	public void setNatureTypeName(String natureTypeName) {
		this.natureTypeName = natureTypeName;
	}

	/**
	 * @return the sectorTypeId
	 */
	public Long getSectorTypeId() {
		return sectorTypeId;
	}

	/**
	 * @param sectorTypeId the sectorTypeId to set
	 */
	public void setSectorTypeId(Long sectorTypeId) {
		this.sectorTypeId = sectorTypeId;
	}

	/**
	 * @return the sectorTypeName
	 */
	public String getSectorTypeName() {
		return sectorTypeName;
	}

	/**
	 * @param sectorTypeName the sectorTypeName to set
	 */
	public void setSectorTypeName(String sectorTypeName) {
		this.sectorTypeName = sectorTypeName;
	}

	/**
	 * @return the elementDesc
	 */
	public String getElementDesc() {
		return elementDesc;
	}

	/**
	 * @param elementDesc the elementDesc to set
	 */
	public void setElementDesc(String elementDesc) {
		this.elementDesc = elementDesc;
	}

	/**
	 * @return the elementParentId
	 */
	public Long getElementParentId() {
		return elementParentId;
	}

	/**
	 * @param elementParentId the elementParentId to set
	 */
	public void setElementParentId(Long elementParentId) {
		this.elementParentId = elementParentId;
	}

	/**
	 * @return the parentElementVer
	 */
	public String getParentElementVer() {
		return parentElementVer;
	}

	/**
	 * @param parentElementVer the parentElementVer to set
	 */
	public void setParentElementVer(String parentElementVer) {
		this.parentElementVer = parentElementVer;
	}

	/**
	 * @return the parentElementDsdCode
	 */
	public String getParentElementDsdCode() {
		return parentElementDsdCode;
	}

	/**
	 * @param parentElementDsdCode the parentElementDsdCode to set
	 */
	public void setParentElementDsdCode(String parentElementDsdCode) {
		this.parentElementDsdCode = parentElementDsdCode;
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
	 * @return the createdBy
	 */
	public Long getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdByUserName
	 */
	public String getCreatedByUserName() {
		return createdByUserName;
	}

	/**
	 * @param createdByUserName the createdByUserName to set
	 */
	public void setCreatedByUserName(String createdByUserName) {
		this.createdByUserName = createdByUserName;
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
	 * @return the modifyBy
	 */
	public Long getModifyBy() {
		return modifyBy;
	}

	/**
	 * @param modifyBy the modifyBy to set
	 */
	public void setModifyBy(Long modifyBy) {
		this.modifyBy = modifyBy;
	}

	/**
	 * @return the modifyOn
	 */
	public Date getModifyOn() {
		return modifyOn;
	}

	/**
	 * @param modifyOn the modifyOn to set
	 */
	public void setModifyOn(Date modifyOn) {
		this.modifyOn = modifyOn;
	}

	/**
	 * @return the modifyByUserName
	 */
	public String getModifyByUserName() {
		return modifyByUserName;
	}

	/**
	 * @param modifyByUserName the modifyByUserName to set
	 */
	public void setModifyByUserName(String modifyByUserName) {
		this.modifyByUserName = modifyByUserName;
	}

	/**
	 * @return the ownerDepartmentId
	 */
	public Long getOwnerDepartmentId() {
		return ownerDepartmentId;
	}

	/**
	 * @param ownerDepartmentId the ownerDepartmentId to set
	 */
	public void setOwnerDepartmentId(Long ownerDepartmentId) {
		this.ownerDepartmentId = ownerDepartmentId;
	}

	/**
	 * @return the ownerDepartmentCode
	 */
	public String getOwnerDepartmentCode() {
		return ownerDepartmentCode;
	}

	/**
	 * @param ownerDepartmentCode the ownerDepartmentCode to set
	 */
	public void setOwnerDepartmentCode(String ownerDepartmentCode) {
		this.ownerDepartmentCode = ownerDepartmentCode;
	}

	/**
	 * @return the ownerDepartmentName
	 */
	public String getOwnerDepartmentName() {
		return ownerDepartmentName;
	}

	/**
	 * @param ownerDepartmentName the ownerDepartmentName to set
	 */
	public void setOwnerDepartmentName(String ownerDepartmentName) {
		this.ownerDepartmentName = ownerDepartmentName;
	}

	public ElementListBean() {
	}

	@Override
	public int compareTo(ElementListBean obj) {
		// TODO Auto-generated method stub
		return this.dsdCode.compareTo(obj.dsdCode);
	}

}
