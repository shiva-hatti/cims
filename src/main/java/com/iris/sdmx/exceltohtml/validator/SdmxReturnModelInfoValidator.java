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
import com.iris.service.impl.UserMasterService;

/**
 * @author apagaria
 *
 */
@Component
public class SdmxReturnModelInfoValidator {

	/**
	 * 
	 */
	private static final Logger LOGGER = LogManager.getLogger(SdmxReturnModelInfoValidator.class);

	/**
	 * 
	 */
	@Autowired
	private UserMasterService userMasterService;

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

	/**
	 * @param jobProcessId
	 * @throws ApplicationException
	 */
	private void validateJobProcessingId(String jobProcessId) throws ApplicationException {
		if (StringUtils.isBlank(jobProcessId)) {
			throw new ApplicationException("EC001", "Job Processing Id can't be blank");
		}
	}

	public void fetchMapCellRefByReturnTemplateId(Long userId, String jobProcessId, Long returnTemplateId) throws ApplicationException {
		validateJobProcessingId(jobProcessId);
	}
}
