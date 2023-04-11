package com.iris.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * MailInfoBean class helps MailUtil class to hold the information regarding following:<br>
 * senderEmail; senderPasswd; host; port; body; subject; recipientIds; ccMailIds; attachments;
 * 
 * @author Sagar Desai
 * @version 1.0
 * @see MailUtil
 * @see AttachmentInfoBean
 */

/*
 * ******************************VERSION HISTORY*********************************
 * Version		Author			Date		Description
 * ------------------------------------------------------------------------------
 * version 1.0	Sagar Desai		14-07-2014	Base version
 * version 1.1	Suman Kumar		29-12-2015	Added BCC variables and XML Root Element annotation
 * ------------------------------------------------------------------------------
 */
@XmlRootElement(name = "MailInfoBean")
public class MailInfoBean {

	private String senderEmail;
	private String senderPasswd;
	private String host;
	private String port;
	private boolean issmtpAuthentication;
	private boolean isTLSAuthentication;
	private boolean isSSLAuthentication;
	private String sslPort;

	private String body;
	private String subject;
	private String[] recipientIds;
	private String[] ccMailIds;
	private String[] bccMailIds;
	private AttachmentInfoBean[] attachments;

	/**
	 * @return the senderEmail
	 */
	public String getSenderEmail() {
		return senderEmail;
	}

	/**
	 * @param senderEmail the Sender Email-Id to set
	 */
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	/**
	 * @return the senderPasswd
	 */
	public String getSenderPasswd() {
		return senderPasswd;
	}

	/**
	 * @param senderPasswd the Sender Password to set
	 */
	public void setSenderPasswd(String senderPasswd) {
		this.senderPasswd = senderPasswd;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body the body of corresponding email to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the subject of corresponding email
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject of corresponding email to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the recipientIds
	 */
	public String[] getRecipientIds() {
		return recipientIds;
	}

	/**
	 * @param recipientIds the recipient email Ids to set
	 */
	public void setRecipientIds(String[] recipientIds) {
		this.recipientIds = recipientIds;
	}

	/**
	 * @return the ccMailIds
	 */
	public String[] getCcMailIds() {
		return ccMailIds;
	}

	/**
	 * @param ccMailIds the ccMail Ids to set
	 */
	public void setCcMailIds(String[] ccMailIds) {
		this.ccMailIds = ccMailIds;
	}

	/**
	 * @return the bccMailIds
	 */
	public String[] getBccMailIds() {
		return bccMailIds;
	}

	/**
	 * @param bccMailIds the bccMailIds to set
	 */
	public void setBccMailIds(String[] bccMailIds) {
		this.bccMailIds = bccMailIds;
	}

	/**
	 * @return the attachments
	 */
	public AttachmentInfoBean[] getAttachments() {
		return attachments;
	}

	/**
	 * User should use AttachmentInfoBean class to add attachment
	 * 
	 * @param attachments the attachments to set
	 * @see AttachmentInfoBean
	 */
	public void setAttachments(AttachmentInfoBean[] attachments) {
		this.attachments = attachments;
	}

	public boolean isIssmtpAuthentication() {
		return issmtpAuthentication;
	}

	public void setIssmtpAuthentication(boolean issmtpAuthentication) {
		this.issmtpAuthentication = issmtpAuthentication;
	}

	public boolean isTLSAuthentication() {
		return isTLSAuthentication;
	}

	public void setIsTLSAuthentication(boolean isTLSAuthentication) {
		this.isTLSAuthentication = isTLSAuthentication;
	}

	public boolean isSSLAuthentication() {
		return isSSLAuthentication;
	}

	public void setIsSSLAuthentication(boolean isSSLAuthentication) {
		this.isSSLAuthentication = isSSLAuthentication;
	}

	public String getSslPort() {
		return sslPort;
	}

	public void setSslPort(String sslPort) {
		this.sslPort = sslPort;
	}

}
