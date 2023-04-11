/**
 * 
 */
package com.iris.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;
import com.iris.caching.ObjectCache;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.PanMasterDto;
import com.iris.dto.ServiceResponse;
import com.iris.exception.ServiceException;
import com.iris.model.PanMaster;
import com.iris.model.PanMasterDetails;
import com.iris.model.Scheduler;
import com.iris.model.SchedulerLog;
import com.iris.model.WebServiceComponentUrl;
import com.iris.repository.PanMasterDetailsRepo;
import com.iris.repository.PanMasterRepo;
import com.iris.repository.PanMasterTempRepo;
import com.iris.service.GenericService;
import com.iris.util.JsonUtility;
import com.iris.util.ResourceUtil;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;
import com.iris.webservices.client.CIMSRestWebserviceClient;

/**
 * @author Siddique
 *
 */
@RestController
public class PanMasterFileCreationScheduler {

	static final Logger logger = LogManager.getLogger(PanMasterFileCreationScheduler.class);

	@Autowired
	private PanMasterRepo panMasterRepo;

	@Autowired
	private GenericService<WebServiceComponentUrl, Long> webServiceComponentService;

	@Autowired
	private PanMasterDetailsRepo panMasterDetailsRepo;

	@Autowired
	private PanMasterTempRepo panMasterTempRepo;

	private static String LANG_CODE = "en";
	private static final String ROOT_PATH = "filepath.root";
	private static final String FILE_PATH = "filePath.pan.master.file.path";
	private String jobProcessId;

	@Value("${scheduler.code.panMaster}")
	private String schedulerCode;

	//	private static final String SCHEDULER_CODE = "PAN_MASTER_DOWNLOAD";

	@Scheduled(cron = "${cron.panMasterSchedular}")
	public void panMasterPrepareSchedular() {
		jobProcessId = UUID.randomUUID().toString();
		logger.info("Pan Master prepare schedular started" + jobProcessId);

		Long schedulerLogId = null;
		PanMasterDetails panMasterDetails = new PanMasterDetails();
		List<PanMasterDetails> panMasterDetailsList = new ArrayList<>();
		Date lastCreatedFileDate = null;
		Date newlyAddedPanApprovalDate = null;

		Scheduler scheduler = getSchedulerStatus();
		if (scheduler != null) {
			if (scheduler.getIsRunning().equals(Boolean.TRUE)) {
				logger.error("Error while starting Pan Master Prepare scheduler -> Reason : Schduler is alrady running ");
				return;
			}
			try {
				// step 1 : if new records approve then start then make start entry in schedular and start the process

				schedulerLogId = makeSchedulerStartEntry(0l, scheduler.getSchedulerId());
				if (schedulerLogId == null) {
					logger.error("Error while starting pan master download scheduler -> Reason : Schduler Log ID not received ");
					return;
				}

				//Step 2: check the last created file details from pan master details table
				panMasterDetailsList = panMasterDetailsRepo.getDataOrderByCreatedOnDesc();

				if (CollectionUtils.isEmpty(panMasterDetailsList)) {

					panMasterDetails.setStatus(1); // status 1 for initial record
				} else {
					lastCreatedFileDate = panMasterDetailsList.get(0).getCreatedOn();
				}
				logger.info("Last file creation date, pan master scheduler:: " + lastCreatedFileDate);
				// step 2 : check the last updated record from pan master temp table
				newlyAddedPanApprovalDate = panMasterTempRepo.getMaxApprovedOnDate();

				if (newlyAddedPanApprovalDate == null) {
					logger.info("No Records available , pan master schedular");
					makeSchedulerStopEntry(0l, schedulerLogId, scheduler.getSchedulerId());
					return;
				}
				logger.info("Newly added records date, pan master scheduler:: " + newlyAddedPanApprovalDate);

				if ((panMasterDetails.getStatus() != null && panMasterDetails.getStatus() == 1) || newlyAddedPanApprovalDate.compareTo(lastCreatedFileDate) > 0) {

					prepareFile();
					logger.info("File Creation successful , pan master schedular");
				} else {
					logger.info("New Records were not added , pan master schedular");
					makeSchedulerStopEntry(0l, schedulerLogId, scheduler.getSchedulerId());
					return;
				}

			} catch (Exception e) {
				logger.error("Exception occoured while processing pan master prepare schedular" + jobProcessId + "Exception is " + e);
				makeSchedulerStopEntry(0l, schedulerLogId, scheduler.getSchedulerId());
			}
			logger.info("No Record available to process");
			makeSchedulerStopEntry(0l, schedulerLogId, scheduler.getSchedulerId());

		} else {
			logger.error("Pan Master Prepare Schedular Not Present");
		}

	}

	private void makeSchedulerStopEntry(long successfullyProcessed, Long schedulerLogId, Long schedulerId) {
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
				headerMap.put(GeneralConstants.JOB_PROCESSING_ID.getConstantVal(), jobProcessId);

				String responsestring = restServiceClient.callRestWebServiceWithMultipleHeader(componentUrl, schedulerLog, null, headerMap);

				ServiceResponse serviceResponse = JsonUtility.getGsonObject().fromJson(responsestring, ServiceResponse.class);

				if (!serviceResponse.isStatus()) {
					logger.info("Error while stopping scheduler -> Reason : Schduler is alrady stopped ");
				} else {
					logger.info("Scheduler stopped successfully ");
				}
			} else {
				logger.error("Scheduler Log Id not found");
			}
		} catch (Exception e) {
			logger.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}

	}

	private void prepareFile() {
		try {
			List<PanMaster> panMasterList = new ArrayList<>();
			String modifiedFileName = "Pan_Master" + DateManip.getCurrentDate("dd_MM_yyyy") + "_" + DateManip.getCurrentTime("HH_MM_SSSS") + ".xlsx";
			String filePath = ResourceUtil.getKeyValue(ROOT_PATH) + File.separator + ResourceUtil.getKeyValue(FILE_PATH) + File.separator;

			File directory = new File(filePath);

			if (!directory.exists()) {
				FileUtils.forceMkdir(new File(filePath));
			}
			filePath = filePath + modifiedFileName;
			List<List<PanMasterDto>> panMasterDtoList = new ArrayList<>();
			PanMasterDetails panMasterDetails = new PanMasterDetails();
			panMasterDetails.setCreatedOn(new Date());

			panMasterDetails.setStatus(2);
			panMasterDetails.setProcessStartTime(new Date());
			panMasterList = panMasterRepo.getPanMasterData();

			if (CollectionUtils.isEmpty(panMasterList)) {
				logger.info("No Records available to process, pan Master file creation");
				return;
			}
			panMasterDetailsRepo.save(panMasterDetails);
			panMasterDetails.setTotalRecords((long) panMasterList.size());

			panMasterDtoList = convertDbObjToDto(panMasterList);

			List<String> headers = new ArrayList<>();
			headers.add(ObjectCache.getLabelKeyValue(LANG_CODE, "field.panMngt.NSDLPAN"));
			headers.add(ObjectCache.getLabelKeyValue(LANG_CODE, "field.panMngt.NSDLBorrowerName"));

			try (Workbook workbook1 = buildExcelFile(headers, panMasterDtoList); FileOutputStream outputStream = new FileOutputStream(filePath)) {
				workbook1.write(outputStream);
				logger.info("File Creation successful");
				panMasterDetails.setModifiedFileName(modifiedFileName);
				panMasterDetails.setFileName("Pan_Master_" + DateManip.getCurrentDate("dd_MM_yyyy") + "_" + DateManip.getCurrentTime("HH") + "_" + panMasterList.size() + ".xlsx");
				panMasterDetails.setProcessEndTime(new Date());
				panMasterDetails.setStatus(3);
				panMasterDetailsRepo.save(panMasterDetails);
			} catch (Exception e) {
				logger.error("Error while writing excel sheet" + e);
			}
		} catch (Exception e) {
			logger.error("Exception occoured in prepareFile, pan master schedular" + e);
		}

	}

	private Workbook buildExcelFile(List<String> headers, List<List<PanMasterDto>> panMasterDtoList) {
		Workbook workbook = new SXSSFWorkbook();
		try {
			XSSFFont font = (XSSFFont) workbook.createFont();
			font.setFontHeightInPoints((short) 10);
			font.setColor((short) Font.COLOR_NORMAL);

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setBorderBottom(BorderStyle.THIN);
			headerCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			headerCellStyle.setBorderRight(BorderStyle.THIN);
			headerCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
			headerCellStyle.setBorderTop(BorderStyle.THIN);
			headerCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			font.setBold(true);
			font.setColor(IndexedColors.BLUE.getIndex());
			headerCellStyle.setFont(font);

			// 2. Sheet creation
			SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet();
			workbook.setSheetName(0, "Data");
			sheet.setRandomAccessWindowSize(100);
			int count = 0;
			Cell headerCell = null;
			Row row = null;
			Cell cell = null;
			Row headerRow = sheet.createRow(count);
			for (String header : headers) {
				headerCell = headerRow.createCell(count++);
				headerCell.setCellValue(header);
				headerCell.setCellStyle(headerCellStyle);
			}
			int rownum = 1;
			count = 0;
			for (List<PanMasterDto> panMasterDtoOuterLoop : panMasterDtoList) {
				for (PanMasterDto panMasterDto : panMasterDtoOuterLoop) {
					count = 0;
					row = sheet.createRow(rownum++);
					cell = row.createCell(count++);
					cell.setCellValue(panMasterDto.getPanNumber());
					cell = row.createCell(count++);
					cell.setCellValue(panMasterDto.getBorrowerName());
				}
			}
		} catch (Exception e) {
			logger.error("Exception while setting bean value into excel" + e);
		}
		return workbook;

	}

	private List<List<PanMasterDto>> convertDbObjToDto(List<PanMaster> panMasterList) {
		List<List<PanMasterDto>> panMasterDtoList = new ArrayList<>();
		List<PanMasterDto> panMasterDtoInnerList = new ArrayList<>();
		PanMasterDto panMasterDto = null;
		try {
			if (!CollectionUtils.isEmpty(panMasterList)) {
				for (PanMaster panMaster : panMasterList) {

					panMasterDto = new PanMasterDto();
					panMasterDto.setPanNumber(panMaster.getPanNumber());
					panMasterDto.setBorrowerName(panMaster.getBorrowerName());
					panMasterDtoInnerList.add(panMasterDto);
				}
			}

			int partiotionData = 0;
			if (panMasterDtoInnerList.size() > 100000) {
				partiotionData = panMasterDtoInnerList.size() / 10;
			} else {
				partiotionData = 5;
			}
			panMasterDtoList = Lists.partition(panMasterDtoInnerList, partiotionData);
		} catch (Exception e) {
			logger.error("Exception occoured while extracting db object , pan master schedular");
		}
		return panMasterDtoList;
	}

	private Scheduler getSchedulerStatus() {
		try {
			CIMSRestWebserviceClient restServiceClient = new CIMSRestWebserviceClient();

			Map<String, String> headerMap = new HashMap<>();
			headerMap.put(GeneralConstants.JOB_PROCESSING_ID.getConstantVal(), jobProcessId);
			headerMap.put(GeneralConstants.SCHEDULER_CODE.getConstantVal(), schedulerCode);

			WebServiceComponentUrl componentUrl = getWebServiceComponentURL(GeneralConstants.GET_ACTIVE_SCHEDULER_STATUS_BY_CODE.getConstantVal(), CIMSRestWebserviceClient.HTTP_METHOD_TYPE_GET);
			String responsestring = restServiceClient.callRestWebServiceWithMultipleHeader(componentUrl, null, null, headerMap);

			Type listToken = new TypeToken<ServiceResponse>() {
			}.getType();
			ServiceResponse serviceResponse = JsonUtility.getGsonObject().fromJson(responsestring, listToken);

			if (!serviceResponse.isStatus()) {
				logger.error("False status received from API with status message " + serviceResponse.getStatusCode());
				return null;
			} else {
				if (serviceResponse.getResponse() != null) {
					listToken = new TypeToken<Scheduler>() {
					}.getType();
					return JsonUtility.getGsonObject().fromJson(serviceResponse.getResponse() + "", listToken);
				} else {
					logger.error("response object not present in the response string, response received from API : {}" + responsestring);
				}
			}
		} catch (Exception e) {
			logger.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
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
			logger.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}
		return componentUrl;
	}

	private Long makeSchedulerStartEntry(long totalRecordCount, Long schedulerId) {
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
			headerMap.put(GeneralConstants.JOB_PROCESSING_ID.getConstantVal(), jobProcessId);

			String responsestring = restServiceClient.callRestWebServiceWithMultipleHeader(componentUrl, schedulerLog, null, headerMap);

			Type listToken = new TypeToken<ServiceResponse>() {
			}.getType();
			ServiceResponse serviceResponse = JsonUtility.getGsonObject().fromJson(responsestring, listToken);
			Long schedulerLogId = null;

			if (!serviceResponse.isStatus()) {
				logger.error("False status received from API with status message " + serviceResponse.getStatusCode());
				return null;
			} else {
				listToken = new TypeToken<SchedulerLog>() {
				}.getType();
				SchedulerLog schedulerLog1 = JsonUtility.getGsonObject().fromJson(serviceResponse.getResponse() + "", listToken);
				schedulerLogId = schedulerLog1.getId();
			}
			return schedulerLogId;
		} catch (Exception e) {
			logger.error(ErrorConstants.DEFAULT_ERROR.getConstantVal(), e);
		}
		return null;
	}

}
