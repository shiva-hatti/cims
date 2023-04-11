package com.iris.controller;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.reflect.TypeToken;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.CustomDates;
import com.iris.dto.ReturnCustomDateInputDto;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.dto.XbrlWebFormDto;
import com.iris.exception.ServiceException;
import com.iris.model.ReturnCustomDate;
import com.iris.model.WebServiceComponentUrl;
import com.iris.repository.ReturnCustomDateRepo;
import com.iris.service.GenericService;
import com.iris.service.impl.XbrlWebFormService;
import com.iris.util.DateAndTimeArithmetic;
import com.iris.util.JsonUtility;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.FrequencyEnum;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;
import com.iris.util.constant.UploadFilingConstants;
import com.iris.webservices.client.CIMSRestWebserviceClient;

@RestController
@RequestMapping("/service/xbrlWebForm")
public class XbrlWebFormController {
	static final Logger logger = LogManager.getLogger(XbrlWebFormController.class);

	@Autowired
	private XbrlWebFormService xbrlWebFormService;

	@Autowired
	private ReturnCustomDateRepo returnCustomDateRepo;

	@Autowired
	private GenericService<WebServiceComponentUrl, Long> webServiceComponentService;

	@PostMapping(value = "/saveOrSubmitData")
	public ServiceResponse savePartialData(@RequestBody XbrlWebFormDto xbrlWebFormDto) {
		logger.info("Request Received to savePartialData method start ");
		ServiceResponse serviceResponse = xbrlWebFormService.checkUserSession(xbrlWebFormDto);
		if (serviceResponse.isStatus()) {
			serviceResponse = xbrlWebFormService.saveDataToPartialTable(xbrlWebFormDto);
			if (serviceResponse.isStatus() && xbrlWebFormDto.getFilingStatus().equals(UploadFilingConstants.UPLOAD_FILING_STATUS.getName())) {
				serviceResponse = xbrlWebFormService.submitXbrlDocument(xbrlWebFormDto);
			}
		}
		logger.info("Request completed savePartialData method end " + JsonUtility.getGsonObject().toJson(serviceResponse));
		return serviceResponse;
	}

	@PostMapping(value = "/submitXbrlData")
	public ServiceResponse submitXbrlData(@RequestBody XbrlWebFormDto xbrlWebFormDto) {
		logger.info("Request Received to submitXbrlData method start ");
		ServiceResponse serviceResponse = xbrlWebFormService.saveDataToPartialTable(xbrlWebFormDto);
		if (serviceResponse.isStatus()) {
			//Update the frequency
			//Call web service to save the file to a particular location and update file details in the partial data record 
		}
		logger.info("Request Received to submitXbrlData method end having status code : " + serviceResponse.getStatusCode() + "Status : " + serviceResponse.isStatus());
		return serviceResponse;
	}

	@PostMapping(value = "/verifyJwtToken")
	public ServiceResponse verifyJWTToken(@RequestBody XbrlWebFormDto xbrlWebFormDto) {
		logger.info("Request Received to Verify Token for User Name : " + xbrlWebFormDto.getUserName() + " And Token : " + xbrlWebFormDto.getJwtToken());
		ServiceResponse serviceResponse = xbrlWebFormService.checkUserSession(xbrlWebFormDto);
		logger.info("Request Completed to Verify Token for User Name : " + xbrlWebFormDto.getUserName() + " And Token : " + xbrlWebFormDto.getJwtToken() + " Session Status : " + serviceResponse.isStatus() + " status Code : " + serviceResponse.getStatusCode());
		return serviceResponse;
	}

	//	@PostMapping(value = "/startXbrlWebFormSession")
	//	public ServiceResponse startXbrlSession(@RequestBody XbrlWebFormDto xbrlWebFormDto) {
	//		return xbrlWebFormService.startXbrlWebFormSession(xbrlWebFormDto);
	//	}

	@PostMapping(value = "/getCustomDatesListUponReturnCode")
	public ServiceResponse getCustomDatesListUponReturnCode(@RequestBody ReturnCustomDateInputDto returnCustomDateInputDto) {
		String jsonResp = null;
		try {
			ReturnCustomDate returnCustomDate = returnCustomDateRepo.getReturnCustomDate(returnCustomDateInputDto.getReturnCode());
			if (returnCustomDate == null) {
				return new ServiceResponseBuilder().setStatus(true).setResponse(null).build();
			}
			Date reportingStartDate = DateManip.convertStringToDate(returnCustomDateInputDto.getReportingStartDate(), returnCustomDateInputDto.getDateFormat());
			Date reportingEndDate = DateManip.convertStringToDate(returnCustomDateInputDto.getReportingEndDate(), returnCustomDateInputDto.getDateFormat());
			jsonResp = fetchCustomJsonData(reportingStartDate, reportingEndDate, "APR_TO_MAR", returnCustomDateInputDto.getDateFormat(), returnCustomDate, returnCustomDateInputDto.getRequestFlag());
		} catch (Exception e) {
			logger.error("Exception while gettting Custom Dates List Upon ReturnCode");
		}
		return new ServiceResponseBuilder().setStatus(true).setResponse(jsonResp).build();
	}

	private static Calendar dateToCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	public String fetchCustomJsonData(Date reportingStartDate, Date reportingEndDate, String financialYear, String dateFomratStr, ReturnCustomDate returnCustomDate, String reqFlag) throws JSONException {
		CustomDates customDates = new CustomDates();
		SimpleDateFormat inputDateFormat = new SimpleDateFormat(dateFomratStr);
		inputDateFormat.setLenient(false);
		Boolean checkPeriod = false;
		JSONObject obj = new JSONObject(returnCustomDate.getCustomDateJson());
		JSONObject jsonObj = new JSONObject();
		String jsonDataString = null;
		try {
			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.PQ_END_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				Date pqEndDate = getPqEndDate(reportingStartDate, reportingEndDate, dateFomratStr);
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.PQ_END_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(pqEndDate));
				jsonObj.put(GeneralConstants.PQ_END_DATE.getConstantVal(), inputDateFormat.format(pqEndDate));
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.PY_END_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				Date pyEndDate = getPyEndDate(reportingStartDate, financialYear);
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.PY_END_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(pyEndDate));
				jsonObj.put(GeneralConstants.PY_END_DATE.getConstantVal(), inputDateFormat.format(pyEndDate));
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.CUR_Q_END_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				Date cunEndDate = getCurEndDate(reportingStartDate, reportingEndDate);
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.CUR_Q_END_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(cunEndDate));
				jsonObj.put(GeneralConstants.CUR_Q_END_DATE.getConstantVal(), inputDateFormat.format(cunEndDate));
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.CUR_Q_START_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				Date curStartDate = getCurStartDate(reportingStartDate, reportingEndDate);
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.CUR_Q_START_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(curStartDate));
				jsonObj.put(GeneralConstants.CUR_Q_START_DATE.getConstantVal(), inputDateFormat.format(curStartDate));
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.YTD_START_DATE.getConstantVal());

			if (Boolean.TRUE.equals(checkPeriod)) {
				Date ytdStartDate = getYtdStartDate(reportingStartDate, financialYear);
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.YTD_START_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(ytdStartDate));
				JSONObject obj2 = (JSONObject) obj.get(GeneralConstants.YTD_END_DATE.getConstantVal());
				obj2.append("value", inputDateFormat.format(reportingEndDate));
				customDates.setYtdStartDate(inputDateFormat.format(ytdStartDate));
				customDates.setYtdEndDate(inputDateFormat.format(reportingEndDate));
				jsonObj.put(GeneralConstants.YTD_START_DATE.getConstantVal(), inputDateFormat.format(ytdStartDate));
				jsonObj.put(GeneralConstants.YTD_END_DATE.getConstantVal(), inputDateFormat.format(reportingEndDate));
				if (customDates.getYtdStartDate().equals(DateManip.convertDateToString(reportingStartDate, dateFomratStr)) && customDates.getYtdEndDate().equals(DateManip.convertDateToString(reportingEndDate, dateFomratStr))) {
					JSONObject obj3 = (JSONObject) obj.get(GeneralConstants.YTD_START_DATE.getConstantVal());
					obj3.append("value", "");
					JSONObject obj4 = (JSONObject) obj.get(GeneralConstants.YTD_END_DATE.getConstantVal());
					obj4.append("value", "");
					jsonObj.put(GeneralConstants.YTD_START_DATE.getConstantVal(), "");
					jsonObj.put(GeneralConstants.YTD_END_DATE.getConstantVal(), "");
				}
			}

			Calendar reportingEndDateCalender = dateToCalendar(reportingEndDate);
			if (reportingEndDateCalender.get(Calendar.MONTH) < 3) {
				reportingEndDateCalender.add(Calendar.YEAR, -1);
			}
			Calendar reportingStartDateCalender = dateToCalendar(reportingStartDate);
			if (reportingStartDateCalender.get(Calendar.MONTH) < 3) {
				reportingStartDateCalender.add(Calendar.YEAR, -1);
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.FIRST_PRV_MAR_FIN_YR_START_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				Calendar firstPrvMarFinYrStartDateCal = new GregorianCalendar(reportingStartDateCalender.get(Calendar.YEAR) - 1, Calendar.APRIL, 1);
				Date firstPrvMarFinYrEndDate = firstPrvMarFinYrStartDateCal.getTime();
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.FIRST_PRV_MAR_FIN_YR_START_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(firstPrvMarFinYrEndDate));
				jsonObj.put(GeneralConstants.FIRST_PRV_MAR_FIN_YR_START_DATE.getConstantVal(), inputDateFormat.format(firstPrvMarFinYrEndDate));
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.FIRST_PRV_MAR_FIN_YR_END_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				Calendar firstPrvMarFinYrEndDateCal = new GregorianCalendar(reportingEndDateCalender.get(Calendar.YEAR), Calendar.MARCH, 31);
				Date firstPrvMarFinYrEndDate = firstPrvMarFinYrEndDateCal.getTime();
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.FIRST_PRV_MAR_FIN_YR_END_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(firstPrvMarFinYrEndDate));
				jsonObj.put(GeneralConstants.FIRST_PRV_MAR_FIN_YR_END_DATE.getConstantVal(), inputDateFormat.format(firstPrvMarFinYrEndDate));
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.SECOND_PRV_MAR_FIN_YR_START_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				Date secondPrvMarFinYrStartDate = new GregorianCalendar(reportingEndDateCalender.get(Calendar.YEAR) - 2, Calendar.APRIL, 1).getTime();
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.SECOND_PRV_MAR_FIN_YR_START_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(secondPrvMarFinYrStartDate));
				jsonObj.put(GeneralConstants.SECOND_PRV_MAR_FIN_YR_START_DATE.getConstantVal(), inputDateFormat.format(secondPrvMarFinYrStartDate));
			}
			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.SECOND_PRV_MAR_FIN_YR_END_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				Date secondPrvMarFinYrEndDate = new GregorianCalendar(reportingEndDateCalender.get(Calendar.YEAR) - 1, Calendar.MARCH, 31).getTime();
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.SECOND_PRV_MAR_FIN_YR_END_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(secondPrvMarFinYrEndDate));
				jsonObj.put(GeneralConstants.SECOND_PRV_MAR_FIN_YR_END_DATE.getConstantVal(), inputDateFormat.format(secondPrvMarFinYrEndDate));
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.THIRD_PRV_MAR_FIN_YR_START_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				Date thirdPrvMarFinYrStartDate = new GregorianCalendar(reportingEndDateCalender.get(Calendar.YEAR) - 3, Calendar.APRIL, 1).getTime();
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.THIRD_PRV_MAR_FIN_YR_START_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(thirdPrvMarFinYrStartDate));
				jsonObj.put(GeneralConstants.THIRD_PRV_MAR_FIN_YR_START_DATE.getConstantVal(), inputDateFormat.format(thirdPrvMarFinYrStartDate));
			}
			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.THIRD_PRV_MAR_FIN_YR_END_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				Date thirdPrvMarFinYrEndDate = new GregorianCalendar(reportingEndDateCalender.get(Calendar.YEAR) - 2, Calendar.MARCH, 31).getTime();
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.THIRD_PRV_MAR_FIN_YR_END_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(thirdPrvMarFinYrEndDate));
				jsonObj.put(GeneralConstants.THIRD_PRV_MAR_FIN_YR_END_DATE.getConstantVal(), inputDateFormat.format(thirdPrvMarFinYrEndDate));
			}

			Calendar reportingEndDateCalenderCal = dateToCalendar(reportingEndDate);
			if (reportingEndDateCalender.get(Calendar.MONTH) < 3) {
				reportingEndDateCalender.add(Calendar.YEAR, -1);
			}
			Calendar reportingStartDateCalenderCal = dateToCalendar(reportingStartDate);
			if (reportingStartDateCalender.get(Calendar.MONTH) < 3) {
				reportingStartDateCalender.add(Calendar.YEAR, -1);
			}
			Calendar t12MonthEndingCurQtrEndDateCal = reportingEndDateCalenderCal;
			Calendar t12MonthEndingCurQtrStartDateCal = reportingStartDateCalenderCal;
			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.TWELVE_MONTH_ENDING_CUR_QTR_START_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				t12MonthEndingCurQtrStartDateCal.add(Calendar.MONTH, -9);
				Date t12MonthEndingCurQtrStartDate = t12MonthEndingCurQtrStartDateCal.getTime();
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.TWELVE_MONTH_ENDING_CUR_QTR_START_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(t12MonthEndingCurQtrStartDate));
				jsonObj.put(GeneralConstants.TWELVE_MONTH_ENDING_CUR_QTR_START_DATE.getConstantVal(), inputDateFormat.format(t12MonthEndingCurQtrStartDate));
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.TWELVE_MONTH_ENDING_CUR_QTR_END_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				Date t12MonthEndingCurQtrEndDate = t12MonthEndingCurQtrEndDateCal.getTime();
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.TWELVE_MONTH_ENDING_CUR_QTR_END_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(t12MonthEndingCurQtrEndDate));
				jsonObj.put(GeneralConstants.TWELVE_MONTH_ENDING_CUR_QTR_END_DATE.getConstantVal(), inputDateFormat.format(t12MonthEndingCurQtrEndDate));
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.TWELVE_MONTH_ENDING_CUR_QTR_FIRST_PRV_FIN_YEAR_START_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				t12MonthEndingCurQtrStartDateCal.add(Calendar.MONTH, -12);
				Date t12MonthEndingCurQtrStartDate = t12MonthEndingCurQtrStartDateCal.getTime();
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.TWELVE_MONTH_ENDING_CUR_QTR_FIRST_PRV_FIN_YEAR_START_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(t12MonthEndingCurQtrStartDate));
				jsonObj.put(GeneralConstants.TWELVE_MONTH_ENDING_CUR_QTR_FIRST_PRV_FIN_YEAR_START_DATE.getConstantVal(), inputDateFormat.format(t12MonthEndingCurQtrStartDate));
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.TWELVE_MONTH_ENDING_CUR_QTR_FIRST_PRV_FIN_YEAR_END_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				Calendar t12MonthEndingFilingQtrFirstPrvFinYearEndDateCal = t12MonthEndingCurQtrEndDateCal;
				t12MonthEndingFilingQtrFirstPrvFinYearEndDateCal.add(Calendar.YEAR, -1);
				Date t12MonthEndingFilingQtrFirstPrvFinYearEndDate = t12MonthEndingFilingQtrFirstPrvFinYearEndDateCal.getTime();
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.TWELVE_MONTH_ENDING_CUR_QTR_FIRST_PRV_FIN_YEAR_END_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(t12MonthEndingFilingQtrFirstPrvFinYearEndDate));
				jsonObj.put(GeneralConstants.TWELVE_MONTH_ENDING_CUR_QTR_FIRST_PRV_FIN_YEAR_END_DATE.getConstantVal(), inputDateFormat.format(t12MonthEndingFilingQtrFirstPrvFinYearEndDate));
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.TWELVE_MONTH_ENDING_CUR_QTR_SECOND_PRV_FIN_YEAR_START_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				t12MonthEndingCurQtrStartDateCal.add(Calendar.MONTH, -12);
				Date t12MonthEndingCurQtrStartDate = t12MonthEndingCurQtrStartDateCal.getTime();
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.TWELVE_MONTH_ENDING_CUR_QTR_SECOND_PRV_FIN_YEAR_START_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(t12MonthEndingCurQtrStartDate));
				jsonObj.put(GeneralConstants.TWELVE_MONTH_ENDING_CUR_QTR_SECOND_PRV_FIN_YEAR_START_DATE.getConstantVal(), inputDateFormat.format(t12MonthEndingCurQtrStartDate));
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.TWELVE_MONTH_ENDING_CUR_QTR_SECOND_PRV_FIN_YEAR_END_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				Calendar t12MonthEndingFilingQtrSecondPrvFinYearEndDateCal = t12MonthEndingCurQtrEndDateCal;
				t12MonthEndingFilingQtrSecondPrvFinYearEndDateCal.add(Calendar.YEAR, -1);
				Date t12MonthEndingFilingQtrSecondPrvFinYearEndDate = t12MonthEndingFilingQtrSecondPrvFinYearEndDateCal.getTime();
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.TWELVE_MONTH_ENDING_CUR_QTR_SECOND_PRV_FIN_YEAR_END_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(t12MonthEndingFilingQtrSecondPrvFinYearEndDate));
				jsonObj.put(GeneralConstants.TWELVE_MONTH_ENDING_CUR_QTR_SECOND_PRV_FIN_YEAR_END_DATE.getConstantVal(), inputDateFormat.format(t12MonthEndingFilingQtrSecondPrvFinYearEndDate));
			}
			Calendar reportingStartDateCalenderCal12 = dateToCalendar(reportingStartDate);
			Calendar reportingPrvQtrEndDateCalender = reportingStartDateCalenderCal12;
			reportingPrvQtrEndDateCalender.add(Calendar.DAY_OF_MONTH, -1);
			Calendar t12MonthEndingPrvQtrEndDateCal = reportingPrvQtrEndDateCalender;

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.TWELVE_MONTH_ENDING_PRV_QTR_END_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				Calendar t12MonthEndingPrvQtrEndDateCal1 = t12MonthEndingPrvQtrEndDateCal;
				Date t12MonthEndingPrvQtrEndDate = t12MonthEndingPrvQtrEndDateCal1.getTime();
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.TWELVE_MONTH_ENDING_PRV_QTR_END_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(t12MonthEndingPrvQtrEndDate));
				jsonObj.put(GeneralConstants.TWELVE_MONTH_ENDING_PRV_QTR_END_DATE.getConstantVal(), inputDateFormat.format(t12MonthEndingPrvQtrEndDate));
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.TWELVE_MONTH_ENDING_PRV_QTR_FIRST_PRV_FIN_YEAR_END_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				Calendar t12MonthEndingPrvQtrFirstPrvFinYearEndDateCal = t12MonthEndingPrvQtrEndDateCal;
				t12MonthEndingPrvQtrFirstPrvFinYearEndDateCal.add(Calendar.YEAR, -1);
				Date t12MonthEndingPrvQtrFirstPrvFinYearEndDate = t12MonthEndingPrvQtrFirstPrvFinYearEndDateCal.getTime();
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.TWELVE_MONTH_ENDING_PRV_QTR_FIRST_PRV_FIN_YEAR_END_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(t12MonthEndingPrvQtrFirstPrvFinYearEndDate));
				jsonObj.put(GeneralConstants.TWELVE_MONTH_ENDING_PRV_QTR_FIRST_PRV_FIN_YEAR_END_DATE.getConstantVal(), inputDateFormat.format(t12MonthEndingPrvQtrFirstPrvFinYearEndDate));
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.TWELVE_MONTH_ENDING_PRV_QTR_SECOND_PRV_FIN_YEAR_END_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				Calendar t12MonthEndingPrvQtrSecondPrvFinYearEndDateCal = t12MonthEndingPrvQtrEndDateCal;
				t12MonthEndingPrvQtrSecondPrvFinYearEndDateCal.add(Calendar.YEAR, -1);
				Date t12MonthEndingPrvQtrSecondPrvFinYearEndDate = t12MonthEndingPrvQtrSecondPrvFinYearEndDateCal.getTime();
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.TWELVE_MONTH_ENDING_PRV_QTR_SECOND_PRV_FIN_YEAR_END_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(t12MonthEndingPrvQtrSecondPrvFinYearEndDate));
				jsonObj.put(GeneralConstants.TWELVE_MONTH_ENDING_PRV_QTR_SECOND_PRV_FIN_YEAR_END_DATE.getConstantVal(), inputDateFormat.format(t12MonthEndingPrvQtrSecondPrvFinYearEndDate));
			}

			Calendar reportingStartDateCalenderCal1 = dateToCalendar(reportingStartDate);
			reportingStartDateCalenderCal1.add(Calendar.DAY_OF_MONTH, -1);
			Calendar t12MonthEndingPrvQtrStartDateCal = reportingStartDateCalenderCal1;

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.TWELVE_MONTH_ENDING_PRV_QTR_START_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				Calendar t12MonthEndingPrvQtrStarttDateCal1 = t12MonthEndingPrvQtrStartDateCal;
				t12MonthEndingPrvQtrStarttDateCal1.add(Calendar.YEAR, -1);
				t12MonthEndingPrvQtrStarttDateCal1.add(Calendar.DAY_OF_MONTH, 1);
				Date t12MonthEndingPrvQtrStartDate = t12MonthEndingPrvQtrStarttDateCal1.getTime();
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.TWELVE_MONTH_ENDING_PRV_QTR_START_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(t12MonthEndingPrvQtrStartDate));
				jsonObj.put(GeneralConstants.TWELVE_MONTH_ENDING_PRV_QTR_START_DATE.getConstantVal(), inputDateFormat.format(t12MonthEndingPrvQtrStartDate));
			}

			Calendar reportingStartFirDateCalenderCal1 = dateToCalendar(reportingStartDate);
			reportingStartFirDateCalenderCal1.add(Calendar.DAY_OF_MONTH, -1);
			Calendar t12MonthEndingPrvQtrFirStartDateCal = reportingStartFirDateCalenderCal1;
			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.TWELVE_MONTH_ENDING_PRV_QTR_FIRST_PRV_FIN_YEAR_START_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				Calendar t12MonthEndingPrvFirQtrStarttDateCal1 = t12MonthEndingPrvQtrFirStartDateCal;
				t12MonthEndingPrvFirQtrStarttDateCal1.add(Calendar.YEAR, -2);
				t12MonthEndingPrvFirQtrStarttDateCal1.add(Calendar.DAY_OF_MONTH, 1);
				Date t12MonthEndingFirPrvQtrStartDate = t12MonthEndingPrvFirQtrStarttDateCal1.getTime();
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.TWELVE_MONTH_ENDING_PRV_QTR_FIRST_PRV_FIN_YEAR_START_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(t12MonthEndingFirPrvQtrStartDate));
				jsonObj.put(GeneralConstants.TWELVE_MONTH_ENDING_PRV_QTR_FIRST_PRV_FIN_YEAR_START_DATE.getConstantVal(), inputDateFormat.format(t12MonthEndingFirPrvQtrStartDate));
			}

			Calendar reportingStartSecDateCalenderCal1 = dateToCalendar(reportingStartDate);
			reportingStartSecDateCalenderCal1.add(Calendar.DAY_OF_MONTH, -1);
			Calendar t12MonthEndingPrvQtrSecStartDateCal = reportingStartSecDateCalenderCal1;
			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.TWELVE_MONTH_ENDING_PRV_QTR_SECOND_PRV_FIN_YEAR_START_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				Calendar t12MonthEndingPrvSecQtrStarttDateCal1 = t12MonthEndingPrvQtrSecStartDateCal;
				t12MonthEndingPrvSecQtrStarttDateCal1.add(Calendar.YEAR, -3);
				t12MonthEndingPrvSecQtrStarttDateCal1.add(Calendar.DAY_OF_MONTH, 1);
				Date t12MonthEndingSecPrvQtrStartDate = t12MonthEndingPrvSecQtrStarttDateCal1.getTime();
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.TWELVE_MONTH_ENDING_PRV_QTR_SECOND_PRV_FIN_YEAR_START_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(t12MonthEndingSecPrvQtrStartDate));
				jsonObj.put(GeneralConstants.TWELVE_MONTH_ENDING_PRV_QTR_SECOND_PRV_FIN_YEAR_START_DATE.getConstantVal(), inputDateFormat.format(t12MonthEndingSecPrvQtrStartDate));
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.CUR_HALF_YEAR_END_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				Date curHalfYearEndDate = getCurHalfYearEndDate(reportingEndDate);
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.CUR_HALF_YEAR_END_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(curHalfYearEndDate));
				jsonObj.put(GeneralConstants.CUR_HALF_YEAR_END_DATE.getConstantVal(), inputDateFormat.format(curHalfYearEndDate));
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.PRV_HALF_YEAR_END_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				Date prvHalfYearEndDate = getPrvHalfYearEndDate(reportingStartDate);
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.PRV_HALF_YEAR_END_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(prvHalfYearEndDate));
				jsonObj.put(GeneralConstants.PRV_HALF_YEAR_END_DATE.getConstantVal(), inputDateFormat.format(prvHalfYearEndDate));
			}
			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.MONTH_END_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.MONTH_END_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(reportingEndDate));
				jsonObj.put(GeneralConstants.MONTH_END_DATE.getConstantVal(), inputDateFormat.format(reportingEndDate));
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.MONTH_START_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				reportingEndDateCalenderCal.set(Calendar.DAY_OF_MONTH, 1);
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.MONTH_START_DATE.getConstantVal());
				obj1.append("value", inputDateFormat.format(reportingEndDateCalenderCal.getTime()));
				jsonObj.put(GeneralConstants.MONTH_START_DATE.getConstantVal(), inputDateFormat.format(reportingEndDateCalenderCal.getTime()));
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.FIRST_FORTNIGHT_END_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				String firstFortnightEndDate = getFortNightDate(reportingStartDate, reportingEndDate, dateFomratStr, GeneralConstants.FIRST_FORTNIGHT_END_DATE.getConstantVal());
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.FIRST_FORTNIGHT_END_DATE.getConstantVal());
				obj1.append("value", firstFortnightEndDate);
				jsonObj.put(GeneralConstants.FIRST_FORTNIGHT_END_DATE.getConstantVal(), firstFortnightEndDate);
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.SECOND_FORTNIGHT_END_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				String secondFortnightEndDate = getFortNightDate(reportingStartDate, reportingEndDate, dateFomratStr, GeneralConstants.SECOND_FORTNIGHT_END_DATE.getConstantVal());
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.SECOND_FORTNIGHT_END_DATE.getConstantVal());
				obj1.append("value", secondFortnightEndDate);
				jsonObj.put(GeneralConstants.SECOND_FORTNIGHT_END_DATE.getConstantVal(), secondFortnightEndDate);
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.THIRD_FORTNIGHT_END_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				String thirdFortnightEndDate = getFortNightDate(reportingStartDate, reportingEndDate, dateFomratStr, GeneralConstants.THIRD_FORTNIGHT_END_DATE.getConstantVal());
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.THIRD_FORTNIGHT_END_DATE.getConstantVal());
				obj1.append("value", thirdFortnightEndDate);
				jsonObj.put(GeneralConstants.THIRD_FORTNIGHT_END_DATE.getConstantVal(), thirdFortnightEndDate);
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.LAST_MONTH_LAST_FORTNIGHT_END_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				String lastMonthLastFortnightEndDate = getFortNightDate(reportingStartDate, reportingEndDate, dateFomratStr, GeneralConstants.LAST_MONTH_LAST_FORTNIGHT_END_DATE.getConstantVal());
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.LAST_MONTH_LAST_FORTNIGHT_END_DATE.getConstantVal());
				obj1.append("value", lastMonthLastFortnightEndDate);
				jsonObj.put(GeneralConstants.LAST_MONTH_LAST_FORTNIGHT_END_DATE.getConstantVal(), lastMonthLastFortnightEndDate);
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.CUR_FY_END_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				String curYearFyEndDate = getCurPrvFyEndDate(reportingEndDate, dateFomratStr, null);
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.CUR_FY_END_DATE.getConstantVal());
				obj1.append("value", curYearFyEndDate);
				jsonObj.put(GeneralConstants.CUR_FY_END_DATE.getConstantVal(), curYearFyEndDate);
			}
			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.PRV_FY_END_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				String prvYearFyEndDate = getCurPrvFyEndDate(reportingEndDate, dateFomratStr, true);
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.PRV_FY_END_DATE.getConstantVal());
				obj1.append("value", prvYearFyEndDate);
				jsonObj.put(GeneralConstants.PRV_FY_END_DATE.getConstantVal(), prvYearFyEndDate);
			}
			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.SECOND_PRV_FY_END_DATE.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				String secPrvYearFyEndDate = getCurPrvFyEndDate(reportingEndDate, dateFomratStr, false);
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.SECOND_PRV_FY_END_DATE.getConstantVal());
				obj1.append("value", secPrvYearFyEndDate);
				jsonObj.put(GeneralConstants.SECOND_PRV_FY_END_DATE.getConstantVal(), secPrvYearFyEndDate);
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.FIRST_ALTERNATIVE_FRIDAY.getConstantVal());
			List<String> alternativeFriday = new ArrayList<>();
			if (Boolean.TRUE.equals(checkPeriod)) {
				alternativeFriday = getAlternativeFriday(reportingStartDate, reportingEndDate, dateFomratStr);
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.FIRST_ALTERNATIVE_FRIDAY.getConstantVal());
				obj1.append("value", alternativeFriday.get(0));
				jsonObj.put(GeneralConstants.FIRST_ALTERNATIVE_FRIDAY.getConstantVal(), alternativeFriday.get(0));
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.SECOND_ALTERNATIVE_FRIDAY.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.SECOND_ALTERNATIVE_FRIDAY.getConstantVal());
				obj1.append("value", alternativeFriday.get(2));
				jsonObj.put(GeneralConstants.SECOND_ALTERNATIVE_FRIDAY.getConstantVal(), alternativeFriday.get(2));
			}

			checkPeriod = checkJsonPeriod(returnCustomDate.getCustomDateJson(), GeneralConstants.THIRD_ALTERNATIVE_FRIDAY.getConstantVal());
			if (Boolean.TRUE.equals(checkPeriod)) {
				if (alternativeFriday.size() == 5) {
					JSONObject obj1 = (JSONObject) obj.get(GeneralConstants.THIRD_ALTERNATIVE_FRIDAY.getConstantVal());
					obj1.append("value", alternativeFriday.get(4));
					jsonObj.put(GeneralConstants.THIRD_ALTERNATIVE_FRIDAY.getConstantVal(), alternativeFriday.get(4));
				}
			}

			if (reqFlag != null) {
				if (reqFlag.equals(GeneralConstants.TALEND.getConstantVal())) {
					jsonDataString = obj.toString().replaceAll("\\[", "").replaceAll("\\]", "");
				}
			} else {
				jsonDataString = jsonObj.toString();
			}
		} catch (JSONException | ParseException e) {
			logger.error("Exception while gettting Custom Dates Json Objects");
		}
		return jsonDataString;
	}

	private String getPlusOneDay(String date, String dateFomratStr) throws ParseException {
		Date covertedDate = DateManip.convertStringToDate(date, dateFomratStr);
		Calendar c = Calendar.getInstance();
		c.setTime(covertedDate);
		c.add(Calendar.DATE, 1);
		return DateManip.convertDateToString(c.getTime(), dateFomratStr);
	}

	private String getFortNightDate(Date reportingStartDate, Date reportingEndDate, String dateFomratStr, String periodType) throws ParseException {
		List<String> fortnightEndDateList = DateAndTimeArithmetic.getAllPossibleEndDatesBetweenPeriod(com.iris.util.DateUtilsParser.Frequency.getEnumByfreqPeriod(FrequencyEnum.FORT_NIGHT.getFrequencyCode()), false, reportingStartDate, reportingEndDate, dateFomratStr);
		if (periodType.equals(GeneralConstants.FIRST_FORTNIGHT_END_DATE.getConstantVal())) {
			return fortnightEndDateList.get(0);
		} else if (periodType.equals(GeneralConstants.SECOND_FORTNIGHT_END_DATE.getConstantVal())) {
			return fortnightEndDateList.get(1);
		} else if (periodType.equals(GeneralConstants.THIRD_FORTNIGHT_END_DATE.getConstantVal()) && fortnightEndDateList.size() == 3) {
			return fortnightEndDateList.get(2);
		} else if (periodType.equals(GeneralConstants.LAST_MONTH_LAST_FORTNIGHT_END_DATE.getConstantVal())) {
			Calendar startDate = dateToCalendar(reportingStartDate);
			startDate.add(Calendar.MONTH, -1);
			Calendar endDate = dateToCalendar(reportingStartDate);
			endDate.add(Calendar.DAY_OF_MONTH, -1);
			List<String> lastMonthFortnightEndDateList = DateAndTimeArithmetic.getAllPossibleEndDatesBetweenPeriod(com.iris.util.DateUtilsParser.Frequency.getEnumByfreqPeriod(FrequencyEnum.FORT_NIGHT.getFrequencyCode()), false, startDate.getTime(), endDate.getTime(), dateFomratStr);
			if (lastMonthFortnightEndDateList.size() == 3) {
				return lastMonthFortnightEndDateList.get(2);
			} else {
				return lastMonthFortnightEndDateList.get(1);
			}
		}
		return null;
	}

	private Date getPrvHalfYearEndDate(Date reportingStartDate) {
		Calendar prvHalfYearEndDate = dateToCalendar(reportingStartDate);
		prvHalfYearEndDate.add(Calendar.DAY_OF_MONTH, -1);
		Calendar prvrentHalfYearEndDate = prvHalfYearEndDate;
		return prvrentHalfYearEndDate.getTime();
	}

	private Date getCurHalfYearEndDate(Date reportingEndDate) {
		Calendar curHalfYearEndDate = dateToCalendar(reportingEndDate);
		Calendar currentHalfYearEndDate = curHalfYearEndDate;
		return currentHalfYearEndDate.getTime();
	}

	private Date getYtdStartDate(Date reportingStartDate, String financialYear) {
		Calendar startDateCalender = dateToCalendar(reportingStartDate);
		Calendar ytdStartDateCalendar = startDateCalender;

		if (GeneralConstants.APR_TO_MAR.getConstantVal().equalsIgnoreCase(financialYear)) {
			if (ytdStartDateCalendar.get(Calendar.MONTH) < 3) {
				ytdStartDateCalendar.add(Calendar.YEAR, -1);
			}
			ytdStartDateCalendar.set(Calendar.MONTH, 3);
		} else if (GeneralConstants.JAN_TO_DEC.getConstantVal().equalsIgnoreCase(financialYear)) {
			ytdStartDateCalendar.set(Calendar.MONTH, 0);
		}
		return ytdStartDateCalendar.getTime();
	}

	private Date getCurStartDate(Date reportingStartDate, Date reportingEndDate) {
		Calendar startDateCalender = dateToCalendar(reportingStartDate);
		startDateCalender = dateToCalendar(reportingEndDate);
		Calendar currentFinancialYearDateCalendar = startDateCalender;
		Date currentFinancialYearDate = currentFinancialYearDateCalendar.getTime();
		Calendar CurrentFinancialYearStartDate = dateToCalendar(currentFinancialYearDate);
		CurrentFinancialYearStartDate.add(Calendar.MONTH, -3);
		Date currentFinancialYearEndDate = CurrentFinancialYearStartDate.getTime();
		CurrentFinancialYearStartDate.add(Calendar.DAY_OF_MONTH, 1);
		currentFinancialYearEndDate = CurrentFinancialYearStartDate.getTime();
		return currentFinancialYearEndDate;
	}

	private Date getCurEndDate(Date reportingStartDate, Date reportingEndDate) {
		Calendar startDateCalender = dateToCalendar(reportingStartDate);
		startDateCalender = dateToCalendar(reportingEndDate);
		Calendar currentFinancialYearDateCalendar = startDateCalender;
		return currentFinancialYearDateCalendar.getTime();
	}

	private Date getPyEndDate(Date reportingStartDate, String financialYear) {
		Calendar startDateCalender = dateToCalendar(reportingStartDate);
		Calendar reportingStartDateCalender = startDateCalender;
		if (GeneralConstants.APR_TO_MAR.getConstantVal().equals("APR_TO_MAR")) {
			if (reportingStartDateCalender.get(Calendar.MONTH) < 3) {
				reportingStartDateCalender.add(Calendar.YEAR, -1);
			}
			reportingStartDateCalender.set(Calendar.MONTH, 3);
		} else if (GeneralConstants.JAN_TO_DEC.getConstantVal().equals(financialYear)) {
			reportingStartDateCalender.set(Calendar.MONTH, 0);
		}
		reportingStartDateCalender.add(Calendar.DAY_OF_MONTH, -1);
		return reportingStartDateCalender.getTime();
	}

	private Date getPqEndDate(Date reportingStartDate, Date reportingEndDate, String dateFomratStr) throws ParseException {
		SimpleDateFormat inputDateFormat = new SimpleDateFormat(dateFomratStr);
		inputDateFormat.setLenient(false);
		long dayDiff = 0;
		String startDate = DateManip.convertDateToString(reportingStartDate, dateFomratStr);
		String endDate = DateManip.convertDateToString(reportingEndDate, dateFomratStr);
		dayDiff = DateManip.getDayDiff(startDate, endDate, dateFomratStr);
		if (dayDiff > 100) {//for Annually
			Calendar startDateCalender = dateToCalendar(reportingEndDate);
			Calendar currentFinancialYearDateCalendar = startDateCalender;
			Date currentFinancialYearDate = currentFinancialYearDateCalendar.getTime();
			Calendar CurrentFinancialYearStartDate = dateToCalendar(currentFinancialYearDate);
			CurrentFinancialYearStartDate.add(Calendar.MONTH, -3);
			return CurrentFinancialYearStartDate.getTime();
		} else {//for quarterly
			Calendar startDateCalender = dateToCalendar(reportingStartDate);
			Calendar reportingStartDateCalender = startDateCalender;
			reportingStartDateCalender.add(Calendar.DAY_OF_MONTH, -1);
			return startDateCalender.getTime();
		}
	}

	private String getCurPrvFyEndDate(Date reportingEndDate, String dateFomratStr, Boolean yearFlag) throws ParseException {
		Calendar endDateCalender = dateToCalendar(reportingEndDate);
		int year = endDateCalender.get(Calendar.YEAR);
		if (Boolean.TRUE.equals(yearFlag)) {
			year = year - 1;
		} else if (Boolean.FALSE.equals(yearFlag)) {
			year = year - 2;
		}
		String curFyEndDate = "31/03/" + year;
		Date dd = DateManip.convertStringToDate(curFyEndDate, dateFomratStr);
		return DateManip.convertDateToString(dd, dateFomratStr);
	}

	public List<String> getAlternativeFriday(Date reportingStartDate, Date reportingEndDate, String dateFomratStr) {
		LocalDate localEndDate = reportingEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate endDate = LocalDate.of(localEndDate.getYear(), localEndDate.getMonthValue(), localEndDate.getDayOfMonth());
		LocalDate localStartDate = reportingStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate startDate = LocalDate.of(localStartDate.getYear(), localStartDate.getMonthValue(), localStartDate.getDayOfMonth());
		long numOfDays = ChronoUnit.DAYS.between(startDate, endDate);
		List<LocalDate> listOfDates = Stream.iterate(endDate, date -> date.minusDays(1)).limit(numOfDays + 1).collect(Collectors.toList());
		Comparator<LocalDate> comparator = (c1, c2) -> {
			return c1.compareTo(c2);
		};
		Collections.sort(listOfDates, comparator);
		List<String> listOfDatesResp = new ArrayList<>();
		for (LocalDate listOfDatesObj : listOfDates) {
			if ((listOfDatesObj.getDayOfWeek() == DayOfWeek.FRIDAY)) {
				String formattedDate = listOfDatesObj.format(DateTimeFormatter.ofPattern(dateFomratStr));
				listOfDatesResp.add(formattedDate);
			}
		}
		return listOfDatesResp;
	}

	private Boolean checkJsonPeriod(String customDateJson, String periodType) {
		try {
			JSONObject obj = new JSONObject(customDateJson);
			Object jsonObj = obj.get(periodType);
			if (!Objects.isNull(jsonObj)) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	//	/**
	//	 * @return the executor
	//	 */
	//	public static ExecutorService getExecutor() {
	//		return executor;
	//	}
	//
	//	/**
	//	 * @param executor the executor to set
	//	 */
	//	public static void setExecutor(ExecutorService executor) {
	//		XbrlWebFormController.executor = executor;
	//	}

	private WebServiceComponentUrl getWebServiceComponentURL(String componentName, String methodType) {
		Map<String, List<String>> valueMap = new HashMap<>();

		List<String> valueList = new ArrayList<>();
		valueList.add(componentName);
		valueMap.put(ColumnConstants.COMPONENTTYPE.getConstantVal(), valueList);

		valueList = new ArrayList<>();
		valueList.add(methodType);
		valueMap.put(ColumnConstants.METHODTYPE.getConstantVal(), valueList);

		WebServiceComponentUrl componentUrl = null;
		try {
			componentUrl = webServiceComponentService.getDataByColumnValue(valueMap, MethodConstants.GET_ACTIVE_DATA_BY_COMPONENTTYPE_METHODTYPE.getConstantVal()).get(0);
		} catch (ServiceException e) {
			logger.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}
		return componentUrl;
	}

}
