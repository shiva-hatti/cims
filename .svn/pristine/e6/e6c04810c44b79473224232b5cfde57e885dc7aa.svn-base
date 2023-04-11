/**
 * 
 */
package com.iris.util.constant;

/**
 * @author sajadhav
 *
 */
public enum ReturnPropertyVal {

	AUDITED("field.returnProperty.audited", "Audited", "A", 1), UN_AUITED("field.returnProperty.unaudited", "Un-Audited", "U", 2), PROVISIONAL("field.returnProperty.provisional", "Provisional", "P", 4), FINAL("field.returnProperty.final", "Final", "F", 3);
	String returnPropertykey;
	String xbrlReturnPropertyString;
	String returnPropertyValueCode;
	Integer returnPropertyId;

	private ReturnPropertyVal(String returnPropertykey, String xbrlReturnPropertyString, String returnPropertyValueCode, Integer returnPropertyId) {
		this.returnPropertykey = returnPropertykey;
		this.xbrlReturnPropertyString = xbrlReturnPropertyString;
		this.returnPropertyValueCode = returnPropertyValueCode;
		this.returnPropertyId = returnPropertyId;
	}

	/**
	 * @return the returnPropertykey
	 */
	public String getReturnPropertykey() {
		return returnPropertykey;
	}

	/**
	 * @return the xbrlReturnPropertyString
	 */
	public String getXbrlReturnPropertyString() {
		return xbrlReturnPropertyString;
	}

	/**
	 * @return the csvReturnPropertyString
	 */
	public String getReturnPropertyValueCode() {
		return returnPropertyValueCode;
	}

	/**
	 * @return the returnPropertyId
	 */
	public Integer getReturnPropertyId() {
		return returnPropertyId;
	}

	public static Integer getReturnPropertyIdByXBRLReturnPropertyString(String xbrlReturnPropertyString) {
		for (ReturnPropertyVal returnroperty : ReturnPropertyVal.values()) {
			if (returnroperty.getXbrlReturnPropertyString().equalsIgnoreCase(xbrlReturnPropertyString)) {
				return returnroperty.getReturnPropertyId();
			}
		}
		return null;
	}

	public static Integer getReturnPropertyIdByReturnPropertyValCode(String returnPropertyValCode) {
		for (ReturnPropertyVal returnroperty : ReturnPropertyVal.values()) {
			if (returnroperty.getReturnPropertyValueCode().equalsIgnoreCase(returnPropertyValCode)) {
				return returnroperty.getReturnPropertyId();
			}
		}
		return null;
	}

}
