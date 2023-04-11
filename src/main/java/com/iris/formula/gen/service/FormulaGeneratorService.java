package com.iris.formula.gen.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iris.exception.ServiceException;
import com.iris.formula.gen.AutoCalVesrion;
import com.iris.formula.gen.AutoCalVesrionRepo;
import com.iris.formula.gen.FormulaBean;
import com.iris.formula.gen.FormulaGen;
import com.iris.formula.gen.FormulaGenRepo;
import com.iris.formula.gen.controller.AutoCalFormulaParts;
import com.iris.formula.gen.controller.FormulaConverter;
import com.iris.model.AutoCalculationFormula;
import com.iris.model.ErrorCodeDetail;
import com.iris.model.ErrorCodeLabelMapping;
import com.iris.model.ErrorVersionChannelMapping;
import com.iris.model.FormulaCategoryType;
import com.iris.model.LanguageMaster;
import com.iris.model.Return;
import com.iris.model.ReturnSectionVersionMap;
import com.iris.model.ReturnTemplate;
import com.iris.model.UserMaster;
import com.iris.repository.AutoCalculationFormulaRepo;
import com.iris.repository.ErrorCodeDetailRepo;
import com.iris.repository.ErrorCodeLabelMappingRepository;
import com.iris.repository.ErrorVersionChannelMappingRepository;
import com.iris.repository.ReturnRepo;
import com.iris.repository.ReturnSectionVersionMapRepo;
import com.iris.repository.ReturnTemplateRepository;
import com.iris.repository.UserMasterRepo;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;

@Service
public class FormulaGeneratorService {

	static final Logger logger = LogManager.getLogger(FormulaGeneratorService.class);

	@Autowired
	private FormulaGenRepo formulaGenRepo;

	@Autowired
	private ReturnRepo returnRepo;

	@Autowired
	private AutoCalculationFormulaRepo autoCalculationFormulaRepo;

	@Autowired
	private ReturnTemplateRepository returnTemplateRepository;

	@Autowired
	private FormulaConverter formulaConverter;

	@Autowired
	private ErrorCodeDetailRepo errorCodeDetailRepo;

	@Autowired
	private ErrorCodeLabelMappingRepository errorCodeLabelMappingRepository;

	@Autowired
	private ErrorVersionChannelMappingRepository errorVersionChannelMappingRepository;

	@Autowired
	private AutoCalVesrionRepo autoCalVesrionRepo;

	@Autowired
	private ReturnSectionVersionMapRepo returnSectionVersionMapRepo;

	@Autowired
	private UserMasterRepo userMasterRepo;

	@Autowired
	DataSource datasource;

	Map<Long, Integer> tableIdElrIdMap = new HashMap<>();

	// This method is to save all the Formula for the selected Return template version
	// Formula Json for the return template will be prepared at the end of this method and the version will be made active
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveFormula(FormulaBean formula) throws JsonProcessingException {
		// prepare errorcodes based on list of new formula's added
		// save ErrorCodeDetail, errorCodeLabelMappingRepository, errorVersionChannelMappingRepository
		// convert all formula's into formulaBean
		List<FormulaBean> convetedFormulaList = new ArrayList<>();
		Map<String, FormulaBean> newFormulaBeanMapWithErrorCode = new HashMap<>();
		// fetch formula's from formula Gen table 
		List<FormulaGen> formulaGenList = formulaGenRepo.findByReturnTemplateIdFkReturnTemplateId(formula.getReturnTemplateId());
		Map<String, ErrorCodeDetail> errorCodeDetailMap = new HashMap<>();
		List<ErrorCodeDetail> errorDetailList = null;
		Map<String, FormulaBean> newFormulaBeanMap = new HashMap<>();
		int count = 0;
		String errorInitials = "E" + formula.getReturnCode() + "T";
		try {
			Return returnEn = returnRepo.findByReturnCode(formula.getReturnCode());
			for (FormulaGen formulaGen : formulaGenList) {
				String json = formulaGen.getFormulaJson();
				Type listToken = new TypeToken<FormulaBean>() {
				}.getType();
				FormulaBean formulaBeanObj = new Gson().fromJson(json, listToken);
				formulaBeanObj.setFormulaId(formulaGen.getFormulaId());
				if (!StringUtils.isBlank(formulaBeanObj.getFormula()) && !StringUtils.isBlank(formulaBeanObj.getTechnicalErrorCode()) && !formulaBeanObj.getTechnicalErrorCode().contains(errorInitials)) {
					newFormulaBeanMap.put(formulaBeanObj.getErrorCode(), formulaBeanObj);
				} else {
					// fetch ErrorCodeDetail object based on TechnicalErrorCode
					ErrorCodeDetail presentErrorCodeDetail = errorCodeDetailRepo.findByTechnicalErrorCode(formulaBeanObj.getTechnicalErrorCode());
					// save ErrorCodeDetail object 
					if (!Objects.isNull(presentErrorCodeDetail) && !Objects.isNull(formulaBeanObj.getErrorMessage()) && Boolean.TRUE.equals(formulaBeanObj.getIsUpdatedFormula())) {
						if (Objects.isNull(presentErrorCodeDetail.getFormulaCategoryTypeIdFk())) {
							FormulaCategoryType formulaCategoryType = new FormulaCategoryType();
							formulaCategoryType.setFormulaCategoryTypeId(Long.valueOf(1));
							presentErrorCodeDetail.setFormulaCategoryTypeIdFk(formulaCategoryType);
						}
						presentErrorCodeDetail.setBusinessErrorCode(formulaBeanObj.getErrorCode());
						presentErrorCodeDetail.setErrorDescription(formulaBeanObj.getErrorMessage());
						errorCodeDetailRepo.save(presentErrorCodeDetail);
						formulaBeanObj.setErrorCodeDetail(presentErrorCodeDetail);
						saveErrorCodeDetailLabelObject(formula, presentErrorCodeDetail, 15L);
						saveErrorCodeDetailLabelObject(formula, presentErrorCodeDetail, 26L);
					} else if (Objects.isNull(presentErrorCodeDetail)) {
						presentErrorCodeDetail = prepareErrorCodeDetailObject(formulaBeanObj, count, null);
						errorCodeDetailRepo.save(presentErrorCodeDetail);
						formulaBeanObj.setErrorCodeDetail(presentErrorCodeDetail);
						if (!Objects.isNull(presentErrorCodeDetail)) {
							saveErrorCodeDetailLabelObject(formula, presentErrorCodeDetail, 15L);
							saveErrorCodeDetailLabelObject(formula, presentErrorCodeDetail, 26L);
						}
					}
					// fetch ErrorVersionChannelMapping object 
					ErrorVersionChannelMapping errorVersionChannelMapping = errorVersionChannelMappingRepository.getErrorVersionChannelMappingRecordByErrorCodeDetailIdAndRetTempId(presentErrorCodeDetail.getErrorCodeDetailId(), formula.getReturnTemplateId());
					if (Objects.isNull(errorVersionChannelMapping) && !Objects.isNull(presentErrorCodeDetail)) {
						// if not present save ErrorVersionChannelMapping object 
						errorVersionChannelMappingRepository.insertErrorChannnelVesrion(presentErrorCodeDetail.getErrorCodeDetailId().intValue(), presentErrorCodeDetail.getErrorCodeDetailId().intValue(), formula.getReturnTemplateId().intValue());
					}
					// prepare formula json from formulaBean object 
					if (!StringUtils.isBlank(formulaBeanObj.getFormula())) {
						FormulaBean convetedFormula = formulaConverter.convert(formulaBeanObj);
						convetedFormulaList.add(convetedFormula);
					} else {
						if (!CollectionUtils.isEmpty(formulaBeanObj.getNormalFormula())) {
							Map<String, List<AutoCalFormulaParts>> normalFormua = formulaBeanObj.getNormalFormula();
							List<AutoCalFormulaParts> formulaList = null;
							List<AutoCalFormulaParts> formulaBeanList = null;
							AutoCalFormulaParts autoCalFormulaParts = null;
							for (String errorCodeStr : normalFormua.keySet()) {
								formulaList = normalFormua.get(errorCodeStr);
							}
							Map<String, List<AutoCalFormulaParts>> result = null;
							if (!CollectionUtils.isEmpty(formulaList)) {
								autoCalFormulaParts = formulaList.get(0);
							}
							if (!Objects.isNull(autoCalFormulaParts) && !Objects.isNull(autoCalFormulaParts.getFormulaCell()) && !autoCalFormulaParts.getFormulaCell().equals("NA")) {// for Summation based Normal Formula Json
								result = new HashMap<>();
								formulaBeanList = new ArrayList<>();
								autoCalFormulaParts.setFormulaId(formulaBeanObj.getFormulaId());
								autoCalFormulaParts.setErrorCode(presentErrorCodeDetail.getTechnicalErrorCode());
								formulaBeanList.add(autoCalFormulaParts);
								String[] formulaFieldArray = autoCalFormulaParts.getFormula().split(" ");
								Set<String> formulaFieldSet = new HashSet<>();
								for (int i = 0; i < formulaFieldArray.length; i++) {
									Boolean isSymbol = formulaConverter.isSymbol(formulaFieldArray[i]);
									if (Boolean.FALSE.equals(isSymbol) && formulaFieldArray[i].contains("0-")) {
										String field = null;
										if ("3".equals(formulaBeanObj.getEventType())) {
											String[] formulaFields = formulaFieldArray[i].split("0-");
											field = formulaFields[1];
										} else {
											field = formulaFieldArray[i].substring(1);
										}
										formulaFieldSet.add(field);
									}
								}
								for (String field : formulaFieldSet) {
									result.put(field, formulaBeanList);
								}
								formulaBeanObj.setNormalFormula(result);
							} else if (!Objects.isNull(autoCalFormulaParts) && !Objects.isNull(autoCalFormulaParts.getExp())) {// for Comparison based Normal Formula Json
								result = new HashMap<>();
								formulaBeanList = new ArrayList<>();
								formulaBeanList.add(autoCalFormulaParts);
								String[] formulaFieldArray = autoCalFormulaParts.getLhs().split(" ");
								Set<String> formulaFieldSet = new HashSet<>();
								for (int i = 0; i < formulaFieldArray.length; i++) {
									Boolean isSymbol = formulaConverter.isSymbol(formulaFieldArray[i]);
									if (Boolean.FALSE.equals(isSymbol) && formulaFieldArray[i].contains("0-")) {
										String field = formulaFieldArray[i].substring(1);
										formulaFieldSet.add(field);
									}
								}
								formulaFieldArray = autoCalFormulaParts.getRhs().split(" ");
								for (int i = 0; i < formulaFieldArray.length; i++) {
									Boolean isSymbol = formulaConverter.isSymbol(formulaFieldArray[i]);
									if (Boolean.FALSE.equals(isSymbol) && formulaFieldArray[i].contains("0-")) {
										String field = formulaFieldArray[i].substring(1);
										formulaFieldSet.add(field);
									}
								}
								for (String field : formulaFieldSet) {
									String formulaStr = field;
									if ("3".equals(formulaBeanObj.getEventType())) {
										String[] formulaFields = formulaStr.split("0-");
										formulaStr = formulaFields[1];
									}
									result.put(formulaStr, formulaBeanList);
								}
								formulaBeanObj.setNormalFormula(result);
							}
						} else if (!CollectionUtils.isEmpty(formulaBeanObj.getCrossElrFormula())) {

						}
						convetedFormulaList.add(formulaBeanObj);
					}
				}
			}
			if (!CollectionUtils.isEmpty(newFormulaBeanMap)) {
				String techErrorCode = null;
				count = 0;
				for (Entry<String, FormulaBean> entry : newFormulaBeanMap.entrySet()) {
					String errorCodeStr = entry.getKey();
					FormulaBean newFormulaBean = newFormulaBeanMap.get(errorCodeStr);
					newFormulaBeanMapWithErrorCode = prepareErrorDetailObject(newFormulaBean, errorCodeDetailMap, errorCodeStr, newFormulaBeanMapWithErrorCode, count, techErrorCode);
					techErrorCode = errorCodeDetailMap.get(errorCodeStr).getTechnicalErrorCode();
					count++;
				}
				errorDetailList = errorCodeDetailRepo.saveAll(new ArrayList<>(errorCodeDetailMap.values()));
				//Save Label Table 
				errorDetailList.sort((o1, o2) -> o1.getErrorCodeDetailId().compareTo(o2.getErrorCodeDetailId()));
				errorCodeLabelMappingRepository.insertErrorCodeLabel(formula.getUserId().intValue(), 15);
				errorCodeLabelMappingRepository.insertErrorCodeLabel(formula.getUserId().intValue(), 26);
				executeCustomErrorVerChannelMapProcedure(formula, errorDetailList);
				//				errorVersionChannelMappingRepository.insertErrorChannnelVesrion(errorDetailList.get(0).getErrorCodeDetailId().intValue(), errorDetailList.get(errorDetailList.size() - 1).getErrorCodeDetailId().intValue(), formula.getReturnTemplateId().intValue());
				List<FormulaBean> formulaBeanList = new ArrayList<>(newFormulaBeanMapWithErrorCode.values());
				for (FormulaBean formulaBean : formulaBeanList) {
					FormulaBean convetedFormula = formulaConverter.convert(formulaBean);
					convetedFormulaList.add(convetedFormula);
				}
			}
			AutoCalculationFormula autoCal = null;
			autoCal = autoCalculationFormulaRepo.findByAutoCalVesrionReturnTemplateFkReturnTemplateId(formula.getReturnTemplateId());
			Map<String, List<AutoCalFormulaParts>> crossRetMap = new HashMap<>();
			Map<String, Map<String, Map<String, List<AutoCalFormulaParts>>>> retMap = new HashMap<>();
			Map<String, Map<String, List<AutoCalFormulaParts>>> onBlurEvnt = new HashMap<>();
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			if (!CollectionUtils.isEmpty(convetedFormulaList)) {
				for (FormulaBean convetedFormula : convetedFormulaList) {
					//AutoCAl present case
					if (autoCal != null) {
						TypeReference<Map<String, Map<String, Map<String, List<AutoCalFormulaParts>>>>> autoFormulaType = new TypeReference<Map<String, Map<String, Map<String, List<AutoCalFormulaParts>>>>>() {
						};
						TypeReference<Map<String, List<AutoCalFormulaParts>>> crossFormulaType = new TypeReference<Map<String, List<AutoCalFormulaParts>>>() {
						};
						String eventType = getEventType(convetedFormula);
						//if no Normal Json present
						if (!StringUtils.isBlank(autoCal.getFormulaJson()) && !CollectionUtils.isEmpty(convetedFormula.getNormalFormula())) {
							retMap = mapper.readValue(autoCal.getFormulaJson(), autoFormulaType);
							onBlurEvnt = retMap.get("customeCalculation");
							Map<String, List<AutoCalFormulaParts>> innerd = onBlurEvnt.get(eventType);
							if (CollectionUtils.isEmpty(innerd)) {
								innerd = new HashMap<>();
							}
							Map<String, List<AutoCalFormulaParts>> convertedFormulaJson = convetedFormula.getNormalFormula();
							Set<String> newFormulaFieldSet = convertedFormulaJson.keySet();
							for (String newFormulaField : newFormulaFieldSet) {
								Set<String> oldFormulaFieldSet = innerd.keySet();
								if (oldFormulaFieldSet.contains(newFormulaField)) {
									List<AutoCalFormulaParts> newFieldFormulaList = convertedFormulaJson.get(newFormulaField);
									List<AutoCalFormulaParts> oldFieldFormulaList = innerd.get(newFormulaField);
									Boolean isFormulaPresent = false;
									for (AutoCalFormulaParts newAutoCalFormulaParts : newFieldFormulaList) {
										for (AutoCalFormulaParts oldAutoCalFormulaParts : oldFieldFormulaList) {
											if (!ObjectUtils.isEmpty(newAutoCalFormulaParts.getFormulaCell()) && !newAutoCalFormulaParts.getFormulaCell().equals("NA")) {
												if (newAutoCalFormulaParts.getFormulaCell().equals(oldAutoCalFormulaParts.getFormulaCell())) {
													isFormulaPresent = true;
													String[] newAutoCalFormulaPartsArray = newAutoCalFormulaParts.getFormula().split(" ");
													String[] oldAutoCalFormulaPartsArray = oldAutoCalFormulaParts.getFormula().split(" ");
													for (int i = 0; i < newAutoCalFormulaPartsArray.length; i++) {
														if (!newAutoCalFormulaPartsArray[i].equals(oldAutoCalFormulaPartsArray[i])) {
															isFormulaPresent = false;
															break;
														}
													}
													if (Boolean.TRUE.equals(isFormulaPresent)) {
														isFormulaPresent = true;
														break;
													}
												}
											} else if (!ObjectUtils.isEmpty(newAutoCalFormulaParts.getLhs()) && !ObjectUtils.isEmpty(newAutoCalFormulaParts.getRhs())) {
												if (newAutoCalFormulaParts.getLhs().equals(oldAutoCalFormulaParts.getLhs()) && newAutoCalFormulaParts.getExp().equals(oldAutoCalFormulaParts.getExp()) && newAutoCalFormulaParts.getRhs().equals(oldAutoCalFormulaParts.getRhs())) {
													isFormulaPresent = true;
													break;
												}
											}
										}
										if (Boolean.FALSE.equals(isFormulaPresent)) {
											oldFieldFormulaList.add(newAutoCalFormulaParts);
										}
									}
								} else {
									innerd.put(newFormulaField, convertedFormulaJson.get(newFormulaField));
								}
							}
							onBlurEvnt.put(eventType, innerd);
							retMap.put("customeCalculation", onBlurEvnt);
							autoCal.setFormulaJson(mapper.writeValueAsString(retMap));
						} else if (!CollectionUtils.isEmpty(convetedFormula.getNormalFormula())) {
							onBlurEvnt.put(eventType, convetedFormula.getNormalFormula());
							retMap.put("customeCalculation", onBlurEvnt);
							autoCal.setFormulaJson(mapper.writeValueAsString(retMap));
						}
						//if no Cross Json present
						if (!StringUtils.isBlank(autoCal.getCrossElrJson()) && !CollectionUtils.isEmpty(convetedFormula.getCrossElrFormula())) {
							crossRetMap = mapper.readValue(autoCal.getCrossElrJson(), crossFormulaType);
							List<AutoCalFormulaParts> newFieldFormulaList = convetedFormula.getCrossElrFormula();
							List<AutoCalFormulaParts> oldFieldFormulaList = crossRetMap.get("crossELRCalculation");
							Boolean isFormulaPresent = false;
							AutoCalFormulaParts newAutoCalFormulaParts = null;
							for (AutoCalFormulaParts autoCalFormulaParts : newFieldFormulaList) {
								newAutoCalFormulaParts = autoCalFormulaParts;
								for (AutoCalFormulaParts oldAutoCalFormulaParts : oldFieldFormulaList) {
									if (!ObjectUtils.isEmpty(newAutoCalFormulaParts.getFormulaCell()) && !newAutoCalFormulaParts.getFormulaCell().equals("NA")) {
										if (newAutoCalFormulaParts.getFormulaCell().equals(oldAutoCalFormulaParts.getFormulaCell())) {
											oldAutoCalFormulaParts.setFormula(newAutoCalFormulaParts.getFormula());
											isFormulaPresent = true;
											break;
										}
									} else if (!ObjectUtils.isEmpty(newAutoCalFormulaParts.getLhs()) && !ObjectUtils.isEmpty(newAutoCalFormulaParts.getRhs())) {
										if (newAutoCalFormulaParts.getLhs().equals(oldAutoCalFormulaParts.getLhs()) && newAutoCalFormulaParts.getExp().equals(oldAutoCalFormulaParts.getExp()) && newAutoCalFormulaParts.getRhs().equals(oldAutoCalFormulaParts.getRhs())) {
											isFormulaPresent = true;
											break;
										}
									}
								}
								if (Boolean.FALSE.equals(isFormulaPresent)) {
									oldFieldFormulaList.add(newAutoCalFormulaParts);
								}
							}
							crossRetMap.put("crossELRCalculation", oldFieldFormulaList);
							autoCal.setCrossElrJson(mapper.writeValueAsString(crossRetMap));
						} else if (!CollectionUtils.isEmpty(convetedFormula.getCrossElrFormula())) {
							crossRetMap.put("crossELRCalculation", convetedFormula.getCrossElrFormula());
							autoCal.setCrossElrJson(mapper.writeValueAsString(crossRetMap));
						}
					} else {
						//Add new entry in auto cal
						autoCal = new AutoCalculationFormula();
						autoCal.setReturnIdFk(returnEn);
						autoCal.setIsActive(true);
						//						bassed on event type add formula to Json
						if (!CollectionUtils.isEmpty(convetedFormula.getNormalFormula())) {
							String eventType = getEventType(convetedFormula);
							onBlurEvnt.put(eventType, convetedFormula.getNormalFormula());
							retMap.put("customeCalculation", onBlurEvnt);
							autoCal.setFormulaJson(mapper.writeValueAsString(retMap));
						} else {
							autoCal.setFormulaJson(null);
						}
						if (!CollectionUtils.isEmpty(convetedFormula.getCrossElrFormula())) {
							crossRetMap.put("crossELRCalculation", convetedFormula.getCrossElrFormula());
							autoCal.setCrossElrJson(mapper.writeValueAsString(crossRetMap));
						} else {
							autoCal.setCrossElrJson(null);
						}
					}
				}
				if (!CollectionUtils.isEmpty(retMap)) {
					onBlurEvnt = retMap.get("customeCalculation");
					if (!CollectionUtils.isEmpty(onBlurEvnt)) {
						Map<String, List<AutoCalFormulaParts>> onblurJson = onBlurEvnt.get("onblurEvent");
						Map<String, List<AutoCalFormulaParts>> onchangeJson = onBlurEvnt.get("onchangeEvent");
						Map<String, List<AutoCalFormulaParts>> onblurSummationJson = onBlurEvnt.get("onblurSummationEvent");
						if (!CollectionUtils.isEmpty(onblurJson) || !CollectionUtils.isEmpty(onchangeJson) || !CollectionUtils.isEmpty(onblurSummationJson)) {
							autoCal.setFormulaJson(mapper.writeValueAsString(retMap));
						} else {
							autoCal.setFormulaJson(null);
						}
					}
				}
				List<AutoCalFormulaParts> crossitems = crossRetMap.get("crossELRCalculation");
				if (!CollectionUtils.isEmpty(crossitems)) {
					autoCal.setCrossElrJson(mapper.writeValueAsString(crossRetMap));
				} else {
					autoCal.setCrossElrJson(null);
				}
				autoCalculationFormulaRepo.save(autoCal);
				saveAutoCalVesrion(autoCal, formula);
				//				List<FormulaGen> formulaGenList = formulaGenRepo.findByReturnTemplateIdFkReturnTemplateId(formula.getReturnTemplateId());
				//				for(FormulaGen formulaGen : formulaGenList) {
				//					formulaGen.setIsFormulaAddedToJson(true);
				//					formulaGenRepo.save(formulaGen);
				//				}
			}
		} catch (Exception e) {
			logger.error("Exception : ", e);
			return false;
		}
		return true;
	}

	/**
	 * @param formula
	 * @param errorDetailIdString
	 */
	private void executeCustomErrorVerChannelMapProcedure(FormulaBean formula, List<ErrorCodeDetail> errorDetailList) {
		StringBuilder errorDetailIdString = new StringBuilder();
		int count = 1;
		for (ErrorCodeDetail errorCodeDetail : errorDetailList) {
			if (count >= 1 && count <= errorDetailList.size() - 1) {
				errorDetailIdString = errorDetailIdString.append(",");
				count++;
			}
			errorDetailIdString = errorDetailIdString.append(errorCodeDetail.getErrorCodeDetailId().intValue());
		}
		try (Connection con = datasource.getConnection(); CallableStatement stmt = con.prepareCall(GeneralConstants.SP_INSERT_CUSTOM_ERROR_VERSION_CHANNEL_MAPPING.getConstantVal());) {
			stmt.setString(1, errorDetailIdString.toString());
			stmt.setLong(2, formula.getReturnTemplateId().intValue());
			stmt.registerOutParameter(3, Types.INTEGER);
			stmt.executeQuery();
			int number = stmt.getInt(3);
			if (number == 0) {
				logger.debug("SP_RETURN_REGULATOR_MAPPING Procedure executed successfully.");
			}
		} catch (SQLException e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	public void mergeJsonsFiles(String newJson, String oldJson) throws Exception {
		HashMap<String, Object> newMap = convertJsonToMap(newJson);
		HashMap<String, Object> oldMap = convertJsonToMap(oldJson);

		for (Entry<String, Object> entry : oldMap.entrySet()) {
			if (newMap.get(entry.getKey()) == null) {
				newMap.put(entry.getKey(), entry.getValue());
			}
		}

		ObjectMapper mapper = new ObjectMapper();
		String jsonFromMap = mapper.writeValueAsString(newMap);

		PrintWriter writer = new PrintWriter(newJson);
		writer.write(jsonFromMap);
		writer.close();
	}

	private HashMap<String, Object> convertJsonToMap(String json) {
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			map = mapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
			});
		} catch (IOException e) {
			map.clear();
		}
		return map;
	}

	/**
	 * @param formula
	 * @param errorCodeDetail
	 * @return
	 */
	private void saveErrorCodeDetailLabelObject(FormulaBean formula, ErrorCodeDetail errorCodeDetail, Long langId) {
		LanguageMaster lang = new LanguageMaster();
		UserMaster master = new UserMaster();
		try {
			lang.setLanguageId(langId);
			ErrorCodeLabelMapping errorCodeLabelMapping = new ErrorCodeLabelMapping();
			errorCodeLabelMapping.setErrorCodeDetailIdFk(errorCodeDetail);
			errorCodeLabelMapping.setErrorKeyLabelForFileBased(errorCodeDetail.getErrorDescription());
			errorCodeLabelMapping.setErrorKeyLabelForWebBased(errorCodeDetail.getErrorDescription());
			master.setUserId(formula.getUserId());
			errorCodeLabelMapping.setCreatedBy(master);
			errorCodeLabelMapping.setModifiedBy(master);
			errorCodeLabelMapping.setModifiedOn(new Date());
			errorCodeLabelMapping.setCreatedOn(new Date());
			errorCodeLabelMapping.setLanguageIdFk(lang);
			errorCodeLabelMapping.setLastUpdatedOn(new Date());
			if (!Objects.isNull(errorCodeDetail.getErrorCodeDetailId())) {
				logger.info(errorCodeDetail.getErrorCodeDetailId());
				ErrorCodeLabelMapping ex = errorCodeLabelMappingRepository.findByErrorCodeDetailIdAndLanguageId(errorCodeDetail.getErrorCodeDetailId(), langId);
				if (ex != null) {
					errorCodeLabelMapping.setErrorCodeLabelMappingId(ex.getErrorCodeLabelMappingId());
				}
			}
			errorCodeLabelMappingRepository.save(errorCodeLabelMapping);
		} catch (Exception e) {
			logger.error("Exception : ", e);
		}
	}

	/**
	 * @param formulaBeanMap
	 * @param errorCodeDetailMap
	 * @param errorCodeStr
	 */
	private Map<String, FormulaBean> prepareErrorDetailObject(FormulaBean formulaBeanObj, Map<String, ErrorCodeDetail> errorCodeDetailMap, String errorCodeStr, Map<String, FormulaBean> newFormulaBeanMapWithErrorCode, int count, String errorCode) {
		ErrorCodeDetail errorDetail = prepareErrorCodeDetailObject(formulaBeanObj, count, errorCode);
		errorCodeDetailMap.put(errorCodeStr, errorDetail);
		newFormulaBeanMapWithErrorCode.put(errorDetail.getTechnicalErrorCode(), formulaBeanObj);
		return newFormulaBeanMapWithErrorCode;
	}

	/**
	 * @param formulaBeanObj
	 * @return
	 */
	private ErrorCodeDetail prepareErrorCodeDetailObject(FormulaBean formulaBeanObj, int count, String errorCode) {
		String errorInitials = " ";//"E"+formulaBeanObj.getReturnCode();
		ErrorCodeDetail errorDetail = new ErrorCodeDetail();
		if (!StringUtils.isBlank(formulaBeanObj.getTechnicalErrorCode())) {
			errorInitials = formulaBeanObj.getTechnicalErrorCode();
			ErrorCodeDetail detail = errorCodeDetailRepo.findMaxErrorCodeDetail(errorInitials);
			if (detail == null) {
				errorDetail.setTechnicalErrorCode(getReturnErrorcode(formulaBeanObj.getReturnCode(), count, errorCode));
				setErrorCodeDetailsValues(formulaBeanObj, errorDetail);
			} else {
				errorDetail.setErrorCodeDetailId(detail.getErrorCodeDetailId());
			}
		} else {
			errorDetail.setTechnicalErrorCode(getReturnErrorcode(formulaBeanObj.getReturnCode(), count, errorCode));
			setErrorCodeDetailsValues(formulaBeanObj, errorDetail);
		}
		formulaBeanObj.setErrorCodeDetail(errorDetail);
		return errorDetail;
	}

	/**
	 * @param formulaBeanObj
	 * @param errorDetail
	 */
	private void setErrorCodeDetailsValues(FormulaBean formulaBeanObj, ErrorCodeDetail errorDetail) {
		errorDetail.setBusinessErrorCode(formulaBeanObj.getErrorCode());
		errorDetail.setErrorDescription(formulaBeanObj.getErrorMessage());
		errorDetail.setRoundOff(2);
		FormulaCategoryType formulaCategoryType = new FormulaCategoryType();
		formulaCategoryType.setFormulaCategoryTypeId(Long.valueOf(1));
		errorDetail.setFormulaCategoryTypeIdFk(formulaCategoryType);
		errorDetail.setNumericFormula("1");
	}

	private void saveAutoCalVesrion(AutoCalculationFormula autoCal, FormulaBean formula) {
		AutoCalVesrion autoCalVersion = new AutoCalVesrion();
		List<AutoCalVesrion> existing = autoCalVesrionRepo.findByReturnTemplateFkReturnTemplateId(formula.getReturnTemplateId());
		if (existing != null && !existing.isEmpty()) {
			autoCalVersion = existing.get(0);
		}
		Optional<UserMaster> user = userMasterRepo.findById(formula.getUserId());
		autoCalVersion.setAutoFormula(autoCal);
		Optional<ReturnTemplate> template = returnTemplateRepository.findById(formula.getReturnTemplateId());
		if (template.isPresent()) {
			autoCalVersion.setReturnTemplateFk(template.get());
		}
		if (user.isPresent()) {
			autoCalVersion.setCreatedByFk(user.get());
			autoCalVersion.setModifiedByFk(user.get());
		}
		autoCalVersion.setIsActive(true);
		Date date = new Date();
		autoCalVersion.setUpdatedOn(date);
		autoCalVersion.setCreatedOn(date);
		autoCalVesrionRepo.save(autoCalVersion);
		autoCal.setAutoCalVesrion(autoCalVersion);
	}

	public FormulaBean prepareEditFormula(FormulaBean formula) throws JsonProcessingException {
		StringBuilder table = new StringBuilder();
		Map<String, String> formulaJsonMap = new HashMap<>();
		Integer action = formula.getAction();
		try {
			if (action == 1) {
				table.append("<div id=\"errorTable\"><table class=\"table\" id=\"erroTableId\">").append("<thead><th>Error Code</th>").append("<th>Edit</th>").append("<th>Delete</th></thead>");
			} else {
				table.append("<div id=\"errorTable\"><table class=\"table\" id=\"erroTableId\">").append("<thead><th>Error Code</th>").append("<th>View</th></thead>");
			}
			Return returnEn = returnRepo.findByReturnCode(formula.getReturnCode());
			formula.setReturnId(String.valueOf(returnEn.getReturnId()));
			// prepare Table ID Map based on return template Id also prepare table and elr map
			Set<Long> tableIds = getSelectedTableIds(formula.getReturnTemplateId());
			Set<String> errorCode = new HashSet<>();
			AutoCalculationFormula autoCal = null;
			List<FormulaGen> formulaGenList = null;
			Boolean newTemplate = false;
			//		Long returnTemplateId = null;
			// fetch formula json
			if (!ObjectUtils.isEmpty(formula.getReturnTemplateId())) {
				// based on return template id form auto cal formula table --> formula are added completely
				autoCal = autoCalculationFormulaRepo.findByAutoCalVesrionReturnTemplateFkReturnTemplateId(formula.getReturnTemplateId());
				if (Objects.isNull(autoCal) || (StringUtils.isEmpty(autoCal.getFormulaJson()) && StringUtils.isEmpty(autoCal.getCrossElrJson()))) {
					// based on return template id from formula gen table --> partial formula added
					formulaGenList = formulaGenRepo.findByReturnTemplateIdFkReturnTemplateId(formula.getReturnTemplateId());
					if (StringUtils.equals(formula.getIncludePreviousFormula(), "Y") && CollectionUtils.isEmpty(formulaGenList)) {
						// based on return id from Auto Cal Formula from latest old version --> new formula generation (template preparation stage)
						//					Long returnTemplateId = returnTemplateRepository.findReturnTemplateIdByReturnIdAndSaveAsDraftIsActiveFalse(returnEn.getReturnId(), false, true);
						newTemplate = true;
						List<AutoCalculationFormula> autoCalList = autoCalculationFormulaRepo.getDataByReturn(returnEn.getReturnId());
						if (!CollectionUtils.isEmpty(autoCalList)) {
							autoCal = autoCalList.get(0);
						}
					}
				}
			}
			// get all Error code and it's details based on return code if new template is getting generated
			// fetch error codes from error version channel mapping based on return template id for present or old return template
			// for partial return template ---> need to check
			// code changes required
			Map<String, ErrorCodeDetail> errorCodeMap = null;
			if (autoCal != null && Boolean.FALSE.equals(newTemplate)) {
				errorCodeMap = getErrorcodeMap(formula.getReturnTemplateId());
			} else {
				Long returnTemplateId = returnTemplateRepository.findReturnTemplateIdByReturnIdAndSaveAsDraftIsActiveFalse(returnEn.getReturnId(), false, true);
				//			AutoCalVersionMap autoCalVersionMap = autoCalVersionMapRepo.findReturnTemplateIdByReturnId(returnEn.getReturnId());
				errorCodeMap = getErrorcodeMap(returnTemplateId);
			}
			if (autoCal != null) {
				//AutoCAL present case
				ObjectMapper mapper = new ObjectMapper();
				//bassed on event type add formula to Json
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				TypeReference<Map<String, Map<String, Map<String, List<AutoCalFormulaParts>>>>> autoFormulaType = new TypeReference<Map<String, Map<String, Map<String, List<AutoCalFormulaParts>>>>>() {
				};
				TypeReference<Map<String, List<AutoCalFormulaParts>>> crossFormulaType = new TypeReference<Map<String, List<AutoCalFormulaParts>>>() {
				};
				//if Normal Json present
				if (!StringUtils.isBlank(autoCal.getFormulaJson()) && !StringUtils.isEmpty(autoCal.getFormulaJson())) {
					Map<String, Map<String, Map<String, List<AutoCalFormulaParts>>>> retMap = mapper.readValue(autoCal.getFormulaJson(), autoFormulaType);
					Map<String, Map<String, List<AutoCalFormulaParts>>> inner = retMap.get("customeCalculation");
					if (!CollectionUtils.isEmpty(inner)) {
						Map<String, List<AutoCalFormulaParts>> innerd = inner.get("onblurEvent");
						String eventType = "1";
						prepareErrorCodeTableDiv(table, formulaJsonMap, tableIds, errorCode, errorCodeMap, innerd, eventType, action);
						innerd = inner.get("onchangeEvent");
						eventType = "2";
						prepareErrorCodeTableDiv(table, formulaJsonMap, tableIds, errorCode, errorCodeMap, innerd, eventType, action);
						innerd = inner.get("onblurSummationEvent");
						eventType = "3";
						prepareErrorCodeTableDiv(table, formulaJsonMap, tableIds, errorCode, errorCodeMap, innerd, eventType, action);
					}
				}
				//if Cross Json present
				if (!StringUtils.isBlank(autoCal.getCrossElrJson())) {
					Map<String, List<AutoCalFormulaParts>> crossRetMap = mapper.readValue(autoCal.getCrossElrJson(), crossFormulaType);
					List<AutoCalFormulaParts> crossitems = crossRetMap.get("crossELRCalculation");
					for (AutoCalFormulaParts pparts : crossitems) {
						// check if formula is for the present table id or not
						if (tableIds != null && Boolean.FALSE.equals(validElrFormula(pparts, tableIds))) {
							continue;
						}
						if (errorCode.add(pparts.getErrorCode())) {
							String error = errorCodeMap.get(pparts.getErrorCode()) == null ? pparts.getErrorCode() : errorCodeMap.get(pparts.getErrorCode()).getBusinessErrorCode();
							FormulaBean formulaBean = new FormulaBean();
							List<AutoCalFormulaParts> crossElrFormula = new ArrayList<>();
							crossElrFormula.add(pparts);
							formulaBean.setCrossElrFormula(crossElrFormula);
							formulaBean.setFormulaId(pparts.getFormulaId());
							System.out.println("ErrorCode : " + pparts.getErrorCode());
							//						if(error.startsWith("E"+formula.getReturnCode()) && !Objects.isNull(errorCodeMap.get(pparts.getErrorCode()))) {
							//							formulaBean.setErrorCode(errorCodeMap.get(pparts.getErrorCode()).getBusinessErrorCode());
							//						} else {
							//							formulaBean.setErrorCode(pparts.getErrorCode());
							//						}
							if (error.startsWith("E" + formula.getReturnCode()) && !Objects.isNull(errorCodeMap.get(pparts.getErrorCode()))) {
								formulaBean.setErrorCode(errorCodeMap.get(pparts.getErrorCode()).getBusinessErrorCode());
							} else if (error.startsWith("E" + formula.getReturnCode()) && Objects.isNull(errorCodeMap.get(pparts.getErrorCode()))) {
								ErrorCodeDetail presentErrorCodeDetail = errorCodeDetailRepo.findByTechnicalErrorCode(pparts.getErrorCode());
								formulaBean.setErrorCode(presentErrorCodeDetail.getBusinessErrorCode());
								formulaBean.setErrorCodeDetail(presentErrorCodeDetail);
								formulaBean.setTechnicalErrorCode(presentErrorCodeDetail.getTechnicalErrorCode());
								formulaBean.setErrorCode(presentErrorCodeDetail.getBusinessErrorCode());
								formulaBean.setErrorMessage(presentErrorCodeDetail.getErrorDescription());
							} else {
								formulaBean.setErrorCode(error);
							}
							formulaBean.setErrorType(pparts.getErrorType());
							if (Objects.isNull(formulaBean.getErrorType())) {
								formulaBean.setErrorType("E");
							}
							if (action == 1) {
								table.append("<tr id = '" + error + "'>").append("<td>").append(error).append("</td>").append("<td>").append("<a onClick=\"editFormula('" + error + "')\"><i class=\"fa fa-pencil-square\"></i></a>").append("</td>").append("<td>").append("<a onClick=\"deleteFormula('" + error + "')\"><i class=\"fa fa-trash-o\"></i></a>").append("</td>").append("</tr>");
							} else {
								if (formulaBean.getErrorType().equals("E")) {
									formulaBean.setErrorType("Error");
								} else {
									formulaBean.setErrorType("Warning");
								}
								table.append("<tr id = '" + error + "'>").append("<td>").append(error).append("</td>").append("<td>").append("<a onClick=\"editFormula('" + error + "')\"><i class=\"fa fa-pencil-square\"></i></a>").append("</td>").append("</tr>");
							}
							formulaJsonMap.put(error, new Gson().toJson(formulaBean));
						}
					}
				}
				formula.setFormulaJsonMap(formulaJsonMap);
			}
			if (!CollectionUtils.isEmpty(formulaGenList)) {
				for (FormulaGen formulaGen : formulaGenList) {
					String json = formulaGen.getFormulaJson();
					Type listToken = new TypeToken<FormulaBean>() {
					}.getType();
					FormulaBean formulaBeanObj = new Gson().fromJson(json, listToken);
					if (!StringUtils.isBlank(formulaBeanObj.getFormula())) {
						formulaBeanObj = formulaConverter.convert(formulaBeanObj);
					}
					if (!CollectionUtils.isEmpty(formulaBeanObj.getNormalFormula())) {
						Set<Entry<String, List<AutoCalFormulaParts>>> entry = formulaBeanObj.getNormalFormula().entrySet();
						Iterator<Entry<String, List<AutoCalFormulaParts>>> entryIterator = entry.iterator();
						while (entryIterator.hasNext()) {
							Entry<String, List<AutoCalFormulaParts>> llt = entryIterator.next();
							List<AutoCalFormulaParts> list = llt.getValue();
							for (AutoCalFormulaParts pparts : list) {
								if (!StringUtils.isBlank(pparts.getErrorCode()) && errorCode.add(pparts.getErrorCode())) {
									if (tableIds != null && Boolean.FALSE.equals(validElrFormula(pparts, tableIds))) {
										continue;
									}
									String error = errorCodeMap.get(pparts.getErrorCode()) == null ? pparts.getErrorCode() : errorCodeMap.get(pparts.getErrorCode()).getBusinessErrorCode();
									Map<String, List<AutoCalFormulaParts>> normalFormulaMap = new HashMap<>();
									List<AutoCalFormulaParts> normalFormulaList = new ArrayList<>();
									pparts.setFormulaId(formulaGen.getFormulaId());
									normalFormulaList.add(pparts);
									normalFormulaMap.put(pparts.getErrorCode(), normalFormulaList);
									formulaBeanObj.setNormalFormula(normalFormulaMap);
									formulaBeanObj.setFormulaId(formulaGen.getFormulaId());
									formulaBeanObj.setErrorType(pparts.getErrorType());
									formulaBeanObj.setErrorCode(error);
									formulaBeanObj.setTechnicalErrorCode(pparts.getErrorCode());
									if (Objects.isNull(formulaBeanObj.getErrorType())) {
										formulaBeanObj.setErrorType("E");
									}
									if (action == 1) {
										table.append("<tr id = '" + error + "'>").append("<td>").append(error).append("</td>").append("<td>").append("<a onClick=\"editFormula('" + error + "')\"><i class=\"fa fa-pencil-square\"></i></a>").append("</td>").append("<td>").append("<a onClick=\"deleteFormula('" + error + "')\"><i class=\"fa fa-trash-o\"></i></a>").append("</td>").append("</tr>");
									} else {
										if (formulaBeanObj.getErrorType().equals("E")) {
											formulaBeanObj.setErrorType("Error");
										} else {
											formulaBeanObj.setErrorType("Information");
										}
										//									formulaBeanObj.setEventType(getEventType(formulaBeanObj));
										table.append("<tr id = '" + error + "'>").append("<td>").append(error).append("</td>").append("<td>").append("<a onClick=\"editFormula('" + error + "')\"><i class=\"fa fa-pencil-square\"></i></a>").append("</td>").append("</tr>");
									}
									formulaJsonMap.put(error, new Gson().toJson(formulaBeanObj));
								}
							}
						}
					} else if (!CollectionUtils.isEmpty(formulaBeanObj.getCrossElrFormula())) {
						List<AutoCalFormulaParts> crossitems = formulaBeanObj.getCrossElrFormula();
						for (AutoCalFormulaParts pparts : crossitems) {
							if (tableIds != null && Boolean.FALSE.equals(validElrFormula(pparts, tableIds))) {
								continue;
							}
							if (errorCode.add(pparts.getErrorCode())) {
								String error = errorCodeMap.get(pparts.getErrorCode()) == null ? pparts.getErrorCode() : errorCodeMap.get(pparts.getErrorCode()).getBusinessErrorCode();
								List<AutoCalFormulaParts> crossElrFormula = new ArrayList<>();
								pparts.setFormulaId(formulaGen.getFormulaId());
								crossElrFormula.add(pparts);
								formulaBeanObj.setCrossElrFormula(crossElrFormula);
								formulaBeanObj.setFormulaId(formulaGen.getFormulaId());
								System.out.println(pparts.getErrorCode());
								formulaBeanObj.setErrorCode(error);
								formulaBeanObj.setTechnicalErrorCode(pparts.getErrorCode());
								formulaBeanObj.setErrorType(pparts.getErrorType());
								if (Objects.isNull(formulaBeanObj.getErrorType())) {
									formulaBeanObj.setErrorType("E");
								}
								if (action == 1) {
									table.append("<tr id = '" + error + "'>").append("<td>").append(error).append("</td>").append("<td>").append("<a onClick=\"editFormula('" + error + "')\"><i class=\"fa fa-pencil-square\"></i></a>").append("</td>").append("<td>").append("<a onClick=\"deleteFormula('" + error + "')\"><i class=\"fa fa-trash-o\"></i></a>").append("</td>").append("</tr>");
								} else {
									if (formulaBeanObj.getErrorType().equals("E")) {
										formulaBeanObj.setErrorType("Error");
									} else {
										formulaBeanObj.setErrorType("Information");
									}
									table.append("<tr id = '" + error + "'>").append("<td>").append(error).append("</td>").append("<td>").append("<a onClick=\"editFormula('" + error + "')\"><i class=\"fa fa-pencil-square\"></i></a>").append("</td>").append("</tr>");
								}
								formulaJsonMap.put(error, new Gson().toJson(formulaBeanObj));
							}
						}
					}
				}
				formula.setFormulaJsonMap(formulaJsonMap);
			}
			table.append("</table></div>");
			formula.setTable(table.toString());
			formula.setTableIdElrIdMap(tableIdElrIdMap);
		} catch (Exception e) {
			logger.error("Exception : ", e);
			return null;
		}
		return formula;
	}

	/**
	 * @param table
	 * @param formulaJsonMap
	 * @param tableIds
	 * @param errorCode
	 * @param errorCodeMap
	 * @param innerd
	 */
	private void prepareErrorCodeTableDiv(StringBuilder table, Map<String, String> formulaJsonMap, Set<Long> tableIds, Set<String> errorCode, Map<String, ErrorCodeDetail> errorCodeMap, Map<String, List<AutoCalFormulaParts>> innerd, String eventType, Integer action) {
		if (!CollectionUtils.isEmpty(innerd) || innerd != null) {
			Set<Entry<String, List<AutoCalFormulaParts>>> entry = innerd.entrySet();
			Iterator<Entry<String, List<AutoCalFormulaParts>>> entryIterator = entry.iterator();
			while (entryIterator.hasNext()) {
				Entry<String, List<AutoCalFormulaParts>> llt = entryIterator.next();
				List<AutoCalFormulaParts> list = llt.getValue();
				for (AutoCalFormulaParts pparts : list) {
					if (!StringUtils.isBlank(pparts.getErrorCode()) && errorCode.add(pparts.getErrorCode())) {
						System.out.println(pparts.getErrorCode());
						if (tableIds != null && Boolean.FALSE.equals(validElrFormula(pparts, tableIds))) {
							continue;
						}
						String error = errorCodeMap.get(pparts.getErrorCode()) == null ? pparts.getErrorCode() : errorCodeMap.get(pparts.getErrorCode()).getBusinessErrorCode();
						FormulaBean formulaBean = new FormulaBean();
						Map<String, List<AutoCalFormulaParts>> normalFormulaMap = new HashMap<>();
						List<AutoCalFormulaParts> normalFormulaList = new ArrayList<>();
						normalFormulaList.add(pparts);
						normalFormulaMap.put(pparts.getErrorCode(), normalFormulaList);
						formulaBean.setNormalFormula(normalFormulaMap);
						formulaBean.setFormulaId(pparts.getFormulaId());
						formulaBean.setErrorCode(error);
						formulaBean.setTechnicalErrorCode(pparts.getErrorCode());
						formulaBean.setErrorType(pparts.getErrorType());
						formulaBean.setEventType(eventType);
						if (Objects.isNull(formulaBean.getErrorType())) {
							formulaBean.setErrorType("E");
						}
						if (action == 1) {
							table.append("<tr id = '" + error + "'>").append("<td>").append(error).append("</td>").append("<td>").append("<a onClick=\"editFormula('" + error + "')\"><i class=\"fa fa-pencil-square\"></i></a>").append("</td>").append("<td>").append("<a onClick=\"deleteFormula('" + error + "')\"><i class=\"fa fa-trash-o\"></i></a>").append("</td>").append("</tr>");
						} else {
							if (formulaBean.getErrorType().equals("E")) {
								formulaBean.setErrorType("Error");
							} else {
								formulaBean.setErrorType("Warning");
							}
							formulaBean.setEventType(getEventType(formulaBean));
							table.append("<tr id = '" + error + "'>").append("<td>").append(error).append("</td>").append("<td>").append("<a onClick=\"editFormula('" + error + "')\"><i class=\"fa fa-pencil-square\"></i></a>").append("</td>").append("</tr>");
						}
						formulaJsonMap.put(error, new Gson().toJson(formulaBean));
					}
				}
			}
		}
	}

	private String getReturnErrorcode(String returnCode, int count, String errorCode) {
		String errorInitials = "E" + returnCode + "T";
		ErrorCodeDetail detail = errorCodeDetailRepo.findMaxErrorCodeDetail(errorInitials);
		if (detail == null) {
			if (errorCode != null) {
				detail = new ErrorCodeDetail();
				detail.setTechnicalErrorCode(errorCode);
				count = 0;
			}
		}
		if (detail == null) {
			return errorInitials + "00001";
		} else {
			String numberPart = detail.getTechnicalErrorCode().substring(errorInitials.length(), detail.getTechnicalErrorCode().length());
			Integer numberValue = Integer.valueOf(numberPart);
			numberValue = numberValue + count;
			numberValue++;
			int numberLength = String.valueOf(numberValue).length();
			if (numberLength < 5) {
				String numberPartPrep = "00000";
				numberPartPrep = numberPartPrep.substring(0, 5 - numberLength);
				numberPart = numberPartPrep + numberValue;
			} else {
				numberPart = String.valueOf(numberValue);
			}
			return errorInitials + numberPart;
		}
	}

	private Map<String, ErrorCodeDetail> getErrorcodeMap(Long returnTemplateId) {
		Map<String, ErrorCodeDetail> result = new HashMap<>();
		List<ErrorCodeDetail> detail = null;
		detail = errorCodeDetailRepo.findErrorCodeDetailByReturnTemplateId(returnTemplateId);
		for (ErrorCodeDetail item : detail) {
			result.put(item.getTechnicalErrorCode(), item);
		}
		return result;
	}

	private String getEventType(FormulaBean formulaBean) {
		String eventType = "onblurEvent";
		if (StringUtils.isBlank(formulaBean.getEventType())) {
			return eventType;
		}

		switch (formulaBean.getEventType()) {
		case "1":
			eventType = "onblurEvent";
			break;

		case "2":
			eventType = "onchangeEvent";
			break;

		case "3":
			eventType = "onblurSummationEvent";
			break;

		default:
			eventType = "onblurEvent";
		}
		return eventType;
	}

	@Transactional(rollbackFor = Exception.class)
	public FormulaBean deleteFormula(FormulaBean formula) {
		FormulaGen entity = new FormulaGen();
		if (formula.getFormulaId() != null) {
			entity.setFormulaId(formula.getFormulaId());
			entity = formulaGenRepo.getDataByFormulaId(formula.getFormulaId());
			entity.setIsActive(false);
			entity = formulaGenRepo.save(entity);
			formula.setFormulaId(entity.getFormulaId());
		}
		return formula;
	}

	/**
	 * @param formula
	 * @param keySet
	 * @param innerd
	 */
	private void checkAndRemoveFormula(FormulaBean formula, Set<String> keySet, Map<String, List<AutoCalFormulaParts>> innerd) {
		if (innerd != null) {
			for (Entry<String, List<AutoCalFormulaParts>> item : innerd.entrySet()) {
				ListIterator<AutoCalFormulaParts> list = item.getValue().listIterator();
				while (list.hasNext()) {
					AutoCalFormulaParts autoCalItem = list.next();
					if (StringUtils.equals(formula.getTechnicalErrorCode(), autoCalItem.getErrorCode())) {
						list.remove();
					}
				}
				keySet.add(item.getKey());
			}
			for (String key : keySet) {
				List<AutoCalFormulaParts> autoCalList = innerd.get(key);
				if (CollectionUtils.isEmpty(autoCalList)) {
					innerd.remove(key);
				}
			}
		}
	}

	private Set<Long> getSelectedTableIds(Long templateId) {
		Set<Long> tableIds = new HashSet<>();
		List<ReturnSectionVersionMap> autoCalList = returnSectionVersionMapRepo.findByReturnTemplate(templateId);
		for (ReturnSectionVersionMap item : autoCalList) {
			tableIds.add(item.getReturnSecIdFk().getReturnSectionMapId());
			tableIdElrIdMap.put(item.getReturnSecIdFk().getReturnSectionMapId(), item.getReturnSecIdFk().getHeaderIdFk().getHeaderId());
		}
		return tableIds;
	}

	private Boolean validElrFormula(AutoCalFormulaParts formula, Set<Long> hearset) {
		Boolean result = true;
		//		logger.info("Formula  validElrFormula : "+ new Gson().toJson(formula));
		if (StringUtils.equals(formula.getFormulaCell(), "NA")) {
			//			logger.info("Formula  LHS: "+ new Gson().toJson(formula.getLhs()));
			String[] lhs = formula.getLhs().split(" ");
			for (String lhsItem : lhs) {
				String[] lshFraction = lhsItem.split("-");
				if (lshFraction.length > 1 && !hearset.contains(Long.valueOf(lshFraction[1]))) {
					result = false;
					break;
				}
			}
			if (Boolean.TRUE.equals(result)) {
				String[] rhs = formula.getRhs().split(" ");
				for (String rhsItem : rhs) {
					String[] rshFraction = rhsItem.split("-");
					if (rshFraction.length > 1 && !hearset.contains(Long.valueOf(rshFraction[1]))) {
						result = false;
						break;
					}
				}
			}
		} else {
			String[] lhs = formula.getFormulaCell().split(" ");
			for (String lhsItem : lhs) {
				String[] lshFraction = lhsItem.split("-");
				if (lshFraction.length > 1 && !hearset.contains(Long.valueOf(lshFraction[1]))) {
					result = false;
					break;
				}
			}
			if (Boolean.TRUE.equals(result)) {
				String[] rhs = formula.getFormula().split(" ");
				for (String rhsItem : rhs) {
					String[] rshFraction = rhsItem.split("-");
					if (rshFraction.length > 1 && !hearset.contains(Long.valueOf(rshFraction[1]))) {
						result = false;
						break;
					}
				}
			}
		}
		return result;
	}

	// To add single validation to Error Code table on UI
	public FormulaBean prepareFormulaTableRow(FormulaBean formula) {
		StringBuilder tableRow = new StringBuilder();
		if (!StringUtils.isBlank(formula.getTable()) && Boolean.TRUE.equals(formula.getIsUpdatedFormula())) {
			String error = formula.getErrorCode();
			tableRow.append("<tr id = '" + formula.getTechnicalErrorCode() + "'>").append("<td>").append(error).append("</td>").append("<td>").append("<a onClick=\"editFormula('" + formula.getErrorCode() + "')\"><i class=\"fa fa-pencil-square\"></i></a>").append("</td>").append("<td>").append("<a onClick=\"deleteFormula('" + formula.getTechnicalErrorCode() + "')\"><i class=\"fa fa-trash-o\"></i></a>").append("</td>").append("</tr>");
		} else if (!StringUtils.isBlank(formula.getFormula())) {
			String error = formula.getErrorCode();
			tableRow.append("<tr id = '" + formula.getErrorCode() + "'>").append("<td>").append(error).append("</td>").append("<td>").append("<a onClick=\"editFormula('" + formula.getErrorCode() + "')\"><i class=\"fa fa-pencil-square\"></i></a>").append("</td>").append("<td>").append("<a onClick=\"deleteFormula('" + formula.getErrorCode() + "')\"><i class=\"fa fa-trash-o\"></i></a>").append("</td>").append("</tr>");
		}
		formula.setTable(tableRow.toString());
		return formula;
	}

	public List<AutoCalculationFormula> getAutoCalculationFormulaList(Long returnId) {
		return autoCalculationFormulaRepo.findByReturnIdFkReturnIdAndIsActiveTrue(returnId);
	}

	public FormulaBean saveOldFormulaGen(FormulaBean formula) throws JsonProcessingException {
		formula.getFormulaJsonMap();
		logger.info("into saveOldFormulaGen Formula Service");
		try {
			Set<String> errorCodeSet = formula.getFormulaJsonMap().keySet();
			Map<String, Long> formulaIdMap = new HashMap<>();
			ReturnTemplate returnTemplate = returnTemplateRepository.findByReturnTemplateId(formula.getReturnTemplateId());
			if (!Objects.isNull(returnTemplate)) {
				for (String str : errorCodeSet) {
					String formulaBeanStr = formula.getFormulaJsonMap().get(str);
					Type listToken = new TypeToken<FormulaBean>() {
					}.getType();
					FormulaBean formulaBean = new Gson().fromJson(formulaBeanStr, listToken);
					ErrorCodeDetail detail = errorCodeDetailRepo.findByBusinessErrorCode(formulaBean.getErrorCode(), "E" + formula.getReturnCode());
					if (!Objects.isNull(detail)) {
						detail.setFormulaCategoryTypeIdFk(null);
						formulaBean.setErrorCodeDetail(detail);
						formulaBean.setTechnicalErrorCode(detail.getTechnicalErrorCode());
						formulaBean.setErrorCode(detail.getBusinessErrorCode());
						formulaBean.setErrorMessage(detail.getErrorDescription());
					}
					if (Objects.isNull(formulaBean.getErrorType())) {
						formulaBean.setErrorType("E");
					}
					FormulaGen entity = new FormulaGen();
					formulaBean.setTable(null);
					formulaBean.setFormulaJsonMap(null);
					formulaBean.setFormulaBeanMap(null);
					formulaBean.setUserId(formula.getUserId());
					formulaBean.setReturnCode(formula.getReturnCode());
					formulaBean.setReturnTemplateId(formula.getReturnTemplateId());
					formulaBean.setIsUpdatedFormula(false);
					if (formulaBean.getFormulaId() != null) {
						entity = formulaGenRepo.getDataByFormulaId(formulaBean.getFormulaId());
						logger.info("into saveOldFormulaGen Formula Service" + entity.getFormulaId());
						if (!formula.getReturnTemplateId().equals(entity.getReturnTemplateIdFk().getReturnTemplateId())) {
							logger.info("into saveOldFormulaGen Formula Service return template Id update" + entity.getFormulaId());
							String formulaBeanObjStr = entity.getFormulaJson();
							listToken = new TypeToken<FormulaBean>() {
							}.getType();
							FormulaBean formulaBeanObj = new Gson().fromJson(formulaBeanObjStr, listToken);
							formulaBeanObj.setFormulaId(null);
							formulaBeanObj.setTable(null);
							formulaBeanObj.setFormulaJsonMap(null);
							formulaBeanObj.setFormulaBeanMap(null);
							formulaBeanObj.setUserId(formula.getUserId());
							formulaBeanObj.setReturnCode(formula.getReturnCode());
							formulaBeanObj.setReturnTemplateId(formula.getReturnTemplateId());
							formulaBeanObj.setIsUpdatedFormula(false);
							if (!Objects.isNull(detail)) {
								FormulaCategoryType formulaCategoryType = new FormulaCategoryType();
								formulaCategoryType.setFormulaCategoryTypeId(Long.valueOf(1));
								detail.setFormulaCategoryTypeIdFk(formulaCategoryType);
								formulaBeanObj.setErrorCodeDetail(detail);
								formulaBeanObj.setTechnicalErrorCode(detail.getTechnicalErrorCode());
								formulaBeanObj.setErrorCode(detail.getBusinessErrorCode());
								formulaBeanObj.setErrorMessage(detail.getErrorDescription());
							}
							FormulaGen newEntity = new FormulaGen();
							ObjectMapper objectMapper = new ObjectMapper();
							newEntity.setFormulaJson(objectMapper.writeValueAsString(formulaBeanObj));
							UserMaster userMaster = userMasterRepo.findByUserId(formula.getUserId());
							newEntity.setModifiedByFk(userMaster);
							newEntity.setReturnTemplateIdFk(returnTemplate);
							newEntity = prepareFormulaGenObject(formula, newEntity);
							formulaIdMap.put(str, newEntity.getFormulaId());
						} else {
							logger.info("into saveOldFormulaGen Formula Service return template id present" + entity.getFormulaId());
							formulaIdMap.put(str, formulaBean.getFormulaId());
						}
					} else {
						ObjectMapper objectMapper = new ObjectMapper();
						entity.setFormulaJson(objectMapper.writeValueAsString(formulaBean));
						UserMaster userMaster = userMasterRepo.findByUserId(formula.getUserId());
						entity.setModifiedByFk(userMaster);
						entity.setFormulaId(null);
						entity.setReturnTemplateIdFk(returnTemplate);
						entity = prepareFormulaGenObject(formula, entity);
						logger.info("into saveOldFormulaGen Formula Service new return template entry" + entity.getFormulaId());
						formulaIdMap.put(str, entity.getFormulaId());
					}
				}
			}
			formula.setFormulaIdMap(formulaIdMap);
			Map<String, String> fromulaJsonMap = new HashMap<>();
			formula.setFormulaJsonMap(fromulaJsonMap);
		} catch (Exception e) {
			logger.error("Exception : ", e);
			return null;
		}
		return formula;
	}

	//This method is to save single validation in FormulaGen table
	@Transactional(rollbackFor = Exception.class)
	public FormulaBean saveAndGetFormulaGen(FormulaBean formula) {
		try {
			FormulaGen entity = new FormulaGen();
			if (formula.getFormulaId() != null) {
				entity.setFormulaId(formula.getFormulaId());
				formula.setIsUpdatedFormula(true);
			} else {
				formula.setIsUpdatedFormula(false);
			}
			prepareErrorCodeDetailObjectTemp(formula);
			formula = formulaConverter.convert(formula);
			formula.setTable(null);
			formula.setFormulaJsonMap(null);
			formula.setFormulaBeanMap(null);
			ReturnTemplate returnTemplate = new ReturnTemplate();
			returnTemplate.setReturnTemplateId(formula.getReturnTemplateId());
			entity.setReturnTemplateIdFk(returnTemplate);
			ObjectMapper objectMapper = new ObjectMapper();
			entity.setFormulaJson(objectMapper.writeValueAsString(formula));
			UserMaster userMaster = userMasterRepo.findByUserId(formula.getUserId());
			entity.setModifiedByFk(userMaster);
			entity = prepareFormulaGenObject(formula, entity);
			formula.setFormulaId(entity.getFormulaId());
		} catch (Exception e) {
			logger.error("Exception : ", e);
			return null;
		}
		return formula;
	}

	/**
	 * @param formula
	 * @param entity
	 * @return
	 * @throws JsonProcessingException
	 */
	private FormulaGen prepareFormulaGenObject(FormulaBean formula, FormulaGen entity) throws JsonProcessingException {
		entity.setIsActive(true);
		entity.setModifedOn(new Date());
		Return returnEn = returnRepo.findByReturnCode(formula.getReturnCode());
		entity.setReturnIdFk(returnEn);
		entity = formulaGenRepo.save(entity);
		return entity;
	}

	public Boolean checkIfFormulaPresentBeforeAdd(FormulaBean formula) {
		AutoCalculationFormula autoCal = autoCalculationFormulaRepo.findByAutoCalVesrionReturnTemplateFkReturnTemplateId(formula.getReturnTemplateId());
		prepareErrorCodeDetailObjectTemp(formula);
		JSONObject jsonObject = formulaConverter.checkIfFormulaPresent(formula, autoCal);
		if (!Objects.isNull(jsonObject)) {
			return true;
		}
		return false;
	}

	private ErrorCodeDetail prepareErrorCodeDetailObjectTemp(FormulaBean formulaBeanObj) {
		ErrorCodeDetail errorDetail = new ErrorCodeDetail();
		errorDetail.setTechnicalErrorCode(formulaBeanObj.getErrorCode());
		setErrorCodeDetailsValues(formulaBeanObj, errorDetail);
		formulaBeanObj.setErrorCodeDetail(errorDetail);
		return errorDetail;
	}

	public Map<Long, Integer> getTableIdElrIdMap() {
		return tableIdElrIdMap;
	}

	public void setTableIdElrIdMap(Map<Long, Integer> tableIdElrIdMap) {
		this.tableIdElrIdMap = tableIdElrIdMap;
	}

	public FormulaBean getFormulaGen(FormulaBean formula) {
		FormulaGen entity = new FormulaGen();
		if (formula.getFormulaId() != null) {
			entity.setFormulaId(formula.getFormulaId());
			entity = formulaGenRepo.getDataByFormulaId(formula.getFormulaId());
			String formulaBeanObjStr = entity.getFormulaJson();
			Type listToken = new TypeToken<FormulaBean>() {
			}.getType();
			formula = new Gson().fromJson(formulaBeanObjStr, listToken);
			formula.setFormulaId(entity.getFormulaId());
			if (CollectionUtils.isEmpty(formula.getNormalFormula()) && CollectionUtils.isEmpty(formula.getCrossElrFormula())) {
				if (!Objects.isNull(formula.getFormula())) {
					prepareErrorCodeDetailObjectTemp(formula);
					formula = formulaConverter.convert(formula);
					if (!CollectionUtils.isEmpty(formula.getNormalFormula())) {
						Set<Entry<String, List<AutoCalFormulaParts>>> entry = formula.getNormalFormula().entrySet();
						Iterator<Entry<String, List<AutoCalFormulaParts>>> entryIterator = entry.iterator();
						while (entryIterator.hasNext()) {
							Entry<String, List<AutoCalFormulaParts>> llt = entryIterator.next();
							List<AutoCalFormulaParts> list = llt.getValue();
							for (AutoCalFormulaParts pparts : list) {
								if (!StringUtils.isBlank(pparts.getErrorCode())) {
									Map<String, List<AutoCalFormulaParts>> normalFormulaMap = new HashMap<>();
									List<AutoCalFormulaParts> normalFormulaList = new ArrayList<>();
									pparts.setFormulaId(entity.getFormulaId());
									normalFormulaList.add(pparts);
									normalFormulaMap.put(formula.getErrorCode(), normalFormulaList);
									formula.setNormalFormula(normalFormulaMap);
									ErrorCodeDetail presentErrorCodeDetail = errorCodeDetailRepo.findByTechnicalErrorCode(pparts.getErrorCode());
									if (!Objects.isNull(presentErrorCodeDetail)) {
										formula.setErrorCode(presentErrorCodeDetail.getBusinessErrorCode());
										formula.setTechnicalErrorCode(presentErrorCodeDetail.getTechnicalErrorCode());
									}
									break;
								}
							}
						}
					}
				} else if (!CollectionUtils.isEmpty(formula.getCrossElrFormula())) {
					AutoCalFormulaParts pparts = formula.getCrossElrFormula().get(0);
					List<AutoCalFormulaParts> crossElrFormula = new ArrayList<>();
					pparts.setFormulaId(entity.getFormulaId());
					crossElrFormula.add(pparts);
					formula.setCrossElrFormula(crossElrFormula);
					ErrorCodeDetail presentErrorCodeDetail = errorCodeDetailRepo.findByTechnicalErrorCode(pparts.getErrorCode());
					if (!Objects.isNull(presentErrorCodeDetail)) {
						formula.setErrorCode(presentErrorCodeDetail.getBusinessErrorCode());
						formula.setTechnicalErrorCode(presentErrorCodeDetail.getTechnicalErrorCode());
					}
				}
			} else if (!CollectionUtils.isEmpty(formula.getNormalFormula())) {
				Set<Entry<String, List<AutoCalFormulaParts>>> entry = formula.getNormalFormula().entrySet();
				Iterator<Entry<String, List<AutoCalFormulaParts>>> entryIterator = entry.iterator();
				while (entryIterator.hasNext()) {
					Entry<String, List<AutoCalFormulaParts>> llt = entryIterator.next();
					List<AutoCalFormulaParts> list = llt.getValue();
					for (AutoCalFormulaParts pparts : list) {
						if (!StringUtils.isBlank(pparts.getErrorCode())) {
							Map<String, List<AutoCalFormulaParts>> normalFormulaMap = new HashMap<>();
							List<AutoCalFormulaParts> normalFormulaList = new ArrayList<>();
							pparts.setFormulaId(entity.getFormulaId());
							normalFormulaList.add(pparts);
							normalFormulaMap.put(formula.getErrorCode(), normalFormulaList);
							formula.setNormalFormula(normalFormulaMap);
							ErrorCodeDetail presentErrorCodeDetail = errorCodeDetailRepo.findByTechnicalErrorCode(pparts.getErrorCode());
							if (!Objects.isNull(presentErrorCodeDetail)) {
								formula.setErrorCode(presentErrorCodeDetail.getBusinessErrorCode());
								formula.setTechnicalErrorCode(presentErrorCodeDetail.getTechnicalErrorCode());
							}
							break;
						}
					}
				}
			} else if (!CollectionUtils.isEmpty(formula.getCrossElrFormula())) {
				AutoCalFormulaParts pparts = formula.getCrossElrFormula().get(0);
				List<AutoCalFormulaParts> crossElrFormula = new ArrayList<>();
				pparts.setFormulaId(entity.getFormulaId());
				crossElrFormula.add(pparts);
				formula.setCrossElrFormula(crossElrFormula);
				ErrorCodeDetail presentErrorCodeDetail = errorCodeDetailRepo.findByTechnicalErrorCode(pparts.getErrorCode());
				if (!Objects.isNull(presentErrorCodeDetail)) {
					formula.setErrorCode(presentErrorCodeDetail.getBusinessErrorCode());
					formula.setTechnicalErrorCode(presentErrorCodeDetail.getTechnicalErrorCode());
				}
			}
		}
		return formula;
	}

	//	@Transactional(rollbackFor = Exception.class)
	//	public void updateFormulaGen(FormulaBean formula) throws JsonProcessingException {
	//		List<FormulaGen> formulaGenList = formulaGenRepo.findByReturnTemplateIdFkReturnTemplateId(formula.getReturnTemplateId());
	//		Map<Long, FormulaGen> newFormulaGenMap = new HashMap<>();
	//		Map<Long, FormulaBean> newFormulaBeanMap = new HashMap<>();
	//		try {
	//			for (FormulaGen formulaGen : formulaGenList) {
	//				String json = formulaGen.getFormulaJson();
	//				Type listToken = new TypeToken<FormulaBean>() {
	//				}.getType();
	//				FormulaBean formulaBeanObj = new Gson().fromJson(json, listToken);
	//				formulaBeanObj.setFormulaId(formulaGen.getFormulaId());
	//				formulaBeanObj.setErrorCode(formulaBeanObj.getErrorCode().replaceAll(" ", "_"));
	//				prepareErrorCodeDetailObjectTemp(formulaBeanObj);
	//				formulaBeanObj = formulaConverter.convert(formulaBeanObj);
	//				newFormulaBeanMap.put(formulaGen.getFormulaId(), formulaBeanObj);
	//				newFormulaGenMap.put(formulaGen.getFormulaId(), formulaGen);
	//			}
	//			for (Long formulaId : newFormulaBeanMap.keySet()) {
	//				FormulaGen formulaGen = newFormulaGenMap.get(formulaId);
	//				ObjectMapper objectMapper = new ObjectMapper();
	//				formulaGen.setFormulaJson(objectMapper.writeValueAsString(newFormulaBeanMap.get(formulaId)));
	//				newFormulaGenMap.put(formulaId, formulaGen);
	//			}
	//			for (FormulaGen formulaGen : formulaGenList) {
	//				formulaGenRepo.save(formulaGen);
	//			}
	//		} catch (Exception e) {
	//			logger.error("Exception : ", e);
	//		}
	//	}

}
