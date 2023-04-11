/**
 * 
 */
package com.iris.controller;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dto.OwnerReturn;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ApplicationException;
import com.iris.exception.ServiceException;
import com.iris.model.ReturnRegulatorMapping;
import com.iris.service.ReturnRegulatorControllerServiceV2;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;
import com.iris.validator.ReturnRegulatorValidatorV2;

/**
 * @author apagaria
 *
 */
@RestController
@RequestMapping("/service/returnRegulatorController/V2")
public class ReturnRegulatorControllerV2 {

	/**
	 * 
	 */
	private static final Logger LOGGER = LogManager.getLogger(ReturnRegulatorControllerV2.class);

	/**
	 * 
	 */
	@Autowired
	private ReturnRegulatorValidatorV2 returnRegulatorValidatorV2;

	/**
	 * 
	 */
	@Autowired
	private ReturnRegulatorControllerServiceV2 returnRegulatorControllerServiceV2;

	/**
	 * @param jobProcessId
	 * @param fetchReturnByUserRoleNRegulatorRequestV2
	 * @return
	 */
	@GetMapping(value = "/fetchReturnByRoleNRegulator/user/{userId}")
	public ServiceResponse getMappedAndUnMappedReturnList(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable Long userId) {
		LOGGER.info(GeneralConstants.JOB_PROCESSING_ID.getConstantVal() + jobProcessId + ", FetchReturnByUserRoleNRegulatorRequestV2 Start with user = " + userId);
		ServiceResponse serviceResponse = null;
		try {

			// Validation Request
			returnRegulatorValidatorV2.validateFetchReturnByUserRoleNRegulatorRequestV2(userId, jobProcessId);

			// Fetch Return process
			Set<OwnerReturn> ownerReturnSet = returnRegulatorControllerServiceV2.fetchReturnByUserRoleNRegulatorRequestV2(userId, jobProcessId);

			if (!CollectionUtils.isEmpty(ownerReturnSet)) {
				serviceResponse = new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.SUCCESS_CODE.getConstantVal()).setStatusMessage(GeneralConstants.SUCCESS.getConstantVal()).setResponse(ownerReturnSet).build();
			} else {
				serviceResponse = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0431.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0431.toString())).build();
			}
		} catch (ApplicationException aex) {
			LOGGER.error(GeneralConstants.JOB_PROCESSING_ID.getConstantVal() + jobProcessId + ", Exception while FetchReturnByUserRoleNRegulatorRequestV2 ", aex);
			serviceResponse = new ServiceResponseBuilder().setStatus(false).setStatusCode(aex.getErrorCode()).setStatusMessage(aex.getErrorMsg()).build();
		} catch (ServiceException e) {
			LOGGER.error(GeneralConstants.JOB_PROCESSING_ID.getConstantVal() + jobProcessId + ", Exception while FetchReturnByUserRoleNRegulatorRequestV2 ", e);
			serviceResponse = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0431.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0431.toString())).build();
		} catch (Exception e) {
			LOGGER.error(GeneralConstants.JOB_PROCESSING_ID.getConstantVal() + jobProcessId + ", Exception while FetchReturnByUserRoleNRegulatorRequestV2 ", e);
			serviceResponse = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		LOGGER.info(GeneralConstants.JOB_PROCESSING_ID.getConstantVal() + jobProcessId + ", FetchReturnByUserRoleNRegulatorRequestV2 End ");
		return serviceResponse;

	}

}
