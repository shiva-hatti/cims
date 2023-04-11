package com.iris.dto;

import java.io.Serializable;

import com.iris.util.Validations;

public class ReturnCustomDateInputDto implements Serializable {
	private static final long serialVersionUID = 4168582779031042261L;

	private String returnCode;
	private String reportingStartDate;
	private String reportingEndDate;
	private String dateFormat;
	private String requestFlag;

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = Validations.trimInput(returnCode);
	}

	public String getReportingStartDate() {
		return reportingStartDate;
	}

	public void setReportingStartDate(String reportingStartDate) {
		this.reportingStartDate = Validations.trimInput(reportingStartDate);
	}

	public String getReportingEndDate() {
		return reportingEndDate;
	}

	public void setReportingEndDate(String reportingEndDate) {
		this.reportingEndDate = Validations.trimInput(reportingEndDate);
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = Validations.trimInput(dateFormat);
	}

	/**
	 * @return the requestFlag
	 */
	public String getRequestFlag() {
		return requestFlag;
	}

	/**
	 * @param requestFlag the requestFlag to set
	 */
	public void setRequestFlag(String requestFlag) {
		this.requestFlag = requestFlag;
	}

}
