package com.iris.dto;

import java.io.Serializable;
import java.util.Date;

import com.iris.model.UserMaster;

/**
 * This is the UnlockingRequest bean
 * 
 * @author Sanjayv
 * @date
 *
 */
public class UnlockingRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4554753410610072213L;

	private Long unlockingReqId;

	private Integer month;

	private Date reportingDate;

	private Date startDate;

	private Date endDate;

	private Integer adminStatusIdFk;

	private Integer actionIdFk;

	private Date createdOn;

	private UserMaster approvedBy;

	private Date approvedOn;

	private String reasonForUnlocking;

	private String reasonForRejection;

	private String unlockStatus;

	private String entCode;

	private String retCode;

	private String retName;
	private Long entityId;
	private Long userId;

	private Long returnId;

	private String returnName;

	private String returnCode;

	private String finYrFreqDesc;

	private Date maxUnlockReqDate;

	private String entityCode;
	private Long roleId;

	private Date reportingPeriodEndDate;

	private Long finYearFormatId;

	private Date reportingPeriodStartDate;

	private String reportingPeriodEndDateInString;

	private String reportingPeriodStartDateInString;

	private Integer returnPropertyValId;

	private String reasonOfNotProcessed;

	private String dateFormat;

	private String calenderFormat;

	private String timeFormat;

	public String getReasonOfNotProcessed() {
		return reasonOfNotProcessed;
	}

	public void setReasonOfNotProcessed(String reasonOfNotProcessed) {
		this.reasonOfNotProcessed = reasonOfNotProcessed;
	}

	public String getReportingPeriodEndDateInString() {
		return reportingPeriodEndDateInString;
	}

	public void setReportingPeriodEndDateInString(String reportingPeriodEndDateInString) {
		this.reportingPeriodEndDateInString = reportingPeriodEndDateInString;
	}

	public String getReportingPeriodStartDateInString() {
		return reportingPeriodStartDateInString;
	}

	public void setReportingPeriodStartDateInString(String reportingPeriodStartDateInString) {
		this.reportingPeriodStartDateInString = reportingPeriodStartDateInString;
	}

	public void setReportingPeriodStartDate(Date reportingPeriodStartDate) {
		this.reportingPeriodStartDate = reportingPeriodStartDate;
	}

	public void setReportingPeriodEndDate(Date reportingPeriodEndDate) {
		this.reportingPeriodEndDate = reportingPeriodEndDate;
	}

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getReturnId() {
		return returnId;
	}

	public void setReturnId(Long returnId) {
		this.returnId = returnId;
	}

	public String getReturnName() {
		return returnName;
	}

	public void setReturnName(String returnName) {
		this.returnName = returnName;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public Long getUnlockingReqId() {
		return unlockingReqId;
	}

	public void setUnlockingReqId(Long unlockingReqId) {
		this.unlockingReqId = unlockingReqId;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getReportingPeriodEndDate() {
		return reportingPeriodEndDate;
	}

	public void setFinYearFormatId(Long finYearFormatId) {
		this.finYearFormatId = finYearFormatId;
	}

	public Long getFinYearFormatId() {
		return finYearFormatId;
	}

	public Date getReportingPeriodStartDate() {
		return reportingPeriodStartDate;
	}

	public void setReturnPropertyValId(Integer returnPropertyValId) {
		this.returnPropertyValId = returnPropertyValId;
	}

	public Integer getReturnPropertyValId() {
		return returnPropertyValId;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getCalenderFormat() {
		return calenderFormat;
	}

	public void setCalenderFormat(String calenderFormat) {
		this.calenderFormat = calenderFormat;
	}

	public String getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

}
