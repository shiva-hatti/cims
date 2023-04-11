/**
 * 
 */
package com.iris.sdmx.exceltohtml.bean;

import java.io.Serializable;
import java.util.Date;

import com.iris.util.Validations;

/**
 * @author apagaria
 *
 */
public class SdmxModelCodesBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Long modelCodesId;

	private String modelDim;

	private String modelDimHash;

	private String modelCode;

	private Long elementIdFk;

	private String elementVer;

	private String elementLabel;

	private String dsdCode;

	private Boolean isActive;

	private Long createdBy;

	private String createdByName;

	private Date createdOn;

	private Boolean isMandatoryCell;

	private String isDependentCell;

	private Integer regexIdFk;

	private String regexPattern;

	private Integer regexMinLength;

	private Integer regexMaxLength;

	/**
	 * 
	 */
	public SdmxModelCodesBean() {
	}

	/**
	 * @param modelCodesId
	 * @param modelDim
	 * @param modelDimHash
	 * @param modelCode
	 * @param elementIdFk
	 * @param elementVer
	 * @param elementLabel
	 * @param dsdCode
	 * @param isActive
	 * @param createdBy
	 * @param createdByName
	 * @param createdOn
	 * @param isMandatoryCell
	 */
	public SdmxModelCodesBean(Long modelCodesId, String modelDim, String modelDimHash, String modelCode, Long elementIdFk, String elementVer, String elementLabel, String dsdCode, Boolean isActive, Long createdBy, String createdByName, Date createdOn, Boolean isMandatoryCell, Integer regexId, String regex) {
		this.modelCodesId = modelCodesId;
		this.modelDim = modelDim;
		this.modelDimHash = modelDimHash;
		this.modelCode = modelCode;
		this.elementIdFk = elementIdFk;
		this.elementVer = elementVer;
		this.elementLabel = elementLabel;
		this.dsdCode = dsdCode;
		this.isActive = isActive;
		this.createdBy = createdBy;
		this.createdByName = createdByName;
		this.createdOn = createdOn;
		this.isMandatoryCell = isMandatoryCell;
		this.regexIdFk = regexId;
		this.regexPattern = regex;

	}

	public SdmxModelCodesBean(Long modelCodesId, String modelDim, String modelDimHash, String modelCode, Long elementIdFk, String elementVer, String elementLabel, String dsdCode, Boolean isActive, Long createdBy, String createdByName, Date createdOn, Integer regexId, String regex) {
		this.modelCodesId = modelCodesId;
		this.modelDim = modelDim;
		this.modelDimHash = modelDimHash;
		this.modelCode = modelCode;
		this.elementIdFk = elementIdFk;
		this.elementVer = elementVer;
		this.elementLabel = elementLabel;
		this.dsdCode = dsdCode;
		this.isActive = isActive;
		this.createdBy = createdBy;
		this.createdByName = createdByName;
		this.createdOn = createdOn;
		this.regexIdFk = regexId;
		this.regexPattern = regex;

	}

	/**
	 * @return the modelCodesId
	 */
	public Long getModelCodesId() {
		return modelCodesId;
	}

	/**
	 * @param modelCodesId the modelCodesId to set
	 */
	public void setModelCodesId(Long modelCodesId) {
		this.modelCodesId = modelCodesId;
	}

	/**
	 * @return the modelDim
	 */
	public String getModelDim() {
		return modelDim;
	}

	/**
	 * @param modelDim the modelDim to set
	 */
	public void setModelDim(String modelDim) {
		this.modelDim = Validations.trimInput(modelDim);
	}

	/**
	 * @return the modelDimHash
	 */
	public String getModelDimHash() {
		return modelDimHash;
	}

	/**
	 * @param modelDimHash the modelDimHash to set
	 */
	public void setModelDimHash(String modelDimHash) {
		this.modelDimHash = modelDimHash;
	}

	/**
	 * @return the modelCode
	 */
	public String getModelCode() {
		return modelCode;
	}

	/**
	 * @param modelCode the modelCode to set
	 */
	public void setModelCode(String modelCode) {
		this.modelCode = Validations.trimInput(modelCode);
	}

	/**
	 * @return the elementIdFk
	 */
	public Long getElementIdFk() {
		return elementIdFk;
	}

	/**
	 * @param elementIdFk the elementIdFk to set
	 */
	public void setElementIdFk(Long elementIdFk) {
		this.elementIdFk = elementIdFk;
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
		this.elementVer = Validations.trimInput(elementVer);
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
		this.elementLabel = Validations.trimInput(elementLabel);
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
		this.dsdCode = Validations.trimInput(dsdCode);
	}

	/**
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the createdBy
	 */
	public Long getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdByName
	 */
	public String getCreatedByName() {
		return createdByName;
	}

	/**
	 * @param createdByName the createdByName to set
	 */
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the isMandatoryCell
	 */
	public Boolean getIsMandatoryCell() {
		return isMandatoryCell;
	}

	/**
	 * @param isMandatoryCell the isMandatoryCell to set
	 */
	public void setIsMandatoryCell(Boolean isMandatoryCell) {
		this.isMandatoryCell = isMandatoryCell;
	}

	/**
	 * @return the isDependentCell
	 */
	public String getIsDependentCell() {
		return isDependentCell;
	}

	/**
	 * @param isDependentCell the isDependentCell to set
	 */
	public void setIsDependentCell(String isDependentCell) {
		this.isDependentCell = isDependentCell;
	}

	/**
	 * @return the regexPattern
	 */
	public String getRegexPattern() {
		return regexPattern;
	}

	/**
	 * @param regexPattern the regexPattern to set
	 */
	public void setRegexPattern(String regexPattern) {
		this.regexPattern = regexPattern;
	}

	/**
	 * @return the regexMinLength
	 */
	public Integer getRegexMinLength() {
		return regexMinLength;
	}

	/**
	 * @param regexMinLength the regexMinLength to set
	 */
	public void setRegexMinLength(Integer regexMinLength) {
		this.regexMinLength = regexMinLength;
	}

	/**
	 * @return the regexMaxLength
	 */
	public Integer getRegexMaxLength() {
		return regexMaxLength;
	}

	/**
	 * @param regexMaxLength the regexMaxLength to set
	 */
	public void setRegexMaxLength(Integer regexMaxLength) {
		this.regexMaxLength = regexMaxLength;
	}

	/**
	 * @return the regexIdFk
	 */
	public Integer getRegexIdFk() {
		return regexIdFk;
	}

	/**
	 * @param regexIdFk the regexIdFk to set
	 */
	public void setRegexIdFk(Integer regexIdFk) {
		this.regexIdFk = regexIdFk;
	}
}
