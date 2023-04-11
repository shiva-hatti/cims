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

@Entity
@Table(name = "TBL_AUTO_CAL_VERSION_MAP")
public class AutoCalVersionMap implements Serializable {

	private static final long serialVersionUID = -1901064284981222619L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AUTO_CAL_VERSION_ID")
	private Integer autoCalVersionId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_TEMPLATE_ID_FK")
	private ReturnTemplate returnTempIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AUTO_CAL_ID_FK")
	private AutoCalculationFormula autoCalFormulaFk;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	public Integer getAutoCalVersionId() {
		return autoCalVersionId;
	}

	public void setAutoCalVersionId(Integer autoCalVersionId) {
		this.autoCalVersionId = autoCalVersionId;
	}

	public ReturnTemplate getReturnTempIdFk() {
		return returnTempIdFk;
	}

	public void setReturnTempIdFk(ReturnTemplate returnTempIdFk) {
		this.returnTempIdFk = returnTempIdFk;
	}

	public AutoCalculationFormula getAutoCalFormulaFk() {
		return autoCalFormulaFk;
	}

	public void setAutoCalFormulaFk(AutoCalculationFormula autoCalFormulaFk) {
		this.autoCalFormulaFk = autoCalFormulaFk;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}