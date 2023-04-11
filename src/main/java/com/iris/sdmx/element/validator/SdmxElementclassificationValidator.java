package com.iris.sdmx.element.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iris.exception.ApplicationException;
import com.iris.sdmx.element.service.SdmxElementClassificationService;
import com.iris.service.impl.UserMasterService;

/**
 * @author vjadhav
 *
 */
@Component
public class SdmxElementclassificationValidator {

	private static final Logger LOGGER = LogManager.getLogger(SdmxElementclassificationValidator.class);

	@Autowired
	private SdmxElementClassificationService sdmxElementClassificationService;

	@Autowired
	private UserMasterService userMasterService;

	public void validateClassificationRequest(String jobProcessId, Long userId) throws ApplicationException {

		validateUser(userId);

		validateJobProcessingId(jobProcessId);

	}

	private void validateUser(Long userId) throws ApplicationException {
		if (userId == null) {
			throw new ApplicationException("ER016", "User Id can't be blank");
		} else if (userMasterService.getDataById(userId) == null) {
			throw new ApplicationException("ER017", "User not Found");
		}
	}

	private void validateJobProcessingId(String jobProcessId) throws ApplicationException {
		if (jobProcessId == null || jobProcessId == "") {
			throw new ApplicationException("ERROR", "Job Processing Id can't be blank");
		}

	}
}
