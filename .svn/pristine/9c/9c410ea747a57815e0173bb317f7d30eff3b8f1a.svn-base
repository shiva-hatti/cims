/**
 * 
 */
package com.iris.nbfc.model;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.iris.model.UserMaster;

/**
 * @author Siddique
 *
 */
@Entity
@Table(name = "TBL_COR_REGISTRATION")
public class NbfcCorRegistrationBean implements Serializable {

	private static final long serialVersionUID = 1751018217528327764L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "COR_REGISTRATION_ID")
	private Long corRegistrationId;

	@Column(name = "COMPANY_PAN")
	private String compPan;

	@Column(name = "COMPANY_NAME")
	private String compName;

	@Column(name = "COMPANY_EMAILID")
	private String compEmailId;

	@Column(name = "COMPANY_PHONE")
	private String compPhone;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "OTP")
	private String otp;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REGISTER_ON")
	private Date registeredOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USERID_FK")
	private UserMaster user;

	@Column(name = "STATUS")
	private String status;

	@Transient
	private String verificationUrl;

	public String getVerificationUrl() {
		return verificationUrl;
	}

	public void setVerificationUrl(String verificationUrl) {
		this.verificationUrl = verificationUrl;
	}

	public Long getCorRegistrationId() {
		return corRegistrationId;
	}

	public void setCorRegistrationId(Long corRegistrationId) {
		this.corRegistrationId = corRegistrationId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCompPan() {
		return compPan;
	}

	public void setCompPan(String compPan) {
		this.compPan = compPan;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getCompEmailId() {
		return compEmailId;
	}

	public void setCompEmailId(String compEmailId) {
		this.compEmailId = compEmailId;
	}

	public String getCompPhone() {
		return compPhone;
	}

	public void setCompPhone(String compPhone) {
		this.compPhone = compPhone;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getRegisteredOn() {
		return registeredOn;
	}

	public void setRegisteredOn(Date registeredOn) {
		this.registeredOn = registeredOn;
	}

	public UserMaster getUser() {
		return user;
	}

	public void setUser(UserMaster user) {
		this.user = user;
	}

}
