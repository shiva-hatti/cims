package com.iris.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.iris.util.Validations;

/**
 * @author psawant
 * @version 1.0
 * @date 10/09/2020
 */
@Entity
@Table(name = "TBL_SAP_BO_DETAILS")
public class SapBoDetails implements Serializable {

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

	@Column(name = "LAST_UPDATED_ON")
	private Date updatedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_UPDATED_BY")
	private UserMaster updatedBy;

	@Column(name = "PERIOD_START_KEY")
	private String periodStartKey;

	@Column(name = "RETURN_CODE")
	private String returnCode;

	@Column(name = "IDENTIFIER")
	private String identifier;

	@Transient
	private String lastUploadedOn;

	@Transient
	private String sessionTimeFormat;

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

	public String getLastUploadedOn() {
		return lastUploadedOn;
	}

	public void setLastUploadedOn(String lastUploadedOn) {
		this.lastUploadedOn = Validations.trimInput(lastUploadedOn);
	}

	public String getSessionTimeFormat() {
		return sessionTimeFormat;
	}

	public void setSessionTimeFormat(String sessionTimeFormat) {
		this.sessionTimeFormat = Validations.trimInput(sessionTimeFormat);
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public UserMaster getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(UserMaster updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getPeriodStartKey() {
		return periodStartKey;
	}

	public void setPeriodStartKey(String periodStartKey) {
		this.periodStartKey = Validations.trimInput(periodStartKey);
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = Validations.trimInput(returnCode);
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = Validations.trimInput(identifier);
	}

}