package com.iris.dto;

import java.io.Serializable;

public class ReturnMetaDataBean implements Serializable {

	private static final long serialVersionUID = 8234286936384310373L;

	private boolean returnCodeMatchCheck;
	private boolean entityCodeMatchCheck;
	private boolean reportingFrequencyCheck;
	private boolean reportingStatusCheck;
	private boolean reportingStartDateCheck;
	private boolean entityNameMatchCheck;
	private boolean reportTypeCheck;
	private boolean versionNumberCheck;
	private boolean specialEconoicZoneCheck;

	public boolean isSpecialEconoicZoneCheck() {
		return specialEconoicZoneCheck;
	}

	public void setSpecialEconoicZoneCheck(boolean specialEconoicZoneCheck) {
		this.specialEconoicZoneCheck = specialEconoicZoneCheck;
	}

	public boolean isReportTypeCheck() {
		return reportTypeCheck;
	}

	public void setReportTypeCheck(boolean reportTypeCheck) {
		this.reportTypeCheck = reportTypeCheck;
	}

	public boolean isVersionNumberCheck() {
		return versionNumberCheck;
	}

	public void setVersionNumberCheck(boolean versionNumberCheck) {
		this.versionNumberCheck = versionNumberCheck;
	}

	public boolean isEntityNameMatchCheck() {
		return entityNameMatchCheck;
	}

	public void setEntityNameMatchCheck(boolean entityNameMatchCheck) {
		this.entityNameMatchCheck = entityNameMatchCheck;
	}

	public boolean isReturnCodeMatchCheck() {
		return returnCodeMatchCheck;
	}

	public void setReturnCodeMatchCheck(boolean returnCodeMatchCheck) {
		this.returnCodeMatchCheck = returnCodeMatchCheck;
	}

	public boolean isEntityCodeMatchCheck() {
		return entityCodeMatchCheck;
	}

	public void setEntityCodeMatchCheck(boolean entityCodeMatchCheck) {
		this.entityCodeMatchCheck = entityCodeMatchCheck;
	}

	public boolean isReportingFrequencyCheck() {
		return reportingFrequencyCheck;
	}

	public void setReportingFrequencyCheck(boolean reportingFrequencyCheck) {
		this.reportingFrequencyCheck = reportingFrequencyCheck;
	}

	public boolean isReportingStatusCheck() {
		return reportingStatusCheck;
	}

	public void setReportingStatusCheck(boolean reportingStatusCheck) {
		this.reportingStatusCheck = reportingStatusCheck;
	}

	public boolean isReportingStartDateCheck() {
		return reportingStartDateCheck;
	}

	public void setReportingStartDateCheck(boolean reportingStartDateCheck) {
		this.reportingStartDateCheck = reportingStartDateCheck;
	}
}
