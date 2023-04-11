/**
 * 
 */
package com.iris.sdmx.apilog.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author sajadhav
 *
 */
@Entity
@Table(name = "TBL_SDMX_API_LOG_DETAILS")
public class SDMXApiLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3754470376217551691L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "USER_ID_FK")
	private Long userIdFk;

	@Column(name = "COMPONENT_URL_ID_FK")
	private Long componentUrlIdFk;

	@Column(name = "REQUEST_JSON")
	private String requestJson;

	@Column(name = "PATH_VARIABLE")
	private String pathVariable;

	@Column(name = "REQUEST_RECEIVED_TIME")
	private Date requestReceivedTime;

	@Column(name = "RESPONSE_SENDING_TIME")
	private Date responseSendingTime;

	@Column(name = "CALLER_IP_ADDRESS")
	private String callerIpAddres;

	@Column(name = "HEADER")
	private String header;

	@Column(name = "SERVER_IP")
	private String serverIp;

	@Column(name = "CREATED_TIME")
	private Date createdTime;

	@Column(name = "RESPONSE_JSON")
	private String responseJson;

	/**
	 * @return the userIdFk
	 */
	public Long getUserIdFk() {
		return userIdFk;
	}

	/**
	 * @param userIdFk the userIdFk to set
	 */
	public void setUserIdFk(Long userIdFk) {
		this.userIdFk = userIdFk;
	}

	/**
	 * @return the componentUrlIdFk
	 */
	public Long getComponentUrlIdFk() {
		return componentUrlIdFk;
	}

	/**
	 * @param componentUrlIdFk the componentUrlIdFk to set
	 */
	public void setComponentUrlIdFk(Long componentUrlIdFk) {
		this.componentUrlIdFk = componentUrlIdFk;
	}

	/**
	 * @return the requestJson
	 */
	public String getRequestJson() {
		return requestJson;
	}

	/**
	 * @param requestJson the requestJson to set
	 */
	public void setRequestJson(String requestJson) {
		this.requestJson = requestJson;
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
	 * @return the requestReceivedTime
	 */
	public Date getRequestReceivedTime() {
		return requestReceivedTime;
	}

	/**
	 * @param requestReceivedTime the requestReceivedTime to set
	 */
	public void setRequestReceivedTime(Date requestReceivedTime) {
		this.requestReceivedTime = requestReceivedTime;
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
	 * @return the callerIpAddres
	 */
	public String getCallerIpAddres() {
		return callerIpAddres;
	}

	/**
	 * @param callerIpAddres the callerIpAddres to set
	 */
	public void setCallerIpAddres(String callerIpAddres) {
		this.callerIpAddres = callerIpAddres;
	}

	/**
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * @return the serverIp
	 */
	public String getServerIp() {
		return serverIp;
	}

	/**
	 * @param serverIp the serverIp to set
	 */
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
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

	/**
	 * @return the responseJson
	 */
	public String getResponseJson() {
		return responseJson;
	}

	/**
	 * @param responseJson the responseJson to set
	 */
	public void setResponseJson(String responseJson) {
		this.responseJson = responseJson;
	}

}
