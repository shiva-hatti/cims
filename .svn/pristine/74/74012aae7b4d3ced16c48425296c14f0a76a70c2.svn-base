package com.iris.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.iris.dto.Option;
import com.iris.dto.Options;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.model.PurposeMasterBean;
import com.iris.service.impl.PurposeMasterService;

/**
 * @author bthakare
 * @date 10/01/2020
 * @version 1.0
 */

@RestController
@RequestMapping("/service/purposeMasterData")
public class PurposeMasterController {
	static final Logger logger = LogManager.getLogger(PurposeMasterController.class);

	@Autowired
	PurposeMasterService purposeMasterService;

	@RequestMapping(value = "/fetchActivePurposeMasterData", method = RequestMethod.GET)
	public ServiceResponse fetchActivePurposeMasterData() {
		logger.info("fetch Purpose Master Data controller started");
		ServiceResponse serviceResponse = null;
		Option option = null;
		Options options = null;
		List<Option> optionList = null;
		//	List<PurposeMasterBean> listOfActivePurposeMasterData = new ArrayList<>();

		optionList = new ArrayList<>();
		options = new Options();
		List<PurposeMasterBean> listOfActivePurposeMasterData = purposeMasterService.getActiveDataFor(PurposeMasterBean.class, null);

		if (listOfActivePurposeMasterData != null) {
			for (PurposeMasterBean purposeMasterBean : listOfActivePurposeMasterData) {
				option = new Option();
				option.setKey(purposeMasterBean.getPurposeCode().toUpperCase());
				option.setValue(purposeMasterBean.getDescription().toUpperCase());
				optionList.add(option);
			}

			options.setOptionList(optionList);
			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(options)).build();
		}
		return serviceResponse;
	}
}
