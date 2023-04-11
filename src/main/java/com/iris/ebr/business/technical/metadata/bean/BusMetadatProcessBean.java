package com.iris.ebr.business.technical.metadata.bean;

import java.io.Serializable;
import java.util.Date;

import com.iris.model.UserMaster;

public class BusMetadatProcessBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7253122350314421983L;

	/**
	 * @author sdhone
	 */

	private Long busMetadataProcessId;

	private String returnCode;

	private String returnVersion;

	private String ebrVersion;

	private Date itemMasterUploadedOn;

	private UserMaster itemMasterCreatedBy;

	private String itemMasterFileName;

	private Date bussCreatedOn;

	private UserMaster busCreatedBy;

	private String bussFileName;

	private Date bussMetadataInsertStart;

	private Date bussMetadataInsertEnd;

	private Boolean isActive;

	private Boolean insertStatus;

	private String bussValidateFileName;

	private String processExecutedBy;

	private boolean valideError;

	private String insertStartTimeStr;

	private String insertEndTimeStr;

	private String returnName;

	/**
	 * @return the busMetadataProcessId
	 */
	public Long getBusMetadataProcessId() {
		return busMetadataProcessId;
	}

	/**
	 * @param busMetadataProcessId the busMetadataProcessId to set
	 */
	public void setBusMetadataProcessId(Long busMetadataProcessId) {
		this.busMetadataProcessId = busMetadataProcessId;
	}

	/**
	 * @return the returnCode
	 */
	public String getReturnCode() {
		return returnCode;
	}

	/**
	 * @param returnCode the returnCode to set
	 */
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	/**
	 * @return the returnVersion
	 */
	public String getReturnVersion() {
		return returnVersion;
	}

	/**
	 * @param returnVersion the returnVersion to set
	 */
	public void setReturnVersion(String returnVersion) {
		this.returnVersion = returnVersion;
	}

	/**
	 * @return the itemMasterUploadedOn
	 */
	public Date getItemMasterUploadedOn() {
		return itemMasterUploadedOn;
	}

	/**
	 * @param itemMasterUploadedOn the itemMasterUploadedOn to set
	 */
	public void setItemMasterUploadedOn(Date itemMasterUploadedOn) {
		this.itemMasterUploadedOn = itemMasterUploadedOn;
	}

	/**
	 * @return the itemMasterFileName
	 */
	public String getItemMasterFileName() {
		return itemMasterFileName;
	}

	/**
	 * @param itemMasterFileName the itemMasterFileName to set
	 */
	public void setItemMasterFileName(String itemMasterFileName) {
		this.itemMasterFileName = itemMasterFileName;
	}

	/**
	 * @return the bussCreatedOn
	 */
	public Date getBussCreatedOn() {
		return bussCreatedOn;
	}

	/**
	 * @param bussCreatedOn the bussCreatedOn to set
	 */
	public void setBussCreatedOn(Date bussCreatedOn) {
		this.bussCreatedOn = bussCreatedOn;
	}

	/**
	 * @return the bussFileName
	 */
	public String getBussFileName() {
		return bussFileName;
	}

	/**
	 * @param bussFileName the bussFileName to set
	 */
	public void setBussFileName(String bussFileName) {
		this.bussFileName = bussFileName;
	}

	/**
	 * @return the bussMetadataInsertStart
	 */
	public Date getBussMetadataInsertStart() {
		return bussMetadataInsertStart;
	}

	/**
	 * @param bussMetadataInsertStart the bussMetadataInsertStart to set
	 */
	public void setBussMetadataInsertStart(Date bussMetadataInsertStart) {
		this.bussMetadataInsertStart = bussMetadataInsertStart;
	}

	/**
	 * @return the bussMetadataInsertEnd
	 */
	public Date getBussMetadataInsertEnd() {
		return bussMetadataInsertEnd;
	}

	/**
	 * @param bussMetadataInsertEnd the bussMetadataInsertEnd to set
	 */
	public void setBussMetadataInsertEnd(Date bussMetadataInsertEnd) {
		this.bussMetadataInsertEnd = bussMetadataInsertEnd;
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
	 * @return the insertStatus
	 */
	public Boolean getInsertStatus() {
		return insertStatus;
	}

	/**
	 * @param insertStatus the insertStatus to set
	 */
	public void setInsertStatus(Boolean insertStatus) {
		this.insertStatus = insertStatus;
	}

	/**
	 * @return the bussValidateFileName
	 */
	public String getBussValidateFileName() {
		return bussValidateFileName;
	}

	/**
	 * @param bussValidateFileName the bussValidateFileName to set
	 */
	public void setBussValidateFileName(String bussValidateFileName) {
		this.bussValidateFileName = bussValidateFileName;
	}

	/**
	 * @return the ebrVersion
	 */
	public String getEbrVersion() {
		return ebrVersion;
	}

	/**
	 * @param ebrVersion the ebrVersion to set
	 */
	public void setEbrVersion(String ebrVersion) {
		this.ebrVersion = ebrVersion;
	}

	/**
	 * @return the itemMasterCreatedBy
	 */
	public UserMaster getItemMasterCreatedBy() {
		return itemMasterCreatedBy;
	}

	/**
	 * @param itemMasterCreatedBy the itemMasterCreatedBy to set
	 */
	public void setItemMasterCreatedBy(UserMaster itemMasterCreatedBy) {
		this.itemMasterCreatedBy = itemMasterCreatedBy;
	}

	/**
	 * @return the busCreatedBy
	 */
	public UserMaster getBusCreatedBy() {
		return busCreatedBy;
	}

	/**
	 * @param busCreatedBy the busCreatedBy to set
	 */
	public void setBusCreatedBy(UserMaster busCreatedBy) {
		this.busCreatedBy = busCreatedBy;
	}

	/**
	 * @return the processExecutedBy
	 */
	public String getProcessExecutedBy() {
		return processExecutedBy;
	}

	/**
	 * @param processExecutedBy the processExecutedBy to set
	 */
	public void setProcessExecutedBy(String processExecutedBy) {
		this.processExecutedBy = processExecutedBy;
	}

	/**
	 * @return the valideError
	 */
	public boolean isValideError() {
		return valideError;
	}

	/**
	 * @param valideError the valideError to set
	 */
	public void setValideError(boolean valideError) {
		this.valideError = valideError;
	}

	/**
	 * @return the insertStartTimeStr
	 */
	public String getInsertStartTimeStr() {
		return insertStartTimeStr;
	}

	/**
	 * @param insertStartTimeStr the insertStartTimeStr to set
	 */
	public void setInsertStartTimeStr(String insertStartTimeStr) {
		this.insertStartTimeStr = insertStartTimeStr;
	}

	/**
	 * @return the insertEndTimeStr
	 */
	public String getInsertEndTimeStr() {
		return insertEndTimeStr;
	}

	/**
	 * @param insertEndTimeStr the insertEndTimeStr to set
	 */
	public void setInsertEndTimeStr(String insertEndTimeStr) {
		this.insertEndTimeStr = insertEndTimeStr;
	}

	/**
	 * @return the returnName
	 */
	public String getReturnName() {
		return returnName;
	}

	/**
	 * @param returnName the returnName to set
	 */
	public void setReturnName(String returnName) {
		this.returnName = returnName;
	}

}
