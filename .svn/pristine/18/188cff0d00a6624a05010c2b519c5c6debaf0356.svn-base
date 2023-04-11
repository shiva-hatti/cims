/**
 * 
 */
package com.iris.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.iris.dto.Option;
import com.iris.dto.Options;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.model.PSUCategoryMaster;
import com.iris.service.impl.PSUCategoryMasterService;

/**
 * @author apagaria
 *
 */
@RestController
@RequestMapping("/service/psuCategoryData")
public class PSUCategoryMasterController {

	static final Logger logger = LogManager.getLogger(PSUCategoryMasterController.class);
	@Autowired
	PSUCategoryMasterService psuCategoryMasterService;

	@GetMapping(value = "/fetchActivePSUCategoryData")
	public ServiceResponse fetchActiveCountryList() {
		Option option = null;
		Options options = null;
		List<Option> optionList = null;
		List<PSUCategoryMaster> listOfActivePSUCategoryData = null;

		listOfActivePSUCategoryData = new ArrayList<>();
		optionList = new ArrayList<>();
		options = new Options();
		listOfActivePSUCategoryData = psuCategoryMasterService.getActiveDataFor(PSUCategoryMaster.class, null);

		for (PSUCategoryMaster psuCategoryMaster : listOfActivePSUCategoryData) {
			option = new Option();
			option.setKey(psuCategoryMaster.getCategoryCode());
			option.setValue(psuCategoryMaster.getCategoryName().toUpperCase());
			optionList.add(option);
		}
		options.setOptionList(optionList);
		return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(options)).build();
	}

}
