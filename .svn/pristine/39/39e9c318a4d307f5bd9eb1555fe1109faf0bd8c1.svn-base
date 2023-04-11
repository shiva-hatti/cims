package com.iris.model;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.iris.util.Validations;

/**
 * This is the User Role bean class with Hibernate mapping.
 * 
 * @author sajadhav
 * @date 27/01/2020
 */
@Entity
@Table(name = "TBL_USER_ROLE")
@JsonInclude(Include.NON_NULL)
public class UserRole implements Serializable {

	private static final long serialVersionUID = 8813042586892944078L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ROLE_ID")
	private Long userRoleId;

	@Column(name = "ROLE_DESC")
	private String roleDesc;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ROLE_TYPE_FK")
	private RoleType roleType;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "LINK_TO_ENTITY")
	private Boolean linkToEntity;

	@Column(name = "LINK_TO_AUDITOR")
	private Boolean linkToAuditor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY_FK")
	private UserMaster user;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_MODIFIED_BY_FK")
	private UserMaster userModify;

	@Column(name = "LAST_MODIFIED_ON")
	private Date lastModifiedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_APPROVED_BY_FK")
	private UserMaster lastApprovedBy;

	@Column(name = "LAST_APPROVED_ON")
	private Date lastApprovedOn;

	@Column(name = "ROLE_PRIORITY")
	private Long rolePriority;

	@Column(name = "LAST_UPDATE_ON")
	private Date lastUpdateOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_ROLE_FK")
	private UserRole createdByRole;

	@Transient
	private Long roleIdKey;

	@OneToMany(mappedBy = "userRole")
	private Set<UserRoleMaster> usrRoleMstrSet;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "REGULATOR_ID_FK")
	private Regulator regulatorIdFk;

	@OneToMany(mappedBy = "userRoleIdFk")
	private Set<UserRoleLabel> usrRoleLabelSet;

	@OneToMany(mappedBy = "userRole")
	private Set<UserRolePlatFormMap> userRolePlatFormMap;

	@Column(name = "ROLE_NAME")
	private String roleName;

	@Column(name = "DEPT_ADMIN")
	private String deptAdmin;

	@OneToMany(mappedBy = "role")
	private Set<UserRoleActivityMap> userRoleActivityMapping;

	@OneToMany(mappedBy = "roleIdFk")
	private Set<UserRoleReturnMapping> userRoleReturnMapping;

	@OneToMany(mappedBy = "userRole")
	private Set<UserRoleEntityMapping> userRoleEntityMapping;

	@Transient
	private int singleLanguage;

	/**
	 * @return the userRoleEntityMapping
	 */
	public Set<UserRoleEntityMapping> getUserRoleEntityMapping() {
		return userRoleEntityMapping;
	}

	/**
	 * @param userRoleEntityMapping the userRoleEntityMapping to set
	 */
	public void setUserRoleEntityMapping(Set<UserRoleEntityMapping> userRoleEntityMapping) {
		this.userRoleEntityMapping = userRoleEntityMapping;
	}

	/**
	 * @return the userRoleReturnMapping
	 */
	public Set<UserRoleReturnMapping> getUserRoleReturnMapping() {
		return userRoleReturnMapping;
	}

	/**
	 * @param userRoleReturnMapping the userRoleReturnMapping to set
	 */
	public void setUserRoleReturnMapping(Set<UserRoleReturnMapping> userRoleReturnMapping) {
		this.userRoleReturnMapping = userRoleReturnMapping;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	/**
	 * @return the lastUpdateOn
	 */
	public Date getLastUpdateOn() {
		return lastUpdateOn;
	}

	/**
	 * @param lastUpdateOn the lastUpdateOn to set
	 */
	public void setLastUpdateOn(Date lastUpdateOn) {
		this.lastUpdateOn = lastUpdateOn;
	}

	/**
	 * @return the userRoleId
	 */
	public Long getUserRoleId() {
		return userRoleId;
	}

	/**
	 * @param userRoleId the userRoleId to set
	 */
	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	/**
	 * @return the roleDesc
	 */
	public String getRoleDesc() {
		return roleDesc;
	}

	/**
	 * @param roleDesc the roleDesc to set
	 */
	public void setRoleDesc(String roleDesc) {
		this.roleDesc = Validations.trimInput(roleDesc);
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
	 * @return the linkToEntity
	 */
	public Boolean getLinkToEntity() {
		return linkToEntity;
	}

	/**
	 * @param linkToEntity the linkToEntity to set
	 */
	public void setLinkToEntity(Boolean linkToEntity) {
		this.linkToEntity = linkToEntity;
	}

	/**
	 * @return the linkToAuditor
	 */
	public Boolean getLinkToAuditor() {
		return linkToAuditor;
	}

	/**
	 * @param linkToAuditor the linkToAuditor to set
	 */
	public void setLinkToAuditor(Boolean linkToAuditor) {
		this.linkToAuditor = linkToAuditor;
	}

	/**
	 * @return the user
	 */
	public UserMaster getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(UserMaster user) {
		this.user = user;
	}

	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the userModify
	 */
	public UserMaster getUserModify() {
		return userModify;
	}

	/**
	 * @param userModify the userModify to set
	 */
	public void setUserModify(UserMaster userModify) {
		this.userModify = userModify;
	}

	/**
	 * @return the lastModifiedOn
	 */
	public Date getLastModifiedOn() {
		return lastModifiedOn;
	}

	/**
	 * @param lastModifiedOn the lastModifiedOn to set
	 */
	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	/**
	 * @return the lastApprovedBy
	 */
	public UserMaster getLastApprovedBy() {
		return lastApprovedBy;
	}

	/**
	 * @param lastApprovedBy the lastApprovedBy to set
	 */
	public void setLastApprovedBy(UserMaster lastApprovedBy) {
		this.lastApprovedBy = lastApprovedBy;
	}

	/**
	 * @return the lastApprovedOn
	 */
	public Date getLastApprovedOn() {
		return lastApprovedOn;
	}

	/**
	 * @param lastApprovedOn the lastApprovedOn to set
	 */
	public void setLastApprovedOn(Date lastApprovedOn) {
		this.lastApprovedOn = lastApprovedOn;
	}

	/**
	 * @return the rolePriority
	 */
	public Long getRolePriority() {
		return rolePriority;
	}

	/**
	 * @param rolePriority the rolePriority to set
	 */
	public void setRolePriority(Long rolePriority) {
		this.rolePriority = rolePriority;
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
	 * @return the usrRoleMstrSet
	 */
	public Set<UserRoleMaster> getUsrRoleMstrSet() {
		return usrRoleMstrSet;
	}

	/**
	 * @param usrRoleMstrSet the usrRoleMstrSet to set
	 */
	public void setUsrRoleMstrSet(Set<UserRoleMaster> usrRoleMstrSet) {
		this.usrRoleMstrSet = usrRoleMstrSet;
	}

	/**
	 * @return the singleLanguage
	 */
	public int getSingleLanguage() {
		return singleLanguage;
	}

	/**
	 * @param singleLanguage the singleLanguage to set
	 */
	public void setSingleLanguage(int singleLanguage) {
		this.singleLanguage = singleLanguage;
	}

	/**
	 * @return the regulatorIdFk
	 */
	public Regulator getRegulatorIdFk() {
		return regulatorIdFk;
	}

	/**
	 * @param regulatorIdFk the regulatorIdFk to set
	 */
	public void setRegulatorIdFk(Regulator regulatorIdFk) {
		this.regulatorIdFk = regulatorIdFk;
	}

	public UserRole getCreatedByRole() {
		return createdByRole;
	}

	public void setCreatedByRole(UserRole createdByRole) {
		this.createdByRole = createdByRole;
	}

	public Set<UserRoleLabel> getUsrRoleLabelSet() {
		return usrRoleLabelSet;
	}

	public void setUsrRoleLabelSet(Set<UserRoleLabel> usrRoleLabelSet) {
		this.usrRoleLabelSet = usrRoleLabelSet;
	}

	public Set<UserRolePlatFormMap> getUserRolePlatFormMap() {
		return userRolePlatFormMap;
	}

	public void setUserRolePlatFormMap(Set<UserRolePlatFormMap> userRolePlatFormMap) {
		this.userRolePlatFormMap = userRolePlatFormMap;
	}

	/**
	 * @return the userRoleActivityMapping
	 */
	public Set<UserRoleActivityMap> getUserRoleActivityMapping() {
		return userRoleActivityMapping;
	}

	/**
	 * @param userRoleActivityMapping the userRoleActivityMapping to set
	 */
	public void setUserRoleActivityMapping(Set<UserRoleActivityMap> userRoleActivityMapping) {
		this.userRoleActivityMapping = userRoleActivityMapping;
	}

	public String getDeptAdmin() {
		return deptAdmin;
	}

	public void setDeptAdmin(String deptAdmin) {
		this.deptAdmin = deptAdmin;
	}

}