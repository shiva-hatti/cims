package com.iris.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.iris.caching.ObjectCache;
import com.iris.dateutility.util.DateManip;
import com.iris.dto.ServiceResponse;
import com.iris.dto.ServiceResponse.ServiceResponseBuilder;
import com.iris.dto.TableElementMetaInfo;
import com.iris.dto.WebFormToCsvConversionDto;
import com.iris.dto.XmlElementsDto;
import com.iris.formula.gen.controller.AutoCalFormulaParts;
import com.iris.formula.gen.controller.CrossFormulaParts;
import com.iris.formula.gen.service.FormulaGeneratorService;
import com.iris.model.AutoCalculationFormula;
import com.iris.model.ReturnTemplate;
import com.iris.repository.AutoCalculationFormulaRepo;
import com.iris.service.impl.ReturnTemplateService;
import com.iris.util.ResourceUtil;
import com.iris.util.Validations;
import com.iris.util.constant.CsvConversionConstants;
import com.iris.util.constant.ErrorCode;

/**
 * @author Siddique
 */
@RestController
@RequestMapping("/service/webFormFormulaToXMLFormulaService")
public class WebformFormulaToXmlFormulaController {

	static final Logger logger = LogManager.getLogger(WebformFormulaToXmlFormulaController.class);

	@Autowired
	private ReturnTemplateService returnTemplateService;

	@Autowired
	private FormulaGeneratorService formulaGeneratorService;

	@Autowired
	private AutoCalculationFormulaRepo autoCalculationFormulaRepo;

	@PostMapping(value = "/webFormFormulaToXMLFormulaConversion")
	public ServiceResponse webFormFormulaToXMLFormulaConversion(@RequestHeader(name = "JobProcessingId") String jobProcessId, @RequestBody WebFormToCsvConversionDto webFormToCsvConversionDto) throws IOException {

		logger.info("Request received to conversion web form json formula to csv xml formula, job processing id : " + jobProcessId);

		if (webFormToCsvConversionDto == null || webFormToCsvConversionDto.getReturnTemplateId() == 0) {

			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0391.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0391.toString())).build();
		}

		ReturnTemplate returnTemplate = returnTemplateService.fetchEntityByReturnTemplateId(webFormToCsvConversionDto.getReturnTemplateId());
		logger.info("conversion web form json to csv xml formula, job processing id : " + jobProcessId + " for return template id " + webFormToCsvConversionDto.getReturnTemplateId());

		if (returnTemplate == null) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.E0053.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.E0053.toString())).build();
		}

		String returnCode = returnTemplate.getReturnObj().getReturnCode();
		Properties prop = ResourceUtil.getResourcePropertyFile();

		String pathToSaveFile = prop.getProperty(CsvConversionConstants.FILE_PATH_ROOT.getConstantVal()) + CsvConversionConstants.SLASH.getConstantVal() + prop.getProperty(CsvConversionConstants.RETURN_TEMPLATE_UPLOAD_PATH.getConstantVal()) + CsvConversionConstants.SLASH.getConstantVal() + returnCode + CsvConversionConstants.SLASH.getConstantVal() + CsvConversionConstants.CSV_FILE_TEXT.getConstantVal() + CsvConversionConstants.SLASH.getConstantVal() + returnTemplate.getVersionNumber() + CsvConversionConstants.SLASH.getConstantVal();

		File directory = new File(pathToSaveFile);

		if (!directory.exists()) {
			FileUtils.forceMkdir(new File(pathToSaveFile));
		}
		String fileName = returnCode + CsvConversionConstants.UNDERSCORE.getConstantVal() + CsvConversionConstants.BUSINESS_RULE.getConstantVal() + CsvConversionConstants.UNDERSCORE_V.getConstantVal() + returnTemplate.getVersionNumber() + CsvConversionConstants.XML_EXT.getConstantVal();
		String pathOfMetaInfoFile = pathToSaveFile + returnCode + CsvConversionConstants.UNDERSCORE.getConstantVal() + CsvConversionConstants.META_INFO_FILE_TEXT.getConstantVal() + CsvConversionConstants.UNDERSCORE.getConstantVal() + returnTemplate.getVersionNumber() + CsvConversionConstants.TXT_EXT.getConstantVal();
		String pathToSaveXMLFile = pathToSaveFile + fileName;

		returnTemplate.setFormulaFileName(fileName);
		returnTemplate.setXsdFileName(fileName);

		String metaInfoJsonString = readTxtFile(pathOfMetaInfoFile);

		logger.info("conversion web form json to csv xml formula, job processing id : " + jobProcessId + " metaInfoJsonString :  " + metaInfoJsonString);
		if (StringUtils.isBlank(metaInfoJsonString)) {
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}

		Map<String, TableElementMetaInfo> elementColumnPositionMap = new HashMap<>();
		try {
			@SuppressWarnings("serial")
			Type listToken = new TypeToken<Map<String, TableElementMetaInfo>>() {
			}.getType();
			elementColumnPositionMap = new Gson().fromJson(metaInfoJsonString, listToken);

			if (elementColumnPositionMap == null || MapUtils.isEmpty(elementColumnPositionMap)) {
				// todo error key needs to be change
				return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
			}

			List<AutoCalculationFormula> autocalculationList = new ArrayList<>();

			AutoCalculationFormula autoCal = null;
			autoCal = autoCalculationFormulaRepo.findByAutoCalVesrionReturnTemplateFkReturnTemplateId(returnTemplate.getReturnTemplateId());

			if (autoCal == null) {
				autocalculationList = formulaGeneratorService.getAutoCalculationFormulaList(returnTemplate.getReturnObj().getReturnId());
			} else {
				autocalculationList.add(autoCal);
			}

			Map<String, Map<String, List<XmlElementsDto>>> dataMap = new HashMap<>();
			List<String> errorCodeList = new ArrayList<>();

			for (AutoCalculationFormula autoCalculationFormula : autocalculationList) {
				if (StringUtils.isNotBlank(autoCalculationFormula.getFormulaJson())) {
					getDataMapFromJson(autoCalculationFormula.getFormulaJson(), elementColumnPositionMap, errorCodeList, dataMap);
				}
				if (StringUtils.isNotBlank(autoCalculationFormula.getCrossElrJson())) {
					evaluateCrossElrJson(autoCalculationFormula.getCrossElrJson(), dataMap, elementColumnPositionMap, errorCodeList);
				}
			}

			evaluateMetaJsonBean(dataMap, elementColumnPositionMap);

			// xml file creation code
			if (dataMap != null) {
				XMLBuilderFromJson.buildXmlFile(dataMap, pathToSaveXMLFile);
			}

			returnTemplateService.add(returnTemplate);

			Map<String, String> filePathToSendInResponseMap = new HashMap<>();
			filePathToSendInResponseMap.put(CsvConversionConstants.XML_FILE_PATH.getConstantVal(), pathToSaveXMLFile);

			return new ServiceResponseBuilder().setStatus(true).setResponse(filePathToSendInResponseMap).build();
		} catch (Exception e) {
			logger.error("Exception occoured while conversion of web form formula json to xml file: ", e);
			return new ServiceResponseBuilder().setStatus(false).setStatusCode(ErrorCode.EC0033.toString()).setStatusMessage(ObjectCache.getErrorCodeKey(ErrorCode.EC0033.toString())).build();
		}
	}

	private void evaluateCrossElrJson(String crossElrJson, Map<String, Map<String, List<XmlElementsDto>>> dataMap, Map<String, TableElementMetaInfo> elementColumnPositionMap, List<String> errorCodeList) {
		TypeReference<Map<String, List<CrossFormulaParts>>> crossFormulaType = new TypeReference<Map<String, List<CrossFormulaParts>>>() {
		};
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.readValue(crossElrJson, crossFormulaType);
			Map<String, List<CrossFormulaParts>> formulaJsonJavaObject = mapper.readValue(crossElrJson, crossFormulaType);
			XmlElementsDto xmlElementsDto = null;

			if (formulaJsonJavaObject != null) {
				Set<Entry<String, List<CrossFormulaParts>>> entry = formulaJsonJavaObject.entrySet();
				Iterator<Entry<String, List<CrossFormulaParts>>> entryIterator = entry.iterator();
				while (entryIterator.hasNext()) {
					Entry<String, List<CrossFormulaParts>> llt = entryIterator.next();
					List<CrossFormulaParts> list = llt.getValue();
					String formula = StringUtils.EMPTY;
					for (CrossFormulaParts pparts : list) {
						xmlElementsDto = new XmlElementsDto();
						if (pparts.getFormulaType().equalsIgnoreCase(CsvConversionConstants.EQUATION.getConstantVal()) && !errorCodeList.contains(pparts.getErrorCode())) {
							errorCodeList.add(pparts.getErrorCode());
							String exp = StringUtils.EMPTY;
							TableElementMetaInfo tableElementMetaInfo = null;

							exp = pparts.getExp();
							tableElementMetaInfo = elementColumnPositionMap.get(pparts.getLhs().substring(3));
							xmlElementsDto.setTableCode(tableElementMetaInfo.getCsvTableCode());
							formula = getFormualColumnPosition(tableElementMetaInfo, exp, pparts.getRhs(), elementColumnPositionMap);
							String formulaTagAndConditionTag = createFormulaTagAndConditionTag(pparts.getRhs(), elementColumnPositionMap);

							if (StringUtils.isNotBlank(formulaTagAndConditionTag) && formulaTagAndConditionTag.split(CsvConversionConstants.TILDA.getConstantVal()).length == 2) {
								if (!formulaTagAndConditionTag.split(CsvConversionConstants.TILDA.getConstantVal())[0].contains(CsvConversionConstants.UNDERSCORE_0.getConstantVal())) { // if it contain _0 then no need to add this in xml file
									xmlElementsDto.setConditionTag(formulaTagAndConditionTag.split(CsvConversionConstants.TILDA.getConstantVal())[0]);
									xmlElementsDto.setFormulaTag(formulaTagAndConditionTag.split(CsvConversionConstants.TILDA.getConstantVal())[1]);
									xmlElementsDto.setIsRowPositionCheck(CsvConversionConstants.TRUE.getConstantVal());
									xmlElementsDto.setIsValidationOnParticularRow(CsvConversionConstants.TRUE.getConstantVal());
								}
							}

							xmlElementsDto.setFormulaType(CsvConversionConstants.SUMMATION.getConstantVal());
							xmlElementsDto.setErrCode(pparts.getErrorCode());
							xmlElementsDto.setErrType(CsvConversionConstants.ERROR.getConstantVal());

							if (StringUtils.isNotBlank(formula)) {
								xmlElementsDto.setFormula(formula.replaceAll(" +", " "));
								createRetMap(xmlElementsDto, dataMap);
							}
						}
						// date compare
						// add here summation formula of LHS and RHS
						if ((StringUtils.isNotBlank(pparts.getFormulaType()) && pparts.getFormulaType().equals(CsvConversionConstants.DATE_CHK.getConstantVal())) && !errorCodeList.contains(pparts.getErrorCode())) {
							errorCodeList.add(pparts.getErrorCode());
							String exp = StringUtils.EMPTY;
							TableElementMetaInfo tableElementMetaInfo = null;
							TableElementMetaInfo tableElementMetaInfoForAnotherTable = null;
							xmlElementsDto = new XmlElementsDto();
							exp = pparts.getExp();
							tableElementMetaInfo = elementColumnPositionMap.get(pparts.getLhs().substring(3));

							if (tableElementMetaInfo.getCsvTableCode().contains(CsvConversionConstants.T001.getConstantVal())) {
								if (exp.equals(CsvConversionConstants.LESS_THAN_EQUAL.getConstantVal())) {
									exp = CsvConversionConstants.GREATER_THAN_EQUAL.getConstantVal();
								} else if (exp.equals(CsvConversionConstants.GREATER_THAN_EQUAL.getConstantVal())) {
									exp = CsvConversionConstants.LESS_THAN_EQUAL.getConstantVal();
								} else if (exp.equals(CsvConversionConstants.GREATER_THAN.getConstantVal())) {
									exp = CsvConversionConstants.LESS_THAN.getConstantVal();
								} else if (exp.equals(CsvConversionConstants.LESS_THAN.getConstantVal())) {
									exp = CsvConversionConstants.GREATER_THAN.getConstantVal();
								}
							}
							tableElementMetaInfoForAnotherTable = elementColumnPositionMap.get(pparts.getRhs().substring(3));

							formula = getFormualColumnPosition(tableElementMetaInfo.getCsvTableCode().contains(CsvConversionConstants.T001.getConstantVal()) ? tableElementMetaInfoForAnotherTable : tableElementMetaInfo, exp, tableElementMetaInfo.getCsvTableCode().contains(CsvConversionConstants.T001.getConstantVal()) ? pparts.getLhs() : pparts.getRhs(), elementColumnPositionMap);
							if (tableElementMetaInfo.getCsvTableCode().contains(CsvConversionConstants.T001.getConstantVal()) || formula.contains(CsvConversionConstants.HASH_6.getConstantVal()) || tableElementMetaInfoForAnotherTable.getCsvTableCode().contains(CsvConversionConstants.T001.getConstantVal())) {
								xmlElementsDto.setFormulaTag(tableElementMetaInfo.getCsvTableCode().contains(CsvConversionConstants.T001.getConstantVal()) ? tableElementMetaInfo.getCsvTableCode() : tableElementMetaInfoForAnotherTable.getCsvTableCode());
								xmlElementsDto.setConditionTag(CsvConversionConstants.REPORTING_DATE_POSITION.getConstantVal());
								xmlElementsDto.setTableCode(tableElementMetaInfo.getCsvTableCode().contains(CsvConversionConstants.T001.getConstantVal()) ? tableElementMetaInfoForAnotherTable.getCsvTableCode() : tableElementMetaInfo.getCsvTableCode());

							} else {
								xmlElementsDto.setTableCode(tableElementMetaInfo.getCsvTableCode());
							}

							xmlElementsDto.setCondition(CsvConversionConstants.DATE_FORMAT_DDMMYYYY.getConstantVal());
							xmlElementsDto.setFormula(formula.replaceAll(" +", " "));
							xmlElementsDto.setFormulaType(CsvConversionConstants.DATE_COMP.getConstantVal());
							xmlElementsDto.setErrCode(pparts.getErrorCode());
							xmlElementsDto.setErrType(CsvConversionConstants.ERROR.getConstantVal());
							createRetMap(xmlElementsDto, dataMap);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception occoured:1 " + e);
		}
	}

	private String createFormulaTagAndConditionTag(String formula, Map<String, TableElementMetaInfo> elementColumnPositionMap) {
		logger.info("inside createFormulaTagAndConditionTag");
		StringBuilder formulaWithValueForConditionTag = new StringBuilder();
		StringBuilder formulaWithValueForFormulTag = new StringBuilder();

		StringTokenizer stringTokenizer = new StringTokenizer(formula);
		while (stringTokenizer.hasMoreElements()) {

			String tokenValForColumnForConditionTag = StringUtils.EMPTY;
			String tokenValForColumnForFormulTag = StringUtils.EMPTY;
			String token = (String) stringTokenizer.nextElement();

			if (token.startsWith(CsvConversionConstants.HASH_SIGN.getConstantVal()) && elementColumnPositionMap.get(token.substring(3)) != null) {
				tokenValForColumnForConditionTag = elementColumnPositionMap.get(token.substring(3)).getCsvTableCode() + CsvConversionConstants.UNDERSCORE.getConstantVal() + elementColumnPositionMap.get(token.substring(3)).getCsvRowPosition() + CsvConversionConstants.UNDERSCORE.getConstantVal() + elementColumnPositionMap.get(token.substring(3)).getCsvColumnPosition();
				tokenValForColumnForFormulTag = elementColumnPositionMap.get(token.substring(3)).getCsvTableCode();

				formulaWithValueForConditionTag.append(tokenValForColumnForConditionTag + CsvConversionConstants.COMMA_SIGN.getConstantVal());
				if (!formulaWithValueForFormulTag.toString().contains(tokenValForColumnForFormulTag)) {
					formulaWithValueForFormulTag.append(tokenValForColumnForFormulTag + CsvConversionConstants.COMMA_SIGN.getConstantVal());
				}
			}
		}
		return formulaWithValueForConditionTag.toString().substring(0, formulaWithValueForConditionTag.toString().length() - 1) + CsvConversionConstants.TILDA.getConstantVal() + formulaWithValueForFormulTag.toString().substring(0, formulaWithValueForFormulTag.toString().length() - 1);
	}

	private void evaluateMetaJsonBean(Map<String, Map<String, List<XmlElementsDto>>> dataMap, Map<String, TableElementMetaInfo> elementColumnPositionMap) {
		XmlElementsDto xmlElementsDto = null;
		for (TableElementMetaInfo map : elementColumnPositionMap.values()) {
			if (StringUtils.isNotBlank(map.getRegex())) {
				xmlElementsDto = new XmlElementsDto();
				xmlElementsDto.setFormulaType(CsvConversionConstants.REGEX.getConstantVal());
				xmlElementsDto.setErrType(CsvConversionConstants.ERROR.getConstantVal());
				xmlElementsDto.setErrCode(CsvConversionConstants.ER000T00001.getConstantVal());
				xmlElementsDto.setCondition(map.getRegex());
				xmlElementsDto.setTableCode(map.getCsvTableCode());
				xmlElementsDto.setIsNullAllow(String.valueOf(map.isNullAllow()));
				if (map.getCsvRowPosition().equals(CsvConversionConstants.ZERO.getConstantVal())) {
					xmlElementsDto.setFormula(map.getCsvColumnPosition());
					createRetMap(xmlElementsDto, dataMap);
				} else {
					if (!map.getCsvColumnPosition().equals(CsvConversionConstants.ONE.getConstantVal())) {
						xmlElementsDto.setFormula(map.getCsvRowPosition() + CsvConversionConstants.UNDERSCORE.getConstantVal() + map.getCsvColumnPosition());
						createRetMap(xmlElementsDto, dataMap);
					}
				}
			}
			if (map.getMinLength() > 0 || map.getMaxLength() > 0) {
				xmlElementsDto = new XmlElementsDto();
				xmlElementsDto.setFormulaType(CsvConversionConstants.LENGTH_CHECK.getConstantVal());
				xmlElementsDto.setErrType(CsvConversionConstants.ERROR.getConstantVal());
				xmlElementsDto.setErrCode(CsvConversionConstants.ER000T00002.getConstantVal());
				xmlElementsDto.setTableCode(map.getCsvTableCode());
				xmlElementsDto.setMinLength(map.getMinLength());
				xmlElementsDto.setMaxLength(map.getMaxLength());
				xmlElementsDto.setIsNullAllow(String.valueOf(map.isNullAllow()));
				if (map.getCsvRowPosition().equals(CsvConversionConstants.ZERO.getConstantVal())) {
					xmlElementsDto.setFormula(map.getCsvColumnPosition());
					createRetMap(xmlElementsDto, dataMap);
				} else {
					if (!map.getCsvColumnPosition().equals(CsvConversionConstants.ONE.getConstantVal())) {
						xmlElementsDto.setFormula(map.getCsvRowPosition() + CsvConversionConstants.UNDERSCORE.getConstantVal() + map.getCsvColumnPosition());
						createRetMap(xmlElementsDto, dataMap);
					}
				}
			}
			if (map.getIsMandatory()) {
				xmlElementsDto = new XmlElementsDto();
				xmlElementsDto.setFormulaType(CsvConversionConstants.MENDATORY.getConstantVal());
				xmlElementsDto.setErrType(CsvConversionConstants.ERROR.getConstantVal());
				xmlElementsDto.setErrCode(CsvConversionConstants.ER000T00003.getConstantVal());
				xmlElementsDto.setTableCode(map.getCsvTableCode());
				if (map.getCsvRowPosition().equals(CsvConversionConstants.ZERO.getConstantVal())) {
					xmlElementsDto.setFormula(map.getCsvColumnPosition());
					createRetMap(xmlElementsDto, dataMap);
				} else {
					if (!map.getCsvColumnPosition().equals(CsvConversionConstants.ONE.getConstantVal())) {
						xmlElementsDto.setFormula(map.getCsvRowPosition() + CsvConversionConstants.UNDERSCORE.getConstantVal() + map.getCsvColumnPosition());
						createRetMap(xmlElementsDto, dataMap);
					}
				}
			}
			if (map.getFieldTypeId() == 3) {
				xmlElementsDto = new XmlElementsDto();
				xmlElementsDto.setFormulaType(CsvConversionConstants.DATE_CHECK.getConstantVal());
				xmlElementsDto.setErrType(CsvConversionConstants.ERROR.getConstantVal());
				xmlElementsDto.setErrCode(CsvConversionConstants.ER000T00004.getConstantVal());
				xmlElementsDto.setIsNullAllow(String.valueOf(map.isNullAllow()));
				xmlElementsDto.setTableCode(map.getCsvTableCode());
				if (map.getCsvRowPosition().equals(CsvConversionConstants.ZERO.getConstantVal())) {
					xmlElementsDto.setFormula(map.getCsvColumnPosition());
					createRetMap(xmlElementsDto, dataMap);
				} else {
					if (!map.getCsvColumnPosition().equals(CsvConversionConstants.ONE.getConstantVal())) {
						xmlElementsDto.setFormula(map.getCsvRowPosition() + CsvConversionConstants.UNDERSCORE.getConstantVal() + map.getCsvColumnPosition());
						createRetMap(xmlElementsDto, dataMap);
					}
				}
				xmlElementsDto.setCondition(CsvConversionConstants.DATE_FORMAT_DDMMYYYY.getConstantVal());
			}

			if (map.getFieldTypeId() == 2 && StringUtils.isNotBlank(map.getMethodURI()) && StringUtils.isNotBlank(map.getMethodType()) && StringUtils.isBlank(map.getFormula())) {
				xmlElementsDto = new XmlElementsDto();
				xmlElementsDto.setFormulaType(CsvConversionConstants.VALUE_CHECK.getConstantVal());
				xmlElementsDto.setErrType(CsvConversionConstants.ERROR.getConstantVal());
				xmlElementsDto.setErrCode(CsvConversionConstants.ER000T00005.getConstantVal());
				xmlElementsDto.setIsNullAllow(String.valueOf(map.isNullAllow()));
				xmlElementsDto.setTableCode(map.getCsvTableCode());
				xmlElementsDto.setMethodURI(map.getMethodURI());
				xmlElementsDto.setMethodType(map.getMethodType());
				xmlElementsDto.setFieldTypeId(String.valueOf(map.getFieldTypeId()));
				if (map.getCsvRowPosition().equals(CsvConversionConstants.ZERO.getConstantVal())) {
					xmlElementsDto.setFormula(map.getCsvColumnPosition());
					createRetMap(xmlElementsDto, dataMap);
				} else {
					if (!map.getCsvColumnPosition().equals(CsvConversionConstants.ONE.getConstantVal())) {
						xmlElementsDto.setFormula(map.getCsvRowPosition() + CsvConversionConstants.UNDERSCORE.getConstantVal() + map.getCsvColumnPosition());
						createRetMap(xmlElementsDto, dataMap);
					}
				}
			}

			if (StringUtils.isNotBlank(map.getMethodURI()) && StringUtils.isNotBlank(map.getMethodType()) && StringUtils.isNotBlank(map.getFormula()) && StringUtils.isNotBlank(map.getFormulaType())) {
				xmlElementsDto = new XmlElementsDto();
				xmlElementsDto.setFormulaType(map.getFormulaType());
				xmlElementsDto.setErrType(CsvConversionConstants.ERROR.getConstantVal());
				xmlElementsDto.setErrCode(map.getErrorCode());
				xmlElementsDto.setIsNullAllow(String.valueOf(map.isNullAllow()));
				xmlElementsDto.setTableCode(map.getCsvTableCode());
				xmlElementsDto.setMethodURI(map.getMethodURI());
				xmlElementsDto.setMethodType(map.getMethodType());
				xmlElementsDto.setFieldTypeId(String.valueOf(map.getFieldTypeId()));
				xmlElementsDto.setFormula(map.getFormula());
				xmlElementsDto.setIsKeyFetch(String.valueOf(map.isKeyFetch()));
				if (map.isPairCheck()) {
					xmlElementsDto.setIsPairCheck(String.valueOf(map.isPairCheck()));
					xmlElementsDto.setSplitType(map.getSplitType());
				}
				createRetMap(xmlElementsDto, dataMap);
			}

			if (StringUtils.isNotBlank(map.getConditionTag()) && StringUtils.isNotBlank(map.getFormulaTag()) && StringUtils.isNotBlank(map.getFormula()) && StringUtils.isNotBlank(map.getErrorCode())) {
				xmlElementsDto = new XmlElementsDto();
				xmlElementsDto.setFormulaType(CsvConversionConstants.SUMMATION.getConstantVal());
				xmlElementsDto.setErrType(CsvConversionConstants.ERROR.getConstantVal());
				xmlElementsDto.setErrCode(map.getErrorCode());
				xmlElementsDto.setIsNullAllow(String.valueOf(map.isNullAllow()));
				xmlElementsDto.setTableCode(map.getCsvTableCode());
				xmlElementsDto.setConditionTag(map.getConditionTag());
				xmlElementsDto.setFormulaTag(map.getFormulaTag());
				xmlElementsDto.setIsRowPositionCheck(String.valueOf(map.isRowPositionCheck()));
				xmlElementsDto.setIsValidationOnParticularRow(String.valueOf(map.isValidationOnParticularRow()));
				xmlElementsDto.setFieldTypeId(String.valueOf(map.getFieldTypeId()));
				xmlElementsDto.setFormula(map.getFormula());
				createRetMap(xmlElementsDto, dataMap);
			}

			if (map.isRowCountValidation() && StringUtils.isNotBlank(map.getErrorCode()) && StringUtils.isNotBlank(map.getMaxRowCount())) {
				xmlElementsDto = new XmlElementsDto();
				xmlElementsDto.setFormulaType(CsvConversionConstants.ROW_COUNT_COMPARE.getConstantVal());
				xmlElementsDto.setErrType(CsvConversionConstants.ERROR.getConstantVal());
				xmlElementsDto.setErrCode(map.getErrorCode());
				xmlElementsDto.setTableCode(map.getCsvTableCode());
				xmlElementsDto.setFieldTypeId(String.valueOf(map.getFieldTypeId()));
				xmlElementsDto.setMaxRowCount(map.getMaxRowCount());
				createRetMap(xmlElementsDto, dataMap);
			}

			if (StringUtils.isNotBlank(map.getFormulaType()) && map.getFormulaType().equals(CsvConversionConstants.CHILD_TOTAL.getConstantVal()) && StringUtils.isNotBlank(map.getErrorCode()) && !map.isGrandTotalPresent()) {
				xmlElementsDto = new XmlElementsDto();
				xmlElementsDto.setFormulaType(CsvConversionConstants.CHILD_TOTAL.getConstantVal());
				xmlElementsDto.setErrType(CsvConversionConstants.ERROR.getConstantVal());
				xmlElementsDto.setErrCode(map.getErrorCode());
				xmlElementsDto.setTableCode(map.getCsvTableCode());
				xmlElementsDto.setFieldTypeId(String.valueOf(map.getFieldTypeId()));
				xmlElementsDto.setFormula(map.getFormula());
				xmlElementsDto.setGrandTotalAvailable(map.isGrandTotalPresent());
				createRetMap(xmlElementsDto, dataMap);
			}

			if (map.getIsUniqueValueCheck() != null) {
				xmlElementsDto = new XmlElementsDto();
				xmlElementsDto.setFormulaType(CsvConversionConstants.UNIQUE_VALUE_CHECK.getConstantVal());
				xmlElementsDto.setErrType(CsvConversionConstants.ERROR.getConstantVal());
				xmlElementsDto.setErrCode(map.getErrorCodeForUniqueValueCheck());
				xmlElementsDto.setTableCode(map.getCsvTableCode());
				xmlElementsDto.setFieldTypeId(String.valueOf(map.getFieldTypeId()));
				xmlElementsDto.setFormula(map.getFormulaForUniqueValueCheck());
				xmlElementsDto.setIsNullAllow(String.valueOf(map.isNullAllow()));
				xmlElementsDto.setIsUniqueValueCheck(map.getIsUniqueValueCheck());
				if (map.isPairCheck()) {
					xmlElementsDto.setIsPairCheck(String.valueOf(map.isPairCheck()));
					xmlElementsDto.setSplitType(map.getSplitType());
				}
				createRetMap(xmlElementsDto, dataMap);
			}

			if (!StringUtils.isBlank(map.getFormulaType()) && map.getFormulaType().equals(CsvConversionConstants.DATE_COMP.getConstantVal()) && (map.isMonthComparison() || map.isYearComparison())) {
				if (map.isMonthComparison()) {
					xmlElementsDto = new XmlElementsDto();
					xmlElementsDto.setFormulaType(CsvConversionConstants.DATE_COMP.getConstantVal());
					xmlElementsDto.setFormula(map.getFormula());
					xmlElementsDto.setErrType(CsvConversionConstants.ERROR.getConstantVal());
					xmlElementsDto.setErrCode(map.getErrorCodeForMonthCompare());
					xmlElementsDto.setIsNullAllow(String.valueOf(map.isNullAllow()));
					xmlElementsDto.setCondition(map.getConditionForMonthCompare());
					xmlElementsDto.setReportedDateFormat(map.getReportedDateFormat());
					xmlElementsDto.setConditionTag(map.getConditionTag());
					xmlElementsDto.setFormulaTag(map.getFormulaTag());
					xmlElementsDto.setTableCode(map.getCsvTableCode());
					xmlElementsDto.setIsMonthComparison(String.valueOf(map.isMonthComparison()));
					createRetMap(xmlElementsDto, dataMap);

				}
				if (map.isYearComparison()) {
					xmlElementsDto = new XmlElementsDto();
					xmlElementsDto.setFormulaType(CsvConversionConstants.DATE_COMP.getConstantVal());
					xmlElementsDto.setFormula(map.getFormula());
					xmlElementsDto.setErrType(CsvConversionConstants.ERROR.getConstantVal());
					xmlElementsDto.setErrCode(map.getErrorCodeForYearCompare());
					xmlElementsDto.setIsNullAllow(String.valueOf(map.isNullAllow()));
					xmlElementsDto.setCondition(map.getConditionForYearCompare());
					xmlElementsDto.setReportedDateFormat(map.getReportedDateFormat());
					xmlElementsDto.setConditionTag(map.getConditionTag());
					xmlElementsDto.setFormulaTag(map.getFormulaTag());
					xmlElementsDto.setTableCode(map.getCsvTableCode());
					xmlElementsDto.setIsYearComparison(String.valueOf(map.isYearComparison()));
					createRetMap(xmlElementsDto, dataMap);

				}

			}

		}
	}

	private Map<String, Map<String, List<XmlElementsDto>>> getDataMapFromJson(String formulaJson, Map<String, TableElementMetaInfo> elementColumnPositionMap, List<String> errorCodeList, Map<String, Map<String, List<XmlElementsDto>>> retMap) {

		// conversion of formula json to java bean
		TypeReference<Map<String, Map<String, Map<String, List<AutoCalFormulaParts>>>>> autoFormulaType = new TypeReference<Map<String, Map<String, Map<String, List<AutoCalFormulaParts>>>>>() {
		};
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.readValue(formulaJson, autoFormulaType);
			Map<String, Map<String, Map<String, List<AutoCalFormulaParts>>>> formulaJsonJavaObject = mapper.readValue(formulaJson, autoFormulaType);
			XmlElementsDto xmlElementsDto = null;

			if (formulaJsonJavaObject != null) {
				Map<String, Map<String, List<AutoCalFormulaParts>>> onBlurMap = formulaJsonJavaObject.get(CsvConversionConstants.CUSTOM_CALCULATION.getConstantVal());
				Map<String, List<AutoCalFormulaParts>> innerd = onBlurMap.get(CsvConversionConstants.ON_BLUR_EVENT.getConstantVal());
				Map<String, List<AutoCalFormulaParts>> onBlurSummationEvent = onBlurMap.get(CsvConversionConstants.ON_BLUR_SUMMATION_EVENT.getConstantVal());
				Map<String, List<AutoCalFormulaParts>> onchangeEvent = onBlurMap.get(CsvConversionConstants.ON_CHANGE_EVENT.getConstantVal());
				int count = 0;

				// on blur Event start
				if (!CollectionUtils.isEmpty(innerd)) {
					for (Entry<String, List<AutoCalFormulaParts>> item : innerd.entrySet()) {
						ListIterator<AutoCalFormulaParts> listIterator = item.getValue().listIterator();

						while (listIterator.hasNext()) {
							logger.info("++count:: ", + ++count);
							AutoCalFormulaParts pparts = listIterator.next();
							String formula = "";
							logger.info("Expression :: " + pparts.getExp());
							logger.info("Error codes:: " + pparts.getErrorCode());

							if (StringUtils.isBlank(pparts.getFormulaRef())) { // if formulaRef is present then skip such rules
								if (elementColumnPositionMap.get(pparts.getFormulaCell().substring(2)) != null) {
									xmlElementsDto = new XmlElementsDto();
									if (StringUtils.isNotBlank(pparts.getFormulaCell()) && StringUtils.isNotBlank(pparts.getFormula()) && (!errorCodeList.contains(pparts.getErrorCode()) || StringUtils.isBlank(pparts.getErrorCode()))) {
										errorCodeList.add(pparts.getErrorCode());
										TableElementMetaInfo tableElementMetaInfo = elementColumnPositionMap.get(pparts.getFormulaCell().substring(2));
										xmlElementsDto.setFormulaType(CsvConversionConstants.SUMMATION.getConstantVal());
										xmlElementsDto.setErrCode(pparts.getErrorCode());
										if (!Validations.isEmpty(pparts.getErrorType())) {
											xmlElementsDto.setErrType(pparts.getErrorType().equalsIgnoreCase(CsvConversionConstants.E.getConstantVal()) ? CsvConversionConstants.ERROR.getConstantVal() : CsvConversionConstants.WARNING.getConstantVal());
										} else {
											xmlElementsDto.setErrType(CsvConversionConstants.ERROR.getConstantVal());
										}
										xmlElementsDto.setTableCode(tableElementMetaInfo.getCsvTableCode());
										xmlElementsDto.setGrandTotalAvailable(tableElementMetaInfo.isGrandTotalPresent());

										String exp = StringUtils.EMPTY;
										if (StringUtils.isBlank(pparts.getExp())) {
											exp = CsvConversionConstants.DOUBLE_EQUAL.getConstantVal();
										} else {
											exp = pparts.getExp();
										}
										formula = getFormualColumnPosition(tableElementMetaInfo, exp, pparts.getFormula(), elementColumnPositionMap);

										if (StringUtils.contains(formula, CsvConversionConstants.UNDERSCORE.getConstantVal())) {
											String formulaTagAndConditionTag = createFormulaTagAndConditionTag(pparts.getFormula(), elementColumnPositionMap);

											if (StringUtils.isNotBlank(formulaTagAndConditionTag) && formulaTagAndConditionTag.split(CsvConversionConstants.TILDA.getConstantVal()).length == 2) {
												xmlElementsDto.setConditionTag(formulaTagAndConditionTag.split(CsvConversionConstants.TILDA.getConstantVal())[0]);
												xmlElementsDto.setFormulaTag(formulaTagAndConditionTag.split(CsvConversionConstants.TILDA.getConstantVal())[1]);
												xmlElementsDto.setIsRowPositionCheck(CsvConversionConstants.TRUE.getConstantVal());
												xmlElementsDto.setIsValidationOnParticularRow(CsvConversionConstants.TRUE.getConstantVal());
											}
										}
										if (StringUtils.isNotBlank(formula)) {
											if (StringUtils.isNumeric(formula)) {
												xmlElementsDto.setFormulaType(CsvConversionConstants.CHILD_TOTAL.getConstantVal());
												xmlElementsDto.setGrandTotalAvailable(true);
											}
											xmlElementsDto.setFormula(formula);
											createRetMap(xmlElementsDto, retMap);
										}
									}
								}
								if (StringUtils.isNotBlank(pparts.getRhs()) && StringUtils.isNotBlank(pparts.getExp()) && StringUtils.isNotBlank(pparts.getLhs()) && !errorCodeList.contains(pparts.getErrorCode())) {
									errorCodeList.add(pparts.getErrorCode());
									// date compare
									// add here summation formula of LHS and RHS
									if ((StringUtils.isNotBlank(pparts.getFormulaType()) && pparts.getFormulaType().equals(CsvConversionConstants.DATE_CHK.getConstantVal())) || (pparts.getFormulaCell().equalsIgnoreCase(CsvConversionConstants.NA.getConstantVal()) && pparts.getFormulaType().equals(CsvConversionConstants.EQUATION.getConstantVal()))) {
										xmlElementsDto = new XmlElementsDto();
										TableElementMetaInfo tableElementMetaInfo = elementColumnPositionMap.get(pparts.getLhs().substring(3));
										if (pparts.getFormulaType().equals(CsvConversionConstants.EQUATION.getConstantVal())) {
											xmlElementsDto.setFormulaType(CsvConversionConstants.SUMMATION.getConstantVal());
										} else {
											xmlElementsDto.setFormulaType(CsvConversionConstants.DATE_COMP.getConstantVal());
											xmlElementsDto.setCondition(CsvConversionConstants.DATE_FORMAT_DDMMYYYY.getConstantVal());
										}
										xmlElementsDto.setErrCode(pparts.getErrorCode());
										if (!Validations.isEmpty(pparts.getErrorType())) {
											xmlElementsDto.setErrType(pparts.getErrorType().equalsIgnoreCase(CsvConversionConstants.E.getConstantVal()) ? CsvConversionConstants.ERROR.getConstantVal() : CsvConversionConstants.WARNING.getConstantVal());
										} else {
											xmlElementsDto.setErrType(CsvConversionConstants.ERROR.getConstantVal());
										}
										if (tableElementMetaInfo.getCsvTableCode().contains(CsvConversionConstants.T001.getConstantVal())) {
											xmlElementsDto.setTableCode(tableElementMetaInfo.getCsvTableCode());
										} else {
											xmlElementsDto.setTableCode(elementColumnPositionMap.get(pparts.getLhs().substring(3)).getCsvTableCode());
										}

										String exp = StringUtils.EMPTY;
										if (StringUtils.isBlank(pparts.getExp())) {
											exp = CsvConversionConstants.DOUBLE_EQUAL.getConstantVal();
										} else {
											exp = pparts.getExp();
										}
										formula = getFormualColumnPosition(tableElementMetaInfo, exp, pparts.getRhs(), elementColumnPositionMap);
										if (StringUtils.contains(formula, CsvConversionConstants.UNDERSCORE.getConstantVal())) {
											String formulaTagAndConditionTag = createFormulaTagAndConditionTag(pparts.getRhs(), elementColumnPositionMap);

											if (StringUtils.isNotBlank(formulaTagAndConditionTag) && formulaTagAndConditionTag.split(CsvConversionConstants.TILDA.getConstantVal()).length == 2) {
												xmlElementsDto.setConditionTag(formulaTagAndConditionTag.split(CsvConversionConstants.TILDA.getConstantVal())[0]);
												xmlElementsDto.setFormulaTag(formulaTagAndConditionTag.split(CsvConversionConstants.TILDA.getConstantVal())[1]);
												xmlElementsDto.setIsRowPositionCheck(CsvConversionConstants.TRUE.getConstantVal());
												xmlElementsDto.setIsValidationOnParticularRow(CsvConversionConstants.TRUE.getConstantVal());
											}
										}

										if (StringUtils.isNotBlank(formula)) {
											if (StringUtils.isNumeric(formula)) {
												xmlElementsDto.setFormulaType(CsvConversionConstants.CHILD_TOTAL.getConstantVal());
												xmlElementsDto.setGrandTotalAvailable(true);
											}
											xmlElementsDto.setFormula(formula.replaceAll(" +", " ")); // replace mutiple space with single 
											createRetMap(xmlElementsDto, retMap);
										}
									}
								}
							}
						}
					}
				}

				// on Blur summation event start
				if (!CollectionUtils.isEmpty(onBlurSummationEvent)) {
					for (Entry<String, List<AutoCalFormulaParts>> items : onBlurSummationEvent.entrySet()) {
						ListIterator<AutoCalFormulaParts> listIterator = items.getValue().listIterator();

						while (listIterator.hasNext()) {
							logger.info("Repeatable summation json starts:: ");
							AutoCalFormulaParts autoCalFormulaParts = listIterator.next();
							String formula = "";
							logger.info("Expression of repeatable summation json:: " + autoCalFormulaParts.getExp());
							logger.info("Error codes of repeatable summation json:: " + autoCalFormulaParts.getErrorCode());

							if (StringUtils.isBlank(autoCalFormulaParts.getFormulaRef())) { // if formulaRef is present then skip such rules
								if (elementColumnPositionMap.get(autoCalFormulaParts.getFormula().substring(3)) != null) {
									xmlElementsDto = new XmlElementsDto();
									if (StringUtils.isNotBlank(autoCalFormulaParts.getFormulaCell()) && StringUtils.isNotBlank(autoCalFormulaParts.getFormula()) && (!errorCodeList.contains(autoCalFormulaParts.getErrorCode()) || StringUtils.isBlank(autoCalFormulaParts.getErrorCode()))) {
										errorCodeList.add(autoCalFormulaParts.getErrorCode());

										TableElementMetaInfo tableElementMetaInfo = elementColumnPositionMap.get(autoCalFormulaParts.getFormula().substring(3));
										xmlElementsDto.setErrCode(autoCalFormulaParts.getErrorCode());
										if (!Validations.isEmpty(autoCalFormulaParts.getErrorType())) {
											xmlElementsDto.setErrType(autoCalFormulaParts.getErrorType().equalsIgnoreCase(CsvConversionConstants.E.getConstantVal()) ? CsvConversionConstants.ERROR.getConstantVal() : CsvConversionConstants.WARNING.getConstantVal());
										} else {
											xmlElementsDto.setErrType(CsvConversionConstants.ERROR.getConstantVal());
										}
										xmlElementsDto.setTableCode(tableElementMetaInfo.getCsvTableCode());
										xmlElementsDto.setGrandTotalAvailable(tableElementMetaInfo.isGrandTotalPresent());

										String exp = StringUtils.EMPTY;
										if (StringUtils.isBlank(autoCalFormulaParts.getExp())) {
											exp = CsvConversionConstants.DOUBLE_EQUAL.getConstantVal();
										} else {
											exp = autoCalFormulaParts.getExp();
										}
										formula = getFormualColumnPositionForRepeatableSummation(tableElementMetaInfo, exp, autoCalFormulaParts.getFormulaCell(), elementColumnPositionMap);

										if (StringUtils.isNotBlank(formula)) {
											xmlElementsDto.setFormulaType(CsvConversionConstants.CHILD_TOTAL.getConstantVal());
											xmlElementsDto.setGrandTotalAvailable(false);
											xmlElementsDto.setFormula(formula.replaceAll(" +", " "));
											createRetMap(xmlElementsDto, retMap);
										}
									}
								}
							}
						}
					}
				}

				// on change event start
				if (!CollectionUtils.isEmpty(onchangeEvent)) {
					for (Entry<String, List<AutoCalFormulaParts>> item : onchangeEvent.entrySet()) {
						ListIterator<AutoCalFormulaParts> listIterator = item.getValue().listIterator();

						while (listIterator.hasNext()) {
							AutoCalFormulaParts pparts = listIterator.next();
							String formula = "";
							logger.info("Expression of onChange evenrt:: " + pparts.getExp());
							logger.info("Error codes:: " + pparts.getErrorCode());
							if (StringUtils.isBlank(pparts.getFormulaRef())) { // if formulaRef is present then skip such rules
								if (elementColumnPositionMap.get(pparts.getFormulaCell().substring(2)) != null) {
									xmlElementsDto = new XmlElementsDto();
									if (StringUtils.isNotBlank(pparts.getFormulaCell()) && StringUtils.isNotBlank(pparts.getFormula()) && (!errorCodeList.contains(pparts.getErrorCode()) || StringUtils.isBlank(pparts.getErrorCode()))) {
										errorCodeList.add(pparts.getErrorCode());
										TableElementMetaInfo tableElementMetaInfo = elementColumnPositionMap.get(pparts.getFormulaCell().substring(2));
										xmlElementsDto.setFormulaType(CsvConversionConstants.SUMMATION.getConstantVal());
										xmlElementsDto.setErrCode(pparts.getErrorCode());
										if (!Validations.isEmpty(pparts.getErrorType())) {
											xmlElementsDto.setErrType(pparts.getErrorType().equalsIgnoreCase(CsvConversionConstants.E.getConstantVal()) ? CsvConversionConstants.ERROR.getConstantVal() : CsvConversionConstants.WARNING.getConstantVal());
										} else {
											xmlElementsDto.setErrType(CsvConversionConstants.ERROR.getConstantVal());
										}

										xmlElementsDto.setTableCode(tableElementMetaInfo.getCsvTableCode());
										xmlElementsDto.setGrandTotalAvailable(tableElementMetaInfo.isGrandTotalPresent());

										String exp = StringUtils.EMPTY;
										if (StringUtils.isBlank(pparts.getExp())) {
											exp = CsvConversionConstants.DOUBLE_EQUAL.getConstantVal();
										} else {
											exp = pparts.getExp();
										}
										formula = getFormualColumnPosition(tableElementMetaInfo, exp, pparts.getFormula(), elementColumnPositionMap);

										if (StringUtils.contains(formula, CsvConversionConstants.UNDERSCORE.getConstantVal())) {
											String formulaTagAndConditionTag = createFormulaTagAndConditionTag(pparts.getFormula(), elementColumnPositionMap);

											if (StringUtils.isNotBlank(formulaTagAndConditionTag) && formulaTagAndConditionTag.split(CsvConversionConstants.TILDA.getConstantVal()).length == 2) {
												xmlElementsDto.setConditionTag(formulaTagAndConditionTag.split(CsvConversionConstants.TILDA.getConstantVal())[0]);
												xmlElementsDto.setFormulaTag(formulaTagAndConditionTag.split(CsvConversionConstants.TILDA.getConstantVal())[1]);
												xmlElementsDto.setIsRowPositionCheck(CsvConversionConstants.TRUE.getConstantVal());
												xmlElementsDto.setIsValidationOnParticularRow(CsvConversionConstants.TRUE.getConstantVal());
											}
										}
										if (StringUtils.isNotBlank(formula)) {
											if (StringUtils.isNumeric(formula)) {
												xmlElementsDto.setFormulaType(CsvConversionConstants.CHILD_TOTAL.getConstantVal());
												xmlElementsDto.setGrandTotalAvailable(true);
											}
											xmlElementsDto.setFormula(formula.replaceAll(" +", " "));
											createRetMap(xmlElementsDto, retMap);
										}
									}
								}
								if (StringUtils.isNotBlank(pparts.getRhs()) && StringUtils.isNotBlank(pparts.getExp()) && StringUtils.isNotBlank(pparts.getLhs()) && !errorCodeList.contains(pparts.getErrorCode())) {
									errorCodeList.add(pparts.getErrorCode());
									// date compare
									// add here summation formula of LHS and RHS
									if ((StringUtils.isNotBlank(pparts.getFormulaType()) && pparts.getFormulaType().equals(CsvConversionConstants.DATE_CHK.getConstantVal())) || (pparts.getFormulaCell().equalsIgnoreCase(CsvConversionConstants.NA.getConstantVal()) && pparts.getFormulaType().equals(CsvConversionConstants.EQUATION.getConstantVal()))) {
										xmlElementsDto = new XmlElementsDto();
										TableElementMetaInfo tableElementMetaInfo = elementColumnPositionMap.get(pparts.getLhs().substring(3));
										if (pparts.getFormulaType().equals(CsvConversionConstants.EQUATION.getConstantVal())) {
											xmlElementsDto.setFormulaType(CsvConversionConstants.SUMMATION.getConstantVal());
										} else {
											xmlElementsDto.setFormulaType(CsvConversionConstants.DATE_COMP.getConstantVal());
											xmlElementsDto.setCondition(CsvConversionConstants.DATE_FORMAT_DDMMYYYY.getConstantVal());
										}
										xmlElementsDto.setErrCode(pparts.getErrorCode());
										if (!Validations.isEmpty(pparts.getErrorType())) {
											xmlElementsDto.setErrType(pparts.getErrorType().equalsIgnoreCase(CsvConversionConstants.E.getConstantVal()) ? CsvConversionConstants.ERROR.getConstantVal() : CsvConversionConstants.WARNING.getConstantVal());
										} else {
											xmlElementsDto.setErrType(CsvConversionConstants.ERROR.getConstantVal());
										}
										if (tableElementMetaInfo.getCsvTableCode().contains(CsvConversionConstants.T001.getConstantVal())) {
											xmlElementsDto.setTableCode(tableElementMetaInfo.getCsvTableCode());
										} else {
											xmlElementsDto.setTableCode(elementColumnPositionMap.get(pparts.getLhs().substring(3)).getCsvTableCode());
										}

										String exp = StringUtils.EMPTY;
										if (StringUtils.isBlank(pparts.getExp())) {
											exp = CsvConversionConstants.DOUBLE_EQUAL.getConstantVal();
										} else {
											exp = pparts.getExp();
										}
										formula = getFormualColumnPosition(tableElementMetaInfo, exp, pparts.getRhs(), elementColumnPositionMap);
										if (StringUtils.contains(formula, CsvConversionConstants.UNDERSCORE.getConstantVal())) {
											String formulaTagAndConditionTag = createFormulaTagAndConditionTag(pparts.getRhs(), elementColumnPositionMap);

											if (StringUtils.isNotBlank(formulaTagAndConditionTag) && formulaTagAndConditionTag.split(CsvConversionConstants.TILDA.getConstantVal()).length == 2) {
												xmlElementsDto.setConditionTag(formulaTagAndConditionTag.split(CsvConversionConstants.TILDA.getConstantVal())[0]);
												xmlElementsDto.setFormulaTag(formulaTagAndConditionTag.split(CsvConversionConstants.TILDA.getConstantVal())[1]);
												xmlElementsDto.setIsRowPositionCheck(CsvConversionConstants.TRUE.getConstantVal());
												xmlElementsDto.setIsValidationOnParticularRow(CsvConversionConstants.TRUE.getConstantVal());
											}
										}

										if (StringUtils.isNotBlank(formula)) {
											if (StringUtils.isNumeric(formula)) {
												xmlElementsDto.setFormulaType(CsvConversionConstants.CHILD_TOTAL.getConstantVal());
												xmlElementsDto.setGrandTotalAvailable(true);
											}
											xmlElementsDto.setFormula(formula.replaceAll(" +", " "));
											createRetMap(xmlElementsDto, retMap);
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception occoured getDataMapFromJson()", e);
		}
		return retMap;
	}

	private String getFormualColumnPositionForRepeatableSummation(TableElementMetaInfo tableElementMetaInfo, String exp, String formulaCell, Map<String, TableElementMetaInfo> elementColumnPositionMap) {
		StringBuilder formulaWithValueForColumn = new StringBuilder();
		StringTokenizer stringTokenizer = new StringTokenizer(formulaCell);

		formulaWithValueForColumn.append("" + tableElementMetaInfo.getCsvColumnPosition()).append(" " + exp);

		while (stringTokenizer.hasMoreElements()) {
			String tokenValForColumn = StringUtils.EMPTY;
			String token = (String) stringTokenizer.nextElement();

			if (elementColumnPositionMap.get(token.substring(2)) != null) {
				tokenValForColumn = elementColumnPositionMap.get(token.substring(2)).getCsvTableCode() + CsvConversionConstants.UNDERSCORE.getConstantVal() + elementColumnPositionMap.get(token.substring(2)).getCsvRowPosition() + CsvConversionConstants.UNDERSCORE.getConstantVal() + elementColumnPositionMap.get(token.substring(2)).getCsvColumnPosition();
			} else {
				if (token.startsWith(CsvConversionConstants.STAR_SIGN.getConstantVal())) {
					String val = getNewString(token, " #", 0);
					tokenValForColumn = val;
				} else {
					if (token.startsWith(CsvConversionConstants.HASH_ZERO_HIGHPHYN.getConstantVal())) { // In R220, abstract field having autocalculation formula, web form team will remove such formulas. then no need to this condition
						return null;
					}
					tokenValForColumn = token; // if value contains * means it is multiplicaion, so for xml we have to add #
				}
			}

			if (StringUtils.isNumeric(token)) {
				if (!formulaWithValueForColumn.toString().endsWith(CsvConversionConstants.HASH_SIGN.getConstantVal())) {
					formulaWithValueForColumn.append(" " + CsvConversionConstants.HASH_SIGN.getConstantVal() + tokenValForColumn);
				} else {
					formulaWithValueForColumn.append("" + tokenValForColumn);
				}

			} else if (isDate(token)) {
				formulaWithValueForColumn.append(" " + CsvConversionConstants.TILDA.getConstantVal() + tokenValForColumn);
			} else {
				formulaWithValueForColumn.append(" " + tokenValForColumn);
			}
		}
		return formulaWithValueForColumn.toString();
	}

	private boolean isDate(String rhs) {
		try {
			DateManip.convertStringToDate(rhs, CsvConversionConstants.DATE_FORMAT_DDMMYYYY.getConstantVal());
			return true;
		} catch (ParseException e) {
			logger.error("Not valid date : " + e);
		}
		return false;
	}

	private void createRetMap(XmlElementsDto xmlElementsDto, Map<String, Map<String, List<XmlElementsDto>>> retMap) {
		Map<String, List<XmlElementsDto>> innerMap = new HashMap<>();
		List<XmlElementsDto> elementList = new ArrayList<>();
		if (retMap.get(xmlElementsDto.getTableCode()) == null) {
			elementList.add(xmlElementsDto);
			innerMap.put(xmlElementsDto.getFormulaType(), elementList);
			retMap.put(xmlElementsDto.getTableCode(), innerMap);
		} else {
			if (retMap.get(xmlElementsDto.getTableCode()).get(xmlElementsDto.getFormulaType()) != null) {
				retMap.get(xmlElementsDto.getTableCode()).get(xmlElementsDto.getFormulaType()).add(xmlElementsDto);
			} else {
				elementList.add(xmlElementsDto);
				retMap.get(xmlElementsDto.getTableCode()).put(xmlElementsDto.getFormulaType(), elementList);
			}
		}
	}

	private String getFormualColumnPosition(TableElementMetaInfo tableElementMetaInfo, String exp, String formula, Map<String, TableElementMetaInfo> elementColumnPositionMap) {
		logger.info(tableElementMetaInfo.getCsvTableCode());
		Boolean isChildTotal = false;
		Boolean isRowWiseSummation = false;
		Boolean isValidatonContainFirstTable = false;
		Boolean isNumericRhs = false;
		Boolean isDate = false;
		String columnPosition = tableElementMetaInfo.getCsvColumnPosition();
		Long totalRowPresentInTable = tableElementMetaInfo.getTotalRowCount();
		StringBuilder formulaWithValueForColumn = new StringBuilder();
		StringBuilder formulaWithValueForRow = new StringBuilder();
		StringTokenizer stringTokenizer = new StringTokenizer(formula);
		String colPositionOfEachElement = StringUtils.EMPTY;
		if (tableElementMetaInfo.getCsvTableCode().contains(CsvConversionConstants.T001.getConstantVal())) {
			isValidatonContainFirstTable = true;
			formulaWithValueForColumn.append(CsvConversionConstants.HASH_SIGN.getConstantVal() + tableElementMetaInfo.getCsvColumnPosition()).append(" " + exp);
		} else {
			if (tableElementMetaInfo.getTotalRowCount() > 0) {
				formulaWithValueForColumn.append("" + tableElementMetaInfo.getCsvRowPosition() + CsvConversionConstants.UNDERSCORE.getConstantVal() + tableElementMetaInfo.getCsvColumnPosition()).append(" " + exp);
			} else {
				formulaWithValueForColumn.append("" + tableElementMetaInfo.getCsvColumnPosition()).append(" " + exp);
			}

		}

		int counter = 1;
		while (stringTokenizer.hasMoreElements()) {
			String tokenValForColumn = StringUtils.EMPTY;
			String token = (String) stringTokenizer.nextElement();

			if (token.startsWith(CsvConversionConstants.HASH_SIGN.getConstantVal()) && elementColumnPositionMap.get(token.substring(3)) != null) {
				if (elementColumnPositionMap.get(token.substring(3)).getCsvTableCode().contains(CsvConversionConstants.T001.getConstantVal())) {
					tokenValForColumn = CsvConversionConstants.HASH_SIGN.getConstantVal() + elementColumnPositionMap.get(token.substring(3)).getCsvColumnPosition();
					isValidatonContainFirstTable = true;
				} else {
					if (isValidatonContainFirstTable || tableElementMetaInfo.isRepeatable() || elementColumnPositionMap.get(token.substring(3)).getCsvRowPosition().equals(CsvConversionConstants.ZERO.getConstantVal())) { // last condition was added on 05-04-2022 there is some table in R330 where only row will be reported and in the json "isRepeatable = false" thats why in the formula row position is taken as 0 which is wrong. now only column will be taken in the formula
						if (tableElementMetaInfo.isRepeatable() || elementColumnPositionMap.get(token.substring(3)).getCsvRowPosition().equals(CsvConversionConstants.ZERO.getConstantVal())) {
							tokenValForColumn = elementColumnPositionMap.get(token.substring(3)).getCsvColumnPosition();
						} else {
							tokenValForColumn = CsvConversionConstants.DOLLER_SIGN.getConstantVal() + elementColumnPositionMap.get(token.substring(3)).getCsvTableCode() + CsvConversionConstants.UNDERSCORE.getConstantVal() + elementColumnPositionMap.get(token.substring(3)).getCsvRowPosition() + CsvConversionConstants.UNDERSCORE.getConstantVal() + elementColumnPositionMap.get(token.substring(3)).getCsvColumnPosition();
						}

					} else {
						if (isChildTotal) {
							colPositionOfEachElement = elementColumnPositionMap.get(token.substring(3)).getCsvColumnPosition();
						}
						tokenValForColumn = CsvConversionConstants.DOLLER_SIGN.getConstantVal() + elementColumnPositionMap.get(token.substring(3)).getCsvTableCode() + CsvConversionConstants.UNDERSCORE.getConstantVal() + elementColumnPositionMap.get(token.substring(3)).getCsvRowPosition() + CsvConversionConstants.UNDERSCORE.getConstantVal() + elementColumnPositionMap.get(token.substring(3)).getCsvColumnPosition();
						isRowWiseSummation = true;
					}
				}
				counter++;
				if (isChildTotal && !columnPosition.equals(colPositionOfEachElement)) {
					isChildTotal = false;
				}
			} else {
				if (token.startsWith(CsvConversionConstants.STAR_SIGN.getConstantVal())) {
					String val = getNewString(token, " #", 0);
					tokenValForColumn = val;
				} else {
					if (token.startsWith(CsvConversionConstants.HASH_ZERO_HIGHPHYN.getConstantVal())) { // In R220, abstract field having autocalculation formula, web form
						// team will remove such formulas. then no need to this condition
						return null;
					}
					tokenValForColumn = token; // if value contains * means it is multiplicaion, so for xml we have to add #
				}
			}

			if (StringUtils.isNumeric(token)) {
				if (!formulaWithValueForColumn.toString().endsWith(CsvConversionConstants.HASH_SIGN.getConstantVal())) {
					formulaWithValueForColumn.append(" " + CsvConversionConstants.HASH_SIGN.getConstantVal() + tokenValForColumn);
				} else {
					formulaWithValueForColumn.append("" + tokenValForColumn);
				}
				isNumericRhs = true;
			} else if (isDate(token)) {
				formulaWithValueForColumn.append(" " + CsvConversionConstants.TILDA.getConstantVal() + tokenValForColumn);
				isDate = true;
			} else {
				formulaWithValueForColumn.append(" " + tokenValForColumn);
			}
		}
		if (isChildTotal && counter == totalRowPresentInTable) {
			return columnPosition;
		} else if (isChildTotal && counter != totalRowPresentInTable && !isRowWiseSummation && !isNumericRhs && !isDate) {
			return formulaWithValueForRow.toString();
		}

		if (isRowWiseSummation) {
			return formulaWithValueForColumn.toString().split(exp)[0].replace(formulaWithValueForColumn.toString().split(exp)[0], tableElementMetaInfo.getCsvRowPosition() + CsvConversionConstants.UNDERSCORE.getConstantVal() + tableElementMetaInfo.getCsvColumnPosition()) + " " + exp + " " + formulaWithValueForColumn.toString().split(exp)[1];
		} else {
			return formulaWithValueForColumn.toString();
		}
	}

	private String getNewString(String word, String stringToInsert, int position) {
		String newString = new String();
		for (int i = 0; i < word.length(); i++) {
			// Insert the original string character into the new string
			newString += word.charAt(i);
			if (i == position) {
				// Insert the string to be inserted into the new string
				newString += stringToInsert;
			}
		}
		// return the modified String
		return newString;
	}

	private String readTxtFile(String pathOfMetaInfoFile) {
		String data = StringUtils.EMPTY;
		try {
			File file = new File(pathOfMetaInfoFile);
			InputStream in = new FileInputStream(file);
			data = WebformToCsvConversionController.getReturnSupportingDoc(in);
			return data;
		} catch (IOException e) {
			logger.info("exception in readTxtFile() :  " + e);
		}
		return data;
	}
}