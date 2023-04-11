package com.iris.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.iris.util.Validations;

/**
 * this class is bean class for currency module
 * 
 * @author psawant
 * @date 13/08/2015
 */
@Entity
@Table(name = "TBL_CURRENCY")
public class Currency implements Serializable {

	private static final long serialVersionUID = 6360297871320003830L;

	@Id
	@Column(name = "CURRENCY_ID")
	private Long currencyId;

	@Column(name = "CURRENCY_SHORT_NAME")
	private String currencyShortName;

	@Column(name = "CURRENCY_ISO_CODE")
	private String currencyISOCode;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "IS_DEFAULT")
	private boolean isDefault;

	@OneToMany(mappedBy = "currencyIdFk")
	private Set<CurrencyLabel> currLblSet;

	@Column(name = "CURRENCY_NAME")
	private String currencyName;

	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Long getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Long currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurrencyISOCode() {
		return currencyISOCode;
	}

	public void setCurrencyISOCode(String currencyISOCode) {
		this.currencyISOCode = Validations.trimInput(currencyISOCode);
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getCurrencyShortName() {
		return currencyShortName;
	}

	public void setCurrencyShortName(String currencyShortName) {
		this.currencyShortName = Validations.trimInput(currencyShortName);
	}

	public Set<CurrencyLabel> getCurrLblSet() {
		return currLblSet;
	}

	public void setCurrLblSet(Set<CurrencyLabel> currLblSet) {
		this.currLblSet = currLblSet;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = Validations.trimInput(currencyName);
	}

}