package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_RETURN_CUSTOMS_DATE")
public class ReturnCustomDate implements Serializable {

	private static final long serialVersionUID = 4640729934026994437L;

	@Id
	@Column(name = "RETURN_CUSTOM_DATE_PK")
	private Integer returnCustomDatePk;

	@Column(name = "CUSTOM_DATE_JSON")
	private String customDateJson;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_ID_FK")
	private Return returnIdFk;

	@Column(name = "DAYS")
	private Long days;

	public Integer getReturnCustomDatePk() {
		return returnCustomDatePk;
	}

	public void setReturnCustomDatePk(Integer returnCustomDatePk) {
		this.returnCustomDatePk = returnCustomDatePk;
	}

	public Return getReturnIdFk() {
		return returnIdFk;
	}

	public void setReturnIdFk(Return returnIdFk) {
		this.returnIdFk = returnIdFk;
	}

	/**
	 * @return the customDateJson
	 */
	public String getCustomDateJson() {
		return customDateJson;
	}

	/**
	 * @param customDateJson the customDateJson to set
	 */
	public void setCustomDateJson(String customDateJson) {
		this.customDateJson = customDateJson;
	}

	/**
	 * @return the days
	 */
	public Long getDays() {
		return days;
	}

	/**
	 * @param days the days to set
	 */
	public void setDays(Long days) {
		this.days = days;
	}
}
