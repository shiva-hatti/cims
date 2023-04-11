package com.iris.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sagar Jadhav
 */
public class DateUtil {

	final static Logger logger = LoggerFactory.getLogger(DateUtilsParser.class);

	public static Date getFormattedDate(String DateString, String dateFormat) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
		try {
			return simpleDateFormat.parse(DateString);
		} catch (ParseException e) {
			logger.error("Exception : ", e);
		}
		return null;
	}

	public static String getStringFormattedDate(Date date, String dateFormat) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
		return simpleDateFormat.format(date);
	}

	public static String getTodaysDateStringFormat() {
		SimpleDateFormat sm = new SimpleDateFormat("dd-MMM-yyyy");
		return sm.format(new Date());
	}

	public static String getTodaysDateWithGivenStringFormat(String dateFormat) {
		SimpleDateFormat sm = new SimpleDateFormat(dateFormat);
		return sm.format(new Date());
	}

	public static String getTodaysDateTimeStringFormat() {
		SimpleDateFormat sm = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
		return sm.format(new Date());
	}

	public static String getTodaysDateStringFormat(String format) {
		SimpleDateFormat sm = new SimpleDateFormat(format);
		return sm.format(new Date());
	}

	public static Date getTodaysDateWithoutTime(String format) {
		DateFormat formatter = new SimpleDateFormat(format);
		Date today = new Date();
		try {
			Date todayWithZeroTime = formatter.parse(formatter.format(today));
			return todayWithZeroTime;
		} catch (ParseException e) {
			logger.error("Exception : ", e);
		}
		return null;
	}

	public static String getTodaysDateMinusOneStringFormat(String format) {
		SimpleDateFormat sm = new SimpleDateFormat(format);
		Date date = new Date();
		Calendar currCal = Calendar.getInstance();
		currCal.setTime(date);
		currCal.add(Calendar.DATE, -1);
		return sm.format(currCal.getTime());
	}
}