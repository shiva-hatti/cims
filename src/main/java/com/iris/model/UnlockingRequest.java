package com.iris.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
 * This is the UnlockingRequest bean class with Hibernate mapping.
 */
@Entity
@Table(name = "TBL_UNLOCKING_REQUEST")
public class UnlockingRequest implements Serializable {

	private static final long serialVersionUID = 6655470142391643753L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "UNLOCK_REQUEST_ID")
	private Long unlockingReqId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_ID_FK")
	private Return returns;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENTITY_ID_FK")
	private EntityBean entity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FIN_YR_FREQUENCY_DESC_ID_FK")
	private FrequencyDescription frequencyDesc;

	@Column(name = "YEAR")
	private Long year;

	@Column(name = "MONTH")
	private Integer month;

	@Column(name = "REPORTING_DATE")
	private Date reportingDate;

	@Column(name = "REPORTING_START_DATE")
	private Date startDate;

	@Column(name = "REPORTING_END_DATE")
	private Date endDate;

	@Column(name = "ADMIN_STATUS_ID_FK")
	private Integer adminStatusIdFk;

	@Column(name = "ACTION_ID_FK")
	private Integer actionIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY_FK")
	private UserMaster createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APPROVED_BY_FK")
	private UserMaster approvedBy;

	@Column(name = "APPROVED_ON")
	private Date approvedOn;

	@Column(name = "REASON_FOR_UNLOCKING")
	private String reasonForUnlocking;

	@Column(name = "REASON_FOR_REJECTION")
	private String reasonForRejection;

	@Column(name = "UNLOCK_STATUS")
	private String unlockStatus;

	@Transient
	private String entCode;

	@Transient
	private String retCode;

	@Transient
	private String retName;

	@Transient
	private String finYrFreqDesc;

	@Column(name = "MAX_UNLOCK_REQ_DATE")
	private Date maxUnlockReqDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_PROP_VAL_ID_FK")
	private ReturnPropertyValue returnPropertyVal;

	@OneToMany(mappedBy = "unlockingReqId")
	private List<ReturnsUploadDetails> returnsUploadDetailsList;

	@Transient
	private String maxUnlockReqDateString;

	@Transient
	private String reportingDateString;

	@Transient
	private String startDateString;

	@Transient
	private String endDateString;

	@Transient
	private String createdOnString;

	@Transient
	private String approvedOnString;

	@Transient
	private String sessionDateFormat;

	@Transient
	private String sessionTimeFormat;

	public String getSessionDateFormat() {
		return sessionDateFormat;
	}

	public void setSessionDateFormat(String sessionDateFormat) {
		this.sessionDateFormat = sessionDateFormat;
	}

	public String getSessionTimeFormat() {
		return sessionTimeFormat;
	}

	public void setSessionTimeFormat(String sessionTimeFormat) {
		this.sessionTimeFormat = sessionTimeFormat;
	}

	public String getApprovedOnString() {
		return approvedOnString;
	}

	public void setApprovedOnString(String approvedOnString) {
		this.approvedOnString = approvedOnString;
	}

	public String getReportingDateString() {
		return reportingDateString;
	}

	public void setReportingDateString(String reportingDateString) {
		this.reportingDateString = reportingDateString;
	}

	public String getStartDateString() {
		return startDateString;
	}

	public void setStartDateString(String startDateString) {
		this.startDateString = startDateString;
	}

	public String getEndDateString() {
		return endDateString;
	}

	public void setEndDateString(String endDateString) {
		this.endDateString = endDateString;
	}

	public String getCreatedOnString() {
		return createdOnString;
	}

	public void setCreatedOnString(String createdOnString) {
		this.createdOnString = createdOnString;
	}

	public String getMaxUnlockReqDateString() {
		return maxUnlockReqDateString;
	}

	public void setMaxUnlockReqDateString(String maxUnlockReqDateString) {
		this.maxUnlockReqDateString = maxUnlockReqDateString;
	}

	/**
	 * @return the returnPropertyVal
	 */
	public ReturnPropertyValue getReturnPropertyVal() {
		return returnPropertyVal;
	}

	/**
	 * @param returnPropertyVal the returnPropertyVal to set
	 */
	public void setReturnPropertyVal(ReturnPropertyValue returnPropertyVal) {
		this.returnPropertyVal = returnPropertyVal;
	}

	public Date getMaxUnlockReqDate() {
		return maxUnlockReqDate;
	}

	public void setMaxUnlockReqDate(Date maxUnlockReqDate) {
		this.maxUnlockReqDate = maxUnlockReqDate;
	}

	/**
	 * @return the unlockingReqId
	 */
	public Long getUnlockingReqId() {
		return unlockingReqId;
	}

	/**
	 * @param unlockingReqId the unlockingReqId to set
	 */
	public void setUnlockingReqId(Long unlockingReqId) {
		this.unlockingReqId = unlockingReqId;
	}

	/**
	 * @return the returns
	 */
	public Return getReturns() {
		return returns;
	}

	/**
	 * @param returns the returns to set
	 */
	public void setReturns(Return returns) {
		this.returns = returns;
	}

	/**
	 * @return the entity
	 */
	public EntityBean getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(EntityBean entity) {
		this.entity = entity;
	}

	/**
	 * @return the frequencyDesc
	 */
	public FrequencyDescription getFrequencyDesc() {
		return frequencyDesc;
	}

	/**
	 * @param frequencyDesc the frequencyDesc to set
	 */
	public void setFrequencyDesc(FrequencyDescription frequencyDesc) {
		this.frequencyDesc = frequencyDesc;
	}

	/**
	 * @return the year
	 */
	public Long getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(Long year) {
		this.year = year;
	}

	/**
	 * @return the month
	 */
	public Integer getMonth() {
		return month;
	}

	/**
	 * @param month the month to set
	 */
	public void setMonth(Integer month) {
		this.month = month;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the adminStatusIdFk
	 */
	public Integer getAdminStatusIdFk() {
		return adminStatusIdFk;
	}

	/**
	 * @param adminStatusIdFk the adminStatusIdFk to set
	 */
	public void setAdminStatusIdFk(Integer adminStatusIdFk) {
		this.adminStatusIdFk = adminStatusIdFk;
	}

	/**
	 * @return the actionIdFk
	 */
	public Integer getActionIdFk() {
		return actionIdFk;
	}

	/**
	 * @param actionIdFk the actionIdFk to set
	 */
	public void setActionIdFk(Integer actionIdFk) {
		this.actionIdFk = actionIdFk;
	}

	/**
	 * @return the createdBy
	 */
	public UserMaster getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(UserMaster createdBy) {
		this.createdBy = createdBy;
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
	 * @return the reasonForUnlocking
	 */
	public String getReasonForUnlocking() {
		return reasonForUnlocking;
	}

	/**
	 * @param reasonForUnlocking the reasonForUnlocking to set
	 */
	public void setReasonForUnlocking(String reasonForUnlocking) {
		this.reasonForUnlocking = Validations.trimInput(reasonForUnlocking);
	}

	/**
	 * @return the reasonForRejection
	 */
	public String getReasonForRejection() {
		return reasonForRejection;
	}

	/**
	 * @param reasonForRejection the reasonForRejection to set
	 */
	public void setReasonForRejection(String reasonForRejection) {
		this.reasonForRejection = Validations.trimInput(reasonForRejection);
	}

	/**
	 * @return the reportingDate
	 */
	public Date getReportingDate() {
		return reportingDate;
	}

	/**
	 * @param reportingDate the reportingDate to set
	 */
	public void setReportingDate(Date reportingDate) {
		this.reportingDate = reportingDate;
	}

	/**
	 * @return the entCode
	 */
	public String getEntCode() {
		return entCode;
	}

	/**
	 * @param entCode the entCode to set
	 */
	public void setEntCode(String entCode) {
		this.entCode = Validations.trimInput(entCode);
	}

	/**
	 * @return the retCode
	 */
	public String getRetCode() {
		return retCode;
	}

	/**
	 * @param retCode the retCode to set
	 */
	public void setRetCode(String retCode) {
		this.retCode = Validations.trimInput(retCode);
	}

	/**
	 * @return the retName
	 */
	public String getRetName() {
		return retName;
	}

	/**
	 * @param retName the retName to set
	 */
	public void setRetName(String retName) {
		this.retName = Validations.trimInput(retName);
	}

	/**
	 * @return the finYrFreqDesc
	 */
	public String getFinYrFreqDesc() {
		return finYrFreqDesc;
	}

	/**
	 * @param finYrFreqDesc the finYrFreqDesc to set
	 */
	public void setFinYrFreqDesc(String finYrFreqDesc) {
		this.finYrFreqDesc = Validations.trimInput(finYrFreqDesc);
	}

	public String getUnlockStatus() {
		return unlockStatus;
	}

	public void setUnlockStatus(String unlockStatus) {
		this.unlockStatus = unlockStatus;
	}

	/**
	 * @return the returnsUploadDetailsList
	 */
	public List<ReturnsUploadDetails> getReturnsUploadDetailsList() {
		return returnsUploadDetailsList;
	}

	/**
	 * @param returnsUploadDetailsList the returnsUploadDetailsList to set
	 */
	public void setReturnsUploadDetailsList(List<ReturnsUploadDetails> returnsUploadDetailsList) {
		this.returnsUploadDetailsList = returnsUploadDetailsList;
	}
}