package com.iris.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.NullArgumentException;
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
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.model.BranchMasterDomestic;
import com.iris.model.ReturnDistrictMapping;
import com.iris.model.StateMaster;
import com.iris.repository.ReturnDistrictMappingRepo;
import com.iris.service.impl.StateMasterService;
import com.iris.util.Validations;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;

/**
 * @author Siddique H Khan
 * 
 * @author psahoo
 */
@RestController
@RequestMapping("/service/stateData")
public class StateMasterController {

	static final Logger logger = LogManager.getLogger(StateMasterController.class);

	@Autowired
	private StateMasterService stateMasterService;

	@Autowired
	private ReturnDistrictMappingRepo returnDistrictMappingRepo;

	@GetMapping(value = "/fetchActiveStateData/{languageCode}")
	public ServiceResponse fetchActiveStateList(@PathVariable String languageCode) {
		logger.info("fetch state data controller started " + languageCode);
		ServiceResponse serviceResponse = null;
		Option option = null;
		Options options = null;
		List<Option> optionList = null;
		List<StateMaster> listOfActiveStateData = null;
		if (languageCode != null) {

			optionList = new ArrayList<>();
			options = new Options();
			listOfActiveStateData = stateMasterService.getActiveDataFor(StateMaster.class, null);

			for (StateMaster stateMaster : listOfActiveStateData) {
				option = new Option();
				if (languageCode.equals(GeneralConstants.ENG_LANG.getConstantVal())) {
					option.setKey(stateMaster.getStateCode());
					option.setValue(stateMaster.getStateName().toUpperCase());
				} else if (languageCode.equals(GeneralConstants.HIN_LANG.getConstantVal())) {
					option.setKey(stateMaster.getStateCode());
					option.setValue(stateMaster.getStateNameBil().toUpperCase());
				}
				optionList.add(option);
			}
			Collections.sort(optionList, new Comparator<Option>() {
				@Override
				public int compare(Option o1, Option o2) {
					return o1.getValue().compareTo(o2.getValue());
				}
			});
			options.setOptionList(optionList);
			return new ServiceResponseBuilder().setStatus(true).setResponse(new Gson().toJson(options)).build();
		}
		return serviceResponse;
	}

	@GetMapping(value = "/fetchActiveStateDataWithLangCode/{languageCode}")
	public ServiceResponse fetchActiveStateDataWithLangCode(@PathVariable String languageCode) {
		logger.info("fetch state data controller started " + languageCode);
		ServiceResponse serviceResponse = null;
		Option option = null;
		Options options = null;
		List<Option> optionList = null;
		List<StateMaster> listOfActiveStateData = null;
		if (languageCode != null) {

			optionList = new ArrayList<>();
			options = new Options();
			listOfActiveStateData = stateMasterService.getActiveDataFor(StateMaster.class, null);

			for (StateMaster stateMaster : listOfActiveStateData) {
				option = new Option();
				if (languageCode.equals(GeneralConstants.ENG_LANG.getConstantVal())) {
					option.setKey(stateMaster.getStateName().toUpperCase());
					option.setValue(stateMaster.getStateName().toUpperCase());
				} else if (languageCode.equals(GeneralConstants.HIN_LANG.getConstantVal())) {
					option.setKey(stateMaster.getStateNameBil());
					option.setValue(stateMaster.getStateNameBil());
				}
				optionList.add(option);
			}
			Collections.sort(optionList, new Comparator<Option>() {
				@Override
				public int compare(Option o1, Option o2) {
					return o1.getValue().compareTo(o2.getValue());
				}
			});
			options.setOptionList(optionList);
			serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			serviceResponse.setResponse(options);
			return serviceResponse;
		}
		return serviceResponse;
	}

	@GetMapping(value = "/fetchActiveStateDataWithLangCodeWithStateCode/{languageCode}")
	public ServiceResponse fetchActiveStateDataWithLangCodeWithStateCode(@PathVariable String languageCode) {
		logger.info("fetch state data controller started " + languageCode);
		ServiceResponse serviceResponse = null;
		Option option = null;
		Options options = null;
		List<Option> optionList = null;
		List<StateMaster> listOfActiveStateData = null;
		if (languageCode != null) {

			optionList = new ArrayList<>();
			options = new Options();
			listOfActiveStateData = stateMasterService.getActiveDataFor(StateMaster.class, null);

			for (StateMaster stateMaster : listOfActiveStateData) {
				option = new Option();
				if (languageCode.equals(GeneralConstants.ENG_LANG.getConstantVal())) {
					option.setKey(stateMaster.getStateName().toUpperCase() + "~" + stateMaster.getStateCode().toUpperCase());
					option.setValue(stateMaster.getStateName().toUpperCase());
				} else if (languageCode.equals(GeneralConstants.HIN_LANG.getConstantVal())) {
					option.setKey(stateMaster.getStateNameBil() + "~" + stateMaster.getStateCode().toUpperCase());
					option.setValue(stateMaster.getStateNameBil());
				}
				optionList.add(option);
			}
			Collections.sort(optionList, new Comparator<Option>() {
				@Override
				public int compare(Option o1, Option o2) {
					return o1.getValue().compareTo(o2.getValue());
				}
			});
			options.setOptionList(optionList);
			serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			serviceResponse.setResponse(options);
			return serviceResponse;
		}
		return serviceResponse;
	}

	@GetMapping(value = "/fetchActiveStateCodeList")
	public ServiceResponse fetchActiveStateCodeData() {
		ServiceResponse serviceResponse = null;
		List<StateMaster> listOfActiveStateData = null;

		listOfActiveStateData = stateMasterService.getActiveDataFor(StateMaster.class, null);

		List<String> stateCodeList = listOfActiveStateData.stream().map(StateMaster::getStateCode).collect(Collectors.toList());

		serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		serviceResponse.setResponse(stateCodeList);
		return serviceResponse;
	}

	@GetMapping("/getMappedStateDistrictUponReturnCode/{returnCode}")
	public ServiceResponse getMappedStateDistrictUponReturnCode(@PathVariable("returnCode") String returnCode) {
		ServiceResponse response = null;
		try {
			if (Validations.isEmpty(returnCode)) {
				logger.error("Exception while fetching drop down value, if returnCode is empty in getMappedStateDistrictUponReturnCode");
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0889.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString())).build();
			}

			Options options = new Options();

			List<ReturnDistrictMapping> returnDistrictMappingList = returnDistrictMappingRepo.getStateDistictUponReturnCode(returnCode);

			if (CollectionUtils.isEmpty(returnDistrictMappingList)) {
				logger.error("Exception while fetching Dynamic drop down, if drop down type list is empty");
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString())).build();
			}
			Option option;
			List<Option> listOfOption = new ArrayList<>();
			for (ReturnDistrictMapping dropDownValues : returnDistrictMappingList) {
				option = new Option();
				option.setKey(dropDownValues.getDistrictIdFk().getStateIdFk().getStateName().toUpperCase() + "~" + dropDownValues.getDistrictIdFk().getDistrictName().toUpperCase());
				option.setValue(dropDownValues.getDistrictIdFk().getStateIdFk().getStateCode() + "~" + dropDownValues.getDistrictIdFk().getDistrictCode());
				listOfOption.add(option);
			}
			Collections.sort(listOfOption, new Comparator<Option>() {
				@Override
				public int compare(Option o1, Option o2) {
					return o1.getValue().compareTo(o2.getValue());
				}
			});

			options.setOptionList(listOfOption);
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(options);
			return response;

		} catch (NullArgumentException nae) {
			logger.error("Exception while fetching Dynamic drop down With lang code dropdownType");
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
	}

	@GetMapping("/getStateDistrictUponReturnCode/{returnCode}")
	public ServiceResponse getStateDistrictUponReturnCode(@PathVariable("returnCode") String returnCode) {
		ServiceResponse response = null;
		try {
			if (Validations.isEmpty(returnCode)) {
				logger.error("Exception while fetching drop down value, if returnCode is empty in getStateDistrictUponReturnCode");
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0889.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString())).build();
			}

			Options options = new Options();

			List<ReturnDistrictMapping> returnDistrictMappingList = returnDistrictMappingRepo.getStateDistictUponReturnCode(returnCode);

			if (CollectionUtils.isEmpty(returnDistrictMappingList)) {
				logger.error("Exception while fetching Dynamic drop down, if drop down type list is empty");
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString())).build();
			}
			Option option;
			List<Option> listOfOption = new ArrayList<>();
			for (ReturnDistrictMapping dropDownValues : returnDistrictMappingList) {
				option = new Option();
				option.setKey(dropDownValues.getDistrictIdFk().getStateIdFk().getStateName().toUpperCase() + "~" + dropDownValues.getDistrictIdFk().getStateIdFk().getStateCode());
				option.setValue(dropDownValues.getDistrictIdFk().getDistrictName().toUpperCase() + "~" + dropDownValues.getDistrictIdFk().getDistrictCode());
				listOfOption.add(option);
			}
			Collections.sort(listOfOption, new Comparator<Option>() {
				@Override
				public int compare(Option o1, Option o2) {
					return o1.getValue().compareTo(o2.getValue());
				}
			});

			options.setOptionList(listOfOption);
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(options);
			return response;

		} catch (NullArgumentException nae) {
			logger.error("Exception while fetching Dynamic drop down With returnCode dropdownType");
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
	}

	/**
	 * Added By psahoo
	 */
	@GetMapping("/getStateMasterDetails/{langCode}/{stateName}")
	public ServiceResponse getStateMasterDetails(@PathVariable(name = "langCode") String langCode, @PathVariable(name = "stateName") String stateName) {
		logger.info("fetch getStateMasterDetails controller started. Language Code : " + langCode + " State Code/Name : " + stateName);
		ServiceResponse response = null;
		Option option = null;
		Options options = null;
		List<Option> optionList = null;
		List<StateMaster> listOfActiveStateData = null;

		try {
			listOfActiveStateData = new ArrayList<>();
			optionList = new ArrayList<>();
			options = new Options();

			if (Validations.isEmpty(langCode) && Validations.isEmpty(stateName)) {
				logger.error("Language Code and State Code/Name is Empty");
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0889.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString())).build();
			}

			if (stateName.equals(GeneralConstants.STATE_CODE.getConstantVal()) || stateName.equals(GeneralConstants.STATE_NAME.getConstantVal()) || stateName.equals(GeneralConstants.STATE_CODE_STATE_NAME.getConstantVal())) {
				listOfActiveStateData = stateMasterService.getActiveDataFor(StateMaster.class, null);
			}

			if (CollectionUtils.isEmpty(listOfActiveStateData)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString())).build();
			}

			if (langCode.equals(GeneralConstants.ENG_LANG.getConstantVal())) {
				logger.info("Language is English");
				if (stateName.equals(GeneralConstants.STATE_CODE.getConstantVal())) {
					for (StateMaster stateMaster : listOfActiveStateData) {
						option = new Option();
						option.setKey(stateMaster.getStateCode());
						option.setValue(stateMaster.getStateCode());
						optionList.add(option);
					}
				} else if (stateName.equals(GeneralConstants.STATE_NAME.getConstantVal())) {
					for (StateMaster stateMaster : listOfActiveStateData) {
						option = new Option();
						option.setKey(stateMaster.getStateName());
						option.setValue(stateMaster.getStateName());
						optionList.add(option);
					}
				} else if (stateName.equals(GeneralConstants.STATE_CODE_STATE_NAME.getConstantVal())) {
					for (StateMaster stateMaster : listOfActiveStateData) {
						option = new Option();
						option.setKey(stateMaster.getStateCode());
						option.setValue(stateMaster.getStateName());
						optionList.add(option);
					}
				}

			} else if (langCode.equals(GeneralConstants.HIN_LANG.getConstantVal())) {
				logger.info("Language is Hindi");
				if (stateName.equals(GeneralConstants.STATE_CODE.getConstantVal())) {
					for (StateMaster stateMaster : listOfActiveStateData) {
						option = new Option();
						option.setKey(stateMaster.getStateCode());
						option.setValue(stateMaster.getStateCode());
						optionList.add(option);
					}
				} else if (stateName.equals(GeneralConstants.STATE_NAME.getConstantVal())) {
					for (StateMaster stateMaster : listOfActiveStateData) {
						option = new Option();
						option.setKey(stateMaster.getStateName());
						option.setValue(stateMaster.getStateName());
						optionList.add(option);
					}
				} else if (stateName.equals(GeneralConstants.STATE_CODE_STATE_NAME.getConstantVal())) {
					for (StateMaster stateMaster : listOfActiveStateData) {
						option = new Option();
						option.setKey(stateMaster.getStateCode());
						option.setValue(stateMaster.getStateName());
						optionList.add(option);
					}
				}
			}
			options.setOptionList(optionList);
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(options);
			return response;
		} catch (Exception e) {
			logger.error("Exception occoured while featch State Details list" + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}

	}

}