/**
 * 
 */
package com.iris.sdmx.upload.validation;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iris.caching.ObjectCache;
import com.iris.exception.ApplicationException;
import com.iris.util.constant.ErrorCode;
import com.iris.validator.CIMSCommonValidator;

/**
 * @author apagaria
 *
 */
@Component
public class EbrFileAuditValidator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2340819447815605618L;

	private static final Logger LOGGER = LogManager.getLogger(EbrFileAuditValidator.class);

	@Autowired
	private CIMSCommonValidator cimsCommonValidator;

	/**
	 * @param jobProcessId
	 * @param userId
	 * @param statusId
	 * @throws ApplicationException
	 */
	public void validateFetchSdmxFileAuditRequest(String jobProcessId, Long userId, Integer statusId) throws ApplicationException {
		LOGGER.debug("Validation request start for fetch sdmx file audit request with job processing id - " + jobProcessId);
		// Job procession id
		cimsCommonValidator.validateJobProcessingId(jobProcessId);

		// Validate User
		cimsCommonValidator.validateUser(userId);

		// Validate status id
		validateStatusId(statusId);

		LOGGER.debug("Validation request end for fetch sdmx file audit request with job processing id - " + jobProcessId);
	}

	/**
	 * @param jobProcessId
	 * @param userId
	 * @param statusId
	 * @throws ApplicationException
	 */
	public void validateSdmxFileAuditRequestUpdateStatus(String jobProcessId, Long userId, Integer statusId) throws ApplicationException {
		LOGGER.debug("Validation request start for fetch sdmx file audit request with job processing id - " + jobProcessId);
		// Job procession id
		cimsCommonValidator.validateJobProcessingId(jobProcessId);

		// Validate User
		cimsCommonValidator.validateUser(userId);

		// Validate status id
		validateStatusId(statusId);

		LOGGER.debug("Validation request end for fetch sdmx file audit request with job processing id - " + jobProcessId);
	}

	/**
	 * @param statusId
	 * @throws ApplicationException
	 */
	private void validateStatusId(Integer statusId) throws ApplicationException {
		if (statusId == null) {
			LOGGER.error("Status id is null");
			throw new ApplicationException(ErrorCode.E0789.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0789.toString()));
		}

		if (statusId <= 0) {
			LOGGER.error("Not valid status id");
			throw new ApplicationException(ErrorCode.E0789.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0789.toString()));
		}
	}

}
