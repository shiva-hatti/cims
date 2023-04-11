package com.iris.dto;

import java.io.Serializable;
import java.util.List;

import com.iris.model.EntityBean;
import com.iris.model.UserMaster;
import com.iris.util.Validations;

public class GroupMasterTempDto implements Serializable {

	private static final long serialVersionUID = 5824617149438092656L;

	private Long groupId;
	private String groupCode;
	private String groupName;
	private String alternateName;
	private String remark;
	private Long mobileNumber;
	private Integer verificationStatus;
	private Long createdOn;
	private Integer entryType;
	private Integer createdBy;
	private String entityCode;
	private Long modifiedOn;
	private String createdByUser;
	private String modifiedByUser;
	private String companyNames;
	private List<CompanyDto> companyDtoList;
	private Long rowNumber;
	private Integer sheetNumber;
	private Boolean isBulkUpload;
	private EntityBean entityBean;
	private UserMaster userMaster;
	private String comment;
	private Integer approvedByFk;
	private Long approvedOn;
	private String[] groupList;
	private String approvedByUser;

	public Integer getSheetNumber() {
		return sheetNumber;
	}

	public void setSheetNumber(Integer sheetNumber) {
		this.sheetNumber = sheetNumber;
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

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = Validations.trimInput(groupCode);
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = Validations.trimInput(groupName);
	}

	public String getAlternateName() {
		return alternateName;
	}

	public void setAlternateName(String alternateName) {
		this.alternateName = Validations.trimInput(alternateName);
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = Validations.trimInput(remark);
	}

	public Long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Integer getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(Integer verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public Long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getEntryType() {
		return entryType;
	}

	public void setEntryType(Integer entryType) {
		this.entryType = entryType;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = Validations.trimInput(entityCode);
	}

	public Long getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Long modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getCreatedByUser() {
		return createdByUser;
	}

	public void setCreatedByUser(String createdByUser) {
		this.createdByUser = Validations.trimInput(createdByUser);
	}

	public String getModifiedByUser() {
		return modifiedByUser;
	}

	public void setModifiedByUser(String modifiedByUser) {
		this.modifiedByUser = Validations.trimInput(modifiedByUser);
	}

	public String getCompanyNames() {
		return companyNames;
	}

	public void setCompanyNames(String companyNames) {
		this.companyNames = Validations.trimInput(companyNames);
	}

	public List<CompanyDto> getCompanyDtoList() {
		return companyDtoList;
	}

	public void setCompanyDtoList(List<CompanyDto> companyDtoList) {
		this.companyDtoList = companyDtoList;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public String[] getGroupList() {
		return groupList;
	}

	public void setGroupList(String[] groupList) {
		this.groupList = groupList;
	}

	public String getApprovedByUser() {
		return approvedByUser;
	}

	public void setApprovedByUser(String approvedByUser) {
		this.approvedByUser = approvedByUser;
	}

}
