/**
 * 
 */
package com.iris.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dto.RequestApprovalBean;
import com.iris.dto.RequestApprovalInputBeanV2;
import com.iris.dto.RevisonRequestQueryOutputBean;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ApplicationException;
import com.iris.model.RevisionRequest;
import com.iris.service.RevisionRequestControllerServiceV2;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;
import com.iris.validator.RevisionRequestValidatorV2;

/**
 * @author apagaria
 *
 */
@RestController
@RequestMapping("/service/revisionRequest/V2/")
public class RevisionRequestControllerV2 {

	private static final Logger LOGGER = LoggerFactory.getLogger(RevisionRequestControllerV2.class);

	@Autowired
	private RevisionRequestControllerServiceV2 revisionRequestControllerServiceV2;

	@Autowired
	private RevisionRequestValidatorV2 revisionRequestValidatorV2;

	@PostMapping("/getPendingRevisionRequest")
	public ServiceResponse getPendingRevisionRequest(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody RequestApprovalInputBeanV2 requestApprovalInputBeanV2) {
		LOGGER.info(jobProcessId + " Request Received to fetch pending revision request for approval V2 ");
		List<RevisionRequest> revisionRequestLists = null;
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(false);
		try {
			// validate
			revisionRequestValidatorV2.validateRequestObjectToFetchPendingRevisionRequest(requestApprovalInputBeanV2);

			List<RevisonRequestQueryOutputBean> revisonRequestQueryOutputBeans = new ArrayList<>();
			revisonRequestQueryOutputBeans = revisionRequestControllerServiceV2.fetchPendingApprovalRequest(jobProcessId, requestApprovalInputBeanV2);

			// Converter
			revisionRequestLists = revisionRequestControllerServiceV2.convertQueryOutputToBean(jobProcessId, revisonRequestQueryOutputBeans);

			// Response
			serviceResponseBuilder.setStatus(true).setStatusCode(GeneralConstants.SUCCESS_CODE.getConstantVal()).setStatusMessage(GeneralConstants.SUCCESS.getConstantVal()).setResponse(revisionRequestLists).build();
		} catch (ApplicationException ae) {
			LOGGER.error(jobProcessId + " Exception occured to fetching pending revision request for approval V2 ", ae);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ae.getErrorCode()).setStatusMessage(ae.getErrorMsg()).build();
		} catch (Exception e) {
			LOGGER.error("Exception while fetching pending revision request for approval for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		LOGGER.info(jobProcessId + " Request completed to fetch pending revision request for approval V2 ");
		return serviceResponseBuilder.build();
	}

}
