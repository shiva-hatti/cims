/**
 * 
 */
package com.iris.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
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
import com.iris.dto.CompanyDto;
import com.iris.dto.DynamicContent;
import com.iris.dto.GroupMasterTempDto;
import com.iris.dto.MailServiceBean;
import com.iris.dto.ServiceResponse;
import com.iris.exception.ServiceException;
import com.iris.model.EntityBean;
import com.iris.model.GroupCompany;
import com.iris.model.GroupMasterBulk;
import com.iris.model.GroupMasterTemp;
import com.iris.model.PanStatus;
import com.iris.model.Scheduler;
import com.iris.model.SchedulerLog;
import com.iris.model.UserEntityRole;
import com.iris.model.UserMaster;
import com.iris.model.UserRoleMaster;
import com.iris.model.WebServiceComponentUrl;
import com.iris.repository.GroupMasterTempRepo;
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
public class GroupMasterSchedulerController {

	static final Logger LOGGER = LogManager.getLogger(GroupMasterSchedulerController.class);

	@Autowired
	private GenericService<WebServiceComponentUrl, Long> webServiceComponentService;

	@Autowired
	private GenericService<GroupMasterBulk, Long> groupMasterBulkService;

	@Autowired
	private PanStatusRepo panStatusRepo;

	@Autowired
	private UserEntityRoleService userEntityRoleService;

	@Autowired
	private GroupMasterController groupMasterController;

	@Autowired
	private PrepareSendMailController prepareSendMailController;

	@Autowired
	private GroupMasterTempRepo groupMasterTempRepo;

	//	private static final String SCHEDULER_CODE = "GROUP_MASTER_SCHEDULER";
	private static final String ROOT_PATH = "filepath.root";
	private static final String FILE_PATH = "filepath.groupMaster.upload";
	private static String LANG_CODE = "en";
	private String jobProcessingId;
	private static final String PASS = "PASS";
	private static final String FAIL = "FAIL";
	private static final String PROCESSING_STATUS = "Status";
	private static final String REASON = "Reason";
	private int[] numberOfHeadersInExcelSheets = new int[] { 5, 6 };
	private EntityBean entityBean = null;
	private UserMaster userMaster = null;
	private Set<String> groupCodeSet = null;
	private Set<String> groupNameSet = null;
	private Set<String> duplicateGroupCodeSet = null;
	private Set<String> duplicateGroupNameSet = null;

	@Value("${scheduler.code.groupMaster}")
	private String schedulerCode;

	@Scheduled(cron = "${cron.groupMasterScheduler}")
	public void groupMasterBulkUplodFiles() {
		jobProcessingId = UUID.randomUUID().toString();
		LOGGER.info("Group Master file validate scheduler started with Job processing ID " + jobProcessingId);
		List<GroupMasterBulk> groupMasterBulkList = null;
		Map<Long, Map<Long, String>> validateFileErrorMap = null;
		List<MailServiceBean> mailServiceBeanList = new ArrayList<>();
		Long schedulerLogId = null;
		PanStatus panStatus = null;
		Scheduler scheduler = getSchedulerStatus();

		if (scheduler != null) {
			if (scheduler.getIsRunning().equals(Boolean.TRUE)) {
				LOGGER.error("Error while starting Group Master scheduler -> Reason : Schduler is alrady running ");
				return;
			}
			try {
				// Step 1 : take the records which is unprocessed by certain condition
				groupMasterBulkList = groupMasterBulkService.getDataByColumnValue(null, MethodConstants.GET_UNPROCESSED_DATA_AND_UPDATE_IS_PROCESSED_FLAG.getConstantVal());
				LOGGER.info("Inside try block of Group Master");
				if (CollectionUtils.isEmpty(groupMasterBulkList)) {
					LOGGER.info("No Record available to process In Group Master");
					return;
				}

				LOGGER.info("Total Taken records size by Group Master scheduler: {}" + groupMasterBulkList.size());
				schedulerLogId = makeSchedulerStartEntry(Long.valueOf(groupMasterBulkList.size()), scheduler.getSchedulerId());
				if (schedulerLogId == null) {
					LOGGER.error("Error while starting scheduler -> Reason : Schduler Log ID not received ");
					return;
				}
			} catch (Exception e) {
				LOGGER.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
			}
			int successfullyProcessed = 0;

			MailServiceBean mailServiceBean = null;
			Map<Long, String> validationMap = null;
			List<GroupMasterTemp> groupCodeList = null;
			List<GroupMasterTemp> groupNameList = null;
			Map<String, GroupMasterTempDto> groupMasterMap = null;
			Map<String, GroupMasterTempDto> groupMasterNameMap = null;

			// Step 2 : iterate each unprocessed record
			try {
				String filePath = ResourceUtil.getKeyValue(ROOT_PATH) + File.separator + ResourceUtil.getKeyValue(FILE_PATH) + File.separator;
				for (GroupMasterBulk groupMasterBulk : groupMasterBulkList) {
					groupMasterBulk.setFilePath(filePath + groupMasterBulk.getFileName());
					groupMasterBulk.setProcessStartTime(new Date());
					panStatus = new PanStatus();
					panStatus.setPanStatusId(2l);
					groupMasterBulk.setStatusId(panStatus);
					// make the status to processing in the db
					groupMasterBulkService.add(groupMasterBulk);

					validateFileErrorMap = new HashMap<>();
					LOGGER.info("Group Management bulk getting bean from excel start #############");
					Map<Long, List<GroupMasterTempDto>> groupMasterTempListFromExcel = getBeanFromExcel(groupMasterBulk);
					LOGGER.info("Group Management bulk getting bean from excel End #############@@@@@@@@@@");

					if (CollectionUtils.isEmpty(groupMasterTempListFromExcel)) {
						panStatus = panStatusRepo.findByPanStatusId(5l);
						groupMasterBulk.setStatusId(panStatus);
						groupMasterBulk.setProcessEndTime(new Date());
						groupMasterBulkService.add(groupMasterBulk);
						mailServiceBean = getMailServiceBean(null, groupMasterBulk, mailServiceBeanList.size());
						if (mailServiceBean != null) {
							mailServiceBeanList.add(mailServiceBean);
						}
						continue;
					}

					validationMap = new HashMap<>();
					for (Map.Entry<Long, List<GroupMasterTempDto>> entry : groupMasterTempListFromExcel.entrySet()) {
						if (CollectionUtils.isEmpty(entry.getValue())) { // if row in the sheet is empty then use below error code
							validationMap.put(1l, "E0829" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0829.toString())));
							validateFileErrorMap.put(entry.getKey(), validationMap);
						}
					}

					/*
					 * we have 2 sheets to read, if both the sheet doesnt contain any row then failure count will 
					 * be such excel should not processed further. then update the status for fail and continue to next record
					 */
					mailServiceBean = new MailServiceBean();
					if (validateFileErrorMap.size() == 2) { // 
						panStatus = panStatusRepo.findByPanStatusId(5l);
						groupMasterBulk.setStatusId(panStatus);
						writeErrorCodesInExcelFileAndUpdateStatus(validateFileErrorMap, groupMasterBulk);
						mailServiceBean = getMailServiceBean(true, groupMasterBulk, mailServiceBeanList.size());
						if (mailServiceBean != null) {
							mailServiceBeanList.add(mailServiceBean);
						}
						continue;
					}
					/*
					 * if any of the sheet is blank then update the such sheet as not row in this sheet
					 */
					LOGGER.info("Rows are not empty");

					if (groupMasterBulk.getEntityBean() != null) {
						entityBean = new EntityBean();
						entityBean.setEntityId(groupMasterBulk.getEntityBean().getEntityId());
						entityBean.setEntityCode(groupMasterBulk.getEntityBean().getEntityCode());
					}

					if (groupMasterBulk.getCreatedBy() != null) {
						userMaster = new UserMaster();
						userMaster.setUserId(groupMasterBulk.getCreatedBy().getUserId());
						userMaster.setUserName(groupMasterBulk.getCreatedBy().getUserName());
					}

					if (!CollectionUtils.isEmpty(groupCodeSet)) {
						groupCodeList = groupMasterTempRepo.getDataByGroupCode(groupCodeSet);
						groupMasterMap = new HashMap<>();
						if (!CollectionUtils.isEmpty(groupCodeList)) {
							groupMasterMap = convertListToMap(groupCodeList);
						}
					}

					if (!CollectionUtils.isEmpty(groupNameSet)) {
						groupNameList = groupMasterTempRepo.getDataByGroupName(groupNameSet);
						groupMasterNameMap = new HashMap<>();
						if (!CollectionUtils.isEmpty(groupNameList)) {
							groupMasterNameMap = convertNameListToMap(groupNameList);
						}
					}

					// step 4 : validation the group master details and insert into db
					validateFileErrorMap = validateGroupInfoAndInsertIntoTable(groupMasterTempListFromExcel, groupMasterMap, groupMasterNameMap, duplicateGroupCodeSet, duplicateGroupNameSet);
					int successCount = 0;
					if (!CollectionUtils.isEmpty(validateFileErrorMap) && validateFileErrorMap.size() > 0) {
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
						groupMasterBulk.setStatusId(panStatus);
						groupMasterBulk.setTotalRecords((long) totalRecordInExcel);
						groupMasterBulk.setNumOfSuccessfull((long) successCount);
						writeErrorCodesInExcelFileAndUpdateStatus(validateFileErrorMap, groupMasterBulk);
						mailServiceBean = getMailServiceBean(Boolean.FALSE, groupMasterBulk, mailServiceBeanList.size());
						if (mailServiceBean != null) {
							mailServiceBeanList.add(mailServiceBean);
						}
					}
					successfullyProcessed = successfullyProcessed + successCount;
				}
			} catch (Exception e) {
				LOGGER.error("Exception occoured while process record", e);
			}
			LOGGER.info("data processed successfully via group master bulk upload scheduler");
			try {
				if (!CollectionUtils.isEmpty(mailServiceBeanList)) {
					String processingId = UUID.randomUUID().toString();
					ServiceResponse serviceResponse = prepareSendMailController.prepareSendEmail(processingId, mailServiceBeanList);
					if (serviceResponse.isStatus()) {
						LOGGER.info("Mail sent successfully");
					}
				}
			} catch (Exception e) {
				LOGGER.error("Exceptoion while sending mail in pan master bulk api", e);
			}
			LOGGER.info("No Record available to process");
			makeSchedulerStopEntry(successfullyProcessed, schedulerLogId, scheduler.getSchedulerId());
		} else {
			LOGGER.error(ErrorConstants.SCHEDULER_INFO_NOT_PRESENT.getConstantVal());
		}
	}

	private Map<String, GroupMasterTempDto> convertNameListToMap(List<GroupMasterTemp> groupNameList) {
		Map<String, GroupMasterTempDto> responseDataMap = new HashMap<>();
		groupNameList.stream().forEach(e -> {
			GroupMasterTempDto groupMasterTempDto = new GroupMasterTempDto();
			groupMasterTempDto.setGroupName(e.getGroupName());
			responseDataMap.put(e.getGroupName(), groupMasterTempDto);
		});
		return responseDataMap;
	}

	private Map<String, GroupMasterTempDto> convertListToMap(List<GroupMasterTemp> groupCodeList) {
		Map<String, GroupMasterTempDto> responseDataMap = new HashMap<>();
		groupCodeList.stream().forEach(f -> {
			GroupMasterTempDto groupMasterTempDto = new GroupMasterTempDto();
			if (f.getGroupIdFk() == null) {
				groupMasterTempDto.setGroupId(f.getGroupId());
			} else {
				groupMasterTempDto.setGroupId(f.getGroupIdFk().getGroupId());
			}
			groupMasterTempDto.setGroupName(f.getGroupName());
			groupMasterTempDto.setGroupCode(f.getGroupCode());
			groupMasterTempDto.setVerificationStatus(f.getVerificationStatus());
			groupMasterTempDto.setAlternateName(f.getAlternateName());
			groupMasterTempDto.setRemark(f.getRemark());
			groupMasterTempDto.setMobileNumber(f.getMobileNumber());
			if (!CollectionUtils.isEmpty(f.getGroupCompanySet())) {
				List<CompanyDto> companyDtoList = new ArrayList<>();
				CompanyDto comDto;
				for (GroupCompany company : f.getGroupCompanySet()) {
					comDto = new CompanyDto();

					comDto.setCompanyId(company.getCompanyId());
					comDto.setCompanyName(company.getCompanyName());
					companyDtoList.add(comDto);
				}
				groupMasterTempDto.setCompanyDtoList(companyDtoList);
			}
			responseDataMap.put(f.getGroupCode(), groupMasterTempDto);
		});
		return responseDataMap;
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

	private Map<Long, Map<Long, String>> validateGroupInfoAndInsertIntoTable(Map<Long, List<GroupMasterTempDto>> groupMasterTempListFromExcel, Map<String, GroupMasterTempDto> groupMasterMap, Map<String, GroupMasterTempDto> groupMasterNameMap, Set<String> multipleGroupCodeSet, Set<String> multipleGroupNameSet) {
		LOGGER.info("validateGroupMasterInfoAndInsertIntoTable started pan ###############");
		Map<Long, Map<Long, String>> groupInfoValidateMap = new HashMap<>();
		Map<Long, String> groupInfoValidate = null;
		GroupMasterTempDto groupMasterTempDtoDbObj = null;
		ServiceResponse serviceResponse = null;
		List<GroupMasterTempDto> groupMasterTempDtoFromMap = null;
		int partiotionData = 0;
		List<List<GroupMasterTempDto>> partitionList;
		try {

			for (Map.Entry<Long, List<GroupMasterTempDto>> entry : groupMasterTempListFromExcel.entrySet()) {

				if (CollectionUtils.isEmpty(entry.getValue())) {
					continue;
				} else {
					groupMasterTempDtoFromMap = entry.getValue();
				}

				if (groupMasterTempDtoFromMap.size() > 1000) {
					partiotionData = groupMasterTempDtoFromMap.size() / 10;
				} else {
					partiotionData = 20;
				}

				groupInfoValidate = new HashMap<>();
				partitionList = Lists.partition(groupMasterTempDtoFromMap, partiotionData);
				for (List<GroupMasterTempDto> listPart : partitionList) {
					for (GroupMasterTempDto groupMasterTempDto : listPart) {

						if (groupMasterTempDto.getSheetNumber() == 0) {
							if (multipleGroupNameSet.contains(groupMasterTempDto.getGroupName())) {
								groupInfoValidate.put(groupMasterTempDto.getRowNumber(), "E0892" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0892.toString())));
								groupInfoValidateMap.put(entry.getKey(), groupInfoValidate);
								continue;
							}
						}

						if (groupMasterTempDto.getSheetNumber() == 1) {
							if (multipleGroupCodeSet.contains(groupMasterTempDto.getGroupCode())) {
								groupInfoValidate.put(groupMasterTempDto.getRowNumber(), "E0893" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0893.toString())));
								groupInfoValidateMap.put(entry.getKey(), groupInfoValidate);
								continue;
							}
						}

						String basicValidationStatus = validateGroupMasterTempDto(groupMasterTempDto, groupMasterMap, groupMasterNameMap);

						if (StringUtils.isNotBlank(basicValidationStatus)) {

							groupInfoValidate.put(groupMasterTempDto.getRowNumber(), basicValidationStatus);
							groupInfoValidateMap.put(entry.getKey(), groupInfoValidate);
							continue;
						}

						groupMasterTempDtoDbObj = new GroupMasterTempDto();
						if (groupMasterTempDtoDbObj.getEntryType() == null) {
							groupMasterTempDtoDbObj.setEntryType(groupMasterTempDto.getEntryType());
						}

						groupMasterTempDtoDbObj.setGroupName(groupMasterTempDto.getGroupName());
						groupMasterTempDtoDbObj.setAlternateName(groupMasterTempDto.getAlternateName());
						groupMasterTempDtoDbObj.setRemark(groupMasterTempDto.getRemark());
						groupMasterTempDtoDbObj.setMobileNumber(groupMasterTempDto.getMobileNumber());
						groupMasterTempDtoDbObj.setCompanyNames(groupMasterTempDto.getCompanyNames());
						groupMasterTempDtoDbObj.setEntityBean(entityBean);
						groupMasterTempDtoDbObj.setUserMaster(userMaster);
						groupMasterTempDtoDbObj.setIsBulkUpload(true);
						groupMasterTempDtoDbObj.setGroupId(groupMasterTempDto.getGroupId());

						if (StringUtils.isNotBlank(groupMasterTempDto.getGroupCode())) {
							groupMasterTempDtoDbObj.setGroupCode(groupMasterTempDto.getGroupCode());
						}

						//					LOGGER.info("Group Master data insertion called#######"+groupMasterTempDto.getRowNumber());
						serviceResponse = groupMasterController.insertGroupdetails(groupMasterTempDtoDbObj);

						if (serviceResponse.isStatus()) {
							groupInfoValidate.put(groupMasterTempDto.getRowNumber(), PASS);

						} else {
							groupInfoValidate.put(groupMasterTempDto.getRowNumber(), serviceResponse.getStatusCode() + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, serviceResponse.getStatusMessage()));
						}
					}
					groupInfoValidateMap.put(entry.getKey(), groupInfoValidate);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception in validateGroupMasterInfoAndInsertIntoTable()", e);
		}
		return groupInfoValidateMap;
	}

	private String validateGroupMasterTempDto(GroupMasterTempDto groupMasterTempDto, Map<String, GroupMasterTempDto> groupMasterMap, Map<String, GroupMasterTempDto> groupMasterNameMap) {

		String errorString = null;
		List<String> basicValidation = new ArrayList<>();
		try {

			if (StringUtils.isBlank(groupMasterTempDto.getGroupName())) {
				basicValidation.add("E0847" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0847.toString())));
			}

			if (StringUtils.isNotBlank(groupMasterTempDto.getGroupName()) && !checkIfAnyCharacterPresent(groupMasterTempDto.getGroupName())) {
				basicValidation.add("E0855" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0855.toString())));
			}

			if (StringUtils.isNotBlank(groupMasterTempDto.getGroupName()) && checkIfRestrictedCharactersPresentInInputString(groupMasterTempDto.getGroupName())) {
				basicValidation.add("E0898" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0898.toString())));
			}

			if (StringUtils.isNotBlank(groupMasterTempDto.getGroupName()) && groupMasterTempDto.getGroupName().length() > 100) {
				basicValidation.add("E0857" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0857.toString())));
			}

			if (StringUtils.isNotBlank(groupMasterTempDto.getAlternateName()) && groupMasterTempDto.getAlternateName().length() > 100) {
				basicValidation.add("E0887" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0887.toString())));
			}

			if (StringUtils.isNotBlank(groupMasterTempDto.getAlternateName()) && checkIfRestrictedCharactersPresentInInputString(groupMasterTempDto.getAlternateName())) {
				basicValidation.add("E0899" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0899.toString())));
			}

			if (StringUtils.isNotBlank(groupMasterTempDto.getRemark()) && groupMasterTempDto.getRemark().length() > 100) {
				basicValidation.add("E0856" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0856.toString())));
			}

			if (StringUtils.isNotBlank(groupMasterTempDto.getRemark()) && checkIfRestrictedCharactersPresentInInputString(groupMasterTempDto.getRemark())) {
				basicValidation.add("E0900" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0900.toString())));
			}

			if (groupMasterTempDto.getMobileNumber() == null) {
				basicValidation.add("E0858" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0858.toString())));
			}

			if (groupMasterTempDto.getMobileNumber() != null && groupMasterTempDto.getMobileNumber() == 1l) {
				basicValidation.add("E0844" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0844.toString())));
			}

			if (groupMasterTempDto.getMobileNumber() != null && (groupMasterTempDto.getMobileNumber().longValue() < 1000000000l || groupMasterTempDto.getMobileNumber().longValue() > 9999999999l)) {
				basicValidation.add("E0766" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0766.toString())));
			}

			if (StringUtils.isBlank(groupMasterTempDto.getCompanyNames())) {
				basicValidation.add("E0843" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0843.toString())));
			}

			if (StringUtils.isNotBlank(groupMasterTempDto.getCompanyNames()) && checkIfRestrictedCharactersPresentInInputString(groupMasterTempDto.getCompanyNames())) {
				basicValidation.add("E0901" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0901.toString())));
			}

			if (groupMasterTempDto.getSheetNumber() == 1) { // sheet 1 for update the record
				if (StringUtils.isNotBlank(groupMasterTempDto.getGroupCode())) {
					if (!groupMasterTempDto.getGroupCode().equals("Invalid")) {

						GroupMasterTempDto groupMasterTempAPIResponseDto = groupMasterMap.get(groupMasterTempDto.getGroupCode());

						if (groupMasterTempAPIResponseDto == null) {
							basicValidation.add("E0894" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0894.toString())));
						}

						if (groupMasterTempAPIResponseDto != null) {
							try {
								if (iSValidUpdationRequest(groupMasterTempAPIResponseDto, groupMasterTempDto)) {
									if (!groupMasterTempAPIResponseDto.getGroupName().equals(groupMasterTempDto.getGroupName())) {
										basicValidation.add("E0849" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0849.toString())));
									}
								} else {
									if (groupMasterTempAPIResponseDto.getGroupName().equals(groupMasterTempDto.getGroupName())) {
										basicValidation.add("E0860" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0860.toString())));
									} else if (!groupMasterTempAPIResponseDto.getGroupName().equals(groupMasterTempDto.getGroupName())) {
										basicValidation.add("E0849" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0849.toString())));
									}

								}

								if (groupMasterTempAPIResponseDto.getVerificationStatus() == 0) {
									basicValidation.add("E0848" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0848.toString())));
								} else {
									groupMasterTempDto.setEntryType(2);
									groupMasterTempDto.setGroupId(groupMasterTempAPIResponseDto.getGroupId());
								}

							} catch (Exception e) {
								LOGGER.error("Exception while parsing API Response in validateGroupMasterTempDto()" + e);
							}

						}
					} else {
						basicValidation.add("E0891" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0891.toString())));
					}
				} else {
					basicValidation.add("E0859" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0859.toString())));
				}
			} else {
				groupMasterTempDto.setEntryType(1);

				if (StringUtils.isNotBlank(groupMasterTempDto.getGroupName()) && !groupMasterTempDto.getGroupName().toUpperCase().endsWith("GROUP")) {
					String groupName = groupMasterTempDto.getGroupName();
					groupName = groupName + " " + "Group";
					if (groupMasterNameMap.get(groupName) != null) {
						basicValidation.add("E0845" + ":" + ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0845.toString())));
					}
				}

			}

			if (!CollectionUtils.isEmpty(basicValidation)) {
				errorString = basicValidation.stream().map(Object::toString).collect(Collectors.joining(", "));
			}

		} catch (Exception e) {
			LOGGER.error("exception occoured while validte group master dto temp", e);
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

	private boolean iSValidUpdationRequest(GroupMasterTempDto groupMasterTempAPIResponseDto, GroupMasterTempDto groupMasterTempDto) {
		boolean flag = false;

		if (!compare(groupMasterTempAPIResponseDto.getAlternateName(), groupMasterTempDto.getAlternateName())) {
			flag = true;
			return flag;
		}
		if (!compare(groupMasterTempAPIResponseDto.getRemark(), groupMasterTempDto.getRemark())) {
			flag = true;
			return flag;
		}
		if (!groupMasterTempAPIResponseDto.getMobileNumber().equals(groupMasterTempDto.getMobileNumber())) {
			flag = true;
			return flag;
		}
		if (!CollectionUtils.isEmpty(groupMasterTempAPIResponseDto.getCompanyDtoList()) && StringUtils.isNotBlank(groupMasterTempDto.getCompanyNames())) {
			String[] cmpSplitArr = groupMasterTempDto.getCompanyNames().split(";");

			List<String> companyList = new ArrayList<>();
			for (String s : cmpSplitArr) {
				if (!Validations.isEmpty(s) && !companyList.contains(s)) {
					companyList.add(s);
				}
			}

			List<String> companyNameDtoList = groupMasterTempAPIResponseDto.getCompanyDtoList().stream().map(f -> f.getCompanyName()).collect(Collectors.toList());

			Collections.sort(companyList);
			Collections.sort(companyNameDtoList);

			if (!companyList.equals(companyNameDtoList)) {
				flag = true;
				return flag;
			}

		}
		return flag;
	}

	private boolean compare(String str1, String str2) {
		return (str1 == null ? str2 == null : str1.equals(str2));
	}

	private boolean checkIfAnyCharacterPresent(String alternateName) {
		try {
			alternateName = alternateName.replaceAll("(\r\n|\n)", "");
			if (alternateName.matches(".*[a-zA-Z].*")) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("Error in checkIfAnyCharacterPresent()", e);
		}
		return false;
	}

	private MailServiceBean getMailServiceBean(Boolean key, GroupMasterBulk groupMasterBulk, int number) {
		MailServiceBean mailServiceBean = new MailServiceBean();
		try {
			List<DynamicContent> dynamicContents = new ArrayList<>();
			DynamicContent dynamicContent = new DynamicContent();

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(LANG_CODE, "field.panMngt.requestNo"));
			dynamicContent.setValue("BULK-00" + groupMasterBulk.getId());
			dynamicContents.add(dynamicContent);

			if (key == null) {
				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(LANG_CODE, "field.panMngt.status"));
				dynamicContent.setValue(ObjectCache.getLabelKeyValue(LANG_CODE, groupMasterBulk.getStatusId().getStatus()));
				dynamicContents.add(dynamicContent);

				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(LANG_CODE, "field.reason"));
				dynamicContent.setValue("Invalid File");
				dynamicContents.add(dynamicContent);
			} else if (key) {
				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(LANG_CODE, "field.panMngt.status"));
				dynamicContent.setValue(ObjectCache.getLabelKeyValue(LANG_CODE, groupMasterBulk.getStatusId().getStatus()));
				dynamicContents.add(dynamicContent);
			}

			if (!key) {
				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(LANG_CODE, "field.panMaster.totalRecords"));
				dynamicContent.setValue(groupMasterBulk.getTotalRecords().toString());
				dynamicContents.add(dynamicContent);

				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(LANG_CODE, "field.panMaster.noOfSuccess"));
				dynamicContent.setValue(groupMasterBulk.getNumOfSuccessfull().toString());
				dynamicContents.add(dynamicContent);

				dynamicContent = new DynamicContent();
				dynamicContent.setLabel(ObjectCache.getLabelKeyValue(LANG_CODE, "field.panMngt.status"));
				dynamicContent.setValue(ObjectCache.getLabelKeyValue(LANG_CODE, groupMasterBulk.getStatusId().getStatus()));
				dynamicContents.add(dynamicContent);
			}

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(LANG_CODE, "grid.createdOn"));
			dynamicContent.setValue(DateManip.convertDateToString(groupMasterBulk.getCreatedOn(), ObjectCache.getDateFormat() + " " + ObjectCache.getTimeFormat()));
			dynamicContents.add(dynamicContent);

			dynamicContent = new DynamicContent();
			dynamicContent.setLabel(ObjectCache.getLabelKeyValue(LANG_CODE, "field.panMaster.processStartTime"));
			dynamicContent.setValue(DateManip.convertDateToString(groupMasterBulk.getProcessStartTime(), ObjectCache.getDateFormat() + " " + ObjectCache.getTimeFormat()));
			dynamicContents.add(dynamicContent);

			UserRoleMaster userRoleMaster = groupMasterBulk.getCreatedBy().getUsrRoleMstrSet().stream().filter(x -> x.getIsActive().equals(Boolean.TRUE)).findFirst().orElse(null);

			Long userRoleMasterId = 0l;
			if (userRoleMaster != null) {
				userRoleMasterId = userRoleMaster.getUserRoleMasterId();
			}

			Map<String, Object> columnValueMap = new HashMap<>();
			columnValueMap.put(ColumnConstants.ENTITYID.getConstantVal(), groupMasterBulk.getEntityBean().getEntityId());
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
				LOGGER.info("No email id mapped to this entity user , group master schedular controller");
				return mailServiceBean;
			}

			UserRoleMaster userRole = groupMasterBulk.getCreatedBy().getUsrRoleMstrSet().stream().findFirst().orElse(null);
			Long userRoleId = 0l;
			if (userRole != null) {
				userRoleId = userRole.getUserRole().getUserRoleId();
			}
			mailServiceBean.setEmailMap(emailMap);
			mailServiceBean.setAlertId(99l);
			mailServiceBean.setMenuId(191l);
			mailServiceBean.setUserId(groupMasterBulk.getCreatedBy().getUserId());
			mailServiceBean.setRoleId(userRoleId);
			mailServiceBean.setUniqueId(Integer.toString(number + 1));
			mailServiceBean.setDynamicContentsList(dynamicContents);

		} catch (Exception e) {
			LOGGER.error("Exception while Sending email for approve reject process for id ", e);
		}
		return mailServiceBean;
	}

	private void writeErrorCodesInExcelFileAndUpdateStatus(Map<Long, Map<Long, String>> validateFileErrorMap, GroupMasterBulk groupMasterBulk) {

		try (FileInputStream file = new FileInputStream(new File(groupMasterBulk.getFilePath())); Workbook workbook = new XSSFWorkbook(file)) {
			LOGGER.info("Writting excel file called %%%%%%%%%%%%%%%%%%");
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

			for (Map.Entry<Long, Map<Long, String>> validateMap : validateFileErrorMap.entrySet()) {

				if (validateMap.getValue() != null) {
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

						Map<Long, String> newMap = validateMap.getValue();
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
									//									LOGGER.info("Row number in Excel #################" + row.getRowNum() + " Failed");
									statusCell.setCellStyle(rowCellStyle);
									reasoncell.setCellStyle(rowCellStyle);
									statusCell.setCellValue(FAIL);
									reasoncell.setCellValue(entry.getValue());
								}

							}
						}
					}

					//					LOGGER.info("writeErrorCodesInExcelFileAndUpdateStatus() After while loop #############");
					groupMasterBulk.setProcessEndTime(new Date());
					groupMasterBulk.setIsProcessed(true);
					groupMasterBulk.setIsActive(false);
					groupMasterBulkService.add(groupMasterBulk);
				}
			}
			try (FileOutputStream out = new FileOutputStream(new File(groupMasterBulk.getFilePath()))) {
				// this Writes the workbook

				workbook.write(out);
				LOGGER.info("excel writting successfully");
			} catch (Exception e) {
				LOGGER.error("Error occoured while writting excel sheet , GroupMasterBulkUploadScheduler", e);
			}
		} catch (Exception e) {
			LOGGER.error("Error in writeErrorCodesInExcelFileAndUpdateStatus()", e);
		}

	}

	private Map<Long, List<GroupMasterTempDto>> getBeanFromExcel(GroupMasterBulk groupMasterBulk) {
		Map<Long, List<GroupMasterTempDto>> groupMasterTempMap = new HashMap<>();
		List<GroupMasterTempDto> groupMasterTempList = null;
		GroupMasterTempDto groupMasterTempDto = null;
		Sheet sheet;
		groupCodeSet = new HashSet<>();
		groupNameSet = new HashSet<>();
		duplicateGroupCodeSet = new HashSet<>();
		duplicateGroupNameSet = new HashSet<>();
		Set<String> groupNameDuplicateCheck = new HashSet<>();
		//		Set<String> groupCodeDuplicateCheck = new HashSet<>();
		try (FileInputStream file = new FileInputStream(new File(groupMasterBulk.getFilePath())); Workbook workbook = new XSSFWorkbook(file)) {
			int i;
			for (i = 0; i < 2; i++) {
				groupMasterTempList = new ArrayList<>();
				String groupName = null;
				// Get first/desired sheet from the workbook
				sheet = workbook.getSheetAt(i);

				for (Row row : sheet) {
					if (row.getRowNum() == 0) {
						continue;
					}

					groupMasterTempDto = new GroupMasterTempDto();

					if (i == 0) {
						for (Cell cell : row) {
							if (cell.getColumnIndex() == 0 && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == cell.CELL_TYPE_STRING) {
								groupMasterTempDto.setGroupName(cell.getStringCellValue());

								groupName = new String();
								if (!groupMasterTempDto.getGroupName().toUpperCase().endsWith("GROUP")) {
									groupName = groupMasterTempDto.getGroupName();
									groupName = groupName + " " + "Group";
									groupNameSet.add(groupName);
								} else {
									groupNameSet.add(groupName);
								}

								if (!groupNameDuplicateCheck.contains(cell.getStringCellValue())) {
									groupNameDuplicateCheck.add(cell.getStringCellValue());
									;
								} else {
									duplicateGroupNameSet.add(groupMasterTempDto.getGroupName());
								}

							} else if (cell.getColumnIndex() == 1 && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == cell.CELL_TYPE_STRING) {
								groupMasterTempDto.setAlternateName(cell.getStringCellValue());
							} else if (cell.getColumnIndex() == 2 && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == cell.CELL_TYPE_STRING) {
								groupMasterTempDto.setRemark(cell.getStringCellValue());
							} else if (cell.getColumnIndex() == 3 && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
								if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
									groupMasterTempDto.setMobileNumber((long) cell.getNumericCellValue());
								} else {
									groupMasterTempDto.setMobileNumber(1l);
								}
							} else if (cell.getColumnIndex() == 4 && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == cell.CELL_TYPE_STRING) {
								groupMasterTempDto.setCompanyNames(cell.getStringCellValue());
							}
						}
						if (validateBean(groupMasterTempDto)) {
							groupMasterTempDto.setEntityCode(groupMasterBulk.getEntityBean().getEntityCode());

							groupMasterTempDto.setCreatedBy(groupMasterBulk.getCreatedBy().getUserId().intValue());
							groupMasterTempDto.setRowNumber((long) row.getRowNum());
							groupMasterTempDto.setSheetNumber(i);
							groupMasterTempList.add(groupMasterTempDto);
						}
					} else {
						for (Cell cell : row) {
							if (cell.getColumnIndex() == 0 && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == cell.CELL_TYPE_STRING) {
								groupMasterTempDto.setGroupName(cell.getStringCellValue());
							} else if (cell.getColumnIndex() == 1 && cell.getCellType() != Cell.CELL_TYPE_BLANK) {

								if (cell.getCellType() == cell.CELL_TYPE_STRING) {
									groupMasterTempDto.setGroupCode(cell.getStringCellValue());
									// groupCodeSet.add(cell.getStringCellValue());
									if (!groupCodeSet.contains(cell.getStringCellValue())) {
										groupCodeSet.add(cell.getStringCellValue());
									} else {
										duplicateGroupCodeSet.add(cell.getStringCellValue());
									}

								} else {
									groupMasterTempDto.setGroupCode("Invalid");
								}
							} else if (cell.getColumnIndex() == 2 && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == cell.CELL_TYPE_STRING) {
								groupMasterTempDto.setAlternateName(cell.getStringCellValue());
							} else if (cell.getColumnIndex() == 3 && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == cell.CELL_TYPE_STRING) {
								groupMasterTempDto.setRemark(cell.getStringCellValue());
							} else if (cell.getColumnIndex() == 4 && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
								if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
									groupMasterTempDto.setMobileNumber((long) cell.getNumericCellValue());
								} else {
									groupMasterTempDto.setMobileNumber(1l);
								}
							} else if (cell.getColumnIndex() == 5 && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == cell.CELL_TYPE_STRING) {
								groupMasterTempDto.setCompanyNames(cell.getStringCellValue());
							}
						}

						if (validateBean(groupMasterTempDto)) {
							groupMasterTempDto.setEntityCode(groupMasterBulk.getEntityBean().getEntityCode());
							groupMasterTempDto.setCreatedBy(groupMasterBulk.getCreatedBy().getUserId().intValue());
							groupMasterTempDto.setRowNumber((long) row.getRowNum());
							groupMasterTempDto.setSheetNumber(i);
							groupMasterTempList.add(groupMasterTempDto);
						}
					}
					groupMasterTempMap.put((long) i, groupMasterTempList);
				}
				//groupMasterTempMap.put((long) i, groupMasterTempList);
			}

		} catch (Exception e) {
			LOGGER.error("Exception occoured while reading excel data", e);
		}
		return groupMasterTempMap;
	}

	private boolean validateBean(GroupMasterTempDto groupMasterTempDto) {
		if (StringUtils.isNotBlank(groupMasterTempDto.getGroupName()) || StringUtils.isNotBlank(groupMasterTempDto.getAlternateName()) || StringUtils.isNotBlank(groupMasterTempDto.getRemark()) || StringUtils.isNotBlank(groupMasterTempDto.getCompanyNames()) || groupMasterTempDto.getMobileNumber() != null || StringUtils.isNotBlank(groupMasterTempDto.getGroupCode())) {
			return true;
		}
		return false;
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
