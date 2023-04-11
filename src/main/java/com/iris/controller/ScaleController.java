package com.iris.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.iris.dto.Option;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.model.Scale;
import com.iris.repository.ScaleRepo;

@RestController
@RequestMapping("/service/scale")
public class ScaleController {

	static final Logger logger = LogManager.getLogger(ScaleController.class);

	@Autowired
	private ScaleRepo scaleRepo;

	@PostMapping(value = "/getActiveScale/{isScaleNameWebform}")
	public ServiceResponse getActiveScale(@RequestHeader(name = "AppId") String appId, @RequestHeader(name = "JobProcessingId") String jobProcessingId, @PathVariable String isScaleNameWebform) {
		try {
			logger.info("getActiveScale method started");
			List<Scale> scaleList = scaleRepo.findByIsActiveTrue();
			Option option;
			List<Option> listOfOption = new ArrayList<>();
			for (Scale scale : scaleList) {
				option = new Option();
				option.setKey(scale.getScaleValue());
				if (Boolean.parseBoolean(isScaleNameWebform)) {
					option.setValue(scale.getScaleNameWebform());
				} else {
					option.setValue(scale.getScaleName());
				}
				listOfOption.add(option);
			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(listOfOption)).build();
		} catch (Exception e) {
			logger.error("getActiveScale method end ", e);
			return new ServiceResponseBuilder().setStatus(false).setResponse(null).build();
		}
	}

}