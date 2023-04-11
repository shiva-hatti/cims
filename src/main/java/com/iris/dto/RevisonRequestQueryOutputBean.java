/**
 * 
 */
package com.iris.dto;

import java.util.Date;

import javax.persistence.Column;

/**
 * @author apagaria
 *
 */
public class RevisonRequestQueryOutputBean {

	private Long entityId;

	private String entityCode;

	private String entityName;

	private Long categoryId;

	private String categoryCode;

	private String categoryName;

	private Long subCategoryId;

	private String subCategoryCode;

	private String subCategoryName;

	private Long returnId;

	private String returnCode;

	private String returnName;

	private Long userId;

	private String userName;

	private Date createdOn;

	private Long freqId;

	private String freqName;

	private String freqDesc;

	private Long year;

	private Integer month;

	private Date reportingDate;

	private Date startDate;

	private Date endDate;

	private String reasonForRequest;

	private String reasonForRejection;

	private Long revisionRequestId;

	private String returnPropValue;

	private String finYrFrquencyDesc;

	private Integer adminStatusIdFk;

	/**
	 * @param entityId
	 * @param entityCode
	 * @param entityName
	 * @param categoryId
	 * @param categoryCode
	 * @param categoryName
	 * @param subCategoryId
	 * @param subCategoryCode
	 * @param subCategoryName
	 * @param returnId
	 * @param returnCode
	 * @param returnName
	 * @param userId
	 * @param userName
	 * @param createdOn
	 * @param freqId
	 * @param freqName
	 * @param freqDesc
	 * @param year
	 * @param month
	 * @param reportingDate
	 * @param startDate
	 * @param endDate
	 * @param reasonForRequest
	 * @param reasonForRejection
	 * @param revisionRequestId
	 * @param returnPropValue
	 * @param finYrFrquencyDesc
	 * @param adminStatusIdFk
	 */
	public RevisonRequestQueryOutputBean(Long entityId, String entityCode, String entityName, Long categoryId, String categoryCode, String categoryName, Long subCategoryId, String subCategoryCode, String subCategoryName, Long returnId, String returnCode, String returnName, Long userId, String userName, Date createdOn, Long freqId, String freqName, String freqDesc, Long year, Integer month, Date reportingDate, Date startDate, Date endDate, String reasonForRequest, String reasonForRejection, Long revisionRequestId, String returnPropValue, String finYrFrquencyDesc, Integer adminStatusIdFk) {
		super();
		this.entityId = entityId;
		this.entityCode = entityCode;
		this.entityName = entityName;
		this.categoryId = categoryId;
		this.categoryCode = categoryCode;
		this.categoryName = categoryName;
		this.subCategoryId = subCategoryId;
		this.subCategoryCode = subCategoryCode;
		this.subCategoryName = subCategoryName;
		this.returnId = returnId;
		this.returnCode = returnCode;
		this.returnName = returnName;
		this.userId = userId;
		this.userName = userName;
		this.createdOn = createdOn;
		this.freqId = freqId;
		this.freqName = freqName;
		this.freqDesc = freqDesc;
		this.year = year;
		this.month = month;
		this.reportingDate = reportingDate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.reasonForRequest = reasonForRequest;
		this.reasonForRejection = reasonForRejection;
		this.revisionRequestId = revisionRequestId;
		this.returnPropValue = returnPropValue;
		this.finYrFrquencyDesc = finYrFrquencyDesc;
		this.adminStatusIdFk = adminStatusIdFk;
	}

	/**
	 * @param entityId
	 * @param entityCode
	 * @param entityName
	 * @param categoryId
	 * @param categoryCode
	 * @param categoryName
	 * @param subCategoryId
	 * @param subCategoryCode
	 * @param subCategoryName
	 * @param returnId
	 * @param returnCode
	 * @param returnName
	 * @param userId
	 * @param userName
	 * @param createdOn
	 * @param freqId
	 * @param freqName
	 * @param freqDesc
	 * @param year
	 * @param month
	 * @param reportingDate
	 * @param startDate
	 * @param endDate
	 * @param reasonForRequest
	 * @param reasonForRejection
	 * @param revisionRequestId
	 * @param finYrFrquencyDesc
	 * @param adminStatusIdFk
	 */
	public RevisonRequestQueryOutputBean(Long entityId, String entityCode, String entityName, Long categoryId, String categoryCode, String categoryName, Long subCategoryId, String subCategoryCode, String subCategoryName, Long returnId, String returnCode, String returnName, Long userId, String userName, Date createdOn, Long freqId, String freqName, String freqDesc, Long year, Integer month, Date reportingDate, Date startDate, Date endDate, String reasonForRequest, String reasonForRejection, Long revisionRequestId, String finYrFrquencyDesc, Integer adminStatusIdFk) {
		super();
		this.entityId = entityId;
		this.entityCode = entityCode;
		this.entityName = entityName;
		this.categoryId = categoryId;
		this.categoryCode = categoryCode;
		this.categoryName = categoryName;
		this.subCategoryId = subCategoryId;
		this.subCategoryCode = subCategoryCode;
		this.subCategoryName = subCategoryName;
		this.returnId = returnId;
		this.returnCode = returnCode;
		this.returnName = returnName;
		this.userId = userId;
		this.userName = userName;
		this.createdOn = createdOn;
		this.freqId = freqId;
		this.freqName = freqName;
		this.freqDesc = freqDesc;
		this.year = year;
		this.month = month;
		this.reportingDate = reportingDate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.reasonForRequest = reasonForRequest;
		this.reasonForRejection = reasonForRejection;
		this.revisionRequestId = revisionRequestId;
		this.finYrFrquencyDesc = finYrFrquencyDesc;
		this.adminStatusIdFk = adminStatusIdFk;
	}

	/**
	 * @return the entityId
	 */
	public Long getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return the entityCode
	 */
	public String getEntityCode() {
		return entityCode;
	}

	/**
	 * @param entityCode the entityCode to set
	 */
	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	/**
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * @param entityName the entityName to set
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	/**
	 * @return the categoryId
	 */
	public Long getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the categoryCode
	 */
	public String getCategoryCode() {
		return categoryCode;
	}

	/**
	 * @param categoryCode the categoryCode to set
	 */
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * @param categoryName the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/**
	 * @return the subCategoryId
	 */
	public Long getSubCategoryId() {
		return subCategoryId;
	}

	/**
	 * @param subCategoryId the subCategoryId to set
	 */
	public void setSubCategoryId(Long subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	/**
	 * @return the subCategoryCode
	 */
	public String getSubCategoryCode() {
		return subCategoryCode;
	}

	/**
	 * @param subCategoryCode the subCategoryCode to set
	 */
	public void setSubCategoryCode(String subCategoryCode) {
		this.subCategoryCode = subCategoryCode;
	}

	/**
	 * @return the subCategoryName
	 */
	public String getSubCategoryName() {
		return subCategoryName;
	}

	/**
	 * @param subCategoryName the subCategoryName to set
	 */
	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

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
	 * @return the returnCode
	 */
	public String getReturnCode() {
		return returnCode;
	}

	/**
	 * @param returnCode the returnCode to set
	 */
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	/**
	 * @return the returnName
	 */
	public String getReturnName() {
		return returnName;
	}

	/**
	 * @param returnName the returnName to set
	 */
	public void setReturnName(String returnName) {
		this.returnName = returnName;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
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
	 * @return the freqId
	 */
	public Long getFreqId() {
		return freqId;
	}

	/**
	 * @param freqId the freqId to set
	 */
	public void setFreqId(Long freqId) {
		this.freqId = freqId;
	}

	/**
	 * @return the freqName
	 */
	public String getFreqName() {
		return freqName;
	}

	/**
	 * @param freqName the freqName to set
	 */
	public void setFreqName(String freqName) {
		this.freqName = freqName;
	}

	/**
	 * @return the freqdesc
	 */
	public String getFreqDesc() {
		return freqDesc;
	}

	/**
	 * @param freqdesc the freqdesc to set
	 */
	public void setFreqDesc(String freqDesc) {
		this.freqDesc = freqDesc;
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
	 * @return the reasonForRequest
	 */
	public String getReasonForRequest() {
		return reasonForRequest;
	}

	/**
	 * @param reasonForRequest the reasonForRequest to set
	 */
	public void setReasonForRequest(String reasonForRequest) {
		this.reasonForRequest = reasonForRequest;
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
		this.reasonForRejection = reasonForRejection;
	}

	/**
	 * @return the revisionRequestId
	 */
	public Long getRevisionRequestId() {
		return revisionRequestId;
	}

	/**
	 * @param revisionRequestId the revisionRequestId to set
	 */
	public void setRevisionRequestId(Long revisionRequestId) {
		this.revisionRequestId = revisionRequestId;
	}

	/**
	 * @return the returnPropValue
	 */
	public String getReturnPropValue() {
		return returnPropValue;
	}

	/**
	 * @param returnPropValue the returnPropValue to set
	 */
	public void setReturnPropValue(String returnPropValue) {
		this.returnPropValue = returnPropValue;
	}

	/**
	 * @return the finYrFrquencyDesc
	 */
	public String getFinYrFrquencyDesc() {
		return finYrFrquencyDesc;
	}

	/**
	 * @param finYrFrquencyDesc the finYrFrquencyDesc to set
	 */
	public void setFinYrFrquencyDesc(String finYrFrquencyDesc) {
		this.finYrFrquencyDesc = finYrFrquencyDesc;
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
}
