package com.iris.dto;

import java.io.Serializable;

public class HTTPRequestDetails implements Serializable {

	private static final long serialVersionUID = 1520449840168279891L;

	private String referer;
	private String fullURL;
	private String clientIPAddr;
	private String clientOS;
	private String clientBrowser;
	private String userAgent;

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getFullURL() {
		return fullURL;
	}

	public void setFullURL(String fullURL) {
		this.fullURL = fullURL;
	}

	public String getClientIPAddr() {
		return clientIPAddr;
	}

	public void setClientIPAddr(String clientIPAddr) {
		this.clientIPAddr = clientIPAddr;
	}

	public String getClientOS() {
		return clientOS;
	}

	public void setClientOS(String clientOS) {
		this.clientOS = clientOS;
	}

	public String getClientBrowser() {
		return clientBrowser;
	}

	public void setClientBrowser(String clientBrowser) {
		this.clientBrowser = clientBrowser;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
}
