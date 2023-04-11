package com.iris.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.dto.ServiceResponse;
import com.iris.model.ReturnGroupLblMod;
import com.iris.service.impl.ReturnGroupLabelModServiceV2;

/**
 * @author pmohite
 *
 */
@RestController
@RequestMapping("/service/returnGroupLabelModController/V2")
public class ReturnGroupLabelModControllerV2 {

	private static final Logger LOGGER = LogManager.getLogger(ReturnGroupLabelModControllerV2.class);

	@Autowired
	private ReturnGroupLabelModServiceV2 returnGroupLabelModServiceV2;

	@PostMapping(value = "/getReturnGroupLabelModList/{returnGroupLabelId}")
	public ServiceResponse getReturnGroupLabelModList(@RequestHeader(name = "JobProcessingId") String jobProcessId, @PathVariable Long returnGroupLabelId) {
		ServiceResponse serviceResponse = null;
		LOGGER.info("getReturnGroupLabelModList method Start for job processigid : " + jobProcessId);
		try {
			List<ReturnGroupLblMod> returnGroupLabelModList = returnGroupLabelModServiceV2.getAllDataFor(null, returnGroupLabelId);
			serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			serviceResponse.setResponse(returnGroupLabelModList);
		} catch (Exception e) {
			LOGGER.error("Exception while fetching return group label Mod list for job processigid : " + jobProcessId, e);
			serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
		}
		return serviceResponse;
	}
}
