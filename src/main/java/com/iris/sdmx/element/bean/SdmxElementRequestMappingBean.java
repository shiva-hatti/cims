package com.iris.sdmx.element.bean;

import java.io.Serializable;

public class SdmxElementRequestMappingBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long elementId;

	private String elementCode;

	private String elementVersion;

	private String elementLabel;

	private String elementDesc;

	private String agencyMasterCode;

	public Long getElementId() {
		return elementId;
	}

	public void setElementId(Long elementId) {
		this.elementId = elementId;
	}

	public String getElementCode() {
		return elementCode;
	}

	public void setElementCode(String elementCode) {
		this.elementCode = elementCode;
	}

	public String getElementVersion() {
		return elementVersion;
	}

	public void setElementVersion(String elementVersion) {
		this.elementVersion = elementVersion;
	}

	/**
	 * @return the elementLabel
	 */
	public String getElementLabel() {
		return elementLabel;
	}

	/**
	 * @param elementLabel the elementLabel to set
	 */
	public void setElementLabel(String elementLabel) {
		this.elementLabel = elementLabel;
	}

	/**
	 * @return the elementDesc
	 */
	public String getElementDesc() {
		return elementDesc;
	}

	/**
	 * @param elementDesc the elementDesc to set
	 */
	public void setElementDesc(String elementDesc) {
		this.elementDesc = elementDesc;
	}

	/**
	 * @return the agencyMasterCode
	 */
	public String getAgencyMasterCode() {
		return agencyMasterCode;
	}

	/**
	 * @param agencyMasterCode the agencyMasterCode to set
	 */
	public void setAgencyMasterCode(String agencyMasterCode) {
		this.agencyMasterCode = agencyMasterCode;
	}

}
