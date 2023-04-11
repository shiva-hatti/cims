/**
 * 
 */
package com.iris.sdmx.elementdimensionmapping.bean;

import java.util.List;

/**
 * @author apagaria
 *
 */
public class ElementDimensionCodeListValueBean {

	private Long elementDimensionId;

	private Long elementId;

	private String dsdCode;

	private String elementLabel;

	private String elementVer;

	private Long dimesnsionMasterId;

	private Long dimesnionTypeId;

	private String dimesnsionTypeName;

	private String dimesnsionName;

	private String dimensionCode;

	private Boolean isCommon;

	private Boolean isMandatory;

	private Long clId;

	private String clCode;

	private String clLable;

	private String clVersion;

	List<CodeListValuesBean> codeListValuesBeans;

	/**
	 * @return the elementDimensionId
	 */
	public Long getElementDimensionId() {
		return elementDimensionId;
	}

	/**
	 * @param elementDimensionId the elementDimensionId to set
	 */
	public void setElementDimensionId(Long elementDimensionId) {
		this.elementDimensionId = elementDimensionId;
	}

	/**
	 * @return the elementId
	 */
	public Long getElementId() {
		return elementId;
	}

	/**
	 * @param elementId the elementId to set
	 */
	public void setElementId(Long elementId) {
		this.elementId = elementId;
	}

	/**
	 * @return the dsdCode
	 */
	public String getDsdCode() {
		return dsdCode;
	}

	/**
	 * @param dsdCode the dsdCode to set
	 */
	public void setDsdCode(String dsdCode) {
		this.dsdCode = dsdCode;
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
	 * @return the elementVer
	 */
	public String getElementVer() {
		return elementVer;
	}

	/**
	 * @param elementVer the elementVer to set
	 */
	public void setElementVer(String elementVer) {
		this.elementVer = elementVer;
	}

	/**
	 * @return the dimesnsionMasterId
	 */
	public Long getDimesnsionMasterId() {
		return dimesnsionMasterId;
	}

	/**
	 * @param dimesnsionMasterId the dimesnsionMasterId to set
	 */
	public void setDimesnsionMasterId(Long dimesnsionMasterId) {
		this.dimesnsionMasterId = dimesnsionMasterId;
	}

	/**
	 * @return the dimesnionTypeId
	 */
	public Long getDimesnionTypeId() {
		return dimesnionTypeId;
	}

	/**
	 * @param dimesnionTypeId the dimesnionTypeId to set
	 */
	public void setDimesnionTypeId(Long dimesnionTypeId) {
		this.dimesnionTypeId = dimesnionTypeId;
	}

	/**
	 * @return the dimesnsionTypeName
	 */
	public String getDimesnsionTypeName() {
		return dimesnsionTypeName;
	}

	/**
	 * @param dimesnsionTypeName the dimesnsionTypeName to set
	 */
	public void setDimesnsionTypeName(String dimesnsionTypeName) {
		this.dimesnsionTypeName = dimesnsionTypeName;
	}

	/**
	 * @return the dimesnsionName
	 */
	public String getDimesnsionName() {
		return dimesnsionName;
	}

	/**
	 * @param dimesnsionName the dimesnsionName to set
	 */
	public void setDimesnsionName(String dimesnsionName) {
		this.dimesnsionName = dimesnsionName;
	}

	/**
	 * @return the dimensionCode
	 */
	public String getDimensionCode() {
		return dimensionCode;
	}

	/**
	 * @param dimensionCode the dimensionCode to set
	 */
	public void setDimensionCode(String dimensionCode) {
		this.dimensionCode = dimensionCode;
	}

	/**
	 * @return
	 */
	public Boolean getIsCommon() {
		return isCommon;
	}

	/**
	 * @param isCommon
	 */
	public void setIsCommon(Boolean isCommon) {
		this.isCommon = isCommon;
	}

	/**
	 * @return
	 */
	public Boolean getIsMandatory() {
		return isMandatory;
	}

	/**
	 * @param isMandatory
	 */
	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	/**
	 * @return the clId
	 */
	public Long getClId() {
		return clId;
	}

	/**
	 * @param clId the clId to set
	 */
	public void setClId(Long clId) {
		this.clId = clId;
	}

	/**
	 * @return the clCode
	 */
	public String getClCode() {
		return clCode;
	}

	/**
	 * @param clCode the clCode to set
	 */
	public void setClCode(String clCode) {
		this.clCode = clCode;
	}

	/**
	 * @return the clLable
	 */
	public String getClLable() {
		return clLable;
	}

	/**
	 * @param clLable the clLable to set
	 */
	public void setClLable(String clLable) {
		this.clLable = clLable;
	}

	/**
	 * @return the clVersion
	 */
	public String getClVersion() {
		return clVersion;
	}

	/**
	 * @param clVersion the clVersion to set
	 */
	public void setClVersion(String clVersion) {
		this.clVersion = clVersion;
	}

	/**
	 * @return the codeListValuesBeans
	 */
	public List<CodeListValuesBean> getCodeListValuesBeans() {
		return codeListValuesBeans;
	}

	/**
	 * @param codeListValuesBeans the codeListValuesBeans to set
	 */
	public void setCodeListValuesBeans(List<CodeListValuesBean> codeListValuesBeans) {
		this.codeListValuesBeans = codeListValuesBeans;
	}
}
