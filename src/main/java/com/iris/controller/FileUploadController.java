package com.iris.controller;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iris.bsr1bsr7.FileReader;
import com.iris.bsr1bsr7.bean.Bsr1HeaderInputModel;
import com.iris.bsr1bsr7.bean.Formula;
import com.iris.bsr1bsr7.bean.HeaderBSR1;
import com.iris.bsr1bsr7.bean.HeaderBSR7;
import com.iris.bsr1bsr7.bean.HeaderDetail;
import com.iris.caching.ObjectCache;
import com.iris.csvProcessing.bean.CSVDataRecord;
import com.iris.csvProcessing.bean.CSVFileExtractorResult;
import com.iris.csvProcessing.bean.CSVInputModel;
import com.iris.csvProcessing.exceptions.CSVProcessorException;
import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.FilingCalendarDto;
import com.iris.dto.FillingEndDatesBean;
import com.iris.dto.RetUploadDetBean;
import com.iris.dto.ReturnMetaDataBean;
import com.iris.dto.ServiceResponse;
import com.iris.excelProcessing.bean.FillingInformation;
import com.iris.exception.ServiceException;
import com.iris.fileDataExtract.ExtractFileData;
import com.iris.json.bean.DocumentInfo;
import com.iris.model.Currency;
import com.iris.model.CurrencyConversion;
import com.iris.model.EntityBean;
import com.iris.model.FileDetails;
import com.iris.model.FilingStatus;
import com.iris.model.Frequency;
import com.iris.model.Return;
import com.iris.model.ReturnFileFormatMap;
import com.iris.model.ReturnTemplate;
import com.iris.model.ReturnsUploadDetails;
import com.iris.model.RevisionRequest;
import com.iris.model.UnlockingRequest;
import com.iris.model.UserMaster;
import com.iris.model.UserRole;
import com.iris.model.WorkFlowMasterBean;
import com.iris.processor.CSVBusinessValidationProcessor;
import com.iris.repository.CurrencyConversionRepository;
import com.iris.repository.CurrencyRepository;
import com.iris.repository.FileDetailsRepo;
import com.iris.service.FilingCalendarModifiedService;
import com.iris.service.GenericService;
import com.iris.service.impl.ReturnUploadDetailsService;
import com.iris.util.DateAndTimeArithmetic;
import com.iris.util.DateUtilsParser;
import com.iris.util.FileManager;
import com.iris.util.JsonUtility;
import com.iris.util.ResourceUtil;
import com.iris.util.Validations;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.FrequencyEnum;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MetaDataCheckConstants;
import com.iris.util.constant.MethodConstants;
import com.iris.util.constant.ReturnPropertyVal;
import com.iris.util.constant.RevisionRequestConstants;
import com.iris.util.constant.ServiceConstants;
import com.iris.util.constant.UploadFilingConstants;
import com.iris.xbrlInstanceProcessing.bean.XMLTagBean;

/**
 * @author sajadhav
 */
@RestController
@RequestMapping(value = "/services")
public class FileUploadController {

	static final Logger LOGGER = LogManager.getLogger(FileUploadController.class);

	@Autowired
	private FilingCalendarModifiedService filingCalendarModifiedService;

	@Autowired
	private GenericService<UnlockingRequest, Long> unlockRequestService;

	@Autowired
	private GenericService<RevisionRequest, Long> revisionRequestService;

	@Autowired
	private ReturnUploadDetailsService returnUploadDetailsService;

	@Autowired
	private GenericService<WorkFlowMasterBean, Long> workflowMasterService;

	@Autowired
	private CurrencyRepository currencyRepository;

	@Autowired
	private CurrencyConversionRepository currencyConversionRepository;

	@Autowired
	private FileDetailsRepo fileDetailsRepo;

	@Autowired
	private HolidayController holidayController;

	@Autowired
	private GenericService<Return, Long> returnService;

	@Autowired
	private GenericService<EntityBean, Long> entityService;

	@Autowired
	private GenericService<ReturnTemplate, Long> returnTemplateService;

	@Autowired
	private GenericService<ReturnFileFormatMap, Long> returnFileFormatMapService;

	private String reportingCurrency;

	@Value("${base.date.for.fortnightly}")
	private String baseDateForFortNightly;

	@Value("${isBsr.newImplementation}")
	private Boolean isBsrNewImplementation;

	@Autowired
	private GenericService<RevisionRequest, Long> returnPropertyValueService;

	private static final String RETURN_CODE_INDEX_POS_IN_CSV = "1_2";
	private static final String ENTITY_NAME_INDEX_POS_IN_CSV = "1_3";
	private static final String ENTITY_CODE_INDEX_POS_IN_CSV = "1_4";
	private static final String START_DATE_INDEX_POS_IN_CSV = "1_0";
	private static final String END_DATE_INDEX_POS_IN_CSV = "1_6";
	private static final String FREQUENCY_INDEX_POS_IN_CSV = "1_5";
	private static final String REPORT_STATUS_INDEX_POS_IN_CSV = "1_7";
	private static final String REPORTING_END_DATE_ERROR_IN_CSV = "1_8";
	private static final String DD_SLASH_MM_SLASH_YYYY = "dd/MM/yyyy";
	private static final String BSR_RETURN_DATE_FORMAT = "ddMMyyyy";
	private static final String DDMMYYYY = "ddMMyyyy";
	private static final String CSV_RETURN_DATE_SAPERATOR = "/";
	private static final String REPORTING_PERIOD = "#reportingPeriod";
	private static final String YYYY_DASH_MM_DASH_DD = "yyyy-MM-dd";
	private static final String REPORT_TYPE_POS = "1_10";
	private static final String VERSION_NO_POS = "1_11";
	private static final String SPECIAL_ECONOMIC_ZONE_POS = "1_9";
	private static final String RETURN_PROPERTY_POS = "1_12";

	/**
	 * This method verify the filing window according to filing calendar.
	 * 
	 * @param returnsUploadDetailsObj
	 * @return UploadFilingEnum
	 */
	@GetMapping(value = "/verifyFilingWindowStatus")
	public ServiceResponse verifyFillingWindow(@RequestHeader(name = "JobProcessingId") String jobProcessingId, @RequestBody RetUploadDetBean returnsUploadDetailsObj) {
		boolean fillingWindowStatusIsOpen = false;
		UploadFilingConstants filingWindowStatus = null;
		boolean fillingWindowNotYetOpen = false;
		FilingCalendarDto filingCalendarDto = new FilingCalendarDto();

		try {
			if (returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_ADHOC.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_AS_AN_WHEN.getConstantLongVal())) {
				filingCalendarDto.setStartDate(DateManip.getCurrentDate(DateConstants.DD_MM_YYYY.getDateConstants()));
				filingCalendarDto.setEndDate(DateManip.getCurrentDate(DateConstants.DD_MM_YYYY.getDateConstants()));
				filingCalendarDto.setFilingStatus(UploadFilingConstants.FILING_WINDOW_OPEN);
				return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(filingCalendarDto).build();
			}

			DateValidationsController dateValidationsControlObj = new DateValidationsController();
			String currentDate = DateManip.getCurrentDate(DateConstants.DD_MM_YYYY.getDateConstants());

			String date = returnsUploadDetailsObj.getStartDate();
			Integer returnPropertyValId = returnsUploadDetailsObj.getReturnPropertyValId();

			if (returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_FIRST_HALF_MONTHLY_EXCLUDE_HOLIDAY.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_FORTNIGHTLY.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_WEEKLY.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_DAILY.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_FORTNIGHTLY_15_DAYS.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_QUARTERLY_WITH_LAST_FORTNIGHT_OF_QUARTER.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_MONTHLY_WITH_LAST_FORTNIGHT_OF_QUARTER.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_CUSTOMIZED_MONTHLY_WITH_LAST_FORTNIGHT_OF_QUARTER.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_CUSTOMIZED_QUARTERLY_WITH_LAST_FORTNIGHT_OF_QUARTER.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_CUSTOMIZED_MONTHLY_WITH_LAST_FRIDAY_OF_QUARTER.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_HALF_MONTHLY.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_ANNUALLY.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_HALF_YEARLY.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_QUARTERLY.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_ANNUAL_QUATER.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_CUSTOMIZED_ANNUALLY.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_MONTHLY.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_CUSTOMIZED_ANNUALY_WITH_LAST_FRIDAY_OF_FINYEAR.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.CUSTOMIZED_ANNUALY_WITH_LAST_FRIDAY_OF_JUNE.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_CUSTOMIZED_HALF_YEARLY.getConstantLongVal())) {
				date = returnsUploadDetailsObj.getEndDate();
			}

			Return returnObj = returnService.getDataById(returnsUploadDetailsObj.getReturnId());
			EntityBean entObj = entityService.getDataById(returnsUploadDetailsObj.getEntityId());

			Map<Long, Map<Long, FillingEndDatesBean>> formFillingWindowMap = filingCalendarModifiedService.fetchFormFillingEndDatesDates(returnObj, entObj, date, returnPropertyValId);
			if (formFillingWindowMap != null && !formFillingWindowMap.isEmpty()) {
				// --- If Filling window is defined in the application Fetch end
				// dates map
				Map<Long, FillingEndDatesBean> fillingWindowEndDatesMap = formFillingWindowMap.get(returnsUploadDetailsObj.getFrequencyId());
				String startDateCalculated = null;
				String endDateCalculated = null;

				if (returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_FIRST_HALF_MONTHLY_EXCLUDE_HOLIDAY.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_FORTNIGHTLY.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_WEEKLY.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_DAILY.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_FORTNIGHTLY_15_DAYS.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_QUARTERLY_WITH_LAST_FORTNIGHT_OF_QUARTER.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_MONTHLY_WITH_LAST_FORTNIGHT_OF_QUARTER.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_CUSTOMIZED_MONTHLY_WITH_LAST_FORTNIGHT_OF_QUARTER.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_CUSTOMIZED_QUARTERLY_WITH_LAST_FORTNIGHT_OF_QUARTER.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_CUSTOMIZED_MONTHLY_WITH_LAST_FRIDAY_OF_QUARTER.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_HALF_MONTHLY.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_ANNUALLY.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_HALF_YEARLY.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_QUARTERLY.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_ANNUAL_QUATER.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_CUSTOMIZED_ANNUALLY.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_MONTHLY.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_CUSTOMIZED_ANNUALY_WITH_LAST_FRIDAY_OF_FINYEAR.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.CUSTOMIZED_ANNUALY_WITH_LAST_FRIDAY_OF_JUNE.getConstantLongVal()) || returnsUploadDetailsObj.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_CUSTOMIZED_HALF_YEARLY.getConstantLongVal())) {
					if (!CollectionUtils.isEmpty(fillingWindowEndDatesMap)) {
						startDateCalculated = fillingWindowEndDatesMap.get(0L).getStartDate();
						//						endDateCalculated = fillingWindowEndDatesMap.get(0L).getEndDate();
						endDateCalculated = fillingWindowEndDatesMap.get(0L).getGraceDaysDate();

						filingCalendarDto.setStartDate(startDateCalculated);
						filingCalendarDto.setEndDate(endDateCalculated);

						if (dateValidationsControlObj.getDayDiff(currentDate, startDateCalculated, DateConstants.DD_MM_YYYY.getDateConstants()) > 0) {
							filingWindowStatus = UploadFilingConstants.FILING_WINDOW_NOT_YET_OPEN;
							fillingWindowNotYetOpen = true;
						} else if (dateValidationsControlObj.getDayDiff(currentDate, startDateCalculated, DateConstants.DD_MM_YYYY.getDateConstants()) <= 0 && dateValidationsControlObj.getDayDiff(currentDate, endDateCalculated, DateConstants.DD_MM_YYYY.getDateConstants()) >= 0) {
							fillingWindowStatusIsOpen = true;
						}
					} else {
						fillingWindowNotYetOpen = true;
					}
				}

				if (fillingWindowStatusIsOpen) {
					filingWindowStatus = UploadFilingConstants.FILING_WINDOW_OPEN;
				} else if (!fillingWindowStatusIsOpen && !fillingWindowNotYetOpen) {
					filingWindowStatus = UploadFilingConstants.FILING_WINDOW_CLOSED;
				} else if (fillingWindowNotYetOpen) {
					filingWindowStatus = UploadFilingConstants.FILING_WINDOW_NOT_YET_OPEN;
				}
			} else {
				// -- No filling window defined
				filingWindowStatus = UploadFilingConstants.FILING_WINDOW_NOT_DEFINED;
			}
			filingCalendarDto.setFilingStatus(filingWindowStatus);
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_CONTROLLER.getErrorMessage(), e);
			return new ServiceResponse.ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorConstants.E027.getErrorCode()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorConstants.E027.getErrorMessage())).build();
		}
		return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(filingCalendarDto).build();
	}

	/**
	 * This method is check whether unlock request is submitted or not
	 * 
	 * @param returnsUploadDetailsObj
	 * @return true if unlock request is done
	 * @throws ServiceException
	 * @throws ParseException
	 */
	public UnlockingRequest getUnlockRequestObj(RetUploadDetBean returnsUploadDetailsObj) {
		try {
			Map<String, List<String>> valueMap = new HashMap<>();
			List<String> valueList = new ArrayList<>();
			valueList.add(returnsUploadDetailsObj.getReturnId() + "");
			valueMap.put(ColumnConstants.RETURNID.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(returnsUploadDetailsObj.getEntityId() + "");
			valueMap.put(ColumnConstants.ENTITYID.getConstantVal(), valueList);

			//			valueList = new ArrayList<>();
			//			valueList.add(returnsUploadDetailsObj.getFinYearFreqDescId() + "");
			//			valueMap.put(ColumnConstants.FIN_YEAR_FREQ_DESC_ID.getConstantVal(), valueList);

			//			valueList = new ArrayList<>();
			//			valueList.add(returnsUploadDetailsObj.getStartDate() + "");
			//			valueMap.put(ColumnConstants.STARTDATE.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(returnsUploadDetailsObj.getEndDate() + "");
			valueMap.put(ColumnConstants.ENDDATE.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			List<UnlockingRequest> unlockingRequestList = null;
			if (!StringUtils.isEmpty(returnsUploadDetailsObj.getReturnPropertyValId())) {
				valueList.add(returnsUploadDetailsObj.getReturnPropertyValId() + "");
				valueMap.put(ColumnConstants.RETURN_PROPERTY_VAL_ID.getConstantVal(), valueList);
				unlockingRequestList = unlockRequestService.getDataByColumnValue(valueMap, MethodConstants.GET_UNLOCKING_REAQUEST_BY_RETURN_STATUS.getConstantVal());
			} else {
				unlockingRequestList = unlockRequestService.getDataByColumnValue(valueMap, MethodConstants.GET_UNLOCKING_REAQUEST.getConstantVal());
			}

			if (!CollectionUtils.isEmpty(unlockingRequestList)) {
				return unlockingRequestList.get(0);
			}
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.DEFAULT_MSG, e);
		}
		return null;
	}

	public RevisionRequest getRevisionRequestObj(RetUploadDetBean returnsUploadDetailsObj) {

		try {
			Map<String, List<String>> valueMap = new HashMap<>();
			List<String> valueList = new ArrayList<>();
			valueList.add(returnsUploadDetailsObj.getReturnId() + "");
			valueMap.put(ColumnConstants.RETURNID.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(returnsUploadDetailsObj.getEntityId() + "");
			valueMap.put(ColumnConstants.ENTITYID.getConstantVal(), valueList);

			//			valueList = new ArrayList<>();
			//			valueList.add(returnsUploadDetailsObj.getStartDate() + "");
			//			valueMap.put(ColumnConstants.STARTDATE.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(returnsUploadDetailsObj.getEndDate() + "");
			valueMap.put(ColumnConstants.ENDDATE.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			List<RevisionRequest> revisionRequestList = null;
			if (!StringUtils.isEmpty(returnsUploadDetailsObj.getReturnPropertyValId())) {
				valueList.add(returnsUploadDetailsObj.getReturnPropertyValId() + "");
				valueMap.put(ColumnConstants.RETURN_PROPERTY_VAL_ID.getConstantVal(), valueList);
				revisionRequestList = revisionRequestService.getDataByColumnValue(valueMap, MethodConstants.GET_REVISION_REAQUEST_BY_RETURN_STATUS.getConstantVal());
			} else {
				revisionRequestList = revisionRequestService.getDataByColumnValue(valueMap, MethodConstants.GET_REVISION_REAQUEST.getConstantVal());
			}

			if (!CollectionUtils.isEmpty(revisionRequestList)) {
				return revisionRequestList.get(0);
			}
			return null;
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.DEFAULT_MSG, e);
		}
		return null;
	}

	/**
	 * This method is check whether same filing is done previously
	 * 
	 * @param returnsUploadDetailsObj
	 * @return UploadFilingEnum
	 * @throws ServiceException
	 * @throws ParseException
	 * @throws com.iris.exception.ExcelFormatInvalidException
	 */
	public List<ReturnsUploadDetails> chkExistingUploadInfo(RetUploadDetBean returnsUploadDetailsObj) throws ServiceException {
		Map<String, List<String>> valueMap = new HashMap<>();
		List<String> valueList = new ArrayList<>();
		valueList.add(returnsUploadDetailsObj.getReturnId() + "");
		valueMap.put(ColumnConstants.RETURNID.getConstantVal(), valueList);

		valueList = new ArrayList<>();
		valueList.add(returnsUploadDetailsObj.getEntityId() + "");
		valueMap.put(ColumnConstants.ENTITYID.getConstantVal(), valueList);

		//		valueList = new ArrayList<>();
		//		valueList.add(returnsUploadDetailsObj.getStartDate() + "");
		//		valueMap.put(ColumnConstants.STARTDATE.getConstantVal(), valueList);

		valueList = new ArrayList<>();
		valueList.add(returnsUploadDetailsObj.getEndDate() + "");
		valueMap.put(ColumnConstants.ENDDATE.getConstantVal(), valueList);

		valueList = new ArrayList<>();
		valueList.add(GeneralConstants.BUSINESS_VALIDATION_FAIL.getConstantIntVal() + "");
		valueList.add(GeneralConstants.CUSTOM_VALIDATION_FAILED.getConstantIntVal() + "");
		valueList.add(GeneralConstants.TECHNICAL_ERROR_ID.getConstantIntVal() + "");

		valueMap.put(ColumnConstants.STATUS.getConstantVal(), valueList);

		return returnUploadDetailsService.getDataByColumnValue(valueMap, MethodConstants.GET_EXISTING_UPLOAD_DATA_WITHOUT_BUSINESS_VALIDATION_FAILED.getConstantVal());
	}

	/**
	 * This method is called during upload instance document in file system
	 * 
	 * @param returnsUploadDetailsObj
	 * @return true if uploaded successfully
	 * @throws ServiceException
	 * @throws ParseException
	 */
	public String uploadFileOperation(RetUploadDetBean returnsUploadDetailsObj) throws ServiceException, ParseException {
		if (returnsUploadDetailsObj.getInstanceFilePath() == null) {
			return null;
		}

		String financialYear = DateManip.formatDate(returnsUploadDetailsObj.getEndDate(), DateConstants.DD_MM_YYYY.getDateConstants(), DateConstants.YYYY.getDateConstants());
		String modifiedInstanceFilePath = ResourceUtil.getKeyValue("filepath.root") + ResourceUtil.getKeyValue("filepath.instanceZip") + returnsUploadDetailsObj.getEntityCode().trim() + File.separator + financialYear + File.separator;
		String date = DateManip.getCurrentDateTime(DateConstants.DD_MM_YYYY.getDateConstants(), "HH:mm:ss:SSS");
		date = date.replaceAll("[:]", "-");
		date = date.replaceAll("[ ]", "_");
		String modifiedInstanceFileName = returnsUploadDetailsObj.getEntityId() + "_" + returnsUploadDetailsObj.getEntityCode().trim() + "_" + returnsUploadDetailsObj.getUploadedByUserId() + "_" + date.trim();
		modifiedInstanceFilePath += modifiedInstanceFileName;

		returnsUploadDetailsObj.setModifiedInstanceFileName(modifiedInstanceFileName + "." + returnsUploadDetailsObj.getInstanceFileType());

		if (new File(returnsUploadDetailsObj.getInstanceFilePath()).isFile()) {
			FileManager fileManageObj = new FileManager();
			if (fileManageObj.copyFile(new File(returnsUploadDetailsObj.getInstanceFilePath()), new File(modifiedInstanceFilePath + File.separator + returnsUploadDetailsObj.getModifiedInstanceFileName()))) {
				return modifiedInstanceFilePath + File.separator + returnsUploadDetailsObj.getModifiedInstanceFileName();
			} else {
				LOGGER.error("Error while moving Source File Path : " + returnsUploadDetailsObj.getInstanceFilePath());
				LOGGER.error("Error while moving Destination File Path : " + modifiedInstanceFilePath + File.separator + returnsUploadDetailsObj.getModifiedInstanceFileName());
				return null;
			}
		}

		return null;
	}

	/**
	 * This method is called during upload attachment in file system
	 * 
	 * @param retUploadDetBean
	 * @return true if upload attachment successfully
	 */
	public String uploadAttachement(RetUploadDetBean retUploadDetBean) {
		String date = DateManip.getCurrentDateTime(DateConstants.DD_MM_YYYY.getDateConstants(), "HH:mm:ss:SSS");
		date = date.replaceAll("[:]", "-");
		date = date.replaceAll("[ ]", "_");

		String newAttachementFileName = retUploadDetBean.getEntityId() + "_" + retUploadDetBean.getEntityCode().trim() + "_" + retUploadDetBean.getUploadedByUserId() + "_" + date.trim() + "." + retUploadDetBean.getSupportiveDocFileType();

		retUploadDetBean.setModifiedSupportiveDocFileName(newAttachementFileName);

		if (retUploadDetBean.getSupportiveDocFilePath() != null) {
			String uploadAttachmentPath = ResourceUtil.getKeyValue("filepath.root") + ResourceUtil.getKeyValue("filepath.attachement") + retUploadDetBean.getEntityCode().trim() + File.separator + newAttachementFileName;

			FileManager fileManageObj = new FileManager();
			if (fileManageObj.copyFile(new File(retUploadDetBean.getSupportiveDocFilePath()), new File(uploadAttachmentPath))) {
				return uploadAttachmentPath + newAttachementFileName;
			} else {
				LOGGER.error("Error while moving Source File Path : " + retUploadDetBean.getSupportiveDocFilePath());
				LOGGER.error("Error while moving Destination File Path : " + uploadAttachmentPath);
				return null;
			}
		} else {
			return null;
		}
	}

	public Long insertData(RetUploadDetBean returnsUploadDetailsObjPassed, Return returnObj, EntityBean entityObj, FileDetails fileDetailBean, Map<String, Map<Boolean, List<String>>> fieldCheckListMap) {
		try {
			ReturnsUploadDetails returnsUploadDetailsObj = new ReturnsUploadDetails();

			// -- Setting Uploaded as filling status
			int id = GeneralConstants.FILLING_STATUS.getConstantIntVal();

			FilingStatus fillingStObj = new FilingStatus();
			fillingStObj.setFilingStatusId(id);
			returnsUploadDetailsObj.setFilingStatus(fillingStObj);
			returnsUploadDetailsObj.setPrevUploadId(returnsUploadDetailsObjPassed.getPrevUploadId());

			if (returnsUploadDetailsObjPassed.getReturnPropertyValId() != null) {
				//				ReturnPropertyValue returnPropertyValue = new ReturnPropertyValue();
				//				returnPropertyValue.setReturnProprtyValId(returnsUploadDetailsObjPassed.getReturnPropertyValId());
				returnsUploadDetailsObj.setReturnPropertyValue(returnUploadDetailsService.getReturnPropertyValue(returnsUploadDetailsObjPassed.getReturnPropertyValId()));
			}

			if (returnsUploadDetailsObjPassed.getRevisionReqId() != null) {
				RevisionRequest revReq = revisionRequestService.getDataById(returnsUploadDetailsObjPassed.getRevisionReqId());
				revReq.setRevisionStatus(GeneralConstants.CLOSED.getConstantVal());
				returnsUploadDetailsObj.setRevisionRequestId(revReq);
			}

			// Update close flag of unlock request
			if (returnsUploadDetailsObjPassed.getUnlockReqId() != null) {
				UnlockingRequest unlockReq = unlockRequestService.getDataById(returnsUploadDetailsObjPassed.getUnlockReqId());
				unlockReq.setUnlockStatus(GeneralConstants.CLOSED.getConstantVal());
				returnsUploadDetailsObj.setUnlockingReqId(unlockReq);
			}

			returnsUploadDetailsObj.setReturnObj(returnObj);
			returnsUploadDetailsObj.setEntity(entityObj);

			Frequency freq = new Frequency();
			freq.setFrequencyId(returnsUploadDetailsObjPassed.getFrequencyId());
			returnsUploadDetailsObj.setFrequency(freq);
			returnsUploadDetailsObj.setUploadedDate(DateManip.getCurrentDateTime());

			returnsUploadDetailsObj.setActive("1");

			UserMaster userMaster = new UserMaster();
			userMaster.setUserId(returnsUploadDetailsObjPassed.getUploadedByUserId());
			returnsUploadDetailsObj.setUploadedBy(userMaster);

			returnsUploadDetailsObj.setStartDate(DateManip.convertStringToDate(returnsUploadDetailsObjPassed.getStartDate(), DateConstants.DD_MM_YYYY.getDateConstants()));
			returnsUploadDetailsObj.setEndDate(DateManip.convertStringToDate(returnsUploadDetailsObjPassed.getEndDate(), DateConstants.DD_MM_YYYY.getDateConstants()));

			if (returnsUploadDetailsObjPassed.getNillable().equals(Boolean.TRUE)) {
				returnsUploadDetailsObj.setNillableComments(returnsUploadDetailsObjPassed.getNillableComments());
				returnsUploadDetailsObj.setNillable(returnsUploadDetailsObjPassed.getNillable());
			} else {
				returnsUploadDetailsObj.setNillable(false);
			}

			returnsUploadDetailsObj.setInstanceFile(returnsUploadDetailsObjPassed.getModifiedInstanceFileName());

			UserRole userRole = new UserRole();
			userRole.setUserRoleId(returnsUploadDetailsObjPassed.getUploadedUserRoleId());
			returnsUploadDetailsObj.setUploadUsrRole(userRole);

			returnsUploadDetailsObj.setApprovalRoleFk(returnsUploadDetailsObjPassed.getApprovalIdFk());
			returnsUploadDetailsObj.setCurrentWFStep(returnsUploadDetailsObjPassed.getCurrentWFStep());

			returnsUploadDetailsObj.setWorkFlowMaster(workflowMasterService.getDataById(returnsUploadDetailsObjPassed.getWorkFlowId()));

			returnsUploadDetailsObj.setApprovalResult(null);
			returnsUploadDetailsObj.setAssignedTo(null);
			returnsUploadDetailsObj.setAssignedOn(DateManip.getCurrentDateTime());
			returnsUploadDetailsObj.setFileType(returnsUploadDetailsObjPassed.getInstanceFileType());

			ReturnTemplate taxonomy = new ReturnTemplate();
			taxonomy.setReturnTemplateId(returnsUploadDetailsObjPassed.getTaxonomyId());
			returnsUploadDetailsObj.setTaxonomyId(taxonomy);

			if (!returnsUploadDetailsObj.isNillable()) {
				Currency currency = currencyRepository.findByIsDefault(true);
				if (Validations.isEmpty(reportingCurrency)) {
					reportingCurrency = "INR";
				}
				if (!currency.getCurrencyISOCode().equalsIgnoreCase(GeneralConstants.CURRENCY_ISO_CODE.getConstantVal() + (reportingCurrency))) {
					List<Currency> currencyLst = currencyRepository.findByCurrencyISOCode(GeneralConstants.CURRENCY_ISO_CODE.getConstantVal() + (reportingCurrency).toUpperCase().trim());
					if (CollectionUtils.isEmpty(currencyLst)) {
						LOGGER.error(ErrorConstants.CURRENCY_NOT_FOUND.getConstantVal());
						insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0714.toString(), fieldCheckListMap);
						return null;
					}
					Long currencyId = currencyLst.get(0).getCurrencyId();
					List<CurrencyConversion> currencyConversionLst = currencyConversionRepository.findByCurrencyId(currencyId);
					if (CollectionUtils.isEmpty(currencyConversionLst)) {
						LOGGER.error(ErrorConstants.CURRENCY_CONVERSION_RATE_NOT_FOUND.getConstantVal());
						insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0496.toString(), fieldCheckListMap);
						return null;
					}
					returnsUploadDetailsObj.setConversionRate(currencyConversionLst.get(0).getConversionRate());
				} else {
					returnsUploadDetailsObj.setConversionRate(1);
				}
				returnsUploadDetailsObj.setReportingCurrency(reportingCurrency);
			}
			returnsUploadDetailsObj.setAttachedFile(returnsUploadDetailsObjPassed.getModifiedSupportiveDocFileName());

			FilingStatus fileStatus = new FilingStatus();
			int status = GeneralConstants.META_DATA_VALIDATED_ID.getConstantIntVal();
			fileStatus.setFilingStatusId(status);

			FileDetails saveFileDetailsBean = null;
			if (StringUtils.isEmpty(fileDetailBean.getId())) {
				returnsUploadDetailsObj.setFileDetailsBean(fileDetailBean);
				fileDetailBean.setFilingStatus(fileStatus);

				fileDetailBean.setCreationDate(new Date());
				fileDetailBean.setIsActive(true);
				fileDetailBean.setProcessingFlag(true);
				fileDetailBean.setUserMaster(userMaster);

				fileDetailBean.setSystemModifiedFileName(returnsUploadDetailsObjPassed.getModifiedInstanceFileName());

				if (fileDetailBean.getUserSelectedFileName() != null) {
					fileDetailBean.setFileName(fileDetailBean.getUserSelectedFileName());
				}

				returnsUploadDetailsObj.setFileDetailsBean(fileDetailBean);

				List<ReturnsUploadDetails> returnUploadDetailsList = new ArrayList<>();
				returnUploadDetailsList.add(returnsUploadDetailsObj);

				fileDetailBean.setReturnUploadDetailsList(returnUploadDetailsList);

				//				if(fileDetailBean.getUploadChannelIdFk() != null && fileDetailBean.getUploadChannelIdFk().getUploadChannelId().equals(GeneralConstants.EMAIL_CHANNEL.getConstantLongVal())) {
				//					fileDetailBean.setIfscCode(entityObj.getIfscCode());
				//				}

				FileDetails fileDetailsBean = fileDetailsRepo.save(fileDetailBean);

				fileDetailBean.setApplicationProcessId(generatedAppliationProcessId(fileDetailBean.getUserName(), entityObj.getIfscCode(), returnObj.getReturnCode(), fileDetailsBean.getId() + ""));
				saveFileDetailsBean = fileDetailsRepo.save(fileDetailBean);
				return saveFileDetailsBean.getReturnUploadDetailsList().get(0).getUploadId();
			} else {
				FileDetails dbFileDetailsBean = fileDetailsRepo.getDataById(fileDetailBean.getId());
				dbFileDetailsBean.setFilingStatus(fileStatus);
				dbFileDetailsBean.setBsr7FileType(fileDetailBean.getBsr7FileType());
				dbFileDetailsBean.setSystemModifiedFileName(returnsUploadDetailsObjPassed.getModifiedInstanceFileName());

				List<ReturnsUploadDetails> returnUploadDetailsList = new ArrayList<>();

				returnsUploadDetailsObj.setFileDetailsBean(dbFileDetailsBean);
				returnUploadDetailsList.add(returnsUploadDetailsObj);

				dbFileDetailsBean.setReturnUploadDetailsList(returnUploadDetailsList);
				//				if(fileDetailBean.getUploadChannelIdFk() != null && fileDetailBean.getUploadChannelIdFk().getUploadChannelId().equals(GeneralConstants.EMAIL_CHANNEL.getConstantLongVal())) {
				//					dbFileDetailsBean.setIfscCode(entityObj.getIfscCode());
				//				}
				saveFileDetailsBean = fileDetailsRepo.save(dbFileDetailsBean);
				return saveFileDetailsBean.getReturnUploadDetailsList().get(0).getUploadId();
			}
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.DEFAULT_MSG, e);
			insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0786.toString(), fieldCheckListMap);
			return null;
		}
	}

	String generatedAppliationProcessId(String userName, String ifscCode, String returnCode, String fileDetailsId) {
		String str = "";
		if (userName != null) {
			str = str + userName;
		} else {
			str = str + "NA";
		}
		if (ifscCode != null) {
			str = str + "-" + ifscCode;
		} else {
			str = str + "NA";
		}
		if (returnCode != null) {
			str = str + "-" + returnCode;
		} else {
			str = str + "NA";
		}
		if (fileDetailsId != null) {
			str = str + "-" + fileDetailsId;
		} else {
			str = str + "NA";
		}
		return str;
	}

	public boolean validateFileMetaData(FileDetails fileDetailsBean, Map<String, Map<Boolean, List<String>>> fieldStatusMap, Return returnObj) {
		boolean isValidationFailed = false;
		File file = new File(fileDetailsBean.getFilePath());
		LOGGER.error("validate file meta data called", isBsrNewImplementation);

		// Set File Size
		fileDetailsBean.setSize(file.length());
		try {
			if (fileDetailsBean.getFileType().equalsIgnoreCase(GeneralConstants.CSV.getConstantVal()) || fileDetailsBean.getFileType().equalsIgnoreCase(GeneralConstants.TXT.getConstantVal())) {
				if (org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase(fileDetailsBean.getReturnCode(), GeneralConstants.R002.getConstantVal(), GeneralConstants.R014.getConstantVal(), GeneralConstants.R088.getConstantVal(), GeneralConstants.R250.getConstantVal(), GeneralConstants.R251.getConstantVal(), GeneralConstants.R146.getConstantVal(), GeneralConstants.R327.getConstantVal(), GeneralConstants.R328.getConstantVal(), GeneralConstants.R105.getConstantVal(), GeneralConstants.R150.getConstantVal(), GeneralConstants.R266.getConstantVal(), GeneralConstants.R267.getConstantVal(), GeneralConstants.R268.getConstantVal())) {
					// BSR1
					if (isBsrNewImplementation) {
						LOGGER.error("inside new implementation method", isBsrNewImplementation);
						isValidationFailed = validateNonXbrlTransactionalReturns(fileDetailsBean, fieldStatusMap);
						LOGGER.error("inside new implementation method --->" + isValidationFailed);
					} else {
						isValidationFailed = validateBSR1AndBSR7Data(fileDetailsBean, fieldStatusMap, returnObj);
					}
				} else {
					isValidationFailed = validateCSVMetaData(fileDetailsBean, fieldStatusMap, returnObj);
				}
			} else if (fileDetailsBean.getFileType().equalsIgnoreCase(GeneralConstants.XML.getConstantVal())) {
				isValidationFailed = validateXMLMetaData(fileDetailsBean, fieldStatusMap, returnObj);
			} else if (fileDetailsBean.getFileType().equalsIgnoreCase(GeneralConstants.JSON.getConstantVal())) {
				isValidationFailed = validateJSONMetaData(fileDetailsBean, fieldStatusMap, returnObj);
			} else if (fileDetailsBean.getFileType().equalsIgnoreCase(GeneralConstants.PDF.getConstantVal())) {
				String date1 = DateManip.formatDate(fileDetailsBean.getReportingPeriodEndDateInString(), BSR_RETURN_DATE_FORMAT, DateConstants.DD_MM_YYYY.getDateConstants());

				fileDetailsBean.setReportingPeriodEndDate(DateManip.convertStringToDate(date1, DateConstants.DD_MM_YYYY.getDateConstants()));
				String calculatedStartDate = getCalculatedStartDate(DateManip.convertStringToDate(date1, DateConstants.DD_MM_YYYY.getDateConstants()), returnObj.getFrequency());
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DD_SLASH_MM_SLASH_YYYY);
				fileDetailsBean.setReportingPeriodStartDate(simpleDateFormat.parse(calculatedStartDate));
				isValidationFailed = false;
			} else {
				isValidationFailed = false;
				insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0641.toString(), fieldStatusMap);
			}
		} catch (Exception e) {
			isValidationFailed = true;
			insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0786.toString(), fieldStatusMap);
			LOGGER.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}
		return isValidationFailed;
	}

	private boolean validateNonXbrlTransactionalReturns(FileDetails fileDetailsBean, Map<String, Map<Boolean, List<String>>> fieldStatusMap) {
		boolean isValidationFailed = false;
		LOGGER.error("validateBSR1AndBSR7DataNewImplementation method called : " + fileDetailsBean.getReturnCode());

		try {
			LOGGER.error("validateBSR1AndBSR7DataNewImplementation method called,inside if method : " + fileDetailsBean.getReturnCode());
			CSVBusinessValidationProcessor csvBusinessValidationProcessor = new CSVBusinessValidationProcessor();
			LOGGER.error("validateBSR1AndBSR7DataNewImplementation method called,inside if method 1");
			ReturnTemplate returnTemplate = returnTemplateService.getDataById(fileDetailsBean.getReturnId());
			LOGGER.error("validateBSR1AndBSR7DataNewImplementation method called,inside if method 2");

			String jsonString = getJsonString(fileDetailsBean.getReturnId());
			LOGGER.error("validateBSR1AndBSR7DataNewImplementation method called,inside if method 2" + jsonString);
			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson gson = gsonBuilder.create();
			LOGGER.error("validateBSR1AndBSR7DataNewImplementation method called,inside if method 3");
			Bsr1HeaderInputModel bsrInputModel = gson.fromJson(jsonString, Bsr1HeaderInputModel.class);
			bsrInputModel.setEntityCodeInFileDetails(fileDetailsBean.getEntityCode());
			LOGGER.error("validateBSR1AndBSR7DataNewImplementation method called,inside if method 4");
			LOGGER.error("validateBSR1AndBSR7DataNewImplementation, getAndValidateBSRHeader method called");
			Map<Integer, List<String>> headerErrorMap = csvBusinessValidationProcessor.getAndValidateBSRHeader(fileDetailsBean.getFilePath(), bsrInputModel);
			LOGGER.error("validateBSR1AndBSR7DataNewImplementation, getAndValidateBSRHeader method called end : " + headerErrorMap);
			if (!CollectionUtils.isEmpty(headerErrorMap) || (bsrInputModel.getIsValidHeader() != null && !bsrInputModel.getIsValidHeader())) {
				isValidationFailed = true;

				Integer key = 0;

				if (bsrInputModel.getIsValidHeader() != null && !bsrInputModel.getIsValidHeader()) {
					// this block of code would call, if reporting end date is not valid. for example: for quaterly return reporting periods month  should be like 03,06,09,12
					String errorCode = null;
					for (Map.Entry<Integer, HeaderDetail> headerDetails : bsrInputModel.getHeaderDetail().entrySet()) {
						if (headerDetails.getValue().getHeaderName().equals(REPORTING_PERIOD)) {
							key = headerDetails.getKey();

							for (Formula formula : headerDetails.getValue().getFormula()) {

								errorCode = formula.getErrorCodeIfNotValid();
							}
							break;
						}
					}
					LOGGER.error("validateBSR1AndBSR7DataNewImplementation, getAndValidateBSRHeader method called end 1:");
					if (headerErrorMap.get(key) == null) {
						List<String> errorCodeList = new ArrayList<>();
						errorCodeList.add(errorCode);
						headerErrorMap.put(key, errorCodeList);

					} else {
						headerErrorMap.get(key).add(errorCode);
					}
					LOGGER.error("validateBSR1AndBSR7DataNewImplementation, getAndValidateBSRHeader method called end 2: " + headerErrorMap);

					prepareErrorString(headerErrorMap, fieldStatusMap, returnTemplate.getReturnTemplateId(), bsrInputModel.getReturnCode());

				} else {
					prepareErrorString(headerErrorMap, fieldStatusMap, returnTemplate.getReturnTemplateId(), bsrInputModel.getReturnCode());
				}
			}

			String dateFormat = ResourceUtil.getKeyValue("generalInfoDateFormat." + fileDetailsBean.getReturnCode());
			if (dateFormat == null) {
				dateFormat = BSR_RETURN_DATE_FORMAT;
			}

			Map<Integer, HeaderDetail> headeDetails = bsrInputModel.getHeaderDetail();

			if (!StringUtils.isEmpty(headeDetails.get(bsrInputModel.getStartDatePosition()).getHeaderVal()) && !StringUtils.isEmpty(headeDetails.get(bsrInputModel.getEndDatePosition()).getHeaderVal().split("~")[0])) {

				fileDetailsBean.setReportingPeriodStartDate(DateManip.convertStringToDate(headeDetails.get(bsrInputModel.getStartDatePosition()).getHeaderVal(), dateFormat));

				fileDetailsBean.setReportingPeriodEndDate(DateManip.convertStringToDate(headeDetails.get(bsrInputModel.getEndDatePosition()).getHeaderVal().split("~")[0], dateFormat));
			}

		} catch (Exception e) {
			LOGGER.error("Exception in validateBSR1AndBSR7DataNewImplementation", e);
			isValidationFailed = true;
			insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0729.toString(), fieldStatusMap);
		}
		return isValidationFailed;
	}

	private void prepareErrorString(Map<Integer, List<String>> headerErrorMap, Map<String, Map<Boolean, List<String>>> fieldStatusMap, Long returnTemplateId, String returnCode) {
		try {
			Map<Integer, String> bSRHeaderPositionMap = new HashMap<>();
			if (org.apache.commons.lang3.StringUtils.isNotBlank(returnCode) && returnCode.equals(GeneralConstants.R014.getConstantVal())) {
				bSRHeaderPositionMap = getBsr7HeaderPosition();
			} else if (org.apache.commons.lang3.StringUtils.isNotBlank(returnCode) && org.apache.commons.lang3.StringUtils.equalsAny(returnCode, GeneralConstants.R002.getConstantVal(), GeneralConstants.R088.getConstantVal(), GeneralConstants.R150.getConstantVal())) {
				bSRHeaderPositionMap = getBSRHeaderPosition();
			} else if (org.apache.commons.lang3.StringUtils.isNotBlank(returnCode) && org.apache.commons.lang3.StringUtils.equalsAny(returnCode, GeneralConstants.R250.getConstantVal(), GeneralConstants.R251.getConstantVal())) {
				bSRHeaderPositionMap = getNCRHeaderPosition();
			} else if (org.apache.commons.lang3.StringUtils.isNotBlank(returnCode) && returnCode.equals(GeneralConstants.R146.getConstantVal())) {
				bSRHeaderPositionMap = getR146Header();
			} else if (org.apache.commons.lang3.StringUtils.isNotBlank(returnCode) && returnCode.equals(GeneralConstants.R105.getConstantVal())) {
				bSRHeaderPositionMap = getR105Header();
			} else if (org.apache.commons.lang3.StringUtils.isNotBlank(returnCode) && (returnCode.equals(GeneralConstants.R327.getConstantVal()) || returnCode.equals(GeneralConstants.R328.getConstantVal()))) {
				bSRHeaderPositionMap = getIBSHeader();
			} else if (org.apache.commons.lang3.StringUtils.isNotBlank(returnCode) && (returnCode.equals(GeneralConstants.R266.getConstantVal()) || returnCode.equals(GeneralConstants.R267.getConstantVal()) || returnCode.equals(GeneralConstants.R268.getConstantVal()))) {
				bSRHeaderPositionMap = getCardReturnsHeader();
			}

			for (Map.Entry<Integer, List<String>> entry : headerErrorMap.entrySet()) {
				if (!CollectionUtils.isEmpty(entry.getValue())) {
					for (String errorCode : entry.getValue()) {
						insertValueIntoStatusMap(bSRHeaderPositionMap.get(entry.getKey()), false, errorCode, fieldStatusMap);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception in prepareErrorString", e);
		}
	}

	private Map<Integer, String> getCardReturnsHeader() {
		Map<Integer, String> headerPositionMap = new HashMap<>();

		headerPositionMap.put(0, MetaDataCheckConstants.RETURN_CODE_CHECK.getConstantVal());
		headerPositionMap.put(1, MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal());
		headerPositionMap.put(2, MetaDataCheckConstants.ENTITY_CODE_MATCH_CHECK.getConstantVal());
		headerPositionMap.put(3, MetaDataCheckConstants.REPORTING_DATE_CHECK.getConstantVal());
		headerPositionMap.put(4, MetaDataCheckConstants.BSR_ROW_COUNT_CHECK.getConstantVal());
		headerPositionMap.put(5, MetaDataCheckConstants.BSR_TOTAL_AMOUNT_CHECK.getConstantVal());
		headerPositionMap.put(6, MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal());
		headerPositionMap.put(7, MetaDataCheckConstants.BSR_COMPARE_FILE_PREPARE_REF_QUARTER_CHECK.getConstantVal());
		headerPositionMap.put(8, MetaDataCheckConstants.HEADER_FORMAT_CHECK.getConstantVal());

		return headerPositionMap;
	}

	private Map<Integer, String> getR105Header() {
		Map<Integer, String> headerPositionMap = new HashMap<>();

		headerPositionMap.put(0, MetaDataCheckConstants.RETURN_NAME_MATCH_CHECK.getConstantVal());
		headerPositionMap.put(1, MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal());
		headerPositionMap.put(2, MetaDataCheckConstants.ENTITY_CODE_MATCH_CHECK.getConstantVal());
		headerPositionMap.put(3, MetaDataCheckConstants.REPORTING_DATE_CHECK.getConstantVal());
		headerPositionMap.put(4, MetaDataCheckConstants.BSR_ROW_COUNT_CHECK.getConstantVal());
		headerPositionMap.put(5, MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal());
		headerPositionMap.put(6, MetaDataCheckConstants.BSR_COMPARE_FILE_PREPARE_REF_QUARTER_CHECK.getConstantVal());
		headerPositionMap.put(7, MetaDataCheckConstants.HEADER_FORMAT_CHECK.getConstantVal());

		return headerPositionMap;
	}

	private Map<Integer, String> getIBSHeader() {
		Map<Integer, String> headerPositionMap = new HashMap<>();

		headerPositionMap.put(0, MetaDataCheckConstants.JOB_CODE.getConstantVal());
		headerPositionMap.put(1, MetaDataCheckConstants.IBS_FILE_TYPE.getConstantVal());
		headerPositionMap.put(2, MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal());
		headerPositionMap.put(3, MetaDataCheckConstants.ENTITY_CODE_MATCH_CHECK.getConstantVal());
		headerPositionMap.put(4, MetaDataCheckConstants.REPORTING_DATE_CHECK.getConstantVal());
		headerPositionMap.put(5, MetaDataCheckConstants.BSR_ROW_COUNT_CHECK.getConstantVal());
		headerPositionMap.put(6, MetaDataCheckConstants.BSR_TOTAL_AMOUNT_CHECK.getConstantVal());
		headerPositionMap.put(7, MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal());
		headerPositionMap.put(8, MetaDataCheckConstants.BSR_COMPARE_FILE_PREPARE_REF_QUARTER_CHECK.getConstantVal());
		headerPositionMap.put(9, MetaDataCheckConstants.HEADER_FORMAT_CHECK.getConstantVal());

		return headerPositionMap;
	}

	private Map<Integer, String> getR146Header() {
		Map<Integer, String> headerPositionMap = new HashMap<>();

		headerPositionMap.put(0, MetaDataCheckConstants.RETURN_NAME_MATCH_CHECK.getConstantVal());
		headerPositionMap.put(1, MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal());
		headerPositionMap.put(2, MetaDataCheckConstants.ENTITY_CODE_MATCH_CHECK.getConstantVal());
		headerPositionMap.put(3, MetaDataCheckConstants.REPORTING_DATE_CHECK.getConstantVal());
		headerPositionMap.put(4, MetaDataCheckConstants.BSR_ROW_COUNT_CHECK.getConstantVal());
		headerPositionMap.put(5, MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal());
		headerPositionMap.put(6, MetaDataCheckConstants.BSR_COMPARE_FILE_PREPARE_REF_QUARTER_CHECK.getConstantVal());
		headerPositionMap.put(7, MetaDataCheckConstants.HEADER_FORMAT_CHECK.getConstantVal());

		return headerPositionMap;
	}

	private Map<Integer, String> getNCRHeaderPosition() {
		Map<Integer, String> headerPositionMap = new HashMap<>();

		headerPositionMap.put(0, MetaDataCheckConstants.PROJECT_NAME.getConstantVal());
		headerPositionMap.put(1, MetaDataCheckConstants.FILE_TYPE.getConstantVal());
		headerPositionMap.put(2, MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal());
		headerPositionMap.put(3, MetaDataCheckConstants.ENTITY_CODE_MATCH_CHECK.getConstantVal());
		headerPositionMap.put(4, MetaDataCheckConstants.REPORTING_DATE_CHECK.getConstantVal());
		headerPositionMap.put(5, MetaDataCheckConstants.BSR_ROW_COUNT_CHECK.getConstantVal());
		headerPositionMap.put(6, MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal());
		headerPositionMap.put(7, MetaDataCheckConstants.HEADER_FORMAT_CHECK.getConstantVal());

		return headerPositionMap;
	}

	private Map<Integer, String> getBsr7HeaderPosition() {
		Map<Integer, String> headerPositionMap = new HashMap<>();

		headerPositionMap.put(0, MetaDataCheckConstants.RETURN_NAME_MATCH_CHECK.getConstantVal());
		headerPositionMap.put(1, MetaDataCheckConstants.BSR7_FILE_TYPE.getConstantVal());
		headerPositionMap.put(2, MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal());
		headerPositionMap.put(3, MetaDataCheckConstants.ENTITY_CODE_MATCH_CHECK.getConstantVal());
		headerPositionMap.put(4, MetaDataCheckConstants.BSR7_AUDIT_STATUS_CHECK.getConstantVal());
		headerPositionMap.put(5, MetaDataCheckConstants.REPORTING_DATE_CHECK.getConstantVal());
		headerPositionMap.put(6, MetaDataCheckConstants.BSR_ROW_COUNT_CHECK.getConstantVal());
		headerPositionMap.put(7, MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal());
		headerPositionMap.put(8, MetaDataCheckConstants.BSR_COMPARE_FILE_PREPARE_REF_QUARTER_CHECK.getConstantVal());
		headerPositionMap.put(9, MetaDataCheckConstants.HEADER_FORMAT_CHECK.getConstantVal());

		return headerPositionMap;
	}

	private Map<Integer, String> getBSRHeaderPosition() {
		Map<Integer, String> headerPositionMap = new HashMap<>();

		headerPositionMap.put(0, MetaDataCheckConstants.RETURN_NAME_MATCH_CHECK.getConstantVal());
		headerPositionMap.put(1, MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal());
		headerPositionMap.put(2, MetaDataCheckConstants.ENTITY_CODE_MATCH_CHECK.getConstantVal());
		headerPositionMap.put(3, MetaDataCheckConstants.REPORTING_DATE_CHECK.getConstantVal());
		headerPositionMap.put(4, MetaDataCheckConstants.BSR_ROW_COUNT_CHECK.getConstantVal());
		headerPositionMap.put(5, MetaDataCheckConstants.BSR_TOTAL_AMOUNT_CHECK.getConstantVal());
		headerPositionMap.put(6, MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal());
		headerPositionMap.put(7, MetaDataCheckConstants.BSR_COMPARE_FILE_PREPARE_REF_QUARTER_CHECK.getConstantVal());
		headerPositionMap.put(8, MetaDataCheckConstants.HEADER_FORMAT_CHECK.getConstantVal());

		return headerPositionMap;
	}

	private String getJsonString(Long returnId) {
		try {
			ReturnFileFormatMap returnFileFormatMap = returnFileFormatMapService.getDataById(returnId);
			if (returnFileFormatMap != null) {
				return returnFileFormatMap.getJsonToReadFile();
			}
		} catch (Exception e) {
			LOGGER.error("Exception occoured while getting data", e);
		}
		return null;
	}

	private boolean validateXMLMetaData(FileDetails fileDetailsBean, Map<String, Map<Boolean, List<String>>> fieldStatusMap, Return returnObj) {
		boolean isValidationFailed = false;
		try {
			@SuppressWarnings("unchecked")
			Map<String, String> valueMap = JsonUtility.getGsonObject().fromJson(returnObj.getJsonStringToReadFile(), Map.class);
			Map<String, String> outputValueMap = null;
			ExtractFileData extractFileData = new ExtractFileData();

			outputValueMap = extractFileData.readXmlData(fileDetailsBean.getFilePath(), valueMap, false);

			if (outputValueMap.containsKey(GeneralConstants.WHETHER_NIL_REPORTING.getConstantVal())) {
				fileDetailsBean.setNillable(outputValueMap.get(GeneralConstants.WHETHER_NIL_REPORTING.getConstantVal()).equalsIgnoreCase(GeneralConstants.TRUE.getConstantVal()) ? Boolean.TRUE : Boolean.FALSE);
			} else {

				Map<String, String> valueMapForNill = null;
				for (ReturnFileFormatMap rec : returnObj.getReturnFileFormatMapList()) {
					if (rec.getFileFormat().getFileFormatId() == 3) {
						valueMapForNill = JsonUtility.getGsonObject().fromJson(rec.getJsonForNillStatus(), Map.class);
						break;
					}
				}

				Predicate<Map<String, String>> isNillFiling = (mapForNill) -> mapForNill.isEmpty();

				if (valueMapForNill != null && !isNillFiling.test(valueMapForNill)) {

					Map<String, String> instanceTagMap = extractFileData.readXMlValueForNillable(fileDetailsBean.getFilePath(), false);
					Set<String> instanceTagList = instanceTagMap.entrySet().stream().map(rec -> rec.getKey()).collect(Collectors.toSet());

					Set<String> tagsToBeRemoved = new HashSet<>();
					tagsToBeRemoved.add(GeneralConstants.SCHEMA_REF.getConstantVal());
					tagsToBeRemoved.add(GeneralConstants.XBRL_TAG.getConstantVal());
					tagsToBeRemoved.add(GeneralConstants.MEASURE.getConstantVal());
					tagsToBeRemoved.add(GeneralConstants.UNIT.getConstantVal());

					instanceTagList.removeAll(tagsToBeRemoved);
					//Set<String> instanceTagList = entitiesList.stream().map(rec -> rec.getEntityTag()).collect(Collectors.toSet());

					Set<String> mapNillStatusTags = valueMapForNill.entrySet().stream().map(rec -> rec.getValue()).collect(Collectors.toSet());

					Set<String> dbTagsFromInstanceList = new HashSet<>();
					for (java.util.Map.Entry<String, String> rec : valueMapForNill.entrySet()) {
						if (instanceTagList.contains(rec.getValue())) {
							dbTagsFromInstanceList.add(rec.getValue());
						}
					}

					if (!dbTagsFromInstanceList.isEmpty() && instanceTagList.size() > dbTagsFromInstanceList.size()) {
						fileDetailsBean.setNillable(false);
						//				} else if (!mapNillStatusTags.isEmpty() && !instanceTagList.isEmpty() && instanceTagList.size() <= outputValueMapForNill.size()) {
					} else if (!mapNillStatusTags.isEmpty() && !instanceTagList.isEmpty() && instanceTagList.size() <= dbTagsFromInstanceList.size()) {
						//check for if any element is present in filing other than json 
						// if any element is found other than json elements then its not nill filing 
						if (mapNillStatusTags.containsAll(instanceTagList)) {
							fileDetailsBean.setNillable(true);
						} else {
							fileDetailsBean.setNillable(false);
						}

					} else {
						fileDetailsBean.setNillable(true);
					}

					dbTagsFromInstanceList = null;
					instanceTagList = null;
					mapNillStatusTags = null;
					tagsToBeRemoved = null;
				} else {
					fileDetailsBean.setNillable(false);
				}
			}

			DateUtilsParser.BASE_DATE = baseDateForFortNightly;
			if (outputValueMap.containsKey(GeneralConstants.SCHEMA_REF.getConstantVal())) {
				fileDetailsBean.setSchemaRef(outputValueMap.get(GeneralConstants.SCHEMA_REF.getConstantVal()));
			}

			LOGGER.info("Output Value Map : " + outputValueMap.toString());

			isValidationFailed = validateXMLMetaDataInternal(outputValueMap, fileDetailsBean, fieldStatusMap, returnObj);
		} catch (Exception e) {
			isValidationFailed = true;
			insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0659.toString(), fieldStatusMap);
			LOGGER.error(ObjectCache.getErrorCodeKey(ErrorCode.E0659.toString()), e);
		}

		return isValidationFailed;
	}

	private boolean validateJSONMetaData(FileDetails fileDetailsBean, Map<String, Map<Boolean, List<String>>> fieldStatusMap, Return returnObj) {
		boolean isValidationFailed = false;
		try {
			ExtractFileData extractFileData = new ExtractFileData();
			DocumentInfo documetInfo = extractFileData.readDocumentInfoFromJSONFileData(fileDetailsBean.getFilePath());
			DateUtilsParser.BASE_DATE = baseDateForFortNightly;
			isValidationFailed = validateJSONMetaDataInternal(documetInfo, fileDetailsBean, fieldStatusMap, returnObj);
		} catch (Exception e) {
			isValidationFailed = true;
			insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0659.toString(), fieldStatusMap);
			LOGGER.error(ObjectCache.getErrorCodeKey(ErrorCode.E0659.toString()), e);
		}

		return isValidationFailed;
	}

	private boolean validateCSVMetaData(FileDetails fileDetailsBean, Map<String, Map<Boolean, List<String>>> fieldStatusMap, Return returnObj) {
		LOGGER.error("In Validate CSV Meta data start");
		boolean isValidationFailed = false;
		try {
			CSVInputModel csvInputModel = JsonUtility.getGsonObject().fromJson(returnObj.getJsonStringToReadFile(), CSVInputModel.class);
			csvInputModel.setCsvFilePath(fileDetailsBean.getFilePath());
			ExtractFileData extractFileData = new ExtractFileData();
			DateUtilsParser.BASE_DATE = baseDateForFortNightly;
			LOGGER.error("CSV FIle Reading start");
			CSVFileExtractorResult csvFileExtractorResult = extractFileData.readCSVFileData(csvInputModel);
			LOGGER.info("Base Date Fort Nightly - " + baseDateForFortNightly);
			LOGGER.error("CSV FIle Reading END");
			isValidationFailed = validateCSVMetaDataInternal(csvFileExtractorResult, fileDetailsBean, fieldStatusMap, returnObj, csvInputModel);
		} catch (CSVProcessorException cpex) {
			isValidationFailed = true;
			if (cpex.getErrorCode().equalsIgnoreCase("ERR012")) {
				insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0687.toString(), fieldStatusMap);
				LOGGER.error(ObjectCache.getErrorCodeKey(ErrorCode.E0687.toString()), cpex);
			} else if (cpex.getErrorCode().equalsIgnoreCase("ERR013")) {
				insertValueIntoStatusMap(MetaDataCheckConstants.GENERAL_INFO_TABLE_CHECK.getConstantVal(), false, ErrorCode.E0712.toString(), fieldStatusMap);
				LOGGER.error(ObjectCache.getErrorCodeKey(ErrorCode.E0712.toString()), cpex);
			} else if (cpex.getErrorCode().equalsIgnoreCase("ERR016")) {
				insertValueIntoStatusMap(MetaDataCheckConstants.GENERAL_INFO_TABLE_CHECK.getConstantVal(), false, ErrorCode.E1168.toString(), fieldStatusMap);
				LOGGER.error(ObjectCache.getErrorCodeKey(ErrorCode.E1168.toString()), cpex);
			} else {
				insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0659.toString(), fieldStatusMap);
				LOGGER.error(ObjectCache.getErrorCodeKey(ErrorCode.E0659.toString()), cpex);
			}
		} catch (Exception e) {
			isValidationFailed = true;
			insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0659.toString(), fieldStatusMap);
			LOGGER.error(ObjectCache.getErrorCodeKey(ErrorCode.E0659.toString()), e);
		}
		LOGGER.error("In Validate CSV Meta data end");
		return isValidationFailed;
	}

	private boolean validateBSR1AndBSR7Data(FileDetails fileDetailsBean, Map<String, Map<Boolean, List<String>>> fieldStatusMap, Return returnObj) {
		boolean isValidationFailed = false;

		String dateFormat = ResourceUtil.getKeyValue("generalInfoDateFormat." + fileDetailsBean.getReturnCode());
		if (dateFormat == null) {
			dateFormat = BSR_RETURN_DATE_FORMAT;
		}
		FileReader bsr1bsr7FileReader = new FileReader();

		if (fileDetailsBean.getReturnCode().equalsIgnoreCase(GeneralConstants.R002.getConstantVal()) || fileDetailsBean.getReturnCode().equalsIgnoreCase(GeneralConstants.R088.getConstantVal())) { // BSR1
			try {
				HeaderBSR1 headerBSR1 = bsr1bsr7FileReader.getHeaderBSR1(fileDetailsBean.getFilePath(), fileDetailsBean.getReturnCode());
				if (headerBSR1 == null) {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0651.toString(), fieldStatusMap);
				} else {
					isValidationFailed = validateBSR1AndBSR7FillingInformation(fileDetailsBean, headerBSR1.getBankCode(), headerBSR1.getReturnCode(), headerBSR1.getReportingStartDate(), headerBSR1.getReportingPeriod(), fieldStatusMap, dateFormat, null, returnObj);
				}
			} catch (Exception e) {
				isValidationFailed = true;
				insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0729.toString(), fieldStatusMap);
			}
		} else if (fileDetailsBean.getReturnCode().equalsIgnoreCase(GeneralConstants.R014.getConstantVal())) { // BSR7
			try {
				HeaderBSR7 headerBSR7 = bsr1bsr7FileReader.getHeaderBSR7(fileDetailsBean.getFilePath());
				if (headerBSR7 == null) {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0651.toString(), fieldStatusMap);
				} else {
					isValidationFailed = validateBSR1AndBSR7FillingInformation(fileDetailsBean, headerBSR7.getBankCode(), headerBSR7.getReturnCode(), headerBSR7.getReportingStartDate(), headerBSR7.getReportingPeriod(), fieldStatusMap, dateFormat, headerBSR7.getFileType(), returnObj);
				}
			} catch (Exception e) {
				isValidationFailed = true;
				insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0729.toString(), fieldStatusMap);
			}
		}
		return isValidationFailed;
	}

	private boolean validateBSR1AndBSR7FillingInformation(FileDetails fileDetailsBean, String entityCode, String returnCode, String startDate, String endDate, Map<String, Map<Boolean, List<String>>> fieldStatusMap, String dateFormat, String bsr7FileType, Return returnObj) throws ParseException {
		boolean isValidationFailed = false;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		simpleDateFormat.setLenient(false);

		Gson gson = new Gson();
		ReturnMetaDataBean metaDataBean = new ReturnMetaDataBean();

		if (returnObj.getReturnFileFormatMapList() != null) {
			for (ReturnFileFormatMap returnFormatMap : returnObj.getReturnFileFormatMapList()) {
				if (returnFormatMap.isActive()) {
					if (fileDetailsBean.getFileType().equalsIgnoreCase(GeneralConstants.TXT.getConstantVal())) {
						if (returnFormatMap.getFileFormat().getFileFormatId() == 5) {
							metaDataBean = gson.fromJson(returnFormatMap.getMetaDataJson(), ReturnMetaDataBean.class);
						}
					} else if (fileDetailsBean.getFileType().equalsIgnoreCase(GeneralConstants.JSON.getConstantVal())) {
						if (returnFormatMap.getFileFormat().getFileFormatId() == 9) {
							metaDataBean = gson.fromJson(returnFormatMap.getMetaDataJson(), ReturnMetaDataBean.class);
						}
					} else if (fileDetailsBean.getFileType().equalsIgnoreCase(GeneralConstants.XML.getConstantVal())) {
						metaDataBean = gson.fromJson(returnFormatMap.getMetaDataJson(), ReturnMetaDataBean.class);
					}
				}
			}
		}

		if (metaDataBean.isEntityCodeMatchCheck()) {
			if (StringUtils.isEmpty(entityCode)) {
				isValidationFailed = true;
				insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_CODE_MATCH_CHECK.getConstantVal(), false, ErrorCode.E0646.toString(), fieldStatusMap);
			} else {
				if (!entityCode.equalsIgnoreCase(fileDetailsBean.getEntityCode())) {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_CODE_MATCH_CHECK.getConstantVal(), false, ErrorCode.E0644.toString(), fieldStatusMap);
				} else {
					insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_CODE_MATCH_CHECK.getConstantVal(), true, null, fieldStatusMap);
				}
			}
		}

		if (metaDataBean.isReturnCodeMatchCheck()) {
			if (StringUtils.isEmpty(returnCode)) {
				isValidationFailed = true;
				insertValueIntoStatusMap(MetaDataCheckConstants.RETURN_CODE_MATCH_CHECK.getConstantVal(), false, ErrorCode.E0648.toString(), fieldStatusMap);
			} else {
				if (!returnCode.equalsIgnoreCase(fileDetailsBean.getOldReturnCode())) {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.RETURN_CODE_MATCH_CHECK.getConstantVal(), false, ErrorCode.E0647.toString(), fieldStatusMap);
				} else {
					insertValueIntoStatusMap(MetaDataCheckConstants.RETURN_CODE_MATCH_CHECK.getConstantVal(), true, null, fieldStatusMap);
				}
			}
		}

		boolean isEndDateFormatValid = true;
		boolean isStartDateFormatValid = true;
		if (StringUtils.isEmpty(startDate)) {
			isValidationFailed = true;
			insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal(), false, ErrorCode.E0650.toString(), fieldStatusMap);
		} else {
			if (startDate.length() != 8) {
				isValidationFailed = true;
				isEndDateFormatValid = false;
				insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal(), false, ErrorCode.E0669.toString(), fieldStatusMap);
			}
		}

		if (StringUtils.isEmpty(endDate)) {
			isValidationFailed = true;
			insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0651.toString(), fieldStatusMap);
		} else {
			if (endDate.length() != 8) {
				isValidationFailed = true;
				isStartDateFormatValid = false;
				insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0799.toString(), fieldStatusMap);
			}
		}

		try {
			if (isEndDateFormatValid && isStartDateFormatValid) {
				fileDetailsBean.setReportingPeriodEndDate(DateManip.convertStringToDate(endDate, dateFormat));
				fileDetailsBean.setReportingPeriodStartDate(DateManip.convertStringToDate(startDate, dateFormat));
				fileDetailsBean.setBsr7FileType(bsr7FileType);

				insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal(), true, null, fieldStatusMap);
				insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), true, null, fieldStatusMap);
			}
		} catch (Exception e) {
			isValidationFailed = true;
			insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0799.toString(), fieldStatusMap);
		}
		return isValidationFailed;
	}

	private boolean validateCSVMetaDataInternal(CSVFileExtractorResult csvFileExtractorResult, FileDetails fileDetailsBean, Map<String, Map<Boolean, List<String>>> fieldStatusMap, Return returnObj, CSVInputModel csvInputModel) throws ParseException {
		LOGGER.error("validateCSVMetaDataInternal start");
		boolean isValidationFailed = false;

		Gson gson = new Gson();
		ReturnMetaDataBean metaDataBean = new ReturnMetaDataBean();

		if (returnObj.getReturnFileFormatMapList() != null) {
			for (ReturnFileFormatMap returnFormatMap : returnObj.getReturnFileFormatMapList()) {
				if (returnFormatMap.isActive()) {
					if (fileDetailsBean.getFileType().equalsIgnoreCase(GeneralConstants.TXT.getConstantVal())) {
						if (returnFormatMap.getFileFormat().getFileFormatId() == 5) {
							metaDataBean = gson.fromJson(returnFormatMap.getMetaDataJson(), ReturnMetaDataBean.class);
						}
					} else if (fileDetailsBean.getFileType().equalsIgnoreCase(GeneralConstants.JSON.getConstantVal())) {
						if (returnFormatMap.getFileFormat().getFileFormatId() == 9) {
							metaDataBean = gson.fromJson(returnFormatMap.getMetaDataJson(), ReturnMetaDataBean.class);
						}
					} else if (fileDetailsBean.getFileType().equalsIgnoreCase(GeneralConstants.XML.getConstantVal())) {
						metaDataBean = gson.fromJson(returnFormatMap.getMetaDataJson(), ReturnMetaDataBean.class);
					}
				}
			}
		}

		String dateFormat = ResourceUtil.getKeyValue("generalInfoDateFormat." + fileDetailsBean.getReturnCode());
		String dateSaperator = ResourceUtil.getKeyValue("dateSaperator." + fileDetailsBean.getReturnCode());
		if (dateFormat == null) {
			dateFormat = DD_SLASH_MM_SLASH_YYYY;
		}
		if (dateSaperator == null) {
			dateSaperator = CSV_RETURN_DATE_SAPERATOR;
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		simpleDateFormat.setLenient(false);

		String entityCode = null;
		String returnCode = null;
		String startDate = null;
		String endDate = null;
		String frequency = null;
		String reportStatus = null;
		String reportingEndDateError = null;
		String entityName = null;
		String reportType = null;
		String versionNumber = null;
		String specialEconomicZone = null;

		String tableCode = returnObj.getReturnCode() + ServiceConstants.HYPHEN + ServiceConstants.FIRST_TABLE_CODE;

		if (!CollectionUtils.isEmpty(csvFileExtractorResult.getCsvDataRecords())) {
			if (csvFileExtractorResult.getCsvDataRecords().containsKey(tableCode)) {
				insertValueIntoStatusMap(MetaDataCheckConstants.GENERAL_INFO_TABLE_CHECK.getConstantVal(), true, null, fieldStatusMap);
				CSVDataRecord csvDataRecord = csvFileExtractorResult.getCsvDataRecords().get(tableCode);
				Map<String, String> tabularDataMap = csvDataRecord.getTabularData();
				if (tabularDataMap != null && tabularDataMap.size() > 0) {

					if (tabularDataMap.get(RETURN_CODE_INDEX_POS_IN_CSV) != null) {
						returnCode = tabularDataMap.get(RETURN_CODE_INDEX_POS_IN_CSV).trim();
					}
					if (tabularDataMap.get(ENTITY_CODE_INDEX_POS_IN_CSV) != null) {
						entityCode = tabularDataMap.get(ENTITY_CODE_INDEX_POS_IN_CSV).trim();
					}
					if (tabularDataMap.get(START_DATE_INDEX_POS_IN_CSV) != null) {
						startDate = tabularDataMap.get(START_DATE_INDEX_POS_IN_CSV).trim();
					}
					if (tabularDataMap.get(END_DATE_INDEX_POS_IN_CSV) != null) {
						endDate = tabularDataMap.get(END_DATE_INDEX_POS_IN_CSV).trim();
					}
					if (tabularDataMap.get(FREQUENCY_INDEX_POS_IN_CSV) != null) {
						frequency = tabularDataMap.get(FREQUENCY_INDEX_POS_IN_CSV).trim();
					}
					if (tabularDataMap.get(REPORT_STATUS_INDEX_POS_IN_CSV) != null) {
						reportStatus = tabularDataMap.get(REPORT_STATUS_INDEX_POS_IN_CSV).trim();
					}
					if (tabularDataMap.get(REPORTING_END_DATE_ERROR_IN_CSV) != null) {
						reportingEndDateError = tabularDataMap.get(REPORTING_END_DATE_ERROR_IN_CSV).trim();
					}
					if (tabularDataMap.get(ENTITY_NAME_INDEX_POS_IN_CSV) != null) {
						entityName = tabularDataMap.get(ENTITY_NAME_INDEX_POS_IN_CSV).trim();
					}
					if (tabularDataMap.get(REPORT_TYPE_POS) != null) {
						reportType = tabularDataMap.get(REPORT_TYPE_POS).trim();
					}
					if (tabularDataMap.get(VERSION_NO_POS) != null) {
						versionNumber = tabularDataMap.get(VERSION_NO_POS).trim();
					}
					if (tabularDataMap.get(SPECIAL_ECONOMIC_ZONE_POS) != null) {
						specialEconomicZone = tabularDataMap.get(SPECIAL_ECONOMIC_ZONE_POS).trim();
					}
				}
			} else {
				insertValueIntoStatusMap(MetaDataCheckConstants.GENERAL_INFO_TABLE_CHECK.getConstantVal(), false, ErrorCode.E0712.toString(), fieldStatusMap);
			}
		}

		if (metaDataBean.isEntityNameMatchCheck()) {
			if (!StringUtils.isEmpty(entityName)) {
				if (entityName.equalsIgnoreCase(fileDetailsBean.getEntityName())) {
					insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_NAME_MATCH_CHECK.getConstantVal(), true, null, fieldStatusMap);
				} else {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_NAME_MATCH_CHECK.getConstantVal(), false, ErrorCode.E1545.toString(), fieldStatusMap);
				}
			} else {
				isValidationFailed = true;
				insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_NAME_MATCH_CHECK.getConstantVal(), false, ErrorCode.E0717.toString(), fieldStatusMap);
			}
		}

		if (metaDataBean.isEntityCodeMatchCheck()) {
			// Entity code validation
			if (StringUtils.isEmpty(entityCode)) {
				isValidationFailed = true;
				insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_CODE_MATCH_CHECK.getConstantVal(), false, ErrorCode.E0646.toString(), fieldStatusMap);
			} else {
				if (!entityCode.equalsIgnoreCase(fileDetailsBean.getEntityCode())) {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_CODE_MATCH_CHECK.getConstantVal(), false, ErrorCode.E0644.toString(), fieldStatusMap);
				} else {
					insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_CODE_MATCH_CHECK.getConstantVal(), true, null, fieldStatusMap);
				}
			}
		}

		if (metaDataBean.isReturnCodeMatchCheck()) {
			// Return code validation
			if (StringUtils.isEmpty(returnCode)) {
				isValidationFailed = true;
				insertValueIntoStatusMap(MetaDataCheckConstants.RETURN_CODE_MATCH_CHECK.getConstantVal(), false, ErrorCode.E0648.toString(), fieldStatusMap);
			} else {
				if (!returnCode.equalsIgnoreCase(fileDetailsBean.getReturnCode())) {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.RETURN_CODE_MATCH_CHECK.getConstantVal(), false, ErrorCode.E0647.toString(), fieldStatusMap);
				} else {
					insertValueIntoStatusMap(MetaDataCheckConstants.RETURN_CODE_MATCH_CHECK.getConstantVal(), true, null, fieldStatusMap);
				}
			}
		}

		// Return property validation
		if (returnObj.getReturnPropertyIdFk() != null) {
			if (StringUtils.isEmpty(reportStatus)) {
				isValidationFailed = true;
				insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_STATUS_CHECK.getConstantVal(), false, ErrorCode.E0734.toString(), fieldStatusMap);
			} else {
				Integer returnPropertyValId = com.iris.util.constant.ReturnPropertyVal.getReturnPropertyIdByReturnPropertyValCode(reportStatus);
				List<Integer> list = returnObj.getReturnPropertyIdFk().getReturnPropertyValList().stream().map(f -> f.getReturnProprtyValId()).collect(Collectors.toList());
				if (!list.contains(returnPropertyValId)) {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_STATUS_CHECK.getConstantVal(), false, ErrorCode.E0735.toString(), fieldStatusMap);
				} else {
					insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_STATUS_CHECK.getConstantVal(), true, null, fieldStatusMap);
					fileDetailsBean.setReturnPropertyValId(returnPropertyValId);
				}
			}
		}

		boolean isFrequencyValid = true;
		try {
			if (metaDataBean.isReportingFrequencyCheck()) {
				if (StringUtils.isEmpty(frequency)) {
					isValidationFailed = true;
					isFrequencyValid = false;
					insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_FREQ_CHECK.getConstantVal(), false, ErrorCode.E0688.toString(), fieldStatusMap);
				} else {
					if (!frequency.equalsIgnoreCase(returnObj.getFrequency().getFrequencyCode())) {
						isValidationFailed = true;
						isFrequencyValid = false;
						insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_FREQ_CHECK.getConstantVal(), false, ErrorCode.E0689.toString(), fieldStatusMap);
					} else {
						insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_FREQ_CHECK.getConstantVal(), true, null, fieldStatusMap);
					}
				}
			}

			//			if(isFrequencyValid) {
			if (!StringUtils.isEmpty(reportingEndDateError)) {
				isValidationFailed = true;
				if (reportingEndDateError.equals("0")) {
					insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0651.toString(), fieldStatusMap);
				} else if (reportingEndDateError.equals("1")) {
					insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0799.toString(), fieldStatusMap);
				}
			} else {
				if (isFrequencyValid) {
					if (!StringUtils.isEmpty(startDate)) {
						// end date is valid
						if (!fileDetailsBean.getUploadChannelIdFk().getUploadChannelId().equals(GeneralConstants.SYSTEM_CHANNEL.getConstantLongVal())) {
							Date inputEndDate = DateManip.convertStringToDate(fileDetailsBean.getReportingPeriodEndDateInString(), DDMMYYYY);
							// Compare API end date with the file end date if it is other than API channel
							if (!endDate.equalsIgnoreCase(DateManip.convertDateToString(inputEndDate, dateFormat))) {
								isValidationFailed = true;
								insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0730.toString(), fieldStatusMap);
								return isValidationFailed;
							}
						}
						fileDetailsBean.setReportingPeriodStartDate(DateManip.convertStringToDate(startDate, dateFormat));
						fileDetailsBean.setReportingPeriodEndDate(DateManip.convertStringToDate(endDate, dateFormat));
						insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), true, null, fieldStatusMap);
						insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal(), true, null, fieldStatusMap);
					} else {
						// end date is invalid
						isValidationFailed = true;
						insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0687.toString(), fieldStatusMap);
					}
				} else {
					// end date is invalid
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0687.toString(), fieldStatusMap);
				}
			}
			//			}
		} catch (Exception e) {
			isValidationFailed = true;
			insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0687.toString(), fieldStatusMap);
		}
		//added this check on 26-09-2021 for additional 2 columns in R010 and for additional 1 columns in R219
		if (metaDataBean.isReportTypeCheck()) {
			if (csvInputModel != null && csvInputModel.getDefaultValue() != null) {

				if (org.apache.commons.lang3.StringUtils.isNotBlank(reportType)) {
					if (csvInputModel.getDefaultValue().getReportType().contains(reportType)) {
						insertValueIntoStatusMap(MetaDataCheckConstants.REPORT_TYPE_CHECK.getConstantVal(), true, null, fieldStatusMap);
					} else {
						isValidationFailed = true;
						insertValueIntoStatusMap(MetaDataCheckConstants.REPORT_TYPE_CHECK.getConstantVal(), false, ErrorCode.E1246.toString(), fieldStatusMap);
					}

				} else {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.REPORT_TYPE_CHECK.getConstantVal(), false, ErrorCode.E1250.toString(), fieldStatusMap);
				}
			}
		}
		if (metaDataBean.isVersionNumberCheck()) {
			if (csvInputModel != null && csvInputModel.getDefaultValue() != null) {
				if (org.apache.commons.lang3.StringUtils.isNotBlank(versionNumber)) {
					if (csvInputModel.getDefaultValue().getVersionNumber().contains(versionNumber)) {
						insertValueIntoStatusMap(MetaDataCheckConstants.VERSION_NUMBER_CHECK.getConstantVal(), true, null, fieldStatusMap);
					} else {
						isValidationFailed = true;
						insertValueIntoStatusMap(MetaDataCheckConstants.VERSION_NUMBER_CHECK.getConstantVal(), false, ErrorCode.E1247.toString(), fieldStatusMap);
					}

				} else {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.VERSION_NUMBER_CHECK.getConstantVal(), false, ErrorCode.E1251.toString(), fieldStatusMap);
				}
			}
		}

		if (metaDataBean.isSpecialEconoicZoneCheck()) {

			if (org.apache.commons.lang3.StringUtils.isNotBlank(specialEconomicZone)) {
				insertValueIntoStatusMap(MetaDataCheckConstants.SPECIAL_ECONOMIC_ZONE_CHECK.getConstantVal(), true, null, fieldStatusMap);

			} else {
				isValidationFailed = true;
				insertValueIntoStatusMap(MetaDataCheckConstants.SPECIAL_ECONOMIC_ZONE_CHECK.getConstantVal(), false, ErrorCode.E1248.toString(), fieldStatusMap);
			}
		}

		LOGGER.error("validateCSVMetaDataInternal end");
		return isValidationFailed;
	}

	private boolean validateXMLMetaDataInternal(Map<String, String> outputValueMap, FileDetails fileDetailsBean, Map<String, Map<Boolean, List<String>>> fieldStatusMap, Return returnObj) throws ParseException {
		boolean isValidationFailed = false;
		ObjectMapper mapper = new ObjectMapper();
		FillingInformation fillingInformation = mapper.convertValue(outputValueMap, FillingInformation.class);

		Gson gson = new Gson();
		ReturnMetaDataBean metaDataBean = new ReturnMetaDataBean();

		if (returnObj.getReturnFileFormatMapList() != null) {
			for (ReturnFileFormatMap returnFormatMap : returnObj.getReturnFileFormatMapList()) {
				if (returnFormatMap.isActive()) {
					if (fileDetailsBean.getFileType().equalsIgnoreCase(GeneralConstants.TXT.getConstantVal())) {
						if (returnFormatMap.getFileFormat().getFileFormatId() == 5) {
							metaDataBean = gson.fromJson(returnFormatMap.getMetaDataJson(), ReturnMetaDataBean.class);
						}
					} else if (fileDetailsBean.getFileType().equalsIgnoreCase(GeneralConstants.JSON.getConstantVal())) {
						if (returnFormatMap.getFileFormat().getFileFormatId() == 9) {
							metaDataBean = gson.fromJson(returnFormatMap.getMetaDataJson(), ReturnMetaDataBean.class);
						}
					} else if (fileDetailsBean.getFileType().equalsIgnoreCase(GeneralConstants.XML.getConstantVal())) {
						metaDataBean = gson.fromJson(returnFormatMap.getMetaDataJson(), ReturnMetaDataBean.class);
					}
				}
			}
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DD_SLASH_MM_SLASH_YYYY);
		fileDetailsBean.setFrequency(fillingInformation.getFrequency());
		LOGGER.info("Filing Information : " + JsonUtility.getGsonObject().toJson(fillingInformation));

		if (metaDataBean.isEntityCodeMatchCheck()) {
			// Entity code validation
			if (StringUtils.isEmpty(fillingInformation.getEntityCode())) {
				isValidationFailed = true;
				insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_CODE_MATCH_CHECK.getConstantVal(), false, ErrorCode.E0646.toString(), fieldStatusMap);
			} else {
				if (!fillingInformation.getEntityCode().equalsIgnoreCase(fileDetailsBean.getEntityCode())) {
					LOGGER.error("DB Entity Code : " + fileDetailsBean.getEntityCode());
					LOGGER.error("File Entity Code : " + fillingInformation.getEntityCode());
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_CODE_MATCH_CHECK.getConstantVal(), false, ErrorCode.E0644.toString(), fieldStatusMap);
				} else {
					insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_CODE_MATCH_CHECK.getConstantVal(), true, null, fieldStatusMap);
				}
			}
		}

		if (metaDataBean.isEntityNameMatchCheck()) {
			if (outputValueMap.containsKey(GeneralConstants.ENTITY_NAME.getConstantVal())) {
				if (!StringUtils.isEmpty(fillingInformation.getEntityName())) {
					if (fillingInformation.getEntityName().equalsIgnoreCase(fileDetailsBean.getEntityName())) {
						insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_NAME_MATCH_CHECK.getConstantVal(), true, null, fieldStatusMap);
					} else {
						LOGGER.error("DB Entity Name : " + fileDetailsBean.getEntityName());
						LOGGER.error("File Entity Name : " + fillingInformation.getEntityName());
						isValidationFailed = true;
						insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_NAME_MATCH_CHECK.getConstantVal(), false, ErrorCode.E1545.toString(), fieldStatusMap);
					}
				} else {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_NAME_MATCH_CHECK.getConstantVal(), false, ErrorCode.E0717.toString(), fieldStatusMap);
				}
			}
		}

		if (metaDataBean.isReturnCodeMatchCheck()) {
			// Return code validation
			if (StringUtils.isEmpty(fillingInformation.getReturnCode())) {
				isValidationFailed = true;
				insertValueIntoStatusMap(MetaDataCheckConstants.RETURN_CODE_MATCH_CHECK.getConstantVal(), false, ErrorCode.E0648.toString(), fieldStatusMap);
			} else {
				if (!fillingInformation.getReturnCode().equalsIgnoreCase(fileDetailsBean.getReturnCode())) {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.RETURN_CODE_MATCH_CHECK.getConstantVal(), false, ErrorCode.E0647.toString(), fieldStatusMap);
				} else {
					insertValueIntoStatusMap(MetaDataCheckConstants.RETURN_CODE_MATCH_CHECK.getConstantVal(), true, null, fieldStatusMap);
				}
			}
		}

		boolean isEndDateFormatValid = true;
		boolean isStartDateFormatValid = true;
		if (StringUtils.isEmpty(fillingInformation.getStartDate())) {
			if (metaDataBean.isReportingStartDateCheck() == true) {
				isValidationFailed = true;
				isStartDateFormatValid = false;
				insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal(), false, ErrorCode.E0650.toString(), fieldStatusMap);

			}
		} else if (!StringUtils.isEmpty(fillingInformation.getStartDate())) {
			try {
				if (fillingInformation.getStartDate().split("-")[0].length() != 4) {
					isValidationFailed = true;
					isStartDateFormatValid = false;
					insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal(), false, ErrorCode.E0669.toString(), fieldStatusMap);
				}
			} catch (Exception e) {
				LOGGER.error("Exception occured ", e);
				isValidationFailed = true;
				isStartDateFormatValid = false;
				insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal(), false, ErrorCode.E0669.toString(), fieldStatusMap);
			}
		}

		if (StringUtils.isEmpty(fillingInformation.getEndDate())) {
			isValidationFailed = true;
			isEndDateFormatValid = false;
			insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0651.toString(), fieldStatusMap);
		} else {
			try {
				if (fillingInformation.getEndDate().split("-")[0].length() != 4) {
					isValidationFailed = true;
					isEndDateFormatValid = false;
					insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0799.toString(), fieldStatusMap);
				}
			} catch (Exception e) {
				isValidationFailed = true;
				isEndDateFormatValid = false;
				insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0799.toString(), fieldStatusMap);
				LOGGER.error("Exception while parsing end date : ", e);
			}
		}

		if (returnObj.getReturnPropertyIdFk() != null) {
			if (StringUtils.isEmpty(fillingInformation.getReportStatus())) {
				isValidationFailed = true;
				insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_STATUS_CHECK.getConstantVal(), false, ErrorCode.E0734.toString(), fieldStatusMap);
			} else {
				Integer returnPropertyValId = ReturnPropertyVal.getReturnPropertyIdByXBRLReturnPropertyString(fillingInformation.getReportStatus());
				List<Integer> list = returnObj.getReturnPropertyIdFk().getReturnPropertyValList().stream().map(f -> f.getReturnProprtyValId()).collect(Collectors.toList());
				if (!list.contains(returnPropertyValId)) {
					insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_STATUS_CHECK.getConstantVal(), false, ErrorCode.E0735.toString(), fieldStatusMap);
				} else {
					insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_STATUS_CHECK.getConstantVal(), true, null, fieldStatusMap);
					fileDetailsBean.setReturnPropertyValId(returnPropertyValId);
				}
			}
		}

		try {
			if (!fileDetailsBean.getUploadChannelIdFk().getUploadChannelId().equals(GeneralConstants.SYSTEM_CHANNEL.getConstantLongVal())) {
				Date inputEndDate = DateManip.convertStringToDate(fileDetailsBean.getReportingPeriodEndDateInString(), DDMMYYYY);
				// Compare API end date with the file end date if it is other than API channel
				if (fillingInformation.getEndDate() != null && !fillingInformation.getEndDate().equalsIgnoreCase(DateManip.convertDateToString(inputEndDate, DateConstants.YYYY_MM_DD.getDateConstants()))) {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0730.toString(), fieldStatusMap);
				} else {
					if (isEndDateFormatValid) {
						// Compare calculated start date with start date of csv file
						String calculatedStartDate = getCalculatedStartDate(DateManip.convertStringToDate(fillingInformation.getEndDate(), DateConstants.YYYY_MM_DD.getDateConstants()), returnObj.getFrequency());

						if (StringUtils.isEmpty(calculatedStartDate)) {
							isValidationFailed = true;
							insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0687.toString(), fieldStatusMap);
						} else {
							if (metaDataBean.isReportingStartDateCheck() == true) {
								if (!fillingInformation.getStartDate().equalsIgnoreCase(DateManip.convertDateToString(simpleDateFormat.parse(calculatedStartDate), DateConstants.YYYY_MM_DD.getDateConstants()))) {
									isValidationFailed = true;
									// Invalid reporting start date
									insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal(), false, ErrorCode.E0731.toString(), fieldStatusMap);
								} else {
									fileDetailsBean.setReportingPeriodStartDate(DateManip.convertStringToDate(fillingInformation.getStartDate(), DateConstants.YYYY_MM_DD.getDateConstants()));
									insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal(), true, null, fieldStatusMap);
									insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), true, null, fieldStatusMap);
									fileDetailsBean.setReportingPeriodEndDate(DateManip.convertStringToDate(fillingInformation.getEndDate(), DateConstants.YYYY_MM_DD.getDateConstants()));
								}
							} else {
								insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), true, null, fieldStatusMap);
								fileDetailsBean.setReportingPeriodEndDate(DateManip.convertStringToDate(fillingInformation.getEndDate(), DateConstants.YYYY_MM_DD.getDateConstants()));
								if (isStartDateFormatValid) {
									if (fillingInformation.getStartDate() == null) {
										fileDetailsBean.setReportingPeriodStartDate(simpleDateFormat.parse(calculatedStartDate));
									} else {
										fileDetailsBean.setReportingPeriodStartDate(DateManip.convertStringToDate(fillingInformation.getStartDate(), DateConstants.YYYY_MM_DD.getDateConstants()));
									}
									// commented because start date check is disabled
									// insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal(),true, null, fieldStatusMap);
								}
							}
						}
					}
				}
			} else {
				if (isEndDateFormatValid) {
					// Compare calculated start date with start date of csv file
					String calculatedStartDate = getCalculatedStartDate(DateManip.convertStringToDate(fillingInformation.getEndDate(), DateConstants.YYYY_MM_DD.getDateConstants()), returnObj.getFrequency());

					if (StringUtils.isEmpty(calculatedStartDate)) {
						isValidationFailed = true;
						insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0687.toString(), fieldStatusMap);
					} else {
						// reporting end date valid
						if (metaDataBean.isReportingStartDateCheck() == true) {
							if (!fillingInformation.getStartDate().equalsIgnoreCase(DateManip.convertDateToString(simpleDateFormat.parse(calculatedStartDate), DateConstants.YYYY_MM_DD.getDateConstants()))) {
								isValidationFailed = true;
								// Invalid reporting start date
								insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal(), false, ErrorCode.E0731.toString(), fieldStatusMap);
							} else {
								fileDetailsBean.setReportingPeriodStartDate(DateManip.convertStringToDate(fillingInformation.getStartDate(), DateConstants.YYYY_MM_DD.getDateConstants()));
								insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal(), true, null, fieldStatusMap);
								insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), true, null, fieldStatusMap);
								fileDetailsBean.setReportingPeriodEndDate(DateManip.convertStringToDate(fillingInformation.getEndDate(), DateConstants.YYYY_MM_DD.getDateConstants()));
							}
						} else {
							insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), true, null, fieldStatusMap);
							fileDetailsBean.setReportingPeriodEndDate(DateManip.convertStringToDate(fillingInformation.getEndDate(), DateConstants.YYYY_MM_DD.getDateConstants()));
							if (isStartDateFormatValid) {
								if (fillingInformation.getStartDate() == null) {
									fileDetailsBean.setReportingPeriodStartDate(simpleDateFormat.parse(calculatedStartDate));
								} else {
									fileDetailsBean.setReportingPeriodStartDate(DateManip.convertStringToDate(fillingInformation.getStartDate(), DateConstants.YYYY_MM_DD.getDateConstants()));
								}
							}
							// commented because start date check is disabled
							//insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal(),true, null, fieldStatusMap);
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception : ", e);
			isValidationFailed = true;
			insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0687.toString(), fieldStatusMap);
		}

		return isValidationFailed;
	}

	private boolean validateJSONMetaDataInternal(DocumentInfo documentInfo, FileDetails fileDetailsBean, Map<String, Map<Boolean, List<String>>> fieldStatusMap, Return returnObj) throws ParseException {
		boolean isValidationFailed = false;
		//SimpleDateFormat simpleDateFormatss = new SimpleDateFormat(YYYY_DASH_MM_DASH_DD);
		SimpleDateFormat simpleDateFormatDDMMYYY = new SimpleDateFormat(DD_SLASH_MM_SLASH_YYYY);
		fileDetailsBean.setFrequency(documentInfo.getFilingDetails().getReportingFreq());

		Gson gson = new Gson();
		ReturnMetaDataBean metaDataBean = new ReturnMetaDataBean();

		if (returnObj.getReturnFileFormatMapList() != null) {
			for (ReturnFileFormatMap returnFormatMap : returnObj.getReturnFileFormatMapList()) {
				if (returnFormatMap.isActive()) {
					if (fileDetailsBean.getFileType().equalsIgnoreCase(GeneralConstants.TXT.getConstantVal())) {
						if (returnFormatMap.getFileFormat().getFileFormatId() == 5) {
							metaDataBean = gson.fromJson(returnFormatMap.getMetaDataJson(), ReturnMetaDataBean.class);
						}
					} else if (fileDetailsBean.getFileType().equalsIgnoreCase(GeneralConstants.JSON.getConstantVal())) {
						if (returnFormatMap.getFileFormat().getFileFormatId() == 9) {
							metaDataBean = gson.fromJson(returnFormatMap.getMetaDataJson(), ReturnMetaDataBean.class);
						}
					} else if (fileDetailsBean.getFileType().equalsIgnoreCase(GeneralConstants.XML.getConstantVal())) {
						metaDataBean = gson.fromJson(returnFormatMap.getMetaDataJson(), ReturnMetaDataBean.class);
					}
				}
			}
		}

		if (metaDataBean.isEntityNameMatchCheck()) {
			if (!StringUtils.isEmpty(documentInfo.getFilingDetails().getEntityName())) {
				if (documentInfo.getFilingDetails().getEntityName().equalsIgnoreCase(fileDetailsBean.getEntityName())) {
					insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_NAME_MATCH_CHECK.getConstantVal(), true, null, fieldStatusMap);
				} else {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_NAME_MATCH_CHECK.getConstantVal(), false, ErrorCode.E1545.toString(), fieldStatusMap);
				}
			} else {
				isValidationFailed = true;
				insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_NAME_MATCH_CHECK.getConstantVal(), false, ErrorCode.E0717.toString(), fieldStatusMap);
			}
		}

		if (metaDataBean.isEntityCodeMatchCheck()) {
			// Validation Part
			if (StringUtils.isEmpty(documentInfo.getFilingDetails().getEntityCode())) {
				isValidationFailed = true;
				insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_CODE_MATCH_CHECK.getConstantVal(), false, ErrorCode.E0646.toString(), fieldStatusMap);
			} else {
				if (!documentInfo.getFilingDetails().getEntityCode().equalsIgnoreCase(fileDetailsBean.getEntityCode())) {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_CODE_MATCH_CHECK.getConstantVal(), false, ErrorCode.E0644.toString(), fieldStatusMap);
				} else {
					insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_CODE_MATCH_CHECK.getConstantVal(), true, null, fieldStatusMap);
				}
			}
		}

		if (metaDataBean.isReturnCodeMatchCheck()) {
			if (StringUtils.isEmpty(documentInfo.getFilingDetails().getReturnCode())) {
				isValidationFailed = true;
				insertValueIntoStatusMap(MetaDataCheckConstants.RETURN_CODE_MATCH_CHECK.getConstantVal(), false, ErrorCode.E0648.toString(), fieldStatusMap);
			} else {
				if (!documentInfo.getFilingDetails().getReturnCode().equalsIgnoreCase(fileDetailsBean.getReturnCode())) {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.RETURN_CODE_MATCH_CHECK.getConstantVal(), false, ErrorCode.E0647.toString(), fieldStatusMap);
				} else {
					insertValueIntoStatusMap(MetaDataCheckConstants.RETURN_CODE_MATCH_CHECK.getConstantVal(), true, null, fieldStatusMap);
				}
			}
		}

		boolean isEndDateFormatValid = true;
		boolean isStartDateFormatValid = true;
		if (StringUtils.isEmpty(documentInfo.getFilingDetails().getReportingStartDate())) {
			isValidationFailed = true;
			isStartDateFormatValid = false;
			insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal(), false, ErrorCode.E0650.toString(), fieldStatusMap);
		} else {
			try {
				if (documentInfo.getFilingDetails().getReportingStartDate().split("-")[0].length() != 4) {
					isValidationFailed = true;
					isStartDateFormatValid = false;
					insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal(), false, ErrorCode.E0669.toString(), fieldStatusMap);
				}
			} catch (Exception e) {
				isValidationFailed = true;
				isStartDateFormatValid = false;
				LOGGER.error("Date Format not supported Exception :", e);
				insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal(), false, ErrorCode.E0669.toString(), fieldStatusMap);
			}
		}

		if (StringUtils.isEmpty(documentInfo.getFilingDetails().getReportingEndDate())) {
			isValidationFailed = true;
			isEndDateFormatValid = false;
			insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0651.toString(), fieldStatusMap);
		} else {
			try {
				if (documentInfo.getFilingDetails().getReportingEndDate().split("-")[0].length() != 4) {
					isValidationFailed = true;
					isEndDateFormatValid = false;
					insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0799.toString(), fieldStatusMap);
				}
			} catch (Exception e) {
				isValidationFailed = true;
				isEndDateFormatValid = false;
				LOGGER.error("Date Format not supported Exception :", e);
				insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0799.toString(), fieldStatusMap);
			}
		}

		if (returnObj.getReturnPropertyIdFk() != null) {
			if (StringUtils.isEmpty(documentInfo.getFilingDetails().getReturnProp())) {
				isValidationFailed = true;
				insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_STATUS_CHECK.getConstantVal(), false, ErrorCode.E0734.toString(), fieldStatusMap);
			} else {
				Integer returnPropertyValId = ReturnPropertyVal.getReturnPropertyIdByXBRLReturnPropertyString(documentInfo.getFilingDetails().getReturnProp());
				List<Integer> list = returnObj.getReturnPropertyIdFk().getReturnPropertyValList().stream().map(f -> f.getReturnProprtyValId()).collect(Collectors.toList());
				if (!list.contains(returnPropertyValId)) {
					insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_STATUS_CHECK.getConstantVal(), false, ErrorCode.E0735.toString(), fieldStatusMap);
				} else {
					insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_STATUS_CHECK.getConstantVal(), true, null, fieldStatusMap);
					fileDetailsBean.setReturnPropertyValId(returnPropertyValId);
				}
			}
		}

		try {
			if (isEndDateFormatValid && isStartDateFormatValid) {
				Date fillingInformationEndDateObj = DateManip.convertStringToDate(documentInfo.getFilingDetails().getReportingEndDate(), YYYY_DASH_MM_DASH_DD);

				if (!fileDetailsBean.getUploadChannelIdFk().getUploadChannelId().equals(GeneralConstants.SYSTEM_CHANNEL.getConstantLongVal())) {
					Date inputEndDate = DateManip.convertStringToDate(fileDetailsBean.getReportingPeriodEndDateInString(), DDMMYYYY);

					// Compare API end date with the file end date if it is other than API channel
					if (!documentInfo.getFilingDetails().getReportingEndDate().equalsIgnoreCase(DateManip.convertDateToString(inputEndDate, YYYY_DASH_MM_DASH_DD))) {
						isValidationFailed = true;
						insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0730.toString(), fieldStatusMap);
					} else {
						insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), true, null, fieldStatusMap);
						fileDetailsBean.setReportingPeriodEndDate(inputEndDate);

						// calculate start date using end date and compare with instance start date
						String calculatedStartDate = getCalculatedStartDate(fillingInformationEndDateObj, returnObj.getFrequency());
						calculatedStartDate = DateManip.convertDateToString(simpleDateFormatDDMMYYY.parse(calculatedStartDate), YYYY_DASH_MM_DASH_DD);
						if (!documentInfo.getFilingDetails().getReportingStartDate().equalsIgnoreCase(calculatedStartDate)) {
							isValidationFailed = true;
							insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal(), false, ErrorCode.E0730.toString(), fieldStatusMap);
						} else {
							fileDetailsBean.setReportingPeriodStartDate(DateManip.convertStringToDate(documentInfo.getFilingDetails().getReportingStartDate(), YYYY_DASH_MM_DASH_DD));
							insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal(), true, null, fieldStatusMap);
						}
					}
				} else {
					insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), true, null, fieldStatusMap);
					fileDetailsBean.setReportingPeriodEndDate(DateManip.convertStringToDate(documentInfo.getFilingDetails().getReportingEndDate(), YYYY_DASH_MM_DASH_DD));

					// Compare calculated start date with start date of csv file
					String calculatedStartDate = getCalculatedStartDate(fillingInformationEndDateObj, returnObj.getFrequency());
					if (!documentInfo.getFilingDetails().getReportingStartDate().equalsIgnoreCase(calculatedStartDate)) {
						isValidationFailed = true;
						insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal(), false, ErrorCode.E0730.toString(), fieldStatusMap);
					} else {
						fileDetailsBean.setReportingPeriodStartDate(DateManip.convertStringToDate(documentInfo.getFilingDetails().getReportingStartDate(), YYYY_DASH_MM_DASH_DD));
						insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal(), true, null, fieldStatusMap);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception : ", e);
			isValidationFailed = true;
			insertValueIntoStatusMap(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal(), false, ErrorCode.E0687.toString(), fieldStatusMap);
		}

		return isValidationFailed;
	}

	public String getCalculatedStartDate(Date endDate, Frequency frequency) throws ParseException {
		String frequencyCode = FrequencyEnum.getCodeByName(frequency.getFrequencyName());
		com.iris.util.DateUtilsParser.Frequency fequencyEnum = com.iris.util.DateUtilsParser.Frequency.getEnumByfreqPeriod(frequencyCode);
		String startDate = "";
		if (frequency.getFrequencyId().equals(RevisionRequestConstants.FREQ_ID_FIRST_HALF_MONTHLY_EXCLUDE_HOLIDAY.getConstantLongVal())) {
			DateAndTimeArithmetic.setWEEKEND_LIST(Arrays.asList(Calendar.SATURDAY, Calendar.SUNDAY));
			ServiceResponse serviceResponse = holidayController.fetchActiveHolidayForYear(UUID.randomUUID().toString(), DateManip.convertDateToString(endDate, DateConstants.YYYY.getDateConstants()));
			if (serviceResponse.isStatus()) {
				@SuppressWarnings("unchecked")
				List<String> holidayList = (List<String>) serviceResponse.getResponse();
				List<Date> holidayListDate = new ArrayList<>();
				holidayList.forEach(f -> {
					try {
						holidayListDate.add(DateManip.convertStringToDate(f, DateConstants.DD_MM_YYYY.getDateConstants()));
					} catch (ParseException e) {
						LOGGER.error("Exception while parsing date : ", e);
					}
				});
				DateAndTimeArithmetic.setHOLIDAY_LIST(holidayListDate);
				startDate = DateAndTimeArithmetic.getDate(endDate, true, fequencyEnum);
			}
		} else {
			startDate = DateAndTimeArithmetic.getDate(endDate, false, fequencyEnum);
		}
		LOGGER.info("Calculated Start Date : " + startDate + " End date : " + endDate + " Frequency : " + fequencyEnum);
		return startDate;
	}

	private FillingInformation getActualFillingInfo(FillingInformation fillingInformation, FileDetails fileDetailsBean) {
		Gson gson = new Gson();
		if (fileDetailsBean.getReturnCode().equalsIgnoreCase(GeneralConstants.R012.getConstantVal())) { // NRD CSR
			// set start date and end Date
			XMLTagBean xmlTagBean = gson.fromJson(fillingInformation.getEndDate(), XMLTagBean.class);
			fillingInformation.setStartDate(xmlTagBean.getContext().getPeriod().getStartDate());
			fillingInformation.setEndDate(xmlTagBean.getContext().getPeriod().getEndDate());

			// set Entity Code
			xmlTagBean = gson.fromJson(fillingInformation.getEntityCode(), XMLTagBean.class);
			fillingInformation.setEntityCode(xmlTagBean.getTagValue());

			// set Entity Code
			xmlTagBean = gson.fromJson(fillingInformation.getReportStatus(), XMLTagBean.class);
			fillingInformation.setReportStatus(xmlTagBean.getTagValue());

			// set same entity code as NRD CSR does not contain return code
			fillingInformation.setReturnCode(fileDetailsBean.getReturnCode());
		} else if (fileDetailsBean.getReturnCode().equalsIgnoreCase(GeneralConstants.R009.getConstantVal())) { // GPB
			// set start date and end Date
			XMLTagBean xmlTagBean = gson.fromJson(fillingInformation.getReturnVersion(), XMLTagBean.class);
			if (xmlTagBean.getContext().getPeriod().isInstant()) {
				fillingInformation.setStartDate(xmlTagBean.getContext().getPeriod().getEndDate());
				fillingInformation.setEndDate(xmlTagBean.getContext().getPeriod().getEndDate());
			}

			// set Entity Code
			fillingInformation.setEntityCode(fileDetailsBean.getEntityCode());
			fillingInformation.setReturnCode(fileDetailsBean.getReturnCode());
		}

		return fillingInformation;
	}

	private void insertValueIntoStatusMap(String key, Boolean b, String errorString, Map<String, Map<Boolean, List<String>>> fieldCheckListMap) {
		if (fieldCheckListMap.get(key) != null) {
			Map<Boolean, List<String>> map = fieldCheckListMap.get(key);
			if (map.get(b) != null) {
				map.get(b).add(errorString);
			} else {
				List<String> errorList = new ArrayList<>();
				errorList.add(errorString);
				map.put(b, errorList);
			}
		} else {
			List<String> errorList = new ArrayList<>();
			errorList.add(errorString);
			Map<Boolean, List<String>> map = new LinkedHashMap<>();
			map.put(b, errorList);
			fieldCheckListMap.put(key, map);
		}
	}
}
