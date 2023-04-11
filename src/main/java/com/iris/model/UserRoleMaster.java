package com.iris.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.iris.util.Validations;

/**
 * This class represents roles which are assigned to user
 * 
 */
@Entity
@Table(name = "TBL_USER_ROLE_MASTER")
public class UserRoleMaster implements Serializable {

	private static final long serialVersionUID = -8368190205176622922L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ROLE_MASTER_ID")
	private long userRoleMasterId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_MASTER_ID_FK")
	private UserMaster userMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ROLE_ID_FK")
	private UserRole userRole;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "EMAIL_SENT_HIS_ID")
	private String emailSentHisIdList;

	@OneToMany(mappedBy = "userRoleMstrIdFk", fetch = FetchType.LAZY)
	private Set<EmailUnsubscribe> emailUnsubscribeSet;

	@OneToMany(mappedBy = "userRoleMaster", fetch = FetchType.LAZY)
	@OrderBy("userEntityRoleId")
	private Set<UserEntityRole> userEntityRole;

	@OneToMany(mappedBy = "userRoleMaster", fetch = FetchType.LAZY)
	@OrderBy("userAuditorRoleId")
	private Set<UserAuditorRole> userAuditorRole;

	/**
	 * @return the userAuditorRole
	 */
	public Set<UserAuditorRole> getUserAuditorRole() {
		return userAuditorRole;
	}

	/**
	 * @param userAuditorRole the userAuditorRole to set
	 */
	public void setUserAuditorRole(Set<UserAuditorRole> userAuditorRole) {
		this.userAuditorRole = userAuditorRole;
	}

	/**
	 * @return the userEntityRole
	 */
	public Set<UserEntityRole> getUserEntityRole() {
		return userEntityRole;
	}

	/**
	 * @param userEntityRole the userEntityRole to set
	 */
	public void setUserEntityRole(Set<UserEntityRole> userEntityRole) {
		this.userEntityRole = userEntityRole;
	}

	/**
	 * @return the emailUnsubscribeSet
	 */
	public Set<EmailUnsubscribe> getEmailUnsubscribeSet() {
		return emailUnsubscribeSet;
	}

	/**
	 * @param emailUnsubscribeSet the emailUnsubscribeSet to set
	 */
	public void setEmailUnsubscribeSet(Set<EmailUnsubscribe> emailUnsubscribeSet) {
		this.emailUnsubscribeSet = emailUnsubscribeSet;
	}

	/**
	 * @return the userRoleMasterId
	 */
	public long getUserRoleMasterId() {
		return userRoleMasterId;
	}

	/**
	 * @param userRoleMasterId the userRoleMasterId to set
	 */
	public void setUserRoleMasterId(long userRoleMasterId) {
		this.userRoleMasterId = userRoleMasterId;
	}

	/**
	 * @return the userMaster
	 */
	public UserMaster getUserMaster() {
		return userMaster;
	}

	/**
	 * @param userMaster the userMaster to set
	 */
	public void setUserMaster(UserMaster userMaster) {
		this.userMaster = userMaster;
	}

	/**
	 * @return the userRole
	 */
	public UserRole getUserRole() {
		return userRole;
	}

	/**
	 * @param userRole the userRole to set
	 */
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
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
	 * @return the emailSentHisIdList
	 */
	public String getEmailSentHisIdList() {
		return emailSentHisIdList;
	}

	/**
	 * @param emailSentHisIdList the emailSentHisIdList to set
	 */
	public void setEmailSentHisIdList(String emailSentHisIdList) {
		this.emailSentHisIdList = Validations.trimInput(emailSentHisIdList);
	}

}