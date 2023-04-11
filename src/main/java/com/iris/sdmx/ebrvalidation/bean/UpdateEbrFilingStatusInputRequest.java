package com.iris.sdmx.ebrvalidation.bean;

import java.io.Serializable;

import com.iris.sdmx.status.bean.SdmxActivityDetailLogRequest;

public class UpdateEbrFilingStatusInputRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -949281176985063267L;

	private Long fileDetailsId;

	private Integer filingStatusId;

	private SdmxActivityDetailLogRequest sdmxActivityDetailLogRequest;

	private Long userId;

	private String vtlRequestId;

	/**
	 * @return the vtlRequestId
	 */
	public String getVtlRequestId() {
		return vtlRequestId;
	}

	/**
	 * @param vtlRequestId the vtlRequestId to set
	 */
	public void setVtlRequestId(String vtlRequestId) {
		this.vtlRequestId = vtlRequestId;
	}

	public Long getFileDetailsId() {
		return fileDetailsId;
	}

	public void setFileDetailsId(Long fileDetailsId) {
		this.fileDetailsId = fileDetailsId;
	}

	public Integer getFilingStatusId() {
		return filingStatusId;
	}

	public void setFilingStatusId(Integer filingStatusId) {
		this.filingStatusId = filingStatusId;
	}

	public SdmxActivityDetailLogRequest getSdmxActivityDetailLogRequest() {
		return sdmxActivityDetailLogRequest;
	}

	public void setSdmxActivityDetailLogRequest(SdmxActivityDetailLogRequest sdmxActivityDetailLogRequest) {
		this.sdmxActivityDetailLogRequest = sdmxActivityDetailLogRequest;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
