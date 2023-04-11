package com.iris.model;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is used for storing user information in case of module approval is ON
 */
@Entity
@Table(name = "TBL_USER_MODIFIED")
public class UserModified implements Serializable {

	private static final long serialVersionUID = -5878493740618915716L;
	private static Logger LOGGER = LogManager.getLogger(UserModified.class);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_MODIFIED_ID")
	private Long userModifiedId;

	@ManyToOne
	@JoinColumn(name = "USER_ID_FK")
	private UserMaster userMaster;

	@Column(name = "USER_NAME")
	private String userName;

	@Column(name = "USER_DETAILS")
	private String userDetailsJson;

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
	@Column(name = "APPROVED_ON")
	private Date approvedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APPROVED_BY_FK")
	private UserMaster approvedBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLE_TYPE_FK")
	private RoleType roleType;

	@Column(name = "ADMIN_STATUS_ID_FK")
	private Integer adminStatus;

	@Column(name = "ACTION_ID_FK")
	private Integer action;

	@Column(name = "REJECT_COMMENT")
	private String rejectComments;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the profilePic
	 */
	public byte[] getProfilePic() {
		return profilePic;
	}

	/**
	 * @param profilePic the profilePic to set
	 */
	public void setProfilePic(byte[] profilePic) {
		this.profilePic = profilePic;
	}

	/**
	 * @return the roleType
	 */
	public RoleType getRoleType() {
		return roleType;
	}

	/**
	 * @param roleType the roleType to set
	 */
	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	/**
	 * @return the modifiedOn
	 */
	public Date getModifiedOn() {
		return modifiedOn;
	}

	/**
	 * @param modifiedOn the modifiedOn to set
	 */
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	/**
	 * @return the modifiedBy
	 */
	public UserMaster getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(UserMaster modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the approvedOn
	 */
	public Date getApprovedOn() {
		return approvedOn;
	}

	/**
	 * @param approvedOn the approvedOn to set
	 */
	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}

	/**
	 * @return the approvedBy
	 */
	public UserMaster getApprovedBy() {
		return approvedBy;
	}

	/**
	 * @param approvedBy the approvedBy to set
	 */
	public void setApprovedBy(UserMaster approvedBy) {
		this.approvedBy = approvedBy;
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
	 * @return the adminStatus
	 */
	public Integer getAdminStatus() {
		return adminStatus;
	}

	/**
	 * @param adminStatus the adminStatus to set
	 */
	public void setAdminStatus(Integer adminStatus) {
		this.adminStatus = adminStatus;
	}

	/**
	 * @return the action
	 */
	public Integer getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(Integer action) {
		this.action = action;
	}

	/**
	 * @return the rejectComments
	 */
	public String getRejectComments() {
		return rejectComments;
	}

	/**
	 * @param rejectComments the rejectComments to set
	 */
	public void setRejectComments(String rejectComments) {
		this.rejectComments = rejectComments;
	}

	public Long getUserModifiedId() {
		return userModifiedId;
	}

	public void setUserModifiedId(Long userModifiedId) {
		this.userModifiedId = userModifiedId;
	}

	public String getUserDetailsJson() {
		return userDetailsJson;
	}

	public void setUserDetailsJson(String userDetailsJson) {
		this.userDetailsJson = userDetailsJson;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createddOn) {
		this.createdOn = createddOn;
	}

	public UserMaster getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserMaster createdBy) {
		this.createdBy = createdBy;
	}

}