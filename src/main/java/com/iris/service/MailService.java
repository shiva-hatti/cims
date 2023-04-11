package com.iris.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.NullArgumentException;

import com.iris.dto.MailInfoBean;
import com.iris.mail.Mailer;
import com.iris.model.WebServiceComponentUrl;
import com.iris.util.DaemonThreadFactory;

public class MailService {

	private static ExecutorService executor;

	private static String reloadKey;
	private static String defaultHost;
	private static String defaultPort;
	private static String defaultSenderEmail;
	private static String defaultSenderPassword;
	private static boolean isTLSAuthentication;
	private static boolean isSSLAuthentication;
	private static boolean issmtpAuthentication;
	private static String sslPort;

	private MailService() {
	}

	static {
		executor = Executors.newFixedThreadPool(10, new DaemonThreadFactory());
	}

	public static void sendMail(WebServiceComponentUrl webServiceComponent, MailInfoBean mailInfoBean) throws NullArgumentException {
		if (mailInfoBean == null || webServiceComponent == null) {
			throw new NullArgumentException("webServiceComponent, mailInfoBean");
		}
		mailInfoBean.setSenderEmail(defaultSenderEmail);
		mailInfoBean.setSenderPasswd(defaultSenderPassword);
		mailInfoBean.setHost(defaultHost);
		mailInfoBean.setPort(defaultPort);
		mailInfoBean.setIsTLSAuthentication(isTLSAuthentication);
		mailInfoBean.setIsSSLAuthentication(isSSLAuthentication);
		mailInfoBean.setIssmtpAuthentication(issmtpAuthentication);
		if (isSSLAuthentication) {
			mailInfoBean.setSslPort(sslPort);
		}
		Mailer mailer = new Mailer(mailInfoBean, webServiceComponent);
		executor.execute(mailer);
	}

	/**
	 * @return the executor
	 */
	public static ExecutorService getExecutor() {
		return executor;
	}

	/**
	 * @param executor the executor to set
	 */
	public static void setExecutor(ExecutorService executor) {
		MailService.executor = executor;
	}

	/**
	 * @return the reloadKey
	 */
	public static String getReloadKey() {
		return reloadKey;
	}

	/**
	 * @param reloadKey the reloadKey to set
	 */
	public static void setReloadKey(String reloadKey) {
		MailService.reloadKey = reloadKey;
	}

	/**
	 * @return the defaultHost
	 */
	public static String getDefaultHost() {
		return defaultHost;
	}

	/**
	 * @param defaultHost the defaultHost to set
	 */
	public static void setDefaultHost(String defaultHost) {
		MailService.defaultHost = defaultHost;
	}

	/**
	 * @return the defaultPort
	 */
	public static String getDefaultPort() {
		return defaultPort;
	}

	/**
	 * @param defaultPort the defaultPort to set
	 */
	public static void setDefaultPort(String defaultPort) {
		MailService.defaultPort = defaultPort;
	}

	/**
	 * @return the defaultSenderEmail
	 */
	public static String getDefaultSenderEmail() {
		return defaultSenderEmail;
	}

	/**
	 * @param defaultSenderEmail the defaultSenderEmail to set
	 */
	public static void setDefaultSenderEmail(String defaultSenderEmail) {
		MailService.defaultSenderEmail = defaultSenderEmail;
	}

	/**
	 * @return the defaultSenderPassword
	 */
	public static String getDefaultSenderPassword() {
		return defaultSenderPassword;
	}

	/**
	 * @param defaultSenderPassword the defaultSenderPassword to set
	 */
	public static void setDefaultSenderPassword(String defaultSenderPassword) {
		MailService.defaultSenderPassword = defaultSenderPassword;
	}

	/**
	 * @return the isTLSAuthentication
	 */
	public static boolean isTLSAuthentication() {
		return isTLSAuthentication;
	}

	/**
	 * @param isTLSAuthentication the isTLSAuthentication to set
	 */
	public static void setTLSAuthentication(boolean isTLSAuthentication) {
		MailService.isTLSAuthentication = isTLSAuthentication;
	}

	/**
	 * @return the isSSLAuthentication
	 */
	public static boolean isSSLAuthentication() {
		return isSSLAuthentication;
	}

	/**
	 * @param isSSLAuthentication the isSSLAuthentication to set
	 */
	public static void setSSLAuthentication(boolean isSSLAuthentication) {
		MailService.isSSLAuthentication = isSSLAuthentication;
	}

	/**
	 * @return the issmtpAuthentication
	 */
	public static boolean isIssmtpAuthentication() {
		return issmtpAuthentication;
	}

	/**
	 * @param issmtpAuthentication the issmtpAuthentication to set
	 */
	public static void setIssmtpAuthentication(boolean issmtpAuthentication) {
		MailService.issmtpAuthentication = issmtpAuthentication;
	}

	/**
	 * @return the sslPort
	 */
	public static String getSslPort() {
		return sslPort;
	}

	/**
	 * @param sslPort the sslPort to set
	 */
	public static void setSslPort(String sslPort) {
		MailService.sslPort = sslPort;
	}

	public static void sendMail(WebServiceComponentUrl webServiceComponent, List<MailInfoBean> mailInfoBeanList) {

		if (mailInfoBeanList == null || webServiceComponent == null) {
			throw new NullArgumentException("webServiceComponent, mailInfoBean");
		}
		List<MailInfoBean> mailInfoBeanListNew = new ArrayList<MailInfoBean>();
		for (MailInfoBean mailInfoBean : mailInfoBeanList) {
			mailInfoBean.setSenderEmail(defaultSenderEmail);
			mailInfoBean.setSenderPasswd(defaultSenderPassword);
			mailInfoBean.setHost(defaultHost);
			mailInfoBean.setPort(defaultPort);
			mailInfoBean.setIsTLSAuthentication(isTLSAuthentication);
			mailInfoBean.setIsSSLAuthentication(isSSLAuthentication);
			mailInfoBean.setIssmtpAuthentication(issmtpAuthentication);
			if (isSSLAuthentication) {
				mailInfoBean.setSslPort(sslPort);
			}
			mailInfoBeanListNew.add(mailInfoBean);
		}
		Mailer mailer = new Mailer(mailInfoBeanListNew, webServiceComponent);
		executor.execute(mailer);

	}

}
