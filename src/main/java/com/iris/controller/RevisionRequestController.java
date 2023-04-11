/**
 * 
 */
package com.iris.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.DataListDto;
import com.iris.dto.DynamicContent;
import com.iris.dto.EntityMasterDto;
import com.iris.dto.MailServiceBean;
import com.iris.dto.RetUploadDetBean;
import com.iris.dto.ReturnDto;
import com.iris.dto.ReturnGroupMappingDto;
import com.iris.dto.ReturnGroupMappingRequest;
import com.iris.dto.RequestApprovalBean;
import com.iris.dto.RevisionRequestBean;
import com.iris.dto.RevisionRequestDto;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ServiceException;
import com.iris.model.Category;
import com.iris.model.CategoryLabel;
import com.iris.model.EntityBean;
import com.iris.model.EntityLabelBean;
import com.iris.model.Frequency;
import com.iris.model.FrequencyDescription;
import com.iris.model.Return;
import com.iris.model.ReturnLabel;
import com.iris.model.ReturnPropertyValue;
import com.iris.model.RevisionRequest;
import com.iris.model.SubCategory;
import com.iris.model.SubCategoryLabel;
import com.iris.model.UserMaster;
import com.iris.model.UserRoleMaster;
import com.iris.repository.RevisionRequestRepository;
import com.iris.service.GenericService;
import com.iris.service.impl.FilingCalendarService;
import com.iris.service.impl.ReturnRegulatorMappingService;
import com.iris.service.impl.RevisionRequestService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author Siddique Khan
 *
 */

@RestController
@RequestMapping("/service/revisionRequest")
public class RevisionRequestController {

	private static final Logger logger = LoggerFactory.getLogger(RevisionRequestController.class);
	private final String isAllowedToSubmitCheck = "field.allowedToSubmitCheck";
	private static final String INPUT_DATE_FORMAT = "ddMMyyyy";

	@Autowired
	FileMetaDataValidateController fileMetaDataValidateController;

	@Autowired
	GenericService<EntityBean, Long> entityService;

	@Autowired
	GenericService<Return, Long> returnService;

	@Autowired
	private PrepareSendMailController prepareSendMailController;

	@Autowired
	ReturnRegulatorMappingService returnRegulatorMappingService;

	@Autowired
	private GenericService<UserMaster, Long> userMasterService;

	@Autowired
	private RevisionRequestService revisionRequestService;

	@Autowired
	private RevisionRequestRepository revisionRequestRepo;

	@Autowired
	FilingCalendarService filingCalendarService;

	@Autowired
	private ReturnGroupController returnGroupController;

	@Autowired
	private NotificationController notificationController;

	@Autowired
	EntityMasterController entityMasterController;

	private static final Object lock1 = new Object();

	private static final String OPEN = "OPEN";

	private static final String CLOSED = "CLOSED";

	@PostMapping("/getPendingRevisionRequest")
	public ServiceResponse getPendingRevisionRequest(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody RequestApprovalBean revisionRequestApproval) {

		logger.info("request received to get pending unlock request for job processingid" + jobProcessId);
		List<RevisionRequest> revisionRequestLists = null;
		ServiceResponse response = null;
		List<Long> userEntityList = null;
		try {
			UserMaster userMaster = userMasterService.getDataById(revisionRequestApproval.getUserId());
			if (userMaster == null || userMaster.getRoleType() == null || userMaster.getRoleType().getRoleTypeId() == 2) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			}

			if (userMaster.getDepartmentIdFk() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0801.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0801.toString())).build();
			}

			Map<String, Object> columnValueMap = new HashMap<>();

			columnValueMap.put(ColumnConstants.LANG_ID.getConstantVal(), revisionRequestApproval.getLangId());
			columnValueMap.put(ColumnConstants.REGULATORID.getConstantVal(), userMaster.getDepartmentIdFk().getRegulatorId());
			columnValueMap.put(ColumnConstants.USER_ID.getConstantVal(), userMaster.getUserId());

			if (revisionRequestApproval.getEntityId() != null) {
				userEntityList = new ArrayList<>();
				userEntityList.add(revisionRequestApproval.getEntityId());
			}
			columnValueMap.put(ColumnConstants.CATEGORY_ID.getConstantVal(), revisionRequestApproval.getCategoryId());
			columnValueMap.put(ColumnConstants.SUB_CATEGORY_ID_LIST.getConstantVal(), revisionRequestApproval.getSubCatIdList());

			List<Long> returnIdList = revisionRequestApproval.getReturnIdList();
			if (revisionRequestApproval.getReturnIdList() == null || revisionRequestApproval.getReturnIdList().isEmpty()) {
				returnIdList = new ArrayList<>();
				ReturnGroupMappingRequest returnGroupMappingRequest = new ReturnGroupMappingRequest();
				returnGroupMappingRequest.setIsActive(true);
				returnGroupMappingRequest.setUserId(userMaster.getUserId());
				returnGroupMappingRequest.setLangId(revisionRequestApproval.getLangId());
				returnGroupMappingRequest.setRoleId(revisionRequestApproval.getRoleId());

				List<ReturnGroupMappingDto> returnList = (List<ReturnGroupMappingDto>) returnGroupController.getReturnGroups(jobProcessId, returnGroupMappingRequest).getResponse();
				for (ReturnGroupMappingDto item : returnList) {
					returnIdList.addAll(item.getReturnList().stream().map(inner -> inner.getReturnId()).collect(Collectors.toList()));
				}
			}
			columnValueMap.put(ColumnConstants.RETURN_ID_LIST.getConstantVal(), returnIdList);

			userEntityList = new ArrayList<>();
			if (revisionRequestApproval.getEntityIdList() == null) {
				EntityMasterDto entityMasterDto = new EntityMasterDto();
				entityMasterDto.setActive(true);
				entityMasterDto.setRoleId(revisionRequestApproval.getRoleId());
				entityMasterDto.setUserId(userMaster.getUserId());

				entityMasterDto.setLanguageCode(revisionRequestApproval.getLangCode());
				List<EntityBean> entityList = (List<EntityBean>) entityMasterController.getEntityMasterList(jobProcessId, entityMasterDto).getResponse();
				if (entityList != null && !entityList.isEmpty()) {
					userEntityList.addAll(entityList.stream().map(inner -> inner.getEntityId()).collect(Collectors.toList()));
				}
			} else {
				userEntityList = revisionRequestApproval.getEntityIdList();
			}

			logger.info("Getting mapped enity list for user id for job processigid : " + jobProcessId);
			if (org.apache.commons.collections4.CollectionUtils.isEmpty(userEntityList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0727.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0639.toString())).build();
			}
			columnValueMap.put(ColumnConstants.ENTITY_ID_LIST.getConstantVal(), userEntityList);

			if (Boolean.TRUE.equals(revisionRequestApproval.getIsCount())) {
				Long revisionReqCount = revisionRequestRepo.getDataByLangIdAndRegIdCount(revisionRequestApproval.getLangId(), userMaster.getDepartmentIdFk().getRegulatorId(), userMaster.getUserId(), returnIdList, userEntityList);

				response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
				response.setResponse(revisionReqCount.intValue());
			} else {
				String startDate = DateManip.formatAppDateTime(new Date(revisionRequestApproval.getStartDateLong()), "yyyy-MM-dd", "en");
				String endDate = DateManip.formatAppDateTime(new Date(revisionRequestApproval.getEndDateLong()), "yyyy-MM-dd", "en");
				columnValueMap.put(ColumnConstants.STARTDATE.getConstantVal(), startDate);
				columnValueMap.put(ColumnConstants.ENDDATE.getConstantVal(), endDate);

				List<RevisionRequest> revisionRequestList = revisionRequestService.getDataByObject(columnValueMap, MethodConstants.GET_PENDING_REVISION_REQUEST_FOR_APPROVAL.getConstantVal());

				if (CollectionUtils.isEmpty(revisionRequestList)) {
					return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.EC0035.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0035.toString())).build();
				}

				revisionRequestLists = new ArrayList<>();
				RevisionRequest revisionRequest = null;
				EntityBean entityBean = null;
				Return returns = null;
				UserMaster userMasterBean = null;
				ReturnPropertyValue returnPropertyValue = null;
				FrequencyDescription frequencyDescription = null;
				for (RevisionRequest revisionRequests : revisionRequestList) {
					entityBean = new EntityBean();
					returns = new Return();
					userMasterBean = new UserMaster();
					revisionRequest = new RevisionRequest();
					frequencyDescription = new FrequencyDescription();

					revisionRequest.setRevisionRequestId(revisionRequests.getRevisionRequestId());
					revisionRequest.setActionIdFk(revisionRequests.getActionIdFk());
					revisionRequest.setAdminStatusIdFk(revisionRequests.getAdminStatusIdFk());

					if (revisionRequests.getReturnPropertyVal() != null && revisionRequests.getReturnPropertyVal().getReturnProprtyIdFK() != null) {
						returnPropertyValue = new ReturnPropertyValue();
						returnPropertyValue.setReturnProprtyIdFK(revisionRequests.getReturnPropertyVal().getReturnProprtyIdFK());
						returnPropertyValue.setReturnProValue(revisionRequests.getReturnPropertyVal().getReturnProValue());
						revisionRequest.setReturnPropertyVal(returnPropertyValue);
					}

					EntityLabelBean entityLabelBean = revisionRequests.getEntity().getEntityLabelSet().stream().filter(f -> f.getLanguageMaster().getLanguageId().equals(revisionRequestApproval.getLangId())).findAny().orElse(null);
					if (entityLabelBean != null) {
						entityBean.setEntityName(entityLabelBean.getEntityNameLabel());
					} else {
						entityBean.setEntityName(revisionRequests.getEntity().getEntityName());
					}
					entityBean.setEntityCode(revisionRequests.getEntity().getEntityCode().trim());
					entityBean.setEntityId(revisionRequests.getEntity().getEntityId());

					//----getting data of category and sub category
					Category categoryDto = new Category();
					CategoryLabel cagtegoryLabel = revisionRequests.getEntity().getCategory().getCatLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(revisionRequestApproval.getLangCode())).findAny().orElse(null);
					if (cagtegoryLabel != null) {
						categoryDto.setCategoryName(cagtegoryLabel.getCategoryLabel());
					} else {
						categoryDto.setCategoryName(revisionRequests.getEntity().getCategory().getCategoryName());
					}
					categoryDto.setCategoryCode(revisionRequests.getEntity().getCategory().getCategoryCode());
					entityBean.setCategory(categoryDto);

					SubCategory subCatDto = new SubCategory();
					SubCategoryLabel subLabel = revisionRequests.getEntity().getSubCategory().getSubCatLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(revisionRequestApproval.getLangCode())).findAny().orElse(null);
					if (subLabel != null) {
						subCatDto.setSubCategoryName(subLabel.getSubCategoryLabel());
					} else {
						subCatDto.setSubCategoryName(revisionRequests.getEntity().getSubCategory().getSubCategoryName());
					}
					subCatDto.setSubCategoryCode(revisionRequests.getEntity().getSubCategory().getSubCategoryCode());
					entityBean.setSubCategory(subCatDto);
					//----

					revisionRequest.setEntity(entityBean);

					ReturnLabel returnLabel = revisionRequests.getReturns().getReturnLblSet().stream().filter(x -> x.getLangIdFk().getLanguageId().equals(revisionRequestApproval.getLangId())).findAny().orElse(null);
					if (returnLabel != null) {
						returns.setReturnName(returnLabel.getReturnLabel());
					} else {
						returns.setReturnName(revisionRequests.getRetName());
					}
					returns.setReturnId(revisionRequests.getReturns().getReturnId());
					returns.setReturnCode(revisionRequests.getReturns().getReturnCode());
					revisionRequest.setReturns(returns);

					userMasterBean.setUserId(revisionRequests.getCreatedBy().getUserId());
					userMasterBean.setUserName(revisionRequests.getCreatedBy().getUserName());
					revisionRequest.setCreatedBy(userMasterBean);
					revisionRequest.setCreatedOn(revisionRequests.getCreatedOn());

					frequencyDescription.setFinYrFrquencyDesc(revisionRequests.getFrequencyDesc().getFinYrFrquencyDesc());
					frequencyDescription.setFinYrFrquencyDescId(revisionRequests.getFrequencyDesc().getFinYrFrquencyDescId());
					revisionRequest.setFrequencyDesc(frequencyDescription);
					revisionRequest.setYear(revisionRequests.getYear());
					revisionRequest.setReportingDate(revisionRequests.getReportingDate());
					revisionRequest.setReasonForRequest(revisionRequests.getReasonForRequest());
					revisionRequest.setReasonForRejection(revisionRequests.getReasonForRejection());
					revisionRequest.setStartDate(revisionRequests.getStartDate());
					revisionRequest.setEndDate(revisionRequests.getEndDate());

					revisionRequestLists.add(revisionRequest);
				}

				response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
				response.setResponse(revisionRequestLists);
			}
		} catch (Exception e) {
			logger.error("Exception while fetching return list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		logger.info("request completed to get return list for job processingid" + jobProcessId);
		return response;
	}

	public ServiceResponse getPendingRevisionRequest(String jobProcessId, RequestApprovalBean revisionRequestApproval, List<ReturnGroupMappingDto> inputReturnGroupDtoList, List<EntityBean> inputEntityList) {
		logger.info("request received to get pending unlock request for job processingid" + jobProcessId);
		List<RevisionRequest> revisionRequestLists = null;
		ServiceResponse response = null;
		List<Long> userEntityList = null;
		try {
			UserMaster userMaster = userMasterService.getDataById(revisionRequestApproval.getUserId());
			if (userMaster == null || userMaster.getRoleType() == null || userMaster.getRoleType().getRoleTypeId() == 2) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			}

			if (userMaster.getDepartmentIdFk() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0801.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0801.toString())).build();
			}

			Map<String, Object> columnValueMap = new HashMap<>();

			columnValueMap.put(ColumnConstants.LANG_ID.getConstantVal(), revisionRequestApproval.getLangId());
			columnValueMap.put(ColumnConstants.REGULATORID.getConstantVal(), userMaster.getDepartmentIdFk().getRegulatorId());
			columnValueMap.put(ColumnConstants.USER_ID.getConstantVal(), userMaster.getUserId());

			if (revisionRequestApproval.getEntityId() != null) {
				userEntityList = new ArrayList<>();
				userEntityList.add(revisionRequestApproval.getEntityId());
			}
			columnValueMap.put(ColumnConstants.CATEGORY_ID.getConstantVal(), revisionRequestApproval.getCategoryId());
			columnValueMap.put(ColumnConstants.SUB_CATEGORY_ID.getConstantVal(), revisionRequestApproval.getSubCategoryId());

			List<Long> returnIdList = revisionRequestApproval.getReturnIdList();
			if (revisionRequestApproval.getReturnIdList() == null || revisionRequestApproval.getReturnIdList().isEmpty()) {
				returnIdList = new ArrayList<>();
				for (ReturnGroupMappingDto item : inputReturnGroupDtoList) {
					returnIdList.addAll(item.getReturnList().stream().map(inner -> inner.getReturnId()).collect(Collectors.toList()));
				}
			}
			columnValueMap.put(ColumnConstants.RETURN_ID_LIST.getConstantVal(), returnIdList);

			userEntityList = new ArrayList<>();
			if (revisionRequestApproval.getEntityId() == null) {
				userEntityList.addAll(inputEntityList.stream().map(inner -> inner.getEntityId()).collect(Collectors.toList()));
			} else {
				userEntityList.add(revisionRequestApproval.getEntityId());
			}

			logger.info("Getting mapped enity list for user id for job processigid : " + jobProcessId);
			if (org.apache.commons.collections4.CollectionUtils.isEmpty(userEntityList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0727.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0639.toString())).build();
			}
			columnValueMap.put(ColumnConstants.ENTITY_ID_LIST.getConstantVal(), userEntityList);

			if (Boolean.TRUE.equals(revisionRequestApproval.getIsCount())) {
				Long revisionReqCount = revisionRequestRepo.getDataByLangIdAndRegIdCount(revisionRequestApproval.getLangId(), userMaster.getDepartmentIdFk().getRegulatorId(), userMaster.getUserId(), returnIdList, userEntityList);

				response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
				response.setResponse(revisionReqCount.intValue());
			} else {
				String startDate = DateManip.formatAppDateTime(new Date(revisionRequestApproval.getStartDateLong()), "yyyy-MM-dd", "en");
				String endDate = DateManip.formatAppDateTime(new Date(revisionRequestApproval.getEndDateLong()), "yyyy-MM-dd", "en");
				columnValueMap.put(ColumnConstants.STARTDATE.getConstantVal(), startDate);
				columnValueMap.put(ColumnConstants.ENDDATE.getConstantVal(), endDate);

				List<RevisionRequest> revisionRequestList = revisionRequestService.getDataByObject(columnValueMap, MethodConstants.GET_PENDING_REVISION_REQUEST_FOR_APPROVAL.getConstantVal());

				if (CollectionUtils.isEmpty(revisionRequestList)) {
					return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.EC0035.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0035.toString())).build();
				}

				revisionRequestLists = new ArrayList<>();
				RevisionRequest revisionRequest = null;
				EntityBean entityBean = null;
				Return returns = null;
				UserMaster userMasterBean = null;
				ReturnPropertyValue returnPropertyValue = null;
				FrequencyDescription frequencyDescription = null;
				for (RevisionRequest revisionRequests : revisionRequestList) {
					entityBean = new EntityBean();
					returns = new Return();
					userMasterBean = new UserMaster();
					revisionRequest = new RevisionRequest();
					frequencyDescription = new FrequencyDescription();

					revisionRequest.setRevisionRequestId(revisionRequests.getRevisionRequestId());
					revisionRequest.setActionIdFk(revisionRequests.getActionIdFk());
					revisionRequest.setAdminStatusIdFk(revisionRequests.getAdminStatusIdFk());

					if (revisionRequests.getReturnPropertyVal() != null && revisionRequests.getReturnPropertyVal().getReturnProprtyIdFK() != null) {
						returnPropertyValue = new ReturnPropertyValue();
						returnPropertyValue.setReturnProprtyIdFK(revisionRequests.getReturnPropertyVal().getReturnProprtyIdFK());
						returnPropertyValue.setReturnProValue(revisionRequests.getReturnPropertyVal().getReturnProValue());
						revisionRequest.setReturnPropertyVal(returnPropertyValue);
					}

					EntityLabelBean entityLabelBean = revisionRequests.getEntity().getEntityLabelSet().stream().filter(f -> f.getLanguageMaster().getLanguageId().equals(revisionRequestApproval.getLangId())).findAny().orElse(null);
					if (entityLabelBean != null) {
						entityBean.setEntityName(entityLabelBean.getEntityNameLabel());
					} else {
						entityBean.setEntityName(revisionRequests.getEntity().getEntityName());
					}
					entityBean.setEntityCode(revisionRequests.getEntity().getEntityCode().trim());
					entityBean.setEntityId(revisionRequests.getEntity().getEntityId());

					//----getting data of category and sub category
					Category categoryDto = new Category();
					CategoryLabel cagtegoryLabel = revisionRequests.getEntity().getCategory().getCatLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(revisionRequestApproval.getLangCode())).findAny().orElse(null);
					if (cagtegoryLabel != null) {
						categoryDto.setCategoryName(cagtegoryLabel.getCategoryLabel());
					} else {
						categoryDto.setCategoryName(revisionRequests.getEntity().getCategory().getCategoryName());
					}
					categoryDto.setCategoryCode(revisionRequests.getEntity().getCategory().getCategoryCode());
					entityBean.setCategory(categoryDto);

					SubCategory subCatDto = new SubCategory();
					SubCategoryLabel subLabel = revisionRequests.getEntity().getSubCategory().getSubCatLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(revisionRequestApproval.getLangCode())).findAny().orElse(null);
					if (subLabel != null) {
						subCatDto.setSubCategoryName(subLabel.getSubCategoryLabel());
					} else {
						subCatDto.setSubCategoryName(revisionRequests.getEntity().getSubCategory().getSubCategoryName());
					}
					subCatDto.setSubCategoryCode(revisionRequests.getEntity().getSubCategory().getSubCategoryCode());
					entityBean.setSubCategory(subCatDto);
					//----

					revisionRequest.setEntity(entityBean);

					ReturnLabel returnLabel = revisionRequests.getReturns().getReturnLblSet().stream().filter(x -> x.getLangIdFk().getLanguageId().equals(revisionRequestApproval.getLangId())).findAny().orElse(null);
					if (returnLabel != null) {
						returns.setReturnName(returnLabel.getReturnLabel());
					} else {
						returns.setReturnName(revisionRequests.getRetName());
					}
					returns.setReturnId(revisionRequests.getReturns().getReturnId());
					returns.setReturnCode(revisionRequests.getReturns().getReturnCode());
					revisionRequest.setReturns(returns);

					userMasterBean.setUserId(revisionRequests.getCreatedBy().getUserId());
					userMasterBean.setUserName(revisionRequests.getCreatedBy().getUserName());
					revisionRequest.setCreatedBy(userMasterBean);
					revisionRequest.setCreatedOn(revisionRequests.getCreatedOn());

					frequencyDescription.setFinYrFrquencyDesc(revisionRequests.getFrequencyDesc().getFinYrFrquencyDesc());
					frequencyDescription.setFinYrFrquencyDescId(revisionRequests.getFrequencyDesc().getFinYrFrquencyDescId());
					revisionRequest.setFrequencyDesc(frequencyDescription);
					revisionRequest.setYear(revisionRequests.getYear());
					revisionRequest.setReportingDate(revisionRequests.getReportingDate());
					revisionRequest.setReasonForRequest(revisionRequests.getReasonForRequest());
					revisionRequest.setReasonForRejection(revisionRequests.getReasonForRejection());
					revisionRequest.setStartDate(revisionRequests.getStartDate());
					revisionRequest.setEndDate(revisionRequests.getEndDate());

					revisionRequestLists.add(revisionRequest);
				}

				response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
				response.setResponse(revisionRequestLists);
			}
		} catch (Exception e) {
			logger.error("Exception while fetching return list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		logger.info("request completed to get return list for job processingid" + jobProcessId);
		return response;
	}

	@PostMapping(value = "/validateAndSave")
	public ServiceResponse validateAndSave(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody RevisionRequestDto revisionRequest) throws ServiceException, ParseException {
		String responseString = StringUtils.EMPTY;
		logger.info("Requesting Unlock Request {}", jobProcessId);
		Boolean valid = false;
		revisionRequest.setReportingPeriodStartDate(DateManip.convertStringToDate(revisionRequest.getReportingPeriodStartDateInString(), INPUT_DATE_FORMAT));
		revisionRequest.setReportingPeriodEndDate(DateManip.convertStringToDate(revisionRequest.getReportingPeriodEndDateInString(), INPUT_DATE_FORMAT));
		Map<String, Map<Boolean, List<String>>> fieldCheckListMap = new LinkedHashMap<String, Map<Boolean, List<String>>>();
		EntityBean entityObj = getEntityObj(revisionRequest.getEntityCode());
		logger.debug("EntityBean fetch Unlock {}", jobProcessId);
		Return returnObj = getReturnObject(revisionRequest.getReturnCode());
		logger.debug("Return fetch Unlock {}", jobProcessId);
		if (returnObj == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		FrequencyDescription frequencyDescription = getFrequencyDescObj(returnObj.getFrequency(), revisionRequest);
		logger.debug("FrequencyDescription fetch Unlock {}", jobProcessId);

		if (frequencyDescription == null || entityObj == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}

		RetUploadDetBean retUploadBean = getReturnUploadDetBean(revisionRequest, returnObj.getFrequency(), frequencyDescription, returnObj, entityObj);
		valid = fileMetaDataValidateController.isAllowedToUploadFilling(retUploadBean, fieldCheckListMap, returnObj);

		logger.info("validation status {} {} ", jobProcessId, valid);
		Map<Boolean, List<String>> errorMap = fieldCheckListMap.get(isAllowedToSubmitCheck);
		if (errorMap != null && !errorMap.isEmpty()) {
			List<String> errorList = errorMap.get(false);
			if (errorList != null && !errorList.isEmpty()) {
				responseString = errorList.get(0);
			} else {
				List<String> sucessList = errorMap.get(true);
				if (sucessList != null && !sucessList.isEmpty()) {
					responseString = sucessList.get(0);
				}
			}
		}
		if (StringUtils.equals(responseString, "E0625") || StringUtils.equals(responseString, "E0627") || StringUtils.equals(responseString, "E0628")) {
			UserMaster user = userMasterService.getDataById(revisionRequest.getUserId());
			revisionRequestService.saveRevisionRequest(revisionRequest, entityObj, returnObj, frequencyDescription, retUploadBean, user);

			List<DynamicContent> dynaContents = preapreDynamicContentList(revisionRequest, entityObj, returnObj, frequencyDescription, retUploadBean, user);
			sendMail(revisionRequest, dynaContents, returnObj, user, entityObj);
			notificationController.sendRevisionRequestNotificationToUsers(revisionRequest.getRevisionReqId(), false);
			ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(responseString);
			return response;
		}
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
		if (org.apache.commons.lang3.StringUtils.equals(ObjectCache.getErrorCodeKey(responseString), "filing.allowed.asFilingDate.lessThan.maxRevisionRequestDate")) {

			responseString = "error.revision.request.already.present";// E0767
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0767.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0767.toString())).setResponse(responseString).build();
		} else if (org.apache.commons.lang3.StringUtils.equalsAny(ObjectCache.getErrorCodeKey(responseString), "filling.allowed.asFilingDate.lessthan.unlockRequest.date", "error.unlockRequest.pending.for.approval", "filling.allowed.asFilingWIndow.opened", "error.unlockRequest.required")) {

			responseString = "error.filling.not.done";//E0757
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0757.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0757.toString())).setResponse(responseString).build();
		} else if (StringUtils.isNotBlank(responseString) && ObjectCache.getErrorCodeKey(responseString).contains("unlock")) {

			responseString = "error.filling.not.done";
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0757.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0757.toString())).setResponse(responseString).build();
		} else if (org.apache.commons.lang3.StringUtils.isBlank(responseString)) {
			responseString = "error.filingwindow.valid";//E0723
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0723.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0723.toString())).setResponse(responseString).build();
		}
		response.setResponse(ObjectCache.getErrorCodeKey(responseString));
		return response;
	}

	private RetUploadDetBean getReturnUploadDetBean(RevisionRequestDto revisionRequest, Frequency frequency, FrequencyDescription frequencyDescription, Return returnObj, EntityBean entityObj) {
		RetUploadDetBean retUploadDetBean = new RetUploadDetBean();
		retUploadDetBean.setUploadedByUserId(revisionRequest.getUserId());
		retUploadDetBean.setEntityId(entityObj.getEntityId());
		retUploadDetBean.setEntityCode(entityObj.getEntityCode());
		retUploadDetBean.setReturnId(returnObj.getReturnId());
		retUploadDetBean.setReturnName(returnObj.getReturnName());
		retUploadDetBean.setReturnCode(returnObj.getReturnCode());
		retUploadDetBean.setUploadedUserRoleId(revisionRequest.getRoleId());

		// retUploadDetBean.setTempUploadInstanceFilePath(fileDetailsBean.getFilePath());
		// retUploadDetBean.setTempUploadAttachmentFilePath(fileDetailsBean.getAttachementFilePath());
		// retUploadDetBean.setAttachedFileFileName(fileDetailsBean.getSupportiveDocName());

		// retUploadDetBean.setExtensionAttached(fileDetailsBean.getSupportiveDocType());
		// retUploadDetBean.setUploadInstanceFileName(fileDetailsBean.getFileName());
		// retUploadDetBean.setNillableComments(fileDetailsBean.getNillabelComments());

		String year = DateManip.convertDateToString(revisionRequest.getReportingPeriodEndDate(), DateConstants.YYYY.getDateConstants());
		String month = DateManip.convertDateToString(revisionRequest.getReportingPeriodEndDate(), DateConstants.MM.getDateConstants());

		retUploadDetBean.setFinancialMonth(month);
		retUploadDetBean.setFinancialYear(year);
		retUploadDetBean.setFrequencyId(frequency.getFrequencyId());
		retUploadDetBean.setFinYearFormatId(revisionRequest.getFinYearFormatId());
		retUploadDetBean.setFormFreqName(frequency.getFrequencyName());
		retUploadDetBean.setStartDate(DateManip.convertDateToString(revisionRequest.getReportingPeriodStartDate(), DateConstants.DD_MM_YYYY.getDateConstants()));
		retUploadDetBean.setEndDate(DateManip.convertDateToString(revisionRequest.getReportingPeriodEndDate(), DateConstants.DD_MM_YYYY.getDateConstants()));

		// to do
		retUploadDetBean.setFinYrFreqDescName(frequencyDescription.getFinYrFrquencyDesc());
		retUploadDetBean.setFinYearFreqDescId(frequencyDescription.getFinYrFrquencyDescId());
		// retUploadDetBean.setWorkFlowId(fileDetailsBean.getWorkflowId());
		// retUploadDetBean.setCurrentWFStep(fileDetailsBean.getWorkflowStepNo());
		// retUploadDetBean.setReportStatus(fileDetailsBean.getReportStatus());
		retUploadDetBean.setReturnPropertyValId(revisionRequest.getReturnPropertyValId());
		return retUploadDetBean;
	}

	private EntityBean getEntityObj(String entityCode) {
		try {
			Map<String, List<String>> columnValueMap = new HashMap<>();
			List<String> valueList = new ArrayList<>();

			valueList.add(entityCode);
			columnValueMap.put(ColumnConstants.ENTITY_CODE.getConstantVal(), valueList);
			List<EntityBean> entityList = entityService.getDataByColumnValue(columnValueMap, MethodConstants.GET_ENTITY_DATA_BY_CODE.getConstantVal());

			EntityBean entityBean = null;
			if (!CollectionUtils.isEmpty(entityList)) {
				entityBean = entityList.get(0);
				return entityBean;
			}
		} catch (Exception e) {
			logger.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}

		return null;
	}

	private Return getReturnObject(String returnCode) {
		try {
			Map<String, List<String>> columnValueMap = new HashMap<>();
			List<String> valueList = new ArrayList<>();
			valueList.add(returnCode);
			columnValueMap.put(ColumnConstants.RETURN_CODE.getConstantVal(), valueList);

			List<Return> returnList = returnService.getDataByColumnValue(columnValueMap, MethodConstants.GET_RETURN_DATA_BY_CODE.getConstantVal());

			Return returnObj = null;
			if (!CollectionUtils.isEmpty(returnList)) {
				returnObj = returnList.get(0);
				return returnObj;
			}
		} catch (Exception e) {
			logger.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}
		return null;
	}

	private FrequencyDescription getFrequencyDescObj(Frequency frequency, RevisionRequestDto revisionRequest) {
		Map<String, String> freqDescMap = new HashMap<>();
		freqDescMap.put("01-01~31-03", "freq.quarterly.jan");
		freqDescMap.put("01-04~30-06", "freq.quarterly.apr");
		freqDescMap.put("01-07~30-09", "freq.quarterly.jul");
		freqDescMap.put("01-10~31-12", "freq.quarterly.oct");

		freqDescMap.put("01-01~30-06", "freq.halfYearly.first");
		freqDescMap.put("01-04~30-09", "freq.halfYearly.first");

		freqDescMap.put("01-07~31-12", "freq.halfYearly.second");
		freqDescMap.put("01-10~31-03", "freq.halfYearly.second");

		freqDescMap.put("01-01~31-12", "freq.annually");
		freqDescMap.put("01-04~31-03", "freq.annually");

		if (revisionRequest.getReportingPeriodStartDate() != null && revisionRequest.getReportingPeriodEndDate() != null) {
			if (frequency.getFreqDesc().size() > 1) {
				String partialStartDate = DateManip.convertDateToString(revisionRequest.getReportingPeriodStartDate(), DateConstants.DD_MM.getDateConstants());
				String partialEndDate = DateManip.convertDateToString(revisionRequest.getReportingPeriodEndDate(), DateConstants.DD_MM.getDateConstants());
				if (frequency.getFrequencyId().equals(GeneralConstants.QUARTERLY_FREQ_ID.getConstantLongVal())) {
					String startYear = DateManip.convertDateToString(revisionRequest.getReportingPeriodStartDate(), DateConstants.YYYY.getDateConstants());
					String endYear = DateManip.convertDateToString(revisionRequest.getReportingPeriodEndDate(), DateConstants.YYYY.getDateConstants());
					if (!startYear.equalsIgnoreCase(endYear)) {
						return null;
					}
				}
				if (freqDescMap.get(partialStartDate + "~" + partialEndDate) != null) {
					String freqDesc = freqDescMap.get(partialStartDate + "~" + partialEndDate);
					return frequency.getFreqDesc().stream().filter(f -> f.getFinYrFrquencyDesc().equalsIgnoreCase(freqDesc)).findAny().orElse(null);
				}
			} else if (frequency.getFreqDesc().size() == 1) {
				return frequency.getFreqDesc().get(0);
			}
		}
		return null;
	}

	/**
	 * Prepare dynamic contends
	 * 
	 * @param revisionRequest
	 * @param entityObj
	 * @param returnObj
	 * @param frequencyDescription
	 * @param retUploadBean
	 * @return
	 * @throws ParseException
	 */
	private List<DynamicContent> preapreDynamicContentList(RevisionRequestDto revisionRequest, EntityBean entityObj, Return returnObj, FrequencyDescription frequencyDescription, RetUploadDetBean retUploadBean, UserMaster user) throws ParseException {
		List<DynamicContent> dynaContentLst = new ArrayList<>();

		DynamicContent dynamicContent = new DynamicContent();

		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.uploadfiling.entity"));
		dynamicContent.setValue(entityObj.getEntityName());
		dynaContentLst.add(dynamicContent);
		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.return.name"));
		dynamicContent.setValue(returnObj.getReturnName());
		dynaContentLst.add(dynamicContent);

		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.return.code"));
		dynamicContent.setValue(returnObj.getReturnCode());
		dynaContentLst.add(dynamicContent);

		if (revisionRequest.getReturnPropertyValId() != null) {
			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.return.returnProperty"));
			ReturnPropertyValue retProp = filingCalendarService.getReturnPropVal(revisionRequest.getReturnPropertyValId());
			dynamicContent.setValue(ObjectCache.getLabelKeyValue("en", retProp.getReturnProValue()));
			dynaContentLst.add(dynamicContent);
		}

		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.filing.start.date"));
		dynamicContent.setValue(DateManip.formatDate(revisionRequest.getReportingPeriodStartDateInString(), INPUT_DATE_FORMAT, revisionRequest.getDateFormat()));
		dynaContentLst.add(dynamicContent);

		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.filing.end.date"));
		dynamicContent.setValue(DateManip.formatDate(revisionRequest.getReportingPeriodEndDateInString(), INPUT_DATE_FORMAT, revisionRequest.getDateFormat()));
		dynaContentLst.add(dynamicContent);

		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.revisionReq.reasonForRequest"));
		dynamicContent.setValue(revisionRequest.getReasonOfNotProcessed());
		dynaContentLst.add(dynamicContent);

		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.filing.raisedby"));
		dynamicContent.setValue(user.getUserName());
		dynaContentLst.add(dynamicContent);

		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.filing.raisedon"));
		dynamicContent.setValue(DateManip.getCurrentDateTime(revisionRequest.getDateFormat(), revisionRequest.getTimeFormat()));
		dynaContentLst.add(dynamicContent);
		return dynaContentLst;

	}

	void sendMail(RevisionRequestDto revisionRequest, List<DynamicContent> dynamicContentList, Return returnObj, UserMaster user, EntityBean entityObj) {
		try {
			logger.info("Mail Sending started For Alert Id ");

			String processingId = UUID.randomUUID().toString();
			MailServiceBean mailServiceBean = new MailServiceBean();
			mailServiceBean.setAlertId(40l);
			mailServiceBean.setMenuId(81L);
			mailServiceBean.setRoleId(revisionRequest.getRoleId());
			mailServiceBean.setUniqueId(processingId);
			mailServiceBean.setReturnCode(returnObj.getReturnCode());
			mailServiceBean.setEntityCode(entityObj.getEntityCode());
			mailServiceBean.setUserId(revisionRequest.getUserId());
			mailServiceBean.setDynamicContentsList(dynamicContentList);

			List<MailServiceBean> mailServiceBeanList = new ArrayList<>();
			mailServiceBeanList.add(mailServiceBean);
			ServiceResponse serviceResponse = prepareSendMailController.prepareSendEmail(processingId, mailServiceBeanList);
			if (serviceResponse.isStatus()) {
				logger.info("Mail sent successfully");
			}
		} catch (Exception e) {
			logger.error("Exception while sending email", e);
		}

	}

	@PostMapping(value = "/approveRejectRevisionRequest")
	public ServiceResponse approveRejectRevisionRequest(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody RevisionRequestBean revisionRequest) {

		logger.info("request received to for approve or reject unlock request for job processingid" + jobProcessId);
		try {
			Date currentDate = new Date();

			if (revisionRequest == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0391.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0391.toString())).build();
			}
			synchronized (lock1) {
				RevisionRequest revisionRequestNew = revisionRequestService.getDataById(revisionRequest.getRevisionRequestId());
				Integer adminStatusIdFK = revisionRequestNew.getAdminStatusIdFk();

				if (GeneralConstants.STATUS_COMPLETED.getConstantIntVal() == (int) adminStatusIdFK) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0488.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0488.toString())).build();
				}
				if (GeneralConstants.STATUS_REJECTED.getConstantIntVal() == (int) adminStatusIdFK) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0489.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0489.toString())).build();
				}

				if (revisionRequest.getAdminStatusIdFk() == 2) {
					revisionRequestNew.setMaxRevisionReqDate(DateManip.convertStringToDate(revisionRequest.getMaxRevisionReqDateString(), revisionRequest.getSessionDateFormat()));
					revisionRequestNew.setRevisionStatus(OPEN);
				}

				UserMaster um = userMasterService.getDataById(revisionRequest.getApprovedByFk().getUserId());
				revisionRequestNew.setApprovedByFk(um);
				revisionRequestNew.setApprovedOn(currentDate);
				revisionRequestNew.setAdminStatusIdFk(revisionRequest.getAdminStatusIdFk());
				revisionRequestNew.setActionIdFk(2);

				if (revisionRequest.getAdminStatusIdFk() == 3) {
					revisionRequestNew.setReasonForRejection(revisionRequest.getReasonForRejection());
					revisionRequestNew.setRevisionStatus(CLOSED);

				}

				logger.info("request Compleyed for approve or reject unlock request for job processingid" + jobProcessId);
				revisionRequestService.update(revisionRequestNew);
				revisionRequestNew.setSessionDateFormat(revisionRequest.getSessionDateFormat());
				revisionRequestNew.setSessionTimeFormat(revisionRequest.getSessionTimeFormat());
				prepareSendMail(revisionRequestNew);
				notificationController.sendRevisionRequestNotificationToUsers(revisionRequest.getRevisionRequestId(), true);
			}
		} catch (Exception e) {
			logger.error("Exception while approve or reject unlock request for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}

		return new ServiceResponseBuilder().setStatus(true).build();
	}

	private void prepareSendMail(RevisionRequest revisionRequest) {
		try {
			String languageCode = "en";
			String processingId = UUID.randomUUID().toString();
			logger.info("Mail send start for approve unlock request for request id " + revisionRequest.getRevisionRequestId());
			MailServiceBean mailServiceBean = new MailServiceBean();
			List<DynamicContent> dynamicContents = new ArrayList<>();
			DynamicContent dynamicContent = new DynamicContent();

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.entity.entityName"));
			dynamicContent.setValue(revisionRequest.getEntity().getEntityName());
			dynamicContents.add(dynamicContent);

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.entity.entityCode"));
			dynamicContent.setValue(revisionRequest.getEntity().getEntityCode());
			dynamicContents.add(dynamicContent);

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.return.name"));
			dynamicContent.setValue(revisionRequest.getReturns().getReturnName());
			dynamicContents.add(dynamicContent);

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.return.code"));
			dynamicContent.setValue(revisionRequest.getReturns().getReturnCode());
			dynamicContents.add(dynamicContent);

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.revisionReq.frequencyDesc"));
			dynamicContent.setValue(ObjectCache.getLabelKeyValue(languageCode, revisionRequest.getFrequencyDesc().getFinYrFrquencyDesc()));
			dynamicContents.add(dynamicContent);

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.reporting.startdate"));
			dynamicContent.setValue(DateManip.convertDateToString(revisionRequest.getStartDate(), revisionRequest.getSessionDateFormat()));
			dynamicContents.add(dynamicContent);

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.reporting.enddate"));
			dynamicContent.setValue(DateManip.convertDateToString(revisionRequest.getEndDate(), revisionRequest.getSessionDateFormat()));
			dynamicContents.add(dynamicContent);

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.reviReq.approvalStatus"));
			if (revisionRequest.getAdminStatusIdFk() == 2) {
				dynamicContent.setValue(ObjectCache.getLabelKeyValue(languageCode, "grid.status.approved"));
				dynamicContents.add(dynamicContent);

			} else {
				dynamicContent.setValue(ObjectCache.getLabelKeyValue(languageCode, "grid.status.rejected"));
				dynamicContents.add(dynamicContent);
			}

			if (revisionRequest.getAdminStatusIdFk() == 2) {
				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.maxReviReq.reqDateNew"));
				dynamicContent.setValue(DateManip.convertDateToString(revisionRequest.getMaxRevisionReqDate(), revisionRequest.getSessionDateFormat()));
				dynamicContents.add(dynamicContent);
			}

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.revisionReq.reasonForRequest"));
			dynamicContent.setValue(revisionRequest.getReasonForRequest());
			dynamicContents.add(dynamicContent);

			dynamicContent = new DynamicContent();
			if (revisionRequest.getAdminStatusIdFk() == 2) {
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "grid.approvedBy"));
				dynamicContent.setValue(revisionRequest.getApprovedByFk().getUserName());
				dynamicContents.add(dynamicContent);

				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "grid.approvedOn"));
				dynamicContent.setValue(DateManip.convertDateToString(revisionRequest.getApprovedOn(), revisionRequest.getSessionDateFormat() + "" + revisionRequest.getSessionTimeFormat()));
				dynamicContents.add(dynamicContent);

			} else {

				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "grid.reject.comment"));
				dynamicContent.setValue(revisionRequest.getReasonForRejection());
				dynamicContents.add(dynamicContent);

				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "grid.rejectedBy"));
				dynamicContent.setValue(revisionRequest.getApprovedByFk().getUserName());
				dynamicContents.add(dynamicContent);

				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "grid.rejectedOn"));
				dynamicContent.setValue(DateManip.convertDateToString(revisionRequest.getApprovedOn(), revisionRequest.getSessionDateFormat() + " " + revisionRequest.getSessionTimeFormat()));
				dynamicContents.add(dynamicContent);

			}

			UserRoleMaster userRoleMaster = revisionRequest.getApprovedByFk().getUsrRoleMstrSet().stream().findFirst().orElse(null);
			Long userRoleId = 0l;
			if (userRoleMaster != null) {
				userRoleId = userRoleMaster.getUserRole().getUserRoleId();
			}

			mailServiceBean.setDynamicContentsList(dynamicContents);
			mailServiceBean.setUserId(revisionRequest.getApprovedByFk().getUserId());
			mailServiceBean.setRoleId(userRoleId);

			mailServiceBean.setEntityCode(revisionRequest.getEntity().getEntityCode());
			mailServiceBean.setReturnCode(revisionRequest.getReturns().getReturnCode());
			mailServiceBean.setAlertId(39l);
			mailServiceBean.setMenuId(82l);

			List<MailServiceBean> mailServiceBeanList = new ArrayList<>();
			mailServiceBeanList.add(mailServiceBean);

			ServiceResponse serviceResponse = prepareSendMailController.prepareSendEmail(processingId, mailServiceBeanList);
			if (serviceResponse.isStatus()) {
				logger.info("Mail sent successfully");
			}

		} catch (Exception e) {
			logger.error("Exception while Sending email for approve reject process for id " + revisionRequest.getRevisionRequestId(), e);
		}

	}

	@PostMapping(value = "/viewRevisionRequest")
	public ServiceResponse viewRevisionRequest(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody DataListDto dataListDto) {
		ServiceResponse response = null;
		Long regulatorId = null;
		List<Long> selectedEntityList = null;

		logger.info("request received to get revisoin request list for job processigid : " + jobProcessId);

		try {

			if (dataListDto == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0391.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0391.toString())).build();
			}

			UserMaster userMaster = userMasterService.getDataById(dataListDto.getUserId());
			if (userMaster == null || userMaster.getRoleType() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			}

			if (userMaster.getRoleType().getRoleTypeId() == 1 && userMaster.getDepartmentIdFk() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0801.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0801.toString())).build();
			} else if (userMaster.getRoleType().getRoleTypeId() == 1 && userMaster.getDepartmentIdFk() != null) {
				regulatorId = userMaster.getDepartmentIdFk().getRegulatorId();
			}

			List<Long> returnIdList = new ArrayList<>();
			if (org.apache.commons.collections4.CollectionUtils.isEmpty(dataListDto.getReturnIdList())) {
				String processingId = UUID.randomUUID().toString();
				ReturnGroupMappingRequest returnGroupMappingRequest = new ReturnGroupMappingRequest();
				returnGroupMappingRequest.setRoleId(dataListDto.getRoleId());
				returnGroupMappingRequest.setUserId(dataListDto.getUserId());
				returnGroupMappingRequest.setIsActive(true);
				returnGroupMappingRequest.setReturnGroupIds(null);
				returnGroupMappingRequest.setLangId(dataListDto.getLangId());

				ServiceResponse serviceResponse = returnGroupController.getReturnGroups(processingId, returnGroupMappingRequest);
				if (!serviceResponse.isStatus()) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0727.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0727.toString())).build();

				}

				@SuppressWarnings("unchecked")
				List<ReturnGroupMappingDto> returnGroupMappingDtoList = (List<ReturnGroupMappingDto>) serviceResponse.getResponse();
				logger.info("Getting mapped return list for role id for job processigid : " + jobProcessId);
				if (org.apache.commons.collections4.CollectionUtils.isEmpty(returnGroupMappingDtoList)) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0727.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0727.toString())).build();
				}

				returnGroupMappingDtoList.forEach(f -> {
					List<ReturnDto> returnList = f.getReturnList();
					returnList.forEach(r -> {
						returnIdList.add(r.getReturnId());
					});
				});

			}
			Map<String, Object> columnValueMap = new HashMap<>();

			if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(returnIdList)) {
				columnValueMap.put(ColumnConstants.RETURN_ID_LIST.getConstantVal(), returnIdList);
			}

			if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(dataListDto.getReturnIdList())) {
				columnValueMap.put(ColumnConstants.RETURN_ID_LIST.getConstantVal(), dataListDto.getReturnIdList());
			}
			if (dataListDto.getEntityIdList() != null) {
				selectedEntityList = dataListDto.getEntityIdList();
			}
			columnValueMap.put(ColumnConstants.LANG_CODE.getConstantVal(), dataListDto.getLangCode());
			if (dataListDto.getStartDate() != null) {
				columnValueMap.put(ColumnConstants.STARTDATE.getConstantVal(), dataListDto.getStartDate());
			}
			if (dataListDto.getSubCatIdList() != null) {
				columnValueMap.put(ColumnConstants.SUB_CATEGORY_ID_LIST.getConstantVal(), dataListDto.getSubCatIdList());
			}
			if (dataListDto.getEndDate() != null) {
				columnValueMap.put(ColumnConstants.ENDDATE.getConstantVal(), dataListDto.getEndDate());
			}
			if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(dataListDto.getStatusIdList())) {
				columnValueMap.put(ColumnConstants.REVISION_STATUS_ID_LIST.getConstantVal(), dataListDto.getStatusIdList());
			}

			if (regulatorId != null) {
				columnValueMap.put(ColumnConstants.REGULATORID.getConstantVal(), regulatorId);
			}

			if (selectedEntityList == null) {
				selectedEntityList = new ArrayList<>();
				EntityMasterDto entityMasterDto = new EntityMasterDto();
				entityMasterDto.setActive(true);
				entityMasterDto.setRoleId(dataListDto.getRoleId());
				entityMasterDto.setUserId(dataListDto.getUserId());

				entityMasterDto.setLanguageCode(dataListDto.getLangCode());
				List<EntityBean> entityList = (List<EntityBean>) entityMasterController.getEntityMasterList(jobProcessId, entityMasterDto).getResponse();
				if (entityList != null && !entityList.isEmpty()) {
					selectedEntityList.addAll(entityList.stream().map(inner -> inner.getEntityId()).collect(Collectors.toList()));
				}
			}

			logger.info("Getting mapped enity list for user id for job processigid : " + jobProcessId);
			if (org.apache.commons.collections4.CollectionUtils.isEmpty(selectedEntityList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0727.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0639.toString())).build();
			}

			if (selectedEntityList != null) {
				columnValueMap.put(ColumnConstants.ENTITY_ID_LIST.getConstantVal(), selectedEntityList);
			}

			List<RevisionRequest> revisionRequestList = revisionRequestService.getDataByObject(columnValueMap, MethodConstants.GET_REVISION_REQUEST_DATA.getConstantVal());
			logger.info("getting revision  request data by role id jobprocessing id" + jobProcessId);

			if (org.apache.commons.collections4.CollectionUtils.isEmpty(revisionRequestList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString())).build();
			}

			List<RevisionRequest> revisionRequestLists = new ArrayList<>();

			RevisionRequest revisionRequest = null;
			EntityBean entityBean = null;
			Return returns = null;
			UserMaster userMasterBean = null;
			ReturnPropertyValue returnPropertyValue = null;
			FrequencyDescription frequencyDescription = null;
			for (RevisionRequest revisionRequests : revisionRequestList) {
				entityBean = new EntityBean();
				returns = new Return();
				userMasterBean = new UserMaster();
				revisionRequest = new RevisionRequest();
				frequencyDescription = new FrequencyDescription();

				revisionRequest.setRevisionRequestId(revisionRequests.getRevisionRequestId());
				revisionRequest.setActionIdFk(revisionRequests.getActionIdFk());
				revisionRequest.setAdminStatusIdFk(revisionRequests.getAdminStatusIdFk());

				if (revisionRequests.getReturnPropertyVal() != null && revisionRequests.getReturnPropertyVal().getReturnProprtyIdFK() != null) {
					returnPropertyValue = new ReturnPropertyValue();
					returnPropertyValue.setReturnProprtyIdFK(revisionRequests.getReturnPropertyVal().getReturnProprtyIdFK());
					returnPropertyValue.setReturnProValue(revisionRequests.getReturnPropertyVal().getReturnProValue());
					revisionRequest.setReturnPropertyVal(returnPropertyValue);
				}

				EntityLabelBean entityLabelBean = revisionRequests.getEntity().getEntityLabelSet().stream().filter(f -> f.getLanguageMaster().getLanguageCode().equals(dataListDto.getLangCode())).findAny().orElse(null);
				if (entityLabelBean != null) {
					entityBean.setEntityName(entityLabelBean.getEntityNameLabel());
				} else {
					entityBean.setEntityName(revisionRequests.getEntity().getEntityName());
				}
				entityBean.setEntityCode(revisionRequests.getEntity().getEntityCode().trim());
				entityBean.setEntityId(revisionRequests.getEntity().getEntityId());

				//----getting data of category and sub category
				Category categoryDto = new Category();
				CategoryLabel cagtegoryLabel = revisionRequests.getEntity().getCategory().getCatLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(dataListDto.getLangCode())).findAny().orElse(null);
				if (cagtegoryLabel != null) {
					categoryDto.setCategoryName(cagtegoryLabel.getCategoryLabel());
				} else {
					categoryDto.setCategoryName(revisionRequests.getEntity().getCategory().getCategoryName());
				}
				categoryDto.setCategoryCode(revisionRequests.getEntity().getCategory().getCategoryCode());
				entityBean.setCategory(categoryDto);

				SubCategory subCatDto = new SubCategory();
				SubCategoryLabel subLabel = revisionRequests.getEntity().getSubCategory().getSubCatLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(dataListDto.getLangCode())).findAny().orElse(null);
				if (subLabel != null) {
					subCatDto.setSubCategoryName(subLabel.getSubCategoryLabel());
				} else {
					subCatDto.setSubCategoryName(revisionRequests.getEntity().getSubCategory().getSubCategoryName());
				}
				subCatDto.setSubCategoryCode(revisionRequests.getEntity().getSubCategory().getSubCategoryCode());
				entityBean.setSubCategory(subCatDto);
				//----

				revisionRequest.setEntity(entityBean);

				ReturnLabel returnLabel = revisionRequests.getReturns().getReturnLblSet().stream().filter(x -> x.getLangIdFk().getLanguageCode().equals(dataListDto.getLangCode())).findAny().orElse(null);
				if (returnLabel != null) {
					returns.setReturnName(returnLabel.getReturnLabel());
				} else {
					returns.setReturnName(revisionRequests.getRetName());
				}
				returns.setReturnId(revisionRequests.getReturns().getReturnId());
				returns.setReturnCode(revisionRequests.getReturns().getReturnCode());
				revisionRequest.setReturns(returns);

				userMasterBean.setUserId(revisionRequests.getCreatedBy().getUserId());
				userMasterBean.setUserName(revisionRequests.getCreatedBy().getUserName());
				revisionRequest.setCreatedBy(userMasterBean);
				revisionRequest.setCreatedOn(revisionRequests.getCreatedOn());

				frequencyDescription.setFinYrFrquencyDesc(revisionRequests.getFrequencyDesc().getFinYrFrquencyDesc());
				frequencyDescription.setFinYrFrquencyDescId(revisionRequests.getFrequencyDesc().getFinYrFrquencyDescId());
				revisionRequest.setFrequencyDesc(frequencyDescription);
				revisionRequest.setYear(revisionRequests.getYear());
				revisionRequest.setReportingDate(revisionRequests.getReportingDate());
				revisionRequest.setReasonForRequest(revisionRequests.getReasonForRequest());
				revisionRequest.setReasonForRejection(revisionRequests.getReasonForRejection());
				revisionRequest.setStartDate(revisionRequests.getStartDate());
				revisionRequest.setEndDate(revisionRequests.getEndDate());
				if (revisionRequests.getAdminStatusIdFk() != 1) {
					userMasterBean = new UserMaster();
					userMasterBean.setUserId(revisionRequests.getApprovedByFk().getUserId());
					userMasterBean.setUserName(revisionRequests.getApprovedByFk().getUserName());
					revisionRequest.setApprovedByFk(userMasterBean);
					revisionRequest.setApprovedOn(revisionRequests.getApprovedOn());
				}
				if (revisionRequests.getAdminStatusIdFk() == 2) {
					revisionRequest.setMaxRevisionReqDate(revisionRequests.getMaxRevisionReqDate());
				}
				revisionRequestLists.add(revisionRequest);
				logger.info("Revision request bean added in the list" + revisionRequests.getRevisionRequestId() + "For jobprocessing id" + jobProcessId);
			}
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(revisionRequestLists);

		} catch (Exception e) {
			logger.error("Exception while fetching revision request list ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		logger.info("request compeleted for For jobprocessing id" + jobProcessId);
		return response;

	}
}
