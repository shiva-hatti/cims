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

@Entity
@Table(name = "TBL_RETURN_COUNTRY_MAPPING")
public class ReturnCountryMapping implements Serializable {
	private static final long serialVersionUID = 8162641139181260433L;

	@Id
	@Column(name = "RETURN_COUNTRY_MAP_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long returnCountryMapId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COUNTRY_ID_FK")
	private CountryMaster countryIdFk;

	@Column(name = "RETURN_CODE")
	private String returnCode;

	@Column(name = "IS_ACTIVE")
	private boolean isActive;

	public Long getReturnCountryMapId() {
		return returnCountryMapId;
	}

	public void setReturnCountryMapId(Long returnCountryMapId) {
		this.returnCountryMapId = returnCountryMapId;
	}

	public CountryMaster getCountryIdFk() {
		return countryIdFk;
	}

	public void setCountryIdFk(CountryMaster countryIdFk) {
		this.countryIdFk = countryIdFk;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
