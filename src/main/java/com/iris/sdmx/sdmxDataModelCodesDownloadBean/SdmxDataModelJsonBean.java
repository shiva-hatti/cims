package com.iris.sdmx.sdmxDataModelCodesDownloadBean;

import java.io.Serializable;
import java.util.List;

public class SdmxDataModelJsonBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1689917858073282201L;

	private String elementCode;
	private String elementVersion;
	private String agencyName;

	List<SdmxDataModelDimJsonBean> sdmxDataModelDimJsonBeans;

	/**
	 * @return the elementCode
	 */
	public String getElementCode() {
		return elementCode;
	}

	/**
	 * @param elementCode the elementCode to set
	 */
	public void setElementCode(String elementCode) {
		this.elementCode = elementCode;
	}

	/**
	 * @return the elementVersion
	 */
	public String getElementVersion() {
		return elementVersion;
	}

	/**
	 * @param elementVersion the elementVersion to set
	 */
	public void setElementVersion(String elementVersion) {
		this.elementVersion = elementVersion;
	}

	/**
	 * @return the agencyName
	 */
	public String getAgencyName() {
		return agencyName;
	}

	/**
	 * @param agencyName the agencyName to set
	 */
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	/**
	 * @return the sdmxDataModelDimJsonBeans
	 */
	public List<SdmxDataModelDimJsonBean> getSdmxDataModelDimJsonBeans() {
		return sdmxDataModelDimJsonBeans;
	}

	/**
	 * @param sdmxDataModelDimJsonBeans the sdmxDataModelDimJsonBeans to set
	 */
	public void setSdmxDataModelDimJsonBeans(List<SdmxDataModelDimJsonBean> sdmxDataModelDimJsonBeans) {
		this.sdmxDataModelDimJsonBeans = sdmxDataModelDimJsonBeans;
	}

}
