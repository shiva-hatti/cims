package com.iris.dto;

import java.io.Serializable;

/**
 * @author Siddique
 */
public class TableElementMetaInfo implements Serializable {

	private static final long serialVersionUID = 1576591851106639703L;
	private String csvColumnPosition;
	private String csvTableCode;
	private String csvRowPosition;
	private long maxLength;
	private long minLength;
	private String regex;
	private boolean isMandatory;
	private long fieldTypeId;
	private boolean isGrandTotalPresent;
	private long totalRowCount;
	private String methodURI;
	private String methodType;
	private String tableName;
	private boolean isNullAllow;
	private String formula;
	private String errorCode;
	private boolean isRowPositionCheck;
	private boolean isValidationOnParticularRow;
	private String formulaTag;
	private String conditionTag;
	private String formulaType;
	private boolean rowCountValidation;
	private String maxRowCount;
	private boolean isRepeatable;
	private boolean isKeyFetch;
	private Boolean isUniqueValueCheck;
	private String errorCodeForUniqueValueCheck;
	private String formulaForUniqueValueCheck;
	private boolean isPairCheck;
	private String splitType;
	private String errorCodeForMonthCompare;
	private String errorCodeForYearCompare;
	private String conditionForYearCompare;
	private String conditionForMonthCompare;
	private String reportedDateFormat;
	private boolean isMonthComparison;
	private boolean isYearComparison;

	public String getErrorCodeForMonthCompare() {
		return errorCodeForMonthCompare;
	}

	public void setErrorCodeForMonthCompare(String errorCodeForMonthCompare) {
		this.errorCodeForMonthCompare = errorCodeForMonthCompare;
	}

	public String getErrorCodeForYearCompare() {
		return errorCodeForYearCompare;
	}

	public void setErrorCodeForYearCompare(String errorCodeForYearCompare) {
		this.errorCodeForYearCompare = errorCodeForYearCompare;
	}

	public String getConditionForYearCompare() {
		return conditionForYearCompare;
	}

	public void setConditionForYearCompare(String conditionForYearCompare) {
		this.conditionForYearCompare = conditionForYearCompare;
	}

	public String getConditionForMonthCompare() {
		return conditionForMonthCompare;
	}

	public void setConditionForMonthCompare(String conditionForMonthCompare) {
		this.conditionForMonthCompare = conditionForMonthCompare;
	}

	public String getReportedDateFormat() {
		return reportedDateFormat;
	}

	public void setReportedDateFormat(String reportedDateFormat) {
		this.reportedDateFormat = reportedDateFormat;
	}

	public boolean isMonthComparison() {
		return isMonthComparison;
	}

	public void setMonthComparison(boolean isMonthComparison) {
		this.isMonthComparison = isMonthComparison;
	}

	public boolean isYearComparison() {
		return isYearComparison;
	}

	public void setYearComparison(boolean isYearComparison) {
		this.isYearComparison = isYearComparison;
	}

	public boolean isPairCheck() {
		return isPairCheck;
	}

	public void setPairCheck(boolean isPairCheck) {
		this.isPairCheck = isPairCheck;
	}

	public String getSplitType() {
		return splitType;
	}

	public void setSplitType(String splitType) {
		this.splitType = splitType;
	}

	public String getFormulaForUniqueValueCheck() {
		return formulaForUniqueValueCheck;
	}

	public void setFormulaForUniqueValueCheck(String formulaForUniqueValueCheck) {
		this.formulaForUniqueValueCheck = formulaForUniqueValueCheck;
	}

	public String getErrorCodeForUniqueValueCheck() {
		return errorCodeForUniqueValueCheck;
	}

	public void setErrorCodeForUniqueValueCheck(String errorCodeForUniqueValueCheck) {
		this.errorCodeForUniqueValueCheck = errorCodeForUniqueValueCheck;
	}

	public Boolean getIsUniqueValueCheck() {
		return isUniqueValueCheck;
	}

	public void setIsUniqueValueCheck(Boolean isUniqueValueCheck) {
		this.isUniqueValueCheck = isUniqueValueCheck;
	}

	public boolean isKeyFetch() {
		return isKeyFetch;
	}

	public void setKeyFetch(boolean isKeyFetch) {
		this.isKeyFetch = isKeyFetch;
	}

	/**
	 * @return the isRepeatable
	 */
	public boolean isRepeatable() {
		return isRepeatable;
	}

	/**
	 * @param isRepeatable the isRepeatable to set
	 */
	public void setRepeatable(boolean isRepeatable) {
		this.isRepeatable = isRepeatable;
	}

	public String getMaxRowCount() {
		return maxRowCount;
	}

	public void setMaxRowCount(String maxRowCount) {
		this.maxRowCount = maxRowCount;
	}

	public boolean isRowCountValidation() {
		return rowCountValidation;
	}

	public void setRowCountValidation(boolean rowCountValidation) {
		this.rowCountValidation = rowCountValidation;
	}

	public String getFormulaType() {
		return formulaType;
	}

	public void setFormulaType(String formulaType) {
		this.formulaType = formulaType;
	}

	public boolean isRowPositionCheck() {
		return isRowPositionCheck;
	}

	public void setRowPositionCheck(boolean isRowPositionCheck) {
		this.isRowPositionCheck = isRowPositionCheck;
	}

	public boolean isValidationOnParticularRow() {
		return isValidationOnParticularRow;
	}

	public void setValidationOnParticularRow(boolean isValidationOnParticularRow) {
		this.isValidationOnParticularRow = isValidationOnParticularRow;
	}

	public String getFormulaTag() {
		return formulaTag;
	}

	public void setFormulaTag(String formulaTag) {
		this.formulaTag = formulaTag;
	}

	public String getConditionTag() {
		return conditionTag;
	}

	public void setConditionTag(String conditionTag) {
		this.conditionTag = conditionTag;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public boolean isNullAllow() {
		return isNullAllow;
	}

	public void setNullAllow(boolean isNullAllow) {
		this.isNullAllow = isNullAllow;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getMethodURI() {
		return methodURI;
	}

	public void setMethodURI(String methodURI) {
		this.methodURI = methodURI;
	}

	public String getMethodType() {
		return methodType;
	}

	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}

	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public long getTotalRowCount() {
		return totalRowCount;
	}

	public void setTotalRowCount(long totalRowCount) {
		this.totalRowCount = totalRowCount;
	}

	/**
	 * @return the isGrandTotalPresent
	 */
	public boolean isGrandTotalPresent() {
		return isGrandTotalPresent;
	}

	/**
	 * @param isGrandTotalPresent the isGrandTotalPresent to set
	 */
	public void setGrandTotalPresent(boolean isGrandTotalPresent) {
		this.isGrandTotalPresent = isGrandTotalPresent;
	}

	/**
	 * @return the fieldTypeId
	 */
	public long getFieldTypeId() {
		return fieldTypeId;
	}

	/**
	 * @param fieldTypeId the fieldTypeId to set
	 */
	public void setFieldTypeId(long fieldTypeId) {
		this.fieldTypeId = fieldTypeId;
	}

	/**
	 * @return the maxLength
	 */
	public long getMaxLength() {
		return maxLength;
	}

	/**
	 * @param maxLength the maxLength to set
	 */
	public void setMaxLength(long maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * @return the minLength
	 */
	public long getMinLength() {
		return minLength;
	}

	/**
	 * @param minLength the minLength to set
	 */
	public void setMinLength(long minLength) {
		this.minLength = minLength;
	}

	/**
	 * @return the regex
	 */
	public String getRegex() {
		return regex;
	}

	/**
	 * @param regex the regex to set
	 */
	public void setRegex(String regex) {
		this.regex = regex;
	}

	/**
	 * @return the isMandatory
	 */
	public boolean getIsMandatory() {
		return isMandatory;
	}

	/**
	 * @param isMandatory the isMandatory to set
	 */
	public void setIsMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	/**
	 * @return the csvRowPosition
	 */
	public String getCsvRowPosition() {
		return csvRowPosition;
	}

	/**
	 * @param csvRowPosition the csvRowPosition to set
	 */
	public void setCsvRowPosition(String csvRowPosition) {
		this.csvRowPosition = csvRowPosition;
	}

	/**
	 * @return the csvColumnPosition
	 */
	public String getCsvColumnPosition() {
		return csvColumnPosition;
	}

	/**
	 * @param csvColumnPosition the csvColumnPosition to set
	 */
	public void setCsvColumnPosition(String csvColumnPosition) {
		this.csvColumnPosition = csvColumnPosition;
	}

	/**
	 * @return the csvTableCode
	 */
	public String getCsvTableCode() {
		return csvTableCode;
	}

	/**
	 * @param csvTableCode the csvTableCode to set
	 */
	public void setCsvTableCode(String csvTableCode) {
		this.csvTableCode = csvTableCode;
	}

}