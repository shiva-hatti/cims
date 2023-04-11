/**
 * 
 */
package com.iris.validator;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iris.exception.ApplicationException;
import com.iris.util.constant.GeneralConstants;

/**
 * @author apagaria
 *
 */
@Component
public class ReturnRegulatorValidatorV2 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2287732981785383775L;

	/**
	 * 
	 */
	@Autowired
	CIMSCommonValidator cimsCommonValidator;

	/**
	 * 
	 */
	private static final Logger LOGGER = LogManager.getLogger(ReturnRegulatorValidatorV2.class);

	/**
	 * @param userId
	 * @param jobProcessId
	 * @throws ApplicationException
	 */
	public void validateFetchReturnByUserRoleNRegulatorRequestV2(Long userId, String jobProcessId) throws ApplicationException {
		LOGGER.debug(GeneralConstants.JOB_PROCESSING_ID.getConstantVal() + jobProcessId + ", Validation of FetchReturnByUserRoleNRegulatorRequestV2 Start ");
		// Validate Job Processing id
		cimsCommonValidator.validateJobProcessingId(jobProcessId);

		// Validate User Id
		cimsCommonValidator.validateUser(userId);
		LOGGER.debug(GeneralConstants.JOB_PROCESSING_ID.getConstantVal() + jobProcessId + ", Validation of FetchReturnByUserRoleNRegulatorRequestV2 End ");
	}

}
