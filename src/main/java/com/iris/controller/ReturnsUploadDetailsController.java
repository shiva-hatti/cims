package com.iris.controller;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.iris.caching.ObjectCache;
import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.DynamicContent;
import com.iris.dto.EntityFilingPendingBean;
import com.iris.dto.EntityFilingPendingData;
import com.iris.dto.MailServiceBean;
import com.iris.dto.RetUploadDetBean;
import com.iris.dto.ReturnDto;
import com.iris.dto.ReturnGroupMappingDto;
import com.iris.dto.ReturnGroupMappingRequest;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ApplicationException;
import com.iris.model.Category;
import com.iris.model.CategoryLabel;
import com.iris.model.EmailSentHistory;
import com.iris.model.EntityBean;
import com.iris.model.EntityLabelBean;
import com.iris.model.FileDetails;
import com.iris.model.FilingStatus;
import com.iris.model.Frequency;
import com.iris.model.MISPendingMailSentHist;
import com.iris.model.Return;
import com.iris.model.ReturnEntityMappingNew;
import com.iris.model.ReturnFileFormatMap;
import com.iris.model.ReturnLabel;
import com.iris.model.ReturnPropertyValue;
import com.iris.model.ReturnTemplate;
import com.iris.model.ReturnsUploadDetails;
import com.iris.model.RevisionRequest;
import com.iris.model.SubCategory;
import com.iris.model.SubCategoryLabel;
import com.iris.model.UnlockingRequest;
import com.iris.model.UploadChannel;
import com.iris.model.UserMaster;
import com.iris.model.UserRole;
import com.iris.model.WorkFlowMasterBean;
import com.iris.service.GenericService;
import com.iris.service.impl.EmailSentHistoryService;
import com.iris.service.impl.MISPendingEmailData;
import com.iris.service.impl.MISPendingMailSentHistService;
import com.iris.service.impl.ReturnEntityMapServiceNew;
import com.iris.util.DateAndTimeArithmeticWrapperService;
import com.iris.util.ResourceUtil;
import com.iris.util.UtilMaster;
import com.iris.util.Validations;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.EmailPlaceholderConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.FrequencyEnum;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author Sajadhav
 */
@RestController
@RequestMapping("/service/returnsUploadDetailsController")
public class ReturnsUploadDetailsController {

	private static final String NO = "N";
	private static final String YES = "Y";
	private static final String ACTUALS = "ACTUALS";
	@Autowired
	private GenericService<ReturnsUploadDetails, Long> returnUploadDetailsService;
	@Autowired
	private ReturnEntityMapServiceNew returnEntityMapService;

	@Autowired
	ReturnGroupController returnGroupController;

	@Autowired
	private DateAndTimeArithmeticWrapperService dateAndTimeArithmeticWrapperService;

	@Autowired
	private MISPendingEmailData misPendingEmailData;

	@Autowired
	private MISPendingMailSentHistService misPendingMailSentHistService;

	@Autowired
	private PrepareSendMailController prepareSendMailController;

	@Autowired
	EmailSentHistoryService emailSentHistoryService;

	static final Logger LOGGER = LogManager.getLogger(ReturnsUploadDetailsController.class);

	@PostMapping(value = "/getFillingRecordsByStatus")
	public ServiceResponse getFillingRecordsByStatus(@RequestHeader("JobProcessingId") String jobProcessId, @RequestBody RetUploadDetBean retUploadDetBean) {
		try {
			LOGGER.info("Request received for Request trans Id " + jobProcessId);

			validateReturnsUploadDetailsObj(jobProcessId, retUploadDetBean);

			Map<String, Object> columnValueMap = new HashMap<>();
			columnValueMap.put(ColumnConstants.FILLING_STATUS_ID.getConstantVal(), Arrays.asList(retUploadDetBean.getFillingStatus()));
			columnValueMap.put(ColumnConstants.TOTAL_RECORD_COUNT.getConstantVal(), retUploadDetBean.getRecordCountToBeFetched());
			columnValueMap.put(ColumnConstants.FILE_TYPE.getConstantVal(), retUploadDetBean.getFileTypeList().stream().map(String::toUpperCase).collect(Collectors.toList()));
			if (!CollectionUtils.isEmpty(retUploadDetBean.getIncludeReturnCodeList())) {
				columnValueMap.put(ColumnConstants.RETURN_CODE.getConstantVal(), retUploadDetBean.getIncludeReturnCodeList());
			}

			List<ReturnsUploadDetails> returnUploadDetailsList = null;
			if (retUploadDetBean.getFillingStatusToBeChanged() != null) {
				columnValueMap.put(ColumnConstants.CHANGED_FILLING_STATUS_ID.getConstantVal(), retUploadDetBean.getFillingStatusToBeChanged());
				returnUploadDetailsList = returnUploadDetailsService.getDataByObject(columnValueMap, MethodConstants.GET_RETURNS_UPLOAD_DETALS_RECORD_BY_STATUS_AND_UPDATE_NEW_STATUS.getConstantVal());
			} else {
				returnUploadDetailsList = returnUploadDetailsService.getDataByObject(columnValueMap, MethodConstants.GET_RETURNS_UPLOAD_DETALS_RECORD_BY_STATUS_AND_TYPE.getConstantVal());
			}
			if (!CollectionUtils.isEmpty(returnUploadDetailsList)) {
				List<ReturnsUploadDetails> responseReturnUploadDetailList = prepareRetUploadDetBeanListResponse(returnUploadDetailsList, retUploadDetBean.getLangCode());
				LOGGER.info("Request successfully processed for Request trans Id " + jobProcessId);
				return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.RECORD_FETCHED_SUCCESSFULLY.getConstantVal()).setResponse(responseReturnUploadDetailList).build();
			} else {
				LOGGER.info("Empty record found for Request trans Id " + jobProcessId);
				return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorConstants.EMPTY_RECORD.getConstantVal()).setResponse(new ArrayList<ReturnsUploadDetails>()).build();
			}
		} catch (ApplicationException applicationException) {
			LOGGER.error(applicationException.getErrorMsg() + " for Transaction ID : " + jobProcessId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(applicationException.getErrorCode()).setStatusMessage(applicationException.getErrorMsg()).build();
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Transaction ID : " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString())).build();
		}
	}

	@GetMapping(value = "/getFilingPathOfLatestSuccessfulFilingByRetCodeEntCode/{returnCode}/{entityCode}")
	public ServiceResponse getFilingPathOfLatestSuccessfulFilingByRetCodeEntCode(@RequestHeader("JobProcessingId") String jobProcessId, @PathVariable String returnCode, @PathVariable String entityCode) {
		try {
			LOGGER.info("getFilingPathOfLatestSuccessfulFilingByRetCodeEntCode Request received for Request trans Id " + jobProcessId);

			Map<String, List<String>> valueMap = new HashMap<>();

			List<String> valueList = new ArrayList<>();
			valueList.add(returnCode);
			valueMap.put(ColumnConstants.RETURN_CODE.getConstantVal(), valueList);

			LOGGER.info("getFilingPathOfLatestSuccessfulFilingByRetCodeEntCode returnCode: " + returnCode + " , entityCode:" + entityCode);

			valueList = new ArrayList<>();
			valueList.add(entityCode);
			valueMap.put(ColumnConstants.ENTITY_CODE.getConstantVal(), valueList);

			List<ReturnsUploadDetails> returnUploadDetailsList = null;
			returnUploadDetailsList = returnUploadDetailsService.getDataByColumnValue(valueMap, MethodConstants.GET_SUCCESSFULL_RETURN_FILING_BY_RETURN_AND_ENTITY.getConstantVal());

			LOGGER.info("getFilingPathOfLatestSuccessfulFilingByRetCodeEntCode returnUploadDetailsList size: " + returnUploadDetailsList.size());

			if (!CollectionUtils.isEmpty(returnUploadDetailsList)) {

				String financialYear = DateManip.convertDateToString(returnUploadDetailsList.get(0).getEndDate(), DateConstants.YYYY.getDateConstants());
				String modifiedInstanceFilePath = ResourceUtil.getKeyValue("filepath.root") + ResourceUtil.getKeyValue("filepath.instanceZip") + returnUploadDetailsList.get(0).getEntity().getEntityCode().trim() + File.separator + financialYear + File.separator;
				String modifiedInstanceFileName = returnUploadDetailsList.get(0).getInstanceFile().split(Pattern.quote("."))[0] + File.separator + returnUploadDetailsList.get(0).getInstanceFile();
				modifiedInstanceFilePath += modifiedInstanceFileName;

				LOGGER.info("getFilingPathOfLatestSuccessfulFilingByRetCodeEntCode modifiedInstanceFilePath: " + modifiedInstanceFilePath);

				return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.RECORD_FETCHED_SUCCESSFULLY.getConstantVal()).setResponse(modifiedInstanceFilePath).build();
			} else {
				LOGGER.info("Empty record found for Request trans Id " + jobProcessId);
				return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorConstants.EMPTY_RECORD.getConstantVal()).setResponse("").build();
			}
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Transaction ID : " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0013.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0013.toString())).build();
		}
	}

	@PostMapping(value = "/getMisSubmitFilingEntityWise")
	public ServiceResponse getMisSubmitFilingEntityWise(@RequestHeader("JobProcessingId") String jobProcessId, @RequestBody RetUploadDetBean retUploadDetBean) {
		try {
			List<ReturnsUploadDetails> responseReturnUploadDetailList;
			if (UtilMaster.isEmpty(retUploadDetBean.getEntityIdList())) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0757.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0757.toString())).build();
			}

			responseReturnUploadDetailList = uploadDetailsEntityWise(retUploadDetBean, GeneralConstants.MIS_REPORT_SUMITTED.getConstantVal(), jobProcessId);
			if (CollectionUtils.isEmpty(responseReturnUploadDetailList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0757.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0757.toString())).build();
			} else {
				List<ReturnsUploadDetails> responseReturnUploadDetailList1 = prepareRetSubmittedResponse(responseReturnUploadDetailList, retUploadDetBean.getLangCode());
				return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.RECORD_FETCHED_SUCCESSFULLY.getConstantVal()).setResponse(responseReturnUploadDetailList1).build();

			}
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Transaction ID : " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0804.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0804.toString())).build();
		}
	}

	@PostMapping(value = "/getMisSubmitFilingReturnWise")
	public ServiceResponse getMisSubmitFilingReturnWise(@RequestHeader("JobProcessingId") String jobProcessId, @RequestBody RetUploadDetBean retUploadDetBean) {
		try {
			List<ReturnsUploadDetails> responseReturnUploadDetailList;

			if (UtilMaster.isEmpty(retUploadDetBean.getEntityIdList())) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0757.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0757.toString())).build();
			}

			responseReturnUploadDetailList = uploadDetailsReturnWise(retUploadDetBean, GeneralConstants.MIS_REPORT_SUMITTED.getConstantVal(), jobProcessId);
			if (CollectionUtils.isEmpty(responseReturnUploadDetailList)) {
				return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.E0757.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0757.toString())).build();
			} else {
				List<ReturnsUploadDetails> responseReturnUploadDetailList1 = prepareRetSubmittedResponse(responseReturnUploadDetailList, retUploadDetBean.getLangCode());
				return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.RECORD_FETCHED_SUCCESSFULLY.getConstantVal()).setResponse(responseReturnUploadDetailList1).build();
			}
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Transaction ID : " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0804.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0804.toString())).build();
		}
	}

	@PostMapping(value = "/getMisPendingFilingReturnWise")
	public ServiceResponse getMisPendingFilingReturnWise(@RequestHeader("JobProcessingId") String jobProcessId, @RequestBody RetUploadDetBean retUploadDetBean) {
		try {
			List<ReturnsUploadDetails> responseReturnUploadDetailList;

			if (UtilMaster.isEmpty(retUploadDetBean.getEntityIdList())) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0757.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0757.toString())).build();
			}

			responseReturnUploadDetailList = uploadDetailsReturnWise(retUploadDetBean, GeneralConstants.MIS_REPORT_PENDING.getConstantVal(), jobProcessId);

			List<Long> retList = new ArrayList<>();
			for (String str : retUploadDetBean.getReturnIdList()) {
				retList.add(Long.valueOf(str));
			}
			List<Long> entList = new ArrayList<>();
			for (String str : retUploadDetBean.getEntityIdList()) {
				entList.add(Long.valueOf(str));
			}

			Map<String, Object> valueMap = new HashMap<>();
			valueMap.put(ColumnConstants.ENTITY_ID_LIST.getConstantVal(), entList);
			valueMap.put(ColumnConstants.RETURN_ID_LIST.getConstantVal(), retList);
			valueMap.put(ColumnConstants.FREQUENCY_ID.getConstantVal(), retUploadDetBean.getFinYearFreqDescId());
			List<ReturnEntityMappingNew> returnChannelMappingNewList = returnEntityMapService.getDataByObject(valueMap, MethodConstants.GET_RET_ENT_MAP_BY_ENT_RET.getConstantVal());
			if (CollectionUtils.isEmpty(returnChannelMappingNewList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0549.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0549.toString())).build();
			}

			List<ReturnsUploadDetails> responseReturnUploadDetailListPending = preparePendingList(responseReturnUploadDetailList, retUploadDetBean, returnChannelMappingNewList);

			List<ReturnsUploadDetails> responseReturnUploadDetailList1 = prepareRetPenDetBeanListResponse(responseReturnUploadDetailListPending, retUploadDetBean.getLangCode());
			return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.RECORD_FETCHED_SUCCESSFULLY.getConstantVal()).setResponse(responseReturnUploadDetailList1).build();
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Transaction ID : " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0804.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0804.toString())).build();
		}
	}

	@PostMapping(value = "/getMisPendingFilingEntityWise")
	public ServiceResponse getMisPendingFilingEntityWise(@RequestHeader("JobProcessingId") String jobProcessId, @RequestBody RetUploadDetBean retUploadDetBean) {
		try {
			List<ReturnsUploadDetails> responseReturnUploadDetailList;
			if (UtilMaster.isEmpty(retUploadDetBean.getEntityIdList())) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0757.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0757.toString())).build();
			}

			responseReturnUploadDetailList = uploadDetailsEntityWise(retUploadDetBean, GeneralConstants.MIS_REPORT_PENDING.getConstantVal(), jobProcessId);

			List<Long> retList = new ArrayList<>();
			for (String str : retUploadDetBean.getReturnIdList()) {
				retList.add(Long.valueOf(str));
			}

			List<Long> entIdList = new ArrayList<>();
			for (String str : retUploadDetBean.getEntityIdList()) {
				entIdList.add(Long.valueOf(str));
			}

			List<Long> catIdList = new ArrayList<>();
			for (String str : retUploadDetBean.getSubCategoryIdList()) {
				catIdList.add(Long.valueOf(str));
			}

			Map<String, Object> valueMap = new HashMap<>();
			valueMap.put(ColumnConstants.ENTITY_ID_LIST.getConstantVal(), entIdList);
			valueMap.put(ColumnConstants.RETURN_ID_LIST.getConstantVal(), retList);
			valueMap.put(ColumnConstants.FREQUENCY_ID.getConstantVal(), retUploadDetBean.getFinYearFreqDescId());
			valueMap.put(ColumnConstants.END_DATE_LIST.getConstantVal(), retUploadDetBean.getEndDateList());
			valueMap.put(ColumnConstants.END_DATE_LIST.getConstantVal(), retUploadDetBean.getEndDateList());
			valueMap.put(ColumnConstants.SUB_CATEGORY_ID_LIST.getConstantVal(), catIdList);

			List<ReturnEntityMappingNew> returnChannelMappingNewList;
			returnChannelMappingNewList = returnEntityMapService.getDataByObject(valueMap, MethodConstants.GET_RET_ENT_MAP_BY_ENT_RET_CAT.getConstantVal());

			if (CollectionUtils.isEmpty(returnChannelMappingNewList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0825.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0825.toString())).build();
			}

			List<ReturnsUploadDetails> responseReturnUploadDetailListPending = preparePendingList(responseReturnUploadDetailList, retUploadDetBean, returnChannelMappingNewList);

			List<ReturnsUploadDetails> responseReturnUploadDetailList1 = prepareRetPenDetBeanListResponse(responseReturnUploadDetailListPending, retUploadDetBean.getLangCode());
			return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.RECORD_FETCHED_SUCCESSFULLY.getConstantVal()).setResponse(responseReturnUploadDetailList1).build();
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Transaction ID : " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0804.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0804.toString())).build();
		}
	}

	public List<ReturnsUploadDetails> preparePendingList(List<ReturnsUploadDetails> responseReturnUploadDetailList, RetUploadDetBean retUploadDetBean, List<ReturnEntityMappingNew> returnChannelMappingNewList) {
		Map<Long, List<Long>> returnEntMap = new HashMap<>();
		Map<Long, List<String>> returnIdEndDatesListMap = retUploadDetBean.getReturnIdEndDateListMap();

		//Preparing return and entity map(return,EntityList)
		for (ReturnEntityMappingNew returnEntityMappingNew : returnChannelMappingNewList) {
			List<Long> ent1List = returnEntMap.get(returnEntityMappingNew.getReturnObj().getReturnId());
			if (ent1List == null) {
				ent1List = new ArrayList<>();
				ent1List.add(returnEntityMappingNew.getEntity().getEntityId());
				returnEntMap.put(returnEntityMappingNew.getReturnObj().getReturnId(), ent1List);
			} else {
				ent1List.add(returnEntityMappingNew.getEntity().getEntityId());
				returnEntMap.put(returnEntityMappingNew.getReturnObj().getReturnId(), ent1List);
			}
		}

		Map<Long, Map<Date, List<Long>>> returnIdEndDateEntityList = new HashMap<>();
		for (Long retId : returnEntMap.keySet()) {

			List<Long> entIdList = new ArrayList<>(returnEntMap.get(retId).size());
			entIdList.addAll(returnEntMap.get(retId));
			//needs to add loop for end dates (Map<returnId,Map<endDate,listOfPendingEntity>)

			Map<Date, List<Long>> dateAndEntityIdListMap = new HashMap<>();

			//In responseReturnUploadDetailList there can be multiple returns with multiple entities so instead of looping just sorting list by returnId(retId) and entityId(from entIdList) combination
			List<ReturnsUploadDetails> filteredReturnUploadList = responseReturnUploadDetailList.stream().filter(retUpDet -> entIdList.stream().anyMatch(entId -> retUpDet.getEntity().getEntityId().equals(entId)) && retUpDet.getReturnObj().getReturnId().equals(retId)).collect(Collectors.toList());

			List<String> endDatesInString = returnIdEndDatesListMap.get(retId);

			for (String endDateStr : endDatesInString) {
				List<Long> entntIdListNew = new ArrayList<>(entIdList.size());
				entntIdListNew.addAll(entIdList);
				Date endDate = null;
				try {
					endDate = DateManip.convertStringToDate(endDateStr, DateConstants.DD_MM_YYYY.getDateConstants());
				} catch (ParseException e) {
					continue;
				}
				List<Long> tempEntIdList = new ArrayList<>();
				for (ReturnsUploadDetails returnsUploadDetails : filteredReturnUploadList) {
					for (Long entId : returnEntMap.get(retId)) {
						if (entId.equals(returnsUploadDetails.getEntity().getEntityId()) && returnsUploadDetails.getEndDate().compareTo(endDate) == 0) {
							tempEntIdList.add(entId);
						}
					}
				}
				//From all the entityList respective to return Id, if match found in the ReturnUploadDetails(submitted data) with the date then removing that entity from the list (need not to show in the pending report)
				for (Long entId : tempEntIdList) {
					entntIdListNew.remove(entId);
				}
				dateAndEntityIdListMap.put(endDate, entntIdListNew);
			}

			if (CollectionUtils.isEmpty(dateAndEntityIdListMap)) {
				dateAndEntityIdListMap = new HashMap<>();
				returnIdEndDateEntityList.put(retId, dateAndEntityIdListMap);
			} else {
				returnIdEndDateEntityList.put(retId, dateAndEntityIdListMap);
			}
		}
		// if NO any instance is submitted for that return id the all entity related to that return should be added into pending
		for (Long retId : returnEntMap.keySet()) {
			if (!returnIdEndDateEntityList.containsKey(retId)) {
				List<Long> entIdList = returnEntMap.get(retId);
				Map<Date, List<Long>> dateAndEntityIdListMap = new HashMap<>();
				List<String> endDatesInString = returnIdEndDatesListMap.get(retId);
				for (String endDateStr : endDatesInString) {
					Date endDate = null;
					try {
						endDate = DateManip.convertStringToDate(endDateStr, DateConstants.DD_MM_YYYY.getDateConstants());
					} catch (ParseException e) {
						continue;
					}
					dateAndEntityIdListMap.put(endDate, entIdList);
				}
				returnIdEndDateEntityList.put(retId, dateAndEntityIdListMap);
			}
		}

		ReturnsUploadDetails returnUpload = null;
		List<ReturnsUploadDetails> responseReturnUploadDetailListPending = new ArrayList<>();

		Set<Long> retIds = returnIdEndDateEntityList.keySet();
		for (Long pendingRetId : retIds) {
			Map<Date, List<Long>> dateAndEntityIdListMap = returnIdEndDateEntityList.get(pendingRetId);
			Set<Date> allEndDates = dateAndEntityIdListMap.keySet();
			for (Date pendingEndDate : allEndDates) {
				List<Long> pendingEntityIdList = dateAndEntityIdListMap.get(pendingEndDate);
				if (!CollectionUtils.isEmpty(pendingEntityIdList)) {
					for (Long pendingEntId : pendingEntityIdList) {
						for (ReturnEntityMappingNew returnEntMapping : returnChannelMappingNewList) {
							if (pendingEntId.equals(returnEntMapping.getEntity().getEntityId()) && pendingRetId.equals(returnEntMapping.getReturnObj().getReturnId())) {
								returnUpload = new ReturnsUploadDetails();
								returnUpload.setReturnObj(returnEntMapping.getReturnObj());
								returnUpload.setEntity(returnEntMapping.getEntity());
								returnUpload.setFrequency(returnEntMapping.getReturnObj().getFrequency());
								returnUpload.setStartDate(pendingEndDate);
								returnUpload.setEndDate(pendingEndDate);
								returnUpload.setEndDateInLong(pendingEndDate.getTime());
								responseReturnUploadDetailListPending.add(returnUpload);
							}
						}
					}
				}
			}
		}

		return responseReturnUploadDetailListPending;
	}

	public List<ReturnsUploadDetails> prepareNewPendingListAsPerPeriod(List<ReturnsUploadDetails> submittedListReturnUploadDetailList, List<String> endDates, List<ReturnEntityMappingNew> returnChannelMappingNewList) throws ParseException {

		List<ReturnsUploadDetails> pendingList = new ArrayList<>();
		try {
			List<Date> tempDates = null;
			List<Date> allEndDates = new ArrayList<>();
			for (String endDate : endDates) {
				Date tempEndDate = DateManip.convertStringToDate(endDate, DateConstants.DD_MM_YYYY.getDateConstants());
				allEndDates.add(tempEndDate);
			}

			for (ReturnEntityMappingNew returnEntityMappingNew : returnChannelMappingNewList) {
				Long returnId = returnEntityMappingNew.getReturnId();

				if (submittedListReturnUploadDetailList != null && !submittedListReturnUploadDetailList.isEmpty()) {
					for (ReturnsUploadDetails returnsUploadDetails : submittedListReturnUploadDetailList) {
						tempDates = new ArrayList<>();
						for (Date tempEndDate : allEndDates) {
							tempDates.add(tempEndDate);
							if (tempEndDate.equals(returnsUploadDetails.getEndDate()) && returnsUploadDetails.getReturnObj().getReturnId().equals(returnId)) {
								tempDates.remove(tempEndDate);
							}
						}

					}
				} else {
					tempDates = new ArrayList<>();
					for (String endDate : endDates) {
						Date tempEndDate = DateManip.convertStringToDate(endDate, DateConstants.DD_MM_YYYY.getDateConstants());
						tempDates.add(tempEndDate);
					}
				}
				//for Remaining dates filing is not done so preparing data for those dates 
				for (Date tempDate : tempDates) {
					ReturnsUploadDetails returnUpload;
					returnUpload = new ReturnsUploadDetails();
					returnUpload.setReturnObj(returnEntityMappingNew.getReturnObj());
					returnUpload.setEntity(returnEntityMappingNew.getEntity());
					returnUpload.setFrequency(returnEntityMappingNew.getReturnObj().getFrequency());
					returnUpload.setStartDate(tempDate);
					returnUpload.setEndDate(tempDate);
					pendingList.add(returnUpload);
				}
			}

		} catch (com.iris.exception.ServiceException e) {
			throw e;
		}
		return pendingList;
	}

	private List<ReturnsUploadDetails> uploadDetailsEntityWise(RetUploadDetBean returnsUploadDetailsObj, String misReportType, String jobProcessId) throws ServiceException, ParseException, ServiceException {
		try {
			Map<String, List<String>> valueMap = new HashMap<>();
			List<String> valueList = new ArrayList<>();

			List<Long> mappedReturn = new ArrayList<>();
			if (returnsUploadDetailsObj.getReturnIdList() == null || returnsUploadDetailsObj.getReturnIdList().isEmpty()) {
				ReturnGroupMappingRequest returnGroupMappingRequest = new ReturnGroupMappingRequest();
				returnGroupMappingRequest.setIsActive(true);
				returnGroupMappingRequest.setUserId(returnsUploadDetailsObj.getLogedInUser().getUserId());
				returnGroupMappingRequest.setLangId(returnsUploadDetailsObj.getLangId());
				returnGroupMappingRequest.setRoleId(returnsUploadDetailsObj.getLogedInUser().getRoleId());

				List<ReturnGroupMappingDto> returnList = (List<ReturnGroupMappingDto>) returnGroupController.getReturnGroups(jobProcessId, returnGroupMappingRequest).getResponse();
				for (ReturnGroupMappingDto item : returnList) {
					if (misReportType.equals(GeneralConstants.MIS_REPORT_PENDING.getConstantVal())) {
						for (ReturnDto returnDto : item.getReturnList()) {
							if (returnDto.getFrequency().getFrequencyId() != 7 && returnDto.getFrequency().getFrequencyId() != 9 && returnDto.getFrequency().getFrequencyId() != 14) {
								mappedReturn.add(returnDto.getReturnId());
							}
						}
					} else {
						mappedReturn.addAll(item.getReturnList().stream().map(inner -> inner.getReturnId()).collect(Collectors.toList()));
					}
				}

				//-------------------

				List<String> newReturnIdList = new ArrayList<>(mappedReturn.size());
				for (Long myInt : mappedReturn) {
					newReturnIdList.add(String.valueOf(myInt));
				}
				returnsUploadDetailsObj.setReturnIdList(newReturnIdList);
				//----------------------------
			}

			Date startDate = DateManip.convertStringToDate(returnsUploadDetailsObj.getStartDate(), returnsUploadDetailsObj.getSessionDateFormat());
			String inputDatStr = DateManip.convertDateToString(startDate, DateConstants.DD_MM_YYYY.getDateConstants());
			returnsUploadDetailsObj.setStartDate(inputDatStr);
			Date endDate = DateManip.convertStringToDate(returnsUploadDetailsObj.getEndDate(), returnsUploadDetailsObj.getSessionDateFormat());
			String inputEnDatStr = DateManip.convertDateToString(endDate, DateConstants.DD_MM_YYYY.getDateConstants());
			returnsUploadDetailsObj.setEndDate(inputEnDatStr);

			if (misReportType.equals(GeneralConstants.MIS_REPORT_PENDING.getConstantVal())) {
				List<Long> retIdListInLong = mappedReturn.stream().collect(Collectors.toList());
				Map<String, Long> returnIdAndFrequency = new HashMap<>();
				;

				List<Return> returnList = (List<Return>) returnGroupController.getReturnListWithFrequency(mappedReturn, returnsUploadDetailsObj.getLangCode());
				for (Return returnVal : returnList) {
					returnIdAndFrequency.put(returnVal.getReturnId() + "", returnVal.getFrequency().getFrequencyId());
				}

				Map<Long, List<String>> returnIdEndDatesList = new HashMap<>();
				List<String> allEndDateList = new ArrayList<>();

				for (Long returnId : retIdListInLong) {
					String frequencyId = FrequencyEnum.getCodeById(returnIdAndFrequency.get(returnId + ""));
					if (frequencyId == null) {
						continue;
					}
					List<String> endDateList = dateAndTimeArithmeticWrapperService.getAllPossibleEndDatesBetweenPeriod(jobProcessId, com.iris.util.DateUtilsParser.Frequency.getEnumByfreqPeriod(frequencyId), false, startDate, endDate, "dd-MM-yyyy");
					returnIdEndDatesList.put(returnId, endDateList);

					//Preparing all endDateList for query date in(End date list)
					for (String dateStr : endDateList) {
						if (!allEndDateList.contains(dateStr)) {
							allEndDateList.add(dateStr);
						}
					}
				}
				returnsUploadDetailsObj.setReturnIdEndDateListMap(returnIdEndDatesList);
				returnsUploadDetailsObj.setEndDateList(allEndDateList);
			}

			valueList = new ArrayList<>();
			valueList.add(returnsUploadDetailsObj.getStartDate() + "");
			valueMap.put(ColumnConstants.STARTDATE.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(returnsUploadDetailsObj.getEndDate() + "");
			valueMap.put(ColumnConstants.ENDDATE.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(returnsUploadDetailsObj.getFinYearFreqDescId() + "");
			valueMap.put(ColumnConstants.FIN_YEAR_FREQ_DESC_ID.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(returnsUploadDetailsObj.getReturnIdList() + "");
			valueMap.put(ColumnConstants.RETURN_ID_LIST.getConstantVal(), returnsUploadDetailsObj.getReturnIdList());

			valueList = new ArrayList<>();
			valueList.add(returnsUploadDetailsObj.getEntityIdList() + "");
			valueMap.put(ColumnConstants.ENTITY_ID_LIST.getConstantVal(), returnsUploadDetailsObj.getEntityIdList());

			valueList = new ArrayList<>();
			valueList.add(returnsUploadDetailsObj.getReturnPropertyValId() + "");
			valueMap.put(ColumnConstants.RETURN_PROPERTY_VAL_ID.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(misReportType);
			valueMap.put(ColumnConstants.MIS_REPORT_TYPE.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(returnsUploadDetailsObj.getSubCategoryIdList() + "");
			valueMap.put(ColumnConstants.SUB_CATEGORY_ID_LIST.getConstantVal(), returnsUploadDetailsObj.getSubCategoryIdList());

			if (misReportType.equals(GeneralConstants.MIS_REPORT_PENDING.getConstantVal())) {
				valueList = new ArrayList<>();
				valueList.add(returnsUploadDetailsObj.getEndDateList() + "");
				valueMap.put(ColumnConstants.END_DATE_LIST.getConstantVal(), returnsUploadDetailsObj.getEndDateList());
			}

			return returnUploadDetailsService.getDataByColumnValue(valueMap, MethodConstants.GET_RETURN_UPLOAD_DETAILS_BY_ENTITY_WISE.getConstantVal());

		} catch (com.iris.exception.ServiceException e) {
			throw e;
		}
	}

	private List<ReturnsUploadDetails> uploadDetailsReturnWise(RetUploadDetBean returnsUploadDetailsObj, String misReportType, String jobProcessId) throws ServiceException, ParseException, ServiceException {
		try {

			List<Long> mappedReturn = new ArrayList<>();
			//------------If Return is not selected then fetch all returns mapped to role id-------
			if (returnsUploadDetailsObj.getReturnIdList() == null || returnsUploadDetailsObj.getReturnIdList().isEmpty()) {
				ReturnGroupMappingRequest returnGroupMappingRequest = new ReturnGroupMappingRequest();
				returnGroupMappingRequest.setIsActive(true);
				returnGroupMappingRequest.setUserId(returnsUploadDetailsObj.getLogedInUser().getUserId());
				returnGroupMappingRequest.setLangId(returnsUploadDetailsObj.getLangId());
				returnGroupMappingRequest.setRoleId(returnsUploadDetailsObj.getLogedInUser().getRoleId());

				List<ReturnGroupMappingDto> returnList = (List<ReturnGroupMappingDto>) returnGroupController.getReturnGroups(jobProcessId, returnGroupMappingRequest).getResponse();
				for (ReturnGroupMappingDto item : returnList) {
					if (misReportType.equals(GeneralConstants.MIS_REPORT_PENDING.getConstantVal())) {
						for (ReturnDto returnDto : item.getReturnList()) {
							if (returnDto.getFrequency().getFrequencyId() != 7 && returnDto.getFrequency().getFrequencyId() != 9 && returnDto.getFrequency().getFrequencyId() != 14) {
								mappedReturn.add(returnDto.getReturnId());
							}
						}
					} else {
						mappedReturn.addAll(item.getReturnList().stream().map(inner -> inner.getReturnId()).collect(Collectors.toList()));
					}
				}

				List<String> newReturnIdList = new ArrayList<>(mappedReturn.size());
				for (Long myInt : mappedReturn) {
					newReturnIdList.add(String.valueOf(myInt));
				}
				returnsUploadDetailsObj.setReturnIdList(newReturnIdList);
				//----------------------------
			}

			Date startDate = DateManip.convertStringToDate(returnsUploadDetailsObj.getStartDate(), returnsUploadDetailsObj.getSessionDateFormat());
			String inputDatStr = DateManip.convertDateToString(startDate, DateConstants.DD_MM_YYYY.getDateConstants());
			returnsUploadDetailsObj.setStartDate(inputDatStr);
			Date endDate = DateManip.convertStringToDate(returnsUploadDetailsObj.getEndDate(), returnsUploadDetailsObj.getSessionDateFormat());
			String inputEnDatStr = DateManip.convertDateToString(endDate, DateConstants.DD_MM_YYYY.getDateConstants());
			returnsUploadDetailsObj.setEndDate(inputEnDatStr);

			if (misReportType.equals(GeneralConstants.MIS_REPORT_PENDING.getConstantVal())) {
				List<Long> retIdListInLong = returnsUploadDetailsObj.getReturnIdList().stream().map(Long::parseLong).collect(Collectors.toList());
				Map<String, Long> returnIdAndFrequency = new HashMap<>();

				List<Return> returnList = (List<Return>) returnGroupController.getReturnListWithFrequency(retIdListInLong, returnsUploadDetailsObj.getLangCode());
				for (Return returnVal : returnList) {
					returnIdAndFrequency.put(returnVal.getReturnId() + "", returnVal.getFrequency().getFrequencyId());
				}

				Map<Long, List<String>> returnIdEndDatesList = new HashMap<>();
				List<String> allEndDateList = new ArrayList<>();

				for (Long returnId : retIdListInLong) {
					String frequencyId = FrequencyEnum.getCodeById(returnIdAndFrequency.get(returnId + ""));
					if (frequencyId == null) {
						continue;
					}
					List<String> endDateList = dateAndTimeArithmeticWrapperService.getAllPossibleEndDatesBetweenPeriod(jobProcessId, com.iris.util.DateUtilsParser.Frequency.getEnumByfreqPeriod(frequencyId), false, startDate, endDate, "dd-MM-yyyy");
					returnIdEndDatesList.put(returnId, endDateList);

					//Preparing all endDateList for query date in(End date list)
					for (String dateStr : endDateList) {
						if (!allEndDateList.contains(dateStr)) {
							allEndDateList.add(dateStr);
						}
					}
				}
				returnsUploadDetailsObj.setReturnIdEndDateListMap(returnIdEndDatesList);
				returnsUploadDetailsObj.setEndDateList(allEndDateList);
			}

			Map<String, List<String>> valueMap = new HashMap<>();
			List<String> valueList = new ArrayList<>();
			valueList.add(returnsUploadDetailsObj.getReturnIdList() + "");
			valueMap.put(ColumnConstants.RETURN_ID_LIST.getConstantVal(), returnsUploadDetailsObj.getReturnIdList());

			valueList = new ArrayList<>();
			valueList.add(returnsUploadDetailsObj.getStartDate() + "");
			valueMap.put(ColumnConstants.STARTDATE.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(returnsUploadDetailsObj.getEndDate() + "");
			valueMap.put(ColumnConstants.ENDDATE.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(returnsUploadDetailsObj.getFinYearFreqDescId() + "");
			valueMap.put(ColumnConstants.FIN_YEAR_FREQ_DESC_ID.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(returnsUploadDetailsObj.getEntityIdList() + "");
			valueMap.put(ColumnConstants.ENTITY_ID_LIST.getConstantVal(), returnsUploadDetailsObj.getEntityIdList());

			valueList = new ArrayList<>();
			valueList.add(returnsUploadDetailsObj.getReturnPropertyValId() + "");
			valueMap.put(ColumnConstants.RETURN_PROPERTY_VAL_ID.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(misReportType);
			valueMap.put(ColumnConstants.MIS_REPORT_TYPE.getConstantVal(), valueList);

			if (misReportType.equals(GeneralConstants.MIS_REPORT_PENDING.getConstantVal())) {
				valueList = new ArrayList<>();
				valueList.add(returnsUploadDetailsObj.getEndDateList() + "");
				valueMap.put(ColumnConstants.END_DATE_LIST.getConstantVal(), returnsUploadDetailsObj.getEndDateList());
			}

			return returnUploadDetailsService.getDataByColumnValue(valueMap, MethodConstants.GET_RETURN_UPLOAD_DETAILS_BY_RETURN_WISE.getConstantVal());

		} catch (com.iris.exception.ServiceException e) {
			throw e;
		}
	}

	private void validateReturnsUploadDetailsObj(String requestTxnId, RetUploadDetBean retUploadDetBean) throws ApplicationException {
		String errorMessage = "";

		if (UtilMaster.isEmpty(requestTxnId)) {
			errorMessage = ErrorConstants.REQUEST_TRANSACTION_ID_NOT_FOUND.getConstantVal();
		}

		if (UtilMaster.isEmpty(retUploadDetBean.getFillingStatus())) {
			if (errorMessage.equals("")) {
				errorMessage = ErrorConstants.FILLING_STATUS_NOT_FOUND.getConstantVal();
			} else {
				errorMessage = errorMessage + ", " + ErrorConstants.FILLING_STATUS_NOT_FOUND.getConstantVal();
			}
		}

		if (UtilMaster.isEmpty(retUploadDetBean.getRecordCountToBeFetched())) {
			if (errorMessage.equals("")) {
				errorMessage = ErrorConstants.RECORD_COUNT_NOT_FOUND.getConstantVal();
			} else {
				errorMessage = errorMessage + ", " + ErrorConstants.RECORD_COUNT_NOT_FOUND.getConstantVal();
			}
		} else {
			if (retUploadDetBean.getRecordCountToBeFetched() <= 0) {
				if (errorMessage.equals("")) {
					errorMessage = ErrorConstants.RECORD_COUNT_SHOULD_BE_GREATER_THAN_0.getConstantVal();
				} else {
					errorMessage = errorMessage + ", " + ErrorConstants.RECORD_COUNT_SHOULD_BE_GREATER_THAN_0.getConstantVal();
				}
			}
		}

		if (UtilMaster.isEmpty(retUploadDetBean.getFileTypeList())) {
			if (errorMessage.equals("")) {
				errorMessage = ErrorConstants.FILE_TYPE_LIST_NOT_FOUND.getConstantVal();
			} else {
				errorMessage = errorMessage + ", " + ErrorConstants.FILE_TYPE_LIST_NOT_FOUND.getConstantVal();
			}
		}
		if (UtilMaster.isEmpty(retUploadDetBean.getLangCode())) {
			if (errorMessage.equals("")) {
				errorMessage = ErrorConstants.LANGUAG_CODE_NOT_FOUND.getConstantVal();
			} else {
				errorMessage = errorMessage + ", " + ErrorConstants.LANGUAG_CODE_NOT_FOUND.getConstantVal();
			}
		}

		if (!errorMessage.equals("")) {
			throw new ApplicationException(ErrorCode.EC0391.toString(), ObjectCache.getErrorCodeKey(ErrorCode.EC0391.toString()));
		}
	}

	private List<ReturnsUploadDetails> prepareRetPenDetBeanListResponse(List<ReturnsUploadDetails> returnUploadDetailsList, String langCode) {
		List<ReturnsUploadDetails> responseRetUploadDetBeanList = new ArrayList<>();

		for (ReturnsUploadDetails returnsUploadDetails : returnUploadDetailsList) {
			ReturnsUploadDetails responseRetUploadDetBean = new ReturnsUploadDetails();

			Return returnDto = new Return();

			ReturnLabel returnLabel = returnsUploadDetails.getReturnObj().getReturnLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(langCode)).findAny().orElse(null);
			returnDto.setReturnCode(returnsUploadDetails.getReturnObj().getReturnCode());
			returnDto.setReturnId(returnsUploadDetails.getReturnObj().getReturnId());
			if (returnLabel != null) {
				returnDto.setReturnName(returnLabel.getReturnLabel());
			} else {
				returnDto.setReturnName(returnsUploadDetails.getReturnObj().getReturnName());
			}

			responseRetUploadDetBean.setReturnObj(returnDto);

			EntityBean entityDto = new EntityBean();

			EntityLabelBean entityLabelBean = returnsUploadDetails.getEntity().getEntityLabelSet().stream().filter(f -> f.getLanguageMaster().getLanguageCode().equalsIgnoreCase(langCode)).findAny().orElse(null);
			entityDto.setEntityId(returnsUploadDetails.getEntity().getEntityId());
			entityDto.setEntityCode(returnsUploadDetails.getEntity().getEntityCode());
			if (entityLabelBean != null) {
				entityDto.setEntityName(entityLabelBean.getEntityNameLabel());
			} else {
				entityDto.setEntityName(returnsUploadDetails.getEntity().getEntityName());
			}

			Category categoryDto = new Category();
			CategoryLabel cagtegoryLabel = returnsUploadDetails.getEntity().getCategory().getCatLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(langCode)).findAny().orElse(null);
			if (cagtegoryLabel != null) {
				categoryDto.setCategoryName(cagtegoryLabel.getCategoryLabel());
			} else {
				categoryDto.setCategoryName(returnsUploadDetails.getEntity().getCategory().getCategoryName());
			}
			categoryDto.setCategoryCode(returnsUploadDetails.getEntity().getCategory().getCategoryCode());
			entityDto.setCategory(categoryDto);

			SubCategory subCatDto = new SubCategory();
			SubCategoryLabel subLabel = returnsUploadDetails.getEntity().getSubCategory().getSubCatLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(langCode)).findAny().orElse(null);
			if (subLabel != null) {
				subCatDto.setSubCategoryName(subLabel.getSubCategoryLabel());
			} else {
				subCatDto.setSubCategoryName(returnsUploadDetails.getEntity().getSubCategory().getSubCategoryName());
			}
			subCatDto.setSubCategoryCode(returnsUploadDetails.getEntity().getSubCategory().getSubCategoryCode());
			entityDto.setSubCategory(subCatDto);

			Frequency frequeDto = new Frequency();
			frequeDto.setFrequencyId(returnsUploadDetails.getFrequency().getFrequencyId());
			frequeDto.setFrequencyName(returnsUploadDetails.getFrequency().getFrequencyName());
			responseRetUploadDetBean.setFrequency(frequeDto);

			responseRetUploadDetBean.setEntity(entityDto);
			responseRetUploadDetBean.setStartDate(returnsUploadDetails.getStartDate());
			responseRetUploadDetBean.setEndDate(returnsUploadDetails.getEndDate());
			responseRetUploadDetBean.setEndDateInLong(returnsUploadDetails.getEndDate().getTime());
			responseRetUploadDetBeanList.add(responseRetUploadDetBean);
		}
		return responseRetUploadDetBeanList;
	}

	private List<ReturnsUploadDetails> prepareRetUploadDetBeanListResponse(List<ReturnsUploadDetails> returnUploadDetailsList, String langCode) {
		List<ReturnsUploadDetails> responseRetUploadDetBeanList = new ArrayList<>();

		for (ReturnsUploadDetails returnsUploadDetails : returnUploadDetailsList) {
			ReturnsUploadDetails responseRetUploadDetBean = new ReturnsUploadDetails();
			responseRetUploadDetBean.setUploadedDate(returnsUploadDetails.getUploadedDate());
			responseRetUploadDetBean.setUploadId(returnsUploadDetails.getUploadId());
			responseRetUploadDetBean.setStartDate(returnsUploadDetails.getStartDate());
			responseRetUploadDetBean.setEndDate(returnsUploadDetails.getEndDate());
			responseRetUploadDetBean.setStartDateInLong(returnsUploadDetails.getStartDate().getTime());
			responseRetUploadDetBean.setEndDateInLong(returnsUploadDetails.getEndDate().getTime());
			if (!Objects.isNull(returnsUploadDetails.getFileDetailsBean())) {
				responseRetUploadDetBean.setApplicationProcessId(returnsUploadDetails.getFileDetailsBean().getApplicationProcessId());

				if (returnsUploadDetails.getFileDetailsBean().getCreationDate() != null) {
					responseRetUploadDetBean.setUploadedOnInLong(returnsUploadDetails.getFileDetailsBean().getCreationDate().getTime());
				}
			}
			responseRetUploadDetBean.setNillable(returnsUploadDetails.isNillable());
			if (!UtilMaster.isEmpty(returnsUploadDetails.getNillableComments())) {
				responseRetUploadDetBean.setNillableComments(returnsUploadDetails.getNillableComments());
			}
			Frequency frequeDto = new Frequency();
			frequeDto.setFrequencyId(returnsUploadDetails.getFrequency().getFrequencyId());
			frequeDto.setFrequencyName(ObjectCache.getLabelKeyValue("en", returnsUploadDetails.getFrequency().getFrequencyName()));
			frequeDto.setFrequencyCode(returnsUploadDetails.getFrequency().getFrequencyCode());
			responseRetUploadDetBean.setFrequency(frequeDto);

			Return returnDto = new Return();
			returnDto.setReturnId(returnsUploadDetails.getReturnObj().getReturnId());
			returnDto.setReturnCode(returnsUploadDetails.getReturnObj().getReturnCode());

			ReturnLabel returnLabel = returnsUploadDetails.getReturnObj().getReturnLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(langCode)).findAny().orElse(null);

			if (returnLabel != null) {
				returnDto.setReturnName(returnLabel.getReturnLabel());
			} else {
				returnDto.setReturnName(returnsUploadDetails.getReturnObj().getReturnName());
			}

			if (returnsUploadDetails.getReturnObj().getErrorCodeBeauJsonObj() != null) {
				returnDto.setErrorCodeBeauJson(returnsUploadDetails.getReturnObj().getErrorCodeBeauJsonObj().getBeauJson());
			}

			List<ReturnFileFormatMap> returnFileFormatList = null;
			if (!CollectionUtils.isEmpty(returnsUploadDetails.getReturnObj().getReturnFileFormatMapList())) {
				returnFileFormatList = new ArrayList<>();
				for (ReturnFileFormatMap returnFileFormatMap : returnsUploadDetails.getReturnObj().getReturnFileFormatMapList()) {
					ReturnFileFormatMap retFileFormatMap = new ReturnFileFormatMap();
					retFileFormatMap.setFileFormat(returnFileFormatMap.getFileFormat());
					retFileFormatMap.setJsonToReadFile(returnFileFormatMap.getJsonToReadFile());
					retFileFormatMap.setFormulaFileName(returnFileFormatMap.getFormulaFileName());
					returnFileFormatList.add(retFileFormatMap);
				}
				returnDto.setReturnFileFormatMapList(returnFileFormatList);
			}
			responseRetUploadDetBean.setReturnObj(returnDto);

			UploadChannel uploadChDto = new UploadChannel();
			uploadChDto.setUploadChannelId(returnsUploadDetails.getFileDetailsBean() != null ? returnsUploadDetails.getFileDetailsBean().getUploadChannelIdFk().getUploadChannelId() : -1L);
			uploadChDto.setUploadChannelDesc(returnsUploadDetails.getFileDetailsBean() != null ? ObjectCache.getLabelKeyValue("en", returnsUploadDetails.getFileDetailsBean().getUploadChannelIdFk().getUploadChannelDesc()) : "NA");
			responseRetUploadDetBean.setUploadChannel(uploadChDto);

			EntityBean entityDto = new EntityBean();
			entityDto.setEntityId(returnsUploadDetails.getEntity().getEntityId());
			entityDto.setEntityName(returnsUploadDetails.getEntity().getEntityName());
			entityDto.setEntityCode(returnsUploadDetails.getEntity().getEntityCode());
			entityDto.setIfscCode(returnsUploadDetails.getEntity().getIfscCode());

			Category categoryDto = new Category();
			categoryDto.setCategoryCode(returnsUploadDetails.getEntity().getCategory().getCategoryCode());
			categoryDto.setCategoryName(returnsUploadDetails.getEntity().getCategory().getCategoryName());
			entityDto.setCategory(categoryDto);

			SubCategory subCatDto = new SubCategory();
			subCatDto.setSubCategoryCode(returnsUploadDetails.getEntity().getSubCategory().getSubCategoryCode());
			subCatDto.setSubCategoryName(returnsUploadDetails.getEntity().getSubCategory().getSubCategoryName());
			entityDto.setSubCategory(subCatDto);

			responseRetUploadDetBean.setEntity(entityDto);

			UserMaster userDto = new UserMaster();
			userDto.setUserId(returnsUploadDetails.getUploadedBy().getUserId());
			userDto.setUserName(returnsUploadDetails.getUploadedBy().getUserName());
			responseRetUploadDetBean.setUploadedBy(userDto);

			if (returnsUploadDetails.getUnlockingReqId() != null) {
				UnlockingRequest unlockRequestDto = new UnlockingRequest();
				unlockRequestDto.setUnlockingReqId(returnsUploadDetails.getUnlockingReqId().getUnlockingReqId());
				unlockRequestDto.setUnlockStatus(returnsUploadDetails.getUnlockingReqId().getUnlockStatus());
				responseRetUploadDetBean.setUnlockingReqId(unlockRequestDto);
			}

			if (returnsUploadDetails.getRevisionRequestId() != null) {
				RevisionRequest revisionRequestDto = new RevisionRequest();
				revisionRequestDto.setRevisionRequestId(returnsUploadDetails.getRevisionRequestId().getRevisionRequestId());
				revisionRequestDto.setRevisionStatus(returnsUploadDetails.getRevisionRequestId().getReasonForRequest());
				responseRetUploadDetBean.setRevisionRequestId(revisionRequestDto);
			}

			responseRetUploadDetBean.setInstanceFile(returnsUploadDetails.getInstanceFile());

			if (returnsUploadDetails.getTaxonomyId() != null) {
				ReturnTemplate taxonomy = new ReturnTemplate();
				taxonomy.setReturnTemplateId(returnsUploadDetails.getTaxonomyId().getReturnTemplateId());
				taxonomy.setVersionDesc(returnsUploadDetails.getTaxonomyId().getVersionDesc());
				taxonomy.setVersionNumber(returnsUploadDetails.getTaxonomyId().getVersionNumber());
				taxonomy.setXsdFileName(returnsUploadDetails.getTaxonomyId().getXsdFileName());
				taxonomy.setTaxonomyName(returnsUploadDetails.getTaxonomyId().getTaxonomyName());
				taxonomy.setFormulaFileName(returnsUploadDetails.getTaxonomyId().getFormulaFileName());
				taxonomy.setJsonDataFileName(returnsUploadDetails.getTaxonomyId().getJsonDataFileName());
				taxonomy.setElrLabelJson(returnsUploadDetails.getTaxonomyId().getElrLabelJson());
				responseRetUploadDetBean.setTaxonomyId(taxonomy);
			}

			if (returnsUploadDetails.getUploadUsrRole() != null) {
				UserRole userRole = new UserRole();
				userRole.setUserRoleId(returnsUploadDetails.getUploadUsrRole().getRoleIdKey());
				userRole.setRoleDesc(returnsUploadDetails.getUploadUsrRole().getRoleDesc());
				userRole.setUserRoleId(returnsUploadDetails.getUploadUsrRole().getUserRoleId());
				responseRetUploadDetBean.setUploadUsrRole(userRole);
			}

			responseRetUploadDetBean.setFileType(returnsUploadDetails.getFileType());

			WorkFlowMasterBean workflow = new WorkFlowMasterBean();
			workflow.setWorkflowId(returnsUploadDetails.getWorkFlowMaster().getWorkflowId());
			workflow.setWorkFlowJson(returnsUploadDetails.getWorkFlowMaster().getWorkFlowJson());

			responseRetUploadDetBean.setWorkFlowMaster(workflow);

			ReturnPropertyValue returnPropertyValue = new ReturnPropertyValue();
			returnPropertyValue.setReturnProprtyValId(returnsUploadDetails.getReturnPropertyValue() != null ? returnsUploadDetails.getReturnPropertyValue().getReturnProprtyValId() : -1);
			//returnPropertyValue.setReturnProValue(returnsUploadDetails.getReturnPropertyValue() != null ? returnsUploadDetails.getReturnPropertyValue().getReturnProValue() : "NA");
			returnPropertyValue.setReturnProValue(returnsUploadDetails.getReturnPropertyValue() != null ? ObjectCache.getLabelKeyValue("en", returnsUploadDetails.getReturnPropertyValue().getReturnProValue()) : "NA");
			responseRetUploadDetBean.setReturnPropertyValue(returnPropertyValue);

			FilingStatus filingStatus = new FilingStatus();
			filingStatus.setFilingStatusId(returnsUploadDetails.getFilingStatus().getFilingStatusId());
			filingStatus.setStatus(ObjectCache.getLabelKeyValue("en", returnsUploadDetails.getFilingStatus().getStatus()));
			responseRetUploadDetBean.setFillingStatusId(returnsUploadDetails.getFilingStatus().getFilingStatusId());
			responseRetUploadDetBean.setFilingStatus(filingStatus);

			FileDetails fileDetailsBean = new FileDetails();
			fileDetailsBean.setId(returnsUploadDetails.getFileDetailsBean() != null ? returnsUploadDetails.getFileDetailsBean().getId() : 0);
			responseRetUploadDetBean.setFileDetailsBean(fileDetailsBean);

			Map<String, String> filingDetailObj = prepareFilingDetails(responseRetUploadDetBean);
			responseRetUploadDetBean.setFilingDetailObj(filingDetailObj);

			responseRetUploadDetBeanList.add(responseRetUploadDetBean);
		}

		return responseRetUploadDetBeanList;
	}

	public static enum FilingDetailsEnum {
		filingNumber, filingStatus, filingStatusId, returnCode, returnName, returnId, entityCode, entityId, entityName, reportingStartDate, uploadedDate, reportingEndDate, auditFlag, provisionalFlag, uploadChannel, frequncyId, frequncyCode, frequencyName, taxonomyVersion, nillable, nillableComments, fileType, reportingCurrency, bankworkingCode, unitRef, bankTypeName, bankTypeCode, categoryName, categoryCode, subCategoryName, subCategoryCode;
	}

	public Map<String, String> prepareFilingDetails(ReturnsUploadDetails returnsUploadDetails) {

		String endDate = null;
		String startDate = null;

		startDate = "";
		if (returnsUploadDetails.getStartDateInLong() != null) {
			startDate = DateManip.convertDateToString(new Date(returnsUploadDetails.getStartDateInLong()), DateConstants.YYYY_MM_DD.getDateConstants());
		}
		endDate = "";
		if (returnsUploadDetails.getEndDateInLong() != null) {
			endDate = DateManip.convertDateToString(new Date(returnsUploadDetails.getEndDateInLong()), DateConstants.YYYY_MM_DD.getDateConstants());
		}
		String uploadedDate = DateManip.convertDateToString(new Date(returnsUploadDetails.getUploadedOnInLong()), DateConstants.YYYY_MM_DD.getDateConstants() + " " + DateConstants.HH_MM_SS.getDateConstants());
		Map<String, String> filingDetails = new HashMap<>();
		filingDetails.put(FilingDetailsEnum.filingNumber.toString(), returnsUploadDetails.getUploadId().toString());
		filingDetails.put(FilingDetailsEnum.filingStatusId.toString(), "" + returnsUploadDetails.getFilingStatus().getFilingStatusId());
		filingDetails.put(FilingDetailsEnum.returnCode.toString(), returnsUploadDetails.getReturnObj().getReturnCode()); //R023, PDR2 monthly //R022, PDR4, MIS, OSS4, ROR quarterly //RDB weekly
		filingDetails.put(FilingDetailsEnum.returnId.toString(), returnsUploadDetails.getReturnObj().getReturnId().toString());
		filingDetails.put(FilingDetailsEnum.entityCode.toString(), returnsUploadDetails.getEntity().getEntityCode());
		filingDetails.put(FilingDetailsEnum.entityId.toString(), returnsUploadDetails.getEntity().getEntityId().toString());
		filingDetails.put(FilingDetailsEnum.reportingStartDate.toString(), startDate);//01012016 01032016 01062018 01012019 01072016 19012019
		filingDetails.put(FilingDetailsEnum.reportingEndDate.toString(), endDate);//31032016	31032016 30062018 31032019 30092016 25012019
		filingDetails.put(FilingDetailsEnum.frequncyId.toString(), returnsUploadDetails.getFrequency().getFrequencyId().toString());
		filingDetails.put(FilingDetailsEnum.frequncyCode.toString(), returnsUploadDetails.getFrequency().getFrequencyCode());
		filingDetails.put(FilingDetailsEnum.frequencyName.toString(), returnsUploadDetails.getFrequency().getFrequencyName());
		filingDetails.put(FilingDetailsEnum.taxonomyVersion.toString(), "1.0.0");
		filingDetails.put(FilingDetailsEnum.auditFlag.toString(), returnsUploadDetails.getReturnPropertyValue().getReturnProprtyValId() != -1 ? returnsUploadDetails.getReturnPropertyValue().getReturnProprtyValId().toString() : "0");
		filingDetails.put(FilingDetailsEnum.uploadChannel.toString(), returnsUploadDetails.getUploadChannel().getUploadChannelDesc());
		filingDetails.put(FilingDetailsEnum.nillable.toString(), returnsUploadDetails.isNillable() ? YES : NO);
		filingDetails.put(FilingDetailsEnum.nillableComments.toString(), returnsUploadDetails.getNillableComments() == null ? "" : returnsUploadDetails.getNillableComments());
		filingDetails.put(FilingDetailsEnum.returnName.toString(), returnsUploadDetails.getReturnObj().getReturnName());
		filingDetails.put(FilingDetailsEnum.entityName.toString(), returnsUploadDetails.getEntity().getEntityName());
		filingDetails.put(FilingDetailsEnum.unitRef.toString(), ACTUALS);
		filingDetails.put(FilingDetailsEnum.uploadedDate.toString(), uploadedDate);
		filingDetails.put(FilingDetailsEnum.fileType.toString(), returnsUploadDetails.getFileType() != null ? returnsUploadDetails.getFileType().toUpperCase() : "");
		filingDetails.put(FilingDetailsEnum.bankTypeName.toString(), returnsUploadDetails.getEntity().getBankTypeIdFk() == null ? "" : returnsUploadDetails.getEntity().getBankTypeIdFk().getBankTypeName());
		filingDetails.put(FilingDetailsEnum.bankTypeCode.toString(), returnsUploadDetails.getEntity().getBankTypeIdFk() == null ? "" : returnsUploadDetails.getEntity().getBankTypeIdFk().getBankTypeCode());

		filingDetails.put(FilingDetailsEnum.categoryName.toString(), returnsUploadDetails.getEntity().getCategory() == null ? "" : returnsUploadDetails.getEntity().getCategory().getCategoryName());
		filingDetails.put(FilingDetailsEnum.categoryCode.toString(), returnsUploadDetails.getEntity().getCategory() == null ? "" : returnsUploadDetails.getEntity().getCategory().getCategoryCode());

		filingDetails.put(FilingDetailsEnum.subCategoryName.toString(), returnsUploadDetails.getEntity().getSubCategory() == null ? "" : returnsUploadDetails.getEntity().getSubCategory().getSubCategoryName());
		filingDetails.put(FilingDetailsEnum.subCategoryCode.toString(), returnsUploadDetails.getEntity().getSubCategory() == null ? "" : returnsUploadDetails.getEntity().getSubCategory().getSubCategoryCode());

		return filingDetails;
	}

	private List<ReturnsUploadDetails> prepareRetSubmittedResponse(List<ReturnsUploadDetails> returnUploadDetailsList, String langCode) {
		List<ReturnsUploadDetails> responseRetUploadDetBeanList = new ArrayList<>();

		for (ReturnsUploadDetails returnsUploadDetails : returnUploadDetailsList) {
			ReturnsUploadDetails responseRetUploadDetBean = new ReturnsUploadDetails();
			responseRetUploadDetBean.setUploadedDate(returnsUploadDetails.getUploadedDate());
			responseRetUploadDetBean.setUploadId(returnsUploadDetails.getUploadId());
			responseRetUploadDetBean.setStartDate(returnsUploadDetails.getStartDate());
			responseRetUploadDetBean.setEndDate(returnsUploadDetails.getEndDate());
			responseRetUploadDetBean.setStartDateInLong(returnsUploadDetails.getStartDate().getTime());
			responseRetUploadDetBean.setEndDateInLong(returnsUploadDetails.getEndDate().getTime());
			responseRetUploadDetBean.setApplicationProcessId(returnsUploadDetails.getFileDetailsBean().getApplicationProcessId());
			responseRetUploadDetBean.setNillable(returnsUploadDetails.isNillable());
			if (!UtilMaster.isEmpty(returnsUploadDetails.getNillableComments())) {
				responseRetUploadDetBean.setNillableComments(returnsUploadDetails.getNillableComments());
			}
			Frequency frequeDto = new Frequency();
			frequeDto.setFrequencyId(returnsUploadDetails.getFrequency().getFrequencyId());
			frequeDto.setFrequencyName(returnsUploadDetails.getFrequency().getFrequencyName());
			responseRetUploadDetBean.setFrequency(frequeDto);
			responseRetUploadDetBean.setInstanceFile(returnsUploadDetails.getInstanceFile());
			Return returnDto = new Return();
			returnDto.setReturnId(returnsUploadDetails.getReturnObj().getReturnId());
			returnDto.setReturnCode(returnsUploadDetails.getReturnObj().getReturnCode());

			ReturnLabel returnLabel = returnsUploadDetails.getReturnObj().getReturnLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(langCode)).findAny().orElse(null);

			if (returnLabel != null) {
				returnDto.setReturnName(returnLabel.getReturnLabel());
			} else {
				returnDto.setReturnName(returnsUploadDetails.getReturnObj().getReturnName());
			}

			List<ReturnFileFormatMap> returnFileFormatList = null;
			if (!CollectionUtils.isEmpty(returnsUploadDetails.getReturnObj().getReturnFileFormatMapList())) {
				returnFileFormatList = new ArrayList<>();
				for (ReturnFileFormatMap returnFileFormatMap : returnsUploadDetails.getReturnObj().getReturnFileFormatMapList()) {
					ReturnFileFormatMap retFileFormatMap = new ReturnFileFormatMap();
					retFileFormatMap.setFileFormat(returnFileFormatMap.getFileFormat());
					retFileFormatMap.setJsonToReadFile(returnFileFormatMap.getJsonToReadFile());
					retFileFormatMap.setFormulaFileName(returnFileFormatMap.getFormulaFileName());
					returnFileFormatList.add(retFileFormatMap);
				}
				returnDto.setReturnFileFormatMapList(returnFileFormatList);
			}
			responseRetUploadDetBean.setReturnObj(returnDto);

			UploadChannel uploadChDto = new UploadChannel();
			uploadChDto.setUploadChannelDesc(returnsUploadDetails.getFileDetailsBean().getUploadChannelIdFk().getUploadChannelDesc());
			uploadChDto.setUploadChannelId(returnsUploadDetails.getFileDetailsBean().getUploadChannelIdFk().getUploadChannelId());
			responseRetUploadDetBean.setUploadChannel(uploadChDto);

			EntityBean entityDto = new EntityBean();

			EntityLabelBean entityLabelBean = returnsUploadDetails.getEntity().getEntityLabelSet().stream().filter(f -> f.getLanguageMaster().getLanguageCode().equalsIgnoreCase(langCode)).findAny().orElse(null);
			entityDto.setEntityId(returnsUploadDetails.getEntity().getEntityId());
			entityDto.setEntityCode(returnsUploadDetails.getEntity().getEntityCode());
			if (entityLabelBean != null) {
				entityDto.setEntityName(entityLabelBean.getEntityNameLabel());
			} else {
				entityDto.setEntityName(returnsUploadDetails.getEntity().getEntityName());
			}

			Category categoryDto = new Category();
			CategoryLabel cagtegoryLabel = returnsUploadDetails.getEntity().getCategory().getCatLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(langCode)).findAny().orElse(null);
			if (cagtegoryLabel != null) {
				categoryDto.setCategoryName(cagtegoryLabel.getCategoryLabel());
			} else {
				categoryDto.setCategoryName(returnsUploadDetails.getEntity().getCategory().getCategoryName());
			}
			categoryDto.setCategoryCode(returnsUploadDetails.getEntity().getCategory().getCategoryCode());
			entityDto.setCategory(categoryDto);

			SubCategory subCatDto = new SubCategory();
			SubCategoryLabel subLabel = returnsUploadDetails.getEntity().getSubCategory().getSubCatLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(langCode)).findAny().orElse(null);
			if (subLabel != null) {
				subCatDto.setSubCategoryName(subLabel.getSubCategoryLabel());
			} else {
				subCatDto.setSubCategoryName(returnsUploadDetails.getEntity().getSubCategory().getSubCategoryName());
			}
			subCatDto.setSubCategoryCode(returnsUploadDetails.getEntity().getSubCategory().getSubCategoryCode());
			entityDto.setSubCategory(subCatDto);

			responseRetUploadDetBean.setEntity(entityDto);

			UserMaster userDto = new UserMaster();
			userDto.setUserId(returnsUploadDetails.getUploadedBy().getUserId());
			userDto.setUserName(returnsUploadDetails.getUploadedBy().getUserName());
			responseRetUploadDetBean.setUploadedBy(userDto);

			if (returnsUploadDetails.getUnlockingReqId() != null) {
				UnlockingRequest unlockRequestDto = new UnlockingRequest();
				unlockRequestDto.setUnlockingReqId(returnsUploadDetails.getUnlockingReqId().getUnlockingReqId());
				unlockRequestDto.setUnlockStatus(returnsUploadDetails.getUnlockingReqId().getUnlockStatus());
				responseRetUploadDetBean.setUnlockingReqId(unlockRequestDto);
			}

			if (returnsUploadDetails.getRevisionRequestId() != null) {
				RevisionRequest revisionRequestDto = new RevisionRequest();
				revisionRequestDto.setRevisionRequestId(returnsUploadDetails.getRevisionRequestId().getRevisionRequestId());
				revisionRequestDto.setRevisionStatus(returnsUploadDetails.getRevisionRequestId().getReasonForRequest());
				responseRetUploadDetBean.setRevisionRequestId(revisionRequestDto);
			}
			if (returnsUploadDetails.getFileDetailsBean().getCreationDate() != null) {
				responseRetUploadDetBean.setUploadedOnInLong(returnsUploadDetails.getFileDetailsBean().getCreationDate().getTime());
			}

			responseRetUploadDetBean.setInstanceFile(returnsUploadDetails.getInstanceFile());

			if (returnsUploadDetails.getTaxonomyId() != null) {
				ReturnTemplate taxonomy = new ReturnTemplate();
				taxonomy.setReturnTemplateId(returnsUploadDetails.getTaxonomyId().getReturnTemplateId());
				taxonomy.setVersionDesc(returnsUploadDetails.getTaxonomyId().getVersionDesc());
				taxonomy.setVersionNumber(returnsUploadDetails.getTaxonomyId().getVersionNumber());
				taxonomy.setXsdFileName(returnsUploadDetails.getTaxonomyId().getXsdFileName());
				responseRetUploadDetBean.setTaxonomyId(taxonomy);
			}

			if (returnsUploadDetails.getUploadUsrRole() != null) {
				UserRole userRole = new UserRole();
				userRole.setUserRoleId(returnsUploadDetails.getUploadUsrRole().getRoleIdKey());
				userRole.setRoleDesc(returnsUploadDetails.getUploadUsrRole().getRoleDesc());
				userRole.setUserRoleId(returnsUploadDetails.getUploadUsrRole().getUserRoleId());
				responseRetUploadDetBean.setUploadUsrRole(userRole);
			}

			if (returnsUploadDetails.getFileType() != null) {
				responseRetUploadDetBean.setFileNameType(returnsUploadDetails.getFileType());
			}
			if (returnsUploadDetails.getFileDetailsBean() != null) {
				responseRetUploadDetBean.setFileName(returnsUploadDetails.getFileDetailsBean().getFileName());
			}
			if (returnsUploadDetails.getFileDetailsBean() != null) {
				responseRetUploadDetBean.setOriginalSupportiveDocName(returnsUploadDetails.getFileDetailsBean().getSupportiveDocName());
			}

			if (!UtilMaster.isEmpty(returnsUploadDetails.getAttachedFile())) {
				responseRetUploadDetBean.setAttachedFile(returnsUploadDetails.getAttachedFile());
			}

			WorkFlowMasterBean workflow = new WorkFlowMasterBean();
			workflow.setWorkflowId(returnsUploadDetails.getWorkFlowMaster().getWorkflowId());
			workflow.setWorkFlowJson(returnsUploadDetails.getWorkFlowMaster().getWorkFlowJson());

			responseRetUploadDetBean.setWorkFlowMaster(workflow);

			if (!Validations.isEmpty(returnsUploadDetails.getAttachedFile())) {
				responseRetUploadDetBean.setAttachedFile(returnsUploadDetails.getAttachedFile());
			}

			if (returnsUploadDetails.getReturnPropertyValue() != null) {
				ReturnPropertyValue returnPropertyValue = new ReturnPropertyValue();
				returnPropertyValue.setReturnProprtyValId(returnsUploadDetails.getReturnPropertyValue().getReturnProprtyValId());
				returnPropertyValue.setReturnProValue(returnsUploadDetails.getReturnPropertyValue().getReturnProValue());
				responseRetUploadDetBean.setReturnPropertyValue(returnPropertyValue);
			}

			responseRetUploadDetBeanList.add(responseRetUploadDetBean);
		}

		return responseRetUploadDetBeanList;
	}

	@PostMapping(value = "/sendMailToEntitiesForPendingFiling")
	public ServiceResponse sendMailToEntitiesForPendingFiling(@RequestHeader("JobProcessingId") String jobProcessId, @RequestBody EntityFilingPendingBean pendingFilingEntities) {
		try {
			LOGGER.info("*In sendMailToEntitiesForPendingFiling" + jobProcessId);
			if (UtilMaster.isEmpty(pendingFilingEntities.getEntityFilingPendingDataList())) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0757.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0757.toString())).build();
			}

			//Adding required data to object like unique number and date in date format
			if (pendingFilingEntities.getEntityFilingPendingDataList() != null) {
				List<EntityFilingPendingData> entityFilingPendingDataList = pendingFilingEntities.getEntityFilingPendingDataList();
				for (int i = 0; i < entityFilingPendingDataList.size(); i++) {
					if (!entityFilingPendingDataList.get(i).getEndDate().isEmpty()) {
						entityFilingPendingDataList.get(i).setDtEndDate(DateManip.convertStringToDate(entityFilingPendingDataList.get(i).getEndDate(), DateConstants.YYYY_MM_DD.getDateConstants()));
						String formattedDate = DateManip.formatDate(entityFilingPendingDataList.get(i).getEndDate(), DateConstants.YYYY_MM_DD.getDateConstants(), "dd/MM/yyyy");
						entityFilingPendingDataList.get(i).setEndDate(formattedDate);
					}
					entityFilingPendingDataList.get(i).setUniqueId(entityFilingPendingDataList.get(i).getDtEndDate().getTime() + new Random().nextInt(1000) + "");
				}
				pendingFilingEntities.setEntityFilingPendingDataList(entityFilingPendingDataList);
			}

			LOGGER.info("*calling getDataRequiredForEmailSending" + jobProcessId);
			pendingFilingEntities = getDataRequiredForEmailSending(pendingFilingEntities, jobProcessId);
			if (pendingFilingEntities == null) {
				LOGGER.info("MIS PENDING EMAIL SENDING FAILED" + jobProcessId);
				return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.E0782.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0782.toString())).build();
			} else {
				LOGGER.info("MIS PENDING EMAIL SENT SUCCESSFULLY" + jobProcessId);
				return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.EMAIL_SENT_SUCCESSFULLY.getConstantVal()).setResponse(pendingFilingEntities).build();
			}
		} catch (Exception e) {
			LOGGER.info("sendMailToEntitiesForPendingFiling:Exception Occured:" + e.getStackTrace() + jobProcessId);
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Transaction ID : " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0782.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0782.toString())).build();
		}
	}

	@PostMapping(value = "/getMISPendingFilingHistoryData")
	public ServiceResponse getMISPendingFilingHistoryData(@RequestHeader("JobProcessingId") String jobProcessId, @RequestBody EntityFilingPendingBean pendingFilingEntities) {
		try {
			LOGGER.info("*Inside getMISPendingFilingHistoryData : " + new Gson().toJson(pendingFilingEntities));
			if (UtilMaster.isEmpty(pendingFilingEntities.getEntityFilingPendingDataList())) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0757.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0757.toString())).build();
			}

			//*************Fetching tbl_mis_pending_mail_sent_hist data
			List<String> entityCodeList = new ArrayList<>();
			entityCodeList.add(pendingFilingEntities.getEntityFilingPendingDataList().get(0).getEntityCode());
			List<String> returnCodeList = new ArrayList<>();
			returnCodeList.add(pendingFilingEntities.getEntityFilingPendingDataList().get(0).getReturnCode());
			List<String> endDateList = new ArrayList<>();
			endDateList.add(pendingFilingEntities.getEntityFilingPendingDataList().get(0).getEndDate());

			Map<String, List<String>> valueMap = new HashMap<>();
			valueMap.put(ColumnConstants.RETURN_ID_LIST.getConstantVal(), returnCodeList);

			valueMap.put(ColumnConstants.ENTITY_ID_LIST.getConstantVal(), entityCodeList);

			valueMap.put(ColumnConstants.END_DATE_LIST.getConstantVal(), endDateList);

			LOGGER.info("*Fetching tbl_mis_pending_mail_sent_hist data:valueMap" + valueMap);
			List<MISPendingMailSentHist> misPendingMailSentHistListFormDB = misPendingMailSentHistService.getDataByColumnValue(valueMap, MethodConstants.GET_MIS_PENDING_EMAIL_HIST_DATA.getConstantVal());
			//************done Fetching tbl_mis_pending_mail_sent_hist data

			if (misPendingMailSentHistListFormDB == null || misPendingMailSentHistListFormDB.isEmpty()) {

				return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.E0757.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0757.toString())).build();
			} else {
				List<String> mailSentHistoryIdFkList = new Gson().fromJson(misPendingMailSentHistListFormDB.get(0).getMailSentHistIdFKListJson(), List.class);

				Long[] mailSentHistIdsInLong = new Long[mailSentHistoryIdFkList.size()];
				for (int i = 0; i < mailSentHistoryIdFkList.size(); i++)
					mailSentHistIdsInLong[i] = Long.parseLong(mailSentHistoryIdFkList.get(i));

				List<EmailSentHistory> EmailSentHistoryList = emailSentHistoryService.getDataByIds(mailSentHistIdsInLong);

				if (EmailSentHistoryList != null) {
					List<EmailSentHistory> emailSentHistoryListNew = new ArrayList<>();
					for (EmailSentHistory emailSentHistory : EmailSentHistoryList) {
						EmailSentHistory emailSentHistoryNew = new EmailSentHistory();
						UserMaster userMaster = new UserMaster();
						userMaster.setUserId(emailSentHistory.getUserMasterFk().getUserId());
						userMaster.setUserName(emailSentHistory.getUserMasterFk().getUserName());
						emailSentHistoryNew.setUserMasterFk(userMaster);

						UserRole userRole = new UserRole();
						userRole.setUserRoleId(emailSentHistory.getUserRoleFk().getUserRoleId());
						emailSentHistoryNew.setUserRoleFk(userRole);

						emailSentHistoryNew.setSentDtTime(emailSentHistory.getSentDtTime());
						emailSentHistoryListNew.add(emailSentHistoryNew);
					}
					LOGGER.info("getMISPendingFilingHistoryData:" + GeneralConstants.RECORD_FETCHED_SUCCESSFULLY.getConstantVal() + jobProcessId);
					return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.RECORD_FETCHED_SUCCESSFULLY.getConstantVal()).setResponse(emailSentHistoryListNew).build();
				} else {
					LOGGER.info("getMISPendingFilingHistoryData:Records not fetched for some reason" + jobProcessId);
					return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.E0757.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0757.toString())).build();
				}

			}
		} catch (Exception e) {
			LOGGER.info("getMISPendingFilingHistoryData:Exception Occured:" + e.getStackTrace() + jobProcessId);
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Transaction ID : " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0804.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0804.toString())).build();
		}
	}

	private EntityFilingPendingBean getDataRequiredForEmailSending(EntityFilingPendingBean pendingFilingEntities, String jobProcessId) {
		//*******Fetching all data including ToEmailList and CCEmailList
		//EntityFilingPendingBean entityFilingPendingBean= misPendingEmailData.getRequiredDataForEmailSending(pendingFilingEntities,jobProcessId);
		EntityFilingPendingBean entityFilingPendingBean = misPendingEmailData.getToAndCCEmailList(pendingFilingEntities);

		//*******End Fetching all data including ToEmailList and CCEmailList

		//Sending mail
		List<EmailSentHistory> emailSentHistoryList = (List<EmailSentHistory>) prepareSendMail(entityFilingPendingBean).getResponse();
		if (emailSentHistoryList == null) {
			return null;
		}
		List<EntityFilingPendingData> entityFilingPendingDataList = pendingFilingEntities.getEntityFilingPendingDataList();

		//Fetch tbl_mis_pending_mail_sent_hist data
		List<String> entityCodeList = entityFilingPendingDataList.stream().map(EntityFilingPendingData::getEntityCode).collect(Collectors.toList());
		List<String> returnCodeList = entityFilingPendingDataList.stream().map(EntityFilingPendingData::getReturnCode).collect(Collectors.toList());
		List<String> endDateList = entityFilingPendingDataList.stream().map(EntityFilingPendingData::getEndDate).collect(Collectors.toList());
		List<String> endDateListNew = new ArrayList<>();
		//***************
		for (String dateStr : endDateList) {
			String formattedDate;
			try {
				formattedDate = DateManip.formatDate(dateStr, "dd/MM/yyyy", DateConstants.YYYY_MM_DD.getDateConstants());
				endDateListNew.add(formattedDate);
			} catch (ParseException e) {
				LOGGER.info("getMISPendingFilingHistoryData:ParseException:" + e.getStackTrace() + jobProcessId);
				LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "while converting date format : Transaction ID : " + jobProcessId, e);
				continue;
			}
		}
		//****************

		Map<String, List<String>> valueMap = new HashMap<>();
		valueMap.put(ColumnConstants.RETURN_ID_LIST.getConstantVal(), returnCodeList);
		valueMap.put(ColumnConstants.ENTITY_ID_LIST.getConstantVal(), entityCodeList);
		valueMap.put(ColumnConstants.END_DATE_LIST.getConstantVal(), endDateListNew);

		//Fetching data from database if already exists
		List<MISPendingMailSentHist> misPendingMailSentHistListFormDB = misPendingMailSentHistService.getDataByColumnValue(valueMap, MethodConstants.GET_MIS_PENDING_EMAIL_HIST_DATA.getConstantVal());

		List<MISPendingMailSentHist> misPendingMailSentHistListForINSERTUpdate = new ArrayList<>();
		if (emailSentHistoryList != null && !emailSentHistoryList.isEmpty()) {

			for (EmailSentHistory emailSentHistory : emailSentHistoryList) { // This is Response from mail sent history table
				try {
					//Checking sent email record using Unique Id
					EntityFilingPendingData matchingObject = entityFilingPendingDataList.stream().filter(p -> p.getUniqueId().equals(emailSentHistory.getUniqueId())).findAny().orElse(null);

					//Checking if recort for ENTITY Id, Return Id , Reference End Date is already present into the db or not, 
					//if Present then update the json or else Insert the new row
					MISPendingMailSentHist matchRecordWithDB = misPendingMailSentHistListFormDB.stream().filter(p -> p.getEntityObj().getEntityId().equals(matchingObject.getEntityId()) && p.getReturnObj().getReturnId().equals(matchingObject.getReturnId()) && p.getReportingEndDate().compareTo(matchingObject.getDtEndDate()) == 0).findAny().orElse(null);

					if (matchRecordWithDB != null) {
						//This is for update
						if (matchingObject != null) {
							MISPendingMailSentHist misPendingMailSentHistUpdate = new MISPendingMailSentHist();
							EntityBean entityBean = new EntityBean();
							entityBean.setEntityId(matchingObject.getEntityId());
							misPendingMailSentHistUpdate.setEntityObj(entityBean);

							Return returnObj = new Return();
							returnObj.setReturnId(matchingObject.getReturnId());
							misPendingMailSentHistUpdate.setReturnObj(returnObj);

							String jsonString = matchRecordWithDB.getMailSentHistIdFKListJson();
							@SuppressWarnings("unchecked")
							List<String> listOfEmailSentHistId = new Gson().fromJson(jsonString, List.class);
							listOfEmailSentHistId.add(emailSentHistory.getMailSentHistId().toString());

							misPendingMailSentHistUpdate.setReportingEndDate(matchingObject.getDtEndDate());

							misPendingMailSentHistUpdate.setMisPendingMailSentHistId(matchRecordWithDB.getMisPendingMailSentHistId());

							misPendingMailSentHistUpdate.setMailSentHistIdFKListJson(new Gson().toJson(listOfEmailSentHistId));
							misPendingMailSentHistListForINSERTUpdate.add(misPendingMailSentHistUpdate);
						}
					} else {
						//This is for insert
						if (matchingObject != null) {
							MISPendingMailSentHist misPendingMailSentHistInsert = new MISPendingMailSentHist();
							EntityBean entityBean = new EntityBean();
							entityBean.setEntityId(matchingObject.getEntityId());
							misPendingMailSentHistInsert.setEntityObj(entityBean);

							Return returnObj = new Return();
							returnObj.setReturnId(matchingObject.getReturnId());
							misPendingMailSentHistInsert.setReturnObj(returnObj);

							misPendingMailSentHistInsert.setReportingEndDate(matchingObject.getDtEndDate());

							List<String> listOfEmailSentHistId = new ArrayList<>();
							listOfEmailSentHistId.add(emailSentHistory.getMailSentHistId().toString());
							misPendingMailSentHistInsert.setMailSentHistIdFKListJson(new Gson().toJson(listOfEmailSentHistId));
							misPendingMailSentHistListForINSERTUpdate.add(misPendingMailSentHistInsert);
						}
					}
				} catch (Exception e) {
					LOGGER.info("getMISPendingFilingHistoryData:ParseException:" + e.getStackTrace() + jobProcessId);
					LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "getDataRequiredForEmailSending : Transaction ID : " + jobProcessId, e);
				}
			}
			misPendingMailSentHistService.saveAll(misPendingMailSentHistListForINSERTUpdate);
		}
		return new EntityFilingPendingBean();

	}

	private ServiceResponse prepareSendMail(EntityFilingPendingBean entityFilingPendingBean) {
		LOGGER.info("*Inside prepareSendMail Returns Upload Details");
		ServiceResponse serviceResponse = null;
		String languageCode = "en";
		String processingId = UUID.randomUUID().toString();
		List<MailServiceBean> mailServiceBeanList = new ArrayList<>();
		for (com.iris.dto.EntityFilingPendingData entityFilingPendingData : entityFilingPendingBean.getEntityFilingPendingDataList()) {
			MailServiceBean mailServiceBean = new MailServiceBean();
			List<DynamicContent> dynamicContents = new ArrayList<>();
			DynamicContent dynamicContent = new DynamicContent();

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.entity.entityName"));
			dynamicContent.setKey(EmailPlaceholderConstants.ENTITY_NAME_KEY.getConstantVal());
			dynamicContent.setValue(entityFilingPendingData.getEntityName());
			dynamicContents.add(dynamicContent);

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.return.name"));
			dynamicContent.setKey(EmailPlaceholderConstants.RETURN_NAME_KEY.getConstantVal());
			dynamicContent.setValue(entityFilingPendingData.getReturnName());
			dynamicContents.add(dynamicContent);

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.reporting.enddate"));
			dynamicContent.setKey(EmailPlaceholderConstants.REF_END_DATE_KEY.getConstantVal());
			dynamicContent.setValue(entityFilingPendingData.getEndDate());
			dynamicContents.add(dynamicContent);

			if (entityFilingPendingData.getReturnProperty() != null && !entityFilingPendingData.getReturnProperty().isEmpty()) {
				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(languageCode, "field.return.returnProperty"));
				dynamicContent.setValue(ObjectCache.getLabelKeyValue(languageCode, "field.propertyVal.AuditedFinal"));
				dynamicContents.add(dynamicContent);
			}

			mailServiceBean.setDynamicContentsList(dynamicContents);
			mailServiceBean.setUserId(entityFilingPendingBean.getUserId());
			mailServiceBean.setRoleId(entityFilingPendingBean.getRoleId());

			mailServiceBean.setEntityCode(entityFilingPendingData.getEntityCode());
			mailServiceBean.setReturnCode(entityFilingPendingData.getReturnCode());
			mailServiceBean.setAlertId(102l);
			mailServiceBean.setMenuId(92l);
			mailServiceBean.setUniqueId(entityFilingPendingData.getUniqueId());

			//setting mail sending "To" List
			mailServiceBean.getEmailMap();
			List<String> toEmailIdList = entityFilingPendingData.getToEmailIdList();
			List<String> ccEmailIdList = entityFilingPendingData.getCcEmailIdList();

			//**************************This is when To list is empty then mail will not be sent to only CC list
			if (toEmailIdList == null || toEmailIdList.isEmpty()) {
				continue;
			} //*****************************

			Map<Integer, List<String>> mailListMap = new HashMap<>();
			mailListMap.put(1, toEmailIdList);

			mailListMap.put(2, ccEmailIdList);
			mailServiceBean.setEmailMap(mailListMap);

			mailServiceBeanList.add(mailServiceBean);

		}
		if (mailServiceBeanList.isEmpty()) {
			return null;
		}
		LOGGER.info("*Calling prepareSendMail Generic API for MIS Pending");
		serviceResponse = prepareSendMailController.prepareSendEmail(processingId, mailServiceBeanList);
		if (serviceResponse.isStatus()) {
			LOGGER.info(GeneralConstants.EMAIL_SENT_SUCCESSFULLY.getConstantVal());
		}

		return serviceResponse;
	}
}