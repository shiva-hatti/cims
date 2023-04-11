package com.iris.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Transient;

import org.springframework.util.StringUtils;

import com.iris.model.ReturnPropertyValue;
import com.iris.model.UserMaster;

public class FilingCalendarInfo implements Serializable {

	private static final long serialVersionUID = 4493022601121932697L;

	private int filingCalendarId;
	private Long returnFrequencyId;
	private Long returnId;
	private Long finYearId = 1L;
	private Integer filingWindowExtensionStart;
	private String sendMail;
	private Integer filingWindowExtensionEnd;
	private UserMaster userModify;
	private Date lastModifiedOn;
	private Date lastUpdateOn;
	private Date createdOn;
	private Date calDate;
	private Boolean includeHoliday;
	private Boolean includeWeekend;
	private Long emailNotificationDays;
	private ReturnPropertyValue returnPropertyVal;
	private UserMaster createdBy;
	private Boolean isActive;
	private String filingEndDate;
	private String returnPropertyName;
	private Boolean updateFromReturn;
	private Boolean isApplicable;
	private Integer graceDays;
	private String frequencyName;

	/**
	 * @return the returnId
	 */
	public Long getReturnId() {
		return returnId;
	}

	/**
	 * @param returnId the returnId to set
	 */
	public void setReturnId(Long returnId) {
		this.returnId = returnId;
	}

	/**
	 * @return the finYearId
	 */
	public Long getFinYearId() {
		return finYearId;
	}

	/**
	 * @param finYearId the finYearId to set
	 */
	public void setFinYearId(Long finYearId) {
		this.finYearId = finYearId;
	}

	/**
	 * @return the filingWindowExtensionStart
	 */
	public Integer getFilingWindowExtensionStart() {
		return filingWindowExtensionStart;
	}

	/**
	 * @param filingWindowExtensionStart the filingWindowExtensionStart to set
	 */
	public void setFilingWindowExtensionStart(Integer filingWindowExtensionStart) {
		this.filingWindowExtensionStart = filingWindowExtensionStart;
	}

	/**
	 * @return the sendMail
	 */
	public String getSendMail() {
		return sendMail;
	}

	/**
	 * @param sendMail the sendMail to set
	 */
	public void setSendMail(String sendMail) {
		this.sendMail = sendMail;
	}

	/**
	 * @return the filingWindowExtensionEnd
	 */
	public Integer getFilingWindowExtensionEnd() {
		return filingWindowExtensionEnd;
	}

	/**
	 * @param filingWindowExtensionEnd the filingWindowExtensionEnd to set
	 */
	public void setFilingWindowExtensionEnd(Integer filingWindowExtensionEnd) {
		this.filingWindowExtensionEnd = filingWindowExtensionEnd;
	}

	/**
	 * @return the returnFrequencyId
	 */
	public Long getReturnFrequencyId() {
		return returnFrequencyId;
	}

	/**
	 * @param returnFrequencyId the returnFrequencyId to set
	 */
	public void setReturnFrequencyId(Long returnFrequencyId) {
		this.returnFrequencyId = returnFrequencyId;
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
	 * @return the lastUpdateOn
	 */
	public Date getLastUpdateOn() {
		return lastUpdateOn;
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
	 * @param lastUpdateOn the lastUpdateOn to set
	 */
	public void setLastUpdateOn(Date lastUpdateOn) {
		this.lastUpdateOn = lastUpdateOn;
	}

	/**
	 * @return the calDate
	 */
	public Date getCalDate() {
		return calDate;
	}

	/**
	 * @param calDate the calDate to set
	 */
	public void setCalDate(Date calDate) {
		this.calDate = calDate;
	}

	/**
	 * @return the filingCalendarId
	 */
	public int getFilingCalendarId() {
		return filingCalendarId;
	}

	/**
	 * @param filingCalendarId the filingCalendarId to set
	 */
	public void setFilingCalendarId(int filingCalendarId) {
		this.filingCalendarId = filingCalendarId;
	}

	/**
	 * @return the includeHoliday
	 */
	public Boolean getIncludeHoliday() {
		if (StringUtils.isEmpty(includeHoliday)) {
			return false;
		} else {
			return includeHoliday;
		}
	}

	/**
	 * @param includeHoliday the includeHoliday to set
	 */
	public void setIncludeHoliday(Boolean includeHoliday) {
		this.includeHoliday = includeHoliday;
	}

	/**
	 * @return the includeWeekend
	 */
	public Boolean getIncludeWeekend() {
		if (StringUtils.isEmpty(includeWeekend)) {
			return false;
		} else {
			return includeWeekend;
		}
	}

	/**
	 * @param includeWeekend the includeWeekend to set
	 */
	public void setIncludeWeekend(Boolean includeWeekend) {
		this.includeWeekend = includeWeekend;
	}

	/**
	 * @return the emailNotificationDays
	 */
	public Long getEmailNotificationDays() {
		return emailNotificationDays;
	}

	/**
	 * @param emailNotificationDays the emailNotificationDays to set
	 */
	public void setEmailNotificationDays(Long emailNotificationDays) {
		this.emailNotificationDays = emailNotificationDays;
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
	 * @return the filingEndDate
	 */
	public String getFilingEndDate() {
		return filingEndDate;
	}

	/**
	 * @param filingEndDate the filingEndDate to set
	 */
	public void setFilingEndDate(String filingEndDate) {
		this.filingEndDate = filingEndDate;
	}

	/**
	 * @return the returnPropertyName
	 */
	public String getReturnPropertyName() {
		return returnPropertyName;
	}

	/**
	 * @param returnPropertyName the returnPropertyName to set
	 */
	public void setReturnPropertyName(String returnPropertyName) {
		this.returnPropertyName = returnPropertyName;
	}

	/**
	 * @return the updateFromReturn
	 */
	public Boolean getUpdateFromReturn() {
		return updateFromReturn;
	}

	/**
	 * @param updateFromReturn the updateFromReturn to set
	 */
	public void setUpdateFromReturn(Boolean updateFromReturn) {
		this.updateFromReturn = updateFromReturn;
	}

	public Boolean getIsApplicable() {
		return isApplicable;
	}

	public void setIsApplicable(Boolean isApplicable) {
		this.isApplicable = isApplicable;
	}

	public Integer getGraceDays() {
		return graceDays;
	}

	public void setGraceDays(Integer graceDays) {
		this.graceDays = graceDays;
	}

	public String getFrequencyName() {
		return frequencyName;
	}

	public void setFrequencyName(String frequencyName) {
		this.frequencyName = frequencyName;
	}
}
