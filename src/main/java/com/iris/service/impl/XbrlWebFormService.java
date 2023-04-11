package com.iris.service.impl;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iris.caching.ObjectCache;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.dto.XbrlWebFormDto;
import com.iris.exception.ServiceException;
import com.iris.model.ApiLogDetails;
import com.iris.model.Currency;
import com.iris.model.EntityBean;
import com.iris.model.FileDetails;
import com.iris.model.Return;
import com.iris.model.UploadChannel;
import com.iris.model.UserMaster;
import com.iris.model.WebServiceComponentUrl;
import com.iris.model.XBRLWebFormPartialData;
import com.iris.model.XbrlWebFormSessionChk;
import com.iris.repository.CurrencyRepository;
import com.iris.repository.FilingStatusRepo;
import com.iris.repository.FrequencyRepository;
import com.iris.repository.ReturnRepo;
import com.iris.repository.ReturnTemplateRepository;
import com.iris.repository.XbrlWebFormRepo;
import com.iris.repository.XbrlWebFormSessionChkRepo;
import com.iris.service.GenericService;
import com.iris.util.AESV2;
import com.iris.util.JsonUtility;
import com.iris.util.LogApiDetailsUtil;
import com.iris.util.ResourceUtil;
import com.iris.util.Validations;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ComponentConstant;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;
import com.iris.webservices.client.CIMSRestWebserviceClient;
import com.iris.webservices.client.WebServiceResponseReader;

@Service
public class XbrlWebFormService implements GenericService<XBRLWebFormPartialData, Long> {
	static final Logger logger = LogManager.getLogger(XbrlWebFormService.class);
	@Autowired
	XbrlWebFormRepo xbrlWebFormRepo;
	@Autowired
	private UserMasterService userMasterService;
	@Autowired
	private WebServiceComponentService webServiceComponentService;
	@Autowired
	private ReturnService returnService;
	@Autowired
	private EntityService entityService;
	@Autowired
	private FilingStatusRepo filingStatusRepo;
	@Autowired
	private ReturnTemplateRepository taxonomyRepository;
	@Autowired
	private FrequencyRepository frequencyRepository;
	@Autowired
	private CurrencyRepository currencyRepository;
	@Autowired
	WebServiceResponseReader webServiceResponseReader;
	@Autowired
	ReturnUploadDetailsService returnsUploadDetailsService;
	@Autowired
	ReturnRepo returnRepo;

	@Autowired
	XbrlWebFormSessionChkRepo xbrlWebFormSessionChkRepo;

	String DATE_FORMAT = "dd/MM/yyyy";
	String FILING_DATE_FORMAT = "ddMMyyyy";

	@Override
	public XBRLWebFormPartialData add(XBRLWebFormPartialData entity) throws ServiceException {
		return xbrlWebFormRepo.save(entity);
	}

	@Override
	public boolean update(XBRLWebFormPartialData entity) throws ServiceException {
		if (xbrlWebFormRepo.save(entity) != null) {
			return true;
		}
		return false;
	}

	@Override
	public List<XBRLWebFormPartialData> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public XBRLWebFormPartialData getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<XBRLWebFormPartialData> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<XBRLWebFormPartialData> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<XBRLWebFormPartialData> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<XBRLWebFormPartialData> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<XBRLWebFormPartialData> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(XBRLWebFormPartialData bean) throws ServiceException {

	}

	public boolean savePartialDataBean(XbrlWebFormDto xbrlWebFormDto) {
		boolean flag = false;

		String DRAFT_STATUS = "field.xbrl.draft";
		try {
			Map<String, List<String>> valueMap = new HashMap<>();
			List<String> paramList = new ArrayList<>();
			boolean isNewFiling = false;
			XBRLWebFormPartialData xbrlWebFormData = xbrlWebFormRepo.findByGuid(xbrlWebFormDto.getGuid());
			if (xbrlWebFormData == null) {
				xbrlWebFormData = new XBRLWebFormPartialData();
				isNewFiling = true;
				xbrlWebFormData.setGuid(xbrlWebFormDto.getGuid());
				paramList.add(xbrlWebFormDto.getReturnCode());
				valueMap.put(ColumnConstants.RETURN_CODE.getConstantVal(), paramList);
				List<Return> returnMasterList = new ArrayList<>();
				returnMasterList = returnService.getDataByColumnValue(valueMap, MethodConstants.GET_RETURN_DATA_BY_CODE.getConstantVal());
				if (CollectionUtils.isEmpty(returnMasterList)) {
					return false;
				}

				Optional<Return> optionalReturn = returnMasterList.stream().findFirst();

				if (optionalReturn.isPresent()) {
					xbrlWebFormData.setReturnObj(optionalReturn.get());
				}

				paramList = new ArrayList<>();
				valueMap = new HashMap<>();
				paramList.add(xbrlWebFormDto.getBankCode());
				valueMap.put(ColumnConstants.ENTITY_CODE.getConstantVal(), paramList);
				List<EntityBean> entityMasterList = entityService.getDataByColumnValue(valueMap, MethodConstants.GET_ENTITY_DATA_BY_CODE.getConstantVal());
				if (CollectionUtils.isEmpty(entityMasterList)) {
					return false;
				}

				Optional<EntityBean> optionalEntity = entityMasterList.stream().findFirst();

				if (optionalEntity.isPresent()) {
					xbrlWebFormData.setEntity(optionalEntity.get());
				}
				xbrlWebFormData.setFilingStatus(filingStatusRepo.getDataByStatus(xbrlWebFormDto.getFilingStatus()));
				xbrlWebFormData.setStartDate(DateManip.convertStringToDate(xbrlWebFormDto.getStartDate(), xbrlWebFormDto.getSessionDateFormat()));
				xbrlWebFormData.setEndDate(DateManip.convertStringToDate(xbrlWebFormDto.getEndDate(), xbrlWebFormDto.getSessionDateFormat()));
				xbrlWebFormData.setIsActive(true);
				xbrlWebFormData.setTaxonomyId(taxonomyRepository.findByReturnIdAndVersionNumber(xbrlWebFormData.getReturnObj().getReturnId(), xbrlWebFormDto.getTaxonomyVersion()));
				xbrlWebFormData.setFrequency(frequencyRepository.findByFrequencyId(Long.parseLong(xbrlWebFormDto.getFrequencyCode())));
				xbrlWebFormData.setLevelOfRounding(xbrlWebFormDto.getLevelOfRounding());
				xbrlWebFormData.setLevelOfRoundingCode(xbrlWebFormDto.getLevelOfRoundingCode());

				xbrlWebFormData.setReturnProperty(xbrlWebFormDto.getReturnProperty());

				Optional<Currency> optionalCurrency = currencyRepository.findByCurrencyISOCode(xbrlWebFormDto.getCurrencyCode()).stream().findFirst();

				if (optionalCurrency.isPresent()) {
					xbrlWebFormData.setCurrency(optionalCurrency.get());
				}
				xbrlWebFormData.setRoleId(Long.parseLong(xbrlWebFormDto.getRoleId()));
			}
			paramList = new ArrayList<>();
			valueMap = new HashMap<>();
			paramList.add(xbrlWebFormDto.getUserName());
			valueMap.put(ColumnConstants.USER_NAME.getConstantVal(), paramList);
			List<UserMaster> userMasterList = userMasterService.getDataByColumnValue(valueMap, MethodConstants.GET_USER_DATA_BY_USER_NAME.getConstantVal());
			if (CollectionUtils.isEmpty(userMasterList)) {
				return false;
			}
			System.out.println(xbrlWebFormDto.getFilingStatus() + "-----------------");
			if (isNewFiling) {
				xbrlWebFormData.setUploadedDate(DateManip.getCurrentDateTime());

				Optional<UserMaster> optionalUserMaster = userMasterList.stream().findFirst();

				if (optionalUserMaster.isPresent()) {
					xbrlWebFormData.setUploadedBy(optionalUserMaster.get());
				}
			} else {
				xbrlWebFormData.setModificationDate(DateManip.getCurrentDateTime());

				Optional<UserMaster> optionalUserMaster = userMasterList.stream().findFirst();

				if (optionalUserMaster.isPresent()) {
					xbrlWebFormData.setModifiedBy(optionalUserMaster.get());
				}
			}

			if (isNewFiling) {
				add(xbrlWebFormData);
			} else {
				update(xbrlWebFormData);
			}
			flag = true;
		} catch (Exception e) {
			logger.error("Exception while getting file info: ", e);
			flag = false;
		}
		return flag;
	}

	public boolean verifyJWTToken(XbrlWebFormDto xbrlWebFormDto) {
		logger.info("inside XbrlWebFormController insert file Details method" + xbrlWebFormDto.getJwtToken());
		boolean flag = true;
		return flag;
	}

	public ServiceResponse checkUserSession(XbrlWebFormDto xbrlWebFormDto) {
		ServiceResponse serviceResponse;
		if (isUserSessionLogout(xbrlWebFormDto.getJwtToken(), xbrlWebFormDto.getUserName())) {
			serviceResponse = new ServiceResponseBuilder().setStatus(true).setStatusMessage(GeneralConstants.JWT_TOKEN_VALID.getConstantVal()).build();
		} else {
			serviceResponse = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1416.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1416.toString())).build();
		}
		return serviceResponse;
		//		return new ServiceResponseBuilder().setStatus(true).setStatusMessage(GeneralConstants.JWT_TOKEN_VALID.getConstantVal()).build();
	}

	private boolean isUserSessionLogout(String sessionToken, String userName) {
		logger.info("This isUserSessionLogout method here call TCS API KEEP_VALID_SESSION session Token" + sessionToken, " UserName: " + userName);
		try {
			CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();
			WebServiceComponentUrl componentUrl = getWebServiceComponentURL(ComponentConstant.KEEP_VALID_SESSION.getComponentConstant(), CIMSRestWebserviceClient.HTTP_METHOD_TYPE_POST);

			XbrlWebFormDto userObjBean = new XbrlWebFormDto();
			userObjBean.setJwtToken(sessionToken);

			Map<String, String> headerMap = new HashMap<>();
			String jobProcessingId = UUID.randomUUID().toString();
			headerMap.put(GeneralConstants.UUID.getConstantVal(), jobProcessingId);

			String responsestring = restServiceClient.callRestWebServiceWithMultipleHeader(componentUrl, userObjBean, null, headerMap);

			Type listToken = new TypeToken<ServiceResponse>() {
			}.getType();
			ServiceResponse serviceResponse = JsonUtility.getGsonObject().fromJson(responsestring, listToken);

			if (serviceResponse == null || !serviceResponse.isStatus()) {
				logger.info("Session Invalid received from TCS API KEEP_VALID_SESSION session Token" + sessionToken, " UserName: " + userName);
				return false;
			}
		} catch (Exception e) {
			logger.error("EXCEPTION occured while calling KEEP_VALID_SESSION API : ", e);
			return false;
		}
		return true;
	}

	private WebServiceComponentUrl getWebServiceComponentURL(String componentName, String methodType) {
		Map<String, List<String>> valueMap = new HashMap<>();

		List<String> valueList = new ArrayList<>();
		valueList.add(componentName);
		valueMap.put(ColumnConstants.COMPONENTTYPE.getConstantVal(), valueList);

		valueList = new ArrayList<>();
		valueList.add(methodType);
		valueMap.put(ColumnConstants.METHODTYPE.getConstantVal(), valueList);

		WebServiceComponentUrl componentUrl = null;
		try {
			componentUrl = webServiceComponentService.getDataByColumnValue(valueMap, MethodConstants.GET_ACTIVE_DATA_BY_COMPONENTTYPE_METHODTYPE.getConstantVal()).get(0);
		} catch (ServiceException e) {
			logger.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}
		return componentUrl;
	}

	public ServiceResponse saveDataToPartialTable(XbrlWebFormDto xbrlWebFormDto) {
		Map<String, List<String>> responseObject = null;
		List<String> errorResponseList = null;
		logger.info("inside XbrlWebFormController insert file Details method" + xbrlWebFormDto.getJwtToken());
		ApiLogDetails apiLogDetails = new ApiLogDetails();
		Long userId = null;
		WebServiceComponentUrl webServiceComponentUrl = null;
		ServiceResponse serviceResponse = null;
		Properties prop = ResourceUtil.getResourcePropertyFile();
		apiLogDetails.setRequestReceivedTimeInLong(DateManip.getCurrentDateTime().getTime());
		Map<String, List<String>> valueMap = new HashMap<>();
		List<String> userNameList = new ArrayList<>();
		userNameList.add(xbrlWebFormDto.getUserName());
		valueMap.put(ColumnConstants.USER_NAME.getConstantVal(), userNameList);
		List<UserMaster> userMasterList = userMasterService.getDataByColumnValue(valueMap, MethodConstants.GET_USER_DATA_BY_USER_NAME.getConstantVal());
		if (CollectionUtils.isEmpty(userMasterList)) {
			return new ServiceResponseBuilder().setStatus(false).setStatusMessage("Username doesnot exist").build();
		}
		for (UserMaster userMaster : userMasterList) {
			userId = userMaster.getUserId();
		}
		UserMaster uM = new UserMaster();
		uM.setUserId(userId);
		if (!StringUtils.isBlank(xbrlWebFormDto.getJwtToken())) {
			apiLogDetails.setJwtToken(xbrlWebFormDto.getJwtToken());
		}
		apiLogDetails.setUserIdFk(uM);
		if (!verifyJWTToken(xbrlWebFormDto)) {
			responseObject = new HashMap<>();
			errorResponseList = new ArrayList<>();
			errorResponseList.add(ObjectCache.getErrorCodeKey(ErrorCode.E0888.toString()));
			responseObject.put("en", errorResponseList);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0888.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0888.toString())).setResponse(responseObject).build();
		}

		if (xbrlWebFormDto.isXbrl()) {
			if (StringUtils.isBlank(xbrlWebFormDto.getGuid()) || StringUtils.isBlank(xbrlWebFormDto.getReturnCode()) || StringUtils.isBlank(xbrlWebFormDto.getReturnName()) || StringUtils.isBlank(xbrlWebFormDto.getBankCode()) || StringUtils.isBlank(xbrlWebFormDto.getBankName()) || StringUtils.isBlank(xbrlWebFormDto.getFrequencyName()) || StringUtils.isBlank(xbrlWebFormDto.getStartDate()) || StringUtils.isBlank(xbrlWebFormDto.getEndDate()) || StringUtils.isBlank(xbrlWebFormDto.getTaxonomyVersion()) || StringUtils.isBlank(xbrlWebFormDto.getFrequencyCode()) || StringUtils.isBlank(xbrlWebFormDto.getUserName()) || StringUtils.isBlank(xbrlWebFormDto.getPlatformName()) || StringUtils.isBlank(xbrlWebFormDto.getLevelOfRounding()) || StringUtils.isBlank(xbrlWebFormDto.getLevelOfRoundingCode()) || StringUtils.isBlank(xbrlWebFormDto.getCurrencyCode()) || StringUtils.isBlank(xbrlWebFormDto.getCurrencyName()) || StringUtils.isBlank(xbrlWebFormDto.getTaxonomyXsd())) {
				responseObject = new HashMap<>();
				errorResponseList = new ArrayList<>();
				errorResponseList.add(ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString()));
				responseObject.put("en", errorResponseList);
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0889.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString())).setResponse(responseObject).build();
			}
		} else {
			if (StringUtils.isBlank(xbrlWebFormDto.getGuid()) || StringUtils.isBlank(xbrlWebFormDto.getReturnCode()) || StringUtils.isBlank(xbrlWebFormDto.getReturnName()) || StringUtils.isBlank(xbrlWebFormDto.getBankCode()) || StringUtils.isBlank(xbrlWebFormDto.getBankName()) || StringUtils.isBlank(xbrlWebFormDto.getFrequencyName()) || StringUtils.isBlank(xbrlWebFormDto.getStartDate()) || StringUtils.isBlank(xbrlWebFormDto.getEndDate()) || StringUtils.isBlank(xbrlWebFormDto.getFrequencyCode()) || StringUtils.isBlank(xbrlWebFormDto.getUserName()) || StringUtils.isBlank(xbrlWebFormDto.getPlatformName()) || StringUtils.isBlank(xbrlWebFormDto.getLevelOfRoundingCode()) || StringUtils.isBlank(xbrlWebFormDto.getLevelOfRounding())) {
				responseObject = new HashMap<>();
				errorResponseList = new ArrayList<>();
				errorResponseList.add(ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString()));
				responseObject.put("en", errorResponseList);
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0889.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0889.toString())).setResponse(responseObject).build();
			}
		}
		try {
			apiLogDetails.setRequestJSON(new Gson().toJson(xbrlWebFormDto));
			webServiceComponentUrl = new WebServiceComponentUrl();
			Long componentUrlIdFk = 447l;
			webServiceComponentUrl.setComponentUrlId(componentUrlIdFk);
			apiLogDetails.setComponentIdFk(webServiceComponentUrl);
			apiLogDetails.setServerIPAddress(prop.getProperty("server.ip"));

			logger.info("Add Partial Data");
			if (savePartialDataBean(xbrlWebFormDto)) {
				logger.info("Data inserted successfully");

				serviceResponse = new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.SUCCESS.getConstantVal()).setStatusMessage(GeneralConstants.FILLING_SAVED_SUCCESSFULLY.getConstantVal()).build();
				apiLogDetails.setResponseSendingTimeInLong(DateManip.getCurrentDateTime().getTime());
				apiLogDetails.setResponseJSON(new Gson().toJson(serviceResponse));
			} else {
				logger.error("Error while inserting data");
				responseObject = new HashMap<>();
				errorResponseList = new ArrayList<>();
				errorResponseList.add(ObjectCache.getErrorCodeKey(ErrorCode.E0786.toString()));
				responseObject.put("en", errorResponseList);
				serviceResponse = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0786.toString()).setResponse(responseObject).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0786.toString())).build();
				apiLogDetails.setResponseSendingTimeInLong(DateManip.getCurrentDateTime().getTime());
				apiLogDetails.setResponseJSON(new Gson().toJson(serviceResponse));
			}
			LogApiDetailsUtil.addApiLogDetails(apiLogDetails);
			return serviceResponse;
		} catch (Exception e) {
			logger.error("Exception : ", e);
			responseObject = new HashMap<>();
			errorResponseList = new ArrayList<>();
			errorResponseList.add(ObjectCache.getErrorCodeKey(ErrorCode.E0786.toString()));
			responseObject.put("en", errorResponseList);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0786.toString()).setResponse(responseObject).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0786.toString())).build();
		}
	}

	public XBRLWebFormPartialData getDataByUploadId(Long uplaodId) {
		return xbrlWebFormRepo.findByUploadId(uplaodId);
	}

	public ServiceResponse submitXbrlDocument(XbrlWebFormDto xbrlWebFormDto) {
		Map<String, List<String>> responseObject = null;
		List<String> errorResponseList = null;
		logger.info("inside XbrlWebFormController insert file Details method" + xbrlWebFormDto.getJwtToken());
		ServiceResponse serviceResponse = null;
		try {
			Long userId = null;
			Properties prop = ResourceUtil.getResourcePropertyFile();
			FileDetails fileDetailsBean = new FileDetails();
			String xbrlFolderPath = prop.getProperty("filepath.root") + File.separator + prop.getProperty("filepath.xbrl.webform.instance");
			String filePath = xbrlFolderPath + "/" + xbrlWebFormDto.getTaxonomyXsd() + "/" + xbrlWebFormDto.getTaxonomyVersion() + "/" + xbrlWebFormDto.getGuid() + "/" + xbrlWebFormDto.getGuid() + GeneralConstants.XML_EXTENSION.getConstantVal();
			File file = new File(filePath);
			fileDetailsBean.setFilePath(filePath);
			fileDetailsBean.setFileType(GeneralConstants.XML.getConstantVal());
			fileDetailsBean.setReturnCode(xbrlWebFormDto.getReturnCode());
			fileDetailsBean.setFileName(file.getName());
			UploadChannel uploadChannel = new UploadChannel();
			uploadChannel.setUploadChannelId(1L);
			fileDetailsBean.setUploadChannelIdFk(uploadChannel);
			fileDetailsBean.setReportingPeriodEndDateInString(DateManip.formatDate(xbrlWebFormDto.getEndDate(), xbrlWebFormDto.getSessionDateFormat(), FILING_DATE_FORMAT));
			fileDetailsBean.setEntityCode(xbrlWebFormDto.getBankCode());

			Map<String, List<String>> valueMap = new HashMap<>();
			List<String> userNameList = new ArrayList<>();
			userNameList.add(xbrlWebFormDto.getUserName());
			valueMap.put(ColumnConstants.USER_NAME.getConstantVal(), userNameList);
			List<UserMaster> userMasterList = userMasterService.getDataByColumnValue(valueMap, MethodConstants.GET_USER_DATA_BY_USER_NAME.getConstantVal());
			if (CollectionUtils.isEmpty(userMasterList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusMessage("Username doesnot exist").build();
			}
			for (UserMaster userMaster : userMasterList) {
				userId = userMaster.getUserId();
			}
			fileDetailsBean.setUserId(userId);
			fileDetailsBean.setRoleId(Long.parseLong(xbrlWebFormDto.getRoleId()));
			fileDetailsBean.setFileMimeType("application/xml");
			CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

			List<String> valueList = new ArrayList<>();
			valueList.add(GeneralConstants.VALIDATE_METADATA.getConstantVal());
			valueMap.put(ColumnConstants.COMPONENTTYPE.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(CIMSRestWebserviceClient.HTTP_METHOD_TYPE_POST);
			valueMap.put(ColumnConstants.METHODTYPE.getConstantVal(), valueList);

			WebServiceComponentUrl componentUrl = webServiceComponentService.getDataByColumnValue(valueMap, MethodConstants.GET_ACTIVE_DATA_BY_COMPONENTTYPE_METHODTYPE.getConstantVal()).get(0);

			String jobProcessingId = UUID.randomUUID().toString();
			Map<String, String> headerMap = new HashMap<>();

			headerMap.put(GeneralConstants.JOB_PROCESSING_ID.getConstantVal(), jobProcessingId);

			String response = restServiceClient.callRestWebServiceWithMultipleHeader(componentUrl, fileDetailsBean, null, headerMap);
			//String response = restServiceClient.callRestWebService(componentUrl, fileDetailsBean, null);

			serviceResponse = (ServiceResponse) webServiceResponseReader.readServiceResponse(ServiceResponse.class, response, componentUrl.getUrlProduceType());
			response = JsonUtility.extractResponseValueFromServiceResponseString(response);

			if (serviceResponse.isStatus()) {
				XBRLWebFormPartialData xbrlWebFormData = xbrlWebFormRepo.findByGuid(xbrlWebFormDto.getGuid());
				xbrlWebFormData.setReturnsUploadDetails(returnsUploadDetailsService.getReturnUploadDetailsByUploadIdNActive(Long.parseLong(response)));
				xbrlWebFormData.setFilingStatus(filingStatusRepo.getDataByStatus(xbrlWebFormDto.getFilingStatus()));
				update(xbrlWebFormData);
				serviceResponse = new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.SUCCESS.getConstantVal()).setStatusMessage(GeneralConstants.FILLING_SUBMITTED_SUCCESSFULLY.getConstantVal()).build();
			} else {
				responseObject = new HashMap<>();
				errorResponseList = new ArrayList<>();
				Type listToken = new TypeToken<Map<String, List<String>>>() {
				}.getType();
				Map<String, List<String>> metaDataValidationBeanMap = new Gson().fromJson(response, listToken);
				//List<String> errorKeyList = new ArrayList<>();
				for (String key : metaDataValidationBeanMap.keySet()) {
					for (String errorKey : metaDataValidationBeanMap.get(key)) {
						errorResponseList.add(ObjectCache.getLabelKeyValue("en", errorKey));
					}
				}
				responseObject.put("en", errorResponseList);

				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0786.toString()).setResponse(responseObject).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0786.toString())).setResponse(responseObject).build();
			}

		} catch (Exception e) {
			logger.error("Exception : ", e);
			responseObject = new HashMap<>();
			errorResponseList = new ArrayList<>();
			errorResponseList.add(ObjectCache.getErrorCodeKey(ErrorCode.E0786.toString()));
			responseObject.put("en", errorResponseList);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0786.toString()).setResponse(responseObject).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0786.toString())).setResponse(responseObject).build();
		}

		return serviceResponse;

	}

	public ServiceResponse startXbrlWebFormSession(XbrlWebFormDto xbrlWebFormDto) {
		Map<String, List<String>> responseObject = null;
		List<String> errorResponseList = null;
		try {
			Map<String, List<String>> valueMap = new HashMap<>();
			List<String> userNameList = new ArrayList<>();
			userNameList.add(xbrlWebFormDto.getUserName());
			valueMap.put(ColumnConstants.USER_NAME.getConstantVal(), userNameList);
			List<UserMaster> userMasterList = userMasterService.getDataByColumnValue(valueMap, MethodConstants.GET_USER_DATA_BY_USER_NAME.getConstantVal());
			UserMaster userMaster = userMasterList.get(0);
			if (userMaster == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0823.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0823.toString())).build();
			}
			XbrlWebFormSessionChk xbrlWebFormSessionChk = xbrlWebFormSessionChkRepo.findByUserIdFk(userMaster.getUserId());
			Date currentTime = new Date();
			Calendar calender = Calendar.getInstance();
			calender.setTimeInMillis(currentTime.getTime());
			calender.add(Calendar.SECOND, Integer.parseInt(xbrlWebFormDto.getSessionTimeOut()));

			if (xbrlWebFormSessionChk == null) {
				xbrlWebFormSessionChk = new XbrlWebFormSessionChk();
				xbrlWebFormSessionChk.setUserIdFk(userMaster.getUserId());
				xbrlWebFormSessionChk.setIsLoggedIn(true);
				xbrlWebFormSessionChk.setLoginTime(currentTime);
				xbrlWebFormSessionChk.setExpirationTime(calender.getTime());
			} else {
				xbrlWebFormSessionChk.setIsLoggedIn(true);
				xbrlWebFormSessionChk.setLoginTime(currentTime);
				xbrlWebFormSessionChk.setExpirationTime(calender.getTime());
			}

			if (xbrlWebFormSessionChkRepo.save(xbrlWebFormSessionChk).getXbrlSessionChkId() != null) {
				return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.SUCCESS.getConstantVal()).setStatusMessage(GeneralConstants.SUCCESS.getConstantVal()).build();
			} else {
				responseObject = new HashMap<>();
				errorResponseList = new ArrayList<>();
				errorResponseList.add(ObjectCache.getErrorCodeKey(ErrorCode.E0823.toString()));
				responseObject.put("en", errorResponseList);
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0823.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0823.toString())).setResponse(responseObject).build();
			}
		} catch (Exception e) {
			logger.error("Exception : ", e);
			responseObject = new HashMap<>();
			errorResponseList = new ArrayList<>();
			errorResponseList.add(ObjectCache.getErrorCodeKey(ErrorCode.E0786.toString()));
			responseObject.put("en", errorResponseList);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0786.toString()).setResponse(responseObject).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0786.toString())).setResponse(responseObject).build();
		}
	}

	public boolean checkXbrlUserSessionChk(String userName, String jwtToken) {
		boolean flag = false;
		try {
			System.out.println(AESV2.getInstance().encrypt("DC4CB4A1B3A29BF4CF972F43C35055CD"));
			Map<String, List<String>> valueMap = new HashMap<>();
			List<String> userNameList = new ArrayList<>();
			userNameList.add(userName);
			valueMap.put(ColumnConstants.USER_NAME.getConstantVal(), userNameList);
			List<UserMaster> userMasterList = userMasterService.getDataByColumnValue(valueMap, MethodConstants.GET_USER_DATA_BY_USER_NAME.getConstantVal());
			UserMaster userMaster = userMasterList.get(0);

			XbrlWebFormSessionChk xbrlWebFormSessionChk = xbrlWebFormSessionChkRepo.findByUserIdFk(userMaster.getUserId());
			if (xbrlWebFormSessionChk != null) {
				if ((!Validations.isEmpty(userMaster.getUserJSessionId()) && userMaster.getUserJSessionId().equals(AESV2.getInstance().decrypt(jwtToken))) && new Date().getTime() >= xbrlWebFormSessionChk.getLoginTime().getTime() && new Date().getTime() <= xbrlWebFormSessionChk.getExpirationTime().getTime()) {
					flag = true;
				}
			}
		} catch (Exception e) {
			logger.error("Exception : ", e);
			return flag;
			//throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		//		return false;
		return true;
	}

}
