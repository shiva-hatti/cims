package com.iris.sdmx.sdmxDataModelCodesDownloadService;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iris.caching.ObjectCache;
import com.iris.exception.ServiceException;
import com.iris.fileDataExtract.ExtractFileData;
import com.iris.model.UserMaster;
import com.iris.sdmx.bean.DataSet;
import com.iris.sdmx.bean.SDMXDocument;
import com.iris.sdmx.bean.StructureRef;
import com.iris.sdmx.element.bean.ElementListBean;
import com.iris.sdmx.element.bean.SdmxElementBean;
import com.iris.sdmx.elementdimensionmapping.bean.ElementDimensionBean;
import com.iris.sdmx.elementdimensionmapping.repo.ElementDimensionRepo;
import com.iris.sdmx.exceltohtml.bean.CodeListDimension;
import com.iris.sdmx.exceltohtml.bean.CommonDimension;
import com.iris.sdmx.exceltohtml.bean.InputDimension;
import com.iris.sdmx.exceltohtml.bean.ModelOtherDetails;
import com.iris.sdmx.exceltohtml.bean.SDMXReturnModelBean;
import com.iris.sdmx.exceltohtml.bean.SdmxReturnPreviewBean;
import com.iris.sdmx.exceltohtml.entity.SdmxModelCodesEntity;
import com.iris.sdmx.exceltohtml.entity.SdmxReturnPreviewEntity;
import com.iris.sdmx.exceltohtml.repo.SdmxModelCodesRepo;
import com.iris.sdmx.exceltohtml.repo.SdmxReturnPreviewRepo;
import com.iris.sdmx.sdmxDataModelCodesDownloadBean.SDMXElementDSDsJSON;
import com.iris.sdmx.sdmxDataModelCodesDownloadBean.SDMXElementDSDsJSONDimension;
import com.iris.sdmx.sdmxDataModelCodesDownloadBean.SdmxModelCodesDownloadBean;
import com.iris.sdmx.sdmxDataModelCodesDownloadBean.ZipUtils;
import com.iris.sdmx.sdmxDataModelCodesDownloadEntity.SdmxFilingDocumentsDownload;
import com.iris.sdmx.sdmxDataModelCodesDownloadRepo.SdmxFillingDocumentDownloadRepo;
import com.iris.sdmx.status.service.JsonToCsvProcessor;
import com.iris.service.GenericService;
import com.iris.util.JsonUtility;
import com.iris.util.ResourceUtil;
import com.iris.util.constant.GeneralConstants;

@Service
public class SdmxModelCodesDownloadService implements GenericService<SdmxReturnPreviewEntity, Long> {

	private static final Logger LOGGER = LogManager.getLogger(SdmxModelCodesDownloadService.class);

	@Autowired
	private SdmxReturnPreviewRepo sdmxReturnPreviewRepo;

	@Autowired
	private SdmxModelCodesRepo sdmxModelCodesRepo;

	@Autowired
	private ElementDimensionRepo elementDimensionRepo;

	@Autowired
	private SdmxFillingDocumentDownloadRepo sdmxFillingRepo;

	@Autowired
	private EntityManager entityManager;

	@Override
	public SdmxReturnPreviewEntity add(SdmxReturnPreviewEntity entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(SdmxReturnPreviewEntity entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SdmxReturnPreviewEntity> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SdmxReturnPreviewEntity getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxReturnPreviewEntity> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;

	}

	@Override
	public List<SdmxReturnPreviewEntity> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxReturnPreviewEntity> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxReturnPreviewEntity> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxReturnPreviewEntity> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(SdmxReturnPreviewEntity bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	public List<SdmxModelCodesDownloadBean> fetchPublishTemplate(Set<Long> returnIdList, String jobProcessId, boolean checkForDataFilteration) throws Exception {

		List<SdmxModelCodesDownloadBean> sdmxModelCodesDownloadBeans = new ArrayList<SdmxModelCodesDownloadBean>();
		LOGGER.debug("Fetch All the record by return id to display on return preview grid - START Job Processing ID : " + jobProcessId);
		List<SdmxReturnPreviewEntity> sdmxReturnPreviewEntities = sdmxReturnPreviewRepo.fetchPublishTemplate(returnIdList);

		LOGGER.debug("Fetch All the record by return id to display on return preview grid - END Job Processing ID : " + jobProcessId);
		if (!CollectionUtils.isEmpty(sdmxReturnPreviewEntities)) {
			LOGGER.debug("Fetch All Return which is Publish And Applicable to User  - " + sdmxReturnPreviewEntities.size() + " Job Processing ID : " + jobProcessId);
			Set<String> checkTemplateAvilable = new HashSet<>();

			for (SdmxReturnPreviewEntity sdmxReturnPreviewEntity : sdmxReturnPreviewEntities) {

				if (checkForDataFilteration) {
					if (!checkTemplateAvilable.contains(sdmxReturnPreviewEntity.getReturnTemplateIdFk().getReturnObj().getReturnName() + "(" + sdmxReturnPreviewEntity.getReturnTemplateIdFk().getReturnObj().getReturnCode() + ")")) {
						checkTemplateAvilable.add(sdmxReturnPreviewEntity.getReturnTemplateIdFk().getReturnObj().getReturnName() + "(" + sdmxReturnPreviewEntity.getReturnTemplateIdFk().getReturnObj().getReturnCode() + ")");
					} else {
						continue;
					}

				}
				SdmxModelCodesDownloadBean sdmxModelCodesDownloadBean = new SdmxModelCodesDownloadBean();
				sdmxModelCodesDownloadBean.setDataTemplateName(sdmxReturnPreviewEntity.getReturnTemplateIdFk().getReturnObj().getReturnName() + "(" + sdmxReturnPreviewEntity.getEbrVersion() + ")");

				sdmxModelCodesDownloadBean.setEbrVersion(sdmxReturnPreviewEntity.getEbrVersion());
				sdmxModelCodesDownloadBean.setTemplateID(sdmxReturnPreviewEntity.getReturnTemplateIdFk().getReturnTemplateId());
				sdmxModelCodesDownloadBean.setReturnName(sdmxReturnPreviewEntity.getReturnTemplateIdFk().getReturnObj().getReturnName());
				sdmxModelCodesDownloadBean.setReturnCode(sdmxReturnPreviewEntity.getReturnTemplateIdFk().getReturnObj().getReturnCode());
				sdmxModelCodesDownloadBean.setReturnPreviewTypeId(sdmxReturnPreviewEntity.getReturnPreviewTypeId());
				sdmxModelCodesDownloadBean.setTemplateNameWithoutVersion(sdmxReturnPreviewEntity.getReturnTemplateIdFk().getReturnObj().getReturnName() + " | " + ObjectCache.getLabelKeyValue("en", sdmxReturnPreviewEntity.getReturnTemplateIdFk().getReturnObj().getFrequency().getFrequencyName()) + " | " + sdmxReturnPreviewEntity.getReturnTemplateIdFk().getReturnObj().getReturnCode());
				sdmxModelCodesDownloadBean.setReturnID(sdmxReturnPreviewEntity.getReturnTemplateIdFk().getReturnObj().getReturnId());
				sdmxModelCodesDownloadBean.setRbrVersion(sdmxReturnPreviewEntity.getReturnTemplateIdFk().getVersionNumber());
				sdmxModelCodesDownloadBean.setAgencyID(sdmxReturnPreviewEntity.getAgencyMasterIdFk().getAgencyMasterId());
				sdmxModelCodesDownloadBeans.add(sdmxModelCodesDownloadBean);
			}

		}

		return sdmxModelCodesDownloadBeans;
	}

	public List<SdmxModelCodesDownloadBean> getAllElementAccordingDataTemplate(Long returnPreviewTypeId, String jobProcessId) throws Exception {

		List<SdmxModelCodesDownloadBean> sdmxModelCodesDownloadBeans = new ArrayList<>();
		SdmxModelCodesDownloadBean sdmxModelCodesDownloadBean = new SdmxModelCodesDownloadBean();
		LOGGER.debug("Fetch All Element According To the Return data template  - START Job Processing ID : " + jobProcessId);

		Map<String, SdmxElementBean> independentElementDetails = getIndependentElementDetails(returnPreviewTypeId);
		Map<String, SdmxElementBean> dependentElementDetails = getDependentElementDetails(returnPreviewTypeId);

		List<SdmxElementBean> independentElementList = new ArrayList<SdmxElementBean>();
		List<SdmxElementBean> dependentElementList = new ArrayList<SdmxElementBean>();

		for (Entry<String, SdmxElementBean> entry : independentElementDetails.entrySet()) {
			SdmxElementBean sdmxElementObj = new SdmxElementBean();
			SdmxElementBean sdmxElementBean = entry.getValue();
			sdmxElementObj.setDsdCode(sdmxElementBean.getDsdCode());
			sdmxElementObj.setElementVer(sdmxElementBean.getElementVer());
			independentElementList.add(sdmxElementObj);
		}

		for (Entry<String, SdmxElementBean> entry : dependentElementDetails.entrySet()) {
			boolean found = false;
			dependentLoop: for (Entry<String, SdmxElementBean> entry1 : independentElementDetails.entrySet()) {
				if (entry.getKey().equals(entry1.getKey())) {
					found = true;
					break dependentLoop;
				}
			}

			if (!found) {
				SdmxElementBean sdmxElementObj = new SdmxElementBean();
				SdmxElementBean sdmxElementBean = entry.getValue();
				sdmxElementObj.setDsdCode(sdmxElementBean.getDsdCode());
				sdmxElementObj.setElementVer(sdmxElementBean.getElementVer());
				dependentElementList.add(sdmxElementObj);
			}
		}

		sdmxModelCodesDownloadBean.setIndependentElementList(independentElementList);
		sdmxModelCodesDownloadBean.setDependentElementList(dependentElementList);
		sdmxModelCodesDownloadBeans.add(sdmxModelCodesDownloadBean);
		return sdmxModelCodesDownloadBeans;
	}

	public Map<String, SdmxElementBean> getIndependentElementDetails(Long returnPreviewTypeId) {

		StringBuilder stringBuilder = new StringBuilder("select distinct(ele.DSD_CODE), ele.ELEMENT_VER from  TBL_SDMX_RETURN_PREVIEW preview, TBL_SDMX_RETURN_SHEET_INFO sheetInfo, " + "TBL_SDMX_RETURN_MODEL_INFO modelInfo, TBL_SDMX_MODEL_CODES modelCode, TBL_SDMX_ELEMENT ele " + "where preview.RETURN_PREVIEW_TYPE_ID = " + returnPreviewTypeId + " and ele.IS_ACTIVE = 1 and preview.IS_ACTIVE = 1 " + "and modelInfo.IS_ACTIVE = 1 and modelCode.IS_ACTIVE = 1 and JSON_SEARCH(modelCode.MODEL_DIM -> '$.modelOtherDetails.dependencyType', 'one','INDEPENDENT')  and preview.RETURN_PREVIEW_TYPE_ID = sheetInfo.RETURN_PREVIEW_ID_FK " + "and sheetInfo.RETURN_SHEET_INFO_ID = modelInfo.RETURN_SHEET_INFO_ID_FK " + "and modelInfo.MODEL_CODES_ID_FK = modelCode.MODEL_CODES_ID and modelCode.ELEMENT_ID_FK = ele.ELEMENT_ID ");

		List<Tuple> tuples = entityManager.createNativeQuery(stringBuilder.toString(), Tuple.class).getResultList();

		Map<String, SdmxElementBean> sdmxElementBeansMap = new HashMap<>();

		for (Tuple tuple : tuples) {
			SdmxElementBean sdmxElementBean = new SdmxElementBean();
			sdmxElementBean.setDsdCode(tuple.get("DSD_CODE").toString());
			sdmxElementBean.setElementVer(tuple.get("ELEMENT_VER").toString());
			sdmxElementBeansMap.put(tuple.get("DSD_CODE").toString(), sdmxElementBean);
		}

		return sdmxElementBeansMap;
	}

	public Map<String, SdmxElementBean> getDependentElementDetails(Long returnPreviewTypeId) {
		StringBuilder stringBuilder = new StringBuilder("select distinct(ele.DSD_CODE), ele.ELEMENT_VER from  TBL_SDMX_RETURN_PREVIEW preview, TBL_SDMX_RETURN_SHEET_INFO sheetInfo, " + "TBL_SDMX_RETURN_MODEL_INFO modelInfo, TBL_SDMX_MODEL_CODES modelCode, TBL_SDMX_ELEMENT ele " + "where preview.RETURN_PREVIEW_TYPE_ID = " + returnPreviewTypeId + " and ele.IS_ACTIVE = 1 and preview.IS_ACTIVE = 1 " + "and modelInfo.IS_ACTIVE = 1 and modelCode.IS_ACTIVE = 1 and JSON_SEARCH(modelCode.MODEL_DIM -> '$.modelOtherDetails.dependencyType', 'one','DEPENDENT')  and preview.RETURN_PREVIEW_TYPE_ID = sheetInfo.RETURN_PREVIEW_ID_FK " + "and sheetInfo.RETURN_SHEET_INFO_ID = modelInfo.RETURN_SHEET_INFO_ID_FK " + "and modelInfo.MODEL_CODES_ID_FK = modelCode.MODEL_CODES_ID and modelCode.ELEMENT_ID_FK = ele.ELEMENT_ID ");

		List<Tuple> tuples = entityManager.createNativeQuery(stringBuilder.toString(), Tuple.class).getResultList();

		Map<String, SdmxElementBean> sdmxElementBeansMap = new HashMap<>();

		for (Tuple tuple : tuples) {
			SdmxElementBean sdmxElementBean = new SdmxElementBean();
			sdmxElementBean.setDsdCode(tuple.get("DSD_CODE").toString());
			sdmxElementBean.setElementVer(tuple.get("ELEMENT_VER").toString());
			sdmxElementBeansMap.put(tuple.get("DSD_CODE").toString(), sdmxElementBean);
		}

		return sdmxElementBeansMap;
	}

	public Map<String, SdmxModelCodesDownloadBean> getIndependentDataTemplateList(Long elementId) {

		StringBuilder stringBuilder = new StringBuilder("select distinct ret.RETURN_CODE, preview.EBR_VERSION , ret.RETURN_NAME  from  TBL_SDMX_RETURN_PREVIEW preview, TBL_SDMX_RETURN_SHEET_INFO sheetInfo,  " + " TBL_SDMX_RETURN_MODEL_INFO modelInfo, TBL_SDMX_MODEL_CODES modelCode, TBL_RETURN ret, TBL_RETURN_TEMPLATE temp " + " where modelCode.ELEMENT_ID_FK = " + elementId + " and ret.IS_ACTIVE = 1 and preview.IS_ACTIVE = 1  " + " and modelInfo.IS_ACTIVE = 1 and modelCode.IS_ACTIVE = 1 and JSON_SEARCH(modelCode.MODEL_DIM -> '$.modelOtherDetails.dependencyType', 'one','INDEPENDENT') " + " and preview.RETURN_PREVIEW_TYPE_ID = sheetInfo.RETURN_PREVIEW_ID_FK  " + " and sheetInfo.RETURN_SHEET_INFO_ID = modelInfo.RETURN_SHEET_INFO_ID_FK " + " and modelInfo.MODEL_CODES_ID_FK = modelCode.MODEL_CODES_ID " + " and ret.RETURN_ID = temp.RETURN_ID_FK and preview.RETURN_TEMPLATE_ID_FK = temp.RETURN_TEMPLATE_ID and preview.IS_PUBLISHED = 1 ");

		List<Tuple> tuples = entityManager.createNativeQuery(stringBuilder.toString(), Tuple.class).getResultList();

		Map<String, SdmxModelCodesDownloadBean> sdmxModelCodeDownloadMap = new HashMap<>();

		for (Tuple tuple : tuples) {
			SdmxModelCodesDownloadBean sdmxModelCodeDownloadBean = new SdmxModelCodesDownloadBean();
			sdmxModelCodeDownloadBean.setReturnCode(tuple.get("RETURN_CODE").toString());
			sdmxModelCodeDownloadBean.setEbrVersion(tuple.get("EBR_VERSION").toString());
			sdmxModelCodeDownloadBean.setReturnName(tuple.get("RETURN_NAME").toString());
			sdmxModelCodeDownloadMap.put(tuple.get("RETURN_CODE").toString() + "~" + tuple.get("EBR_VERSION").toString(), sdmxModelCodeDownloadBean);
		}

		return sdmxModelCodeDownloadMap;

	}

	public Map<String, SdmxModelCodesDownloadBean> getDependentDataTemplateList(Long elementId) {

		StringBuilder stringBuilder = new StringBuilder("select distinct ret.RETURN_CODE, preview.EBR_VERSION , ret.RETURN_NAME from  TBL_SDMX_RETURN_PREVIEW preview, TBL_SDMX_RETURN_SHEET_INFO sheetInfo,  " + " TBL_SDMX_RETURN_MODEL_INFO modelInfo, TBL_SDMX_MODEL_CODES modelCode, TBL_RETURN ret, TBL_RETURN_TEMPLATE temp " + " where modelCode.ELEMENT_ID_FK = " + elementId + " and ret.IS_ACTIVE = 1 and preview.IS_ACTIVE = 1  " + " and modelInfo.IS_ACTIVE = 1 and modelCode.IS_ACTIVE = 1 and JSON_SEARCH(modelCode.MODEL_DIM -> '$.modelOtherDetails.dependencyType', 'one','DEPENDENT') " + " and preview.RETURN_PREVIEW_TYPE_ID = sheetInfo.RETURN_PREVIEW_ID_FK  " + " and sheetInfo.RETURN_SHEET_INFO_ID = modelInfo.RETURN_SHEET_INFO_ID_FK " + " and modelInfo.MODEL_CODES_ID_FK = modelCode.MODEL_CODES_ID " + " and ret.RETURN_ID = temp.RETURN_ID_FK and preview.RETURN_TEMPLATE_ID_FK = temp.RETURN_TEMPLATE_ID and preview.IS_PUBLISHED = 1 ");

		List<Tuple> tuples = entityManager.createNativeQuery(stringBuilder.toString(), Tuple.class).getResultList();

		Map<String, SdmxModelCodesDownloadBean> sdmxModelCodeDownloadMap = new HashMap<>();

		for (Tuple tuple : tuples) {
			SdmxModelCodesDownloadBean sdmxModelCodeDownloadBean = new SdmxModelCodesDownloadBean();
			sdmxModelCodeDownloadBean.setReturnCode(tuple.get("RETURN_CODE").toString());
			sdmxModelCodeDownloadBean.setEbrVersion(tuple.get("EBR_VERSION").toString());
			sdmxModelCodeDownloadBean.setReturnName(tuple.get("RETURN_NAME").toString());
			sdmxModelCodeDownloadMap.put(tuple.get("RETURN_CODE").toString() + "~" + tuple.get("EBR_VERSION").toString(), sdmxModelCodeDownloadBean);
		}

		return sdmxModelCodeDownloadMap;
	}

	public List<SdmxModelCodesDownloadBean> getTemplateDetailsAccordingEle(SdmxModelCodesDownloadBean sdmxModelCodesDownloadBean, String jobProcessId) throws Exception {

		List<SdmxModelCodesDownloadBean> sdmxModelCodesDownloadBeans = new ArrayList<SdmxModelCodesDownloadBean>();
		SdmxModelCodesDownloadBean sdmxModelCodesDownloadObj = new SdmxModelCodesDownloadBean();
		LOGGER.debug("Fetch All Return Template According to Element And its Version - START Job Processing ID : " + jobProcessId);

		Map<String, SdmxModelCodesDownloadBean> independentTemtDetailsMap = getIndependentDataTemplateList(sdmxModelCodesDownloadBean.getElementId());
		Map<String, SdmxModelCodesDownloadBean> dependentTemtDetailsMap = getDependentDataTemplateList(sdmxModelCodesDownloadBean.getElementId());

		List<SdmxModelCodesDownloadBean> independentTemplateList = new ArrayList<SdmxModelCodesDownloadBean>();
		List<SdmxModelCodesDownloadBean> dependentTemplateList = new ArrayList<SdmxModelCodesDownloadBean>();

		if (independentTemtDetailsMap.size() > 0) {
			for (Entry<String, SdmxModelCodesDownloadBean> entry : independentTemtDetailsMap.entrySet()) {
				SdmxModelCodesDownloadBean sdmxModelCodesDownloadBeanObj = new SdmxModelCodesDownloadBean();
				SdmxModelCodesDownloadBean scdbObj = entry.getValue();
				sdmxModelCodesDownloadBeanObj.setReturnCode(scdbObj.getReturnCode());
				sdmxModelCodesDownloadBeanObj.setEbrVersion(scdbObj.getEbrVersion());
				sdmxModelCodesDownloadBeanObj.setReturnName(scdbObj.getReturnName());
				independentTemplateList.add(sdmxModelCodesDownloadBeanObj);
			}
		}

		if (dependentTemtDetailsMap.size() > 0) {
			for (Entry<String, SdmxModelCodesDownloadBean> entry : dependentTemtDetailsMap.entrySet()) {
				boolean found = false;
				dependentLoop: for (Entry<String, SdmxModelCodesDownloadBean> entry1 : independentTemtDetailsMap.entrySet()) {
					if (entry.getKey().equals(entry1.getKey())) {
						found = true;
						break dependentLoop;
					}
				}

				if (!found) {
					SdmxModelCodesDownloadBean sdmxTempObj = new SdmxModelCodesDownloadBean();
					SdmxModelCodesDownloadBean sdmxTemBean = entry.getValue();
					sdmxTempObj.setReturnCode(sdmxTemBean.getReturnCode());
					sdmxTempObj.setEbrVersion(sdmxTemBean.getEbrVersion());
					dependentTemplateList.add(sdmxTempObj);
				}
			}
		}

		sdmxModelCodesDownloadObj.setIndependentTemplateList(independentTemplateList);
		sdmxModelCodesDownloadObj.setDependentTemplateList(dependentTemplateList);
		sdmxModelCodesDownloadBeans.add(sdmxModelCodesDownloadObj);
		return sdmxModelCodesDownloadBeans;

	}

	public String generateJsonFile(SdmxModelCodesDownloadBean sdmxModelCodesDownloadBean, String jobProcessId, UserMaster userMaster) {
		LOGGER.debug("Generation of Json for Template View  - Start Job Processing ID : " + jobProcessId);
		File jsonFile = null;
		List<String> eleCodeWithVersionList = new ArrayList<String>();

		try {
			String directoryPath = ResourceUtil.getKeyValue("filepath.root") + File.separator + ResourceUtil.getKeyValue("returnTemplate.upload.path") + File.separator + sdmxModelCodesDownloadBean.getReturnCode() + File.separator + ResourceUtil.getKeyValue("filePath.SDMXFolder") + File.separator + sdmxModelCodesDownloadBean.getVersionNumber();

			SDMXDocument csvSdmxDocument = new SDMXDocument();

			for (ElementListBean sdmxElementBean : sdmxModelCodesDownloadBean.getSdmxElementList()) {
				ExtractFileData extractFileData = new ExtractFileData();
				SDMXDocument sdmxDocument = extractFileData.readSDMCSVDocument(directoryPath + File.separator + sdmxModelCodesDownloadBean.getReturnCode() + "_" + sdmxElementBean.getDsdCode() + "_" + sdmxElementBean.getElementVer() + ".csv", null);

				if (csvSdmxDocument.getDataSets() == null) {
					csvSdmxDocument.setDataSets(sdmxDocument.getDataSets());
				} else {
					csvSdmxDocument.getDataSets().addAll(sdmxDocument.getDataSets());
				}

				eleCodeWithVersionList.add(sdmxElementBean.getDsdCode() + "(" + sdmxElementBean.getElementVer() + ")");
			}

			String folderCreationPath = ResourceUtil.getKeyValue("filepath.root") + File.separator + ResourceUtil.getKeyValue("filepath.upload.temp") + File.separator + userMaster.getUserId();

			jsonFile = new File(folderCreationPath + File.separator + ResourceUtil.getKeyValue("file.ebr.document") + File.separator + sdmxModelCodesDownloadBean.getReturnCode() + ".json");

			if (!jsonFile.getParentFile().exists()) {
				jsonFile.getParentFile().mkdirs();
			}

			try (FileWriter writer = new FileWriter(jsonFile)) {
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				String jsonOutput = gson.toJson(csvSdmxDocument);
				//writer.write(JsonUtility.getGsonObject().toJson(csvSdmxDocument));
				writer.write(jsonOutput);
			} catch (Exception e) {
				LOGGER.error(e);
			}

			if (sdmxModelCodesDownloadBean != null) {
				Date createdOn = new Date();
				LOGGER.debug("User Audit Entry  Start    : ");
				SdmxFilingDocumentsDownload sdmxFilingDocumentsDownload = new SdmxFilingDocumentsDownload();
				sdmxFilingDocumentsDownload.setTypeOfRequest(GeneralConstants.TEMPLATE_VIEW_TYPE_OF_REQUEST.getConstantIntVal());
				String eleJsonStr = JSONArray.toJSONString(eleCodeWithVersionList);
				List<String> returnComb = new ArrayList<String>();
				returnComb.add(sdmxModelCodesDownloadBean.getReturnCode() + "(" + sdmxModelCodesDownloadBean.getVersionNumber() + ")");
				String retJsonStr = JSONArray.toJSONString(returnComb);
				sdmxFilingDocumentsDownload.setElementCombination(eleJsonStr);
				sdmxFilingDocumentsDownload.setReturnCombination(retJsonStr);
				sdmxFilingDocumentsDownload.setDocumentType(GeneralConstants.JSON.getConstantVal());
				sdmxFilingDocumentsDownload.setCreatedBy(userMaster);
				sdmxFilingDocumentsDownload.setCreatedOn(createdOn);
				sdmxFillingRepo.save(sdmxFilingDocumentsDownload);
				LOGGER.debug("User Audit Entry  Submitted With Data    : " + sdmxFilingDocumentsDownload);
			}

		} catch (Exception e) {
			LOGGER.error("Exception : ", e);
		}

		if (jsonFile != null) {
			LOGGER.debug("Json fro Template View  Creation Path :" + jsonFile.getPath());
			return jsonFile.getPath();
		}

		return null;

	}
	// This Code Is Use for Generate CSV While Publish return 

	public void generateCsvForTemplateNew(SdmxModelCodesDownloadBean sdmxModelCodesDownloadBean, String jobProcessId, UserMaster userMaster) throws Exception {
		String csvTemplateFolderPath = ResourceUtil.getKeyValue("filepath.root") + File.separator + ResourceUtil.getKeyValue("returnTemplate.upload.path") + File.separator + sdmxModelCodesDownloadBean.getReturnCode() + File.separator + ResourceUtil.getKeyValue("filePath.SDMXFolder") + File.separator + sdmxModelCodesDownloadBean.getVersionNumber();

		LOGGER.debug("Generation of CSV when Template Is Publish  - START Job Processing ID : " + jobProcessId);

		Map<Long, List<Map<String, String>>> cellRefMap = new HashMap<>();
		cellRefMap = getCellrefMap(sdmxModelCodesRepo.generateCsvForTemplateView(sdmxModelCodesDownloadBean.getReturnCode(), sdmxModelCodesDownloadBean.getReturnPreviewTypeId()));

		List<Long> elementIdList = new ArrayList<>(cellRefMap.keySet());
		List<ElementDimensionBean> elementDimensionBeans = elementDimensionRepo.findByComboUsingDsdCode(elementIdList);

		LOGGER.debug("Element Dimension Combo size   -  " + elementDimensionBeans.size());

		Gson gson = new Gson();
		SDMXElementDSDsJSON sdmxEleDSDs;
		List<SDMXElementDSDsJSONDimension> dimCombination;

		StringBuilder sbContent;
		String dsdId;
		List<String> headerList;
		List<String> contentList;
		List<Map<String, String>> innerCellRefList;
		File csvFile = null;
		for (ElementDimensionBean sed : elementDimensionBeans) {
			sdmxEleDSDs = gson.fromJson(sed.getAllApplicableDim(), SDMXElementDSDsJSON.class);
			dsdId = sdmxEleDSDs.getDsdId();
			if (cellRefMap.containsKey(sed.getElementId())) {
				contentList = new ArrayList<>();
				headerList = new ArrayList<>();

				headerList.add("DSDID");
				headerList.add("CELL_REF_NUMBER");
				//headerList.add("DMID");

				dimCombination = sdmxEleDSDs.getDimCombination();
				for (SDMXElementDSDsJSONDimension sedj : dimCombination) {
					if (!headerList.contains(sedj.getDimConceptId())) {
						headerList.add(sedj.getDimConceptId());
					}
				}

				innerCellRefList = cellRefMap.get(sed.getElementId());
				for (Map<String, String> innerCellRefMap : innerCellRefList) {
					sbContent = new StringBuilder();
					sbContent.append("RBI:" + dsdId + "(" + innerCellRefMap.get("elementVersion") + ")").append(",");
					sbContent.append(innerCellRefMap.get("CELL_REF_NUMBER"));
					//sbContent.append(innerCellRefMap.get("DMID"));
					for (String header : headerList) {
						if (header.equals("DSDID") || header.equals("CELL_REF_NUMBER") || header.equals("DMID")) {
							continue;
						}
						if (innerCellRefMap.containsKey(header)) {
							sbContent.append(",").append(innerCellRefMap.get(header));
						}
					}
					contentList.add(sbContent.toString());
				}

				contentList.add(0, String.join(",", headerList));

				csvFile = new File(csvTemplateFolderPath + File.separator + sdmxModelCodesDownloadBean.getReturnCode() + "_" + sed.getDsdCode() + "_" + sed.getElementVersion() + ".csv");

				LOGGER.debug("Generate Csv Path   -  " + csvFile);

				if (!csvFile.getParentFile().exists()) {
					csvFile.getParentFile().mkdirs();
				}

				try (FileWriter writer = new FileWriter(csvFile)) {
					for (String str : contentList) {
						writer.write(str + System.lineSeparator());
					}
				} catch (Exception e) {
					LOGGER.error(e);
				}
			}
		}
	}

	public Map<Long, List<Map<String, String>>> getCellrefMap(List<SdmxModelCodesDownloadBean> sdmxModelCodesDownloadBeans) throws Exception {
		Map<Long, List<Map<String, String>>> cellRefMap = new HashMap<>();
		LOGGER.debug("Get Cell ref And Dim Data   -  ");

		List<Map<String, String>> innerCellRefList;
		Map<String, String> innerCellRefMap;
		Gson gson = new Gson();
		SDMXReturnModelBean sdmxReturnModelBean;

		List<CodeListDimension> closedDim;
		List<CommonDimension> commonDimension;
		List<InputDimension> openDimension;
		ModelOtherDetails modelOtherDetails;
		String dmId;
		String modelDim;

		List<String> dmIdList = new ArrayList<>();

		boolean considerData;

		if (!CollectionUtils.isEmpty(sdmxModelCodesDownloadBeans)) {

			for (SdmxModelCodesDownloadBean sdmxModelCodesDownloadBean : sdmxModelCodesDownloadBeans) {

				dmId = sdmxModelCodesDownloadBean.getModelCode();

				if (!dmIdList.contains(dmId)) {
					dmIdList.add(dmId);
				} else {
					continue;
				}

				considerData = true;
				modelDim = sdmxModelCodesDownloadBean.getModelDim();

				sdmxReturnModelBean = gson.fromJson(modelDim, SDMXReturnModelBean.class);

				closedDim = sdmxReturnModelBean.getClosedDim();
				commonDimension = sdmxReturnModelBean.getCommonDimension();
				openDimension = sdmxReturnModelBean.getOpenDimension();
				modelOtherDetails = sdmxReturnModelBean.getModelOtherDetails();

				innerCellRefMap = new HashMap<>();

				if (closedDim != null && !closedDim.isEmpty()) {
					for (CodeListDimension cld : closedDim) {
						if (cld.getDimConceptId().equals("DEPENDENCY_TYPE") && cld.getClValueCode().equals("DEPENDENT")) {
							considerData = false;
							break;
						}
						innerCellRefMap.put(cld.getDimConceptId(), cld.getClValueCode());
					}
				}

				if (modelOtherDetails != null) {
					if (modelOtherDetails.getDependencyType().equals("DEPENDENT")) {
						considerData = false;
					}
				}

				if (!considerData) {
					continue;
				}

				if (commonDimension != null && !commonDimension.isEmpty()) {
					for (CommonDimension cd : commonDimension) {
						if (cd.getDimConceptId().equalsIgnoreCase("DMID")) {
							innerCellRefMap.put(cd.getDimConceptId(), dmId);
						} else {

							innerCellRefMap.put(cd.getDimConceptId(), " ");
						}
					}
				}

				if (openDimension != null && !openDimension.isEmpty()) {
					for (InputDimension id : openDimension) {

						innerCellRefMap.put(id.getDimConceptId(), id.getClValueCode());
					}
				}

				innerCellRefMap.put("elementVersion", sdmxReturnModelBean.getElementVersion());
				innerCellRefMap.put("CELL_REF_NUMBER", sdmxModelCodesDownloadBean.getReturnCellRef().toString());

				if (cellRefMap.containsKey(sdmxModelCodesDownloadBean.getElementId())) {
					innerCellRefList = cellRefMap.get(sdmxModelCodesDownloadBean.getElementId());
				} else {
					innerCellRefList = new ArrayList<>();
				}

				innerCellRefList.add(innerCellRefMap);
				cellRefMap.put(sdmxModelCodesDownloadBean.getElementId(), innerCellRefList);
			}

		}
		LOGGER.debug("Cell Ref With Unique DMID Combination Map   -  " + cellRefMap);
		return cellRefMap;
	}

	public String downloadCsvForTemplateView(SdmxModelCodesDownloadBean sdmxModelCodesDownloadBean, String jobProcessId, UserMaster userMaster) {

		LOGGER.debug("Get All Csv Start With Job Id    : " + jobProcessId);
		String csvTemplateFolderPath = ResourceUtil.getKeyValue("filepath.root") + File.separator + ResourceUtil.getKeyValue("returnTemplate.upload.path") + File.separator + sdmxModelCodesDownloadBean.getReturnCode() + File.separator + ResourceUtil.getKeyValue("filePath.SDMXFolder") + File.separator + sdmxModelCodesDownloadBean.getVersionNumber();

		List<String> eleCodeWithVersionList = new ArrayList<String>();

		String returnPath = null;
		try {

			Properties prop = ResourceUtil.getResourcePropertyFile();
			String returnTemplatePath;
			List<String> filePathList = new ArrayList<String>();

			if (!CollectionUtils.isEmpty(sdmxModelCodesDownloadBean.getSdmxElementList())) {
				for (ElementListBean ele : sdmxModelCodesDownloadBean.getSdmxElementList()) {
					returnTemplatePath = csvTemplateFolderPath + File.separator + sdmxModelCodesDownloadBean.getReturnCode() + "_" + ele.getDsdCode() + "_" + ele.getElementVer() + ".csv";

					File csvFile = new File(returnTemplatePath);
					if (csvFile.exists()) {
						filePathList.add(returnTemplatePath);
						eleCodeWithVersionList.add(ele.getDsdCode() + "(" + ele.getElementVer() + ")");
						LOGGER.debug("Path of  Csv    : " + returnTemplatePath);
					}

				}

			}

			String folderCreationPath = ResourceUtil.getKeyValue("filepath.root") + File.separator + ResourceUtil.getKeyValue("filepath.upload.temp") + File.separator + userMaster.getUserId();

			File file = null;
			ZipUtils zip = new ZipUtils();

			if (!new File(folderCreationPath).isDirectory()) {
				file = new File(folderCreationPath);
				file.mkdir();
			}

			if (!new File(folderCreationPath + File.separator + ResourceUtil.getKeyValue("file.ebr.document")).isDirectory()) {
				file = new File(folderCreationPath + File.separator + ResourceUtil.getKeyValue("file.ebr.document"));
				file.mkdir();
			}

			if (new File(folderCreationPath + File.separator + ResourceUtil.getKeyValue("file.ebr.document") + File.separator + sdmxModelCodesDownloadBean.getReturnCode() + ".zip").isDirectory()) {
				zip.zipIt(folderCreationPath + File.separator + ResourceUtil.getKeyValue("file.ebr.document") + sdmxModelCodesDownloadBean.getReturnCode() + ".zip", filePathList);
			} else {
				zip.zipIt(folderCreationPath + File.separator + ResourceUtil.getKeyValue("file.ebr.document") + File.separator + "Template_" + sdmxModelCodesDownloadBean.getReturnCode() + "_" + sdmxModelCodesDownloadBean.getVersionNumber() + ".zip", filePathList);
			}

			if (sdmxModelCodesDownloadBean != null) {
				Date createdOn = new Date();
				LOGGER.debug("User Audit Entry Start for download Csv in Template View     : ");
				SdmxFilingDocumentsDownload sdmxFilingDocumentsDownload = new SdmxFilingDocumentsDownload();
				sdmxFilingDocumentsDownload.setTypeOfRequest(GeneralConstants.TEMPLATE_VIEW_TYPE_OF_REQUEST.getConstantIntVal());
				String eleJsonStr = JSONArray.toJSONString(eleCodeWithVersionList);
				List<String> returnComb = new ArrayList<String>();
				returnComb.add(sdmxModelCodesDownloadBean.getReturnCode() + "(" + sdmxModelCodesDownloadBean.getVersionNumber() + ")");
				String retJsonStr = JSONArray.toJSONString(returnComb);
				sdmxFilingDocumentsDownload.setElementCombination(eleJsonStr);
				sdmxFilingDocumentsDownload.setReturnCombination(retJsonStr);
				sdmxFilingDocumentsDownload.setDocumentType(GeneralConstants.CSV.getConstantVal());
				sdmxFilingDocumentsDownload.setCreatedBy(userMaster);
				sdmxFilingDocumentsDownload.setCreatedOn(createdOn);
				sdmxFillingRepo.save(sdmxFilingDocumentsDownload);
				LOGGER.debug("User Audit Entry End With Data    : " + sdmxFilingDocumentsDownload);
			}

			returnPath = folderCreationPath + File.separator + ResourceUtil.getKeyValue("file.ebr.document") + File.separator + "Template_" + sdmxModelCodesDownloadBean.getReturnCode() + "_" + sdmxModelCodesDownloadBean.getVersionNumber() + ".zip";

		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("DownloadCsvError    : " + jobProcessId);
		}
		LOGGER.debug("Csv Generation Path for Download    : " + returnPath);
		return returnPath;
	}

	public String generateElementWiseJsonFile(SdmxModelCodesDownloadBean sdmxModelCodesDownloadBean, String jobProcessId, UserMaster userMaster) {

		LOGGER.debug("Generate Element Wise Json File Start With Job Id    : " + jobProcessId);

		File jsonFile = null;

		try {
			String directoryPath;

			ExtractFileData extractFileData = new ExtractFileData();

			Set<String> headerString = new LinkedHashSet<>();

			Set<Map<String, String>> valueMapSet = new LinkedHashSet<>();
			List<String> returnComb = new ArrayList<String>();

			for (SdmxReturnPreviewBean sdmxReturnPreviewBeanBean : sdmxModelCodesDownloadBean.getSdmxReturnPreviewBeans()) {
				directoryPath = ResourceUtil.getKeyValue("filepath.root") + File.separator + ResourceUtil.getKeyValue("returnTemplate.upload.path") + File.separator + sdmxReturnPreviewBeanBean.getReturnCode() + File.separator + ResourceUtil.getKeyValue("filePath.SDMXFolder") + File.separator + sdmxReturnPreviewBeanBean.getEbrVersion();

				File directoryFile = new File(directoryPath);

				if (directoryFile.exists()) {
					for (File file : new File(directoryPath).listFiles()) {
						if (file.getName().equalsIgnoreCase(sdmxReturnPreviewBeanBean.getReturnCode() + "_" + sdmxModelCodesDownloadBean.getElementCode() + "_" + sdmxModelCodesDownloadBean.getVersionNumber() + ".csv")) {
							SDMXDocument sdmxDocument = extractFileData.readSDMCSVDocument(file.getPath(), null);
							if (sdmxDocument.getDataSets() != null) {
								DataSet dataSet = sdmxDocument.getDataSets().get(0);
								if (dataSet.getData() != null) {
									for (Map<String, String> dataMap : dataSet.getData()) {
										if (headerString.isEmpty()) {
											headerString.addAll(dataMap.keySet());
											valueMapSet.add(dataMap);
										} else {
											if (headerString.size() != dataMap.keySet().size()) {
												// Syso : throw an error
											} else {
												valueMapSet.add(dataMap);
											}
										}
									}
								}
							}
						}
					}
				}

				returnComb.add(sdmxReturnPreviewBeanBean.getReturnCode() + "(" + sdmxReturnPreviewBeanBean.getEbrVersion() + ")");

			}

			List<DataSet> dataSetList = new ArrayList<>();

			DataSet dataSet = new DataSet();

			StructureRef structureRef = new StructureRef();
			structureRef.setCode(sdmxModelCodesDownloadBean.getElementCode());
			structureRef.setVersion(sdmxModelCodesDownloadBean.getVersionNumber());
			structureRef.setAgencyID("RBI");

			dataSet.setStructureRef(structureRef);
			dataSet.setData(new ArrayList<>(valueMapSet));
			dataSetList.add(dataSet);

			SDMXDocument csvSdmxDocument = new SDMXDocument();
			csvSdmxDocument.setDataSets(dataSetList);

			String folderCreationPath = ResourceUtil.getKeyValue("filepath.root") + File.separator + ResourceUtil.getKeyValue("filepath.upload.temp") + File.separator + userMaster.getUserId();

			jsonFile = new File(folderCreationPath + File.separator + ResourceUtil.getKeyValue("file.ebr.document") + File.separator + sdmxModelCodesDownloadBean.getElementCode() + "_" + sdmxModelCodesDownloadBean.getVersionNumber() + ".json");

			if (!jsonFile.getParentFile().exists()) {
				jsonFile.getParentFile().mkdirs();
			}

			try (FileWriter writer = new FileWriter(jsonFile)) {
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				String jsonOutput = gson.toJson(csvSdmxDocument);
				//writer.write(JsonUtility.getGsonObject().toJson(csvSdmxDocument));
				writer.write(jsonOutput);
			} catch (Exception e) {
				LOGGER.error(e);
			}

			if (sdmxModelCodesDownloadBean != null) {
				Date createdOn = new Date();
				LOGGER.debug("User Audit Entry Start for Element Wise Json     : ");
				SdmxFilingDocumentsDownload sdmxFilingDocumentsDownload = new SdmxFilingDocumentsDownload();

				sdmxFilingDocumentsDownload.setTypeOfRequest(GeneralConstants.ELEMENT_VIEW_TYPE_OF_REQUEST.getConstantIntVal());
				List<String> elementList = new ArrayList<String>();
				elementList.add(sdmxModelCodesDownloadBean.getElementCode() + "(" + sdmxModelCodesDownloadBean.getVersionNumber() + ")");
				String eleJsonStr = JSONArray.toJSONString(elementList);
				String retJsonStr = JSONArray.toJSONString(returnComb);
				sdmxFilingDocumentsDownload.setElementCombination(eleJsonStr);
				sdmxFilingDocumentsDownload.setReturnCombination(retJsonStr);
				sdmxFilingDocumentsDownload.setDocumentType(GeneralConstants.JSON.getConstantVal());
				sdmxFilingDocumentsDownload.setCreatedBy(userMaster);
				sdmxFilingDocumentsDownload.setCreatedOn(createdOn);
				sdmxFillingRepo.save(sdmxFilingDocumentsDownload);
				LOGGER.debug("User Audit Entry End With Data     : " + sdmxFilingDocumentsDownload);
			}
		} catch (Exception e) {
			LOGGER.error("Exception : ", e);
		}

		if (jsonFile != null) {
			LOGGER.debug("Json file Creation Path      : " + jsonFile.getPath());
			return jsonFile.getPath();
		}

		return null;
	}

	public String generateElementWiseCsvFile(SdmxModelCodesDownloadBean sdmxModelCodesDownloadBean, String jobProcessId, UserMaster userMaster) {
		LOGGER.debug("Generation Element Wise Csv File Start  With Job ID   : " + jobProcessId);
		String csvCreationPath = null;

		try {
			String directoryPath;

			ExtractFileData extractFileData = new ExtractFileData();

			Set<String> headerString = new LinkedHashSet<>();

			Set<Map<String, String>> valueMapSet = new LinkedHashSet<>();
			List<String> returnComb = new ArrayList<String>();

			for (SdmxReturnPreviewBean sdmxReturnPreviewBeanBean : sdmxModelCodesDownloadBean.getSdmxReturnPreviewBeans()) {
				directoryPath = ResourceUtil.getKeyValue("filepath.root") + File.separator + ResourceUtil.getKeyValue("returnTemplate.upload.path") + File.separator + sdmxReturnPreviewBeanBean.getReturnCode() + File.separator + ResourceUtil.getKeyValue("filePath.SDMXFolder") + File.separator + sdmxReturnPreviewBeanBean.getEbrVersion();

				File directoryFile = new File(directoryPath);

				if (directoryFile.exists()) {
					for (File file : new File(directoryPath).listFiles()) {
						if (file.getName().equalsIgnoreCase(sdmxReturnPreviewBeanBean.getReturnCode() + "_" + sdmxModelCodesDownloadBean.getElementCode() + "_" + sdmxModelCodesDownloadBean.getVersionNumber() + ".csv")) {
							SDMXDocument sdmxDocument = extractFileData.readSDMCSVDocument(file.getPath(), null);
							if (sdmxDocument.getDataSets() != null) {
								DataSet dataSet = sdmxDocument.getDataSets().get(0);
								if (dataSet.getData() != null) {
									for (Map<String, String> dataMap : dataSet.getData()) {
										if (headerString.isEmpty()) {
											headerString.addAll(dataMap.keySet());
											valueMapSet.add(dataMap);
										} else {
											if (headerString.size() != dataMap.keySet().size()) {
												// Syso : throw an error
											} else {
												valueMapSet.add(dataMap);
											}
										}
									}
								}
							}
						}
					}
				}

				returnComb.add(sdmxReturnPreviewBeanBean.getReturnCode() + "(" + sdmxReturnPreviewBeanBean.getEbrVersion() + ")");

			}

			List<DataSet> dataSetList = new ArrayList<>();

			DataSet dataSet = new DataSet();

			StructureRef structureRef = new StructureRef();
			structureRef.setCode(sdmxModelCodesDownloadBean.getElementCode());
			structureRef.setVersion(sdmxModelCodesDownloadBean.getVersionNumber());
			structureRef.setAgencyID("RBI");

			dataSet.setStructureRef(structureRef);
			dataSet.setData(new ArrayList<>(valueMapSet));
			dataSetList.add(dataSet);

			SDMXDocument csvSdmxDocument = new SDMXDocument();
			csvSdmxDocument.setDataSets(dataSetList);

			String jsonString = JsonUtility.getGsonObject().toJson(csvSdmxDocument.getDataSets());
			LOGGER.debug("Converted Json String   : " + jsonString);

			JsonToCsvProcessor jsonToCsvProcessor = new JsonToCsvProcessor();

			File file = null;

			String folderCreationPath = ResourceUtil.getKeyValue("filepath.root") + File.separator + ResourceUtil.getKeyValue("filepath.upload.temp") + File.separator + userMaster.getUserId();

			if (!new File(folderCreationPath).isDirectory()) {
				file = new File(folderCreationPath);
				file.mkdir();
			}

			if (!new File(folderCreationPath + File.separator + ResourceUtil.getKeyValue("file.ebr.document")).isDirectory()) {
				file = new File(folderCreationPath + File.separator + ResourceUtil.getKeyValue("file.ebr.document"));
				file.mkdir();
			}

			csvCreationPath = folderCreationPath + File.separator + ResourceUtil.getKeyValue("file.ebr.document");

			Map<String, String> elementCodeAndCSVFileMap = jsonToCsvProcessor.processJsonToCsvConversion(jsonString, csvCreationPath);
			if (elementCodeAndCSVFileMap.size() > 0) {
				for (Entry<String, String> entry : elementCodeAndCSVFileMap.entrySet()) {
					csvCreationPath = csvCreationPath + File.separator + entry.getValue();
				}
			}

			if (sdmxModelCodesDownloadBean != null) {
				Date createdOn = new Date();
				LOGGER.debug("User Audiit Entry Start for Element Wise Csv    : ");
				SdmxFilingDocumentsDownload sdmxFilingDocumentsDownload = new SdmxFilingDocumentsDownload();
				sdmxFilingDocumentsDownload.setTypeOfRequest(GeneralConstants.ELEMENT_VIEW_TYPE_OF_REQUEST.getConstantIntVal());
				List<String> elementList = new ArrayList<String>();
				elementList.add(sdmxModelCodesDownloadBean.getElementCode() + "(" + sdmxModelCodesDownloadBean.getVersionNumber() + ")");
				String eleJsonStr = JSONArray.toJSONString(elementList);
				String retJsonStr = JSONArray.toJSONString(returnComb);
				sdmxFilingDocumentsDownload.setElementCombination(eleJsonStr);
				sdmxFilingDocumentsDownload.setReturnCombination(retJsonStr);
				sdmxFilingDocumentsDownload.setDocumentType(GeneralConstants.CSV.getConstantVal());
				sdmxFilingDocumentsDownload.setCreatedBy(userMaster);
				sdmxFilingDocumentsDownload.setCreatedOn(createdOn);
				sdmxFillingRepo.save(sdmxFilingDocumentsDownload);
				LOGGER.debug("User Audiit Entry End for Element Wise Csv    : " + sdmxFilingDocumentsDownload);
			}
		} catch (Exception e) {
			LOGGER.error("Exception : ", e);
		}

		if (csvCreationPath != null) {
			return csvCreationPath;
		}

		return null;
	}

}
