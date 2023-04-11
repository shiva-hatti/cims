/**
 * 
 */
package com.iris.sdmx.exceltohtml.validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iris.exception.ApplicationException;
import com.iris.sdmx.exceltohtml.bean.ModelCodeInputBean;
import com.iris.sdmx.model.code.data.SdmxDataModelCodeRequestBean;
import com.iris.service.impl.UserMasterService;

/**
 * @author apagaria
 *
 */
@Component
public class SdmxModelCodeValidator {

	/**
	 * 
	 */
	private static final Logger LOGGER = LogManager.getLogger(SdmxModelCodeValidator.class);

	/**
	 * 
	 */
	@Autowired
	private UserMasterService userMasterService;

	/**
	 * @param userId
	 * @param jobProcessId
	 * @throws ApplicationException
	 */
	public void validatefetchEntityByReturnCellNReturnTemplate(Long userId, String jobProcessId, Integer returnCellRef, Long returnTemplateId) throws ApplicationException {
		LOGGER.info("START - Validation request for Fetch All Record with Job Processing ID : " + jobProcessId);

		// Validate Job Processing Id
		validateJobProcessingId(jobProcessId);
		// Validate User
		validateUser(userId);
		// Return cell ref
		validateReturnCellRef(returnCellRef);
		LOGGER.info("END - Validation request for Fetch All Record with Job Processing ID : " + jobProcessId);
	}

	/**
	 * @param userId
	 * @param jobProcessId
	 * @throws ApplicationException
	 */
	public void validatefetchEntityByDmModelCode(Long userId, String jobProcessId, String dmModelCode) throws ApplicationException {
		LOGGER.info("START - Validation request for Fetch All Record with Job Processing ID : " + jobProcessId);

		// Validate Job Processing Id
		validateJobProcessingId(jobProcessId);
		// Validate User
		validateUser(userId);
		LOGGER.info("END - Validation request for Fetch All Record with Job Processing ID : " + jobProcessId);
	}

	public void validatefetchEntityByModelCode(Long userId, String jobProcessId, String modelCode) throws ApplicationException {
		// Validate Job Processing Id
		validateJobProcessingId(jobProcessId);
		// Validate User
		validateUser(userId);
		// DM Code
		validateDmCode(modelCode);

	}

	/**
	 * @param userId
	 * @param jobProcessId
	 * @param cellRef
	 * @throws ApplicationException
	 */
	public void validatefetchEntityByReturnCellRef(Long userId, String jobProcessId, Integer cellRef) throws ApplicationException {
		// Validate Job Processing Id
		validateJobProcessingId(jobProcessId);
		// Validate User
		validateUser(userId);
	}

	public void validateDeleteUnusedModelCodes(Long userId, String jobProcessId) throws ApplicationException {
		// Validate Job Processing Id
		validateJobProcessingId(jobProcessId);
		// Validate User
		validateUser(userId);
	}

	/**
	 * @param userId
	 * @param jobProcessId
	 * @param modelCodeInputBean
	 * @throws ApplicationException
	 */
	public void validateSaveEntityRequest(Long userId, String jobProcessId, ModelCodeInputBean modelCodeInputBean) throws ApplicationException {
		// Validate Job Processing Id
		validateJobProcessingId(jobProcessId);
		// Validate User
		validateUser(userId);
		// Model Dim
		validateModelDim(modelCodeInputBean.getModelDim());

	}

	public void validateFetchDmCodeRequest(Long userId, String jobProcessId, String dmCode) throws ApplicationException {
		LOGGER.info("START - Validation request for Fetch All Record with Job Processing ID : " + jobProcessId);
		// Validate Job Processing Id
		validateJobProcessingId(jobProcessId);
		// Validate User
		validateUser(userId);
		// DM Code
		validateDmCode(dmCode);
		LOGGER.info("END - Validation request for Fetch All Record with Job Processing ID : " + jobProcessId);
	}

	/**
	 * @param userId
	 * @throws ApplicationException
	 */
	private void validateUser(Long userId) throws ApplicationException {
		if (userId == null) {
			throw new ApplicationException("ER016", "User Id can't blank");
		} else if (StringUtils.isBlank(userMasterService.getUserNameByUserId(userId))) {
			throw new ApplicationException("ER017", "User not Found");
		}
	}

	private void validateReturnCellRef(Integer returnCellRef) throws ApplicationException {
		if (returnCellRef == null) {
			throw new ApplicationException("ER019", "Return cell ref can't blank");
		}
	}

	private void validateDmCode(String dmCode) throws ApplicationException {
		if (StringUtils.isBlank(dmCode)) {
			throw new ApplicationException("ER019", "DM code can't blank");
		}
	}

	private void validateModelDim(String modelDim) throws ApplicationException {
		if (StringUtils.isBlank(modelDim)) {
			throw new ApplicationException("ER019", "Model Dim can't blank");
		}
	}

	/**
	 * @param jobProcessId
	 * @throws ApplicationException
	 */
	private void validateJobProcessingId(String jobProcessId) throws ApplicationException {
		if (StringUtils.isBlank(jobProcessId)) {
			throw new ApplicationException("EC001", "Job Processing Id can't be blank");
		}
	}

	public void validateSdmxDataModelCode(SdmxDataModelCodeRequestBean sdmxDataModelCodeRequestBean, String jobProcessId) throws ApplicationException {
		validateJobProcessingId(jobProcessId);
		validateUser(sdmxDataModelCodeRequestBean.getUserId());
	}

	public void validateCheckReturnModelMapping(String jobProcessId, Long userId) throws ApplicationException {
		validateJobProcessingId(jobProcessId);
		validateUser(userId);
	}
}
