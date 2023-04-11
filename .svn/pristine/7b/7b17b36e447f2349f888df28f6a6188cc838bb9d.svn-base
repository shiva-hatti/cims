package com.iris.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iris.dto.ServiceResponse;
import com.iris.service.impl.ActivityApplicableMenuServie;

@RestController
@RequestMapping("/service")
public class ActivityApplicableMenuController {

	@Autowired
	ActivityApplicableMenuServie service;

	@RequestMapping(value = "/activityApplicableMenu/{isDept}", method = { RequestMethod.GET, RequestMethod.POST })
	public ServiceResponse getActivityApplicableMenu(@PathVariable("isDept") Boolean isDept) {
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		response.setResponse(service.getActivityApplicableMenu(isDept));
		return response;

	}

}
