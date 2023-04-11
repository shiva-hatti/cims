package com.iris.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.iris.caching.ObjectCache;
import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.CsvReturnTempUploadDTO;
import com.iris.dto.ReturnTempUploadDTO;
import com.iris.dto.ReturnTemplateDto;
import com.iris.dto.SdmxReturnTempUploadDTO;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.dto.TaxonomyDTO;
import com.iris.dto.WebformReturnTempUploadDTO;
import com.iris.dto.XbrlReturnTempUploadDTO;
import com.iris.exception.ApplicationException;
import com.iris.formula.gen.FormulaBean;
import com.iris.model.Return;
import com.iris.model.ReturnLabel;
import com.iris.model.ReturnReturnTypeMapping;
import com.iris.model.ReturnTemplate;
import com.iris.model.ReturnType;
import com.iris.model.UserMaster;
import com.iris.repository.ReturnRepo;
import com.iris.repository.ReturnReturnTypeMappingRepo;
import com.iris.repository.ReturnTemplateRepository;
import com.iris.repository.ReturnTypeRepo;
import com.iris.repository.UserMasterRepo;
import com.iris.service.GenericService;
import com.iris.service.impl.ReturnTemplateService;
import com.iris.service.impl.WebFormVersionService;
import com.iris.util.FileManager;
import com.iris.util.ResourceUtil;
import com.iris.util.UtilMaster;
import com.iris.util.Validations;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;
import com.iris.validator.ReturnTemplateUploadValidator;

@RestController
@RequestMapping("/service/retTemplateUpload")
public class ReturnTemplateUploadController {

	@Autowired
	private ReturnReturnTypeMappingRepo returnReturnTypeMappingRepo;
	@Autowired
	private ReturnRepo returnRepo;
	@Autowired
	private ReturnTypeRepo returnTypeRepo;
	@Autowired
	private UserMasterRepo userMasterRepo;
	@Autowired
	private ReturnTemplateRepository taxonomyRepo;
	@Autowired
	private WebFormVersionService webFormVersionService;

	@Autowired
	private ReturnTemplateService returnTemplateService;

	@Autowired
	private ReturnTemplateUploadValidator returnTemplateUploadValidator;

	@Autowired
	private GenericService<ReturnTemplate, Long> taxonomyService;
	static final Logger LOGGER = LogManager.getLogger(ReturnTemplateUploadController.class);
	private File parentFileName;
	private String metaInfFilePath;

	@RequestMapping(value = "/getRetunTypeAndFileFormat/{returnCode}", method = RequestMethod.POST)
	public ServiceResponse getRetunTypeAndFileFormat(@PathVariable("returnCode") String returnCode) {
		List<ReturnReturnTypeMapping> retMapList = null;
		try {
			retMapList = returnReturnTypeMappingRepo.getActiveMappForReturnCode(returnCode);
			if (CollectionUtils.isEmpty(retMapList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0949.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0764.toString())).setResponse(null).build();
			}
			List<Long> returnType = new ArrayList<>();
			for (ReturnReturnTypeMapping retMap : retMapList) {
				if (returnType.contains(retMap.getReturnTypeIdFk().getReturnTypeId())) {
					continue;
				}
				returnType.add(retMap.getReturnTypeIdFk().getReturnTypeId());
			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(returnType).build();
		} catch (Exception e) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	/*
	 * @RequestMapping(value = "/getRetunTypeAndFileFormat", method =
	 * RequestMethod.POST) public ServiceResponse getRetunTypeAndFileFormat() {
	 * List<ReturnReturnTypeMapping> retMapList = null; try { retMapList =
	 * returnReturnTypeMappingRepo.findAll();
	 * if(CollectionUtils.isEmpty(retMapList)) { return new
	 * ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0764.
	 * toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0764.
	 * toString())).setResponse(null).build(); } return new
	 * ServiceResponseBuilder().setStatus(true).setResponse(retMapList).build(); }
	 * catch(Exception e) { return new
	 * ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.
	 * toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.
	 * toString())).build(); } }
	 */

	@PostMapping(value = "/getAllVersionListUponReturnCode")
	public ServiceResponse getAllVersionListUponReturnCode(@RequestHeader("JobProcessingId") String jobProcessId, @RequestBody ReturnTempUploadDTO returnTempUploadDTO) {
		List<ReturnReturnTypeMapping> retMapList = null;
		try {
			retMapList = returnReturnTypeMappingRepo.getActiveMappForReturnCode(returnTempUploadDTO.getReturnCode());
			if (CollectionUtils.isEmpty(retMapList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0949.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0949.toString())).setResponse(null).build();
			}
			List<String> returnType = new ArrayList<>();
			for (ReturnReturnTypeMapping retMap : retMapList) {
				returnType.add(String.valueOf(retMap.getReturnTypeIdFk().getReturnTypeId()));
			}

			Map<String, List<String>> valueMap = new HashMap<>();
			List<String> valueList = new ArrayList<String>();

			valueList.add(returnTempUploadDTO.getReturnCode() + "");
			valueMap.put(ColumnConstants.RETURN_CODE.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueMap.put(ColumnConstants.RETURN_TYPE.getConstantVal(), returnType);

			List<ReturnTemplate> texonomyList = taxonomyService.getDataByColumnValue(valueMap, MethodConstants.GET_ALL_VERSION_UPON_RETRUN_RET_TYPE.getConstantVal());

			if (CollectionUtils.isEmpty(texonomyList)) {
				return new ServiceResponseBuilder().setStatus(true).setResponse(null).build();
			}

			List<TaxonomyDTO> taxonomyDTOList = new ArrayList<>();
			TaxonomyDTO taxonomyDTO = null;
			for (ReturnTemplate taxonomy : texonomyList) {
				taxonomyDTO = new TaxonomyDTO();
				taxonomyDTO.setTaxId(taxonomy.getReturnTemplateId());
				taxonomyDTO.setReturnReturnTypeId(taxonomy.getReturnTypeIdFk().getReturnTypeId());
				taxonomyDTO.setValidFromDate(DateManip.convertDateToString(taxonomy.getValidFromDate(), returnTempUploadDTO.getSessionFromat()));
				taxonomyDTO.setVersionDesc(taxonomy.getVersionDesc());
				taxonomyDTO.setVersionNum(taxonomy.getVersionNumber());
				taxonomyDTO.setSection(taxonomy.getReturnTypeSectionId());
				if (taxonomy.getIsActive()) {
					taxonomyDTO.setIsActive(1);
				} else {
					taxonomyDTO.setIsActive(0);
				}

				taxonomyDTOList.add(taxonomyDTO);
			}
			return new ServiceResponseBuilder().setStatus(true).setResponse(taxonomyDTOList).build();
		} catch (Exception e) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	@RequestMapping(value = "/validateReturnTemplateVersion", method = RequestMethod.POST)
	public ServiceResponse validateReturnTemplateVersion(@RequestBody ReturnTemplateDto returnTemplateDto
	// @PathVariable("returnCode") String returnCode,
	// @PathVariable("returnType") String returnType,
	// @PathVariable("retTempVersion") String retTempVersion,
	// @PathVariable("returnTypeSection") String returnTypeSection
	) {
		boolean validationResult = true;
		try {
			if (UtilMaster.isEmpty(returnTemplateDto.getReturnCode()) || returnTemplateDto.getReturnType() == null || UtilMaster.isEmpty(returnTemplateDto.getVersionNumber())) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0784.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0784.toString())).build();
			}

			Map<String, List<String>> valueMap = new HashMap<>();
			List<String> valueList = new ArrayList<>();

			valueList.add(returnTemplateDto.getReturnCode() + "");
			valueMap.put(ColumnConstants.RETURN_CODE.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(returnTemplateDto.getReturnType() + "");
			valueMap.put(ColumnConstants.RETURN_TYPE.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(returnTemplateDto.getReturnTypeSection());
			valueMap.put(ColumnConstants.RETURN_TYPE_SECTION.getConstantVal(), valueList);

			List<ReturnTemplate> taxonomyList = taxonomyService.getDataByColumnValue(valueMap, MethodConstants.VALIDATION_FOR_RETURN_TEMPLATE_VERSION.getConstantVal());
			if (CollectionUtils.isEmpty(taxonomyList)) {
				ReturnTemplate taxonomy = null;
				return new ServiceResponseBuilder().setStatus(true).setResponse(taxonomy).build();
			}
			ReturnTemplate retTaxonomy = taxonomyList.get(0);
			// List<String> versionList = texonomyList.stream().map(f ->
			// f.getVersionNumber()).collect(Collectors.toList());

			Map<String, ReturnTemplate> versionList = new HashMap<>();
			for (ReturnTemplate taxonomy : taxonomyList) {
				if (!versionList.keySet().contains(taxonomy.getVersionNumber())) {
					versionList.put(taxonomy.getVersionNumber(), taxonomy);
				}
			}

			try {
				if (versionList.keySet().contains(returnTemplateDto.getVersionNumber())) {
					ReturnTemplate taxonomy = versionList.get(returnTemplateDto.getVersionNumber());
					if (returnTemplateDto.getIsFormulaGenAction() && taxonomy.getSaveFormulaAsDraft() && !taxonomy.getIsActive()) {
						ReturnTemplateDto returnTemplate = new ReturnTemplateDto();
						returnTemplate.setVersionNumber(taxonomy.getVersionNumber());
						returnTemplate.setVersionDesc(taxonomy.getVersionDesc());
						String dte = DateManip.convertDateToString(taxonomy.getValidFromDate(), DateConstants.DD_MM_YYYY.getDateConstants());
						returnTemplate.setValidFromDate(dte);
						returnTemplate.setRetTemplateId(taxonomy.getReturnTemplateId());
						return new ServiceResponseBuilder().setStatus(true).setResponse(returnTemplate).build();
					} else {
						return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0212.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0212.toString())).build();
					}
				} else if (!checkVesion(retTaxonomy.getVersionNumber(), returnTemplateDto.getVersionNumber())) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0937.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0937.toString())).build();
				}
			} catch (Exception e) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0936.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0936.toString())).build();
			}

		} catch (Exception e) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
		return new ServiceResponseBuilder().setStatus(true).setResponse(validationResult).build();
	}

	@RequestMapping(value = "/chkValidFromDateExit/{returnCode}/{returnType}/{validFormdate}/{returnTypeSection}", method = RequestMethod.POST)
	public ServiceResponse chkValidFromDateExit(@PathVariable("returnCode") String returnCode, @PathVariable("returnType") String returnType, @PathVariable("validFormdate") String validFormdate, @PathVariable("returnTypeSection") String returnTypeSection) {
		try {
			if (UtilMaster.isEmpty(returnCode) || UtilMaster.isEmpty(returnType) || UtilMaster.isEmpty(validFormdate)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0784.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0784.toString())).build();
			}
			Date startDate = DateManip.convertStringToDate(validFormdate, "dd-MM-yyyy");
			String inputDatStr = DateManip.convertDateToString(startDate, GeneralConstants.SESSION_FORMAT.getConstantVal());

			ServiceResponse response = chkValidFromDate(returnCode, returnType, inputDatStr, returnTypeSection);
			if (!response.isStatus()) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0938.toString()).setStatusMessage("From date already exit for same return").build();
			} else {
				return new ServiceResponseBuilder().setStatus(true).setResponse(true).build();
			}
		} catch (Exception e) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}

	}

	public ServiceResponse chkValidFromDate(String returnCode, String returnType, String validFormdate, String returnTypeSection) {
		try {
			if (UtilMaster.isEmpty(returnCode) || UtilMaster.isEmpty(returnType) || UtilMaster.isEmpty(validFormdate)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0784.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0784.toString())).build();
			}
			Map<String, List<String>> valueMap = new HashMap<>();
			List<String> valueList = new ArrayList<>();

			valueList.add(returnCode + "");
			valueMap.put(ColumnConstants.RETURN_CODE.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(returnType + "");
			valueMap.put(ColumnConstants.RETURN_TYPE.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(validFormdate + "");
			valueMap.put(ColumnConstants.VALID_FROM_DATE.getConstantVal(), valueList);

			valueList = new ArrayList<>();
			valueList.add(returnTypeSection + "");
			valueMap.put(ColumnConstants.RETURN_TYPE_SECTION.getConstantVal(), valueList);

			List<ReturnTemplate> texonomyList = taxonomyService.getDataByColumnValue(valueMap, MethodConstants.VALIDATION_FOR_VALID_FROM_DATE.getConstantVal());
			if (CollectionUtils.isEmpty(texonomyList)) {
				return new ServiceResponseBuilder().setStatus(true).setResponse(true).build();
			} else {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0938.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0938.toString())).build();
			}

		} catch (Exception e) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}

	}

	@PostMapping(value = "/uploadRetTemplateAndSaveData")
	public ServiceResponse uploadRetTemplateAndSaveData(@RequestHeader("JobProcessingId") String jobProcessId, @RequestBody ReturnTempUploadDTO returnTempUploadDTO) {
		try {
			LOGGER.info("Request received for Request trans Id " + jobProcessId);
			if (returnTempUploadDTO == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0841.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0841.toString())).build();
			}
			ReturnTemplate taxonomy = null;
			Return returnBean = returnRepo.findByReturnCode(returnTempUploadDTO.getReturnCode());

			UserMaster userMaster = userMasterRepo.getOne(returnTempUploadDTO.getUserId());

			ReturnType returnType = new ReturnType();
			if (returnTempUploadDTO.getXbrlRetTempUploadDTO() != null) {
				if (UtilMaster.isEmpty(returnTempUploadDTO.getXbrlRetTempUploadDTO().getXbrlVersionNumber())) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0747.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0747.toString())).build();
				}

				taxonomy = new ReturnTemplate();
				taxonomy.setReturnObj(returnBean);
				XbrlReturnTempUploadDTO xbrlRetTempData = returnTempUploadDTO.getXbrlRetTempUploadDTO();
				returnType = returnTypeRepo.getOne(xbrlRetTempData.getReturnTypeId());
				taxonomy.setReturnTypeIdFk(returnType);
				Date fromDate = DateManip.convertStringToDate(xbrlRetTempData.getXbrlValidFromDate(), returnTempUploadDTO.getSessionFromat());
				taxonomy.setValidFromDate(fromDate);
				taxonomy.setVersionDesc(xbrlRetTempData.getXbrlVersionDesc());
				taxonomy.setVersionNumber(xbrlRetTempData.getXbrlVersionNumber());
				// .content("/")
				/*
				 * String slash = "\\"; String[] xsdFileName = null; String str =
				 * xbrlRetTempData.getXbrlXSDFileName();
				 * 
				 * if(str.contains("/")) { xsdFileName =
				 * xbrlRetTempData.getXbrlXSDFileName().split("/"); } else
				 * if(str.contains(slash)) { str = str.replace("\\", "/"); xsdFileName =
				 * str.split("/"); }
				 */

				String xsdFileName = "";
				if (xbrlRetTempData.getXbrlXSDFileName().startsWith("../")) {
					xsdFileName = xbrlRetTempData.getXbrlXSDFileName().replace("../", "");
				}

				if (xbrlRetTempData.getXbrlXSDFileName().contains("\\")) {
					xsdFileName = xbrlRetTempData.getXbrlXSDFileName().replace("\\", "/");
				}
				if (!Validations.isEmpty(xsdFileName)) {
					taxonomy.setXsdFileName(xsdFileName);
				} else {
					taxonomy.setXsdFileName(xbrlRetTempData.getXbrlXSDFileName());
				}
				taxonomy.setReturnPackage(xbrlRetTempData.getXbrlReturnFullPackageFileName());
				taxonomy.setTaxonomyName(xbrlRetTempData.getXbrlTaxonomyFileName());
				taxonomy.setIsActive(true);
				taxonomy.setCreatedBy(userMaster);
				taxonomy.setCreatedOn(DateManip.getCurrentDateTime());
				taxonomyRepo.save(taxonomy);
			}
			if (returnTempUploadDTO.getSdmxRetTempUploadDTO() != null) {
				taxonomy = new ReturnTemplate();
				taxonomy.setReturnObj(returnBean);
				SdmxReturnTempUploadDTO sdmxRetTempData = returnTempUploadDTO.getSdmxRetTempUploadDTO();
				returnType = returnTypeRepo.findByReturnTypeId(sdmxRetTempData.getReturnTypeId());
				taxonomy.setReturnTypeIdFk(returnType);
				Date fromDate = DateManip.convertStringToDate(sdmxRetTempData.getSdmxValidFromDate(), returnTempUploadDTO.getSessionFromat());
				taxonomy.setValidFromDate(fromDate);
				taxonomy.setVersionDesc(sdmxRetTempData.getSdmxVersionDesc());
				taxonomy.setVersionNumber(sdmxRetTempData.getSdmxVersionNumber());
				// taxonomy.setXsdFileName(sdmxRetTempData.getSdmxXSDFileName());
				// taxonomy.setTaxonomyName(sdmxRetTempData.getSdmxTaxonomyFileName());
				taxonomy.setIsActive(true);
				taxonomy.setCreatedBy(userMaster);
				taxonomy.setCreatedOn(DateManip.getCurrentDateTime());
				taxonomyRepo.save(taxonomy);
			}
			if (returnTempUploadDTO.getCsvRetTempUploadDTO() != null) {
				taxonomy = new ReturnTemplate();
				taxonomy.setReturnObj(returnBean);
				CsvReturnTempUploadDTO csvRetTemp = returnTempUploadDTO.getCsvRetTempUploadDTO();
				returnType = returnTypeRepo.findByReturnTypeId(3L);
				taxonomy.setReturnTypeIdFk(returnType);
				Date fromDate = DateManip.convertStringToDate(csvRetTemp.getCsvValidFromDate(), returnTempUploadDTO.getSessionFromat());
				taxonomy.setValidFromDate(fromDate);
				taxonomy.setVersionDesc(csvRetTemp.getCsvVersionDesc());
				taxonomy.setVersionNumber(csvRetTemp.getCsvVersionNumber());
				taxonomy.setFormulaFileName(csvRetTemp.getCsvFormulaFileName());
				taxonomy.setTaxonomyName(csvRetTemp.getCsvTaxonomyFileName());
				taxonomy.setReturnTypeSectionId(1);
				taxonomy.setIsActive(true);
				taxonomy.setCreatedBy(userMaster);
				taxonomy.setCreatedOn(DateManip.getCurrentDateTime());
				taxonomy.setReturnPackage(csvRetTemp.getCsvReturnFullPackage());
				taxonomyRepo.save(taxonomy);
			}
			if (returnTempUploadDTO.getWebformRetTempUploadDTO() != null) {
				taxonomy = new ReturnTemplate();
				taxonomy.setReturnObj(returnBean);
				WebformReturnTempUploadDTO webFromRetTemp = returnTempUploadDTO.getWebformRetTempUploadDTO();
				returnType = returnTypeRepo.findByReturnTypeId(3L);
				taxonomy.setReturnTypeIdFk(returnType);
				Date fromDate = DateManip.convertStringToDate(webFromRetTemp.getWebfrmValidFromDate(), returnTempUploadDTO.getSessionFromat());
				taxonomy.setValidFromDate(fromDate);
				taxonomy.setVersionDesc(webFromRetTemp.getWebfrmVersionDesc());
				taxonomy.setVersionNumber(webFromRetTemp.getWebfrmVersionNumber());
				taxonomy.setReturnTypeIdFk(returnType);
				taxonomy.setTaxonomyName(webFromRetTemp.getWebfrmtxtFileNm());
				if (Boolean.TRUE.equals(returnTempUploadDTO.getSaveFormulaAsDraft())) {
					taxonomy.setIsActive(false);
					taxonomy.setSaveFormulaAsDraft(true);
				} else {
					taxonomy.setIsActive(true);
				}
				taxonomy.setCreatedBy(userMaster);
				taxonomy.setCreatedOn(DateManip.getCurrentDateTime());
				if (!UtilMaster.isEmpty(webFromRetTemp.getWebfrmFormulaFileNm())) {
					taxonomy.setFormulaFileName(webFromRetTemp.getWebfrmFormulaFileNm());
				}
				taxonomy.setReturnTypeSectionId(2);
				taxonomy = taxonomyRepo.save(taxonomy);
				if (!webFormVersionService.addEditVersionMapping(webFromRetTemp.getEleHeaderList(), taxonomy, userMaster)) {
					return new ServiceResponseBuilder().setStatus(true).setStatusCode(ErrorCode.E0823.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0823.toString())).build();
				}
			}
			return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.RECORD_SAVED_SUCCESSFULLY.getConstantVal()).setResponse(true).build();
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Transaction ID : " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0804.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0804.toString())).build();
			// return new
			// ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorConstants.DEFAULT_ERROR.getConstantVal()).build();
		}
	}

	@PostMapping(value = "/updateInfoOfReturnTemplateVesion")
	public ServiceResponse updateInfoOfReturnTemplateVesion(@RequestHeader("JobProcessingId") String jobProcessId, @RequestBody ReturnTempUploadDTO returnTempUploadDTO) {
		try {
			LOGGER.info("Request received for Request trans Id " + jobProcessId);
			if (returnTempUploadDTO == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0784.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0784.toString())).build();
			}

			Return returnBean = returnRepo.findByReturnCode(returnTempUploadDTO.getReturnCode());
			if (returnBean == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0784.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0784.toString())).build();
			}

			UserMaster userMaster = userMasterRepo.getOne(returnTempUploadDTO.getUserId());
			if (userMaster == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0638.toString())).build();
			}

			if (returnTempUploadDTO.getXbrlRetTempUploadDTO() != null) {
				XbrlReturnTempUploadDTO xbrlRetTempData = returnTempUploadDTO.getXbrlRetTempUploadDTO();
				ReturnTemplate taxonomy = taxonomyRepo.findByReturnCodeRetTypeAndVersionNumber(returnTempUploadDTO.getReturnCode(), xbrlRetTempData.getXbrlVersionNumber(), xbrlRetTempData.getReturnTypeId());
				Date fromDate = DateManip.convertStringToDate(xbrlRetTempData.getXbrlValidFromDate(), returnTempUploadDTO.getSessionFromat());
				taxonomy.setValidFromDate(fromDate);
				taxonomy.setVersionDesc(xbrlRetTempData.getXbrlVersionDesc());
				taxonomy.setModifiedByFk(userMaster);
				taxonomy.setModifedOn(DateManip.getCurrentDateTime());
				taxonomy.setIsActive(xbrlRetTempData.getIsActive());
				taxonomyRepo.save(taxonomy);
			}
			if (returnTempUploadDTO.getSdmxRetTempUploadDTO() != null) {
				SdmxReturnTempUploadDTO sdmxRetTempData = returnTempUploadDTO.getSdmxRetTempUploadDTO();
				ReturnTemplate taxonomy = taxonomyRepo.findByReturnCodeRetTypeAndVersionNumber(returnTempUploadDTO.getReturnCode(), sdmxRetTempData.getSdmxVersionNumber(), sdmxRetTempData.getReturnTypeId());
				Date fromDate = DateManip.convertStringToDate(sdmxRetTempData.getSdmxValidFromDate(), returnTempUploadDTO.getSessionFromat());
				taxonomy.setValidFromDate(fromDate);
				taxonomy.setVersionDesc(sdmxRetTempData.getSdmxVersionDesc());
				taxonomy.setModifiedByFk(userMaster);
				taxonomy.setModifedOn(DateManip.getCurrentDateTime());
				taxonomy.setIsActive(sdmxRetTempData.getIsActive());
				taxonomyRepo.save(taxonomy);
			}
			if (returnTempUploadDTO.getCsvRetTempUploadDTO() != null) {
				CsvReturnTempUploadDTO csvRetTemp = returnTempUploadDTO.getCsvRetTempUploadDTO();
				ReturnTemplate taxonomy = taxonomyRepo.findByReturnCodeRetTypeAndVersionNumberForCSV(returnTempUploadDTO.getReturnCode(), csvRetTemp.getCsvVersionNumber(), 3L);
				Date fromDate = DateManip.convertStringToDate(csvRetTemp.getCsvValidFromDate(), returnTempUploadDTO.getSessionFromat());
				taxonomy.setValidFromDate(fromDate);
				taxonomy.setVersionDesc(csvRetTemp.getCsvVersionDesc());
				taxonomy.setModifiedByFk(userMaster);
				taxonomy.setModifedOn(DateManip.getCurrentDateTime());
				taxonomy.setIsActive(csvRetTemp.getIsActive());
				taxonomyRepo.save(taxonomy);
			}
			if (returnTempUploadDTO.getWebformRetTempUploadDTO() != null) {

				WebformReturnTempUploadDTO webFromRetTemp = returnTempUploadDTO.getWebformRetTempUploadDTO();
				ReturnTemplate taxonomy = taxonomyRepo.findByReturnCodeRetTypeAndVersionNumberForWebForm(returnTempUploadDTO.getReturnCode(), webFromRetTemp.getWebfrmVersionNumber(), 3L);
				Date fromDate = DateManip.convertStringToDate(webFromRetTemp.getWebfrmValidFromDate(), returnTempUploadDTO.getSessionFromat());
				taxonomy.setValidFromDate(fromDate);
				taxonomy.setVersionDesc(webFromRetTemp.getWebfrmVersionDesc());
				taxonomy.setModifiedByFk(userMaster);
				taxonomy.setModifedOn(DateManip.getCurrentDateTime());
				taxonomy.setIsActive(webFromRetTemp.getIsActive());
				taxonomyRepo.save(taxonomy);
			}
			return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.RECORD_FETCHED_SUCCESSFULLY.getConstantVal()).setResponse(true).build();
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Transaction ID : " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0804.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0804.toString())).build();
			// return new
			// ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorConstants.DEFAULT_ERROR.getConstantVal()).build();
		}
	}

	private static boolean checkVesion(String preVersion, String curVersion) {

		String[] v1 = preVersion.split("\\.");
		String[] v2 = curVersion.split("\\.");
		int i = 0;
		int j = 0;
		while (i <= v1.length - 1 && j <= v2.length) {
			if (Integer.valueOf(v1[i]) < Integer.valueOf(v2[j])) {
				System.out.println("the 2nd one is greater");
				return true;
			} else if (Integer.valueOf(v1[i]) > Integer.valueOf(v2[j])) {
				System.out.println("the 1st one is greater");
				return false;
			} else {
				i++;
				j++;
			}
		}
		return true;
	}

	@PostMapping(value = "/user/{userId}/getAllDataBySelectType")
	public ServiceResponse findReturnByReturnTypeAndSectionId(@RequestHeader("JobProcessingId") String jobProcessId, @PathVariable("userId") Long userId, @RequestBody ReturnTemplateDto returnTemplateDto) {
		List<ReturnTemplateDto> returnTemplateDtoList = null;
		try {
			returnTemplateUploadValidator.validateFindReturnByReturnTypeAndSectionIdRequest(returnTemplateDto, jobProcessId, userId);
			List<ReturnTemplate> returnTemplates = returnTemplateService.findByReturnTypeAndSectionId(Long.valueOf(returnTemplateDto.getReturnType()), returnTemplateDto.getReturnSectionId(), returnTemplateDto.getReturnIdList());
			if (!CollectionUtils.isEmpty(returnTemplates)) {
				returnTemplateDtoList = new ArrayList<>();
				for (ReturnTemplate returnTemplate : returnTemplates) {
					ReturnTemplateDto dto = new ReturnTemplateDto();
					dto.setRetTemplateId(returnTemplate.getReturnTemplateId());
					dto.setReturnName(returnTemplate.getReturnObj().getReturnName());
					dto.setReturnCode(returnTemplate.getReturnObj().getReturnCode());
					dto.setVersionNumber(returnTemplate.getVersionNumber());
					returnTemplateDtoList.add(dto);
				}
			}
			return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.RECORD_FETCHED_SUCCESSFULLY.getConstantVal()).setResponse(returnTemplateDtoList).build();
		} catch (ApplicationException aex) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + " Transaction ID : " + jobProcessId + ", Error Code - " + aex.getErrorCode() + ", Error Msg - " + aex.getErrorMsg(), aex);
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + " Transaction ID : " + jobProcessId, aex);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(aex.getErrorCode()).setStatusMessage(aex.getErrorMsg()).build();
		} catch (Exception ex) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + " Transaction ID : " + jobProcessId, ex);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
	}

	@PostMapping(value = "/getAllDataOfReturnTemplateFromReturn")
	public ServiceResponse getAllDataOfReturnTemplateFromReturn(@RequestHeader("JobProcessingId") String jobProcessId, @RequestBody ReturnTemplateDto returnTemplateDto) {
		try {
			Map<String, List<String>> valueMap = new HashMap<>();
			List<String> valueList = new ArrayList<>();
			// valueList.add(returnTemplateDto.getReturnCodeList() + "");
			valueMap.put(ColumnConstants.RETURN_CODE.getConstantVal(), returnTemplateDto.getReturnCodeList());

			List<ReturnTemplate> returnTemplateList = taxonomyService.getDataByColumnValue(valueMap, MethodConstants.GET_ALL_RETURN_TEMPLATE_UPON_RETURN.getConstantVal());
			if (CollectionUtils.isEmpty(returnTemplateList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0804.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0804.toString())).build();
			}
			List<ReturnTemplateDto> returnTemplateDtoList = new ArrayList<>();
			ReturnTemplateDto retTempObj;
			for (ReturnTemplate returnTemplateObj : returnTemplateList) {
				if (returnTemplateObj.getReturnTypeSectionId() == 2) {
					continue;
				}

				retTempObj = new ReturnTemplateDto();
				retTempObj.setReturnCode(returnTemplateObj.getReturnObj().getReturnCode());
				retTempObj.setReturnSectionId(returnTemplateObj.getReturnTypeSectionId());
				ReturnLabel returnLabel = returnTemplateObj.getReturnObj().getReturnLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(returnTemplateDto.getLangCode())).findAny().orElse(null);
				if (returnLabel != null) {
					retTempObj.setReturnName(returnLabel.getReturnLabel());
				}
				retTempObj.setVersionNumber(returnTemplateObj.getVersionNumber());
				retTempObj.setVersionDesc(returnTemplateObj.getVersionDesc());
				retTempObj.setTaxonomyName(returnTemplateObj.getTaxonomyName());
				retTempObj.setReturnType(returnTemplateObj.getReturnTypeIdFk().getReturnTypeDesc());
				retTempObj.setValidFromDate(DateManip.convertDateToString(returnTemplateObj.getValidFromDate(), ObjectCache.getDateFormat()));
				if (Validations.isEmpty(returnTemplateObj.getXsdFileName())) {
					retTempObj.setXsdFileName("-");
				} else {
					retTempObj.setXsdFileName(returnTemplateObj.getXsdFileName());
				}

				if (returnTemplateObj.getModifedOn() != null) {
					retTempObj.setUploadedOn(returnTemplateObj.getModifedOn().getTime());
				} else {
					retTempObj.setUploadedOn(returnTemplateObj.getCreatedOn().getTime());
				}

				if (returnTemplateObj.getModifiedByFk() != null) {
					retTempObj.setUploadedBy(returnTemplateObj.getModifiedByFk().getUserName());
				} else {
					retTempObj.setUploadedBy(returnTemplateObj.getCreatedBy().getUserName());
				}
				returnTemplateDtoList.add(retTempObj);
			}
			return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.RECORD_FETCHED_SUCCESSFULLY.getConstantVal()).setResponse(returnTemplateDtoList).build();
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Transaction ID : " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0804.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0804.toString())).build();
		}
	}

	public List<ReturnTemplateDto> getAllDataOfReturnTemplateFromReturn_new(ReturnTemplateDto returnTemplateDto) throws ApplicationException {
		try {
			LOGGER.info("getAllDataOfReturnTemplateFromReturn_new method calling start");
			Map<String, List<String>> valueMap = new HashMap<>();

			valueMap.put(ColumnConstants.RETURN_CODE.getConstantVal(), returnTemplateDto.getReturnCodeList());

			List<ReturnTemplate> returnTemplateList = returnTemplateService.getDataByColumnValue(valueMap, MethodConstants.GET_ALL_RETURN_TEMPLATE_UPON_RETURN.getConstantVal());
			if (CollectionUtils.isEmpty(returnTemplateList)) {
				throw new ApplicationException(ErrorCode.E0804.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0804.toString()));
			}
			List<ReturnTemplateDto> returnTemplateDtoList = new ArrayList<>();
			ReturnTemplateDto retTempObj;
			for (ReturnTemplate returnTemplateObj : returnTemplateList) {
				/*
				 * if (returnTemplateObj.getReturnTypeSectionId() == 2) { continue; }
				 */

				retTempObj = new ReturnTemplateDto();
				retTempObj.setReturnCode(returnTemplateObj.getReturnObj().getReturnCode());
				retTempObj.setReturnSectionId(returnTemplateObj.getReturnTypeSectionId());
				ReturnLabel returnLabel = returnTemplateObj.getReturnObj().getReturnLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(returnTemplateDto.getLangCode())).findAny().orElse(null);
				if (returnLabel != null) {
					retTempObj.setReturnName(returnLabel.getReturnLabel());
				}
				retTempObj.setVersionNumber(returnTemplateObj.getVersionNumber());
				retTempObj.setVersionDesc(returnTemplateObj.getVersionDesc());
				retTempObj.setTaxonomyName(returnTemplateObj.getTaxonomyName());
				retTempObj.setReturnType(returnTemplateObj.getReturnTypeIdFk().getReturnTypeDesc());
				retTempObj.setValidFromDate(DateManip.convertDateToString(returnTemplateObj.getValidFromDate(), ObjectCache.getDateFormat()));
				if (Validations.isEmpty(returnTemplateObj.getXsdFileName())) {
					retTempObj.setXsdFileName("-");
				} else {
					retTempObj.setXsdFileName(returnTemplateObj.getXsdFileName());
				}

				if (Validations.isEmpty(returnTemplateObj.getReturnPackage())) {
					retTempObj.setReturnPackage("-");
				} else {
					retTempObj.setReturnPackage(returnTemplateObj.getReturnPackage());
				}

				if (returnTemplateObj.getModifedOn() != null) {
					retTempObj.setUploadedOn(returnTemplateObj.getModifedOn().getTime());
				} else {
					retTempObj.setUploadedOn(returnTemplateObj.getCreatedOn().getTime());
				}

				if (returnTemplateObj.getModifiedByFk() != null) {
					retTempObj.setUploadedBy(returnTemplateObj.getModifiedByFk().getUserName());
				} else {
					retTempObj.setUploadedBy(returnTemplateObj.getCreatedBy().getUserName());
				}
				returnTemplateDtoList.add(retTempObj);
			}
			if (CollectionUtils.isEmpty(returnTemplateDtoList)) {
				LOGGER.info(" List in getAllDataOfReturnTemplateFromReturn_new list ");
			}
			return returnTemplateDtoList;
		} catch (Exception e) {
			LOGGER.error(" Exception occured to fetch getAllDataOfReturnTemplateFromReturn_new list ", e);
			throw new ApplicationException(ErrorCode.E0804.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0804.toString()));
		}
	}

	@SuppressWarnings("null")
	@PostMapping(value = "/uploadRetTemplateAndSaveDataViaApi")
	public ServiceResponse uploadRetTemplateAndSaveDataViaApi(@RequestHeader("JobProcessingId") String jobProcessId, @RequestBody ReturnTempUploadDTO returnTempUploadDTO) {
		String taxonomyZipExtraxtPath = null;
		File tempSaveDirectory = null;
		String returnTemplateZipPath = null;
		try {
			LOGGER.info("Request received for Request trans Id " + jobProcessId);
			if (returnTempUploadDTO == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0841.toString()).setStatusMessage("Input Data is Empty").build();
			}
			if (UtilMaster.isEmpty(returnTempUploadDTO.getReturnCode())) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0784.toString()).setStatusMessage("Return code is empty into the request").build();
			}
			if (UtilMaster.isEmpty(returnTempUploadDTO.getSessionFromat())) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1130.toString()).setStatusMessage("Session format is empty into the request").build();
			}
			if (returnTempUploadDTO.getUserId() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0789.toString()).setStatusMessage("User Id is empty into the request").build();
			}

			UserMaster userMaster = userMasterRepo.findByUserIdAndIsActiveTrue(returnTempUploadDTO.getUserId());
			if (userMaster == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0638.toString()).setStatusMessage("User not found ").build();
			}

			Return returnBean = returnRepo.findByReturnCode(returnTempUploadDTO.getReturnCode());
			if (returnBean == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0134.toString()).setStatusMessage("Retrun Code not exit").build();
			}

			ReturnType returnType = new ReturnType();
			if (returnTempUploadDTO.getXbrlRetTempUploadDTO() != null) {
				if (UtilMaster.isEmpty(returnTempUploadDTO.getXbrlRetTempUploadDTO().getXbrlVersionNumber())) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0747.toString()).setStatusMessage("Validator Version is empty into the request").build();
				}
				if (UtilMaster.isEmpty(returnTempUploadDTO.getXbrlRetTempUploadDTO().getXbrlValidFromDate())) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1131.toString()).setStatusMessage("Form Date is empty into the request").build();
				}
				if (UtilMaster.isEmpty(returnTempUploadDTO.getXbrlRetTempUploadDTO().getXbrlTaxonomyFileName())) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1133.toString()).setStatusMessage("Taxonomy Zip is empty into the request").build();
				}
				if (UtilMaster.isEmpty(returnTempUploadDTO.getXbrlRetTempUploadDTO().getXbrlVersionDesc())) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1134.toString()).setStatusMessage("Version Description is empty into the request").build();
				}
				if (UtilMaster.isEmpty(returnTempUploadDTO.getXbrlRetTempUploadDTO().getXbrlXSDFileName())) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1141.toString()).setStatusMessage("XSD file name is empty into the request").build();
				}
				if (UtilMaster.isEmpty(returnTempUploadDTO.getXbrlRetTempUploadDTO().getXbrlWebFormJSON())) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1137.toString()).setStatusMessage("XBRL Webform File is empty").build();
				}

				// Version validation Check here .....

				Map<String, List<String>> valueMap = new HashMap<>();
				List<String> valueList = new ArrayList<>();

				valueList.add(returnTempUploadDTO.getReturnCode() + "");
				valueMap.put(ColumnConstants.RETURN_CODE.getConstantVal(), valueList);

				valueList = new ArrayList<>();
				valueList.add("1" + "");
				valueMap.put(ColumnConstants.RETURN_TYPE.getConstantVal(), valueList);

				valueList = new ArrayList<>();
				valueList.add("0" + "");
				valueMap.put(ColumnConstants.RETURN_TYPE_SECTION.getConstantVal(), valueList);

				List<ReturnTemplate> texonomyList = taxonomyService.getDataByColumnValue(valueMap, MethodConstants.VALIDATION_FOR_RETURN_TEMPLATE_VERSION.getConstantVal());
				if (!CollectionUtils.isEmpty(texonomyList)) {

					ReturnTemplate retTaxonomy = texonomyList.get(0);
					List<String> versionList = new ArrayList<>();
					for (ReturnTemplate taxonomy : texonomyList) {
						if (!versionList.contains(taxonomy.getVersionNumber())) {
							versionList.add(taxonomy.getVersionNumber());
						}
					}
					try {
						if (versionList.contains(returnTempUploadDTO.getXbrlRetTempUploadDTO().getXbrlVersionNumber())) {
							return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0212.toString()).setStatusMessage("Version number already exit").build();
						} else if (!checkVesion(retTaxonomy.getVersionNumber(), returnTempUploadDTO.getXbrlRetTempUploadDTO().getXbrlVersionNumber())) {
							return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0937.toString()).setStatusMessage("Version Should be greater than previous version").build();
						}
					} catch (Exception e) {
						return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0936.toString()).setStatusMessage("Please Enter Valid Version. i.e 1.1.1 or Varsion validation error").build();
					}
				}

				// Version validation Check here .....
				Date startDate = DateManip.convertStringToDate(returnTempUploadDTO.getXbrlRetTempUploadDTO().getXbrlValidFromDate(), returnTempUploadDTO.getSessionFromat());
				String inputDatStr = DateManip.convertDateToString(startDate, GeneralConstants.SESSION_FORMAT.getConstantVal());
				ServiceResponse response = chkValidFromDate(returnTempUploadDTO.getReturnCode(), "1", inputDatStr, "0");
				if (!response.isStatus()) {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0938.toString()).setStatusMessage("From date already exit for same return").build();
				}

				byte[] decodeStrOfZipFile = Base64.decodeBase64(returnTempUploadDTO.getXbrlRetTempUploadDTO().getXbrlTaxonomyFileName().getBytes());

				Properties prop = ResourceUtil.getResourcePropertyFile();
				String saveDirectory = prop.getProperty("filepath.root");
				saveDirectory = saveDirectory + ResourceUtil.getKeyValue("filepath.upload.temp") + File.separator + returnTempUploadDTO.getUserId();

				tempSaveDirectory = new File(saveDirectory);
				if (!FileManager.checkDirExistence(tempSaveDirectory)) {
					FileManager.makeDirWithParentDir(tempSaveDirectory);
				}

				String tempFilePath = tempSaveDirectory.getAbsolutePath() + File.separator + prop.getProperty("xbrl.sdmx.template.file.name");

				FileManager.writeByteArrayToFile(new File(tempFilePath), decodeStrOfZipFile);
				List<String> xsdList = new ArrayList<>();
				xsdList = unzipAndGetXSDFiles(saveDirectory + File.separator + prop.getProperty("xbrl.sdmx.template.file.name"), tempSaveDirectory.getAbsolutePath(), returnTempUploadDTO.getReturnCode());
				if (UtilMaster.isEmpty(xsdList)) {
					LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Transaction ID : " + jobProcessId);
					FileManager.deleteDirectory(tempSaveDirectory);
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1144.toString()).setStatusMessage("Unable to Read taxonomy zip folder").build();
				}

				if (!xsdList.contains(returnTempUploadDTO.getXbrlRetTempUploadDTO().getXbrlXSDFileName())) {
					LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Transaction ID : " + jobProcessId);
					FileManager.deleteDirectory(tempSaveDirectory);
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1142.toString()).setStatusMessage("XSD File Not Found in given taxonomy folder").build();
				}

				returnTemplateZipPath = prop.getProperty("filepath.root") + prop.getProperty("returnTemplate.upload.path.zip") + File.separator + returnTempUploadDTO.getReturnCode() + File.separator + "XBRL" + File.separator + returnTempUploadDTO.getXbrlRetTempUploadDTO().getXbrlVersionNumber();
				String finalPathForTax = new StringBuilder().append(prop.getProperty("filepath.root")).append(prop.getProperty("returnTemplate.upload.path")).toString();
				String basePathTaxonomy = finalPathForTax + File.separator + returnTempUploadDTO.getReturnCode() + File.separator + "XBRL";
				taxonomyZipExtraxtPath = basePathTaxonomy + File.separator + returnTempUploadDTO.getXbrlRetTempUploadDTO().getXbrlVersionNumber();

				File retCodeTaxPath = new File(basePathTaxonomy);
				File versionPathTax = new File(taxonomyZipExtraxtPath);

				if (FileManager.checkDirExistence(retCodeTaxPath)) {
					if (!FileManager.checkDirExistence(versionPathTax)) {
						FileManager.makeDirWithParentDir(versionPathTax);
					}
				} else {
					FileManager.makeDirWithParentDir(retCodeTaxPath);
					FileManager.makeDirWithParentDir(versionPathTax);
				}
				File srcFile = new File(tempFilePath);
				File destFile = new File(returnTemplateZipPath + File.separator + returnTempUploadDTO.getReturnCode() + GeneralConstants.UNDER_SCORE.getConstantVal() + prop.getProperty("xbrl.sdmx.template.file.name"));
				boolean flag = FileManager.copyFile(srcFile, destFile, true);

				if (flag) {
					unZipFile(returnTemplateZipPath + File.separator + returnTempUploadDTO.getReturnCode() + GeneralConstants.UNDER_SCORE.getConstantVal() + prop.getProperty("xbrl.sdmx.template.file.name"), taxonomyZipExtraxtPath);
					FileManager.deleteDirectory(tempSaveDirectory);

					ReturnTemplate taxonomy = new ReturnTemplate();
					taxonomy.setReturnObj(returnBean);
					XbrlReturnTempUploadDTO xbrlRetTempData = returnTempUploadDTO.getXbrlRetTempUploadDTO();
					returnType = returnTypeRepo.getOne(xbrlRetTempData.getReturnTypeId());
					taxonomy.setReturnTypeIdFk(returnType);
					Date fromDate = DateManip.convertStringToDate(xbrlRetTempData.getXbrlValidFromDate(), returnTempUploadDTO.getSessionFromat());
					taxonomy.setValidFromDate(fromDate);
					taxonomy.setVersionDesc(xbrlRetTempData.getXbrlVersionDesc());
					taxonomy.setVersionNumber(xbrlRetTempData.getXbrlVersionNumber());

					String xsdFileName = "";
					if (xbrlRetTempData.getXbrlXSDFileName().startsWith("../")) {
						xsdFileName = xbrlRetTempData.getXbrlXSDFileName().replace("../", "");
					}

					if (xbrlRetTempData.getXbrlXSDFileName().contains("\\")) {
						xsdFileName = xbrlRetTempData.getXbrlXSDFileName().replace("\\", "/");
					}
					if (!Validations.isEmpty(xsdFileName)) {
						taxonomy.setXsdFileName(xsdFileName);
					} else {
						taxonomy.setXsdFileName(xbrlRetTempData.getXbrlXSDFileName());
					}
					// taxonomy.setXsdFileName(xsdFileName[xsdFileName.length-1]);
					taxonomy.setTaxonomyName(returnTempUploadDTO.getReturnCode() + GeneralConstants.UNDER_SCORE.getConstantVal() + prop.getProperty("xbrl.sdmx.template.file.name"));
					taxonomy.setIsActive(true);
					taxonomy.setCreatedBy(userMaster);
					taxonomy.setCreatedOn(DateManip.getCurrentDateTime());
					taxonomyRepo.save(taxonomy);
					ReturnTemplate taxonomyObj = taxonomyRepo.save(taxonomy);
					if (taxonomyObj == null) {
						FileManager.deleteDirectory(new File(taxonomyZipExtraxtPath));
						FileManager.deleteDirectory(tempSaveDirectory);
						return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0804.toString()).setStatusMessage("Unable to Save Record").build();
					}
					if (UtilMaster.isEmpty(returnTempUploadDTO.getXbrlRetTempUploadDTO().getXbrlWebFormJSON())) {
						return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1137.toString()).setStatusMessage("XBRL Webform File is empty").build();
					}

					byte[] decodeStrOfWebformJson = Base64.decodeBase64(returnTempUploadDTO.getXbrlRetTempUploadDTO().getXbrlWebFormJSON().getBytes());

					// String tempWebFormFilePath = prop.getProperty("filepath.xbrl.webform.shard");

					FileManager.writeByteArrayToFile(new File(saveDirectory + prop.getProperty("webform.FileName")), decodeStrOfWebformJson);
					String unZipDirectoryPath = unZipFile(saveDirectory + prop.getProperty("webform.FileName"), saveDirectory);
					String rootPath = prop.getProperty("filepath.root");
					metaInfFilePath = getXSDFolderPath(saveDirectory);

					File destFile1 = new File(rootPath + prop.getProperty("filepath.xbrl.webform.shard") + metaInfFilePath);

					if (!FileManager.checkDirExistence(destFile1)) {
						FileManager.makeDirWithParentDir(destFile1);
					}

					copyAllFileToDest(parentFileName, new File(rootPath + prop.getProperty("filepath.xbrl.webform.shard") + metaInfFilePath));
					FileManager.cleanDirectory(new File(rootPath + ResourceUtil.getKeyValue("filepath.upload.temp")));
				} else {
					return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E1144.toString()).setStatusMessage("Unable to Read taxonomy zip folder").build();
				}
			}
			return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.RECORD_SAVED_SUCCESSFULLY.getConstantVal()).setResponse(true).build();
		} catch (Exception e) {
			FileManager.deleteDirectory(new File(taxonomyZipExtraxtPath));
			FileManager.deleteDirectory(tempSaveDirectory);
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Transaction ID : " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0804.toString()).setStatusMessage("Exception occoured while fatching to Responce from API").build();
		}
	}

	public static void copyAllFileToDest(File source, File destination) {
		if (source.isDirectory()) {
			if (!destination.exists()) {
				destination.mkdirs();
			}

			String files[] = source.list();

			for (String file : files) {
				File srcFile = new File(source, file);
				File destFile = new File(destination, file);

				copyAllFileToDest(srcFile, destFile);
			}
		} else {
			InputStream in = null;
			OutputStream out = null;

			try {
				in = new FileInputStream(source);
				out = new FileOutputStream(destination);

				byte[] buffer = new byte[1024];

				int length;
				while ((length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}
				in.close();
				out.close();
			} catch (Exception e) {
				LOGGER.error("Exception", e);
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException e1) {
					LOGGER.error("Exception", e1);
				}

				try {
					if (out != null) {
						out.close();
					}
				} catch (IOException e1) {
					LOGGER.error("Exception", e1);
				}
			}
		}
	}

	private List<String> unzipAndGetXSDFiles(String zipFilePath, String destDir, String returnCode) {
		List<String> allXSDFileList = new ArrayList<>();
		try {
			String unZipDirectoryPath = unZipFile(zipFilePath, destDir);
			String metaInfFileName = getMetaInfFolderPath(unZipDirectoryPath);
			File xmlfilePath = new File(metaInfFileName + ResourceUtil.getKeyValue("filePath.taxonomyPackageXml.fileName"));
			allXSDFileList = readXMLFileFromTaxonomyZip(xmlfilePath.getAbsolutePath(), returnCode);
		} catch (Exception e1) {
			LOGGER.error("Exception : ", e1);
		}
		return allXSDFileList;
	}

	private String getMetaInfFolderPath(String fileloc) {
		File f = new File(fileloc);
		File[] fList = f.listFiles();
		String fileName = null;
		for (File file : fList) {
			if (file.isDirectory()) {
				fileName = file.getName();
				if (fileName.equalsIgnoreCase("META-INF")) {

					metaInfFilePath = file.getAbsolutePath();
					break;
				} else {
					getMetaInfFolderPath(file.getAbsolutePath());
				}
			}
		}
		return metaInfFilePath;
	}

	private String getXSDFolderPath(String fileloc) {
		File f = new File(fileloc);
		File[] fList = f.listFiles();
		String fileName = null;
		for (File file : fList) {
			if (file.isDirectory()) {
				fileName = file.getName();

				if (fileName.endsWith(".xsd")) {
					parentFileName = file;
					metaInfFilePath = fileName;
					break;
				} else {
					getXSDFolderPath(file.getAbsolutePath());
				}
			}
		}
		return metaInfFilePath;
	}

	private List<String> readXMLFileFromTaxonomyZip(String xmlFileName, String returnCodeVal) {
		final List<String> xsdFileList = new ArrayList<>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(true);
			factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

			SAXParser saxParser = factory.newSAXParser();
			DefaultHandler handler = new DefaultHandler() {
				boolean entryPointDocument = false;
				boolean returnCode = false;
				boolean returnCodeFlag = false;
				boolean xsdListExitFlag = false;

				@Autowired
				public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
					if (qName.equalsIgnoreCase("tp:returnCode")) {
						System.out.println("returnCode :" + qName);
						returnCode = true;
					}

					/*
					 * System.out.println("Start Element :" + qName); if
					 * (qName.equalsIgnoreCase("tp:entryPointDocument")) { String xsdFileName =
					 * attributes.getValue("href");
					 * 
					 * if(!xsdFileList.contains(xsdFileName)) { xsdFileList.add(xsdFileName); }
					 * entryPointDocument = true; }
					 */

					if (returnCodeFlag) {
						if (qName.equalsIgnoreCase(GeneralConstants.XML_ELEMENT_ENTRY_POINT_DOCUMENT.getConstantVal())) {
							if (!xsdListExitFlag) {
								String xsdFileName = attributes.getValue("href");
								xsdFileList.clear();
								if (!xsdFileList.contains(xsdFileName)) {
									/*
									 * String slash = "\\"; String[] xsdFileNameArr = null; // String str =
									 * xsdFileName;
									 * 
									 * if(xsdFileName.contains("/")) { xsdFileNameArr = xsdFileName.split("/"); }
									 * else if(xsdFileName.contains(slash)) { xsdFileName =
									 * xsdFileName.replace("\\", "/"); xsdFileNameArr = xsdFileName.split("/"); }
									 * 
									 * xsdFileList.add(xsdFileNameArr[xsdFileNameArr.length-1]);
									 */
									xsdFileList.add(xsdFileName);
									// returnCodeFlag = false;
									xsdListExitFlag = true;
								}
							}
						}
					} else {
						if (qName.equalsIgnoreCase(GeneralConstants.XML_ELEMENT_ENTRY_POINT_DOCUMENT.getConstantVal())) {
							String xsdFileName = attributes.getValue("href");
							if (!xsdFileList.contains(xsdFileName)) {
								/*
								 * String slash = "\\"; String[] xsdFileNameArr = null; // String str =
								 * xsdFileName;
								 * 
								 * if(xsdFileName.contains("/")) { xsdFileNameArr = xsdFileName.split("/"); }
								 * else if(xsdFileName.contains(slash)) { xsdFileName =
								 * xsdFileName.replace("\\", "/"); xsdFileNameArr = xsdFileName.split("/"); }
								 * 
								 * xsdFileList.add(xsdFileNameArr[xsdFileNameArr.length-1]);
								 */
								xsdFileList.add(xsdFileName);
							}
							entryPointDocument = true;
						}
					}
				}

				public void endElement(String uri, String localName, String qName) throws SAXException {
					System.out.println("End Element :" + qName);

				}

				public void characters(char ch[], int start, int length) throws SAXException {
					if (returnCode) {
						System.out.println("returnCode value : " + new String(ch, start, length));
						String returnCodeValue = new String(ch, start, length);
						if (returnCodeValue.equals(returnCodeVal)) {
							returnCode = false;
							returnCodeFlag = true;
						}
					}
					if (entryPointDocument) {
						// System.out.println("First Name : " + new String(ch, start, length));
						entryPointDocument = false;
					}
				}
			};
			String xmlPath = xmlFileName;
			saxParser.parse(xmlPath, handler);
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Transaction ID : " + e);
		}
		// System.out.println("xsdFileList : " + xsdFileList);
		return xsdFileList;
	}

	public String unZipFile(String zipFilePath, String destinationDirectory) throws Exception {
		String unZipDirectoryPath = null;

		File sourceZipFile = new File(zipFilePath);
		File unzipDestinationDirectory = new File(destinationDirectory);
		File destFile = null;
		File destinationParent = null;

		ZipEntry entry = null;
		String currentEntry = null;

		BufferedInputStream inputStream = null;
		int currentByte;
		byte[] data = null;

		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(sourceZipFile, ZipFile.OPEN_READ);
			Enumeration zipFileEntries = zipFile.entries();
			while (zipFileEntries.hasMoreElements()) {
				// grab a zip file entry
				entry = (ZipEntry) zipFileEntries.nextElement();

				currentEntry = entry.getName().replace("\\", "/");
				// currentEntry = currentEntry.replaceAll("\\s+", "_");
				// unzipDirectory = currentEntry.split("[/]")[0];

				destFile = new File(unzipDestinationDirectory, currentEntry);
				destinationParent = destFile.getParentFile();

				// create the parent directory structure if needed
				destinationParent.mkdirs();

				// extract file if not a directory
				if (!entry.isDirectory()) {
					inputStream = new BufferedInputStream(zipFile.getInputStream(entry));
					// establish buffer for writing file
					data = new byte[(int) sourceZipFile.length()];

					// write the current file to disk
					try (FileOutputStream fosStream = new FileOutputStream(destFile); BufferedOutputStream destStream = new BufferedOutputStream(fosStream, (int) sourceZipFile.length())) {

						if (destStream != null && fosStream != null && inputStream != null) {
							// read and write until last byte is encountered
							while ((currentByte = inputStream.read(data, 0, (int) sourceZipFile.length())) != -1) {
								destStream.write(data, 0, currentByte);
							}
							if (null != destStream) {
								destStream.flush();
								destStream.close();
							}
							if (null != fosStream) {
								fosStream.flush();
								fosStream.close();
							}
							if (null != inputStream) {
								inputStream.close();
							}
						} else {
							throw new Exception("One or more data streams are null while unziping the file.");
						}
					} catch (Exception e) {
						LOGGER.error("Exception occoured in unZipFile method" + e);
					}
				} else {
					parentFileName = destinationParent;
				}
			}
			if (null != zipFile) {
				zipFile.close();
				zipFile = null;
			}
			String originalZipFileName = null;
			String renameFileName = null;

			originalZipFileName = destinationDirectory;
			// renameFileName = destinationDirectory + unZipDirectoryName;

			renameFileName = originalZipFileName;

			File testFile = new File(originalZipFileName);
			File testRenameFile = new File(renameFileName);
			boolean isFileRenamed = testFile.renameTo(testRenameFile);
			LOGGER.info("File Renamed status :" + isFileRenamed);
			unZipDirectoryPath = renameFileName;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (null != zipFile) {
					zipFile.close();
				}
			} catch (Exception e) {
				LOGGER.error("Exception occoured" + e);
			}

			try {
				if (null != inputStream) {
					inputStream.close();
				}
			} catch (Exception e) {
				LOGGER.error("Exception occoured" + e);
			}
		}
		return unZipDirectoryPath;
	}

	@PostMapping(value = "/getReturnTemplateFromReturnIDAndValidFromDateAndVersion")
	public ServiceResponse getReturnTemplateFromReturnIDAndValidFromDateAndVersion(@RequestHeader("JobProcessingId") String jobProcessId, @RequestBody ReturnTemplateDto returnTemplateDto) {
		try {
			Map<String, List<String>> valueMap = new HashMap<>();
			List<String> versionList = new ArrayList<>();
			versionList.add(returnTemplateDto.getVersionNumber());
			valueMap.put(ColumnConstants.RET_TEMP_VERSION.getConstantVal(), versionList);

			// List<String> validFromDateList = new ArrayList<>();
			// validFromDateList.add(returnTemplateDto.getValidFromDate());
			// valueMap.put(ColumnConstants.VALID_FROM_DATE.getConstantVal(),
			// validFromDateList);

			List<String> returnCodes = new ArrayList<>();
			returnCodes.add(returnTemplateDto.getReturnCode());
			valueMap.put(ColumnConstants.RETURN_CODE.getConstantVal(), returnCodes);
			List<ReturnTemplate> returnTemplateList = null;
			if (returnTemplateDto.getIsFormulaGenAction()) {
				returnTemplateList = returnTemplateService.getDataByColumnValue(valueMap, MethodConstants.GET_TAXONOMY_BY_RETURN_ID_AND_FROM_DATE_AND_VERSION_FORMULA.getConstantVal());
			} else {
				returnTemplateList = returnTemplateService.getDataByColumnValue(valueMap, MethodConstants.GET_TAXONOMY_BY_RETURN_ID_AND_FROM_DATE_AND_VERSION.getConstantVal());
			}
			if (CollectionUtils.isEmpty(returnTemplateList)) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0804.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0804.toString())).build();
			}
			List<ReturnTemplateDto> returnTemplateDtoList = new ArrayList<>();
			ReturnTemplateDto retTempObj;
			for (ReturnTemplate returnTemplateObj : returnTemplateList) {
				retTempObj = new ReturnTemplateDto();
				retTempObj.setRetTemplateId(returnTemplateObj.getReturnTemplateId());
				retTempObj.setReturnCode(returnTemplateObj.getReturnObj().getReturnCode());
				retTempObj.setReturnSectionId(returnTemplateObj.getReturnTypeSectionId());
				ReturnLabel returnLabel = returnTemplateObj.getReturnObj().getReturnLblSet().stream().filter(f -> f.getLangIdFk().getLanguageCode().equalsIgnoreCase(returnTemplateDto.getLangCode())).findAny().orElse(null);
				if (returnLabel != null) {
					retTempObj.setReturnName(returnLabel.getReturnLabel());
				}
				retTempObj.setVersionNumber(returnTemplateObj.getVersionNumber());
				retTempObj.setVersionDesc(returnTemplateObj.getVersionDesc());
				retTempObj.setTaxonomyName(returnTemplateObj.getTaxonomyName());
				retTempObj.setReturnType(returnTemplateObj.getReturnTypeIdFk().getReturnTypeDesc());
				retTempObj.setValidFromDate(DateManip.convertDateToString(returnTemplateObj.getValidFromDate(), ObjectCache.getDateFormat()));
				if (Validations.isEmpty(returnTemplateObj.getXsdFileName())) {
					retTempObj.setXsdFileName("-");
				} else {
					retTempObj.setXsdFileName(returnTemplateObj.getXsdFileName());
				}

				if (returnTemplateObj.getModifedOn() != null) {
					retTempObj.setUploadedOn(returnTemplateObj.getModifedOn().getTime());
				} else {
					retTempObj.setUploadedOn(returnTemplateObj.getCreatedOn().getTime());
				}

				if (returnTemplateObj.getModifiedByFk() != null) {
					retTempObj.setUploadedBy(returnTemplateObj.getModifiedByFk().getUserName());
				} else {
					retTempObj.setUploadedBy(returnTemplateObj.getCreatedBy().getUserName());
				}
				returnTemplateDtoList.add(retTempObj);
			}
			return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.RECORD_FETCHED_SUCCESSFULLY.getConstantVal()).setResponse(returnTemplateDtoList).build();
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Transaction ID : " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0804.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0804.toString())).build();
		}
	}

	@PutMapping(value = "/user/{userId}/role/{roleId}/lang/{langCode}/getAllTemplateVersionByReturnType")
	public ServiceResponse findAllTemplateVersionByReturnType(@RequestHeader("JobProcessingId") String jobProcessId, @PathVariable("userId") Long userId, @PathVariable("roleId") String roleId, @PathVariable("langCode") String langCode, @RequestBody ReturnTemplateDto returnTemplateDto) {
		LOGGER.info("START - Fetch return template version by return type request received with Job Processing ID : " + jobProcessId);
		List<ReturnTemplateDto> returnTemplateDtoList = new ArrayList<>();
		try {
			LOGGER.debug("Fetch return template version by return type request return id list - " + returnTemplateDto.getReturnIdList() + ", Return type - " + returnTemplateDto.getReturnType());
			returnTemplateUploadValidator.validateFindAllTemplateVersionByReturnTypeRequest(returnTemplateDto, jobProcessId, userId);
			List<ReturnTemplate> returnTemplates = returnTemplateService.findAllTemplateVersionByReturnType(Long.valueOf(returnTemplateDto.getReturnType()), returnTemplateDto.getReturnIdList());
			if (!CollectionUtils.isEmpty(returnTemplates)) {
				LOGGER.debug("Fetch return template version by return type response size " + returnTemplates.size());
				returnTemplateDtoList = new ArrayList<>();
				for (ReturnTemplate returnTemplate : returnTemplates) {
					ReturnTemplateDto dto = new ReturnTemplateDto();
					dto.setRetTemplateId(returnTemplate.getReturnTemplateId());
					dto.setReturnName(returnTemplate.getReturnObj().getReturnName());
					dto.setReturnCode(returnTemplate.getReturnObj().getReturnCode());
					dto.setVersionNumber(returnTemplate.getVersionNumber());
					returnTemplateDtoList.add(dto);
				}
			} else {
				LOGGER.debug("Fetch return template version by return type response empty");
			}

			LOGGER.info("END - Fetch return template version by return type request completed with Job Processing ID : " + jobProcessId);
			return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.RECORD_FETCHED_SUCCESSFULLY.getConstantVal()).setResponse(returnTemplateDtoList).build();
		} catch (ApplicationException aex) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + " Transaction ID : " + jobProcessId + ", Error Code - " + aex.getErrorCode() + ", Error Msg - " + aex.getErrorMsg(), aex);
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + " Transaction ID : " + jobProcessId, aex);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(aex.getErrorCode()).setStatusMessage(aex.getErrorMsg()).build();
		} catch (Exception ex) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + " Transaction ID : " + jobProcessId, ex);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0692.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0692.toString())).build();
		}
	}

	@PostMapping(value = "/uploadRetTemplateAfterFormulaSave")
	public ServiceResponse uploadRetTemplateAfterFormulaSave(@RequestHeader("JobProcessingId") String jobProcessId, @RequestBody FormulaBean formulaBean) {
		try {
			LOGGER.info("Request received for Request trans Id " + jobProcessId);
			if (formulaBean == null || formulaBean.getReturnTemplateId() == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0841.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0841.toString())).build();
			}
			ReturnTemplate taxonomy = taxonomyRepo.findByReturnTemplateId(formulaBean.getReturnTemplateId());
			taxonomy.setIsActive(formulaBean.getIsActive());
			taxonomy.setSaveFormulaAsDraft(formulaBean.getIsSaveAsDraft());
			taxonomy.setReturnPackage(formulaBean.getZipFileName());
			taxonomyRepo.save(taxonomy);

			return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.RECORD_SAVED_SUCCESSFULLY.getConstantVal()).setResponse(true).build();
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Transaction ID : " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0804.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0804.toString())).build();
			// return new
			// ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorConstants.DEFAULT_ERROR.getConstantVal()).build();
		}
	}

	@PostMapping(value = "/uploadRetTemplateForFormulaEdit")
	public ServiceResponse uploadRetTemplateForFormulaEdit(@RequestHeader("JobProcessingId") String jobProcessId, @RequestBody ReturnTempUploadDTO returnTempUploadDTO) {
		try {
			LOGGER.info("Request received for Request trans Id " + jobProcessId);
			if (returnTempUploadDTO == null) {
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0841.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0841.toString())).build();
			}

			ReturnTemplate taxonomy = taxonomyRepo.findByReturnCodeRetTypeAndVersionNumber(returnTempUploadDTO.getReturnCode(), returnTempUploadDTO.getWebformRetTempUploadDTO().getWebfrmVersionNumber(), 3L);
			taxonomy.setIsActive(false);
			taxonomy.setSaveFormulaAsDraft(false);
			taxonomyRepo.save(taxonomy);

			return new ServiceResponseBuilder().setStatus(true).setStatusCode(GeneralConstants.RECORD_SAVED_SUCCESSFULLY.getConstantVal()).setResponse(true).build();
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal() + "Transaction ID : " + jobProcessId, e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0804.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0804.toString())).build();
			// return new
			// ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorConstants.DEFAULT_ERROR.getConstantVal()).build();
		}
	}

	public File getParentFileName() {
		return parentFileName;
	}

	public void setParentFileName(File parentFileName) {
		this.parentFileName = parentFileName;
	}

	public String getMetaInfFilePath() {
		return metaInfFilePath;
	}

	public void setMetaInfFilePath(String metaInfFilePath) {
		this.metaInfFilePath = metaInfFilePath;
	}
}
