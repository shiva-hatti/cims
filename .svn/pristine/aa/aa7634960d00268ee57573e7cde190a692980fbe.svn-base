/**
 * 
 */
package com.iris.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dto.EntityReturnChanneMapAppDto;
import com.iris.dto.ReturnEntityMapDto;
import com.iris.dto.ReturnEntityOutputDto;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.dto.UserDto;
import com.iris.exception.ApplicationException;
import com.iris.model.Return;
import com.iris.service.impl.EntityReturnChannelMappingApprovalService;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;
import com.iris.validator.EntityReturnChannelMapValidatorV2;

/**
 * @author sajadhav
 *
 */
@RestController
@RequestMapping("/service/entityReturnChannelMapService/V2")
public class EntityReturnChannelMapControllerV2 {

	private static final Logger LOGGER = LogManager.getLogger(EntityReturnChannelMapControllerV2.class);

	@Autowired
	private EntityReturnChannelMapValidatorV2 entityReturnChannelMapControllerV2;

	@Autowired
	private EntityMasterControllerV2 entityMasterControllerV2;

	@Autowired
	private EntityReturnChannelMappingApprovalService entityReturnChannelMapService;

	@Autowired
	private EntityReturnChannelMappingApprovalService entityReturnChannelMappingApprovalService;

	@PostMapping(value = "/getEntReturnChannelMappingList")
	public ServiceResponse getEntityReturnChannelMapp(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ReturnEntityMapDto returnEntityMapDto) {
		LOGGER.info("request received to get entity return channel map list for job processigid : " + jobProcessId);

		try {
			entityReturnChannelMapControllerV2.validateReturnEntityMapDto(returnEntityMapDto);

			ServiceResponse serviceResponse = entityMasterControllerV2.getEntityReturnChannelMapp(jobProcessId, returnEntityMapDto);

			if (serviceResponse.isStatus()) {
				List<ReturnEntityOutputDto> returnEntityOutputDtoList = (List<ReturnEntityOutputDto>) serviceResponse.getResponse();

				List<Return> returnList = convertResultToOutputBean(returnEntityOutputDtoList);

				return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.SUCCESS_CODE.getConstantVal()).setStatusMessage(GeneralConstants.SUCCESS.getConstantVal()).setResponse(returnList).build();
			} else {
				return serviceResponse;
			}
		} catch (ApplicationException applicationException) {
			LOGGER.error("Eception occured for job processigid : " + jobProcessId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(applicationException.getErrorCode()).setStatusMessage(applicationException.getErrorMsg()).build();
		} catch (Exception e) {
			LOGGER.error("Eception occured for job processigid : " + jobProcessId);
			LOGGER.error(ErrorCode.EC0033.toString(), e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	@PostMapping(value = "/getEntityReturnChannelMappForApproval")
	public ServiceResponse getEntityReturnChannelMappForApproval(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody UserDto userDto) {
		LOGGER.info("request received to get getEntityReturnChannelMappForApproval for job processigid : " + jobProcessId);
		try {
			entityReturnChannelMapControllerV2.validateReturnEntityMapDto(userDto);
			return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.SUCCESS_CODE.getConstantVal()).setStatusMessage(GeneralConstants.SUCCESS.getConstantVal()).setResponse(entityReturnChannelMapService.getEntityReturnChannelMappingApprovalData(GeneralConstants.PENDING_ADMIN_STATUS_ID.getConstantIntVal().longValue(), true, userDto.getUserId(), userDto.getLangCode())).build();
		} catch (ApplicationException applicationException) {
			LOGGER.error("Eception occured to get getEntityReturnChannelMappForApproval for job processigid : " + jobProcessId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(applicationException.getErrorCode()).setStatusMessage(applicationException.getErrorMsg()).build();
		} catch (Exception e) {
			LOGGER.error("Eception occured to get getEntityReturnChannelMappForApproval for job processigid : " + jobProcessId);
			LOGGER.error(ErrorCode.EC0033.toString(), e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	@PostMapping(value = "/approveEntityReturnChannelMapData")
	public ServiceResponse approveEntityReturnChannelMapData(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody EntityReturnChanneMapAppDto entityReturnChanneMapAppDto) {
		LOGGER.info("request received to approved and reject record for job processigid : " + jobProcessId);
		try {
			entityReturnChannelMapControllerV2.validateDtoForApproval(entityReturnChanneMapAppDto);
			return entityReturnChannelMappingApprovalService.approveAndRejectEntityReturnChannelMappingData(entityReturnChanneMapAppDto, jobProcessId);
		} catch (ApplicationException applicationException) {
			LOGGER.error("Eception occured to approved and reject record for job processigid : " + jobProcessId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(applicationException.getErrorCode()).setStatusMessage(applicationException.getErrorMsg()).build();
		} catch (Exception e) {
			LOGGER.error("Eception occured  to approved and reject record for job processigid : " + jobProcessId);
			LOGGER.error(ErrorCode.EC0033.toString(), e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	private List<Return> convertResultToOutputBean(List<ReturnEntityOutputDto> returnEntityOutputDtoList) {

		List<Return> returnBeans = new ArrayList<>();
		for (ReturnEntityOutputDto returnEntityOutputDto : returnEntityOutputDtoList) {
			Return returnBean = new Return();
			returnBean.setReturnId(returnEntityOutputDto.getReturnId());
			returnBean.setReturnCode(returnEntityOutputDto.getReturnCode());
			returnBean.setReturnName(returnEntityOutputDto.getReturnName());

			returnEntityOutputDto.getReturnEntityDtoSet().forEach(f -> {
				returnEntityOutputDto.getReturnEntityDtoSet().forEach(k -> {
					returnBean.setApiChannel(f.getApiChannel());
					returnBean.setWebChannel(f.getWebChannel());
					returnBean.setUploadChannel(f.getUploadChannel());
					returnBean.setEmailChannel(f.getEmailChannel());
					returnBean.setStsChannel(f.getStsChannel());
					if (!f.getApiChannel().equals(k.getApiChannel())) {
						returnBean.setMultipleChannelFound(true);
					}

					if (!f.getWebChannel().equals(k.getWebChannel())) {
						returnBean.setMultipleChannelFound(true);
					}

					if (!f.getUploadChannel().equals(k.getUploadChannel())) {
						returnBean.setMultipleChannelFound(true);
					}

					if (!f.getEmailChannel().equals(k.getEmailChannel())) {
						returnBean.setMultipleChannelFound(true);
					}

					if (!f.getStsChannel().equals(k.getStsChannel())) {
						returnBean.setMultipleChannelFound(true);
					}
				});
			});
			returnBeans.add(returnBean);
		}

		return returnBeans;
	}
}
