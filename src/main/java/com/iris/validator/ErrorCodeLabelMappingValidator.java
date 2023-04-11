/**
 * 
 */
package com.iris.validator;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.iris.exception.ApplicationException;
import com.iris.model.ErrorCodeLabelRequest;
import com.iris.util.constant.ErrorConstants;

/**
 * This is a validator class responsible for validate error code detail API parameters
 * 
 * @author apagaria
 *
 */
public class ErrorCodeLabelMappingValidator {

	/**
	 * This method is used to validate fetch error detail api request parameters
	 * 
	 * @param jobProcessId
	 * @param channel
	 * @throws ApplicationException
	 */
	public static void validateFetchErrorCodeLabelParameter(String jobProcessId, ErrorCodeLabelRequest errorCodeLabelRequest) throws ApplicationException {
		// Job Process Id
		if (StringUtils.isEmpty(jobProcessId)) {
			throw new ApplicationException(ErrorConstants.E028.getErrorCode(), ErrorConstants.E028.getErrorMessage());
		}

		if (errorCodeLabelRequest == null || CollectionUtils.isEmpty(errorCodeLabelRequest.getErrorCodeIdList())) {
			throw new ApplicationException(ErrorConstants.E031.getErrorCode(), ErrorConstants.E031.getErrorMessage());
		}
	}
}
