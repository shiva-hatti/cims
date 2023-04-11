package com.iris.sdmx.dimesnsion.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.iris.util.Validations;

/**
 * @author psawant
 * @version 1.0
 * @date 11/10/2017
 */
@Entity
@Table(name = "TBL_REGEX")
public class Regex implements Serializable {

	private static final long serialVersionUID = 6727111617378055920L;

	@Id
	@Column(name = "REGEX_ID")
	private Integer regexId;

	@Column(name = "REGEX_NAME")
	private String regexName;

	@Column(name = "REGEX")
	private String regex;

	/**
	 * 
	 */
	public Regex() {
	}

	/**
	 * @param regexId
	 */
	public Regex(Integer regexId) {
		this.regexId = regexId;
	}

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