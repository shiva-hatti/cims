package com.iris.sdmx.exceltohtml.bean;

import java.io.Serializable;

import com.iris.util.Validations;

public class CommonDimension implements Serializable {

	private static final long serialVersionUID = 2408013680247504234L;

	private String dimConceptId;
	private String clValueCode;

	private String commonDimIdLable;

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
	 * @return the commonDimIdLable
	 */
	public String getCommonDimIdLable() {
		return commonDimIdLable;
	}

	/**
	 * @param commonDimIdLable the commonDimIdLable to set
	 */
	public void setCommonDimIdLable(String commonDimIdLable) {
		this.commonDimIdLable = Validations.trimInput(commonDimIdLable);
	}

}
