/**
 * 
 */
package com.iris.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dto.ServiceResponse;
import com.iris.model.DateFormat;
import com.iris.model.TimeFormat;
import com.iris.model.UserRegulator;
import com.iris.repository.DateFormatRepo;
import com.iris.repository.TimeFormatRepo;
import com.iris.repository.UserRegulatorRepo;
import com.iris.util.constant.ErrorConstants;

/**
 * @author Siddique
 *
 */

@RestController
@RequestMapping("/service/userRegulator")
public class UserRegulatorController {

	private static final Logger LOGGER = LogManager.getLogger(UserRegulatorController.class);

	@Autowired
	private DateFormatRepo dateFormatRepo;

	@Autowired
	private TimeFormatRepo timeFormatRepo;

	@Autowired
	private UserRegulatorRepo userRegulatorRepo;

	@PostMapping(value = "/reloadMasterContent")
	public ServiceResponse reloadMasterContent(@RequestBody List<String> dateTimeFormatList) {

		try {
			if (CollectionUtils.isEmpty(dateTimeFormatList)) {
				DateFormat dateFormat = dateFormatRepo.findByIsActiveTrue();

				if (dateFormat != null) {
					ObjectCache.setDateFormat(dateFormat.getDateFrmt());
				}

				TimeFormat timeFormat = timeFormatRepo.findByIsActiveTrue();
				if (timeFormat != null) {
					ObjectCache.setTimeFormat(timeFormat.getTimeFrmt());
				}
			}
			if (!CollectionUtils.isEmpty(dateTimeFormatList)) {
				ObjectCache.setDateFormat(dateTimeFormatList.get(0));
				ObjectCache.setTimeFormat(dateTimeFormatList.get(1));
			}
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
			return new ServiceResponse.ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorConstants.DEFAULT_ERROR.getConstantVal()).setStatusMessage(ErrorConstants.DEFAULT_MSG.getConstantVal()).build();
		}

		return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setStatusMessage("User Regulator Master content reloaded Successfully").build();
	}

	@GetMapping(value = "/fetchUserRegulatorData")
	public ServiceResponse fetchUserRegulatorData(@RequestHeader(name = "JobProcessingId") String jobProcessId) {
		try {
			LOGGER.info("Request received to fetchUserRegulatorData info");
			UserRegulator userRegulator = userRegulatorRepo.findAll().get(0);
			userRegulator.setModifiedBy(null);
			LOGGER.info("Request completed to fetchUserRegulatorData info");
			return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setResponse(userRegulator).build();
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
			return new ServiceResponse.ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorConstants.DEFAULT_ERROR.getConstantVal()).setStatusMessage(ErrorConstants.DEFAULT_MSG.getConstantVal()).build();
		}
	}
}
