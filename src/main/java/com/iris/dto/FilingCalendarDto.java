/**
 * 
 */
package com.iris.dto;

import java.io.Serializable;

import com.iris.util.constant.UploadFilingConstants;

/**
 * @author sajadhav
 *
 */
public class FilingCalendarDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8297235556923962124L;

	private String startDate;

	private String endDate;

	private UploadFilingConstants filingStatus;

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the filingStatus
	 */
	public UploadFilingConstants getFilingStatus() {
		return filingStatus;
	}

	/**
	 * @param filingStatus the filingStatus to set
	 */
	public void setFilingStatus(UploadFilingConstants filingStatus) {
		this.filingStatus = filingStatus;
	}

}
