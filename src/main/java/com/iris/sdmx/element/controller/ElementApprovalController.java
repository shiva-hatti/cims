package com.iris.sdmx.element.controller;

import java.util.List;

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
import com.iris.ebr.metadata.flow.constant.EbrMetadataFlowContstant;
import com.iris.ebr.metadata.flow.service.EbrMetadataFlowService;
import com.iris.exception.ApplicationException;
import com.iris.model.RoleType;
import com.iris.model.UserMaster;
import com.iris.sdmx.dimesnsion.bean.DimensionMasterBean;
import com.iris.sdmx.element.bean.ElementApprovalOutputBean;
import com.iris.sdmx.element.bean.ElementListRequestBean;
import com.iris.sdmx.element.service.ElementApprovalService;
import com.iris.sdmx.element.validator.ElementApprovalValidator;
import com.iris.sdmx.status.bean.AdminStatusBean;
import com.iris.sdmx.status.service.AdminStatusService;
import com.iris.sdmx.userMangement.bean.ApprovalInputBean;
import com.iris.service.GenericService;
import com.iris.util.UtilMaster;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;

/**
 * @author vjadhav
 *
 */

@RestController
@RequestMapping("/service/sdmx/approval")
public class ElementApprovalController {
	/**
	 * 
	 */
	private static final Logger LOGGER = LogManager.getLogger(ElementApprovalController.class);

	@Autowired
	private ElementApprovalValidator elementApprovalValidator;

	@Autowired
	private ElementApprovalService elementApprovalService;

	@Autowired
	private AdminStatusService adminStatusService;

	@Autowired
	private GenericService<UserMaster, Long> userMasterService;

	@Autowired
	EbrMetadataFlowService ebrMetadataFlowService;

	/**
	 * @param jobProcessId
	 * @return
	 */
	@PostMapping("/user/element")
	public ServiceResponse fetchApprovalRequest(@RequestHeader("JobProcessingId") String jobProcessId, @RequestBody ApprovalInputBean approvalInputBean) {
		LOGGER.info("START - Fetch approval request received with Job Processing ID : " + jobProcessId);
		ServiceResponse serviceResponse = null;
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(true);
		ApprovalInputBean validInputBean = new ApprovalInputBean();
		try {
			elementApprovalValidator.validateFetchApprovalRequest(approvalInputBean, validInputBean, jobProcessId);

			List<ElementApprovalOutputBean> elementRequestList = elementApprovalService.getRequests(validInputBean);
			serviceResponseBuilder.setResponse(elementRequestList);

		} catch (ApplicationException applicationException) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + " Transaction ID : " + jobProcessId + ", Error Code - " + applicationException.getErrorCode() + ", Error Msg - " + applicationException.getErrorMsg(), applicationException);
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + " Transaction ID : " + jobProcessId, applicationException);
			serviceResponseBuilder.setStatus(false);
			serviceResponseBuilder.setStatusCode(applicationException.getErrorCode());
			serviceResponseBuilder.setStatusMessage(applicationException.getErrorMsg());
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Job Processing ID : " + jobProcessId, e);
			serviceResponseBuilder = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString()));

		}
		serviceResponse = serviceResponseBuilder.build();
		LOGGER.info("End - Fetch approval request completed with Job Processing ID : " + jobProcessId);
		return serviceResponse;
	}

	@PostMapping("/user/adminStatusList/{userId}/{roleId}/{langCode}")
	public ServiceResponse getAdminStatusList(@RequestHeader("JobProcessingId") String jobProcessId, @PathVariable("userId") Long userId, @PathVariable(name = "roleId") Long roleId, @PathVariable("langCode") String langCode) {
		LOGGER.info("START - Fetch admin status list request received with Job Processing ID : " + jobProcessId);
		ServiceResponse serviceResponse = null;
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(true);
		try {
			List<AdminStatusBean> adminStatusList = adminStatusService.findByActiveStatus(Boolean.TRUE);
			serviceResponseBuilder.setResponse(adminStatusList);
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Job Processing ID : " + jobProcessId, e);
			serviceResponseBuilder = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString()));
		}
		serviceResponse = serviceResponseBuilder.build();
		LOGGER.info("End - Fetch admin status list request received with Job Processing ID : " + jobProcessId);
		return serviceResponse;
	}

	@PostMapping("/user/element/approveReject")
	public ServiceResponse approveRejectElement(@RequestHeader("JobProcessingId") String jobProcessId, @RequestBody ApprovalInputBean approvalInputBean) {
		LOGGER.info("START - Element approve/reject request received with Job Processing ID : " + jobProcessId);
		ServiceResponse serviceResponse = null;
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(true);
		ApprovalInputBean validInputBean = new ApprovalInputBean();
		try {
			elementApprovalValidator.validateApproveRejectElementRequest(approvalInputBean, validInputBean, jobProcessId);
			elementApprovalService.approveRejectElementRecord(validInputBean);
			ebrMetadataFlowService.ctlEntryForEbrMetadata(EbrMetadataFlowContstant.ELEMENT.getConstantVal());
		} catch (ApplicationException applicationException) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + " Transaction ID : " + jobProcessId + ", Error Code - " + applicationException.getErrorCode() + ", Error Msg - " + applicationException.getErrorMsg(), applicationException);
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + " Transaction ID : " + jobProcessId, applicationException);
			serviceResponseBuilder.setStatus(false);
			serviceResponseBuilder.setStatusCode(applicationException.getErrorCode());
			serviceResponseBuilder.setStatusMessage(applicationException.getErrorMsg());
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Job Processing ID : " + jobProcessId, e);
			serviceResponseBuilder = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString()));
		}

		serviceResponse = serviceResponseBuilder.build();
		LOGGER.info("End - Element approve/reject request received with Job Processing ID : " + jobProcessId);
		return serviceResponse;
	}

	@PostMapping(value = "/user/{userId}/isElementArrayPending")
	public ServiceResponse isElementArrayPending(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody List<ElementListRequestBean> elementRequestBean, @PathVariable("userId") Long userId) {
		LOGGER.info("Request received to check if Element Array pending for job processing Id : " + jobProcessId);
		boolean found = false;
		try {
			validateInputRequestForPendingArray(jobProcessId, elementRequestBean);

			UserMaster userMaster = userMasterService.getDataById(userId);
			if (userMaster == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			} else if (userMaster.getRoleType() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0481.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0481.toString())).build();
			}

			RoleType roleType = userMaster.getRoleType();

			if (GeneralConstants.ENTITY_ROLE_TYPE_ID.getConstantLongVal().equals(roleType.getRoleTypeId())) {
				// return with error response;
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0481.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0481.toString())).build();
			}

			found = elementApprovalService.isElementArrayPending(elementRequestBean);
			return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(found).build();

		} catch (ApplicationException e) {
			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(e.getErrorCode()).setStatusMessage(ObjectCache.getErrorCodeKey(e.getErrorCode())).build();
		} catch (Exception e) {

			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();

		}

	}

	private void validateInputRequestForPendingArray(String jobProcessingId, List<ElementListRequestBean> dimensionRequestBeanList) throws ApplicationException {
		if (UtilMaster.isEmpty(jobProcessingId) || UtilMaster.isEmpty(dimensionRequestBeanList)) {
			throw new ApplicationException(ErrorCode.E0889.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString()));
		}
	}

}
