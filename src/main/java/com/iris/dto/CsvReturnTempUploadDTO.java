package com.iris.dto;

import java.io.Serializable;

import com.iris.util.Validations;

public class CsvReturnTempUploadDTO implements Serializable {
	private static final long serialVersionUID = -1163464401888761967L;
	private String csvVersionNumber;
	private String csvVersionDesc;
	private String csvValidFromDate;
	private String csvXSDFileName;
	private String csvTaxonomyFileName;
	private String csvFormulaFileName;
	private Boolean isActive;
	private String csvReturnFullPackage;

	public String getCsvVersionNumber() {
		return csvVersionNumber;
	}

	public void setCsvVersionNumber(String csvVersionNumber) {
		this.csvVersionNumber = Validations.trimInput(csvVersionNumber);
	}

	public String getCsvVersionDesc() {
		return csvVersionDesc;
	}

	public void setCsvVersionDesc(String csvVersionDesc) {
		this.csvVersionDesc = Validations.trimInput(csvVersionDesc);
	}

	public String getCsvValidFromDate() {
		return csvValidFromDate;
	}

	public void setCsvValidFromDate(String csvValidFromDate) {
		this.csvValidFromDate = Validations.trimInput(csvValidFromDate);
	}

	public String getCsvXSDFileName() {
		return csvXSDFileName;
	}

	public void setCsvXSDFileName(String csvXSDFileName) {
		this.csvXSDFileName = Validations.trimInput(csvXSDFileName);
	}

	public String getCsvTaxonomyFileName() {
		return csvTaxonomyFileName;
	}

	public void setCsvTaxonomyFileName(String csvTaxonomyFileName) {
		this.csvTaxonomyFileName = Validations.trimInput(csvTaxonomyFileName);
	}

	public String getCsvFormulaFileName() {
		return csvFormulaFileName;
	}

	public void setCsvFormulaFileName(String csvFormulaFileName) {
		this.csvFormulaFileName = Validations.trimInput(csvFormulaFileName);
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getCsvReturnFullPackage() {
		return csvReturnFullPackage;
	}

	public void setCsvReturnFullPackage(String csvReturnFullPackage) {
		this.csvReturnFullPackage = csvReturnFullPackage;
	}
}
