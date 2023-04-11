
package com.iris.dto;

import java.io.Serializable;
import java.util.List;

import com.iris.util.Validations;

/**
 * @author bthakare
 * @date 18/06/2020
 * @Version 1.0
 */
public class ReturnTemplateDto implements Serializable {

	private static final long serialVersionUID = 2262419540923802502L;

	private Long retTemplateId;
	private String returnCode;
	private String returnName;
	private String versionNumber;
	private String versionDesc;
	private String csvFileName;
	private String taxonomyName;
	private String validFromDate;
	private String uploadedBy;
	private Long uploadedOn;
	private List<String> returnCodeList;
	private String langCode;
	private String dateFormat;
	private String returnType;
	private int returnSectionId;
	private String xsdFileName;
	private List<Long> returnIdList;
	private String returnTypeSection;
	private Boolean isFormulaGenAction;
	private List<HeaderInfoDto> mappedHeaderInfoList;
	private List<HeaderInfoDto> unMappedHeaderInfoList;
	private Long validFromDateLong;
	private String returnPackage;

	public Long getRetTemplateId() {
		return retTemplateId;
	}

	public void setRetTemplateId(Long retTemplateId) {
		this.retTemplateId = retTemplateId;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = Validations.trimInput(returnCode);
	}

	public String getReturnName() {
		return returnName;
	}

	public void setReturnName(String returnName) {
		this.returnName = Validations.trimInput(returnName);
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = Validations.trimInput(versionNumber);
	}

	public String getVersionDesc() {
		return versionDesc;
	}

	public void setVersionDesc(String versionDesc) {
		this.versionDesc = Validations.trimInput(versionDesc);
	}

	public String getTaxonomyName() {
		return taxonomyName;
	}

	public void setTaxonomyName(String taxonomyName) {
		this.taxonomyName = Validations.trimInput(taxonomyName);
	}

	public String getValidFromDate() {
		return validFromDate;
	}

	public void setValidFromDate(String validFromDate) {
		this.validFromDate = Validations.trimInput(validFromDate);
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = Validations.trimInput(uploadedBy);
	}

	public String getCsvFileName() {
		return csvFileName;
	}

	public void setCsvFileName(String csvFileName) {
		this.csvFileName = Validations.trimInput(csvFileName);
	}

	public Long getUploadedOn() {
		return uploadedOn;
	}

	public void setUploadedOn(Long uploadedOn) {
		this.uploadedOn = uploadedOn;
	}

	public List<String> getReturnCodeList() {
		return returnCodeList;
	}

	public void setReturnCodeList(List<String> returnCodeList) {
		this.returnCodeList = returnCodeList;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = Validations.trimInput(langCode);
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = Validations.trimInput(dateFormat);
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public int getReturnSectionId() {
		return returnSectionId;
	}

	public void setReturnSectionId(int returnSectionId) {
		this.returnSectionId = returnSectionId;
	}

	public String getXsdFileName() {
		return xsdFileName;
	}

	public void setXsdFileName(String xsdFileName) {
		this.xsdFileName = Validations.trimInput(xsdFileName);
	}

	/**
	 * @return the returnIdList
	 */
	public List<Long> getReturnIdList() {
		return returnIdList;
	}

	/**
	 * @param returnIdList the returnIdList to set
	 */
	public void setReturnIdList(List<Long> returnIdList) {
		this.returnIdList = returnIdList;
	}

	public String getReturnTypeSection() {
		return returnTypeSection;
	}

	public void setReturnTypeSection(String returnTypeSection) {
		this.returnTypeSection = returnTypeSection;
	}

	public Boolean getIsFormulaGenAction() {
		return isFormulaGenAction;
	}

	public void setIsFormulaGenAction(Boolean isFormulaGenAction) {
		this.isFormulaGenAction = isFormulaGenAction;
	}

	public List<HeaderInfoDto> getMappedHeaderInfoList() {
		return mappedHeaderInfoList;
	}

	public void setMappedHeaderInfoList(List<HeaderInfoDto> mappedHeaderInfoList) {
		this.mappedHeaderInfoList = mappedHeaderInfoList;
	}

	public List<HeaderInfoDto> getUnMappedHeaderInfoList() {
		return unMappedHeaderInfoList;
	}

	public void setUnMappedHeaderInfoList(List<HeaderInfoDto> unMappedHeaderInfoList) {
		this.unMappedHeaderInfoList = unMappedHeaderInfoList;
	}

	public Long getValidFromDateLong() {
		return validFromDateLong;
	}

	public void setValidFromDateLong(Long validFromDateLong) {
		this.validFromDateLong = validFromDateLong;
	}

	public String getReturnPackage() {
		return returnPackage;
	}

	public void setReturnPackage(String returnPackage) {
		this.returnPackage = Validations.trimInput(returnPackage);
	}
}
