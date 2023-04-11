/**
 * 
 */
package com.iris.sdmx.upload.bean;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.iris.caching.ObjectCache;
import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.FileDetailRequestDto;
import com.iris.dto.FileDetailsBeanLimitedField;
import com.iris.dto.FilingHistoryDto;
import com.iris.dto.SdmxWebserviceUrlDto;
import com.iris.dto.UserDto;
import com.iris.exception.ApplicationException;
import com.iris.fileDataExtract.ExtractFileData;
import com.iris.model.EntityBean;
import com.iris.model.FileDetails;
import com.iris.model.FilingStatus;
import com.iris.model.UploadChannel;
import com.iris.model.UserMaster;
import com.iris.model.UserRole;
import com.iris.repository.EntityRepo;
import com.iris.repository.FileDetailsRepo;
import com.iris.repository.UserMasterRepo;
import com.iris.sdmx.bean.DataSet;
import com.iris.sdmx.bean.SDMXDocument;
import com.iris.sdmx.element.bean.SdmxElementBean;
import com.iris.sdmx.exceltohtml.bean.DimensionCodeListValueBean;
import com.iris.sdmx.exceltohtml.bean.DimensionDetailCategories;
import com.iris.sdmx.exceltohtml.entity.SdmxEleDimTypeMapEntity;
import com.iris.sdmx.fusion.service.FusionApiService;
import com.iris.sdmx.fusion.util.FusionPropertiesConstant;
import com.iris.sdmx.status.service.JsonToCsvProcessor;
import com.iris.sdmx.upload.entity.ElementAudit;
import com.iris.sdmx.upload.helper.EbrFileAuditHelper;
import com.iris.sdmx.util.EcuConstants;
import com.iris.sdmx.util.RestClientResponse;
import com.iris.sdmx.util.SDMXMLDocumentReaderUtilityService;
import com.iris.sdmx.util.SDMXWebServiceConstant;
import com.iris.util.FileCheckSumUtility;
import com.iris.util.FileManager;
import com.iris.util.FileMimeType;
import com.iris.util.JsonUtility;
import com.iris.util.ResourceUtil;
import com.iris.util.XmlValidate;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.FilingStatusConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MetaDataCheckConstants;
import com.iris.webservices.client.CIMSRestWebserviceClient;

/**
 * @author apagaria
 *
 */
@Service
public class EBRFileUploadService {

	static final Logger LOGGER = LogManager.getLogger(EBRFileUploadService.class);

	@Autowired
	private FileDetailsRepo fileDetailsRepo;

	@Autowired
	private FusionApiService fusionApiService;

	@Autowired
	private EntityRepo entityRepo;

	@Autowired
	private UserMasterRepo userMasterRepo;

	@Autowired
	private SDMXMLDocumentReaderUtilityService sDMXMLDocumentReaderUtilityService;

	@Autowired
	private EntityManager em;

	//private static final String SUBMIT_DATA_TO_FUSION = "submitDataToFusion";

	@Transactional(rollbackFor = Exception.class)
	public boolean processDocument(EbrFileDetailsBean ebrFileAuditInputBean, Map<String, Map<Boolean, Set<String>>> fieldCheckListMap, String jobProcessingId) throws Exception {
		File file = new File(ebrFileAuditInputBean.getFilePath());

		// If file not exsit return with failed validation
		if (!file.exists()) {
			insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0793.toString(), fieldCheckListMap);
			return true;
		}

		ebrFileAuditInputBean.setFileName(file.getName());
		ebrFileAuditInputBean.setFileType(FilenameUtils.getExtension(file.getName()));
		ExtractFileData extractFileData = new ExtractFileData();

		SDMXDocument sdmxDocument = null;
		// Batch 1 validation
		boolean isValidationFailed = false;
		if (ebrFileAuditInputBean.getFileType().equalsIgnoreCase(GeneralConstants.CSV.getConstantVal()) || FilenameUtils.getExtension(file.getName()).equalsIgnoreCase(GeneralConstants.XML.getConstantVal()) || FilenameUtils.getExtension(file.getName()).equalsIgnoreCase(GeneralConstants.JSON.getConstantVal())) {
			insertValueIntoStatusMap(MetaDataCheckConstants.FILE_EXTENSION_CHECK.getConstantVal(), true, null, fieldCheckListMap);
		} else {
			isValidationFailed = true;
			insertValueIntoStatusMap(MetaDataCheckConstants.FILE_EXTENSION_CHECK.getConstantVal(), false, ErrorCode.E0664.toString(), fieldCheckListMap);
		}

		String[] fileTypeArrBasedOnStructure;
		if (ebrFileAuditInputBean.getFileType().equalsIgnoreCase(GeneralConstants.CSV.getConstantVal()) || ebrFileAuditInputBean.getFileType().equalsIgnoreCase(GeneralConstants.JSON.getConstantVal())) {
			fileTypeArrBasedOnStructure = FileMimeType.getFileMimeTypeForJsonAndCSVFile(file);
		} else {
			fileTypeArrBasedOnStructure = FileMimeType.getFileMimeType(file);
		}

		if (fileTypeArrBasedOnStructure[1].equalsIgnoreCase(GeneralConstants.CSVMIMETYPE.getConstantVal()) || fileTypeArrBasedOnStructure[1].equalsIgnoreCase(GeneralConstants.TXTMIMETYPE.getConstantVal()) || fileTypeArrBasedOnStructure[1].equalsIgnoreCase(GeneralConstants.XMLMIMETYPE.getConstantVal()) || fileTypeArrBasedOnStructure[1].equalsIgnoreCase(GeneralConstants.JSONMIMETYPE.getConstantVal())) {
			if (fileTypeArrBasedOnStructure[0].equalsIgnoreCase(ebrFileAuditInputBean.getFileType())) {
				ebrFileAuditInputBean.setFileMimeType(fileTypeArrBasedOnStructure[1]);
				ebrFileAuditInputBean.setFileType(ebrFileAuditInputBean.getFileType());
				insertValueIntoStatusMap(MetaDataCheckConstants.FILE_MIME_TYPE_CHECK.getConstantVal(), true, null, fieldCheckListMap);
			} else {
				isValidationFailed = true;
				insertValueIntoStatusMap(MetaDataCheckConstants.FILE_MIME_TYPE_CHECK.getConstantVal(), false, ErrorCode.E1149.toString(), fieldCheckListMap);
			}
		} else {
			isValidationFailed = true;
			insertValueIntoStatusMap(MetaDataCheckConstants.FILE_MIME_TYPE_CHECK.getConstantVal(), false, ErrorCode.E0797.toString(), fieldCheckListMap);
		}

		if (!isValidationFailed) {
			if (ebrFileAuditInputBean.getFileType().equalsIgnoreCase(GeneralConstants.XML.getConstantVal())) {
				XmlValidate xmlValidate = new XmlValidate();
				if (xmlValidate.isValidXmlDocument(ebrFileAuditInputBean.getFilePath())) {
					if (xmlValidate.checkXMLDocumentType(ebrFileAuditInputBean.getFilePath()).equalsIgnoreCase(GeneralConstants.SDMX.getConstantVal())) {
						insertValueIntoStatusMap(MetaDataCheckConstants.IS_VALID_XML.getConstantVal(), true, null, fieldCheckListMap);
						try {
							sdmxDocument = extractFileData.readSDMXMlDocument(ebrFileAuditInputBean.getFilePath());
						} catch (Exception e) {
							isValidationFailed = true;
							LOGGER.error("Exception while reading XML file for job processing Id : " + jobProcessingId, e);
							insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0659.toString(), fieldCheckListMap);
						}
					} else {
						isValidationFailed = true;
						insertValueIntoStatusMap(MetaDataCheckConstants.IS_VALID_XML.getConstantVal(), false, ErrorCode.E0297.toString(), fieldCheckListMap);
					}
				} else {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.IS_VALID_XML.getConstantVal(), false, ErrorCode.E0796.toString(), fieldCheckListMap);
				}
			} else if (ebrFileAuditInputBean.getFileType().equalsIgnoreCase(GeneralConstants.JSON.getConstantVal())) {
				try {
					sdmxDocument = extractFileData.readSDMXJSONDocument(ebrFileAuditInputBean.getFilePath());
					insertValueIntoStatusMap(MetaDataCheckConstants.IS_VALID_JSON.getConstantVal(), true, null, fieldCheckListMap);
				} catch (Exception e) {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.IS_VALID_JSON.getConstantVal(), false, ErrorCode.E0307.toString(), fieldCheckListMap);
					LOGGER.error("Exception while reading json file for job processing Id : " + jobProcessingId, e);
				}
			} else if (ebrFileAuditInputBean.getFileType().equalsIgnoreCase(GeneralConstants.CSV.getConstantVal())) {
				try {
					sdmxDocument = extractFileData.readSDMCSVDocument(ebrFileAuditInputBean.getFilePath(), null);
				} catch (Exception e) {
					isValidationFailed = true;
					LOGGER.error("Exception while reading csv file for job processing Id : " + jobProcessingId, e);
					insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0659.toString(), fieldCheckListMap);
				}
			} else {
				isValidationFailed = true;
				LOGGER.error("File Type Not Found in the system for job processing Id : " + jobProcessingId);
				insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0786.toString(), fieldCheckListMap);
			}
		}

		if (isValidationFailed) {
			return isValidationFailed;
		}

		// Batch 2 validation
		if (ebrFileAuditInputBean.getEntityCode() != null) {
			if (ebrFileAuditInputBean.getEntityCode().equalsIgnoreCase(sdmxDocument.getDataSets().get(0).getStructureRef().getSender())) {
				EntityBean entity = entityRepo.getDataByLangCodeAndIsActiveAndEntityCode(ebrFileAuditInputBean.getLangCode(), true, ebrFileAuditInputBean.getEntityCode());

				if (Objects.isNull(entity)) {
					insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_CHECK.getConstantVal(), false, ErrorCode.E0330.toString(), fieldCheckListMap);
					isValidationFailed = true;
				} else {
					insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_CHECK.getConstantVal(), true, null, fieldCheckListMap);
					ebrFileAuditInputBean.setIfscCode(entity.getIfscCode());
					ebrFileAuditInputBean.setEntityIdFk(entity.getEntityId());
				}
			} else {
				insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_CHECK.getConstantVal(), false, ErrorCode.E1161.toString(), fieldCheckListMap);
				isValidationFailed = true;
			}
		} else {
			insertValueIntoStatusMap(MetaDataCheckConstants.ENTITY_CHECK.getConstantVal(), false, ErrorCode.E0330.toString(), fieldCheckListMap);
			isValidationFailed = true;
		}

		if (isValidationFailed) {
			return isValidationFailed;
		}

		// Batch 3 validation
		List<UserDto> userDtoList = null;
		if (ebrFileAuditInputBean.getUserIdFk() != null) {
			userDtoList = userMasterRepo.getUserDetailForEbrFileUpload(ebrFileAuditInputBean.getUserIdFk(), ebrFileAuditInputBean.getRoleIdFk());

			if (userDtoList.isEmpty()) {
				isValidationFailed = true;
				insertValueIntoStatusMap(MetaDataCheckConstants.USER_CHECK.getConstantVal(), false, ErrorCode.E0638.toString(), fieldCheckListMap);
			} else {
				UserDto userDto = userDtoList.get(0);
				if (Objects.isNull(userDto.getEntityId()) || !userDto.getEntityId().equals(ebrFileAuditInputBean.getEntityIdFk())) {
					isValidationFailed = true;
					insertValueIntoStatusMap(MetaDataCheckConstants.USER_CHECK.getConstantVal(), false, ErrorCode.E0639.toString(), fieldCheckListMap);
				} else {
					insertValueIntoStatusMap(MetaDataCheckConstants.USER_CHECK.getConstantVal(), true, null, fieldCheckListMap);
				}
			}
		} else {
			isValidationFailed = true;
			insertValueIntoStatusMap(MetaDataCheckConstants.USER_CHECK.getConstantVal(), false, ErrorCode.E0638.toString(), fieldCheckListMap);
		}

		if (isValidationFailed) {
			return isValidationFailed;
		}

		// Batch 4 validation
		UserDto activityUserDto = userDtoList.stream().filter(f -> f.getActivityId().equals(GeneralConstants.UPLOAD_ACTIVITY_ID.getConstantLongVal())).findAny().orElse(null);

		if (Objects.isNull(activityUserDto)) {
			isValidationFailed = true;
			insertValueIntoStatusMap(MetaDataCheckConstants.ROLE_ACTIVITY_MAPP_CHECK.getConstantVal(), false, ErrorCode.E0728.toString(), fieldCheckListMap);
		} else {
			insertValueIntoStatusMap(MetaDataCheckConstants.ROLE_ACTIVITY_MAPP_CHECK.getConstantVal(), true, null, fieldCheckListMap);
		}

		if (isValidationFailed) {
			return isValidationFailed;
		}

		// Batch 5 validation
		if (ebrFileAuditInputBean.getFileType().equalsIgnoreCase(GeneralConstants.CSV.getConstantVal())) {
			return processCSVDocument(ebrFileAuditInputBean, sdmxDocument, fieldCheckListMap);
		} else if (ebrFileAuditInputBean.getFileType().equalsIgnoreCase(GeneralConstants.XML.getConstantVal())) {
			return processXMLDocument(ebrFileAuditInputBean, sdmxDocument, fieldCheckListMap);
		} else if (ebrFileAuditInputBean.getFileType().equalsIgnoreCase(GeneralConstants.JSON.getConstantVal())) {
			return processJSONDocument(ebrFileAuditInputBean, sdmxDocument, fieldCheckListMap);
		} else {
			LOGGER.error("File Type Not Found in the system for job processing Id : " + jobProcessingId);
			insertValueIntoStatusMap(MetaDataCheckConstants.TECHNICAL_ERROR_CHECK.getConstantVal(), false, ErrorCode.E0786.toString(), fieldCheckListMap);
			return true;
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public boolean processXMLDocument(EbrFileDetailsBean ebrFileAuditInputBean, SDMXDocument sdmxDocument, Map<String, Map<Boolean, Set<String>>> fieldCheckListMap) throws Exception {
		boolean isValidationFailed = false;
		Date date = new Date();
		Map<String, List<String>> dbDimCodeAndReturnListMap = new HashMap<>();

		Map<String, Map<String, List<String>>> dmIdMapForAllElement = new HashMap<>();
		isValidationFailed = sDMXMLDocumentReaderUtilityService.validateSdmxDocumentAndGetDmIds(sdmxDocument, ebrFileAuditInputBean, dmIdMapForAllElement, fieldCheckListMap, dbDimCodeAndReturnListMap);

		if (isValidationFailed) {
			return isValidationFailed;
		}

		Map<String, List<RepDateReturnDataPoint>> elementAndDateWiseJson = sDMXMLDocumentReaderUtilityService.generateDateWiseJsonModified(dmIdMapForAllElement, dbDimCodeAndReturnListMap);

		UserMaster uploadedBy = new UserMaster();
		uploadedBy.setUserId(ebrFileAuditInputBean.getUserIdFk());

		UserRole userRole = new UserRole();
		userRole.setUserRoleId(ebrFileAuditInputBean.getRoleIdFk());

		UploadChannel uploadChannel = new UploadChannel();
		uploadChannel.setUploadChannelId(ebrFileAuditInputBean.getUploadChannelIdFk());

		List<DataSet> dataSetList = sdmxDocument.getDataSets();
		Date currentDate = new Date();

		FileDetails fileDetails = null;

		if (ebrFileAuditInputBean.getEbrFileAuditId() != null) {
			fileDetails = fileDetailsRepo.getDataById(ebrFileAuditInputBean.getEbrFileAuditId());
		} else {
			fileDetails = new FileDetails();
			fileDetails.setFileName(ebrFileAuditInputBean.getUserSelectedFileName());
		}

		fileDetails.setElementCount(dataSetList.size());
		fileDetails.setIfscCode(ebrFileAuditInputBean.getIfscCode());
		fileDetails.setEntityCode(ebrFileAuditInputBean.getEntityCode());
		fileDetails.setUserMaster(uploadedBy);
		fileDetails.setUserRole(userRole);
		fileDetails.setUploadChannelIdFk(uploadChannel);

		FilingStatus filingStatus = new FilingStatus();
		if (fusionApiService.getFusionPropertyValue(FusionPropertiesConstant.SUBMIT_DATA_TO_FUSION.getConstant()).equalsIgnoreCase("true")) {
			LOGGER.debug(" SubmitDataToFusion = true ");
			filingStatus.setFilingStatusId(FilingStatusConstants.FILE_STATUS_META_DATA_VALIDATED_ID.getConstantIntVal());
		} else {
			LOGGER.debug(" SubmitDataToFusion = false ");
			filingStatus.setFilingStatusId(FilingStatusConstants.CSV_TO_XML_JSON_TO_XML_ID.getConstantIntVal());
		}

		fileDetails.setFilingStatus(filingStatus);
		fileDetails.setSystemModifiedFileName(getModifiedFileName(ebrFileAuditInputBean.getEntityIdFk(), ebrFileAuditInputBean.getEntityCode(), ebrFileAuditInputBean.getUserIdFk(), date) + "." + ebrFileAuditInputBean.getFileType());
		fileDetails.setFileType(ebrFileAuditInputBean.getFileType());
		fileDetails.setFileMimeType(ebrFileAuditInputBean.getFileMimeType());
		fileDetails.setSize(new File(ebrFileAuditInputBean.getFilePath()).length());
		fileDetails.setCreationDate(date);
		fileDetails.setEmailId(ebrFileAuditInputBean.getUserEmailId());
		fileDetails.setJsonFileName(getModifiedFileName(ebrFileAuditInputBean.getEntityIdFk(), ebrFileAuditInputBean.getEntityCode(), ebrFileAuditInputBean.getUserIdFk(), date) + "." + com.iris.util.constant.GeneralConstants.JSON.getConstantVal());
		fileDetails.setFileCheckSum(FileCheckSumUtility.calculateFileChecksum(ebrFileAuditInputBean.getFilePath()));
		fileDetails.setFileCopyingStartTime(currentDate);
		fileDetails.setFileCopyingEndTime(currentDate);
		fileDetails.setFileCreationTime(currentDate);
		fileDetails.setIsActive(true);
		fileDetails.setProcessingFlag(true);
		fileDetails.setEbrFiling(true);
		List<ElementAudit> elementAuditList = new ArrayList<>();

		// set Attached attribute, dataset_attributes and sender null
		sdmxDocument.getDataSets().forEach(f -> {
			f.setAttached_attributes(null);
			f.setDataset_attributes(null);
			f.getStructureRef().setSender(null);
		});
		// set Attached attribute, dataset_attributes and sender null

		String jsonString = JsonUtility.getGsonObject().toJson(sdmxDocument.getDataSets());

		String finalPath = getModifiedFilePathOfSDMXDocument(ebrFileAuditInputBean.getEntityIdFk(), ebrFileAuditInputBean.getEntityCode().trim(), ebrFileAuditInputBean.getUserIdFk(), ebrFileAuditInputBean.getFileType(), date);

		// Move Uploaded XML file to Instance folder
		pushSDMXFileToServer(ebrFileAuditInputBean, finalPath);

		ebrFileAuditInputBean.setFilePath(finalPath);

		createFileAndWriteContentToFile(jsonString, getModifiedFilePathOfSDMXDocument(ebrFileAuditInputBean.getEntityIdFk(), ebrFileAuditInputBean.getEntityCode(), ebrFileAuditInputBean.getUserIdFk(), "json", date));

		JsonToCsvProcessor jsonToCsvProcessor = new JsonToCsvProcessor();
		Map<String, String> elementCodeAndCSVFileMap = jsonToCsvProcessor.processJsonToCsvConversion(jsonString, getDirectoryPathOfSDMXDocument(ebrFileAuditInputBean.getEntityIdFk(), ebrFileAuditInputBean.getEntityCode(), ebrFileAuditInputBean.getUserIdFk(), date));

		for (DataSet dataSetObj : dataSetList) {
			ElementAudit elementAudit = new ElementAudit();

			elementAudit.setRowCount(dataSetObj.getData().size());
			elementAudit.setElementCode(dataSetObj.getStructureRef().getCode());
			elementAudit.setElementVersion(dataSetObj.getStructureRef().getVersion());
			elementAudit.setConvertedFileName(elementCodeAndCSVFileMap.get(dataSetObj.getStructureRef().getCode()));
			elementAudit.setIsRevised("0");
			if (elementAndDateWiseJson != null && elementAndDateWiseJson.get(dataSetObj.getStructureRef().getCode()) != null) {
				elementAudit.setEleReturnRef(JsonUtility.getGsonObject().toJson(elementAndDateWiseJson.get(dataSetObj.getStructureRef().getCode())));
			}
			elementAudit.setIsPushedToLnd("0");
			elementAudit.setValResult(null);
			elementAudit.setValStatus(0);

			FilingStatus elementFilingStatus = new FilingStatus();

			if (fusionApiService.getFusionPropertyValue(FusionPropertiesConstant.SUBMIT_DATA_TO_FUSION.getConstant()).equalsIgnoreCase("true")) {
				elementFilingStatus.setFilingStatusId(FilingStatusConstants.FILE_STATUS_META_DATA_VALIDATED_ID.getConstantIntVal());
			} else {
				elementFilingStatus.setFilingStatusId(FilingStatusConstants.CSV_TO_XML_JSON_TO_XML_ID.getConstantIntVal());
			}

			elementAudit.setStatus(elementFilingStatus);
			elementAudit.setFileDetails(fileDetails);
			elementAuditList.add(elementAudit);
		}
		fileDetails.setElementAudits(elementAuditList);

		fileDetailsRepo.save(fileDetails);

		fileDetails.setApplicationProcessId(generatedAppliationProcessId(uploadedBy.getUserName(), ebrFileAuditInputBean.getIfscCode(), fileDetails.getId()));
		fileDetailsRepo.save(fileDetails);
		return isValidationFailed;
	}

	@Transactional(rollbackFor = Exception.class)
	public boolean processJSONDocument(EbrFileDetailsBean ebrFileAuditInputBean, SDMXDocument sdmxDocument, Map<String, Map<Boolean, Set<String>>> fieldCheckListMap) throws Exception {
		boolean isValidationFailed = false;
		Date date = new Date();
		Map<String, List<String>> dbDimCodeAndReturnListMap = new HashMap<>();

		Map<String, Map<String, List<String>>> dmIdMapForAllElement = new HashMap<>();
		isValidationFailed = sDMXMLDocumentReaderUtilityService.validateSdmxDocumentAndGetDmIds(sdmxDocument, ebrFileAuditInputBean, dmIdMapForAllElement, fieldCheckListMap, dbDimCodeAndReturnListMap);

		if (isValidationFailed) {
			return isValidationFailed;
		}

		Map<String, List<RepDateReturnDataPoint>> elementAndDateWiseJson = sDMXMLDocumentReaderUtilityService.generateDateWiseJsonModified(dmIdMapForAllElement, dbDimCodeAndReturnListMap);

		UserMaster uploadedBy = new UserMaster();
		uploadedBy.setUserId(ebrFileAuditInputBean.getUserIdFk());

		UserRole userRole = new UserRole();
		userRole.setUserRoleId(ebrFileAuditInputBean.getRoleIdFk());

		UploadChannel uploadChannel = new UploadChannel();
		uploadChannel.setUploadChannelId(ebrFileAuditInputBean.getUploadChannelIdFk());

		List<DataSet> dataSetList = sdmxDocument.getDataSets();
		ebrFileAuditInputBean.setCreatedRecordDate(date);

		FileDetails fileDetails = null;

		if (ebrFileAuditInputBean.getEbrFileAuditId() != null) {
			fileDetails = fileDetailsRepo.getDataById(ebrFileAuditInputBean.getEbrFileAuditId());
		} else {
			fileDetails = new FileDetails();
			fileDetails.setFileName(ebrFileAuditInputBean.getUserSelectedFileName());
		}

		fileDetails.setElementCount(dataSetList.size());
		fileDetails.setIfscCode(ebrFileAuditInputBean.getIfscCode());
		fileDetails.setEntityCode(ebrFileAuditInputBean.getEntityCode());
		fileDetails.setUserMaster(uploadedBy);
		fileDetails.setUserRole(userRole);
		fileDetails.setUploadChannelIdFk(uploadChannel);

		FilingStatus filingStatus = new FilingStatus();

		if (fusionApiService.getFusionPropertyValue(FusionPropertiesConstant.SUBMIT_DATA_TO_FUSION.getConstant()).equalsIgnoreCase("true")) {
			filingStatus.setFilingStatusId(FilingStatusConstants.FILE_STATUS_META_DATA_VALIDATED_ID.getConstantIntVal());
		} else {
			filingStatus.setFilingStatusId(FilingStatusConstants.CSV_TO_XML_JSON_TO_XML_ID.getConstantIntVal());
		}

		fileDetails.setFilingStatus(filingStatus);
		fileDetails.setSystemModifiedFileName(getModifiedFileName(ebrFileAuditInputBean.getEntityIdFk(), ebrFileAuditInputBean.getEntityCode(), ebrFileAuditInputBean.getUserIdFk(), date) + "." + ebrFileAuditInputBean.getFileType());
		fileDetails.setFileType(ebrFileAuditInputBean.getFileType());
		fileDetails.setFileMimeType(ebrFileAuditInputBean.getFileMimeType());
		fileDetails.setSize(new File(ebrFileAuditInputBean.getFilePath()).length());
		fileDetails.setCreationDate(date);
		fileDetails.setEmailId(ebrFileAuditInputBean.getUserEmailId());
		fileDetails.setJsonFileName(getModifiedFileName(ebrFileAuditInputBean.getEntityIdFk(), ebrFileAuditInputBean.getEntityCode(), ebrFileAuditInputBean.getUserIdFk(), date) + "." + com.iris.util.constant.GeneralConstants.JSON.getConstantVal());
		fileDetails.setFileCheckSum(FileCheckSumUtility.calculateFileChecksum(ebrFileAuditInputBean.getFilePath()));
		fileDetails.setFileCopyingStartTime(date);
		fileDetails.setFileCopyingEndTime(date);
		fileDetails.setFileCreationTime(date);
		fileDetails.setIsActive(true);
		fileDetails.setProcessingFlag(true);
		fileDetails.setEbrFiling(true);
		List<ElementAudit> elementAuditList = new ArrayList<>();

		// set Attached attribute, dataset_attributes and sender null
		sdmxDocument.getDataSets().forEach(f -> {
			f.setAttached_attributes(null);
			f.setDataset_attributes(null);
			f.getStructureRef().setSender(null);
		});
		// set Attached attribute, dataset_attributes and sender null

		String jsonString = JsonUtility.getGsonObject().toJson(sdmxDocument.getDataSets());

		String finalPath = getModifiedFilePathOfSDMXDocument(ebrFileAuditInputBean.getEntityIdFk(), ebrFileAuditInputBean.getEntityCode().trim(), ebrFileAuditInputBean.getUserIdFk(), ebrFileAuditInputBean.getFileType(), date);

		// Move uploaded Json document to Instance doc folder
		pushSDMXFileToServer(ebrFileAuditInputBean, finalPath);

		ebrFileAuditInputBean.setFilePath(finalPath);

		JsonToCsvProcessor jsonToCsvProcessor = new JsonToCsvProcessor();
		Map<String, String> elementCodeAndCSVFileMap = jsonToCsvProcessor.processJsonToCsvConversion(jsonString, getDirectoryPathOfSDMXDocument(ebrFileAuditInputBean.getEntityIdFk(), ebrFileAuditInputBean.getEntityCode(), ebrFileAuditInputBean.getUserIdFk(), date));

		for (DataSet dataSetObj : dataSetList) {
			ElementAudit elementAudit = new ElementAudit();

			elementAudit.setRowCount(dataSetObj.getData().size());
			elementAudit.setElementCode(dataSetObj.getStructureRef().getCode());
			elementAudit.setElementVersion(dataSetObj.getStructureRef().getVersion());
			elementAudit.setConvertedFileName(elementCodeAndCSVFileMap.get(dataSetObj.getStructureRef().getCode()));
			elementAudit.setIsRevised("0");
			if (elementAndDateWiseJson != null && elementAndDateWiseJson.get(dataSetObj.getStructureRef().getCode()) != null) {
				elementAudit.setEleReturnRef(JsonUtility.getGsonObject().toJson(elementAndDateWiseJson.get(dataSetObj.getStructureRef().getCode())));
			}
			elementAudit.setIsPushedToLnd("0");
			elementAudit.setValResult(null);
			elementAudit.setValStatus(0);

			FilingStatus elementFilingStatus = new FilingStatus();

			if (fusionApiService.getFusionPropertyValue(FusionPropertiesConstant.SUBMIT_DATA_TO_FUSION.getConstant()).equalsIgnoreCase("true")) {
				elementFilingStatus.setFilingStatusId(FilingStatusConstants.FILE_STATUS_META_DATA_VALIDATED_ID.getConstantIntVal());
			} else {
				elementFilingStatus.setFilingStatusId(FilingStatusConstants.CSV_TO_XML_JSON_TO_XML_ID.getConstantIntVal());
			}

			elementAudit.setStatus(elementFilingStatus);
			elementAudit.setFileDetails(fileDetails);
			elementAuditList.add(elementAudit);
		}
		fileDetails.setElementAudits(elementAuditList);

		fileDetailsRepo.save(fileDetails);

		fileDetails.setApplicationProcessId(generatedAppliationProcessId(uploadedBy.getUserName(), ebrFileAuditInputBean.getIfscCode(), fileDetails.getId()));
		fileDetailsRepo.save(fileDetails);

		// Call API to Convert CSV document to XML
		try {
			callEcuApiToConvertCsvDocToXML(ebrFileAuditInputBean);
		} catch (ApplicationException e) {
			insertValueIntoStatusMap(MetaDataCheckConstants.JSON_TO_XML_CONVERSION_CHECK.getConstantVal(), false, "Technical Error occured while converting to XML", fieldCheckListMap);

		}

		return isValidationFailed;
	}

	@Transactional(rollbackFor = Exception.class)
	public boolean processCSVDocument(EbrFileDetailsBean ebrFileAuditInputBean, SDMXDocument sdmxDocument, Map<String, Map<Boolean, Set<String>>> fieldCheckListMap) throws Exception {
		boolean isValidationFailed = false;
		ebrFileAuditInputBean.setCreatedRecordDate(new Date());
		Map<String, List<String>> dbDimCodeAndReturnListMap = new HashMap<>();

		Map<String, Map<String, List<String>>> dmIdMapForAllElement = new HashMap<>();
		isValidationFailed = sDMXMLDocumentReaderUtilityService.validateSdmxDocumentAndGetDmIds(sdmxDocument, ebrFileAuditInputBean, dmIdMapForAllElement, fieldCheckListMap, dbDimCodeAndReturnListMap);

		if (isValidationFailed) {
			return isValidationFailed;
		}

		Map<String, List<RepDateReturnDataPoint>> elementAndDateWiseJson = sDMXMLDocumentReaderUtilityService.generateDateWiseJsonModified(dmIdMapForAllElement, dbDimCodeAndReturnListMap);

		UserMaster uploadedBy = new UserMaster();
		uploadedBy.setUserId(ebrFileAuditInputBean.getUserIdFk());

		UserRole userRole = new UserRole();
		userRole.setUserRoleId(ebrFileAuditInputBean.getRoleIdFk());

		UploadChannel uploadChannel = new UploadChannel();
		uploadChannel.setUploadChannelId(ebrFileAuditInputBean.getUploadChannelIdFk());
		ebrFileAuditInputBean.setEntityIdFk(ebrFileAuditInputBean.getEntityIdFk());

		List<DataSet> dataSetList = sdmxDocument.getDataSets();

		FileDetails fileDetails = null;

		if (ebrFileAuditInputBean.getEbrFileAuditId() != null) {
			fileDetails = fileDetailsRepo.getDataById(ebrFileAuditInputBean.getEbrFileAuditId());
		} else {
			fileDetails = new FileDetails();
			fileDetails.setFileName(ebrFileAuditInputBean.getUserSelectedFileName());
		}

		fileDetails.setElementCount(dataSetList.size());
		fileDetails.setIfscCode(ebrFileAuditInputBean.getIfscCode());
		fileDetails.setEntityCode(ebrFileAuditInputBean.getEntityCode());
		fileDetails.setUserMaster(uploadedBy);
		fileDetails.setUserRole(userRole);
		fileDetails.setUploadChannelIdFk(uploadChannel);

		FilingStatus filingStatus = new FilingStatus();

		if (fusionApiService.getFusionPropertyValue(FusionPropertiesConstant.SUBMIT_DATA_TO_FUSION.getConstant()).equalsIgnoreCase("true")) {
			filingStatus.setFilingStatusId(FilingStatusConstants.FILE_STATUS_META_DATA_VALIDATED_ID.getConstantIntVal());
		} else {
			filingStatus.setFilingStatusId(FilingStatusConstants.CSV_TO_XML_JSON_TO_XML_ID.getConstantIntVal());
		}

		fileDetails.setFilingStatus(filingStatus);
		fileDetails.setSystemModifiedFileName(getModifiedFileName(ebrFileAuditInputBean.getEntityIdFk(), ebrFileAuditInputBean.getEntityCode(), ebrFileAuditInputBean.getUserIdFk(), ebrFileAuditInputBean.getCreatedRecordDate()) + "." + ebrFileAuditInputBean.getFileType());
		fileDetails.setFileType(ebrFileAuditInputBean.getFileType());
		fileDetails.setFileMimeType(ebrFileAuditInputBean.getFileMimeType());
		fileDetails.setSize(new File(ebrFileAuditInputBean.getFilePath()).length());
		fileDetails.setCreationDate(ebrFileAuditInputBean.getCreatedRecordDate());
		fileDetails.setEmailId(ebrFileAuditInputBean.getUserEmailId());
		fileDetails.setJsonFileName(getModifiedFileName(ebrFileAuditInputBean.getEntityIdFk(), ebrFileAuditInputBean.getEntityCode(), ebrFileAuditInputBean.getUserIdFk(), ebrFileAuditInputBean.getCreatedRecordDate()) + "." + com.iris.util.constant.GeneralConstants.JSON.getConstantVal());
		fileDetails.setFileCheckSum(FileCheckSumUtility.calculateFileChecksum(ebrFileAuditInputBean.getFilePath()));
		fileDetails.setFileCopyingStartTime(ebrFileAuditInputBean.getCreatedRecordDate());
		fileDetails.setFileCopyingEndTime(ebrFileAuditInputBean.getCreatedRecordDate());
		fileDetails.setFileCreationTime(ebrFileAuditInputBean.getCreatedRecordDate());
		fileDetails.setIsActive(true);
		fileDetails.setProcessingFlag(true);
		fileDetails.setEbrFiling(true);
		List<ElementAudit> elementAuditList = new ArrayList<>();

		for (DataSet dataSetObj : dataSetList) {
			ElementAudit elementAudit = new ElementAudit();

			elementAudit.setRowCount(dataSetObj.getData().size());
			elementAudit.setElementCode(dataSetObj.getStructureRef().getCode());
			elementAudit.setElementVersion(dataSetObj.getStructureRef().getVersion());
			elementAudit.setConvertedFileName(null);
			elementAudit.setIsRevised("0");
			if (elementAndDateWiseJson != null && elementAndDateWiseJson.get(dataSetObj.getStructureRef().getCode()) != null) {
				elementAudit.setEleReturnRef(JsonUtility.getGsonObject().toJson(elementAndDateWiseJson.get(dataSetObj.getStructureRef().getCode())));
			}
			elementAudit.setIsPushedToLnd("0");
			elementAudit.setValResult(null);
			elementAudit.setValStatus(0);

			FilingStatus elementFilingStatus = new FilingStatus();

			if (fusionApiService.getFusionPropertyValue(FusionPropertiesConstant.SUBMIT_DATA_TO_FUSION.getConstant()).equalsIgnoreCase("true")) {
				elementFilingStatus.setFilingStatusId(FilingStatusConstants.FILE_STATUS_META_DATA_VALIDATED_ID.getConstantIntVal());
			} else {
				elementFilingStatus.setFilingStatusId(FilingStatusConstants.CSV_TO_XML_JSON_TO_XML_ID.getConstantIntVal());
			}

			elementAudit.setStatus(elementFilingStatus);

			elementAudit.setConvertedFileName(null);
			elementAudit.setFileDetails(fileDetails);
			elementAuditList.add(elementAudit);
		}
		fileDetails.setElementAudits(elementAuditList);

		fileDetailsRepo.save(fileDetails);

		String jsonString = JsonUtility.getGsonObject().toJson(sdmxDocument.getDataSets());

		String finalPathWhereUploadedFileNeedsToBeMoved = getModifiedFilePathOfSDMXDocument(ebrFileAuditInputBean.getEntityIdFk(), ebrFileAuditInputBean.getEntityCode().trim(), ebrFileAuditInputBean.getUserIdFk(), ebrFileAuditInputBean.getFileType(), ebrFileAuditInputBean.getCreatedRecordDate());

		// Move uploaded CSV file to Instance folder
		pushSDMXFileToServer(ebrFileAuditInputBean, finalPathWhereUploadedFileNeedsToBeMoved);

		ebrFileAuditInputBean.setFilePath(finalPathWhereUploadedFileNeedsToBeMoved);

		createFileAndWriteContentToFile(jsonString, getModifiedFilePathOfSDMXDocument(ebrFileAuditInputBean.getEntityIdFk(), ebrFileAuditInputBean.getEntityCode(), ebrFileAuditInputBean.getUserIdFk(), "json", ebrFileAuditInputBean.getCreatedRecordDate()));

		fileDetails.setApplicationProcessId(generatedAppliationProcessId(uploadedBy.getUserName(), ebrFileAuditInputBean.getIfscCode(), fileDetails.getId()));
		fileDetailsRepo.save(fileDetails);

		LOGGER.info("DATA SAVED DONE AITHING TRANSACTION CALLING API TO CONVERT INTO XML");

		try {
			// Call API to Convert CSV document to XML
			callEcuApiToConvertCsvDocToXML(ebrFileAuditInputBean);
		} catch (ApplicationException e) {
			insertValueIntoStatusMap(MetaDataCheckConstants.CSV_TO_XML_CONVERSION_CHECK.getConstantVal(), false, "Technical Error occured while converting to XML", fieldCheckListMap);
		}

		return isValidationFailed;
	}

	private String generatedAppliationProcessId(String userName, String ifscCode, Long fileDetailsId) {
		String str = "";
		if (userName != null) {
			str = str + userName;
		} else {
			str = str + "NA";
		}
		if (ifscCode != null) {
			str = str + "-" + ifscCode;
		} else {
			str = str + "NA";
		}
		if (fileDetailsId != null) {
			str = str + "-" + fileDetailsId;
		} else {
			str = str + "NA";
		}
		return str;
	}

	public boolean pushSDMXFileToServer(EbrFileDetailsBean ebrFileAuditInputBean, String finalPath) throws ApplicationException {
		if (new File(ebrFileAuditInputBean.getFilePath()).isFile()) {
			FileManager fileManageObj = new FileManager();
			if (!fileManageObj.copyFile(new File(ebrFileAuditInputBean.getFilePath()), new File(finalPath))) {
				throw new ApplicationException("E0001", "File Not Copied");
			}
		}
		return true;
	}

	public boolean moveSDMXFileToServer(EbrFileDetailsBean ebrFileAuditInputBean, String finalPath) throws ApplicationException {
		if (new File(ebrFileAuditInputBean.getFilePath()).isFile()) {
			if (!FileManager.moveFile(new File(ebrFileAuditInputBean.getFilePath()), new File(finalPath))) {
				throw new ApplicationException("E0001", "File Not Moved");
			}
		}
		return true;
	}

	/**
	 * @param status
	 * @return
	 */
	public List<FileDetailsBeanLimitedField> fetchSdmxAuditRecords(Integer statusId, String jobProcessingId, Integer recordToProcess) {
		LOGGER.debug("Start fetchSdmxAuditRecords with job processing id - " + jobProcessingId);
		List<FileDetailsBeanLimitedField> fileDetailsBeanLimitedFieldList = null;
		// Filing Status
		FilingStatus status = new FilingStatus(statusId);
		Pageable pageable = PageRequest.of(0, recordToProcess);
		List<FileDetails> fileDetailsList = fileDetailsRepo.fetchSdmxAuditRecords(status, pageable);

		if (!CollectionUtils.isEmpty(fileDetailsList)) {
			LOGGER.debug("fetchSdmxAuditRecords with job processing id - " + jobProcessingId + " ,EBR file audit list size - " + fileDetailsList.size());
			fileDetailsBeanLimitedFieldList = new ArrayList<>();
			for (FileDetails fileDetails : fileDetailsList) {
				FileDetailsBeanLimitedField fileDetailsBeanLimitedField = new FileDetailsBeanLimitedField();
				EbrFileAuditHelper.convertEbrFileAuditEntityToBean(fileDetails, fileDetailsBeanLimitedField);
				fileDetailsBeanLimitedFieldList.add(fileDetailsBeanLimitedField);
			}
		} else {
			LOGGER.debug("fetchSdmxAuditRecords empty with job processing id - " + jobProcessingId);
		}
		LOGGER.debug("End fetchSdmxAuditRecords with job processing id - " + jobProcessingId);
		return fileDetailsBeanLimitedFieldList;
	}

	/**
	 * @param ebrFileAuditIds
	 * @param statusId
	 * @param jobProcessingId
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void updateStatusOfSdmxFileAuditRecords(List<Long> ebrFileAuditIds, Integer statusId, String jobProcessingId) {
		LOGGER.debug("Start updateStatusOfSdmxFileAuditRecords with job processing id - " + jobProcessingId);
		FilingStatus status = new FilingStatus(statusId);
		fileDetailsRepo.updateStatusOfSdmxFileAuditRecords(ebrFileAuditIds, status);
		LOGGER.debug("End updateStatusOfSdmxFileAuditRecords with job processing id - " + jobProcessingId);
	}

	@SuppressWarnings("unchecked")
	public List<FilingHistoryDto> getFilingHistoryDataForRBR(FileDetailRequestDto fileDetailRequestDto) {
		StringBuilder query = null;
		query = getBasicQueryForReturnFilingHistory(fileDetailRequestDto);

		boolean isReturnCodeAddedInQuery = false;

		if (!StringUtils.isEmpty(fileDetailRequestDto.getReturnCode())) {
			isReturnCodeAddedInQuery = true;
			List<String> returnCodeList = fileDetailRequestDto.getReturnCode().stream().map(String::toUpperCase).collect(Collectors.toList());
			query.append("and (UPPER(FD.returnCode) in ('" + String.join("','", returnCodeList) + "')");
		}

		if (fileDetailRequestDto.getUnMappedReturn().equals(Boolean.TRUE)) {
			query.append("or FD.returnCode not in (select returnCode from Return)");
			query.append("or FD.returnCode is NULL");
		}

		if (isReturnCodeAddedInQuery) {
			query.append(")");
		}

		query.append(" order by FD.creationDate desc");

		Query queryResult = em.createQuery(query.toString());
		return (List<FilingHistoryDto>) queryResult.getResultList();
	}

	private StringBuilder getBasicQueryForReturnFilingHistory(FileDetailRequestDto fileDetailRequestDto) {

		Set<Integer> fileDetailsFilingStatusId = null;

		Set<Integer> returnsUploadFilingStatusId = null;

		for (Integer integer : fileDetailRequestDto.getStatus()) {
			if (integer == 17 || integer == 18 || integer == 19) {
				if (fileDetailsFilingStatusId == null) {
					fileDetailsFilingStatusId = new HashSet<>();
				}
				fileDetailsFilingStatusId.add(integer);
			} else if (integer != 13) {
				if (fileDetailsFilingStatusId == null) {
					fileDetailsFilingStatusId = new HashSet<>();
				}
				if (returnsUploadFilingStatusId == null) {
					returnsUploadFilingStatusId = new HashSet<>();
				}
				returnsUploadFilingStatusId.add(integer);
				fileDetailsFilingStatusId.add(1);
			}
		}

		StringBuilder stringBuilder = new StringBuilder("select new com.iris.dto.FilingHistoryDto(FD.id, FSTATUS.status, RFSTATUS.status, UPCHANNEL.uploadChannelDesc, FD.returnCode, " + " RET.returnCode, RET.returnId, RET.returnName, FD.ifscCode, ENT.ifscCode, ENT.entityId, FD.entityCode, ENT.entityCode, " + " ENT.entityName, RD.startDate, RD.endDate, FD.frequencyIdFk.frequencyId, " + " FREQ.frequencyId, FREQ.frequencyCode, FREQ.frequencyName, FD.fileType, FD.fileName, RD.instanceFile, " + " FD.size, FD.reasonOfNotProcessed, RD.prevUploadId, RD.revisionRequestId.revisionRequestId, RD.unlockingReqId.unlockingReqId, RD.taxonomyId.returnTemplateId, " + " RD.returnPropertyValue.returnProprtyValId, RET_PROP_VAL.returnProValue,  RD.noOfErrors, RD.noOfWarnings, FD.fileMimeType, " + " FD.creationDate, RD.uploadId, FD.reportingPeriodStartDate," + " FD.reportingPeriodEndDate, FSTATUS.filingStatusId, RFSTATUS.filingStatusId, FD.supportiveDocName," + " FD.supportiveDocType, RD.attachedFile, USR.userName, FD.emailId) " + " FROM FileDetails FD " + " left join ReturnsUploadDetails RD  on FD.id = RD.fileDetailsBean.id  " + " left join Frequency FREQ on RD.frequency.frequencyId = FREQ.frequencyId " + " left join FilingStatus FSTATUS on FSTATUS.filingStatusId = FD.filingStatus.filingStatusId " + " left join FilingStatus RFSTATUS on RFSTATUS.filingStatusId = RD.filingStatus.filingStatusId " + " left join UploadChannel UPCHANNEL on UPCHANNEL.uploadChannelId = FD.uploadChannelIdFk.uploadChannelId " + " left join Return RET on RET.returnCode = FD.returnCode " + " left join ReturnPropertyValue RET_PROP_VAL on RET_PROP_VAL.returnProprtyValId = RD.returnPropertyValue.returnProprtyValId" + " left join EntityBean ENT on UPPER(ENT.ifscCode) = UPPER(FD.ifscCode)" + " left join UserMaster USR on USR.userId = FD.userMaster.userId" + " where UPCHANNEL.uploadChannelId IN " + "(" + org.apache.commons.lang3.StringUtils.join(fileDetailRequestDto.getUploadChannelList().toArray(), ",") + ")" + " and date(FD.creationDate) between date('" + DateManip.convertDateToString(new Date(fileDetailRequestDto.getStartDate()), DateConstants.YYYY_MM_DD.getDateConstants()) + "')" + " and date('" + DateManip.convertDateToString(new Date(fileDetailRequestDto.getEndDate()), DateConstants.YYYY_MM_DD.getDateConstants()) + "')" + " and FD.isEbrFiling = 0 " + " and (");

		if (fileDetailsFilingStatusId != null) {
			stringBuilder.append("FD.filingStatus.filingStatusId in (" + org.apache.commons.lang3.StringUtils.join(fileDetailsFilingStatusId.toArray(), ",") + ")");
		}

		if (returnsUploadFilingStatusId != null) {
			stringBuilder.append("OR RD.filingStatus.filingStatusId in (" + org.apache.commons.lang3.StringUtils.join(returnsUploadFilingStatusId.toArray(), ",") + ")");
		}

		stringBuilder.append(")");

		if (!StringUtils.isEmpty(fileDetailRequestDto.getIfscCode())) {
			List<String> ifscCodeList = fileDetailRequestDto.getIfscCode().stream().map(String::toUpperCase).collect(Collectors.toList());
			stringBuilder.append("and UPPER(FD.ifscCode) in ('" + String.join("','", ifscCodeList) + "')");
		}

		return stringBuilder;
	}

	@SuppressWarnings("unchecked")
	public List<FilingHistoryDto> getFilingHistoryDataForEbr(FileDetailRequestDto fileDetailRequestDto) {

		Set<Integer> returnsUploadFilingStatusId = new HashSet<>();
		;

		for (Integer integer : fileDetailRequestDto.getStatus()) {
			returnsUploadFilingStatusId.add(integer);
		}

		StringBuilder stringBuilder = new StringBuilder("select new com.iris.dto.FilingHistoryDto(RFSTATUS.status," + " RET.returnCode, RET.returnId, RET.returnName, ENT.ifscCode, ENT.entityId, ENT.entityCode, " + " ENT.entityName, RD.startDate, RD.endDate,  " + " FREQ.frequencyId, FREQ.frequencyCode, FREQ.frequencyName, RD.instanceFile, " + " RD.prevUploadId, RD.revisionRequestId.revisionRequestId, RD.unlockingReqId.unlockingReqId, RD.taxonomyId.returnTemplateId, " + " RD.returnPropertyValue.returnProprtyValId, RET_PROP_VAL.returnProValue,  RD.noOfErrors, RD.noOfWarnings,  " + " RD.uploadedDate, RD.uploadId, RFSTATUS.filingStatusId, RD.attachedFile, USR.userName, true) " + " FROM ReturnsUploadDetails RD " + " left join Frequency FREQ on RD.frequency.frequencyId = FREQ.frequencyId " + " left join FilingStatus RFSTATUS on RFSTATUS.filingStatusId = RD.filingStatus.filingStatusId " + " left join Return RET on RET.returnId = RD.returnObj.returnId " + " left join ReturnPropertyValue RET_PROP_VAL on RET_PROP_VAL.returnProprtyValId = RD.returnPropertyValue.returnProprtyValId" + " left join EntityBean ENT on ENT.entityId = RD.entity.entityId" + " left join UserMaster USR on USR.userId = RD.uploadedBy.userId" + " where date(RD.uploadedDate) between date('" + DateManip.convertDateToString(new Date(fileDetailRequestDto.getStartDate()), DateConstants.YYYY_MM_DD.getDateConstants()) + "')" + " and date('" + DateManip.convertDateToString(new Date(fileDetailRequestDto.getEndDate()), DateConstants.YYYY_MM_DD.getDateConstants()) + "')" + "and RD.filingStatus.filingStatusId in (" + org.apache.commons.lang3.StringUtils.join(returnsUploadFilingStatusId.toArray(), ",") + ")" + " and RD.sdmxEbrToRbrPreparation.ebrToRbrPreparationId is not NULL ");

		if (!StringUtils.isEmpty(fileDetailRequestDto.getIfscCode())) {
			List<String> ifscCodeList = fileDetailRequestDto.getIfscCode().stream().map(String::toUpperCase).collect(Collectors.toList());
			stringBuilder.append(" and UPPER(RD.entity.ifscCode) in ('" + String.join("','", ifscCodeList) + "')");
		}

		if (!StringUtils.isEmpty(fileDetailRequestDto.getReturnCode())) {
			List<String> returnCodeList = fileDetailRequestDto.getReturnCode().stream().map(String::toUpperCase).collect(Collectors.toList());
			stringBuilder.append(" and UPPER(RD.returnObj.returnCode) in ('" + String.join("','", returnCodeList) + "')");
		}

		stringBuilder.append(" order by RD.uploadedDate desc");

		Query queryResult = em.createQuery(stringBuilder.toString());
		return (List<FilingHistoryDto>) queryResult.getResultList();
	}

	public List<HashValueDimensionBean> getHashValueDimensionBean(List<SdmxElementBean> sdmxElementBeanList) {

		try {
			List<SdmxEleDimTypeMapEntity> sdmxEleDimTypeMapEntities = getElementDimTypeMap(sdmxElementBeanList);

			return preapreHashValueDimensionBean(sdmxEleDimTypeMapEntities);
		} catch (Exception e) {
			LOGGER.error("Exception while getting hash Value Dimension beans", e);
			return Collections.emptyList();
		}
	}

	public void insertValueIntoStatusMap(String key, Boolean b, String errorString, Map<String, Map<Boolean, Set<String>>> fieldCheckListMap) {
		if (fieldCheckListMap.get(key) != null) {
			Map<Boolean, Set<String>> map = fieldCheckListMap.get(key);
			if (map.get(b) != null) {
				map.get(b).add(errorString);
			} else {
				Set<String> errorList = new LinkedHashSet<>();
				errorList.add(errorString);
				map.put(b, errorList);
			}
		} else {
			Set<String> errorList = new LinkedHashSet<>();
			errorList.add(errorString);
			Map<Boolean, Set<String>> map = new LinkedHashMap<>();
			map.put(b, errorList);
			fieldCheckListMap.put(key, map);
		}
	}

	private String callEcuApiToConvertCsvDocToXML(EbrFileDetailsBean ebrFileAuditInputBean) throws Exception {
		CIMSRestWebserviceClient cimsRestWebserviceClient = new CIMSRestWebserviceClient();

		SdmxWebserviceUrlDto sdmxWebserviceUrlDto = (SdmxWebserviceUrlDto) ObjectCache.getSdmxServiceMap().get(SDMXWebServiceConstant.ECU_LOGIN.getConstantVal() + "~" + CIMSRestWebserviceClient.HTTP_METHOD_TYPE_POST);

		ebrFileAuditInputBean.setEntityIdFk(ebrFileAuditInputBean.getEntityIdFk());

		JSONObject jsonObject = new JSONObject();
		jsonObject.put(EcuConstants.AUTH_TOKEN_KEY.getConstant(), ResourceUtil.getKeyValue(EcuConstants.AUTH_TOKEN_KEY.getConstant()));
		jsonObject.put(EcuConstants.ACCESS_TOKEN.getConstant(), ResourceUtil.getKeyValue(EcuConstants.ACCESS_TOKEN.getConstant()));

		RestClientResponse restClientResponse = cimsRestWebserviceClient.callRestWebServiceWithMultipleHeaderAndGetResponse(sdmxWebserviceUrlDto, jsonObject.toString(), null, null);

		if (restClientResponse.getStatusCode() == 200) {
			String responseString = restClientResponse.getRestClientResponse();

			String accessToken = JsonUtility.extractResponseValueFromServiceResponseString(responseString, "access");

			if (accessToken != null) {

				if (ebrFileAuditInputBean.getFileType().equals(GeneralConstants.CSV.getConstantVal())) {
					sdmxWebserviceUrlDto = (SdmxWebserviceUrlDto) ObjectCache.getSdmxServiceMap().get(SDMXWebServiceConstant.CONVERT_CSV_TO_SDMX.getConstantVal() + "~" + CIMSRestWebserviceClient.HTTP_METHOD_TYPE_POST);
				} else {
					sdmxWebserviceUrlDto = (SdmxWebserviceUrlDto) ObjectCache.getSdmxServiceMap().get(SDMXWebServiceConstant.CONVERT_JSON_TO_SDMX.getConstantVal() + "~" + CIMSRestWebserviceClient.HTTP_METHOD_TYPE_POST);
				}

				Map<String, Object> formDataMap = new HashMap<>();
				formDataMap.put(EcuConstants.SDMX_TYPE.getConstant(), EcuConstants.STRUCTURE.getConstant());
				formDataMap.put(EcuConstants.SEMANTIC_VALIDATION.getConstant(), EcuConstants.TRUE.getConstant());
				formDataMap.put(EcuConstants.FILE.getConstant(), new File(ebrFileAuditInputBean.getFilePath()));
				formDataMap.put(EcuConstants.MULTIPLE.getConstant(), EcuConstants.FALSE.getConstant());
				formDataMap.put(EcuConstants.SENDER.getConstant(), ebrFileAuditInputBean.getEntityCode());

				Map<String, String> headerMap = new HashMap<>();
				headerMap.put(EcuConstants.AUTHORIZATION.getConstant(), EcuConstants.BEARER.getConstant() + " " + accessToken);

				restClientResponse = cimsRestWebserviceClient.callRestWebServiceWithMultipleHeaderAndFormData(sdmxWebserviceUrlDto, formDataMap, null, headerMap);

				if (restClientResponse.getStatusCode() == 200) {
					jsonObject = new JSONObject(restClientResponse.getRestClientResponse());

					String uid = jsonObject.getJSONObject(EcuConstants.RESULT.getConstant()).getString(EcuConstants.UID.getConstant());

					if (uid != null) {
						sdmxWebserviceUrlDto = (SdmxWebserviceUrlDto) ObjectCache.getSdmxServiceMap().get(SDMXWebServiceConstant.DOWNLOAD_SDMX_DOCUMENT.getConstantVal() + "~" + CIMSRestWebserviceClient.HTTP_METHOD_TYPE_GET);

						List<String> pathVariableList = new ArrayList<>();
						pathVariableList.add(uid);

						restClientResponse = cimsRestWebserviceClient.callRestWebServiceWithMultipleHeaderAndGetResponse(sdmxWebserviceUrlDto, null, pathVariableList, headerMap);

						if (restClientResponse.getStatusCode() == 200) {
							String fileContent = restClientResponse.getRestClientResponse();
							String filePath = getModifiedFilePathOfSDMXDocument(ebrFileAuditInputBean.getEntityIdFk(), ebrFileAuditInputBean.getEntityCode(), ebrFileAuditInputBean.getUserIdFk(), GeneralConstants.XML.getConstantVal(), ebrFileAuditInputBean.getCreatedRecordDate());
							createFileAndWriteContentToFile(fileContent, filePath);
							return filePath;
						} else {
							LOGGER.error("Issue occured while downloading xml document");
							throw new ApplicationException(restClientResponse.getStatusCode() + "", restClientResponse.getRestClientResponse());
						}
					}
				} else {
					LOGGER.error("Issue occured while converting into xml document");
					throw new ApplicationException(restClientResponse.getStatusCode() + "", restClientResponse.getRestClientResponse());
				}
			}
		} else {
			LOGGER.error("Issue occured while calling login API");
			throw new ApplicationException(restClientResponse.getStatusCode() + "", restClientResponse.getRestClientResponse());
		}
		return null;
	}

	private String getDirectoryPathOfSDMXDocument(Long entityId, String entityCode, Long userId, Date createdDate) {
		String currentDate = DateManip.convertDateToString(createdDate, DateConstants.DD_MM_YYYY.getDateConstants());

		String modifiedInstanceFilePath = ResourceUtil.getKeyValue("filepath.root") + ResourceUtil.getKeyValue("filepath.instanceEbr") + File.separator + entityCode.trim() + File.separator + currentDate;

		String modifiedInstanceFileName = getModifiedFileName(entityId, entityCode, userId, createdDate);

		return modifiedInstanceFilePath + File.separator + modifiedInstanceFileName;
	}

	private String getModifiedFileName(Long entityId, String entityCode, Long userId, Date createdDate) {

		String date = DateManip.convertDateToString(createdDate, DateConstants.DD_MM_YYYY.getDateConstants() + " " + "HH:mm:ss:SSS");
		date = date.replaceAll("[:]", "-");
		date = date.replaceAll("[ ]", "_");

		return entityId + "_" + entityCode.trim() + "_" + userId + "_" + date.trim();
	}

	private String getModifiedFilePathOfSDMXDocument(Long entityId, String entityCode, Long userId, String fileType, Date createdDate) {
		String currentDate = DateManip.convertDateToString(createdDate, DateConstants.DD_MM_YYYY.getDateConstants());

		String modifiedInstanceFilePath = ResourceUtil.getKeyValue("filepath.root") + ResourceUtil.getKeyValue("filepath.instanceEbr") + File.separator + entityCode.trim() + File.separator + currentDate;

		String modifiedInstanceFileName = getModifiedFileName(entityId, entityCode, userId, createdDate);

		return modifiedInstanceFilePath + File.separator + modifiedInstanceFileName + File.separator + modifiedInstanceFileName + "." + fileType;
	}

	private boolean createFileAndWriteContentToFile(String jsonString, String jsonFilePath) throws ApplicationException {

		try {
			File file = new File(jsonFilePath);
			File parentDir = file.getParentFile();
			if (!parentDir.exists()) {
				parentDir.mkdirs();
			}
			Files.write(Paths.get(jsonFilePath), jsonString.getBytes());
			return true;
		} catch (Exception e) {
			LOGGER.error("Exception occured while creating json file");
			throw new ApplicationException("E0001", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List<SdmxEleDimTypeMapEntity> getElementDimTypeMap(List<SdmxElementBean> sdmxElementBeanList) {

		StringBuilder stringBuilder = new StringBuilder("select new com.iris.sdmx.exceltohtml.entity.SdmxEleDimTypeMapEntity" + "(dsdCode, elementVer, dimCode, dimType, groupNum) " + " from SdmxEleDimTypeMapEntity where ");

		int i = 0;
		for (SdmxElementBean sdmxElementBean : sdmxElementBeanList) {
			if (i > 0) {
				stringBuilder.append(" or ");
			}
			stringBuilder.append("(dsdCode in ('" + sdmxElementBean.getDsdCode() + "') and elementVer in ('" + sdmxElementBean.getElementVer() + "'))");
			i++;
		}
		stringBuilder.append("order by dsdCode, elementVer, dimType, dimCode, groupNum asc");

		Query queryResult = em.createQuery(stringBuilder.toString());
		return (List<SdmxEleDimTypeMapEntity>) queryResult.getResultList();
	}

	private List<HashValueDimensionBean> preapreHashValueDimensionBean(List<SdmxEleDimTypeMapEntity> sdmXEleDimTypeMapEntities) {

		List<HashValueDimensionBean> hashValueDimensionBeans = new ArrayList<>();

		HashValueDimensionBean hashValueDimensionBean = null;
		for (SdmxEleDimTypeMapEntity sdmxEleDimTypeMapEntity : sdmXEleDimTypeMapEntities) {
			hashValueDimensionBean = new HashValueDimensionBean();
			hashValueDimensionBean.setDsdId(sdmxEleDimTypeMapEntity.getDsdCode());
			hashValueDimensionBean.setElementVersion(sdmxEleDimTypeMapEntity.getElementVer());

			if (hashValueDimensionBeans.contains(hashValueDimensionBean)) {
				hashValueDimensionBean = hashValueDimensionBeans.get(hashValueDimensionBeans.indexOf(hashValueDimensionBean));
			} else {
				hashValueDimensionBeans.add(hashValueDimensionBean);
			}

			DimensionDetailCategories dimensionDetailCategories = null;

			if (hashValueDimensionBean.getDimensionDetailsCategoriesGroupWise() != null) {

				dimensionDetailCategories = getDimensionDetailCategory(hashValueDimensionBean.getDimensionDetailsCategoriesGroupWise(), sdmxEleDimTypeMapEntity.getDsdCode(), sdmxEleDimTypeMapEntity.getElementVer(), sdmxEleDimTypeMapEntity.getGroupNum());

				if (dimensionDetailCategories != null) {
					getDimensionDetailCategory(sdmxEleDimTypeMapEntity, dimensionDetailCategories);
				} else {
					dimensionDetailCategories = new DimensionDetailCategories();
					dimensionDetailCategories.setGroupNo(sdmxEleDimTypeMapEntity.getGroupNum());
					dimensionDetailCategories.setDsdId(sdmxEleDimTypeMapEntity.getDsdCode());
					dimensionDetailCategories.setElementVersion(sdmxEleDimTypeMapEntity.getElementVer());

					hashValueDimensionBean.getDimensionDetailsCategoriesGroupWise().add(getDimensionDetailCategory(sdmxEleDimTypeMapEntity, dimensionDetailCategories));
				}
			} else {
				List<DimensionDetailCategories> dimensionDetailCategoriesList = new ArrayList<>();
				dimensionDetailCategories = new DimensionDetailCategories();
				dimensionDetailCategories.setGroupNo(sdmxEleDimTypeMapEntity.getGroupNum());
				dimensionDetailCategories.setDsdId(sdmxEleDimTypeMapEntity.getDsdCode());
				dimensionDetailCategories.setElementVersion(sdmxEleDimTypeMapEntity.getElementVer());
				dimensionDetailCategoriesList.add(getDimensionDetailCategory(sdmxEleDimTypeMapEntity, dimensionDetailCategories));

				hashValueDimensionBean.setDimensionDetailsCategoriesGroupWise(dimensionDetailCategoriesList);
			}
		}

		if (hashValueDimensionBeans.isEmpty()) {
			LOGGER.error("Returns empty data set from HASH generation table");
		}
		return hashValueDimensionBeans;

	}

	private DimensionDetailCategories getDimensionDetailCategory(List<DimensionDetailCategories> dimensionDetailsCategoriesGroupWise, String dsdCode, String elementVer, Integer groupNum) {

		if (dimensionDetailsCategoriesGroupWise != null) {
			for (DimensionDetailCategories dimensionDetailCategories : dimensionDetailsCategoriesGroupWise) {
				if (dimensionDetailCategories.getDsdId().equalsIgnoreCase(dsdCode) && dimensionDetailCategories.getElementVersion().equalsIgnoreCase(elementVer) && dimensionDetailCategories.getGroupNo().equals(groupNum)) {
					return dimensionDetailCategories;
				}
			}
		}
		return null;
	}

	private DimensionDetailCategories getDimensionDetailCategory(SdmxEleDimTypeMapEntity sdmxEleDimTypeMapEntity, DimensionDetailCategories dimensionDetailCategories) {

		// Repeat of if block
		DimensionCodeListValueBean dimensionCodeListValueBean = new DimensionCodeListValueBean();
		dimensionCodeListValueBean.setDimConceptId(sdmxEleDimTypeMapEntity.getDimCode());

		if (sdmxEleDimTypeMapEntity.getDimType().equalsIgnoreCase("OPEN")) {
			if (dimensionDetailCategories.getOpenDimension() != null) {
				dimensionDetailCategories.getOpenDimension().add(dimensionCodeListValueBean);
			} else {
				List<DimensionCodeListValueBean> dimensionCodeListValueBeans = new ArrayList<>();
				dimensionCodeListValueBeans.add(dimensionCodeListValueBean);
				dimensionDetailCategories.setOpenDimension(dimensionCodeListValueBeans);
			}
		} else {
			if (dimensionDetailCategories.getClosedDim() != null) {
				dimensionDetailCategories.getClosedDim().add(dimensionCodeListValueBean);
			} else {
				List<DimensionCodeListValueBean> dimensionCodeListValueBeans = new ArrayList<>();
				dimensionCodeListValueBeans.add(dimensionCodeListValueBean);
				dimensionDetailCategories.setClosedDim(dimensionCodeListValueBeans);
			}
		}

		return dimensionDetailCategories;
	}
}
