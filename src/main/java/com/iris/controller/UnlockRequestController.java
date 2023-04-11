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
import com.iris.dto.EntityDto;
import com.iris.dto.EntityMasterDto;
import com.iris.dto.MailServiceBean;
import com.iris.dto.RequestApprovalBean;
import com.iris.dto.RetUploadDetBean;
import com.iris.dto.ReturnDto;
import com.iris.dto.ReturnEntityMapDto;
import com.iris.dto.ReturnEntityOutputDto;
import com.iris.dto.ReturnEntityQueryOutputDto;
import com.iris.dto.ReturnGroupMappingDto;
import com.iris.dto.ReturnGroupMappingRequest;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.dto.UnlockRequestBean;
import com.iris.dto.UnlockingRequestDto;
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
import com.iris.model.SubCategory;
import com.iris.model.SubCategoryLabel;
import com.iris.model.UnlockingRequest;
import com.iris.model.UserMaster;
import com.iris.model.UserRoleMaster;
import com.iris.repository.UnlockRequestRepository;
import com.iris.service.EntityMasterControllerServiceV2;
import com.iris.service.GenericService;
import com.iris.service.impl.EntityServiceV2;
import com.iris.service.impl.FilingCalendarService;
import com.iris.service.impl.ReturnRegulatorMappingService;
import com.iris.service.impl.UnlockRequestService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author svishwakarma
 */
@RestController
@RequestMapping("/service/unlockrequest")
public class UnlockRequestController {

	private static final Logger logger = LoggerFactory.getLogger(UnlockRequestController.class);
	private final String IS_ALLOWED_TO_SUBMIT_CHECK = "field.allowedToSubmitCheck";
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
	UnlockRequestService unlockRequestService;

	@Autowired
	private GenericService<UserMaster, Long> userMasterService;

	@Autowired
	FilingCalendarService filingCalendarService;

	@Autowired
	private UnlockRequestRepository unlockingRequestRepo;

	@Autowired
	private ReturnGroupController returnGroupController;

	@Autowired
	private NotificationController notificationController;

	@Autowired
	private EntityMasterControllerServiceV2 entityMasterControllerServiceV2;

	@Autowired
	private EntityServiceV2 entityServiceV2;

	@Autowired
	EntityMasterController entityMasterController;

	private static final Object lock1 = new Object();

	private static final String OPEN = "OPEN";

	private static final String CLOSED = "CLOSED";

	@PostMapping(value = "/validateAndSave")
	public ServiceResponse validateAndSave(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody UnlockingRequestDto unlockingRequest) throws ServiceException, ParseException {
		String responseString = StringUtils.EMPTY;
		logger.info("Requesting Unlock Request " + jobProcessId);
		Boolean valid = false;
		unlockingRequest.setReportingPeriodStartDate(DateManip.convertStringToDate(unlockingRequest.getReportingPeriodStartDateInString(), INPUT_DATE_FORMAT));
		unlockingRequest.setReportingPeriodEndDate(DateManip.convertStringToDate(unlockingRequest.getReportingPeriodEndDateInString(), INPUT_DATE_FORMAT));

		Map<String, Map<Boolean, List<String>>> fieldCheckListMap = new LinkedHashMap<String, Map<Boolean, List<String>>>();
		EntityBean entityObj = getEntityObj(unlockingRequest.getEntityCode());
		logger.debug("EntityBean fetch Unlock " + jobProcessId);
		Return returnObj = getReturnObject(unlockingRequest.getReturnCode());
		if (returnObj == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		logger.debug("Return fetch Unlock " + jobProcessId);
		//FrequencyDescription frequencyDescription = getFrequencyDescObj(returnObj.getFrequency(), unlockingRequest);
		logger.debug("FrequencyDescription fetch Unlock " + jobProcessId);
		if (entityObj == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		RetUploadDetBean retUploadBean = getReturnUploadDetBean(unlockingRequest, returnObj.getFrequency(), returnObj, entityObj);
		valid = fileMetaDataValidateController.isAllowedToUploadFilling(retUploadBean, fieldCheckListMap, returnObj);
		logger.info("validation status " + jobProcessId + " " + valid);
		Map<Boolean, List<String>> errorMap = fieldCheckListMap.get(IS_ALLOWED_TO_SUBMIT_CHECK);
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
		if (StringUtils.equals(responseString, "E0629") || StringUtils.equals(responseString, "E0630") || StringUtils.equals(responseString, "E0631")) {
			unlockRequestService.saveUnlockRequest(unlockingRequest, entityObj, returnObj, retUploadBean);
			UserMaster user = userMasterService.getDataById(unlockingRequest.getUserId());
			List<DynamicContent> dynaContents = preapreDynamicContentList(unlockingRequest, entityObj, returnObj, retUploadBean, user);
			sendMail(unlockingRequest, dynaContents, returnObj, user, entityObj);
			notificationController.sendUnlockRequestNotificationToUsers(unlockingRequest.getUnlockingReqId(), false);
			ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(responseString);
			return response;
		}
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
		if (StringUtils.isNotBlank(responseString) && ObjectCache.getErrorCodeKey(responseString).toLowerCase().contains("revision")) {
			responseString = "error.filing.already.present";//E0756
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0756.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0756.toString())).setResponse(responseString).build();
		} else if (valid) {

			if (org.apache.commons.lang3.StringUtils.equalsAny(responseString, "filing.allowed.asFilingDate.lessThan.maxRevisionRequestDate", "error.filling.allowed.asMaximumRevisionCount.notExceeded", "filling.allowed.asFilingDate.lessthan.unlockRequest.date")) {
				responseString = "error.filingwindow.valid";//E0756
				response = new ServiceResponse.ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0756.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0756.toString())).setResponse(responseString).build();
			} else if (org.apache.commons.lang3.StringUtils.equalsAny(responseString, "filling.allowed.asFilingDate.lessthan.unlockRequest.date")) {
				responseString = "error.unlockReq.unlockRequestAlreadySubmitted";//E0228
				response = new ServiceResponse.ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0228.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0228.toString())).setResponse(responseString).build();
			} else {
				responseString = "error.filingwindow.valid";//E0723
				response = new ServiceResponse.ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0723.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0723.toString())).setResponse(responseString).build();
			}
		}
		response.setResponse(ObjectCache.getErrorCodeKey(responseString));
		return response;
	}

	private RetUploadDetBean getReturnUploadDetBean(UnlockingRequestDto unlockingRequest, Frequency frequency, Return returnObj, EntityBean entityObj) {
		RetUploadDetBean retUploadDetBean = new RetUploadDetBean();
		retUploadDetBean.setUploadedByUserId(unlockingRequest.getUserId());
		retUploadDetBean.setEntityId(entityObj.getEntityId());
		retUploadDetBean.setEntityCode(entityObj.getEntityCode());
		retUploadDetBean.setReturnId(returnObj.getReturnId());
		retUploadDetBean.setReturnName(returnObj.getReturnName());
		retUploadDetBean.setReturnCode(returnObj.getReturnCode());
		retUploadDetBean.setUploadedUserRoleId(unlockingRequest.getRoleId());

		// retUploadDetBean.setTempUploadInstanceFilePath(fileDetailsBean.getFilePath());
		// retUploadDetBean.setTempUploadAttachmentFilePath(fileDetailsBean.getAttachementFilePath());
		// retUploadDetBean.setAttachedFileFileName(fileDetailsBean.getSupportiveDocName());

		// retUploadDetBean.setExtensionAttached(fileDetailsBean.getSupportiveDocType());
		// retUploadDetBean.setUploadInstanceFileName(fileDetailsBean.getFileName());
		// retUploadDetBean.setNillableComments(fileDetailsBean.getNillabelComments());

		String year = DateManip.convertDateToString(unlockingRequest.getReportingPeriodEndDate(), DateConstants.YYYY.getDateConstants());
		String month = DateManip.convertDateToString(unlockingRequest.getReportingPeriodEndDate(), DateConstants.MM.getDateConstants());

		retUploadDetBean.setFinancialMonth(month);
		retUploadDetBean.setFinancialYear(year);
		retUploadDetBean.setFrequencyId(frequency.getFrequencyId());
		retUploadDetBean.setFinYearFormatId(unlockingRequest.getFinYearFormatId());
		retUploadDetBean.setFormFreqName(frequency.getFrequencyName());
		retUploadDetBean.setStartDate(DateManip.convertDateToString(unlockingRequest.getReportingPeriodStartDate(), DateConstants.DD_MM_YYYY.getDateConstants()));
		retUploadDetBean.setEndDate(DateManip.convertDateToString(unlockingRequest.getReportingPeriodEndDate(), DateConstants.DD_MM_YYYY.getDateConstants()));

		// to do
		//retUploadDetBean.setFinYrFreqDescName(frequencyDescription.getFinYrFrquencyDesc());
		//retUploadDetBean.setFinYearFreqDescId(frequencyDescription.getFinYrFrquencyDescId());
		// retUploadDetBean.setWorkFlowId(fileDetailsBean.getWorkflowId());
		// retUploadDetBean.setCurrentWFStep(fileDetailsBean.getWorkflowStepNo());
		// retUploadDetBean.setReportStatus(fileDetailsBean.getReportStatus());
		retUploadDetBean.setReturnPropertyValId(unlockingRequest.getReturnPropertyValId());
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

	private FrequencyDescription getFrequencyDescObj(Frequency frequency, UnlockingRequestDto unlockingRequest) {
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

		if (unlockingRequest.getReportingPeriodStartDate() != null && unlockingRequest.getReportingPeriodEndDate() != null) {
			if (frequency.getFreqDesc().size() > 1) {
				String partialStartDate = DateManip.convertDateToString(unlockingRequest.getReportingPeriodStartDate(), DateConstants.DD_MM.getDateConstants());
				String partialEndDate = DateManip.convertDateToString(unlockingRequest.getReportingPeriodEndDate(), DateConstants.DD_MM.getDateConstants());
				if (frequency.getFrequencyId().equals(GeneralConstants.QUARTERLY_FREQ_ID.getConstantLongVal())) {
					String startYear = DateManip.convertDateToString(unlockingRequest.getReportingPeriodStartDate(), DateConstants.YYYY.getDateConstants());
					String endYear = DateManip.convertDateToString(unlockingRequest.getReportingPeriodEndDate(), DateConstants.YYYY.getDateConstants());
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

	public ServiceResponse getPendingUnlockRequest(String jobProcessId, RequestApprovalBean unlockRequestApprovalBean, List<ReturnGroupMappingDto> inputReturnGroupDtoList, List<EntityBean> inputEntityList) {

		logger.info("request received to get pending unlock request for job processingid, UnlockRequestController.java _" + jobProcessId);
		List<UnlockingRequest> unlockRequestLists;
		ServiceResponse response = null;
		List<Long> userEntityList = null;
		try {
			UserMaster userMaster = userMasterService.getDataById(unlockRequestApprovalBean.getUserId());
			if (userMaster == null || userMaster.getRoleType() == null || userMaster.getRoleType().getRoleTypeId() == 2) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();

			}

			if (userMaster.getDepartmentIdFk() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0801.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0801.toString())).build();
			}

			Map<String, Object> columnValueMap = new HashMap<>();

			columnValueMap.put(ColumnConstants.LANG_ID.getConstantVal(), unlockRequestApprovalBean.getLangId());
			columnValueMap.put(ColumnConstants.REGULATORID.getConstantVal(), userMaster.getDepartmentIdFk().getRegulatorId());
			columnValueMap.put(ColumnConstants.USER_ID.getConstantVal(), userMaster.getUserId());
			columnValueMap.put(ColumnConstants.ENTITYID.getConstantVal(), unlockRequestApprovalBean.getEntityId());

			columnValueMap.put(ColumnConstants.CATEGORY_ID.getConstantVal(), unlockRequestApprovalBean.getCategoryId());
			columnValueMap.put(ColumnConstants.SUB_CATEGORY_ID.getConstantVal(), unlockRequestApprovalBean.getSubCategoryId());

			List<Long> returnIdList = unlockRequestApprovalBean.getReturnIdList();
			if (unlockRequestApprovalBean.getReturnIdList() == null || unlockRequestApprovalBean.getReturnIdList().isEmpty()) {
				returnIdList = new ArrayList<>();
				for (ReturnGroupMappingDto item : inputReturnGroupDtoList) {
					returnIdList.addAll(item.getReturnList().stream().map(inner -> inner.getReturnId()).collect(Collectors.toList()));
				}
			}
			columnValueMap.put(ColumnConstants.RETURN_ID_LIST.getConstantVal(), returnIdList);
			userEntityList = new ArrayList<>();
			if (unlockRequestApprovalBean.getEntityId() == null) {
				userEntityList.addAll(inputEntityList.stream().map(inner -> inner.getEntityId()).collect(Collectors.toList()));
			} else {
				userEntityList.add(unlockRequestApprovalBean.getEntityId());
			}

			logger.info("Getting mapped enity list for user id for job processigid : " + jobProcessId);
			if (org.apache.commons.collections4.CollectionUtils.isEmpty(userEntityList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0727.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0639.toString())).build();
			}
			columnValueMap.put(ColumnConstants.ENTITY_ID_LIST.getConstantVal(), userEntityList);

			if (Boolean.TRUE.equals(unlockRequestApprovalBean.getIsCount())) {

				Long unlockReqCount = unlockingRequestRepo.getDataByLangIdAndRegIdCount(unlockRequestApprovalBean.getLangId(), userMaster.getDepartmentIdFk().getRegulatorId(), userMaster.getUserId(), returnIdList, userEntityList);

				response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
				response.setResponse(unlockReqCount.intValue());
			} else {

				String startDate = DateManip.formatAppDateTime(new Date(unlockRequestApprovalBean.getStartDateLong()), "yyyy-MM-dd", "en");
				String endDate = DateManip.formatAppDateTime(new Date(unlockRequestApprovalBean.getEndDateLong()), "yyyy-MM-dd", "en");
				columnValueMap.put(ColumnConstants.STARTDATE.getConstantVal(), startDate);
				columnValueMap.put(ColumnConstants.ENDDATE.getConstantVal(), endDate);

				List<UnlockingRequest> unlockRequestList = unlockRequestService.getDataByObject(columnValueMap, MethodConstants.GET_PENDING_UNLOCK_REQUEST_FOR_APPROVAL.getConstantVal());

				if (CollectionUtils.isEmpty(unlockRequestList)) {
					return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.EC0035.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0035.toString())).build();
				}
				unlockRequestLists = new ArrayList<>();

				UnlockingRequest unlockRequest = null;
				EntityBean entityBean = null;
				Return returns = null;
				UserMaster userMasterBean = null;
				ReturnPropertyValue returnPropertyValue = null;
				FrequencyDescription frequencyDescription = null;
				for (UnlockingRequest unlockRequests : unlockRequestList) {
					entityBean = new EntityBean();
					returns = new Return();
					userMasterBean = new UserMaster();
					unlockRequest = new UnlockingRequest();
					frequencyDescription = new FrequencyDescription();

					unlockRequest.setUnlockingReqId(unlockRequests.getUnlockingReqId());
					unlockRequest.setActionIdFk(unlockRequests.getActionIdFk());
					unlockRequest.setAdminStatusIdFk(unlockRequests.getAdminStatusIdFk());

					if (unlockRequests.getReturnPropertyVal() != null && unlockRequests.getReturnPropertyVal().getReturnProprtyIdFK() != null) {
						returnPropertyValue = new ReturnPropertyValue();
						returnPropertyValue.setReturnProprtyIdFK(unlockRequests.getReturnPropertyVal().getReturnProprtyIdFK());
						returnPropertyValue.setReturnProValue(unlockRequests.getReturnPropertyVal().getReturnProValue());
						unlockRequest.setReturnPropertyVal(returnPropertyValue);
					}

					EntityLabelBean entityLabelBean = unlockRequests.getEntity().getEntityLabelSet().stream().filter(f -> f.getLanguageMaster().getLanguageId().equals(unlockRequestApprovalBean.getLangId())).findAny().orElse(null);
					if (entityLabelBean != null) {
						entityBean.setEntityName(entityLabelBean.getEntityNameLabel());
					} else {
						entityBean.setEntityName(unlockRequests.getEntity().getEntityName());
					}
					entityBean.setEntityCode(unlockRequests.getEntity().getEntityCode().trim());
					entityBean.setEntityId(unlockRequests.getEntity().getEntityId());

					//----getting data of category and sub category
					Category categoryDto = new Category();
					CategoryLabel cagtegoryLabel = unlockRequests.getEntity().getCategory().getCatLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(unlockRequestApprovalBean.getLangCode())).findAny().orElse(null);
					if (cagtegoryLabel != null) {
						categoryDto.setCategoryName(cagtegoryLabel.getCategoryLabel());
					} else {
						categoryDto.setCategoryName(unlockRequests.getEntity().getCategory().getCategoryName());
					}
					categoryDto.setCategoryCode(unlockRequests.getEntity().getCategory().getCategoryCode());
					entityBean.setCategory(categoryDto);

					SubCategory subCatDto = new SubCategory();
					SubCategoryLabel subLabel = unlockRequests.getEntity().getSubCategory().getSubCatLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(unlockRequestApprovalBean.getLangCode())).findAny().orElse(null);
					if (subLabel != null) {
						subCatDto.setSubCategoryName(subLabel.getSubCategoryLabel());
					} else {
						subCatDto.setSubCategoryName(unlockRequests.getEntity().getSubCategory().getSubCategoryName());
					}
					subCatDto.setSubCategoryCode(unlockRequests.getEntity().getSubCategory().getSubCategoryCode());
					entityBean.setSubCategory(subCatDto);
					//----

					unlockRequest.setEntity(entityBean);

					ReturnLabel returnLabel = unlockRequests.getReturns().getReturnLblSet().stream().filter(x -> x.getLangIdFk().getLanguageId().equals(unlockRequestApprovalBean.getLangId())).findAny().orElse(null);

					if (returnLabel != null) {
						returns.setReturnName(returnLabel.getReturnLabel());
					} else {
						returns.setReturnName(unlockRequests.getRetName());
					}
					returns.setReturnId(unlockRequests.getReturns().getReturnId());
					returns.setReturnCode(unlockRequests.getReturns().getReturnCode());
					unlockRequest.setReturns(returns);

					userMasterBean.setUserId(unlockRequests.getCreatedBy().getUserId());
					userMasterBean.setUserName(unlockRequests.getCreatedBy().getUserName());
					unlockRequest.setCreatedBy(userMasterBean);
					unlockRequest.setCreatedOn(unlockRequests.getCreatedOn());

					frequencyDescription.setFinYrFrquencyDesc(unlockRequests.getFrequencyDesc().getFinYrFrquencyDesc());
					frequencyDescription.setFinYrFrquencyDescId(unlockRequests.getFrequencyDesc().getFinYrFrquencyDescId());
					unlockRequest.setFrequencyDesc(frequencyDescription);
					unlockRequest.setYear(unlockRequests.getYear());
					unlockRequest.setReportingDate(unlockRequests.getReportingDate());
					unlockRequest.setReasonForUnlocking(unlockRequests.getReasonForUnlocking());
					unlockRequest.setReasonForRejection(unlockRequests.getReasonForRejection());
					unlockRequest.setStartDate(unlockRequests.getStartDate());
					unlockRequest.setEndDate(unlockRequests.getEndDate());

					unlockRequestLists.add(unlockRequest);
				}
				response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
				response.setResponse(unlockRequestLists);
			}
		} catch (Exception e) {
			logger.error("Exception while fetching return list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		logger.info("request completed to get return list for job processingid,UnlockRequestController.java _" + jobProcessId);
		return response;
	}

	@PostMapping(value = "/getPendingUnlockRequest")
	public ServiceResponse getPendingUnlockRequest(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody RequestApprovalBean unlockRequestApprovalBean) {

		logger.info("request received to get pending unlock request for job processingid, UnlockRequestController.java _" + jobProcessId);
		List<UnlockingRequest> unlockRequestLists;
		ServiceResponse response = null;

		try {
			UserMaster userMaster = userMasterService.getDataById(unlockRequestApprovalBean.getUserId());
			if (userMaster == null || userMaster.getRoleType() == null || userMaster.getRoleType().getRoleTypeId() == 2) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();

			}

			if (userMaster.getDepartmentIdFk() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0801.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0801.toString())).build();
			}

			Map<String, Object> columnValueMap = new HashMap<>();

			columnValueMap.put(ColumnConstants.LANG_ID.getConstantVal(), unlockRequestApprovalBean.getLangId());
			columnValueMap.put(ColumnConstants.REGULATORID.getConstantVal(), userMaster.getDepartmentIdFk().getRegulatorId());
			columnValueMap.put(ColumnConstants.USER_ID.getConstantVal(), userMaster.getUserId());

			//columnValueMap.put(ColumnConstants.CATEGORY_ID.getConstantVal(), unlockRequestApprovalBean.getCategoryId());
			//columnValueMap.put(ColumnConstants.SUB_CATEGORY_ID_LIST.getConstantVal(), unlockRequestApprovalBean.getSubCatIdList());
			columnValueMap.put(ColumnConstants.CATEGORY_ID.getConstantVal(), unlockRequestApprovalBean.getCatCodeList());
			columnValueMap.put(ColumnConstants.SUB_CATEGORY_CODE_LIST.getConstantVal(), unlockRequestApprovalBean.getSubCatCodeList());

			List<Long> returnIdList = unlockRequestApprovalBean.getReturnIdList();
			if (unlockRequestApprovalBean.getReturnIdList() == null || unlockRequestApprovalBean.getReturnIdList().isEmpty()) {
				returnIdList = new ArrayList<>();
				/*ReturnGroupMappingRequest returnGroupMappingRequest = new ReturnGroupMappingRequest();
				returnGroupMappingRequest.setIsActive(true);
				returnGroupMappingRequest.setUserId(useL̥rMaster.getUserId());
				returnGroupMappingRequest.setLangId(unlockRequestApprovalBean.getLangId());
				returnGroupMappingRequest.setRoleId(unlockRequestApprovalBean.getRoleId());*/
				ReturnEntityMapDto returnChannelMapReqDto = new ReturnEntityMapDto();
				returnChannelMapReqDto.setUserId(unlockRequestApprovalBean.getUserId());
				returnChannelMapReqDto.setIsActive(unlockRequestApprovalBean.getIsActive());
				returnChannelMapReqDto.setLangCode(unlockRequestApprovalBean.getLangCode());
				if (unlockRequestApprovalBean.getRoleId() != null) {
					returnChannelMapReqDto.setRoleId(unlockRequestApprovalBean.getRoleId());
				}

				List<ReturnEntityQueryOutputDto> returnEntityQueryOutputDtoList = entityMasterControllerServiceV2.fetchReturnListByRoleNEntity(returnChannelMapReqDto, jobProcessId);
				//ServiceResponse responseString = entityMasterControllerV2.getEntityReturnChannelMapp(jobProcessId,returnChannelMapReqDto);

				List<ReturnEntityOutputDto> returnEntityOutputDtoList = entityMasterControllerServiceV2.convertResultToOutputBean(returnEntityQueryOutputDtoList);
				//				List<ReturnGroupMappingDto> returnList = (List<ReturnGroupMappingDto>) returnGroupController.getReturnGroups(jobProcessId, returnGroupMappingRequest).getResponse();
				//for (ReturnEntityOutputDto item : returnEntityOutputDtoList) {
				//	returnIdList.addAll(item.getReturnList().stream().map(inner -> inner.getReturnId()).collect(Collectors.toList()));
				//}
				for (ReturnEntityOutputDto returnEntityOutputDto : returnEntityOutputDtoList) {
					returnIdList.add(returnEntityOutputDto.getReturnId());
				}
			}
			columnValueMap.put(ColumnConstants.RETURN_ID_LIST.getConstantVal(), returnIdList);

			List<Long> userEntityList = new ArrayList<>();
			if (unlockRequestApprovalBean.getEntityIdList() == null) {
				EntityMasterDto entityMasterDto = new EntityMasterDto();
				entityMasterDto.setActive(true);
				entityMasterDto.setRoleId(unlockRequestApprovalBean.getRoleId());
				entityMasterDto.setUserId(userMaster.getUserId());

				entityMasterDto.setLanguageCode(unlockRequestApprovalBean.getLangCode());
				//	List<EntityBean> entityList = (List<EntityBean>) entityMasterController.getEntityMasterList(jobProcessId, entityMasterDto).getResponse();
				List<EntityDto> entityDtos = entityServiceV2.getFlatEntityList(entityMasterDto);
				if (entityDtos != null && !entityDtos.isEmpty()) {
					for (EntityDto entityDto : entityDtos) {
						if (userEntityList.contains(entityDto.getEntityId())) {
							continue;
						}
						userEntityList.add(entityDto.getEntityId());
					}
				}
			} else {
				userEntityList = unlockRequestApprovalBean.getEntityIdList();
			}

			logger.info("Getting mapped enity list for user id for job processigid : " + jobProcessId);
			if (org.apache.commons.collections4.CollectionUtils.isEmpty(userEntityList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0727.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0639.toString())).build();
			}
			columnValueMap.put(ColumnConstants.ENTITY_ID_LIST.getConstantVal(), userEntityList);

			if (Boolean.TRUE.equals(unlockRequestApprovalBean.getIsCount())) {

				Long unlockReqCount = unlockingRequestRepo.getDataByLangIdAndRegIdCount(unlockRequestApprovalBean.getLangId(), userMaster.getDepartmentIdFk().getRegulatorId(), userMaster.getUserId(), returnIdList, userEntityList);

				response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
				response.setResponse(unlockReqCount.intValue());
			} else {
				List<UnlockingRequest> unlockRequestList = null;
				if (unlockRequestApprovalBean.getStartDateLong() == null && unlockRequestApprovalBean.getEndDateLong() == null) {
					unlockRequestList = unlockRequestService.getDataByObject(columnValueMap, MethodConstants.GET_PENDING_UNLOCK_REQUEST_FOR_APPROVAL.getConstantVal());
				} else {
					String startDate = DateManip.formatAppDateTime(new Date(unlockRequestApprovalBean.getStartDateLong()), "yyyy-MM-dd", "en");
					String endDate = DateManip.formatAppDateTime(new Date(unlockRequestApprovalBean.getEndDateLong()), "yyyy-MM-dd", "en");
					columnValueMap.put(ColumnConstants.STARTDATE.getConstantVal(), startDate);
					columnValueMap.put(ColumnConstants.ENDDATE.getConstantVal(), endDate);
					unlockRequestList = unlockRequestService.getDataByObject(columnValueMap, MethodConstants.GET_PENDING_UNLOCK_REQUEST_FOR_APPROVAL.getConstantVal());
				}

				if (CollectionUtils.isEmpty(unlockRequestList)) {
					return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.EC0035.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0035.toString())).build();
				}
				unlockRequestLists = new ArrayList<>();

				UnlockingRequest unlockRequest = null;
				EntityBean entityBean = null;
				Return returns = null;
				UserMaster userMasterBean = null;
				ReturnPropertyValue returnPropertyValue = null;
				FrequencyDescription frequencyDescription = null;
				for (UnlockingRequest unlockRequests : unlockRequestList) {
					entityBean = new EntityBean();
					returns = new Return();
					userMasterBean = new UserMaster();
					unlockRequest = new UnlockingRequest();
					frequencyDescription = new FrequencyDescription();

					unlockRequest.setUnlockingReqId(unlockRequests.getUnlockingReqId());
					unlockRequest.setActionIdFk(unlockRequests.getActionIdFk());
					unlockRequest.setAdminStatusIdFk(unlockRequests.getAdminStatusIdFk());

					if (unlockRequests.getReturnPropertyVal() != null && unlockRequests.getReturnPropertyVal().getReturnProprtyIdFK() != null) {
						returnPropertyValue = new ReturnPropertyValue();
						returnPropertyValue.setReturnProprtyIdFK(unlockRequests.getReturnPropertyVal().getReturnProprtyIdFK());
						returnPropertyValue.setReturnProValue(unlockRequests.getReturnPropertyVal().getReturnProValue());
						unlockRequest.setReturnPropertyVal(returnPropertyValue);
					}

					EntityLabelBean entityLabelBean = unlockRequests.getEntity().getEntityLabelSet().stream().filter(f -> f.getLanguageMaster().getLanguageId().equals(unlockRequestApprovalBean.getLangId())).findAny().orElse(null);
					if (entityLabelBean != null) {
						entityBean.setEntityName(entityLabelBean.getEntityNameLabel());
					} else {
						entityBean.setEntityName(unlockRequests.getEntity().getEntityName());
					}
					entityBean.setEntityCode(unlockRequests.getEntity().getEntityCode().trim());
					entityBean.setEntityId(unlockRequests.getEntity().getEntityId());

					//----getting data of category and sub category
					Category categoryDto = new Category();
					CategoryLabel cagtegoryLabel = unlockRequests.getEntity().getCategory().getCatLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(unlockRequestApprovalBean.getLangCode())).findAny().orElse(null);
					if (cagtegoryLabel != null) {
						categoryDto.setCategoryName(cagtegoryLabel.getCategoryLabel());
					} else {
						categoryDto.setCategoryName(unlockRequests.getEntity().getCategory().getCategoryName());
					}
					categoryDto.setCategoryCode(unlockRequests.getEntity().getCategory().getCategoryCode());
					entityBean.setCategory(categoryDto);

					SubCategory subCatDto = new SubCategory();
					SubCategoryLabel subLabel = unlockRequests.getEntity().getSubCategory().getSubCatLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(unlockRequestApprovalBean.getLangCode())).findAny().orElse(null);
					if (subLabel != null) {
						subCatDto.setSubCategoryName(subLabel.getSubCategoryLabel());
					} else {
						subCatDto.setSubCategoryName(unlockRequests.getEntity().getSubCategory().getSubCategoryName());
					}
					subCatDto.setSubCategoryCode(unlockRequests.getEntity().getSubCategory().getSubCategoryCode());
					entityBean.setSubCategory(subCatDto);
					//----

					unlockRequest.setEntity(entityBean);

					ReturnLabel returnLabel = unlockRequests.getReturns().getReturnLblSet().stream().filter(x -> x.getLangIdFk().getLanguageId().equals(unlockRequestApprovalBean.getLangId())).findAny().orElse(null);

					if (returnLabel != null) {
						returns.setReturnName(returnLabel.getReturnLabel());
					} else {
						returns.setReturnName(unlockRequests.getRetName());
					}
					returns.setReturnId(unlockRequests.getReturns().getReturnId());
					returns.setReturnCode(unlockRequests.getReturns().getReturnCode());
					unlockRequest.setReturns(returns);

					userMasterBean.setUserId(unlockRequests.getCreatedBy().getUserId());
					userMasterBean.setUserName(unlockRequests.getCreatedBy().getUserName());
					unlockRequest.setCreatedBy(userMasterBean);
					unlockRequest.setCreatedOn(unlockRequests.getCreatedOn());

					frequencyDescription.setFinYrFrquencyDesc(unlockRequests.getFrequencyDesc().getFinYrFrquencyDesc());
					frequencyDescription.setFinYrFrquencyDescId(unlockRequests.getFrequencyDesc().getFinYrFrquencyDescId());
					unlockRequest.setFrequencyDesc(frequencyDescription);
					unlockRequest.setYear(unlockRequests.getYear());
					unlockRequest.setReportingDate(unlockRequests.getReportingDate());
					unlockRequest.setReasonForUnlocking(unlockRequests.getReasonForUnlocking());
					unlockRequest.setReasonForRejection(unlockRequests.getReasonForRejection());
					unlockRequest.setStartDate(unlockRequests.getStartDate());
					unlockRequest.setEndDate(unlockRequests.getEndDate());

					unlockRequestLists.add(unlockRequest);
				}
				response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
				response.setResponse(unlockRequestLists);
			}
		} catch (Exception e) {
			logger.error("Exception while fetching return list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		logger.info("request completed to get return list for job processingid,UnlockRequestController.java _" + jobProcessId);
		return response;
	}

	@PostMapping(value = "/approveRejectUnlockRequest")
	public ServiceResponse approveRejectUnlockRequest(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody UnlockRequestBean unlockingRequest) {

		logger.info("request received to for approve or reject unlock request for job processingid" + jobProcessId);
		try {
			Date currentDate = new Date();
			if (unlockingRequest == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0391.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0391.toString())).build();
			}
			synchronized (lock1) {
				UnlockingRequest unlockRequestNew = unlockRequestService.getDataById(unlockingRequest.getUnlockingReqId());

				Integer adminStatusIdFK = unlockRequestNew.getAdminStatusIdFk();

				if (GeneralConstants.STATUS_COMPLETED.getConstantIntVal() == (int) adminStatusIdFK) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0488.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0488.toString())).build();
				}
				if (GeneralConstants.STATUS_REJECTED.getConstantIntVal() == (int) adminStatusIdFK) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0489.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0489.toString())).build();
				}

				if (unlockingRequest.getAdminStatusIdFk() == 2) {
					unlockRequestNew.setMaxUnlockReqDate(DateManip.convertStringToDate(unlockingRequest.getMaxUnlockReqDateString(), unlockingRequest.getSessionDateFormat()));
					unlockRequestNew.setUnlockStatus(OPEN);
				}

				UserMaster um = userMasterService.getDataById(unlockingRequest.getApprovedBy().getUserId());
				unlockRequestNew.setApprovedBy(um);
				unlockRequestNew.setApprovedOn(currentDate);
				unlockRequestNew.setAdminStatusIdFk(unlockingRequest.getAdminStatusIdFk());
				unlockRequestNew.setActionIdFk(2);
				if (unlockingRequest.getAdminStatusIdFk() == 3) {
					unlockRequestNew.setReasonForRejection(unlockingRequest.getReasonForRejection());
					unlockRequestNew.setUnlockStatus(CLOSED);
				}

				logger.info("request Compleyed for approve or reject unlock request for job processingid" + jobProcessId);
				unlockRequestService.update(unlockRequestNew);
				unlockRequestNew.setSessionDateFormat(unlockingRequest.getSessionDateFormat());
				unlockRequestNew.setSessionTimeFormat(unlockingRequest.getSessionTimeFormat());
				prepareSendMail(unlockRequestNew);
				notificationController.sendUnlockRequestNotificationToUsers(unlockingRequest.getUnlockingReqId(), true);
			}
		} catch (Exception e) {
			logger.error("Exception while approve or reject unlock request for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		return new ServiceResponseBuilder().setStatus(true).build();
	}

	private void prepareSendMail(UnlockingRequest unlockingRequest) {
		try {
			String languageCode = "en";
			String processingId = UUID.randomUUID().toString();
			logger.info("Mail send start for approve unlock request for request id " + unlockingRequest.getUnlockingReqId());
			MailServiceBean mailServiceBean = new MailServiceBean();
			List<DynamicContent> dynamicContents = new ArrayList<>();
			DynamicContent dynamicContent = new DynamicContent();

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.entity.entityName"));
			dynamicContent.setValue(unlockingRequest.getEntity().getEntityName());
			dynamicContents.add(dynamicContent);

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.entity.entityCode"));
			dynamicContent.setValue(unlockingRequest.getEntity().getEntityCode());
			dynamicContents.add(dynamicContent);

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.return.name"));
			dynamicContent.setValue(unlockingRequest.getReturns().getReturnName());
			dynamicContents.add(dynamicContent);

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.return.code"));
			dynamicContent.setValue(unlockingRequest.getReturns().getReturnCode());
			dynamicContents.add(dynamicContent);

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.revisionReq.frequencyDesc"));
			dynamicContent.setValue(ObjectCache.getLabelKeyValue(languageCode, unlockingRequest.getFrequencyDesc().getFinYrFrquencyDesc()));
			dynamicContents.add(dynamicContent);

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.reporting.startdate"));
			dynamicContent.setValue(DateManip.convertDateToString(unlockingRequest.getStartDate(), unlockingRequest.getSessionDateFormat()));
			dynamicContents.add(dynamicContent);

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.reporting.enddate"));
			dynamicContent.setValue(DateManip.convertDateToString(unlockingRequest.getEndDate(), unlockingRequest.getSessionDateFormat()));
			dynamicContents.add(dynamicContent);

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.unlock.approvalStatus"));
			if (unlockingRequest.getAdminStatusIdFk() == 2) {
				dynamicContent.setValue(ObjectCache.getLabelKeyValue(languageCode, "grid.status.approved"));
				dynamicContents.add(dynamicContent);

			} else {
				dynamicContent.setValue(ObjectCache.getLabelKeyValue(languageCode, "grid.status.rejected"));
				dynamicContents.add(dynamicContent);
			}

			if (unlockingRequest.getAdminStatusIdFk() == 2) {
				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.maxUnlockReq.reqDateNew"));
				dynamicContent.setValue(DateManip.convertDateToString(unlockingRequest.getMaxUnlockReqDate(), unlockingRequest.getSessionDateFormat()));
				dynamicContents.add(dynamicContent);
			}

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.revisionReq.reasonForRequest"));
			dynamicContent.setValue(unlockingRequest.getReasonForUnlocking());
			dynamicContents.add(dynamicContent);

			if (unlockingRequest.getAdminStatusIdFk() == 2) {
				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "grid.approvedBy"));
				dynamicContent.setValue(unlockingRequest.getApprovedBy().getUserName());
				dynamicContents.add(dynamicContent);

				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "grid.approvedOn"));
				dynamicContent.setValue(DateManip.convertDateToString(unlockingRequest.getApprovedOn(), unlockingRequest.getSessionDateFormat() + " " + unlockingRequest.getSessionTimeFormat()));
				dynamicContents.add(dynamicContent);

			} else {

				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "grid.reject.comment"));
				dynamicContent.setValue(unlockingRequest.getReasonForRejection());
				dynamicContents.add(dynamicContent);

				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "grid.rejectedBy"));
				dynamicContent.setValue(unlockingRequest.getApprovedBy().getUserName());
				dynamicContents.add(dynamicContent);

				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "grid.rejectedOn"));
				dynamicContent.setValue(DateManip.convertDateToString(unlockingRequest.getApprovedOn(), unlockingRequest.getSessionDateFormat() + " " + unlockingRequest.getSessionTimeFormat()));
				dynamicContents.add(dynamicContent);

			}

			UserRoleMaster userRoleMaster = unlockingRequest.getApprovedBy().getUsrRoleMstrSet().stream().findFirst().orElse(null);
			Long userRoleId = 0l;
			if (userRoleMaster != null) {
				userRoleId = userRoleMaster.getUserRole().getUserRoleId();
			}

			mailServiceBean.setDynamicContentsList(dynamicContents);
			mailServiceBean.setUserId(unlockingRequest.getApprovedBy().getUserId());
			if (unlockingRequest.getApprovedBy() != null) {
				mailServiceBean.setRoleId(userRoleId);
			}
			mailServiceBean.setEntityCode(unlockingRequest.getEntity().getEntityCode());
			mailServiceBean.setReturnCode(unlockingRequest.getReturns().getReturnCode());
			mailServiceBean.setAlertId(43l);
			mailServiceBean.setMenuId(85l);

			List<MailServiceBean> mailServiceBeanList = new ArrayList<>();
			mailServiceBeanList.add(mailServiceBean);

			ServiceResponse serviceResponse = prepareSendMailController.prepareSendEmail(processingId, mailServiceBeanList);
			if (serviceResponse.isStatus()) {
				logger.info("Mail sent successfully");
			}

		} catch (Exception e) {
			logger.error("Exception while Sending email for approve reject process for id " + unlockingRequest.getUnlockingReqId(), e);
		}

	}

	/**
	 * Prepare dynamic contends
	 * 
	 * @param unlockingRequest
	 * @param entityObj
	 * @param returnObj
	 * @param frequencyDescription
	 * @param retUploadBean
	 * @return
	 * @throws ParseException
	 */
	private List<DynamicContent> preapreDynamicContentList(UnlockingRequestDto unlockingRequest, EntityBean entityObj, Return returnObj, RetUploadDetBean retUploadBean, UserMaster user) throws ParseException {
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

		if (unlockingRequest.getReturnPropertyValId() != null) {
			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.return.returnProperty"));
			ReturnPropertyValue retProp = filingCalendarService.getReturnPropVal(unlockingRequest.getReturnPropertyValId());
			dynamicContent.setValue(ObjectCache.getLabelKeyValue("en", retProp.getReturnProValue()));
			dynaContentLst.add(dynamicContent);
		}

		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.filing.start.date"));
		dynamicContent.setValue(DateManip.formatDate(unlockingRequest.getReportingPeriodStartDateInString(), INPUT_DATE_FORMAT, unlockingRequest.getDateFormat()));
		dynaContentLst.add(dynamicContent);

		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.filing.end.date"));
		dynamicContent.setValue(DateManip.formatDate(unlockingRequest.getReportingPeriodEndDateInString(), INPUT_DATE_FORMAT, unlockingRequest.getDateFormat()));
		dynaContentLst.add(dynamicContent);

		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.revisionReq.reasonForRequest"));
		dynamicContent.setValue(unlockingRequest.getReasonOfNotProcessed());
		dynaContentLst.add(dynamicContent);

		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.filing.raisedby"));
		dynamicContent.setValue(user.getUserName());
		dynaContentLst.add(dynamicContent);

		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.filing.raisedon"));
		dynamicContent.setValue(DateManip.getCurrentDateTime(unlockingRequest.getDateFormat(), unlockingRequest.getTimeFormat()));
		dynaContentLst.add(dynamicContent);
		return dynaContentLst;

	}

	void sendMail(UnlockingRequestDto unlockingRequest, List<DynamicContent> dynamicContentList, Return returnObj, UserMaster user, EntityBean entityObj) {
		try {
			logger.info("Mail Sending started For Alert Id ");

			String processingId = UUID.randomUUID().toString();
			MailServiceBean mailServiceBean = new MailServiceBean();
			mailServiceBean.setAlertId(42L);
			mailServiceBean.setMenuId(84L);
			mailServiceBean.setRoleId(unlockingRequest.getRoleId());
			mailServiceBean.setUniqueId(processingId);
			mailServiceBean.setUserId(unlockingRequest.getUserId());
			mailServiceBean.setDynamicContentsList(dynamicContentList);
			mailServiceBean.setReturnCode(returnObj.getReturnCode());
			mailServiceBean.setEntityCode(entityObj.getEntityCode());
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

	@PostMapping(value = "/viewUnlockRequest")
	public ServiceResponse viewUnlockRequest(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody DataListDto dataListDto) {
		ServiceResponse response = null;
		List<Long> selectedEntityList = null;
		Long regulatorId = null;

		logger.info("request received to get unlock request list for job processigid : " + jobProcessId);
		try {

			if (dataListDto == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0391.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0391.toString())).build();
			}
			List<Long> returnIdList = new ArrayList<>();
			if (org.apache.commons.collections4.CollectionUtils.isEmpty(dataListDto.getReturnsList())) {
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

				UserMaster userMaster = userMasterService.getDataById(dataListDto.getUserId());
				if (userMaster == null || userMaster.getRoleType() == null) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
				}

				if (userMaster.getRoleType().getRoleTypeId() == 1 && userMaster.getDepartmentIdFk() == null) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0801.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0801.toString())).build();
				} else if (userMaster.getRoleType().getRoleTypeId() == 1 && userMaster.getDepartmentIdFk() != null) {
					regulatorId = userMaster.getDepartmentIdFk().getRegulatorId();
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

			if (dataListDto.getEntityId() != null) {
				//columnValueMap.put(ColumnConstants.ENTITYID.getConstantVal(), dataListDto.getEntityId());
				selectedEntityList = new ArrayList<>();
				selectedEntityList.add(dataListDto.getEntityId());
			}
			columnValueMap.put(ColumnConstants.LANG_CODE.getConstantVal(), dataListDto.getLangCode());
			columnValueMap.put(ColumnConstants.USER_ID.getConstantVal(), dataListDto.getUserId());

			if (dataListDto.getStartDate() != null) {
				columnValueMap.put(ColumnConstants.STARTDATE.getConstantVal(), dataListDto.getStartDate());
			}
			if (dataListDto.getEndDate() != null) {
				columnValueMap.put(ColumnConstants.ENDDATE.getConstantVal(), dataListDto.getEndDate());
			}
			if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(dataListDto.getStatusIdList())) {
				columnValueMap.put(ColumnConstants.UNLOCK_STATUS_ID_LIST.getConstantVal(), dataListDto.getStatusIdList());
			}

			if (regulatorId != null) {
				columnValueMap.put(ColumnConstants.REGULATORID.getConstantVal(), regulatorId);
			}

			columnValueMap.put(ColumnConstants.CATEGORY_ID.getConstantVal(), dataListDto.getCateId());
			if (dataListDto.getSubCatIdList() != null) {
				columnValueMap.put(ColumnConstants.SUB_CATEGORY_ID_LIST.getConstantVal(), dataListDto.getSubCatIdList());
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

			columnValueMap.put(ColumnConstants.ENTITY_ID_LIST.getConstantVal(), selectedEntityList);

			List<UnlockingRequest> unlockRequestList = unlockRequestService.getDataByObject(columnValueMap, MethodConstants.GET_UNLOCK_REQUEST_DATA.getConstantVal());
			logger.info("getting unlock request data by role id For jobprocessing id" + jobProcessId);

			if (org.apache.commons.collections4.CollectionUtils.isEmpty(unlockRequestList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString())).build();
			}

			List<UnlockingRequest> unlockRequestLists = new ArrayList<>();

			UnlockingRequest unlockRequest = null;
			EntityBean entityBean = null;
			Return returns = null;
			UserMaster userMasterBean = null;
			ReturnPropertyValue returnPropertyValue = null;
			FrequencyDescription frequencyDescription = null;
			for (UnlockingRequest unlockRequests : unlockRequestList) {
				entityBean = new EntityBean();
				returns = new Return();
				userMasterBean = new UserMaster();
				unlockRequest = new UnlockingRequest();
				frequencyDescription = new FrequencyDescription();

				unlockRequest.setUnlockingReqId(unlockRequests.getUnlockingReqId());
				unlockRequest.setActionIdFk(unlockRequests.getActionIdFk());
				unlockRequest.setAdminStatusIdFk(unlockRequests.getAdminStatusIdFk());

				if (unlockRequests.getReturnPropertyVal() != null && unlockRequests.getReturnPropertyVal().getReturnProprtyIdFK() != null) {
					returnPropertyValue = new ReturnPropertyValue();
					returnPropertyValue.setReturnProprtyIdFK(unlockRequests.getReturnPropertyVal().getReturnProprtyIdFK());
					returnPropertyValue.setReturnProValue(unlockRequests.getReturnPropertyVal().getReturnProValue());
					unlockRequest.setReturnPropertyVal(returnPropertyValue);
				}

				EntityLabelBean entityLabelBean = unlockRequests.getEntity().getEntityLabelSet().stream().filter(f -> f.getLanguageMaster().getLanguageCode().equals(dataListDto.getLangCode())).findAny().orElse(null);
				if (entityLabelBean != null) {
					entityBean.setEntityName(entityLabelBean.getEntityNameLabel());
				} else {
					entityBean.setEntityName(unlockRequests.getEntity().getEntityName());
				}
				entityBean.setEntityCode(unlockRequests.getEntity().getEntityCode().trim());
				entityBean.setEntityId(unlockRequests.getEntity().getEntityId());

				//----getting data of category and sub category
				Category categoryDto = new Category();
				CategoryLabel cagtegoryLabel = unlockRequests.getEntity().getCategory().getCatLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(dataListDto.getLangCode())).findAny().orElse(null);
				if (cagtegoryLabel != null) {
					categoryDto.setCategoryName(cagtegoryLabel.getCategoryLabel());
				} else {
					categoryDto.setCategoryName(unlockRequests.getEntity().getCategory().getCategoryName());
				}
				categoryDto.setCategoryCode(unlockRequests.getEntity().getCategory().getCategoryCode());
				entityBean.setCategory(categoryDto);

				SubCategory subCatDto = new SubCategory();
				SubCategoryLabel subLabel = unlockRequests.getEntity().getSubCategory().getSubCatLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(dataListDto.getLangCode())).findAny().orElse(null);
				if (subLabel != null) {
					subCatDto.setSubCategoryName(subLabel.getSubCategoryLabel());
				} else {
					subCatDto.setSubCategoryName(unlockRequests.getEntity().getSubCategory().getSubCategoryName());
				}
				subCatDto.setSubCategoryCode(unlockRequests.getEntity().getSubCategory().getSubCategoryCode());
				entityBean.setSubCategory(subCatDto);
				//----

				unlockRequest.setEntity(entityBean);

				ReturnLabel returnLabel = unlockRequests.getReturns().getReturnLblSet().stream().filter(x -> x.getLangIdFk().getLanguageCode().equals(dataListDto.getLangCode())).findAny().orElse(null);
				if (returnLabel != null) {
					returns.setReturnName(returnLabel.getReturnLabel());
				} else {
					returns.setReturnName(unlockRequests.getRetName());
				}
				returns.setReturnId(unlockRequests.getReturns().getReturnId());
				returns.setReturnCode(unlockRequests.getReturns().getReturnCode());
				unlockRequest.setReturns(returns);

				userMasterBean.setUserId(unlockRequests.getCreatedBy().getUserId());
				userMasterBean.setUserName(unlockRequests.getCreatedBy().getUserName());
				unlockRequest.setCreatedBy(userMasterBean);
				unlockRequest.setCreatedOn(unlockRequests.getCreatedOn());

				frequencyDescription.setFinYrFrquencyDesc(unlockRequests.getFrequencyDesc().getFinYrFrquencyDesc());
				frequencyDescription.setFinYrFrquencyDescId(unlockRequests.getFrequencyDesc().getFinYrFrquencyDescId());
				unlockRequest.setFrequencyDesc(frequencyDescription);
				unlockRequest.setYear(unlockRequests.getYear());
				unlockRequest.setReportingDate(unlockRequests.getReportingDate());
				unlockRequest.setReasonForUnlocking(unlockRequests.getReasonForUnlocking());
				unlockRequest.setReasonForRejection(unlockRequests.getReasonForRejection());
				unlockRequest.setStartDate(unlockRequests.getStartDate());
				unlockRequest.setEndDate(unlockRequests.getEndDate());
				if (unlockRequests.getAdminStatusIdFk() != 1) {
					userMasterBean = new UserMaster();
					userMasterBean.setUserId(unlockRequests.getApprovedBy().getUserId());
					userMasterBean.setUserName(unlockRequests.getApprovedBy().getUserName());
					unlockRequest.setApprovedBy(userMasterBean);
					unlockRequest.setApprovedOn(unlockRequests.getApprovedOn());
				}
				if (unlockRequests.getAdminStatusIdFk() == 2) {
					unlockRequest.setMaxUnlockReqDate(unlockRequests.getMaxUnlockReqDate());
				}
				unlockRequestLists.add(unlockRequest);
				logger.info("Unlock request bean added in the list" + unlockRequests.getUnlockingReqId() + "For jobprocessing id" + jobProcessId);
			}
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(unlockRequestLists);

		} catch (Exception e) {
			logger.error("Exception while fetching unlock request list ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		logger.info("request compeleted for For jobprocessing id" + jobProcessId);

		return response;

	}

}
