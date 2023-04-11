package com.iris.formula.gen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iris.formula.gen.controller.AutoCalFormulaParts;
import com.iris.model.ErrorCodeDetail;

public class FormulaBean implements Serializable {

	private static final long serialVersionUID = -6590192323182741600L;

	private String returnId;
	private String formula;
	private String errorCode;
	private String formulaType;
	private String dataType;
	private String errorMessage;
	private String returnCode;
	private String elrIdPair;
	private Map<String, List<AutoCalFormulaParts>> normalFormula = new HashMap<>();
	private Boolean isCrossElr;
	private Long formulaId;
	private List<AutoCalFormulaParts> crossElrFormula = new ArrayList<>();
	private Long userId;
	private String table;
	private Map<String, String> formulaJsonMap = new HashMap<>();
	private String technicalErrorCode;
	private String oldTechnicalErrorCode;
	private Long returnTemplateId;
	private String errorType;
	private ErrorCodeDetail errorCodeDetail;
	private String errorEvent;
	private Map<String, ErrorCodeDetail> errorCodeDetailMap = new HashMap<>();
	private String eventType;
	private Map<String, String> normalFormulaJsonString = new HashMap<>();
	private String includePreviousFormula;
	private Map<String, FormulaBean> formulaBeanMap = new HashMap<>();
	private Boolean isUpdatedFormula;
	private Map<Long, Integer> tableIdElrIdMap = new HashMap<>();
	private Boolean isFormulaPresentAlready;
	private Boolean isActive;
	private Boolean isSaveAsDraft;
	private Integer action;
	private Map<String, Long> formulaIdMap = new HashMap<>();
	private String zipFileName;
	private String dyanmicTableId;

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getReturnId() {
		return returnId;
	}

	public void setReturnId(String returnId) {
		this.returnId = returnId;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public Boolean getIsCrossElr() {
		return isCrossElr;
	}

	public void setIsCrossElr(Boolean isCrossElr) {
		this.isCrossElr = isCrossElr;
	}

	public List<AutoCalFormulaParts> getCrossElrFormula() {
		return crossElrFormula;
	}

	public void setCrossElrFormula(List<AutoCalFormulaParts> crossElrFormula) {
		this.crossElrFormula = crossElrFormula;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getFormulaType() {
		return formulaType;
	}

	public void setFormulaType(String formulaType) {
		this.formulaType = formulaType;
	}

	public String getElrIdPair() {
		return elrIdPair;
	}

	public void setElrIdPair(String elrIdPair) {
		this.elrIdPair = elrIdPair;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Long getFormulaId() {
		return formulaId;
	}

	public void setFormulaId(Long formulaId) {
		this.formulaId = formulaId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public Map<String, String> getFormulaJsonMap() {
		return formulaJsonMap;
	}

	public void setFormulaJsonMap(Map<String, String> formulaJsonMap) {
		this.formulaJsonMap = formulaJsonMap;
	}

	public String getTechnicalErrorCode() {
		return technicalErrorCode;
	}

	public void setTechnicalErrorCode(String technicalErrorCode) {
		this.technicalErrorCode = technicalErrorCode;
	}

	public String getOldTechnicalErrorCode() {
		return oldTechnicalErrorCode;
	}

	public void setOldTechnicalErrorCode(String oldTechnicalErrorCode) {
		this.oldTechnicalErrorCode = oldTechnicalErrorCode;
	}

	public Long getReturnTemplateId() {
		return returnTemplateId;
	}

	public void setReturnTemplateId(Long returnTemplateId) {
		this.returnTemplateId = returnTemplateId;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public ErrorCodeDetail getErrorCodeDetail() {
		return errorCodeDetail;
	}

	public void setErrorCodeDetail(ErrorCodeDetail errorCodeDetail) {
		this.errorCodeDetail = errorCodeDetail;
	}

	public String getErrorEvent() {
		return errorEvent;
	}

	public void setErrorEvent(String errorEvent) {
		this.errorEvent = errorEvent;
	}

	public Map<String, ErrorCodeDetail> getErrorCodeDetailMap() {
		return errorCodeDetailMap;
	}

	public void setErrorCodeDetailMap(Map<String, ErrorCodeDetail> errorCodeDetailMap) {
		this.errorCodeDetailMap = errorCodeDetailMap;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Map<String, List<AutoCalFormulaParts>> getNormalFormula() {
		return normalFormula;
	}

	public void setNormalFormula(Map<String, List<AutoCalFormulaParts>> normalFormula) {
		this.normalFormula = normalFormula;
	}

	public String getIncludePreviousFormula() {
		return includePreviousFormula;
	}

	public void setIncludePreviousFormula(String includePreviousFormula) {
		this.includePreviousFormula = includePreviousFormula;
	}

	public Map<String, String> getNormalFormulaJsonString() {
		return normalFormulaJsonString;
	}

	public void setNormalFormulaJsonString(Map<String, String> normalFormulaJsonString) {
		this.normalFormulaJsonString = normalFormulaJsonString;
	}

	public Map<String, FormulaBean> getFormulaBeanMap() {
		return formulaBeanMap;
	}

	public void setFormulaBeanMap(Map<String, FormulaBean> formulaBeanMap) {
		this.formulaBeanMap = formulaBeanMap;
	}

	public Boolean getIsUpdatedFormula() {
		return isUpdatedFormula;
	}

	public void setIsUpdatedFormula(Boolean isUpdatedFormula) {
		this.isUpdatedFormula = isUpdatedFormula;
	}

	public Map<Long, Integer> getTableIdElrIdMap() {
		return tableIdElrIdMap;
	}

	public void setTableIdElrIdMap(Map<Long, Integer> tableIdElrIdMap) {
		this.tableIdElrIdMap = tableIdElrIdMap;
	}

	public Boolean getIsFormulaPresentAlready() {
		return isFormulaPresentAlready;
	}

	public void setIsFormulaPresentAlready(Boolean isFormulaPresentAlready) {
		this.isFormulaPresentAlready = isFormulaPresentAlready;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsSaveAsDraft() {
		return isSaveAsDraft;
	}

	public void setIsSaveAsDraft(Boolean isSaveAsDraft) {
		this.isSaveAsDraft = isSaveAsDraft;
	}

	public Integer getAction() {
		return action;
	}

	public void setAction(Integer action) {
		this.action = action;
	}

	public Map<String, Long> getFormulaIdMap() {
		return formulaIdMap;
	}

	public String getZipFileName() {
		return zipFileName;
	}

	public void setZipFileName(String zipFileName) {
		this.zipFileName = zipFileName;
	}

	public void setFormulaIdMap(Map<String, Long> formulaIdMap) {
		this.formulaIdMap = formulaIdMap;
	}

	public String getDyanmicTableId() {
		return dyanmicTableId;
	}

	public void setDyanmicTableId(String dyanmicTableId) {
		this.dyanmicTableId = dyanmicTableId;
	}
}
