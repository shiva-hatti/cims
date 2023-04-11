/**
 * 
 */
package com.iris.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.controller.HolidayController;
import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.exception.ServiceException;

/**
 * @author pradnya
 *
 */
@Service
public class DateAndTimeArithmeticWrapperService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DateAndTimeArithmeticWrapperService.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -9031998501673646716L;

	@Autowired
	private HolidayController holidayController;

	public String getDate(Date endDate, Boolean excludeWeekend, DateUtilsParser.Frequency frequency, String jobProcessingId) throws ParseException {
		if (frequency.equals(DateUtilsParser.Frequency.FIRST_HALF_MONTHLY_EXCLUDE_HOLIDAY)) {
			DateAndTimeArithmetic.setWEEKEND_LIST(Arrays.asList(Calendar.SATURDAY, Calendar.SUNDAY));
			List<Date> holidayListDate;
			try {
				holidayListDate = fetchActiveHolidayList(jobProcessingId, DateManip.convertDateToString(endDate, DateConstants.YYYY.getDateConstants()));
				DateAndTimeArithmetic.setHOLIDAY_LIST(holidayListDate);
				return DateAndTimeArithmetic.getDate(endDate, true, frequency);
			} catch (ServiceException e) {
				return "";
			}
		} else {
			return DateAndTimeArithmetic.getDate(endDate, excludeWeekend, frequency);
		}
	}

	public List<String> getAllPossibleEndDatesBetweenPeriod(String jobProcessingId, DateUtilsParser.Frequency frequency, Boolean excludeWeekend, Date startDate, Date endDate, String dateFormatter) throws ParseException {
		if (frequency.equals(DateUtilsParser.Frequency.FIRST_HALF_MONTHLY_EXCLUDE_HOLIDAY)) {
			DateAndTimeArithmetic.setWEEKEND_LIST(Arrays.asList(Calendar.SATURDAY, Calendar.SUNDAY));
			List<Date> holidayListDate;
			try {
				holidayListDate = fetchActiveHolidayList(DateManip.convertDateToString(endDate, DateConstants.YYYY.getDateConstants()), jobProcessingId);
				DateAndTimeArithmetic.setHOLIDAY_LIST(holidayListDate);
				return DateAndTimeArithmetic.getAllPossibleEndDatesBetweenPeriod(frequency, excludeWeekend, startDate, endDate, dateFormatter);
			} catch (ServiceException e) {
				return new ArrayList<String>();
			}
		} else {
			return DateAndTimeArithmetic.getAllPossibleEndDatesBetweenPeriod(frequency, excludeWeekend, startDate, endDate, dateFormatter);
		}
	}

	private List<Date> fetchActiveHolidayList(String convertDateToString, String jobProcessingId) throws ServiceException {
		//		String jobProcessingId = UUID.randomUUID().toString();

		try {
			List<Date> holidayListDate = new ArrayList<>();
			// call web service to authorize user

			LOGGER.info("FETCH Holiday List API calling for job processing ID : " + jobProcessingId);

			List<String> holidayList = (List<String>) holidayController.fetchActiveHolidayForYear(jobProcessingId, convertDateToString).getResponse();

			try {
				for (String holiday : holidayList) {
					holidayListDate.add(DateManip.convertStringToDate(holiday, DateConstants.DD_MM_YYYY.getDateConstants()));
				}
				return holidayListDate;
			} catch (ParseException e) {
				LOGGER.error("Date Parsing exception job procesign ID :  " + jobProcessingId, e);
				throw new ServiceException("Date Parsing exception job procesign ID :  ");
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured for fetching holiday List and job procesign ID :  " + jobProcessingId, e);
			throw new ServiceException("Date Parsing exception job procesign ID :  ");
		}
	}

}
