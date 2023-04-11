package com.iris.dto;

import java.io.Serializable;

import com.iris.util.Validations;

public class FillingEndDatesBean implements Serializable {

	private static final long serialVersionUID = 5717044962724686398L;

	private int fillingEndDatesBeanId;
	private String startDate = null;
	private String endDate = null;
	private String graceDaysDate = null;
	private String startTimeFirstHalf;
	private String endTimeFirstHalf;
	private String startTimeSecondHalf;
	private String endTimeSecondHalf;

	public String getGraceDaysDate() {
		return graceDaysDate;
	}

	public void setGraceDaysDate(String graceDayDate) {
		this.graceDaysDate = graceDayDate;
	}

	/**
	 * @return the startTimeFirstHalf
	 */
	public String getStartTimeFirstHalf() {
		return startTimeFirstHalf;
	}

	/**
	 * @param startTimeFirstHalf the startTimeFirstHalf to set
	 */
	public void setStartTimeFirstHalf(String startTimeFirstHalf) {
		this.startTimeFirstHalf = Validations.trimInput(startTimeFirstHalf);
	}

	/**
	 * @return the endTimeFirstHalf
	 */
	public String getEndTimeFirstHalf() {
		return endTimeFirstHalf;
	}

	/**
	 * @param endTimeFirstHalf the endTimeFirstHalf to set
	 */
	public void setEndTimeFirstHalf(String endTimeFirstHalf) {
		this.endTimeFirstHalf = Validations.trimInput(endTimeFirstHalf);
	}

	/**
	 * @return the startTimeSecondHalf
	 */
	public String getStartTimeSecondHalf() {
		return startTimeSecondHalf;
	}

	/**
	 * @param startTimeSecondHalf the startTimeSecondHalf to set
	 */
	public void setStartTimeSecondHalf(String startTimeSecondHalf) {
		this.startTimeSecondHalf = Validations.trimInput(startTimeSecondHalf);
	}

	/**
	 * @return the endTimeSecondHalf
	 */
	public String getEndTimeSecondHalf() {
		return endTimeSecondHalf;
	}

	/**
	 * @param endTimeSecondHalf the endTimeSecondHalf to set
	 */
	public void setEndTimeSecondHalf(String endTimeSecondHalf) {
		this.endTimeSecondHalf = Validations.trimInput(endTimeSecondHalf);
	}

	/**
	 * @return the fillingEndDatesBeanId
	 */
	public int getFillingEndDatesBeanId() {
		return fillingEndDatesBeanId;
	}

	/**
	 * @param fillingEndDatesBeanId the fillingEndDatesBeanId to set
	 */
	public void setFillingEndDatesBeanId(int fillingEndDatesBeanId) {
		this.fillingEndDatesBeanId = fillingEndDatesBeanId;
	}

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
		this.startDate = Validations.trimInput(startDate);
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
		this.endDate = Validations.trimInput(endDate);
	}

}
