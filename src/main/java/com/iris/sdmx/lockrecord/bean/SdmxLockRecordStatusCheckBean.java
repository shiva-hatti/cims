/**
 * 
 */
package com.iris.sdmx.lockrecord.bean;

import java.io.Serializable;

/**
 * @author apagaria
 *
 */
public class SdmxLockRecordStatusCheckBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String recordDetailEncodedJson;

	/**
	 * @return the recordDetailEncodedJson
	 */
	public String getRecordDetailEncodedJson() {
		return recordDetailEncodedJson;
	}

	/**
	 * @param recordDetailEncodedJson the recordDetailEncodedJson to set
	 */
	public void setRecordDetailEncodedJson(String recordDetailEncodedJson) {
		this.recordDetailEncodedJson = recordDetailEncodedJson;
	}

	@Override
	public String toString() {
		return "SdmxLockRecordStatusCheckBean [recordDetailEncodedJson=" + recordDetailEncodedJson + "]";
	}
}
