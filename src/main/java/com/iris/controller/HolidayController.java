package com.iris.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.model.Holiday;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author sajadhav
 */
@RestController
@RequestMapping("/service/holidayData")
public class HolidayController {
	static final Logger logger = LogManager.getLogger(HolidayController.class);

	@Autowired
	GenericService<Holiday, Long> holidayService;

	@RequestMapping(value = "/fetchActiveHolidayForYear/{year}", method = RequestMethod.GET)
	public ServiceResponse fetchActiveHolidayForYear(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable String year) {
		try {
			logger.info("fetch country data controller started " + year);
			if (year != null) {
				Map<String, Object> valueMap = new HashMap<String, Object>();
				String startYear = "01-01-" + year;
				String endYear = "31-12-" + year;
				valueMap.put(ColumnConstants.FROM_YEAR.getConstantVal(), DateManip.convertStringToDate(startYear, DateConstants.DD_MM_YYYY.getDateConstants()));
				valueMap.put(ColumnConstants.TO_YEAR.getConstantVal(), DateManip.convertStringToDate(endYear, DateConstants.DD_MM_YYYY.getDateConstants()));
				List<Holiday> holidayList = holidayService.getDataByObject(valueMap, MethodConstants.GET_ACTIVE_HOLIDAY_BY_YEAR.getConstantVal());
				List<String> holidayStringList = new ArrayList<>();
				for (Holiday holiday : holidayList) {
					if (holiday.getHolidayDate() != null) {
						holidayStringList.add(DateManip.convertDateToString(holiday.getHolidayDate(), DateConstants.DD_MM_YYYY.getDateConstants()));
					}
				}
				return new ServiceResponseBuilder().setStatus(true).setResponse(holidayStringList).build();
			} else {
				return new ServiceResponseBuilder().setStatus(false).setStatusMessage("Year is empty").build();
			}

		} catch (Exception e) {
			logger.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
			return new ServiceResponseBuilder().setStatus(false).setStatusMessage(ErrorConstants.DEFAULT_ERROR.getConstantVal()).build();
		}

	}

}