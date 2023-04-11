package com.iris.sdmx.util;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This Validations class is utility class and is used to do server side Validations in all controller classes.
 * 
 * @author apagaria
 * 
 * 
 */

@Component
@Scope("prototype")
public class SdmxValidations {

	/**
	 * This method would trim the input string
	 * 
	 * @param input The input string to trim
	 * @return trim content of the input if not null, else null
	 */
	public static String trimInput(String input) {
		if (input != null) {
			return input.trim();
		}
		return null;
	}
}