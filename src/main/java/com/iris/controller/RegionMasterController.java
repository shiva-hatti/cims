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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.iris.dto.Option;
import com.iris.dto.Options;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.model.RegionMaster;
import com.iris.service.impl.RegionMasterService;
import com.iris.util.constant.GeneralConstants;

/**
 * @author Siddique H Khan
 */
@RestController
@RequestMapping("/service/regionData")
public class RegionMasterController {

	static final Logger logger = LogManager.getLogger(RegionMasterController.class);

	@Autowired
	RegionMasterService regionMasterService;

	@GetMapping(value = "/fetchActiveRegionData/{languageCode}")
	public ServiceResponse fetchActiveRegionList(@PathVariable String languageCode) {
		logger.info("fetch Region data controller started " + languageCode);
		ServiceResponse serviceResponse = null;
		Option option = null;
		Options options = null;
		List<Option> optionList = null;
		List<RegionMaster> listOfActiveRegionData = null;
		if (languageCode != null) {

			listOfActiveRegionData = new ArrayList<>();
			optionList = new ArrayList<>();
			options = new Options();
			listOfActiveRegionData = regionMasterService.getActiveDataFor(RegionMaster.class, null);

			for (RegionMaster regionMaster : listOfActiveRegionData) {
				option = new Option();
				if (languageCode.equals(GeneralConstants.ENG_LANG.getConstantVal())) {
					option.setKey(regionMaster.getRegionCode());
					option.setValue(regionMaster.getRegionName().toUpperCase());
				} else if (languageCode.equals(GeneralConstants.HIN_LANG.getConstantVal())) {
					option.setKey(regionMaster.getRegionCode());
					option.setValue(regionMaster.getRegionNameBil().toUpperCase());
				}
				optionList.add(option);
			}
			options.setOptionList(optionList);
			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(options)).build();
		}
		return serviceResponse;
	}
}