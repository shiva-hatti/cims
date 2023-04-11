/**
 * 
 */
package com.iris.rbrToEbr.validator;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iris.exception.ApplicationException;
import com.iris.validator.CIMSCommonValidator;

/**
 * @author apagaria
 *
 */
@Component
public class CtlEbrElementValidator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2340819447815605618L;

	private static final Logger LOGGER = LogManager.getLogger(CtlEbrElementValidator.class);

	@Autowired
	private CIMSCommonValidator cimsCommonValidator;

	/**
	 * @param jobProcessId
	 * @param userId
	 * @param statusId
	 * @throws ApplicationException
	 */
	public void validateSaveCtlEbrElementRequest(String jobProcessId, Long userId) throws ApplicationException {
		LOGGER.debug("Validation request start for save Ctl Ebr element request with job processing id - " + jobProcessId);
		// Job procession id
		cimsCommonValidator.validateJobProcessingId(jobProcessId);

		// Validate User
		cimsCommonValidator.validateUser(userId);

		LOGGER.debug("Validation request end for save Ctl Ebr element request with job processing id - " + jobProcessId);
	}

}
