package com.iris.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.dto.ServiceResponse;
import com.iris.dto.XbrlWebFormDto;
import com.iris.service.impl.XbrlWebFormService;

@RestController
@RequestMapping("/xbrlWebFormSession/verifySession")
public class XbrlSessionCheckController {
	static final Logger logger = LogManager.getLogger(XbrlSessionCheckController.class);

	@Autowired
	private XbrlWebFormService xbrlWebFormService;

	@PostMapping(value = "/checkXbrlWebFormSession")
	public ServiceResponse checkXbrlSession(@RequestBody XbrlWebFormDto xbrlWebFormDto) {
		logger.error("in checkXbrlWebFormSession start ");
		boolean status = xbrlWebFormService.checkXbrlUserSessionChk(xbrlWebFormDto.getUserName(), xbrlWebFormDto.getJwtToken());
		logger.error("in checkXbrlWebFormSession end " + status);
		return new ServiceResponse.ServiceResponseBuilder().setStatus(status).setResponse(status).build();
	}
}
