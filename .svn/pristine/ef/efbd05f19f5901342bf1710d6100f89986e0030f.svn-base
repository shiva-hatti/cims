package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.iris.util.Validations;

/**
 * This class act as bean for EmailBody with hibernate mapping.
 * 
 * @author sshah
 * @Version 1.0
 */
@Entity
@Table(name = "TBL_EMAIL_BODY")
public class EmailBodyBean implements Serializable {

	private static final long serialVersionUID = -6589972967125624857L;
	@Id
	@Column(name = "EMAIL_BODY_ID")
	private long emailBodyId;

	@Column(name = "EMAIL_BODY_1")
	private String emailBody1;

	@Column(name = "EMAIL_BODY_2")
	private String emailBody2;

	@Column(name = "EMAIL_SUBJECT_1")
	private String emailSubject1;

	@Column(name = "EMAIL_SUBJECT_2")
	private String emailSubject2;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMAIL_ALERT_ID_FK")
	private EmailAlert emailAlertId;

	@Column(name = "IS_RTL_1")
	private Boolean isRtl1;

	@Column(name = "IS_RTL_2")
	private Boolean isRtl2;

	/**
	 * @return the emailBodyId
	 */
	public long getEmailBodyId() {
		return emailBodyId;
	}

	/**
	 * @param emailBodyId the emailBodyId to set
	 */
	public void setEmailBodyId(long emailBodyId) {
		this.emailBodyId = emailBodyId;
	}

	/**
	 * @return the emailBody1
	 */
	public String getEmailBody1() {
		return emailBody1;
	}

	/**
	 * @param emailBody1 the emailBody1 to set
	 */
	public void setEmailBody1(String emailBody1) {
		this.emailBody1 = Validations.trimInput(emailBody1);
	}

	/**
	 * @return the emailBody2
	 */
	public String getEmailBody2() {
		return emailBody2;
	}

	/**
	 * @param emailBody2 the emailBody2 to set
	 */
	public void setEmailBody2(String emailBody2) {
		this.emailBody2 = Validations.trimInput(emailBody2);
	}

	/**
	 * @return the emailSubject1
	 */
	public String getEmailSubject1() {
		return emailSubject1;
	}

	/**
	 * @param emailSubject1 the emailSubject1 to set
	 */
	public void setEmailSubject1(String emailSubject1) {
		this.emailSubject1 = Validations.trimInput(emailSubject1);
	}

	/**
	 * @return the emailSubject2
	 */
	public String getEmailSubject2() {
		return emailSubject2;
	}

	/**
	 * @param emailSubject2 the emailSubject2 to set
	 */
	public void setEmailSubject2(String emailSubject2) {
		this.emailSubject2 = Validations.trimInput(emailSubject2);
	}

	/**
	 * @return the emailAlertId
	 */
	public EmailAlert getEmailAlertId() {
		return emailAlertId;
	}

	/**
	 * @param emailAlertId the emailAlertId to set
	 */
	public void setEmailAlertId(EmailAlert emailAlertId) {
		this.emailAlertId = emailAlertId;
	}

	/**
	 * @return the isRtl1
	 */
	public Boolean getIsRtl1() {
		return isRtl1;
	}

	/**
	 * @param isRtl1 the isRtl1 to set
	 */
	public void setIsRtl1(Boolean isRtl1) {
		this.isRtl1 = isRtl1;
	}

	/**
	 * @return the isRtl2
	 */
	public Boolean getIsRtl2() {
		return isRtl2;
	}

	/**
	 * @param isRtl2 the isRtl2 to set
	 */
	public void setIsRtl2(Boolean isRtl2) {
		this.isRtl2 = isRtl2;
	}

}