package com.iris.sdmx.upload.bean;

import java.io.Serializable;

public class SdmxModelCodeLiteBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 896964929007563867L;

	private String hashCode;

	private String modelCode;

	private String dsdCode;

	private String dsdVersion;

	private Integer returnCellRefNo;

	public SdmxModelCodeLiteBean() {

	}

	public SdmxModelCodeLiteBean(String hashCode, String modelCode, String dsdCode, String dsdVersion, Integer returnCellRefNo) {
		this.hashCode = hashCode;
		this.modelCode = modelCode;
		this.dsdCode = dsdCode;
		this.dsdVersion = dsdVersion;
		this.returnCellRefNo = returnCellRefNo;
	}

	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getDsdCode() {
		return dsdCode;
	}

	public void setDsdCode(String dsdCode) {
		this.dsdCode = dsdCode;
	}

	public String getDsdVersion() {
		return dsdVersion;
	}

	public void setDsdVersion(String dsdVersion) {
		this.dsdVersion = dsdVersion;
	}

	public Integer getReturnCellRefNo() {
		return returnCellRefNo;
	}

	public void setReturnCellRefNo(Integer returnCellRefNo) {
		this.returnCellRefNo = returnCellRefNo;
	}

}
