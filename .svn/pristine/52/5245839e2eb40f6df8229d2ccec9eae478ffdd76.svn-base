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

import com.iris.util.Validations;

/**
 * This is the Nationality bean class with Hibernate mapping.
 * 
 * @author pippar
 * @date 01/06/2015
 *
 */

@Entity
@Table(name = "TBL_NATIONALITY")
public class Nationality implements Serializable {

	@Id
	//	@SequenceGenerator(name = "NATIONALITY_ID_GENERATOR", sequenceName = "TBL_NATIONALITY_SEQ", allocationSize = 1)
	//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="NATIONALITY_ID_GENERATOR")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NATIONALITY_ID")
	private Long nationalityId;

	@Column(name = "NATIONALITY_NAME")
	private String nationalityName;

	@Column(name = "NATIONALITY_CODE")
	private String nationalityCode;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

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

	@Column(name = "LAST_UPDATE_ON")
	private Date lastUpdateOn;

	@Transient
	private Long roleIdKey;

	@OneToMany(mappedBy = "nationalityIdFk")
	private Set<NationalityLabel> natLblSet;

	@Column(name = "IS_BULK_UPLOAD")
	private Boolean isBulkUpload;

	@Column(name = "IS_MEMBER")
	private Boolean isMember;

	@Transient
	private int singleLanguage;

	@Transient
	private String uniqueIdentifier;

	@Transient
	private String nationalityNameBil;

	private static final long serialVersionUID = 7228919078231774511L;

	/**
	 * @return the nationalityId
	 */
	public Long getNationalityId() {
		return nationalityId;
	}

	/**
	 * @param nationalityId the nationalityId to set
	 */
	public void setNationalityId(Long nationalityId) {
		this.nationalityId = nationalityId;
	}

	/**
	 * @return the nationalityName
	 */
	public String getNationalityName() {
		return nationalityName;
	}

	/**
	 * @param nationalityName the nationalityName to set
	 */
	public void setNationalityName(String nationalityName) {
		this.nationalityName = Validations.trimInput(nationalityName);
	}

	/**
	 * @return the nationalityCode
	 */
	public String getNationalityCode() {
		return nationalityCode;
	}

	/**
	 * @param nationalityCode the nationalityCode to set
	 */
	public void setNationalityCode(String nationalityCode) {
		this.nationalityCode = Validations.trimInput(nationalityCode);
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
	 * @return the natLblSet
	 */
	public Set<NationalityLabel> getNatLblSet() {
		return natLblSet;
	}

	/**
	 * @param natLblSet the natLblSet to set
	 */
	public void setNatLblSet(Set<NationalityLabel> natLblSet) {
		this.natLblSet = natLblSet;
	}

	/**
	 * @return the isBulkUpload
	 */
	public Boolean getIsBulkUpload() {
		return isBulkUpload;
	}

	/**
	 * @param isBulkUpload the isBulkUpload to set
	 */
	public void setIsBulkUpload(Boolean isBulkUpload) {
		this.isBulkUpload = isBulkUpload;
	}

	public Boolean getIsMember() {
		return isMember;
	}

	public void setIsMember(Boolean isMember) {
		this.isMember = isMember;
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
	 * @return the nationalityNameBil
	 */
	public String getNationalityNameBil() {
		return nationalityNameBil;
	}

	/**
	 * @param nationalityNameBil the nationalityNameBil to set
	 */
	public void setNationalityNameBil(String nationalityNameBil) {
		this.nationalityNameBil = Validations.trimInput(nationalityNameBil);
	}

}
