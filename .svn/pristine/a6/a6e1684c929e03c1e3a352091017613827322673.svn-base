package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.iris.util.Validations;

/**
 * @author sajadhav
 * @version 1.0
 * @date 27/01/2021
 */
@Entity
@Table(name = "TBL_SAP_BO_DETAILS_EBR_PILOT")
public class SapBoEBRPilotDetails implements Serializable {

	private static final long serialVersionUID = 8369476085524984362L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SAP_BO_DETAILS_ID")
	private Integer sapBoDetailsId;

	@Column(name = "USERNAME")
	private String username;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "AUTHENTICATE_URL")
	private String authenticateUrl;

	@Column(name = "REPORT_URL")
	private String reportUrl;

	@Column(name = "IDOC_ID_KEY")
	private String iDocIdKey;

	@Column(name = "PERIOD_ENDED_KEY")
	private String periodEndedKey;

	@Column(name = "BANK_NAME_KEY")
	private String bankNameKey;

	@Column(name = "SER_SESSION_KEY")
	private String serSessionKey;

	public Integer getSapBoDetailsId() {
		return sapBoDetailsId;
	}

	public void setSapBoDetailsId(Integer sapBoDetailsId) {
		this.sapBoDetailsId = sapBoDetailsId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = Validations.trimInput(username);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = Validations.trimInput(password);
	}

	public String getAuthenticateUrl() {
		return authenticateUrl;
	}

	public void setAuthenticateUrl(String authenticateUrl) {
		this.authenticateUrl = Validations.trimInput(authenticateUrl);
	}

	public String getReportUrl() {
		return reportUrl;
	}

	public void setReportUrl(String reportUrl) {
		this.reportUrl = Validations.trimInput(reportUrl);
	}

	public String getiDocIdKey() {
		return iDocIdKey;
	}

	public void setiDocIdKey(String iDocIdKey) {
		this.iDocIdKey = Validations.trimInput(iDocIdKey);
	}

	public String getPeriodEndedKey() {
		return periodEndedKey;
	}

	public void setPeriodEndedKey(String periodEndedKey) {
		this.periodEndedKey = Validations.trimInput(periodEndedKey);
	}

	public String getBankNameKey() {
		return bankNameKey;
	}

	public void setBankNameKey(String bankNameKey) {
		this.bankNameKey = Validations.trimInput(bankNameKey);
	}

	public String getSerSessionKey() {
		return serSessionKey;
	}

	public void setSerSessionKey(String serSessionKey) {
		this.serSessionKey = Validations.trimInput(serSessionKey);
	}

}