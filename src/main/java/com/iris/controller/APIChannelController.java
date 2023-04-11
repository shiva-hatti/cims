package com.iris.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iris.caching.ObjectCache;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.ApiFileDetailDto;
import com.iris.dto.FileDetailLog;
import com.iris.dto.HTTPRequestDetails;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.dto.UserAndBrowserInfoInputBean;
import com.iris.dto.UserLoginMetaInfoUtil;
import com.iris.exception.ServiceException;
import com.iris.model.ApiLogDetails;
import com.iris.model.EntityBean;
import com.iris.model.FileDetails;
import com.iris.model.FilingStatus;
import com.iris.model.Frequency;
import com.iris.model.Return;
import com.iris.model.UploadChannel;
import com.iris.model.UserMaster;
import com.iris.model.WebServiceComponentUrl;
import com.iris.service.GenericService;
import com.iris.service.impl.EntityService;
import com.iris.service.impl.ReturnService;
import com.iris.service.impl.WebServiceComponentService;
import com.iris.util.AESV2;
import com.iris.util.JsonUtility;
import com.iris.util.ResourceUtil;
import com.iris.util.XmlValidate;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.FrequencyEnum;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;
import com.iris.webservices.client.CIMSRestWebserviceClient;

@RestController
@RequestMapping("/service/APIChannelController")
public class APIChannelController {

	private static final Logger LOGGER = LogManager.getLogger(APIChannelController.class);

	@Autowired
	private WebServiceComponentService webServiceComponentService;

	@Autowired
	private GenericService<UserMaster, Long> userMasterService;

	@Autowired
	private EntityService entityService;

	@Autowired
	private ReturnService returnService;

	@Autowired
	private HttpServletRequest request;

	Gson gson = new Gson();

	XmlValidate xmlValidate;

	@Context
	HttpHeaders httpHeaders;

	private String jobProcessingId;

	@PostMapping(value = "/addAPIFileDetails")
	public ServiceResponse insertAPIFileDetails(@RequestHeader(name = "AppId") String appId, @RequestHeader(name = "userName") String userName, @RequestHeader(name = "password") String password, @RequestHeader(name = "entityCode") String entityCode, @RequestHeader(name = "returnCode") String returnCode, @RequestBody ApiFileDetailDto apiFileDetailDto) {

		LOGGER.info("inside APIChannelController insert file Details method" + appId);
		ApiLogDetails apiLogDetails = new ApiLogDetails();
		apiLogDetails.setRequestReceivedTimeInLong(DateManip.getCurrentDateTime().getTime());
		Properties prop = ResourceUtil.getResourcePropertyFile();
		String saveDirectory = prop.getProperty("filepath.root");
		Long userId = null;
		String entCode = null;
		String fileNameFormat = null;
		String fileNameFormatDoc = null;
		FileDetailLog fileDetailLog = null;
		WebServiceComponentUrl webServiceComponentUrl = null;
		ServiceResponse serviceResponse = null;
		List<FileDetails> listOfFileDetails = null;
		FileDetails fileDetails = new FileDetails();
		UploadChannel uploadChannel = null;
		FilingStatus fileStatus = null;
		String fileType = null;
		File file = null;
		String ifscCode = null;
		List<String> validationList = new ArrayList<>();
		UserMaster userMasterData = new UserMaster();
		UserAndBrowserInfoInputBean userAndBrowserInfoInputBeanNew = null;

		if (StringUtils.isBlank(apiFileDetailDto.getFrequency())) {
			validationList.add(GeneralConstants.NULL_FREQUENCY.getConstantVal());
		}

		if (StringUtils.isBlank(apiFileDetailDto.getReportingPeriodEndDate())) {
			validationList.add(GeneralConstants.NULL_END_DATE.getConstantVal());
		}

		if (StringUtils.isBlank(apiFileDetailDto.getBase64EncodedStringValue())) {
			validationList.add(GeneralConstants.NULL_BASE_64_STRING.getConstantVal());
		}

		apiLogDetails.setRequestJSON(new Gson().toJson(apiFileDetailDto));
		webServiceComponentUrl = new WebServiceComponentUrl();
		webServiceComponentUrl.setComponentUrlId(GeneralConstants.WEB_SERVICE_COMPONENT_URL_ID_API.getConstantLongVal());

		LOGGER.info("API channel controller, component url " + GeneralConstants.WEB_SERVICE_COMPONENT_URL_ID_API.getConstantLongVal());
		apiLogDetails.setComponentIdFk(webServiceComponentUrl);
		apiLogDetails.setServerIPAddress(UserLoginMetaInfoUtil.getClientIpAddr(request));

		/*
		 * setting data into API log bean
		 */
		if (!StringUtils.isBlank(apiFileDetailDto.getJwtToken())) {
			apiLogDetails.setJwtToken(apiFileDetailDto.getJwtToken());
		}

		if (!StringUtils.isBlank(appId)) {
			apiLogDetails.setAppId(appId);
		}

		if (!CollectionUtils.isEmpty(validationList)) {
			apiLogDetails.setResponseJSON(ObjectCache.getErrorCodeKey(ErrorCode.EC0391.toString()));
			apiLogDetails.setResponseSendingTimeInLong(DateManip.getCurrentDateTime().getTime());
			addApiLogDetails(apiLogDetails);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0391.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0391.toString())).build();
		}

		try {
			Map<String, List<String>> valueMap = new HashMap<>();
			List<String> userNameList = new ArrayList<>();
			userNameList.add(AESV2.getInstance().encrypt(userName));
			valueMap.put(ColumnConstants.USER_NAME.getConstantVal(), userNameList);
			List<UserMaster> userMasterList = userMasterService.getDataByColumnValue(valueMap, MethodConstants.GET_USER_DATA_BY_USER_NAME.getConstantVal());
			if (CollectionUtils.isEmpty(userMasterList)) {
				apiLogDetails.setResponseJSON(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString()));
				apiLogDetails.setResponseSendingTimeInLong(DateManip.getCurrentDateTime().getTime());
				addApiLogDetails(apiLogDetails);
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			}

			/*
			 * New Approach to authenticate user with his username and password as per discussion with suman sir on 21 march 2020
			 */

			userAndBrowserInfoInputBeanNew = new UserAndBrowserInfoInputBean();
			userAndBrowserInfoInputBeanNew.setUserName(AESV2.getInstance().encrypt(userName));
			userAndBrowserInfoInputBeanNew.setPassword(AESV2.getInstance().encrypt(password));

			HTTPRequestDetails httpReqDetails = new HTTPRequestDetails();

			httpReqDetails.setClientBrowser(UserLoginMetaInfoUtil.getClientBrowser(request));
			httpReqDetails.setClientBrowser(UserLoginMetaInfoUtil.getClientBrowser(request));
			httpReqDetails.setClientIPAddr(UserLoginMetaInfoUtil.getClientIpAddr(request));
			httpReqDetails.setClientOS(UserLoginMetaInfoUtil.getClientOS(request));
			httpReqDetails.setReferer(UserLoginMetaInfoUtil.getReferer(request));
			httpReqDetails.setFullURL(UserLoginMetaInfoUtil.getFullURL(request));

			userAndBrowserInfoInputBeanNew.setHttpReqDetails(httpReqDetails);

			CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

			WebServiceComponentUrl componentUrl = getWebServiceComponentURL(GeneralConstants.AUTHENTICATE_USER.getConstantVal(), CIMSRestWebserviceClient.HTTP_METHOD_TYPE_POST);

			Map<String, String> headerMap = new HashMap<>();

			jobProcessingId = UUID.randomUUID().toString();
			headerMap.put(GeneralConstants.UUID.getConstantVal(), jobProcessingId);

			String responsestring = restServiceClient.callRestWebServiceWithMultipleHeader(componentUrl, userAndBrowserInfoInputBeanNew, null, headerMap);

			ServiceResponse serviceResponseNew = JsonUtility.getGsonObject().fromJson(responsestring, ServiceResponse.class);

			if (!serviceResponseNew.isStatus()) {
				apiLogDetails.setResponseJSON(ObjectCache.getErrorCodeKey(ErrorCode.E0817.toString()));
				apiLogDetails.setResponseSendingTimeInLong(DateManip.getCurrentDateTime().getTime());
				addApiLogDetails(apiLogDetails);
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0817.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0817.toString())).build();
			}

			for (UserMaster userMaster : userMasterList) {
				userId = userMaster.getUserId();
				userMasterData.setUserId(userMaster.getUserId());
			}

			apiLogDetails.setUserIdFk(userMasterData);

			Map<String, List<String>> columnValueMap = new HashMap<>();
			List<String> entityCodes = new ArrayList<>();
			entityCodes.add(entityCode);
			columnValueMap.put(ColumnConstants.ENTITY_CODE.getConstantVal(), entityCodes);

			List<EntityBean> entityBeanList = entityService.getDataByColumnValue(columnValueMap, MethodConstants.GET_ENTITY_DATA_BY_CODE.getConstantVal());
			if (CollectionUtils.isEmpty(entityBeanList)) {

				apiLogDetails.setResponseJSON(ObjectCache.getErrorCodeKey(ErrorCode.E0818.toString()));
				apiLogDetails.setResponseSendingTimeInLong(DateManip.getCurrentDateTime().getTime());
				addApiLogDetails(apiLogDetails);

				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0818.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0818.toString())).build();
			}

			for (EntityBean entityBean : entityBeanList) {
				entCode = entityBean.getEntityCode();
				ifscCode = entityBean.getIfscCode();
			}

			columnValueMap.clear();
			List<String> returnCodes = new ArrayList<>();
			returnCodes.add(returnCode);
			columnValueMap.put(ColumnConstants.RETURN_CODE.getConstantVal(), returnCodes);
			List<Return> returnBeanList = returnService.getDataByColumnValue(columnValueMap, MethodConstants.GET_RETURN_DATA_BY_CODE.getConstantVal());
			if (CollectionUtils.isEmpty(returnBeanList)) {

				apiLogDetails.setResponseJSON(ObjectCache.getErrorCodeKey(ErrorCode.E0819.toString()));
				apiLogDetails.setResponseSendingTimeInLong(DateManip.getCurrentDateTime().getTime());
				addApiLogDetails(apiLogDetails);

				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0819.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0819.toString())).build();
			}

			if (!apiFileDetailDto.getReportingPeriodEndDate().matches(prop.getProperty("REPORTING_PERIOD_DATE_FORMAT_REGEX"))) {

				apiLogDetails.setResponseJSON(ObjectCache.getErrorCodeKey(ErrorCode.E0820.toString()));
				apiLogDetails.setResponseSendingTimeInLong(DateManip.getCurrentDateTime().getTime());
				addApiLogDetails(apiLogDetails);

				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0820.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0820.toString())).build();
			}

			if ((Integer.parseInt(apiFileDetailDto.getReportingPeriodEndDate().subSequence(2, 4).toString()) > 13)) {

				apiLogDetails.setResponseJSON(ObjectCache.getErrorCodeKey(ErrorCode.E0820.toString()));
				apiLogDetails.setResponseSendingTimeInLong(DateManip.getCurrentDateTime().getTime());
				addApiLogDetails(apiLogDetails);

				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0820.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0820.toString())).build();
			}

			fileNameFormat = entCode + "_instFile" + "_" + DateManip.getCurrentDate(GeneralConstants.FILE_CREATE_DATE_FORMAT.getConstantVal() + "_" + DateManip.getCurrentTime(GeneralConstants.FILE_CREATE_TIME_FORMAT.getConstantVal())) + "_" + userId;
			fileNameFormatDoc = fileNameFormat;
			file = null;
			if (apiFileDetailDto.getBase64EncodedStringValue() != null) {

				// Base 64 coding to decode the encoded string and check the file type and store
				// to destination folder if valid

				byte[] decodedBytes = Base64.decodeBase64(apiFileDetailDto.getBase64EncodedStringValue().getBytes());
				String filemMimeType = new Tika().detect(decodedBytes);
				LOGGER.info("fileMimetype" + filemMimeType);
				fileDetails.setUserMaster(userMasterData);
				fileDetails.setUserId(userId);
				xmlValidate = new XmlValidate();
				if (!StringUtils.isBlank(filemMimeType)) {
					saveDirectory = saveDirectory + File.separator + prop.getProperty("filePath.system.dest.path") + File.separator;
					fileDetails.setFileMimeType(filemMimeType);
					if (filemMimeType.contains(GeneralConstants.CSVMIMETYPE.getConstantVal()) || filemMimeType.contains(GeneralConstants.APPLICATIONCSVMIMETYPE.getConstantVal())) {
						fileDetails.setFileType(GeneralConstants.CSV.getConstantVal());
						fileType = "." + GeneralConstants.CSV.getConstantVal();
						fileNameFormat = fileNameFormat + fileType;

						file = writeByteArraysToFile(saveDirectory, decodedBytes, fileNameFormat);
						fileDetails.setIsActive(true);
						fileStatus = new FilingStatus();
						fileStatus.setFilingStatusId(GeneralConstants.FILE_STATUS_VALID_ID.getConstantIntVal());
						fileDetails.setFilingStatus(fileStatus);

					} else if (filemMimeType.contains(GeneralConstants.TEXTFILEMIMETYPE.getConstantVal())) {
						fileDetails.setFileType(GeneralConstants.TXT.getConstantVal());
						fileType = "." + GeneralConstants.TXT.getConstantVal();
						fileNameFormat = fileNameFormat + fileType;
						file = writeByteArraysToFile(saveDirectory, decodedBytes, fileNameFormat);
						fileDetails.setIsActive(true);
						fileStatus = new FilingStatus();
						fileStatus.setFilingStatusId(GeneralConstants.FILE_STATUS_VALID_ID.getConstantIntVal());
						fileDetails.setFilingStatus(fileStatus);

					} else if (filemMimeType.contains(GeneralConstants.XMLMIMETYPE.getConstantVal())) {
						fileDetails.setFileType(GeneralConstants.XML.getConstantVal());
						fileType = "." + GeneralConstants.XML.getConstantVal();
						fileNameFormat = fileNameFormat + fileType;
						file = writeByteArraysToFile(saveDirectory, decodedBytes, fileNameFormat);
						if (!xmlValidate.isValidXmlDocument(file.toString())) {
							return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0821.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0821.toString())).build();
						}
						fileDetails.setIsActive(true);
						fileStatus = new FilingStatus();
						fileStatus.setFilingStatusId(GeneralConstants.FILE_STATUS_VALID_ID.getConstantIntVal());
						fileDetails.setFilingStatus(fileStatus);
					} else {

						apiLogDetails.setResponseJSON(ObjectCache.getErrorCodeKey(ErrorCode.E0821.toString()));
						apiLogDetails.setResponseSendingTimeInLong(DateManip.getCurrentDateTime().getTime());
						addApiLogDetails(apiLogDetails);

						return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0821.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0821.toString())).build();
					}
				} else {
					LOGGER.error("Error while generating mimetype" + filemMimeType);
				}
			}

			fileDetails.setFileName(file.getName());
			LOGGER.info("file name to be processing" + fileDetails.getFileName());
			fileDetails.setFileCopyingStartTimeInLong(DateManip.getCurrentDateTime().getTime());
			fileDetails.setUserName(userName);
			fileDetails.setEntityCode(entityCode);
			fileDetails.setReturnCode(returnCode);
			fileDetails.setIfscCode(ifscCode);

			if (FrequencyEnum.getIdByCode(apiFileDetailDto.getFrequency()) != null) {
				Frequency frequency = new Frequency();
				frequency.setFrequencyId(FrequencyEnum.getIdByCode(apiFileDetailDto.getFrequency()));
				fileDetails.setFrequencyIdFk(frequency);

			} else {
				apiLogDetails.setResponseJSON(ObjectCache.getErrorCodeKey(ErrorCode.E0689.toString()));
				apiLogDetails.setResponseSendingTimeInLong(DateManip.getCurrentDateTime().getTime());
				addApiLogDetails(apiLogDetails);

				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0689.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0689.toString())).build();
			}
			fileDetails.setReportingPeriodEndDateInLong(DateManip.convertStringToDate(apiFileDetailDto.getReportingPeriodEndDate(), GeneralConstants.CONST_DDMMYYYY.getConstantVal()).getTime());
			uploadChannel = new UploadChannel();
			uploadChannel.setUploadChannelId(GeneralConstants.API_CHANNEL.getConstantLongVal());
			fileDetails.setUploadChannelIdFk(uploadChannel);

			fileDetails.setFileCopyingEndTimeInLong(DateManip.getCurrentDateTime().getTime());
			Date fileCreationDate = new Date(file.lastModified());
			fileDetails.setFileCreationTimeInNumber(fileCreationDate.getTime());
			fileDetails.setSize(file.length());

			// Reading supporting doc start

			fileNameFormatDoc = fileNameFormatDoc + "_Doc";

			fileType = null;
			file = null;
			if (!StringUtils.isEmpty(apiFileDetailDto.getBase64DocStringValue())) {
				byte[] decodedBytes = Base64.decodeBase64(apiFileDetailDto.getBase64DocStringValue().getBytes());
				String filemMimeType = new Tika().detect(decodedBytes);

				if (!StringUtils.isBlank(filemMimeType)) {
					if (filemMimeType.contains(GeneralConstants.XLSMIMETYPEFORBASE64STRING.getConstantVal()) || filemMimeType.contains(GeneralConstants.XLSXMIMETYPE.getConstantVal())) {
						fileType = "." + GeneralConstants.XLS.getConstantVal();
						fileNameFormatDoc = fileNameFormatDoc + fileType;

						file = writeByteArraysToFile(saveDirectory, decodedBytes, fileNameFormatDoc);
						fileDetails.setSupportiveDocName(fileNameFormatDoc);
						fileDetails.setSupportiveDocType(GeneralConstants.XLS.getConstantVal());

					} else if (filemMimeType.contains(GeneralConstants.MSWORDMIMETYPE.getConstantVal())) {
						fileType = "." + GeneralConstants.MSWORD.getConstantVal();
						fileNameFormatDoc = fileNameFormatDoc + fileType;

						file = writeByteArraysToFile(saveDirectory, decodedBytes, fileNameFormatDoc);
						fileDetails.setSupportiveDocName(fileNameFormatDoc);
						fileDetails.setSupportiveDocType(GeneralConstants.MSWORD.getConstantVal());

					} else if (filemMimeType.contains(GeneralConstants.DOCXMIMETYPE.getConstantVal())) {
						fileType = "." + GeneralConstants.DOCX.getConstantVal();
						fileNameFormatDoc = fileNameFormatDoc + fileType;
						file = writeByteArraysToFile(saveDirectory, decodedBytes, fileNameFormatDoc);
						fileDetails.setSupportiveDocName(fileNameFormatDoc);
						fileDetails.setSupportiveDocType(GeneralConstants.DOCX.getConstantVal());
					} else if (filemMimeType.contains(GeneralConstants.XLSXMIMETYPE.getConstantVal()) || filemMimeType.contains(GeneralConstants.XLSXMIMETYPEFORBASE64STRING.getConstantVal())) {
						fileType = "." + GeneralConstants.XLSX.getConstantVal();
						fileNameFormatDoc = fileNameFormatDoc + fileType;
						file = writeByteArraysToFile(saveDirectory, decodedBytes, fileNameFormatDoc);
						fileDetails.setSupportiveDocName(fileNameFormatDoc);
						fileDetails.setSupportiveDocType(GeneralConstants.XLSX.getConstantVal());
					} else if (filemMimeType.contains(GeneralConstants.PDFMIMETYPE.getConstantVal())) {
						fileType = "." + GeneralConstants.PDF.getConstantVal();
						fileNameFormatDoc = fileNameFormatDoc + fileType;
						file = writeByteArraysToFile(saveDirectory, decodedBytes, fileNameFormatDoc);
						fileDetails.setSupportiveDocName(fileNameFormatDoc);
						fileDetails.setSupportiveDocType(GeneralConstants.PDF.getConstantVal());
					} else {
						LOGGER.error("Invalid Supporting Document Format" + filemMimeType);

						apiLogDetails.setResponseJSON(ObjectCache.getErrorCodeKey(ErrorCode.E0822.toString()));
						apiLogDetails.setResponseSendingTimeInLong(DateManip.getCurrentDateTime().getTime());
						addApiLogDetails(apiLogDetails);

						return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0822.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0822.toString())).build();

					}
				} else {
					LOGGER.error("Error while generating mimetype of Supporting Document" + filemMimeType);
				}

			} else {
				LOGGER.info("Supporting Doc Not Received");
			}
			// Reading supporting doc end

			fileDetailLog = new FileDetailLog();
			listOfFileDetails = new ArrayList<>();
			listOfFileDetails.add(fileDetails);
			fileDetailLog.setFileDetailsList(listOfFileDetails);
			LOGGER.info("API file Detail called");
			if (addFileDetaill(fileDetailLog)) {
				LOGGER.info("Data inserted successfully");

				serviceResponse = new ServiceResponseBuilder().setStatus(true).build();
				apiLogDetails.setResponseSendingTimeInLong(DateManip.getCurrentDateTime().getTime());
				apiLogDetails.setResponseJSON(new Gson().toJson(serviceResponse));
			} else {
				LOGGER.info("Error while inserting data into file details table");
				serviceResponse = new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();

				apiLogDetails.setResponseSendingTimeInLong(DateManip.getCurrentDateTime().getTime());
				apiLogDetails.setResponseJSON(new Gson().toJson(serviceResponse));
			}
			addApiLogDetails(apiLogDetails);
			return serviceResponse;
		} catch (Exception e) {

			apiLogDetails.setResponseJSON(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString()));
			apiLogDetails.setResponseSendingTimeInLong(DateManip.getCurrentDateTime().getTime());
			addApiLogDetails(apiLogDetails);

			LOGGER.error("Exception : ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	private void addApiLogDetails(ApiLogDetails apiLogDetails) {
		try {
			CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();
			Map<String, List<String>> valueMap = new HashMap<>();

			List<String> valueList = new ArrayList<>();
			valueList.add(GeneralConstants.API_LOG_DETAILS_COMP.getConstantVal());
			valueMap.put(ColumnConstants.COMPONENTTYPE.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(CIMSRestWebserviceClient.HTTP_METHOD_TYPE_POST);
			valueMap.put(ColumnConstants.METHODTYPE.getConstantVal(), valueList);

			WebServiceComponentUrl componentUrl = webServiceComponentService.getDataByColumnValue(valueMap, MethodConstants.GET_ACTIVE_DATA_BY_COMPONENTTYPE_METHODTYPE.getConstantVal()).get(0);

			String response = restServiceClient.callRestWebService(componentUrl, apiLogDetails, null);
			LOGGER.info("service response: " + response);

		} catch (Exception e) {
			LOGGER.error("Exception while getting file info: ", e);
		}
	}

	private File writeByteArraysToFile(String destPath, byte[] decodedBytes, String fileNameFormat) {
		File file = new File(destPath + fileNameFormat);
		try (BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(file))) {
			writer.write(decodedBytes);
			writer.flush();
		} catch (Exception e) {
			LOGGER.error("Exception while writting file int: ", e);
		}
		return file;
	}

	/*
	 * This method is used to call the audit log component url and insert file
	 * details into database
	 */
	private boolean addFileDetaill(FileDetailLog fileDetailLog) {
		try {
			CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();
			Map<String, List<String>> valueMap = new HashMap<>();

			List<String> valueList = new ArrayList<>();
			valueList.add(GeneralConstants.FILE_DETAILS_COMP.getConstantVal());
			valueMap.put(ColumnConstants.COMPONENTTYPE.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(CIMSRestWebserviceClient.HTTP_METHOD_TYPE_POST);
			valueMap.put(ColumnConstants.METHODTYPE.getConstantVal(), valueList);

			WebServiceComponentUrl componentUrl = webServiceComponentService.getDataByColumnValue(valueMap, MethodConstants.GET_ACTIVE_DATA_BY_COMPONENTTYPE_METHODTYPE.getConstantVal()).get(0);

			String response = restServiceClient.callRestWebService(componentUrl, fileDetailLog, null);

			Type listToken = new TypeToken<ServiceResponse>() {
			}.getType();
			ServiceResponse serviceResponse = gson.fromJson(response, listToken);

			if (serviceResponse.isStatus() && !serviceResponse.getStatusCode().equalsIgnoreCase("ERROR")) {
				return true;
			}
			LOGGER.info("add file details response" + response);
		} catch (Exception e) {
			LOGGER.error("Exception while getting file info: ", e);
		}
		return false;
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
			LOGGER.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}
		return componentUrl;
	}

}
