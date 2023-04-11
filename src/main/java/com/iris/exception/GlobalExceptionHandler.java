/**
 * 
 */
package com.iris.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;

/**
 * @author sajadhav
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
	public ServiceResponse ResourceNotFounException(ResourceNotFoundException rs, WebRequest webrequest) {
		ServiceResponse serviceResponse = new ServiceResponseBuilder().setStatusCode(HttpStatus.NOT_FOUND.value() + "").setStatusMessage(rs.getMessage()).build();
		return serviceResponse;
	}

	@ExceptionHandler(Exception.class)
	public ServiceResponse globalExceptionHandler(ResourceNotFoundException rs, WebRequest webrequest) {
		ServiceResponse serviceResponse = new ServiceResponseBuilder().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value() + "").setStatusMessage(rs.getMessage()).build();
		return serviceResponse;
	}
}
