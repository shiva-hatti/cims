package com.iris.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.iris.dateutility.enums.DateConstants;
import com.iris.util.constant.FilingCalenderConstants;
import com.iris.util.constant.GeneralConstants;

public class FillingCalendarDateUtil {
	static final Logger logger = LogManager.getLogger(FillingCalendarDateUtil.class);

	public Map<Long, String> getFilingEndDate(LocalDateTime currentDate, List<String> holidayListDate, String jobProcessingId) {
		Map<Long, String> endDateMap = new HashMap<>();
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DateConstants.DD_MM_YYYY.getDateConstants());
			endDateMap.put(FilingCalenderConstants.DAILY.getConstantLongVal(), dtf.format(currentDate));
			endDateMap.put(FilingCalenderConstants.MONTHLY.getConstantLongVal(), getMonthEndDate(currentDate));
			endDateMap.put(FilingCalenderConstants.QUATERLY.getConstantLongVal(), getQuaterlyEndDate(currentDate));
			endDateMap.put(FilingCalenderConstants.FIRST_HALF_MONTHLY_EXCLUDE_HOLIDAY.getConstantLongVal(), getHalfMonthlyEndDate(currentDate, holidayListDate));
			endDateMap.put(FilingCalenderConstants.YEARLY.getConstantLongVal(), getAnnuallyAprMarEndDate(currentDate));
			endDateMap.put(FilingCalenderConstants.CUSTOMIZED_ANNUALLY.getConstantLongVal(), getAnnuallyJanDecEndDate(currentDate));
			endDateMap.put(FilingCalenderConstants.HALF_YEARLY.getConstantLongVal(), getHalfYearlyEndDate(currentDate));
			endDateMap.put(FilingCalenderConstants.WEEKLY.getConstantLongVal(), getWeekendEndDateEndDate(dtf.format(currentDate)));
			endDateMap.put(FilingCalenderConstants.FORT_NIGHTLY.getConstantLongVal(), getFortNightEndDate(dtf.format(currentDate), jobProcessingId));
			endDateMap.put(FilingCalenderConstants.FORTNIGHTLY_15_DAYS.getConstantLongVal(), getFortNightEndDate(dtf.format(currentDate), jobProcessingId));
			endDateMap.put(FilingCalenderConstants.QUARTERLY_WITH_LAST_FORTNIGHT_OF_QUARTER.getConstantLongVal(), getQuaterlyLastFortnightEndDate(currentDate, jobProcessingId));
			endDateMap.put(FilingCalenderConstants.MONTHLY_WITH_LAST_FORTNIGHT_OF_QUARTER.getConstantLongVal(), getMonthlyLastFortnightEndDate(currentDate, jobProcessingId));
			endDateMap.put(FilingCalenderConstants.CUSTOMIZED_MONTHLY_WITH_LAST_FORTNIGHT_OF_QUARTER.getConstantLongVal(), getMonthlyLastFortnightEndDate(currentDate, jobProcessingId));
			endDateMap.put(FilingCalenderConstants.CUSTOMIZED_QUARTERLY_WITH_LAST_FORTNIGHT_OF_QUARTER.getConstantLongVal(), getQuaterlyLastFortnightEndDate(currentDate, jobProcessingId));
			endDateMap.put(FilingCalenderConstants.CUSTOMIZED_MONTHLY_WITH_LAST_FRIDAY.getConstantLongVal(), getMonthlyWithLastFridayEndDate(currentDate, jobProcessingId));
			endDateMap.put(FilingCalenderConstants.FREQ_ID_HALF_MONTHLY.getConstantLongVal(), getHalfMonthly(currentDate));
		} catch (Exception e) {
			logger.error("Exception in getFilingEndDate for JobProcessingId " + jobProcessingId + "Exception is" + e);
		}
		return endDateMap;
	}

	public static String getMonthlyWithLastFridayEndDate(LocalDateTime currentDate, String jobProcessingId) throws ParseException {
		SimpleDateFormat format2 = new SimpleDateFormat("ddMMyyyy");
		Date dateToBeCheck = format2.parse(getMonthEndDate(currentDate));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateToBeCheck);
		if (calendar.get(Calendar.DAY_OF_WEEK) == 6) {
			return getMonthEndDate(currentDate);
		} else {
			return getWeekendEndDateEndDate(getMonthEndDate(currentDate));
		}
	}

	public static String getMonthlyLastFortnightEndDate(LocalDateTime currentDate, String jobProcessingId) throws ParseException {
		return getFortNightEndDate(getMonthEndDate(currentDate), jobProcessingId);
	}

	public static String getQuaterlyLastFortnightEndDate(LocalDateTime currentDate, String jobProcessingId) throws ParseException {
		return getFortNightEndDate(getQuaterlyEndDate(currentDate), jobProcessingId);
	}

	public static String getFortNightEndDate(String currentDate, String jobProcessingId) throws ParseException {
		int match = 0;
		String lastMatchDate = "";
		String previousMatchDate = "";
		String endDate = "";
		try {
			SimpleDateFormat format = new SimpleDateFormat(DateConstants.DD_MM_YYYY.getDateConstants());
			SimpleDateFormat format2 = new SimpleDateFormat("ddMMyyyy");
			Calendar baseDateCal = Calendar.getInstance();
			baseDateCal.setTime(format.parse(CustomizeDateUtil.BASE_DATE));
			Date dateToBeCheck = format2.parse(format2.format(format.parse(currentDate)));
			Calendar dateToCheckCal = Calendar.getInstance();
			dateToCheckCal.setTime(dateToBeCheck);
			String dateToCheckMonthYearStr = ("" + (dateToCheckCal.get(Calendar.MONTH) + 1)) + dateToCheckCal.get(Calendar.YEAR);

			while (true) {
				String baseDateMonthYearStr = ("" + (baseDateCal.get(Calendar.MONTH) + 1)) + baseDateCal.get(Calendar.YEAR);
				if (dateToCheckMonthYearStr.equalsIgnoreCase(baseDateMonthYearStr)) {
					match++;
					if ((dateToCheckCal.get(Calendar.DATE) + dateToCheckMonthYearStr).equalsIgnoreCase(baseDateCal.get(Calendar.DATE) + baseDateMonthYearStr)) {
						lastMatchDate = getStartDateInFormat("" + baseDateCal.get(Calendar.DATE), baseDateCal);
						endDate = previousMatchDate;
						break;
					} else if ((dateToCheckCal.get(Calendar.DATE) >= baseDateCal.get(Calendar.DATE))) {
						if (match > 2) {
							endDate = previousMatchDate;
							break;
						}
					} else {
						endDate = previousMatchDate;
						break;
					}
				} else if (match > 0) {
					endDate = previousMatchDate;
					break;
				}
				previousMatchDate = getStartDateInFormat("" + baseDateCal.get(Calendar.DATE), baseDateCal);
				baseDateCal.add(Calendar.DATE, 14);
			}

		} catch (Exception ex) {
			logger.error("Exception in getFilingEndDate for JobProcessingId " + jobProcessingId + "Exception is" + ex);
		}
		return getFormattedDate(endDate);
	}

	public static String getHalfYearlyEndDate(LocalDateTime currentDate) {
		if (currentDate.getMonthValue() > 3 && currentDate.getMonthValue() <= 9) {
			return getExactDate("31", "03", currentDate.getYear());
		} else {
			if (currentDate.getMonthValue() == 1 || currentDate.getMonthValue() == 2 || currentDate.getMonthValue() == 3) {
				return getExactDate("30", "09", currentDate.getYear() - 1);
			} else {
				return getExactDate("30", "09", currentDate.getYear());
			}

		}
	}

	public static String getAnnuallyJanDecEndDate(LocalDateTime currentDate) {
		return getExactDate("31", "12", currentDate.getYear() - 1);
	}

	public static String getAnnuallyAprMarEndDate(LocalDateTime currentDate) {
		if (currentDate.getMonthValue() > 3 && currentDate.getMonthValue() < 12)
			return getExactDate("31", "03", currentDate.getYear());
		else
			return getExactDate("31", "03", currentDate.getYear() - 1);
	}

	public static String getHalfMonthlyEndDate(LocalDateTime currentDate, List<String> holidayListDate) {
		int count = 0;
		int exactDay = 0;
		String halfMonthlyEndDate = "";
		if (currentDate.getDayOfMonth() <= 15 && currentDate.getMonthValue() == 1) {
			halfMonthlyEndDate = getExactDate("15", "12", currentDate.getYear() - 1);
			boolean isWeekend = isWeekend(halfMonthlyEndDate);
			if (isWeekend) {
				halfMonthlyEndDate = getExactDate("13", "12", currentDate.getYear() - 1);
			}
			for (String dateString : holidayListDate) {
				if (dateString.equals(halfMonthlyEndDate)) {
					count++;
					if (isWeekend) {
						exactDay = 13 - count;
					} else {
						exactDay = 15 - count;
					}
					halfMonthlyEndDate = getExactDate(String.valueOf(exactDay), "12", currentDate.getYear() - 1);
				}
			}
			return halfMonthlyEndDate;
		} else if (currentDate.getDayOfMonth() > 15) {
			halfMonthlyEndDate = getExactDate("15", getRoundOffData(Integer.toString(currentDate.getMonthValue())), currentDate.getYear());
			boolean isWeekend = isWeekend(halfMonthlyEndDate);
			if (isWeekend) {
				halfMonthlyEndDate = getExactDate("13", getRoundOffData(Integer.toString(currentDate.getMonthValue())), currentDate.getYear());
			}
			for (String dateString : holidayListDate) {
				if (dateString.equals(halfMonthlyEndDate)) {
					count++;
					if (isWeekend) {
						exactDay = 13 - count;
					} else {
						exactDay = 15 - count;
					}
					halfMonthlyEndDate = getExactDate(String.valueOf(exactDay), getRoundOffData(Integer.toString(currentDate.getMonthValue())), currentDate.getYear());
				}
			}
			return halfMonthlyEndDate;
		} else if (currentDate.getDayOfMonth() <= 15) {
			halfMonthlyEndDate = getExactDate("15", getRoundOffData(Integer.toString(currentDate.getMonthValue() - 1)), currentDate.getYear());
			boolean isWeekend = isWeekend(halfMonthlyEndDate);
			if (isWeekend) {
				halfMonthlyEndDate = getExactDate("13", getRoundOffData(Integer.toString(currentDate.getMonthValue() - 1)), currentDate.getYear());
			}
			for (String dateString : holidayListDate) {
				if (dateString.equals(halfMonthlyEndDate)) {
					count++;
					if (isWeekend) {
						exactDay = 13 - count;
					} else {
						exactDay = 15 - count;
					}
					halfMonthlyEndDate = getExactDate(String.valueOf(exactDay), getRoundOffData(Integer.toString(currentDate.getMonthValue() - 1)), currentDate.getYear());
				}
			}
			return halfMonthlyEndDate;
		}
		return null;
	}

	public static String getHalfMonthly(LocalDateTime currentDate) {
		String halfMonthlyEndDate = "";
		if (currentDate.getDayOfMonth() <= 15 && currentDate.getMonthValue() == 1) {
			halfMonthlyEndDate = getExactDate("15", "12", currentDate.getYear() - 1);
			return halfMonthlyEndDate;
		} else if (currentDate.getDayOfMonth() > 15) {
			halfMonthlyEndDate = getExactDate("15", getRoundOffData(Integer.toString(currentDate.getMonthValue())), currentDate.getYear());
			return halfMonthlyEndDate;
		} else if (currentDate.getDayOfMonth() <= 15) {
			halfMonthlyEndDate = getExactDate("15", getRoundOffData(Integer.toString(currentDate.getMonthValue() - 1)), currentDate.getYear());
			return halfMonthlyEndDate;
		}
		return null;
	}

	private static boolean isWeekend(String halfMonthlyEndDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateConstants.DD_MM_YYYY.getDateConstants());
		DayOfWeek dayOfWeek = DayOfWeek.of(LocalDate.parse(halfMonthlyEndDate, formatter).get(ChronoField.DAY_OF_WEEK));
		switch (dayOfWeek) {
		case SATURDAY:
		case SUNDAY:
			return true;
		default:
			return false;
		}
	}

	public static String getQuaterlyEndDate(LocalDateTime currentDate) {
		if (currentDate.getMonthValue() == 1 || currentDate.getMonthValue() == 2 || currentDate.getMonthValue() == 3)
			return getExactDate("31", "12", currentDate.getYear() - 1);
		else if (currentDate.getMonthValue() == 4 || currentDate.getMonthValue() == 5 || currentDate.getMonthValue() == 6)
			return getExactDate("31", "03", currentDate.getYear());
		else if (currentDate.getMonthValue() == 7 || currentDate.getMonthValue() == 8 || currentDate.getMonthValue() == 9)
			return getExactDate("30", "06", currentDate.getYear());
		else if (currentDate.getMonthValue() == 10 || currentDate.getMonthValue() == 11 || currentDate.getMonthValue() == 12)
			return getExactDate("30", "09", currentDate.getYear());

		return null;
	}

	public static String getMonthEndDate(LocalDateTime currentDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(currentDate.getYear(), currentDate.getMonthValue() - 1, 0);
		if (currentDate.getMonthValue() == 1) {
			return getExactDate(Integer.toString(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)), "12", currentDate.getYear() - 1);
		} else {
			return getExactDate(Integer.toString(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)), getRoundOffData(Integer.toString(currentDate.getMonthValue() - 1)), currentDate.getYear());
		}

	}

	public static String getWeekendEndDateEndDate(String currentDate) throws ParseException {
		Calendar cal = Calendar.getInstance();
		Date date1 = new SimpleDateFormat(DateConstants.DD_MM_YYYY.getDateConstants()).parse(currentDate);
		cal.setTime(date1);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek <= 6) {
			cal.add(Calendar.DATE, -(dayOfWeek + 1));
		} else {
			cal.add(Calendar.DATE, -1);
		}
		return getFormattedDate(getStartDateInFormat("" + cal.get(Calendar.DATE), cal));
	}

	public static String getFormattedDate(String dateInFormat) throws ParseException {
		DateFormat srcDf = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat destDf = new SimpleDateFormat(DateConstants.DD_MM_YYYY.getDateConstants());
		return destDf.format(srcDf.parse(dateInFormat));
	}

	public static String getRoundOffData(String roundOffData) {
		if (roundOffData.length() == 1)
			return "0" + roundOffData;
		else
			return roundOffData;
	}

	public static String getStartDateInFormat(String date, Calendar cal) throws ParseException {
		String finalDate = "" + date + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
		SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
		Date startDate = format2.parse(finalDate);
		return format2.format(startDate);
	}

	public static String getExactDate(String givenDay, String givenMonth, int givenYear) {
		return givenDay + GeneralConstants.FILLING_WINDOW_DATE_SEPARATOR.getConstantVal() + givenMonth + GeneralConstants.FILLING_WINDOW_DATE_SEPARATOR.getConstantVal() + Integer.toString(givenYear);
	}
}