package com.iris.sdmx.dimesnsion.controller;

import java.util.ArrayList;
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
import com.iris.sdmx.dimesnsion.bean.DimensionModBean;
import com.iris.sdmx.dimesnsion.bean.DimensionRequestBean;
import com.iris.sdmx.dimesnsion.service.DimensionApprovalService;
import com.iris.sdmx.dimesnsion.validator.DimensionApprovalValidator;
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
public class DimensionApprovalController {

	/**
	 * 
	 */
	private static final Logger LOGGER = LogManager.getLogger(DimensionApprovalController.class);

	@Autowired
	private DimensionApprovalValidator dimensionApprovalValidator;

	@Autowired
	private DimensionApprovalService dimensionApprovalService;

	@Autowired
	private GenericService<UserMaster, Long> userMasterService;

	@Autowired
	EbrMetadataFlowService ebrMetadataFlowService;

	@PostMapping("/user/dimension")
	public ServiceResponse fetchApprovalRequest(@RequestHeader("JobProcessingId") String jobProcessId, @RequestBody ApprovalInputBean approvalInputBean) {
		LOGGER.info("START - Fetch approval request received with Job Processing ID : " + jobProcessId);
		ServiceResponse serviceResponse = null;
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(true);

		ApprovalInputBean validInputBean = new ApprovalInputBean();
		List<DimensionModBean> dimensionModBeanList = new ArrayList<>();

		try {
			dimensionApprovalValidator.validateFetchApprovalRequest(approvalInputBean, validInputBean, jobProcessId);
			System.out.println("Validation passed");
			dimensionModBeanList = dimensionApprovalService.getRequests(validInputBean);
			serviceResponseBuilder.setResponse(dimensionModBeanList);

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

	@PostMapping("/user/dimension/approveReject")
	public ServiceResponse approveRejectDimension(@RequestHeader("JobProcessingId") String jobProcessId, @RequestBody ApprovalInputBean approvalInputBean) {
		LOGGER.info("START - dimension approve/reject request received with Job Processing ID : " + jobProcessId);
		ServiceResponse serviceResponse = null;
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(true);
		ApprovalInputBean validInputBean = new ApprovalInputBean();
		try {
			dimensionApprovalValidator.validateApproveRejectDimensionRequest(approvalInputBean, validInputBean, jobProcessId);
			System.out.println("Validation passed");
			dimensionApprovalService.approveRejectDimensionRecord(validInputBean);
			ebrMetadataFlowService.ctlEntryForEbrMetadata(EbrMetadataFlowContstant.DIMENSION.getConstantVal());
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
		LOGGER.info("End - dimension approve/reject request received with Job Processing ID : " + jobProcessId);
		return serviceResponse;
	}

	@PostMapping(value = "/isDimensionCodePending")
	public ServiceResponse isDimCodePending(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody DimensionRequestBean dimensionRequestBean) {
		LOGGER.info("Request received to check if dimension pending for job processing Id : " + jobProcessId);
		boolean found = false;
		try {
			validateInputRequestForRecordPending(jobProcessId, dimensionRequestBean);

			UserMaster userMaster = userMasterService.getDataById(dimensionRequestBean.getUserId());
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

			found = dimensionApprovalService.isDimensionPending(dimensionRequestBean.getDimCode(), dimensionRequestBean.getAgencyMasterCode(), dimensionRequestBean.getConceptVersion());
			return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(found).build();

		} catch (ApplicationException e) {
			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(e.getErrorCode()).setStatusMessage(ObjectCache.getErrorCodeKey(e.getErrorCode())).build();
		} catch (Exception e) {

			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();

		}

	}

	@PostMapping(value = "/user/{userId}/isDimensionArrayPending")
	public ServiceResponse isDimensionArrayPending(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody List<DimensionMasterBean> dimensionRequestBean, @PathVariable("userId") Long userId) {
		LOGGER.info("Request received to check if dimension Array pending for job processing Id : " + jobProcessId);
		boolean found = false;
		try {
			validateInputRequestForPendingArray(jobProcessId, dimensionRequestBean);

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

			found = dimensionApprovalService.isDimensionArrayPending(dimensionRequestBean);
			return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(found).build();

		} catch (ApplicationException e) {
			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(e.getErrorCode()).setStatusMessage(ObjectCache.getErrorCodeKey(e.getErrorCode())).build();
		} catch (Exception e) {

			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();

		}

	}

	private void validateInputRequestForRecordPending(String jobProcessingId, DimensionRequestBean dimensionRequestBean) throws ApplicationException {
		if (UtilMaster.isEmpty(jobProcessingId) || UtilMaster.isEmpty(dimensionRequestBean.getDimCode()) || UtilMaster.isEmpty(dimensionRequestBean.getUserId()) || UtilMaster.isEmpty(dimensionRequestBean.getRoleId())) {
			throw new ApplicationException(ErrorCode.E0889.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString()));
		}
	}

	private void validateInputRequestForPendingArray(String jobProcessingId, List<DimensionMasterBean> dimensionRequestBeanList) throws ApplicationException {
		if (UtilMaster.isEmpty(jobProcessingId) || UtilMaster.isEmpty(dimensionRequestBeanList)) {
			throw new ApplicationException(ErrorCode.E0889.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString()));
		}
	}
}
