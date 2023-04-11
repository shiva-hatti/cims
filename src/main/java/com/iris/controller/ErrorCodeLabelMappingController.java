package com.iris.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ApplicationException;
import com.iris.model.ErrorCodeDetail;
import com.iris.model.ErrorCodeLabelMapping;
import com.iris.model.ErrorCodeLabelRequest;
import com.iris.service.impl.ErrorCodeLabelMappingService;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.validator.ErrorCodeLabelMappingValidator;

/**
 * @author apagaria
 */
@RestController
@RequestMapping("/service/errorCodeLabelMappingController")
public class ErrorCodeLabelMappingController {

	@Autowired
	private ErrorCodeLabelMappingService errorCodeLabelMappingService;

	static final Logger LOGGER = LogManager.getLogger(ErrorCodeLabelMappingController.class);

	/**
	 * This API is used to fetch the
	 * 
	 * @param jobProcessId
	 * @return
	 */
	@PostMapping(value = "/getErrorCodeLabelMappingByErrorCodeIds")
	public ServiceResponse fetchErrorCodeDetailByStatus(@RequestHeader("JobProcessingId") String jobProcessId, @RequestBody ErrorCodeLabelRequest errorCodeLabelRequest) {
		ServiceResponse serviceResponse = null;
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(true);
		try {
			LOGGER.info("START - Get Error Code Detail By Status Request received for Request trans Id " + jobProcessId);

			// validation
			ErrorCodeLabelMappingValidator.validateFetchErrorCodeLabelParameter(jobProcessId, errorCodeLabelRequest);

			// Fetching the result
			List<ErrorCodeLabelMapping> errorCodeLabelMappingList = fetchErrorCodeLabelObject(errorCodeLabelRequest);

			serviceResponseBuilder.setResponse(errorCodeLabelMappingList);
			serviceResponse = serviceResponseBuilder.build();

		} catch (ApplicationException applicationException) {
			LOGGER.error(applicationException.getErrorMsg() + " for Transaction ID : " + jobProcessId);
			serviceResponse = new ServiceResponseBuilder().setStatus(false).setStatusCode(applicationException.getErrorCode()).setStatusMessage(applicationException.getErrorMsg()).build();
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Transaction ID : " + jobProcessId, e);
			serviceResponse = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString())).build();
		}
		LOGGER.info("END - Get Error Code Detail By Status Request received for Request trans Id " + jobProcessId);
		return serviceResponse;
	}

	@GetMapping(value = "/getStaticErrorCodeLabel")
	public ServiceResponse fetchStaticErrorCodeLabel(@RequestHeader("JobProcessingId") String jobProcessId) {
		ServiceResponse serviceResponse = null;
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(true);
		try {
			LOGGER.info("START - Get static Error Code Detail By Status Request received for Request trans Id " + jobProcessId);

			List<String> staticErrorCodeList = new ArrayList<>();
			staticErrorCodeList.add("ER000T00001");
			staticErrorCodeList.add("ER000T00002");
			staticErrorCodeList.add("ER000T00003");
			staticErrorCodeList.add("ER000T00004");
			staticErrorCodeList.add("ER000T00005");
			staticErrorCodeList.add("ER000T00901");
			staticErrorCodeList.add("ER000T00902");

			// Fetching the result
			List<ErrorCodeDetail> errorCodeLabelMappingList = fetchStaticErrorCodeLabelObject(staticErrorCodeList);

			serviceResponseBuilder.setResponse(errorCodeLabelMappingList);
			serviceResponse = serviceResponseBuilder.build();

		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Transaction ID : " + jobProcessId, e);
			serviceResponse = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString())).build();
		}
		LOGGER.info("END - Get Error Code Detail By Status Request received for Request trans Id " + jobProcessId);
		return serviceResponse;
	}

	private List<ErrorCodeDetail> fetchStaticErrorCodeLabelObject(List<String> staticErrorCodeList) {
		List<ErrorCodeDetail> errorCodeLabelMappingList = null;
		// For Web form based
		errorCodeLabelMappingList = errorCodeLabelMappingService.getStaticErrorCodeLabels(staticErrorCodeList);
		return errorCodeLabelMappingList;
	}

	/**
	 * This method is used to create object to fetch error code detail record from database.
	 * 
	 * @param columnValueMap
	 * @param channel
	 * @param isActive
	 */
	private List<ErrorCodeLabelMapping> fetchErrorCodeLabelObject(ErrorCodeLabelRequest errorCodeLabelRequest) {
		List<ErrorCodeLabelMapping> errorCodeLabelMappingList = null;
		// For Web form based
		errorCodeLabelMappingList = errorCodeLabelMappingService.getErrorCodeLabelByErrorCodeIds(errorCodeLabelRequest);
		return errorCodeLabelMappingList;
	}
}
