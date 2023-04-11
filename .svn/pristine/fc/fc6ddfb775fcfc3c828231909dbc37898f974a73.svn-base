package com.iris.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dto.LoggedInUserDeptReturnDto;
import com.iris.dto.RegulatorDto;
import com.iris.dto.ReturnByRoleInputDto;
import com.iris.dto.ReturnByRoleOutputDto;
import com.iris.dto.ReturnEntityMapDto;
import com.iris.dto.ReturnEntityOutputDto;
import com.iris.dto.ReturnGroupMappingDto;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ApplicationException;
import com.iris.exception.ServiceException;
import com.iris.model.Return;
import com.iris.model.ReturnGroupMapping;
import com.iris.service.impl.ReturnGroupServiceV2;
import com.iris.service.impl.UserRoleReturnMappingService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;
import com.iris.validator.ReturnGroupValidatorV2;

/**
 * @author sajadhav
 *
 */
@RestController
@RequestMapping("/service/returnGroupController/V2")
public class ReturnGroupControllerV2 {

	private static final Logger LOGGER = LogManager.getLogger(ReturnGroupControllerV2.class);

	@Autowired
	private EntityMasterControllerV2 entityMasterControllerV2;

	@Autowired
	private ReturnGroupServiceV2 returnGroupServiceV2;

	@Autowired
	private ReturnGroupValidatorV2 returnGroupValidatorV2;

	@Autowired
	private UserRoleReturnMappingService userRoleReturnMappingService;

	/**
	 * Gets the ReturnGroup. This method is to get all ReturnGroups .
	 *
	 * @param jobProcessId
	 * @param returnGroupMappingRequest
	 * @return
	 */
	@PostMapping(value = "/getReturnGroups")
	public ServiceResponse getReturnGroups(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ReturnEntityMapDto returnEntityMapDto) {
		try {
			LOGGER.info("request received to get getReturnGroups for job processigid : " + jobProcessId + " returnEntityMapDto - " + returnEntityMapDto);
			returnGroupValidatorV2.validateReturnGroupMappingRequest(returnEntityMapDto);
			ServiceResponse serviceResponse = entityMasterControllerV2.getEntityReturnChannelMapp(jobProcessId, returnEntityMapDto);

			if (serviceResponse != null && serviceResponse.getResponse() != null) {
				List<ReturnEntityOutputDto> returnEntityOutputDtoList = (List<ReturnEntityOutputDto>) serviceResponse.getResponse();

				List<ReturnGroupMappingDto> returnGroupMappingDtoList = returnGroupServiceV2.transposeObject(returnEntityOutputDtoList);

				return new ServiceResponse.ServiceResponseBuilder().setResponse(returnGroupMappingDtoList).setStatus(true).build();
			}
			return serviceResponse;
		} catch (ApplicationException ae) {
			LOGGER.error("Exception : ", ae);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		} catch (Exception e) {
			LOGGER.error("Exception : ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	/**
	 * Gets the ReturnGroup. This method is to get all ReturnGroups .
	 *
	 * @param jobProcessId
	 * @param returnGroupMappingRequest
	 * @return
	 */
	@PostMapping(value = "/getUserReturnListAssignedToEntity")
	public ServiceResponse getUserReturnListAssignedToEntity(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ReturnEntityMapDto returnEntityMapDto) {
		try {
			LOGGER.info("request received to get getUserReturnListAssignedToEntity for job processigid : " + jobProcessId + " returnEntityMapDto - " + returnEntityMapDto);
			returnGroupValidatorV2.validateReturnGroupMappingRequest(returnEntityMapDto);

			ReturnByRoleInputDto returnByRoleInputDto = new ReturnByRoleInputDto();
			returnByRoleInputDto.setUserId(returnEntityMapDto.getUserId());
			returnByRoleInputDto.setIsActive(returnEntityMapDto.getIsActive());
			returnByRoleInputDto.setLangCode(returnEntityMapDto.getLangCode());
			returnByRoleInputDto.setRoleId(returnEntityMapDto.getRoleId());
			List<ReturnByRoleOutputDto> returnByRoleOutputDtoList = returnGroupServiceV2.fetchReturnListByRole(jobProcessId, returnByRoleInputDto);

			List<ReturnGroupMappingDto> returnGroupMappingDtoList = returnGroupServiceV2.transposeReturnByRoleOutputDtoObject(returnByRoleOutputDtoList);

			ServiceResponse entityReturnServiceResponse = entityMasterControllerV2.getEntityReturnChannelMapp(jobProcessId, returnEntityMapDto);

			if (entityReturnServiceResponse != null && entityReturnServiceResponse.isStatus()) {
				List<ReturnEntityOutputDto> entityOutputDtoList = (List<ReturnEntityOutputDto>) entityReturnServiceResponse.getResponse();
				returnGroupMappingDtoList.forEach(f -> {
					f.getReturnList().forEach(k -> {
						ReturnEntityOutputDto re = entityOutputDtoList.stream().filter(l -> l.getReturnCode().equals(k.getReturnCode())).findAny().orElse(null);
						if (re != null) {
							k.setAssignedToEntity(true);
						}
					});
				});
			}
			return new ServiceResponse.ServiceResponseBuilder().setResponse(returnGroupMappingDtoList).setStatus(true).build();
		} catch (ApplicationException ae) {
			LOGGER.error("Exception : ", ae);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		} catch (Exception e) {
			LOGGER.error("Exception : ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}

	}

	@PostMapping(value = "/getReturnListByRole")
	public ServiceResponse getReturnByRole(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ReturnByRoleInputDto returnByRoleInputDto) {
		LOGGER.info("request received to get return list by role for job processigid : " + jobProcessId + " returnByRoleInputDto - " + returnByRoleInputDto);
		Instant start = Instant.now();
		try {
			returnGroupValidatorV2.validateReturnByRoleDto(returnByRoleInputDto);
			List<ReturnByRoleOutputDto> returnByRoleOutputDtoList = returnGroupServiceV2.fetchReturnListByRole(jobProcessId, returnByRoleInputDto);
			LOGGER.info("request successfully completed to get return list by role for job processigid : " + jobProcessId);
			Instant end = Instant.now();
			LOGGER.info(jobProcessId + " to get return list by role Total response time taken in seconds - " + Duration.between(start, end).getSeconds());
			return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.SUCCESS_CODE.getConstantVal()).setStatusMessage(GeneralConstants.SUCCESS.getConstantVal()).setResponse(returnByRoleOutputDtoList).build();

		} catch (ApplicationException ae) {
			Instant end = Instant.now();
			LOGGER.info(jobProcessId + " to get return list by role Total response time taken in seconds - " + Duration.between(start, end).getSeconds());
			LOGGER.error(jobProcessId + " Exception occured to fetch getReturnByRole V2 ", ae);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ae.getErrorCode()).setStatusMessage(ae.getErrorMsg()).build();
		} catch (Exception e) {
			Instant end = Instant.now();
			LOGGER.info(jobProcessId + "to get return list by role Total response time taken in seconds - " + Duration.between(start, end).getSeconds());
			LOGGER.error(jobProcessId + " Exception occured to fetch getReturnByRole V2 ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	/**
	 * Gets the ReturnGroup. This method is to get all ReturnGroups assigned to role .
	 *
	 * @param jobProcessId
	 * @param returnByRoleInputDto
	 * @return
	 */
	@PostMapping(value = "/getReturnGroupsByRole")
	public ServiceResponse getReturnGroupsByRole(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ReturnByRoleInputDto returnByRoleInputDto) {
		try {
			LOGGER.info("request received to get getReturnGroupsByRole for job processigid : " + jobProcessId + " returnByRoleInputDto - " + returnByRoleInputDto);
			returnGroupValidatorV2.validateReturnByRoleInputDtoMappingRequest(returnByRoleInputDto);
			List<ReturnByRoleOutputDto> returnByRoleOutputDtoList = returnGroupServiceV2.fetchReturnListByRole(jobProcessId, returnByRoleInputDto);
			List<ReturnGroupMappingDto> returnGroupMappingDtoList = returnGroupServiceV2.transposeReturnByRoleOutputDtoObject(returnByRoleOutputDtoList);
			return new ServiceResponse.ServiceResponseBuilder().setResponse(returnGroupMappingDtoList).setStatus(true).build();
		} catch (ApplicationException ae) {
			LOGGER.error("Exception : ", ae);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		} catch (Exception e) {
			LOGGER.error("Exception : ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	/**
	 * Gets the getRegulatorAndReturnListByRole. This method is to get all getRegulatorAndReturnList assigned to role .
	 *
	 * @param jobProcessId
	 * @param returnByRoleInputDto
	 * @return
	 */
	@PostMapping(value = "/getRegulatorAndReturnListByRole")
	public ServiceResponse getRegulatorAndReturnListByRole(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ReturnByRoleInputDto returnByRoleInputDto) {
		try {
			LOGGER.info("request received to get getReturnGroupsByRole for job processigid : " + jobProcessId + " returnByRoleInputDto - " + returnByRoleInputDto);
			returnGroupValidatorV2.validateReturnByRoleInputDtoMappingRequest(returnByRoleInputDto);
			List<ReturnByRoleOutputDto> returnByRoleOutputDtoList = returnGroupServiceV2.fetchReturnListByRole(jobProcessId, returnByRoleInputDto);
			List<RegulatorDto> regulatorDtos = returnGroupServiceV2.transposeDtoToRegulatoReturnDto(returnByRoleOutputDtoList);
			return new ServiceResponse.ServiceResponseBuilder().setResponse(regulatorDtos).setStatus(true).build();
		} catch (ApplicationException ae) {
			LOGGER.error("Exception : ", ae);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		} catch (Exception e) {
			LOGGER.error("Exception : ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	/**
	 * Gets the ReturnGroup. This method is to get all getReturnListOwnedByLoggedInuserDept .
	 *
	 * @param jobProcessId
	 * @param returnByRoleInputDto
	 * @return
	 */
	@PostMapping(value = "/getReturnListOwnedByLoggedInuserDept")
	public ServiceResponse getReturnListOwnedByLoggedInuserDept(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ReturnByRoleInputDto returnByRoleInputDto) {
		try {
			LOGGER.info("request received to get getReturnListOwnedByLoggedInuserDept for job processigid : " + jobProcessId + " returnByRoleInputDto - " + returnByRoleInputDto);
			returnGroupValidatorV2.validateReturnByRoleInputDtoMappingRequest(returnByRoleInputDto);
			List<LoggedInUserDeptReturnDto> returnByRoleOutputDtoList = userRoleReturnMappingService.fetchReturnOwnedByLoggedInUserDepartment(returnByRoleInputDto);
			return new ServiceResponse.ServiceResponseBuilder().setResponse(returnByRoleOutputDtoList).setStatus(true).build();
		} catch (ApplicationException ae) {
			LOGGER.error("Exception : ", ae);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		} catch (Exception e) {
			LOGGER.error("Exception : ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	@PostMapping(value = "/getReturnGroupList/{languageCode}")
	public ServiceResponse getReturnGroupList(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable String languageCode) {
		ServiceResponse serviceResponse = null;
		LOGGER.info("getReturnGroupList method Start for job processigid : " + jobProcessId);
		try {
			if (StringUtils.isEmpty(languageCode)) {
				languageCode = GeneralConstants.ENG_LANG.getConstantVal();
			}
			// en specific by default for null lang code
			List<ReturnGroupMapping> returnGroupList = returnGroupServiceV2.getReturnGroupMappingList(languageCode);
			serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			serviceResponse.setResponse(returnGroupList);
		} catch (Exception e) {
			LOGGER.error("Exception while fetching return group list for job processigid : " + jobProcessId, e);
			serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
		}
		return serviceResponse;
	}

	/**
	 * Get the Return List. This method is to get all Returns which are not mapped with any ReturnGroup .
	 *
	 * @param jobProcessId
	 * @param returnGroupMappingRequest
	 * @return
	 */
	@PostMapping(value = "/getMappedReturnList/{languageCode}/{returnGroupId}")
	public ServiceResponse getMappedReturnList(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable String languageCode, @PathVariable Long returnGroupId) {
		List<Return> returnList = new ArrayList<>();
		LOGGER.info("getUnMappedReturnList method Start for job processigid : " + jobProcessId);
		try {
			if (StringUtils.isEmpty(languageCode)) {
				languageCode = GeneralConstants.ENG_LANG.getConstantVal();
			}
			Map<String, Object> columneMap = new HashMap<>();// languge based label 
			columneMap.put(ColumnConstants.IS_ACTIVE.getConstantVal(), true);
			columneMap.put(ColumnConstants.LANG_CODE.getConstantVal(), languageCode);
			columneMap.put(ColumnConstants.RETURN_GROUP_ID.getConstantVal(), returnGroupId);
			returnList = returnGroupServiceV2.getReturnsListForReturnGroupMapping(columneMap, MethodConstants.GET_RETURN_LIST_MAPPED_TO_RETURN_GROUP.getConstantVal());
		} catch (ServiceException e) {
			LOGGER.error("Exception while fetching return list info for job processingid %s : %s", jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0431.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0431.toString())).build();
		} catch (Exception e) {
			LOGGER.error("Exception while fetching return list info for job processingid  %s : %s", jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		response.setResponse(returnList);
		LOGGER.info("request completed to get return list for job processingid %s", jobProcessId);
		return response;
	}

	/**
	 * Get the Return List. This method is to get all Returns which are not mapped with any ReturnGroup .
	 *
	 * @param jobProcessId
	 * @param returnGroupMappingRequest
	 * @return
	 */
	@PostMapping(value = "/getUnMappedReturnList/{languageCode}")
	public ServiceResponse getUnMappedReturnList(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable String languageCode) {
		List<Return> returnList = new ArrayList<>();
		LOGGER.info("getUnMappedReturnList method Start for job processigid : " + jobProcessId);
		try {
			if (StringUtils.isEmpty(languageCode)) {
				languageCode = GeneralConstants.ENG_LANG.getConstantVal();
			}
			Map<String, Object> columneMap = new HashMap<>();// languge based label 
			columneMap.put(ColumnConstants.IS_ACTIVE.getConstantVal(), true);
			columneMap.put(ColumnConstants.LANG_CODE.getConstantVal(), languageCode);
			returnList = returnGroupServiceV2.getReturnsListForReturnGroupMapping(columneMap, MethodConstants.GET_RETURN_LIST_WITH_OUT_RETURN_GROUP.getConstantVal());
		} catch (ServiceException e) {
			LOGGER.error("Exception while fetching return list info for job processingid %s : %s", jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0431.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0431.toString())).build();
		} catch (Exception e) {
			LOGGER.error("Exception while fetching return list info for job processingid  %s : %s", jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		response.setResponse(returnList);
		LOGGER.info("request completed to get return list for job processingid %s", jobProcessId);
		return response;
	}

	/**
	 * save the ReturnGroupMapping. This method is to save ReturnGroupMapping .
	 *
	 * @param jobProcessId
	 * @param returnGroupMappingDto
	 * @return
	 */
	@PostMapping(value = "/saveReturnGroup")
	public ServiceResponse saveReturnGroup(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ReturnGroupMapping returnGroupMapping) {
		LOGGER.info("saveReturnGroup method Start for job processigid : " + jobProcessId);
		try {
			returnGroupMapping.setCreatedOn(new Date());
			returnGroupMapping.setLastUpdateOn(new Date());
			if (returnGroupMapping.getIsCrossValidation() == null) {
				returnGroupMapping.setIsCrossValidation(false);
			}
			returnGroupServiceV2.add(returnGroupMapping);

		} catch (ServiceException e) {
			LOGGER.error("Request object not proper for saving ReturnGroupMapping - %s and %s", jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0771.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0771.toString())).build();
		} catch (Exception e) {
			LOGGER.error("Exception while saving ReturnGroupMapping info for job processingid %s and %s ", jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		LOGGER.info("request completed to save ReturnGroupMapping for job processingid %s", jobProcessId);
		return response;
	}

	/**
	 * update the ReturnGroupMapping. This method is to update ReturnGroupMapping
	 *
	 * @param jobProcessId
	 * @param returnGroupMappingDto
	 * @return
	 */
	@PostMapping(value = "/updateReturnGroup")
	public ServiceResponse updateReturnGroup(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ReturnGroupMapping returnGroupMappingDto) {
		Boolean flag = false;
		LOGGER.info("updateReturnGroup method Start for job processigid : " + jobProcessId);
		try {
			returnGroupMappingDto.setLastModifiedOn(new Date());
			returnGroupMappingDto.setLastUpdateOn(new Date());
			flag = returnGroupServiceV2.update(returnGroupMappingDto);
		} catch (ServiceException e) {
			LOGGER.error("Request object not proper for update for job processigid : " + jobProcessId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0771.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0771.toString())).build();
		} catch (Exception e) {
			LOGGER.error("Exception while updating ReturnGroupMapping info for job processingid %s : %s", jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(flag).build();
		LOGGER.info("request completed to update ReturnGroupMapping for job processingid %s", jobProcessId);
		return response;
	}

	@PostMapping(value = "/chectReturnGroupNameExist/{returnGroupName}/{languageCode}")
	public ServiceResponse chectReturnGroupNameExist(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable String languageCode, @PathVariable String returnGroupName) {
		Boolean flag = false;
		LOGGER.info("chectReturnGroupNameExist method Start for job processigid : " + jobProcessId);
		try {
			flag = returnGroupServiceV2.checkReturnGroupNameExist(returnGroupName);
		} catch (Exception e) {
			LOGGER.error("Exception while checking ReturnGroup Name Exist info for job processingid %s : %s", jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(flag).build();
		LOGGER.info("request completed of checking ReturnGroup Name Exist for job processingid %s", jobProcessId);
		return response;
	}
}
