package com.iris.sdmx.upload.history.controller;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dto.FileDetailsBean;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.ebr.rbr.error.bean.CtlEbrRbrErrorMasterBean;
import com.iris.exception.ApplicationException;
import com.iris.model.FileDetails;
import com.iris.model.FilingStatus;
import com.iris.model.RoleType;
import com.iris.model.UserMaster;
import com.iris.sdmx.upload.bean.ElementAuditBean;
import com.iris.sdmx.upload.history.dto.SdmxFilingHitoryRequestBean;
import com.iris.sdmx.upload.history.service.SdmxFilingHistoryService;
import com.iris.service.GenericService;
import com.iris.service.impl.FileDetailsService;
import com.iris.service.impl.FilingStatusService;
import com.iris.util.JsonUtility;
import com.iris.util.UtilMaster;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;

/*
 * @author sdhone
 */

@RestController
@RequestMapping(value = "/service/sdmxFilingUploadHistory")
public class SdmxFilingUploadHistoryController {
	private static final Logger LOGGER = LogManager.getLogger(SdmxFilingUploadHistoryController.class);

	@Autowired
	private GenericService<UserMaster, Long> userMasterService;

	@Autowired
	private SdmxFilingHistoryService sdmxFilingHistoryService;

	@Autowired
	private FilingStatusService filingStatusService;

	@Autowired
	private FileDetailsService fileDetailsService;

	@PostMapping(value = "/getSdmxUploadFilingData")
	public ServiceResponse getSdmxUploadFilingData(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody SdmxFilingHitoryRequestBean sdmxFilingHitoryRequestBean) {
		LOGGER.info("Request received to get SDMX Upload Histoy for job processing Id : " + jobProcessId + " With Input request : " + JsonUtility.getGsonObject().toJson(sdmxFilingHitoryRequestBean));

		try {
			validateInputRequestToFetchDimensionData(jobProcessId, sdmxFilingHitoryRequestBean);

			UserMaster userMaster = userMasterService.getDataById(sdmxFilingHitoryRequestBean.getUserId());
			if (userMaster == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			} else if (userMaster.getRoleType() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0481.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0481.toString())).build();
			}

			RoleType roleType = userMaster.getRoleType();

			if (GeneralConstants.ENTITY_ROLE_TYPE_ID.getConstantLongVal().equals(roleType.getRoleTypeId())) {
				sdmxFilingHitoryRequestBean.setRoleId(roleType.getRoleTypeId());

			}

			if (GeneralConstants.AUDITOR_ROLE_TYPE_ID.getConstantLongVal().equals(roleType.getRoleTypeId()) || GeneralConstants.NBFC_ROLE_TYPE_ID.getConstantLongVal().equals(roleType.getRoleTypeId())) {
				// return with error response;
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0613.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0613.toString())).build();
			}

			List<FileDetailsBean> fileDetailsBeans = sdmxFilingHistoryService.getAllSdmxFilingdata(sdmxFilingHitoryRequestBean, roleType);
			return new ServiceResponseBuilder().setStatus(true).setResponse(fileDetailsBeans).build();
		} catch (ApplicationException e) {
			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(e.getErrorCode()).setStatusMessage(ObjectCache.getErrorCodeKey(e.getErrorCode())).build();
		} catch (Exception e) {
			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	@PostMapping(value = "/getSdmxUploadFilingStatus")
	public ServiceResponse getSdmxUploadFilingStatus(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody SdmxFilingHitoryRequestBean sdmxFilingHitoryRequestBean) {
		LOGGER.info("Request received to get SDMX Upload Histoy for job processing Id : " + jobProcessId + " With Input request : " + JsonUtility.getGsonObject().toJson(sdmxFilingHitoryRequestBean));

		try {
			validateInputRequestToFetchDimensionData(jobProcessId, sdmxFilingHitoryRequestBean);

			UserMaster userMaster = userMasterService.getDataById(sdmxFilingHitoryRequestBean.getUserId());
			if (userMaster == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			} else if (userMaster.getRoleType() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0481.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0481.toString())).build();
			}

			RoleType roleType = userMaster.getRoleType();

			if (GeneralConstants.AUDITOR_ROLE_TYPE_ID.getConstantLongVal().equals(roleType.getRoleTypeId()) || GeneralConstants.NBFC_ROLE_TYPE_ID.getConstantLongVal().equals(roleType.getRoleTypeId())) {
				// return with error response;
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0613.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0613.toString())).build();
			}

			List<FilingStatus> fileFilingStatus = filingStatusService.getActiveDataFor(null, null);
			return new ServiceResponseBuilder().setStatus(true).setResponse(filingStatusService.prepareDataOfFilingStatus(fileFilingStatus)).build();
		} catch (ApplicationException e) {
			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(e.getErrorCode()).setStatusMessage(ObjectCache.getErrorCodeKey(e.getErrorCode())).build();
		} catch (Exception e) {
			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	@PostMapping(value = "/getAllElementAuditDetailsAccordingFileDetails")
	public ServiceResponse getAllElementAuditDetailsAccordingFileDetails(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody SdmxFilingHitoryRequestBean sdmxFilingHitoryRequestBean) {
		LOGGER.info("Request received to get SDMX Upload Histoy for job processing Id : " + jobProcessId + " With Input request : " + JsonUtility.getGsonObject().toJson(sdmxFilingHitoryRequestBean));

		try {
			validateInputRequestToFetchDimensionData(jobProcessId, sdmxFilingHitoryRequestBean);

			UserMaster userMaster = userMasterService.getDataById(sdmxFilingHitoryRequestBean.getUserId());
			if (userMaster == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			} else if (userMaster.getRoleType() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0481.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0481.toString())).build();
			}

			RoleType roleType = userMaster.getRoleType();

			if (GeneralConstants.AUDITOR_ROLE_TYPE_ID.getConstantLongVal().equals(roleType.getRoleTypeId()) || GeneralConstants.NBFC_ROLE_TYPE_ID.getConstantLongVal().equals(roleType.getRoleTypeId())) {
				// return with error response;
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0613.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0613.toString())).build();
			}

			if (sdmxFilingHitoryRequestBean.getFilingStatusId() == GeneralConstants.CUSTOM_SUCCESS_ID.getConstantIntVal()) {
				List<CtlEbrRbrErrorMasterBean> flowErrorMasterList = sdmxFilingHistoryService.getErrorCodeAndMsg(sdmxFilingHitoryRequestBean.getFileDetailsId());
				if (CollectionUtils.isEmpty(flowErrorMasterList)) {
					List<ElementAuditBean> elementAuditBeans = sdmxFilingHistoryService.getElementAuditData(sdmxFilingHitoryRequestBean);
					return new ServiceResponseBuilder().setStatus(true).setResponse(elementAuditBeans).setStatusMessage(GeneralConstants.ELEMENT_DETAILS.getConstantVal()).build();
				} else {
					return new ServiceResponseBuilder().setStatus(true).setResponse(flowErrorMasterList).setStatusMessage(GeneralConstants.ERROR_CODE_AND_MSG_DETAILS.getConstantVal()).build();
				}
			}

			List<ElementAuditBean> elementAuditBeans = sdmxFilingHistoryService.getElementAuditData(sdmxFilingHitoryRequestBean);
			return new ServiceResponseBuilder().setStatus(true).setResponse(elementAuditBeans).setStatusMessage(GeneralConstants.ELEMENT_DETAILS.getConstantVal()).build();

		} catch (ApplicationException e) {
			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(e.getErrorCode()).setStatusMessage(ObjectCache.getErrorCodeKey(e.getErrorCode())).build();
		} catch (Exception e) {
			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	private void validateInputRequestToFetchDimensionData(String jobProcessingId, SdmxFilingHitoryRequestBean sdmxFilingHitoryRequestBean) throws ApplicationException {
		if (UtilMaster.isEmpty(jobProcessingId) || UtilMaster.isEmpty(sdmxFilingHitoryRequestBean.getUserId())) {
			throw new ApplicationException(ErrorCode.E0889.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString()));
		}
	}

}
