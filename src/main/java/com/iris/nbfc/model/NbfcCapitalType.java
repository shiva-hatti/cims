package com.iris.nbfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author LKumar
 */
@Entity
@Table(name = "TBL_NBFC_CAPITAL_TYPE")
public class NbfcCapitalType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CAPITAL_TYPE_ID")
	private Integer capitalTypeId;

	@Column(name = "CAPITAL_TYPE")
	private String capitalType;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	/**
	 * @return the capitalTypeId
	 */
	public Integer getCapitalTypeId() {
		return capitalTypeId;
	}

	/**
	 * @param capitalTypeId the capitalTypeId to set
	 */
	public void setCapitalTypeId(Integer capitalTypeId) {
		this.capitalTypeId = capitalTypeId;
	}

	public String getCapitalType() {
		return capitalType;
	}

	public void setCapitalType(String capitalType) {
		this.capitalType = capitalType;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}
