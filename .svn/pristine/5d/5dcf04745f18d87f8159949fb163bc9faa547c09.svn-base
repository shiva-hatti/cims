package com.iris.sdmx.status.validator;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iris.exception.ApplicationException;
import com.iris.validator.CIMSCommonValidator;

/**
 * @author vjadhav
 *
 */
@Component
public class SdmxModuleDetailValidator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2923228494666673522L;

	private static final Logger LOGGER = LogManager.getLogger(SdmxModuleDetailValidator.class);

	@Autowired
	private CIMSCommonValidator cimsCommonValidator;

	public void fetchModuleDetailListValidateRequest(Long userId, String jobProcessId) throws ApplicationException {
		LOGGER.debug("Validation request start for fetch sdmx Module detail request with job processing id - " + jobProcessId);
		// Job procession id
		cimsCommonValidator.validateJobProcessingId(jobProcessId);

		// Validate User
		cimsCommonValidator.validateUser(userId);

		LOGGER.debug("Validation request end for fetch sdmx Module detail log request with job processing id - " + jobProcessId);

	}

}
