/**
 * 
 */
package com.iris.sdmx.exceltohtml.bean;

/**
 * @author apagaria
 *
 */
public class ModelCodeInputBean {

	private String modelDim;

	private Long elementIdFk;

	private String sheetCode;

	private String sheetName;

	private String sectionCode;

	private String sectionName;

	private String modelCode;

	private Integer returnCellRef;

	private Long returnTemplateIdFk;

	private Boolean isMandatoryCell;

	private String isDependentCell;

	private String regexPatternWithId;

	private Integer regexMinLength;

	private Integer regexMaxLength;

	private String cellFormula;

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
		this.modelDim = modelDim;
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
	 * @return the sheetCode
	 */
	public String getSheetCode() {
		return sheetCode;
	}

	/**
	 * @param sheetCode the sheetCode to set
	 */
	public void setSheetCode(String sheetCode) {
		this.sheetCode = sheetCode;
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
		this.sheetName = sheetName;
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
		this.sectionCode = sectionCode;
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
		this.sectionName = sectionName;
	}

	/**
	 * @return
	 */
	public String getModelCode() {
		return modelCode;
	}

	/**
	 * @param modelCode
	 */
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
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
	 * @return the returnTemplateIdFk
	 */
	public Long getReturnTemplateIdFk() {
		return returnTemplateIdFk;
	}

	/**
	 * @param returnTemplateIdFk the returnTemplateIdFk to set
	 */
	public void setReturnTemplateIdFk(Long returnTemplateIdFk) {
		this.returnTemplateIdFk = returnTemplateIdFk;
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
	 * @return the regexPatternWithId
	 */
	public String getRegexPatternWithId() {
		return regexPatternWithId;
	}

	/**
	 * @param regexPatternWithId the regexPatternWithId to set
	 */
	public void setRegexPatternWithId(String regexPatternWithId) {
		this.regexPatternWithId = regexPatternWithId;
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
