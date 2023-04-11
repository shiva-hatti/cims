package com.iris.sdmx.element.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ApplicationException;
import com.iris.sdmx.element.bean.SdmxElementRegulatorBean;
import com.iris.sdmx.element.service.SdmxElementRegulatorService;
import com.iris.sdmx.element.validator.SdmxElementRegulatorValidator;
import com.iris.sdmx.util.SDMXConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;

@RestController
@RequestMapping("/service/sdmx/regulator")
public class SdmxElementRegulatorController {

	/**
	 * 
	 */
	@Autowired
	private SdmxElementRegulatorService sdmxElementRegulatorService;

	@Autowired
	private SdmxElementRegulatorValidator sdmxElementRegulatorValidator;

	/**
	 * 
	 */
	static final Logger LOGGER = LogManager.getLogger(SdmxElementRegulatorController.class);

	/**
	 * @param jobProcessId
	 * @param isActive
	 * @return
	 */
	@PostMapping(value = "/fetchRegulatorByActiveStatus/{userId}/{languageCode}")
	public ServiceResponse fetchElementRegulator(@RequestHeader("JobProcessingId") String jobProcessId, @RequestHeader("IsActive") Boolean isActive, @PathVariable("userId") Long userId, @PathVariable("languageCode") String languageCode, @RequestBody SdmxElementRegulatorBean sdmxElementRegulatorBean) {
		LOGGER.info("START - Fetch Element Regulator by Active status request received with Job Processing ID : " + jobProcessId);
		ServiceResponse serviceResponse = null;
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(true);
		List<SdmxElementRegulatorBean> sdmxElementRegulatorBeans = null;
		try {
			sdmxElementRegulatorValidator.validateRegulatorRequest(jobProcessId, userId);
			sdmxElementRegulatorBeans = sdmxElementRegulatorService.findByActiveStatus(languageCode, userId);
			serviceResponseBuilder.setResponse(sdmxElementRegulatorBeans);
			serviceResponseBuilder.setStatusCode(SDMXConstants.SUCCESS_CODE);
			serviceResponseBuilder.setStatusMessage(SDMXConstants.SUCCESS_MESSAGE);
			serviceResponse = serviceResponseBuilder.build();
		} catch (ApplicationException applicationException) {
			serviceResponseBuilder.setStatus(false);
			serviceResponseBuilder.setStatusCode(applicationException.getErrorCode());
			serviceResponseBuilder.setStatusMessage(applicationException.getErrorMsg());
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Job Processing ID : " + jobProcessId, e);
			serviceResponse = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString())).build();
		}
		serviceResponse = serviceResponseBuilder.build();
		LOGGER.info("End - Fetch Element Regulator by Active status request completed with Job Processing ID : " + jobProcessId);
		return serviceResponse;
	}
}
