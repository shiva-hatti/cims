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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * This class is bean class for Email Unsubscribe Notification
 * 
 * @author nsasane
 * @date 27/09/16
 * @version 1.0
 */

@Entity
@Table(name = "TBL_EMAIL_UNSUBSCRIBE")
public class EmailUnsubscribe implements Serializable {
	private static final long serialVersionUID = -7441773167576173379L;

	@Id
	/*@SequenceGenerator(name = "EMAIL_UNSUBSCRIBE_ID_GENERATOR", sequenceName = "TBL_EMAIL_UNSUBSCRIBE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="EMAIL_UNSUBSCRIBE_ID_GENERATOR")*/
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EMAIL_UNSUBSCRIBE_ID")
	private long emailUnsubscribeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ROLE_MASTER_ID_FK")
	private UserRoleMaster userRoleMstrIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMAIL_ALERT_ID_FK")
	private EmailAlert emailAlertIdFk;

	@Column(name = "IS_EMAIL_UNSUBSCRIBE")
	private Boolean isEmailUnsubscribe;

	/**
	 * @return the emailUnsubscribeId
	 */
	public long getEmailUnsubscribeId() {
		return emailUnsubscribeId;
	}

	/**
	 * @param emailUnsubscribeId the emailUnsubscribeId to set
	 */
	public void setEmailUnsubscribeId(long emailUnsubscribeId) {
		this.emailUnsubscribeId = emailUnsubscribeId;
	}

	/**
	 * @return the userRoleMstrIdFk
	 */
	public UserRoleMaster getUserRoleMstrIdFk() {
		return userRoleMstrIdFk;
	}

	/**
	 * @param userRoleMstrIdFk the userRoleMstrIdFk to set
	 */
	public void setUserRoleMstrIdFk(UserRoleMaster userRoleMstrIdFk) {
		this.userRoleMstrIdFk = userRoleMstrIdFk;
	}

	/**
	 * @return the emailAlertIdFk
	 */
	public EmailAlert getEmailAlertIdFk() {
		return emailAlertIdFk;
	}

	/**
	 * @param emailAlertIdFk the emailAlertIdFk to set
	 */
	public void setEmailAlertIdFk(EmailAlert emailAlertIdFk) {
		this.emailAlertIdFk = emailAlertIdFk;
	}

	/**
	 * @return the isEmailUnsubscribe
	 */
	public Boolean getIsEmailUnsubscribe() {
		return isEmailUnsubscribe;
	}

	/**
	 * @param isEmailUnsubscribe the isEmailUnsubscribe to set
	 */
	public void setIsEmailUnsubscribe(Boolean isEmailUnsubscribe) {
		this.isEmailUnsubscribe = isEmailUnsubscribe;
	}

}
