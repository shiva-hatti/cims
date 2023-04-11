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
import com.iris.dto.ServiceResponse;
import com.iris.exception.ServiceException;
import com.iris.model.ReturnsUploadDetails;
import com.iris.model.Scheduler;
import com.iris.model.SchedulerLog;
import com.iris.model.WebServiceComponentUrl;
import com.iris.repository.ReturnUploadDetailsRepository;
import com.iris.service.GenericService;
import com.iris.service.impl.ReturnUploadDetailsService;
import com.iris.service.impl.WorkflowService;
import com.iris.util.JsonUtility;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;
import com.iris.webservices.client.CIMSRestWebserviceClient;

/**
 * @author akhandagale
 *
 */
@RestController
public class WorkflowTriggerScheduler {

	static final Logger logger = LogManager.getLogger(WorkflowTriggerScheduler.class);

	@Autowired
	private ReturnUploadDetailsRepository returnUploadDetailsRepository;

	@Autowired
	private GenericService<WebServiceComponentUrl, Long> webServiceComponentService;

	@Autowired
	private ReturnUploadDetailsService returnsUploadDetailsService;

	@Autowired
	private WorkflowService workflowService;

	private String jobProcessId;

	@Value("${scheduler.code.workFlowTrigger}")
	private String schedulerCode;

	//	private static final String SCHEDULER_CODE = "WORKFLOW_TRIGGER";

	@Scheduled(cron = "${cron.workFlowTrigger}")
	public void workFlowTriggerSchedular() {
		jobProcessId = UUID.randomUUID().toString();
		logger.info("WorkflowTriggerScheduler started");

		Long schedulerLogId = null;
		Scheduler scheduler = getSchedulerStatus();
		if (scheduler != null) {
			if (scheduler.getIsRunning().equals(Boolean.TRUE)) {
				logger.error("Error while starting workFlow Trigger scheduler -> Reason : Schduler is alrady running " + jobProcessId);
				return;
			}
			long successCount = 0;
			long failedCount = 0;
			try {
				logger.info("WorkFlow Trigger Scheduler entry started" + jobProcessId);
				schedulerLogId = makeSchedulerStartEntry(0l, scheduler.getSchedulerId());
				if (schedulerLogId == null) {
					logger.error("Error while starting workFlow Trigger scheduler -> Reason : Schduler Log ID not received " + jobProcessId);
					return;
				}

				// update status for status 5 record 
				List<ReturnsUploadDetails> pendingRecords = returnUploadDetailsRepository.getPendingWorkFlowRecordsAfterETLProcess();

				for (ReturnsUploadDetails appDet : pendingRecords) {
					try {
						workflowService.prepareObjectForWorkflowAndBusinessValidationSuccess(appDet);
						successCount++;
					} catch (Exception e) {
						failedCount++;
						logger.error("Exception occoured while processing workflow trigger schedular Exception is ", e);
					}
				}

				// Update Status of ETL failed record
				failedCount = failedCount + workflowService.updateFilingStatusOfETLFailedRecord();

			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Exception occoured while processing workflow trigger schedular Exception is ", e);
			} finally {
				makeSchedulerStopEntry(successCount, schedulerLogId, scheduler.getSchedulerId());
			}
		}

	}

	private void makeSchedulerStopEntry(long successfullyProcessed, Long schedulerLogId, Long schedulerId) {
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
				headerMap.put(GeneralConstants.JOB_PROCESSING_ID.getConstantVal(), jobProcessId);

				String responsestring = restServiceClient.callRestWebServiceWithMultipleHeader(componentUrl, schedulerLog, null, headerMap);

				ServiceResponse serviceResponse = JsonUtility.getGsonObject().fromJson(responsestring, ServiceResponse.class);

				if (!serviceResponse.isStatus()) {
					logger.info("Error while stopping scheduler -> Reason : Schduler is alrady stopped ");
				} else {
					logger.info("Scheduler stopped successfully ");
				}
			} else {
				logger.error("Scheduler Log Id not found");
			}
		} catch (Exception e) {
			logger.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}

	}

	private Scheduler getSchedulerStatus() {
		try {
			CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

			Map<String, String> headerMap = new HashMap<>();
			headerMap.put(GeneralConstants.JOB_PROCESSING_ID.getConstantVal(), jobProcessId);
			headerMap.put(GeneralConstants.SCHEDULER_CODE.getConstantVal(), schedulerCode);

			WebServiceComponentUrl componentUrl = getWebServiceComponentURL(GeneralConstants.GET_ACTIVE_SCHEDULER_STATUS_BY_CODE.getConstantVal(), CIMSRestWebserviceClient.HTTP_METHOD_TYPE_GET);
			String responsestring = restServiceClient.callRestWebServiceWithMultipleHeader(componentUrl, null, null, headerMap);

			Type listToken = new TypeToken<ServiceResponse>() {
			}.getType();
			ServiceResponse serviceResponse = JsonUtility.getGsonObject().fromJson(responsestring, listToken);

			if (!serviceResponse.isStatus()) {
				logger.error("False status received from API with status message " + serviceResponse.getStatusCode());
				return null;
			} else {
				if (serviceResponse.getResponse() != null) {
					listToken = new TypeToken<Scheduler>() {
					}.getType();
					return JsonUtility.getGsonObject().fromJson(serviceResponse.getResponse() + "", listToken);
				} else {
					logger.error("response object not present in the response string, response received from API : {}" + responsestring);
				}
			}
		} catch (Exception e) {
			logger.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
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
			logger.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}
		return componentUrl;
	}

	private Long makeSchedulerStartEntry(long totalRecordCount, Long schedulerId) {
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
			headerMap.put(GeneralConstants.JOB_PROCESSING_ID.getConstantVal(), jobProcessId);

			String responsestring = restServiceClient.callRestWebServiceWithMultipleHeader(componentUrl, schedulerLog, null, headerMap);

			Type listToken = new TypeToken<ServiceResponse>() {
			}.getType();
			ServiceResponse serviceResponse = JsonUtility.getGsonObject().fromJson(responsestring, listToken);
			Long schedulerLogId = null;

			if (!serviceResponse.isStatus()) {
				logger.error("False status received from API with status message " + serviceResponse.getStatusCode());
				return null;
			} else {
				listToken = new TypeToken<SchedulerLog>() {
				}.getType();
				SchedulerLog schedulerLog1 = JsonUtility.getGsonObject().fromJson(serviceResponse.getResponse() + "", listToken);
				schedulerLogId = schedulerLog1.getId();
			}
			return schedulerLogId;
		} catch (Exception e) {
			logger.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}
		return null;
	}

}
