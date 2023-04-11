package com.iris.dto;

import java.io.Serializable;

public class CsvMetaDataBean implements Serializable {

	private static final long serialVersionUID = 8234286936384310373L;

	private boolean returnCodeMatchCheck;
	private boolean entityCodeMatchCheck;
	private boolean reportingFrequencyCheck;
	private boolean reportingStatusCheck;

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

}
