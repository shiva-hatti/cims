/**
 * 
 */
package com.iris.sdmx.util;

import java.io.File;
import java.io.Serializable;

/**
 * @author sajadhav
 *
 */
public class RestClientResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int statusCode;

	private String restClientResponse;

	private File fileResponse;

	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * @return the restClientResponse
	 */
	public String getRestClientResponse() {
		return restClientResponse;
	}

	/**
	 * @param restClientResponse the restClientResponse to set
	 */
	public void setRestClientResponse(String restClientResponse) {
		this.restClientResponse = restClientResponse;
	}

	/**
	 * @return the fileResponse
	 */
	public File getFileResponse() {
		return fileResponse;
	}

	/**
	 * @param fileResponse the fileResponse to set
	 */
	public void setFileResponse(File fileResponse) {
		this.fileResponse = fileResponse;
	}

}
