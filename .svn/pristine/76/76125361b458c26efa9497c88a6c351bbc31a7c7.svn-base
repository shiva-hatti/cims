package com.iris.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.iris.util.Validations;

/**
 * @author Lakhan Kumar
 * @date 02/04/2020
 *
 */
@Entity
@Table(name = "TBL_PAN_MASTER_TEMP")
@JsonInclude(Include.NON_NULL)
public class PanMasterTemp implements Serializable {

	private static final long serialVersionUID = 1884924972907254165L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PAN_ID")
	private Long panId;

	@Column(name = "PAN_NUM")
	private String panNumber;

	@Column(name = "BORROWER_NAME")
	private String borrowerName;

	@Column(name = "BORROWER_ALTERNATE_NAME")
	private String borrowerAlternateName;

	@Column(name = "BORROWER_TITLE")
	private String borrowerTitle;

	@Column(name = "BORROWER_MOBILE")
	private Long borrowerMobile;

	@Column(name = "NSDL_VERIFICATION_STATUS")
	private Integer verificationStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY_FK")
	private UserMaster createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "ENTRY_TYPE")
	private Integer entryType;

	@Column(name = "IS_RBI_GENERATED_PAN")
	private Boolean rbiGenerated;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENTITY_ID_FK")
	private EntityBean entityBean;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PAN_ID_FK")
	private PanMasterTemp panIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INSTITUTION_TYPE_ID_FK")
	private InstitutionType institutionType;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "COMMENT")
	private String comment;

	@Column(name = "IS_BULK_UPLOAD")
	private Boolean isBulkUpload;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APPROVED_BY_FK")
	private UserMaster approvedByFk;

	@Column(name = "APPROVED_ON")
	private Date approvedOn;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUPPORTING_DOC_TYPE_ID_FK")
	private SupportingDocType supportingDocType;

	@Column(name = "SUPPORT_DOC_IDENTITY_NUM")
	private String supportingDocIdentityNum;

	@Column(name = "BORROWER_DOB")
	private Date borrowerDob;
	//commented by shridhar as no field is present in DB but available in user story
	@Column(name = "SUPPORTING_DOC_NAME")
	private String supportDocName;

	public String getSupportDocName() {
		return supportDocName;
	}

	public void setSupportDocName(String supportDocName) {
		this.supportDocName = supportDocName;
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

	/**
	 * @return the rbiGenerated
	 */
	public Boolean getRbiGenerated() {
		return rbiGenerated;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
		this.panNumber = Validations.trimInput(panNumber);
	}

	public String getBorrowerName() {
		return borrowerName;
	}

	public void setBorrowerName(String borrowerName) {
		this.borrowerName = Validations.trimInput(borrowerName);
	}

	public String getBorrowerAlternateName() {
		return borrowerAlternateName;
	}

	public void setBorrowerAlternateName(String borrowerAlternateName) {
		this.borrowerAlternateName = Validations.trimInput(borrowerAlternateName);
	}

	public String getBorrowerTitle() {
		return borrowerTitle;
	}

	public void setBorrowerTitle(String borrowerTitle) {
		this.borrowerTitle = Validations.trimInput(borrowerTitle);
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

	public UserMaster getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserMaster createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getEntryType() {
		return entryType;
	}

	public void setEntryType(Integer entryType) {
		this.entryType = entryType;
	}

	public Boolean isRbiGenerated() {
		return rbiGenerated;
	}

	public void setRbiGenerated(Boolean rbiGenerated) {
		this.rbiGenerated = rbiGenerated;
	}

	public EntityBean getEntityBean() {
		return entityBean;
	}

	public void setEntityBean(EntityBean entityBean) {
		this.entityBean = entityBean;
	}

	public PanMasterTemp getPanIdFk() {
		return panIdFk;
	}

	public void setPanIdFk(PanMasterTemp panIdFk) {
		this.panIdFk = panIdFk;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public UserMaster getApprovedByFk() {
		return approvedByFk;
	}

	public void setApprovedByFk(UserMaster approvedByFk) {
		this.approvedByFk = approvedByFk;
	}

	public Date getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}

	public InstitutionType getInstitutionType() {
		return institutionType;
	}

	public void setInstitutionType(InstitutionType institutionType) {
		this.institutionType = institutionType;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public SupportingDocType getSupportingDocType() {
		return supportingDocType;
	}

	public void setSupportingDocType(SupportingDocType supportingDocType) {
		this.supportingDocType = supportingDocType;
	}

	public String getSupportingDocIdentityNum() {
		return supportingDocIdentityNum;
	}

	public void setSupportingDocIdentityNum(String supportingDocIdentityNum) {
		this.supportingDocIdentityNum = supportingDocIdentityNum;
	}

	public Date getBorrowerDob() {
		return borrowerDob;
	}

	public void setBorrowerDob(Date borrowerDob) {
		this.borrowerDob = borrowerDob;
	}

}
