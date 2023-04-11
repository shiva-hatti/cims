package com.iris.sdmx.model.code.data;

import java.io.Serializable;
import java.util.Date;

public class SdmxDataModelCodeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4002288555413737289L;

	private String modelCode;
	private String sheetCode;
	private String sheetName;
	private String sectionCode;
	private String sectionName;
	private Integer returnCellRef;
	private String dependencyTypeName;
	private String departmentName;
	private String returnCode;
	private Long modelCodesId;
	private Long elementId;
	private String returnName;
	private Date lastUpdatedOn;
	private Long lastUpdateOnInLong;
	private String dsdCode;

	public SdmxDataModelCodeBean() {

	}

	public SdmxDataModelCodeBean(String modelCode, String returnCode, String dsdCode) {
		this.modelCode = modelCode;
		this.returnCode = returnCode;
		this.dsdCode = dsdCode;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getSheetCode() {
		return sheetCode;
	}

	public void setSheetCode(String sheetCode) {
		this.sheetCode = sheetCode;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public Integer getReturnCellRef() {
		return returnCellRef;
	}

	public void setReturnCellRef(Integer returnCellRef) {
		this.returnCellRef = returnCellRef;
	}

	public String getDependencyTypeName() {
		return dependencyTypeName;
	}

	public void setDependencyTypeName(String dependencyTypeName) {
		this.dependencyTypeName = dependencyTypeName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public Long getModelCodesId() {
		return modelCodesId;
	}

	public void setModelCodesId(Long modelCodesId) {
		this.modelCodesId = modelCodesId;
	}

	public Long getElementId() {
		return elementId;
	}

	public void setElementId(Long elementId) {
		this.elementId = elementId;
	}

	public String getReturnName() {
		return returnName;
	}

	public void setReturnName(String returnName) {
		this.returnName = returnName;
	}

	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public Long getLastUpdateOnInLong() {
		return lastUpdateOnInLong;
	}

	public void setLastUpdateOnInLong(Long lastUpdateOnInLong) {
		this.lastUpdateOnInLong = lastUpdateOnInLong;
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

}
