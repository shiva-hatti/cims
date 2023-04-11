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
 * This class is Email Sent history information maintaining bean class
 * 
 * @author Suman Kumar
 */
@Entity
@Table(name = "TBL_EMAIL_SENT_HISTORY")
public class EmailSentHistory implements Serializable {

	private static final long serialVersionUID = 8102482918572287235L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MAIL_SENT_HIST_ID")
	private Long mailSentHistId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID_FK")
	private UserMaster userMasterFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ROLE_ID_FK")
	private UserRole userRoleFk;

	@Column(name = "MAIL_SUBJECT")
	private String mailSubject;

	@Column(name = "MAIL_BODY")
	private String mailBody;

	@Column(name = "MAIL_RECIPIENTS")
	private String mailRecipients;

	@Column(name = "SENT_DATE_TIME")
	private Date sentDtTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MENU_ID_FK")
	private Menu menuFk;

	@Transient
	private String uniqueId;

	@Transient
	private Boolean isHistoryMaintained;

	@Transient
	private String reason;

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * @return the uniqueId
	 */
	public String getUniqueId() {
		return uniqueId;
	}

	/**
	 * @param uniqueId the uniqueId to set
	 */
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	/**
	 * @return the isHistoryMaintained
	 */
	public Boolean getIsHistoryMaintained() {
		return isHistoryMaintained;
	}

	/**
	 * @param isHistoryMaintained the isHistoryMaintained to set
	 */
	public void setIsHistoryMaintained(Boolean isHistoryMaintained) {
		this.isHistoryMaintained = isHistoryMaintained;
	}

	/**
	 * @return the mailSentHistId
	 */
	public Long getMailSentHistId() {
		return mailSentHistId;
	}

	/**
	 * @param mailSentHistId the mailSentHistId to set
	 */
	public void setMailSentHistId(Long mailSentHistId) {
		this.mailSentHistId = mailSentHistId;
	}

	/**
	 * @return the userMasterFk
	 */
	public UserMaster getUserMasterFk() {
		return userMasterFk;
	}

	/**
	 * @param userMasterFk the userMasterFk to set
	 */
	public void setUserMasterFk(UserMaster userMasterFk) {
		this.userMasterFk = userMasterFk;
	}

	/**
	 * @return the userRoleFk
	 */
	public UserRole getUserRoleFk() {
		return userRoleFk;
	}

	/**
	 * @param userRoleFk the userRoleFk to set
	 */
	public void setUserRoleFk(UserRole userRoleFk) {
		this.userRoleFk = userRoleFk;
	}

	/**
	 * @return the mailSubject
	 */
	public String getMailSubject() {
		return mailSubject;
	}

	/**
	 * @param mailSubject the mailSubject to set
	 */
	public void setMailSubject(String mailSubject) {
		this.mailSubject = Validations.trimInput(mailSubject);
	}

	/**
	 * @return the mailBody
	 */
	public String getMailBody() {
		return mailBody;
	}

	/**
	 * @param mailBody the mailBody to set
	 */
	public void setMailBody(String mailBody) {
		this.mailBody = Validations.trimInput(mailBody);
	}

	/**
	 * @return the sentDtTime
	 */
	public Date getSentDtTime() {
		return sentDtTime;
	}

	/**
	 * @param sentDtTime the sentDtTime to set
	 */
	public void setSentDtTime(Date sentDtTime) {
		this.sentDtTime = sentDtTime;
	}

	/**
	 * @return the menuFk
	 */
	public Menu getMenuFk() {
		return menuFk;
	}

	/**
	 * @param menuFk the menuFk to set
	 */
	public void setMenuFk(Menu menuFk) {
		this.menuFk = menuFk;
	}

	/**
	 * @return the mailRecipients
	 */
	public String getMailRecipients() {
		return mailRecipients;
	}

	/**
	 * @param mailRecipients the mailRecipients to set
	 */
	public void setMailRecipients(String mailRecipients) {
		this.mailRecipients = Validations.trimInput(mailRecipients);
	}

}