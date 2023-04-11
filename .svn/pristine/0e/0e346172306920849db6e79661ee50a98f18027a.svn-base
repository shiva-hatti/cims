package com.iris.controller;

import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.model.ReturnTableMap;
import com.iris.service.impl.ReturnTableMapService;
import com.iris.util.constant.ErrorConstants;

/**
 * @author apagaria
 */
@RestController
@RequestMapping("/service/returnTableMap")
public class ReturnTableMapController {

	@Autowired
	private ReturnTableMapService returnTableMapService;

	static final Logger LOGGER = LogManager.getLogger(ReturnTableMapController.class);

	@GetMapping(value = "/getOptionalTableCode")
	public ServiceResponse insertRecordProcessingTime(@RequestHeader("JobProcessingId") String jobProcessingId, @RequestHeader("returnCode") String returnCode) {
		Set<ReturnTableMap> returnTableMapSet = null;
		ServiceResponse serviceResponse = null;
		LOGGER.info("GetOptionalTableCode - Request START for request transaction id :" + jobProcessingId);
		try {
			returnTableMapSet = returnTableMapService.getListOfTableCodes(returnCode, true);
			if (!CollectionUtils.isEmpty(returnTableMapSet)) {
				serviceResponse = new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorConstants.RECORD_SAVED_SUCCESSFULLY.getConstantVal()).setResponse(returnTableMapSet).build();
			} else {
				/*
				 * serviceResponse = new ServiceResponseBuilder().setStatus(false)
				 * .setStatusCode(ErrorConstants.ERROR_NO_RECORD_FOUND.getConstantVal()).build()
				 * ;
				 */
			}
		} catch (Exception e) {
			LOGGER.error("GetOptionalTableCode - Exception Occured for request transaction id :" + jobProcessingId + " " + ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
			serviceResponse = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorConstants.DEFAULT_ERROR.getConstantVal()).build();
		}
		LOGGER.info("GetOptionalTableCode - Request END for request transaction id :" + jobProcessingId);
		return serviceResponse;
	}
}
