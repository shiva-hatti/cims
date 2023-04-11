package com.iris.dto;

import java.util.Date;

import com.iris.util.Validations;

public class UserInfoDTO {
	private Long userId;
	private String userName;
	private String firstName;
	private String lastName;
	private String primaryEmail;
	private String primaryPhoneNo;
	private Long userRoleId;
	private String userRoleName;
	private Long roleTypeId;
	private Long entityId;
	private String entityCode;
	private Boolean isActive;
	private String lastModBy;
	private Boolean isLoggedIn;
	private String profilePicture;
	private String password;
	private Long createdBy;
	private Long modifiedBy;
	private Long approvedBy;
	private Boolean isPartial;
	private Boolean isPasswordReset;
	private String pwdChangeOn;
	private String lastLogedOn;
	private int noOfFailedAttempts = 0;
	private String salt;
	private String userJSessionId;
	private Boolean isOtpEnabled;
	private Boolean isPkiEnabled;
	private Long departmentId;
	private Date lastModOn;
	private String userRoleIds;
	private Boolean addEditFlag;
	private Boolean autoApprovedFlag;
	private String langCode;
	private Long loggedInUserRoleId;
	private Long loggedInUserId;
	private String roleDesc;
	private Long loggedInUserDeptId;

	public UserInfoDTO() {
	}

	public UserInfoDTO(Long userId, String userName, String firstName, String lastName, String primaryEmail, String primaryPhoneNo, Boolean isActive, Long roleTypeId, String roleDesc, Date lastModOn) {
		this.userId = userId;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.primaryEmail = primaryEmail;
		this.primaryPhoneNo = primaryPhoneNo;
		this.isActive = isActive;
		this.roleTypeId = roleTypeId;
		this.roleDesc = roleDesc;
		this.lastModOn = lastModOn;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = Validations.trimInput(userName);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = Validations.trimInput(firstName);
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = Validations.trimInput(lastName);
	}

	public String getPrimaryEmail() {
		return primaryEmail;
	}

	public void setPrimaryEmail(String primaryEmail) {
		this.primaryEmail = Validations.trimInput(primaryEmail);
	}

	public String getPrimaryPhoneNo() {
		return primaryPhoneNo;
	}

	public void setPrimaryPhoneNo(String primaryPhoneNo) {
		this.primaryPhoneNo = Validations.trimInput(primaryPhoneNo);
	}

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getUserRoleName() {
		return userRoleName;
	}

	public void setUserRoleName(String userRoleName) {
		this.userRoleName = Validations.trimInput(userRoleName);
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getLastModBy() {
		return lastModBy;
	}

	public void setLastModBy(String lastModBy) {
		this.lastModBy = Validations.trimInput(lastModBy);
	}

	public Date getLastModOn() {
		return lastModOn;
	}

	public void setLastModOn(Date lastModOn) {
		this.lastModOn = lastModOn;
	}

	public Boolean getIsLoggedIn() {
		return isLoggedIn;
	}

	public void setIsLoggedIn(Boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = Validations.trimInput(profilePicture);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = Validations.trimInput(password);
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Long getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Long approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Boolean getIsPartial() {
		return isPartial;
	}

	public void setIsPartial(Boolean isPartial) {
		this.isPartial = isPartial;
	}

	public Boolean getIsPasswordReset() {
		return isPasswordReset;
	}

	public void setIsPasswordReset(Boolean isPasswordReset) {
		this.isPasswordReset = isPasswordReset;
	}

	public String getPwdChangeOn() {
		return pwdChangeOn;
	}

	public void setPwdChangeOn(String pwdChangeOn) {
		this.pwdChangeOn = Validations.trimInput(pwdChangeOn);
	}

	public String getLastLogedOn() {
		return lastLogedOn;
	}

	public void setLastLogedOn(String lastLogedOn) {
		this.lastLogedOn = Validations.trimInput(lastLogedOn);
	}

	public int getNoOfFailedAttempts() {
		return noOfFailedAttempts;
	}

	public void setNoOfFailedAttempts(int noOfFailedAttempts) {
		this.noOfFailedAttempts = noOfFailedAttempts;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = Validations.trimInput(salt);
	}

	public String getUserJSessionId() {
		return userJSessionId;
	}

	public void setUserJSessionId(String userJSessionId) {
		this.userJSessionId = Validations.trimInput(userJSessionId);
	}

	public Boolean getIsOtpEnabled() {
		return isOtpEnabled;
	}

	public void setIsOtpEnabled(Boolean isOtpEnabled) {
		this.isOtpEnabled = isOtpEnabled;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getRoleTypeId() {
		return roleTypeId;
	}

	public void setRoleTypeId(Long roleTypeId) {
		this.roleTypeId = roleTypeId;
	}

	public String getUserRoleIds() {
		return userRoleIds;
	}

	public void setUserRoleIds(String userRoleIds) {
		this.userRoleIds = Validations.trimInput(userRoleIds);
	}

	public Boolean getAddEditFlag() {
		return addEditFlag;
	}

	public void setAddEditFlag(Boolean addEditFlag) {
		this.addEditFlag = addEditFlag;
	}

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = Validations.trimInput(entityCode);
	}

	public Boolean getIsPkiEnabled() {
		return isPkiEnabled;
	}

	public void setIsPkiEnabled(Boolean isPkiEnabled) {
		this.isPkiEnabled = isPkiEnabled;
	}

	public Long getLoggedInUserRoleId() {
		return loggedInUserRoleId;
	}

	public void setLoggedInUserRoleId(Long loggedInUserRoleId) {
		this.loggedInUserRoleId = loggedInUserRoleId;
	}

	public Long getLoggedInUserId() {
		return loggedInUserId;
	}

	public void setLoggedInUserId(Long loggedInUserId) {
		this.loggedInUserId = loggedInUserId;
	}

	public Boolean getAutoApprovedFlag() {
		return autoApprovedFlag;
	}

	public void setAutoApprovedFlag(Boolean autoApprovedFlag) {
		this.autoApprovedFlag = autoApprovedFlag;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = Validations.trimInput(langCode);
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = Validations.trimInput(roleDesc);
	}

	/**
	 * @return the loggedInUserDeptId
	 */
	public Long getLoggedInUserDeptId() {
		return loggedInUserDeptId;
	}

	/**
	 * @param loggedInUserDeptId the loggedInUserDeptId to set
	 */
	public void setLoggedInUserDeptId(Long loggedInUserDeptId) {
		this.loggedInUserDeptId = loggedInUserDeptId;
	}

}
