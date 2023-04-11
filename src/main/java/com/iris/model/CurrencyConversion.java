package com.iris.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author bthakare
 *
 */
@Entity
@Table(name = "TBL_CURRENCY_CONVERSION")
public class CurrencyConversion implements Serializable {

	private static final long serialVersionUID = -1443236477021873853L;
	@Id
	@Column(name = "CURRENCY_CONVERSION_ID")
	private Long currencyConversionId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CURRENCY_ID_FK")
	private Currency currencyIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IS_DEFAULT_CURRENCY_FK")
	private Currency isDefaultCurrencyFk;

	@Column(name = "CONVERSION_RATE")
	private float conversionRate;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY_FK")
	private UserMaster createdByFk;

	/**
	 * @return the currencyConversionId
	 */
	public Long getCurrencyConversionId() {
		return currencyConversionId;
	}

	/**
	 * @param currencyConversionId the currencyConversionId to set
	 */
	public void setCurrencyConversionId(Long currencyConversionId) {
		this.currencyConversionId = currencyConversionId;
	}

	/**
	 * @return the currencyIdFk
	 */
	public Currency getCurrencyIdFk() {
		return currencyIdFk;
	}

	/**
	 * @param currencyIdFk the currencyIdFk to set
	 */
	public void setCurrencyIdFk(Currency currencyIdFk) {
		this.currencyIdFk = currencyIdFk;
	}

	/**
	 * @return the isDefaultCurrencyFk
	 */
	public Currency getIsDefaultCurrencyFk() {
		return isDefaultCurrencyFk;
	}

	/**
	 * @param isDefaultCurrencyFk the isDefaultCurrencyFk to set
	 */
	public void setIsDefaultCurrencyFk(Currency isDefaultCurrencyFk) {
		this.isDefaultCurrencyFk = isDefaultCurrencyFk;
	}

	/**
	 * @return the conversionRate
	 */
	public float getConversionRate() {
		return conversionRate;
	}

	/**
	 * @param conversionRate the conversionRate to set
	 */
	public void setConversionRate(float conversionRate) {
		this.conversionRate = conversionRate;
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
	 * @return the createdByFk
	 */
	public UserMaster getCreatedByFk() {
		return createdByFk;
	}

	/**
	 * @param createdByFk the createdByFk to set
	 */
	public void setCreatedByFk(UserMaster createdByFk) {
		this.createdByFk = createdByFk;
	}
}
