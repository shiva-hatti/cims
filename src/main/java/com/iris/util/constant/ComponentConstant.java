/**
 * 
 */
package com.iris.util.constant;

/**
 * @author sajadhav
 *
 */
public enum ComponentConstant {

	KEEP_VALID_SESSION("KEEP_VALID_SESSION");

	private String componentConstant;

	private ComponentConstant(String componentConstant) {
		this.componentConstant = componentConstant;
	}

	public String getComponentConstant() {
		return componentConstant;
	}
}
