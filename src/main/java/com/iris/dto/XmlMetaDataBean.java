package com.iris.dto;

import java.io.Serializable;

public class XmlMetaDataBean implements Serializable {

	private static final long serialVersionUID = 8234286936384310373L;

	private boolean returnCodeMatchCheck;
	private boolean entityCodeMatchCheck;
	private boolean reportingStatusCheck;
	private boolean reportingStartDateCheck;

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
