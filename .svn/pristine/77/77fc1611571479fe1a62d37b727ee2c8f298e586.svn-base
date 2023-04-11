package com.iris.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dto.Option;
import com.iris.dto.OptionBean;
import com.iris.dto.Options;
import com.iris.dto.ServiceResponse;
import com.iris.dto.XbrlWebFormDto;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.model.BranchMasterOverseas;
import com.iris.service.impl.BranchMasterOverseasService;
import com.iris.service.impl.CountryMasterService;
import com.iris.util.Validations;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;

@RestController
@RequestMapping("/service/overseasBranchMaster")
public class BranchMasterOverseasController {

	static final Logger logger = LogManager.getLogger(BranchMasterOverseasController.class);

	@Autowired
	private BranchMasterOverseasService branchMasterOverseasService;

	@Autowired
	private CountryMasterService countryMasterService;

	@GetMapping(value = "/fetchOverseasBranchData/{countryCode}/{entityCode}")
	public ServiceResponse fetchOverseasBranchData(@PathVariable String countryCode, @PathVariable String entityCode) {
		logger.info("fetch fetchBranchMasterOverseasData method started " + countryCode);
		ServiceResponse serviceResponse = null;
		if (StringUtils.isBlank(countryCode) || StringUtils.isBlank(entityCode)) {
			return serviceResponse;
		}
		List<BranchMasterOverseas> listOfActiveBranchMasterOverseas = null;
		Option option = null;
		List<Option> optionList = new ArrayList<>();
		Map<String, Object> columnValueMap = new HashMap<>();
		columnValueMap.put(ColumnConstants.COUNTRY_CODE.getConstantVal(), countryCode);
		columnValueMap.put(ColumnConstants.ENTITY_CODE.getConstantVal(), entityCode);
		listOfActiveBranchMasterOverseas = branchMasterOverseasService.getDataByObject(columnValueMap, MethodConstants.GET_DATA_BY_COUNTRY_CODE.getConstantVal());
		for (BranchMasterOverseas branchMasterOverseas : listOfActiveBranchMasterOverseas) {
			option = new Option();
			option.setKey(branchMasterOverseas.getBranchCode());
			option.setValue(branchMasterOverseas.getBranch());
			optionList.add(option);
		}
		Collections.sort(optionList, new Comparator<Option>() {
			@Override
			public int compare(Option o1, Option o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});
		logger.info("fetch fetchBranchMasterOverseasData list size" + optionList.size());
		serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		serviceResponse.setResponse(optionList);
		return serviceResponse;

	}

	/**
	 * Added By psahoo
	 */
	@GetMapping("/getBranchOverseasDetails/{langCode}/{branchCode}")
	public ServiceResponse getBranchOverseasDetails(@PathVariable(name = "langCode") String langCode, @PathVariable(name = "branchCode") String branchCode) {
		logger.info("fetch getBranchOverseasDetails controller started. Language Code : " + langCode + " Branch Code/Name : " + branchCode);
		ServiceResponse response = null;
		Option option = null;
		Options options = null;
		List<Option> optionList = null;
		List<BranchMasterOverseas> listOfActiveBranchOverseasData = null;

		try {
			listOfActiveBranchOverseasData = new ArrayList<>();
			optionList = new ArrayList<>();
			options = new Options();

			if (Validations.isEmpty(langCode) && Validations.isEmpty(branchCode)) {
				logger.error("Language Code and Branch Code/Name is Empty");
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0889.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString())).build();
			}

			listOfActiveBranchOverseasData = branchMasterOverseasService.getAllDataForBranchOverseas(BranchMasterOverseas.class, null);
			if (CollectionUtils.isEmpty(listOfActiveBranchOverseasData)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString())).build();
			}

			if (langCode.equals(GeneralConstants.ENG_LANG.getConstantVal())) {
				logger.info("Language is English");
				System.out.println("Language is English");

				if (branchCode.equals(GeneralConstants.BRANCH_CODE.getConstantVal())) {
					for (BranchMasterOverseas branchOverseas : listOfActiveBranchOverseasData) {
						option = new Option();
						option.setKey(branchOverseas.getBranchCode());
						option.setValue(branchOverseas.getBranchCode());
						optionList.add(option);
					}
				} else if (branchCode.equals(GeneralConstants.BRANCH_NAME.getConstantVal())) {
					for (BranchMasterOverseas branchOverseas : listOfActiveBranchOverseasData) {
						option = new Option();
						option.setKey(branchOverseas.getBranch());
						option.setValue(branchOverseas.getBranch());
						optionList.add(option);
					}
				}

			} else if (langCode.equals(GeneralConstants.HIN_LANG.getConstantVal())) {
				logger.info("Language is Hindi");
				System.out.println("Language is Hindi");

				if (branchCode.equals(GeneralConstants.BRANCH_CODE.getConstantVal())) {
					for (BranchMasterOverseas branchOverseas : listOfActiveBranchOverseasData) {
						option = new Option();
						option.setKey(branchOverseas.getBranchCode());
						option.setValue(branchOverseas.getBranchCode());
						optionList.add(option);
					}
				} else if (branchCode.equals(GeneralConstants.BRANCH_NAME.getConstantVal())) {
					for (BranchMasterOverseas branchOverseas : listOfActiveBranchOverseasData) {
						option = new Option();
						option.setKey(branchOverseas.getBranch());
						option.setValue(branchOverseas.getBranch());
						optionList.add(option);
					}
				}
			}
			options.setOptionList(optionList);
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(options);
			return response;

		} catch (Exception e) {
			logger.error("Exception occoured while featch Entity Details list" + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}

	}

	/**
	 * Added by psahoo Fetch Active Country-Branch List based on Entity Code
	 */
	@GetMapping(value = "/getActiveCountryWithBranch")
	public ServiceResponse getActiveCountryWithBranch(@RequestBody XbrlWebFormDto xbrlWebFormDto) {
		logger.info("Fetch Active Country Branch List data controller started ");
		ServiceResponse serviceResponse = null;
		Option option = null;
		Options options = null;
		OptionBean optionBean = null;
		List<Option> optionList = null;
		List<BranchMasterOverseas> listOfActiveCountryBranchMasterData = null;
		List<OptionBean> detailsList = null;
		List<String> countryCodeCheck = null;
		try {
			listOfActiveCountryBranchMasterData = new ArrayList<>();
			optionList = new ArrayList<>();
			options = new Options();
			countryCodeCheck = new ArrayList<>();

			listOfActiveCountryBranchMasterData = branchMasterOverseasService.getAllDataForBranchOverseas(BranchMasterOverseas.class, xbrlWebFormDto.getBankCode());

			if (CollectionUtils.isEmpty(listOfActiveCountryBranchMasterData)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString())).build();
			}
			if (!CollectionUtils.isEmpty(listOfActiveCountryBranchMasterData)) {
				for (BranchMasterOverseas branchMasterOverseas : listOfActiveCountryBranchMasterData) {
					detailsList = new ArrayList<>();
					option = new Option();

					if (!countryCodeCheck.contains(branchMasterOverseas.getCountryIdFk().getCountryCode())) {
						countryCodeCheck.add(branchMasterOverseas.getCountryIdFk().getCountryCode());
						option.setKey(branchMasterOverseas.getCountryIdFk().getCountryCode());
						option.setValue(branchMasterOverseas.getCountryIdFk().getCountryName());
						for (BranchMasterOverseas branchMasterOverseas1 : listOfActiveCountryBranchMasterData) {
							optionBean = new OptionBean();
							if (branchMasterOverseas.getCountryIdFk().getCountryCode().equals(branchMasterOverseas1.getCountryIdFk().getCountryCode())) {
								optionBean.setKey(branchMasterOverseas1.getBranchCode());
								optionBean.setValue(branchMasterOverseas1.getBranch());
								detailsList.add(optionBean);
							}
						}
						option.setDetailsList(detailsList);
						optionList.add(option);
					}
				}
			}
			options.setOptionList(optionList);
			serviceResponse = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			serviceResponse.setResponse(options);
			return serviceResponse;
		} catch (Exception e) {
			logger.error("fetchActiveCountryBranchList ", e);
			return new ServiceResponseBuilder().setStatus(false).setResponse(null).build();
		}
	}
}
