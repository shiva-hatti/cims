/**
 * 
 */
package com.iris.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iris.caching.ObjectCache;
import com.iris.dto.Option;
import com.iris.dto.Options;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.dto.XbrlWebFormDto;
import com.iris.model.RosMaster;
import com.iris.service.impl.RosMasterService;
import com.iris.util.Validations;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.GeneralConstants;

/**
 * @author Shivabasava Hatti
 *
 */
@RestController
@RequestMapping("/service/rosMaster")
public class RosMasterController {
	private static final Logger logger = LogManager.getLogger(RosMasterController.class);

	@Autowired
	private RosMasterService rosMasterService;

	@PostMapping("/getRosMasterDetailsWebform")
	public ServiceResponse getRosMasterDetailsWebform(@RequestBody XbrlWebFormDto xbrlWebFormDto) {
		logger.info("Call getRosMasterDetailsWebform service with RequestBody(XbrlWebFormDto)");
		ServiceResponse response = null;
		try {
			List<RosMaster> rosMasterList = new ArrayList<>();
			List<Option> optionList = new ArrayList<>();
			Option option = null;
			Options options = new Options();
			Map<String, String> detailsMap = new LinkedHashMap<>();
			if (Validations.isEmpty(xbrlWebFormDto.getReturnCode()) || Validations.isEmpty(xbrlWebFormDto.getBankCode())) {
				logger.error("returnCodem or bankCode is empty");
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0889.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString())).build();
			}
			rosMasterList = rosMasterService.findRosMasterDetailsByEntityCode(xbrlWebFormDto.getReturnCode(), xbrlWebFormDto.getBankCode());
			if (CollectionUtils.isEmpty(rosMasterList)) {
				logger.info("rosMasterList empty)");
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString())).build();
			}
			for (RosMaster rosMaster : rosMasterList) {
				option = new Option();
				detailsMap = new LinkedHashMap<>();
				option.setKey(rosMaster.getInstituteName());
				option.setValue(rosMaster.getInstituteName());
				detailsMap.put(GeneralConstants.JOINT_VENTURE_CATEGORY.getConstantVal(), rosMaster.getInstituteCategory());
				detailsMap.put(GeneralConstants.AREA_OF_OPERATION.getConstantVal(), rosMaster.getInstituteAreaOperation());
				detailsMap.put(GeneralConstants.JOINT_VENTURE_CODE.getConstantVal(), rosMaster.getInstituteCode());
				detailsMap.put(GeneralConstants.JOINT_VENTURE_NAME.getConstantVal(), rosMaster.getInstituteName());
				detailsMap.put(GeneralConstants.ACTIVITY_NAME.getConstantVal(), rosMaster.getActivityName());
				detailsMap.put(GeneralConstants.REGULATOR_NAME.getConstantVal(), rosMaster.getInstituteRegulatorName());
				option.setDetailsMap(detailsMap);
				optionList.add(option);
			}
			options.setOptionList(optionList);
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(options);
			return response;
		} catch (Exception e) {
			logger.error("Exception occoured while featch getRosMasterDetailsWebform Details list" + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
	}

	@PostMapping("/getCPRMasterDetails")
	public ServiceResponse getCPRMasterDetails(@RequestBody XbrlWebFormDto xbrlWebFormDto) {
		logger.info("Call getCPRMasterDetails service with xbrlWebFormDto");
		ServiceResponse response = null;
		try {
			List<RosMaster> cprMasterList = new ArrayList<>();
			List<Option> optionList = new ArrayList<>();
			Option option = null;
			Options options = new Options();
			Map<String, String> detailsMap = new LinkedHashMap<>();
			if (Validations.isEmpty(xbrlWebFormDto.getReturnCode())) {
				logger.error("returnCodem is empty");
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0889.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString())).build();
			}
			cprMasterList = rosMasterService.findRosMasterDetailsByEntityCode(xbrlWebFormDto.getReturnCode(), xbrlWebFormDto.getBankCode());
			if (CollectionUtils.isEmpty(cprMasterList)) {
				logger.info("cprMasterList empty)");
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0660.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString())).build();
			}
			for (RosMaster rosMaster : cprMasterList) {
				option = new Option();
				detailsMap = new LinkedHashMap<>();
				option.setKey(rosMaster.getInstituteName());
				option.setValue(rosMaster.getInstituteName());
				detailsMap.put(GeneralConstants.ACTIVITY_NAME.getConstantVal(), rosMaster.getActivityName());
				detailsMap.put(GeneralConstants.REGULATOR_NAME.getConstantVal(), rosMaster.getInstituteRegulatorName());
				option.setDetailsMap(detailsMap);
				optionList.add(option);
			}
			options.setOptionList(optionList);
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(options);
			return response;
		} catch (Exception e) {
			logger.error("Exception occoured while featch getCPRMasterDetails Details list" + e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
	}
}
