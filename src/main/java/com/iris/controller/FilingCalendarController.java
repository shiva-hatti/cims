package com.iris.controller;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iris.caching.ObjectCache;
import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.FilingCalendarInfo;
import com.iris.dto.FillingEndDatesBean;
import com.iris.dto.ReturnFileFormatMapDto;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ServiceException;
import com.iris.model.EntityBean;
import com.iris.model.FilingCalendar;
import com.iris.model.FilingCalendarModificationHistory;
import com.iris.model.FinYrFormat;
import com.iris.model.Frequency;
import com.iris.model.Return;
import com.iris.model.ReturnPropertyValue;
import com.iris.model.UserMaster;
import com.iris.service.FilingCalendarModifiedService;
import com.iris.service.impl.FilingCalModHistoryService;
import com.iris.service.impl.FilingCalendarService;
import com.iris.service.impl.ReturnService;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.RevisionRequestConstants;

@RestController
@RequestMapping("/service/filingCalendar")
public class FilingCalendarController {

	static final Logger logger = LogManager.getLogger(FilingCalendarController.class);

	@Autowired
	private FilingCalendarService filingCalendarService;
	@Autowired
	private ReturnService returnService;
	@Autowired
	private FilingCalendarModifiedService filingCalendarModifiedService;
	@Autowired
	FileUploadController fileUploadController;
	@Autowired
	private FilingCalModHistoryService filingCalModHistoryService;

	private FilingCalendar filingCalendarBean = new FilingCalendar();

	@GetMapping(value = "/getAllFrequencyList")
	public ServiceResponse getAllFrequencyList(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		logger.info("Request received to get frequency list for processing id", jobProcessId);
		try {
			List<Frequency> fruequencyList = filingCalendarService.getActiveDataFor(Frequency.class, null);

			Frequency fre;
			List<Frequency> freqList = new ArrayList<>();
			for (Frequency frequency : fruequencyList) {
				fre = new Frequency();
				fre.setFrequencyId(frequency.getFrequencyId());
				fre.setFrequencyName(frequency.getFrequencyName());
				fre.setFrequencyCode(frequency.getFrequencyCode());
				freqList.add(fre);
			}

			if (CollectionUtils.isEmpty(freqList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0688.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0688.toString())).build();
			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(freqList).build();
		} catch (Exception e) {
			logger.error("Exception occoured while fatching frequency list data for processing id", jobProcessId + "Exception is " + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0805.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0805.toString())).build();
		}
	}

	@PostMapping(value = "/saveFilingCalendar")
	public ServiceResponse saveFilingCalendar(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody FilingCalendarInfo filingCalendarInfo) {
		logger.info("Requseting saveFilingCalendar " + jobProcessId);
		boolean status = false;
		Date currentTimeStamp = new Date(System.currentTimeMillis());
		String modHistoryJson = null;
		ServiceResponse response;
		try {
			FilingCalendar filingCalendar = new FilingCalendar();
			if (filingCalendarInfo.getReturnPropertyVal() == null)
				filingCalendarBean = filingCalendarService.checkFilingInfoWithoutPropVal(filingCalendarInfo.getReturnId());
			else
				filingCalendarBean = filingCalendarService.checkFilingInfo(filingCalendarInfo.getReturnId(), filingCalendarInfo.getReturnPropertyVal().getReturnProprtyValId());

			if (filingCalendarBean == null) {
				if (filingCalendarInfo.getReturnPropertyVal() != null) {
					ReturnPropertyValue returnPropertyValue = new ReturnPropertyValue();
					returnPropertyValue.setReturnProprtyValId(filingCalendarInfo.getReturnPropertyVal().getReturnProprtyValId());
					filingCalendar.setReturnPropertyVal(returnPropertyValue);
				}
				filingCalendar.setReturnId(filingCalendarInfo.getReturnId());
				filingCalendar.setReturnFrequencyId(filingCalendarInfo.getReturnFrequencyId());
				filingCalendar.setFilingWindowExtensionStart(filingCalendarInfo.getFilingWindowExtensionStart());
				filingCalendar.setEmailNotificationDays(filingCalendarInfo.getEmailNotificationDays());
				filingCalendar.setIncludeHoliday(filingCalendarInfo.getIncludeHoliday());
				filingCalendar.setIncludeWeekend(filingCalendarInfo.getIncludeWeekend());
				filingCalendar.setSendMail(filingCalendarInfo.getSendMail());
				filingCalendar.setCreatedBy(filingCalendarInfo.getCreatedBy());
				filingCalendar.setCreatedOn(currentTimeStamp);
				filingCalendar.setIsActive(true);
				filingCalendar.setGraceDays(filingCalendarInfo.getGraceDays());
				filingCalendar.setIsApplicable(filingCalendarInfo.getIsApplicable());
				status = filingCalendarService.saveFilingCal(filingCalendar);
			} else {
				if (filingCalendarInfo.getReturnPropertyVal() == null) {
					if (filingCalendarInfo.getReturnFrequencyId().equals(filingCalendarBean.getReturnFrequencyId()) && filingCalendarInfo.getReturnId().toString().trim().equals(filingCalendarBean.getReturnId().toString().trim()) && filingCalendarInfo.getFilingWindowExtensionStart().toString().trim().equals(filingCalendarBean.getFilingWindowExtensionStart().toString().trim()) && filingCalendarInfo.getEmailNotificationDays().toString().trim().equals(filingCalendarBean.getEmailNotificationDays().toString().trim()) && filingCalendarInfo.getIncludeHoliday().equals(filingCalendarBean.getIncludeHoliday()) && filingCalendarInfo.getIncludeWeekend().equals(filingCalendarBean.getIncludeWeekend()) && filingCalendarInfo.getIsApplicable().equals(filingCalendarBean.getIsApplicable()) && filingCalendarInfo.getGraceDays().equals(filingCalendarBean.getGraceDays())) {
						status = false;
					} else {
						if (filingCalendarInfo.getReturnPropertyVal() != null) {
							ReturnPropertyValue returnPropertyValue = new ReturnPropertyValue();
							returnPropertyValue.setReturnProprtyValId(filingCalendarInfo.getReturnPropertyVal().getReturnProprtyValId());
							filingCalendar.setReturnPropertyVal(returnPropertyValue);
						}
						filingCalendar.setReturnId(filingCalendarInfo.getReturnId());
						filingCalendar.setReturnFrequencyId(filingCalendarInfo.getReturnFrequencyId());
						filingCalendar.setFilingWindowExtensionStart(filingCalendarInfo.getFilingWindowExtensionStart());
						filingCalendar.setEmailNotificationDays(filingCalendarInfo.getEmailNotificationDays());
						filingCalendar.setIncludeHoliday(filingCalendarInfo.getIncludeHoliday());
						filingCalendar.setIncludeWeekend(filingCalendarInfo.getIncludeWeekend());
						filingCalendar.setSendMail(filingCalendarInfo.getSendMail());
						filingCalendar.setLastModifiedOn(currentTimeStamp);
						filingCalendar.setUserModify(filingCalendarInfo.getCreatedBy());
						filingCalendar.setUpdateFromReturn(false);
						filingCalendar.setGraceDays(filingCalendarInfo.getGraceDays());
						filingCalendar.setIsApplicable(filingCalendarInfo.getIsApplicable());
						status = filingCalendarService.updateFilingCal(filingCalendar);
					}
				} else {
					if (filingCalendarInfo.getReturnFrequencyId().equals(filingCalendarBean.getReturnFrequencyId()) && filingCalendarInfo.getReturnId().toString().trim().equals(filingCalendarBean.getReturnId().toString().trim()) && filingCalendarInfo.getFilingWindowExtensionStart().toString().trim().equals(filingCalendarBean.getFilingWindowExtensionStart().toString().trim()) && filingCalendarInfo.getEmailNotificationDays().toString().trim().equals(filingCalendarBean.getEmailNotificationDays().toString().trim()) && filingCalendarInfo.getIncludeHoliday().equals(filingCalendarBean.getIncludeHoliday()) && filingCalendarInfo.getIncludeWeekend().equals(filingCalendarBean.getIncludeWeekend()) && filingCalendarInfo.getReturnPropertyVal().getReturnProprtyValId().equals(filingCalendarBean.getReturnPropertyVal().getReturnProprtyValId()) && filingCalendarInfo.getIsApplicable().equals(filingCalendarBean.getIsApplicable()) && filingCalendarInfo.getGraceDays().equals(filingCalendarBean.getGraceDays())) {
						status = false;
					} else {
						if (filingCalendarInfo.getReturnPropertyVal() != null) {
							ReturnPropertyValue returnPropertyValue = new ReturnPropertyValue();
							returnPropertyValue.setReturnProprtyValId(filingCalendarInfo.getReturnPropertyVal().getReturnProprtyValId());
							filingCalendar.setReturnPropertyVal(returnPropertyValue);
						}
						filingCalendar.setReturnId(filingCalendarInfo.getReturnId());
						filingCalendar.setReturnFrequencyId(filingCalendarInfo.getReturnFrequencyId());
						filingCalendar.setFilingWindowExtensionStart(filingCalendarInfo.getFilingWindowExtensionStart());
						filingCalendar.setEmailNotificationDays(filingCalendarInfo.getEmailNotificationDays());
						filingCalendar.setIncludeHoliday(filingCalendarInfo.getIncludeHoliday());
						filingCalendar.setIncludeWeekend(filingCalendarInfo.getIncludeWeekend());
						filingCalendar.setSendMail(filingCalendarInfo.getSendMail());
						filingCalendar.setLastModifiedOn(currentTimeStamp);
						filingCalendar.setUserModify(filingCalendarInfo.getCreatedBy());
						filingCalendar.setUpdateFromReturn(false);
						filingCalendar.setGraceDays(filingCalendarInfo.getGraceDays());
						filingCalendar.setIsApplicable(filingCalendarInfo.getIsApplicable());
						status = filingCalendarService.updateFilingCal(filingCalendar);
					}
				}
				if (status) {
					FilingCalendarInfo filingCalendarInfoObj = new FilingCalendarInfo();
					if (filingCalendarBean.getReturnPropertyVal() != null) {
						ReturnPropertyValue returnPropertyValue = new ReturnPropertyValue();
						returnPropertyValue.setReturnProprtyValId(filingCalendarInfo.getReturnPropertyVal().getReturnProprtyValId());
						filingCalendarInfoObj.setReturnPropertyVal(returnPropertyValue);
					}
					filingCalendarInfoObj.setFilingCalendarId(filingCalendarBean.getFilingCalendarId());
					filingCalendarInfoObj.setReturnId(filingCalendarBean.getReturnId());
					filingCalendarInfoObj.setReturnFrequencyId(filingCalendarBean.getReturnFrequencyId());
					filingCalendarInfoObj.setFilingWindowExtensionStart(filingCalendarBean.getFilingWindowExtensionStart());
					filingCalendarInfoObj.setEmailNotificationDays(filingCalendarBean.getEmailNotificationDays());
					filingCalendarInfoObj.setIncludeHoliday(filingCalendarBean.getIncludeHoliday());
					filingCalendarInfoObj.setIncludeWeekend(filingCalendarBean.getIncludeWeekend());
					filingCalendarInfoObj.setGraceDays(filingCalendarBean.getGraceDays());
					filingCalendarInfoObj.setIsApplicable(filingCalendarBean.getIsApplicable());
					filingCalendarInfoObj.setFrequencyName(filingCalendarInfo.getFrequencyName());
					modHistoryJson = new Gson().toJson(filingCalendarInfoObj);
					FilingCalendarModificationHistory filingCalendarModificationHistory = new FilingCalendarModificationHistory();
					FilingCalendar filingCalendarObj = new FilingCalendar();
					filingCalendarObj.setFilingCalendarId(filingCalendarBean.getFilingCalendarId());
					filingCalendarModificationHistory.setFilingModJson(modHistoryJson);
					filingCalendarModificationHistory.setFilingCalendarFk(filingCalendarObj);
					if (filingCalendarBean.getUserModify() != null) {
						filingCalendarModificationHistory.setUserModify(filingCalendarBean.getUserModify());
					} else {
						filingCalendarModificationHistory.setUserModify(filingCalendarBean.getCreatedBy());
					}

					if (filingCalendarBean.getLastModifiedOn() == null) {
						filingCalendarModificationHistory.setModifiedOn(filingCalendarBean.getCreatedOn());
					} else {
						filingCalendarModificationHistory.setModifiedOn(filingCalendarBean.getLastModifiedOn());
					}
					filingCalModHistoryService.saveFilingCalendarModificationHistory(filingCalendarModificationHistory);
				}
			}

			logger.info("Requset saveFilingCalendar complete" + jobProcessId);
		} catch (Exception e) {
			logger.error("Exception occoured while saveFilingCalendar data for processing id", jobProcessId + "Exception is " + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusMessage("Exception occoured while saveFilingCalendar data").build();
		}
		if (status) {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(status).setStatusMessage("ADD").build();
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(status).setStatusMessage("EDIT").build();
		}
		response.setResponse(status);
		return response;
	}

	@PostMapping(value = "/fetchFilingCalendarInfo")
	public ServiceResponse fetchFilingCalendarInfo(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody FilingCalendarInfo filingCalendarInfoObj) {
		logger.info("Requseting fetchFilingCalendarInfo " + jobProcessId);
		try {
			List<FilingCalendar> fetchCalendarInfo = new ArrayList<>();
			if (filingCalendarInfoObj != null) {
				FilingCalendar filingCalendar = new FilingCalendar();
				if (filingCalendarInfoObj.getReturnPropertyVal() != null) {
					ReturnPropertyValue returnPropertyValue = new ReturnPropertyValue();
					returnPropertyValue.setReturnProprtyValId(filingCalendarInfoObj.getReturnPropertyVal().getReturnProprtyValId());
					filingCalendar.setReturnPropertyVal(returnPropertyValue);
				}
				filingCalendar.setReturnId(filingCalendarInfoObj.getReturnId());
				filingCalendar.setReturnFrequencyId(filingCalendarInfoObj.getReturnFrequencyId());
				filingCalendar.setFilingWindowExtensionStart(filingCalendarInfoObj.getFilingWindowExtensionStart());
				fetchCalendarInfo = filingCalendarService.getFilingCalendarData(filingCalendar);
			}

			List<FilingCalendar> filingCalendarList = new ArrayList<>();

			for (FilingCalendar filingCalendarTemp : fetchCalendarInfo) {
				filingCalendarBean = new FilingCalendar();
				filingCalendarBean.setFilingWindowExtensionStart(filingCalendarTemp.getFilingWindowExtensionStart());
				filingCalendarBean.setEmailNotificationDays(filingCalendarTemp.getEmailNotificationDays());
				filingCalendarBean.setSendMail(filingCalendarTemp.getSendMail());
				filingCalendarBean.setIncludeHoliday(filingCalendarTemp.getIncludeHoliday());
				filingCalendarBean.setIncludeWeekend(filingCalendarTemp.getIncludeWeekend());
				filingCalendarBean.setIsApplicable(filingCalendarTemp.getIsApplicable());
				filingCalendarBean.setGraceDays(filingCalendarTemp.getGraceDays());
				UserMaster userMaster = new UserMaster();
				if (filingCalendarTemp.getUserModify() != null) {
					userMaster.setUserName(filingCalendarTemp.getUserModify().getUserName());
				} else {
					userMaster.setUserName(filingCalendarTemp.getCreatedBy().getUserName());
				}
				filingCalendarBean.setUserModify(userMaster);
				if (filingCalendarTemp.getLastModifiedOn() != null) {
					filingCalendarBean.setLastModifiedOn(filingCalendarTemp.getLastModifiedOn());
				} else {
					filingCalendarBean.setLastModifiedOn(filingCalendarTemp.getCreatedOn());
				}
				filingCalendarList.add(filingCalendarBean);
			}
			if (CollectionUtils.isEmpty(filingCalendarList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusMessage("There is no FilingCalendarInfo List").build();
			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(filingCalendarList).build();
		} catch (Exception e) {
			logger.error("Exception occoured while fatching data for processing id", jobProcessId + "Exception is " + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusMessage("Exception occoured while FilingCalendarInfo").build();
		}
	}

	@PostMapping(value = "/viewFilingCalendarInfo")
	public ServiceResponse viewFilingCalendarInfo(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody List<String> viewCalFilingList) {
		logger.info("Requseting viewFilingCalendarInfo " + jobProcessId);
		List<String> outputList = new ArrayList<>();
		Integer propertyId = 0;
		String notificationEndDate = "";
		String reportingStartDate = "";
		try {
			Long formFreqId = Long.parseLong(String.valueOf(viewCalFilingList.get(0)));
			Long returnIdAjax = Long.parseLong(String.valueOf(viewCalFilingList.get(1)));
			if (viewCalFilingList.get(2) == null)
				propertyId = null;
			else
				propertyId = Integer.parseInt(viewCalFilingList.get(2));
			filingCalendarBean = filingCalendarService.getViewCalInfo(formFreqId, returnIdAjax, propertyId);
			Return returnName = returnService.getDataById(returnIdAjax);
			Frequency fruequency = filingCalendarService.getFrequencyName(formFreqId);
			FillingEndDatesBean fillingEndDatesBeanObj = new FillingEndDatesBean();
			reportingStartDate = fileUploadController.getCalculatedStartDate(DateManip.convertStringToDate(viewCalFilingList.get(3), viewCalFilingList.get(5)), fruequency);
			if (!reportingStartDate.equals(""))
				fillingEndDatesBeanObj = getStartEndDate(viewCalFilingList.get(4), formFreqId, returnName, propertyId, jobProcessId);

			if (fillingEndDatesBeanObj.getStartDate() != null) {
				outputList.add(DateManip.formatDate(fillingEndDatesBeanObj.getStartDate(), DateConstants.DD_MM_YYYY.getDateConstants(), viewCalFilingList.get(5)));
				outputList.add(DateManip.formatDate(fillingEndDatesBeanObj.getEndDate(), DateConstants.DD_MM_YYYY.getDateConstants(), viewCalFilingList.get(5)));
				outputList.add(fruequency.getFrequencyName());
				if (viewCalFilingList.get(2) == null)
					outputList.add(null);
				else
					outputList.add(filingCalendarBean.getReturnPropertyVal().getReturnProValue());
				outputList.add(returnName.getReturnName());
				outputList.add(filingCalendarBean.getIncludeHoliday().toString());
				outputList.add(filingCalendarBean.getIncludeWeekend().toString());
				if (filingCalendarBean.getEmailNotificationDays() == null) {
					outputList.add(notificationEndDate);
				} else {
					notificationEndDate = getWindowEndDate(fillingEndDatesBeanObj.getStartDate(), (int) (long) filingCalendarBean.getEmailNotificationDays(), (filingCalendarBean.getFilingWindowExtensionStart() + filingCalendarBean.getGraceDays()));
					outputList.add(DateManip.formatDate(notificationEndDate, DateConstants.DD_MM_YYYY.getDateConstants(), viewCalFilingList.get(5)));
				}
				outputList.add(filingCalendarBean.getSendMail());
				outputList.add(DateManip.formatDate(getFormattedDate(reportingStartDate), DateConstants.DD_MM_YYYY.getDateConstants(), viewCalFilingList.get(5)));
				outputList.add(DateManip.formatDate(fillingEndDatesBeanObj.getGraceDaysDate(), DateConstants.DD_MM_YYYY.getDateConstants(), viewCalFilingList.get(5)));
			}

			if (CollectionUtils.isEmpty(outputList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusMessage("There is no FilingCalendarInfo List").build();
			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(outputList).build();
		} catch (Exception e) {
			logger.error("Exception occoured while viewFilingCalendarInfo data for processing id", jobProcessId + "Exception is " + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusMessage("Exception occoured while FilingCalendarInfo").build();
		}
	}

	private FillingEndDatesBean getStartEndDate(String endDate, Long formFreqId, Return returnName, Integer propertyId, String jobProcessId) {
		FillingEndDatesBean fillingEndDatesBeanObj = new FillingEndDatesBean();
		try {
			EntityBean entObj = new EntityBean();
			FinYrFormat fyf = new FinYrFormat();
			fyf.setFinYrFormatId(1L);
			entObj.setFinYrFormat(fyf);
			Date date = getCustomizeFinDate(endDate);
			String stringDate = DateManip.convertDateToString(date, DateConstants.DD_MM_YYYY.getDateConstants());
			List<Integer> WEEKEND_LIST = Arrays.asList(DateConstants.SATURDAY.getDateConstantInt(), DateConstants.SUNDAY.getDateConstantInt());
			DateManip.setWEEKEND_LIST(WEEKEND_LIST);
			Map<Long, FillingEndDatesBean> fillingWindowEndDatesMap = new HashMap<Long, FillingEndDatesBean>();
			if (formFreqId.equals(RevisionRequestConstants.FREQ_ID_FORTNIGHTLY.getConstantLongVal())) {
				Map<Long, Map<Long, FillingEndDatesBean>> formFillingWindowMap = filingCalendarModifiedService.fetchFormFillingEndDatesDates(returnName, entObj, stringDate, propertyId);
				fillingWindowEndDatesMap = formFillingWindowMap.get(formFreqId);
				fillingEndDatesBeanObj = fillingWindowEndDatesMap.get(0L);
			} else if (formFreqId.equals(RevisionRequestConstants.FREQ_ID_DAILY.getConstantLongVal())) {
				Map<Long, Map<Long, FillingEndDatesBean>> formFillingWindowMap = filingCalendarModifiedService.fetchFormFillingEndDatesDates(returnName, entObj, stringDate, propertyId);
				fillingWindowEndDatesMap = formFillingWindowMap.get(formFreqId);
				for (Entry<Long, FillingEndDatesBean> entry : fillingWindowEndDatesMap.entrySet()) {
					fillingEndDatesBeanObj = fillingWindowEndDatesMap.get(entry.getKey());
				}
			} else if (formFreqId.equals(RevisionRequestConstants.FREQ_ID_FIRST_HALF_MONTHLY_EXCLUDE_HOLIDAY.getConstantLongVal())) {
				Map<Long, Map<Long, FillingEndDatesBean>> formFillingWindowMap = filingCalendarModifiedService.fetchFormFillingEndDatesDates(returnName, entObj, stringDate, propertyId);
				fillingWindowEndDatesMap = formFillingWindowMap.get(formFreqId);
				for (Entry<Long, FillingEndDatesBean> entry : fillingWindowEndDatesMap.entrySet()) {
					fillingEndDatesBeanObj = fillingWindowEndDatesMap.get(entry.getKey());
				}
			} else /*
					 * if
					 * (formFreqId.equals(RevisionRequestConstants.FREQ_ID_WEEKLY.getConstantLongVal
					 * ()) ||
					 * formFreqId.equals(RevisionRequestConstants.FREQ_ID_FORTNIGHTLY_15_DAYS.
					 * getConstantLongVal()) || formFreqId.equals(RevisionRequestConstants.
					 * FREQ_ID_QUARTERLY_WITH_LAST_FORTNIGHT_OF_QUARTER.getConstantLongVal()) ||
					 * formFreqId.equals(RevisionRequestConstants.
					 * FREQ_ID_MONTHLY_WITH_LAST_FORTNIGHT_OF_QUARTER.getConstantLongVal()) ||
					 * formFreqId.equals(RevisionRequestConstants.
					 * FREQ_ID_CUSTOMIZED_MONTHLY_WITH_LAST_FORTNIGHT_OF_QUARTER.getConstantLongVal(
					 * )) || formFreqId.equals(RevisionRequestConstants.
					 * FREQ_ID_CUSTOMIZED_QUARTERLY_WITH_LAST_FORTNIGHT_OF_QUARTER.
					 * getConstantLongVal()) || formFreqId.equals(RevisionRequestConstants.
					 * FREQ_ID_CUSTOMIZED_MONTHLY_WITH_LAST_FRIDAY_OF_QUARTER.getConstantLongVal())
					 * || formFreqId.equals(RevisionRequestConstants.FREQ_ID_HALF_MONTHLY.
					 * getConstantLongVal()) ||
					 * formFreqId.equals(RevisionRequestConstants.FREQ_ID_ANNUALLY.
					 * getConstantLongVal()) ||
					 * formFreqId.equals(RevisionRequestConstants.FREQ_ID_HALF_YEARLY.
					 * getConstantLongVal()) ||
					 * formFreqId.equals(RevisionRequestConstants.FREQ_ID_QUARTERLY.
					 * getConstantLongVal()) ||
					 * formFreqId.equals(RevisionRequestConstants.FREQ_ID_ANNUAL_QUATER.
					 * getConstantLongVal()) ||
					 * formFreqId.equals(RevisionRequestConstants.FREQ_ID_CUSTOMIZED_ANNUALLY.
					 * getConstantLongVal()) ||
					 * formFreqId.equals(RevisionRequestConstants.FREQ_ID_MONTHLY.getConstantLongVal
					 * ()))
					 */ {
				Map<Long, Map<Long, FillingEndDatesBean>> formFillingWindowMap = filingCalendarModifiedService.fetchFormFillingEndDatesDates(returnName, entObj, stringDate, propertyId);
				fillingWindowEndDatesMap = formFillingWindowMap.get(formFreqId);
				fillingEndDatesBeanObj = fillingWindowEndDatesMap.get(0L);

			}
		} catch (Exception e) {
			logger.error("Exception occoured while fatching data for processing id", jobProcessId + "Exception is " + e);
		}
		return fillingEndDatesBeanObj;
	}

	private Date getCustomizeFinDate(String endDate) throws ParseException {
		DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		return formatter.parse(endDate);
	}

	public String getWindowEndDate(String windowFirstDate, Integer emailDays, Integer windowDays) throws ParseException {
		String outputDate = "";
		SimpleDateFormat sdf = new SimpleDateFormat(DateConstants.DD_MM_YYYY.getDateConstants());
		SimpleDateFormat sdf1 = new SimpleDateFormat(DateConstants.DD_MM_YYYY.getDateConstants());
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse(windowFirstDate));
		c.add(Calendar.DAY_OF_MONTH, (windowDays - emailDays));
		String endWindowDate = sdf.format(c.getTime());
		outputDate = sdf1.format(sdf.parse(endWindowDate));
		return outputDate;
	}

	public String getFormattedDate(String inputDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf1 = new SimpleDateFormat(DateConstants.DD_MM_YYYY.getDateConstants());
		return sdf1.format(sdf.parse(inputDate));
	}

	@PostMapping(value = "/updateFilingCalender")
	public ServiceResponse updateFilingCalenderFromUpdateReturn(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody ReturnFileFormatMapDto returnFileFormatMapDto) {
		logger.info("Request received to update Filing Calender for processing id", jobProcessId);
		filingCalendarBean = new FilingCalendar();
		Boolean status = false;
		try {
			filingCalendarBean.setReturnId(returnFileFormatMapDto.getReturnIdFk());
			filingCalendarBean.setUpdateFromReturn(true);
			status = filingCalendarService.updateFilingCal(filingCalendarBean);
		} catch (ServiceException e) {
			logger.error("Request object not proper for update");
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0771.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0771.toString())).build();
		} catch (Exception e) {
			logger.error("Exception occoured while upadating FilingCalender for processing id", jobProcessId + "Exception is " + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0805.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0805.toString())).build();
		}
		ServiceResponse response = null;
		if (status) {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(filingCalendarBean);
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
		}
		logger.info("request completed to update FilingCalender for job processingid" + jobProcessId);
		return response;
	}

	@PostMapping(value = "/getFilingCalHistory")
	public ServiceResponse getFilingCalHistory(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody FilingCalendarInfo filingCalendarInfoObj) {
		logger.info("Requseting getFilingCalHistory " + jobProcessId);
		try {
			List<FilingCalendar> fetchCalendarInfoList = new ArrayList<>();
			List<Integer> filingCalIdList = new ArrayList<>();
			List<FilingCalendarModificationHistory> filingCalendarModificationHistoryList = new ArrayList<>();
			List<FilingCalendar> filingCalendarList = new ArrayList<>();
			if (filingCalendarInfoObj != null) {
				fetchCalendarInfoList = filingCalendarService.getFilingCalDataByReturnId(filingCalendarInfoObj.getReturnId());
				if (!CollectionUtils.isEmpty(fetchCalendarInfoList)) {
					for (FilingCalendar fetchCalendarInfoObj : fetchCalendarInfoList) {
						filingCalIdList.add(fetchCalendarInfoObj.getFilingCalendarId());
					}
					Type listToken = new TypeToken<List<String>>() {
					}.getType();
					if (!CollectionUtils.isEmpty(filingCalIdList)) {
						filingCalendarModificationHistoryList = filingCalModHistoryService.getFilingCalModHistoryData(filingCalIdList);
						FilingCalendar filingCalendarObj = new FilingCalendar();
						for (FilingCalendarModificationHistory filingCalendarModificationHistory : filingCalendarModificationHistoryList) {
							filingCalendarObj = new FilingCalendar();
							listToken = new TypeToken<FilingCalendar>() {
							}.getType();
							filingCalendarObj = new Gson().fromJson(filingCalendarModificationHistory.getFilingModJson(), listToken);
							UserMaster userMaster = new UserMaster();
							userMaster.setUserName(filingCalendarModificationHistory.getUserModify().getUserName());
							filingCalendarObj.setUserModify(userMaster);
							filingCalendarObj.setLastModifiedOn(filingCalendarModificationHistory.getModifiedOn());
							ReturnPropertyValue returnPropertyValue = new ReturnPropertyValue();
							if (filingCalendarModificationHistory.getFilingCalendarFk().getReturnPropertyVal() != null) {
								returnPropertyValue.setReturnProValue(filingCalendarModificationHistory.getFilingCalendarFk().getReturnPropertyVal().getReturnProValue());
								filingCalendarObj.setReturnPropertyVal(returnPropertyValue);
							}
							filingCalendarList.add(filingCalendarObj);
						}
					}
				}
			}

			if (CollectionUtils.isEmpty(filingCalendarList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusMessage("There is no FilingCalendarInfo List").build();
			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(filingCalendarList).build();
		} catch (Exception e) {
			logger.error("Exception occoured while fatching data for processing id", jobProcessId + "Exception is " + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusMessage("Exception occoured while FilingCalendarInfo").build();
		}
	}

}
