/**
 * 
 */
package com.iris.sdmx.lockrecord.validator;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iris.caching.ObjectCache;
import com.iris.exception.ApplicationException;
import com.iris.sdmx.exceltohtml.validator.SdmxRetrunPreviewValidator;
import com.iris.sdmx.lockrecord.bean.SdmxLockRecordSetBean;
import com.iris.sdmx.status.repo.SdmxModuleDetailRepo;
import com.iris.util.constant.ErrorCode;
import com.iris.validator.CIMSCommonValidator;

/**
 * @author apagaria
 *
 */
@Component
public class SdmxLockRecordValidator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private static final Logger LOGGER = LogManager.getLogger(SdmxLockRecordValidator.class);

	/**
	 * 
	 */
	@Autowired
	private CIMSCommonValidator cimsCommonValidator;

	@Autowired
	private SdmxRetrunPreviewValidator sdmxRetrunPreviewValidator;

	@Autowired
	private SdmxModuleDetailRepo sdmxModuleDetailRepo;

	/**
	 * @param userId
	 * @param jobProcessId
	 * @throws ApplicationException
	 */
	public void validateCheckLockRecordStatus(Long userId, String jobProcessId, String recordDetailJson) throws ApplicationException {
		LOGGER.info("START - Validation request for CheckLockRecordStatus with Job Processing ID : " + jobProcessId);

		// Validate Job Processing Id
		cimsCommonValidator.validateJobProcessingId(jobProcessId);
		// Validate User
		cimsCommonValidator.validateUser(userId);
		LOGGER.info("END - Validation request for CheckLockRecordStatus with Job Processing ID : " + jobProcessId);
	}

	/**
	 * @param userId
	 * @param jobProcessId
	 * @throws ApplicationException
	 */
	public void validateReturnTemplateLockCheck(Long userId, String jobProcessId) throws ApplicationException {
		LOGGER.info("START - Validation request for ReturnTemplateLockcheck with Job Processing ID : " + jobProcessId);

		// Validate Job Processing Id
		cimsCommonValidator.validateJobProcessingId(jobProcessId);
		// Validate User
		cimsCommonValidator.validateUser(userId);
		LOGGER.info("END - Validation request for ReturnTemplateLockcheck with Job Processing ID : " + jobProcessId);
	}

	/**
	 * @param userId
	 * @param jobProcessId
	 * @throws ApplicationException
	 */
	public void validateUpdateLockOnRecordStatus(Long userId, String jobProcessId, String recordDetailJson) throws ApplicationException {
		LOGGER.info("START - Validation request for validateUpdateLockOnRecordStatus with Job Processing ID : " + jobProcessId);

		// Validate Job Processing Id
		cimsCommonValidator.validateJobProcessingId(jobProcessId);
		// Validate User
		cimsCommonValidator.validateUser(userId);
		LOGGER.info("END - Validation request for validateUpdateLockOnRecordStatus with Job Processing ID : " + jobProcessId);
	}

	/**
	 * @param userId
	 * @param jobProcessId
	 * @param sdmxLockRecordSetBean
	 * @throws ApplicationException
	 */
	public void validateSetLockOnRecord(Long userId, String jobProcessId, SdmxLockRecordSetBean sdmxLockRecordSetBean) throws ApplicationException {
		LOGGER.info("START - Validation request for SetLockOnRecord with Job Processing ID : " + jobProcessId);

		// Validate Job Processing Id
		cimsCommonValidator.validateJobProcessingId(jobProcessId);
		// Validate User
		cimsCommonValidator.validateUser(userId);
		LOGGER.info("END - Validation request for SetLockOnRecord with Job Processing ID : " + jobProcessId);
	}

	public void validateGetCellRefLockLogs(Long userId, String jobProcessId, Long returnPreviewId) throws ApplicationException {
		LOGGER.info("START - Validation request for GetCellRefLockLogs with Job Processing ID : " + jobProcessId);

		// Validate Job Processing Id
		cimsCommonValidator.validateJobProcessingId(jobProcessId);

		// Validate User
		cimsCommonValidator.validateUser(userId);

		//validate return preview Id
		sdmxRetrunPreviewValidator.validateReturnPreviewId(returnPreviewId);

		LOGGER.info("END - Validation request for GetCellRefLockLogs with Job Processing ID : " + jobProcessId);
	}

	public void validateGetModuleWiseLocks(Long userId, String jobProcessId, Long moduleId) throws ApplicationException {
		LOGGER.info("START - Validation request for GetModuleWiseLocks with Job Processing ID : " + jobProcessId);

		// Validate Job Processing Id
		cimsCommonValidator.validateJobProcessingId(jobProcessId);

		// Validate User
		cimsCommonValidator.validateUser(userId);

		validateModuleId(moduleId);

		LOGGER.info("END - Validation request for GetModuleWiseLocks with Job Processing ID : " + jobProcessId);
	}

	public void validateReleaseModuleWiseLocks(Long moduleId, Long userId, String jobProcessId) throws ApplicationException {
		LOGGER.info("START - Validation request for ReleaseModuleWiseLocks with Job Processing ID : " + jobProcessId);

		// Validate Job Processing Id
		cimsCommonValidator.validateJobProcessingId(jobProcessId);

		// Validate User
		cimsCommonValidator.validateUser(userId);

		validateModuleId(moduleId);

		LOGGER.info("END - Validation request for ReleaseModuleWiseLocks with Job Processing ID : " + jobProcessId);
	}

	public void validateGetReturnInfo(Long userId, String jobProcessId) throws ApplicationException {
		LOGGER.info("START - Validation request for GetReturnInfo with Job Processing ID : " + jobProcessId);

		// Validate Job Processing Id
		cimsCommonValidator.validateJobProcessingId(jobProcessId);

		// Validate User
		cimsCommonValidator.validateUser(userId);

		LOGGER.info("END - Validation request for GetReturnInfo with Job Processing ID : " + jobProcessId);
	}

	public void validateModuleId(Long moduleId) throws ApplicationException {
		if (moduleId == null) {
			throw new ApplicationException(ErrorCode.E1214.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1214.toString()));
		} else if (sdmxModuleDetailRepo.findModuleByModuleId(moduleId) == null) {
			throw new ApplicationException(ErrorCode.E1215.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1215.toString()));
		}
	}

}
