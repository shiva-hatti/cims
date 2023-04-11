package com.iris.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.dto.ServiceResponse;
import com.iris.model.ReturnGroupMod;
import com.iris.service.impl.ReturnGroupModServiceV2;
import com.iris.util.constant.GeneralConstants;

/**
 * @author pmohite
 *
 */
@RestController
@RequestMapping("/service/returnGroupModController/V2")
public class ReturnGroupModControllerV2 {

	private static final Logger LOGGER = LogManager.getLogger(ReturnGroupModControllerV2.class);

	@Autowired
	private ReturnGroupModServiceV2 returnGroupModServiceV2;

	@PostMapping(value = "/getReturnGroupModList/{returnGroupId}/{languageCode}")
	public ServiceResponse getReturnGroupModList(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable Long returnGroupId, @PathVariable String languageCode) {
		ServiceResponse serviceResponse = null;
		LOGGER.info("getReturnGroupModList method Start for job processigid : " + jobProcessId);
		try {
			if (StringUtils.isEmpty(languageCode)) {
				languageCode = GeneralConstants.ENG_LANG.getConstantVal();
			}
			List<ReturnGroupMod> returnGroupModList = returnGroupModServiceV2.getReturnGroupModList(returnGroupId, languageCode, 1);
			serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			serviceResponse.setResponse(returnGroupModList);
		} catch (Exception e) {
			LOGGER.error("Exception while fetching return group Mod list for job processigid : " + jobProcessId, e);
			serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
		}
		return serviceResponse;
	}
}
