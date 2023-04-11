package com.iris.dto;

import java.io.Serializable;

public class UserModDetailsJsonBean implements Serializable {

	private static final long serialVersionUID = 8234286936384310373L;

	private String firstName;

	private String lastName;

	private String primaryEmail;

	private String primaryPhoneNo;

	private Boolean isActive;

	private Long roleType;

	private Long auditorFormId;

	private Long entityId;

	private String icaiMembershipNumber;

	private Boolean isOtpEnabled;

	private Boolean isPkiEnabled;

	private Long departmentIdFk;

	private String selectedRole;

	private String selectedBank;

	private String selectedUcbOrNbfc;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPrimaryEmail() {
		return primaryEmail;
	}

	public void setPrimaryEmail(String primaryEmail) {
		this.primaryEmail = primaryEmail;
	}

	public String getPrimaryPhoneNo() {
		return primaryPhoneNo;
	}

	public void setPrimaryPhoneNo(String primaryPhoneNo) {
		this.primaryPhoneNo = primaryPhoneNo;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Long getRoleType() {
		return roleType;
	}

	public void setRoleType(Long roleType) {
		this.roleType = roleType;
	}

	public Long getAuditorFormId() {
		return auditorFormId;
	}

	public void setAuditorFormId(Long auditorFormId) {
		this.auditorFormId = auditorFormId;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getIcaiMembershipNumber() {
		return icaiMembershipNumber;
	}

	public void setIcaiMembershipNumber(String icaiMembershipNumber) {
		this.icaiMembershipNumber = icaiMembershipNumber;
	}

	public Boolean getIsOtpEnabled() {
		return isOtpEnabled;
	}

	public void setIsOtpEnabled(Boolean isOtpEnabled) {
		this.isOtpEnabled = isOtpEnabled;
	}

	public Boolean getIsPkiEnabled() {
		return isPkiEnabled;
	}

	public void setIsPkiEnabled(Boolean isPkiEnabled) {
		this.isPkiEnabled = isPkiEnabled;
	}

	public Long getDepartmentIdFk() {
		return departmentIdFk;
	}

	public void setDepartmentIdFk(Long departmentIdFk) {
		this.departmentIdFk = departmentIdFk;
	}

	public String getSelectedRole() {
		return selectedRole;
	}

	public void setSelectedRole(String selectedRole) {
		this.selectedRole = selectedRole;
	}

	public String getSelectedBank() {
		return selectedBank;
	}

	public void setSelectedBank(String selectedBank) {
		this.selectedBank = selectedBank;
	}

	public String getSelectedUcbOrNbfc() {
		return selectedUcbOrNbfc;
	}

	public void setSelectedUcbOrNbfc(String selectedUcbOrNbfc) {
		this.selectedUcbOrNbfc = selectedUcbOrNbfc;
	}

}
