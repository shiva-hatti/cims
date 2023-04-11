package com.iris.model;

import java.io.Serializable;
import java.sql.Timestamp;
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

@Entity
@Table(name = "TBL_XBRL_WEB_FORM_PARTIAL_DATA")
public class XBRLWebFormPartialData implements Serializable {

	private static final long serialVersionUID = 1560427779848083699L;

	@Id
	@Column(name = "PARTIAL_SAVE_DATA_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long partialDataId;

	@Column(name = "GUID")
	private String guid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_ID_FK")
	private Return returnObj;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENTITY_ID_FK")
	private EntityBean entity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILING_STATUS_ID_FK")
	private FilingStatus filingStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FORM_VERSION_ID")
	private ReturnTemplate taxonomyId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FIN_YR_FREQUENCY_ID_FK")
	private Frequency frequency;

	@Column(name = "START_DATE")
	private Date startDate;

	@Column(name = "END_DATE")
	private Date endDate;

	@Column(name = "UPLOADED_DATE")
	private Timestamp uploadedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPLOADED_BY")
	private UserMaster uploadedBy;

	@Column(name = "MODIFICATION_DATE")
	private Timestamp modificationDate;

	@Column(name = "LEVEL_OF_ROUNDING")
	private String levelOfRounding;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFIED_BY")
	private UserMaster modifiedBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CURRENCY_ID_FK")
	private Currency currency;

	@Column(name = "RETURN_PROPERTY_ID_FK")
	private String returnProperty;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_UPLOAD_DETAILES_ID_FK")
	private ReturnsUploadDetails returnsUploadDetails;

	@Column(name = "ROLE_ID")
	private Long roleId;

	@Column(name = "LEVEL_OF_ROUNDING_CODE")
	private String levelOfRoundingCode;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getLevelOfRounding() {
		return levelOfRounding;
	}

	public void setLevelOfRounding(String levelOfRounding) {
		this.levelOfRounding = levelOfRounding;
	}

	public ReturnsUploadDetails getReturnsUploadDetails() {
		return returnsUploadDetails;
	}

	public void setReturnsUploadDetails(ReturnsUploadDetails returnsUploadDetails) {
		this.returnsUploadDetails = returnsUploadDetails;
	}

	public String getReturnProperty() {
		return returnProperty;
	}

	public void setReturnProperty(String returnProperty) {
		this.returnProperty = returnProperty;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Long getPartialDataId() {
		return partialDataId;
	}

	public void setPartialDataId(Long partialDataId) {
		this.partialDataId = partialDataId;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public Return getReturnObj() {
		return returnObj;
	}

	public void setReturnObj(Return returnObj) {
		this.returnObj = returnObj;
	}

	public EntityBean getEntity() {
		return entity;
	}

	public void setEntity(EntityBean entity) {
		this.entity = entity;
	}

	public FilingStatus getFilingStatus() {
		return filingStatus;
	}

	public void setFilingStatus(FilingStatus filingStatus) {
		this.filingStatus = filingStatus;
	}

	public ReturnTemplate getTaxonomyId() {
		return taxonomyId;
	}

	public void setTaxonomyId(ReturnTemplate taxonomyId) {
		this.taxonomyId = taxonomyId;
	}

	public Frequency getFrequency() {
		return frequency;
	}

	public void setFrequency(Frequency frequency) {
		this.frequency = frequency;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Timestamp getUploadedDate() {
		return uploadedDate;
	}

	public void setUploadedDate(Timestamp uploadedDate) {
		this.uploadedDate = uploadedDate;
	}

	public UserMaster getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(UserMaster uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public Timestamp getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Timestamp modificationDate) {
		this.modificationDate = modificationDate;
	}

	public UserMaster getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(UserMaster modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getLevelOfRoundingCode() {
		return levelOfRoundingCode;
	}

	public void setLevelOfRoundingCode(String levelOfRoundingCode) {
		this.levelOfRoundingCode = levelOfRoundingCode;
	}

}
