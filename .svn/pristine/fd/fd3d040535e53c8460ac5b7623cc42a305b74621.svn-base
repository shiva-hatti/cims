package com.iris.controller;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ServiceException;
import com.iris.model.ReturnGroupLabelMapping;
import com.iris.service.impl.ReturnGroupLabelServiceV2;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author pmohite
 *
 */
@RestController
@RequestMapping("/service/returnGroupLabelController/V2")
public class ReturnGroupLabelControllerV2 {

	private static final Logger LOGGER = LogManager.getLogger(ReturnGroupLabelControllerV2.class);

	@Autowired
	private ReturnGroupLabelServiceV2 returnGroupLabelServiceV2;

	@PostMapping(value = "/getReturnGroupLabelList/{languageCode}")
	public ServiceResponse getReturnGroupLabelList(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable String languageCode) {
		ServiceResponse serviceResponse = null;
		LOGGER.info("getReturnGroupLabelList method Start for job processigid : " + jobProcessId);
		try {
			if (StringUtils.isEmpty(languageCode)) {
				languageCode = GeneralConstants.ENG_LANG.getConstantVal();
			}
			// en specific by default for null lang code
			List<ReturnGroupLabelMapping> returnGroupLabelList = returnGroupLabelServiceV2.getReturnGroupLabelList(languageCode);
			serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			serviceResponse.setResponse(returnGroupLabelList);
		} catch (Exception e) {
			LOGGER.error("Exception while fetching return group label list for job processigid : " + jobProcessId, e);
			serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
		}
		LOGGER.info("getReturnGroupLabelList method end for job processigid : " + jobProcessId);
		return serviceResponse;
	}

	@PostMapping(value = "/checkRetrunGroupLabelExist/{languageCode}/{returnGroupName}")
	public ServiceResponse checkRetrunGroupLabelExist(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable String languageCode, @PathVariable String returnGroupName) {
		Boolean flag = false;
		LOGGER.info("checkRetrunGroupLabelExist method Start for job processigid : " + jobProcessId);
		try {
			flag = returnGroupLabelServiceV2.checkRetrunGroupLabelExist(returnGroupName, languageCode, MethodConstants.CHECK_RETURN_GROUP_LABEL_EXIST.getConstantVal());
		} catch (Exception e) {
			LOGGER.error("Exception while checking ReturnGroup Label Exist info for job processingid %s : %s", jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(flag).build();
		LOGGER.info("request completed of checking ReturnGroup Label Exist for job processingid %s", jobProcessId);
		return response;
	}

	/**
	 * update the ReturnGroupLabel. This method is to update ReturnGroupLabel
	 *
	 * @param jobProcessId
	 * @param returnGroupLabelMapping
	 * @return
	 */
	@PostMapping(value = "/updateReturnGroupLabel")
	public ServiceResponse updateReturnGroupLabel(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ReturnGroupLabelMapping returnGroupLabelMapping) {
		Boolean flag = false;
		LOGGER.info("updateReturnGroupLabel method Start for job processigid : " + jobProcessId);
		try {
			returnGroupLabelMapping.setLastModifiedOn(new Date());
			returnGroupLabelMapping.setLastUpdateOn(new Date());
			flag = returnGroupLabelServiceV2.update(returnGroupLabelMapping);
		} catch (ServiceException e) {
			LOGGER.error("Request object not proper for update for job processigid : " + jobProcessId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0771.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0771.toString())).build();
		} catch (Exception e) {
			LOGGER.error("Exception while updating ReturnGroupLabelMapping info for job processingid %s : %s", jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(flag).build();
		LOGGER.info("request completed to update ReturnGroupLabelMapping for job processingid %s", jobProcessId);
		return response;
	}

}
