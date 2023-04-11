package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.iris.util.Validations;

/**
 * @author Snehal
 *
 */
@Entity
@Table(name = "TBL_FIN_YR_FREQUENCY_DATES")
public class FinYearFreqDates implements Serializable {

	private static final long serialVersionUID = -744617226177042473L;

	@Id
	@Column(name = "FIN_YR_FREQUENCY_DATES_ID")
	private int finYrFreqDatesId;

	@Column(name = "FIN_YR_ID_FK_CK")
	private Long finYrId;

	@Column(name = "FIN_YR_FREQUENCY_DESC_ID_FK_CK")
	private Long finYrFreqId;

	@Column(name = "START_DATE")
	private String startDate;

	@Column(name = "END_DATE")
	private String endDate;

	/**
	 * @return the finYrId
	 */
	public Long getFinYrId() {
		return finYrId;
	}

	/**
	 * @param finYrId the finYrId to set
	 */
	public void setFinYrId(Long finYrId) {
		this.finYrId = finYrId;
	}

	/**
	 * @return the finYrFreqId
	 */
	public Long getFinYrFreqId() {
		return finYrFreqId;
	}

	/**
	 * @param finYrFreqId the finYrFreqId to set
	 */
	public void setFinYrFreqId(Long finYrFreqId) {
		this.finYrFreqId = finYrFreqId;
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

	/**
	 * @return the finYrFreqDatesId
	 */
	public int getFinYrFreqDatesId() {
		return finYrFreqDatesId;
	}

	/**
	 * @param finYrFreqDatesId the finYrFreqDatesId to set
	 */
	public void setFinYrFreqDatesId(int finYrFreqDatesId) {
		this.finYrFreqDatesId = finYrFreqDatesId;
	}

}
