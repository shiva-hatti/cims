/**
 * 
 */
package com.iris.util.constant;

/**
 * @author sajadhav
 *
 */
public enum FilingPanStatusEnum {
	NSDL_PENDING("NSDL-PENDING"), NSDL_APPROVED("NSDL-APPROVED"), NSDL_REJECTED("NSDL-REJECTED");

	String status;

	private FilingPanStatusEnum(String status) {
		this.status = status;
	}

	/**
	 * @return the moduleCode
	 */
	public String getStatus() {
		return status;
	}

}
