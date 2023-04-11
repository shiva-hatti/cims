package com.iris.mail;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iris.dto.MailInfoBean;
import com.iris.dto.MailUtil;
import com.iris.model.WebServiceComponentUrl;

public class Mailer implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(Mailer.class);
	private MailInfoBean mailInfo;
	private WebServiceComponentUrl webServiceComponent;
	private List<MailInfoBean> mailInfoBeanList;

	public List<MailInfoBean> getMailInfoBeanList() {
		return mailInfoBeanList;
	}

	public void setMailInfoBeanListNew(List<MailInfoBean> mailInfoBeanList) {
		this.mailInfoBeanList = mailInfoBeanList;
	}

	/**
	 * @return the mailInfo
	 */
	public MailInfoBean getMailInfo() {
		return mailInfo;
	}

	/**
	 * @param mailInfo the mailInfo to set
	 */
	public void setMailInfo(MailInfoBean mailInfo) {
		this.mailInfo = mailInfo;
	}

	/**
	 * @return the webServiceComponent
	 */
	public WebServiceComponentUrl getWebServiceComponent() {
		return webServiceComponent;
	}

	/**
	 * @param webServiceComponent the webServiceComponent to set
	 */
	public void setWebServiceComponent(WebServiceComponentUrl webServiceComponent) {
		this.webServiceComponent = webServiceComponent;
	}

	public Mailer(MailInfoBean mailInfo) {
		this.mailInfo = mailInfo;
	}

	public Mailer(MailInfoBean mailInfo, WebServiceComponentUrl webServiceComponent) {
		this.mailInfo = mailInfo;
		this.webServiceComponent = webServiceComponent;
	}

	public Mailer(List<MailInfoBean> mailInfoBeanList, WebServiceComponentUrl webServiceComponent) {
		this.mailInfoBeanList = mailInfoBeanList;
		this.webServiceComponent = webServiceComponent;
	}

	@Override
	public void run() {
		try {
			if (this.webServiceComponent != null) {
				MailUtil.sendMail(getWebServiceComponent(), getMailInfoBeanList());
			} else {
				MailUtil.sendMail(getMailInfo());
			}
		} catch (Exception e) {
			LOGGER.error("Exception in Mailer run method", e);
		}

	}

}
