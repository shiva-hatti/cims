package com.iris.sdmx.sdmxDataModelCodesDownloadBean;

import java.io.Serializable;

public class SdmxDataModelDimJsonBean implements Serializable {

	/**
	 * @author sdhone
	 */
	private static final long serialVersionUID = 7336321008542627337L;

	private String dimCode;
	private String dimValue;

	/**
	 * @return the dimCode
	 */
	public String getDimCode() {
		return dimCode;
	}

	/**
	 * @param dimCode the dimCode to set
	 */
	public void setDimCode(String dimCode) {
		this.dimCode = dimCode;
	}

	/**
	 * @return the dimValue
	 */
	public String getDimValue() {
		return dimValue;
	}

	/**
	 * @param dimValue the dimValue to set
	 */
	public void setDimValue(String dimValue) {
		this.dimValue = dimValue;
	}

}
