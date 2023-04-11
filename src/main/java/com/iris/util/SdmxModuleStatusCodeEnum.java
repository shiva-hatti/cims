/**
 * 
 */
package com.iris.util;

/**
 * @author apagaria
 *
 */
public enum SdmxModuleStatusCodeEnum {

	UPLOADED("UPLOADED"), IN_PROGRESS("INPROG"), UPLOADED_SUCCESSFULLY("UPLOADSUCCESS"), UPLOAD_FAILED("UPLOADFAILED"), READY_PUBLISH("READYPUBLISH"), PUBLISH_IN_PROGRESS("PUBLISHINPROGRESS"), PUBLISH_SUCCESS("PUBLISHSUCCESS"), PUBLISH_FAILED("PUBLISHFAILED");

	String statusCode;

	/**
	 * @param statusId
	 * @param statusCode
	 * @param statusMessage
	 */
	private SdmxModuleStatusCodeEnum(String statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * @return the statusCode
	 */
	public String getStatusCode() {
		return statusCode;
	}
}
