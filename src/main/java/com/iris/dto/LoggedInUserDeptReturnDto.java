/**
 * 
 */
package com.iris.dto;

import java.io.Serializable;

/**
 * @author sajadhav
 *
 */
public class LoggedInUserDeptReturnDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1545390339628070118L;

	private Long returnId;

	private String returnCode;

	private String returnName;

	private String freqCode;

	private String freqName;

	private Long groupId;

	private String groupName;

	private String regulatorCode;

	private String regulatorName;

	/**
	 * 
	 */
	public LoggedInUserDeptReturnDto() {
		super();
	}

	/**
	 * @param returnId
	 * @param returnCode
	 * @param returnName
	 * @param freqCode
	 * @param freqName
	 * @param groupId
	 * @param groupName
	 * @param regulatorCode
	 * @param regulatorName
	 */
	public LoggedInUserDeptReturnDto(Long returnId, String returnCode, String returnName, String freqCode, String freqName, Long groupId, String groupName, String regulatorCode, String regulatorName) {
		super();
		this.returnId = returnId;
		this.returnCode = returnCode;
		this.returnName = returnName;
		this.freqCode = freqCode;
		this.freqName = freqName;
		this.groupId = groupId;
		this.groupName = groupName;
		this.regulatorCode = regulatorCode;
		this.regulatorName = regulatorName;
	}

	/**
	 * @return the returnCode
	 */
	public String getReturnCode() {
		return returnCode;
	}

	/**
	 * @param returnCode the returnCode to set
	 */
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	/**
	 * @return the returnName
	 */
	public String getReturnName() {
		return returnName;
	}

	/**
	 * @param returnName the returnName to set
	 */
	public void setReturnName(String returnName) {
		this.returnName = returnName;
	}

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
	 * @return the groupId
	 */
	public Long getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the regulatorCode
	 */
	public String getRegulatorCode() {
		return regulatorCode;
	}

	/**
	 * @param regulatorCode the regulatorCode to set
	 */
	public void setRegulatorCode(String regulatorCode) {
		this.regulatorCode = regulatorCode;
	}

	/**
	 * @return the regulatorName
	 */
	public String getRegulatorName() {
		return regulatorName;
	}

	/**
	 * @param regulatorName the regulatorName to set
	 */
	public void setRegulatorName(String regulatorName) {
		this.regulatorName = regulatorName;
	}

	/**
	 * @return the returnId
	 */
	public Long getReturnId() {
		return returnId;
	}

	/**
	 * @param returnId the returnId to set
	 */
	public void setReturnId(Long returnId) {
		this.returnId = returnId;
	}
}
