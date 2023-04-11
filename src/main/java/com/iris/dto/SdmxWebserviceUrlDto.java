package com.iris.dto;

import java.io.Serializable;

import javax.persistence.Column;

public class SdmxWebserviceUrlDto implements Serializable {

	private static final long serialVersionUID = 6489623878503159451L;

	private Long sdmxWebserviceUrlID;
	private String componentUrlPath;
	private String urlHttpMethodType;
	private String componentType;
	private String authTokenVal;
	private String produceType;
	private String acceptType;

	/**
	 * @return the produceType
	 */
	public String getProduceType() {
		return produceType;
	}

	/**
	 * @param produceType the produceType to set
	 */
	public void setProduceType(String produceType) {
		this.produceType = produceType;
	}

	/**
	 * @return the acceptType
	 */
	public String getAcceptType() {
		return acceptType;
	}

	/**
	 * @param acceptType the acceptType to set
	 */
	public void setAcceptType(String acceptType) {
		this.acceptType = acceptType;
	}

	public Long getSdmxWebserviceUrlID() {
		return sdmxWebserviceUrlID;
	}

	public void setSdmxWebserviceUrlID(Long sdmxWebserviceUrlID) {
		this.sdmxWebserviceUrlID = sdmxWebserviceUrlID;
	}

	public String getUrlHttpMethodType() {
		return urlHttpMethodType;
	}

	public void setUrlHttpMethodType(String urlHttpMethodType) {
		this.urlHttpMethodType = urlHttpMethodType;
	}

	public String getAuthTokenVal() {
		return authTokenVal;
	}

	public void setAuthTokenVal(String authTokenVal) {
		this.authTokenVal = authTokenVal;
	}

	public String getComponentUrlPath() {
		return componentUrlPath;
	}

	public void setComponentUrlPath(String componentUrlPath) {
		this.componentUrlPath = componentUrlPath;
	}

	public String getComponentType() {
		return componentType;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

}
