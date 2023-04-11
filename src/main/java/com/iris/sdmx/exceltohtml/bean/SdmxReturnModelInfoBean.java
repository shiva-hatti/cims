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
public class SdmxReturnModelInfoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long returnModelInfoId;

	private Long returnSheetInfoId;

	private Long returnSheetIdFk;

	private Long returnTemplateId;

	private String sheetCode;

	private String sheetName;

	private String sectionCode;

	private String sectionName;

	private Integer returnCellRef;

	private Long modelCodesIdFk;

	private String modelDim;

	private String modelCode;

	private Long elementIdFk;

	private Boolean isActive;

	private Long createdBy;

	private String createdByName;

	private Date createdOn;

	private Boolean isMandatory;

	private String cellFormula;

	/* private Long returnPreviewIdFk; */

	/**
	 * @return the returnPreviewIdFk
	 */
	/*
	 * public Long getReturnPreviewIdFk() { return returnPreviewIdFk; }
	 */

	/**
	 * @param returnPreviewIdFk the returnPreviewIdFk to set
	 */
	/*
	 * public void setReturnPreviewIdFk(Long returnPreviewIdFk) {
	 * this.returnPreviewIdFk = returnPreviewIdFk; }
	 */

	/**
	 * @return the returnModelInfoId
	 */
	public Long getReturnModelInfoId() {
		return returnModelInfoId;
	}

	/**
	 * @param returnModelInfoId the returnModelInfoId to set
	 */
	public void setReturnModelInfoId(Long returnModelInfoId) {
		this.returnModelInfoId = returnModelInfoId;
	}

	/**
	 * @return the returnSheetInfoId
	 */
	public Long getReturnSheetInfoId() {
		return returnSheetInfoId;
	}

	/**
	 * @param returnSheetInfoId the returnSheetInfoId to set
	 */
	public void setReturnSheetInfoId(Long returnSheetInfoId) {
		this.returnSheetInfoId = returnSheetInfoId;
	}

	/**
	 * @return the returnSheetIdFk
	 */
	public Long getReturnSheetIdFk() {
		return returnSheetIdFk;
	}

	/**
	 * @param returnSheetIdFk the returnSheetIdFk to set
	 */
	public void setReturnSheetIdFk(Long returnSheetIdFk) {
		this.returnSheetIdFk = returnSheetIdFk;
	}

	/**
	 * @return the returnTemplateId
	 */
	public Long getReturnTemplateId() {
		return returnTemplateId;
	}

	/**
	 * @param returnTemplateId the returnTemplateId to set
	 */
	public void setReturnTemplateId(Long returnTemplateId) {
		this.returnTemplateId = returnTemplateId;
	}

	/**
	 * @return the sheetCode
	 */
	public String getSheetCode() {
		return sheetCode;
	}

	/**
	 * @param sheetCode the sheetCode to set
	 */
	public void setSheetCode(String sheetCode) {
		this.sheetCode = Validations.trimInput(sheetCode);
	}

	/**
	 * @return the sheetName
	 */
	public String getSheetName() {
		return sheetName;
	}

	/**
	 * @param sheetName the sheetName to set
	 */
	public void setSheetName(String sheetName) {
		this.sheetName = Validations.trimInput(sheetName);
	}

	/**
	 * @return the sectionCode
	 */
	public String getSectionCode() {
		return sectionCode;
	}

	/**
	 * @param sectionCode the sectionCode to set
	 */
	public void setSectionCode(String sectionCode) {
		this.sectionCode = Validations.trimInput(sectionCode);
	}

	/**
	 * @return the sectionName
	 */
	public String getSectionName() {
		return sectionName;
	}

	/**
	 * @param sectionName the sectionName to set
	 */
	public void setSectionName(String sectionName) {
		this.sectionName = Validations.trimInput(sectionName);
	}

	/**
	 * @return the returnCellRef
	 */
	public Integer getReturnCellRef() {
		return returnCellRef;
	}

	/**
	 * @param returnCellRef the returnCellRef to set
	 */
	public void setReturnCellRef(Integer returnCellRef) {
		this.returnCellRef = returnCellRef;
	}

	/**
	 * @return the modelCodesIdFk
	 */
	public Long getModelCodesIdFk() {
		return modelCodesIdFk;
	}

	/**
	 * @param modelCodesIdFk the modelCodesIdFk to set
	 */
	public void setModelCodesIdFk(Long modelCodesIdFk) {
		this.modelCodesIdFk = modelCodesIdFk;
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
		this.createdByName = Validations.trimInput(createdByName);
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
	 * @return the isMandatory
	 */
	public Boolean getIsMandatory() {
		return isMandatory;
	}

	/**
	 * @param isMandatory the isMandatory to set
	 */
	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	/**
	 * @return the cellFormula
	 */
	public String getCellFormula() {
		return cellFormula;
	}

	/**
	 * @param cellFormula the cellFormula to set
	 */
	public void setCellFormula(String cellFormula) {
		this.cellFormula = cellFormula;
	}

}
