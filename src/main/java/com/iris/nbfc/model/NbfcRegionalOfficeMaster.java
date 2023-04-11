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
@Table(name = "TBL_NBFC_REGIONAL_OFFICE")
public class NbfcRegionalOfficeMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NBFC_RO_ID")
	private int nbfcROId;

	@Column(name = "NBFC_RO_CODE")
	private String nbfcROCode;

	@Column(name = "NBFC_RO_NAME")
	private String nbfcROName;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the nbfcROId
	 */
	public int getNbfcROId() {
		return nbfcROId;
	}

	/**
	 * @param nbfcROId the nbfcROId to set
	 */
	public void setNbfcROId(int nbfcROId) {
		this.nbfcROId = nbfcROId;
	}

	/**
	 * @return the nbfcROCode
	 */
	public String getNbfcROCode() {
		return nbfcROCode;
	}

	/**
	 * @param nbfcROCode the nbfcROCode to set
	 */
	public void setNbfcROCode(String nbfcROCode) {
		this.nbfcROCode = nbfcROCode;
	}

	/**
	 * @return the nbfcROName
	 */
	public String getNbfcROName() {
		return nbfcROName;
	}

	/**
	 * @param nbfcROName the nbfcROName to set
	 */
	public void setNbfcROName(String nbfcROName) {
		this.nbfcROName = nbfcROName;
	}

}
