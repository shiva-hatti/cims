/**
 * 
 */
package com.iris.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.reflect.TypeToken;
import com.iris.crossvalidation.service.CrossValidationStatusService;
import com.iris.crossvalidation.service.dto.CrossValidationDto;
import com.iris.dto.ServiceResponse;
import com.iris.exception.ServiceException;
import com.iris.model.Scheduler;
import com.iris.model.SchedulerLog;
import com.iris.model.WebServiceComponentUrl;
import com.iris.service.GenericService;
import com.iris.util.JsonUtility;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;
import com.iris.webservices.client.CIMSRestWebserviceClient;

/**
 * @author Svishwakarma
 *
 */

@RestController
public class CrossValidationMailSchedulerController {

	private static final Logger LOGGER = LogManager.getLogger(CrossValidationMailSchedulerController.class);

	@Autowired
	private GenericService<WebServiceComponentUrl, Long> webServiceComponentService;

	@Autowired
	private CrossValidationStatusService crossService;

	@Value("${scheduler.code.crossStatus}")
	private String schedulerCode;

	//	private static final String SCHEDULER_CODE = "CROSS_VALIDATION_SCHEDULER";

	private String jobProcessingId;

	@Scheduled(cron = "${cron.crosStatusScheduler}")
	public void crossValidationMailSend() throws Exception {
		jobProcessingId = UUID.randomUUID().toString();
		LOGGER.info("Cross Validation scheduler started with Job processing ID {}", jobProcessingId);
		Long schedulerLogId = null;
		Scheduler scheduler = getSchedulerStatus();

		if (scheduler != null) {
			if (scheduler.getIsRunning().equals(Boolean.TRUE)) {
				LOGGER.error("Error while starting Cross validation scheduler -> Reason : Schduler is alrady running ");
				return;
			}
			try {
				// Step 1 : Scheduler start entry
				schedulerLogId = makeSchedulerStartEntry(0l, scheduler.getSchedulerId());
				if (schedulerLogId == null) {
					LOGGER.error("Error while starting scheduler ,  Cross validation -> Reason : Schduler Log ID not received ");
					return;
				}
			} catch (Exception e) {
				LOGGER.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
				return;
			}
			List<String> total = new ArrayList<>();
			CrossValidationDto input = new CrossValidationDto();
			total = crossService.generateReportForScheduler(input, jobProcessingId, schedulerCode);
			makeSchedulerStopEntry(total.size(), schedulerLogId, scheduler.getSchedulerId());
		}
	}

	private void makeSchedulerStopEntry(int successfullyProcessed, Long schedulerLogId, Long schedulerId) {
		try {
			if (schedulerLogId != null) {
				SchedulerLog schedulerLog = new SchedulerLog();
				schedulerLog.setSuccessfullyProcessedCount(Long.valueOf(successfullyProcessed));
				schedulerLog.setId(schedulerLogId);
				Scheduler scheduler = new Scheduler();
				scheduler.setSchedulerId(schedulerId);
				scheduler.setIsRunning(false);
				schedulerLog.setSchedulerIdFk(scheduler);
				CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

				WebServiceComponentUrl componentUrl = getWebServiceComponentURL(GeneralConstants.ADD_UPDATE_SCHEDULER_LOG.getConstantVal(), CIMSRestWebserviceClient.HTTP_METHOD_TYPE_POST);

				Map<String, String> headerMap = new HashMap<>();
				headerMap.put(GeneralConstants.JOB_PROCESSING_ID.getConstantVal(), jobProcessingId);

				String responsestring = restServiceClient.callRestWebServiceWithMultipleHeader(componentUrl, schedulerLog, null, headerMap);

				ServiceResponse serviceResponse = JsonUtility.getGsonObject().fromJson(responsestring, ServiceResponse.class);

				if (!serviceResponse.isStatus()) {
					LOGGER.info("Error while stopping scheduler -> Reason : Schduler is alrady stopped ");
				} else {
					LOGGER.info("Scheduler stopped successfully ");
				}
			} else {
				LOGGER.error("Scheduler Log Id not found");
			}
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}

	}

	private Long makeSchedulerStartEntry(Long totalRecordCount, Long schedulerId) {
		try {
			SchedulerLog schedulerLog = new SchedulerLog();
			schedulerLog.setTakedRecordsCount(totalRecordCount);
			Scheduler scheduler = new Scheduler();
			scheduler.setSchedulerId(schedulerId);
			scheduler.setIsRunning(true);
			schedulerLog.setSchedulerIdFk(scheduler);
			CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

			WebServiceComponentUrl componentUrl = getWebServiceComponentURL(GeneralConstants.ADD_UPDATE_SCHEDULER_LOG.getConstantVal(), CIMSRestWebserviceClient.HTTP_METHOD_TYPE_POST);

			Map<String, String> headerMap = new HashMap<>();
			headerMap.put(GeneralConstants.JOB_PROCESSING_ID.getConstantVal(), jobProcessingId);

			String responsestring = restServiceClient.callRestWebServiceWithMultipleHeader(componentUrl, schedulerLog, null, headerMap);

			Type listToken = new TypeToken<ServiceResponse>() {
			}.getType();
			ServiceResponse serviceResponse = JsonUtility.getGsonObject().fromJson(responsestring, listToken);
			Long schedulerLogId = null;

			if (!serviceResponse.isStatus()) {
				LOGGER.error("False status received from API with status message {}", serviceResponse.getStatusCode());
				return null;
			} else {
				listToken = new TypeToken<SchedulerLog>() {
				}.getType();
				SchedulerLog schedulerLog1 = JsonUtility.getGsonObject().fromJson(serviceResponse.getResponse() + "", listToken);
				schedulerLogId = schedulerLog1.getId();
			}
			return schedulerLogId;
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}
		return null;
	}

	private Scheduler getSchedulerStatus() {
		try {
			CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

			Map<String, String> headerMap = new HashMap<>();
			headerMap.put(GeneralConstants.JOB_PROCESSING_ID.getConstantVal(), jobProcessingId);
			headerMap.put(GeneralConstants.SCHEDULER_CODE.getConstantVal(), schedulerCode);

			WebServiceComponentUrl componentUrl = getWebServiceComponentURL(GeneralConstants.GET_ACTIVE_SCHEDULER_STATUS_BY_CODE.getConstantVal(), CIMSRestWebserviceClient.HTTP_METHOD_TYPE_GET);
			String responsestring = restServiceClient.callRestWebServiceWithMultipleHeader(componentUrl, null, null, headerMap);

			Type listToken = new TypeToken<ServiceResponse>() {
			}.getType();
			ServiceResponse serviceResponse = JsonUtility.getGsonObject().fromJson(responsestring, listToken);

			if (!serviceResponse.isStatus()) {
				LOGGER.error("False status received from API with status message {}", serviceResponse.getStatusCode());
				return null;
			} else {
				if (serviceResponse.getResponse() != null) {
					listToken = new TypeToken<Scheduler>() {
					}.getType();
					return JsonUtility.getGsonObject().fromJson(serviceResponse.getResponse() + "", listToken);
				} else {
					LOGGER.error("response object not present in the response string, response received from API : {}", responsestring);
				}
			}
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}
		return null;
	}

	private WebServiceComponentUrl getWebServiceComponentURL(String componentName, String methodType) {
		Map<String, List<String>> valueMap = new HashMap<>();

		List<String> valueList = new ArrayList<>();
		valueList.add(componentName);
		valueMap.put(ColumnConstants.COMPONENTTYPE.getConstantVal(), valueList);

		valueList = new ArrayList<>();
		valueList.add(methodType);
		valueMap.put(ColumnConstants.METHODTYPE.getConstantVal(), valueList);

		WebServiceComponentUrl componentUrl = null;
		try {
			componentUrl = webServiceComponentService.getDataByColumnValue(valueMap, MethodConstants.GET_ACTIVE_DATA_BY_COMPONENTTYPE_METHODTYPE.getConstantVal()).get(0);
		} catch (ServiceException e) {
			LOGGER.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}
		return componentUrl;
	}

}
