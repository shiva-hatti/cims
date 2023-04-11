package com.iris.exception;

import com.iris.util.constant.ErrorCode;

public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = -3579516876352596129L;
	private ErrorCode errorCode;
	private Throwable rootCause;
	private String errorMsg;

	public ServiceException(ErrorCode errorCode, Throwable rootCause) {
		this.errorCode = errorCode;
		this.rootCause = rootCause;
	}

	public ServiceException(String errorMsg, Throwable rootCause) {
		this.errorMsg = errorMsg;
		this.rootCause = rootCause;
	}

	public ServiceException(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	@Override
	public synchronized Throwable getCause() {
		return this.rootCause;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	/**
	 * @return the errorMsg
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

}