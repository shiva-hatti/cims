/**
 * 
 */
package com.iris.dto;

import java.io.Serializable;

/**
 * @author sajadhav
 *
 */
public class ReturnChannelDetailsDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5215682601870586317L;

	private Long returnId;

	private String returnCode;

	private boolean apiChannel;

	private boolean stsChannel;

	private boolean webChannel;

	private boolean emailChannel;

	private boolean uploadChannel;

	/**
	 * @return the returnId
	 */
	public Long getReturnId() {
		return returnId;
	}

	/**
	 * @param returnId the returnId to set
	 */
	public void setReturnId(Long returnId) {
		this.returnId = returnId;
	}

	/**
	 * @return the returnCode
	 */
	public String getReturnCode() {
		return returnCode;
	}

	/**
	 * @param returnCode the returnCode to set
	 */
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	/**
	 * @return the apiChannel
	 */
	public boolean isApiChannel() {
		return apiChannel;
	}

	/**
	 * @param apiChannel the apiChannel to set
	 */
	public void setApiChannel(boolean apiChannel) {
		this.apiChannel = apiChannel;
	}

	/**
	 * @return the stsChannel
	 */
	public boolean isStsChannel() {
		return stsChannel;
	}

	/**
	 * @param stsChannel the stsChannel to set
	 */
	public void setStsChannel(boolean stsChannel) {
		this.stsChannel = stsChannel;
	}

	/**
	 * @return the webChannel
	 */
	public boolean isWebChannel() {
		return webChannel;
	}

	/**
	 * @param webChannel the webChannel to set
	 */
	public void setWebChannel(boolean webChannel) {
		this.webChannel = webChannel;
	}

	/**
	 * @return the emailChannel
	 */
	public boolean isEmailChannel() {
		return emailChannel;
	}

	/**
	 * @param emailChannel the emailChannel to set
	 */
	public void setEmailChannel(boolean emailChannel) {
		this.emailChannel = emailChannel;
	}

	/**
	 * @return the uploadChannel
	 */
	public boolean isUploadChannel() {
		return uploadChannel;
	}

	/**
	 * @param uploadChannel the uploadChannel to set
	 */
	public void setUploadChannel(boolean uploadChannel) {
		this.uploadChannel = uploadChannel;
	}

}
