package com.iris.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dto.ReturnByRoleInputDto;
import com.iris.dto.ReturnByRoleOutputDto;
import com.iris.dto.ReturnTemplateDto;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ApplicationException;
import com.iris.service.impl.ReturnGroupServiceV2;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;
import com.iris.validator.DownloadReturnTempValidator;

/**
 * @author Bhavana Thakare This service class is created to write business logic of get all data ReturnTemplate file
 *
 */

@RestController
@RequestMapping("/service/downloadReturnTempService/V2")
public class DownloadReturnTemplateControllerV2 {
	private static final Logger LOGGER = LogManager.getLogger(EntityMasterControllerV2.class);
	@Autowired
	private DownloadReturnTempValidator downloadReturnTempValidator;
	@Autowired
	private ReturnGroupServiceV2 returnGroupServiceV2;
	@Autowired
	private ReturnTemplateUploadController returnTemplateUploadController;

	@PostMapping(value = "/getAllDataReturnTemplateDataByReturn")
	public ServiceResponse getAllDataReturnTemplateDataByReturn(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ReturnByRoleInputDto returnByRoleInputDto) {
		LOGGER.info("Request received to getAllData Return Template Data By Return list for job processigid : " + jobProcessId);
		try {
			downloadReturnTempValidator.validateReturnTempMapReqDto(returnByRoleInputDto);
			// Fetch the result
			//List<ReturnEntityQueryOutputDto> returnEntityQueryOutputDtoList = entityMasterControllerServiceV2.fetchReturnListByRoleNEntity(returnChannelMapReqDto, jobProcessId);
			LOGGER.info(jobProcessId + "Request received for fetch return list from fetchReturnListByRole --");
			List<ReturnByRoleOutputDto> returnByRoleOutputDtoList = returnGroupServiceV2.fetchReturnListByRole(jobProcessId, returnByRoleInputDto);
			LOGGER.info("request successfully completed to get return list by user for job processigid : " + jobProcessId);
			// convert result
			List<String> returnCodeList = convertResultToOutputBean(returnByRoleOutputDtoList);
			if (CollectionUtils.isEmpty(returnCodeList)) {
				LOGGER.error(jobProcessId + " Return list is empty into the getAllDataReturnTemplateDataByReturn ");
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
			}
			ReturnTemplateDto returnTemplateDto = new ReturnTemplateDto();
			returnTemplateDto.setReturnCodeList(returnCodeList);
			returnTemplateDto.setLangCode(returnByRoleInputDto.getLangCode());
			LOGGER.info("Call getAllDataOfReturnTemplateFromReturn_new function");
			List<ReturnTemplateDto> retTempDtoList = returnTemplateUploadController.getAllDataOfReturnTemplateFromReturn_new(returnTemplateDto);
			LOGGER.info("Call getAllDataOfReturnTemplateFromReturn_new function end ");
			return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.SUCCESS_CODE.getConstantVal()).setStatusMessage(GeneralConstants.SUCCESS.getConstantVal()).setResponse(retTempDtoList).build();
		} catch (ApplicationException ae) {
			LOGGER.error(jobProcessId + " Exception occured to fetch getAllDataReturnTemplateDataByReturn V2 ", ae);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ae.getErrorCode()).setStatusMessage(ae.getErrorMsg()).build();
		} catch (Exception e) {
			LOGGER.error(jobProcessId + " Exception occured to fetch getAllDataReturnTemplateDataByReturn V2 ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	private List<String> convertResultToOutputBean(List<ReturnByRoleOutputDto> returnByRoleOutputDtoList) {
		List<String> returnCodeList = new ArrayList<>();
		for (ReturnByRoleOutputDto retOutList : returnByRoleOutputDtoList) {
			if (!returnCodeList.contains(retOutList.getReturnCode())) {
				returnCodeList.add(retOutList.getReturnCode());
			}
		}

		return returnCodeList;
	}
}
