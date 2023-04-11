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
@Table(name = "TBL_LEAD_BANK_MAPPING")
public class LeadBankMaster implements Serializable {

	private static final long serialVersionUID = -9179125889769472395L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "LEAD_BANK_ID")
	private Long leadBankId;

	@Column(name = "LEAD_BANK_NAME")
	private String leadBankName;

	@Column(name = "LEAD_BANK_CODE")
	private String leadBankCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_ID_FK")
	private Return returnObj;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	/**
	 * @return the leadBankId
	 */
	public Long getLeadBankId() {
		return leadBankId;
	}

	/**
	 * @param leadBankId the leadBankId to set
	 */
	public void setLeadBankId(Long leadBankId) {
		this.leadBankId = leadBankId;
	}

	/**
	 * @return the leadBankName
	 */
	public String getLeadBankName() {
		return leadBankName;
	}

	/**
	 * @param leadBankName the leadBankName to set
	 */
	public void setLeadBankName(String leadBankName) {
		this.leadBankName = leadBankName;
	}

	/**
	 * @return the leadBankCode
	 */
	public String getLeadBankCode() {
		return leadBankCode;
	}

	/**
	 * @param leadBankCode the leadBankCode to set
	 */
	public void setLeadBankCode(String leadBankCode) {
		this.leadBankCode = leadBankCode;
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
