package com.iris.nbfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author pmohite
 */
@Entity
@Table(name = "TBL_NBFC_COMPANY_CLASS")
public class NbfcCompanyClass {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "COMPANY_CLASS_ID")
	private int companyClassId;

	@Column(name = "COMPANY_CLASS")
	private String companyClass;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the companyClassId
	 */
	public int getCompanyClassId() {
		return companyClassId;
	}

	/**
	 * @param companyClassId the companyClassId to set
	 */
	public void setCompanyClassId(int companyClassId) {
		this.companyClassId = companyClassId;
	}

	/**
	 * @return the companyClass
	 */
	public String getCompanyClass() {
		return companyClass;
	}

	/**
	 * @param companyClass the companyClass to set
	 */
	public void setCompanyClass(String companyClass) {
		this.companyClass = companyClass;
	}

}
