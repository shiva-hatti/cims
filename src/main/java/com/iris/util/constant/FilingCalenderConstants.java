package com.iris.util.constant;

public enum FilingCalenderConstants {

	START_DATE("reportStartDate"),
	END_DATE("reportEndDate"),
	EXTENSION_DAY("extensionDay"),
	EXTENSION_DAY_END("extensionDayEnd"),
	FILING_WINDOW_EXTENSION("fillingWindowExtension"),
	EMAIL_NOTIFICATION_DAYS("emailNotificationDays"),
	ACTION_STATUS("Action"),
	FREQUENCY("frequency"),
	FIN_YR_FORMAT("finYrFormat"),
	SEND_MAIL("sendMail"),
	START_TIME("startTime"),
	END_TIME("endTime"),
	START_TIME_SECOND_HALF("startTimeFirstHalf"),
	END_TIME_SECOND_HALF("endTimeSecondHalf"),
	FILING_WINDOW_EXTENSION_END("fillingWindowExtensionEnd"),
	FREQUENCY_ID("frequencyId"),
	ADHOC_DATE("calDate"),
	FIN_YR_FORMAT_ID("finYrFormatId"),
	UPLOADED(1L),
	VERIFY_ENTITY(5L),
	VERIFY_AUDITOR(10L),
	VERIFY_REGULATOR(11L),
	SUBMITTED(9L),
	VALIDATION_FAILED(2L),
	IS_ONTIME("isOntime"),
	IS_DELAYED("isDelayed"),
	IS_REVISED("isRevised"),
	VERIFY_JSE(22L),
	REJECTED(19L),
	COMPLETED(16L),
	VERIFY_ASE(21L),
	QUATERLY(3L),
	DAILY(7L),
	FORT_NIGHTLY(5L),
	HALF_MONTHLY(11L),
	MONTHLY(4L),
	HALF_YEARLY(2L),
	YEARLY(1L),
	WEEKLY(6L),
	CUSTOMIZED_ANNUALLY(11L),
	FIRST_HALF_MONTHLY_EXCLUDE_HOLIDAY(12L),
	FORTNIGHTLY_15_DAYS(13L),
	AS_AN_WHEN(14L),
	QUARTERLY_WITH_LAST_FORTNIGHT_OF_QUARTER(15L),
	MONTHLY_WITH_LAST_FORTNIGHT_OF_QUARTER(16L),
	CUSTOMIZED_MONTHLY_WITH_LAST_FORTNIGHT_OF_QUARTER(17L),
	CUSTOMIZED_QUARTERLY_WITH_LAST_FORTNIGHT_OF_QUARTER(18L),
	CUSTOMIZED_MONTHLY_WITH_LAST_FRIDAY(19L),
	FREQ_ID_HALF_MONTHLY(20L);

	private String name;
	private Long number;

	private FilingCalenderConstants(String name) {
		this.name = name;
	}

	private FilingCalenderConstants(Long number) {
		this.number = number;
	}

	public String getConstantVal() {
		return name;
	}

	public Long getConstantLongVal() {
		return number;
	}
}
