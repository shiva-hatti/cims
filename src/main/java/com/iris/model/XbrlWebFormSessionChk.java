package com.iris.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_XBRL_WEBFORM_SESSION_CHK")
public class XbrlWebFormSessionChk implements Serializable {

	private static final long serialVersionUID = -7009208067450930581L;

	@Id
	@Column(name = "XBRL_SESSION_CHK_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long xbrlSessionChkId;

	//@ManyToOne
	//@JoinColumn(name = "USER_ID_FK")
	@Column(name = "USER_ID_FK")
	private Long userIdFk;

	@Column(name = "EXPIRATION_TIME")
	private Date expirationTime;

	@Column(name = "LOGIN_TIME")
	private Date loginTime;

	@Column(name = "LOGOUT_TIME")
	private Date logOutTIme;

	@Column(name = "IS_LOGGED_IN")
	private boolean isLoggedIn;

	public Long getXbrlSessionChkId() {
		return xbrlSessionChkId;
	}

	public void setXbrlSessionChkId(Long xbrlSessionChkId) {
		this.xbrlSessionChkId = xbrlSessionChkId;
	}

	public Long getUserIdFk() {
		return userIdFk;
	}

	public void setUserIdFk(Long userIdFk) {
		this.userIdFk = userIdFk;
	}

	public Date getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Date getLogOutTIme() {
		return logOutTIme;
	}

	public void setLogOutTIme(Date logOutTIme) {
		this.logOutTIme = logOutTIme;
	}

	public boolean getIsLoggedIn() {
		return isLoggedIn;
	}

	public void setIsLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

}
