/**
 * 
 */
package com.iris.sdmx.agency.master.validator;

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
public class SdmxAgencyMasterValidator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LogManager.getLogger(SdmxAgencyMasterValidator.class);

	@Autowired
	private CIMSCommonValidator cimsCommonValidator;

	/**
	 * @param jobProcessId
	 * @param userId
	 * @param statusId
	 * @throws ApplicationException
	 */
	public void validateFetchAgencyMasterListRequest(String jobProcessId, Long userId) throws ApplicationException {
		LOGGER.debug("Validation request start for fetch agency master list request request with job processing id - " + jobProcessId);
		// Job procession id
		cimsCommonValidator.validateJobProcessingId(jobProcessId);

		// Validate User
		cimsCommonValidator.validateUser(userId);

		LOGGER.debug("Validation request end for save fetch agency master list request with job processing id - " + jobProcessId);
	}

}
