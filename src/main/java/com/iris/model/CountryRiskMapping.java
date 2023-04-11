package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Shivabasava Hatti
 *
 */
@Entity
@Table(name = "TBL_COUNTRY_RISK_MAPPING")
public class CountryRiskMapping implements Serializable {

	private static final long serialVersionUID = 7627133575044013329L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "COUNTRY_RISK_ID")
	private Long countryRiskId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_ID_FK")
	private Return returnObj;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COUNTRY_ID_FK")
	private CountryMaster countryIdFk;

	@Column(name = "RISK_CLASSIFICATION")
	private String riskClassification;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	/**
	 * @return the countryRiskId
	 */
	public Long getCountryRiskId() {
		return countryRiskId;
	}

	/**
	 * @param countryRiskId the countryRiskId to set
	 */
	public void setCountryRiskId(Long countryRiskId) {
		this.countryRiskId = countryRiskId;
	}

	/**
	 * @return the returnObj
	 */
	public Return getReturnObj() {
		return returnObj;
	}

	/**
	 * @param returnObj the returnObj to set
	 */
	public void setReturnObj(Return returnObj) {
		this.returnObj = returnObj;
	}

	/**
	 * @return the countryIdFk
	 */
	public CountryMaster getCountryIdFk() {
		return countryIdFk;
	}

	/**
	 * @param countryIdFk the countryIdFk to set
	 */
	public void setCountryIdFk(CountryMaster countryIdFk) {
		this.countryIdFk = countryIdFk;
	}

	/**
	 * @return the riskClassification
	 */
	public String getRiskClassification() {
		return riskClassification;
	}

	/**
	 * @param riskClassification the riskClassification to set
	 */
	public void setRiskClassification(String riskClassification) {
		this.riskClassification = riskClassification;
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
}
