/**
 * 
 */
package com.iris.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author sikhan
 *
 */
@Entity
@Table(name = "TBL_API_LOG_DETAILS")
public class ApiLogDetails implements Serializable {

	private static final long serialVersionUID = -8273258327301690259L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "USER_ID_FK")
	private UserMaster userIdFk;

	@JoinColumn(name = "COMPONENT_URL_ID_FK")
	private WebServiceComponentUrl componentIdFk;

	@Column(name = "REQUEST_JSON")
	private String requestJSON;

	@Column(name = "RESPONSE_JSON")
	private String responseJSON;

	@Column(name = "PATH_VARIABLE")
	private String pathVariable;

	@Column(name = "REQUEST_RECEIVED_TIME")
	private Date reuestReceivedTime;

	@Column(name = "RESPONSE_SENDING_TIME")
	private Date responseSendingTime;

	@Column(name = "CALLER_IP_ADDRESS")
	private String callerIPAddress;

	@Column(name = "JWT_TOKEN")
	private String jwtToken;

	@Column(name = "APP_ID")
	private String appId;

	@Column(name = "SERVER_IP")
	private String serverIPAddress;

	@Column(name = "CREATED_TIME")
	private Date createdTime;

	@Transient
	private Long requestReceivedTimeInLong;

	@Transient
	private Long responseSendingTimeInLong;

	/**
	 * @return the componentIdFk
	 */
	public WebServiceComponentUrl getComponentIdFk() {
		return componentIdFk;
	}

	/**
	 * @param componentIdFk the componentIdFk to set
	 */
	public void setComponentIdFk(WebServiceComponentUrl componentIdFk) {
		this.componentIdFk = componentIdFk;
	}

	/**
	 * @return the requestReceivedTimeInLong
	 */
	public Long getRequestReceivedTimeInLong() {
		return requestReceivedTimeInLong;
	}

	/**
	 * @param requestReceivedTimeInLong the requestReceivedTimeInLong to set
	 */
	public void setRequestReceivedTimeInLong(Long requestReceivedTimeInLong) {
		this.requestReceivedTimeInLong = requestReceivedTimeInLong;
	}

	/**
	 * @return the responseSendingTimeInLong
	 */
	public Long getResponseSendingTimeInLong() {
		return responseSendingTimeInLong;
	}

	/**
	 * @param responseSendingTimeInLong the responseSendingTimeInLong to set
	 */
	public void setResponseSendingTimeInLong(Long responseSendingTimeInLong) {
		this.responseSendingTimeInLong = responseSendingTimeInLong;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the userIdFk
	 */
	public UserMaster getUserIdFk() {
		return userIdFk;
	}

	/**
	 * @param userIdFk the userIdFk to set
	 */
	public void setUserIdFk(UserMaster userIdFk) {
		this.userIdFk = userIdFk;
	}

	/**
	 * @return the requestJSON
	 */
	public String getRequestJSON() {
		return requestJSON;
	}

	/**
	 * @param requestJSON the requestJSON to set
	 */
	public void setRequestJSON(String requestJSON) {
		this.requestJSON = requestJSON;
	}

	/**
	 * @return the responseJSON
	 */
	public String getResponseJSON() {
		return responseJSON;
	}

	/**
	 * @param responseJSON the responseJSON to set
	 */
	public void setResponseJSON(String responseJSON) {
		this.responseJSON = responseJSON;
	}

	/**
	 * @return the pathVariable
	 */
	public String getPathVariable() {
		return pathVariable;
	}

	/**
	 * @param pathVariable the pathVariable to set
	 */
	public void setPathVariable(String pathVariable) {
		this.pathVariable = pathVariable;
	}

	/**
	 * @return the reuestReceivedTime
	 */
	public Date getReuestReceivedTime() {
		return reuestReceivedTime;
	}

	/**
	 * @param reuestReceivedTime the reuestReceivedTime to set
	 */
	public void setReuestReceivedTime(Date reuestReceivedTime) {
		this.reuestReceivedTime = reuestReceivedTime;
	}

	/**
	 * @return the responseSendingTime
	 */
	public Date getResponseSendingTime() {
		return responseSendingTime;
	}

	/**
	 * @param responseSendingTime the responseSendingTime to set
	 */
	public void setResponseSendingTime(Date responseSendingTime) {
		this.responseSendingTime = responseSendingTime;
	}

	/**
	 * @return the callerIPAddress
	 */
	public String getCallerIPAddress() {
		return callerIPAddress;
	}

	/**
	 * @param callerIPAddress the callerIPAddress to set
	 */
	public void setCallerIPAddress(String callerIPAddress) {
		this.callerIPAddress = callerIPAddress;
	}

	/**
	 * @return the jwtToken
	 */
	public String getJwtToken() {
		return jwtToken;
	}

	/**
	 * @param jwtToken the jwtToken to set
	 */
	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * @param appId the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * @return the serverIPAddress
	 */
	public String getServerIPAddress() {
		return serverIPAddress;
	}

	/**
	 * @param serverIPAddress the serverIPAddress to set
	 */
	public void setServerIPAddress(String serverIPAddress) {
		this.serverIPAddress = serverIPAddress;
	}

	/**
	 * @return the createdTime
	 */
	public Date getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

}
