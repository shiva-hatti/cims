package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_SDMX_WEBSERVICE_URL")
public class SdmxWebserviceUrl implements Serializable {

	private static final long serialVersionUID = -6700682468195441752L;

	@Id
	@Column(name = "SDMX_WEBSERVICE_URL_ID")
	private Long sdmxWebserviceUrlID;

	@Column(name = "COMPONENT_URL_PATH")
	private String callUrlPath;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "URL_HTTP_METHOD_TYPE")
	private String httpMethodType;

	@Column(name = "COMPONENT_TYPE")
	private String callType;

	@Column(name = "APPID_KEY")
	private String authTokenVal;

	@Column(name = "URL_PRODUCE_TYPE")
	private String produceType;

	@Column(name = "URL_ACCEPT_TYPE")
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

	/**
	 * @return the componentUrlId
	 */
	public Long getSdmxWebserviceUrlID() {
		return sdmxWebserviceUrlID;
	}

	/**
	 * @param componentUrlId the componentUrlId to set
	 */
	public void setSdmxWebserviceUrlID(Long sdmxWebserviceUrlID) {
		this.sdmxWebserviceUrlID = sdmxWebserviceUrlID;
	}

	/**
	 * @return the componentUrlPath
	 */
	public String getCallUrlPath() {
		return callUrlPath;
	}

	/**
	 * @param componentUrlPath the componentUrlPath to set
	 */
	public void setCallUrlPath(String callUrlPath) {
		this.callUrlPath = callUrlPath;
	}

	/**
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the urlHttpMethodType
	 */

	public String getHttpMethodType() {
		return httpMethodType;
	}

	/**
	 * @param urlHttpMethodType the urlHttpMethodType to set
	 */

	public void setHttpMethodType(String httpMethodType) {
		this.httpMethodType = httpMethodType;
	}

	/**
	 * @return the componentType
	 */
	public String getCallType() {
		return callType;
	}

	/**
	 * @param componentType the componentType to set
	 */
	public void setCallType(String callType) {
		this.callType = callType;
	}

	/**
	 * @return the appIdKey
	 */

	public String getAuthTokenVal() {
		return authTokenVal;
	}

	/**
	 * @param appIdKey the appIdKey to set
	 */

	public void setAuthTokenVal(String authTokenVal) {
		this.authTokenVal = authTokenVal;
	}

}
