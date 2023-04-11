package com.iris.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.iris.dateutility.util.DateManip;
import com.iris.dto.EntityDto;
import com.iris.dto.ReturnApprovalDataDto;
import com.iris.dto.ReturnByRoleInputDto;
import com.iris.dto.ReturnByRoleOutputDto;
import com.iris.dto.ReturnEntityMapDto;
import com.iris.dto.ReturnEntityOutputDto;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.dto.UserDetailsDto;
import com.iris.dto.UserDto;
import com.iris.exception.ApplicationException;
import com.iris.model.EntityLabelBean;
import com.iris.model.ReturnApprovalDetail;
import com.iris.model.ReturnLabel;
import com.iris.model.ReturnsUploadDetails;
import com.iris.model.UserRole;
import com.iris.repository.ReturnApprovalDetailsRepo;
import com.iris.service.GenericService;
import com.iris.service.impl.ReturnApprovalDetailsService;
import com.iris.service.impl.ReturnGroupServiceV2;
import com.iris.service.impl.UserMasterServiceV2;
import com.iris.service.impl.UserRoleService;
import com.iris.util.AESV2;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;
import com.iris.validator.ApprovalValidatorV2;

/**
 * @author sajadhav
 */
@RestController
@RequestMapping("/service/approvalService/V2")
public class ApprovalControllerV2 {

	private static final Logger LOGGER = LogManager.getLogger(ApprovalControllerV2.class);

	@Autowired
	private EntityMasterControllerV2 entityMasterControllerV2;

	@Autowired
	private ApprovalValidatorV2 approvalValidatorV2;

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private ReturnApprovalDetailsService returnApprovalDetailService;

	@Autowired
	private ReturnApprovalDetailsRepo returnApprovalDetailRepo;

	@Autowired
	private UserMasterServiceV2 userMasterServiceV2;

	@Autowired
	private ReturnGroupServiceV2 returnGroupServiceV2;

	@PostMapping(value = "/getPendingRecordForApproval")
	public ServiceResponse getPendingRecords(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody UserDto userDto) {
		try {
			approvalValidatorV2.validateUserDto(userDto);

			List<Long> entityIds = new ArrayList<>();
			UserDetailsDto userDetailsDto = userMasterServiceV2.getUserWithEntityDetails(userDto);

			List<EntityDto> entityDtos = userDetailsDto.getEntityDtos();

			if (!CollectionUtils.isEmpty(entityDtos)) {
				entityDtos.forEach(f -> {
					entityIds.add(f.getEntityId());
				});

				List<String> returnCodes = new ArrayList<>();

				ReturnByRoleInputDto returnByRoleInputDto = new ReturnByRoleInputDto();
				returnByRoleInputDto.setUserId(userDto.getUserId());
				returnByRoleInputDto.setRoleId(userDto.getRoleId());
				returnByRoleInputDto.setLangCode(userDto.getLangCode());
				returnByRoleInputDto.setIsActive(userDto.getIsActive());

				List<ReturnByRoleOutputDto> returnByRoleOutputDtoList = returnGroupServiceV2.fetchReturnListByRole(jobProcessId, returnByRoleInputDto);

				if (!CollectionUtils.isEmpty(returnByRoleOutputDtoList)) {
					returnByRoleOutputDtoList.forEach(f -> {
						returnCodes.add(f.getReturnCode());
					});
				}

				if (!CollectionUtils.isEmpty(entityIds) && !CollectionUtils.isEmpty(returnCodes)) {
					return getPendingRecordsInternal(userDto, returnCodes, entityIds, userDetailsDto);
				}
			}
			return new ServiceResponseBuilder().setStatus(false).build();
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_CONTROLLER.getConstantVal(), e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString())).build();
		}
	}

	private ServiceResponse getPendingRecordsInternal(UserDto userDto, List<String> returnCodes, List<Long> entityIds, UserDetailsDto userDetailsDto) throws ApplicationException {

		if (userDetailsDto != null) {
			if (userDto.getRoleId() != null && userDetailsDto.getUserRoleDtos().stream().filter(f -> f.getUserRoleId().equals(userDto.getRoleId())).findAny().orElse(null) == null) {
				throw new ApplicationException("PASSED ROLE ID not EXIST", "PASSED ROLE ID not EXIST");
			}

			List<Long> activityIds = userRoleService.getActivityIdByRoleIds(userDetailsDto.getUserRoleDtos().stream().map(m -> m.getUserRoleId()).collect(Collectors.toList()));

			List<ReturnApprovalDataDto> returnApprovalDtos = new ArrayList<>();
			if (!CollectionUtils.isEmpty(returnCodes) && !CollectionUtils.isEmpty(entityIds) && !CollectionUtils.isEmpty(activityIds)) {
				if (userDto.getIsCount()) {
					Long filingCount = returnApprovalDetailRepo.getPendingForApprovalDataCountV2(activityIds, userDto.getUserId(), entityIds, returnCodes);

					return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(filingCount.intValue()).build();
				} else {
					Map<String, Object> valueMap = new HashMap<String, Object>();
					valueMap.put(ColumnConstants.USER_ID.getConstantVal(), userDto.getUserId());
					valueMap.put(ColumnConstants.ENTITYID.getConstantVal(), entityIds);
					valueMap.put(ColumnConstants.RETURN_CODE_LIST.getConstantVal(), returnCodes);
					valueMap.put(ColumnConstants.ACTIVITY_IDS.getConstantVal(), activityIds);
					valueMap.put(ColumnConstants.LANG_CODE.getConstantVal(), userDto.getLangCode());

					List<ReturnApprovalDetail> returnApprovalDetailList = returnApprovalDetailService.getDataByObject(valueMap, MethodConstants.GET_PENDING_FOR_APPROVAL_DATA_V2.getConstantVal());
					for (ReturnApprovalDetail returnApprovalDetail : returnApprovalDetailList) {
						try {
							ReturnApprovalDataDto returnApprovalDto = new ReturnApprovalDataDto();
							returnApprovalDto.setUploadId(returnApprovalDetail.getUploadId());
							returnApprovalDto.setReturnApprovalDtlId(returnApprovalDetail.getReturnApprovalDetailId());
							returnApprovalDto.setReturnId(returnApprovalDetail.getReturnId());
							returnApprovalDto.setEntityId(returnApprovalDetail.getEntityId());
							returnApprovalDto.setEntityCode(returnApprovalDetail.getEntityCode());
							returnApprovalDto.setFilingStatusId(returnApprovalDetail.getFilingStatusId());
							returnApprovalDto.setRoleId(returnApprovalDetail.getUserRoleId());
							returnApprovalDto.setUploadedUserId(returnApprovalDetail.getUserId());
							if (returnApprovalDetail.getUserName() != null) {
								returnApprovalDto.setUploadedUserName(AESV2.getInstance().decrypt(returnApprovalDetail.getUserName()));
							}
							returnApprovalDto.setPreviousUploadId(returnApprovalDetail.getPreviousUploadId());
							returnApprovalDto.setWorkflowId(returnApprovalDetail.getWorkflowId());
							returnApprovalDto.setCurrentStep(returnApprovalDetail.getWorkflowStep());

							returnApprovalDto.setUploadChannel(returnApprovalDetail.getUploadChannelDesc());
							returnApprovalDto.setUploadChannelId(returnApprovalDetail.getUploadChannelId());
							returnApprovalDto.setFileType(returnApprovalDetail.getFileType());
							returnApprovalDto.setInstanceName(returnApprovalDetail.getInstanceFile());
							returnApprovalDto.setFileName(returnApprovalDetail.getFileName());
							returnApprovalDto.setSupportiveDocName(returnApprovalDetail.getAttachedFile());
							returnApprovalDto.setOriginalSupportiveDocName(returnApprovalDetail.getSupportiveDocName());
							returnApprovalDto.setFilingStatus(returnApprovalDetail.getStatus());
							returnApprovalDto.setFrequency(returnApprovalDetail.getFrequencyName());
							returnApprovalDto.setFileStatusId(returnApprovalDetail.getFilingStatusId());
							returnApprovalDto.setEndDateInLong(returnApprovalDetail.getEndDate().getTime());
							returnApprovalDto.setStartDateInLong(returnApprovalDetail.getStartDate().getTime());
							returnApprovalDto.setReturnCode(returnApprovalDetail.getReturnCode());

							returnApprovalDto.setUnlockRequestId(returnApprovalDetail.getUnlockRequestId());

							if (returnApprovalDetail.getReturnPropertyValue() != null) {
								returnApprovalDto.setReturnPropertyVal(returnApprovalDetail.getReturnPropertyValue() + "");
							}

							returnApprovalDto.setReturnName(returnApprovalDetail.getReturnName());

							returnApprovalDto.setEntityName(returnApprovalDetail.getEntityName());

							try {
								if (returnApprovalDetail.getUploadedDate() != null) {
									returnApprovalDto.setUploadedDate(DateManip.formatAppDateTime(returnApprovalDetail.getUploadedDate(), userDto.getDateFormat(), userDto.getCalendarFormat()));
								}
							} catch (Exception e) {
								LOGGER.error("Exception while parsing uploaded date : ", e);
							}

							if (returnApprovalDetail.getTaxonomyId() != null) {
								returnApprovalDto.setTemplateVersionNo(returnApprovalDetail.getTaxonomyId() + "");
							}

							returnApprovalDtos.add(returnApprovalDto);
						} catch (Exception e) {
							LOGGER.error("Exception for approval detail Id " + returnApprovalDetail.getReturnApprovalDetailId());
						}
					}
				}
				return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(returnApprovalDtos).build();
			} else {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0832.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0832.toString())).build();
			}
		} else {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0832.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0832.toString())).build();
		}
	}

}
