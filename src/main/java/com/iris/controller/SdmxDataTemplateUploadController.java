/**
 * 
 */
package com.iris.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.format.CellFormat;
import org.apache.poi.ss.format.CellFormatResult;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iris.caching.ObjectCache;
import com.iris.dto.DataTemplateUploadValidationDetail;
import com.iris.dto.ElementDimentionClMappingDetails;
import com.iris.dto.ServiceResponse;
import com.iris.exception.ApplicationException;
import com.iris.exception.ServiceException;
import com.iris.model.ReturnTemplate;
import com.iris.model.Scheduler;
import com.iris.model.SchedulerLog;
import com.iris.model.WebServiceComponentUrl;
import com.iris.repository.ReturnTemplateRepository;
import com.iris.sdmx.codelist.entity.CodeListValues;
import com.iris.sdmx.dimesnsion.entity.DimensionMaster;
import com.iris.sdmx.dimesnsion.entity.Regex;
import com.iris.sdmx.dimesnsion.repo.DimensionRepo;
import com.iris.sdmx.dimesnsion.repo.RegexRepo;
import com.iris.sdmx.element.service.SdmxElementService;
import com.iris.sdmx.elementdimensionmapping.bean.DimCombination;
import com.iris.sdmx.elementdimensionmapping.bean.ElementDimensionStoredJson;
import com.iris.sdmx.elementdimensionmapping.repo.ElementDimensionRepo;
import com.iris.sdmx.exceltohtml.bean.DimensionCodeListValueBean;
import com.iris.sdmx.exceltohtml.bean.DimensionDetailCategories;
import com.iris.sdmx.exceltohtml.bean.ModelOtherDetails;
import com.iris.sdmx.exceltohtml.bean.ProcessUploadInputBean;
import com.iris.sdmx.exceltohtml.bean.RegexDetails;
import com.iris.sdmx.exceltohtml.bean.SdmxEleDimTypeMapBean;
import com.iris.sdmx.exceltohtml.bean.SdmxModelCodesBean;
import com.iris.sdmx.exceltohtml.bean.SdmxReturnModelInfoBean;
import com.iris.sdmx.exceltohtml.bean.SdmxReturnPreviewBean;
import com.iris.sdmx.exceltohtml.bean.SdmxReturnSheetInfoBean;
import com.iris.sdmx.exceltohtml.entity.SdmxModelCodesEntity;
import com.iris.sdmx.exceltohtml.entity.SdmxReturnModelInfoEntity;
import com.iris.sdmx.exceltohtml.entity.SdmxReturnPreviewEntity;
import com.iris.sdmx.exceltohtml.helper.SdmxModelCodesHelper;
import com.iris.sdmx.exceltohtml.repo.SdmxReturnModelInfoRepo;
import com.iris.sdmx.exceltohtml.service.SdmxFormulaService;
import com.iris.sdmx.exceltohtml.service.SdmxModelCodesService;
import com.iris.sdmx.exceltohtml.service.SdmxReturnModelInfoService;
import com.iris.sdmx.exceltohtml.service.SdmxReturnPreviewService;
import com.iris.sdmx.exceltohtml.service.SdmxReturnSheetInfoService;
import com.iris.sdmx.lockrecord.bean.SdmxLockRecordSetBean;
import com.iris.sdmx.lockrecord.bean.SdmxLockRecordStatusCheckBean;
import com.iris.sdmx.lockrecord.helper.SdmxLockRecordHelper;
import com.iris.sdmx.lockrecord.service.SdmxLockRecordService;
import com.iris.sdmx.status.entity.SdmxModuleDetailEntity;
import com.iris.sdmx.status.entity.SdmxModuleStatus;
import com.iris.sdmx.status.service.SdmxModuleStatusService;
import com.iris.sdmx.util.SDMXConstants;
import com.iris.service.GenericService;
import com.iris.util.JsonUtility;
import com.iris.util.ResourceUtil;
import com.iris.util.SdmxModuleStatusCodeEnum;
import com.iris.util.Validations;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;
import com.iris.webservices.client.CIMSRestWebserviceClient;

/**
 * @author apagaria
 *
 */
@RestController
public class SdmxDataTemplateUploadController {

	/**
	 * 
	 */
	private static final Logger LOGGER = LogManager.getLogger(SdmxDataTemplateUploadController.class);

	/**
	 * 
	 */
	@Autowired
	private GenericService<WebServiceComponentUrl, Long> webServiceComponentService;

	/**
	 * 
	 */
	@Autowired
	private SdmxReturnModelInfoService sdmxReturnModelInfoService;

	@Autowired
	private SdmxElementService sdmxElementService;

	@Autowired
	private SdmxLockRecordService sdmxLockRecordService;

	/**
	 * 
	 */
	@Autowired
	private SdmxModelCodesService sdmxModelCodesService;

	/**
	 * 
	 */
	@Autowired
	private SdmxReturnPreviewService sdmxReturnPreviewService;

	/**
	 * 
	 */
	@Autowired
	private SdmxModuleStatusService sdmxModuleStatusService;

	@Autowired
	private RegexRepo regexRepo;

	@Autowired
	ReturnTemplateRepository returnTemplateRepository;

	@Autowired
	private SdmxModelCodesHelper sdmxModelCodesHelper;

	@Autowired
	private ElementDimensionRepo elementDimensionRepo;

	@Autowired
	private DimensionRepo dimensionRepo;

	@Autowired
	private SdmxReturnModelInfoRepo sdmxReturnModelInfoRepo;

	@Autowired
	private SdmxFormulaService sdmxFormulaService;

	// private static final String HTML_EXT = ".html";

	private static final String XLSX_EXT = ".xlsx";
	private static String LANG_CODE = "en";
	private static final String COMMON_STR = "Common";
	private static final String OPEN_STR = "Open";
	private static final String CLOSED_STR = "Closed";
	private static final String ELEMENT_VERSION_STR = "@VER#";
	private static final String DIMENSION_TYPE_STR = "@type#";
	private static final String COMMENT_STR = "COMMENT";
	private static final String ELEMENT_STR = "[ELEMENT]";
	private static final String CODE_LIST_DIMENSION_STR = "[CODELIST DIMENSION]";
	private static final String OPEN_DIMENSION_STR = "[OPEN DIMENSION]";
	private static final String COMMON_DIMENSION_STR = "[COMMON DIMENSION]";
	private static final String OTHER_DETAIL_STR = "[OTHER DETAILS]";
	private static final String DEPENDENCY_TYPE_STR = "DEPENDENCY_TYPE";
	/**
	 * 
	 */
	@Autowired
	private SdmxReturnSheetInfoService sdmxReturnSheetInfoService;

	@Value("${scheduler.code.sdmxDataTemplate}")
	private String schedulerCode;

	@Scheduled(cron = "${cron.data.template.upload}")
	public void processUploadedDataTemplate() {
		String jobProcessingId = UUID.randomUUID().toString();
		LOGGER.info("Scheduler started with Job processing ID {}", jobProcessingId);
		Long schedulerLogId = null;
		// Fetching Schedulers
		LOGGER.debug("Fetching schedulers with Job processing ID {}", jobProcessingId);
		Scheduler scheduler = getSchedulerStatus(jobProcessingId);
		if (scheduler != null) {
			// Checking scheduler is running or not
			if (scheduler.getIsRunning().equals(Boolean.TRUE)) {
				LOGGER.error("SdmxDataTemplateUploadController Error while starting scheduler -> Reason : Schduler is alrady running ");
				return;
			}

			List<SdmxReturnPreviewBean> sdmxReturnPreviewBeanList = null;

			try {
				// Fetching list of un processed record
				LOGGER.debug("SdmxDataTemplateUploadController Fetching list of un processed record with Job processing ID {}", jobProcessingId);
				sdmxReturnPreviewBeanList = sdmxReturnPreviewService.fetchEntityByStatusId(1, true);
				if (!CollectionUtils.isEmpty(sdmxReturnPreviewBeanList)) {
					// Making scheduler start entry
					schedulerLogId = makeSchedulerStartEntry(new Long(sdmxReturnPreviewBeanList.size()), scheduler.getSchedulerId(), jobProcessingId);
					if (schedulerLogId == null) {
						LOGGER.error("SdmxDataTemplateUploadController Error while starting scheduler -> Reason : Schduler Log ID not received ");
						return;
					}
				} else {
					LOGGER.info(jobProcessingId + " - SdmxDataTemplateUploadController no record to process for scheduler");
					return;
				}
			} catch (Exception e) {
				LOGGER.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
				return;
			}
			int successfullyProcessedRecord = 0;
			// Start Processing the records
			for (SdmxReturnPreviewBean sdmxReturnPreviewBean : sdmxReturnPreviewBeanList) {
				if (sdmxReturnPreviewBean != null) {
					// Status Map
					// Setting Module 1 as hard code to fetch module specific status and create a
					// map
					Map<String, SdmxModuleStatus> sdmxModuleStatusMap = fetchModuleSpecificStatusMap();

					// Convert JSON into
					ProcessUploadInputBean processUploadInputBean = new Gson().fromJson(sdmxReturnPreviewBean.getOtherDetailJson(), ProcessUploadInputBean.class);

					ReturnTemplate returnTemplate = returnTemplateRepository.findActiveReturnTemplate(sdmxReturnPreviewBean.getReturnTemplateIdFk());
					// if return template is active
					if (returnTemplate != null) {
						LOGGER.debug("Return Template is active - Job processing ID {}", jobProcessingId);

						// Lock table column
						String recordDetailEncodedStr = null;
						DataTemplateUploadValidationDetail dataTemplateUploadValidationDetail = new DataTemplateUploadValidationDetail();
						try {
							LOGGER.info(" START : @saveSdmxReturnSheetInfo with Job processing ID {}", jobProcessingId);
							saveSdmxReturnSheetInfo(processUploadInputBean, sdmxReturnPreviewBean.getCreatedBy(), sdmxReturnPreviewBean.getReturnTemplateIdFk(), sdmxReturnPreviewBean, sdmxModuleStatusMap, processUploadInputBean.getOldReturnPreviewId(), dataTemplateUploadValidationDetail);
							LOGGER.info(" END : @saveSdmxReturnSheetInfo with Job processing ID {}", jobProcessingId);
							// Uploaded Return Lock release
							// Request Bean
							SdmxLockRecordStatusCheckBean sdmxLockRecordStatusCheckBean = new SdmxLockRecordStatusCheckBean();

							Map<String, Long> otherDetailMap = new HashMap<>();

							otherDetailMap.put("returnTemplateId", sdmxReturnPreviewBean.getReturnTemplateIdFk());
							sdmxLockRecordStatusCheckBean.setRecordDetailEncodedJson(Base64.encodeBase64String((new Gson().toJson(otherDetailMap)).getBytes()));
							// Sorting and Base 64 conversion
							recordDetailEncodedStr = SdmxLockRecordHelper.sortingNBase64FromRecordDetailJsonString(sdmxLockRecordStatusCheckBean.getRecordDetailEncodedJson(), jobProcessingId);

							// Check Lock Status
							sdmxLockRecordService.findNSaveByRecordDetailEncodeNActive(recordDetailEncodedStr, Boolean.TRUE, null, jobProcessingId);

							// Relative Return Lock release
							if (!StringUtils.isBlank(processUploadInputBean.getOldReturnTemplateId())) {
								LOGGER.debug("Relative Return Lock release with Job processing ID {}", jobProcessingId);
								sdmxLockRecordStatusCheckBean = new SdmxLockRecordStatusCheckBean();
								otherDetailMap = new HashMap<>();
								otherDetailMap.put("returnTemplateId", Long.parseLong(processUploadInputBean.getOldReturnTemplateId()));
								otherDetailMap.put("returnPreviewId", Long.parseLong(processUploadInputBean.getOldReturnPreviewId()));
								sdmxLockRecordStatusCheckBean.setRecordDetailEncodedJson(Base64.encodeBase64String((new Gson().toJson(otherDetailMap)).getBytes()));
								// Sorting and Base 64 conversion
								recordDetailEncodedStr = SdmxLockRecordHelper.sortingNBase64FromRecordDetailJsonString(sdmxLockRecordStatusCheckBean.getRecordDetailEncodedJson(), jobProcessingId);

								// Check Lock Status
								sdmxLockRecordService.findNSaveByRecordDetailEncodeNActive(recordDetailEncodedStr, Boolean.TRUE, null, jobProcessingId);
								successfullyProcessedRecord++;

							}
						} catch (ApplicationException ae) {
							// Update return preview status
							SdmxModuleStatus sdmxModuleStatus = sdmxModuleStatusMap.get(SdmxModuleStatusCodeEnum.UPLOAD_FAILED.getStatusCode());
							sdmxReturnPreviewBean.setModuleStatusId(sdmxModuleStatus.getModuleStatusId());
							sdmxReturnPreviewBean.setModuleStatusMessage(sdmxModuleStatus.getModuleStatusLabel());
							sdmxReturnPreviewBean.setIsActive(false);
							if (dataTemplateUploadValidationDetail != null) {
								if (!StringUtils.isBlank((dataTemplateUploadValidationDetail.getErrorCode())) || !(CollectionUtils.isEmpty(dataTemplateUploadValidationDetail.getFormulaValidationBeanList()))) {
									sdmxReturnPreviewBean.setValidationJson(new Gson().toJson(dataTemplateUploadValidationDetail));

								}
							}
							sdmxReturnPreviewService.saveEntityByBean(sdmxReturnPreviewBean);
							// Check Lock Status
							sdmxLockRecordService.findNSaveByRecordDetailEncodeNActive(recordDetailEncodedStr, Boolean.TRUE, null, jobProcessingId);
							// Relative Return Lock release
							if (!StringUtils.isBlank(processUploadInputBean.getOldReturnTemplateId())) {
								LOGGER.debug("Relative Return Lock release with Job processing ID {}", jobProcessingId);
								SdmxLockRecordStatusCheckBean sdmxLockRecordStatusCheckBean = new SdmxLockRecordStatusCheckBean();
								Map<String, Long> otherDetailMap = new HashMap<>();
								otherDetailMap.put("returnTemplateId", Long.parseLong(processUploadInputBean.getOldReturnTemplateId()));
								otherDetailMap.put("returnPreviewId", Long.parseLong(processUploadInputBean.getOldReturnPreviewId()));
								sdmxLockRecordStatusCheckBean.setRecordDetailEncodedJson(Base64.encodeBase64String((new Gson().toJson(otherDetailMap)).getBytes()));
								// Sorting and Base 64 conversion
								recordDetailEncodedStr = SdmxLockRecordHelper.sortingNBase64FromRecordDetailJsonString(sdmxLockRecordStatusCheckBean.getRecordDetailEncodedJson(), jobProcessingId);

								// Check Lock Status
								sdmxLockRecordService.findNSaveByRecordDetailEncodeNActive(recordDetailEncodedStr, Boolean.TRUE, null, jobProcessingId);
							}
							LOGGER.error("Exception with sdmx scheduler - ", ae);

						} catch (Exception e) {
							// Update return preview status
							SdmxModuleStatus sdmxModuleStatus = sdmxModuleStatusMap.get(SdmxModuleStatusCodeEnum.UPLOAD_FAILED.getStatusCode());
							sdmxReturnPreviewBean.setModuleStatusId(sdmxModuleStatus.getModuleStatusId());
							sdmxReturnPreviewBean.setModuleStatusMessage(sdmxModuleStatus.getModuleStatusLabel());
							sdmxReturnPreviewBean.setIsActive(false);
							sdmxReturnPreviewService.saveEntityByBean(sdmxReturnPreviewBean);
							// Check Lock Status
							sdmxLockRecordService.findNSaveByRecordDetailEncodeNActive(recordDetailEncodedStr, Boolean.TRUE, null, jobProcessingId);
							// Relative Return Lock release
							if (!StringUtils.isBlank(processUploadInputBean.getOldReturnTemplateId())) {
								LOGGER.debug("Relative Return Lock release with Job processing ID {}", jobProcessingId);
								SdmxLockRecordStatusCheckBean sdmxLockRecordStatusCheckBean = new SdmxLockRecordStatusCheckBean();
								Map<String, Long> otherDetailMap = new HashMap<>();
								otherDetailMap.put("returnTemplateId", Long.parseLong(processUploadInputBean.getOldReturnTemplateId()));
								otherDetailMap.put("returnPreviewId", Long.parseLong(processUploadInputBean.getOldReturnPreviewId()));
								sdmxLockRecordStatusCheckBean.setRecordDetailEncodedJson(Base64.encodeBase64String((new Gson().toJson(otherDetailMap)).getBytes()));
								// Sorting and Base 64 conversion
								recordDetailEncodedStr = SdmxLockRecordHelper.sortingNBase64FromRecordDetailJsonString(sdmxLockRecordStatusCheckBean.getRecordDetailEncodedJson(), jobProcessingId);

								// Check Lock Status
								sdmxLockRecordService.findNSaveByRecordDetailEncodeNActive(recordDetailEncodedStr, Boolean.TRUE, null, jobProcessingId);
							}
							LOGGER.error("Exception with sdmx scheduler - ", e);
						}
					} else {
						// Update return preview status
						SdmxModuleStatus sdmxModuleStatus = sdmxModuleStatusMap.get(SdmxModuleStatusCodeEnum.UPLOAD_FAILED.getStatusCode());
						sdmxReturnPreviewBean.setModuleStatusId(sdmxModuleStatus.getModuleStatusId());
						sdmxReturnPreviewBean.setModuleStatusMessage(sdmxModuleStatus.getModuleStatusLabel());
						sdmxReturnPreviewBean.setIsActive(false);
						sdmxReturnPreviewService.saveEntityByBean(sdmxReturnPreviewBean);

						// Relative Return Lock release
						if (!StringUtils.isBlank(processUploadInputBean.getOldReturnTemplateId())) {
							LOGGER.debug("Relative Return Lock release with Job processing ID {}", jobProcessingId);
							SdmxLockRecordStatusCheckBean sdmxLockRecordStatusCheckBean = new SdmxLockRecordStatusCheckBean();
							Map<String, Long> otherDetailMap = new HashMap<>();
							otherDetailMap.put("returnTemplateId", Long.parseLong(processUploadInputBean.getOldReturnTemplateId()));
							otherDetailMap.put("returnPreviewId", Long.parseLong(processUploadInputBean.getOldReturnPreviewId()));
							sdmxLockRecordStatusCheckBean.setRecordDetailEncodedJson(Base64.encodeBase64String((new Gson().toJson(otherDetailMap)).getBytes()));
							// Sorting and Base 64 conversion
							String recordDetailEncodedStr = SdmxLockRecordHelper.sortingNBase64FromRecordDetailJsonString(sdmxLockRecordStatusCheckBean.getRecordDetailEncodedJson(), jobProcessingId);

							// Check Lock Status
							sdmxLockRecordService.findNSaveByRecordDetailEncodeNActive(recordDetailEncodedStr, Boolean.TRUE, null, jobProcessingId);
						}

						LOGGER.debug("Return template is not active - Job processing ID {}", jobProcessingId);
					}
				}
			}
			makeSchedulerStopEntry(successfullyProcessedRecord, schedulerLogId, scheduler.getSchedulerId(), jobProcessingId);
		} else {
			LOGGER.error(ErrorConstants.SCHEDULER_INFO_NOT_PRESENT.getConstantVal());
		}
	}

	private Map<String, SdmxModuleStatus> fetchModuleSpecificStatusMap() {
		// Setting Module 1 as hard code to fetch module specific status and create a
		// map
		SdmxModuleDetailEntity moduleDetailEntity = new SdmxModuleDetailEntity(1L);

		Map<String, SdmxModuleStatus> sdmxModuleStatusMap = sdmxModuleStatusService.findModuleStatusByModuleIdNActive(moduleDetailEntity, true);

		return sdmxModuleStatusMap;
	}

	@SuppressWarnings("unlikely-arg-type")
	private void deactivateOldTemplate(Long returnTemplateId, Long currentReturnPreviewTypeId, Map<String, SdmxModuleStatus> sdmxModuleStatusMap) {
		// Deactivate old data templates
		ReturnTemplate returnTemplate = new ReturnTemplate();
		returnTemplate.setReturnTemplateId(returnTemplateId);
		List<SdmxReturnPreviewEntity> sdmxReturnPreviewEntities = sdmxReturnPreviewService.fetchActiveByReturnTemplateId(returnTemplate, true);
		for (SdmxReturnPreviewEntity sdmxReturnPreviewEntity : sdmxReturnPreviewEntities) {
			if (currentReturnPreviewTypeId != sdmxReturnPreviewEntity.getReturnPreviewTypeId()) {
				// Deactivate Return Preview records
				sdmxReturnPreviewEntity.setIsActive(false);
				/*
				 * sdmxReturnPreviewEntity.setModuleStatusIdFk(
				 * sdmxModuleStatusMap.get(SdmxModuleStatusCodeEnum.UPLOAD_FAILED.getStatusCode(
				 * )));
				 */
				sdmxReturnPreviewService.add(sdmxReturnPreviewEntity);

				// Delete return model info entries
				List<SdmxModelCodesEntity> modelCodesEntityList = new ArrayList<>();
				Long returnPreviewId = 12L;// dummy value
				List<SdmxReturnModelInfoEntity> sdmxReturnModelInfoEntityList = sdmxReturnModelInfoService.findListByReturnTemplate(returnTemplate, returnPreviewId);
				for (SdmxReturnModelInfoEntity sdmxReturnModelInfoEntity : sdmxReturnModelInfoEntityList) {
					modelCodesEntityList.add(sdmxReturnModelInfoEntity.getModelCodesIdFk());
					sdmxReturnModelInfoEntity.setIsActive(Boolean.FALSE);
					sdmxReturnModelInfoService.add(sdmxReturnModelInfoEntity);
				}

				// Deactivate unmapped mode code ids
				for (SdmxModelCodesEntity sdmxModelCodesEntity : modelCodesEntityList) {
					if (!sdmxReturnModelInfoService.getBeanByModelCode(sdmxModelCodesEntity.getModelCodesId())) {
						sdmxModelCodesEntity.setIsActive(Boolean.FALSE);
						sdmxModelCodesService.add(sdmxModelCodesEntity);
					}
				}
			}
		}
	}

	private void saveSdmxReturnSheetInfo(ProcessUploadInputBean processUploadInputBean, Long userId, Long returnTemplateId, SdmxReturnPreviewBean sdmxReturnPreviewBean, Map<String, SdmxModuleStatus> sdmxModuleStatusMap, String oldReturnPreviewId, DataTemplateUploadValidationDetail dataTemplateUploadValidationDetail) throws EncryptedDocumentException, InvalidFormatException, IOException, Exception {

		// Update return preview status
		SdmxModuleStatus sdmxModuleStatus = sdmxModuleStatusMap.get(SdmxModuleStatusCodeEnum.IN_PROGRESS.getStatusCode());
		sdmxReturnPreviewBean.setModuleStatusId(sdmxModuleStatus.getModuleStatusId());
		sdmxReturnPreviewBean.setModuleStatusMessage(sdmxModuleStatus.getModuleStatusLabel());
		sdmxReturnPreviewService.saveEntityByBean(sdmxReturnPreviewBean);
		Long returnPreviewId = sdmxReturnPreviewBean.getReturnPreviewTypeId();
		// Deactivate Other data template of same return template id
		// Deactivate model entry also
		/*
		 * deactivateOldTemplate(returnTemplateId,
		 * sdmxReturnPreviewBean.getReturnPreviewTypeId(), sdmxModuleStatusMap);
		 */

		Type listType = new TypeToken<ArrayList<SdmxReturnSheetInfoBean>>() {
		}.getType();

		// return sheet info details
		List<SdmxReturnSheetInfoBean> sdmxReturnSheetInfoBeanList = new Gson().fromJson(new String(Base64.decodeBase64(processUploadInputBean.getSheetInfoBean().getBytes("utf-8"))), listType);
		Type MapType = new TypeToken<Map<String, List<Integer>>>() {
		}.getType();
		// sheet cell mapping
		Map<String, List<Integer>> sheetCellMapping = new Gson().fromJson(processUploadInputBean.getSheetCellMappingJson(), MapType);

		Map<Integer, SdmxReturnModelInfoEntity> oldCellRefWithModelCodeMap = new HashMap<>();
		Map<String, Long> sheetCodeNewIdMap = new HashMap<>();
		int isMandatoryCount = 0;
		sdmxReturnSheetInfoService.saveEntityByBean(sdmxReturnSheetInfoBeanList, returnTemplateId, userId, oldCellRefWithModelCodeMap, sheetCodeNewIdMap, sheetCellMapping, processUploadInputBean.getOldReturnTemplateId(), sdmxReturnPreviewBean, oldReturnPreviewId);

		if (StringUtils.isBlank(processUploadInputBean.getOldReturnTemplateId())) {
			String xlsFilePath = ResourceUtil.getKeyValue("filepath.root") + File.separator + ResourceUtil.getKeyValue("excel.to.html.xls.base.path") + File.separator + processUploadInputBean.getReturnCode() + File.separator + processUploadInputBean.getFileName() + XLSX_EXT;

			InputStream in = new FileInputStream(xlsFilePath);
			Workbook wb = null;
			Map<Integer, String> cellRefJsonMapping = new HashMap<>();
			Map<Integer, String> cellRefRegexMapping = new HashMap<>();
			Map<Integer, String> cellRefIsMandatoryMap = new HashMap<>();

			try {
				wb = WorkbookFactory.create(in);
				Sheet sheet = wb.getSheetAt(0);
				Iterator<Row> rows = sheet.rowIterator();

				// int rowCount = 0;
				while (rows.hasNext()) {
					// rowCount++;
					Row row = rows.next();
					// String labelValue = "";
					row.getLastCellNum();
					Cell cell = row.getCell(2);

					if (cell != null) {
						CellStyle style = cell.getCellStyle();
						// Set the value that is rendered for the cell
						// also applies the format
						CellFormat cf = CellFormat.getInstance(style.getDataFormatString());
						CellFormatResult result = cf.apply(cell);
						String content = result.text;
						if (!StringUtils.isBlank(content)) {
							cell = row.getCell(4);
							style = cell.getCellStyle();
							// Set the value that is rendered for the cell
							// also applies the format
							cf = CellFormat.getInstance(style.getDataFormatString());
							result = cf.apply(cell);
							if (!StringUtils.isBlank(result.text)) {
								if (NumberUtils.isDigits(content)) {
									cellRefJsonMapping.put(Integer.parseInt(content), result.text);
									cell = row.getCell(3);
									if (cell != null) {
										style = cell.getCellStyle();
										// Set the value that is rendered for the cell
										// also applies the format
										cf = CellFormat.getInstance(style.getDataFormatString());
										result = cf.apply(cell);
										cellRefRegexMapping.put(Integer.parseInt(content), result.text);
									} else {
										cellRefRegexMapping.put(Integer.parseInt(content), "");
									}

								}
							}

							Cell isMandatoryCell = row.getCell(3);
							if (isMandatoryCell != null) {
								CellStyle isMandatoryCellstyle = isMandatoryCell.getCellStyle();
								CellFormat isMandatoryCf = CellFormat.getInstance(isMandatoryCellstyle.getDataFormatString());
								CellFormatResult isMandatoryResult = isMandatoryCf.apply(isMandatoryCell);
								String isMandatory = isMandatoryResult.text;
								if (!StringUtils.isBlank(isMandatory)) {
									if (NumberUtils.isDigits(content)) {
										cellRefIsMandatoryMap.put(Integer.parseInt(content), isMandatory);
									}
								}
							}
						}
					}
				}
			} catch (Exception ex) {
				throw ex;
			} finally {
				wb.close();
			}
			List<Regex> regexMaster = regexRepo.findAll();
			Map<String, Integer> regexMasterMap = new HashMap<>();
			for (Regex regex : regexMaster) {
				regexMasterMap.put(regex.getRegex(), regex.getRegexId());
			}
			Map<String, ElementDimentionClMappingDetails> elementDimensionMap = new HashMap<>();
			for (Map.Entry<Integer, String> entry : cellRefJsonMapping.entrySet()) {
				String regex = "";
				Gson gson = new Gson();
				// Dimension Grouping
				SdmxEleDimTypeMapBean sdmxEleDimTypeMapBean = new SdmxEleDimTypeMapBean();
				Map<String, Object> responseMap = convertEleSDMXModelToJSON(entry.getValue(), gson, sdmxEleDimTypeMapBean, sdmxReturnPreviewBean.getAgencyMasterCode(), dataTemplateUploadValidationDetail, entry.getKey(), elementDimensionMap);
				DimensionDetailCategories sdmxReturnModelBean = null;
				DimensionDetailCategories sdmxReturnModelHashBean = null;

				if (!MapUtils.isEmpty(responseMap)) {
					sdmxReturnModelBean = (DimensionDetailCategories) responseMap.get("SDMX_RETURN_MODEL_BEAN");
					sdmxReturnModelHashBean = (DimensionDetailCategories) responseMap.get("SDMX_RETURN_MODEL_HASH_BEAN");
					if (sdmxReturnModelBean != null && sdmxReturnModelBean.getDsdId() == null) {
						continue;
					}
					if (sdmxReturnModelBean != null && StringUtils.isBlank(sdmxReturnModelBean.getElementVersion())) {
						sdmxReturnModelBean.setElementVersion("1.0");
						sdmxReturnModelHashBean.setElementVersion("1.0");
						sdmxEleDimTypeMapBean.setElementVer("1.0");
					}
				}

				// DMID Grouping

				// Setting regex
				RegexDetails regexDetails = null;
				if (sdmxReturnModelBean != null) {
					if (sdmxReturnModelBean.getRegexDetails() == null) {
						regexDetails = new RegexDetails();
					} else {
						regexDetails = sdmxReturnModelBean.getRegexDetails();
					}
				}
				// regex = cellRefRegexMapping.get(entry.getKey());
				if (regexDetails != null) {
					if (StringUtils.isBlank(regex)) {
						Properties prop = ResourceUtil.getResourcePropertyFile();
						regexDetails.setRegex(prop.getProperty("data.template.default.regex.pattern"));
						regexDetails.setMinLength(Integer.parseInt(prop.getProperty("data.template.default.regex.minLength")));
						regexDetails.setMaxLength(Integer.parseInt(prop.getProperty("data.template.default.regex.maxLength")));
					} else {
						regexDetails.setRegex(regex);
					}
				}
				if (sdmxReturnModelBean != null) {
					sdmxReturnModelBean.setRegexDetails(regexDetails);
				}

				String sdmxReturnModelHashJson = gson.toJson(sdmxReturnModelHashBean);
				sdmxReturnModelHashJson = sortJsonStr(sdmxReturnModelHashJson, gson);

				String sdmxReturnModelJsonBase64Enc = Base64.encodeBase64String(sdmxReturnModelHashJson.getBytes());
				SdmxModelCodesBean sdmxModelCodesBean = sdmxModelCodesService.findEntityByModelCodeHash(sdmxReturnModelJsonBase64Enc);

				String sdmxReturnModelJson = gson.toJson(sdmxReturnModelBean);
				sdmxReturnModelJson = sortJsonStr(sdmxReturnModelJson, gson);

				Long modelCodeIdFk = null;
				if (sdmxReturnModelBean != null) {
					if (sdmxModelCodesBean == null) {
						sdmxModelCodesBean = new SdmxModelCodesBean();
						Long elementId = sdmxElementService.findByDsdCodeAndVerAndIsActive(sdmxReturnModelBean.getDsdId(), sdmxReturnModelBean.getElementVersion(), true, processUploadInputBean.getAgencyCode());
						sdmxModelCodesBean.setElementIdFk(elementId);
						sdmxModelCodesBean.setModelDim(sdmxReturnModelJson);
						sdmxModelCodesBean.setModelDimHash(sdmxReturnModelJsonBase64Enc);
						sdmxModelCodesBean.setModelCode(SdmxModelCodesHelper.generateDMISeq(sdmxModelCodesService.findMaxDMIModelCodes("DMI")));
						Integer regexId = 1;
						if (regexMasterMap.containsKey(regex)) {
							regexId = regexMasterMap.get(regex);
						}
						sdmxModelCodesBean.setRegexIdFk(regexId);
						sdmxModelCodesBean.setIsActive(true);
						modelCodeIdFk = sdmxModelCodesService.saveEntityByBean(sdmxModelCodesBean, userId);
					} else {
						modelCodeIdFk = sdmxModelCodesBean.getModelCodesId();
						if (sdmxModelCodesBean != null && !sdmxModelCodesBean.getIsActive()) {
							sdmxModelCodesBean.setIsActive(true);
							sdmxModelCodesService.saveEntityByBean(sdmxModelCodesBean, userId);
						}
					}
				}
				String sectionCode = null;
				SdmxReturnModelInfoBean sdmxReturnModelInfoBean = new SdmxReturnModelInfoBean();
				sdmxReturnModelInfoBean.setModelCodesIdFk(modelCodeIdFk);
				sdmxReturnModelInfoBean.setReturnCellRef(entry.getKey());
				sdmxReturnModelInfoBean.setIsActive(true);

				if (!cellRefIsMandatoryMap.isEmpty()) {
					if (cellRefIsMandatoryMap.containsKey(entry.getKey())) {
						if (cellRefIsMandatoryMap.get(entry.getKey()).equals("1")) {
							sdmxReturnModelInfoBean.setIsMandatory(true);
							isMandatoryCount++;
						} else {
							sdmxReturnModelInfoBean.setIsMandatory(false);
						}

					} else {
						sdmxReturnModelInfoBean.setIsMandatory(false);
					}

				} else {
					sdmxReturnModelInfoBean.setIsMandatory(false);

				}
				// sdmxReturnModelInfoBean.setReturnPreviewIdFk(sdmxReturnPreviewBean.getReturnPreviewTypeId());
				for (Map.Entry<String, List<Integer>> sheetCellMappingEntry : sheetCellMapping.entrySet()) {
					List<Integer> cellRefList = sheetCellMappingEntry.getValue();
					if (cellRefList.contains(entry.getKey())) {
						sectionCode = sheetCellMappingEntry.getKey();
						break;
					}
				}
				if (!StringUtils.isBlank(sectionCode)) {
					Long newReturnSheetInfoId = sheetCodeNewIdMap.get(sectionCode);
					sdmxReturnModelInfoBean.setReturnSheetIdFk(newReturnSheetInfoId);
				}
				if (sdmxReturnModelInfoBean != null && sdmxReturnModelInfoBean.getReturnSheetIdFk() != null) {
					sdmxReturnModelInfoService.saveEntityByBean(sdmxReturnModelInfoBean, userId);
				} else {
					LOGGER.error("Extra cell found");
					dataTemplateUploadValidationDetail.setErrorCode(ErrorCode.E1195.toString());
					dataTemplateUploadValidationDetail.setErrorMessage(ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E1195.toString())));
					dataTemplateUploadValidationDetail.setCellNo(sdmxReturnModelInfoBean.getReturnCellRef());
					dataTemplateUploadValidationDetail.setElementCode(sdmxReturnModelBean.getDsdId());
					dataTemplateUploadValidationDetail.setElementVersion(sdmxReturnModelBean.getElementVersion());
					// dataTemplateUploadValidationDetail.setDimensionCode(dimConceptId);
					// dataTemplateUploadValidationDetail.setDimensionType(OPEN_STR);
					throw new ApplicationException(ErrorCode.E1195.toString(), ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E1195.toString())));
				}

				// DMID Grouping
				sdmxModelCodesHelper.saveDMIDGrouping(sdmxEleDimTypeMapBean, gson);
			}

			Map<Integer, String> cellRefFormulaJsonMap = new HashMap<>();
			cellRefFormulaJsonMap = sdmxFormulaService.getCellRefFormulaMapping(xlsFilePath, processUploadInputBean.getReturnCode(), returnPreviewId, dataTemplateUploadValidationDetail);
			LOGGER.debug("DEBUG - cellRefFormulaJsonMap size :" + cellRefFormulaJsonMap.size());
			SdmxReturnModelInfoEntity sdmxReturnModelInfoEntity;
			List<SdmxReturnModelInfoEntity> modelInfoList = new ArrayList<>();

			if (!cellRefFormulaJsonMap.isEmpty()) {
				modelInfoList = sdmxReturnModelInfoRepo.findEntityByPreviewId(returnPreviewId);
				LOGGER.debug("DEBUG - modelInfoList size :" + modelInfoList.size());

				Map<Integer, SdmxReturnModelInfoEntity> modelInfoMap = new HashMap<>();
				modelInfoMap = modelInfoList.stream().collect(Collectors.toMap(modelInfoEntity -> modelInfoEntity.getReturnCellRef(), modelInfoEntity -> modelInfoEntity));

				for (Map.Entry<Integer, String> entry : cellRefFormulaJsonMap.entrySet()) {
					Integer cellRef = entry.getKey();
					if (modelInfoMap.containsKey(cellRef)) {
						sdmxReturnModelInfoEntity = modelInfoMap.get(cellRef);
						if (sdmxReturnModelInfoEntity != null) {
							sdmxReturnModelInfoEntity.setCellFormula(cellRefFormulaJsonMap.get(entry.getKey()));
							sdmxReturnModelInfoService.add(sdmxReturnModelInfoEntity);
						}
					}

				}
			}

			if (isMandatoryCount < 1) {
				List<Integer> mandatoryCellList = new ArrayList<>();
				Map<Integer, List<String>> elementCellToModelMap = new HashMap<>();
				elementCellToModelMap = sdmxFormulaService.getElementCellToModelMap(returnPreviewId);
				Iterator<Map.Entry<Integer, List<String>>> itr = elementCellToModelMap.entrySet().iterator();
				int count = 0;
				while (itr.hasNext()) {
					Map.Entry<Integer, List<String>> entry = itr.next();
					Integer cellRef = entry.getKey();
					List<String> value = entry.getValue();
					if (count >= 0 && count <= 4) {
						if (value.get(1).equals("0")) {
							mandatoryCellList.add(cellRef);
							count++;
						}
					}

				}

				if (!mandatoryCellList.isEmpty()) {
					modelInfoList = new ArrayList<>();
					modelInfoList = sdmxReturnModelInfoRepo.findEntityByPreviewId(returnPreviewId);
					LOGGER.debug("DEBUG - modelInfoList size :" + modelInfoList.size());

					Map<Integer, SdmxReturnModelInfoEntity> modelInfoMap = new HashMap<>();
					modelInfoMap = modelInfoList.stream().collect(Collectors.toMap(modelInfoEntity -> modelInfoEntity.getReturnCellRef(), modelInfoEntity -> modelInfoEntity));

					for (Integer cellRef : mandatoryCellList) {

						if (modelInfoMap.containsKey(cellRef)) {
							sdmxReturnModelInfoEntity = modelInfoMap.get(cellRef);
							if (sdmxReturnModelInfoEntity != null) {
								sdmxReturnModelInfoEntity.setIsMandatory(true);
								sdmxReturnModelInfoService.add(sdmxReturnModelInfoEntity);
							}
						}

					}
				}

			}

		} else {
			// Request Bean
			SdmxLockRecordSetBean sdmxLockRecordSetBean = new SdmxLockRecordSetBean();
			sdmxLockRecordSetBean.setLockedBy(userId);
			sdmxLockRecordSetBean.setModuleId(2L);
			Map<String, Long> otherDetailMap = new HashMap<>();

			otherDetailMap.put("returnTemplateId", Long.parseLong(processUploadInputBean.getOldReturnTemplateId()));
			sdmxLockRecordSetBean.setRecordDetailEncodedJson(Base64.encodeBase64String((new Gson().toJson(otherDetailMap)).getBytes()));

			for (Map.Entry<Integer, SdmxReturnModelInfoEntity> oldCellRefWithModelEntry : oldCellRefWithModelCodeMap.entrySet()) {
				Long returnSheetIdFk = null;
				for (Map.Entry<String, List<Integer>> sheetCellEntry : sheetCellMapping.entrySet()) {
					List<Integer> sheetMappingCellList = sheetCellEntry.getValue();
					if (sheetMappingCellList.contains(oldCellRefWithModelEntry.getKey())) {
						returnSheetIdFk = sheetCodeNewIdMap.get(sheetCellEntry.getKey());
						break;
					}
				}
				SdmxReturnModelInfoBean sdmxReturnModelInfoBean = new SdmxReturnModelInfoBean();
				sdmxReturnModelInfoBean.setModelCodesIdFk(oldCellRefWithModelEntry.getValue().getModelCodesIdFk().getModelCodesId());
				sdmxReturnModelInfoBean.setReturnCellRef(oldCellRefWithModelEntry.getKey());
				sdmxReturnModelInfoBean.setIsActive(true);
				sdmxReturnModelInfoBean.setReturnSheetIdFk(returnSheetIdFk);
				sdmxReturnModelInfoBean.setIsMandatory(oldCellRefWithModelEntry.getValue().getIsMandatory());
				sdmxReturnModelInfoBean.setCellFormula(oldCellRefWithModelEntry.getValue().getCellFormula());
				sdmxReturnModelInfoService.saveEntityByBean(sdmxReturnModelInfoBean, userId);
			}
		}

		// Update return preview status
		sdmxModuleStatus = sdmxModuleStatusMap.get(SdmxModuleStatusCodeEnum.UPLOADED_SUCCESSFULLY.getStatusCode());
		sdmxReturnPreviewBean.setModuleStatusId(sdmxModuleStatus.getModuleStatusId());
		sdmxReturnPreviewBean.setModuleStatusMessage(sdmxModuleStatus.getModuleStatusLabel());
		sdmxReturnPreviewService.saveEntityByBean(sdmxReturnPreviewBean);
	}

	private void makeSchedulerStopEntry(int successfullyProcessedRecord, Long schedulerLogId, Long schedulerId, String jobProcessingId) {
		try {
			if (schedulerLogId != null) {
				SchedulerLog schedulerLog = new SchedulerLog();
				schedulerLog.setSuccessfullyProcessedCount(Long.valueOf(successfullyProcessedRecord));
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

	private Long makeSchedulerStartEntry(Long totalRecordCount, Long schedulerId, String jobProcessingId) {
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

	private Scheduler getSchedulerStatus(String jobProcessingId) {
		try {
			// String SCHEDULER_CODE = "SDMX_DATA_TEMPLATE_SCHEDULER";
			CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

			Map<String, String> headerMap = new HashMap<>();
			headerMap.put(GeneralConstants.JOB_PROCESSING_ID.getConstantVal(), jobProcessingId);
			// headerMap.put(GeneralConstants.SCHEDULER_CODE.getConstantVal(),
			// SCHEDULER_CODE);
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

	private Map<String, Object> convertEleSDMXModelToJSON(String input, Gson gson, SdmxEleDimTypeMapBean sdmxEleDimTypeMapBean, String agencyMasterCode, DataTemplateUploadValidationDetail dataTemplateUploadValidationDetail, Integer cellNo, Map<String, ElementDimentionClMappingDetails> elementDimensionMap) throws Exception {
		Map<String, Object> responseMap = new HashMap<>();
		if (StringUtils.isBlank(input)) {
			return null;
		}

		String[] sdmxModelArrHash, sdmxModelArrSemiColon, sdmxModelArrHyphen, dimValPipe;
		String splitVal;

		String sdmxModel = input;

		sdmxModelArrHash = sdmxModel.split("#"); // #

		if (sdmxModelArrHash == null) {
			return null;
		}

		DimensionDetailCategories sdmxReturnModelBean = new DimensionDetailCategories();
		DimensionDetailCategories sdmxReturnModelHashBean = new DimensionDetailCategories();

		List<DimensionCodeListValueBean> codeListDim;
		List<DimensionCodeListValueBean> codeListDimForHash;
		List<DimensionCodeListValueBean> commonDimension;
		List<DimensionCodeListValueBean> inputDimension;
		List<DimensionCodeListValueBean> inputDimensionForHash;
		RegexDetails regexDetails;
		ModelOtherDetails modelOtherDetails;
		Set<String> dimConceptIdSet = new HashSet<>();
		Map<String, String> elementDimConceptMap = new HashMap<>();
		for (String sdmxModelArrHashVal : sdmxModelArrHash) {
			if (sdmxModelArrHashVal.contains(ELEMENT_STR)) {
				splitVal = sdmxModelArrHashVal.replace(ELEMENT_STR, "");

				sdmxModelArrSemiColon = splitVal.split(";"); // ;

				if (sdmxModelArrSemiColon == null) {
					continue;
				}

				for (String sdmxModelArrSemiColonVal : sdmxModelArrSemiColon) {
					if (StringUtils.isBlank(sdmxModelArrSemiColonVal)) {
						continue;
					}
					String dsdId = Validations.trimInput(sdmxModelArrSemiColonVal);
					if (("AUTO").equalsIgnoreCase(dsdId)) {
						continue;
					}
					String elementVersion = "";
					if (dsdId.contains("(") && (dsdId.contains(")"))) {
						elementVersion = dsdId.substring(dsdId.indexOf("(") + 1, dsdId.indexOf(")"));
						dsdId = dsdId.substring(0, dsdId.indexOf("("));
					} else {
						LOGGER.error("Element version not found for element code : " + dsdId);
						dataTemplateUploadValidationDetail.setErrorCode(ErrorCode.E1357.toString());
						dataTemplateUploadValidationDetail.setErrorMessage(ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E1357.toString())));
						dataTemplateUploadValidationDetail.setCellNo(cellNo);
						dataTemplateUploadValidationDetail.setElementCode(dsdId);
						throw new ApplicationException(ErrorCode.E1357.toString(), ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E1357.toString())));
					}

					sdmxReturnModelBean.setDsdId(dsdId);
					sdmxReturnModelHashBean.setDsdId(dsdId);
					// DMID Grouping
					sdmxEleDimTypeMapBean.setDsdCode(dsdId);
					if (!StringUtils.isBlank(elementVersion)) {
						sdmxReturnModelBean.setElementVersion(elementVersion);
						sdmxReturnModelHashBean.setElementVersion(elementVersion);
						sdmxEleDimTypeMapBean.setElementVer(elementVersion);
					}
				}
				if (StringUtils.isBlank(sdmxReturnModelBean.getDsdId()) || StringUtils.isBlank(sdmxReturnModelBean.getElementVersion())) {
					LOGGER.error("Element Id/version can't be blank ");
					dataTemplateUploadValidationDetail.setErrorCode(ErrorCode.E1415.toString());
					dataTemplateUploadValidationDetail.setErrorMessage(ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E1415.toString())));
					dataTemplateUploadValidationDetail.setCellNo(cellNo);
					dataTemplateUploadValidationDetail.setElementCode(sdmxReturnModelBean.getDsdId());
					dataTemplateUploadValidationDetail.setElementVersion(sdmxReturnModelBean.getElementVersion());
					throw new ApplicationException(ErrorCode.E1415.toString(), ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E1415.toString())));
				}
				// Element Validation
				if (!elementDimensionMap.containsKey(sdmxReturnModelBean.getDsdId() + ELEMENT_VERSION_STR + sdmxReturnModelBean.getElementVersion())) {
					// Fetch the element from the DB
					String elementDimensionJson = elementDimensionRepo.findByElementCodeDsdCodeAgencyNisActiveStatus(sdmxReturnModelBean.getDsdId(), sdmxReturnModelBean.getElementVersion(), agencyMasterCode, true);
					if (StringUtils.isEmpty(elementDimensionJson)) {
						LOGGER.error("Element does not exist for Element Code : " + sdmxReturnModelBean.getDsdId() + " , and Element Version : " + sdmxReturnModelBean.getElementVersion());
						dataTemplateUploadValidationDetail.setErrorCode(ErrorCode.E1415.toString());
						dataTemplateUploadValidationDetail.setErrorMessage(ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E1415.toString())));
						dataTemplateUploadValidationDetail.setCellNo(cellNo);
						dataTemplateUploadValidationDetail.setElementCode(sdmxReturnModelBean.getDsdId());
						dataTemplateUploadValidationDetail.setElementVersion(sdmxReturnModelBean.getElementVersion());
						throw new ApplicationException(ErrorCode.E1415.toString(), ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E1415.toString())));
					} else {
						// Convert json to Dim bean
						ElementDimensionStoredJson elementDimensionStoredJson = JsonUtility.getGsonObject().fromJson(elementDimensionJson, ElementDimensionStoredJson.class);

						if (elementDimensionStoredJson != null && !CollectionUtils.isEmpty(elementDimensionStoredJson.getDimCombination())) {
							for (DimCombination dimCombination : elementDimensionStoredJson.getDimCombination()) {
								elementDimConceptMap.put(dimCombination.getDimConceptId(), dimCombination.getConceptVersion());
							}
						}
						// Setting up all the dimension from element dimension mapping
						ElementDimentionClMappingDetails elementDimentionClMappingDetails = new ElementDimentionClMappingDetails();
						elementDimentionClMappingDetails.setElementDimConceptMap(elementDimConceptMap);
						elementDimensionMap.put(sdmxReturnModelBean.getDsdId() + ELEMENT_VERSION_STR + sdmxReturnModelBean.getElementVersion(), elementDimentionClMappingDetails);
					}
				}
				continue;
			} else if (sdmxModelArrHashVal.contains(CODE_LIST_DIMENSION_STR)) {
				splitVal = sdmxModelArrHashVal.replace(CODE_LIST_DIMENSION_STR, "");

				sdmxModelArrSemiColon = splitVal.split(";"); // ;

				if (sdmxModelArrSemiColon == null) {
					continue;
				}

				codeListDim = new ArrayList<>();
				codeListDimForHash = new ArrayList<>();
				// DMID Grouping
				List<String> closeDimList = new ArrayList<>();
				for (String sdmxModelArrSemiColonVal : sdmxModelArrSemiColon) {
					if (StringUtils.isEmpty(sdmxModelArrSemiColonVal)) {
						continue;
					} else {
						sdmxModelArrSemiColonVal = sdmxModelArrSemiColonVal.replace("\n", "");
						if (StringUtils.isBlank(sdmxModelArrSemiColonVal)) {
							continue;
						}
					}

					sdmxModelArrHyphen = sdmxModelArrSemiColonVal.split("[|]"); // |

					if (sdmxModelArrHyphen == null || sdmxModelArrHyphen.length > 2) {
						continue;
					}

					String clValueCode = null;
					String dimConceptId = null;

					dimConceptId = sdmxModelArrHyphen[0];
					if (sdmxModelArrHyphen == null || sdmxModelArrHyphen.length > 1) {
						clValueCode = sdmxModelArrHyphen[1].trim();
					}
					vaidateDimension(dimConceptId, clValueCode, dataTemplateUploadValidationDetail, cellNo, sdmxReturnModelBean, agencyMasterCode, elementDimensionMap, CLOSED_STR);
					sdmxModelCodesHelper.setClosedDimension(codeListDim, clValueCode, dimConceptId, codeListDimForHash, closeDimList);
					if (!dimConceptIdSet.contains(dimConceptId)) {
						dimConceptIdSet.add(dimConceptId);
					} else {
						LOGGER.error("Duplicate dimension : " + dimConceptId + " found for element : " + sdmxReturnModelBean.getDsdId() + "(" + sdmxReturnModelBean.getElementVersion() + ")");
						dataTemplateUploadValidationDetail.setErrorCode(ErrorCode.E1411.toString());
						dataTemplateUploadValidationDetail.setErrorMessage(ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E1411.toString())));
						dataTemplateUploadValidationDetail.setCellNo(cellNo);
						dataTemplateUploadValidationDetail.setElementCode(sdmxReturnModelBean.getDsdId());
						dataTemplateUploadValidationDetail.setElementVersion(sdmxReturnModelBean.getElementVersion());
						dataTemplateUploadValidationDetail.setDimensionCode(dimConceptId);
						dataTemplateUploadValidationDetail.setDimensionType(CLOSED_STR);
						throw new ApplicationException(ErrorCode.E1411.toString(), ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E1411.toString())));
					}
				}
				// Setting close dimensions for DMID Grouping
				sdmxEleDimTypeMapBean.setClosedDim(closeDimList);

				if (!CollectionUtils.isEmpty(codeListDim)) {
					if (codeListDim.size() > 1) {
						codeListDim.sort((DimensionCodeListValueBean s1, DimensionCodeListValueBean s2) -> s1.getDimConceptId().compareTo(s2.getDimConceptId()));
					}
					sdmxReturnModelBean.setClosedDim(codeListDim);
				}
				if (!CollectionUtils.isEmpty(codeListDimForHash)) {
					if (codeListDimForHash.size() > 1) {
						codeListDimForHash.sort((DimensionCodeListValueBean s1, DimensionCodeListValueBean s2) -> s1.getDimConceptId().compareTo(s2.getDimConceptId()));
					}
					sdmxReturnModelHashBean.setClosedDim(codeListDimForHash);
				}

				continue;
			} else if (sdmxModelArrHashVal.contains(OPEN_DIMENSION_STR)) {
				splitVal = sdmxModelArrHashVal.replace(OPEN_DIMENSION_STR, "");

				sdmxModelArrSemiColon = splitVal.split(";"); // ;

				if (sdmxModelArrSemiColon == null) {
					continue;
				}

				inputDimension = new ArrayList<>();
				inputDimensionForHash = new ArrayList<>();
				// DMID Grouping
				List<String> openDimList = new ArrayList<>();

				for (String sdmxModelArrSemiColonVal : sdmxModelArrSemiColon) {
					if (StringUtils.isEmpty(sdmxModelArrSemiColonVal)) {
						continue;
					} else {
						sdmxModelArrSemiColonVal = sdmxModelArrSemiColonVal.replace("\n", "");
						if (StringUtils.isEmpty(sdmxModelArrSemiColonVal)) {
							continue;
						}
					}

					dimValPipe = sdmxModelArrSemiColonVal.split("[|]");
					String clValueCode = null;
					String dimConceptId = null;
					if (dimValPipe.length > 1) {
						dimConceptId = dimValPipe[0].trim();
						clValueCode = dimValPipe[1].trim();
					} else {
						dimConceptId = sdmxModelArrSemiColonVal;
						clValueCode = "N_A";
					}

					vaidateDimension(dimConceptId, clValueCode, dataTemplateUploadValidationDetail, cellNo, sdmxReturnModelBean, agencyMasterCode, elementDimensionMap, OPEN_STR);

					// Set Open Dimension
					sdmxModelCodesHelper.setOpenDimension(inputDimension, clValueCode, dimConceptId, inputDimensionForHash, openDimList);
					if (!dimConceptIdSet.contains(dimConceptId)) {
						dimConceptIdSet.add(dimConceptId);
					} else {
						LOGGER.error("Duplicate dimension : " + dimConceptId + " found for element : " + sdmxReturnModelBean.getDsdId() + "(" + sdmxReturnModelBean.getElementVersion() + ")");
						dataTemplateUploadValidationDetail.setErrorCode(ErrorCode.E1411.toString());
						dataTemplateUploadValidationDetail.setErrorMessage(ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E1411.toString())));
						dataTemplateUploadValidationDetail.setCellNo(cellNo);
						dataTemplateUploadValidationDetail.setElementCode(sdmxReturnModelBean.getDsdId());
						dataTemplateUploadValidationDetail.setElementVersion(sdmxReturnModelBean.getElementVersion());
						dataTemplateUploadValidationDetail.setDimensionCode(dimConceptId);
						dataTemplateUploadValidationDetail.setDimensionType(OPEN_STR);
						throw new ApplicationException(ErrorCode.E1411.toString(), ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E1411.toString())));
					}
				}

				// DMID Grouping
				sdmxEleDimTypeMapBean.setOpenDim(openDimList);

				if (!CollectionUtils.isEmpty(inputDimension)) {
					if (inputDimension.size() > 1) {
						inputDimension.sort((DimensionCodeListValueBean s1, DimensionCodeListValueBean s2) -> s1.getDimConceptId().compareTo(s2.getDimConceptId()));
					}
					sdmxReturnModelBean.setOpenDimension(inputDimension);
				}

				if (!CollectionUtils.isEmpty(inputDimensionForHash)) {
					if (inputDimensionForHash.size() > 1) {
						inputDimensionForHash.sort((DimensionCodeListValueBean s1, DimensionCodeListValueBean s2) -> s1.getDimConceptId().compareTo(s2.getDimConceptId()));
					}

					sdmxReturnModelHashBean.setOpenDimension(inputDimensionForHash);
				}

				continue;
			} else if (sdmxModelArrHashVal.contains(COMMON_DIMENSION_STR)) {
				splitVal = sdmxModelArrHashVal.replace(COMMON_DIMENSION_STR, "");

				sdmxModelArrSemiColon = splitVal.split(";"); // ;

				if (sdmxModelArrSemiColon == null) {
					continue;
				}

				commonDimension = new ArrayList<>();

				for (String sdmxModelArrSemiColonVal : sdmxModelArrSemiColon) {
					if (StringUtils.isEmpty(sdmxModelArrSemiColonVal)) {
						continue;
					} else {
						sdmxModelArrSemiColonVal = sdmxModelArrSemiColonVal.replace("\n", "");
						if (StringUtils.isEmpty(sdmxModelArrSemiColonVal)) {
							continue;
						}
					}
					dimValPipe = sdmxModelArrSemiColonVal.split("[|]");
					String clValueCode = null;
					String dimConceptId = null;
					if (dimValPipe != null) {
						if (dimValPipe.length > 1) {
							dimConceptId = dimValPipe[0].trim();
							clValueCode = dimValPipe[1].trim();
						} else {
							dimConceptId = sdmxModelArrSemiColonVal;
						}
					}

					vaidateDimension(dimConceptId, clValueCode, dataTemplateUploadValidationDetail, cellNo, sdmxReturnModelBean, agencyMasterCode, elementDimensionMap, COMMON_STR);
					// Set Common Dimension
					sdmxModelCodesHelper.setCommonDimension(commonDimension, clValueCode, dimConceptId);
					if (!dimConceptIdSet.contains(dimConceptId)) {
						dimConceptIdSet.add(dimConceptId);
					} else {
						LOGGER.error("Duplicate dimension : " + dimConceptId + " found for element : " + sdmxReturnModelBean.getDsdId() + "(" + sdmxReturnModelBean.getElementVersion() + ")");
						dataTemplateUploadValidationDetail.setErrorCode(ErrorCode.E1411.toString());
						dataTemplateUploadValidationDetail.setErrorMessage(ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E1411.toString())));
						dataTemplateUploadValidationDetail.setCellNo(cellNo);
						dataTemplateUploadValidationDetail.setElementCode(sdmxReturnModelBean.getDsdId());
						dataTemplateUploadValidationDetail.setElementVersion(sdmxReturnModelBean.getElementVersion());
						dataTemplateUploadValidationDetail.setDimensionCode(dimConceptId);
						dataTemplateUploadValidationDetail.setDimensionType(COMMON_STR);
						throw new ApplicationException(ErrorCode.E1411.toString(), ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E1411.toString())));
					}
				}

				if (!CollectionUtils.isEmpty(commonDimension)) {
					if (commonDimension.size() > 1) {
						commonDimension.sort((DimensionCodeListValueBean s1, DimensionCodeListValueBean s2) -> s1.getDimConceptId().compareTo(s2.getDimConceptId()));
					}
					sdmxReturnModelBean.setCommonDimension(commonDimension);
				}
				continue;
			} /*
				 * else if (sdmxModelArrHashVal.contains("[Regex Details]")) { splitVal =
				 * sdmxModelArrHashVal.replace("[Regex  Details]", "");
				 * 
				 * sdmxModelArrSemiColon = splitVal.split(";"); // ;
				 * 
				 * if (sdmxModelArrSemiColon == null) { continue; }
				 * 
				 * regexDetails = new RegexDetails(); for (String sdmxModelArrSemiColonVal :
				 * sdmxModelArrSemiColon) { if (StringUtils.isEmpty(sdmxModelArrSemiColonVal)) {
				 * continue; } else { sdmxModelArrSemiColonVal =
				 * sdmxModelArrSemiColonVal.replace("\n", ""); if
				 * (StringUtils.isEmpty(sdmxModelArrSemiColonVal)) { continue; } } dimValPipe =
				 * sdmxModelArrSemiColonVal.split("[|]");
				 * 
				 * if (dimValPipe != null && dimValPipe[0].trim().equalsIgnoreCase("minLength"))
				 * { regexDetails.setMinLength(Integer.parseInt(dimValPipe[1].trim())); } else
				 * if (dimValPipe != null && dimValPipe[0].trim().equalsIgnoreCase("maxLength"))
				 * { regexDetails.setMaxLength(Integer.parseInt(dimValPipe[1].trim())); } }
				 * sdmxReturnModelBean.setRegexDetails(regexDetails);
				 * 
				 * }
				 */ else if (sdmxModelArrHashVal.contains(OTHER_DETAIL_STR)) {
				splitVal = sdmxModelArrHashVal.replace(OTHER_DETAIL_STR, "");

				sdmxModelArrSemiColon = splitVal.split(";"); // ;

				if (sdmxModelArrSemiColon == null) {
					continue;
				}

				modelOtherDetails = new ModelOtherDetails();
				for (String sdmxModelArrSemiColonVal : sdmxModelArrSemiColon) {
					if (StringUtils.isEmpty(sdmxModelArrSemiColonVal)) {
						continue;
					} else {
						sdmxModelArrSemiColonVal = sdmxModelArrSemiColonVal.replace("\n", "");
						if (StringUtils.isEmpty(sdmxModelArrSemiColonVal)) {
							continue;
						}
					}
					dimValPipe = sdmxModelArrSemiColonVal.split("[|]");

					if (dimValPipe != null && dimValPipe[0].trim().equalsIgnoreCase(DEPENDENCY_TYPE_STR)) {
						modelOtherDetails.setDependencyType(dimValPipe[1].trim());
					} else {
						LOGGER.error("Dependency Type node is not correct");
						dataTemplateUploadValidationDetail.setErrorCode(ErrorCode.E1237.toString());
						dataTemplateUploadValidationDetail.setErrorMessage(ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E1237.toString())));
						dataTemplateUploadValidationDetail.setCellNo(cellNo);
						dataTemplateUploadValidationDetail.setElementCode(sdmxReturnModelBean.getDsdId());
						dataTemplateUploadValidationDetail.setElementVersion(sdmxReturnModelBean.getElementVersion());
						throw new ApplicationException(ErrorCode.E1237.toString(), ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E1237.toString())));
					}
				}
				sdmxReturnModelBean.setModelOtherDetails(modelOtherDetails);
				sdmxReturnModelHashBean.setModelOtherDetails(modelOtherDetails);
			} else {
				LOGGER.error("Mapping is not correct");
				dataTemplateUploadValidationDetail.setErrorCode(ErrorCode.E1504.toString());
				dataTemplateUploadValidationDetail.setErrorMessage(ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E1504.toString())));
				dataTemplateUploadValidationDetail.setCellNo(cellNo);
				dataTemplateUploadValidationDetail.setElementCode(sdmxReturnModelBean.getDsdId());
				dataTemplateUploadValidationDetail.setElementVersion(sdmxReturnModelBean.getElementVersion());
				throw new ApplicationException(ErrorCode.E1504.toString(), ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E1504.toString())));
			}
		}
		responseMap.put("SDMX_RETURN_MODEL_BEAN", sdmxReturnModelBean);
		responseMap.put("SDMX_RETURN_MODEL_HASH_BEAN", sdmxReturnModelHashBean);
		return responseMap;
	}

	private void vaidateDimension(String dimConceptId, String clValueCode, DataTemplateUploadValidationDetail dataTemplateUploadValidationDetail, Integer cellNo, DimensionDetailCategories sdmxReturnModelBean, String agencyMasterCode, Map<String, ElementDimentionClMappingDetails> elementDimensionMap, String dimensionType) throws ApplicationException {
		// Set Close Dimension
		clValueCode = Validations.trimInput(clValueCode);
		dimConceptId = Validations.trimInput(dimConceptId);
		ElementDimentionClMappingDetails elementDimentionClMappingDetails = elementDimensionMap.get(sdmxReturnModelBean.getDsdId() + ELEMENT_VERSION_STR + sdmxReturnModelBean.getElementVersion());
		Map<String, String> elementDimConceptMap = elementDimentionClMappingDetails.getElementDimConceptMap();

		if (!elementDimConceptMap.containsKey(dimConceptId)) {
			dataTemplateUploadValidationDetail.setErrorCode(ErrorCode.E0152.toString());
			dataTemplateUploadValidationDetail.setErrorMessage(ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0152.toString())));
			dataTemplateUploadValidationDetail.setCellNo(cellNo);
			dataTemplateUploadValidationDetail.setElementCode(sdmxReturnModelBean.getDsdId());
			dataTemplateUploadValidationDetail.setElementVersion(sdmxReturnModelBean.getElementVersion());
			dataTemplateUploadValidationDetail.setDimensionCode(dimConceptId);
			dataTemplateUploadValidationDetail.setDimensionType(dimensionType);
			throw new ApplicationException(ErrorCode.E0152.toString(), ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0152.toString())));
		} else {
			String dimVersion = elementDimConceptMap.get(dimConceptId);
			Set<String> clValueSet = new HashSet<>();
			Boolean isCommon = false;
			Map<String, Set<String>> dimClMap = elementDimentionClMappingDetails.getDimClMap();
			if (!MapUtils.isEmpty(dimClMap) && (dimClMap.get(dimConceptId + ELEMENT_VERSION_STR + dimVersion + DIMENSION_TYPE_STR + dimensionType) != null)) {
				clValueSet = dimClMap.get(dimConceptId + ELEMENT_VERSION_STR + dimVersion + DIMENSION_TYPE_STR + dimensionType);
				if (dimensionType.equalsIgnoreCase(COMMON_STR)) {
					isCommon = true;
				}
			} else {

				DimensionMaster dimensionMaster = dimensionRepo.getDimensionByCodeAndAgency(dimConceptId, agencyMasterCode, dimVersion);
				if (dimensionMaster != null) {
					if (dimensionType.equalsIgnoreCase(COMMON_STR)) {
						if (dimensionMaster.getIsCommon()) {
							isCommon = true;
						} else {
							dataTemplateUploadValidationDetail.setErrorCode(ErrorCode.E0152.toString());
							dataTemplateUploadValidationDetail.setErrorMessage(ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0152.toString())));
							dataTemplateUploadValidationDetail.setCellNo(cellNo);
							dataTemplateUploadValidationDetail.setElementCode(sdmxReturnModelBean.getDsdId());
							dataTemplateUploadValidationDetail.setElementVersion(sdmxReturnModelBean.getElementVersion());
							dataTemplateUploadValidationDetail.setDimensionCode(dimConceptId);
							dataTemplateUploadValidationDetail.setDimensionVersion(dimVersion);
							dataTemplateUploadValidationDetail.setDimensionType(dimensionType);
							throw new ApplicationException(ErrorCode.E0152.toString(), ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0152.toString())));
						}
					}

					if (dimensionMaster.getCodeListMaster() != null) {
						List<CodeListValues> codeListValuesList = dimensionMaster.getCodeListMaster().getCodeListValues();

						for (CodeListValues codeListValues : codeListValuesList) {
							clValueSet.add(codeListValues.getClValueCode());
						}
						if (MapUtils.isEmpty(dimClMap)) {
							dimClMap = new HashMap<>();
						}
						dimClMap.put(dimConceptId + ELEMENT_VERSION_STR + dimVersion + DIMENSION_TYPE_STR + dimensionType, clValueSet);
						elementDimentionClMappingDetails.setDimClMap(dimClMap);
						elementDimensionMap.put(sdmxReturnModelBean.getDsdId() + ELEMENT_VERSION_STR + sdmxReturnModelBean.getElementVersion(), elementDimentionClMappingDetails);
					} else {
						if (MapUtils.isEmpty(dimClMap)) {
							dimClMap = new HashMap<>();
						}
						dimClMap.put(dimConceptId + ELEMENT_VERSION_STR + dimVersion + DIMENSION_TYPE_STR + dimensionType, clValueSet);
						elementDimentionClMappingDetails.setDimClMap(dimClMap);
						elementDimensionMap.put(sdmxReturnModelBean.getDsdId() + ELEMENT_VERSION_STR + sdmxReturnModelBean.getElementVersion(), elementDimentionClMappingDetails);
					}
				} else {
					dataTemplateUploadValidationDetail.setErrorCode(ErrorCode.E0152.toString());
					dataTemplateUploadValidationDetail.setErrorMessage(ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0152.toString())));
					dataTemplateUploadValidationDetail.setCellNo(cellNo);
					dataTemplateUploadValidationDetail.setElementCode(sdmxReturnModelBean.getDsdId());
					dataTemplateUploadValidationDetail.setElementVersion(sdmxReturnModelBean.getElementVersion());
					dataTemplateUploadValidationDetail.setDimensionCode(dimConceptId);
					dataTemplateUploadValidationDetail.setDimensionVersion(dimVersion);
					dataTemplateUploadValidationDetail.setDimensionType(dimensionType);
					throw new ApplicationException(ErrorCode.E0152.toString(), ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E0152.toString())));
				}
			}

			if (COMMENT_STR.equalsIgnoreCase(dimConceptId)) {
				if (!clValueCode.equals(SDMXConstants.ITEM_FREE_TEXT)) {
					dataTemplateUploadValidationDetail.setErrorCode(ErrorCode.E1180.toString());
					dataTemplateUploadValidationDetail.setErrorMessage(ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E1180.toString())));
					dataTemplateUploadValidationDetail.setCellNo(cellNo);
					dataTemplateUploadValidationDetail.setElementCode(sdmxReturnModelBean.getDsdId());
					dataTemplateUploadValidationDetail.setElementVersion(sdmxReturnModelBean.getElementVersion());
					dataTemplateUploadValidationDetail.setDimensionCode(dimConceptId);
					dataTemplateUploadValidationDetail.setDimensionVersion(dimVersion);
					dataTemplateUploadValidationDetail.setDimensionType(dimensionType);
					dataTemplateUploadValidationDetail.setClValue(clValueCode);
					throw new ApplicationException(ErrorCode.E1180.toString(), ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E1180.toString())));
				}
			}
			if (!((dimensionType.equalsIgnoreCase(COMMON_STR) && isCommon) || (dimensionType.equalsIgnoreCase(CLOSED_STR) && (clValueCode.equalsIgnoreCase(SDMXConstants.FILER_TO_SELECT) || clValueSet.contains(clValueCode))) || dimensionType.equalsIgnoreCase(OPEN_STR) && (clValueCode.equalsIgnoreCase(SDMXConstants.N_A_STR) || clValueCode.equalsIgnoreCase(SDMXConstants.ITEM_FREE_TEXT) || clValueSet.contains(clValueCode)))) {
				dataTemplateUploadValidationDetail.setErrorCode(ErrorCode.E1180.toString());
				dataTemplateUploadValidationDetail.setErrorMessage(ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E1180.toString())));
				dataTemplateUploadValidationDetail.setCellNo(cellNo);
				dataTemplateUploadValidationDetail.setElementCode(sdmxReturnModelBean.getDsdId());
				dataTemplateUploadValidationDetail.setElementVersion(sdmxReturnModelBean.getElementVersion());
				dataTemplateUploadValidationDetail.setDimensionCode(dimConceptId);
				dataTemplateUploadValidationDetail.setDimensionVersion(dimVersion);
				dataTemplateUploadValidationDetail.setDimensionType(dimensionType);
				dataTemplateUploadValidationDetail.setClValue(clValueCode);
				throw new ApplicationException(ErrorCode.E1180.toString(), ObjectCache.getLabelKeyValue(LANG_CODE, ObjectCache.getErrorCodeKey(ErrorCode.E1180.toString())));

			}
		}
	}

	private String sortJsonStr(String jsonString, Gson gson) {
		SortedMap<String, Object> retMap = gson.fromJson(jsonString, new TypeToken<TreeMap<String, Object>>() {
		}.getType());
		return gson.toJson(retMap);
	}

}
