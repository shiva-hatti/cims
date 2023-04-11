package com.iris.sdmx.agency.master.controller;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ApplicationException;
import com.iris.model.UserMaster;
import com.iris.sdmx.agency.master.bean.SdmxAgencyMasterBean;
import com.iris.sdmx.agency.master.bean.SdmxAgencyRequestBean;
import com.iris.sdmx.agency.master.service.SdmxAgencyMasterService;
import com.iris.sdmx.agency.master.validator.SdmxAgencyMasterValidator;
import com.iris.sdmx.util.SDMXConstants;
import com.iris.service.GenericService;
import com.iris.util.JsonUtility;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;

@RestController
@RequestMapping(value = "/service/sdmxAgencyMasterController")
public class SdmxAgencyMasterController {

	private static final Logger LOGGER = LogManager.getLogger(SdmxAgencyMasterController.class);

	@Autowired
	private GenericService<UserMaster, Long> userMasterService;

	@Autowired
	private SdmxAgencyMasterService sdmxGroupMasterService;

	@Autowired
	private SdmxAgencyMasterValidator sdmxAgencyMasterValidator;

	@PostMapping(value = "/fetchActiveSdmxAgencyMaster")
	public ServiceResponse getActiveSdmxAgencyMaster(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody SdmxAgencyRequestBean sdmxGroupRequestBean) {

		LOGGER.info("Request received to SDMX Group Master for job processing Id : " + jobProcessId + " With Input request : " + JsonUtility.getGsonObject().toJson(sdmxGroupRequestBean));

		try {

			UserMaster userMaster = userMasterService.getDataById(sdmxGroupRequestBean.getUserId());
			if (userMaster == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			} else if (userMaster.getRoleType() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0481.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0481.toString())).build();
			}

			List<SdmxAgencyMasterBean> sdmxGroupMasterBean = sdmxGroupMasterService.getActiveRecord();

			if (!CollectionUtils.isEmpty(sdmxGroupMasterBean)) {
				LOGGER.info("Size of  SDMX Group Master Data" + sdmxGroupMasterBean.size());
				return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(sdmxGroupMasterBean).build();
			} else {
				LOGGER.info("Size of  SDMX Group Master Data Not Found Any record ");
				return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(null).build();
			}

		} catch (Exception e) {
			LOGGER.error("Exception occured for job processing Id : " + jobProcessId + "", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusMessage(ErrorCode.E0808.toString()).setStatusCode(ObjectCache.getErrorCodeKey(ErrorCode.E0808.toString())).build();
		}
	}

	/**
	 * @param jobProcessId
	 * @param userId
	 * @param roleId
	 * @param langCode
	 * @return
	 */
	@GetMapping(value = "/user/{userId}/role/{roleId}/lang/{langCode}/fetchActiveAgencyNameList")
	public ServiceResponse getActiveAgencyNameList(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable("userId") Long userId, @PathVariable("roleId") Long roleId, @PathVariable("langCode") String langCode) {

		LOGGER.info("@fetchActiveAgencyName ListRequest received to SDMX Group Master for job processing Id : " + jobProcessId);
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(false);
		try {

			// Validation
			sdmxAgencyMasterValidator.validateFetchAgencyMasterListRequest(jobProcessId, userId);
			// Fetch agency name
			List<SdmxAgencyMasterBean> sdmxGroupMasterBean = sdmxGroupMasterService.findAgencyNameByStatus(true);

			LOGGER.info("@fetchActiveAgencyName Size of  SDMX Group Master Data" + sdmxGroupMasterBean.size());
			if (!CollectionUtils.isEmpty(sdmxGroupMasterBean)) {
				// Success response
				serviceResponseBuilder.setStatus(true);
				serviceResponseBuilder.setStatusCode(SDMXConstants.SUCCESS_CODE);
				serviceResponseBuilder.setStatusMessage(SDMXConstants.SUCCESS_MESSAGE);
				serviceResponseBuilder.setResponse(sdmxGroupMasterBean);
			} else {
				serviceResponseBuilder.setStatusCode(SDMXConstants.FAILURE_CODE);
				serviceResponseBuilder.setStatusMessage(SDMXConstants.FAILURE_MESSAGE);
			}
		} catch (ApplicationException applicationException) {
			LOGGER.error("@fetchActiveAgencyName Exception occured while fetch agency master records " + ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Job Processing ID : " + jobProcessId, applicationException);
			serviceResponseBuilder.setStatusCode(applicationException.getErrorCode());
			serviceResponseBuilder.setStatusMessage(applicationException.getErrorMsg());
		} catch (Exception e) {
			LOGGER.error("@fetchActiveAgencyName Exception occured while fetch agency master for job processing Id : " + jobProcessId + "", e);
			serviceResponseBuilder.setStatusCode(ErrorCode.EC0033.toString());
			serviceResponseBuilder.setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString()));
		}
		return serviceResponseBuilder.build();
	}
}
