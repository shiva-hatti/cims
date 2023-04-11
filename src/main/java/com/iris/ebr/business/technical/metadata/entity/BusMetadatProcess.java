package com.iris.ebr.business.technical.metadata.entity;

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

import com.iris.model.UserMaster;

@Table(name = "TBL_SDMX_BUS_METADATA_PROCESS")
@Entity
public class BusMetadatProcess implements Serializable {

	/**
	 * @author sdhone
	 */
	private static final long serialVersionUID = 6451899270789252217L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SDMX_BUS_METADATA_PROCESS_ID")
	private Long busMetadataProcessId;

	@Column(name = "RETURN_CODE")
	private String returnCode;

	@Column(name = "RETURN_VERSION")
	private String returnVersion;

	@Column(name = "EBR_VERSION")
	private String ebrVersion;

	@Column(name = "ITEM_MASTER_UPLOADED_ON")
	private Date itemMasterUploadedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ITEM_MASTER_CREATED_BY_FK")
	private UserMaster itemMasterCreatedBy;

	@Column(name = "ITEM_MASTER_FILE_NAME")
	private String itemMasterFileName;

	@Column(name = "BUSS_CREATED_ON")
	private Date bussCreatedOn;

	@Column(name = "BUSS_FILE_NAME")
	private String bussFileName;

	@Column(name = "BUSS_METADATA_INSERT_START")
	private Date bussMetadataInsertStart;

	@Column(name = "BUSS_METADATA_INSERT_END")
	private Date bussMetadataInsertEnd;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "INSERT_STATUS")
	private Boolean insertStatus;

	@Column(name = "BUSS_VALIDATE_FILE_NAME")
	private String bussValidateFileName;

	@Column(name = "RETURN_NAME")
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
