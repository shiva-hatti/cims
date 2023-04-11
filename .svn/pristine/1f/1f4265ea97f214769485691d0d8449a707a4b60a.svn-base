package com.iris.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.iris.caching.ObjectCache;
import com.iris.dto.Option;
import com.iris.dto.Options;
import com.iris.dto.PincodeMasterDto;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ServiceException;
import com.iris.model.CityMaster;
import com.iris.model.PincodeMaster;
import com.iris.service.impl.CityMasterService;
import com.iris.service.impl.PincodeMasterService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;

@RestController
@RequestMapping("/service/allPincodeData")
public class PincodeMasterController {
	static final Logger logger = LogManager.getLogger(PincodeMasterController.class);

	@Autowired
	PincodeMasterService pincodeMasterService;

	@GetMapping(value = "/fetchActivePincodeData/{languageCode}")
	public ServiceResponse fetchActivePincodeList(@PathVariable String languageCode) {
		logger.info("fetch Pincode data controller started ");
		List<PincodeMaster> listOfActivePincodeData = null;
		listOfActivePincodeData = pincodeMasterService.getActiveDataFor(PincodeMaster.class, null);

		if (!CollectionUtils.isEmpty(listOfActivePincodeData)) {
			List<PincodeMasterDto> finalPincodeObject = preparePincodeObject(listOfActivePincodeData);
			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(finalPincodeObject)).build();
		}
		return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(listOfActivePincodeData)).build();
	}

	@GetMapping(value = "/fetchActivePincodeDataByCity/{cityId}/{languageCode}")
	public ServiceResponse fetchActivePincodeListByCity(@PathVariable("cityId") Long cityId, @PathVariable String languageCode) {
		Map<String, List<String>> columnValueMap = new HashMap<>();

		List<String> cityList = new ArrayList<>();
		cityList.add(cityId + "");
		columnValueMap.put(ColumnConstants.CITY_ID.getConstantVal(), cityList);
		List<PincodeMaster> listOfActivePincodeData = pincodeMasterService.getDataByColumnValue(columnValueMap, MethodConstants.GET_PINCODE_LIST_BY_CITY_ID.getConstantVal());
		if (!CollectionUtils.isEmpty(listOfActivePincodeData)) {
			List<PincodeMasterDto> finalPincodeObject = preparePincodeObject(listOfActivePincodeData);
			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(finalPincodeObject)).build();
		}
		return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(listOfActivePincodeData)).build();
	}

	private List<PincodeMasterDto> preparePincodeObject(List<PincodeMaster> listOfActivePincodeData) {
		List<PincodeMasterDto> pincodeMasterListNew = new ArrayList();
		for (PincodeMaster pincodeMaster : listOfActivePincodeData) {
			PincodeMasterDto pincodeMasterDto = new PincodeMasterDto();
			pincodeMasterDto.setId(pincodeMaster.getId());
			pincodeMasterDto.setPincode(pincodeMaster.getPincode());
			pincodeMasterDto.setIsActive(pincodeMaster.getIsActive());
			pincodeMasterDto.setCityId(pincodeMaster.getCityIdFk().getId() + "");
			pincodeMasterDto.setCityCode(pincodeMaster.getCityIdFk().getCityCode());
			pincodeMasterDto.setCityName(pincodeMaster.getCityIdFk().getCityName());
			pincodeMasterDto.setCityNameBil(pincodeMaster.getCityIdFk().getCityNameBil());
			pincodeMasterDto.setStdCode(pincodeMaster.getCityIdFk().getStdCode());
			pincodeMasterDto.setStateId(pincodeMaster.getCityIdFk().getDistrictIdFk().getStateIdFk().getId() + "");
			pincodeMasterDto.setStateCode(pincodeMaster.getCityIdFk().getDistrictIdFk().getStateIdFk().getStateCode());
			pincodeMasterDto.setStateName(pincodeMaster.getCityIdFk().getDistrictIdFk().getStateIdFk().getStateName());
			pincodeMasterDto.setStateNameBil(pincodeMaster.getCityIdFk().getDistrictIdFk().getStateIdFk().getStateNameBil());
			pincodeMasterListNew.add(pincodeMasterDto);
		}
		return pincodeMasterListNew;
	}

	@GetMapping(value = "/fetchActivePincodeDataByCityName/{cityName}")
	public ServiceResponse fetchActivePincodeDataByCityName(@PathVariable("cityName") String cityName) {
		ServiceResponse serviceResponse = null;
		Map<String, List<String>> columnValueMap = new HashMap<>();
		try {
			List<String> cityList = new ArrayList<>();
			cityList.add(cityName);
			columnValueMap.put(ColumnConstants.CITY_NAME.getConstantVal(), cityList);
			List<PincodeMaster> listOfActivePincodeData = pincodeMasterService.getDataByColumnValue(columnValueMap, MethodConstants.GET_PINCODE_LIST_BY_CITY_NAME.getConstantVal());
			if (CollectionUtils.isEmpty(listOfActivePincodeData)) {
				logger.error("Exception while fetching City Dynamic drop down With lang code and cityName is empty ");
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
			}
			Option option;
			Options options = new Options();

			List<Option> listOfOption = new ArrayList<>();
			for (PincodeMaster pincodeMasterdropDownValues : listOfActivePincodeData) {
				option = new Option();
				option.setKey(String.valueOf(pincodeMasterdropDownValues.getPincode()));
				option.setValue(String.valueOf(pincodeMasterdropDownValues.getPincode()));
				listOfOption.add(option);
			}

			options.setOptionList(listOfOption);
			serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			serviceResponse.setResponse(options);
			return serviceResponse;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}

	}
}
