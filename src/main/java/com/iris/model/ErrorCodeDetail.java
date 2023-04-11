package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This bean represents Error code detail for NON-XBRL returns
 * 
 * @author apagaria
 */
@Entity
@Table(name = "TBL_ERROR_CODE_DETAIL")
public class ErrorCodeDetail implements Serializable {

	private static final long serialVersionUID = -6305709526119023051L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ERROR_CODE_DETAIL_ID")
	private Long errorCodeDetailId;

	/**
	 * For backend
	 */
	@Column(name = "TECHNICAL_ERROR_CODE")
	private String technicalErrorCode;

	/**
	 * To display at front end
	 */
	@Column(name = "BUSINESS_ERROR_CODE")
	private String businessErrorCode;

	@Column(name = "ERROR_DESCRIPTION")
	private String errorDescription;

	@Column(name = "NUMERIC_FORMULA")
	private String numericFormula;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FORMULA_CATEGORY_TYPE_ID_FK")
	private FormulaCategoryType formulaCategoryTypeIdFk;

	@Column(name = "ROUND_OFF")
	private int roundOff;

	/**
	 * @return the errorCodeDetailId
	 */
	public Long getErrorCodeDetailId() {
		return errorCodeDetailId;
	}

	/**
	 * @param errorCodeDetailId the errorCodeDetailId to set
	 */
	public void setErrorCodeDetailId(Long errorCodeDetailId) {
		this.errorCodeDetailId = errorCodeDetailId;
	}

	/**
	 * @return the errorDescription
	 */
	public String getErrorDescription() {
		return errorDescription;
	}

	/**
	 * @return the technicalErrorCode
	 */
	public String getTechnicalErrorCode() {
		return technicalErrorCode;
	}

	/**
	 * @param technicalErrorCode the technicalErrorCode to set
	 */
	public void setTechnicalErrorCode(String technicalErrorCode) {
		this.technicalErrorCode = technicalErrorCode;
	}

	/**
	 * @return the businessErrorCode
	 */
	public String getBusinessErrorCode() {
		return businessErrorCode;
	}

	/**
	 * @param businessErrorCode the businessErrorCode to set
	 */
	public void setBusinessErrorCode(String businessErrorCode) {
		this.businessErrorCode = businessErrorCode;
	}

	/**
	 * @param errorDescription the errorDescription to set
	 */
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	/**
	 * @return the numericFormula
	 */
	public String getNumericFormula() {
		return numericFormula;
	}

	/**
	 * @param numericFormula the numericFormula to set
	 */
	public void setNumericFormula(String numericFormula) {
		this.numericFormula = numericFormula;
	}

	/**
	 * @return the formulaCategoryTypeIdFk
	 */
	public FormulaCategoryType getFormulaCategoryTypeIdFk() {
		return formulaCategoryTypeIdFk;
	}

	/**
	 * @param formulaCategoryTypeIdFk the formulaCategoryTypeIdFk to set
	 */
	public void setFormulaCategoryTypeIdFk(FormulaCategoryType formulaCategoryTypeIdFk) {
		this.formulaCategoryTypeIdFk = formulaCategoryTypeIdFk;
	}

	/**
	 * @return the roundOff
	 */
	public int getRoundOff() {
		return roundOff;
	}

	/**
	 * @param roundOff the roundOff to set
	 */
	public void setRoundOff(int roundOff) {
		this.roundOff = roundOff;
	}

	@Override
	public String toString() {
		return "ErrorCodeDetail [errorCodeDetailId=" + errorCodeDetailId + ", technicalErrorCode=" + technicalErrorCode + ", businessErrorCode=" + businessErrorCode + ", errorDescription=" + errorDescription + ", numericFormula=" + numericFormula + ", formulaCategoryTypeIdFk=" + formulaCategoryTypeIdFk + ", roundOff=" + roundOff + "]";
	}
}