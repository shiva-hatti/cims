package com.iris.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.iris.model.DistrictMaster;
import com.iris.model.StateMaster;
import com.iris.service.impl.DistrictMasterService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author Siddique H Khan
 */
@RestController
@RequestMapping("/service/districtData")
public class DistrictMasterController {
	static final Logger logger = LogManager.getLogger(DistrictMasterController.class);

	@Autowired
	DistrictMasterService districtMasterService;

	@GetMapping(value = "/fetchActiveDistrictData/{languageCode}")
	public ServiceResponse fetchActiveDistrictList(@PathVariable String languageCode) {
		logger.info("fetch district data controller started " + languageCode);
		ServiceResponse serviceResponse = null;
		Option option = null;
		Options options = null;
		List<Option> optionList = null;
		List<DistrictMaster> listOfActiveDistrictData = null;

		if (languageCode != null) {
			optionList = new ArrayList<>();
			options = new Options();
			listOfActiveDistrictData = new ArrayList<>();
			listOfActiveDistrictData = districtMasterService.getActiveDataFor(DistrictMaster.class, null);

			for (DistrictMaster districtMaster : listOfActiveDistrictData) {
				option = new Option();
				if (languageCode.equals(GeneralConstants.ENG_LANG.getConstantVal())) {
					option.setKey(districtMaster.getId() + "");
					option.setValue(districtMaster.getDistrictName().toUpperCase());
				} else if (languageCode.equals(GeneralConstants.HIN_LANG.getConstantVal())) {
					option.setKey(districtMaster.getId() + "");
					option.setValue(districtMaster.getDistrictNameBil().toUpperCase());
				}
				optionList.add(option);
			}
			options.setOptionList(optionList);
			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(options)).build();
		}
		return serviceResponse;
	}

	@GetMapping(value = "/fetchAllActiveDistrictData/{languageCode}")
	public ServiceResponse fetchAllActiveDistrictList(@PathVariable String languageCode) {
		logger.info("fetch district data controller started " + languageCode);
		List<DistrictMaster> listOfActiveDistrictData = null;
		try {
			listOfActiveDistrictData = districtMasterService.getActiveDataFor(DistrictMaster.class, null);
			List<DistrictMaster> listOfActiveDistrictDataResult = prepareDistrictObjectList(listOfActiveDistrictData);
			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(listOfActiveDistrictDataResult)).build();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(e.getMessage()).setStatusMessage(e.getMessage()).build();
		}
	}

	@GetMapping("/fetchActiveDistrictDataByState/{stateCode}/{languageCode}")
	public ServiceResponse fetchActiveDistrictListByState(@PathVariable("stateCode") String stateCode, @PathVariable String languageCode) {
		try {
			Map<String, List<String>> columnValueMap = new HashMap<>();
			List<String> stateList = new ArrayList<>();
			stateList.add(stateCode);
			columnValueMap.put(ColumnConstants.STATE_CODE.getConstantVal(), stateList);
			List<DistrictMaster> listOfActiveDistrictData = districtMasterService.getDataByColumnValue(columnValueMap, MethodConstants.GET_DISTRICT_LIST_BY_STATE_CODE.getConstantVal());
			List<DistrictMaster> listOfActiveDistrictDataResult = prepareDistrictObjectList(listOfActiveDistrictData);
			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(listOfActiveDistrictDataResult)).build();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(e.getMessage()).setStatusMessage(e.getMessage()).build();
		}
	}

	private List<DistrictMaster> prepareDistrictObjectList(List<DistrictMaster> listOfActiveDistrictData) {
		List<DistrictMaster> districtMasterListNew = new ArrayList<>();
		for (DistrictMaster districtMaster : listOfActiveDistrictData) {
			DistrictMaster tempDistrictMaster = new DistrictMaster();
			tempDistrictMaster.setId(districtMaster.getId());
			tempDistrictMaster.setDistrictCode(districtMaster.getDistrictCode());
			tempDistrictMaster.setDistrictName(districtMaster.getDistrictName());
			StateMaster tempStateMaster = new StateMaster();
			tempStateMaster.setId(districtMaster.getStateIdFk().getId());
			tempStateMaster.setStateCode(districtMaster.getStateIdFk().getStateCode());
			tempStateMaster.setStateName(districtMaster.getStateIdFk().getStateName());
			tempDistrictMaster.setStateIdFk(tempStateMaster);
			districtMasterListNew.add(tempDistrictMaster);
		}
		return districtMasterListNew;
	}

	@GetMapping(value = "/fetchActiveDistrictUponState/{stateCode}")
	public ServiceResponse fetchActiveDistrictUponState(@PathVariable String stateCode) {
		logger.info("fetch district data controller started " + stateCode);
		ServiceResponse serviceResponse = null;
		Option option = null;
		Options options = null;
		List<Option> optionList = null;

		if (stateCode != null) {
			optionList = new ArrayList<>();
			options = new Options();
			Map<String, List<String>> columnValueMap = new HashMap<>();
			List<String> stateList = new ArrayList<>();
			stateList.add(stateCode);
			columnValueMap.put(ColumnConstants.STATE_CODE.getConstantVal(), stateList);
			List<DistrictMaster> listOfActiveDistrictData = districtMasterService.getDataByColumnValue(columnValueMap, MethodConstants.GET_DISTRICT_LIST_BY_STATE_CODE.getConstantVal());
			for (DistrictMaster districtMaster : listOfActiveDistrictData) {
				option = new Option();
				option.setKey(districtMaster.getDistrictName() + "~" + districtMaster.getDistrictCode());
				option.setValue(districtMaster.getDistrictName().toUpperCase());
				optionList.add(option);
			}
			options.setOptionList(optionList);
			serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			serviceResponse.setResponse(options);
			return serviceResponse;
		}
		return serviceResponse;
	}

	@GetMapping(value = "/fetchActiveDistrictCodeList")
	public ServiceResponse fetchActiveDistrictCodeData() {
		ServiceResponse serviceResponse = null;
		List<DistrictMaster> listOfActiveDistrictData = null;

		listOfActiveDistrictData = districtMasterService.getActiveDataFor(DistrictMaster.class, null);

		List<String> districtCodeList = listOfActiveDistrictData.stream().map(DistrictMaster::getDistrictCode).collect(Collectors.toList());

		serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		serviceResponse.setResponse(districtCodeList);
		return serviceResponse;
	}

	@GetMapping(value = "/fetchActiveDistrictCodeWithStateCode")
	public ServiceResponse fetchActiveDistrictCodeWithStateCode() {
		ServiceResponse serviceResponse = null;
		List<DistrictMaster> listOfActiveDistrictData = null;
		List<String> districtCodeList = new ArrayList<>();

		listOfActiveDistrictData = districtMasterService.getActiveDataFor(DistrictMaster.class, null);

		for (DistrictMaster districtMaster : listOfActiveDistrictData) {
			districtCodeList.add((districtMaster.getStateIdFk() != null ? districtMaster.getStateIdFk().getStateCode() : "") + "~" + districtMaster.getDistrictCode());
		}

		serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		serviceResponse.setResponse(districtCodeList);
		return serviceResponse;
	}
}