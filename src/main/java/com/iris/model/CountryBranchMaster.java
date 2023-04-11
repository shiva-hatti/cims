package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "TBL_COUNTRY_BRANCH_MAPPING")
public class CountryBranchMaster implements Serializable {

	private static final long serialVersionUID = 8372455477866491318L;

	@Id
	@Column(name = "COUNTRY_BRANCH_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long countryBranchId;

	@ManyToOne
	@JoinColumn(name = "COUNTRY_ID_FK")
	private CountryMaster countryIdFk;

	@Column(name = "BRANCH_NAME")
	private String branchName;

	@Column(name = "BRANCH_CODE")
	private String branchCode;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	public Long getCountryBranchId() {
		return countryBranchId;
	}

	public void setCountryBranchId(Long countryBranchId) {
		this.countryBranchId = countryBranchId;
	}

	public CountryMaster getCountryIdFk() {
		return countryIdFk;
	}

	public void setCountryIdFk(CountryMaster countryIdFk) {
		this.countryIdFk = countryIdFk;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
}
