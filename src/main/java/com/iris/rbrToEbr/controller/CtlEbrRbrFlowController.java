package com.iris.rbrToEbr.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.iris.model.ReturnsUploadDetails;
import com.iris.model.UserMaster;
import com.iris.rbrToEbr.bean.EbrRbrFlowBean;
import com.iris.rbrToEbr.entity.EbrRbrFlow;
import com.iris.rbrToEbr.service.CtlEbrRbrFlowService;
import com.iris.rbrToEbr.validator.CtlEbrRbrFlowValidator;
import com.iris.sdmx.ebrtorbr.service.EbrToRbrPreparationService;
import com.iris.sdmx.util.SDMXConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;

/**
 * @author vjadhav
 *
 */
@Controller
@RestController
@RequestMapping(value = "/service/ctlEbrRbrFlow")
public class CtlEbrRbrFlowController {

	/**
	 * 
	 */
	@Autowired
	private CtlEbrRbrFlowService ctlEbrRbrFlowService;

	@Autowired
	private CtlEbrRbrFlowValidator ctlEbrRbrFlowValidator;

	@Autowired
	private EbrToRbrPreparationService ebrToRbrPreparationService;

	private static final Logger LOGGER = LogManager.getLogger(CtlEbrRbrFlowController.class);

	/**
	 * @param jobProcessId
	 * @param userId
	 * @param roleId
	 * @param langCode
	 * @param ebrRbrFlowMasterBean
	 */
	@PostMapping(value = "/user/{userId}/role/{roleId}/lang/{langCode}/ctlEbrRbrFlow")
	public ServiceResponse saveEbrRbrFlowMaster(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable("userId") Long userId, @PathVariable(name = "roleId") Long roleId, @PathVariable("langCode") String langCode, @RequestBody List<EbrRbrFlowBean> ebrRbrFlowBeanList) {
		LOGGER.info("START - Add CTL Ebr Rbr flow master request received with Job Processing ID : " + jobProcessId);
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(false);
		try {
			LOGGER.info("Validation start for CTL Add Ebr Rbr flow master request received with Job Processing ID : " + jobProcessId);
			ctlEbrRbrFlowValidator.validateSaveCtlEbrRbrFlowMasterRequest(jobProcessId, userId);
			LOGGER.info("Validation end for CTL Add Ebr Rbr flow master request received with Job Processing ID : " + jobProcessId);
			if (!CollectionUtils.isEmpty(ebrRbrFlowBeanList)) {
				Map<Long, Long> responseMap = new HashMap<>();
				for (EbrRbrFlowBean ebrRbrFlowBean : ebrRbrFlowBeanList) {
					ebrRbrFlowBean.setReportingPeriod(new Date(ebrRbrFlowBean.getReportingPeriodLong()));

					EbrRbrFlow ebrRbrFlow = new EbrRbrFlow();
					BeanUtils.copyProperties(ebrRbrFlowBean, ebrRbrFlow);
					ReturnsUploadDetails returnsUploadDetails = new ReturnsUploadDetails();
					returnsUploadDetails.setUploadId(new Long(ebrRbrFlowBean.getUploadId()));
					UserMaster userMaster = new UserMaster();
					userMaster.setUserId(userId);

					ebrRbrFlow.setCreatedBy("ADMINISTRATOR");
					ebrRbrFlow.setCreatedDate(new Date());

					ebrRbrFlow = ctlEbrRbrFlowService.add(ebrRbrFlow);
					BeanUtils.copyProperties(ebrRbrFlow, ebrRbrFlowBean);
					ebrRbrFlowBean.setCreatedDate(null);
					ebrRbrFlowBean.setEntityName(null);
					responseMap.put(ebrRbrFlowBean.getReportingPeriodLong(), ebrRbrFlow.getControlId());
				}
				serviceResponseBuilder.setResponse(responseMap);
				serviceResponseBuilder.setStatus(true);
				serviceResponseBuilder.setStatusCode(SDMXConstants.SUCCESS_CODE);
				serviceResponseBuilder.setStatusMessage(SDMXConstants.SUCCESS_MESSAGE);
			} else {
				serviceResponseBuilder.setStatus(false);
				serviceResponseBuilder.setStatusCode(SDMXConstants.FAILURE_CODE);
				serviceResponseBuilder.setStatusMessage(SDMXConstants.FAILURE_MESSAGE);
			}

		} catch (ApplicationException applicationException) {
			LOGGER.error("Exception occured while fetching sdmx file audit records " + ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Job Processing ID : " + jobProcessId, applicationException);
			serviceResponseBuilder.setStatusCode(applicationException.getErrorCode());
			serviceResponseBuilder.setStatusMessage(applicationException.getErrorMsg());
		} catch (Exception e) {
			LOGGER.error("Exception occured while fetching sdmx file audit records for job processing Id : " + jobProcessId + "", e);
			serviceResponseBuilder.setStatusCode(ErrorCode.EC0033.toString());
			serviceResponseBuilder.setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString()));
		}
		LOGGER.info("END - Add CTL Ebr Rbr flow master request received with Job Processing ID : " + jobProcessId);
		return serviceResponseBuilder.build();
	}

	@GetMapping(value = "/user/{userId}/role/{roleId}/lang/{langCode}/getJobSequence/jobId/{jobTempId}")
	public ServiceResponse getJobSequence(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable("userId") Long userId, @PathVariable(name = "roleId") Long roleId, @PathVariable("langCode") String langCode, @PathVariable("jobTempId") String jobTempId) {
		LOGGER.info("START - Add CTL Ebr Rbr flow master request received with Job Processing ID : " + jobProcessId);
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(false);
		try {
			LOGGER.info("Validation start for CTL Add Ebr Rbr flow master request received with Job Processing ID : " + jobProcessId);
			ctlEbrRbrFlowValidator.validateSaveCtlEbrRbrFlowMasterRequest(jobProcessId, userId);
			LOGGER.info("Validation end for CTL Add Ebr Rbr flow master request received with Job Processing ID : " + jobProcessId);
			serviceResponseBuilder.setResponse(ebrToRbrPreparationService.getJobId(jobTempId));
			serviceResponseBuilder.setStatus(true);
			serviceResponseBuilder.setStatusCode(SDMXConstants.SUCCESS_CODE);
			serviceResponseBuilder.setStatusMessage(SDMXConstants.SUCCESS_MESSAGE);
		} catch (ApplicationException applicationException) {
			LOGGER.error("Exception occured while fetching sdmx file audit records " + ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Job Processing ID : " + jobProcessId, applicationException);
			serviceResponseBuilder.setStatusCode(applicationException.getErrorCode());
			serviceResponseBuilder.setStatusMessage(applicationException.getErrorMsg());
		} catch (Exception e) {
			LOGGER.error("Exception occured while fetching sdmx file audit records for job processing Id : " + jobProcessId + "", e);
			serviceResponseBuilder.setStatusCode(ErrorCode.EC0033.toString());
			serviceResponseBuilder.setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString()));
		}
		LOGGER.info("END - Add CTL Ebr Rbr flow master request received with Job Processing ID : " + jobProcessId);
		return serviceResponseBuilder.build();
	}

}
