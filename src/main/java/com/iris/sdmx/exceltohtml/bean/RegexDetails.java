/**
 * 
 */
package com.iris.sdmx.exceltohtml.bean;

/**
 * @author apagaria
 *
 */
public class RegexDetails {

	private String regex;

	private Integer minLength;

	private Integer maxLength;

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
		this.regex = regex;
	}

	/**
	 * @return the minLength
	 */
	public Integer getMinLength() {
		return minLength;
	}

	/**
	 * @param minLength the minLength to set
	 */
	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	/**
	 * @return the maxLength
	 */
	public Integer getMaxLength() {
		return maxLength;
	}

	/**
	 * @param maxLength the maxLength to set
	 */
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "RegexDetails [regex=" + regex + ", minLength=" + minLength + ", maxLength=" + maxLength + "]";
	}
}
