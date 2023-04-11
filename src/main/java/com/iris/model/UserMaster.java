package com.iris.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.iris.util.AESV2;
import com.iris.util.Validations;
import com.iris.util.constant.GeneralConstants;

/**
 * This bean represents user's information
 * 
 * @author Suman Kumar
 * @version 1.0
 */
@Entity
@Table(name = "TBL_USER_MASTER")
@JsonInclude(Include.NON_NULL)
public class UserMaster implements Serializable {

	private static final long serialVersionUID = -6305709526119023051L;
	private static Logger LOGGER = LogManager.getLogger(UserMaster.class);

	@Id
	@Column(name = "USER_ID")
	private Long userId;

	@Column(name = "USER_NAME")
	private String userName;

	@Column(name = "UNIQUE_IDENTIFIER")
	private String uniqueIdentifier;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "IS_LOGGED_IN")
	private Boolean isLoggedIn;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "PRIMARY_EMAIL")
	private String primaryEmail;

	@Column(name = "PRIMARY_PHONE_NO")
	private String primaryPhoneNo;

	@Temporal(TemporalType.DATE)
	@Column(name = "DOB")
	private Date dob;

	@Column(name = "PROFILE_PICTURE")
	private byte[] profilePic;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY_FK")
	private UserMaster createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFIED_BY_FK")
	private UserMaster modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPROVED_DATE")
	private Date approvedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APPROVED_BY_FK")
	private UserMaster approvedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_UPDATED_ON")
	private Date lastUpdatedOn;

	@Column(name = "is_partial")
	private Boolean isPartial;

	@Column(name = "SEND_EMAIL_NOTICE")
	private Boolean sendEmailNotice;

	@Column(name = "SEND_SMS_NOTICE")
	private Boolean sendSmsNotice;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "IS_PWD_RESET")
	private Boolean isPasswordReset;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PWD_CHANGE_ON")
	private Date pwdChangeOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_LOGEDON")
	private Date lastLogedOn;

	@Column(name = "NO_OF_FAIL_ATTEMPT")
	private int noOfFailedAttempts = 0;

	@Column(name = "SALT")
	private String salt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLE_TYPE_FK")
	private RoleType roleType;

	@Transient
	private Long roleIdKey;

	@Transient
	private boolean isComngFrmPwd = false;

	@Column(name = "POSITION_EN")
	private String positionEn;

	@Column(name = "POSITION_BIL")
	private String positionBil;

	@Column(name = "USER_JSESSION_ID")
	private String userJSessionId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEPARTMENT_ID_FK")
	private Regulator departmentIdFk;

	@Column(name = "ICAI_MEMBERSHIP_NUMBER")
	private String icaiMemberNumber;

	@Transient
	private String tempPassword;

	@Transient
	private String plainPwd;

	@Transient
	private String loginPageUrl;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userMaster")
	@OrderBy("userRoleMasterId")
	private Set<UserRoleMaster> usrRoleMstrSet;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "userIdFk")
	private Set<DeptUserEntityMapping> deptUserEntMapSet;

	@Transient
	private UserInfo userInfo;

	public UserMaster(Long userId, String userName) {
		super();
		this.userId = userId;
		this.userName = userName;
	}

	public UserMaster() {

	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	public Set<UserRoleMaster> getUsrRoleMstrSet() {
		return usrRoleMstrSet;
	}

	public void setUsrRoleMstrSet(Set<UserRoleMaster> usrRoleMstrSet) {
		this.usrRoleMstrSet = usrRoleMstrSet;
	}

	public String getUserName() {
		if (!Validations.isEmpty(userName)) {
			try {
				return AESV2.getInstance().decrypt(userName);
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
				return userName;
			}
		} else {
			return userName;
		}
	}

	public void setUserName(String userName) {
		if (!Validations.isEmpty(userName)) {
			try {
				this.userName = AESV2.getInstance().encrypt(Validations.trimInput(userName));
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
				this.userName = Validations.trimInput(userName);
			}
		} else {
			this.userName = Validations.trimInput(userName);
		}
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = Validations.trimInput(password);
	}

	/**
	 * @return the isLoggedIn
	 */
	public Boolean getIsLoggedIn() {
		return isLoggedIn;
	}

	/**
	 * @param isLoggedIn the isLoggedIn to set
	 */
	public void setIsLoggedIn(Boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public String getFirstName() {
		if (!Validations.isEmpty(firstName)) {
			try {
				return AESV2.getInstance().decrypt(firstName);
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
				return firstName;
			}
		} else {
			return firstName;
		}
	}

	public void setFirstName(String firstName) {
		if (!Validations.isEmpty(firstName)) {
			try {
				this.firstName = AESV2.getInstance().encrypt(Validations.trimInput(firstName));
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
				this.firstName = Validations.trimInput(firstName);
			}
		} else {
			this.firstName = Validations.trimInput(firstName);
		}
	}

	public String getLastName() {
		if (!Validations.isEmpty(lastName)) {
			try {
				return AESV2.getInstance().decrypt(lastName);
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
				return lastName;
			}
		} else {
			return lastName;
		}
	}

	public void setLastName(String lastName) {
		if (!Validations.isEmpty(lastName)) {
			try {
				this.lastName = AESV2.getInstance().encrypt(Validations.trimInput(lastName));
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
				this.lastName = Validations.trimInput(lastName);
			}
		} else {
			this.lastName = Validations.trimInput(lastName);
		}
	}

	public String getPrimaryEmail() {
		if (!Validations.isEmpty(primaryEmail)) {
			try {
				return AESV2.getInstance().decrypt(primaryEmail);
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
				return primaryEmail;
			}
		} else {
			return primaryEmail;
		}
	}

	public void setPrimaryEmail(String primaryEmail) {
		if (!Validations.isEmpty(primaryEmail)) {
			try {
				this.primaryEmail = AESV2.getInstance().encrypt(Validations.trimInput(primaryEmail));
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
				this.primaryEmail = Validations.trimInput(primaryEmail);
			}
		} else {
			this.primaryEmail = Validations.trimInput(primaryEmail);
		}
	}

	public String getPrimaryPhoneNo() {
		if (!Validations.isEmpty(primaryPhoneNo)) {
			try {
				return AESV2.getInstance().decrypt(primaryPhoneNo);
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
				return primaryPhoneNo;
			}
		} else {
			return primaryPhoneNo;
		}
	}

	public void setPrimaryPhoneNo(String primaryPhoneNo) {
		if (!Validations.isEmpty(primaryPhoneNo)) {
			try {
				this.primaryPhoneNo = AESV2.getInstance().encrypt(Validations.trimInput(primaryPhoneNo));
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
				this.primaryPhoneNo = Validations.trimInput(primaryPhoneNo);
			}
		} else {
			this.primaryPhoneNo = Validations.trimInput(primaryPhoneNo);
		}
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public byte[] getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(byte[] profilePic) {
		this.profilePic = profilePic;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public UserMaster getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserMaster createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public UserMaster getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(UserMaster modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}

	public UserMaster getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(UserMaster approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
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
	 * @return the isPartial
	 */
	public Boolean getIsPartial() {
		return isPartial;
	}

	/**
	 * @param isPartial the isPartial to set
	 */
	public void setIsPartial(Boolean isPartial) {
		this.isPartial = isPartial;
	}

	/**
	 * @return the uniqueIdentifier
	 */
	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	/**
	 * @param uniqueIdentifier the uniqueIdentifier to set
	 */
	public void setUniqueIdentifier(String uniqueIdentifier) {
		this.uniqueIdentifier = Validations.trimInput(uniqueIdentifier);
	}

	/**
	 * @return the sendEmailNotice
	 */
	public Boolean getSendEmailNotice() {
		return sendEmailNotice;
	}

	/**
	 * @param sendEmailNotice the sendEmailNotice to set
	 */
	public void setSendEmailNotice(Boolean sendEmailNotice) {
		this.sendEmailNotice = sendEmailNotice;
	}

	/**
	 * @return the sendSmsNotice
	 */
	public Boolean getSendSmsNotice() {
		return sendSmsNotice;
	}

	/**
	 * @param sendSmsNotice the sendSmsNotice to set
	 */
	public void setSendSmsNotice(Boolean sendSmsNotice) {
		this.sendSmsNotice = sendSmsNotice;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the isPasswordReset
	 */
	public Boolean getIsPasswordReset() {
		return isPasswordReset;
	}

	/**
	 * @param isPasswordReset the isPasswordReset to set
	 */
	public void setIsPasswordReset(Boolean isPasswordReset) {
		this.isPasswordReset = isPasswordReset;
	}

	/**
	 * @return the pwdChangeOn
	 */
	public Date getPwdChangeOn() {
		return pwdChangeOn;
	}

	/**
	 * @param pwdChangeOn the pwdChangeOn to set
	 */
	public void setPwdChangeOn(Date pwdChangeOn) {
		this.pwdChangeOn = pwdChangeOn;
	}

	/**
	 * @return the lastLogedOn
	 */
	public Date getLastLogedOn() {
		return lastLogedOn;
	}

	/**
	 * @param lastLogedOn the lastLogedOn to set
	 */
	public void setLastLogedOn(Date lastLogedOn) {
		this.lastLogedOn = lastLogedOn;
	}

	/**
	 * @return the noOfFailedAttempts
	 */
	public int getNoOfFailedAttempts() {
		return noOfFailedAttempts;
	}

	/**
	 * @param noOfFailedAttempts the noOfFailedAttempts to set
	 */
	public void setNoOfFailedAttempts(int noOfFailedAttempts) {
		this.noOfFailedAttempts = noOfFailedAttempts;
	}

	/**
	 * @return the isComngFrmPwd
	 */
	public boolean isComngFrmPwd() {
		return isComngFrmPwd;
	}

	/**
	 * @param isComngFrmPwd the isComngFrmPwd to set
	 */
	public void setComngFrmPwd(boolean isComngFrmPwd) {
		this.isComngFrmPwd = isComngFrmPwd;
	}

	/**
	 * @return the salt
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * @param salt the salt to set
	 */
	public void setSalt(String salt) {
		this.salt = Validations.trimInput(salt);
	}

	/**
	 * @return the tempPassword
	 */
	public String getTempPassword() {
		return tempPassword;
	}

	/**
	 * @param tempPassword the tempPassword to set
	 */
	public void setTempPassword(String tempPassword) {
		this.tempPassword = Validations.trimInput(tempPassword);
	}

	public String getPositionEn() {
		return positionEn;
	}

	public void setPositionEn(String positionEn) {
		this.positionEn = Validations.trimInput(positionEn);
	}

	public String getPositionBil() {
		return positionBil;
	}

	public void setPositionBil(String positionBil) {
		this.positionBil = Validations.trimInput(positionBil);
	}

	public String getUserJSessionId() {
		return userJSessionId;
	}

	public void setUserJSessionId(String userJSessionId) {
		this.userJSessionId = Validations.trimInput(userJSessionId);
	}

	/**
	 * @return the plainPwd
	 */
	public String getPlainPwd() {
		return plainPwd;
	}

	/**
	 * @param plainPwd the plainPwd to set
	 */
	public void setPlainPwd(String plainPwd) {
		this.plainPwd = Validations.trimInput(plainPwd);
	}

	/**
	 * @return the loginPageUrl
	 */
	public String getLoginPageUrl() {
		return loginPageUrl;
	}

	/**
	 * @param loginPageUrl the loginPageUrl to set
	 */
	public void setLoginPageUrl(String loginPageUrl) {
		this.loginPageUrl = Validations.trimInput(loginPageUrl);
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Regulator getDepartmentIdFk() {
		return departmentIdFk;
	}

	public void setDepartmentIdFk(Regulator departmentIdFk) {
		this.departmentIdFk = departmentIdFk;
	}

	/**
	 * @return the icaiMemberNumber
	 */
	public String getIcaiMemberNumber() {
		if (!Validations.isEmpty(icaiMemberNumber)) {
			try {
				return AESV2.getInstance().decrypt(icaiMemberNumber);
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
				return icaiMemberNumber;
			}
		} else {
			return icaiMemberNumber;
		}
	}

	/**
	 * @param icaiMemberNumber the icaiMemberNumber to set
	 */
	public void setIcaiMemberNumber(String icaiMemberNumber) {
		if (!Validations.isEmpty(icaiMemberNumber)) {
			try {
				this.icaiMemberNumber = AESV2.getInstance().encrypt(Validations.trimInput(icaiMemberNumber));
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
				this.icaiMemberNumber = Validations.trimInput(icaiMemberNumber);
			}
		} else {
			this.icaiMemberNumber = Validations.trimInput(icaiMemberNumber);
		}
	}

	public Set<DeptUserEntityMapping> getDeptUserEntMapSet() {
		return deptUserEntMapSet;
	}

	public void setDeptUserEntMapSet(Set<DeptUserEntityMapping> deptUserEntMapSet) {
		this.deptUserEntMapSet = deptUserEntMapSet;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

}