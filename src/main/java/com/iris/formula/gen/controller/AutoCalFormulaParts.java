package com.iris.formula.gen.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class AutoCalFormulaParts implements Serializable {

	private static final long serialVersionUID = -2261384674501425659L;
	@JsonInclude(Include.NON_NULL)
	private String formulaCell;

	@JsonInclude(Include.NON_NULL)
	private String formula;

	@JsonInclude(Include.NON_NULL)
	private String tableId;

	@JsonInclude(Include.NON_NULL)
	private Long formulaId;

	@JsonInclude(Include.NON_NULL)
	private String formulaType;

	@SerializedName(value = "LHS")
	@JsonInclude(Include.NON_NULL)
	private String lhs;

	@SerializedName(value = "EXP")
	@JsonInclude(Include.NON_NULL)
	private String exp;

	@SerializedName(value = "RHS")
	@JsonInclude(Include.NON_NULL)
	private String rhs;

	private Map<String, Object> fallBack = new HashMap<>();

	private Map<String, Object> elementElr = new HashMap<>();

	@JsonInclude(Include.NON_NULL)
	private String errorCode;

	@JsonInclude(Include.NON_NULL)
	private String errorType;

	@JsonInclude(Include.NON_NULL)
	private Integer rowId = 0;

	@JsonInclude(Include.NON_NULL)
	private String formulaRef;

	/**
	 * @return the formulaRef
	 */
	public String getFormulaRef() {
		return formulaRef;
	}

	/**
	 * @param formulaRef the formulaRef to set
	 */
	public void setFormulaRef(String formulaRef) {
		this.formulaRef = formulaRef;
	}

	public String getFormulaCell() {
		return formulaCell;
	}

	public void setFormulaCell(String formulaCell) {
		this.formulaCell = formulaCell;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public Long getFormulaId() {
		return formulaId;
	}

	public void setFormulaId(Long formulaId) {
		this.formulaId = formulaId;
	}

	public String getFormulaType() {
		return formulaType;
	}

	public void setFormulaType(String formulaType) {
		this.formulaType = formulaType;
	}

	public Map<String, Object> getFallBack() {
		return fallBack;
	}

	public void setFallBack(Map<String, Object> fallBack) {
		this.fallBack = fallBack;
	}

	public Map<String, Object> getElementElr() {
		return elementElr;
	}

	public void setElementElr(Map<String, Object> elementElr) {
		this.elementElr = elementElr;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public Integer getRowId() {
		return rowId;
	}

	public void setRowId(Integer rowId) {
		this.rowId = rowId;
	}

	@JsonProperty("LHS")
	public String getLhs() {
		return lhs;
	}

	@JsonProperty("LHS")
	public void setLhs(String lhs) {
		this.lhs = lhs;
	}

	@JsonProperty("EXP")
	public String getExp() {
		return exp;
	}

	@JsonProperty("EXP")
	public void setExp(String exp) {
		this.exp = exp;
	}

	@JsonProperty("RHS")
	public String getRhs() {
		return rhs;
	}

	@JsonProperty("RHS")
	public void setRhs(String rhs) {
		this.rhs = rhs;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

}
