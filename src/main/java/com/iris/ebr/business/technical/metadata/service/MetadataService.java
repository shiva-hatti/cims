package com.iris.ebr.business.technical.metadata.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.sql.DataSource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.ebr.business.technical.metadata.bean.BusMetadatProcessBean;
import com.iris.ebr.business.technical.metadata.bean.CSVItemMappingBean;
import com.iris.ebr.business.technical.metadata.bean.CodeListDimension;
import com.iris.ebr.business.technical.metadata.bean.CommonDimension;
import com.iris.ebr.business.technical.metadata.bean.DimCombination;
import com.iris.ebr.business.technical.metadata.bean.EbrRbrTableDto;
import com.iris.ebr.business.technical.metadata.bean.ElementDimensionBean;
import com.iris.ebr.business.technical.metadata.bean.ElementDimensionStoredJson;
import com.iris.ebr.business.technical.metadata.bean.InputDimension;
import com.iris.ebr.business.technical.metadata.bean.ItemCodeBeanForMapping;
import com.iris.ebr.business.technical.metadata.bean.ItemDto;
import com.iris.ebr.business.technical.metadata.bean.ReadReturnCSVForEBRMapping;
import com.iris.ebr.business.technical.metadata.bean.SDMXReturnModelBean;
import com.iris.ebr.business.technical.metadata.bean.TechMetadatProcessBean;
import com.iris.ebr.business.technical.metadata.bean.TechMetadataDto;
import com.iris.ebr.business.technical.metadata.bean.TechMetadatareturnDto;
import com.iris.ebr.business.technical.metadata.constant.ExcelContstant;
import com.iris.ebr.business.technical.metadata.entity.BusMetadatProcess;
import com.iris.ebr.business.technical.metadata.entity.TechMetadatProcess;
import com.iris.ebr.business.technical.metadata.repo.MetadataRepo;
import com.iris.ebr.business.technical.metadata.repo.TechMetadataRepo;
import com.iris.exception.ServiceException;
import com.iris.model.UserMaster;
import com.iris.service.GenericService;
import com.iris.util.ResourceUtil;
import com.iris.util.Validations;
import com.mysql.cj.jdbc.result.ResultSetMetaData;

@Service
public class MetadataService implements GenericService<BusMetadatProcess, Long> {

	private static final Logger LOGGER = LogManager.getLogger(MetadataService.class);
	private String filename = "";
	//private HSSFWorkbook workbook;
	private Map<Integer, ItemCodeBeanForMapping> itemCodeMap = null;
	private Map<String, List<String>> rowHeaderMap = null;
	private Map<String, List<EbrRbrTableDto>> ebrRbrTableMap = null;
	private Map<String, ItemDto> oldItemDetailMap = null;
	private static boolean SET_LENIENT = DateConstants.SET_LENIENT.isDateConstantsBoolean();
	private Map<String, String> dmidMap = new TreeMap<>();
	private Map<String, TechMetadataDto> techMetadataRbrColumnMap = new TreeMap<String, TechMetadataDto>();
	private Set<String> itemFreeTextApplicableSet = null;
	private Map<String, Set<String>> dimMapData = null;
	private BusMetadatProcess busMetadatProcess = null;
	private int excelRowOutputCount = 0;
	private int excelCreateCellOutoutCount = 0;
	private HSSFSheet sheet2;
	private HSSFRow outputRow;
	private static final String DD_SLASH_MM_SLASH_YYYY = "dd/MM/yyyy";
	private static final String calendarFormat = "en";
	private static final String timeFormat = DateConstants.HH_MM_SS.getDateConstants().toLowerCase() + " " + DateConstants.AM_PM.getDateConstants();
	private static boolean itemFreeTextFoundForEntry = false;
	private List<TechMetadataDto> returnWiseExtraNaList = new ArrayList<TechMetadataDto>();

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private MetadataRepo metadataRepo;

	@Autowired
	DataSource datasource;

	@Autowired
	TechMetadataRepo techMetadataRepo;

	@Override
	public BusMetadatProcess add(BusMetadatProcess entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(BusMetadatProcess entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<BusMetadatProcess> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BusMetadatProcess getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BusMetadatProcess> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BusMetadatProcess> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BusMetadatProcess> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BusMetadatProcess> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BusMetadatProcess> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(BusMetadatProcess bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Transactional(rollbackFor = Exception.class)
	public List<BusMetadatProcessBean> sdmxDataMappingConvertToExcel(Boolean isActive, String returnCode, String returnVersion, String csvPathHavingEBRCellRef, String ebrVersion, UserMaster userMaster, String fileName, String returnName, Long agencyID) throws Exception {

		LOGGER.info("Business Metadata Generation Start : ");

		HSSFWorkbook workbook = new HSSFWorkbook();

		String key = "";
		String query = " SELECT " + " TSE.DSD_CODE ," + " TSE.ELEMENT_VER ," + " TSE.ELEMENT_LABEL ," + " TSED.ELEMENT_DIMENSIONS ," + " TSMC.MODEL_DIM ," + " TSMC.MODEL_CODE ," + " TSRSI.SHEET_CODE ," + " TR.RETURN_CODE ," + " TR.RETURN_NAME ," + " TSRSI.SHEET_NAME ," + " TSRSI.SECTION_CODE ," + " TSRSI.SECTION_NAME ," + " TSRMI.RETURN_CELL_REF ," + " TR.FREQUENCY_ID_FK , TSMC.MODEL_DIM_HASH ,TN.NATURE_NAME " +

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		" FROM TBL_SDMX_RETURN_SHEET_INFO TSRSI  "
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		+ " JOIN TBL_SDMX_RETURN_MODEL_INFO  TSRMI ON TSRMI.RETURN_SHEET_INFO_ID_FK = TSRSI.RETURN_SHEET_INFO_ID "
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		+ " JOIN TBL_SDMX_MODEL_CODES TSMC ON TSMC.MODEL_CODES_ID = TSRMI.MODEL_CODES_ID_FK "
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		+ " JOIN TBL_SDMX_ELEMENT TSE ON TSE.ELEMENT_ID = TSMC.ELEMENT_ID_FK "
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		+ " JOIN TBL_SDMX_ELEMENT_DIMENSIONS TSED ON TSED.ELEMENT_ID_FK = TSE.ELEMENT_ID "
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		+ " JOIN TBL_RETURN_TEMPLATE TRT ON TRT.RETURN_TEMPLATE_ID = TSRSI.RETURN_TEMPLATE_ID_FK "
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		+ " JOIN TBL_RETURN TR ON TR.RETURN_ID = TRT.RETURN_ID_FK "
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		+ " JOIN TBL_SDMX_RETURN_PREVIEW SRP ON TSRSI.RETURN_PREVIEW_ID_FK = SRP.RETURN_PREVIEW_TYPE_ID  "
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		+ " JOIN TBL_SDMX_ELE_NATURE TN ON TN.NATURE_ID = TSE.NATURE_ID_FK ";

		if (isActive != null) {
			if (isActive.booleanValue()) {
				query = query + " WHERE (  TSRMI.IS_ACTIVE = '1' AND SRP.IS_ACTIVE = '1'  AND SRP.IS_PUBLISHED = '1' AND SRP.AGENCY_MASTER_ID_FK = " + agencyID + "  AND TSMC.IS_ACTIVE = '1'  AND TSE.IS_ACTIVE = '1' AND TSED.IS_ACTIVE = '1' AND TRT.IS_ACTIVE = '1' AND TR.IS_ACTIVE = '1' AND TR.RETURN_CODE = '" + returnCode + "' AND SRP.EBR_VERSION = '" + ebrVersion + "' )  ";
			} else {
				query = query + " WHERE (  TSRMI.IS_ACTIVE = '0' AND TSMC.IS_ACTIVE = TSRMI.IS_ACTIVE AND TSE.IS_ACTIVE = TSRMI.IS_ACTIVE AND TSED.IS_ACTIVE = TSRMI.IS_ACTIVE AND TRT.IS_ACTIVE = TSRMI.IS_ACTIVE AND TR.IS_ACTIVE = TSRMI.IS_ACTIVE AND TR.RETURN_CODE = '" + returnCode + "'  AND SRP.EBR_VERSION = '" + ebrVersion + "' )  ";
			}
		}

		query = query + " ORDER BY TSE.DSD_CODE , TSE.ELEMENT_VER ";
		LOGGER.info("SQL to execute: " + query);

		List<Tuple> tuples = entityManager.createNativeQuery(query.toString(), Tuple.class).getResultList();
		Date createdOn = new Date();
		busMetadatProcess = new BusMetadatProcess();
		busMetadatProcess.setReturnCode(returnCode);
		busMetadatProcess.setReturnVersion(returnVersion);
		busMetadatProcess.setEbrVersion(ebrVersion);
		busMetadatProcess.setItemMasterUploadedOn(createdOn);
		busMetadatProcess.setItemMasterCreatedBy(userMaster);
		busMetadatProcess.setItemMasterFileName(fileName);
		busMetadatProcess.setReturnName(returnName);

		List<ElementDimensionBean> elementDimensionBeans = null;
		ElementDimensionBean bean = null;
		int p = 0;
		Map<String, List<ElementDimensionBean>> elementData = new TreeMap();
		Map<Integer, String> frequencyMap = getFrequncyMap();

		for (Tuple tuple : tuples) {
			key = (String) tuple.get("DSD_CODE") + "#@" + (String) tuple.get("ELEMENT_VER");
			if (p == 0) {
				p++;
				elementDimensionBeans = new ArrayList<>();
				bean = new ElementDimensionBean();

				bean.setDsdCode((String) tuple.get("DSD_CODE"));

				bean.setElementVersion((String) tuple.get("ELEMENT_VER"));

				bean.setElementLabel((String) tuple.get("ELEMENT_LABEL"));
				bean.setElementDimensions((String) tuple.get("ELEMENT_DIMENSIONS"));

				bean.setModelDim((String) tuple.get("MODEL_DIM"));
				bean.setModelCode((String) tuple.get("MODEL_CODE"));

				bean.setElementReturnTempSheetNo((String) tuple.get("SHEET_CODE"));
				returnCode = (String) tuple.get("RETURN_CODE");

				bean.setReturnCode((String) tuple.get("RETURN_CODE"));

				bean.setReturnName((String) tuple.get("RETURN_NAME"));

				bean.setReturnTempSheetName((String) tuple.get("SHEET_NAME"));

				bean.setReturnSectionCode((String) tuple.get("SECTION_CODE"));

				bean.setReturnSectionName((String) tuple.get("SECTION_NAME"));

				bean.setElementDimensions((String) tuple.get("ELEMENT_DIMENSIONS"));

				bean.setReturnCellRef(tuple.get("RETURN_CELL_REF").toString());

				bean.setFrequency(frequencyMap.get((Integer) tuple.get("FREQUENCY_ID_FK")));

				bean.setEncodedString((String) tuple.get("MODEL_DIM_HASH"));

				bean.setNatureName((String) tuple.get("NATURE_NAME"));

				elementDimensionBeans.add(bean);
				elementData.put(key, elementDimensionBeans);
			} else {

				bean = new ElementDimensionBean();

				bean.setDsdCode((String) tuple.get("DSD_CODE"));
				bean.setElementVersion((String) tuple.get("ELEMENT_VER"));
				bean.setElementLabel((String) tuple.get("ELEMENT_LABEL"));
				bean.setElementDimensions((String) tuple.get("ELEMENT_DIMENSIONS"));
				bean.setModelDim((String) tuple.get("MODEL_DIM"));
				bean.setModelCode((String) tuple.get("MODEL_CODE"));
				bean.setElementReturnTempSheetNo((String) tuple.get("SHEET_CODE"));
				bean.setReturnCode((String) tuple.get("RETURN_CODE"));
				bean.setReturnName((String) tuple.get("RETURN_NAME"));
				bean.setReturnTempSheetName((String) tuple.get("SHEET_NAME"));
				bean.setReturnSectionCode((String) tuple.get("SECTION_CODE"));
				bean.setReturnSectionName((String) tuple.get("SECTION_NAME"));
				bean.setElementDimensions((String) tuple.get("ELEMENT_DIMENSIONS"));
				bean.setReturnCellRef(tuple.get("RETURN_CELL_REF").toString());
				bean.setFrequency(frequencyMap.get((Integer) tuple.get("FREQUENCY_ID_FK")));
				bean.setEncodedString((String) tuple.get("MODEL_DIM_HASH"));
				bean.setNatureName((String) tuple.get("NATURE_NAME"));

				if (elementData.containsKey(key)) {
					elementDimensionBeans = elementData.get(key);
				} else {
					elementDimensionBeans = new ArrayList<>();
				}

				elementDimensionBeans.add(bean);
				elementData.put(key, elementDimensionBeans);
			}
		}

		loadFactTableForItemFreeText(returnCode);

		/*
		 * THIS CODE CREATE DYNAMIC ELEMENT WISE SHEET FOR UNIQUE ELEMENT
		 */

		int sheetCount = 0;
		dmidMap = new TreeMap<>();
		for (Map.Entry<String, List<ElementDimensionBean>> entry : elementData.entrySet()) {
			String elementNameAndVerCombo = entry.getKey();
			String[] elementNameAndVer = elementNameAndVerCombo.split("#@", 2);
			String elementName = elementNameAndVer[0];
			String elementVersion = elementNameAndVer[1];
			createMappingExcel(elementName + "__" + elementVersion, sheetCount, entry.getValue(), csvPathHavingEBRCellRef, returnCode, ebrVersion, workbook);
			Date metadataCreatedOn = new Date();
			if (sheetCount == 0) {
				busMetadatProcess.setBussCreatedOn(metadataCreatedOn);
				//busMetadatProcess.setBusCreatedBy(userMaster);
			}
			sheetCount++;

		}

		/*
		 * THIS CODE CREATE EBR_RBR_TABLE_STRUCTURE SHEET USING ELEMENT WISE SHEEET
		 * HEADER COLUMN
		 */

		String sheetNameForMappingTblStr = ExcelContstant.MAP_EBR_RBR_LND_COL_TECH_METADATA.getExcelColumnConstants();
		createEbrRbrMappingTableStructure(elementData, rowHeaderMap, sheetNameForMappingTblStr, workbook);

		/*
		 * This Code Create EBR To RBR Table Mapping EXCEL
		 */

		String ebrRbrTableSheetName = ExcelContstant.MAP_EBR_RBR_LND_TBL_TECH_METADATA.getExcelColumnConstants();
		createEbrRbrTableMap(ebrRbrTableMap, ebrRbrTableSheetName, workbook);

		/*
		 * This Code For Creating Automatic DDL Creation
		 */

		// updateDB();

		//updateUserInfo(returnCode,returnVersion,ebrVersion,userMaster,fileName);
		LOGGER.info("Get Only Active Record");
		List<BusMetadatProcessBean> busMetadatProcessBean = getActiveBusinessMetadata(returnCode, returnVersion, ebrVersion, false);
		return busMetadatProcessBean;

	}

	public List<BusMetadatProcessBean> getActiveBusinessMetadata(String returnCode, String returnVersion, String ebrVersion, boolean ValideErrorFound) throws Exception {
		List<BusMetadatProcessBean> busMetadatProcessBean = null;
		LOGGER.info("Get Only Active Record");
		BusMetadatProcess busMetadatProcess = metadataRepo.getActiveRecordForReturnAndEbrVersion(returnCode, returnVersion, ebrVersion);
		if (busMetadatProcess != null) {
			busMetadatProcessBean = new ArrayList<>();
			BusMetadatProcessBean bean = new BusMetadatProcessBean();
			bean.setReturnCode(busMetadatProcess.getReturnCode());
			bean.setEbrVersion(busMetadatProcess.getEbrVersion());
			bean.setReturnVersion(busMetadatProcess.getReturnVersion());
			bean.setBussFileName(busMetadatProcess.getBussFileName());
			bean.setItemMasterFileName(busMetadatProcess.getItemMasterFileName());
			bean.setProcessExecutedBy(busMetadatProcess.getItemMasterCreatedBy().getUserName());
			bean.setBusMetadataProcessId(busMetadatProcess.getBusMetadataProcessId());
			bean.setBussValidateFileName(busMetadatProcess.getBussValidateFileName());
			bean.setReturnName(busMetadatProcess.getReturnName());
			bean.setValideError(ValideErrorFound);
			bean.setInsertStatus(busMetadatProcess.getInsertStatus());

			if (busMetadatProcess.getBussMetadataInsertStart() != null) {
				String insertStartDate = DateManip.formatAppDateTime(busMetadatProcess.getBussMetadataInsertStart(), DD_SLASH_MM_SLASH_YYYY + " " + timeFormat, calendarFormat);
				bean.setInsertStartTimeStr(insertStartDate);
			} else {
				bean.setInsertStartTimeStr("");
			}

			if (busMetadatProcess.getBussMetadataInsertEnd() != null) {
				String insertEndDate = DateManip.formatAppDateTime(busMetadatProcess.getBussMetadataInsertEnd(), DD_SLASH_MM_SLASH_YYYY + " " + timeFormat, calendarFormat);
				bean.setInsertEndTimeStr(insertEndDate);
			} else {
				bean.setInsertEndTimeStr("");
			}

			busMetadatProcessBean.add(bean);
			LOGGER.info("only Active Record for return found");
		}

		return busMetadatProcessBean;
	}

	public List<BusMetadatProcessBean> getActiveBusinessMetadataWrapper(Long metadataProcessI) throws Exception {
		List<BusMetadatProcessBean> busMetadatProcessBean = null;
		LOGGER.info("Get Only Active Record");
		BusMetadatProcess busMetadatProcess = metadataRepo.getOne(metadataProcessI);
		if (busMetadatProcess != null) {
			busMetadatProcessBean = new ArrayList<>();
			BusMetadatProcessBean bean = new BusMetadatProcessBean();
			bean.setReturnCode(busMetadatProcess.getReturnCode());
			bean.setEbrVersion(busMetadatProcess.getEbrVersion());
			bean.setReturnVersion(busMetadatProcess.getReturnVersion());
			bean.setBussFileName(busMetadatProcess.getBussFileName());
			bean.setItemMasterFileName(busMetadatProcess.getItemMasterFileName());
			bean.setProcessExecutedBy(busMetadatProcess.getItemMasterCreatedBy().getUserName());
			bean.setBusMetadataProcessId(busMetadatProcess.getBusMetadataProcessId());
			bean.setBussValidateFileName(busMetadatProcess.getBussValidateFileName());
			bean.setReturnName(busMetadatProcess.getReturnName());
			bean.setValideError(false);
			bean.setInsertStatus(busMetadatProcess.getInsertStatus());

			if (busMetadatProcess.getBussMetadataInsertStart() != null) {
				String insertStartDate = DateManip.formatAppDateTime(busMetadatProcess.getBussMetadataInsertStart(), DD_SLASH_MM_SLASH_YYYY + " " + timeFormat, calendarFormat);
				bean.setInsertStartTimeStr(insertStartDate);
			} else {
				bean.setInsertStartTimeStr("");
			}

			if (busMetadatProcess.getBussMetadataInsertEnd() != null) {
				String insertEndDate = DateManip.formatAppDateTime(busMetadatProcess.getBussMetadataInsertEnd(), DD_SLASH_MM_SLASH_YYYY + " " + timeFormat, calendarFormat);
				bean.setInsertEndTimeStr(insertEndDate);
			} else {
				bean.setInsertEndTimeStr("");
			}

			busMetadatProcessBean.add(bean);
		}

		return busMetadatProcessBean;
	}

	public Set<String> loadFactTableForItemFreeText(String returnCodeName) throws SQLException {
		LOGGER.info("load 181 sheet for ITEM_FREE_TEXT : ");
		String returnTableName = "";
		itemFreeTextApplicableSet = new HashSet<>();
		String columnName = "ITEM_FREE_TEXT";

		String sql = "select DW_HIVE_TBL_NAME from TBL_DW_HIVE_TBL_DESIGN where DW_HIVE_TBL_RETURN_CODE like '%" + returnCodeName + "%' " + " and DW_HIVE_TBL_COLS like '%" + columnName + "%' and DW_HIVE_TBL_COLS != 'ITEM_FREE_TEXT_SORT_ORDER' ";
		LOGGER.info("load 181 sheet for ITEM_FREE_TEXT Query : " + sql);

		List<Tuple> tuples = entityManager.createNativeQuery(sql.toString(), Tuple.class).getResultList();

		for (Tuple tuple : tuples) {
			if ((String) tuple.get("DW_HIVE_TBL_NAME") != null) {
				returnTableName = (String) tuple.get("DW_HIVE_TBL_NAME");
				if (!itemFreeTextApplicableSet.contains(returnTableName)) {
					itemFreeTextApplicableSet.add(returnTableName);
				}
			}
		}

		return itemFreeTextApplicableSet;
	}

	public void updateUserInfo(String returnCode, String returnVersion, String ebrVersion, UserMaster userMaster, String fileName) throws Exception {

	}

	public Map<Integer, String> getFrequncyMap() {
		Map<Integer, String> frequencyMap = new HashMap<Integer, String>();
		frequencyMap.put(1, "ANLY");
		frequencyMap.put(2, "HFYRLY");
		frequencyMap.put(3, "QTRLY");
		frequencyMap.put(4, "MNTH");
		frequencyMap.put(5, "FRTNT");
		frequencyMap.put(6, "WKLY");
		frequencyMap.put(7, "DLY");
		return frequencyMap;

	}

	/*
	 * THIS CODE CREATE DYNAMIC ELEMENT WISE SHEET FOR UNIQUE ELEMENT
	 */
	public void createMappingExcel(String sheetName, int sheetCount, List<ElementDimensionBean> elementDimensionBeans, String csvPathHavingEBRCellRef, String returnCodeTemp, String ebrVersionData, HSSFWorkbook workbook) throws Exception {
		LOGGER.info("Business Metadata Sheet Start Generate Element Wise");

		//ITEM_FREE_TEXT Logic

		boolean elementFound = false;
		boolean microFound = false;
		boolean othSpecifyDimFound = false;
		boolean itemFreeTextFoundWithOthDim = false;
		boolean itemFreeTextBoolean = false;

		if (!CollectionUtils.isEmpty(elementDimensionBeans)) {
			String dsdCode = elementDimensionBeans.get(0).getDsdCode();
			String natureName = elementDimensionBeans.get(0).getNatureName();
			if (natureName != null) {
				if (natureName.equals(ExcelContstant.MICRO.getExcelColumnConstants())) {
					microFound = true;
				}

			}

			if (dsdCode.startsWith("OPS_")) {
				elementFound = true;

			}
		}

		Set<String> rbrTableSet = new HashSet<String>();

		if (sheetCount == 0) {
			LOGGER.info("Business Data Found For  return and sheetCount " + returnCodeTemp + "####" + sheetCount);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String dateString = simpleDateFormat.format(new Date()).replace(" ", "_");
			dateString = dateString.replace(":", "_");

			File fileCheck = new File(ResourceUtil.getKeyValue("filepath.root") + ResourceUtil.getKeyValue("returnTemplate.upload.path") + returnCodeTemp + File.separator + ResourceUtil.getKeyValue("filePath.SDMXFolder") + File.separator + ebrVersionData + ResourceUtil.getKeyValue("file.ebr.buss.metadata") + File.separator + "RBR_EBR_MAP_BUS_METADATA" + "_" + dateString + ".xls");

			if (!fileCheck.getParentFile().exists()) {
				fileCheck.getParentFile().mkdirs();
			}

			filename = new File(csvPathHavingEBRCellRef).getParent() + File.separator + "RBR_EBR_MAP_BUS_METADATA" + "_" + dateString + ".xls";
			busMetadatProcess.setBussFileName("RBR_EBR_MAP_BUS_METADATA" + "_" + dateString + ".xls");
			//workbook = new HSSFWorkbook();
			itemCodeMap = getCellRefrence(csvPathHavingEBRCellRef);
			rowHeaderMap = new TreeMap<>();
			ebrRbrTableMap = new TreeMap<>();
			oldItemDetailMap = new TreeMap<>();
			oldItemDetailMap = readItemMasterSheet(returnCodeTemp);
		}

		String elementCodeTemp = "";
		String elementVerTemp = "";

		Map<Integer, String> itemFreeTextMap = new HashMap<>();
		Map<Integer, String> filerToSelectMap = new HashMap<>();

		List<EbrRbrTableDto> ebrRbrTableDtos = new ArrayList<>();

		HSSFSheet sheet = workbook.createSheet(sheetName);
		HSSFRow rowhead = sheet.createRow((short) 0);

		int itemFreetextIndexColumn = 0;
		int filerToSelectIndexColumn = 0;

		int j = 0;
		int dynamicDimHeaderCount = 0;
		List<String> headerList = new ArrayList<>();

		CellStyle cellStyle1 = workbook.createCellStyle();
		cellStyle1.setFillForegroundColor(IndexedColors.AQUA.index);
		cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		rowhead.createCell(j).setCellValue(ExcelContstant.EBR_TABLE_NAME.getExcelColumnConstants());
		headerList.add(ExcelContstant.EBR_TABLE_NAME.getExcelColumnConstants());
		j++;
		rowhead.createCell(j).setCellValue(ExcelContstant.ELEMENT_CODE.getExcelColumnConstants());
		headerList.add(ExcelContstant.ELEMENT_CODE.getExcelColumnConstants());
		j++;
		rowhead.createCell(j).setCellValue(ExcelContstant.ELEMENT_VERSION.getExcelColumnConstants());
		headerList.add(ExcelContstant.ELEMENT_VERSION.getExcelColumnConstants());
		j++;
		rowhead.createCell(j).setCellValue(ExcelContstant.ELEMENT_LABEL.getExcelColumnConstants());
		headerList.add(ExcelContstant.ELEMENT_LABEL.getExcelColumnConstants());
		j++;
		rowhead.createCell(j).setCellValue(ExcelContstant.ELEMENT_DIMENSIONS.getExcelColumnConstants());
		headerList.add(ExcelContstant.ELEMENT_DIMENSIONS.getExcelColumnConstants());
		j++;
		rowhead.createCell(j).setCellValue(ExcelContstant.ELEMENT_SDMX_MODEL_DIM.getExcelColumnConstants());
		headerList.add(ExcelContstant.ELEMENT_SDMX_MODEL_DIM.getExcelColumnConstants());
		j++;

		// For Dynamic Dim Header Creation Start
		ElementDimensionStoredJson elementDimensionStoredJson = new Gson().fromJson(elementDimensionBeans.get(0).getElementDimensions(), ElementDimensionStoredJson.class);
		int dynamicHeaderAppendCount = elementDimensionStoredJson.getDimCombination().size();
		for (DimCombination dim : elementDimensionStoredJson.getDimCombination()) {

			if (!StringUtils.isBlank(dim.getDimConceptId()) && !dim.getDimConceptId().equalsIgnoreCase(ExcelContstant.DMID.getExcelColumnConstants())) {

				dynamicDimHeaderCount++;
				String updatedValue = "";

				if (dim.getDimConceptId().equalsIgnoreCase(ExcelContstant.COMMENT.getExcelColumnConstants())) {
					updatedValue = ExcelContstant.COMMENTS.getExcelColumnConstants();
				} else if (dim.getDimConceptId().equalsIgnoreCase(ExcelContstant.DATE.getExcelColumnConstants())) {
					updatedValue = ExcelContstant.OBS_VALUE_RECORD_DT.getExcelColumnConstants();
				} else if (dim.getDimConceptId().equalsIgnoreCase(ExcelContstant.NOT.getExcelColumnConstants())) {
					updatedValue = ExcelContstant.NATURE_OF_TRANSACTIONS.getExcelColumnConstants();
				} else if (dim.getDimConceptId().equalsIgnoreCase(ExcelContstant.CASE.getExcelColumnConstants())) {
					updatedValue = ExcelContstant.TYPE_OF_CASE.getExcelColumnConstants();
				} else {
					updatedValue = dim.getDimConceptId().toUpperCase();
				}

				if (dim.getDimConceptId().equals(ExcelContstant.OTH_SPECIFY.getExcelColumnConstants()) && !othSpecifyDimFound) {
					othSpecifyDimFound = true;
				}

				rowhead.createCell(j).setCellValue(ExcelContstant.ELEMENT_.getExcelColumnConstants() + updatedValue);
				headerList.add(ExcelContstant.ELEMENT_.getExcelColumnConstants() + updatedValue);
				j++;

				if (dynamicHeaderAppendCount == dynamicDimHeaderCount) {
					rowhead.createCell(j).setCellValue(ExcelContstant.ELEMENT_DEPENDENCY_TYPE.getExcelColumnConstants());
					headerList.add(ExcelContstant.ELEMENT_DEPENDENCY_TYPE.getExcelColumnConstants());
					j++;
					dynamicDimHeaderCount++;
				}
			}

		}
		// end

		rowhead.createCell(j).setCellValue(ExcelContstant.ELEMENT_DMID.getExcelColumnConstants());
		headerList.add(ExcelContstant.ELEMENT_DMID.getExcelColumnConstants());
		j++;

		rowhead.createCell(j).setCellValue(ExcelContstant.RETURN_TOTAL_DISB_BAL_OUTSTD_OFWHICH_FLG.getExcelColumnConstants());
		headerList.add(ExcelContstant.RETURN_TOTAL_DISB_BAL_OUTSTD_OFWHICH_FLG.getExcelColumnConstants());
		j++;

		rowhead.createCell(j).setCellValue(ExcelContstant.ELEMENT_RETURN_TEMPLATE_SHEET_NO.getExcelColumnConstants());
		headerList.add(ExcelContstant.ELEMENT_RETURN_TEMPLATE_SHEET_NO.getExcelColumnConstants());
		j++;
		rowhead.createCell(j).setCellValue(ExcelContstant.RETURN_CODE.getExcelColumnConstants());
		headerList.add(ExcelContstant.RETURN_CODE.getExcelColumnConstants());
		j++;
		rowhead.createCell(j).setCellValue(ExcelContstant.RETURN_NAME.getExcelColumnConstants());
		headerList.add(ExcelContstant.RETURN_NAME.getExcelColumnConstants());
		j++;
		rowhead.createCell(j).setCellValue(ExcelContstant.RETURN_TEMPLATE_SHEET_NAME.getExcelColumnConstants());
		headerList.add(ExcelContstant.RETURN_TEMPLATE_SHEET_NAME.getExcelColumnConstants());
		j++;
		rowhead.createCell(j).setCellValue(ExcelContstant.RETURN_SECTION_CODE.getExcelColumnConstants());
		headerList.add(ExcelContstant.RETURN_SECTION_CODE.getExcelColumnConstants());
		j++;
		rowhead.createCell(j).setCellValue(ExcelContstant.RETURN_SECTION_NAME.getExcelColumnConstants());
		headerList.add(ExcelContstant.RETURN_SECTION_NAME.getExcelColumnConstants());
		j++;
		rowhead.createCell(j).setCellValue(ExcelContstant.RETURN_CELL_REF_NO.getExcelColumnConstants());
		headerList.add(ExcelContstant.RETURN_CELL_REF_NO.getExcelColumnConstants());
		j++;
		rowhead.createCell(j).setCellValue(ExcelContstant.RETURN_TABLE_NAME.getExcelColumnConstants());
		headerList.add(ExcelContstant.RETURN_TABLE_NAME.getExcelColumnConstants());
		j++;
		rowhead.createCell(j).setCellValue(ExcelContstant.RETURN_TEMPLATE_SHEET_NO.getExcelColumnConstants());
		headerList.add(ExcelContstant.RETURN_TEMPLATE_SHEET_NO.getExcelColumnConstants());
		j++;
		rowhead.createCell(j).setCellValue(ExcelContstant.RETURN_ITEM_CODE.getExcelColumnConstants());
		headerList.add(ExcelContstant.RETURN_ITEM_CODE.getExcelColumnConstants());
		j++;
		rowhead.createCell(j).setCellValue(ExcelContstant.RETURN_COLUMN_NAME.getExcelColumnConstants());
		headerList.add(ExcelContstant.RETURN_COLUMN_NAME.getExcelColumnConstants());
		j++;
		//
		rowhead.createCell(j).setCellValue(ExcelContstant.RETURN_ITEM_OLD_SK.getExcelColumnConstants());
		headerList.add(ExcelContstant.RETURN_ITEM_OLD_SK.getExcelColumnConstants());
		j++;

		rowhead.createCell(j).setCellValue(ExcelContstant.RETURN_ITEM_DISPLAY_TEXT.getExcelColumnConstants());
		headerList.add(ExcelContstant.RETURN_ITEM_DISPLAY_TEXT.getExcelColumnConstants());
		j++;

		rowhead.createCell(j).setCellValue(ExcelContstant.RETURN_ITEM_ACTIVE_FLAG.getExcelColumnConstants());
		headerList.add(ExcelContstant.RETURN_ITEM_ACTIVE_FLAG.getExcelColumnConstants());
		j++;

		rowhead.createCell(j).setCellValue(ExcelContstant.RETURN_ITEM_START_EFFECTIVE_DATE.getExcelColumnConstants());
		headerList.add(ExcelContstant.RETURN_ITEM_START_EFFECTIVE_DATE.getExcelColumnConstants());
		j++;

		rowhead.createCell(j).setCellValue(ExcelContstant.RETURN_ITEM_END_EFFECTIVE_DATE.getExcelColumnConstants());
		headerList.add(ExcelContstant.RETURN_ITEM_END_EFFECTIVE_DATE.getExcelColumnConstants());
		j++;

		rowhead.createCell(j).setCellValue(ExcelContstant.ENCODE_VALUE.getExcelColumnConstants());
		headerList.add(ExcelContstant.ENCODE_VALUE.getExcelColumnConstants());
		j++;

		rowhead.createCell(j).setCellValue(ExcelContstant.DECODE_VALUE.getExcelColumnConstants());
		headerList.add(ExcelContstant.DECODE_VALUE.getExcelColumnConstants());
		j++;

		rowhead.createCell(j).setCellValue(ExcelContstant.IS_EBR_RBR_APPLICABLE.getExcelColumnConstants());
		headerList.add(ExcelContstant.IS_EBR_RBR_APPLICABLE.getExcelColumnConstants());
		j++;

		rowhead.setRowStyle(cellStyle1);

		boolean itemFreeTextColumnFound = false;
		boolean filerToSelectColumnFound = false;

		boolean itemFreeTextGoInside = false;
		Set<String> returnTableNameSet = new HashSet<>();
		boolean itemFreeTextCreatedNewly = false;

		int i = 0;
		int len = 1;

		for (ElementDimensionBean elementDimensionBean : elementDimensionBeans) {
			boolean samedmidFound = false;

			elementCodeTemp = elementDimensionBean.getDsdCode();
			elementVerTemp = elementDimensionBean.getElementVersion();

			if (i == 0) {
				rowHeaderMap.put(elementDimensionBean.getDsdCode() + "#@" + elementDimensionBean.getElementVersion(), headerList);
				dmidMap.put(elementDimensionBean.getModelCode(), elementCodeTemp + "~~" + elementVerTemp);
			} else {
				if (dmidMap.containsKey(elementDimensionBean.getModelCode())) {
					samedmidFound = true;
				} else {
					dmidMap.put(elementDimensionBean.getModelCode(), elementCodeTemp + "~~" + elementVerTemp);
				}
			}

			i++;
			HSSFRow row = sheet.createRow((short) i);
			int k = 0;

			row.createCell(k).setCellValue("LND_FACT_EBR_" + elementDimensionBean.getDsdCode() + "_00" + elementDimensionBean.getElementVersion().replace(".", ""));
			k++;
			row.createCell(k).setCellValue(elementDimensionBean.getDsdCode());
			k++;
			row.createCell(k).setCellValue(elementDimensionBean.getElementVersion());
			k++;
			row.createCell(k).setCellValue(elementDimensionBean.getElementLabel());
			k++;
			row.createCell(k).setCellValue(elementDimensionBean.getElementDimensions());
			k++;
			row.createCell(k).setCellValue(elementDimensionBean.getModelDim());
			k++;

			SDMXReturnModelBean sdmxReturnModelBean = new SDMXReturnModelBean();
			sdmxReturnModelBean = new Gson().fromJson(elementDimensionBean.getModelDim(), SDMXReturnModelBean.class);
			String outStdOfWhichFlag = ExcelContstant.BLANK_SPACE.getExcelColumnConstants();
			boolean elementAndDimensionFound = false;
			// For Dynamic Dimension Value Creations Start
			for (int g = 0; g < dynamicDimHeaderCount; g++) {
				boolean columnFound = false;
				HSSFCell rowHeaderValue = rowhead.getCell(k);// 5
				String dimNameCom = rowHeaderValue.toString();
				String[] dimName = dimNameCom.split("_", 2);
				String finalDim = dimName[1];
				if (!finalDim.equals(ExcelContstant.DMID.getExcelColumnConstants())) {

					if (!Validations.isEmpty(sdmxReturnModelBean.getClosedDim())) {
						closeDimBreak: for (CodeListDimension closeDim : sdmxReturnModelBean.getClosedDim()) {
							if (closeDim.getDimConceptId().equalsIgnoreCase(finalDim)) {

								if (elementDimensionBean.getDsdCode().equals(ExcelContstant.EXP_CRDT.getExcelColumnConstants()) && finalDim.equals(ExcelContstant.EXPC_LI.getExcelColumnConstants()) && !StringUtils.isBlank(closeDim.getClValueCode())) {

									if (closeDim.getClValueCode().equals(ExcelContstant.DISBP.getExcelColumnConstants()) || closeDim.getClValueCode().equals(ExcelContstant.BOPE.getExcelColumnConstants())) {
										outStdOfWhichFlag = "1";
									} else if (closeDim.getClValueCode().equals(ExcelContstant.DISBPGH.getExcelColumnConstants()) || closeDim.getClValueCode().equals(ExcelContstant.BOPEGH.getExcelColumnConstants()) || closeDim.getClValueCode().equals(ExcelContstant.GCI.getExcelColumnConstants())) {
										outStdOfWhichFlag = "2";
									}
								} else if (elementDimensionBean.getDsdCode().equals(ExcelContstant.T20_EBK.getExcelColumnConstants())) {
									outStdOfWhichFlag = "0";
								} else if (elementDimensionBean.getDsdCode().equals(ExcelContstant.T20_FI.getExcelColumnConstants())) {
									outStdOfWhichFlag = "1";
								}

								if (!StringUtils.isBlank(closeDim.getClValueCode()) && closeDim.getClValueCode().equalsIgnoreCase(ExcelContstant.ITEM_FREE_TEXT.getExcelColumnConstants())) {
									itemFreeTextColumnFound = true;
									itemFreeTextBoolean = true;
									row.createCell(k).setCellValue(ExcelContstant.ITEM_FREE_TEXT.getExcelColumnConstants());
									k++;
									itemFreeTextMap.put(row.getRowNum(), ExcelContstant.ITEM_FREE_TEXT.getExcelColumnConstants());
								} else if (!StringUtils.isBlank(closeDim.getClValueCode()) && closeDim.getClValueCode().equalsIgnoreCase(ExcelContstant.FILER_TO_SELECT.getExcelColumnConstants())) {
									filerToSelectColumnFound = true;
									row.createCell(k).setCellValue(closeDim.getClValueCode());
									k++;
									filerToSelectMap.put(row.getRowNum(), closeDim.getClValueCode());
								} else {
									row.createCell(k).setCellValue(closeDim.getClValueCode());
									k++;
								}

								columnFound = true;
								break closeDimBreak;
							}
						}
					}

					if (!Validations.isEmpty(sdmxReturnModelBean.getOpenDimension())) {
						openDimBreak: for (InputDimension openDim : sdmxReturnModelBean.getOpenDimension()) {
							if (openDim.getDimConceptId().equalsIgnoreCase(finalDim)) {

								if (elementDimensionBean.getDsdCode().equals(ExcelContstant.EXP_CRDT.getExcelColumnConstants()) && finalDim.equals(ExcelContstant.EXPC_LI.getExcelColumnConstants()) && !StringUtils.isBlank(openDim.getClValueCode())) {

									if (openDim.getClValueCode().equals(ExcelContstant.DISBP.getExcelColumnConstants()) || openDim.getClValueCode().equals(ExcelContstant.BOPE.getExcelColumnConstants())) {
										outStdOfWhichFlag = "1";
									} else if (openDim.getClValueCode().equals(ExcelContstant.DISBPGH.getExcelColumnConstants()) || openDim.getClValueCode().equals(ExcelContstant.BOPEGH.getExcelColumnConstants()) || openDim.getClValueCode().equals(ExcelContstant.GCI.getExcelColumnConstants())) {
										outStdOfWhichFlag = "2";
									}

								} else if (elementDimensionBean.getDsdCode().equals(ExcelContstant.T20_EBK.getExcelColumnConstants())) {
									outStdOfWhichFlag = "0";
								} else if (elementDimensionBean.getDsdCode().equals(ExcelContstant.T20_FI.getExcelColumnConstants())) {
									outStdOfWhichFlag = "1";
								}

								if (!StringUtils.isBlank(openDim.getClValueCode()) && openDim.getClValueCode().equalsIgnoreCase(ExcelContstant.ITEM_FREE_TEXT.getExcelColumnConstants())) {
									itemFreeTextColumnFound = true;
									itemFreeTextBoolean = true;
									row.createCell(k).setCellValue(ExcelContstant.ITEM_FREE_TEXT.getExcelColumnConstants());

									itemFreeTextMap.put(row.getRowNum(), ExcelContstant.ITEM_FREE_TEXT.getExcelColumnConstants());
									k++;
								} else if (!StringUtils.isBlank(openDim.getClValueCode()) && openDim.getClValueCode().equalsIgnoreCase(ExcelContstant.FILER_TO_SELECT.getExcelColumnConstants())) {
									filerToSelectColumnFound = true;
									row.createCell(k).setCellValue(openDim.getClValueCode());
									k++;
									filerToSelectMap.put(row.getRowNum(), openDim.getClValueCode());
								} else {
									row.createCell(k).setCellValue(openDim.getClValueCode());
									k++;
								}

								columnFound = true;
								break openDimBreak;
							}
						}
					}

					if (!Validations.isEmpty(sdmxReturnModelBean.getCommonDimension())) {
						commonDimBreak: for (CommonDimension commDim : sdmxReturnModelBean.getCommonDimension()) {
							if (commDim.getDimConceptId().equalsIgnoreCase(finalDim)) {

								if (elementDimensionBean.getDsdCode().equals(ExcelContstant.EXP_CRDT.getExcelColumnConstants()) && finalDim.equals(ExcelContstant.EXPC_LI.getExcelColumnConstants()) && !StringUtils.isBlank(commDim.getClValueCode())) {

									if (commDim.getClValueCode().equals(ExcelContstant.DISBP.getExcelColumnConstants()) || commDim.getClValueCode().equals(ExcelContstant.BOPE.getExcelColumnConstants())) {
										outStdOfWhichFlag = "1";
									} else if (commDim.getClValueCode().equals(ExcelContstant.DISBPGH.getExcelColumnConstants()) || commDim.getClValueCode().equals(ExcelContstant.BOPEGH.getExcelColumnConstants()) || commDim.getClValueCode().equals(ExcelContstant.GCI.getExcelColumnConstants())) {
										outStdOfWhichFlag = "2";
									}

								} else if (elementDimensionBean.getDsdCode().equals(ExcelContstant.T20_EBK.getExcelColumnConstants())) {
									outStdOfWhichFlag = "0";
								} else if (elementDimensionBean.getDsdCode().equals(ExcelContstant.T20_FI.getExcelColumnConstants())) {
									outStdOfWhichFlag = "1";
								}

								if (!StringUtils.isBlank(commDim.getClValueCode()) && commDim.getClValueCode().equalsIgnoreCase(ExcelContstant.ITEM_FREE_TEXT.getExcelColumnConstants())) {
									itemFreeTextColumnFound = true;
									itemFreeTextBoolean = true;
									row.createCell(k).setCellValue(ExcelContstant.ITEM_FREE_TEXT.getExcelColumnConstants());
									itemFreeTextMap.put(row.getRowNum(), ExcelContstant.ITEM_FREE_TEXT.getExcelColumnConstants());
									k++;
								} else if (!StringUtils.isBlank(commDim.getClValueCode()) && commDim.getClValueCode().equalsIgnoreCase(ExcelContstant.FILER_TO_SELECT.getExcelColumnConstants())) {
									filerToSelectColumnFound = true;
									row.createCell(k).setCellValue(commDim.getClValueCode());
									filerToSelectMap.put(row.getRowNum(), commDim.getClValueCode());
									k++;
								} else {
									row.createCell(k).setCellValue(commDim.getClValueCode());
									k++;
								}

								columnFound = true;
								break commonDimBreak;
							}
						}
					}

					if (sdmxReturnModelBean.getModelOtherDetails() != null) {
						if (finalDim.equalsIgnoreCase(ExcelContstant.DEPENDENCY_TYPE.getExcelColumnConstants())) {

							if (!Validations.isEmpty(sdmxReturnModelBean.getModelOtherDetails().getDependencyType())) {

								row.createCell(k).setCellValue(sdmxReturnModelBean.getModelOtherDetails().getDependencyType());
								k++;
							} else {
								row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
								k++;
							}
							columnFound = true;

						}
					}

					if (!columnFound) {
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
					}
				}

				if (finalDim.equals(ExcelContstant.OTH_SPECIFY.getExcelColumnConstants()) && !itemFreeTextFoundWithOthDim) {
					itemFreeTextFoundWithOthDim = true;
				}

			}
			// end

			row.createCell(k).setCellValue(elementDimensionBean.getModelCode());
			k++;

			row.createCell(k).setCellValue(outStdOfWhichFlag);
			k++;

			row.createCell(k).setCellValue(elementDimensionBean.getElementReturnTempSheetNo());
			k++;
			row.createCell(k).setCellValue(elementDimensionBean.getReturnCode());
			k++;
			row.createCell(k).setCellValue(elementDimensionBean.getReturnName());
			k++;
			row.createCell(k).setCellValue(elementDimensionBean.getReturnTempSheetName());
			k++;
			row.createCell(k).setCellValue(elementDimensionBean.getReturnSectionCode());
			k++;
			row.createCell(k).setCellValue(elementDimensionBean.getReturnSectionName());
			k++;
			row.createCell(k).setCellValue(elementDimensionBean.getReturnCellRef());

			DataFormatter formatter = new DataFormatter();
			String cellRefValue = formatter.formatCellValue(row.getCell(k));
			ItemCodeBeanForMapping ietmBean = null;

			if (itemCodeMap != null && itemCodeMap.containsKey(Integer.parseInt(cellRefValue))) {
				ietmBean = itemCodeMap.get(Integer.parseInt(cellRefValue));
			}

			k++;

			if (ietmBean != null) {
				if (!returnTableNameSet.contains(ietmBean.getTableColumn())) {
					returnTableNameSet.add(ietmBean.getTableColumn());
				}
				row.createCell(k).setCellValue(ietmBean.getTableColumn());
				k++;
				row.createCell(k).setCellValue(ietmBean.getTemSheetNo());
				k++;
				row.createCell(k).setCellValue(ietmBean.getItemCode());
				k++;
				row.createCell(k).setCellValue(ietmBean.getColName());
				k++;
			} else {
				row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
				k++;
				row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
				k++;
				row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
				k++;
				row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
				k++;
			}

			if (ietmBean != null) {
				if (!rbrTableSet.contains(ietmBean.getTableColumn())) {
					EbrRbrTableDto ebrRbrTableDto = new EbrRbrTableDto();
					ebrRbrTableDto.setElementCode(elementDimensionBean.getDsdCode());
					ebrRbrTableDto.setElementVer(elementDimensionBean.getElementVersion());
					ebrRbrTableDto.setEbrTableName(formatter.formatCellValue(row.getCell(0)));
					ebrRbrTableDto.setBusMetaDataTableName("");
					ebrRbrTableDto.setReturnCode(elementDimensionBean.getReturnCode());
					ebrRbrTableDto.setRbrTableName(ietmBean.getTableColumn());
					ebrRbrTableDtos.add(ebrRbrTableDto);
					rbrTableSet.add(ietmBean.getTableColumn());
				}
			}

			ItemDto itemDto = null;
			if (oldItemDetailMap != null && ietmBean != null && oldItemDetailMap.containsKey(returnCodeTemp + "~" + ietmBean.getItemCode())) {
				itemDto = oldItemDetailMap.get(returnCodeTemp + "~" + ietmBean.getItemCode());
			}

			if (itemDto != null) {
				row.createCell(k).setCellValue(itemDto.getItemOldSk());
				k++;
				row.createCell(k).setCellValue(itemDto.getItemDisplayText());
				k++;
				row.createCell(k).setCellValue(itemDto.getActiveFlag());
				k++;

				String startDate = "";
				String endDate = "";

				if (!StringUtils.isBlank(itemDto.getStartEffectiveDate())) {
					startDate = convertDate(itemDto.getStartEffectiveDate(), "dd/MMM/yyyy", DateConstants.LOCALE_ENG.getDateConstantsLocale(), DateConstants.YYYY_MM_DD.getDateConstants(), DateConstants.LOCALE_ENG.getDateConstantsLocale());
				}

				if (!StringUtils.isBlank(itemDto.getEndEffectiveDate())) {
					endDate = convertDate(itemDto.getEndEffectiveDate(), "dd/MMM/yyyy", DateConstants.LOCALE_ENG.getDateConstantsLocale(), DateConstants.YYYY_MM_DD.getDateConstants(), DateConstants.LOCALE_ENG.getDateConstantsLocale());
				}

				row.createCell(k).setCellValue(startDate);
				k++;
				row.createCell(k).setCellValue(endDate);
				k++;
			} else {
				row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
				k++;
				row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
				k++;
				row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
				k++;
				row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
				k++;
				row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
				k++;
			}

			row.createCell(k).setCellValue(elementDimensionBean.getEncodedString());
			k++;

			byte[] encodeStringByte = Base64.decodeBase64(elementDimensionBean.getEncodedString());
			String decodedString = new String(encodeStringByte);
			Gson gson = new Gson();
			String str = sortJsonStr(decodedString, gson);
			row.createCell(k).setCellValue(str);
			k++;

			//Check Duplicate DMID Logic Here 
			if (!samedmidFound) {
				row.createCell(k).setCellValue(ExcelContstant.IS_EBR_RBR_APPLICABLE_YES.getExcelColumnConstants());
			} else {
				row.createCell(k).setCellValue(ExcelContstant.IS_EBR_RBR_APPLICABLE_NO.getExcelColumnConstants());
			}

			k++;

			// These Code Will Create for Dynamically Item_free_text Column

			if (len == elementDimensionBeans.size() && !CollectionUtils.isEmpty(returnTableNameSet)) {
				/*
				 * rowhead.createCell(j).setCellValue(ExcelContstant.RETURN_ITEM_FREE_TEXT.
				 * getExcelColumnConstants());
				 * headerList.add(ExcelContstant.RETURN_ITEM_FREE_TEXT.getExcelColumnConstants()
				 * ); itemFreetextIndexColumn = j; j++;
				 */
				//itemFreeTextGoInside = true;

				itemFreeTectBreak: for (String strTableName : returnTableNameSet) {
					if (itemFreeTextApplicableSet.contains(strTableName)) {
						rowhead.createCell(j).setCellValue(ExcelContstant.RETURN_ITEM_FREE_TEXT.getExcelColumnConstants());
						headerList.add(ExcelContstant.RETURN_ITEM_FREE_TEXT.getExcelColumnConstants());
						itemFreetextIndexColumn = j;
						j++;
						itemFreeTextCreatedNewly = true;
						break itemFreeTectBreak;
					}
				}
			}

			if (len == elementDimensionBeans.size() && filerToSelectColumnFound) {
				rowhead.createCell(j).setCellValue(ExcelContstant.RETURN_FILER_TO_SELECT.getExcelColumnConstants());
				headerList.add(ExcelContstant.RETURN_FILER_TO_SELECT.getExcelColumnConstants());
				filerToSelectIndexColumn = j;
				j++;
			}

			/*
			 * if(itemFreeTextGoInside && !CollectionUtils.isEmpty(returnTableNameSet) &&
			 * len == elementDimensionBeans.size() ) { itemFreeTectBreak:for(String
			 * strTableName :returnTableNameSet ) {
			 * if(itemFreeTextApplicableSet.contains(strTableName)) {
			 * rowhead.createCell(j).setCellValue(ExcelContstant.RETURN_ITEM_FREE_TEXT.
			 * getExcelColumnConstants());
			 * headerList.add(ExcelContstant.RETURN_ITEM_FREE_TEXT.getExcelColumnConstants()
			 * ); itemFreetextIndexColumn = j; j++; itemFreeTextCreatedNewly = true; break
			 * itemFreeTectBreak; } } }
			 */

			/*
			 * if (len == elementDimensionBeans.size() && elementFound && microFound &&
			 * othSpecifyDimFound && itemFreeTextFoundWithOthDim && itemFreeTextBoolean) {
			 * int t = 0; for (Row r : sheet) { String itemFreeTextValue = ""; if (t != 0) {
			 * if (itemFreeTextMap.containsKey(t)) { itemFreeTextValue =
			 * itemFreeTextMap.get(t); } else { itemFreeTextValue = "N_A"; }
			 * r.createCell(itemFreetextIndexColumn).setCellValue(itemFreeTextValue); } t++;
			 * } }else
			 */
			if (len == elementDimensionBeans.size() && itemFreeTextCreatedNewly) {
				int t = 0;
				for (Row r : sheet) {
					String itemFreeTextValue = ExcelContstant.BLANK_SPACE.getExcelColumnConstants();
					if (t != 0) {
						if (itemFreeTextMap.containsKey(t) && microFound && itemFreeTextFoundWithOthDim) {
							itemFreeTextValue = itemFreeTextMap.get(t);
						} else {
							itemFreeTextValue = ExcelContstant.N_A.getExcelColumnConstants();
						}
						r.createCell(itemFreetextIndexColumn).setCellValue(itemFreeTextValue);
					}
					t++;
				}
			}

			if (len == elementDimensionBeans.size() && filerToSelectColumnFound) {
				int t = 0;
				for (Row r : sheet) {
					String filerToSelectValues = ExcelContstant.BLANK_SPACE.getExcelColumnConstants();
					if (t != 0) {
						if (filerToSelectMap.containsKey(t)) {
							filerToSelectValues = filerToSelectMap.get(t);
						} else {
							filerToSelectValues = ExcelContstant.N_A.getExcelColumnConstants();
						}
						r.createCell(filerToSelectIndexColumn).setCellValue(filerToSelectValues);
					}
					t++;
				}
			}

			len++;
		}

		ebrRbrTableMap.put(elementCodeTemp + "###" + elementVerTemp, ebrRbrTableDtos);
		LOGGER.info("File Name For Element Wise Business Metadata  " + filename);
		FileOutputStream fileOut = new FileOutputStream(filename);
		workbook.write(fileOut);
		fileOut.close();

	}

	private String sortJsonStr(String jsonString, Gson gson) {
		SortedMap<String, Object> retMap = gson.fromJson(jsonString, new TypeToken<TreeMap<String, Object>>() {
		}.getType());
		return gson.toJson(retMap);
	}

	@SuppressWarnings("unlikely-arg-type")
	public Map<Integer, ItemCodeBeanForMapping> getCellRefrence(String csvPathHavingEBRCellRef) throws Exception {
		LOGGER.info("RBR CSV Reading Start ");

		Map<Integer, ItemCodeBeanForMapping> itemCodeMap = new TreeMap<>();
		ItemCodeBeanForMapping itemBean = null;

		ReadReturnCSVForEBRMapping readReturnCSVForEBRMapping = new ReadReturnCSVForEBRMapping();
		List<CSVItemMappingBean> csvItemMappingBeans = readReturnCSVForEBRMapping.readDataFromCustomSeperator(csvPathHavingEBRCellRef);

		if (!Validations.isEmpty(csvItemMappingBeans)) {
			for (CSVItemMappingBean csvItemMappingBean : csvItemMappingBeans) {

				if (csvItemMappingBean.getSdmxCellRef() == 0) {
					continue;
				}

				itemBean = new ItemCodeBeanForMapping();
				itemBean.setCellRef(csvItemMappingBean.getSdmxCellRef());
				itemBean.setColName(csvItemMappingBean.getColumnName());
				itemBean.setItemCode(csvItemMappingBean.getItemCode());
				itemBean.setTableColumn(csvItemMappingBean.getTableName());
				itemBean.setTemSheetNo(csvItemMappingBean.getTemplateSheetNo());

				if (!itemCodeMap.containsKey(itemBean.getCellRef())) {
					itemCodeMap.put(itemBean.getCellRef(), itemBean);
				}
			}
		}
		LOGGER.info("RBR CSV Reading End ");
		return itemCodeMap;

	}

	/*
	 * THIS CODE READ 236 EXCEL WHICH IS PROVIDED BY TCS AND READ ITEM_CODE SPECIFIC
	 * INFO
	 */
	public Map<String, ItemDto> readItemMasterSheet(String returnCode) throws Exception {

		String key = "";
		String query = " SELECT " + " TDTIM.ITEM_CODE ," + " TDTIM.ACTIVE_FLAG ," + " TDTIM.START_EFFECTIVE_DATE ," + " TDTIM.END_EFFECTIVE_DATE ," + " TDTIM.ITEM_DISPLAY_TEXT ," + " TDTIM.RETURN_CODE ," + " TDTIM.ITEM_OLD_SK " + " FROM TBL_DW_TEMP_ITEM_MASTER TDTIM  ";

		query = query + " WHERE TDTIM.RETURN_CODE = '" + returnCode + "'";
		LOGGER.info("SQL to execute: " + query);

		List<Tuple> tuples = entityManager.createNativeQuery(query.toString(), Tuple.class).getResultList();

		Map<String, ItemDto> itemMasterMap = new TreeMap<>();

		int p = 0;
		ItemDto itemBean = null;

		for (Tuple tuple : tuples) {
			key = (String) tuple.get("RETURN_CODE") + "~" + (String) tuple.get("ITEM_CODE");
			if (p == 0) {
				p++;
				itemBean = new ItemDto();
				itemBean.setActiveFlag(tuple.get("ACTIVE_FLAG").toString());
				itemBean.setEndEffectiveDate((String) tuple.get("END_EFFECTIVE_DATE"));
				itemBean.setItemCode((String) tuple.get("ITEM_CODE"));
				itemBean.setItemDisplayText((String) tuple.get("ITEM_DISPLAY_TEXT"));
				itemBean.setItemOldSk((String) tuple.get("ITEM_OLD_SK"));
				itemBean.setReturnCode((String) tuple.get("RETURN_CODE"));
				itemBean.setStartEffectiveDate((String) tuple.get("START_EFFECTIVE_DATE"));
				itemMasterMap.put(key, itemBean);
			} else {
				if (!itemMasterMap.containsKey(key)) {
					itemBean = new ItemDto();
					itemBean.setActiveFlag(tuple.get("ACTIVE_FLAG").toString());
					itemBean.setEndEffectiveDate((String) tuple.get("END_EFFECTIVE_DATE"));
					itemBean.setItemCode((String) tuple.get("ITEM_CODE"));
					itemBean.setItemDisplayText((String) tuple.get("ITEM_DISPLAY_TEXT"));
					itemBean.setItemOldSk((String) tuple.get("ITEM_OLD_SK"));
					itemBean.setReturnCode((String) tuple.get("RETURN_CODE"));
					itemBean.setStartEffectiveDate((String) tuple.get("START_EFFECTIVE_DATE"));
					itemMasterMap.put(key, itemBean);
				}
			}
		}
		return itemMasterMap;
	}

	public String convertDate(String inDate, String inDateFmt, Locale inLocale, String outDateFmt, Locale outLocale) throws Exception {
		String outDt = null;
		SimpleDateFormat sdf = new SimpleDateFormat(inDateFmt, inLocale);
		sdf.setLenient(SET_LENIENT);
		Date baseDate = sdf.parse(inDate);
		DateFormat dateFormat = new SimpleDateFormat(outDateFmt, outLocale);
		outDt = dateFormat.format(baseDate);
		return outDt;
	}

	public void createEbrRbrMappingTableStructure(Map<String, List<ElementDimensionBean>> elementData, Map<String, List<String>> rowHeaderMapData, String sheetName, HSSFWorkbook workbook) throws Exception {

		LOGGER.info("In Business Metadata Element Wise Sheet Start: ");
		workbook.getNumberOfSheets();

		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			LOGGER.info("element Wise sheet Name : " + workbook.getSheetName(i));
		}
		LOGGER.info("Sheet Name #####WorkbookSheetName  " + sheetName);

		HSSFSheet sheet = null;
		try {
			sheet = workbook.createSheet(sheetName);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			LOGGER.error("#####WorkbookData" + ExceptionUtils.getFullStackTrace(e));
			throw e;
		}

		//HSSFSheet sheet = workbook.createSheet(sheetName);
		HSSFRow rowhead = sheet.createRow((short) 0);

		List<String> headerDetails = new ArrayList<>();

		int h = 0;
		rowhead.createCell(h).setCellValue(ExcelContstant.DL_LAYER.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.TABLE_NAME.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.TABLE_COMMENT.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.COLUMN_NAME_.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.COLUMN_COMMENT.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.KEY.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.DATA_TYPE.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.PRECISION.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.SCALE.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.IS_NULL.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.CREATED_BY.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.DEPARTMENT.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.ELEMENT.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.RETURN_NON_RETURN.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.SOURCE.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.COMMENTS.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.ETL_TABLE_LEVEL_REMARKS.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.ETL_COLUMN_LEVEL_REMARKS.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.DATA_COLLECTION.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.PORTAL.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.SAP_BO.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.PRIORITY.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.METADATA_FILENAME.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.RELEASE_VERSION.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.CHANGE_DATE.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.SCHEMA.getExcelColumnConstants());
		h++;

		int p = 0;
		int sizeOfMap = 0;
		if (!elementData.isEmpty()) {

			for (Map.Entry<String, List<ElementDimensionBean>> entry : elementData.entrySet()) {
				sizeOfMap++;

				headerDetails = rowHeaderMapData.get(entry.getKey());
				String elementNameAndVerCombo = entry.getKey();
				List<ElementDimensionBean> elementDimensionBeans = elementData.get(entry.getKey());

				String[] elementNameAndVer = elementNameAndVerCombo.split("#@", 2);
				String elementName = elementNameAndVer[0];
				boolean insideFound = false;

				for (String str : headerDetails) {
					boolean found = false;
					p++;
					HSSFRow row = sheet.createRow((short) p);
					int k = 0;
					row.createCell(k).setCellValue(ExcelContstant.EBR_LANDING.getExcelColumnConstants());
					k++;
					row.createCell(k).setCellValue(ExcelContstant.MAP_EBR_RBR_.getExcelColumnConstants() + elementName + ExcelContstant._BUSINESS_METADATA.getExcelColumnConstants());
					k++;
					row.createCell(k).setCellValue("Business Metadata RBR and EBR Mapping table for " + elementName + " element ");
					k++;
					row.createCell(k).setCellValue(str.toUpperCase());
					k++;

					if (str.equalsIgnoreCase(ExcelContstant.ELEMENT_CODE.getExcelColumnConstants())) {
						found = true;
						row.createCell(k).setCellValue("Short code given to Element(eg:LNA, OIBA)");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.UK.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.VARCHAR_45.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("N");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.ELEMENT_VERSION.getExcelColumnConstants())) {
						found = true;
						row.createCell(k).setCellValue("Version of element (eg:1.0,2.0)");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.UK.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.VARCHAR_5.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("N");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.ELEMENT_LABEL.getExcelColumnConstants())) {
						found = true;
						row.createCell(k).setCellValue("Element Name provided by source system (eg: Loans and Advances, Other Interest Bearing Assets)");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.TEXT.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("N");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.ELEMENT_DIMENSIONS.getExcelColumnConstants())) {
						found = true;
						row.createCell(k).setCellValue("Element JSON combo of all dimension");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.JSON.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("N");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.ELEMENT_SDMX_MODEL_DIM.getExcelColumnConstants())) {
						found = true;
						row.createCell(k).setCellValue("Element JSON combo of all code list value");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.JSON.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("N");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.EBR_TABLE_NAME.getExcelColumnConstants())) {
						found = true;
						row.createCell(k).setCellValue("EBR Table Name");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.TEXT.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("N");
						k++;
					} else if (!found && !insideFound) {
						String[] eleDimName = str.split("_", 2);
						String dimName = eleDimName[1];

						if (!str.equalsIgnoreCase(ExcelContstant.ELEMENT_DMID.getExcelColumnConstants())) {
							row.createCell(k).setCellValue("Short code of the codelist value of the " + dimName + " dimension");
							k++;
							row.createCell(k).setCellValue(ExcelContstant.UK.getExcelColumnConstants());
							k++;
							row.createCell(k).setCellValue(ExcelContstant.TEXT.getExcelColumnConstants());
							k++;
							row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
							k++;
							row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
							k++;
							row.createCell(k).setCellValue("Y");
							k++;
						}
					}

					// break else if for facing issues in for dynamic data generation

					if (str.equalsIgnoreCase(ExcelContstant.ELEMENT_DMID.getExcelColumnConstants())) {
						insideFound = true;
						row.createCell(k).setCellValue("Unique alphanumeric identifier for each combination of element and dimension (code list values)");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.UK.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.TEXT.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("N");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.RETURN_TOTAL_DISB_BAL_OUTSTD_OFWHICH_FLG.getExcelColumnConstants())) {
						insideFound = true;
						row.createCell(k).setCellValue("Identifier for SOme Returns");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.UK.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.VARCHAR_5.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("Y");
						k++;
					} else if (str.equalsIgnoreCase("ELEMENT_RETURN_TEMPLATE_SHEET_NO")) {
						insideFound = true;
						row.createCell(k).setCellValue("Unique alphanumeric identifier for EBR Return Template Sheet No");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.UK.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.VARCHAR_1000.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("N");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.RETURN_CODE.getExcelColumnConstants())) {
						insideFound = true;
						row.createCell(k).setCellValue("Unique alphanumeric identifier for Return Code");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.UK.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.VARCHAR_1000.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("N");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.RETURN_NAME.getExcelColumnConstants())) {
						insideFound = true;
						row.createCell(k).setCellValue("Return Name provided by business ");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.UK.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.TEXT.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("N");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.RETURN_TEMPLATE_SHEET_NO.getExcelColumnConstants())) {
						insideFound = true;
						row.createCell(k).setCellValue("Unique alphanumeric identifier for Return Template Sheet No");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.UK.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.VARCHAR_1000.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("N");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.RETURN_TEMPLATE_SHEET_NAME.getExcelColumnConstants())) {
						insideFound = true;
						row.createCell(k).setCellValue("Return Template sheet Name provided in return format");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.UK.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.TEXT.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("N");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.RETURN_SECTION_CODE.getExcelColumnConstants())) {
						insideFound = true;
						row.createCell(k).setCellValue("Return Section Code  provided in return format");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.UK.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.VARCHAR_1000.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("N");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.RETURN_SECTION_NAME.getExcelColumnConstants())) {
						insideFound = true;
						row.createCell(k).setCellValue("Return Section Name  provided in return format");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.UK.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.TEXT.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("N");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.RETURN_CELL_REF_NO.getExcelColumnConstants())) {
						insideFound = true;
						row.createCell(k).setCellValue("Return Cell Ref No  provided in return format");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.UK.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.INT.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("N");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.RETURN_ITEM_CODE.getExcelColumnConstants())) {
						insideFound = true;
						row.createCell(k).setCellValue("Unique alphanumeric identifier for Return Item Code");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.UK.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.VARCHAR_1000.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("Y");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.RETURN_COLUMN_NAME.getExcelColumnConstants())) {
						insideFound = true;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.UK.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.TEXT.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("N");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.RETURN_TABLE_NAME.getExcelColumnConstants())) {
						insideFound = true;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.UK.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.TEXT.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("N");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.RETURN_ITEM_OLD_SK.getExcelColumnConstants())) {
						insideFound = true;
						row.createCell(k).setCellValue("Old Surrogate Key  identifier for Return Item Code exists in existing EDW");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.INT.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("Y");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.RETURN_ITEM_DISPLAY_TEXT.getExcelColumnConstants())) {
						insideFound = true;
						row.createCell(k).setCellValue("Item Display Text ( Item Name)");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.TEXT.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("Y");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.RETURN_ITEM_ACTIVE_FLAG.getExcelColumnConstants())) {
						insideFound = true;
						row.createCell(k).setCellValue("Flag for Active / Inactive");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.VARCHAR_1.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("Y");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.RETURN_ITEM_START_EFFECTIVE_DATE.getExcelColumnConstants())) {
						insideFound = true;
						row.createCell(k).setCellValue("Effective Start Date");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.DATE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("Y");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.RETURN_ITEM_END_EFFECTIVE_DATE.getExcelColumnConstants())) {
						insideFound = true;
						row.createCell(k).setCellValue("Effective End Date");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.DATE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("Y");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.ENCODE_VALUE.getExcelColumnConstants())) {
						insideFound = true;
						row.createCell(k).setCellValue("Encode Value");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.TEXT.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("N");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.DECODE_VALUE.getExcelColumnConstants())) {
						insideFound = true;
						row.createCell(k).setCellValue("Decoded Value");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.TEXT.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("N");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.IS_EBR_RBR_APPLICABLE.getExcelColumnConstants())) {
						insideFound = true;
						row.createCell(k).setCellValue("Ebr Rbr Applicable Check");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.VARCHAR_1.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("N");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.RETURN_ITEM_FREE_TEXT.getExcelColumnConstants())) {
						insideFound = true;
						row.createCell(k).setCellValue("Item Free Text");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.VARCHAR_1000.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("Y");
						k++;
					} else if (str.equalsIgnoreCase(ExcelContstant.RETURN_FILER_TO_SELECT.getExcelColumnConstants())) {
						insideFound = true;
						row.createCell(k).setCellValue("Filer To Select");
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.VARCHAR.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						k++;
						row.createCell(k).setCellValue("Y");
						k++;
					}

					row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
					k++;
					row.createCell(k).setCellValue(ExcelContstant.ALL.getExcelColumnConstants());
					k++;
					row.createCell(k).setCellValue(elementDimensionBeans.get(0).getElementLabel());
					k++;
					row.createCell(k).setCellValue(elementDimensionBeans.get(0).getReturnName());

					k++;
					row.createCell(k).setCellValue(ExcelContstant.EBR_RBR_BUSINESS_METADATA.getExcelColumnConstants());
					k++;
					row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
					k++;
					row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
					k++;
					row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
					k++;
					row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
					k++;
					row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
					k++;
					row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
					k++;
					row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
					k++;
					row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
					k++;
					row.createCell(k).setCellValue(elementDimensionBeans.get(0).getElementVersion());
					k++;
					row.createCell(k).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
					k++;
					row.createCell(k).setCellValue(ExcelContstant.SRC_ELEMENT.getExcelColumnConstants());
					k++;
				}
			}

		}
		LOGGER.info("File Name For Col tech Metadata  " + filename);
		FileOutputStream fileOut = new FileOutputStream(filename);
		workbook.write(fileOut);
		fileOut.close();
		//workbook.close();
		LOGGER.info("In Business Metadata Element Wise Sheet End: ");

	}

	/*
	 * This Code Create EBR To RBR Table Mapping EXCEL
	 */
	public void createEbrRbrTableMap(Map<String, List<EbrRbrTableDto>> ebrRbrMap, String sheetName, HSSFWorkbook workbook) throws Exception {

		LOGGER.info("Business Metadata Sheet Tech Metadata Sheet Start");
		HSSFSheet sheet = workbook.createSheet(sheetName);
		HSSFRow rowhead = sheet.createRow((short) 0);

		int h = 0;
		rowhead.createCell(h).setCellValue(ExcelContstant.ELEMENT_CODE.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.ELEMENT_VERSION.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.EBR_TABLE_NAME.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.BUSINESS_METADATA_TABLE_NAME.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.RETURN_CODE.getExcelColumnConstants());
		h++;
		rowhead.createCell(h).setCellValue(ExcelContstant.RBR_TABLE_NAME.getExcelColumnConstants());
		h++;

		int p = 0;
		String returnCodeTemp = "";

		if (!ebrRbrMap.isEmpty()) {
			for (Map.Entry<String, List<EbrRbrTableDto>> entry : ebrRbrMap.entrySet()) {
				List<EbrRbrTableDto> elemDtos = ebrRbrMap.get(entry.getKey());
				for (EbrRbrTableDto ebrdto : elemDtos) {
					int k = 0;
					p++;
					returnCodeTemp = ebrdto.getReturnCode();
					HSSFRow row = sheet.createRow((short) p);
					row.createCell(k).setCellValue(ebrdto.getElementCode());
					k++;
					row.createCell(k).setCellValue(ebrdto.getElementVer());
					k++;
					row.createCell(k).setCellValue(ebrdto.getEbrTableName());
					k++;
					row.createCell(k).setCellValue(ExcelContstant.MAP_EBR_RBR_.getExcelColumnConstants() + ebrdto.getElementCode() + ExcelContstant._BUSINESS_METADATA.getExcelColumnConstants());
					k++;
					row.createCell(k).setCellValue(ebrdto.getReturnCode());
					k++;
					row.createCell(k).setCellValue(ebrdto.getRbrTableName());
					k++;
				}
			}
		}

		String returnTableName = loadReturnVersionSpecificTableName(returnCodeTemp);

		int k = 0;
		p++;

		HSSFRow row = sheet.createRow((short) p);
		row.createCell(k).setCellValue("ALL");
		k++;
		row.createCell(k).setCellValue("N_A");
		k++;
		row.createCell(k).setCellValue("N_A");
		k++;
		row.createCell(k).setCellValue("N_A");
		k++;
		row.createCell(k).setCellValue(returnCodeTemp);
		k++;
		row.createCell(k).setCellValue(returnTableName);
		k++;

		LOGGER.info("File Name For EBR RBR  " + filename);

		FileOutputStream fileOut = new FileOutputStream(filename);
		workbook.write(fileOut);
		fileOut.close();
		//workbook.close();
		LOGGER.info("Business Metadata Sheet Tech Metadata Sheet End");

		busMetadatProcess.setIsActive(true);
		busMetadatProcess.setInsertStatus(false);

		BusMetadatProcess deactivateBusExistingEntity = metadataRepo.getActiveRecordForReturnAndEbrVersion(busMetadatProcess.getReturnCode(), busMetadatProcess.getReturnVersion(), busMetadatProcess.getEbrVersion());
		if (deactivateBusExistingEntity != null) {
			deactivateBusExistingEntity.setIsActive(false);
			metadataRepo.save(deactivateBusExistingEntity);
		}

		metadataRepo.save(busMetadatProcess);

	}

	public String loadReturnVersionSpecificTableName(String returnCodeName) throws Exception {
		String returnTableName = "";
		String likeTableName = "RETURN_VERSION";

		String sql = "select DW_HIVE_TBL_NAME from TBL_DW_HIVE_TBL_DESIGN where DW_HIVE_TBL_RETURN_CODE like '%" + returnCodeName + "%' and DW_HIVE_TBL_NAME like '%" + likeTableName + "%'";

		List<Tuple> tuples = entityManager.createNativeQuery(sql.toString(), Tuple.class).getResultList();
		for (Tuple tuple : tuples) {
			if ((String) tuple.get("DW_HIVE_TBL_NAME") != null) {
				returnTableName = (String) tuple.get("DW_HIVE_TBL_NAME");
				return returnTableName;
			}
		}

		return returnTableName;
	}

	@Transactional(rollbackFor = Exception.class)
	public List<BusMetadatProcessBean> validateBusinessMetaDataSheet(String fileName, String returnCode, String ebrVersion, Long metadataProcessId, String returnVersion) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		sheet2 = workbook.createSheet("Error");
		try (FileInputStream fis = new FileInputStream(new File(ResourceUtil.getKeyValue("filepath.root") + ResourceUtil.getKeyValue("returnTemplate.upload.path") + File.separator + returnCode + File.separator + ResourceUtil.getKeyValue("filePath.SDMXFolder") + File.separator + ebrVersion + ResourceUtil.getKeyValue("file.ebr.buss.metadata") + File.separator + fileName)); HSSFWorkbook wb = new HSSFWorkbook(fis);) {
			Map<String, List<String>> issueMap = new TreeMap<>();
			int returnCellReferenceIndex = 0;
			int returnTableNameIndex = 0;
			int returnItemCodeIndex = 0;
			int returnColumnNameIndex = 0;
			int returnItemDisplayTextIndex = 0;
			int returnItemFreeTextIndex = -1;

			int rowNo = 0;

			boolean itemFreeTextFound = false;

			boolean errorFound = false;

			Set<String> columnNames = new HashSet<>();

			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				HSSFSheet sheet = wb.getSheetAt(i);
				if (sheet.getSheetName().toUpperCase().contains("MAP_EBR_RBR_LND_COL_TECH") || sheet.getSheetName().toUpperCase().contains("MAP_EBR_RBR_LND_COL_TECH") || sheet.getSheetName().toUpperCase().contains("MAP_EBR_RBR_LND_TBL_TECH") || sheet.getSheetName().toUpperCase().contains("SHEET")) {
					continue;
				}
				returnItemFreeTextIndex = -1;

				issueMap.put(sheet.getSheetName(), new ArrayList<>());

				FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
				for (Row row : sheet) // iteration over row using for each loop
				{
					try {
						rowNo = row.getRowNum();
						itemFreeTextFound = false;
						for (int j = 0; j < row.getLastCellNum(); j++) {
							Cell cell = row.getCell(j);

							if (cell != null) {
								if (rowNo == 0) {

									switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
									case Cell.CELL_TYPE_STRING: // field that represents string cell type
										if (cell.getStringCellValue().equalsIgnoreCase("RETURN_ITEM_DISPLAY_TEXT")) {
											returnItemDisplayTextIndex = cell.getColumnIndex();
										} else if (cell.getStringCellValue().equalsIgnoreCase("RETURN_CELL_REF_NO")) {
											returnCellReferenceIndex = cell.getColumnIndex();
										} else if (cell.getStringCellValue().equalsIgnoreCase("RETURN_TABLE_NAME")) {
											returnTableNameIndex = cell.getColumnIndex();
										} else if (cell.getStringCellValue().equalsIgnoreCase("RETURN_ITEM_CODE")) {
											returnItemCodeIndex = cell.getColumnIndex();
										} else if (cell.getStringCellValue().equalsIgnoreCase("RETURN_COLUMN_NAME")) {
											returnColumnNameIndex = cell.getColumnIndex();
										} else if (cell.getStringCellValue().equalsIgnoreCase("RETURN_ITEM_FREE_TEXT")) {
											returnItemFreeTextIndex = cell.getColumnIndex();
										}
										break;
									}
								} else {
									if (cell.getColumnIndex() == returnItemCodeIndex) {
										switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
										case Cell.CELL_TYPE_STRING:
											if (cell.getStringCellValue() == null || cell.getStringCellValue().equals("") || cell.getStringCellValue().equals(" ")) {
												//issueMap.get(sheet.getSheetName()).add("Item Code blank for row : " + rowNo + "  Having return cell reference :  " + row.getCell(returnCellReferenceIndex).getStringCellValue());
											}
											break;
										}
									} else if (cell.getColumnIndex() == returnColumnNameIndex) {
										switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
										case Cell.CELL_TYPE_STRING:
											if (cell.getStringCellValue() == null || cell.getStringCellValue().equals("") || cell.getStringCellValue().equals(" ")) {
												if (!errorFound) {
													errorFound = true;
												}

												issueMap.get(sheet.getSheetName()).add("ReturnColumn name blank for row : " + rowNo + " ,sdmx cell reference no : " + row.getCell(returnCellReferenceIndex).getStringCellValue());
											} else {
												if (!cell.getStringCellValue().toUpperCase().equals(cell.getStringCellValue())) {
													issueMap.get(sheet.getSheetName()).add("ReturnColumn name Invalid for row : " + rowNo + " ,sdmx cell reference no : " + row.getCell(returnCellReferenceIndex).getStringCellValue());

													if (!errorFound) {
														errorFound = true;
													}
												} else {
													columnNames.add(cell.getStringCellValue());
												}
											}
											break;
										}
									} else if (cell.getColumnIndex() == returnTableNameIndex) {
										switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
										case Cell.CELL_TYPE_STRING:
											if (cell.getStringCellValue() == null || cell.getStringCellValue().equals("") || cell.getStringCellValue().equals(" ")) {
												if (!errorFound) {
													errorFound = true;
												}

												issueMap.get(sheet.getSheetName()).add("Return Table  name blank for row : " + rowNo + ", cell reference no is : " + row.getCell(returnCellReferenceIndex).getStringCellValue());
											}
											break;
										}
									} else if (cell.getColumnIndex() == returnItemDisplayTextIndex) {
										switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
										case Cell.CELL_TYPE_STRING:
											if (cell.getStringCellValue() == null || cell.getStringCellValue().equals("") || cell.getStringCellValue().equals(" ")) {
												if (row.getCell(returnItemCodeIndex) != null && row.getCell(returnItemCodeIndex).getStringCellValue() != null && !row.getCell(returnItemCodeIndex).getStringCellValue().equals("") && !row.getCell(returnItemCodeIndex).getStringCellValue().equals(" ")) {
													//issueMap.get(sheet.getSheetName()).add("Item Code Present but Display Text null for row : "
													//	+ rowNo + " having Item Code : "
													//+ row.getCell(returnItemCodeIndex).getStringCellValue()
													//+ " and cell reference no is : "
													//+ row.getCell(returnCellReferenceIndex).getStringCellValue());
												}
											}
											break;
										}
									} else if (cell.getColumnIndex() == returnItemFreeTextIndex) {
										switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
										case Cell.CELL_TYPE_STRING:
											if (cell.getStringCellValue() != null && !cell.getStringCellValue().equals("") && !cell.getStringCellValue().equals(" ")) {
												if (!itemFreeTextFound) {
													if (!cell.getStringCellValue().equals("N_A")) {
														issueMap.get(sheet.getSheetName()).add("N_A not found for cell reference no is : " + row.getCell(returnCellReferenceIndex).getStringCellValue());
													}
												}
											}
											break;
										}
									} else {
										switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
										case Cell.CELL_TYPE_STRING:
											if (cell.getStringCellValue().equals("ITEM_FREE_TEXT")) {
												itemFreeTextFound = true;
												if (returnItemFreeTextIndex < 0 || row.getCell(returnItemFreeTextIndex) == null || !row.getCell(returnItemFreeTextIndex).getStringCellValue().equals("ITEM_FREE_TEXT")) {
													//issueMap.get(sheet.getSheetName()).add(
													//	"Return Item free text not found for cell reference no is : "
													//		+ row.getCell(returnCellReferenceIndex)
													//			.getStringCellValue());
												}
											}
											break;
										}
									}
								}
							} else {
								// cell is null
								if (j == returnItemCodeIndex) {
									issueMap.get(sheet.getSheetName()).add("Item Code blank for row : " + rowNo + "  Having return cell reference :  " + row.getCell(returnCellReferenceIndex).getStringCellValue());
								} else if (j == returnColumnNameIndex) {
									if (!errorFound) {
										errorFound = true;
									}
									issueMap.get(sheet.getSheetName()).add("ReturnColumn name blank for row : " + rowNo + " ,sdmx cell reference no : " + row.getCell(returnCellReferenceIndex).getStringCellValue());
								} else if (j == returnTableNameIndex) {
									if (!errorFound) {
										errorFound = true;
									}
									issueMap.get(sheet.getSheetName()).add("Return Table  name blank for row : " + rowNo + ", cell reference no is : " + row.getCell(returnCellReferenceIndex).getStringCellValue());
								} else if (j == returnItemDisplayTextIndex) {
									issueMap.get(sheet.getSheetName()).add("Item Code Present but Display Text null for row : " + rowNo + " having Item Code : " + row.getCell(returnItemCodeIndex).getStringCellValue() + " and cell reference no is : " + row.getCell(returnCellReferenceIndex).getStringCellValue());
								} else if (j == returnItemFreeTextIndex && itemFreeTextFound) {
									issueMap.get(sheet.getSheetName()).add("Return Item free text not found for cell reference no is : " + row.getCell(returnCellReferenceIndex).getStringCellValue());
								}
							}
						}
					} catch (Exception e) {
						LOGGER.error("Exception :", e);
					}
				}
			}
			File inputFile = new File(ResourceUtil.getKeyValue("filepath.root") + ResourceUtil.getKeyValue("returnTemplate.upload.path") + File.separator + returnCode + File.separator + ResourceUtil.getKeyValue("filePath.SDMXFolder") + File.separator + ebrVersion + ResourceUtil.getKeyValue("file.ebr.buss.metadata") + File.separator + fileName);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String dateString = simpleDateFormat.format(new Date()).replace(" ", "_");
			dateString = dateString.replace(":", "_");
			//new File(fileName).getParent() + File.separator + returnCode + "_" + dateString + "_Error_List.xls";
			excelRowOutputCount = 0;
			excelCreateCellOutoutCount = 0;
			//String errorFileName= new File( 
			String errorFileName = ResourceUtil.getKeyValue("filepath.root") + ResourceUtil.getKeyValue("returnTemplate.upload.path") + File.separator + returnCode + File.separator + ResourceUtil.getKeyValue("filePath.SDMXFolder") + File.separator + ebrVersion + ResourceUtil.getKeyValue("file.ebr.buss.metadata") + File.separator + "RBR_EBR_MAP_BUS_METADATA_RESULT" + "_" + dateString + ".xls";
			//LOGGER.info("File created : " + fileName.createNewFile());

			List<String> errorList = new ArrayList<>();
			errorList.clear();

			for (Map.Entry<String, List<String>> issueMapList : issueMap.entrySet()) {
				outputRow = sheet2.createRow((short) excelRowOutputCount);
				outputRow.createCell(excelCreateCellOutoutCount).setCellValue("Sheet Name : " + issueMapList.getKey());
				excelRowOutputCount++;

				//errorList.add("Sheet Name : " + issueMapList.getKey());
				List<String> listData = issueMap.get(issueMapList.getKey());
				for (String data : listData) {
					outputRow = sheet2.createRow((short) excelRowOutputCount);
					outputRow.createCell(excelCreateCellOutoutCount).setCellValue(data);
					excelRowOutputCount++;
				}

				//errorList.addAll(issueMapList.getValue());

				outputRow = sheet2.createRow((short) excelRowOutputCount);
				outputRow.createCell(excelCreateCellOutoutCount).setCellValue("----------------------------------------------------------------------------------------");
				excelRowOutputCount++;
				//errorList.add("----------------------------------------------------------------------------------------");
			}

			outputRow = sheet2.createRow((short) excelRowOutputCount);
			outputRow.createCell(excelCreateCellOutoutCount).setCellValue("Total Unique column Names :");
			excelRowOutputCount++;
			//errorList.add("Total Unique column Names :");
			for (String columnName : columnNames) {
				outputRow = sheet2.createRow((short) excelRowOutputCount);
				outputRow.createCell(excelCreateCellOutoutCount).setCellValue(columnName);
				excelRowOutputCount++;
				//errorList.add(columnName);
			}

			columnNames.add("REPORT_AS_ON_DATE");
			columnNames.add("BANK_WORKING_CODE");
			columnNames.add("BANK_PEERGROUP_CODE");
			columnNames.add("TEMPLATE_SHEET_NO");
			columnNames.add("UPLOAD_ID");
			columnNames.add("AUDIT_FLAG");
			columnNames.add("RETURN_CODE");
			columnNames.add("RETURN_REPORTING_FREQUENCY");
			columnNames.add("RETURN_TAXONOMY_VERSION");
			columnNames.add("UNIT_DESCRIPTION");
			columnNames.add("CURRENCY_CODE");
			columnNames.add("PERIOD_DETAIL_START_DATE");
			columnNames.add("PERIOD_DETAIL_END_DATE");
			columnNames.add("UPLOAD_DATE");
			columnNames.add("REPORTING_INSTITUTION");
			columnNames.add("ADDRESS_REPORTING_INSTITUTION");
			columnNames.add("BANK_CATEGORY");
			columnNames.add("RETURN_NAME");
			columnNames.add("FOR_PERIOD_ENDED");
			//columnNames.add("ITEM_FREE_TEXT");
			columnNames.add("ITEM_FREE_TEXT_SORT_ORDER");
			columnNames.add("ITEM_CODE");
			columnNames.add("FREQ");

			FileOutputStream fileOut = new FileOutputStream(errorFileName);
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();

			BusMetadatProcess busMetadatProcess = metadataRepo.getOne(metadataProcessId);
			if (busMetadatProcess != null) {
				busMetadatProcess.setBussValidateFileName("RBR_EBR_MAP_BUS_METADATA_RESULT" + "_" + dateString + ".xls");
				metadataRepo.save(busMetadatProcess);
			}

			List<BusMetadatProcessBean> busMetadatProcessBean = getActiveBusinessMetadata(returnCode, returnVersion, ebrVersion, errorFound);

			return busMetadatProcessBean;
		} catch (Exception e) {
			LOGGER.error("Exception : ", e);
		}
		return null;
	}

	@Transactional(rollbackFor = Exception.class)
	public List<BusMetadatProcessBean> insertBusinessMetadata(String fileName, String returnCode, String ebrVersion, String returnVersion, Long metadataProcessId) throws Exception {

		StringBuilder insertQueryBuilder = null;
		BusMetadatProcess busMetadatProcess = metadataRepo.getOne(metadataProcessId);
		Date insertStart = new Date();
		busMetadatProcess.setBussMetadataInsertStart(insertStart);
		metadataRepo.save(busMetadatProcess);

		String filePath = ResourceUtil.getKeyValue("filepath.root") + ResourceUtil.getKeyValue("returnTemplate.upload.path") + File.separator + returnCode + File.separator + ResourceUtil.getKeyValue("filePath.SDMXFolder") + File.separator + ebrVersion + ResourceUtil.getKeyValue("file.ebr.buss.metadata") + File.separator + fileName;
		StringBuilder columnStringBuilder = null;
		StringBuilder questionMarkStringBuilder = null;
		PreparedStatement pstmt = null;
		List<String> headerColumns = null;
		Connection connection = datasource.getConnection();
		int index = 1;
		File file = new File(filePath);

		try (FileInputStream fis = new FileInputStream(file); HSSFWorkbook wb = new HSSFWorkbook(fis);) {
			if (createTables(wb, connection, returnCode)) {
				Sheet sheet = null;

				for (int sheetNo = 0; sheetNo < wb.getNumberOfSheets(); sheetNo++) {
					sheet = wb.getSheetAt(sheetNo);

					//System.out.println(""+sheetNo+"   #########" + sheet.getSheetName() + "#########");

					if (sheet.getSheetName().equalsIgnoreCase("MAP_EBR_RBR_LND_COL_TECH") || sheet.getSheetName().toUpperCase().contains("SHEET")) {
						continue;
					}

					headerColumns = new ArrayList<>();
					insertQueryBuilder = new StringBuilder();
					columnStringBuilder = new StringBuilder();
					questionMarkStringBuilder = new StringBuilder();

					for (int rowNo = 0; rowNo <= sheet.getLastRowNum(); rowNo++) {
						Row row = sheet.getRow(rowNo);
						if (rowNo == 0) {
							for (int columnNo = row.getFirstCellNum(); columnNo < row.getLastCellNum(); columnNo++) {
								Cell cell = row.getCell(columnNo);
								headerColumns.add(cell.getStringCellValue());
							}
						} else {
							if (rowNo == 1) {
								String elementCode = sheet.getRow(rowNo).getCell(1).getStringCellValue();
								if (sheet.getSheetName().equalsIgnoreCase("MAP_EBR_RBR_LND_TBL_TECH")) {
									insertQueryBuilder.append("insert into MAP_EBR_RBR_LND_TBL_TECH_METADATA (");
								} else {
									insertQueryBuilder.append("insert into MAP_EBR_RBR_" + elementCode + "_BUSINESS_METADATA (");
								}

								for (int i = 0; i < headerColumns.size(); i++) {
									if (i == 0) {
										columnStringBuilder.append(headerColumns.get(i));
										questionMarkStringBuilder.append("?");
									} else {
										columnStringBuilder.append(", " + headerColumns.get(i));
										questionMarkStringBuilder.append(", ?");
									}
								}
								insertQueryBuilder.append(columnStringBuilder + " ) values ( " + questionMarkStringBuilder + " )");

								pstmt = connection.prepareStatement(insertQueryBuilder.toString());
							}

							if (pstmt != null) {
								for (int k = row.getFirstCellNum(); k < row.getLastCellNum(); k++) {
									Cell cell = row.getCell(k);
									index = k + 1;
									if (cell != null) {
										if (cell.getStringCellValue().trim().equals("")) {
											pstmt.setString(index, null);
										} else {
											pstmt.setString(index, cell.getStringCellValue().trim());
										}
									} else {
										pstmt.setString(index, null);
									}
								}
								pstmt.addBatch();
							}
						}
					}

					pstmt.executeBatch();
				}
				connection.commit();
			}
		} catch (Exception e) {
			LOGGER.error("Exception : ", e);
			//e.printStackTrace();
			throw e;
		} finally {
			connection.close();
		}

		if (busMetadatProcess != null) {
			Date insertEnd = new Date();
			busMetadatProcess.setBussMetadataInsertEnd(insertEnd);
			busMetadatProcess.setInsertStatus(true);
			metadataRepo.save(busMetadatProcess);
		}

		List<BusMetadatProcessBean> busMetadatProcessBean = getActiveBusinessMetadata(returnCode, returnVersion, ebrVersion, false);
		connection.close();
		return busMetadatProcessBean;

	}

	@SuppressWarnings("resource")
	private boolean createTables(HSSFWorkbook wb, Connection connection, String returnCode) throws Exception {
		Sheet sheet = wb.getSheet("MAP_EBR_RBR_LND_COL_TECH");

		String tableName = null;
		String columnName = null;
		String dataType = null;
		String isNullable = null;

		Map<String, List<String>> tableMap = new HashMap<>();

		for (int j = 0; j <= sheet.getLastRowNum(); j++) {
			if (j > 0) {
				Row row = sheet.getRow(j);

				try {
					if (row != null && row.getCell(1) != null && row.getCell(3) != null && row.getCell(6) != null) {
						tableName = row.getCell(1).getStringCellValue();
						columnName = row.getCell(3).getStringCellValue();
						dataType = row.getCell(6).getStringCellValue();
						isNullable = row.getCell(9).getStringCellValue();

						if (isNullable == null || isNullable.equals("Y")) {
							isNullable = "NULL";
						} else {
							isNullable = "NOT NULL";
						}

						if (tableMap.containsKey(tableName)) {
							List<String> columnList = tableMap.get(tableName);

							columnList.add(columnName + "~" + dataType + "~" + isNullable);

							tableMap.put(tableName, columnList);
						} else {
							List<String> columnList = new ArrayList<>();

							columnList.add(columnName + "~" + dataType + "~" + isNullable);

							tableMap.put(tableName, columnList);
						}
					}
				} catch (Exception e) {
					LOGGER.error("Exception :", e);
				}
			}
		}

		StringBuilder createTableQuery = null;
		int i = 0;

		boolean tableExist = false;
		//		ResultSet resultSet = null;
		try (Statement statement = connection.createStatement();) {
			connection.setAutoCommit(false);
			for (String key : tableMap.keySet()) {
				if (key.equalsIgnoreCase("MAP_EBR_RBR_TECHNICAL_METADATA")) {
					continue;
				}
				createTableQuery = new StringBuilder("");
				try (ResultSet resultSet = statement.executeQuery("show tables like '%" + key + "%'");) {
					tableExist = false;
					i = 0;

					while (resultSet.next()) {
						tableExist = true;
						break;
					}

					if (!tableExist) {
						createTableQuery.append("create table " + key + " (");

						for (String column : tableMap.get(key)) {
							if (i == 0) {
								createTableQuery.append(column.split("~")[0] + " " + column.split("~")[1] + " " + column.split("~")[2]);
							} else {
								createTableQuery.append(",  " + column.split("~")[0] + " " + column.split("~")[1] + " " + column.split("~")[2]);
							}
							i++;
						}
						createTableQuery.append(")");
						//System.out.println(createTableQuery.toString());		
						statement.executeUpdate(createTableQuery.toString());
					} else {
						statement.executeUpdate("delete from " + key + " where RETURN_CODE = '" + returnCode + "'");

						String descQuery = "desc " + key + "";

						ResultSet descQueryResultSet = statement.executeQuery(descQuery);

						List<String> existingColumnList = new ArrayList<>();
						Map<String, String> existingColumnMap = new HashMap<>();

						while (descQueryResultSet.next()) {
							existingColumnList.add(descQueryResultSet.getString("Field"));
							existingColumnMap.put(descQueryResultSet.getString("Field"), descQueryResultSet.getString("Type") + "~" + descQueryResultSet.getString("Null"));
						}

						StringBuilder alterQuery = new StringBuilder();
						int j = 0;
						String columnDataType = "";
						String nullableString = "";
						for (String column : tableMap.get(key)) {
							if (!existingColumnList.contains(column.split("~")[0])) {
								if (j == 0) {
									alterQuery.append("ADD COLUMN " + column.split("~")[0] + " " + column.split("~")[1] + " " + column.split("~")[2] + "");
								} else {
									alterQuery.append(", ADD COLUMN " + column.split("~")[0] + " " + column.split("~")[1] + " " + column.split("~")[2] + "");
								}
								j++;
							} else {
								columnDataType = existingColumnMap.get(column.split("~")[0]).split("~")[0];
								if (existingColumnMap.get(column.split("~")[0]).split("~")[1].equals("NO")) {
									nullableString = "NOT NULL";
								} else {
									nullableString = "NULL";
								}

								if (!columnDataType.equalsIgnoreCase(column.split("~")[1]) || !nullableString.equalsIgnoreCase(column.split("~")[2])) {
									if (j == 0) {
										alterQuery.append("CHANGE COLUMN " + column.split("~")[0] + " " + column.split("~")[0] + " " + column.split("~")[1] + " " + column.split("~")[2] + "");
									} else {
										alterQuery.append(", CHANGE COLUMN " + column.split("~")[0] + " " + column.split("~")[0] + " " + column.split("~")[1] + " " + column.split("~")[2] + "");
									}
									j++;
								}
							}
						}

						if (j > 0) {
							//System.out.println("Alter table "+key+" " + alterQuery.toString());
							statement.executeUpdate("Alter table " + key + " " + alterQuery.toString());
						}
					}
				} catch (Exception e) {
					LOGGER.error("Exception : ", e);
					throw e;
				}
			}

			// EBR_RBR_MAPPING_TABLE_STRUCTURE
			tableExist = false;
			createTableQuery = new StringBuilder("");
			try (ResultSet resSet = statement.executeQuery("show tables like '%MAP_EBR_RBR_LND_TBL_TECH_METADATA%'");) {
				while (resSet.next()) {
					tableExist = true;
					break;
				}

				if (!tableExist) {
					createTableQuery.append("create table MAP_EBR_RBR_LND_TBL_TECH_METADATA (ELEMENT_CODE varchar(45), ELEMENT_VERSION varchar(45), EBR_TABLE_NAME varchar(250), BUSINESS_METADATA_TABLE_NAME varchar(250), RETURN_CODE varchar(45), RBR_TABLE_NAME varchar(250))");
					//	System.out.println(createTableQuery.toString());		
					statement.executeUpdate(createTableQuery.toString());
				} else {
					statement.executeUpdate("delete from MAP_EBR_RBR_LND_TBL_TECH_METADATA where RETURN_CODE = '" + returnCode + "'");
				}
			}

			connection.commit();
		} catch (Exception e) {
			LOGGER.error("Exception", e);
			throw e;
		}

		return true;
	}

	@Transactional(rollbackFor = Exception.class)
	public List<TechMetadatProcessBean> generateAnddownloadTechnicalMetadata(UserMaster userMaster, String returnCode, String busFileName, String ebrVersion) throws Exception {

		LOGGER.info("Download Technical Metadata Export Start : ");
		Connection connection = datasource.getConnection();

		LOGGER.info("Coonection Created: " + connection);
		LOGGER.info("Read Data to avoid Mannual Mapping Start: " + connection);

		LOGGER.info("Read Data to avoid Mannual Mapping End: " + connection);

		Set<String> currencyExtraRecord = null;

		if (StringUtils.isNoneBlank(returnCode) && StringUtils.isNoneBlank(busFileName) && StringUtils.isNoneBlank(ebrVersion)) {
			techMetadataRbrColumnMap = new TreeMap<String, TechMetadataDto>();
			returnWiseExtraNaList = new ArrayList<TechMetadataDto>();
			getRbrColumnMap();
			currencyExtraRecord = new HashSet<String>();
			currencyExtraRecord = getCurrencyDataForTech(returnCode);
			LOGGER.info("Delete Specific Return Call  : " + returnCode);
			deleteReturnSpecificTechMetadata(returnCode, connection);
		}

		String sql = "SELECT * FROM MAP_EBR_RBR_LND_COL_TECH_METADATA";
		File excelFile = null;

		Statement statement = connection.createStatement();
		ResultSet result = statement.executeQuery(sql);

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("TECH_METADATA");

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String dateString = simpleDateFormat.format(new Date()).replace(" ", "_");
		dateString = dateString.replace(":", "_");

		writeHeaderLine(sheet, connection);
		writeDataLines(result, workbook, sheet, returnCode, busFileName, ebrVersion, connection, currencyExtraRecord, returnWiseExtraNaList);

		excelFile = new File(ResourceUtil.getKeyValue("filepath.root") + ResourceUtil.getKeyValue("returnTemplate.upload.path") + ResourceUtil.getKeyValue("file.ebr.tech.metadata") + File.separator + "TECH_METADATA_" + dateString + ".xls");

		if (!excelFile.getParentFile().exists()) {
			excelFile.getParentFile().mkdirs();
		}

		FileOutputStream outputStream = new FileOutputStream(excelFile);
		workbook.write(outputStream);
		workbook.close();

		statement.close();

		List<TechMetadatProcessBean> techMetadatProcessBeans = new ArrayList<>();

		TechMetadatProcessBean techObj = new TechMetadatProcessBean();
		techObj.setTechMetadataFileName("TECH_METADATA_" + dateString + ".xls");
		techMetadatProcessBeans.add(techObj);
		connection.close();
		return techMetadatProcessBeans;

	}

	@Transactional(rollbackFor = Exception.class)
	public List<TechMetadatProcessBean> updateTechnicalMetadata(UserMaster userMaster, String fileName) throws Exception {
		LOGGER.info("Update Technical Metadata Start  : ");
		TechMetadatProcess techMetadatProcess2 = techMetadataRepo.getActiveTechnicalMetadata();
		if (techMetadatProcess2 != null) {
			techMetadatProcess2.setIsActive(false);
			techMetadataRepo.save(techMetadatProcess2);

		}

		StringBuilder insertQueryBuilder = null;
		TechMetadatProcess techMetadatProcess = new TechMetadatProcess();

		Date createdOn = new Date();
		techMetadatProcess.setTechMetadataFileName(fileName);
		techMetadatProcess.setTechMetadataUploadedOn(createdOn);
		techMetadatProcess.setTechMetadataCreatedBy(userMaster);
		techMetadatProcess.setIsActive(true);
		techMetadatProcess.setTechMetadataInsertStart(createdOn);

		StringBuilder columnStringBuilder = null;
		StringBuilder questionMarkStringBuilder = null;
		PreparedStatement pstmt = null;
		List<String> headerColumns = null;
		Connection connection = datasource.getConnection();
		int index = 1;

		deleteCompleteTechMetadata(connection);

		try (FileInputStream fis = new FileInputStream(new File(ResourceUtil.getKeyValue("filepath.root") + ResourceUtil.getKeyValue("returnTemplate.upload.path") + ResourceUtil.getKeyValue("file.ebr.tech.metadata") + File.separator + fileName))) {
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			XSSFSheet sheet = wb.getSheetAt(0);

			headerColumns = new ArrayList<>();
			insertQueryBuilder = new StringBuilder();
			columnStringBuilder = new StringBuilder();
			questionMarkStringBuilder = new StringBuilder();

			for (int rowNo = 0; rowNo <= sheet.getLastRowNum(); rowNo++) {
				Row row = sheet.getRow(rowNo);
				if (rowNo == 0) {
					for (int columnNo = row.getFirstCellNum(); columnNo < row.getLastCellNum(); columnNo++) {
						Cell cell = row.getCell(columnNo);
						headerColumns.add(cell.getStringCellValue());
					}
				} else {
					if (rowNo == 1) {

						insertQueryBuilder.append("insert into MAP_EBR_RBR_LND_COL_TECH_METADATA (");

						for (int i = 0; i < headerColumns.size(); i++) {
							if (i == 0) {
								columnStringBuilder.append(headerColumns.get(i));
								questionMarkStringBuilder.append("?");
							} else {
								columnStringBuilder.append(", " + headerColumns.get(i));
								questionMarkStringBuilder.append(", ?");
							}
						}
						insertQueryBuilder.append(columnStringBuilder + " ) values ( " + questionMarkStringBuilder + " )");

						pstmt = connection.prepareStatement(insertQueryBuilder.toString());
					}

					if (pstmt != null) {
						for (int k = 0; k < headerColumns.size(); k++) {
							Cell cell = row.getCell(k);
							index = k + 1;
							if (cell != null) {

								if (cell.getCellType() == cell.CELL_TYPE_STRING) {
									if (cell.getStringCellValue().trim().equals("")) {
										pstmt.setString(index, null);
									} else {
										pstmt.setString(index, cell.getStringCellValue().trim());
									}
								} else {
									pstmt.setString(index, cell.getNumericCellValue() + "");
								}

							} else {
								pstmt.setString(index, null);
							}
						}
						pstmt.addBatch();
					}
				}
			}

			pstmt.executeBatch();

			//connection.commit();

		} catch (Exception e) {
			LOGGER.error("Exception : ", e);
			//e.printStackTrace();
			throw e;
		}

		connection.close();
		Date insertEnd = new Date();
		techMetadatProcess.setInsertStatus(true);
		techMetadatProcess.setTechMetadataInsertEnd(insertEnd);
		techMetadataRepo.save(techMetadatProcess);

		List<TechMetadatProcessBean> techMetadatProcessBeans = getActiveTechnicalMetadata();
		return techMetadatProcessBeans;

	}

	private void writeHeaderLine(XSSFSheet sheet, Connection connection) throws Exception {

		LOGGER.info("Header Creation Start  : ");

		List<String> listOfTechnicalMetadata = techMetadataColumndetail(connection);

		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < listOfTechnicalMetadata.size(); i++) {
			Cell headerCell = headerRow.createCell(i);
			headerCell.setCellValue(listOfTechnicalMetadata.get(i));
		}

		LOGGER.info("Header Creation End : ");

	}

	private void writeDataLines(ResultSet result, XSSFWorkbook workbook, XSSFSheet sheet, String returnCodeData, String busFileName, String ebrVersion, Connection connection, Set<String> currencyExtraRecord, List<TechMetadataDto> returnWiseExtraNaList) throws Exception {
		int rowCount = 1;

		LOGGER.info("Excel Data Render Operation Start  : ");

		while (result.next()) {

			String elementCode = result.getString("ELEMENT_CODE") == null ? " " : result.getString("ELEMENT_CODE");
			String elementVersion = result.getString("ELEMENT_VERSION") == null ? " " : result.getString("ELEMENT_VERSION");
			String ebrTableName = result.getString("EBR_TABLE_NAME") == null ? " " : result.getString("EBR_TABLE_NAME");
			String ebrColumnName = result.getString("EBR_COLUMN_NAME") == null ? " " : result.getString("EBR_COLUMN_NAME");
			String ebrRbrColumnMap = result.getString("EBR_RBR_COLUMN_MAP") == null ? " " : result.getString("EBR_RBR_COLUMN_MAP");
			String ebrDerivedTableName = result.getString("EBR_DERIVED_TABLE_NAME") == null ? " " : result.getString("EBR_DERIVED_TABLE_NAME");
			String ebrDerivedColumnName = result.getString("EBR_DERIVED_COLUMN_NAME") == null ? " " : result.getString("EBR_DERIVED_COLUMN_NAME");
			String ebrLookupColumnName = result.getString("EBR_LOOKUP_COLUMN_NAME") == null ? " " : result.getString("EBR_LOOKUP_COLUMN_NAME");
			String returnCode = result.getString("RETURN_CODE") == null ? " " : result.getString("RETURN_CODE");
			String rbrTableName = result.getString("RBR_TABLE_NAME") == null ? " " : result.getString("RBR_TABLE_NAME");
			String rbrColumnName = result.getString("RBR_COLUMN_NAME") == null ? " " : result.getString("RBR_COLUMN_NAME");
			String rbrEbrColumnMap = result.getString("RBR_EBR_COLUMN_MAP") == null ? " " : result.getString("RBR_EBR_COLUMN_MAP");
			String rbrDerivedTableName = result.getString("RBR_DERIVED_TABLE_NAME") == null ? " " : result.getString("RBR_DERIVED_TABLE_NAME");
			String rbrDerivedColumnName = result.getString("RBR_DERIVED_COLUMN_NAME") == null ? " " : result.getString("RBR_DERIVED_COLUMN_NAME");
			String rbrLookupColumnName = result.getString("RBR_LOOKUP_COLUMN_NAME") == null ? " " : result.getString("RBR_LOOKUP_COLUMN_NAME");
			String ebrDerivedTableAlias = result.getString("EBR_DERIVED_TABLE_ALIAS") == null ? " " : result.getString("EBR_DERIVED_TABLE_ALIAS");
			String rbrDerivedTableAlias = result.getString("RBR_DERIVED_TABLE_ALIAS") == null ? " " : result.getString("RBR_DERIVED_TABLE_ALIAS");
			String rbrebrCaseStatment = result.getString("RBR_EBR_CASE_STMT") == null ? " " : result.getString("RBR_EBR_CASE_STMT");
			String ebrRbrCaseStatment = result.getString("EBR_RBR_CASE_STMT") == null ? " " : result.getString("EBR_RBR_CASE_STMT");

			Row row = sheet.createRow(rowCount++);

			int columnCount = 0;

			Cell cell = row.createCell(columnCount++);
			cell.setCellValue(elementCode);
			cell = row.createCell(columnCount++);
			cell.setCellValue(elementVersion);
			cell = row.createCell(columnCount++);
			cell.setCellValue(ebrTableName);
			cell = row.createCell(columnCount++);
			cell.setCellValue(ebrColumnName);
			cell = row.createCell(columnCount++);
			cell.setCellValue(ebrRbrColumnMap);
			cell = row.createCell(columnCount++);
			cell.setCellValue(ebrDerivedTableName);
			cell = row.createCell(columnCount++);
			cell.setCellValue(ebrDerivedColumnName);
			cell = row.createCell(columnCount++);
			cell.setCellValue(ebrLookupColumnName);
			cell = row.createCell(columnCount++);
			cell.setCellValue(returnCode);
			cell = row.createCell(columnCount++);
			cell.setCellValue(rbrTableName);
			cell = row.createCell(columnCount++);
			cell.setCellValue(rbrColumnName);
			cell = row.createCell(columnCount++);
			cell.setCellValue(rbrEbrColumnMap);
			cell = row.createCell(columnCount++);
			cell.setCellValue(rbrDerivedTableName);
			cell = row.createCell(columnCount++);
			cell.setCellValue(rbrDerivedColumnName);
			cell = row.createCell(columnCount++);
			cell.setCellValue(rbrLookupColumnName);
			cell = row.createCell(columnCount++);
			cell.setCellValue(ebrDerivedTableAlias);
			cell = row.createCell(columnCount++);
			cell.setCellValue(rbrDerivedTableAlias);
			cell = row.createCell(columnCount++);
			cell.setCellValue(rbrebrCaseStatment);
			cell = row.createCell(columnCount++);
			cell.setCellValue(ebrRbrCaseStatment);

		}

		if (StringUtils.isNoneBlank(returnCodeData) && StringUtils.isNoneBlank(busFileName) && StringUtils.isNoneBlank(ebrVersion)) {
			deleteReturnSpecificTechMetadata(returnCodeData, connection);
			String busExcelFileName = ResourceUtil.getKeyValue("filepath.root") + ResourceUtil.getKeyValue("returnTemplate.upload.path") + File.separator + returnCodeData + File.separator + ResourceUtil.getKeyValue("filePath.SDMXFolder") + File.separator + ebrVersion + ResourceUtil.getKeyValue("file.ebr.buss.metadata") + File.separator + busFileName;
			generateReturnWiseTechMetadata(busExcelFileName, returnCodeData, rowCount, workbook, sheet, ebrVersion, currencyExtraRecord, returnWiseExtraNaList);
		}

		LOGGER.info("Excel Data Render End : ");

	}

	public List<String> techMetadataColumndetail(Connection con) throws SQLException {

		List<String> totalColumnInTechnicalMetadata = new ArrayList<>();
		Statement stmt = con.createStatement();

		ResultSet rs = stmt.executeQuery("select * from MAP_EBR_RBR_LND_COL_TECH_METADATA");
		java.sql.ResultSetMetaData rsMetaData = rs.getMetaData();
		//System.out.println("List of column names in the current table: ");
		LOGGER.info("List of column names in the current table: ");
		int count = rsMetaData.getColumnCount();
		for (int i = 1; i <= count; i++) {
			totalColumnInTechnicalMetadata.add(rsMetaData.getColumnName(i));
			//  System.out.println(rsMetaData.getColumnName(i));
			LOGGER.info("Column Name : " + rsMetaData.getColumnName(i));
		}

		return totalColumnInTechnicalMetadata;

	}

	public void deleteReturnSpecificTechMetadata(String returnCode, Connection connection) throws Exception {
		String sql = "DELETE FROM MAP_EBR_RBR_LND_COL_TECH_METADATA WHERE RETURN_CODE = '" + returnCode + "' ";
		Statement statement = connection.createStatement();
		statement.executeUpdate(sql);

	}

	public void deleteCompleteTechMetadata(Connection connection) throws Exception {
		LOGGER.info("Delete COmplete Technical Metadata Call : ");
		String sql = "DELETE FROM MAP_EBR_RBR_LND_COL_TECH_METADATA  ";
		Statement statement = connection.createStatement();
		statement.executeUpdate(sql);

	}

	public List<TechMetadatProcessBean> getActiveTechnicalMetadata() throws Exception {

		LOGGER.info("get Active Only one Technical Metadata : ");

		TechMetadatProcess techDataObj = techMetadataRepo.getActiveTechnicalMetadata();
		List<TechMetadatProcessBean> techMetadatProcessBeans = new ArrayList<TechMetadatProcessBean>();
		if (techDataObj != null) {
			TechMetadatProcessBean techMetadatProcessBean = new TechMetadatProcessBean();
			techMetadatProcessBean.setIsActive(techDataObj.getIsActive());
			techMetadatProcessBean.setProcessExecutedBy(techDataObj.getTechMetadataCreatedBy().getUserName());
			techMetadatProcessBean.setTechMetadataFileName(techDataObj.getTechMetadataFileName());
			techMetadatProcessBean.setInsertStatus(techDataObj.getInsertStatus());

			techMetadatProcessBean.setTechMetadataInsertEnd(techDataObj.getTechMetadataInsertEnd());

			techMetadatProcessBean.setTechMetadataInsertStart(techDataObj.getTechMetadataInsertEnd());

			techMetadatProcessBean.setTechMetadataProcessId(techDataObj.getTechMetadataProcessId());

			techMetadatProcessBean.setTechMetadataUploadedOn(techDataObj.getTechMetadataInsertEnd());

			if (techDataObj.getTechMetadataInsertStart() != null) {
				String insertStartDate = DateManip.formatAppDateTime(techDataObj.getTechMetadataInsertStart(), DD_SLASH_MM_SLASH_YYYY + " " + timeFormat, calendarFormat);
				techMetadatProcessBean.setInsertDataStr(insertStartDate);
			} else {
				techMetadatProcessBean.setInsertDataStr("");
			}

			if (techDataObj.getTechMetadataInsertEnd() != null) {
				String insertEndtDate = DateManip.formatAppDateTime(techDataObj.getTechMetadataInsertEnd(), DD_SLASH_MM_SLASH_YYYY + " " + timeFormat, calendarFormat);
				techMetadatProcessBean.setInsertEndStr(insertEndtDate);
			} else {
				techMetadatProcessBean.setInsertEndStr("");
			}

			techMetadatProcessBeans.add(techMetadatProcessBean);
		}
		return techMetadatProcessBeans;
	}

	public void generateReturnWiseTechMetadata(String fileName, String returnCode, int rowCount, XSSFWorkbook workbook, XSSFSheet sheet, String ebrVersion, Set<String> currencyExtraRecord, List<TechMetadataDto> returnWiseExtraNaList) {

		dimMapData = new TreeMap<String, Set<String>>();

		LOGGER.info("Generating technical Metadata With return Code: " + returnCode);

		Map<String, List<String>> techMetadataPrint = new TreeMap<String, List<String>>();

		try {

			TechMetadatareturnDto techMetadatareturnDto = new TechMetadatareturnDto();
			//TechMetaDataDao techMetaDataDao = new TechMetaDataDao();
			techMetadatareturnDto = getHiveTblColsData(returnCode);

			int count = rowCount;

			if (techMetadatareturnDto != null) {
				for (String filterRecord : techMetadatareturnDto.getFilterCommonRecord()) {
					Row row = sheet.createRow(count);
					row.createCell(0).setCellValue(ExcelContstant.ALL.getExcelColumnConstants());
					row.createCell(1).setCellValue(ExcelContstant.ALL.getExcelColumnConstants());
					row.createCell(2).setCellValue(ExcelContstant.NA.getExcelColumnConstants());
					row.createCell(3).setCellValue(ExcelContstant.NA.getExcelColumnConstants());
					row.createCell(4).setCellValue(ExcelContstant.NA.getExcelColumnConstants());

					row.createCell(5).setCellValue(ExcelContstant.NA.getExcelColumnConstants());
					row.createCell(6).setCellValue(ExcelContstant.NA.getExcelColumnConstants());
					row.createCell(7).setCellValue(ExcelContstant.NA.getExcelColumnConstants());
					row.createCell(8).setCellValue(returnCode);
					row.createCell(9).setCellValue(techMetadatareturnDto.getReturnVersionTableName());
					row.createCell(10).setCellValue(filterRecord);
					row.createCell(11).setCellValue(ExcelContstant.NA.getExcelColumnConstants());
					row.createCell(12).setCellValue(ExcelContstant.NA.getExcelColumnConstants());
					row.createCell(13).setCellValue(ExcelContstant.NA.getExcelColumnConstants());
					row.createCell(14).setCellValue(ExcelContstant.NA.getExcelColumnConstants());
					row.createCell(15).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
					row.createCell(16).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
					row.createCell(17).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
					row.createCell(18).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
					count++;
				}

				LOGGER.info("Return Version Specific Part Complete With record Count  : " + (count - 1));
			}

			//This Logic For Extra RBR Column 

			LOGGER.info("RBR Extra Column Started ");

			techMetadataPrint = getTechnicalMetadata(fileName, returnCode);

			//Map<String,List<String>> extraLandingColumnDetailsMap =  printTechnicalMetadataHelper.extraLandingColumnDetails(returnCode);

			int y = 0;

			Map<String, List<String>> tableAndReturnCodeMap = getEbrRbrTableInfoWrapper(returnCode);

			for (Map.Entry<String, List<String>> tableMapData : techMetadataPrint.entrySet()) {

				if (y == 0) {
					y++;
					continue;
				}

				List<String> extraColumn = tableMapData.getValue();
				for (String columnName : extraColumn) {

					if (tableAndReturnCodeMap.containsKey(tableMapData.getKey() + "~" + returnCode)) {
						List<String> ebrTableName = tableAndReturnCodeMap.get(tableMapData.getKey() + "~" + returnCode);

						for (String ebrTableNameData : ebrTableName) {

							boolean dimFound = false;
							String[] ebrTableAndVersionArray = ebrTableNameData.split("@##");
							ebrTableNameData = ebrTableAndVersionArray[0];
							String version = ebrTableAndVersionArray[1];
							String eleCode = ebrTableAndVersionArray[2];

							String[] techColumnAndMapData = null;
							Set<String> dimData = listOfDim(eleCode);
							dimData.add("NA");

							for (String dimDataStr : dimData) {

								String key = eleCode + "#@" + ebrTableNameData + "#@" + dimDataStr + "#@" + tableMapData.getKey() + "#@" + columnName;

								if (techMetadataRbrColumnMap.containsKey(key)) {

									dimFound = true;
									TechMetadataDto techMetadataDto = techMetadataRbrColumnMap.get(key);
									CellStyle cellStyle1 = workbook.createCellStyle();
									cellStyle1.setFillForegroundColor(IndexedColors.YELLOW.index);
									cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
									Row row = sheet.createRow(count);

									row.createCell(0).setCellValue(eleCode);
									row.createCell(1).setCellValue(version);
									row.createCell(2).setCellValue(ebrTableNameData);

									Cell cell = row.createCell(3);
									cell.setCellStyle(cellStyle1);

									cell.setCellValue(techMetadataDto.getEbrColumnName());
									row.createCell(4).setCellValue(techMetadataDto.getEbrRbrColumnMap());

									row.createCell(5).setCellValue(techMetadataDto.getEbrDerivedTableName());
									row.createCell(6).setCellValue(techMetadataDto.getEbrDerivedColumnName());
									row.createCell(7).setCellValue(techMetadataDto.getEbrLookUpColumnName());
									row.createCell(8).setCellValue(returnCode);
									row.createCell(9).setCellValue(tableMapData.getKey());
									row.createCell(10).setCellValue(columnName);
									row.createCell(11).setCellValue(techMetadataDto.getRbrEbrColumnMap());
									row.createCell(12).setCellValue(techMetadataDto.getRbrDerivedTableName());
									row.createCell(13).setCellValue(techMetadataDto.getRbrDerivedColumnName());
									row.createCell(14).setCellValue(techMetadataDto.getRbrLookUpColumnName());
									row.createCell(15).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
									row.createCell(16).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
									row.createCell(17).setCellValue(techMetadataDto.getRbrEbrCaseStatment());
									row.createCell(18).setCellValue(techMetadataDto.getEbrRbrCaseStatment());
									count++;

								}

								if (dimFound) {
									break;
								}
							}

							String convertedData = "";
							if (!CollectionUtils.isEmpty(dimData)) {
								convertedData = String.join(", ", dimData);
							}

							if (!dimFound) {

								CellStyle cellStyle1 = workbook.createCellStyle();
								cellStyle1.setFillForegroundColor(IndexedColors.YELLOW.index);
								cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
								Row row = sheet.createRow(count);

								row.createCell(0).setCellValue(eleCode);
								row.createCell(1).setCellValue(version);
								row.createCell(2).setCellValue(ebrTableNameData);

								Cell cell = row.createCell(3);
								cell.setCellStyle(cellStyle1);

								if (techColumnAndMapData != null) {
									if (techColumnAndMapData[0] != null) {
										cell.setCellValue(techColumnAndMapData[0]);
									} else {
										cell.setCellValue(convertedData);
									}

									row.createCell(4).setCellValue(techColumnAndMapData[1]);
								} else {
									cell.setCellValue(convertedData);
									row.createCell(4).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
								}

								row.createCell(5).setCellValue(ExcelContstant.NA.getExcelColumnConstants());
								row.createCell(6).setCellValue(ExcelContstant.NA.getExcelColumnConstants());
								row.createCell(7).setCellValue(ExcelContstant.NA.getExcelColumnConstants());
								row.createCell(8).setCellValue(returnCode);
								row.createCell(9).setCellValue(tableMapData.getKey());
								row.createCell(10).setCellValue(columnName);
								row.createCell(11).setCellValue(ExcelContstant.DL.getExcelColumnConstants());
								row.createCell(12).setCellValue(ExcelContstant.NA.getExcelColumnConstants());
								row.createCell(13).setCellValue(ExcelContstant.NA.getExcelColumnConstants());
								row.createCell(14).setCellValue(ExcelContstant.NA.getExcelColumnConstants());
								row.createCell(15).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
								row.createCell(16).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
								row.createCell(17).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
								row.createCell(18).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
								count++;
							}
						}
					}

				}
			}

			//Extra Entry Case Where 2 or more than 2 RBR column Point One EBR Fact Table 

			if (!CollectionUtils.isEmpty(returnWiseExtraNaList)) {
				for (TechMetadataDto techData : returnWiseExtraNaList) {

					if (returnCode.equals(techData.getReturnCode())) {

						CellStyle cellStyle1 = workbook.createCellStyle();
						cellStyle1.setFillForegroundColor(IndexedColors.YELLOW.index);
						cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
						Row row = sheet.createRow(count);

						row.createCell(0).setCellValue(techData.getElementCode());
						row.createCell(1).setCellValue(techData.getElementVersion());
						row.createCell(2).setCellValue(techData.getEbrTableName());

						Cell cell = row.createCell(3);
						cell.setCellStyle(cellStyle1);

						cell.setCellValue(techData.getEbrColumnName());
						row.createCell(4).setCellValue(techData.getEbrRbrColumnMap());

						row.createCell(5).setCellValue(techData.getEbrDerivedTableName());
						row.createCell(6).setCellValue(techData.getEbrDerivedColumnName());
						row.createCell(7).setCellValue(techData.getEbrLookUpColumnName());
						row.createCell(8).setCellValue(returnCode);
						row.createCell(9).setCellValue(techData.getRbrTableName());
						row.createCell(10).setCellValue(techData.getRbrColumnName());
						row.createCell(11).setCellValue(techData.getRbrEbrColumnMap());
						row.createCell(12).setCellValue(techData.getRbrDerivedTableName());
						row.createCell(13).setCellValue(techData.getRbrDerivedColumnName());
						row.createCell(14).setCellValue(techData.getRbrLookUpColumnName());
						row.createCell(15).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						row.createCell(16).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
						row.createCell(17).setCellValue(techData.getRbrEbrCaseStatment());
						row.createCell(18).setCellValue(techData.getEbrRbrCaseStatment());
						count++;
					}
				}
			}

			//currency Logic 

			if (!CollectionUtils.isEmpty(currencyExtraRecord)) {
				for (String str : currencyExtraRecord) {
					String[] arrayData = str.split("~~~");
					Row row = sheet.createRow(count);

					row.createCell(0).setCellValue(arrayData[0]);
					row.createCell(1).setCellValue(arrayData[1]);
					row.createCell(2).setCellValue(arrayData[2]);
					row.createCell(3).setCellValue(ExcelContstant.CURRENCY.getExcelColumnConstants());
					row.createCell(4).setCellValue(ExcelContstant.DL.getExcelColumnConstants());
					row.createCell(5).setCellValue(ExcelContstant.NA.getExcelColumnConstants());
					row.createCell(6).setCellValue(ExcelContstant.NA.getExcelColumnConstants());
					row.createCell(7).setCellValue(ExcelContstant.NA.getExcelColumnConstants());
					row.createCell(8).setCellValue(returnCode);
					row.createCell(9).setCellValue(ExcelContstant.NA.getExcelColumnConstants());
					row.createCell(10).setCellValue(ExcelContstant.CURRENCY_CODE.getExcelColumnConstants());
					row.createCell(11).setCellValue(ExcelContstant.DL.getExcelColumnConstants());
					row.createCell(12).setCellValue(ExcelContstant.NA.getExcelColumnConstants());
					row.createCell(13).setCellValue(ExcelContstant.NA.getExcelColumnConstants());
					row.createCell(14).setCellValue(ExcelContstant.NA.getExcelColumnConstants());
					row.createCell(15).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
					row.createCell(16).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
					row.createCell(17).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());
					row.createCell(18).setCellValue(ExcelContstant.BLANK_SPACE.getExcelColumnConstants());

					count++;
				}
			}

			/*
			 * FileOutputStream fileOut = new FileOutputStream(generatingFileName);
			 * workbook.write(fileOut); fileOut.close(); workbook.close();
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}

		//return "Excel generate Location "+generatingFileName;
	}

	public Set<String> commonRbrColumns() {
		Set<String> columnNames = new HashSet<String>();
		columnNames.add("REPORT_AS_ON_DATE");
		columnNames.add("BANK_WORKING_CODE");
		columnNames.add("BANK_PEERGROUP_CODE");
		columnNames.add("TEMPLATE_SHEET_NO");
		columnNames.add("UPLOAD_ID");
		columnNames.add("AUDIT_FLAG");
		columnNames.add("RETURN_CODE");
		columnNames.add("RETURN_REPORTING_FREQUENCY");
		columnNames.add("RETURN_TAXONOMY_VERSION");
		columnNames.add("UNIT_DESCRIPTION");
		columnNames.add("CURRENCY_CODE");
		columnNames.add("PERIOD_DETAIL_START_DATE");
		columnNames.add("REPORTING_PERIOD_START_DATE");
		columnNames.add("PERIOD_DETAIL_END_DATE");
		columnNames.add("UPLOAD_DATE");
		columnNames.add("REPORTING_INSTITUTION");
		columnNames.add("ADDRESS_REPORTING_INSTITUTION");
		columnNames.add("BANK_CATEGORY");
		columnNames.add("RETURN_NAME");
		columnNames.add("FOR_PERIOD_ENDED");
		//columnNames.add("ITEM_FREE_TEXT");
		columnNames.add("ITEM_FREE_TEXT_SORT_ORDER");
		columnNames.add("ITEM_CODE");
		columnNames.add("UPLOAD_FORMAT");
		columnNames.add("UPLOAD_CHANNEL");
		columnNames.add("UPLOAD_FORMAT");
		columnNames.add("SR_NO");
		return columnNames;
	}

	public TechMetadatareturnDto getHiveTblColsData(String returnCode) {
		TechMetadatareturnDto techMetadatareturnDto = new TechMetadatareturnDto();

		List<String> hiveDataList = new ArrayList<>();
		String likeTableName = "RETURN_VERSION";

		String hiveQuery = "SELECT DW_HIVE_TBL_COLS FROM TBL_DW_HIVE_TBL_DESIGN";

		if (returnCode != null) {
			hiveQuery = hiveQuery + " WHERE DW_HIVE_TBL_RETURN_CODE like '%" + returnCode + "%' and DW_HIVE_TBL_NAME like '%" + likeTableName + "%' ";

		} else {
			LOGGER.error("null returncode");
		}

		hiveQuery = hiveQuery + " ORDER BY DW_HIVE_TBL_COLS";

		List<Tuple> tuples = entityManager.createNativeQuery(hiveQuery.toString(), Tuple.class).getResultList();

		try {

			for (Tuple tuple : tuples) {
				hiveDataList.add((String) tuple.get("DW_HIVE_TBL_COLS"));
			}

		} catch (Exception e) {
			System.out.println(e);
			LOGGER.error(e);
		}
		LOGGER.info("hive list size ==> " + hiveDataList.size());

		List<String> techMetadatRbrColList = getTechMetaDataRbrColData();
		Set<String> finalFilterRecord = techMetaDataHelper(returnCode, hiveDataList, techMetadatRbrColList);
		String returnVersionTablName = loadReturnVersionSpecificTableNameWarpper(returnCode);

		techMetadatareturnDto.setHiveColData(hiveDataList);
		techMetadatareturnDto.setTechMetadataRbrColData(techMetadatRbrColList);
		techMetadatareturnDto.setFilterCommonRecord(finalFilterRecord);
		techMetadatareturnDto.setReturnCode(returnCode);
		techMetadatareturnDto.setReturnVersionTableName(returnVersionTablName);

		return techMetadatareturnDto;
	}

	public List<String> getTechMetaDataRbrColData() {
		List<String> techMetaDataList = new ArrayList<>();

		String MetaDataQuery = "SELECT RBR_COLUMN_NAME FROM MAP_EBR_RBR_LND_COL_TECH_METADATA";

		MetaDataQuery = MetaDataQuery + " WHERE RETURN_CODE != 'NA' AND RETURN_CODE != 'ALL' ORDER BY RBR_COLUMN_NAME";

		try {
			List<Tuple> tuples = entityManager.createNativeQuery(MetaDataQuery.toString(), Tuple.class).getResultList();
			for (Tuple tuple : tuples) {
				techMetaDataList.add((String) tuple.get("RBR_COLUMN_NAME"));
			}

		} catch (Exception e) {
			System.out.println(e);
			LOGGER.error(e);
		}

		LOGGER.info("tech meta data list size ==> " + techMetaDataList.size());
		return techMetaDataList;
	}

	public Set<String> techMetaDataHelper(String returnCode, List<String> hiveDataList, List<String> techMetaDataList) {

		//PrintTechnicalMetadataHelper printTechnicalMetadataHelper = new PrintTechnicalMetadataHelper();
		Set<String> returnWiseColList = new HashSet<String>();
		Set<String> commonRbrColumns = commonRbrColumns();

		if (CollectionUtils.isEmpty(techMetaDataList)) {
			//This Logic For First Time Insert Record In DB 
			for (String hiveCol : hiveDataList) {
				if (!returnWiseColList.contains(hiveCol) && !commonRbrColumns.contains(hiveCol)) {
					returnWiseColList.add(hiveCol);
				}
			}
		} else {
			for (String hiveCol : hiveDataList) {
				for (String metaDataCol : techMetaDataList) {
					if (hiveCol.equals(metaDataCol)) {
						if (!returnWiseColList.contains(metaDataCol) && !commonRbrColumns.contains(hiveCol)) {
							returnWiseColList.add(hiveCol);
							break;
						}
					}
				}
			}
		}

		LOGGER.info("return specific column list size ==> " + returnWiseColList.size());
		return returnWiseColList;

	}

	public String loadReturnVersionSpecificTableNameWarpper(String returnCodeName) {
		String returnTableName = "";
		String likeTableName = "RETURN_VERSION";
		try {
			String sql = "select DW_HIVE_TBL_NAME from TBL_DW_HIVE_TBL_DESIGN where DW_HIVE_TBL_RETURN_CODE like '%" + returnCodeName + "%' and DW_HIVE_TBL_NAME like '%" + likeTableName + "%'";
			List<Tuple> tuples = entityManager.createNativeQuery(sql.toString(), Tuple.class).getResultList();

			for (Tuple tuple : tuples) {
				if ((String) tuple.get("DW_HIVE_TBL_NAME") != null) {
					returnTableName = (String) tuple.get("DW_HIVE_TBL_NAME");
					return returnTableName;
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception : ", e);
		}

		return returnTableName;
	}

	public void getRbrColumnMap() {

		String query = "SELECT ELEMENT_CODE,ELEMENT_VERSION,EBR_TABLE_NAME," + "     EBR_COLUMN_NAME,EBR_RBR_COLUMN_MAP,EBR_DERIVED_TABLE_NAME," + "     EBR_DERIVED_COLUMN_NAME,EBR_LOOKUP_COLUMN_NAME,RETURN_CODE," + "     RBR_TABLE_NAME ,RBR_COLUMN_NAME , RBR_EBR_COLUMN_MAP ," + "     RBR_DERIVED_TABLE_NAME,RBR_DERIVED_COLUMN_NAME,RBR_LOOKUP_COLUMN_NAME,EBR_DERIVED_TABLE_ALIAS,RBR_DERIVED_TABLE_ALIAS ," + "     RBR_EBR_CASE_STMT,EBR_RBR_CASE_STMT " + "     FROM MAP_EBR_RBR_LND_COL_TECH_METADATA WHERE RETURN_CODE != 'ALL' AND RETURN_CODE != 'NA' AND ELEMENT_CODE != 'ALL' AND EBR_COLUMN_NAME != 'CURRENCY' ";
		try {
			List<Tuple> tuples = entityManager.createNativeQuery(query.toString(), Tuple.class).getResultList();

			for (Tuple tuple : tuples) {

				String key = (String) tuple.get("ELEMENT_CODE") + "#@" + (String) tuple.get("EBR_TABLE_NAME") + "#@" + (String) tuple.get("EBR_COLUMN_NAME") + "#@" + (String) tuple.get("RBR_TABLE_NAME") + "#@" + (String) tuple.get("RBR_COLUMN_NAME");

				if (!techMetadataRbrColumnMap.containsKey(key)) {

					TechMetadataDto techMetadataDto = new TechMetadataDto();

					techMetadataDto.setElementCode((String) tuple.get("ELEMENT_CODE"));
					techMetadataDto.setElementVersion((String) tuple.get("ELEMENT_VERSION"));
					techMetadataDto.setEbrTableName((String) tuple.get("EBR_TABLE_NAME"));
					techMetadataDto.setEbrColumnName((String) tuple.get("EBR_COLUMN_NAME"));
					techMetadataDto.setEbrRbrColumnMap((String) tuple.get("EBR_RBR_COLUMN_MAP"));
					techMetadataDto.setEbrDerivedTableName((String) tuple.get("EBR_DERIVED_TABLE_NAME"));
					techMetadataDto.setEbrDerivedColumnName((String) tuple.get("EBR_DERIVED_COLUMN_NAME"));
					techMetadataDto.setEbrLookUpColumnName((String) tuple.get("EBR_LOOKUP_COLUMN_NAME"));
					techMetadataDto.setReturnCode((String) tuple.get("RETURN_CODE"));
					techMetadataDto.setRbrTableName((String) tuple.get("RBR_TABLE_NAME"));
					techMetadataDto.setRbrColumnName((String) tuple.get("RBR_COLUMN_NAME"));
					techMetadataDto.setRbrEbrColumnMap((String) tuple.get("RBR_EBR_COLUMN_MAP"));
					techMetadataDto.setRbrDerivedTableName((String) tuple.get("RBR_DERIVED_TABLE_NAME"));
					techMetadataDto.setRbrDerivedColumnName((String) tuple.get("RBR_DERIVED_COLUMN_NAME"));
					techMetadataDto.setRbrLookUpColumnName((String) tuple.get("RBR_LOOKUP_COLUMN_NAME"));
					techMetadataDto.setEbrDerivedTableNameAlias((String) tuple.get("EBR_DERIVED_TABLE_ALIAS"));
					techMetadataDto.setRbrDerivedTableAlias((String) tuple.get("RBR_DERIVED_TABLE_ALIAS"));
					techMetadataDto.setRbrEbrCaseStatment((String) tuple.get("RBR_EBR_CASE_STMT"));
					techMetadataDto.setEbrRbrCaseStatment((String) tuple.get("EBR_RBR_CASE_STMT"));

					if (tuple.get("RBR_COLUMN_NAME").toString().equals("NA")) {
						returnWiseExtraNaList.add(techMetadataDto);
					}

					techMetadataRbrColumnMap.put(key, techMetadataDto);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public Set<String> getCurrencyDataForTech(String returnCode) {
		Set<String> ebrDistinctTableList = new HashSet<>();
		String query = "  SELECT TEC.ELEMENT_CODE ,TEC.ELEMENT_VERSION , TEC.EBR_TABLE_NAME ,TE.TABLE_NAME,TE.COLUMN_NAME " + " FROM MAP_EBR_RBR_LND_TBL_TECH_METADATA TEC " + " JOIN TBL_EBR_DATA_DESIGN TE WHERE TE.TABLE_NAME = TEC.EBR_TABLE_NAME AND TEC.RETURN_CODE = '" + returnCode + "' " + " AND TE.COLUMN_NAME = '" + ExcelContstant.CURRENCY.getExcelColumnConstants() + "' ORDER BY TEC.ELEMENT_CODE ";
		try {
			List<Tuple> tuples = entityManager.createNativeQuery(query.toString(), Tuple.class).getResultList();

			for (Tuple tuple : tuples) {

				String key = (String) tuple.get("ELEMENT_CODE") + "~~~" + (String) tuple.get("ELEMENT_VERSION") + "~~~" + (String) tuple.get("EBR_TABLE_NAME");
				if (!ebrDistinctTableList.contains(key)) {
					ebrDistinctTableList.add(key);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ebrDistinctTableList;
	}

	public Map<String, List<String>> getTechnicalMetadata(String fileName, String returnCode) throws Exception {
		Set<String> columnsName = new HashSet<String>();
		columnsName = validateBusinessMetaDataSheetWrapper(fileName, returnCode);
		Map<String, List<String>> techMetadataPrint = new TreeMap<String, List<String>>();
		techMetadataPrint = printTechMetadata(columnsName, fileName, returnCode);
		return techMetadataPrint;

	}

	public Map<String, List<String>> getEbrRbrTableInfoWrapper(String returnCode) {
		String query = " SELECT " + " ERTM.ELEMENT_CODE ," + " ERTM.ELEMENT_VERSION ," + " ERTM.EBR_TABLE_NAME ," + " ERTM.BUSINESS_METADATA_TABLE_NAME ," + " ERTM.RETURN_CODE ," + " ERTM.RBR_TABLE_NAME " + " FROM MAP_EBR_RBR_LND_TBL_TECH_METADATA ERTM  WHERE ERTM.RETURN_CODE = '" + returnCode + "' ORDER BY ERTM.RBR_TABLE_NAME ";
		int p = 0;
		List<String> ebrTableNameList = null;
		Map<String, List<String>> tableAndReturnCodeMap = new HashedMap<>();
		LOGGER.info("SQL to execute: " + query);
		try {
			List<Tuple> tuples = entityManager.createNativeQuery(query.toString(), Tuple.class).getResultList();

			for (Tuple tuple : tuples) {
				if (!tuple.get("EBR_TABLE_NAME").toString().equals("N_A")) {
					if (p == 0) {
						p++;
						ebrTableNameList = new ArrayList<>();
						ebrTableNameList.add((String) tuple.get("EBR_TABLE_NAME") + "@##" + (String) tuple.get("ELEMENT_VERSION") + "@##" + (String) tuple.get("ELEMENT_CODE"));
						tableAndReturnCodeMap.put((String) tuple.get("RBR_TABLE_NAME") + "~" + (String) tuple.get("RETURN_CODE"), ebrTableNameList);
					} else {
						if (tableAndReturnCodeMap.containsKey((String) tuple.get("RBR_TABLE_NAME") + "~" + (String) tuple.get("RETURN_CODE"))) {
							String ebrTableName = (String) tuple.get("EBR_TABLE_NAME") + "@##" + (String) tuple.get("ELEMENT_VERSION") + "@##" + (String) tuple.get("ELEMENT_CODE");
							List<String> tableNameList = tableAndReturnCodeMap.get((String) tuple.get("RBR_TABLE_NAME") + "~" + (String) tuple.get("RETURN_CODE"));
							tableNameList.add(ebrTableName);
							tableAndReturnCodeMap.put((String) tuple.get("RBR_TABLE_NAME") + "~" + (String) tuple.get("RETURN_CODE"), tableNameList);
						} else {
							ebrTableNameList = new ArrayList<>();
							ebrTableNameList.add((String) tuple.get("EBR_TABLE_NAME") + "@##" + (String) tuple.get("ELEMENT_VERSION") + "@##" + (String) tuple.get("ELEMENT_CODE"));
							tableAndReturnCodeMap.put((String) tuple.get("RBR_TABLE_NAME") + "~" + (String) tuple.get("RETURN_CODE"), ebrTableNameList);
						}
					}
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return tableAndReturnCodeMap;
	}

	public Map<String, List<String>> printTechMetadata(Set<String> columnNames, String fileName, String returnCode) throws Exception {

		File inputFile = new File(fileName);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String dateString = simpleDateFormat.format(new Date()).replace(" ", "_");
		dateString = dateString.replace(":", "_");

		Map<String, List<String>> tableAndColumnMap = new TreeMap<>();
		tableAndColumnMap = loadLandingReturnTableDetails(returnCode);

		Map<String, List<String>> tableAndReturnCodeMap = getEbrRbrTableInfo(returnCode);
		Map<String, List<String>> finalUpdatedTableColumnMap = new TreeMap<>();
		itemFreeTextFoundForEntry = false;

		for (Map.Entry<String, List<String>> elementData : tableAndColumnMap.entrySet()) {

			List<String> tableColumnList = elementData.getValue();
			List<String> finalTableColumnList = new ArrayList<>();

			for (String tableColumn : tableColumnList) {

				columnListLabel: for (String column : columnNames) {
					if (columnNames.contains(tableColumn)) {
						if (tableColumn.equals("ITEM_FREE_TEXT")) {
							finalTableColumnList.add(tableColumn);
							//itemFreeTextFoundForEntry = true;
						}
						//do nothing
					} else {
						finalTableColumnList.add(tableColumn);
					}

					break columnListLabel;
				}
			}

			finalUpdatedTableColumnMap.put(elementData.getKey(), finalTableColumnList);
		}

		return finalUpdatedTableColumnMap;

	}

	public Map<String, List<String>> getEbrRbrTableInfo(String returnCode) {
		String query = " SELECT " + " ERTM.ELEMENT_CODE ," + " ERTM.ELEMENT_VERSION ," + " ERTM.EBR_TABLE_NAME ," + " ERTM.BUSINESS_METADATA_TABLE_NAME ," + " ERTM.RETURN_CODE ," + " ERTM.RBR_TABLE_NAME " + " FROM MAP_EBR_RBR_LND_TBL_TECH_METADATA ERTM  WHERE ERTM.RETURN_CODE = '" + returnCode + "' ORDER BY ERTM.RBR_TABLE_NAME ";
		int p = 0;
		List<String> ebrTableNameList = null;
		Map<String, List<String>> tableAndReturnCodeMap = new HashedMap<>();
		LOGGER.info("SQL to execute: " + query);
		try {
			List<Tuple> tuples = entityManager.createNativeQuery(query.toString(), Tuple.class).getResultList();

			for (Tuple tuple : tuples) {
				if (!tuple.get("EBR_TABLE_NAME").toString().equals("N_A")) {
					if (p == 0) {
						p++;
						ebrTableNameList = new ArrayList<>();
						ebrTableNameList.add((String) tuple.get("EBR_TABLE_NAME"));
						tableAndReturnCodeMap.put((String) tuple.get("RBR_TABLE_NAME") + "~" + (String) tuple.get("RETURN_CODE"), ebrTableNameList);
					} else {
						if (tableAndReturnCodeMap.containsKey((String) tuple.get("RBR_TABLE_NAME") + "~" + (String) tuple.get("RETURN_CODE"))) {
							String ebrTableName = (String) tuple.get("EBR_TABLE_NAME");
							List<String> tableNameList = tableAndReturnCodeMap.get((String) tuple.get("RBR_TABLE_NAME") + "~" + (String) tuple.get("RETURN_CODE"));
							tableNameList.add(ebrTableName);
							tableAndReturnCodeMap.put((String) tuple.get("RBR_TABLE_NAME") + "~" + (String) tuple.get("RETURN_CODE"), tableNameList);
						} else {
							ebrTableNameList = new ArrayList<>();
							ebrTableNameList.add((String) tuple.get("EBR_TABLE_NAME"));
							tableAndReturnCodeMap.put((String) tuple.get("RBR_TABLE_NAME") + "~" + (String) tuple.get("RETURN_CODE"), ebrTableNameList);
						}
					}
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return tableAndReturnCodeMap;
	}

	private Map<String, List<String>> loadLandingReturnTableDetails(String returnCode) throws Exception {

		Map<String, List<String>> landingTableDetails = new HashMap<>();

		try {
			String sql = "select * from TBL_DW_HIVE_TBL_DESIGN where DW_HIVE_TBL_RETURN_CODE like '%" + returnCode + "%'";
			LOGGER.info("SQL to execute: " + sql);

			List<Tuple> tuples = entityManager.createNativeQuery(sql.toString(), Tuple.class).getResultList();

			for (Tuple tuple : tuples) {
				if ((String) tuple.get("DW_HIVE_TBL_NAME") != null && (String) tuple.get("DW_HIVE_TBL_COLS") != null) {
					try {
						if (landingTableDetails.containsKey((String) tuple.get("DW_HIVE_TBL_NAME"))) {
							List<String> columnList = landingTableDetails.get((String) tuple.get("DW_HIVE_TBL_NAME"));

							columnList.add((String) tuple.get("DW_HIVE_TBL_COLS"));

							landingTableDetails.put((String) tuple.get("DW_HIVE_TBL_NAME"), columnList);
						} else {

							List<String> columnList = new ArrayList<>();
							columnList.add((String) tuple.get("DW_HIVE_TBL_COLS"));

							landingTableDetails.put((String) tuple.get("DW_HIVE_TBL_NAME"), columnList);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return landingTableDetails;
	}

	public Set<String> validateBusinessMetaDataSheetWrapper(String fileName, String returnCode) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		sheet2 = workbook.createSheet("Error");
		Set<String> columnNames = new HashSet<>();
		try (FileInputStream fis = new FileInputStream(new File(fileName)); HSSFWorkbook wb = new HSSFWorkbook(fis);) {
			Map<String, List<String>> issueMap = new TreeMap<>();
			int returnCellReferenceIndex = 0;
			int returnTableNameIndex = 0;
			int returnItemCodeIndex = 0;
			int returnColumnNameIndex = 0;
			int returnItemDisplayTextIndex = 0;
			int returnItemFreeTextIndex = -1;

			int rowNo = 0;

			boolean itemFreeTextFound = false;

			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				HSSFSheet sheet = wb.getSheetAt(i);
				if (sheet.getSheetName().toUpperCase().contains("EBR_RBR_MAPPING_TABLE_STRUCTURE") || sheet.getSheetName().toUpperCase().contains("EBR-RBR-MAPPING-TABLE-STRUCTURE") || sheet.getSheetName().toUpperCase().contains("EBR_RBR_TABLE_MAP") || sheet.getSheetName().toUpperCase().contains("SHEET")) {
					continue;
				}
				returnItemFreeTextIndex = -1;

				issueMap.put(sheet.getSheetName(), new ArrayList<>());

				FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
				for (Row row : sheet) // iteration over row using for each loop
				{
					try {
						rowNo = row.getRowNum();
						itemFreeTextFound = false;
						for (int j = 0; j < row.getLastCellNum(); j++) {
							Cell cell = row.getCell(j);

							if (cell != null) {
								if (rowNo == 0) {
									switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
									case Cell.CELL_TYPE_STRING: // field that represents string cell type
										if (cell.getStringCellValue().equalsIgnoreCase("RETURN_ITEM_DISPLAY_TEXT")) {
											returnItemDisplayTextIndex = cell.getColumnIndex();
										} else if (cell.getStringCellValue().equalsIgnoreCase("RETURN_CELL_REF_NO")) {
											returnCellReferenceIndex = cell.getColumnIndex();
										} else if (cell.getStringCellValue().equalsIgnoreCase("RETURN_TABLE_NAME")) {
											returnTableNameIndex = cell.getColumnIndex();
										} else if (cell.getStringCellValue().equalsIgnoreCase("RETURN_ITEM_CODE")) {
											returnItemCodeIndex = cell.getColumnIndex();
										} else if (cell.getStringCellValue().equalsIgnoreCase("RETURN_COLUMN_NAME")) {
											returnColumnNameIndex = cell.getColumnIndex();
										} else if (cell.getStringCellValue().equalsIgnoreCase("RETURN_ITEM_FREE_TEXT")) {
											returnItemFreeTextIndex = cell.getColumnIndex();
										}
										break;
									}
								} else {
									if (cell.getColumnIndex() == returnItemCodeIndex) {
										switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
										case Cell.CELL_TYPE_STRING:
											if (cell.getStringCellValue() == null || cell.getStringCellValue().equals("") || cell.getStringCellValue().equals(" ")) {
												issueMap.get(sheet.getSheetName()).add("Item Code blank for row : " + rowNo + "  Having return cell reference :  " + row.getCell(returnCellReferenceIndex).getStringCellValue());
											}
											break;
										}
									} else if (cell.getColumnIndex() == returnColumnNameIndex) {
										switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
										case Cell.CELL_TYPE_STRING:
											if (cell.getStringCellValue() == null || cell.getStringCellValue().equals("") || cell.getStringCellValue().equals(" ")) {
												issueMap.get(sheet.getSheetName()).add("ReturnColumn name blank for row : " + rowNo + " ,sdmx cell reference no : " + row.getCell(returnCellReferenceIndex).getStringCellValue());
											} else {
												if (!cell.getStringCellValue().toUpperCase().equals(cell.getStringCellValue())) {
													issueMap.get(sheet.getSheetName()).add("ReturnColumn name Invalid for row : " + rowNo + " ,sdmx cell reference no : " + row.getCell(returnCellReferenceIndex).getStringCellValue());
												} else {
													columnNames.add(cell.getStringCellValue());
												}
											}
											break;
										}
									} else if (cell.getColumnIndex() == returnTableNameIndex) {
										switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
										case Cell.CELL_TYPE_STRING:
											if (cell.getStringCellValue() == null || cell.getStringCellValue().equals("") || cell.getStringCellValue().equals(" ")) {
												issueMap.get(sheet.getSheetName()).add("Return Table  name blank for row : " + rowNo + ", cell reference no is : " + row.getCell(returnCellReferenceIndex).getStringCellValue());
											}
											break;
										}
									} else if (cell.getColumnIndex() == returnItemDisplayTextIndex) {
										switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
										case Cell.CELL_TYPE_STRING:
											if (cell.getStringCellValue() == null || cell.getStringCellValue().equals("") || cell.getStringCellValue().equals(" ")) {
												if (row.getCell(returnItemCodeIndex) != null && row.getCell(returnItemCodeIndex).getStringCellValue() != null && !row.getCell(returnItemCodeIndex).getStringCellValue().equals("") && !row.getCell(returnItemCodeIndex).getStringCellValue().equals(" ")) {
													issueMap.get(sheet.getSheetName()).add("Item Code Present but Display Text null for row : " + rowNo + " having Item Code : " + row.getCell(returnItemCodeIndex).getStringCellValue() + " and cell reference no is : " + row.getCell(returnCellReferenceIndex).getStringCellValue());
												}
											}
											break;
										}
									} else if (cell.getColumnIndex() == returnItemFreeTextIndex) {
										switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
										case Cell.CELL_TYPE_STRING:
											if (cell.getStringCellValue() != null && !cell.getStringCellValue().equals("") && !cell.getStringCellValue().equals(" ")) {
												if (!itemFreeTextFound) {
													if (!cell.getStringCellValue().equals("N_A")) {
														issueMap.get(sheet.getSheetName()).add("N_A not found for cell reference no is : " + row.getCell(returnCellReferenceIndex).getStringCellValue());
													}
												}
											}
											break;
										}
									} else {
										switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
										case Cell.CELL_TYPE_STRING:
											if (cell.getStringCellValue().equals("ITEM_FREE_TEXT")) {
												itemFreeTextFound = true;
												if (returnItemFreeTextIndex < 0 || row.getCell(returnItemFreeTextIndex) == null || !row.getCell(returnItemFreeTextIndex).getStringCellValue().equals("ITEM_FREE_TEXT")) {
													issueMap.get(sheet.getSheetName()).add("Return Item free text not found for cell reference no is : " + row.getCell(returnCellReferenceIndex).getStringCellValue());
												}
											}
											break;
										}
									}
								}
							} else {
								// cell is null
								if (j == returnItemCodeIndex) {
									issueMap.get(sheet.getSheetName()).add("Item Code blank for row : " + rowNo + "  Having return cell reference :  " + row.getCell(returnCellReferenceIndex).getStringCellValue());
								} else if (j == returnColumnNameIndex) {
									issueMap.get(sheet.getSheetName()).add("ReturnColumn name blank for row : " + rowNo + " ,sdmx cell reference no : " + row.getCell(returnCellReferenceIndex).getStringCellValue());
								} else if (j == returnTableNameIndex) {
									issueMap.get(sheet.getSheetName()).add("Return Table  name blank for row : " + rowNo + ", cell reference no is : " + row.getCell(returnCellReferenceIndex).getStringCellValue());
								} else if (j == returnItemDisplayTextIndex) {
									issueMap.get(sheet.getSheetName()).add("Item Code Present but Display Text null for row : " + rowNo + " having Item Code : " + row.getCell(returnItemCodeIndex).getStringCellValue() + " and cell reference no is : " + row.getCell(returnCellReferenceIndex).getStringCellValue());
								} else if (j == returnItemFreeTextIndex && itemFreeTextFound) {
									issueMap.get(sheet.getSheetName()).add("Return Item free text not found for cell reference no is : " + row.getCell(returnCellReferenceIndex).getStringCellValue());
								}
							}
						}
					} catch (Exception e) {
						LOGGER.error("Exception :", e);
					}
				}
			}

			columnNames.add("REPORT_AS_ON_DATE");
			columnNames.add("BANK_WORKING_CODE");
			columnNames.add("BANK_PEERGROUP_CODE");
			columnNames.add("TEMPLATE_SHEET_NO");
			columnNames.add("UPLOAD_ID");
			columnNames.add("AUDIT_FLAG");
			columnNames.add("RETURN_CODE");
			columnNames.add("RETURN_REPORTING_FREQUENCY");
			columnNames.add("RETURN_TAXONOMY_VERSION");
			columnNames.add("UNIT_DESCRIPTION");
			columnNames.add("CURRENCY_CODE");
			columnNames.add("PERIOD_DETAIL_START_DATE");
			columnNames.add("REPORTING_PERIOD_START_DATE");
			columnNames.add("PERIOD_DETAIL_END_DATE");
			columnNames.add("UPLOAD_DATE");
			columnNames.add("REPORTING_INSTITUTION");
			columnNames.add("ADDRESS_REPORTING_INSTITUTION");
			columnNames.add("BANK_CATEGORY");
			columnNames.add("RETURN_NAME");
			columnNames.add("FOR_PERIOD_ENDED");
			columnNames.add("ITEM_FREE_TEXT");
			columnNames.add("ITEM_FREE_TEXT_SORT_ORDER");
			columnNames.add("ITEM_CODE");
			columnNames.add("UPLOAD_FORMAT");
			columnNames.add("UPLOAD_CHANNEL");
			columnNames.add("UPLOAD_FORMAT");
			columnNames.add("SR_NO");

		} catch (Exception e) {
			LOGGER.error("Exception : ", e);
		}
		return columnNames;
	}

	public Set<String> listOfDim(String eleCode) throws SQLException {
		Set<String> dimensionListData = null;
		try {
			if (dimMapData.containsKey(eleCode)) {
				dimensionListData = new HashSet<String>();
				dimensionListData = dimMapData.get(eleCode);
				return dimensionListData;
			} else {
				String tableName = ExcelContstant.MAP_EBR_RBR_.getExcelColumnConstants() + eleCode + ExcelContstant._BUSINESS_METADATA.getExcelColumnConstants();
				String query = "SELECT * FROM " + tableName + " LIMIT 1";
				dimensionListData = new HashSet<>();

				Connection connection = datasource.getConnection();
				Statement statement = connection.createStatement();
				ResultSet result = statement.executeQuery(query);

				//rs = cp.getResultSet(query);
				ResultSetMetaData rsmd = (ResultSetMetaData) result.getMetaData();
				int columnCount = rsmd.getColumnCount();
				// The column count starts from 1
				for (int i = 1; i <= columnCount; i++) {
					String name = rsmd.getColumnName(i);
					if (name.contains("ELEMENT_")) {

						if (name.equals(ExcelContstant.ELEMENT_CODE.getExcelColumnConstants())) {
							continue;
						} else if (name.equals(ExcelContstant.ELEMENT_VERSION.getExcelColumnConstants())) {
							continue;
						} else if (name.equals(ExcelContstant.ELEMENT_LABEL.getExcelColumnConstants())) {
							continue;
						} else if (name.equals(ExcelContstant.ELEMENT_DIMENSIONS.getExcelColumnConstants())) {
							continue;
						} else if (name.equals(ExcelContstant.ELEMENT_SDMX_MODEL_DIM.getExcelColumnConstants())) {
							continue;
						} else if (name.equals(ExcelContstant.ELEMENT_RETURN_TEMPLATE_SHEET_NO.getExcelColumnConstants())) {
							continue;
						} else if (name.equals(ExcelContstant.ELEMENT_FREQ.getExcelColumnConstants())) {
							continue;
						} else if (name.equals(ExcelContstant.ELEMENT_DEPENDENCY_TYPE.getExcelColumnConstants())) {
							continue;
						} else if (name.equals(ExcelContstant.ELEMENT_TIME_PERIOD.getExcelColumnConstants())) {
							continue;
						} else if (name.equals(ExcelContstant.ELEMENT_OBS_VALUE.getExcelColumnConstants())) {
							continue;
						} else if (name.equals(ExcelContstant.ELEMENT_DMID.getExcelColumnConstants())) {
							continue;
						} else if (name.equals(ExcelContstant.ELEMENT_CURRENCY.getExcelColumnConstants())) {
							continue;
						}

						if (!dimensionListData.contains(name)) {
							String[] dimArraySplit = name.split("ELEMENT_");
							dimensionListData.add(dimArraySplit[1]);
						}
					}
				}
				dimMapData.put(eleCode, dimensionListData);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return dimensionListData;

	}

}
