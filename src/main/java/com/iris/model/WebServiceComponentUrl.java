package com.iris.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_WEBSERVICE_COMPONENT_URL")
public class WebServiceComponentUrl implements Serializable {

	private static final long serialVersionUID = 4863417516166337783L;

	@Id
	@Column(name = "COMPONENT_URL_ID")
	private Long componentUrlId;

	@Column(name = "COMPONENT_URL_PATH")
	private String componentUrlPath;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "URL_HTTP_METHOD_TYPE")
	private String urlHttpMethodType;

	@Column(name = "URL_PRODUCE_TYPE")
	private String urlProduceType;

	@Column(name = "URL_ACCEPT_TYPE")
	private String urlAcceptType;

	@Column(name = "COMPONENT_TYPE")
	private String componentType;

	@Column(name = "APPID_KEY")
	private String appIdKey;

	@Column(name = "LAST_MODIFIED_ON")
	private Date lastModifiedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_MODIFIED_BY_FK")
	private UserMaster modifiedBy;

	/**
	 * @return the componentUrlId
	 */
	public Long getComponentUrlId() {
		return componentUrlId;
	}

	/**
	 * @param componentUrlId the componentUrlId to set
	 */
	public void setComponentUrlId(Long componentUrlId) {
		this.componentUrlId = componentUrlId;
	}

	/**
	 * @return the componentUrlPath
	 */
	public String getComponentUrlPath() {
		return componentUrlPath;
	}

	/**
	 * @param componentUrlPath the componentUrlPath to set
	 */
	public void setComponentUrlPath(String componentUrlPath) {
		this.componentUrlPath = componentUrlPath;
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
	public String getUrlHttpMethodType() {
		return urlHttpMethodType;
	}

	/**
	 * @param urlHttpMethodType the urlHttpMethodType to set
	 */
	public void setUrlHttpMethodType(String urlHttpMethodType) {
		this.urlHttpMethodType = urlHttpMethodType;
	}

	/**
	 * @return the urlProduceType
	 */
	public String getUrlProduceType() {
		return urlProduceType;
	}

	/**
	 * @param urlProduceType the urlProduceType to set
	 */
	public void setUrlProduceType(String urlProduceType) {
		this.urlProduceType = urlProduceType;
	}

	/**
	 * @return the urlAcceptType
	 */
	public String getUrlAcceptType() {
		return urlAcceptType;
	}

	/**
	 * @param urlAcceptType the urlAcceptType to set
	 */
	public void setUrlAcceptType(String urlAcceptType) {
		this.urlAcceptType = urlAcceptType;
	}

	/**
	 * @return the componentType
	 */
	public String getComponentType() {
		return componentType;
	}

	/**
	 * @param componentType the componentType to set
	 */
	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	/**
	 * @return the appIdKey
	 */
	public String getAppIdKey() {
		return appIdKey;
	}

	/**
	 * @param appIdKey the appIdKey to set
	 */
	public void setAppIdKey(String appIdKey) {
		this.appIdKey = appIdKey;
	}

	/**
	 * @return the lastModifiedOn
	 */
	public Date getLastModifiedOn() {
		return lastModifiedOn;
	}

	/**
	 * @param lastModifiedOn the lastModifiedOn to set
	 */
	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	/**
	 * @return the modifiedBy
	 */
	public UserMaster getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(UserMaster modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

}
