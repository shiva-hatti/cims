/**
 * 
 */
package com.iris.sdmx.fusion.bean;

import java.io.Serializable;

/**
 * @author sajadhav
 *
 */
public class TextFormat implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4861378101162357442L;

	private String textType;

	private String pattern;

	private String minValue;

	private String maxValue;

	private String decimals;

	/**
	 * @return the textType
	 */
	public String getTextType() {
		return textType;
	}

	/**
	 * @param textType the textType to set
	 */
	public void setTextType(String textType) {
		this.textType = textType;
	}

	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return the minValue
	 */
	public String getMinValue() {
		return minValue;
	}

	/**
	 * @param minValue the minValue to set
	 */
	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	/**
	 * @return the maxValue
	 */
	public String getMaxValue() {
		return maxValue;
	}

	/**
	 * @param maxValue the maxValue to set
	 */
	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	/**
	 * @return the decimals
	 */
	public String getDecimals() {
		return decimals;
	}

	/**
	 * @param decimals the decimals to set
	 */
	public void setDecimals(String decimals) {
		this.decimals = decimals;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "TextFormat [textType=" + textType + ", pattern=" + pattern + ", minValue=" + minValue + ", maxValue=" + maxValue + ", decimals=" + decimals + "]";
	}

}
