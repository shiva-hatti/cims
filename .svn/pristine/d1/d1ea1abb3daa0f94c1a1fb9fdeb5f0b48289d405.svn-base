package com.iris.rbrToEbr.controller;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import com.iris.rbrToEbr.bean.EbrRbrFlowMasterBean;
import com.iris.rbrToEbr.entity.EbrRbrFlowMaster;
import com.iris.rbrToEbr.service.EbrRbrFlowMasterService;
import com.iris.rbrToEbr.validator.EbrRbrFlowMasterValidator;
import com.iris.sdmx.util.SDMXConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;

/**
 * @author vjadhav
 *
 */
@Controller
@RestController
@RequestMapping(value = "/service/ebrRbrFlowMaster")
public class EbrRbrFlowMasterController {

	/**
	 * 
	 */
	@Autowired
	private EbrRbrFlowMasterService ebrRbrFlowMasterService;

	@Autowired
	private EbrRbrFlowMasterValidator ebrRbrFlowMasterValidator;

	private static final Logger LOGGER = LogManager.getLogger(EbrRbrFlowMasterController.class);

	/**
	 * @param jobProcessId
	 * @param userId
	 * @param roleId
	 * @param langCode
	 * @param ebrRbrFlowMasterBean
	 */
	@PostMapping(value = "/user/{userId}/role/{roleId}/lang/{langCode}/saveEbrRbrFlowMaster")
	public ServiceResponse saveEbrRbrFlowMaster(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable("userId") Long userId, @PathVariable(name = "roleId") Long roleId, @PathVariable("langCode") String langCode, @RequestBody EbrRbrFlowMasterBean ebrRbrFlowMasterBean) {
		LOGGER.info("START - Add Ebr Rbr flow master request received with Job Processing ID : " + jobProcessId);
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(false);
		try {
			LOGGER.info("Validation start for Add Ebr Rbr flow master request received with Job Processing ID : " + jobProcessId);
			ebrRbrFlowMasterValidator.validateSaveEbrRbrFlowMasterRequest(jobProcessId, userId);
			LOGGER.info("Validation end for Add Ebr Rbr flow master request received with Job Processing ID : " + jobProcessId);
			EbrRbrFlowMaster ebrRbrFlowMaster = new EbrRbrFlowMaster();
			BeanUtils.copyProperties(ebrRbrFlowMasterBean, ebrRbrFlowMaster);
			ebrRbrFlowMaster = ebrRbrFlowMasterService.add(ebrRbrFlowMaster);
			BeanUtils.copyProperties(ebrRbrFlowMaster, ebrRbrFlowMasterBean);
			serviceResponseBuilder.setStatus(true);
			serviceResponseBuilder.setStatusCode(SDMXConstants.SUCCESS_CODE);
			serviceResponseBuilder.setStatusMessage(SDMXConstants.SUCCESS_MESSAGE);
			serviceResponseBuilder.setResponse(ebrRbrFlowMasterBean);
		} catch (ApplicationException applicationException) {
			LOGGER.error("Exception occured while fetching sdmx file audit records " + ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Job Processing ID : " + jobProcessId, applicationException);
			serviceResponseBuilder.setStatusCode(applicationException.getErrorCode());
			serviceResponseBuilder.setStatusMessage(applicationException.getErrorMsg());
		} catch (Exception e) {
			LOGGER.error("Exception occured while fetching sdmx file audit records for job processing Id : " + jobProcessId + "", e);
			serviceResponseBuilder.setStatusCode(ErrorCode.EC0033.toString());
			serviceResponseBuilder.setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString()));
		}
		LOGGER.info("END - Add Ebr Rbr flow master request received with Job Processing ID : " + jobProcessId);
		return serviceResponseBuilder.build();
	}

	/**
	 * @param jobProcessId
	 * @param userId
	 * @param roleId
	 * @param langCode
	 * @param flowId
	 * @return
	 */
	@GetMapping(value = "/user/{userId}/role/{roleId}/lang/{langCode}/flow/{flowId}")
	public ServiceResponse getFlowDetailWithFlowId(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable("userId") Long userId, @PathVariable(name = "roleId") Long roleId, @PathVariable("langCode") String langCode, @PathVariable("flowId") Integer flowId) {
		LOGGER.info("START - Fetch flow detail with flow id request received with Job Processing ID : " + jobProcessId + ", Flow id - " + flowId);
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(false);
		try {
			LOGGER.info("Validation start for Fetch flow detail with flow id request received with Job Processing ID : " + jobProcessId);
			ebrRbrFlowMasterValidator.validateFetchEbrRbrFlowMasterRequest(jobProcessId, userId);
			LOGGER.info("Validation end for Fetch flow detail with flow id request received with Job Processing ID : " + jobProcessId);
			List<EbrRbrFlowMasterBean> ebrRbrFlowMasterBeanList = ebrRbrFlowMasterService.getDataByFlowIdd(flowId, jobProcessId);
			if (!CollectionUtils.isEmpty(ebrRbrFlowMasterBeanList)) {
				LOGGER.info("Fetch flow detail with flow id request received with Job Processing ID : " + jobProcessId + ", Size  - " + ebrRbrFlowMasterBeanList.size());
				serviceResponseBuilder.setStatus(true);
				serviceResponseBuilder.setStatusCode(SDMXConstants.SUCCESS_CODE);
				serviceResponseBuilder.setStatusMessage(SDMXConstants.SUCCESS_MESSAGE);
				serviceResponseBuilder.setResponse(ebrRbrFlowMasterBeanList);
			} else {
				LOGGER.info("Fetch flow detail with flow id request received with Job Processing ID : " + jobProcessId + ", No Data found");
				serviceResponseBuilder.setStatus(false);
				serviceResponseBuilder.setStatusCode(SDMXConstants.FAILURE_CODE);
				serviceResponseBuilder.setStatusMessage(SDMXConstants.FAILURE_MESSAGE);
			}
		} catch (ApplicationException applicationException) {
			LOGGER.error("Exception occured while Fetch flow detail with flow id request " + ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Job Processing ID : " + jobProcessId, applicationException);
			serviceResponseBuilder.setStatusCode(applicationException.getErrorCode());
			serviceResponseBuilder.setStatusMessage(applicationException.getErrorMsg());
		} catch (Exception e) {
			LOGGER.error("Exception occured while Fetch flow detail with flow id request for job processing Id : " + jobProcessId + "", e);
			serviceResponseBuilder.setStatusCode(ErrorCode.EC0033.toString());
			serviceResponseBuilder.setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString()));
		}
		LOGGER.info("END - Fetch flow detail with flow id request received with Job Processing ID : " + jobProcessId + ", Flow id - " + flowId);
		return serviceResponseBuilder.build();
	}

}
