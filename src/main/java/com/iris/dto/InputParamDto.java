package com.iris.dto;

public class InputParamDto {
	private String entityId;
	private String dateFrmt;
	private String timeFrmt;
	private String calendarFrmt;

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getDateFrmt() {
		return dateFrmt;
	}

	public void setDateFrmt(String dateFrmt) {
		this.dateFrmt = dateFrmt;
	}

	public String getTimeFrmt() {
		return timeFrmt;
	}

	public void setTimeFrmt(String timeFrmt) {
		this.timeFrmt = timeFrmt;
	}

	public String getCalendarFrmt() {
		return calendarFrmt;
	}

	public void setCalendarFrmt(String calendarFrmt) {
		this.calendarFrmt = calendarFrmt;
	}
}
