package com.iris.rbrToEbr.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ApplicationException;
import com.iris.model.UserMaster;
import com.iris.rbrToEbr.bean.EbrRbrFlowBean;
import com.iris.rbrToEbr.entity.EbrRbrFlow;
import com.iris.rbrToEbr.service.EbrDataConversionService;
import com.iris.rbrToEbr.validator.EbrDataConversionRequestValidator;
import com.iris.sdmx.util.SDMXConstants;
import com.iris.service.GenericService;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;

/**
 * @author vjadhav
 *
 */
@Controller
@RestController
@RequestMapping(value = "/service/ebrDataConversionLog")
public class EbrDataConversionController {

	private static final Logger LOGGER = LogManager.getLogger(EbrDataConversionController.class);

	@Autowired
	private GenericService<UserMaster, Long> userMasterService;

	@Autowired
	private EbrDataConversionService ebrDataConversionService;

	@Autowired
	private EbrDataConversionRequestValidator ebrDataConversionRequestValidator;

	@PostMapping(value = "/addEbrDataConversionLog/{userId}")
	public ServiceResponse addEbrConversionLog(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody EbrRbrFlowBean ebrDataConversionLogBean, @PathVariable("userId") Long userId) {
		LOGGER.info("START - Add Ebr data conversion log request received with Job Processing ID : " + jobProcessId);
		ServiceResponse serviceResponse = null;
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();

		try {
			ebrDataConversionRequestValidator.validateDataConversionRequest(jobProcessId, userId);
			boolean flag = ebrDataConversionService.addEbrLog(ebrDataConversionLogBean, userId);
			serviceResponseBuilder.setStatus(flag);
			serviceResponseBuilder.setStatusCode(SDMXConstants.SUCCESS_CODE);
			serviceResponseBuilder.setStatusMessage(SDMXConstants.SUCCESS_MESSAGE);
		} catch (ApplicationException applicationException) {
			// TODO Auto-generated catch block
			serviceResponseBuilder.setStatus(false);
			serviceResponseBuilder.setStatusCode(applicationException.getErrorCode());
			serviceResponseBuilder.setStatusMessage(applicationException.getErrorMsg());
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Job Processing ID : " + jobProcessId, applicationException);

		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Job Processing ID : " + jobProcessId, e);

			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ErrorCode.EC0033.toString()).build();
		}

		serviceResponse = serviceResponseBuilder.build();
		LOGGER.info("END - Add Ebr data conversion log request received with Job Processing ID : " + jobProcessId);
		return serviceResponse;
	}

	@PostMapping(value = "/fetchEbrDataConversionLog/{userId}/{entityCode}")
	public ServiceResponse fetchEbrConversionLog(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable("userId") Long userId, @PathVariable("entityCode") String entityCode) {
		LOGGER.info("START - fetch Ebr data conversion log request received with Job Processing ID : " + jobProcessId);
		ServiceResponse serviceResponse = null;
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		List<EbrRbrFlowBean> ebrRbrFlowBeanList = new ArrayList<>();
		try {
			ebrDataConversionRequestValidator.validateDataConversionRequest(jobProcessId, userId);
			List<EbrRbrFlow> ebrRbrlogList = ebrDataConversionService.getAllEbrRbrLogs(userId, entityCode);
			ebrRbrFlowBeanList = ebrDataConversionService.prepareEbrRbrLogsList(ebrRbrlogList);
			serviceResponseBuilder.setStatus(true);
			serviceResponseBuilder.setStatusCode(SDMXConstants.SUCCESS_CODE);
			serviceResponseBuilder.setStatusMessage(SDMXConstants.SUCCESS_MESSAGE);
			serviceResponseBuilder.setResponse(ebrRbrFlowBeanList);
		} catch (ApplicationException applicationException) {
			// TODO Auto-generated catch block
			serviceResponseBuilder.setStatus(false);
			serviceResponseBuilder.setStatusCode(applicationException.getErrorCode());
			serviceResponseBuilder.setStatusMessage(applicationException.getErrorMsg());
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Job Processing ID : " + jobProcessId, applicationException);
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Job Processing ID : " + jobProcessId, e);

			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ErrorCode.EC0033.toString()).build();
		}
		serviceResponse = serviceResponseBuilder.build();
		LOGGER.info("END - fetch Ebr data conversion log request received with Job Processing ID : " + jobProcessId);
		return serviceResponse;
	}

}
