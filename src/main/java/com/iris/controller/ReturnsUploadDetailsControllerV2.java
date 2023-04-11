package com.iris.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.service.spi.ServiceException;
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
import com.iris.dto.RetUploadDetBean;
import com.iris.dto.ReturnEntityMapDto;
import com.iris.dto.ReturnEntityOutputDto;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.model.Category;
import com.iris.model.CategoryLabel;
import com.iris.model.EntityBean;
import com.iris.model.EntityLabelBean;
import com.iris.model.Frequency;
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
import com.iris.util.UtilMaster;
import com.iris.util.Validations;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.FrequencyEnum;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author pmhatre
 */
@RestController
@RequestMapping("/service/returnsUploadDetailsController/V2")
public class ReturnsUploadDetailsControllerV2 {

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

	@Autowired
	EntityMasterControllerV2 entityMasterControllerV2;

	static final Logger LOGGER = LogManager.getLogger(ReturnsUploadDetailsControllerV2.class);

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

				ReturnEntityMapDto returnEntityMapDto = new ReturnEntityMapDto();
				returnEntityMapDto.setIsActive(true);
				returnEntityMapDto.setUserId(returnsUploadDetailsObj.getLogedInUser().getUserId());
				returnEntityMapDto.setLangCode(returnsUploadDetailsObj.getLangCode());
				if (returnsUploadDetailsObj.getLogedInUser().getRoleId() != 0) {
					returnEntityMapDto.setRoleId(returnsUploadDetailsObj.getLogedInUser().getRoleId());
				}

				LOGGER.info("User:" + returnsUploadDetailsObj.getLogedInUser().getUserName() + "******MISV2**uploadDetailsEntityWise: starting to fetch Return list: Start Time: " + DateManip.getCurrentDateTime(DateConstants.DD_MM_YYYY.getDateConstants(), "HH:mm:ss:SSS"), jobProcessId);
				ServiceResponse serviceResponse = entityMasterControllerV2.getEntityReturnChannelMapp(jobProcessId, returnEntityMapDto);
				LOGGER.info("User:" + returnsUploadDetailsObj.getLogedInUser().getUserName() + "******MISV2**uploadDetailsEntityWise: starting to fetch Return list: End Time: " + DateManip.getCurrentDateTime(DateConstants.DD_MM_YYYY.getDateConstants(), "HH:mm:ss:SSS"), jobProcessId);
				List<ReturnEntityOutputDto> returnEntityOutputDtoList = (List<ReturnEntityOutputDto>) serviceResponse.getResponse();
				for (ReturnEntityOutputDto returnGroupMappingDto : returnEntityOutputDtoList) {
					if (misReportType.equals(GeneralConstants.MIS_REPORT_PENDING.getConstantVal())) {
						if (!"D".equals(returnGroupMappingDto.getReturnEntityFreqDto().getFreqCode()) && !"AD".equals(returnGroupMappingDto.getReturnEntityFreqDto().getFreqCode()) && !"G".equals(returnGroupMappingDto.getReturnEntityFreqDto().getFreqCode())) {
							mappedReturn.add(returnGroupMappingDto.getReturnId());
						}
					} else {
						mappedReturn.add(returnGroupMappingDto.getReturnId());
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
			//			if (returnsUploadDetailsObj.getReturnIdList() == null || returnsUploadDetailsObj.getReturnIdList().isEmpty()) {

			ReturnEntityMapDto returnEntityMapDto = new ReturnEntityMapDto();
			returnEntityMapDto.setIsActive(true);
			returnEntityMapDto.setUserId(returnsUploadDetailsObj.getLogedInUser().getUserId());
			returnEntityMapDto.setLangCode(returnsUploadDetailsObj.getLangCode());
			if (returnsUploadDetailsObj.getLogedInUser().getRoleId() != 0) {
				returnEntityMapDto.setRoleId(returnsUploadDetailsObj.getLogedInUser().getRoleId());
			}

			LOGGER.info("User:" + returnsUploadDetailsObj.getLogedInUser().getUserName() + "******MISV2**uploadDetailsReturnWise: starting to fetch Return list: Start Time: " + DateManip.getCurrentDateTime(DateConstants.DD_MM_YYYY.getDateConstants(), "HH:mm:ss:SSS"), jobProcessId);
			ServiceResponse serviceResponse = entityMasterControllerV2.getEntityReturnChannelMapp(jobProcessId, returnEntityMapDto);
			LOGGER.info("User:" + returnsUploadDetailsObj.getLogedInUser().getUserName() + "******MISV2**uploadDetailsReturnWise: starting to fetch Return list: End Time: " + DateManip.getCurrentDateTime(DateConstants.DD_MM_YYYY.getDateConstants(), "HH:mm:ss:SSS"), jobProcessId);
			List<ReturnEntityOutputDto> returnEntityOutputDtoList = (List<ReturnEntityOutputDto>) serviceResponse.getResponse();
			for (ReturnEntityOutputDto returnGroupMappingDto : returnEntityOutputDtoList) {
				if (misReportType.equals(GeneralConstants.MIS_REPORT_PENDING.getConstantVal())) {
					if (!"D".equals(returnGroupMappingDto.getReturnEntityFreqDto().getFreqCode()) && !"AD".equals(returnGroupMappingDto.getReturnEntityFreqDto().getFreqCode()) && !"G".equals(returnGroupMappingDto.getReturnEntityFreqDto().getFreqCode())) {
						mappedReturn.add(returnGroupMappingDto.getReturnId());
					}
				} else {
					mappedReturn.add(returnGroupMappingDto.getReturnId());
				}
			}

			List<String> newReturnIdList = new ArrayList<>(mappedReturn.size());
			for (Long myInt : mappedReturn) {
				newReturnIdList.add(String.valueOf(myInt));
			}
			//returnsUploadDetailsObj.setReturnIdList(newReturnCodeList);
			//----------------------------

			List<Return> returnList = (List<Return>) returnGroupController.getReturnListWithFrequencyByReturnCodes(returnsUploadDetailsObj.getReturnIdList(), returnsUploadDetailsObj.getLangCode());
			List<Long> selReturnIdList = returnList.stream().map(Return::getReturnId).collect(Collectors.toList());

			/*
			 * From front end there is all return list which is applicable to logged in users deparment,
			 * So taking intersection of returns which are assigned to entites of logged in user
			 */
			if (returnsUploadDetailsObj.getReturnIdList() == null || returnsUploadDetailsObj.getReturnIdList().isEmpty()) {
				returnsUploadDetailsObj.setReturnIdList(newReturnIdList);
			} else if (returnsUploadDetailsObj.getReturnIdList() != null || !returnsUploadDetailsObj.getReturnIdList().isEmpty()) {
				//selectedReturnCodeList.stream().filter(newReturnCodeList::contains).collect(Collectors.toList());
				Set<Long> result = selReturnIdList.stream().distinct().filter(mappedReturn::contains).collect(Collectors.toSet());
				List<String> returnIdList = result.stream().map(String::valueOf).collect(Collectors.toCollection(ArrayList::new));
				returnIdList.addAll(returnIdList);
				returnsUploadDetailsObj.setReturnIdList(returnIdList);
			}
			//			}

			Date startDate = DateManip.convertStringToDate(returnsUploadDetailsObj.getStartDate(), returnsUploadDetailsObj.getSessionDateFormat());
			String inputDatStr = DateManip.convertDateToString(startDate, DateConstants.DD_MM_YYYY.getDateConstants());
			returnsUploadDetailsObj.setStartDate(inputDatStr);
			Date endDate = DateManip.convertStringToDate(returnsUploadDetailsObj.getEndDate(), returnsUploadDetailsObj.getSessionDateFormat());
			String inputEnDatStr = DateManip.convertDateToString(endDate, DateConstants.DD_MM_YYYY.getDateConstants());
			returnsUploadDetailsObj.setEndDate(inputEnDatStr);

			if (misReportType.equals(GeneralConstants.MIS_REPORT_PENDING.getConstantVal())) {
				List<Long> retIdListInLong = returnsUploadDetailsObj.getReturnIdList().stream().map(Long::parseLong).collect(Collectors.toList());

				Map<String, Long> returnCodeAndFrequency = new HashMap<>();

				returnList = (List<Return>) returnGroupController.getReturnListWithFrequency(retIdListInLong, returnsUploadDetailsObj.getLangCode());
				for (Return returnVal : returnList) {
					returnCodeAndFrequency.put(returnVal.getReturnId() + "", returnVal.getFrequency().getFrequencyId());
				}

				Map<Long, List<String>> returnIdEndDatesList = new HashMap<>();
				List<String> allEndDateList = new ArrayList<>();

				for (Long returnId : retIdListInLong) {
					String frequencyId = FrequencyEnum.getCodeById(returnCodeAndFrequency.get(returnId + ""));
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

	public static void main(String args[]) {
		List<Long> list = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
		List<Long> otherList = Arrays.asList(6L, 7L, 8L);

		Set<Long> result = list.stream().distinct().filter(otherList::contains).collect(Collectors.toSet());
		System.out.println("result:" + result);
	}
}