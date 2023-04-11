package com.iris.dto;

import com.iris.util.Validations;

/**
 * This class would act as a generic response to all the web-services
 * 
 * @author Suman Kumar
 * @version 1.0
 */
public class ServiceResponse {

	private boolean status;
	private String statusCode;
	private String statusMessage;
	private Object response;

	private ServiceResponse(ServiceResponseBuilder serviceResponseBuilder) {
		this.status = serviceResponseBuilder.status;
		this.statusCode = serviceResponseBuilder.statusCode;
		this.statusMessage = serviceResponseBuilder.statusMessage;
		this.response = serviceResponseBuilder.response;
	}

	/**
	 * @return the status
	 */
	public boolean isStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

	/**
	 * @return the statusCode
	 */
	public String getStatusCode() {
		return statusCode;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = Validations.trimInput(statusCode);
	}

	/**
	 * @return the statusMessage
	 */
	public String getStatusMessage() {
		return statusMessage;
	}

	/**
	 * @param statusMessage the statusMessage to set
	 */
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = Validations.trimInput(statusMessage);
	}

	/**
	 * @return the response
	 */
	public Object getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(Object response) {
		this.response = response;
	}

	public static class ServiceResponseBuilder {
		private boolean status;
		private String statusCode;
		private String statusMessage;
		private Object response;

		public ServiceResponseBuilder setStatus(boolean status) {
			this.status = status;
			return this;
		}

		public ServiceResponseBuilder setStatusCode(String statusCode) {
			this.statusCode = statusCode;
			return this;
		}

		public ServiceResponseBuilder setStatusMessage(String statusMessage) {
			this.statusMessage = statusMessage;
			return this;
		}

		public ServiceResponseBuilder setResponse(Object response) {
			this.response = response;
			return this;
		}

		public ServiceResponse build() {
			ServiceResponse serviceResponse = new ServiceResponse(this);
			return serviceResponse;
		}

		@Override
		public String toString() {
			return "ServiceResponseBuilder [status=" + status + ", statusCode=" + statusCode + ", statusMessage=" + statusMessage + ", response=" + response + "]";
		}
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "ServiceResponse [status=" + status + ", statusCode=" + statusCode + ", statusMessage=" + statusMessage + ", response=" + response + "]";
	}
}
