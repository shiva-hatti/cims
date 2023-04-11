/**
 * 
 */
package com.iris.controller;

import java.io.File;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.iris.caching.ObjectCache;
import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.DynamicContent;
import com.iris.dto.EntityDto;
import com.iris.dto.FileDetailsBean;
import com.iris.dto.FilingCalendarDto;
import com.iris.dto.MailServiceBean;
import com.iris.dto.RetUploadDetBean;
import com.iris.dto.ReturnEntityMapDto;
import com.iris.dto.ReturnEntityOutputDto;
import com.iris.dto.ServiceResponse;
import com.iris.dto.UserDetailsDto;
import com.iris.dto.UserDto;
import com.iris.dto.UserRoleDto;
import com.iris.dto.WorkflowReturnMappingInfo;
import com.iris.exception.ServiceException;
import com.iris.fileDataExtract.ExtractFileData;
import com.iris.json.bean.CustomJsonBean;
import com.iris.json.bean.DocumentInfo;
import com.iris.json.bean.FilingDetailsBean;
import com.iris.model.EntityBean;
import com.iris.model.FileDetails;
import com.iris.model.FilingStatus;
import com.iris.model.Frequency;
import com.iris.model.FrequencyDescription;
import com.iris.model.Return;
import com.iris.model.ReturnEntityMappingNew;
import com.iris.model.ReturnFileFormatMap;
import com.iris.model.ReturnTemplate;
import com.iris.model.ReturnUploadNBusinessData;
import com.iris.model.ReturnsUploadDetails;
import com.iris.model.RevisionRequest;
import com.iris.model.Scheduler;
import com.iris.model.SchedulerLog;
import com.iris.model.UnlockingRequest;
import com.iris.model.UserEntityRole;
import com.iris.model.UserMaster;
import com.iris.model.UserRole;
import com.iris.model.UserRoleReturnMapping;
import com.iris.model.WebServiceComponentUrl;
import com.iris.model.WorkflowReturnMapping;
import com.iris.sdmx.status.bean.SdmxActivityDetailLogRequest;
import com.iris.sdmx.status.service.SdmxFileActivityLogService;
import com.iris.service.GenericService;
import com.iris.service.impl.FilingCalendarService;
import com.iris.service.impl.ReturnTemplateService;
import com.iris.service.impl.UserMasterServiceV2;
import com.iris.service.impl.UserRoleService;
import com.iris.util.FileCheckSumUtility;
import com.iris.util.FileManager;
import com.iris.util.FileMimeType;
import com.iris.util.JsonUtility;
import com.iris.util.ResourceUtil;
import com.iris.util.XmlValidate;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MetaDataCheckConstants;
import com.iris.util.constant.MethodConstants;
import com.iris.util.constant.ReturnPropertyVal;
import com.iris.util.constant.UploadFilingConstants;
import com.iris.webservices.client.CIMSRestWebserviceClient;

/**
 * @author sajadhav
 *
 */
@RestController
@RequestMapping("/service/fileMetaDataValidateController")
public class FileMetaDataValidateController {

	private static final Logger LOGGER = LogManager.getLogger(FileMetaDataValidateController.class);

	@Autowired
	private GenericService<WebServiceComponentUrl, Long> webServiceComponentService;

	@Autowired
	private FileUploadController fileUploadController;

	@Autowired
	private GenericService<Return, Long> returnService;

	@Autowired
	private GenericService<EntityBean, Long> entityService;

	@Autowired
	private GenericService<UserMaster, Long> userMasterService;

	@Autowired
	private GenericService<FileDetails, Long> fileDetailsService;

	@Autowired
	private GenericService<UnlockingRequest, Long> unlockRequestService;

	@Autowired
	private GenericService<RevisionRequest, Long> revisionRequestService;

	@Autowired
	private GenericService<UserRoleReturnMapping, Long> userRoleReturnMappingService;

	@Autowired
	private PrepareSendMailController prepareSendMailController;

	@Autowired
	private WorkFlowController workFlowController;

	@Autowired
	private ReturnUploadResultProcessorController returnUploadResultProcessorController;

	@Autowired
	private GenericService<UserEntityRole, Long> userEntityRoleService;

	@Autowired
	private GenericService<ReturnsUploadDetails, Long> returnUploadDetailsService;

	@Autowired
	private FilingCalendarService filingCalendarService;

	@Autowired
	private ReturnTemplateService returnTemplateService;

	@Autowired
	private EntityMasterControllerV2 entityMasterControllerV2;

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private UserMasterServiceV2 userMasterServiceV2;

	private static final String ROOT_PATH = "filepath.root";
	private static final String DEST_PATH = "filePath.system.dest.path";
	private static final String VALIDATE_META_PATH = "filePath.validate.meta.failed.path";
	private static final String META_DATA_FAILED_PATH = "filePath.validate.meta.failed.path";

	private static final String RBR_METADATA_PROCESS_CODE = "RBR_METADATA_VALIDATION";
	private static final Long ADMIN_USER_ID = 3L;

	@Autowired
	private SdmxFileActivityLogService activityDetailLogService;

	@Value("${scheduler.code.metadata}")
	private String schedulerCode;

	@Scheduled(cron = "${cron.entry}")
	public void validateMetaDataValidation() {
		String jobProcessingId = UUID.randomUUID().toString();
		LOGGER.info("Scheduler started with Job processing ID {}", jobProcessingId);
		List<FileDetails> fileDetailsList = null;
		Long schedulerLogId = null;
		Scheduler scheduler = getSchedulerStatus(jobProcessingId, schedulerCode);

		if (scheduler != null) {
			if (scheduler.getIsRunning().equals(Boolean.TRUE)) {
				LOGGER.error("Error while starting scheduler -> Reason : Schduler is alrady running ");
				return;
			}
			try {
				fileDetailsList = fileDetailsService.getDataByColumnValue(null, MethodConstants.GET_UNPROCESSED_DATA_AND_UPDATE_PROCESSING_FLAG.getConstantVal());
				LOGGER.info("Total Taken records size by meta data scheduler: {}", fileDetailsList.size());
				schedulerLogId = makeSchedulerStartEntry(Long.valueOf(fileDetailsList.size()), scheduler.getSchedulerId(), jobProcessingId);
				if (StringUtils.isEmpty(schedulerLogId)) {
					LOGGER.error("Error while starting scheduler -> Reason : Schduler Log ID not received ");
					return;
				}
			} catch (Exception e) {
				LOGGER.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
				return;
			}

			String ipAddress = null;
			try {
				ipAddress = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e1) {
				LOGGER.error("Error while getting IP address");
			}

			int successfullyProcessedRecord = 0;
			for (FileDetails fileDetails : fileDetailsList) {
				try {
					SdmxActivityDetailLogRequest sdmxFileActivityLog = getSdmxFileActivityLog(fileDetails, ipAddress);

					FileDetailsBean fileDetailsBean = domainToDtoConverter(fileDetails);
					ServiceResponse serviceResponse = validateMetaData(jobProcessingId, fileDetailsBean);
					if (serviceResponse.isStatus()) {
						sdmxFileActivityLog.setIsSuccess(false);
						successfullyProcessedRecord++;
					} else {
						sdmxFileActivityLog.setIsSuccess(true);
					}
					sdmxFileActivityLog.setProcessEndTimeLong(new Date().getTime());
					activityDetailLogService.saveActivityDetailLog(sdmxFileActivityLog, jobProcessingId, ADMIN_USER_ID);
				} catch (Exception e) {
					LOGGER.error("Exception occured while doing metadata validation for file Details ID : " + fileDetails.getId(), e);
				}
			}
			makeSchedulerStopEntry(successfullyProcessedRecord, schedulerLogId, scheduler.getSchedulerId(), jobProcessingId);
		} else {
			LOGGER.error(ErrorConstants.SCHEDULER_INFO_NOT_PRESENT.getConstantVal());
		}
	}

	private FileDetailsBean domainToDtoConverter(FileDetails fileDetails) {

		FileDetailsBean fileDetailsBean = new FileDetailsBean();
		fileDetailsBean.setBusinessValidationPass(fileDetails.isBusinessValidationPass());
		fileDetailsBean.setLangCode(fileDetails.getLangCode());
		fileDetailsBean.setDateFormat(fileDetails.getDateFormat());
		fileDetailsBean.setCalendarFormat(fileDetails.getCalendarFormat());
		fileDetailsBean.setFrequencyIdFk(fileDetails.getFrequencyIdFk());
		fileDetailsBean.setFrequency(fileDetails.getFrequency());
		fileDetailsBean.setReturnPropertyValId(fileDetails.getReturnPropertyValId());
		fileDetailsBean.setReportStatus(fileDetails.getReportStatus());
		fileDetailsBean.setReportingPeriodStartDateInString(fileDetails.getReportingPeriodStartDateInString());
		fileDetailsBean.setApplicationProcessId(fileDetails.getApplicationProcessId());
		fileDetailsBean.setFilingStatus(fileDetails.getFilingStatus());
		fileDetailsBean.setWorkflowId(fileDetails.getWorkflowId());
		fileDetailsBean.setWorkflowStepNo(fileDetails.getWorkflowStepNo());
		fileDetailsBean.setReportingPeriodStartDateInLong(fileDetails.getReportingPeriodStartDateInLong());
		fileDetailsBean.setReportingPeriodEndDateInLong(fileDetails.getReportingPeriodEndDateInLong());
		fileDetailsBean.setFileCreationTimeInNumber(fileDetails.getFileCreationTimeInNumber());
		fileDetailsBean.setId(fileDetails.getId());
		fileDetailsBean.setFileName(fileDetails.getFileName());
		fileDetailsBean.setFileCopyingStartTime(fileDetails.getFileCopyingStartTime());
		fileDetailsBean.setFileCopyingEndTime(fileDetails.getFileCopyingEndTime());
		fileDetailsBean.setFileCreationTime(fileDetails.getFileCreationTime());
		fileDetailsBean.setFileType(fileDetails.getFileType());
		fileDetailsBean.setReasonOfNotProcessed(fileDetails.getReasonOfNotProcessed());
		fileDetailsBean.setSize(fileDetails.getSize());
		fileDetailsBean.setFileCheckSum(fileDetails.getFileCheckSum());
		fileDetailsBean.setFileMimeType(fileDetails.getFileMimeType());
		fileDetailsBean.setCreationDate(fileDetails.getCreationDate());
		fileDetailsBean.setIsActive(fileDetails.getIsActive());
		fileDetailsBean.setReportingPeriodStartDate(fileDetails.getReportingPeriodStartDate());
		fileDetailsBean.setReportingPeriodEndDate(fileDetails.getReportingPeriodEndDate());
		fileDetailsBean.setUserName(fileDetails.getUserName());
		fileDetailsBean.setReturnCode(fileDetails.getReturnCode());
		fileDetailsBean.setEntityCode(fileDetails.getEntityCode());
		fileDetailsBean.setUploadChannelIdFk(fileDetails.getUploadChannelIdFk());
		fileDetailsBean.setNillabelComments(fileDetails.getNillabelComments());
		//		fileDetailsBean.setFilingStatus(fileDetails.getFilingStatus());
		fileDetailsBean.setFilingStatusId(fileDetails.getFilingStatusId());
		fileDetailsBean.setOldReturnCode(fileDetails.getOldReturnCode());
		fileDetailsBean.setUserMaster(fileDetails.getUserMaster());
		fileDetailsBean.setBsr7FileType(fileDetails.getBsr7FileType());
		fileDetailsBean.setIfscCode(fileDetails.getIfscCode());
		fileDetailsBean.setNillable(fileDetails.isNillable());
		fileDetailsBean.setEmailId(fileDetails.getEmailId());
		fileDetailsBean.setIfscCode(fileDetails.getIfscCode());
		fileDetailsBean.setSupportiveDocName(fileDetails.getSupportiveDocName());
		fileDetailsBean.setSupportiveDocType(fileDetails.getSupportiveDocType());
		fileDetailsBean.setReturnId(fileDetails.getReturnId());
		fileDetailsBean.setReturnName(fileDetails.getReturnName());
		fileDetailsBean.setEntityName(fileDetails.getEntityName());
		fileDetailsBean.setEntityId(fileDetails.getEntityId());
		fileDetailsBean.setFinYearFormatId(fileDetails.getFinYearFormatId());
		fileDetailsBean.setChannelId(fileDetails.getChannelId());
		fileDetailsBean.setRoleId(fileDetails.getRoleId());
		fileDetailsBean.setFileCopyingStartTimeInLong(fileDetails.getFileCopyingStartTimeInLong());
		fileDetailsBean.setFileCopyingEndTimeInLong(fileDetails.getFileCopyingEndTimeInLong());

		if (fileDetails.getFileName() != null) {
			String filePath = ResourceUtil.getKeyValue(ROOT_PATH) + File.separator + ResourceUtil.getKeyValue(DEST_PATH) + File.separator + fileDetails.getFileName();
			fileDetailsBean.setFilePath(filePath);
		}

		if (fileDetails.getSupportiveDocName() != null) {
			String attachementFlilePath = ResourceUtil.getKeyValue(ROOT_PATH) + File.separator + ResourceUtil.getKeyValue(DEST_PATH) + File.separator + fileDetails.getSupportiveDocName();
			fileDetailsBean.setSupportiveDocFilePath(attachementFlilePath);
		}

		if (fileDetails.getUserMaster() != null) {
			fileDetailsBean.setUserId(fileDetails.getUserMaster().getUserId());
		}

		if (fileDetails.getReportingPeriodEndDate() != null) {
			fileDetailsBean.setReportingPeriodEndDateInString(DateManip.convertDateToString(fileDetails.getReportingPeriodEndDate(), "ddMMyyyy"));
		}

		return fileDetailsBean;
	}

	public void makeSchedulerStopEntry(int successfullyProcessedRecord, Long schedulerLogId, Long schedulerId, String jobProcessingId) {
		try {
			if (schedulerLogId != null) {
				SchedulerLog schedulerLog = new SchedulerLog();
				schedulerLog.setSuccessfullyProcessedCount(Long.valueOf(successfullyProcessedRecord));
				schedulerLog.setId(schedulerLogId);
				Scheduler scheduler = new Scheduler();
				scheduler.setSchedulerId(schedulerId);
				scheduler.setIsRunning(false);
				schedulerLog.setSchedulerIdFk(scheduler);
				schedulerLog.setSchedulerStoppedTimeInLong(new Date().getTime());
				CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

				WebServiceComponentUrl componentUrl = getWebServiceComponentURL(GeneralConstants.ADD_UPDATE_SCHEDULER_LOG.getConstantVal(), CIMSRestWebserviceClient.HTTP_METHOD_TYPE_POST);

				Map<String, String> headerMap = new HashMap<>();
				headerMap.put(GeneralConstants.JOB_PROCESSING_ID.getConstantVal(), jobProcessingId);

				String responsestring = restServiceClient.callRestWebServiceWithMultipleHeader(componentUrl, schedulerLog, null, headerMap);

				ServiceResponse serviceResponse = JsonUtility.getGsonObject().fromJson(responsestring, ServiceResponse.class);

				if (!serviceResponse.isStatus()) {
					LOGGER.info("Error while stopping scheduler -> Reason : Schduler is alrady stopped ");
				} else {
					LOGGER.info("Scheduler stopped successfully ");
				}
			} else {
				LOGGER.error("Scheduler Log Id not found");
			}
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}
	}

	public Long makeSchedulerStartEntry(Long totalRecordCount, Long schedulerId, String jobProcessingId) {
		try {
			SchedulerLog schedulerLog = new SchedulerLog();
			schedulerLog.setTakedRecordsCount(totalRecordCount);
			Scheduler scheduler = new Scheduler();
			scheduler.setSchedulerId(schedulerId);
			scheduler.setIsRunning(true);
			schedulerLog.setSchedulerIdFk(scheduler);
			schedulerLog.setSchedulerStartedTimeInLong(new Date().getTime());
			CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

			WebServiceComponentUrl componentUrl = getWebServiceComponentURL(GeneralConstants.ADD_UPDATE_SCHEDULER_LOG.getConstantVal(), CIMSRestWebserviceClient.HTTP_METHOD_TYPE_POST);

			Map<String, String> headerMap = new HashMap<>();
			headerMap.put(GeneralConstants.JOB_PROCESSING_ID.getConstantVal(), jobProcessingId);

			String responsestring = restServiceClient.callRestWebServiceWithMultipleHeader(componentUrl, schedulerLog, null, headerMap);

			Type listToken = new TypeToken<ServiceResponse>() {
			}.getType();
			ServiceResponse serviceResponse = JsonUtility.getGsonObject().fromJson(responsestring, listToken);
			Long schedulerLogId = null;

			if (!serviceResponse.isStatus()) {
				LOGGER.error("False status received from API with status message " + serviceResponse.getStatusCode());
				return null;
			} else {
				listToken = new TypeToken<SchedulerLog>() {
				}.getType();
				SchedulerLog schedulerLog1 = JsonUtility.getGsonObject().fromJson(serviceResponse.getResponse() + "", listToken);
				schedulerLogId = schedulerLog1.getId();
			}
			return schedulerLogId;
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}
		return null;
	}

	public Scheduler getSchedulerStatus(String jobProcessingId, String schedulerCode) {
		try {
			CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

			Map<String, String> headerMap = new HashMap<>();
			headerMap.put(GeneralConstants.JOB_PROCESSING_ID.getConstantVal(), jobProcessingId);
			headerMap.put(GeneralConstants.SCHEDULER_CODE.getConstantVal(), schedulerCode);

			WebServiceComponentUrl componentUrl = getWebServiceComponentURL(GeneralConstants.GET_ACTIVE_SCHEDULER_STATUS_BY_CODE.getConstantVal(), CIMSRestWebserviceClient.HTTP_METHOD_TYPE_GET);
			String responsestring = restServiceClient.callRestWebServiceWithMultipleHeader(componentUrl, null, null, headerMap);

			Type listToken = new TypeToken<ServiceResponse>() {
			}.getType();
			ServiceResponse serviceResponse = JsonUtility.getGsonObject().fromJson(responsestring, listToken);

			if (!serviceResponse.isStatus()) {
				LOGGER.error("False status received from API with status message " + serviceResponse.getStatusCode());
				return null;
			} else {
				if (serviceResponse.getResponse() != null) {
					listToken = new TypeToken<Scheduler>() {
					}.getType();
					return JsonUtility.getGsonObject().fromJson(serviceResponse.getResponse() + "", listToken);
				} else {
					LOGGER.error("response object not present in the response string, response received from API : {}" + responsestring);
				}
			}
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}
		return null;
	}

	private void moveFileInstanceFileAndAttachementFileToFailedFolder(FileDetails fileDetailsBean) {
		String uploadInstanceSrcFilePath = fileDetailsBean.getFilePath();
		String uploadInstanceDestFilePath = ResourceUtil.getKeyValue(ROOT_PATH) + File.separator + ResourceUtil.getKeyValue(VALIDATE_META_PATH) + File.separator + fileDetailsBean.getFileName();

		// If file already present in VALIDATE_META_FAILED folder delete it
		File uploadInstanceSrcFile = new File(uploadInstanceSrcFilePath);
		File uploadInstanceDestFile = new File(uploadInstanceDestFilePath);

		if (uploadInstanceSrcFile.exists()) {
			deleteFileFromLocation(uploadInstanceDestFile.getPath());
			FileManager.moveFile(uploadInstanceSrcFile, uploadInstanceDestFile);
		} else {
			LOGGER.error("upload instance src file not exist {}", uploadInstanceSrcFilePath);
		}

		if (fileDetailsBean.getSupportiveDocName() != null && !fileDetailsBean.getSupportiveDocName().equals("")) {

			String uploadAttchementSrcFilePath = fileDetailsBean.getSupportiveDocFilePath();

			String uploadAttchementDestFilePath = ResourceUtil.getKeyValue(ROOT_PATH) + File.separator + ResourceUtil.getKeyValue(META_DATA_FAILED_PATH) + File.separator + fileDetailsBean.getSupportiveDocName();

			File uploadAttchementSrcFile = new File(uploadAttchementSrcFilePath);
			File uploadAttchementDestFile = new File(uploadAttchementDestFilePath);

			if (uploadAttchementSrcFile.exists()) {
				deleteFileFromLocation(uploadAttchementDestFile.getPath());
				FileManager.moveFile(uploadAttchementSrcFile, uploadAttchementDestFile);
			} else {
				LOGGER.error("upload attachement src file not exist {}", uploadAttchementSrcFilePath);
			}
		}
	}

	private boolean performBasicValidation(FileDetails fileDetailsBean, Return returnObj, EntityBean entityBean, Map<String, Map<Boolean, List<String>>> fieldCheckListMap, UserMaster userMaster) {
		boolean isValidationFailed = false;
		if (returnObj != null) {
			fileDetailsBean.setReturnId(returnObj.getReturnId());
			fileDetailsBean.setReturnName(returnObj.getReturnName());
			fileDetailsBean.setOldReturnCode(returnObj.getOldReturnCode());
			insertValueIntoStatusMap(MetaDataCheckConstants.RETURN_CODE_CHECK.getConstantVal(), true, null, fieldCheckListMap);
		} else {
			isValidationFailed = true;
			fileDetailsBean.setReturnCode(null);
			insertValueIntoStatusMap(MetaDataCheckConstants.RETURN_CODE_CHECK.getConstantVal(), false, ErrorCode.E0634.toString(), fieldCheckListMap);
		}

		if (entityBean != null) {
			fileDetailsBean.setEntityId(entityBean.getEntityId());
			fileDetailsBean.setEntityName(entityBean.getEntityName());
			fileDetailsBean.setEntityCode(entityBean.getEntityCode());
			fileDetailsBean.setIfscCode(entityBean.getIfscCode());
			fileDetailsBean.setFinYearFormatId(entityBean.getFinYrFormat().getFinYrFormatId());
			insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_CHECK.getConstantVal(), true, null, fieldCheckListMap);
		} else {
			isValidationFailed = true;
			insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_CHECK.getConstantVal(), false, ErrorCode.E0330.toString(), fieldCheckListMap);
		}

		if (StringUtils.isEmpty(userMaster)) {
			insertValueIntoStatusMap(MetaDataCheckConstants.USER_CHECK.getConstantVal(), false, ErrorCode.E0638.toString(), fieldCheckListMap);
		} else {
			fileDetailsBean.setUserMaster(userMaster);
			fileDetailsBean.setUserId(userMaster.getUserId());
			fileDetailsBean.setUserName(userMaster.getUserName());
			insertValueIntoStatusMap(MetaDataCheckConstants.USER_CHECK.getConstantVal(), true, null, fieldCheckListMap);
		}

		fileDetailsBean.setFileType(fileDetailsBean.getFileType());
		return isValidationFailed;
	}

	public boolean isAllowedToUploadFilling(RetUploadDetBean retUploadDetBean, Map<String, Map<Boolean, List<String>>> fieldCheckListMap, Return returnObj) throws ParseException {

		List<ReturnsUploadDetails> returnUploadDetailsList = fileUploadController.chkExistingUploadInfo(retUploadDetBean);

		if (!CollectionUtils.isEmpty(returnUploadDetailsList)) {
			ReturnsUploadDetails returnsUploadDetails = returnUploadDetailsList.get(0);
			int fillingStatus = returnsUploadDetails.getFilingStatus().getFilingStatusId();
			boolean isWorkflowComplete = true;

			if (!CollectionUtils.isEmpty(returnsUploadDetails.getReturnApprovalDetailsList())) {
				isWorkflowComplete = returnsUploadDetails.getReturnApprovalDetailsList().get(0).isComplete();
			}

			if (fillingStatus == GeneralConstants.UPLOADED.getConstantIntVal() || fillingStatus == GeneralConstants.UPLOAD_FILE_PROCESSING_ID.getConstantIntVal() || fillingStatus == GeneralConstants.BUSINESS_VALIDATION_SUCCESS.getConstantIntVal() || fillingStatus == GeneralConstants.CUSTOM_VALIDATION_PENDING.getConstantIntVal() || fillingStatus == GeneralConstants.CUSTOM_VALIDATION_SUCCESSFUL.getConstantIntVal() || !isWorkflowComplete) {
				insertValueIntoStatusMap(MetaDataCheckConstants.ALLOWED_TO_SUBMIT_CHECK.getConstantVal(), false, ErrorCode.E0726.toString(), fieldCheckListMap);
				return false;
			} else {
				// Start Revision Request Logic
				return isRequiredToRaiseRevisionRequest(returnUploadDetailsList, returnObj, fieldCheckListMap, retUploadDetBean);
			}
		} else {

			ServiceResponse serviceResponse = fileUploadController.verifyFillingWindow(UUID.randomUUID().toString(), retUploadDetBean);
			if (serviceResponse.isStatus()) {
				FilingCalendarDto filingCalendarDto = (FilingCalendarDto) serviceResponse.getResponse();
				UploadFilingConstants fillingWindowStatus = filingCalendarDto.getFilingStatus();
				if (fillingWindowStatus != null) {
					if (fillingWindowStatus == UploadFilingConstants.FILING_WINDOW_OPEN) {
						insertValueIntoStatusMap(MetaDataCheckConstants.ALLOWED_TO_SUBMIT_CHECK.getConstantVal(), true, GeneralConstants.FILING_WINDOW_OPENED.getConstantVal(), fieldCheckListMap);
						return true;
					} else if (fillingWindowStatus == UploadFilingConstants.FILING_WINDOW_CLOSED) {
						return checkUnlockRequestStatus(retUploadDetBean, fieldCheckListMap);
					} else if (fillingWindowStatus == UploadFilingConstants.FILING_WINDOW_NOT_YET_OPEN) {
						insertValueIntoStatusMap(MetaDataCheckConstants.ALLOWED_TO_SUBMIT_CHECK.getConstantVal(), false, ErrorCode.E0737.toString(), fieldCheckListMap);
						return false;
					} else {
						insertValueIntoStatusMap(MetaDataCheckConstants.ALLOWED_TO_SUBMIT_CHECK.getConstantVal(), false, ErrorCode.E0738.toString(), fieldCheckListMap);
						return false;
					}
				} else {
					insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0790.toString(), fieldCheckListMap);
					return false;
				}
			} else {
				insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0790.toString(), fieldCheckListMap);
				return false;
			}
		}
	}

	private boolean isRequiredToRaiseRevisionRequest(List<ReturnsUploadDetails> returnUploadDetailsList, Return returnObj, Map<String, Map<Boolean, List<String>>> fieldCheckListMap, RetUploadDetBean retUploadDetBean) {
		try {

			Map<Integer, Integer> countMap = getReturnCountList(returnUploadDetailsList);
			Integer succesfullySubmittedCount = 0;
			if (countMap.get(0) != null) {
				succesfullySubmittedCount = countMap.get(0);
			}
			if (returnObj.getReturnPropertyIdFk() != null) { // Return Has Property
				Integer auditedOrFinalCount = 0;
				Integer unAuditedOrProvisionalCount = 0;
				boolean isAuditedOrFinalFillingPresent = false;

				if (returnObj.getReturnPropertyIdFk().getReturnProprtyId().equals(GeneralConstants.AUDITED_UNAUDITED_PROPERTY_ID.getConstantIntVal())) {
					if (countMap.get(ReturnPropertyVal.getReturnPropertyIdByReturnPropertyValCode(GeneralConstants.AUDITED_PROPERTY_CODE.getConstantVal())) != null) {
						isAuditedOrFinalFillingPresent = true;
						// substracted 1 from count because first audited or unaudited will not be
						// considered for count
						auditedOrFinalCount = countMap.get(ReturnPropertyVal.getReturnPropertyIdByReturnPropertyValCode(GeneralConstants.AUDITED_PROPERTY_CODE.getConstantVal())) - 1;
					}
					if (countMap.get(ReturnPropertyVal.getReturnPropertyIdByReturnPropertyValCode(GeneralConstants.UNAUDITED_PROPERTY_CODE.getConstantVal())) != null) {
						unAuditedOrProvisionalCount = countMap.get(ReturnPropertyVal.getReturnPropertyIdByReturnPropertyValCode(GeneralConstants.UNAUDITED_PROPERTY_CODE.getConstantVal())) - 1;
					}
				} else {
					if (countMap.get(ReturnPropertyVal.getReturnPropertyIdByReturnPropertyValCode(GeneralConstants.FINAL_PROPERTY_CODE.getConstantVal())) != null) {
						isAuditedOrFinalFillingPresent = true;
						auditedOrFinalCount = countMap.get(ReturnPropertyVal.getReturnPropertyIdByReturnPropertyValCode(GeneralConstants.FINAL_PROPERTY_CODE.getConstantVal())) - 1;
					}
					if (countMap.get(GeneralConstants.PROVISIONAL_FINAL_PROPERTY_ID.getConstantIntVal()) != null) {
						unAuditedOrProvisionalCount = countMap.get(GeneralConstants.PROVISIONAL_FINAL_PROPERTY_ID.getConstantIntVal()) - 1;
					}
				}
				if (retUploadDetBean.getReturnPropertyValId().equals(ReturnPropertyVal.getReturnPropertyIdByReturnPropertyValCode(GeneralConstants.PROVISION_PROPERTY_CODE.getConstantVal())) || retUploadDetBean.getReturnPropertyValId().equals(ReturnPropertyVal.getReturnPropertyIdByReturnPropertyValCode(GeneralConstants.UNAUDITED_PROPERTY_CODE.getConstantVal()))) {
					if (isAuditedOrFinalFillingPresent) {
						insertValueIntoStatusMap(MetaDataCheckConstants.ALLOWED_TO_SUBMIT_CHECK.getConstantVal(), false, ErrorCode.E0751.toString(), fieldCheckListMap);
						return false;
					}
				} else { // Auditd or Final
					// No record found for audited
					if (auditedOrFinalCount + unAuditedOrProvisionalCount < returnObj.getMaxRevisionCount()) {
						//						retUploadDetBean.setPrevUploadId(returnUploadDetailsList.get(0).getUploadId());
						insertValueIntoStatusMap(MetaDataCheckConstants.ALLOWED_TO_SUBMIT_CHECK.getConstantVal(), true, GeneralConstants.REVISION_COUNT_NOT_EXCEEDED.getConstantVal(), fieldCheckListMap);
						return true;
					} else {
						if (isAuditedOrFinalFillingPresent) {
							if (checkRevisionRequestStatus(retUploadDetBean, fieldCheckListMap)) {
								retUploadDetBean.setPrevUploadId(returnUploadDetailsList.get(0).getUploadId());
								return true;
							} else {
								return false;
							}
						} else {
							insertValueIntoStatusMap(MetaDataCheckConstants.ALLOWED_TO_SUBMIT_CHECK.getConstantVal(), true, GeneralConstants.REVISION_COUNT_NOT_EXCEEDED.getConstantVal(), fieldCheckListMap);
							return true;
						}
					}
				}
			}

			// Return Does not have property
			if (succesfullySubmittedCount - 1 < returnObj.getMaxRevisionCount()) {
				//				retUploadDetBean.setPrevUploadId(returnUploadDetailsList.get(0).getUploadId());
				// User can allowed to submit filling
				insertValueIntoStatusMap(MetaDataCheckConstants.ALLOWED_TO_SUBMIT_CHECK.getConstantVal(), true, GeneralConstants.REVISION_COUNT_NOT_EXCEEDED.getConstantVal(), fieldCheckListMap);
				return true;
			} else {
				if (checkRevisionRequestStatus(retUploadDetBean, fieldCheckListMap)) {
					retUploadDetBean.setPrevUploadId(returnUploadDetailsList.get(0).getUploadId());
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception :", e);
			insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0786.toString(), fieldCheckListMap);
		}
		return false;
	}

	private Map<Integer, Integer> getReturnCountList(List<ReturnsUploadDetails> returnUploadDetailsList) {
		Map<Integer, Integer> countMap = new HashMap<>();
		Integer count = 0;
		Integer totalCount = 1;
		Integer allCountProperty = 0;

		for (ReturnsUploadDetails returnsUploadDetails : returnUploadDetailsList) {
			if (!CollectionUtils.isEmpty(returnsUploadDetails.getReturnApprovalDetailsList()) && returnsUploadDetails.getReturnApprovalDetailsList().get(0).isComplete()) {
				if (!StringUtils.isEmpty(returnsUploadDetails.getReturnPropertyValue())) {
					if (countMap.containsKey(returnsUploadDetails.getReturnPropertyValue().getReturnProprtyValId())) {
						count = countMap.get(returnsUploadDetails.getReturnPropertyValue().getReturnProprtyValId()) + 1;
						countMap.put(returnsUploadDetails.getReturnPropertyValue().getReturnProprtyValId(), count);
					} else {
						countMap.put(returnsUploadDetails.getReturnPropertyValue().getReturnProprtyValId(), 1);
					}
				}
				countMap.put(allCountProperty, totalCount++);
			}
		}

		return countMap;
	}

	private boolean checkRevisionRequestStatus(RetUploadDetBean retUploadDetBean, Map<String, Map<Boolean, List<String>>> fieldCheckListMap) throws ParseException {
		RevisionRequest revisionRequest = fileUploadController.getRevisionRequestObj(retUploadDetBean);

		if (revisionRequest != null && revisionRequest.getAdminStatusIdFk() != null && revisionRequest.getRevisionStatus() != null) {
			if (CollectionUtils.isEmpty(revisionRequest.getReturnsUploadDetailsList())) {
				if (revisionRequest.getAdminStatusIdFk() != null && revisionRequest.getAdminStatusIdFk().intValue() == GeneralConstants.REQUEST_PENDING_FOR_APPOVAL.getConstantIntVal()) {
					insertValueIntoStatusMap(MetaDataCheckConstants.ALLOWED_TO_SUBMIT_CHECK.getConstantVal(), false, ErrorCode.E0752.toString(), fieldCheckListMap);
					return false;
				} else {
					if (revisionRequest.getRevisionStatus() != null && revisionRequest.getRevisionStatus().equals(GeneralConstants.CLOSED.getConstantVal())) {
						insertValueIntoStatusMap(MetaDataCheckConstants.ALLOWED_TO_SUBMIT_CHECK.getConstantVal(), false, ErrorCode.E0627.toString(), fieldCheckListMap);
						return false;
					} else if (revisionRequest.getRevisionStatus() != null && revisionRequest.getRevisionStatus().equals(GeneralConstants.EXPIRED.getConstantVal())) {
						insertValueIntoStatusMap(MetaDataCheckConstants.ALLOWED_TO_SUBMIT_CHECK.getConstantVal(), false, ErrorCode.E0628.toString(), fieldCheckListMap);
						return false;
					} else {
						if (revisionRequest.getMaxRevisionReqDate() != null) {
							String currentDate = DateManip.getCurrentDate(DateConstants.DD_MM_YYYY.getDateConstants());
							// date calculation
							if (DateManip.convertStringToDate(currentDate, DateConstants.DD_MM_YYYY.getDateConstants()).compareTo(revisionRequest.getMaxRevisionReqDate()) <= 0) {
								insertValueIntoStatusMap(MetaDataCheckConstants.ALLOWED_TO_SUBMIT_CHECK.getConstantVal(), true, GeneralConstants.REVISION_REQUEST_DATE_NOT_CROSSED.getConstantVal(), fieldCheckListMap);
								retUploadDetBean.setRevisionReqId(revisionRequest.getRevisionRequestId());
								return true;
							} else {
								revisionRequest.setRevisionStatus(GeneralConstants.EXPIRED.getConstantVal());
								revisionRequestService.update(revisionRequest);
								insertValueIntoStatusMap(MetaDataCheckConstants.ALLOWED_TO_SUBMIT_CHECK.getConstantVal(), false, ErrorCode.E0628.toString(), fieldCheckListMap);
								return false;
							}
						} else {
							insertValueIntoStatusMap(MetaDataCheckConstants.ALLOWED_TO_SUBMIT_CHECK.getConstantVal(), false, ErrorCode.E0753.toString(), fieldCheckListMap);
							return false;
						}
					}
				}
			} else {
				insertValueIntoStatusMap(MetaDataCheckConstants.ALLOWED_TO_SUBMIT_CHECK.getConstantVal(), false, ErrorCode.E0627.toString(), fieldCheckListMap);
				return false;
			}
		} else {
			insertValueIntoStatusMap(MetaDataCheckConstants.ALLOWED_TO_SUBMIT_CHECK.getConstantVal(), false, ErrorCode.E0625.toString(), fieldCheckListMap);
			return false;
		}
	}

	private boolean checkUnlockRequestStatus(RetUploadDetBean retUploadDetBean, Map<String, Map<Boolean, List<String>>> fieldCheckListMap) throws ParseException {
		UnlockingRequest unlockingRequest = fileUploadController.getUnlockRequestObj(retUploadDetBean);

		if (unlockingRequest != null && unlockingRequest.getAdminStatusIdFk() != null && unlockingRequest.getUnlockStatus() != null) {
			if (CollectionUtils.isEmpty(unlockingRequest.getReturnsUploadDetailsList())) {
				if (unlockingRequest.getAdminStatusIdFk() != null && unlockingRequest.getAdminStatusIdFk().intValue() == GeneralConstants.REQUEST_PENDING_FOR_APPOVAL.getConstantIntVal()) {
					insertValueIntoStatusMap(MetaDataCheckConstants.ALLOWED_TO_SUBMIT_CHECK.getConstantVal(), false, ErrorCode.E0754.toString(), fieldCheckListMap);
					return false;
				} else {
					if (unlockingRequest.getUnlockStatus() != null && unlockingRequest.getUnlockStatus().equals(GeneralConstants.CLOSED.getConstantVal())) {
						insertValueIntoStatusMap(MetaDataCheckConstants.ALLOWED_TO_SUBMIT_CHECK.getConstantVal(), false, ErrorCode.E0630.toString(), fieldCheckListMap);
						return false;
					} else if (unlockingRequest.getUnlockStatus() != null && unlockingRequest.getUnlockStatus().equals(GeneralConstants.EXPIRED.getConstantVal())) {
						insertValueIntoStatusMap(MetaDataCheckConstants.ALLOWED_TO_SUBMIT_CHECK.getConstantVal(), false, ErrorCode.E0631.toString(), fieldCheckListMap);
						return false;
					} else {
						if (unlockingRequest.getMaxUnlockReqDate() != null) {
							String currentDate = DateManip.getCurrentDate(DateConstants.DD_MM_YYYY.getDateConstants());
							if (DateManip.convertStringToDate(currentDate, DateConstants.DD_MM_YYYY.getDateConstants()).compareTo(unlockingRequest.getMaxUnlockReqDate()) <= 0) {
								retUploadDetBean.setUnlockReqId(unlockingRequest.getUnlockingReqId());
								// If current date less than or equal to unlockReq Date
								insertValueIntoStatusMap(MetaDataCheckConstants.ALLOWED_TO_SUBMIT_CHECK.getConstantVal(), true, GeneralConstants.UNLOCK_REQUEST_DATE_NOT_CROSSED.getConstantVal(), fieldCheckListMap);
								return true;
							} else {
								unlockingRequest.setUnlockStatus(GeneralConstants.EXPIRED.getConstantVal());
								unlockRequestService.update(unlockingRequest);
								insertValueIntoStatusMap(MetaDataCheckConstants.ALLOWED_TO_SUBMIT_CHECK.getConstantVal(), false, ErrorCode.E0631.toString(), fieldCheckListMap);
								return false;
							}
						} else {
							unlockRequestService.update(unlockingRequest);
							insertValueIntoStatusMap(MetaDataCheckConstants.ALLOWED_TO_SUBMIT_CHECK.getConstantVal(), false, ErrorCode.E0755.toString(), fieldCheckListMap);
							return false;
						}
					}
				}
			} else {
				insertValueIntoStatusMap(MetaDataCheckConstants.ALLOWED_TO_SUBMIT_CHECK.getConstantVal(), false, ErrorCode.E0630.toString(), fieldCheckListMap);
				return false;
			}
		} else {
			insertValueIntoStatusMap(MetaDataCheckConstants.ALLOWED_TO_SUBMIT_CHECK.getConstantVal(), false, ErrorCode.E0629.toString(), fieldCheckListMap);
			return false;
		}
	}

	private boolean isWorkflowMapped(FileDetails fileDetailsBean) {
		List<Long> returnIds = new ArrayList<>();
		returnIds.add(fileDetailsBean.getReturnId());

		WorkflowReturnMappingInfo workflowReturnMapping = new WorkflowReturnMappingInfo();
		workflowReturnMapping.setChannelIdFk(fileDetailsBean.getUploadChannelIdFk());

		Return returnBean = new Return();
		returnBean.setReturnId(fileDetailsBean.getReturnId());

		workflowReturnMapping.setReturnIdFk(returnBean);

		ServiceResponse serviceResponse = workFlowController.getWorkflowDetailsByReturnId(UUID.randomUUID().toString(), workflowReturnMapping);
		if (serviceResponse.isStatus()) {
			@SuppressWarnings("unchecked")
			List<WorkflowReturnMapping> workflowList = (List<WorkflowReturnMapping>) serviceResponse.getResponse();
			if (!CollectionUtils.isEmpty(workflowList)) {
				fileDetailsBean.setWorkflowId(workflowList.get(0).getWorkFlowMaster().getWorkflowId());
				fileDetailsBean.setWorkflowStepNo(1);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private ServiceResponse insertDataIntoUploadTableAndSendEmail(RetUploadDetBean retUploadDetBean, Return returnObj, EntityBean entityBean, FileDetails fileDetailsBean, String destinationUploadFilePath, String destinationAttachementFilePath, Map<String, Map<Boolean, List<String>>> fieldCheckListMap) {

		//		Step 9 : Insert data into TBL_RET_UPLOAD_DETAILS				
		Long uploadId = fileUploadController.insertData(retUploadDetBean, returnObj, entityBean, fileDetailsBean, fieldCheckListMap);

		if (!StringUtils.isEmpty(uploadId)) {
			if (!fileDetailsBean.getUploadChannelIdFk().getUploadChannelId().equals(GeneralConstants.WEB_CHANNEL.getConstantLongVal()) && !fileDetailsBean.getUploadChannelIdFk().getUploadChannelId().equals(GeneralConstants.UPLOAD_CHANNEL.getConstantLongVal())) {
				if (retUploadDetBean.getInstanceFilePath() != null && deleteFileFromLocation(retUploadDetBean.getInstanceFilePath())) {
					LOGGER.info("Upload File succefully deleted from src folder as a successfull activity");
				}
				if (retUploadDetBean.getSupportiveDocFilePath() != null && deleteFileFromLocation(retUploadDetBean.getSupportiveDocFilePath())) {
					LOGGER.info("attachement File succefully deleted from src folder as a successfull activity");
				}
			}

			if (fileDetailsBean.getFileType().equalsIgnoreCase(GeneralConstants.JSON.getConstantVal()) || fileDetailsBean.getFileType().equalsIgnoreCase(GeneralConstants.PDF.getConstantVal())) {
				// Code for set Business validation success when data submitted through dynamic
				// form
				boolean saveEtlFilesFlag = saveFilesForEtl(uploadId, destinationUploadFilePath);
				if (!saveEtlFilesFlag) {
					return updateFailedStatusAndReturnResponse(fileDetailsBean, GeneralConstants.FILE_STATUS_META_DATA_FAILED_ID.getConstantIntVal(), fieldCheckListMap);
				}
				List<ReturnUploadNBusinessData> returnUploadResultDetails = new ArrayList<>();
				ReturnUploadNBusinessData returnUploadNBusinessData = new ReturnUploadNBusinessData();
				returnUploadNBusinessData.setUploadId(uploadId);
				returnUploadNBusinessData.setFillingStatusId(GeneralConstants.BUSINESS_VALIDATION_SUCCESS.getConstantIntVal());
				returnUploadNBusinessData.setEtlFolderPath(new File(destinationUploadFilePath).getParentFile().getAbsolutePath() + File.separator + GeneralConstants.FILE_TO_PUSH.getConstantVal());
				returnUploadResultDetails.add(returnUploadNBusinessData);
				ServiceResponse serviceResponse = returnUploadResultProcessorController.updateReturnUploadNBusinessResultData(null, UUID.randomUUID().toString(), returnUploadResultDetails);
				if (!serviceResponse.isStatus()) {
					LOGGER.error("Exception : " + ErrorConstants.ERROR_UPDATING_BUSINESS_VALIDATION_STATUS.getConstantVal());
				}
				return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.SUCCESS.getConstantVal()).setStatusMessage(GeneralConstants.FILLING_SUBMITTED_SUCCESSFULLY.getConstantVal()).setResponse(uploadId).build();
			} else {
				return new ServiceResponse.ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.SUCCESS.getConstantVal()).setStatusMessage(GeneralConstants.FILLING_SUBMITTED_SUCCESSFULLY.getConstantVal()).setResponse(uploadId).build();
			}
		} else {
			//			Start rollback
			LOGGER.debug("Returns upload DB error");
			if (destinationUploadFilePath != null && deleteFileFromLocation(destinationUploadFilePath)) {
				LOGGER.info("Uploaded file successfully deleted as a rolllback activity");
			}

			if (destinationAttachementFilePath != null && deleteFileFromLocation(destinationAttachementFilePath)) {
				LOGGER.info("Attachement file successfully deleted as a rolllback activity");
			}
			return updateFailedStatusAndReturnResponse(fileDetailsBean, GeneralConstants.FILE_STATUS_META_DATA_FAILED_ID.getConstantIntVal(), fieldCheckListMap);
		}
	}

	boolean deleteFileFromLocation(String path) {
		File file = new File(path);
		return file.delete();
	}

	private RetUploadDetBean getReturnUploadDetBean(FileDetails fileDetailsBean, Frequency frequency) {
		RetUploadDetBean retUploadDetBean = new RetUploadDetBean();
		retUploadDetBean.setNillable(fileDetailsBean.isNillable());
		retUploadDetBean.setUploadedByUserId(fileDetailsBean.getUserId());
		retUploadDetBean.setEntityId(fileDetailsBean.getEntityId());
		retUploadDetBean.setEntityCode(fileDetailsBean.getEntityCode());
		retUploadDetBean.setReturnId(fileDetailsBean.getReturnId());
		retUploadDetBean.setReturnName(fileDetailsBean.getReturnName());
		retUploadDetBean.setReturnCode(fileDetailsBean.getReturnCode());
		retUploadDetBean.setUploadedUserRoleId(fileDetailsBean.getRoleId());

		retUploadDetBean.setInstanceFilePath(fileDetailsBean.getFilePath());
		retUploadDetBean.setModifiedInstanceFileName(fileDetailsBean.getFileName());
		retUploadDetBean.setInstanceFileType(fileDetailsBean.getFileType());

		retUploadDetBean.setSupportiveDocFilePath(fileDetailsBean.getSupportiveDocFilePath());
		retUploadDetBean.setSupportiveDocFileName(fileDetailsBean.getSupportiveDocName());
		retUploadDetBean.setSupportiveDocFileType(fileDetailsBean.getSupportiveDocType());

		retUploadDetBean.setNillableComments(fileDetailsBean.getNillabelComments());

		String year = DateManip.convertDateToString(fileDetailsBean.getReportingPeriodEndDate(), DateConstants.YYYY.getDateConstants());
		String month = DateManip.convertDateToString(fileDetailsBean.getReportingPeriodEndDate(), DateConstants.MM.getDateConstants());

		retUploadDetBean.setFinancialMonth(month);
		retUploadDetBean.setFinancialYear(year);
		retUploadDetBean.setFrequencyId(frequency.getFrequencyId());
		retUploadDetBean.setFinYearFormatId(fileDetailsBean.getFinYearFormatId());
		retUploadDetBean.setFormFreqName(frequency.getFrequencyName());
		retUploadDetBean.setStartDate(DateManip.convertDateToString(fileDetailsBean.getReportingPeriodStartDate(), DateConstants.DD_MM_YYYY.getDateConstants()));
		retUploadDetBean.setEndDate(DateManip.convertDateToString(fileDetailsBean.getReportingPeriodEndDate(), DateConstants.DD_MM_YYYY.getDateConstants()));

		retUploadDetBean.setWorkFlowId(fileDetailsBean.getWorkflowId());
		retUploadDetBean.setCurrentWFStep(fileDetailsBean.getWorkflowStepNo());
		retUploadDetBean.setReturnPropertyValId(fileDetailsBean.getReturnPropertyValId());
		return retUploadDetBean;
	}

	private UserRoleDto checkRoleReturnAndRoleActivityValidation(UserMaster userMaster, FileDetails fileDetailsBean, Map<String, Map<Boolean, List<String>>> fieldCheckListMap, String jobProcessingId) {
		try {
			Long returnId = fileDetailsBean.getReturnId();

			if (userMaster != null) {
				UserDto userDto = new UserDto();
				userDto.setUserId(userMaster.getUserId());
				if (fileDetailsBean.getLangCode() == null) {
					userDto.setLangCode(GeneralConstants.ENG_LANG.getConstantVal());
				} else {
					userDto.setLangCode(fileDetailsBean.getLangCode());
				}
				userDto.setIsActive(true);
				UserDetailsDto userDetailsDto = userMasterServiceV2.getUserWithEntityDetails(userDto);

				if (ObjectUtils.isEmpty(userDetailsDto)) {
					insertValueIntoStatusMap(MetaDataCheckConstants.USER_CHECK.getConstantVal(), false, ErrorCode.E0481.toString(), fieldCheckListMap);
					return null;
				} else {
					// Check user is mapped to passed entity or not
					if (userDetailsDto.getRoleTypeId().equals(Integer.parseInt(GeneralConstants.ENTITY_ROLE_TYPE_ID.getConstantLongVal() + ""))) {
						EntityDto entityDto = userDetailsDto.getEntityDtos().stream().filter(f -> f.getEntityId().equals(fileDetailsBean.getEntityId())).findAny().orElse(null);
						if (ObjectUtils.isEmpty(entityDto)) {
							insertValueIntoStatusMap(MetaDataCheckConstants.USER_CHECK.getConstantVal(), false, ErrorCode.E0639.toString(), fieldCheckListMap);
							return null;
						}
					}

					boolean isRetunMapped = checkReturnMappedToRole(jobProcessingId, returnId, userMaster.getUserId());
					boolean isActivityMapped = checkActivityMappedToRole(userMaster.getUserId(), GeneralConstants.UPLOAD_ACTIVITY_ID.getConstantLongVal());

					if (isRetunMapped) {
						insertValueIntoStatusMap(MetaDataCheckConstants.ROLE_RETURN_MAPP_CHECK.getConstantVal(), true, null, fieldCheckListMap);
					} else {
						insertValueIntoStatusMap(MetaDataCheckConstants.ROLE_RETURN_MAPP_CHECK.getConstantVal(), false, ErrorCode.E0785.toString(), fieldCheckListMap);
					}

					if (isActivityMapped) {
						insertValueIntoStatusMap(MetaDataCheckConstants.ROLE_ACTIVITY_MAPP_CHECK.getConstantVal(), true, null, fieldCheckListMap);
					} else {
						insertValueIntoStatusMap(MetaDataCheckConstants.ROLE_ACTIVITY_MAPP_CHECK.getConstantVal(), false, ErrorCode.E0728.toString(), fieldCheckListMap);
					}

					if (isRetunMapped && isActivityMapped) {
						return userDetailsDto.getUserRoleDtos().get(0);
					} else {
						return null;
					}
				}
			}
		} catch (Exception e) {
			insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0728.toString(), fieldCheckListMap);
			LOGGER.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}
		return null;
	}

	@SuppressWarnings("unused")
	private boolean checkReturnMappedToRole(UserRole userRole, Long returnId) {
		if (!StringUtils.isEmpty(userRole)) {
			Map<String, Object> valueMap = new HashMap<>();
			valueMap.put(ColumnConstants.ROLEID.getConstantVal(), userRole.getUserRoleId());

			List<UserRoleReturnMapping> userRoleReturnMappings = userRoleReturnMappingService.getDataByObject(valueMap, MethodConstants.GET_USER_ROLE_RETURN_MAPP_DATA_BY_ROLE_ID.getConstantVal());

			if (CollectionUtils.isEmpty(userRoleReturnMappings)) {
				//Entity+Return mapping present
				return true;
			} else {
				UserRoleReturnMapping userRolMapping = userRoleReturnMappings.stream().filter(f -> f.getReturnIdFk().getReturnId().equals(returnId) && f.getReturnIdFk().getIsActive() && f.getIsActive()).findAny().orElse(null);
				if (userRolMapping != null) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkReturnMappedToRole(String jobProcessingId, Long returnId, Long userId) {
		if (!StringUtils.isEmpty(returnId)) {
			ReturnEntityMapDto returnEntityMapDto = new ReturnEntityMapDto();
			returnEntityMapDto.setUserId(userId);
			returnEntityMapDto.setIsActive(true);

			ServiceResponse serviceResponse = entityMasterControllerV2.getEntityReturnChannelMapp(jobProcessingId, returnEntityMapDto);

			if (serviceResponse != null && serviceResponse.isStatus()) {
				@SuppressWarnings("unchecked")
				List<ReturnEntityOutputDto> returnEntityOutputDtoList = (List<ReturnEntityOutputDto>) serviceResponse.getResponse();

				ReturnEntityOutputDto returnEntityOutputDto = returnEntityOutputDtoList.stream().filter(f -> f.getReturnId().equals(returnId)).findAny().orElse(null);

				if (returnEntityOutputDto != null) {
					return true;
				}
			}
		}
		return false;
	}

	//	private boolean checkActivityMappedToRole(UserRole userRole, Long uploadActivity) {
	//		if (!StringUtils.isEmpty(userRole) && !CollectionUtils.isEmpty(userRole.getUserRoleActivityMapping())) {
	//			UserRoleActivityMap userRoleActivityMap = userRole.getUserRoleActivityMapping().stream().filter(f -> f.getWorkFlowActivity().getActivityId().equals(uploadActivity) && f.getIsActive().equals(true)).findAny().orElse(null);
	//			if (!StringUtils.isEmpty(userRoleActivityMap)) {
	//				return true;
	//			}
	//		}
	//
	//		return false;
	//	}

	private boolean checkActivityMappedToRole(Long userId, Long uploadActivity) {
		if (userId != null && uploadActivity != null) {
			List<UserDto> userDtoList = userRoleService.checkActivityMappedToLoggedInUser(userId, uploadActivity);
			if (!CollectionUtils.isEmpty(userDtoList)) {
				return true;
			}
		}

		return false;
	}

	//	private boolean isReturnMappedToUserRole(List<ReturnGroupMappingDto> returnGroupMappingDtoList, Long returnId) {
	//		for (ReturnGroupMappingDto returnGroupMappingDto : returnGroupMappingDtoList) {
	//			ReturnDto returnDto = returnGroupMappingDto.getReturnList().stream().filter(f -> f.getReturnId().equals(returnId)).findAny().orElse(null);
	//			if(!StringUtils.isEmpty(returnDto)) {
	//				return true;
	//			}
	//		}
	//		return false;
	//	}

	private UserMaster getUserObj(FileDetails fileDetailsBean) {
		try {
			List<UserMaster> userMasterList = null;
			if (fileDetailsBean.getUploadChannelIdFk().getUploadChannelId().equals(GeneralConstants.EMAIL_CHANNEL.getConstantLongVal())) {
				//				Map<String, List<String>> valueMap = new HashMap<>();
				//				List<String> emailList = new ArrayList<>();
				//				emailList.add(fileDetailsBean.getEmailId());
				//				valueMap.put(ColumnConstants.EMAIL_ID.getConstantVal(), emailList);
				//				userMasterList = userMasterService.getDataByColumnValue(valueMap,
				//						MethodConstants.GET_USER_DATA_BY_EMAIL_ID.getConstantVal());

				userMasterList = getUserMasterListForEMailChannel(fileDetailsBean);

			} else {
				Map<String, Object> valueMap = new HashMap<>();
				List<Long> userIdList = new ArrayList<>();
				userIdList.add(fileDetailsBean.getUserId());
				List<Boolean> isActiveList = new ArrayList<>();
				valueMap.put(ColumnConstants.USER_ID.getConstantVal(), userIdList);
				if (fileDetailsBean.getUploadChannelIdFk().getUploadChannelId().equals(GeneralConstants.SYSTEM_CHANNEL.getConstantLongVal())) {
					isActiveList.add(Boolean.TRUE);
					isActiveList.add(Boolean.FALSE);
				} else {
					isActiveList.add(Boolean.TRUE);
				}
				valueMap.put(ColumnConstants.IS_ACTIVE.getConstantVal(), isActiveList);

				userMasterList = userMasterService.getDataByObject(valueMap, MethodConstants.GET_USER_DATA_BY_USER_ID_ACITVE_FLAG.getConstantVal());
			}
			if (userMasterList == null || userMasterList.isEmpty()) {
				LOGGER.error("User not found in the system for user name or email Id: " + fileDetailsBean.getUserName() + ", Email Address is : " + fileDetailsBean.getEmailId());
				return null;
			} else {
				UserMaster userMaster = null;
				if (userMasterList.size() > 1) {
					LOGGER.error("Multiple user found for user name or email ID, User name is : " + fileDetailsBean.getUserName() + ", Email Address is : " + fileDetailsBean.getEmailId());
					userMaster = userMasterList.get(0);
					LOGGER.error("Returning with user ID : " + userMaster.getUserId());
				} else {
					userMaster = userMasterList.get(0);
				}
				return userMaster;
			}
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}
		return null;
	}

	private List<UserMaster> getUserMasterListForEMailChannel(FileDetails fileDetailsBean) {

		List<UserMaster> userMasterList = new ArrayList<>();

		Map<String, List<String>> valueMap = new HashMap<>();
		List<String> emailList = new ArrayList<>();
		List<String> entityCodeList = new ArrayList<>();
		emailList.add(fileDetailsBean.getEmailId());
		entityCodeList.add(fileDetailsBean.getEntityCode());
		valueMap.put(ColumnConstants.EMAIL_ID.getConstantVal(), emailList);
		valueMap.put(ColumnConstants.ENTITY_CODE.getConstantVal(), entityCodeList);
		List<UserEntityRole> userEntityRoleList = userEntityRoleService.getDataByColumnValue(valueMap, MethodConstants.GET_USER_DATA_BY_EMAIL_ID.getConstantVal());

		if (!CollectionUtils.isEmpty(userEntityRoleList)) {
			for (UserEntityRole uer : userEntityRoleList) {
				userMasterList.add(uer.getUserRoleMaster().getUserMaster());
			}
			return userMasterList;
		}

		return null;
	}

	private EntityBean getEntityObj(FileDetails fileDetailsBean) {
		try {
			Map<String, List<String>> columnValueMap = new HashMap<>();
			List<String> valueList = new ArrayList<>();
			List<EntityBean> entityList = null;
			if (fileDetailsBean.getUploadChannelIdFk().getUploadChannelId().equals(GeneralConstants.SYSTEM_CHANNEL.getConstantLongVal())) {
				valueList.add(fileDetailsBean.getIfscCode());
				columnValueMap.put(ColumnConstants.IFSC_CODE.getConstantVal(), valueList);
				entityList = entityService.getDataByColumnValue(columnValueMap, MethodConstants.GET_ENTITY_DATA_BY_IFSC_CODE.getConstantVal());
			} else {
				valueList.add(fileDetailsBean.getEntityCode());
				columnValueMap.put(ColumnConstants.ENTITY_CODE.getConstantVal(), valueList);
				entityList = entityService.getDataByColumnValue(columnValueMap, MethodConstants.GET_ENTITY_DATA_BY_CODE.getConstantVal());
			}

			EntityBean entityBean = null;
			if (!CollectionUtils.isEmpty(entityList)) {
				entityBean = entityList.get(0);
				return entityBean;
			}
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}

		return null;
	}

	private Return getReturnObject(String returnCode) {
		try {
			Map<String, List<String>> columnValueMap = new HashMap<>();
			List<String> valueList = new ArrayList<>();
			valueList.add(returnCode);
			columnValueMap.put(ColumnConstants.RETURN_CODE.getConstantVal(), valueList);

			List<Return> returnList = returnService.getDataByColumnValue(columnValueMap, MethodConstants.GET_RETURN_DATA_BY_CODE.getConstantVal());

			Return returnObj = null;
			if (!CollectionUtils.isEmpty(returnList)) {
				returnObj = returnList.get(0);
				return returnObj;
			}
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}
		return null;
	}

	@SuppressWarnings("unused")
	private FrequencyDescription getFrequencyDescObj(Frequency frequency, FileDetails fileDetailsBean) {
		Map<String, String> freqDescMap = new HashMap<>();
		freqDescMap.put("01-01~31-03", "freq.quarterly.jan");
		freqDescMap.put("01-04~30-06", "freq.quarterly.apr");
		freqDescMap.put("01-07~30-09", "freq.quarterly.jul");
		freqDescMap.put("01-10~31-12", "freq.quarterly.oct");

		freqDescMap.put("01-01~30-06", "freq.halfYearly.first");
		freqDescMap.put("01-04~30-09", "freq.halfYearly.first");

		freqDescMap.put("01-07~31-12", "freq.halfYearly.second");
		freqDescMap.put("01-10~31-03", "freq.halfYearly.second");

		freqDescMap.put("01-01~31-12", "freq.annually");
		freqDescMap.put("01-04~31-03", "freq.annually");

		if (fileDetailsBean.getReportingPeriodStartDate() != null && fileDetailsBean.getReportingPeriodEndDate() != null) {
			if (frequency.getFreqDesc().size() > 1) {
				String partialStartDate = DateManip.convertDateToString(fileDetailsBean.getReportingPeriodStartDate(), DateConstants.DD_MM.getDateConstants());
				String partialEndDate = DateManip.convertDateToString(fileDetailsBean.getReportingPeriodEndDate(), DateConstants.DD_MM.getDateConstants());
				if (frequency.getFrequencyId().equals(GeneralConstants.QUARTERLY_FREQ_ID.getConstantLongVal())) {
					String startYear = DateManip.convertDateToString(fileDetailsBean.getReportingPeriodStartDate(), DateConstants.YYYY.getDateConstants());
					String endYear = DateManip.convertDateToString(fileDetailsBean.getReportingPeriodEndDate(), DateConstants.YYYY.getDateConstants());
					if (!startYear.equalsIgnoreCase(endYear)) {
						return null;
					}
				}
				if (freqDescMap.get(partialStartDate + "~" + partialEndDate) != null) {
					String freqDesc = freqDescMap.get(partialStartDate + "~" + partialEndDate);
					return frequency.getFreqDesc().stream().filter(f -> f.getFinYrFrquencyDesc().equalsIgnoreCase(freqDesc)).findAny().orElse(null);
				}
			} else if (frequency.getFreqDesc().size() == 1) {
				return frequency.getFreqDesc().get(0);
			}
		}
		return null;
	}

	public boolean checkEntityReturnChannelMapping(FileDetails fileDetailsBean, Return returnObj, Map<String, Map<Boolean, List<String>>> fieldCheckListMap) {
		boolean isMappingPresent = false;
		try {
			// Entity return mapping check and Entity + Return + channel mapping check
			if (!CollectionUtils.isEmpty(returnObj.getReturnEntityMappingNewList())) {
				ReturnEntityMappingNew returnEntityMappingNew = returnObj.getReturnEntityMappingNewList().stream().filter(f -> f.getEntity() != null && f.getEntity().getEntityId().equals(fileDetailsBean.getEntityId()) && f.isActive()).findAny().orElse(null);

				if (!StringUtils.isEmpty(returnEntityMappingNew)) {
					if (fileDetailsBean.getUploadChannelIdFk().getUploadChannelId().equals(GeneralConstants.UPLOAD_CHANNEL.getConstantLongVal()) && returnEntityMappingNew != null && returnEntityMappingNew.isUploadChannel()) {
						isMappingPresent = true;
					} else if (fileDetailsBean.getUploadChannelIdFk().getUploadChannelId().equals(GeneralConstants.SYSTEM_CHANNEL.getConstantLongVal()) && returnEntityMappingNew != null && returnEntityMappingNew.isStsChannel()) {
						isMappingPresent = true;
					} else if (fileDetailsBean.getUploadChannelIdFk().getUploadChannelId().equals(GeneralConstants.API_CHANNEL.getConstantLongVal()) && returnEntityMappingNew != null && returnEntityMappingNew.isApiChannel()) {
						isMappingPresent = true;
					} else if (fileDetailsBean.getUploadChannelIdFk().getUploadChannelId().equals(GeneralConstants.WEB_CHANNEL.getConstantLongVal()) && returnEntityMappingNew != null && returnEntityMappingNew.isWebChannel()) {
						isMappingPresent = true;
					} else if (fileDetailsBean.getUploadChannelIdFk().getUploadChannelId().equals(GeneralConstants.EMAIL_CHANNEL.getConstantLongVal()) && returnEntityMappingNew != null && returnEntityMappingNew.isEmailChannel()) {
						isMappingPresent = true;
					} else {
						isMappingPresent = false;
					}
				} else {
					insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_RETURN_CHANNEL_MAP_CHECK.getConstantVal(), false, ErrorCode.E0637.toString(), fieldCheckListMap);
					return isMappingPresent;
				}
			}

			if (isMappingPresent) {
				insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_RETURN_CHANNEL_MAP_CHECK.getConstantVal(), true, null, fieldCheckListMap);
			} else {
				insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_RETURN_CHANNEL_MAP_CHECK.getConstantVal(), false, ErrorCode.E0745.toString(), fieldCheckListMap);
			}
		} catch (Exception e) {
			insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0745.toString(), fieldCheckListMap);
			LOGGER.error("Exception : ", e);
		}
		return isMappingPresent;
	}

	public boolean validateReturnAndFileTypeMapping(FileDetails fileDetailsBean, Return returnObj, Map<String, Map<Boolean, List<String>>> fieldCheckListMap) {
		boolean isValidationFailed = false;
		try {
			// Return file format mapping check
			if (!CollectionUtils.isEmpty(returnObj.getReturnFileFormatMapList())) {
				ReturnFileFormatMap returnFileFormatMap = returnObj.getReturnFileFormatMapList().stream().filter(f -> f.isActive() && f.getFileFormat().getFormatDesc().equalsIgnoreCase(fileDetailsBean.getFileType())).findAny().orElse(null);
				if (returnFileFormatMap != null) {
					if (StringUtils.isEmpty(returnFileFormatMap.getJsonToReadFile())) {
						isValidationFailed = true;
						insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0641.toString(), fieldCheckListMap);
					} else {
						insertValueIntoStatusMap(MetaDataCheckConstants.RETURN_FILE_TYPE_MAP_CHECK.getConstantVal(), true, null, fieldCheckListMap);
						returnObj.setJsonStringToReadFile(returnFileFormatMap.getJsonToReadFile());
					}
				} else {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.RETURN_FILE_TYPE_MAP_CHECK.getConstantVal(), false, ErrorCode.E0641.toString(), fieldCheckListMap);
				}
			} else {
				isValidationFailed = true;
				insertValueIntoStatusMap(MetaDataCheckConstants.RETURN_FILE_TYPE_MAP_CHECK.getConstantVal(), false, ErrorCode.E0641.toString(), fieldCheckListMap);
			}
		} catch (Exception e) {
			isValidationFailed = true;
			insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0641.toString(), fieldCheckListMap);
		}
		return isValidationFailed;
	}

	private Map<String, List<String>> prepareJsonOfFailure(Map<String, Map<Boolean, List<String>>> fieldCheckListMap) {
		Map<String, List<String>> responseObject = new LinkedHashMap<>();
		try {
			if (fieldCheckListMap != null) {
				for (Map.Entry<String, Map<Boolean, List<String>>> key : fieldCheckListMap.entrySet()) {
					Map<Boolean, List<String>> statusMap = fieldCheckListMap.get(key.getKey());
					for (Map.Entry<Boolean, List<String>> booleanKey : statusMap.entrySet()) {
						if (booleanKey.getKey().equals(Boolean.FALSE)) {
							List<String> errorListWithKey = new ArrayList<>();
							if (statusMap.get(booleanKey.getKey()) != null) {
								statusMap.get(booleanKey.getKey()).forEach(f -> errorListWithKey.add(ObjectCache.getErrorCodeKey(f)));
							}
							responseObject.put(key.getKey(), errorListWithKey);
						} else {
							responseObject.put(key.getKey(), new ArrayList<String>());
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception", e);
		}
		return responseObject;
	}

	private boolean isReturnCodeValidationFailed(Map<String, List<String>> failedMap) {
		return failedMap.get(MetaDataCheckConstants.RETURN_CODE_CHECK.getConstantVal()) != null && !failedMap.get(MetaDataCheckConstants.RETURN_CODE_CHECK.getConstantVal()).isEmpty();
	}

	@SuppressWarnings("unused")
	private boolean isDateValidationFailed(Map<String, Map<Boolean, List<String>>> fieldCheckListMap) {
		if (fieldCheckListMap.get(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal()) != null && !fieldCheckListMap.get(MetaDataCheckConstants.REPORTING_END_DATE_CHECK.getConstantVal()).isEmpty()) {
			return true;
		}

		if (fieldCheckListMap.get(MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal()) != null && !fieldCheckListMap.get(MetaDataCheckConstants.REPORTING_START_DATE_CHECK.getConstantVal()).isEmpty()) {
			return true;
		}

		return false;
	}

	private void sendValidationFailedEmail(FileDetails fileDetails, Map<String, Map<Boolean, List<String>>> fieldCheckListMap) {

		if (fileDetails.getEntityId() != null) {
			List<DynamicContent> dynamicContents;
			try {
				dynamicContents = prepareDynamicContent(fileDetails.getUserName(), fileDetails.getReturnCode(), fileDetails.getEntityCode(), fileDetails.getCreationDate(), fieldCheckListMap);
				sendMail(fileDetails, dynamicContents);
				LOGGER.info("Request served to send Category mail");
			} catch (Exception e) {
				LOGGER.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
			}
		}
	}

	void sendMail(FileDetails fileDetailsBean, List<DynamicContent> dynamicContentList) {
		try {
			LOGGER.info("Mail Sending started For Alert Id ");

			String processingId = UUID.randomUUID().toString();
			MailServiceBean mailServiceBean = new MailServiceBean();
			mailServiceBean.setAlertId(GeneralConstants.META_DATA_VAL_FAILED_ALERT_ID.getConstantLongVal());
			mailServiceBean.setMenuId(93L);
			mailServiceBean.setRoleId(fileDetailsBean.getRoleId());
			mailServiceBean.setUniqueId(processingId);
			mailServiceBean.setUserId(fileDetailsBean.getUserId());
			mailServiceBean.setDynamicContentsList(dynamicContentList);
			mailServiceBean.setReturnCode(fileDetailsBean.getReturnCode());
			mailServiceBean.setEntityCode(fileDetailsBean.getEntityCode());

			List<MailServiceBean> mailServiceBeanList = new ArrayList<>();
			mailServiceBeanList.add(mailServiceBean);
			ServiceResponse serviceResponse = prepareSendMailController.prepareSendEmail(processingId, mailServiceBeanList);
			if (serviceResponse.isStatus()) {
				LOGGER.info("Mail sent successfully");
			}
		} catch (Exception e) {
			LOGGER.error("Exception while sending email", e);
		}

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

	@PostMapping(value = "/validateMetaData")
	public ServiceResponse validateMetaData(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody FileDetailsBean fileDetailsBean) {
		if (fileDetailsBean.getId() != null) {
			LOGGER.info("Request received via system channel for file details Id {}" + fileDetailsBean.getId() + "Job processing ID : " + jobProcessId);
		} else {
			LOGGER.info("Request received other than system channel for jobprocessingId" + jobProcessId);
		}

		Map<String, Map<Boolean, List<String>>> fieldCheckListMap = new LinkedHashMap<>();
		FileDetails fileDetails = null;
		try {
			fileDetails = dtoToDomainConverter(fileDetailsBean);
			//	Step 1 : Validate null check
			boolean isValidationFailed = validateInputRequest(fileDetails, fieldCheckListMap);
			LOGGER.error("validateInputRequest -- >", isValidationFailed);

			if (isValidationFailed) {
				return updateFailedStatusAndReturnResponse(fileDetails, GeneralConstants.FILE_STATUS_META_DATA_FAILED_ID.getConstantIntVal(), fieldCheckListMap);
			}

			//	 			Step 2 : Validate Return + Entity + User + User Role validation
			Return returnObj = getReturnObject(fileDetails.getReturnCode());
			EntityBean entityBean = getEntityObj(fileDetails);
			UserMaster userMaster = getUserObj(fileDetails);
			if (entityBean != null) {
				fileDetails.setEntityName(entityBean.getEntityName());
			}

			isValidationFailed = performBasicValidation(fileDetails, returnObj, entityBean, fieldCheckListMap, userMaster);

			if (isValidationFailed) {
				return updateFailedStatusAndReturnResponse(fileDetails, GeneralConstants.FILE_STATUS_META_DATA_FAILED_ID.getConstantIntVal(), fieldCheckListMap);
			}

			//	 			Step 3 : Validate Return + Entity + User role + return validation
			boolean isEntitReturnChannelMappFound = checkEntityReturnChannelMapping(fileDetails, returnObj, fieldCheckListMap);
			UserRoleDto userRoleDto = checkRoleReturnAndRoleActivityValidation(userMaster, fileDetails, fieldCheckListMap, jobProcessId);

			if (ObjectUtils.isEmpty(userRoleDto) || !isEntitReturnChannelMappFound) {
				return updateFailedStatusAndReturnResponse(fileDetails, GeneralConstants.FILE_STATUS_META_DATA_FAILED_ID.getConstantIntVal(), fieldCheckListMap);
			} else {
				fileDetails.setRoleId(userRoleDto.getUserRoleId());
			}

			//				Step 4 : Validate Mapping
			isValidationFailed = validateReturnAndFileTypeMapping(fileDetails, returnObj, fieldCheckListMap);
			if (isValidationFailed) {
				return updateFailedStatusAndReturnResponse(fileDetails, GeneralConstants.FILE_STATUS_META_DATA_FAILED_ID.getConstantIntVal(), fieldCheckListMap);
			}

			//	  			Step 5 : perform meta data check and Append start date and end date into filedetails bean in case of system to system
			isValidationFailed = fileUploadController.validateFileMetaData(fileDetails, fieldCheckListMap, returnObj);
			if (isValidationFailed) {
				return updateFailedStatusAndReturnResponse(fileDetails, GeneralConstants.FILE_STATUS_META_DATA_FAILED_ID.getConstantIntVal(), fieldCheckListMap);
			}

			LOGGER.info("File Contains validated successfully for job processign Id" + jobProcessId);
			// Step 6 : getWorkflowJSon
			if (!isWorkflowMapped(fileDetails)) {
				insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0724.toString(), fieldCheckListMap);
				return updateFailedStatusAndReturnResponse(fileDetails, GeneralConstants.FILE_STATUS_META_DATA_FAILED_ID.getConstantIntVal(), fieldCheckListMap);
			}

			RetUploadDetBean retUploadDetBean = getReturnUploadDetBean(fileDetails, returnObj.getFrequency());

			//	Step 7 : validate Filling window, revision request, unlocking request
			boolean isAllowedToUploadFilling = isAllowedToUploadFilling(retUploadDetBean, fieldCheckListMap, returnObj);
			if (!isAllowedToUploadFilling) {
				return updateFailedStatusAndReturnResponse(fileDetails, GeneralConstants.FILE_STATUS_META_DATA_FAILED_ID.getConstantIntVal(), fieldCheckListMap);
			}

			// Step 8 : Return template
			ReturnTemplate taxonomy = getTaxonomyObject(returnObj, fileDetails.getReportingPeriodEndDate(), fileDetails.getFileType());

			if (taxonomy == null) {
				insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0787.toString(), fieldCheckListMap);
				return updateFailedStatusAndReturnResponse(fileDetails, GeneralConstants.FILE_STATUS_META_DATA_FAILED_ID.getConstantIntVal(), fieldCheckListMap);
			}
			retUploadDetBean.setTaxonomyId(taxonomy.getReturnTemplateId());

			// Step 9 : if xsd file not match to the taxonomy uploaded
			if (fileDetails.getFileType().equalsIgnoreCase(GeneralConstants.XML.getConstantVal())) {
				boolean isSchemaRefMatch = isSchemaRefFound(fileDetails, taxonomy, fieldCheckListMap);
				if (!isSchemaRefMatch) {
					insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0557.toString(), fieldCheckListMap);
					return updateFailedStatusAndReturnResponse(fileDetails, GeneralConstants.FILE_STATUS_META_DATA_FAILED_ID.getConstantIntVal(), fieldCheckListMap);
				}
			}

			// Step 10 : calculate Filechecksum
			fileDetails.setFileCheckSum(FileCheckSumUtility.calculateFileChecksum(fileDetails.getFilePath()));

			// Step 11 : Move uploaded file from source to destlination (Uploaded folder)
			String destinationUploadFilePath = null;
			String destinationAttachementFilePath = null;
			// removed for nill filing upload issue 11012021
			//if (retUploadDetBean.getNillable().equals(Boolean.FALSE)) {
			destinationUploadFilePath = fileUploadController.uploadFileOperation(retUploadDetBean);
			if (destinationUploadFilePath == null) {
				LOGGER.error("Upload file moving error for job processing ID " + jobProcessId);
				insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0800.toString(), fieldCheckListMap);
				return updateFailedStatusAndReturnResponse(fileDetails, GeneralConstants.FILE_STATUS_META_DATA_FAILED_ID.getConstantIntVal(), fieldCheckListMap);
			} else {
				LOGGER.info("Upload file successfully moved for job processing ID " + jobProcessId);
			}
			//}

			//				Step 12 :Move attchement file from src to destination ( Attachement folder)
			if (retUploadDetBean.getSupportiveDocFileName() != null && !retUploadDetBean.getSupportiveDocFileName().isEmpty()) {
				destinationAttachementFilePath = fileUploadController.uploadAttachement(retUploadDetBean);
				if (destinationAttachementFilePath == null) {
					LOGGER.error("Attachement file moving error");
					if (deleteFileFromLocation(destinationUploadFilePath)) {
						LOGGER.info("Uploaded file successfully deleted as a rolllback activity for job processing Id" + jobProcessId);
					}
					insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0725.toString(), fieldCheckListMap);
					return updateFailedStatusAndReturnResponse(fileDetails, GeneralConstants.FILE_STATUS_META_DATA_FAILED_ID.getConstantIntVal(), fieldCheckListMap);
				} else {
					LOGGER.info("Attachement file successfully moved for job processing Id" + jobProcessId);
				}
			}

			//				Step 13 : Insert data into TBL_RETURNS_UPLOAD_DETAILS
			ServiceResponse insertDataServiceResponse = insertDataIntoUploadTableAndSendEmail(retUploadDetBean, returnObj, entityBean, fileDetails, destinationUploadFilePath, destinationAttachementFilePath, fieldCheckListMap);
			LOGGER.info("Record inserted succesfully for job processing ID " + jobProcessId);

			if (!insertDataServiceResponse.isStatus() && !fileDetails.getUploadChannelIdFk().getUploadChannelId().equals(GeneralConstants.WEB_CHANNEL.getConstantLongVal()) && !fileDetails.getUploadChannelIdFk().getUploadChannelId().equals(GeneralConstants.UPLOAD_CHANNEL.getConstantLongVal())) {
				moveFileInstanceFileAndAttachementFileToFailedFolder(fileDetails);
			}

			return insertDataServiceResponse;
		} catch (Exception e) {
			insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0786.toString(), fieldCheckListMap);
			LOGGER.error("Exception :", e);
			return updateFailedStatusAndReturnResponse(fileDetails, GeneralConstants.FILE_STATUS_META_DATA_FAILED_ID.getConstantIntVal(), fieldCheckListMap);
		}
	}

	private boolean saveFilesForEtl(Long uploadId, String destinationUploadFilePath) {
		try {
			Long[] uploadIds = new Long[1];
			uploadIds[0] = uploadId;

			List<ReturnsUploadDetails> returnUploadIds = returnUploadDetailsService.getDataByIds(uploadIds);
			String customFileName;
			String customFileNameFilingDetails;
			String parentFolder = new File(destinationUploadFilePath).getParentFile().getAbsolutePath();

			String datetFormat = DateConstants.YYYY_MM_DD.getDateConstants();
			String calendarFormat = "en";
			String timeFormat = DateConstants.HH_MM_SS.getDateConstants().toLowerCase();

			for (ReturnsUploadDetails returnUploadDetails : returnUploadIds) {
				if (returnUploadDetails.getFileType().equals(GeneralConstants.JSON.getConstantVal())) {
					DocumentInfo documetInfo = new ExtractFileData().readDocumentInfoFromJSONFileData(destinationUploadFilePath);
					customFileName = returnUploadDetails.getReturnObj().getReturnCode() + GeneralConstants.UNDER_SCORE.getConstantVal() + returnUploadDetails.getUploadId() + GeneralConstants.UNDER_SCORE.getConstantVal() + GeneralConstants.WEBFORM.getConstantVal().toUpperCase() + GeneralConstants.UNDER_SCORE.getConstantVal() + returnUploadDetails.getInstanceFile();
					customFileNameFilingDetails = returnUploadDetails.getReturnObj().getReturnCode() + GeneralConstants.UNDER_SCORE.getConstantVal() + returnUploadDetails.getUploadId() + GeneralConstants.UNDER_SCORE.getConstantVal() + GeneralConstants.WEBFORM.getConstantVal().toUpperCase() + GeneralConstants.UNDER_SCORE.getConstantVal() + GeneralConstants.FILING_DETAILS_CONSTANT.getConstantVal() + GeneralConstants.UNDER_SCORE.getConstantVal() + returnUploadDetails.getInstanceFile();

					CustomJsonBean customJsonBean = new Gson().fromJson(FileUtils.readFileToString(new File(destinationUploadFilePath), "UTF-8"), CustomJsonBean.class);
					documetInfo = customJsonBean.getDocumentInfo();
					FilingDetailsBean filingDetails = documetInfo.getFilingDetails();
					filingDetails.setFilingNumber(returnUploadDetails.getUploadId().toString());
					filingDetails.setEntityId(returnUploadDetails.getEntity().getEntityId().toString());
					filingDetails.setFrequncyCode(filingCalendarService.getFrequency(returnUploadDetails.getFrequency().getFrequencyId()).getFrequencyCode());

					String uploadTime = DateManip.formatAppDateTime(returnUploadDetails.getUploadedDate(), datetFormat + " " + timeFormat, calendarFormat);

					filingDetails.setUploadedDate(uploadTime);
					filingDetails.setAuditFlag(returnUploadDetails.getReturnPropertyValue() == null ? "0" : returnUploadDetails.getReturnPropertyValue().getReturnProprtyIdFK().getReturnProprtyId().toString());
					filingDetails.setFrequncyId(returnUploadDetails.getFrequency().getFrequencyId().toString());
					filingDetails.setReturnId(returnUploadDetails.getReturnObj().getReturnId().toString());
					filingDetails.setFrequencyName(filingDetails.getReportingFreq());
					//filingDetails.setTaxonomyVersion(returnTemplateService.fetchEntityByReturnTemplateId(returnUploadDetails.getTaxonomyId().getReturnTemplateId()).getVersionNumber());
					filingDetails.setTaxonomyVersion("1.0.0");

					documetInfo.setFilingDetails(filingDetails);
					customJsonBean.setDocumentInfo(documetInfo);

					FileManager.writeStringToFile(new File(parentFolder + File.separator + GeneralConstants.FILE_TO_PUSH.getConstantVal() + File.separator + customFileName), new Gson().toJson(customJsonBean), "UTF-8");
					//FileManager.writeStringToFile(new File(parentFolder + File.separator + GeneralConstants.FILE_TO_PUSH.getConstantVal() + File.separator + customFileNameFilingDetails), new Gson().toJson(documetInfo), "UTF-8");

				} else if (returnUploadDetails.getFileType().equals(GeneralConstants.PDF.getConstantVal())) {

					customFileNameFilingDetails = returnUploadDetails.getReturnObj().getReturnCode() + GeneralConstants.UNDER_SCORE.getConstantVal() + returnUploadDetails.getUploadId() + GeneralConstants.UNDER_SCORE.getConstantVal() + GeneralConstants.FILING_DETAILS_CONSTANT.getConstantVal() + GeneralConstants.UNDER_SCORE.getConstantVal() + returnUploadDetails.getInstanceFile().split("\\.")[0] + GeneralConstants.JSON_EXTENSION.getConstantVal();
					FilingDetailsBean filingDetails = new FilingDetailsBean();
					DocumentInfo documetInfo = new DocumentInfo();
					filingDetails.setFilingNumber(returnUploadDetails.getUploadId().toString());
					filingDetails.setEntityId(returnUploadDetails.getEntity().getEntityId().toString());
					filingDetails.setEntityCode(returnUploadDetails.getEntity().getEntityCode());
					filingDetails.setEntityName(returnUploadDetails.getEntity().getEntityName());

					String uploadTime = DateManip.formatAppDateTime(returnUploadDetails.getUploadedDate(), datetFormat + " " + timeFormat, calendarFormat);

					filingDetails.setUploadedDate(uploadTime);
					filingDetails.setUploadFileName(returnUploadDetails.getInstanceFile());
					filingDetails.setReturnId(returnUploadDetails.getReturnObj().getReturnId().toString());
					filingDetails.setReturnCode(returnUploadDetails.getReturnObj().getReturnCode());
					filingDetails.setReturnName(returnUploadDetails.getReturnObj().getReturnName());
					filingDetails.setReportingEndDate(DateManip.formatAppDateTime(returnUploadDetails.getEndDate(), datetFormat, calendarFormat));
					filingDetails.setReportingStartDate(DateManip.formatAppDateTime(returnUploadDetails.getStartDate(), datetFormat, calendarFormat));
					filingDetails.setFrequncyCode(returnUploadDetails.getReturnObj().getFrequency().getFrequencyCode());
					filingDetails.setFrequencyName(ObjectCache.getLabelKeyValue("en", returnUploadDetails.getReturnObj().getFrequency().getFrequencyName()));
					filingDetails.setFrequncyId(returnUploadDetails.getReturnObj().getFrequency().getFrequencyId().toString());
					filingDetails.setAuditFlag(returnUploadDetails.getReturnPropertyValue() == null ? "" : returnUploadDetails.getReturnPropertyValue().getReturnProprtyValId().toString());
					filingDetails.setFileType(returnUploadDetails.getFileType().toUpperCase());
					filingDetails.setUploadChannel(GeneralConstants.FILE_UPLOAD.getConstantVal());
					filingDetails.setTaxonomyVersion(returnTemplateService.fetchEntityByReturnTemplateId(returnUploadDetails.getTaxonomyId().getReturnTemplateId()).getVersionNumber());

					documetInfo.setFilingDetails(filingDetails);
					FileManager.writeStringToFile(new File(parentFolder + File.separator + GeneralConstants.FILE_TO_PUSH.getConstantVal() + File.separator + customFileNameFilingDetails), new Gson().toJson(documetInfo), "UTF-8");
				}
			}

		} catch (Exception e) {
			LOGGER.error("Exception :", e);
			return false;
		}

		return true;
	}

	private FileDetails dtoToDomainConverter(FileDetailsBean fileDetailsBean) {
		FileDetails fileDetails = new FileDetails();

		fileDetails.setBusinessValidationPass(fileDetailsBean.isBusinessValidationPass());
		fileDetails.setLangCode(fileDetailsBean.getLangCode());
		fileDetails.setDateFormat(fileDetailsBean.getDateFormat());
		fileDetails.setCalendarFormat(fileDetailsBean.getCalendarFormat());
		fileDetails.setFrequencyIdFk(fileDetailsBean.getFrequencyIdFk());
		fileDetails.setFrequency(fileDetailsBean.getFrequency());
		fileDetails.setReturnPropertyValId(fileDetailsBean.getReturnPropertyValId());
		fileDetails.setReportStatus(fileDetailsBean.getReportStatus());
		fileDetails.setReportingPeriodStartDateInString(fileDetailsBean.getReportingPeriodStartDateInString());
		fileDetails.setApplicationProcessId(fileDetailsBean.getApplicationProcessId());
		fileDetails.setFilingStatus(fileDetailsBean.getFilingStatus());
		fileDetails.setWorkflowId(fileDetailsBean.getWorkflowId());
		fileDetails.setWorkflowStepNo(fileDetailsBean.getWorkflowStepNo());
		fileDetails.setReportingPeriodStartDateInLong(fileDetailsBean.getReportingPeriodStartDateInLong());
		fileDetails.setReportingPeriodEndDateInLong(fileDetailsBean.getReportingPeriodEndDateInLong());
		fileDetails.setFileCreationTimeInNumber(fileDetailsBean.getFileCreationTimeInNumber());
		fileDetails.setId(fileDetailsBean.getId());
		fileDetails.setFileName(fileDetailsBean.getFileName());
		fileDetails.setFileCopyingStartTime(fileDetailsBean.getFileCopyingStartTime());
		fileDetails.setFileCopyingEndTime(fileDetailsBean.getFileCopyingEndTime());
		fileDetails.setFileCreationTime(fileDetailsBean.getFileCreationTime());
		fileDetails.setFileType(fileDetailsBean.getFileType());
		fileDetails.setReasonOfNotProcessed(fileDetailsBean.getReasonOfNotProcessed());
		fileDetails.setSize(fileDetailsBean.getSize());
		fileDetails.setFileCheckSum(fileDetailsBean.getFileCheckSum());
		fileDetails.setFileMimeType(fileDetailsBean.getFileMimeType());
		fileDetails.setCreationDate(fileDetailsBean.getCreationDate());
		fileDetails.setIsActive(fileDetailsBean.getIsActive());
		fileDetails.setReportingPeriodStartDate(fileDetailsBean.getReportingPeriodStartDate());
		fileDetails.setReportingPeriodEndDate(fileDetailsBean.getReportingPeriodEndDate());
		fileDetails.setUserName(fileDetailsBean.getUserName());
		fileDetails.setReturnCode(fileDetailsBean.getReturnCode());
		fileDetails.setEntityCode(fileDetailsBean.getEntityCode());
		fileDetails.setUploadChannelIdFk(fileDetailsBean.getUploadChannelIdFk());
		fileDetails.setNillabelComments(fileDetailsBean.getNillabelComments());
		//		fileDetails.setFilingStatus(fileDetailsBean.getFilingStatus());
		fileDetails.setFilingStatusId(fileDetailsBean.getFilingStatusId());
		fileDetails.setOldReturnCode(fileDetailsBean.getOldReturnCode());
		fileDetails.setUserMaster(fileDetailsBean.getUserMaster());
		fileDetails.setBsr7FileType(fileDetailsBean.getBsr7FileType());
		fileDetails.setFilePath(fileDetailsBean.getFilePath());
		fileDetails.setSupportiveDocFilePath(fileDetailsBean.getSupportiveDocFilePath());
		fileDetails.setUserId(fileDetailsBean.getUserId());
		fileDetails.setReportingPeriodEndDateInString(fileDetailsBean.getReportingPeriodEndDateInString());
		fileDetails.setIfscCode(fileDetailsBean.getIfscCode());
		fileDetails.setNillable(fileDetailsBean.isNillable());
		fileDetails.setEmailId(fileDetailsBean.getEmailId());
		fileDetails.setIfscCode(fileDetailsBean.getIfscCode());
		fileDetails.setSupportiveDocName(fileDetailsBean.getSupportiveDocName());
		fileDetails.setSupportiveDocType(fileDetailsBean.getSupportiveDocType());
		fileDetails.setReturnId(fileDetailsBean.getReturnId());
		fileDetails.setReturnName(fileDetailsBean.getReturnName());
		fileDetails.setEntityName(fileDetailsBean.getEntityName());
		fileDetails.setEntityId(fileDetailsBean.getEntityId());
		fileDetails.setFinYearFormatId(fileDetailsBean.getFinYearFormatId());
		fileDetails.setChannelId(fileDetailsBean.getChannelId());
		fileDetails.setRoleId(fileDetailsBean.getRoleId());
		fileDetails.setFileCopyingStartTimeInLong(fileDetailsBean.getFileCopyingStartTimeInLong());
		fileDetails.setFileCopyingEndTimeInLong(fileDetailsBean.getFileCopyingEndTimeInLong());
		fileDetails.setUserSelectedFileName(fileDetailsBean.getUserSelectedFileName());

		return fileDetails;
	}

	public ReturnTemplate getTaxonomyObject(Return returnObj, Date reportingEndDate, String fileType) {
		try {
			String taxonomyDate = null;
			DateValidationsController dateValidationsControlObj = new DateValidationsController();
			Long dateDiff = 100000L;
			Long dateDiffCal = null;
			Set<ReturnTemplate> taxonomySet = null;
			//			if (returnObj.getIsParent().equals(Boolean.FALSE) && returnObj.getIsParentIdFk().getReturnId() != null) {
			//				taxonomySet = returnObj.getIsParentIdFk().getTaxonomy();
			//			} else if (returnObj.getIsParent().equals(Boolean.TRUE)) {
			//				taxonomySet = returnObj.getTaxonomy();
			//			}

			taxonomySet = returnObj.getTaxonomy();

			if (CollectionUtils.isEmpty(taxonomySet)) {
				return null;
			}

			ReturnTemplate taxObjPassed = null;
			boolean isOldTaxonomy = false;
			String reportingDatePassed = DateManip.convertDateToString(reportingEndDate, DateConstants.DD_MM_YYYY.getDateConstants());

			if (fileType.equalsIgnoreCase(GeneralConstants.XML.getConstantVal())) {
				taxonomySet = taxonomySet.stream().filter(f -> f.getReturnTypeIdFk().getReturnTypeId().equals(GeneralConstants.XBRL_RETURN_TYPE_ID.getConstantLongVal())).collect(Collectors.toSet());
			} else if (fileType.equalsIgnoreCase(GeneralConstants.CSV.getConstantVal()) || fileType.equalsIgnoreCase(GeneralConstants.TXT.getConstantVal())) {
				taxonomySet = taxonomySet.stream().filter(f -> f.getReturnTypeIdFk().getReturnTypeId().equals(GeneralConstants.OTHER_RETURN_TYPE_ID.getConstantLongVal()) && f.getReturnTypeSectionId() == GeneralConstants.CSV_RETURN_TYPE_SECTION.getConstantIntVal()).collect(Collectors.toSet());
			} else if (fileType.equalsIgnoreCase(GeneralConstants.JSON.getConstantVal())) {
				taxonomySet = taxonomySet.stream().filter(f -> f.getReturnTypeIdFk().getReturnTypeId().equals(GeneralConstants.OTHER_RETURN_TYPE_ID.getConstantLongVal()) && f.getReturnTypeSectionId() == GeneralConstants.WEB_RETURN_TYPE_SECTION.getConstantIntVal()).collect(Collectors.toSet());
			}

			for (ReturnTemplate taxObj : taxonomySet) {
				taxonomyDate = taxObj.getValidFromDate().toString();
				if (taxonomyDate != null && taxObj.getIsActive().equals(Boolean.TRUE)) {
					taxonomyDate = taxonomyDate.substring(0, 10);
					taxonomyDate = dateValidationsControlObj.formatDate(taxonomyDate, DateConstants.YYYY_MM_DD.getDateConstants(), DateConstants.DD_MM_YYYY.getDateConstants());
					dateDiffCal = dateValidationsControlObj.getDayDiff(taxonomyDate, reportingDatePassed, DateConstants.DD_MM_YYYY.getDateConstants());
					if (dateDiffCal > 0 && dateDiffCal < dateDiff) {
						taxObjPassed = taxObj;
						dateDiff = dateDiffCal;
						isOldTaxonomy = false;
					} else if (dateDiffCal == 0) {
						taxObjPassed = taxObj;
						isOldTaxonomy = false;
						break;
					} else if (dateDiffCal < 0) {
						isOldTaxonomy = true;
					}
				}
			}
			if (taxObjPassed == null && isOldTaxonomy) {
				return null;
			} else {
				return taxObjPassed;
			}
		} catch (Exception e) {
			LOGGER.error("Exception :", e);
		}
		return null;
	}

	private ServiceResponse updateFailedStatusAndReturnResponse(FileDetails fileDetails, Integer statusId, Map<String, Map<Boolean, List<String>>> fieldCheckListMap) {
		try {
			FileDetails fileDetailsBean = null;
			FilingStatus fileStatus = new FilingStatus();
			fileStatus.setFilingStatusId(statusId);

			Map<String, List<String>> failedMap = null;
			// Meta data failed
			failedMap = prepareJsonOfFailure(fieldCheckListMap);

			if (fileDetails.getId() != null) {
				fileDetailsBean = fileDetailsService.getDataById(fileDetails.getId());

				LOGGER.info("Return has Meta data validation failed--------------> FileDetailsID :->" + fileDetailsBean.getId());

				if (isReturnCodeValidationFailed(failedMap)) {
					fileDetailsBean.setReturnCode(null);
				}

				fileDetailsBean.setReasonOfNotProcessed(JsonUtility.getGsonObject().toJson(failedMap));
				if (fileDetails.getUploadChannelIdFk() != null && fileDetails.getUploadChannelIdFk().getUploadChannelId().equals(GeneralConstants.EMAIL_CHANNEL.getConstantLongVal())) {
					fileDetailsBean.setIfscCode(fileDetails.getIfscCode());
				}
				fileDetailsBean.setFilingStatus(fileStatus);
				fileDetailsBean.setIsActive(false);
				fileDetailsBean.setFileCheckSum(fileDetails.getFileCheckSum());
				fileDetailsBean.setUserName(fileDetails.getUserName());
				fileDetailsService.add(fileDetailsBean);
				sendValidationFailedEmail(fileDetails, fieldCheckListMap);
				moveFileInstanceFileAndAttachementFileToFailedFolder(fileDetails);
			}

			return new ServiceResponse.ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0809.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0809.toString())).setResponse(failedMap).build();
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
			insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0786.toString(), fieldCheckListMap);
			return new ServiceResponse.ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0786.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0786.toString())).setResponse(prepareJsonOfFailure(fieldCheckListMap)).build();
		}
	}

	private boolean validateInputRequest(FileDetails fileDetailsBean, Map<String, Map<Boolean, List<String>>> fieldCheckListMap) {
		boolean isValidationFailed = false;
		File file = null;
		if (StringUtils.isEmpty(fileDetailsBean.getFileName())) {
			isValidationFailed = true;
			insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0657.toString(), fieldCheckListMap);
		}

		if (StringUtils.isEmpty(fileDetailsBean.getFileType())) {
			isValidationFailed = true;
			insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0658.toString(), fieldCheckListMap);
		}

		// Return Code validation
		if (StringUtils.isEmpty(fileDetailsBean.getReturnCode())) {
			isValidationFailed = true;
			insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0784.toString(), fieldCheckListMap);
		}

		if (StringUtils.isEmpty(fileDetailsBean.getFilePath())) {
			isValidationFailed = true;
			insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0794.toString(), fieldCheckListMap);
		} else {
			file = new File(fileDetailsBean.getFilePath());
			if (!file.exists()) {
				LOGGER.error("File Not exist having path : " + fileDetailsBean.getFilePath());
				isValidationFailed = true;
				insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0793.toString(), fieldCheckListMap);
				return isValidationFailed;
			}
		}

		// channelId validation
		if (StringUtils.isEmpty(fileDetailsBean.getUploadChannelIdFk()) || StringUtils.isEmpty(fileDetailsBean.getUploadChannelIdFk().getUploadChannelId())) {
			isValidationFailed = true;
			insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0788.toString(), fieldCheckListMap);
		} else {
			// Mime type
			if (StringUtils.isEmpty(fileDetailsBean.getFileMimeType())) {
				isValidationFailed = true;
				insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0798.toString(), fieldCheckListMap);
			} else {
				if (fileDetailsBean.getFileType() != null) {
					if (!fileDetailsBean.getUploadChannelIdFk().getUploadChannelId().equals(GeneralConstants.WEB_CHANNEL.getConstantLongVal())) {
						String fileTypeArr[] = FileMimeType.getFileMimeType(file); // getting the file mime type
						if (fileTypeArr != null && fileTypeArr.length == 2) {
							// File mime type valid
							String fileType = fileTypeArr[0];

							if (!fileDetailsBean.getFileType().equalsIgnoreCase(fileType)) {
								isValidationFailed = true;
								// Update Error code : Input file type and File type is not matching
								insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E1149.toString(), fieldCheckListMap);
							}
						} else {
							isValidationFailed = true;
							insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0797.toString(), fieldCheckListMap);
						}
					}

					if (!isValidationFailed && fileDetailsBean.getFileMimeType().equalsIgnoreCase(GeneralConstants.XMLMIMETYPE.getConstantVal())) {
						XmlValidate xmlValidate = new XmlValidate();
						if (!xmlValidate.isValidXmlDocument(fileDetailsBean.getFilePath())) {
							isValidationFailed = true;
							insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0796.toString(), fieldCheckListMap);
							return isValidationFailed;
						}
					}
				}
			}

			if (fileDetailsBean.getUploadChannelIdFk().getUploadChannelId().equals(GeneralConstants.SYSTEM_CHANNEL.getConstantLongVal())) {
				// IFSC Code validation
				if (StringUtils.isEmpty(fileDetailsBean.getIfscCode())) {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0795.toString(), fieldCheckListMap);
				}

				// User Id validation
				//				if (StringUtils.isEmpty(fileDetailsBean.getUserId())) {
				//					isValidationFailed = true;
				//					insertValueIntoStatusMap(MetaDataCheckConstants.MANDATORY_CHECK.getConstantVal(), false,
				//							ErrorCode.E0789.toString(), fieldCheckListMap);
				//				}

			} else if (fileDetailsBean.getUploadChannelIdFk().getUploadChannelId().equals(GeneralConstants.EMAIL_CHANNEL.getConstantLongVal())) {
				// Entity Code validation
				if (StringUtils.isEmpty(fileDetailsBean.getEntityCode())) {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0792.toString(), fieldCheckListMap);
				}

				// Email Id validation
				if (StringUtils.isEmpty(fileDetailsBean.getEmailId())) {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0791.toString(), fieldCheckListMap);
				}
			} else if (fileDetailsBean.getUploadChannelIdFk().getUploadChannelId().equals(GeneralConstants.API_CHANNEL.getConstantLongVal())) {
				// Entity Code validation
				if (StringUtils.isEmpty(fileDetailsBean.getEntityCode())) {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0792.toString(), fieldCheckListMap);
				}

				// User Id validation
				if (StringUtils.isEmpty(fileDetailsBean.getUserId())) {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0789.toString(), fieldCheckListMap);
				}

				// Reporting end date validation
				if (StringUtils.isEmpty(fileDetailsBean.getReportingPeriodEndDateInString())) {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0783.toString(), fieldCheckListMap);
				}
			} else {
				// Entity Code validation
				if (StringUtils.isEmpty(fileDetailsBean.getEntityCode())) {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0792.toString(), fieldCheckListMap);
				}

				// User Id validation
				if (StringUtils.isEmpty(fileDetailsBean.getUserId())) {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0789.toString(), fieldCheckListMap);
				}

				// Reporting end date validation
				if (StringUtils.isEmpty(fileDetailsBean.getReportingPeriodEndDateInString())) {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0783.toString(), fieldCheckListMap);
				}

				if (StringUtils.isEmpty(fileDetailsBean.getRoleId())) {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0777.toString(), fieldCheckListMap);
				}
			}
		}

		return isValidationFailed;
	}

	private void insertValueIntoStatusMap(String key, Boolean b, String errorString, Map<String, Map<Boolean, List<String>>> fieldCheckListMap) {
		if (fieldCheckListMap.get(key) != null) {
			Map<Boolean, List<String>> map = fieldCheckListMap.get(key);
			if (map.get(b) != null) {
				map.get(b).add(errorString);
			} else {
				List<String> errorList = new ArrayList<>();
				errorList.add(errorString);
				map.put(b, errorList);
			}
		} else {
			List<String> errorList = new ArrayList<>();
			errorList.add(errorString);
			Map<Boolean, List<String>> map = new LinkedHashMap<>();
			map.put(b, errorList);
			fieldCheckListMap.put(key, map);
		}
	}

	/**
	 * @param userName
	 * @param retName
	 * @param companyName
	 * @param uploadDate
	 * @param fieldStatusMap
	 * @throws Exception
	 */
	public List<DynamicContent> prepareDynamicContent(String userName, String retName, String companyName, Date uploadDate, Map<String, Map<Boolean, List<String>>> fieldCheckListMap) throws Exception {

		List<DynamicContent> dynaContentLst = new ArrayList<>();

		DynamicContent dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.return.code"));
		if (retName != null) {
			dynamicContent.setValue(retName);
		} else {
			dynamicContent.setValue("");
		}
		dynaContentLst.add(dynamicContent);

		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.entity.entityCode"));
		dynamicContent.setValue(companyName);
		dynaContentLst.add(dynamicContent);

		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.filingMngt.uploadedBy"));
		dynamicContent.setValue(userName);
		dynaContentLst.add(dynamicContent);

		String datetFormat = DateConstants.DD_MMM_YYYY.getDateConstants();
		String calendarFormat = "en";
		String timeFormat = DateConstants.HH_MM_SS.getDateConstants().toLowerCase() + " " + DateConstants.AM_PM.getDateConstants();

		String uploadTime = DateManip.formatAppDateTime(uploadDate, datetFormat + " " + timeFormat, calendarFormat);
		dynamicContent = new DynamicContent();
		dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", "field.filingMngt.uplodedDate"));
		dynamicContent.setValue(uploadTime);
		dynaContentLst.add(dynamicContent);

		try {
			if (fieldCheckListMap != null) {
				for (Map.Entry<String, Map<Boolean, List<String>>> entry : fieldCheckListMap.entrySet()) {
					dynamicContent = new DynamicContent();
					if (ObjectCache.getLabelKeyValue("en", entry.getKey()) != null) {
						dynamicContent.setLabel(ObjectCache.getLabelKeyValue("en", entry.getKey()));
					} else {
						dynamicContent.setLabel(entry.getKey());
					}
					Map<Boolean, List<String>> statusMap = fieldCheckListMap.get(entry.getKey());

					for (Map.Entry<Boolean, List<String>> entry1 : statusMap.entrySet()) {
						if (entry1.getKey().equals(Boolean.FALSE)) {
							if (statusMap.get(entry1.getKey()) != null) {
								List<String> list = statusMap.get(entry1.getKey());
								StringBuilder preparedErrorString = new StringBuilder("");
								for (String errorString : list) {
									if (!preparedErrorString.toString().equals("")) {
										preparedErrorString = preparedErrorString.append("," + errorString + " : " + ObjectCache.getLabelKeyValue("en", ObjectCache.getErrorCodeKey(errorString)));
									} else {
										preparedErrorString = preparedErrorString.append(errorString + " : " + ObjectCache.getLabelKeyValue("en", ObjectCache.getErrorCodeKey(errorString)));
									}
								}
								dynamicContent.setValue(ObjectCache.getLabelKeyValue("en", "field.inValid") + " | " + preparedErrorString.toString());
							}
						} else {
							dynamicContent.setValue(ObjectCache.getLabelKeyValue("en", "field.valid"));
						}
					}
					dynaContentLst.add(dynamicContent);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured while preparing mail string", e);
		}

		return dynaContentLst;
	}

	private SdmxActivityDetailLogRequest getSdmxFileActivityLog(FileDetails fileDetails, String ipAddress) {
		SdmxActivityDetailLogRequest sdmxFileActivityLog = new SdmxActivityDetailLogRequest();
		sdmxFileActivityLog.setFileDetailId(fileDetails.getId());
		sdmxFileActivityLog.setProcessStartTimeLong(new Date().getTime());
		sdmxFileActivityLog.setProcessCode(RBR_METADATA_PROCESS_CODE);
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("ipAddress", ipAddress);
		sdmxFileActivityLog.setOtherDetails(jsonObject.toString());
		return sdmxFileActivityLog;
	}

	public boolean isSchemaRefFound(FileDetails fileDetails, ReturnTemplate taxonomy, Map<String, Map<Boolean, List<String>>> fieldCheckListMap) {
		if (fileDetails.getSchemaRef() == null || "".equals(fileDetails.getSchemaRef()) || taxonomy.getXsdFileName() == null || taxonomy.getXsdFileName().isEmpty()) {
			return false;
		}

		if (taxonomy.getXsdFileName() != null) {
			File file = new File(taxonomy.getXsdFileName());
			String fileName = file.getName();
			if (!fileName.equals(fileDetails.getSchemaRef())) {
				return false;
			}
		}
		return true;
	}

}
