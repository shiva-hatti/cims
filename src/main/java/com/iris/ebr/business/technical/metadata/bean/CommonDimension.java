package com.iris.ebr.business.technical.metadata.bean;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

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
		this.dimConceptId = StringUtils.trim(dimConceptId);
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
		this.clValueCode = StringUtils.trim(clValueCode);
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
		this.commonDimIdLable = StringUtils.trim(commonDimIdLable);
	}

}
