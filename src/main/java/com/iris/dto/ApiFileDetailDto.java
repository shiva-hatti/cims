/**
 * 
 */
package com.iris.dto;

/**
 * @author sikhan
 *
 */
public class ApiFileDetailDto {

	private String userName;
	private String password;
	private String entityCode;
	private String returnCode;
	private String reportingPeriodStartDate;
	private String reportingPeriodEndDate;
	private String base64EncodedStringValue;
	private String jwtToken;
	private String bankWorkingCode;
	private String frequency;
	private String base64DocStringValue;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the jwtToken
	 */
	public String getJwtToken() {
		return jwtToken;
	}

	/**
	 * @param jwtToken the jwtToken to set
	 */
	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	/**
	 * @return the base64DocStringValue
	 */
	public String getBase64DocStringValue() {
		return base64DocStringValue;
	}

	/**
	 * @param base64DocStringValue the base64DocStringValue to set
	 */
	public void setBase64DocStringValue(String base64DocStringValue) {
		this.base64DocStringValue = base64DocStringValue;
	}

	/**
	 * @return the bankWorkingCode
	 */
	public String getBankWorkingCode() {
		return bankWorkingCode;
	}

	/**
	 * @param bankWorkingCode the bankWorkingCode to set
	 */
	public void setBankWorkingCode(String bankWorkingCode) {
		this.bankWorkingCode = bankWorkingCode;
	}

	/**
	 * @return the frequency
	 */
	public String getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(String frequency) {
		this.frequency = frequency;
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
	 * @return the base64EncodedStringValue
	 */
	public String getBase64EncodedStringValue() {
		return base64EncodedStringValue;
	}

	/**
	 * @param base64EncodedStringValue the base64EncodedStringValue to set
	 */
	public void setBase64EncodedStringValue(String base64EncodedStringValue) {
		this.base64EncodedStringValue = base64EncodedStringValue;
	}

	/**
	 * @return the reportingPeriodStartDate
	 */
	public String getReportingPeriodStartDate() {
		return reportingPeriodStartDate;
	}

	/**
	 * @param reportingPeriodStartDate the reportingPeriodStartDate to set
	 */
	public void setReportingPeriodStartDate(String reportingPeriodStartDate) {
		this.reportingPeriodStartDate = reportingPeriodStartDate;
	}

	/**
	 * @return the reportingPeriodEndDate
	 */
	public String getReportingPeriodEndDate() {
		return reportingPeriodEndDate;
	}

	/**
	 * @param reportingPeriodEndDate the reportingPeriodEndDate to set
	 */
	public void setReportingPeriodEndDate(String reportingPeriodEndDate) {
		this.reportingPeriodEndDate = reportingPeriodEndDate;
	}

}
