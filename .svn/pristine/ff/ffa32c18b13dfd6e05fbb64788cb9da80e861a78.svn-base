package com.iris.sdmx.dimesnsion.bean;

import java.io.Serializable;

import com.iris.util.Validations;

/**
 * @author sajadhav
 *
 */
public class RegexBean implements Serializable {

	private static final long serialVersionUID = 6727111617378055920L;

	private Integer regexId;

	private String regexName;

	private String regex;

	/**
	 * @return the regexId
	 */
	public Integer getRegexId() {
		return regexId;
	}

	/**
	 * @param regexId the regexId to set
	 */
	public void setRegexId(Integer regexId) {
		this.regexId = regexId;
	}

	/**
	 * @return the regexName
	 */
	public String getRegexName() {
		return regexName;
	}

	/**
	 * @param regexName the regexName to set
	 */
	public void setRegexName(String regexName) {
		this.regexName = Validations.trimInput(regexName);
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
		this.regex = Validations.trimInput(regex);
	}

}