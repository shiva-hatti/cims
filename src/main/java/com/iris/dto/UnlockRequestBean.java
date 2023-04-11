/**
 * 
 */
package com.iris.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.iris.model.EntityBean;
import com.iris.model.FrequencyDescription;
import com.iris.model.Return;
import com.iris.model.ReturnPropertyValue;
import com.iris.model.ReturnsUploadDetails;
import com.iris.model.UserMaster;

/**
 * @author Siddique
 *
 */
public class UnlockRequestBean implements Serializable {

	private static final long serialVersionUID = 812634199169409038L;

	private Long unlockingReqId;

	private Return returns;

	private EntityBean entity;

	private FrequencyDescription frequencyDesc;

	private Long year;

	private Integer month;

	private Date reportingDate;

	private Date startDate;

	private Date endDate;

	private Integer adminStatusIdFk;

	private Integer actionIdFk;

	private UserMaster createdBy;

	private Date createdOn;

	private UserMaster approvedBy;

	private Date approvedOn;

	private String reasonForUnlocking;

	private String reasonForRejection;

	private String unlockStatus;

	private String entCode;

	private String retCode;

	private String retName;

	private String finYrFreqDesc;

	private Date maxUnlockReqDate;

	private ReturnPropertyValue returnPropertyVal;

	private List<ReturnsUploadDetails> returnsUploadDetailsList;

	private String maxUnlockReqDateString;

	private String reportingDateString;

	private String startDateString;

	private String endDateString;

	private String createdOnString;

	private String approvedOnString;

	private String sessionDateFormat;

	private String sessionTimeFormat;

	public Long getUnlockingReqId() {
		return unlockingReqId;
	}

	public void setUnlockingReqId(Long unlockingReqId) {
		this.unlockingReqId = unlockingReqId;
	}

	public Return getReturns() {
		return returns;
	}

	public void setReturns(Return returns) {
		this.returns = returns;
	}

	public EntityBean getEntity() {
		return entity;
	}

	public void setEntity(EntityBean entity) {
		this.entity = entity;
	}

	public FrequencyDescription getFrequencyDesc() {
		return frequencyDesc;
	}

	public void setFrequencyDesc(FrequencyDescription frequencyDesc) {
		this.frequencyDesc = frequencyDesc;
	}

	public Long getYear() {
		return year;
	}

	public void setYear(Long year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Date getReportingDate() {
		return reportingDate;
	}

	public void setReportingDate(Date reportingDate) {
		this.reportingDate = reportingDate;
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

	public Integer getAdminStatusIdFk() {
		return adminStatusIdFk;
	}

	public void setAdminStatusIdFk(Integer adminStatusIdFk) {
		this.adminStatusIdFk = adminStatusIdFk;
	}

	public Integer getActionIdFk() {
		return actionIdFk;
	}

	public void setActionIdFk(Integer actionIdFk) {
		this.actionIdFk = actionIdFk;
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

	public UserMaster getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(UserMaster approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}

	public String getReasonForUnlocking() {
		return reasonForUnlocking;
	}

	public void setReasonForUnlocking(String reasonForUnlocking) {
		this.reasonForUnlocking = reasonForUnlocking;
	}

	public String getReasonForRejection() {
		return reasonForRejection;
	}

	public void setReasonForRejection(String reasonForRejection) {
		this.reasonForRejection = reasonForRejection;
	}

	public String getUnlockStatus() {
		return unlockStatus;
	}

	public void setUnlockStatus(String unlockStatus) {
		this.unlockStatus = unlockStatus;
	}

	public String getEntCode() {
		return entCode;
	}

	public void setEntCode(String entCode) {
		this.entCode = entCode;
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetName() {
		return retName;
	}

	public void setRetName(String retName) {
		this.retName = retName;
	}

	public String getFinYrFreqDesc() {
		return finYrFreqDesc;
	}

	public void setFinYrFreqDesc(String finYrFreqDesc) {
		this.finYrFreqDesc = finYrFreqDesc;
	}

	public Date getMaxUnlockReqDate() {
		return maxUnlockReqDate;
	}

	public void setMaxUnlockReqDate(Date maxUnlockReqDate) {
		this.maxUnlockReqDate = maxUnlockReqDate;
	}

	public ReturnPropertyValue getReturnPropertyVal() {
		return returnPropertyVal;
	}

	public void setReturnPropertyVal(ReturnPropertyValue returnPropertyVal) {
		this.returnPropertyVal = returnPropertyVal;
	}

	public List<ReturnsUploadDetails> getReturnsUploadDetailsList() {
		return returnsUploadDetailsList;
	}

	public void setReturnsUploadDetailsList(List<ReturnsUploadDetails> returnsUploadDetailsList) {
		this.returnsUploadDetailsList = returnsUploadDetailsList;
	}

	public String getMaxUnlockReqDateString() {
		return maxUnlockReqDateString;
	}

	public void setMaxUnlockReqDateString(String maxUnlockReqDateString) {
		this.maxUnlockReqDateString = maxUnlockReqDateString;
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

	public String getApprovedOnString() {
		return approvedOnString;
	}

	public void setApprovedOnString(String approvedOnString) {
		this.approvedOnString = approvedOnString;
	}

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

}
