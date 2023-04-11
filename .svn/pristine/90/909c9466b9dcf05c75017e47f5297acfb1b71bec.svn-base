package com.iris.nbfc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.iris.caching.ObjectCache;
import com.iris.dto.PanSupportingInfo;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.exception.ServiceException;
import com.iris.model.NSDLPanVerif;
import com.iris.model.PanMaster;
import com.iris.model.UserMaster;
import com.iris.nbfc.model.NbfcCorRegistrationBean;
import com.iris.nbfc.model.NbfcCorRegistrationDto;
import com.iris.nbfc.model.NbfcDisplayMessages;
import com.iris.nbfc.service.NbfcCorRegistrationService;
import com.iris.nbfc.service.NsdlPanVerficationService;
import com.iris.repository.PanMasterRepo;
import com.iris.service.GenericService;
import com.iris.util.AESV2;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.CorRegistrationStatusEnum;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.MethodConstants;
import com.iris.util.constant.NsdlPanVerfStatusEnum;

/**
 * @author pmohite
 */
@RestController
@RequestMapping("/service/nbfcMngtController")
public class NbfcMngtController {

	private static final Logger Logger = LogManager.getLogger(NbfcMngtController.class);

	@Autowired
	private GenericService<UserMaster, Long> userMasterService;

	@Autowired
	private GenericService<NbfcDisplayMessages, Long> nbfcMessagesService;

	@Autowired
	private NbfcCorRegistrationService nbfcCorRegistrationService;

	@Autowired
	private NsdlPanVerficationService nsdlPanVerficationService;

	@Autowired
	private PanMasterRepo panMasterRepo;

	private NbfcCorRegistrationBean nbfcCorRegistration;
	private NSDLPanVerif nsdlPanVerif;

	@GetMapping(value = "/getNbfcBorrower/{panNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse getNbfcBorrower(@PathVariable(value = "panNumber") String panNumber, @RequestHeader(name = "JobProcessingId") String jobProcessId) {
		System.out.println(panNumber);
		Logger.info("Pan number : " + panNumber);
		//		ServiceResponse response;
		List<NbfcCorRegistrationBean> nbfcCorRegistrationList = new ArrayList<>();
		try {
			if (panNumber != null) {

				// check if pan is rbi generated or not

				PanMaster panMaster = panMasterRepo.checkIfRbiGeneratedPan(panNumber);

				if (panMaster != null) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1300.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1300.toString())).build();
				}

				Map<String, List<String>> columnValueMap = new HashMap<>();
				List<String> panList = new ArrayList<>();
				panList.add(panNumber);
				columnValueMap.put(ColumnConstants.PAN_NUMBER.getConstantVal(), panList);

				nbfcCorRegistrationList = nbfcCorRegistrationService.getDataByColumnValue(columnValueMap, MethodConstants.GET_BORROWER_NAME_BY_PAN_NUMBER.getConstantVal());
				if (!CollectionUtils.isEmpty(nbfcCorRegistrationList)) {

					Logger.error("PAN Details already present job processingid " + jobProcessId);

					NbfcCorRegistrationBean bean = nbfcCorRegistrationList.get(0);

					if (StringUtils.isNotBlank(bean.getStatus())) {

						if (bean.getStatus().equals(CorRegistrationStatusEnum.NSDL_REJECTED.getStatus()) || bean.getStatus().equals(CorRegistrationStatusEnum.USER_REJECTED.getStatus())) {
							return new ServiceResponseBuilder().setStatus(true).build();
						} else if (bean.getStatus().equals(CorRegistrationStatusEnum.NSDL_PENDING.getStatus())) {
							return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1282.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1282.toString())).build();
						} else if (bean.getStatus().equals(CorRegistrationStatusEnum.NSDL_APPROVED.getStatus())) {
							return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1283.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1283.toString())).build();
						} else if (bean.getStatus().equals(CorRegistrationStatusEnum.USER_VERIFIED.getStatus())) {
							return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1284.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1284.toString())).build();
						}
						return new ServiceResponseBuilder().setStatus(true).build();
					}

				} else {
					return new ServiceResponseBuilder().setStatus(true).build();
				}
			}
		} catch (Exception e) {
			Logger.error("Exception while fetching PAN Details info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
	}

	@GetMapping(value = "/getNbfcBorrowerDetails/{panNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse getBorrowerPANDetails(@PathVariable(value = "panNumber") String panNumber, @RequestHeader(name = "JobProcessingId") String jobProcessId) {

		List<NbfcCorRegistrationBean> nbfcCorRegistrationBeanList = new ArrayList<>();
		ServiceResponse response = null;
		NbfcCorRegistrationBean nbfcCorRegistration;
		try {
			if (panNumber != null) {
				Map<String, List<String>> columnValueMap = new HashMap<>();
				List<String> panList = new ArrayList<>();
				panList.add(panNumber);

				columnValueMap.put(ColumnConstants.PAN_NUMBER.getConstantVal(), panList);

				nbfcCorRegistrationBeanList = nbfcCorRegistrationService.getDataByColumnValue(columnValueMap, MethodConstants.GET_BORROWER_NAME_BY_PAN_NUMBER.getConstantVal());
				if (!CollectionUtils.isEmpty(nbfcCorRegistrationBeanList) && nbfcCorRegistrationBeanList.get(0) != null) {
					nbfcCorRegistration = nbfcCorRegistrationBeanList.get(0);
					if (nbfcCorRegistrationBeanList != null && nbfcCorRegistration.getUser() != null) {
						UserMaster user = new UserMaster();
						user.setUserId(nbfcCorRegistration.getUser().getUserId());
						nbfcCorRegistration.setUser(user);
					}
					response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
					response.setResponse(nbfcCorRegistration);
				} else {
					// nbfc details not found
					response = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0835.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0835.toString())).build();
				}
			} else {
				response = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0835.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0835.toString())).build();
			}
			Logger.info("return list prepared for job processingid" + jobProcessId);
		} catch (ServiceException e) {
			Logger.error("Exception while fetching return list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0835.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0835.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching return list info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		Logger.info("request completed to get return list for job processingid" + jobProcessId);
		return response;
	}

	/**
	 * save the NbfcEntityBean. This method is to save ReturnGroupMapping .
	 *
	 * @param jobProcessId
	 * @param returnGroupMappingDto
	 * @return
	 */

	@PostMapping(value = "/saveNbfcEntityBean")
	public ServiceResponse saveNbfcEntityBean(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody NbfcCorRegistrationDto nbfcCorRegistrationDto) {
		nbfcCorRegistration = new NbfcCorRegistrationBean();
		NbfcCorRegistrationBean oldNbfcCorBean = null;
		try {

			Logger.info("Request received to save nbfc entity bean Job processing id : " + jobProcessId);
			if (nbfcCorRegistrationDto != null) {
				String validationResult = validateInputBean(nbfcCorRegistrationDto);

				if (StringUtils.isNotBlank(validationResult)) {
					Logger.error("Exception while saving NbfcEntityBean for job processingid " + jobProcessId + "validation of bean has been failed : " + validationResult);
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(validationResult).setStatusMessage(ObjectCache.getErrorCodeKey(validationResult)).build();
				}
				Logger.info("Request received to save nbfc entity bean Job processing id : " + jobProcessId + "And Pan number : " + nbfcCorRegistrationDto.getPanNumber());
				Map<String, List<String>> columnValueMap = new HashMap<>();
				List<String> panList = new ArrayList<>();
				panList.add(nbfcCorRegistrationDto.getPanNumber());
				columnValueMap.put(ColumnConstants.PAN_NUMBER.getConstantVal(), panList);

				List<NbfcCorRegistrationBean> nbfcCorRegistrationList = nbfcCorRegistrationService.getDataByColumnValue(columnValueMap, MethodConstants.GET_BORROWER_NAME_BY_PAN_NUMBER_AND_REJECTED_STATUS.getConstantVal());
				if (!CollectionUtils.isEmpty(nbfcCorRegistrationList)) {
					oldNbfcCorBean = nbfcCorRegistrationList.get(0);
				}
				if (oldNbfcCorBean != null && oldNbfcCorBean.getIsActive() && StringUtils.isNotBlank(oldNbfcCorBean.getStatus())) {

					if (oldNbfcCorBean.getStatus().equals(CorRegistrationStatusEnum.NSDL_PENDING.getStatus())) {
						Logger.error("Exception while saving NbfcEntityBean for job processingid " + jobProcessId + " Pan is pending for NSDL pending");
						return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1282.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1282.toString())).build();
					} else if (oldNbfcCorBean.getStatus().equals(CorRegistrationStatusEnum.NSDL_APPROVED.getStatus())) {
						Logger.error("Exception while saving NbfcEntityBean for job processingid " + jobProcessId + " Pan is pending for NSDL Approved");
						return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1283.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1283.toString())).build();
					} else if (oldNbfcCorBean.getStatus().equals(CorRegistrationStatusEnum.USER_VERIFIED.getStatus())) {
						Logger.error("Exception while saving NbfcEntityBean for job processingid " + jobProcessId + " Pan is pending for user verified");
						return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1284.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1284.toString())).build();
					}

				} else {

					PanMaster pm = panMasterRepo.getPanInfoByPanNumAndBorrowerName(nbfcCorRegistrationDto.getPanNumber(), nbfcCorRegistrationDto.getBorrowerName().toUpperCase());

					nbfcCorRegistration.setCompPan(nbfcCorRegistrationDto.getPanNumber());
					nbfcCorRegistration.setCompName(nbfcCorRegistrationDto.getBorrowerName());
					nbfcCorRegistration.setCompEmailId(nbfcCorRegistrationDto.getBorrowerEmail());
					nbfcCorRegistration.setCompPhone(nbfcCorRegistrationDto.getBorrowerPhone());
					nbfcCorRegistration.setIsActive(true);
					nbfcCorRegistration.setCreatedOn(new Date());
					if (pm != null) {
						nbfcCorRegistration.setStatus(CorRegistrationStatusEnum.NSDL_APPROVED.getStatus());
					} else {
						nbfcCorRegistration.setStatus(CorRegistrationStatusEnum.NSDL_PENDING.getStatus());
					}

					nbfcCorRegistration = nbfcCorRegistrationService.add(nbfcCorRegistration);

					if (nbfcCorRegistration != null && nbfcCorRegistration.getCorRegistrationId() != null) {
						nsdlPanVerif = new NSDLPanVerif();

						PanSupportingInfo panSupportingInfo = new PanSupportingInfo();
						nsdlPanVerif.setActualPanNumber(nbfcCorRegistrationDto.getPanNumber());
						panSupportingInfo.setRefId(String.valueOf(nbfcCorRegistration.getCorRegistrationId()));
						nsdlPanVerif.setSupportingInfo(new Gson().toJson(panSupportingInfo));
						nsdlPanVerif.setModuleName("COR");
						if (pm != null) {
							nsdlPanVerif.setStatus(NsdlPanVerfStatusEnum.NSDL_PASS.getStaus());
							nsdlPanVerif.setNsdlVerifiedOn(new Date());
						} else {
							nsdlPanVerif.setStatus(NsdlPanVerfStatusEnum.PEND_NSDL_VERIFY.getStaus());
						}
						nsdlPanVerif.setIsActive(true);
						nsdlPanVerif.setCreatedOn(new Date());
						nsdlPanVerif.setSubTaskStatus(false);

						nsdlPanVerficationService.add(nsdlPanVerif);
					}

				}
			}
		} catch (ServiceException e) {
			Logger.error("Request object not proper to save ");
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0771.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0771.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while saving NbfcEntityBean info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		ServiceResponse response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
		Logger.info("request completed to save NbfcEntityBean for job processingid" + jobProcessId);
		return response;
	}

	private String validateInputBean(NbfcCorRegistrationDto nbfcCorRegistrationDto) {

		//valiate pan number start
		Logger.info("COR registration Bean validation start");
		Logger.info("COR registration Bean validation start, validating Pan number : " + nbfcCorRegistrationDto.getPanNumber());

		if (StringUtils.isBlank(nbfcCorRegistrationDto.getPanNumber())) {
			Logger.info("NULL check for Pan number");
			return ErrorCode.E0871.toString();
		}
		Logger.info("NULL check Pan validation pass");

		// pan length check
		if (nbfcCorRegistrationDto.getPanNumber().length() != 10) {
			Logger.info("Length check Pan number");
			return ErrorCode.E0872.toString();
		}
		Logger.info("Length check Pan number validation pass");

		// pan regex check
		if (StringUtils.isNotBlank(nbfcCorRegistrationDto.getPanNumber())) {
			Logger.info("Pan format check (Regex)");
			String pattern = "([A-Z]){5}([0-9]){4}([A-Z]){1}$";
			if (!validateRegex(pattern, nbfcCorRegistrationDto.getPanNumber())) {
				return ErrorCode.E0764.toString();
			}
			Logger.info("Regex check Pan number validation pass");
		}

		//valiate pan number end

		// company name validation start
		if (StringUtils.isBlank(nbfcCorRegistrationDto.getBorrowerName())) {
			Logger.info("NULL check for Company name");
			return ErrorCode.E1287.toString();
		}
		Logger.info("NULL check company name validation pass");

		if (nbfcCorRegistrationDto.getBorrowerName().length() > 100) {
			Logger.info("Length check for company name");
			return ErrorCode.E1286.toString();
		}
		Logger.info("Length check company validation pass");
		// company name validation End

		//Company emaild id validation start
		// email null check
		if (StringUtils.isBlank(nbfcCorRegistrationDto.getBorrowerEmail())) {
			Logger.info("NULL check for Company email");
			return ErrorCode.E1033.toString();
		}
		Logger.info("NULL check company email validation pass");

		if (nbfcCorRegistrationDto.getBorrowerEmail().length() > 100) {
			Logger.info("Length check for company email");
			return ErrorCode.E1288.toString();
		}
		Logger.info("Length check company email validation pass");

		// Company email regex check
		if (StringUtils.isNotBlank(nbfcCorRegistrationDto.getBorrowerEmail())) {

			Logger.info("Company email format check (Regex)");
			String pattern = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
			if (!validateRegex(pattern, nbfcCorRegistrationDto.getBorrowerEmail())) {
				return ErrorCode.E1289.toString();
			}
			Logger.info("Regex check Email validation pass");
		}

		// company Email validation End

		//Company phone validation start
		// phone null check
		if (StringUtils.isBlank(nbfcCorRegistrationDto.getBorrowerPhone())) {
			Logger.info("NULL check for Company phone");
			return ErrorCode.E0879.toString();
		}
		Logger.info("NULL check company Phone validation pass");

		if (nbfcCorRegistrationDto.getBorrowerPhone().length() > 100) {
			Logger.info("Length check for company phone number");
			return ErrorCode.E1290.toString();
		}
		Logger.info("Length check company phone number validation pass");

		// Company phone number regex check
		if (StringUtils.isNotBlank(nbfcCorRegistrationDto.getBorrowerPhone())) {
			Logger.info("Company email format check (Regex)");
			String pattern = "[0-9]+";
			if (!validateRegex(pattern, nbfcCorRegistrationDto.getBorrowerPhone())) {
				return ErrorCode.E1291.toString();
			}
			Logger.info("Regex check company phone number validation pass");
		}

		// Company phone number validation End

		return null;
	}

	private boolean validateRegex(String pattern, String content) {

		Pattern patternObj = Pattern.compile(pattern);
		Matcher matcher = patternObj.matcher(content);
		return matcher.matches();
	}

	/**
	 * save the NbfcEntityBean. This method is to save ReturnGroupMapping .
	 *
	 * @param jobProcessId
	 * @param nbfcRegistrationBean
	 * @return
	 */
	@PostMapping(value = "/updateNbfcEntityBean")
	public ServiceResponse updateNbfcEntityBean(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody NbfcCorRegistrationBean nbfcCorRegistrationBean) {
		Boolean flag;
		try {
			Logger.info("request received to update NbfcEntityBean for job processingid" + jobProcessId);
			Map<String, List<String>> columnValueMap = new HashMap<>();
			List<String> panList = new ArrayList<>();
			panList.add(nbfcCorRegistrationBean.getCompPan());

			columnValueMap.put(ColumnConstants.PAN_NUMBER.getConstantVal(), panList);

			nbfcCorRegistration = nbfcCorRegistrationService.getDataByColumnValue(columnValueMap, MethodConstants.GET_BORROWER_NAME_BY_PAN_NUMBER.getConstantVal()).get(0);

			Map<String, List<String>> valueMap = new HashMap<>();
			List<String> userNameList = new ArrayList<>();
			AESV2 aesv2 = AESV2.getInstance();
			userNameList.add(aesv2.encrypt(nbfcCorRegistrationBean.getCompPan()));
			valueMap.put(ColumnConstants.USER_NAME.getConstantVal(), userNameList);
			List<UserMaster> userMasterList = userMasterService.getDataByColumnValue(valueMap, MethodConstants.GET_USER_DATA_BY_USER_NAME.getConstantVal());
			if (CollectionUtils.isEmpty(userMasterList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			}
			nbfcCorRegistration.setUser(userMasterList.get(0));
			//			nbfcCorRegistration.setIsVerified(true);  discussion required
			nbfcCorRegistration.setRegisteredOn(new Date());
			flag = nbfcCorRegistrationService.update(nbfcCorRegistration);
		} catch (ServiceException e) {
			Logger.error("Request object not proper to update ");
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0869.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0869.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while updating NbfcEntityBean info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		ServiceResponse response = null;
		if (Boolean.TRUE.equals(flag)) {
			if (nbfcCorRegistration.getUser() != null) {
				UserMaster user = new UserMaster();
				user.setUserId(nbfcCorRegistration.getUser().getUserId());
				nbfcCorRegistration.setUser(user);
			}
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(nbfcCorRegistration);
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(false).build();
		}
		Logger.info("request completed to update NbfcEntityBean for job processingid" + jobProcessId);
		return response;
	}

	@GetMapping(value = "/getNBFCMessages/{companyType}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse getNBFCMessages(@PathVariable(value = "companyType") Integer companyType, @RequestHeader(name = "JobProcessingId") String jobProcessId) {
		ServiceResponse response;
		NbfcDisplayMessages nbfcDisplayMessages = null;
		try {
			Map<String, Object> columnValueMap = new HashMap<>();
			columnValueMap.put(ColumnConstants.COMPANY_TYPE.getConstantVal(), companyType);
			nbfcDisplayMessages = nbfcMessagesService.getDataByObject(columnValueMap, MethodConstants.GET_NBFC_MESSAGES_BY_COMPANY_TYPE.getConstantVal()).get(0);
		} catch (ServiceException e) {
			Logger.error("Exception while fetching NBFCMessages info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0836.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0836.toString())).build();
		} catch (Exception e) {
			Logger.error("Exception while fetching NBFCMessages info for job processingid " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
		if (Objects.isNull(nbfcDisplayMessages)) {
			Logger.error("Exception while fetching NBFCMessages info for job processingid " + jobProcessId);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0836.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0836.toString())).build();
		} else {
			response = new ServiceResponse.ServiceResponseBuilder().setStatus(true).build();
			response.setResponse(nbfcDisplayMessages);
			return response;
		}
	}
}
