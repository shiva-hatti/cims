package com.iris.sdmx.element.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iris.caching.ObjectCache;
import com.iris.exception.ApplicationException;
import com.iris.sdmx.element.service.SdmxElementDependencyTypeService;
import com.iris.sdmx.element.service.SdmxElementFlowTypeService;
import com.iris.service.impl.UserMasterService;
import com.iris.util.constant.ErrorCode;

/**
 * @author vjadhav
 *
 */
@Component
public class SdmxElementFlowValidator {

	private static final Logger LOGGER = LogManager.getLogger(SdmxElementFlowValidator.class);

	@Autowired
	private SdmxElementFlowTypeService SdmxElementFlowTypeService;

	@Autowired
	private UserMasterService userMasterService;

	public void validateFlowRequest(String jobProcessId, Long userId) throws ApplicationException {

		validateUser(userId);

		validateJobProcessingId(jobProcessId);

	}

	private void validateUser(Long userId) throws ApplicationException {
		if (userId == null) {
			throw new ApplicationException(ErrorCode.E1554.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1554.toString()));
		} else if (userMasterService.getDataById(userId) == null) {
			throw new ApplicationException(ErrorCode.E0638.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString()));
		}
	}

	private void validateJobProcessingId(String jobProcessId) throws ApplicationException {
		if (jobProcessId == null || jobProcessId == "") {
			throw new ApplicationException(ErrorCode.E1450.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1450.toString()));
		}

	}

}
