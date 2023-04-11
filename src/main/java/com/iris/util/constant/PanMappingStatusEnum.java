/**
 * 
 */
package com.iris.util.constant;

/**
 * @author sajadhav
 *
 */
public enum PanMappingStatusEnum {
	NSDL_PENDING("NSDL-PENDING"), RBI_PENDING("RBI-PENDING"), APPROVED("APPROVED"), REJECTED("REJECTED"), NSDL_REJECTED("NSDL-REJECTED");

	String status;

	private PanMappingStatusEnum(String status) {
		this.status = status;
	}

	/**
	 * @return the moduleCode
	 */
	public String getStatus() {
		return status;
	}

}
