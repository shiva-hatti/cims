/**
 * 
 */
package com.iris.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;
import com.iris.caching.ObjectCache;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.DynamicContent;
import com.iris.dto.MailServiceBean;
import com.iris.dto.PanMasterTempDto;
import com.iris.dto.ServiceResponse;
import com.iris.exception.ServiceException;
import com.iris.model.EntityBean;
import com.iris.model.PanMasterBulk;
import com.iris.model.PanMasterTemp;
import com.iris.model.PanStatus;
import com.iris.model.Scheduler;
import com.iris.model.SchedulerLog;
import com.iris.model.UserEntityRole;
import com.iris.model.UserMaster;
import com.iris.model.UserRoleMaster;
import com.iris.model.WebServiceComponentUrl;
import com.iris.repository.PanMasterTempRepo;
import com.iris.repository.PanStatusRepo;
import com.iris.service.GenericService;
import com.iris.service.impl.UserEntityRoleService;
import com.iris.util.JsonUtility;
import com.iris.util.ResourceUtil;
import com.iris.util.Validations;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;
import com.iris.webservices.client.CIMSRestWebserviceClient;

/**
 * @author Siddique
 *
 */

@RestController
public class PanMasterBulkUploadSchedulerController {

	static final Logger LOGGER = LogManager.getLogger(PanMasterBulkUploadSchedulerController.class);

	@Autowired
	private GenericService<WebServiceComponentUrl, Long> webServiceComponentService;

	@Autowired
	private GenericService<PanMasterBulk, Long> panMasterBulkService;

	@Autowired
	private PANMasterController panMasterController;

	@Autowired
	private UserEntityRoleService userEntityRoleService;

	@Autowired
	private PrepareSendMailController prepareSendMailController;

	@Autowired
	private PanStatusRepo panStatusRepo;

	@Autowired
	private PanMasterTempRepo panMasterTempRepo;

	//	private static final String SCHEDULER_CODE = "PAN_MASTER_SCHEDULER";

	private static final String ROOT_PATH = "filepath.root";
	private static final String FILE_PATH = "filepath.pan.upload";

	private static final String PASS = "PASS";
	private static final String FAIL = "FAIL";
	private static final String PROCESSING_STATUS = "Status";
	private static final String REASON = "Reason";
	private static String LANG_CODE = "en";
	private static final String CENT_GOVT = "Central Government";
	private static final String FOREIGN = "Foreign";
	private static final String STATE_GOVT = "State Government";
	private static final String TEMP = "Temporary";
	private static final String COMPANY = "Company";
	private static final String INDIVIDUAL = "Individual";
	private static final String GOVT = "Government";
	private static final String TRUST = "Trust";
	private int[] numberOfHeadersInExcelSheets = new int[] { 5, 6 };
	private EntityBean entityBean = null;
	private UserMaster userMaster = null;
	private Set<String> panNumberSet = null;
	private Set<String> duplicatePanNumberSet = null;

	private String jobProcessingId;

	@Value("${scheduler.code.panBulk}")
	private String schedulerCode;

	@Scheduled(cron = "${cron.panBulkScheduler}")
	public void validatePanBulkUploadFiles() {
		jobProcessingId = UUID.randomUUID().toString();
		LOGGER.info("Pan Master Bulk file validate scheduler started with Job processing ID " + jobProcessingId);
		List<PanMasterBulk> panMasterBulkList = null;
		Map<Long, Map<Long, String>> validateFileErrorMap = null;
		List<MailServiceBean> mailServiceBeanList = new ArrayList<>();
		Long schedulerLogId = null;
		PanStatus panStatus = null;
		Scheduler scheduler = getSchedulerStatus();

		if (scheduler != null) {
			if (scheduler.getIsRunning().equals(Boolean.TRUE)) {
				LOGGER.error("Error while starting Pan Master scheduler -> Reason : Schduler is alrady running ");
				return;
			}
			try {
				// Step 1 : take the records which is unprocessed by certain condition
				panMasterBulkList = panMasterBulkService.getDataByColumnValue(null, MethodConstants.GET_UNPROCESSED_DATA_AND_UPDATE_IS_PROCESSED_FLAG.getConstantVal());
				LOGGER.info("Inside try block of Pan Master Bulk upload");
				if (CollectionUtils.isEmpty(panMasterBulkList)) {
					LOGGER.info("No Record available to process, Pan master bulk uplaod process");
					return;
				}
				LOGGER.info("Total Taken records size by Pan Master scheduler: {}" + panMasterBulkList.size());
				schedulerLogId = makeSchedulerStartEntry(Long.valueOf(panMasterBulkList.size()), scheduler.getSchedulerId());
				if (schedulerLogId == null) {
					LOGGER.error("Error while starting scheduler ,  Pan master bulk uplaod processa -> Reason : Schduler Log ID not received ");
					return;
				}
			} catch (Exception e) {
				LOGGER.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
				return;
			}

			int totalSuccess = 0;

			MailServiceBean mailServiceBean = null;
			Map<Long, String> validationMap = null;
			// Step 2 : iterate each unprocessed record
			try {
				String filePath = ResourceUtil.getKeyValue(ROOT_PATH) + File.separator + ResourceUtil.getKeyValue(FILE_PATH) + File.separator;
				for (PanMasterBulk panMasterBulk : panMasterBulkList) {
					panMasterBulk.setFilePath(filePath + panMasterBulk.getFileName());
					panMasterBulk.setProcessStartTime(new Date());
					panStatus = new PanStatus();
					panStatus.setPanStatusId(2l);
					panMasterBulk.setStatusId(panStatus);
					// make the status to processing in the db
					panMasterBulkService.add(panMasterBulk);

					validateFileErrorMap = new HashMap<>();
					// step 3 : create the pan master temp bean of each record of the excel file
					LOGGER.info("Pan Management bulk getting bean from excel start #############");
					Map<Long, List<PanMasterTempDto>> panMasterTempListFromExcel = getBeanFromExcel(panMasterBulk);

					if (CollectionUtils.isEmpty(panMasterTempListFromExcel)) {

						panStatus = panStatusRepo.findByPanStatusId(5l);
						panMasterBulk.setStatusId(panStatus);
						panMasterBulk.setProcessEndTime(new Date());
						panMasterBulkService.add(panMasterBulk);

						mailServiceBean = getMailServiceBean(null, panMasterBulk, mailServiceBeanList.size());
						if (mailServiceBean != null) {
							mailServiceBeanList.add(mailServiceBean);
						}
						continue;
					}

					LOGGER.info("Pan Management bulk getting bean from excel End #############@@@@@@@@@@");
					validationMap = new HashMap<>();
					for (Map.Entry<Long, List<PanMasterTempDto>> entry : panMasterTempListFromExcel.entrySet()) {
						if (CollectionUtils.isEmpty(entry.getValue())) { // if row in the sheet is empty then use below
																			// error code
							validationMap.put(1l, "E0829" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0829.toString())));
							validateFileErrorMap.put(entry.getKey(), validationMap);
						}
					}

					/*
					 * we have 2 sheets to read, if both the sheet doesnt contain any row then
					 * failure count will be such excel should not processed further. then update
					 * the status for fail and continue to next record
					 */
					mailServiceBean = new MailServiceBean();
					if (validateFileErrorMap.size() == 2) { //
						panStatus = panStatusRepo.findByPanStatusId(5l);
						panMasterBulk.setStatusId(panStatus);
						writeErrorCodesInExcelFileAndUpdateStatus(validateFileErrorMap, panMasterBulk);
						mailServiceBean = getMailServiceBean(true, panMasterBulk, mailServiceBeanList.size());
						if (mailServiceBean != null) {
							mailServiceBeanList.add(mailServiceBean);
						}
						continue;
					}
					LOGGER.info("Rows are not empty,  Pan master bulk uplaod process");

					if (panMasterBulk.getEntityBean() != null) {
						entityBean = new EntityBean();
						entityBean.setEntityId(panMasterBulk.getEntityBean().getEntityId());
						entityBean.setEntityCode(panMasterBulk.getEntityBean().getEntityCode());
					}

					if (panMasterBulk.getCreatedBy() != null) {
						userMaster = new UserMaster();
						userMaster.setUserId(panMasterBulk.getCreatedBy().getUserId());
						userMaster.setUserName(panMasterBulk.getCreatedBy().getUserName());
					}

					// step 4 : validation the pan details and insert into db
					validateFileErrorMap = validatePanInfoAndInsertIntoTable(panMasterTempListFromExcel, panNumberSet, duplicatePanNumberSet);

					LOGGER.info("size of validateFileErrorMap ::" + validateFileErrorMap.size());
					int successCount = 0;
					if (!CollectionUtils.isEmpty(validateFileErrorMap) && validateFileErrorMap.size() > 0) {
						LOGGER.info("inside if blocck ::" + validateFileErrorMap.size());
						mailServiceBean = new MailServiceBean();

						int totalRecordInExcel = 0;

						for (Map.Entry<Long, Map<Long, String>> entry : validateFileErrorMap.entrySet()) {

							Map<Long, String> newEntry = entry.getValue();
							for (Map.Entry<Long, String> innerMap : newEntry.entrySet()) {
								totalRecordInExcel++;
								if (innerMap.getValue().equalsIgnoreCase(PASS)) {
									successCount++;
								}
							}
						}

						panStatus = new PanStatus();
						if (totalRecordInExcel == successCount) {
							panStatus.setPanStatusId(4l);
						} else if (successCount == 0) {
							panStatus.setPanStatusId(5l);
						} else {
							panStatus.setPanStatusId(3l);
						}
						panStatus = panStatusRepo.findByPanStatusId(panStatus.getPanStatusId());
						panMasterBulk.setStatusId(panStatus);
						panMasterBulk.setTotalRecords((long) totalRecordInExcel);
						panMasterBulk.setNumOfSuccessfull((long) successCount);
						LOGGER.info("final excel write called Total Record in Excel::" + totalRecordInExcel + " pan data total record" + panMasterBulk.getTotalRecords());
						LOGGER.info("final excel write called successCount::" + successCount + "Success count in pab data" + panMasterBulk.getNumOfSuccessfull());
						writeErrorCodesInExcelFileAndUpdateStatus(validateFileErrorMap, panMasterBulk);
						LOGGER.info("final excel write Compeleted,  Pan master bulk uplaod process");
						mailServiceBean = getMailServiceBean(Boolean.FALSE, panMasterBulk, mailServiceBeanList.size());
						if (mailServiceBean != null) {
							mailServiceBeanList.add(mailServiceBean);
						}
					}
					totalSuccess = totalSuccess + successCount;
				}
			} catch (Exception e) {
				LOGGER.error("Exception occoured while process record,  Pan master bulk uplaod process" + e);
			}
			LOGGER.info("data processed successfully via bulk upload scheduler" + jobProcessingId);
			try {
				if (!CollectionUtils.isEmpty(mailServiceBeanList)) {
					String processingId = UUID.randomUUID().toString();
					ServiceResponse serviceResponse = prepareSendMailController.prepareSendEmail(processingId, mailServiceBeanList);
					if (serviceResponse.isStatus()) {
						LOGGER.info("Mail sent successfully");
					}
				}
			} catch (Exception e) {
				LOGGER.error("Exceptoion while sending mail in pan master bulk api" + e);
			}
			LOGGER.info("No Record available to process, Pan master bulk uplaod process");
			makeSchedulerStopEntry(totalSuccess, schedulerLogId, scheduler.getSchedulerId());
		} else {
			LOGGER.error(ErrorConstants.SCHEDULER_INFO_NOT_PRESENT.getConstantVal());
		}
	}

	private Map<String, PanMasterTempDto> convertListToMap(List<PanMasterTemp> panDetailsList) {
		Map<String, PanMasterTempDto> responseDataMap = new HashMap<>();
		panDetailsList.stream().forEach(f -> {
			PanMasterTempDto panMasterTempDto = new PanMasterTempDto();
			panMasterTempDto.setPanNumber(f.getPanNumber());
			panMasterTempDto.setVerificationStatus(f.getVerificationStatus());
			panMasterTempDto.setRbiGenerated(f.getRbiGenerated());
			if (f.getPanIdFk() == null) {
				panMasterTempDto.setPanId(f.getPanId());
			} else {
				panMasterTempDto.setPanId(f.getPanIdFk().getPanId());
			}
			responseDataMap.put(f.getPanNumber(), panMasterTempDto);
		});
		return responseDataMap;
	}

	private MailServiceBean getMailServiceBean(Boolean key, PanMasterBulk panMasterBulk, int number) {
		MailServiceBean mailServiceBean = null;

		try {
			List<DynamicContent> dynamicContents = new ArrayList<>();
			DynamicContent dynamicContent = new DynamicContent();

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(LANG_CODE, "field.panMngt.requestNo"));
			dynamicContent.setValue("BULK-00" + panMasterBulk.getId());
			dynamicContents.add(dynamicContent);

			if (key == null) {
				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(LANG_CODE, "field.panMngt.status"));
				dynamicContent.setValue(ObjectCache.getLabelKeyValue(LANG_CODE, panMasterBulk.getStatusId().getStatus()));
				dynamicContents.add(dynamicContent);

				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(LANG_CODE, "field.reason"));
				dynamicContent.setValue("Invalid File");
				dynamicContents.add(dynamicContent);
			} else if (key) {
				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(LANG_CODE, "field.panMngt.status"));
				dynamicContent.setValue(ObjectCache.getLabelKeyValue(LANG_CODE, panMasterBulk.getStatusId().getStatus()));
				dynamicContents.add(dynamicContent);
			}

			else if (!key) {
				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(LANG_CODE, "field.panMaster.totalRecords"));
				dynamicContent.setValue(panMasterBulk.getTotalRecords().toString());
				dynamicContents.add(dynamicContent);

				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(LANG_CODE, "field.panMaster.noOfSuccess"));
				dynamicContent.setValue(panMasterBulk.getNumOfSuccessfull().toString());
				dynamicContents.add(dynamicContent);

				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(LANG_CODE, "field.panMngt.status"));
				dynamicContent.setValue(ObjectCache.getLabelKeyValue(LANG_CODE, panMasterBulk.getStatusId().getStatus()));
				dynamicContents.add(dynamicContent);
			}

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(LANG_CODE, "grid.createdOn"));
			dynamicContent.setValue(DateManip.convertDateToString(panMasterBulk.getCreatedOn(), ObjectCache.getDateFormat() + " " + ObjectCache.getTimeFormat()));
			dynamicContents.add(dynamicContent);

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(LANG_CODE, "field.panMaster.processStartTime"));
			dynamicContent.setValue(DateManip.convertDateToString(panMasterBulk.getProcessStartTime(), ObjectCache.getDateFormat() + " " + ObjectCache.getTimeFormat()));
			dynamicContents.add(dynamicContent);

			UserRoleMaster userRoleMaster = panMasterBulk.getCreatedBy().getUsrRoleMstrSet().stream().filter(x -> x.getIsActive().equals(Boolean.TRUE)).findFirst().orElse(null);

			Long userRoleMasterId = 0l;
			if (userRoleMaster != null) {
				userRoleMasterId = userRoleMaster.getUserRoleMasterId();
			}

			Map<String, Object> columnValueMap = new HashMap<>();
			columnValueMap.put(ColumnConstants.ENTITYID.getConstantVal(), panMasterBulk.getEntityBean().getEntityId());
			columnValueMap.put(ColumnConstants.USER_ROLE_MASTER_ID.getConstantVal(), userRoleMasterId);
			Map<Integer, List<String>> emailMap = null;
			List<String> mailId = null;
			List<UserEntityRole> userEntitRoleList = userEntityRoleService.getDataByObject(columnValueMap, MethodConstants.GET_DATA_BY_ENTITY_ID_AND_ROLE_MASTER_ID.getConstantVal());
			if (!CollectionUtils.isEmpty(userEntitRoleList)) {
				emailMap = new HashMap<>();
				mailId = new ArrayList<>();
				for (UserEntityRole uEr : userEntitRoleList) {
					mailId.add(uEr.getCompanyEmail());
				}
				emailMap.put(1, mailId);
			} else {
				LOGGER.info("No email id mapped to this entity user, pan mastet bulk upload schedular");
				return mailServiceBean;
			}
			UserRoleMaster userRole = panMasterBulk.getCreatedBy().getUsrRoleMstrSet().stream().findFirst().orElse(null);
			Long userRoleId = 0l;
			if (userRole != null) {
				userRoleId = userRole.getUserRole().getUserRoleId();
			}
			mailServiceBean = new MailServiceBean();
			mailServiceBean.setEmailMap(emailMap);
			mailServiceBean.setAlertId(97l);
			mailServiceBean.setMenuId(186l);
			mailServiceBean.setUserId(panMasterBulk.getCreatedBy().getUserId());
			mailServiceBean.setRoleId(userRoleId);
			mailServiceBean.setUniqueId(Integer.toString(number + 1));
			mailServiceBean.setDynamicContentsList(dynamicContents);

		} catch (Exception e) {
			LOGGER.error("Exception while Sending email for approve reject process for id ", e);
		}
		return mailServiceBean;
	}

	private void makeSchedulerStopEntry(int successfullyProcessed, Long schedulerLogId, Long schedulerId) {
		try {
			if (schedulerLogId != null) {
				SchedulerLog schedulerLog = new SchedulerLog();
				schedulerLog.setSuccessfullyProcessedCount(Long.valueOf(successfullyProcessed));
				schedulerLog.setId(schedulerLogId);
				Scheduler scheduler = new Scheduler();
				scheduler.setSchedulerId(schedulerId);
				scheduler.setIsRunning(false);
				schedulerLog.setSchedulerIdFk(scheduler);
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

	private Map<Long, Map<Long, String>> validatePanInfoAndInsertIntoTable(Map<Long, List<PanMasterTempDto>> panMasterTempListFromExcelMap, Set<String> panNumberSet, Set<String> duplicatePanNumber) {
		LOGGER.info("validatePanInfoAndInsertIntoTable started pan ###############");
		Map<Long, Map<Long, String>> panInfoValidateMap = new HashMap<>();
		Map<Long, String> panInfoValidate = null;
		List<PanMasterTempDto> panMasterTempDtoFromMap = new ArrayList<>();
		List<PanMasterTempDto> panMasterTempDtoFromList = null;
		Map<Long, String> duplicatePanNumberMap = new HashMap<>();
		List<PanMasterTempDto> rbiPanMasterTempDtoFromList = null;
		Map<Long, String> sheet2Map = null;
		List<PanMasterTemp> panDetailsList;
		Map<String, PanMasterTempDto> panMasterMap = null;
		int partiotionData = 0;
		List<List<PanMasterTempDto>> partitionList;
		List<Callable<Map<Long, Map<Long, String>>>> callables;
		List<Future<Map<Long, Map<Long, String>>>> set;
		try {

			for (Map.Entry<Long, List<PanMasterTempDto>> entry : panMasterTempListFromExcelMap.entrySet()) {

				if (entry.getKey() == 0) {
					if (CollectionUtils.isEmpty(entry.getValue())) {
						continue;
					} else {
						panMasterTempDtoFromList = entry.getValue();
						panMasterTempDtoFromList.stream().forEach((x -> {
							if (!duplicatePanNumber.contains(x.getPanNumber())) {
								x.setEntityBean(entityBean);
								x.setUserMaster(userMaster);
								panMasterTempDtoFromMap.add(x);
							} else {
								duplicatePanNumberMap.put(x.getRowNumber(), "E0890" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0890.toString())));
							}

						}));
					}
				} else {
					rbiPanMasterTempDtoFromList = entry.getValue();
				}
			}

			if (panMasterTempDtoFromMap.size() > 1000) {
				partiotionData = panMasterTempDtoFromMap.size() / 10;
			} else {
				partiotionData = 20;
			}

			if (!CollectionUtils.isEmpty(panNumberSet)) {
				panDetailsList = new ArrayList<>();
				panDetailsList = panMasterTempRepo.getDataByPanNum(panNumberSet);
				panMasterMap = new HashMap<>();
				if (!CollectionUtils.isEmpty(panDetailsList)) {
					panMasterMap = convertListToMap(panDetailsList);
				}
			}

			partitionList = Lists.partition(panMasterTempDtoFromMap, partiotionData);
			callables = new ArrayList<>();
			set = new ArrayList<>();
			ExecutorService executorService = Executors.newFixedThreadPool(5);
			for (List<PanMasterTempDto> listPart : partitionList) {

				callables.add(new PanMasterThreadController(listPart, panInfoValidateMap, panMasterController, panMasterMap));
			}

			for (Callable<Map<Long, Map<Long, String>>> callable : callables) {
				Future<Map<Long, Map<Long, String>>> future = executorService.submit(callable);
				set.add(future);
			}

			Map<Long, String> sheet1Map = new HashMap<>();

			for (Future<Map<Long, Map<Long, String>>> futureSet : set) {
				try {
					for (Map.Entry<Long, Map<Long, String>> map : futureSet.get().entrySet()) {
						panInfoValidate = map.getValue();
						if (map.getKey() == 0) {
							for (Map.Entry<Long, String> innerMap : panInfoValidate.entrySet()) {
								sheet1Map.put(innerMap.getKey(), innerMap.getValue());
							}
						}
						//						if (map.getKey() == 1) {
						//							for (Map.Entry<Long, String> innerMap : panInfoValidate.entrySet()) {
						//								sheet2Map.put(innerMap.getKey(), innerMap.getValue());
						//							}
						//						}
					}
				} catch (Exception e) {
					LOGGER.error("Exception while iterating future set()" + e);
				}
			}

			executorService.shutdown();

			if (!CollectionUtils.isEmpty(duplicatePanNumberMap)) {
				for (Map.Entry<Long, String> innerMapForDuplicatePanMap : duplicatePanNumberMap.entrySet()) {
					sheet1Map.put(innerMapForDuplicatePanMap.getKey(), innerMapForDuplicatePanMap.getValue());
				}
			}

			if (!CollectionUtils.isEmpty(sheet1Map)) {
				panInfoValidateMap.put(0l, sheet1Map);
			}

			// process the rbi generated PAN
			if (!CollectionUtils.isEmpty(rbiPanMasterTempDtoFromList)) {

				sheet2Map = validateAndInsertRbiGeneratedPan(rbiPanMasterTempDtoFromList);

				if (!CollectionUtils.isEmpty(sheet2Map)) {
					panInfoValidateMap.put(1l, sheet2Map);
				}
			}

		} catch (Exception e) {
			LOGGER.error("Exception in validatePanInfoAndInsertIntoTable()" + e);
		}
		return panInfoValidateMap;
	}

	private Map<Long, String> validateAndInsertRbiGeneratedPan(List<PanMasterTempDto> rbiPanMasterTempDtoFromList) {

		Map<Long, String> outPutMap = new HashMap<>();
		PanMasterTempDto panMasterTempDtoDbObj = null;
		ServiceResponse serviceResponse = null;

		for (PanMasterTempDto panMasterTempDto : rbiPanMasterTempDtoFromList) {

			String basicValidationStatus = validatePanMasterTempDto(panMasterTempDto);

			if (StringUtils.isNotBlank(basicValidationStatus)) {

				outPutMap.put(panMasterTempDto.getRowNumber(), basicValidationStatus);

				continue;
			}

			panMasterTempDtoDbObj = new PanMasterTempDto();

			if (panMasterTempDtoDbObj.getEntryType() == null) {
				panMasterTempDtoDbObj.setEntryType(panMasterTempDto.getEntryType());
			}
			panMasterTempDtoDbObj.setRbiGenerated(panMasterTempDto.getRbiGenerated());
			panMasterTempDtoDbObj.setPanNumber(panMasterTempDto.getPanNumber());
			panMasterTempDtoDbObj.setBorrowerName(panMasterTempDto.getBorrowerName());
			panMasterTempDtoDbObj.setInstitutionType(panMasterTempDto.getInstitutionType());
			panMasterTempDtoDbObj.setBorrowerAlternateName(panMasterTempDto.getBorrowerAlternateName());
			panMasterTempDtoDbObj.setBorrowerTitle(panMasterTempDto.getBorrowerTitle());
			panMasterTempDtoDbObj.setBorrowerMobile(panMasterTempDto.getBorrowerMobile());
			panMasterTempDtoDbObj.setEntityCode(panMasterTempDto.getEntityCode());
			panMasterTempDtoDbObj.setCreatedBy(panMasterTempDto.getCreatedBy());
			panMasterTempDtoDbObj.setEntityBean(entityBean);
			panMasterTempDtoDbObj.setUserMaster(userMaster);
			panMasterTempDtoDbObj.setIsBulkUpload(true);
			panMasterTempDtoDbObj.setPanId(panMasterTempDto.getPanId());
			LOGGER.info("Pan data insertion called#######" + panMasterTempDto.getRowNumber());
			serviceResponse = panMasterController.insertBorrowerDetails(panMasterTempDtoDbObj);
			LOGGER.info("Pan data insertion called and response received from api########", serviceResponse.getStatusMessage());
			if (serviceResponse.isStatus()) {
				outPutMap.put(panMasterTempDto.getRowNumber(), "PASS");

			} else {
				outPutMap.put(panMasterTempDto.getRowNumber(), serviceResponse.getStatusCode() + ":" + ObjectCache.getLabelKeyValue("en", serviceResponse.getStatusMessage()));
			}

		}
		return outPutMap;
	}

	private String validatePanMasterTempDto(PanMasterTempDto panMasterTempDto) {
		String errorString = null;
		List<String> basicValidation = new ArrayList<>();
		try {

			if (panMasterTempDto.getRbiGenerated() == null) {
				basicValidation.add("E0816" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0816.toString())));
			}

			if (panMasterTempDto.getRbiGenerated() != null && panMasterTempDto.getRbiGenerated()) {

				panMasterTempDto.setEntryType(1);

				if (panMasterTempDto.getInstitutionType() != null && panMasterTempDto.getInstitutionType() == 5) {
					basicValidation.add("E0812" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0812.toString())));
				} else if (panMasterTempDto.getInstitutionType() == null || panMasterTempDto.getInstitutionType().intValue() < 0 || panMasterTempDto.getInstitutionType().intValue() > 4) {
					basicValidation.add("E0839" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0839.toString())));
				}

				if (panMasterTempDto.getBorrowerMobile() == null) {
					basicValidation.add("E0837" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0837.toString())));
				}

			}

			if (StringUtils.isBlank(panMasterTempDto.getBorrowerName())) {
				basicValidation.add("E0765" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0765.toString())));
			}

			if (!StringUtils.isBlank(panMasterTempDto.getBorrowerName()) && panMasterTempDto.getBorrowerName().length() > 100) {
				basicValidation.add("E0850" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0850.toString())));
			}

			if (!StringUtils.isBlank(panMasterTempDto.getBorrowerName()) && !checkIfAnyCharacterPresentInBorrowerName(panMasterTempDto.getBorrowerName())) {
				basicValidation.add("E0854" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0854.toString())));
			}

			if (!StringUtils.isBlank(panMasterTempDto.getBorrowerName()) && checkIfRestrictedCharactersPresentInInputString(panMasterTempDto.getBorrowerName())) {
				basicValidation.add("E0895" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0895.toString())));
			}

			if (!StringUtils.isBlank(panMasterTempDto.getBorrowerAlternateName()) && panMasterTempDto.getBorrowerAlternateName().length() > 100) {
				basicValidation.add("E0851" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0851.toString())));
			}

			if (!StringUtils.isBlank(panMasterTempDto.getBorrowerAlternateName()) && checkIfRestrictedCharactersPresentInInputString(panMasterTempDto.getBorrowerAlternateName())) {
				basicValidation.add("E0896" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0896.toString())));
			}

			if (!StringUtils.isBlank(panMasterTempDto.getBorrowerTitle()) && panMasterTempDto.getBorrowerTitle().length() > 100) {
				basicValidation.add("E0852" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0852.toString())));
			}

			if (!StringUtils.isBlank(panMasterTempDto.getBorrowerTitle()) && checkIfRestrictedCharactersPresentInInputString(panMasterTempDto.getBorrowerTitle())) {
				basicValidation.add("E0897" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0897.toString())));
			}

			if (panMasterTempDto.getBorrowerMobile() != null && panMasterTempDto.getBorrowerMobile() == 1l) {
				basicValidation.add("E0844" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0844.toString())));
			}

			if (panMasterTempDto.getBorrowerMobile() != null && (panMasterTempDto.getBorrowerMobile().longValue() < 1000000000l || panMasterTempDto.getBorrowerMobile().longValue() > 9999999999l)) {
				basicValidation.add("E0766" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0766.toString())));

			}

			if (Validations.isEmpty(panMasterTempDto.getEntityCode())) {
				basicValidation.add("E0108" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0108.toString())));
			}

			if (!CollectionUtils.isEmpty(basicValidation)) {
				errorString = basicValidation.stream().map(Object::toString).collect(Collectors.joining(", "));
			}
		} catch (Exception e) {
			LOGGER.error("exception occoured while validte pan master dto temp", e);
		}
		return errorString;
	}

	private boolean checkIfRestrictedCharactersPresentInInputString(String inputString) {
		Pattern regex = Pattern.compile("[~|&\"\"]");
		if (regex.matcher(inputString).find()) {
			return true;
		}
		return false;
	}

	private boolean checkIfAnyCharacterPresentInBorrowerName(String borrowerName) {
		try {
			borrowerName = borrowerName.replaceAll("(\r\n|\n)", "");
			if (borrowerName.matches(".*[a-zA-Z].*")) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("Error in checkIfAnyCharacterPresentInBorrowerName()", e);
		}
		return false;
	}

	private Map<Long, List<PanMasterTempDto>> getBeanFromExcel(PanMasterBulk panMasterBulk) {
		Map<Long, List<PanMasterTempDto>> panMasterTempMap = new HashMap<>();
		List<PanMasterTempDto> panMasterTempList = null;
		PanMasterTempDto panMasterTempDto = null;
		Sheet sheet;
		panNumberSet = new HashSet<>();
		duplicatePanNumberSet = new HashSet<>();
		try (FileInputStream file = new FileInputStream(new File(panMasterBulk.getFilePath())); Workbook workbook = new XSSFWorkbook(file)) {
			int i;

			for (i = 0; i < 2; i++) {
				panMasterTempList = new ArrayList<>();
				sheet = workbook.getSheetAt(i);
				for (Row row : sheet) {
					if (row.getRowNum() == 0) {
						continue;
					}

					panMasterTempDto = new PanMasterTempDto();
					if (i == 0) {
						for (Cell cell : row) {
							if (cell.getColumnIndex() == 0 && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == cell.CELL_TYPE_STRING) {
								panMasterTempDto.setPanNumber(cell.getStringCellValue());
							} else if (cell.getColumnIndex() == 1 && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == cell.CELL_TYPE_STRING) {
								panMasterTempDto.setBorrowerName(cell.getStringCellValue());
							} else if (cell.getColumnIndex() == 2 && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == cell.CELL_TYPE_STRING) {
								panMasterTempDto.setBorrowerAlternateName(cell.getStringCellValue());
							} else if (cell.getColumnIndex() == 3 && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == cell.CELL_TYPE_STRING) {
								panMasterTempDto.setBorrowerTitle(cell.getStringCellValue());
							} else if (cell.getColumnIndex() == 4 && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
								if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
									panMasterTempDto.setBorrowerMobile((long) cell.getNumericCellValue());
								} else {
									panMasterTempDto.setBorrowerMobile(1l);
								}
							}
						}

						if (StringUtils.isNotBlank(panMasterTempDto.getPanNumber())) {
							if (!panNumberSet.contains(panMasterTempDto.getPanNumber())) {
								panNumberSet.add(panMasterTempDto.getPanNumber());
							} else {
								duplicatePanNumberSet.add(panMasterTempDto.getPanNumber());
							}
						}
						if (validateBean(panMasterTempDto)) {
							panMasterTempDto.setEntityCode(panMasterBulk.getEntityBean().getEntityCode());
							panMasterTempDto.setCreatedBy(panMasterBulk.getCreatedBy().getUserId().intValue());
							panMasterTempDto.setRowNumber((long) row.getRowNum());
							panMasterTempDto.setRbiGenerated(Boolean.FALSE);
							panMasterTempDto.setSheetNumber(0l);
							panMasterTempList.add(panMasterTempDto);
						}

					} else {
						for (Cell cell : row) {
							if (cell.getColumnIndex() == 0 && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == cell.CELL_TYPE_STRING) {
								panMasterTempDto.setBorrowerName(cell.getStringCellValue());
							} else if (cell.getColumnIndex() == 1 && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == cell.CELL_TYPE_STRING) {
								panMasterTempDto.setBorrowerAlternateName(cell.getStringCellValue());
							} else if (cell.getColumnIndex() == 2 && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == cell.CELL_TYPE_STRING) {
								panMasterTempDto.setBorrowerTitle(cell.getStringCellValue());
							} else if (cell.getColumnIndex() == 3 && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == cell.CELL_TYPE_STRING) {
								if (cell.getStringCellValue().equalsIgnoreCase(CENT_GOVT)) {
									panMasterTempDto.setInstitutionType(1);
								} else if (cell.getStringCellValue().equals(FOREIGN)) {
									panMasterTempDto.setInstitutionType(2);
								} else if (cell.getStringCellValue().equals(STATE_GOVT)) {
									panMasterTempDto.setInstitutionType(3);
								} else if (cell.getStringCellValue().equals(TEMP)) {
									panMasterTempDto.setInstitutionType(4);
								} else {
									panMasterTempDto.setInstitutionType(5);
								}
							} else if (cell.getColumnIndex() == 5 && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
								if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
									panMasterTempDto.setBorrowerMobile((long) cell.getNumericCellValue());
								} else {
									panMasterTempDto.setBorrowerMobile(1l);
								}
							}
							panMasterTempDto.setRbiGenerated(true);
						}

						if (validateBean(panMasterTempDto)) {
							panMasterTempDto.setEntityCode(panMasterBulk.getEntityBean().getEntityCode());
							panMasterTempDto.setCreatedBy(panMasterBulk.getCreatedBy().getUserId().intValue());
							panMasterTempDto.setRowNumber((long) row.getRowNum());
							panMasterTempDto.setRbiGenerated(Boolean.TRUE);
							panMasterTempDto.setSheetNumber(1l);
							panMasterTempList.add(panMasterTempDto);
						}
					}
					panMasterTempMap.put((long) i, panMasterTempList);
				}
			}

		} catch (Exception e) {
			LOGGER.error("Exception occoured while reading excel data", e);
		}
		return panMasterTempMap;
	}

	private boolean validateBean(PanMasterTempDto panMasterTempDto) {
		if (StringUtils.isNotBlank(panMasterTempDto.getBorrowerName()) || StringUtils.isNotBlank(panMasterTempDto.getBorrowerAlternateName()) || StringUtils.isNotBlank(panMasterTempDto.getPanNumber()) || StringUtils.isNotBlank(panMasterTempDto.getBorrowerTitle()) || panMasterTempDto.getInstitutionType() != null || panMasterTempDto.getBorrowerMobile() != null) {
			return true;
		}

		return false;
	}

	private void writeErrorCodesInExcelFileAndUpdateStatus(Map<Long, Map<Long, String>> validateFileErrorMap, PanMasterBulk panMasterBulk) {

		try (FileInputStream file = new FileInputStream(new File(panMasterBulk.getFilePath())); Workbook workbook = new XSSFWorkbook(file)) {
			Map<Long, String> newMap;
			Sheet sheet;
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setBorderBottom(BorderStyle.THIN);
			headerCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			headerCellStyle.setBorderRight(BorderStyle.THIN);
			headerCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
			headerCellStyle.setBorderTop(BorderStyle.THIN);
			headerCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			XSSFFont font = (XSSFFont) workbook.createFont();
			font.setBold(true);
			font.setColor(IndexedColors.BLUE.getIndex());
			headerCellStyle.setFont(font);

			CellStyle rowCellStyle = workbook.createCellStyle();
			rowCellStyle.setBorderBottom(BorderStyle.THIN);
			rowCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			rowCellStyle.setBorderRight(BorderStyle.THIN);
			rowCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
			rowCellStyle.setBorderTop(BorderStyle.THIN);
			rowCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

			Cell statusCell;
			Cell reasoncell;

			LOGGER.info("Writting excel file called %%%%%%%%%%%%%%%%%%");
			for (Map.Entry<Long, Map<Long, String>> validateMap : validateFileErrorMap.entrySet()) {
				if (validateMap.getValue() != null) {
					newMap = new HashMap<>();
					sheet = workbook.getSheetAt(validateMap.getKey().intValue());

					//					LOGGER.info("writeErrorCodesInExcelFileAndUpdateStatus() before while loop #############");
					for (Row row : sheet) {

						statusCell = row.createCell(numberOfHeadersInExcelSheets[validateMap.getKey().intValue()]);
						reasoncell = row.createCell(numberOfHeadersInExcelSheets[validateMap.getKey().intValue()] + 1);
						if (row.getRowNum() == 0) {
							statusCell.setCellStyle(headerCellStyle);
							statusCell.setCellValue(PROCESSING_STATUS);
							reasoncell.setCellStyle(headerCellStyle);
							reasoncell.setCellValue(REASON);
						}

						newMap = validateMap.getValue();
						for (Map.Entry<Long, String> entry : newMap.entrySet()) {
							if (entry.getKey() == row.getRowNum()) {
								//								LOGGER.info("Row number in Excel #################" + row.getRowNum());
								if (entry.getValue().equalsIgnoreCase(PASS)) {
									//									LOGGER.info("Row number in Excel #################" + row.getRowNum() + " Pass");

									statusCell.setCellStyle(rowCellStyle);
									reasoncell.setCellStyle(rowCellStyle);
									statusCell.setCellValue(PASS);
									reasoncell.setCellValue("-");
								} else {
									statusCell.setCellStyle(rowCellStyle);
									reasoncell.setCellStyle(rowCellStyle);
									statusCell.setCellValue(FAIL);
									reasoncell.setCellValue(entry.getValue());
								}

							}
						}

					}

					//					LOGGER.info("writeErrorCodesInExcelFileAndUpdateStatus() After while loop #############");
					panMasterBulk.setProcessEndTime(new Date());
					panMasterBulk.setIsProcessed(true);
					panMasterBulk.setIsActive(false);
					panMasterBulkService.add(panMasterBulk);
				}
			}
			try (FileOutputStream out = new FileOutputStream(new File(panMasterBulk.getFilePath()))) {
				// this Writes the workbook
				workbook.write(out);
				LOGGER.info("excel writting successfully");
			} catch (Exception e) {
				LOGGER.error("Error occoured while writting excel sheet , panMasterBulkUploadScheduler", e);
			}

		} catch (Exception e) {
			LOGGER.error("Error in writeErrorCodesInExcelFileAndUpdateStatus()", e);
		}

	}

	private Long makeSchedulerStartEntry(Long totalRecordCount, Long schedulerId) {
		try {
			SchedulerLog schedulerLog = new SchedulerLog();
			schedulerLog.setTakedRecordsCount(totalRecordCount);
			Scheduler scheduler = new Scheduler();
			scheduler.setSchedulerId(schedulerId);
			scheduler.setIsRunning(true);
			schedulerLog.setSchedulerIdFk(scheduler);
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

	private Scheduler getSchedulerStatus() {
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
