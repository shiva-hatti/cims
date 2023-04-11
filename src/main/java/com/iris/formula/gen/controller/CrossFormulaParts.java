package com.iris.formula.gen.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CrossFormulaParts implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2261384674501425659L;
	private String formulaCell;
	private String formula;
	private Integer tableId;
	private Integer formulaId;
	private String formulaType;
	@JsonProperty("LHS")
	private String lhs;
	@JsonProperty("EXP")
	private String exp;
	@JsonProperty("RHS")
	private String rhs;
	private Map<String, Object> fallBack = new HashMap<>();
	private Map<String, Integer> elementElr = new HashMap<>();
	private String errorCode;
	private Integer rowId = 0;

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

	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	public Integer getFormulaId() {
		return formulaId;
	}

	public void setFormulaId(Integer formulaId) {
		this.formulaId = formulaId;
	}

	public String getFormulaType() {
		return formulaType;
	}

	public void setFormulaType(String formulaType) {
		this.formulaType = formulaType;
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

	public Map<String, Object> getFallBack() {
		return fallBack;
	}

	public void setFallBack(Map<String, Object> fallBack) {
		this.fallBack = fallBack;
	}

	public Map<String, Integer> getElementElr() {
		return elementElr;
	}

	public void setElementElr(Map<String, Integer> elementElr) {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
