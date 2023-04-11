/**
 * 
 */
package com.iris.dto;

import java.io.Serializable;

/**
 * @author apagaria
 *
 */
public class ReturnEntityFreqDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String freqCode;

	private String freqName;

	private String freqLabel;

	/**
	 * @return the freqCode
	 */
	public String getFreqCode() {
		return freqCode;
	}

	/**
	 * @param freqCode the freqCode to set
	 */
	public void setFreqCode(String freqCode) {
		this.freqCode = freqCode;
	}

	/**
	 * @return the freqName
	 */
	public String getFreqName() {
		return freqName;
	}

	/**
	 * @param freqName the freqName to set
	 */
	public void setFreqName(String freqName) {
		this.freqName = freqName;
	}

	/**
	 * @return the freqLabel
	 */
	public String getFreqLabel() {
		return freqLabel;
	}

	/**
	 * @param freqLabel the freqLabel to set
	 */
	public void setFreqLabel(String freqLabel) {
		this.freqLabel = freqLabel;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "ReturnEntityFreqDto [freqCode=" + freqCode + ", freqName=" + freqName + ", freqLabel=" + freqLabel + "]";
	}

}
