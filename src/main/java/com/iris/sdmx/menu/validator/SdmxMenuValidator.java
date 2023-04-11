package com.iris.sdmx.menu.validator;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iris.caching.ObjectCache;
import com.iris.exception.ApplicationException;
import com.iris.sdmx.lockrecord.validator.SdmxLockRecordValidator;
import com.iris.sdmx.menu.repo.SdmxMenuRepo;
import com.iris.util.constant.ErrorCode;
import com.iris.validator.CIMSCommonValidator;

/**
 * @author vjadhav
 *
 */
@Component
public class SdmxMenuValidator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6477700732961718094L;

	/**
	 * 
	 */
	private static final Logger LOGGER = LogManager.getLogger(SdmxMenuValidator.class);

	/**
	 * 
	 */
	@Autowired
	private CIMSCommonValidator cimsCommonValidator;

	@Autowired
	private SdmxMenuRepo sdmxMenuRepo;

	public void validateGetMenuAccessList(Long userId, String jobProcessId) throws ApplicationException {
		LOGGER.info("START - validate  @getMenuAccessList request received with Job Processing ID : " + jobProcessId);
		// Validate Job Processing Id
		cimsCommonValidator.validateJobProcessingId(jobProcessId);
		// Validate User
		cimsCommonValidator.validateUser(userId);
		LOGGER.info("END - validate  @getMenuAccessList request received with Job Processing ID : " + jobProcessId);

	}

	public void validateGetMenuActionList(Long userId, String jobProcessId) throws ApplicationException {
		LOGGER.info("START - validate  @getMenuActionList request received with Job Processing ID : " + jobProcessId);
		// Validate Job Processing Id
		cimsCommonValidator.validateJobProcessingId(jobProcessId);
		// Validate User
		cimsCommonValidator.validateUser(userId);
		//validate sdmx menu id
		//validateSdmxMenuId(sdmxMenuId);
		LOGGER.info("END - validate  @getMenuActionList request received with Job Processing ID : " + jobProcessId);

	}

	public void validateSdmxMenuId(Long sdmxMenuId) throws ApplicationException {
		if (sdmxMenuId == null) {
			throw new ApplicationException(ErrorCode.E1229.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1214.toString()));
		} else if (sdmxMenuRepo.findByMenuIdAndIsActive(sdmxMenuId) == null) {
			throw new ApplicationException(ErrorCode.E1230.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1214.toString()));
		}

	}
}
