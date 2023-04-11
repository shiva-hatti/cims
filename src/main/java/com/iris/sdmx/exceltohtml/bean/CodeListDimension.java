package com.iris.sdmx.exceltohtml.bean;

import java.io.Serializable;

import com.iris.util.Validations;

public class CodeListDimension implements Serializable {

	private static final long serialVersionUID = -6958034941287647006L;

	private String clValueCode;
	private String clCode;
	private String dimConceptId;

	private String clValueCodeLable;
	private String dimConceptIdLable;

	/**
	 * @return the clValueCode
	 */
	public String getClValueCode() {
		return clValueCode;
	}

	/**
	 * @param clValueCode the clValueCode to set
	 */
	public void setClValueCode(String clValueCode) {
		this.clValueCode = Validations.trimInput(clValueCode);
	}

	/**
	 * @return the clCode
	 */
	public String getClCode() {
		return clCode;
	}

	/**
	 * @param clCode the clCode to set
	 */
	public void setClCode(String clCode) {
		this.clCode = Validations.trimInput(clCode);
	}

	/**
	 * @return the dimConceptId
	 */
	public String getDimConceptId() {
		return dimConceptId;
	}

	/**
	 * @param dimConceptId the dimConceptId to set
	 */
	public void setDimConceptId(String dimConceptId) {
		this.dimConceptId = Validations.trimInput(dimConceptId);
	}

	/**
	 * @return the clValueCodeLable
	 */
	public String getClValueCodeLable() {
		return clValueCodeLable;
	}

	/**
	 * @param clValueCodeLable the clValueCodeLable to set
	 */
	public void setClValueCodeLable(String clValueCodeLable) {
		this.clValueCodeLable = Validations.trimInput(clValueCodeLable);
	}

	/**
	 * @return the dimConceptIdLable
	 */
	public String getDimConceptIdLable() {
		return dimConceptIdLable;
	}

	/**
	 * @param dimConceptIdLable the dimConceptIdLable to set
	 */
	public void setDimConceptIdLable(String dimConceptIdLable) {
		this.dimConceptIdLable = Validations.trimInput(dimConceptIdLable);
	}

}
