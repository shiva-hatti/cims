/**
 * 
 */
package com.iris.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.model.DateAndTimeFormatResponse;
import com.iris.model.DateFormat;
import com.iris.model.TimeFormat;
import com.iris.repository.DateFormatRepo;
import com.iris.repository.TimeFormatRepo;
import com.iris.util.constant.ErrorConstants;

/**
 * @author apagaria
 * 
 *         This API is used to get the Active Date and Time format from TBL_DATE_FORMAT and TBL_TIME_FORMAT
 *
 */

@RestController
@RequestMapping("/service/dateAndTime")
public class DateAndTimeController {

	private static final Logger logger = LogManager.getLogger(DateAndTimeController.class);

	@Autowired
	private DateFormatRepo dateFormatRepo;

	@Autowired
	private TimeFormatRepo timeFormatRepo;

	@GetMapping(value = "/getActiveDateAndTimeFormat")
	public ServiceResponse getActiveDateAndTimeFormat(@RequestHeader(name = "AppId") String appId, @RequestHeader(name = "JobProcessingId") String jobProcessingId) {
		DateAndTimeFormatResponse dateAndTimeFormatResponse = new DateAndTimeFormatResponse();
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();
		serviceResponseBuilder.setStatus(true);
		try {

			DateFormat dateFormat = dateFormatRepo.findByIsActiveTrue();
			if (dateFormat != null) {
				dateAndTimeFormatResponse.setActiveDateFormat(dateFormat.getDateFrmt());
			}

			TimeFormat timeFormat = timeFormatRepo.findByIsActiveTrue();
			if (timeFormat != null) {
				dateAndTimeFormatResponse.setActiveTimeFormat(timeFormat.getTimeFrmt());
			}

		} catch (Exception e) {
			// Exception occured
			logger.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
			return new ServiceResponse.ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorConstants.DEFAULT_ERROR.getConstantVal()).setStatusMessage(ErrorConstants.DEFAULT_MSG.getConstantVal()).build();
		}

		// Set Response 
		serviceResponseBuilder.setResponse(dateAndTimeFormatResponse);
		ServiceResponse serviceResponse = serviceResponseBuilder.build();

		// Send Response
		return serviceResponse;
	}
}
