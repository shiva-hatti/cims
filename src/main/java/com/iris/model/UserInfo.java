package com.iris.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.iris.util.Validations;

/**
 * @author psawant
 * @version 1.0
 * @date 27/02/2020
 */
public class UserInfo implements Serializable {

	private static final long serialVersionUID = -8705981923732385891L;

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
	private String createdByUserName;
	private Long modifiedBy;
	private String modifiedByUserName;
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
	private String entityIds;
	private String nbfcEntityIds;
	private Boolean addEditFlag;
	private Boolean autoApprovedFlag;
	private String langCode;
	private Long loggedInUserRoleId;
	private Long loggedInUserId;
	private Date createdOn;
	private Date modifiedOn;
	private String roleDesc;
	private Boolean isEditUser;
	private String departmentName;
	private String entityName;
	private List<String> userRoleList;
	private String jSessionString;
	private Long securityQuesId;
	private String securityQuesAns;
	private Long emailAlertId;
	private Long nbfcEntityBeanIdFk;
	private Long auditFirmId;
	private String iCAIMembershipNumber;
	private String auditFirmRegNo;
	private String auditFirmName;
	private Boolean auditorUserExists;
	private Boolean icaiMemNumExists;
	private Boolean isAccess;

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

	public Boolean getIsEditUser() {
		return isEditUser;
	}

	public void setIsEditUser(Boolean isEditUser) {
		this.isEditUser = isEditUser;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = Validations.trimInput(departmentName);
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = Validations.trimInput(entityName);
	}

	public List<String> getUserRoleList() {
		return userRoleList;
	}

	public void setUserRoleList(List<String> userRoleList) {
		this.userRoleList = userRoleList;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getjSessionString() {
		return jSessionString;
	}

	public void setjSessionString(String jSessionString) {
		this.jSessionString = Validations.trimInput(jSessionString);
	}

	public Long getSecurityQuesId() {
		return securityQuesId;
	}

	public void setSecurityQuesId(Long securityQuesId) {
		this.securityQuesId = securityQuesId;
	}

	public String getSecurityQuesAns() {
		return securityQuesAns;
	}

	public void setSecurityQuesAns(String securityQuesAns) {
		this.securityQuesAns = Validations.trimInput(securityQuesAns);
	}

	public Long getNbfcEntityBeanIdFk() {
		return nbfcEntityBeanIdFk;
	}

	public void setNbfcEntityBeanIdFk(Long nbfcEntityBeanIdFk) {
		this.nbfcEntityBeanIdFk = nbfcEntityBeanIdFk;
	}

	public Long getEmailAlertId() {
		return emailAlertId;
	}

	public void setEmailAlertId(Long emailAlertId) {
		this.emailAlertId = emailAlertId;
	}

	public Long getAuditFirmId() {
		return auditFirmId;
	}

	public void setAuditFirmId(Long auditFirmId) {
		this.auditFirmId = auditFirmId;
	}

	public String getiCAIMembershipNumber() {
		return iCAIMembershipNumber;
	}

	public void setiCAIMembershipNumber(String iCAIMembershipNumber) {
		this.iCAIMembershipNumber = Validations.trimInput(iCAIMembershipNumber);
	}

	public String getEntityIds() {
		return entityIds;
	}

	public void setEntityIds(String entityIds) {
		this.entityIds = Validations.trimInput(entityIds);
	}

	public String getNbfcEntityIds() {
		return nbfcEntityIds;
	}

	public void setNbfcEntityIds(String nbfcEntityIds) {
		this.nbfcEntityIds = Validations.trimInput(nbfcEntityIds);
	}

	public String getAuditFirmRegNo() {
		return auditFirmRegNo;
	}

	public void setAuditFirmRegNo(String auditFirmRegNo) {
		this.auditFirmRegNo = Validations.trimInput(auditFirmRegNo);
	}

	public String getAuditFirmName() {
		return auditFirmName;
	}

	public void setAuditFirmName(String auditFirmName) {
		this.auditFirmName = Validations.trimInput(auditFirmName);
	}

	public String getCreatedByUserName() {
		return createdByUserName;
	}

	public void setCreatedByUserName(String createdByUserName) {
		this.createdByUserName = Validations.trimInput(createdByUserName);
	}

	public String getModifiedByUserName() {
		return modifiedByUserName;
	}

	public void setModifiedByUserName(String modifiedByUserName) {
		this.modifiedByUserName = Validations.trimInput(modifiedByUserName);
	}

	public Boolean getAuditorUserExists() {
		return auditorUserExists;
	}

	public void setAuditorUserExists(Boolean auditorUserExists) {
		this.auditorUserExists = auditorUserExists;
	}

	public Boolean getIcaiMemNumExists() {
		return icaiMemNumExists;
	}

	public void setIcaiMemNumExists(Boolean icaiMemNumExists) {
		this.icaiMemNumExists = icaiMemNumExists;
	}

	/**
	 * @return the isAccess
	 */
	public Boolean getIsAccess() {
		return isAccess;
	}

	/**
	 * @param isAccess the isAccess to set
	 */
	public void setIsAccess(Boolean isAccess) {
		this.isAccess = isAccess;
	}

}