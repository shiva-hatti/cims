package com.iris.dto;

import java.io.Serializable;

import com.iris.model.EntityBean;
import com.iris.model.UserMaster;

/**
 * @author Lakhan Kumar
 * @date 02/04/2020
 *
 */
public class PanMasterTempDto implements Serializable {

	private static final long serialVersionUID = -5425726721089298149L;

	private String panNumber;

	private String borrowerName;

	private String borrowerAlternateName;

	private String borrowerTitle;

	private Long borrowerMobile;

	private Integer verificationStatus;

	private Integer createdBy;

	private Integer entryType;

	private Boolean rbiGenerated;

	private String entityCode;

	private Long createdOn;

	private Long modifiedOn;

	private Integer institutionType;

	private String status;

	private String createdUser;

	private String modifiedUser;

	private String comment;

	private Boolean isBulkUpload;

	private Long rowNumber;

	private EntityBean entityBean;

	private UserMaster userMaster;

	private Long sheetNumber;

	private Long panId;

	private Integer approvedByFk;

	private Long approvedOn;

	private String[] panList;

	private String approvedByUser;
	private String supportDocFilePath;
	private String institutionTypeName;
	private Integer supportingDocTypeId;
	private String supportDocName;
	private Long lastModifiedByUserId;

	public Integer getSupportingDocTypeId() {
		return supportingDocTypeId;
	}

	public void setSupportingDocTypeId(Integer supportingDocTypeId) {
		this.supportingDocTypeId = supportingDocTypeId;
	}

	public String getSupportingDocIdentityNumber() {
		return supportingDocIdentityNumber;
	}

	public void setSupportingDocIdentityNumber(String supportingDocIdentityNumber) {
		this.supportingDocIdentityNumber = supportingDocIdentityNumber;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getSupportDocFileType() {
		return supportDocFileType;
	}

	public void setSupportDocFileType(String supportDocFileType) {
		this.supportDocFileType = supportDocFileType;
	}

	private String supportingDocIdentityNumber;
	private String dateOfBirth;
	private String supportDocFileType;

	public String getInstitutionTypeName() {
		return institutionTypeName;
	}

	public void setInstitutionTypeName(String institutionTypeName) {
		this.institutionTypeName = institutionTypeName;
	}

	public Long getPanId() {
		return panId;
	}

	public void setPanId(Long panId) {
		this.panId = panId;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getBorrowerName() {
		return borrowerName;
	}

	public void setBorrowerName(String borrowerName) {
		this.borrowerName = borrowerName;
	}

	public String getBorrowerAlternateName() {
		return borrowerAlternateName;
	}

	public void setBorrowerAlternateName(String borrowerAlternateName) {
		this.borrowerAlternateName = borrowerAlternateName;
	}

	public String getBorrowerTitle() {
		return borrowerTitle;
	}

	public void setBorrowerTitle(String borrowerTitle) {
		this.borrowerTitle = borrowerTitle;
	}

	public Long getBorrowerMobile() {
		return borrowerMobile;
	}

	public void setBorrowerMobile(Long borrowerMobile) {
		this.borrowerMobile = borrowerMobile;
	}

	public Integer getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(Integer verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getEntryType() {
		return entryType;
	}

	public void setEntryType(Integer entryType) {
		this.entryType = entryType;
	}

	public Boolean getRbiGenerated() {
		return rbiGenerated;
	}

	public void setRbiGenerated(Boolean rbiGenerated) {
		this.rbiGenerated = rbiGenerated;
	}

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	public Long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}

	public Long getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Long modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public Integer getInstitutionType() {
		return institutionType;
	}

	public void setInstitutionType(Integer institutionType) {
		this.institutionType = institutionType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public String getModifiedUser() {
		return modifiedUser;
	}

	public void setModifiedUser(String modifiedUser) {
		this.modifiedUser = modifiedUser;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Boolean getIsBulkUpload() {
		return isBulkUpload;
	}

	public void setIsBulkUpload(Boolean isBulkUpload) {
		this.isBulkUpload = isBulkUpload;
	}

	public Long getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(Long rowNumber) {
		this.rowNumber = rowNumber;
	}

	public EntityBean getEntityBean() {
		return entityBean;
	}

	public void setEntityBean(EntityBean entityBean) {
		this.entityBean = entityBean;
	}

	public UserMaster getUserMaster() {
		return userMaster;
	}

	public void setUserMaster(UserMaster userMaster) {
		this.userMaster = userMaster;
	}

	public Long getSheetNumber() {
		return sheetNumber;
	}

	public void setSheetNumber(Long sheetNumber) {
		this.sheetNumber = sheetNumber;
	}

	public Integer getApprovedByFk() {
		return approvedByFk;
	}

	public void setApprovedByFk(Integer approvedByFk) {
		this.approvedByFk = approvedByFk;
	}

	public Long getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(Long approvedOn) {
		this.approvedOn = approvedOn;
	}

	public String[] getPanList() {
		return panList;
	}

	public void setPanList(String[] panList) {
		this.panList = panList;
	}

	public String getApprovedByUser() {
		return approvedByUser;
	}

	public void setApprovedByUser(String approvedByUser) {
		this.approvedByUser = approvedByUser;
	}

	public String getSupportDocFilePath() {
		return supportDocFilePath;
	}

	public void setSupportDocFilePath(String supportDocFilePath) {
		this.supportDocFilePath = supportDocFilePath;
	}

	/**
	 * @return the supportDocName
	 */
	public String getSupportDocName() {
		return supportDocName;
	}

	/**
	 * @param supportDocName the supportDocName to set
	 */
	public void setSupportDocName(String supportDocName) {
		this.supportDocName = supportDocName;
	}

	/**
	 * @return the lastModifiedByUserId
	 */
	public Long getLastModifiedByUserId() {
		return lastModifiedByUserId;
	}

	/**
	 * @param lastModifiedByUserId the lastModifiedByUserId to set
	 */
	public void setLastModifiedByUserId(Long lastModifiedByUserId) {
		this.lastModifiedByUserId = lastModifiedByUserId;
	}
}
