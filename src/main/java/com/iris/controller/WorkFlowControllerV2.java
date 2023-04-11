package com.iris.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.iris.caching.ObjectCache;
import com.iris.dto.ReturnDto;
import com.iris.dto.ReturnEntityMapDto;
import com.iris.dto.ReturnEntityOutputDto;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.dto.WorkflowReturnMappingInfo;
import com.iris.model.Return;
import com.iris.model.WorkFlowMasterBean;
import com.iris.model.WorkflowReturnMapping;
import com.iris.service.impl.ReturnService;
import com.iris.service.impl.WorkflowService;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;

@RestController
@RequestMapping("/service/V2")
public class WorkFlowControllerV2 {

	private static final Logger LOGGER = LogManager.getLogger(WorkFlowControllerV2.class);

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private ReturnService returnService;

	@Autowired
	private FileDetailsController fileDetailsController;

	@Autowired
	private EntityMasterControllerV2 entityMatserController;

	@PostMapping(value = "/fetchWorkflowData")
	public ServiceResponse fetchWorkflowData(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody WorkflowReturnMappingInfo workflow) {
		LOGGER.info("Request received to fetchWorkflowData for processing id " + jobProcessId);
		WorkflowReturnMapping workflowBean = new WorkflowReturnMapping();

		List<WorkflowReturnMapping> responseList = new ArrayList<WorkflowReturnMapping>();
		try {
			List<WorkflowReturnMapping> workFLowReturnMappingList = new ArrayList<>();
			if (workflow != null) {
				workFLowReturnMappingList = workflowService.fetchWorkFlowDtoData(workflow.getWorkFlowMaster().getWorkflowId());
			}
			List<Long> returnIdList = workFLowReturnMappingList.stream().map(f -> f.getReturnIdFk().getReturnId()).collect(Collectors.toList());

			String jsonData = fileDetailsController.getActivityTrackerListExcludeFiling(workflow.getWorkFlowMaster().getWorkflowId());

			ReturnEntityMapDto returnEntityMapDto = new ReturnEntityMapDto();
			returnEntityMapDto.setRoleId(workflow.getLastUpdatedBy().getRoleIdKey());
			returnEntityMapDto.setUserId(workflow.getLastUpdatedBy().getUserId());
			returnEntityMapDto.setIsActive(true);
			returnEntityMapDto.setLangCode(workflow.getLangCode());

			List<ReturnDto> returnObjList = returnService.getReturnRegulatorMapping(workflow.getReturnIds(), workflow.getLangCode());

			if (!CollectionUtils.isEmpty(returnObjList)) {

				returnObjList.sort((ReturnDto p1, ReturnDto p2) -> p1.getReturnName().compareToIgnoreCase(p2.getReturnName()));
				returnObjList.sort((ReturnDto p1, ReturnDto p2) -> p1.getRegulator().getRegulatorName().compareToIgnoreCase(p2.getRegulator().getRegulatorName()));

				Return returnBean;
				WorkFlowMasterBean workFlowMasterBean;
				Map<String, String> checkMap = new HashMap<>();
				for (ReturnDto returnDTO : returnObjList) {
					workflowBean = new WorkflowReturnMapping();
					returnBean = new Return();
					workFlowMasterBean = new WorkFlowMasterBean();
					if (returnIdList.contains(returnDTO.getReturnId())) {
						returnBean.setReturnId(returnDTO.getReturnId());
						returnBean.setReturnName(returnDTO.getReturnName());
						returnBean.setReturnCode(returnDTO.getReturnCode());
						workFlowMasterBean.setWorkFlowJson(jsonData);
						workflowBean.setReturnIdFk(returnBean);
						workflowBean.setRegulator(returnDTO.getRegulator());
						workflowBean.setWorkFlowMaster(workFlowMasterBean);
						for (WorkflowReturnMapping workflowItr1 : workFLowReturnMappingList) {
							if (workflowItr1.getReturnIdFk().getReturnId().equals(returnDTO.getReturnId())) {
								checkMap.put(workflowItr1.getReturnIdFk().getReturnId() + "~1", "deactive");
								checkMap.put(workflowItr1.getReturnIdFk().getReturnId() + "~2", "deactive");
								checkMap.put(workflowItr1.getReturnIdFk().getReturnId() + "~3", "deactive");
								checkMap.put(workflowItr1.getReturnIdFk().getReturnId() + "~4", "deactive");
								checkMap.put(workflowItr1.getReturnIdFk().getReturnId() + "~5", "deactive");

								if (workflowItr1.getChannelIdFk().getUploadChannelId().equals(GeneralConstants.WEB_CHANNEL.getConstantLongVal())) {
									checkMap.put(workflowItr1.getReturnIdFk().getReturnId() + "~1", "active");
									workflowBean.setWebChannel(true);
								}
								if (workflowItr1.getChannelIdFk().getUploadChannelId().equals(GeneralConstants.UPLOAD_CHANNEL.getConstantLongVal())) {
									checkMap.put(workflowItr1.getReturnIdFk().getReturnId() + "~2", "active");
									workflowBean.setUploadChannel(true);
								}
								if (workflowItr1.getChannelIdFk().getUploadChannelId().equals(GeneralConstants.API_CHANNEL.getConstantLongVal())) {
									checkMap.put(workflowItr1.getReturnIdFk().getReturnId() + "~3", "active");
									workflowBean.setApiChannel(true);
								}
								if (workflowItr1.getChannelIdFk().getUploadChannelId().equals(GeneralConstants.EMAIL_CHANNEL.getConstantLongVal())) {
									checkMap.put(workflowItr1.getReturnIdFk().getReturnId() + "~4", "active");
									workflowBean.setEmailChannel(true);
								}
								if (workflowItr1.getChannelIdFk().getUploadChannelId().equals(GeneralConstants.SYSTEM_CHANNEL.getConstantLongVal())) {
									checkMap.put(workflowItr1.getReturnIdFk().getReturnId() + "~5", "active");
									workflowBean.setStsChannel(true);
								}
							}
						}
					} else {
						checkMap.put(returnDTO.getReturnId() + "~1", "deactive");
						checkMap.put(returnDTO.getReturnId() + "~2", "deactive");
						checkMap.put(returnDTO.getReturnId() + "~3", "deactive");
						checkMap.put(returnDTO.getReturnId() + "~4", "deactive");
						checkMap.put(returnDTO.getReturnId() + "~5", "deactive");

						returnBean.setReturnId(returnDTO.getReturnId());
						returnBean.setReturnName(returnDTO.getReturnName());
						returnBean.setReturnCode(returnDTO.getReturnCode());
						workFlowMasterBean.setWorkFlowJson(jsonData);
						workflowBean.setReturnIdFk(returnBean);
						workflowBean.setRegulator(returnDTO.getRegulator());
						workflowBean.setWorkFlowMaster(workFlowMasterBean);
						workflowBean.setWebChannel(false);
						workflowBean.setUploadChannel(false);
						workflowBean.setApiChannel(false);
						workflowBean.setEmailChannel(false);
						workflowBean.setStsChannel(false);
					}
					responseList.add(workflowBean);
				}
				if (CollectionUtils.isEmpty(responseList)) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0964.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0964.toString())).build();
				}

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("channelEntityDTOs", responseList);
				jsonObject.put("channelEntityMap", checkMap);
				jsonObject.put("originalMap", checkMap);
				jsonObject.put("jsonData", jsonData);
				return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(jsonObject)).build();
			}
			return new ServiceResponseBuilder().setStatus(false).build();
		} catch (Exception e) {
			LOGGER.error("Exception occoured while fatching fetchWorkflowData for processing id " + jobProcessId + "Exception is " + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0964.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0964.toString())).build();
		}
	}

	@GetMapping(value = "/getActiveWorkflowData")
	public ServiceResponse getActiveWorkflowData(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		LOGGER.info("Request received to getActiveWorkflowData for processing id" + jobProcessId);
		try {
			List<WorkflowReturnMapping> workflowList = workflowService.fetchWorkFlowDtoData();
			if (CollectionUtils.isEmpty(workflowList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0961.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0961.toString())).build();
			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(workflowList).build();
		} catch (Exception e) {
			LOGGER.error("Exception occoured while fatching getActiveWorkflowData for processing id" + jobProcessId + "Exception is " + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0961.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0961.toString())).build();
		}
	}

	@PostMapping(value = "/getLightWeigthReturnAndRegulatorList")
	public ServiceResponse getLightWeigthReturnAndRegulatorList(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ReturnEntityMapDto returnEntityMapDto) {
		LOGGER.info("Request received to getActiveWorkflowData for processing id" + jobProcessId);
		try {
			ServiceResponse serviceResponse = entityMatserController.getEntityReturnChannelMapp(jobProcessId, returnEntityMapDto);

			if (serviceResponse.isStatus()) {
				List<ReturnEntityOutputDto> returnEntityOutputDtoList = (List<ReturnEntityOutputDto>) serviceResponse.getResponse();
				returnEntityOutputDtoList.forEach(f -> {
					f.setReturnEntityDtoSet(null);
				});
				return new ServiceResponseBuilder().setStatus(true).setResponse(returnEntityOutputDtoList).build();
			}

			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0961.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0961.toString())).build();
		} catch (Exception e) {
			LOGGER.error("Exception occoured while fatching getActiveWorkflowData for processing id" + jobProcessId + "Exception is " + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0961.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0961.toString())).build();
		}
	}

}