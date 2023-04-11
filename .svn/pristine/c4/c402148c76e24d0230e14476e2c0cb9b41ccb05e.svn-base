package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.iris.util.AESV2;
import com.iris.util.UtilMaster;
import com.iris.util.constant.GeneralConstants;

/**
 * This is the EmailSetting bean class with Hibernate mapping.
 * 
 * @author Amruta Kadam
 * @version 1.0
 * @since 26-11-2015
 */
@Entity
@Table(name = "TBL_EMAIL_SETTING")
public class EmailSetting implements Serializable {

	private static final long serialVersionUID = 2222620701645250202L;
	private static Logger LOGGER = LogManager.getLogger(EmailSetting.class);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EMAIL_SETTING_ID")
	private long emailSettingId;

	@Column(name = "SERVER_NAME")
	private String serverName;

	@Column(name = "PORT")
	private String portNumber;

	@Column(name = "MAILBOX")
	private String mailBox;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "IS_SMTP_AUTHENTICATION")
	private boolean issmtpAuthentication;

	@Column(name = "IS_TLS_AUTHENTICATION")
	private boolean tlsAuthentication;

	@Column(name = "IS_SSL_AUTHENTICATION")
	private boolean slsAuthentication;

	/**
	 * @return the emailSettingId
	 */
	public long getEmailSettingId() {
		return emailSettingId;
	}

	/**
	 * @param emailSettingId the emailSettingId to set
	 */
	public void setEmailSettingId(long emailSettingId) {
		this.emailSettingId = emailSettingId;
	}

	/**
	 * @return the serverName
	 */
	public String getServerName() {
		if (!UtilMaster.isEmpty(serverName)) {
			try {
				return AESV2.getInstance().decrypt(serverName);
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
				return serverName;
			}
		} else {
			return serverName;
		}
	}

	/**
	 * @param serverName the serverName to set
	 */
	public void setServerName(String serverName) {
		if (!UtilMaster.isEmpty(serverName)) {
			try {
				this.serverName = AESV2.getInstance().encrypt(UtilMaster.trimInput(serverName));
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
				this.serverName = UtilMaster.trimInput(serverName);
			}
		} else {
			this.serverName = UtilMaster.trimInput(serverName);
		}
	}

	/**
	 * @return the portNumber
	 */
	public String getPortNumber() {
		if (!UtilMaster.isEmpty(portNumber)) {
			try {
				return AESV2.getInstance().decrypt(portNumber);
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
				return portNumber;
			}
		} else {
			return portNumber;
		}
	}

	/**
	 * @param portNumber the portNumber to set
	 */
	public void setPortNumber(String portNumber) {
		if (!UtilMaster.isEmpty(portNumber)) {
			try {
				this.portNumber = AESV2.getInstance().encrypt(UtilMaster.trimInput(portNumber));
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
				this.portNumber = UtilMaster.trimInput(portNumber);
			}
		} else {
			this.portNumber = UtilMaster.trimInput(portNumber);
		}
	}

	/**
	 * @return the mailBox
	 */
	public String getMailBox() {
		if (!UtilMaster.isEmpty(mailBox)) {
			try {
				return AESV2.getInstance().decrypt(mailBox);
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
				return mailBox;
			}
		} else {
			return mailBox;
		}
	}

	/**
	 * @param mailBox the mailBox to set
	 */
	public void setMailBox(String mailBox) {
		if (!UtilMaster.isEmpty(mailBox)) {
			try {
				this.mailBox = AESV2.getInstance().encrypt(UtilMaster.trimInput(mailBox));
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
				this.mailBox = UtilMaster.trimInput(mailBox);
			}
		} else {
			this.mailBox = UtilMaster.trimInput(mailBox);
		}
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		if (!UtilMaster.isEmpty(password)) {
			try {
				return AESV2.getInstance().decrypt(password);
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
				return password;
			}
		} else {
			return password;
		}
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		if (!UtilMaster.isEmpty(password)) {
			try {
				this.password = AESV2.getInstance().encrypt(UtilMaster.trimInput(password));
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
				this.password = UtilMaster.trimInput(password);
			}
		} else {
			this.password = UtilMaster.trimInput(password);
		}
	}

	/**
	 * @return the issmtpAuthentication
	 */
	public boolean isIssmtpAuthentication() {
		return issmtpAuthentication;
	}

	/**
	 * @param issmtpAuthentication the issmtpAuthentication to set
	 */
	public void setIssmtpAuthentication(boolean issmtpAuthentication) {
		this.issmtpAuthentication = issmtpAuthentication;
	}

	/**
	 * @return the tlsAuthentication
	 */
	public boolean isTlsAuthentication() {
		return tlsAuthentication;
	}

	/**
	 * @param tlsAuthentication the tlsAuthentication to set
	 */
	public void setTlsAuthentication(boolean tlsAuthentication) {
		this.tlsAuthentication = tlsAuthentication;
	}

	/**
	 * @return the slsAuthentication
	 */
	public boolean isSlsAuthentication() {
		return slsAuthentication;
	}

	/**
	 * @param slsAuthentication the slsAuthentication to set
	 */
	public void setSlsAuthentication(boolean slsAuthentication) {
		this.slsAuthentication = slsAuthentication;
	}

}