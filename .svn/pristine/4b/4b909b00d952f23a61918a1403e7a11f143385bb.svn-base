package com.iris.rbrToEbr.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iris.caching.ObjectCache;
import com.iris.exception.ApplicationException;
import com.iris.sdmx.element.validator.SdmxElementclassificationValidator;
import com.iris.service.impl.UserMasterService;
import com.iris.util.constant.ErrorCode;

/**
 * @author vjadhav
 *
 */
@Component
public class EbrDataConversionRequestValidator {

	private static final Logger LOGGER = LogManager.getLogger(EbrDataConversionRequestValidator.class);

	@Autowired
	private UserMasterService userMasterService;

	public void validateDataConversionRequest(String jobProcessId, Long userId) throws ApplicationException {

		validateUser(userId);

		validateJobProcessingId(jobProcessId);

	}

	private void validateUser(Long userId) throws ApplicationException {
		if (userId == null) {
			throw new ApplicationException(ErrorCode.EC0458.toString(), ObjectCache.getErrorCodeKey(ErrorCode.EC0458.toString()));
		} else if (userMasterService.getDataById(userId) == null) {
			throw new ApplicationException(ErrorCode.EC0457.toString(), ObjectCache.getErrorCodeKey(ErrorCode.EC0457.toString()));
		}
	}

	private void validateJobProcessingId(String jobProcessId) throws ApplicationException {
		if (jobProcessId == null || jobProcessId == "") {
			throw new ApplicationException(ErrorCode.EC0459.toString(), ObjectCache.getErrorCodeKey(ErrorCode.EC0459.toString()));
		}

	}
}
