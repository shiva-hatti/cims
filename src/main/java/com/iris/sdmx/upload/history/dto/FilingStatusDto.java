package com.iris.sdmx.upload.history.dto;

import java.io.Serializable;

public class FilingStatusDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5447745073530243053L;

	private Integer filingStatusId;

	private String status;

	private Boolean forWorkFlow;

	public Integer getFilingStatusId() {
		return filingStatusId;
	}

	public void setFilingStatusId(Integer filingStatusId) {
		this.filingStatusId = filingStatusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getForWorkFlow() {
		return forWorkFlow;
	}

	public void setForWorkFlow(Boolean forWorkFlow) {
		this.forWorkFlow = forWorkFlow;
	}

}
