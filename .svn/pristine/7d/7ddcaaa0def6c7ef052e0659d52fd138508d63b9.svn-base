package com.iris.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class CallStompMsgViaThread implements Runnable {

	private Long userId;
	private String roleId;
	private SimpMessagingTemplate template;
	private static final String SLASH = "/";
	private static final String WEB_SOCKER_BROKER_NAME = "webSockerBrokerName";
	private static final String VERIFY_XBRL_SESSION_STRING = "verifyXbrlSession";
	private static final Logger LOGGER = LoggerFactory.getLogger(CallStompMsgViaThread.class);

	public CallStompMsgViaThread(Long userId, String roleId, SimpMessagingTemplate template) {
		this.setUserId(userId);
		this.setRoleId(roleId);
		this.setTemplate(template);
	}

	@Override
	public void run() {
		try {
			if (this.userId != null && this.roleId != null && this.template != null) {
				this.template.convertAndSend(SLASH + ResourceUtil.getKeyValue(WEB_SOCKER_BROKER_NAME) + SLASH + this.roleId + SLASH + this.userId + SLASH + VERIFY_XBRL_SESSION_STRING, true);
			}
		} catch (Exception e) {
			LOGGER.error("Exception in Mailer run method", e);
		}

	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public SimpMessagingTemplate getTemplate() {
		return template;
	}

	public void setTemplate(SimpMessagingTemplate template) {
		this.template = template;
	}

}
