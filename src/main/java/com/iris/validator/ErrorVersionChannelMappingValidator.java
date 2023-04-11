/**
 * 
 */
package com.iris.validator;

import org.apache.commons.lang3.StringUtils;

import com.iris.exception.ApplicationException;
import com.iris.util.constant.ErrorConstants;

/**
 * This is a validator class responsible for validate error code detail API parameters
 * 
 * @author apagaria
 *
 */
public class ErrorVersionChannelMappingValidator {

	/**
	 * This method is used to validate fetch error detail api request parameters
	 * 
	 * @param jobProcessId
	 * @param channel
	 * @throws ApplicationException
	 */
	public static void validateFetchErrorCodeDetailParameter(String jobProcessId, int channel, Long returnTemplateId) throws ApplicationException {
		// Job Process Id
		if (StringUtils.isEmpty(jobProcessId)) {
			throw new ApplicationException(ErrorConstants.E028.getErrorCode(), ErrorConstants.E028.getErrorMessage());
		}

		// Version
		if (returnTemplateId < 1) {
			throw new ApplicationException(ErrorConstants.E030.getErrorCode(), ErrorConstants.E030.getErrorMessage());
		}

		// channel
		if (channel < 1 && channel > 3) {
			throw new ApplicationException(ErrorConstants.E029.getErrorCode(), ErrorConstants.E029.getErrorMessage());
		}
	}
}
