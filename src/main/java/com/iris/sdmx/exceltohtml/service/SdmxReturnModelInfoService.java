
package com.iris.sdmx.exceltohtml.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iris.caching.ObjectCache;
import com.iris.dto.DataTemplateUploadValidationDetail;
import com.iris.exception.ApplicationException;
import com.iris.exception.ServiceException;
import com.iris.model.ReturnTemplate;
import com.iris.sdmx.exceltohtml.bean.FormulaValidationBean;
import com.iris.sdmx.exceltohtml.bean.ReturnFormulaBean;
import com.iris.sdmx.exceltohtml.bean.SdmxReturnModelInfoBean;
import com.iris.sdmx.exceltohtml.bean.SdmxReturnModelMappingBean;
import com.iris.sdmx.exceltohtml.entity.SdmxModelCodesEntity;
import com.iris.sdmx.exceltohtml.entity.SdmxReturnModelInfoEntity;
import com.iris.sdmx.exceltohtml.entity.SdmxReturnSheetInfoEntity;
import com.iris.sdmx.exceltohtml.helper.SdmxReturnModelInfoHelper;
import com.iris.sdmx.exceltohtml.repo.SdmxReturnModelInfoRepo;
import com.iris.sdmx.fusion.service.FusionApiService;
import com.iris.sdmx.fusion.util.FusionPropertiesConstant;
import com.iris.service.GenericService;
import com.iris.util.ResourceUtil;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;

/**
 * @author apagaria
 *
 */
@Service
@Transactional(readOnly = true)
public class SdmxReturnModelInfoService implements GenericService<SdmxReturnModelInfoEntity, Long> {

	private static final Logger LOGGER = LogManager.getLogger(SdmxReturnModelInfoService.class);

	@Autowired
	private SdmxReturnModelInfoRepo sdmxReturnModelInfoRepo;

	@Autowired
	private FusionApiService fusionApiService;

	@Override
	@Transactional(readOnly = false)
	public SdmxReturnModelInfoEntity add(SdmxReturnModelInfoEntity entity) throws ServiceException {
		return sdmxReturnModelInfoRepo.save(entity);
	}

	@Override
	public boolean update(SdmxReturnModelInfoEntity entity) throws ServiceException {
		return false;
	}

	@Override
	public List<SdmxReturnModelInfoEntity> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public SdmxReturnModelInfoEntity getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxReturnModelInfoEntity> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxReturnModelInfoEntity> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxReturnModelInfoEntity> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxReturnModelInfoEntity> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxReturnModelInfoEntity> getAllDataFor(Class bean, Long id) throws ServiceException {
		List<SdmxReturnModelInfoEntity> sdmxReturnModelInfoEntity = null;
		if (bean == null && id == null) {
			sdmxReturnModelInfoEntity = sdmxReturnModelInfoRepo.getAllSdmxReturnModelInfoEntity();
			return sdmxReturnModelInfoEntity;
		}
		return null;
	}

	@Override
	public void deleteData(SdmxReturnModelInfoEntity bean) throws ServiceException {

	}

	/**
	 * @param sdmxReturnModelInfoBean
	 * @param userId
	 */
	@Transactional(readOnly = false)
	public void saveEntityByBean(SdmxReturnModelInfoBean sdmxReturnModelInfoBean, Long userId) {
		SdmxReturnModelInfoEntity sdmxReturnModelInfoEntity = new SdmxReturnModelInfoEntity();
		SdmxReturnModelInfoHelper.convertBeanToEntity(sdmxReturnModelInfoBean, sdmxReturnModelInfoEntity, userId);
		add(sdmxReturnModelInfoEntity);
	}

	/**
	 * @param modelCodeId
	 * @return
	 */
	public boolean getBeanByModelCode(Long modelCodeId) {
		boolean isDataExist = false;
		if (sdmxReturnModelInfoRepo.findByModelCodesFk(new SdmxModelCodesEntity(modelCodeId)) > 0)
			isDataExist = true;
		return isDataExist;
	}

	/**
	 * @param returnTemplateId
	 * @return
	 */

	public List<SdmxReturnModelInfoBean> fetchMapCellRefByReturnTemplateId(Long returnTemplateId, Long returnPreviewIdFk) {
		List<SdmxReturnModelInfoBean> SdmxReturnModelInfoBeanList = new ArrayList<>();
		List<SdmxReturnModelInfoEntity> sdmxReturnModelInfoEntityList = sdmxReturnModelInfoRepo.fetchMapCellRefByReturnTemplateId(new ReturnTemplate(returnTemplateId), returnPreviewIdFk);
		SdmxReturnModelInfoBean sdmxReturnModelInfoBean;
		if (!sdmxReturnModelInfoEntityList.isEmpty()) {
			for (SdmxReturnModelInfoEntity entity : sdmxReturnModelInfoEntityList) {
				sdmxReturnModelInfoBean = new SdmxReturnModelInfoBean();
				sdmxReturnModelInfoBean.setCellFormula(entity.getCellFormula());
				sdmxReturnModelInfoBean.setReturnCellRef(entity.getReturnCellRef());
				SdmxReturnModelInfoBeanList.add(sdmxReturnModelInfoBean);
			}
		}

		return SdmxReturnModelInfoBeanList;

	}

	/**
	 * @param returnPreviewId
	 * @return
	 * @throws ApplicationException
	 */
	public List<ReturnFormulaBean> fetchFormulaBuilderJson(Long returnPreviewId) throws ApplicationException {
		List<String> cellFormulaList = null;
		cellFormulaList = sdmxReturnModelInfoRepo.fetchFormulaBuilderJsonFromTemplateId(returnPreviewId);
		List<Integer> dependentCellList = sdmxReturnModelInfoRepo.fetchTheDepedentCellNumbers(returnPreviewId);
		Gson gson = new Gson();
		List<ReturnFormulaBean> finalReturnFormulaBean = new ArrayList<>();
		Set<Integer> cellCalulationFormula = new HashSet<>();

		/*Properties prop = ResourceUtil.getResourcePropertyFile();
		String formulaValidation = prop.getProperty("data.template.publish.formula.validation");*/

		// Fetch value from table
		String formulaValidation = fusionApiService.getFusionPropertyValue(FusionPropertiesConstant.DATA_TEMPLATE_PUBLISH_FORMULA_VALIDATION.getConstant());

		if (!CollectionUtils.isEmpty(cellFormulaList)) {
			for (String cellFormula : cellFormulaList) {
				Type listToken = new TypeToken<List<ReturnFormulaBean>>() {
				}.getType();
				List<ReturnFormulaBean> returnFormulaBeanList = gson.fromJson(cellFormula, listToken);
				if (!CollectionUtils.isEmpty(returnFormulaBeanList)) {
					for (ReturnFormulaBean returnFormulaBean : returnFormulaBeanList) {
						if (!StringUtils.isBlank(formulaValidation) && Boolean.TRUE.toString().equalsIgnoreCase(formulaValidation)) {

							if (ColumnConstants.CALCULATION.getConstantVal().equalsIgnoreCase(returnFormulaBean.getFormulaType())) {
								if (!cellCalulationFormula.add(Integer.valueOf(returnFormulaBean.getLHS().replace("#", "").trim()))) {

									List<FormulaValidationBean> formulaValidationBeanList = new ArrayList<>();
									FormulaValidationBean formulaValidationBean = new FormulaValidationBean();
									formulaValidationBean.setErrorMsgCode(ErrorCode.E1271.toString());
									formulaValidationBean.setErrorMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1271.toString()));
									formulaValidationBean.setCellRef(returnFormulaBean.getLHS().replace("#", "").trim());
									formulaValidationBeanList.add(formulaValidationBean);

									DataTemplateUploadValidationDetail dataTemplateUploadValidationDetail = new DataTemplateUploadValidationDetail();
									dataTemplateUploadValidationDetail.setFormulaValidationBeanList(formulaValidationBeanList);

									LOGGER.error("[CALCULATION] Duplicate cell reference: " + returnFormulaBean.getLHS().replace("#", "").trim());

									throw new ApplicationException(ErrorCode.E0692.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1415.toString()));

								}
							}
						}
						List<String> dependentList = new ArrayList<>();
						dependentList = returnFormulaBean.getDependentList();
						// LHS
						String fromlaLHS = returnFormulaBean.getLHS();
						fromlaLHS = fromlaLHS.replace("ROUND", "numpy.ROUND");
						fromlaLHS = fromlaLHS.replace("MAX", "numpy.MAX");
						fromlaLHS = fromlaLHS.replace("MIN", "numpy.MIN");
						fromlaLHS = fromlaLHS.replace("SUM", "numpy.SUM");
						String[] lhsArray = fromlaLHS.split(" ");
						String finalLhsString = "";
						for (String lhsChunk : lhsArray) {
							if (lhsChunk.contains("#")) {
								lhsChunk = lhsChunk.replace("#", "");
								if (NumberUtils.isDigits(lhsChunk)) {

									String modelCode = sdmxReturnModelInfoRepo.fetchDmIdFromCellNumberNPreviewId(Integer.parseInt(lhsChunk), returnPreviewId);
									if (dependentList != null) {
										String lhs = "#" + lhsChunk;
										if (dependentList.contains(lhs)) {
											int index = dependentList.indexOf(lhs);
											dependentList.set(index, modelCode);

										}
									}

									finalLhsString = finalLhsString + "#" + modelCode + " ";
								}

							} else {
								finalLhsString = finalLhsString + lhsChunk + " ";
							}

						}
						returnFormulaBean.setLHS(finalLhsString);

						// RHS
						String fromlaRHS = returnFormulaBean.getRHS();
						fromlaRHS = fromlaRHS.replace("ROUND", "numpy.ROUND");
						fromlaRHS = fromlaRHS.replace("MAX", "numpy.MAX");
						fromlaRHS = fromlaRHS.replace("MIN", "numpy.MIN");
						fromlaRHS = fromlaRHS.replace("SUM", "numpy.SUM");
						String[] rhsArray = fromlaRHS.split(" ");
						String finalRhsString = "";
						for (String rhsChunk : rhsArray) {
							if (rhsChunk.contains("#")) {
								rhsChunk = rhsChunk.replace("#", "");
								if (NumberUtils.isDigits(rhsChunk)) {
									String modelCode = sdmxReturnModelInfoRepo.fetchDmIdFromCellNumberNPreviewId(Integer.parseInt(rhsChunk), returnPreviewId);
									if (dependentList != null) {
										String rhs = "#" + rhsChunk;
										if (dependentList.contains(rhs)) {
											int index = dependentList.indexOf(rhs);
											dependentList.set(index, modelCode);
										}
									}
									finalRhsString = finalRhsString + "#" + modelCode + " ";
								}

							} else {
								finalRhsString = finalRhsString + rhsChunk + " ";
							}

						}
						returnFormulaBean.setRHS(finalRhsString);

						// Dependent White Cell
						if (returnFormulaBean.getDependentWhiteCell() != null) {
							String dependentWhiteCell = returnFormulaBean.getDependentWhiteCell().replace("#", "");
							String modelCode = "";
							modelCode = sdmxReturnModelInfoRepo.fetchDmIdFromCellNumberNPreviewId(Integer.parseInt(dependentWhiteCell), returnPreviewId);
							returnFormulaBean.setDependentWhiteCell(modelCode);
						}
						finalReturnFormulaBean.add(returnFormulaBean);
					}
				}
			}
		}
		// Validation
		if (!StringUtils.isBlank(formulaValidation) && Boolean.TRUE.toString().equalsIgnoreCase(formulaValidation)) {
			if (!CollectionUtils.isEmpty(dependentCellList) && !CollectionUtils.isEmpty(cellCalulationFormula) && !cellCalulationFormula.containsAll(dependentCellList)) {
				List<FormulaValidationBean> formulaValidationBeanList = new ArrayList<>();
				FormulaValidationBean formulaValidationBean = new FormulaValidationBean();
				formulaValidationBeanList = new ArrayList<>();
				formulaValidationBean.setErrorMsgCode(ErrorCode.E1269.toString());
				formulaValidationBean.setErrorMessage(ObjectCache.getErrorCodeKey(ErrorCode.E1269.toString()));
				formulaValidationBeanList.add(formulaValidationBean);
				DataTemplateUploadValidationDetail dataTemplateUploadValidationDetail = new DataTemplateUploadValidationDetail();
				dataTemplateUploadValidationDetail.setFormulaValidationBeanList(formulaValidationBeanList);
				LOGGER.error("No model found for cell reference");
				throw new ApplicationException(ErrorCode.E1269.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1415.toString()));
			}
		}
		return finalReturnFormulaBean;
	}

	/**
	 * @param modelCodeId
	 * @param returnCellRef
	 * @return
	 */
	public SdmxReturnModelInfoBean findActiveEntityByModelCodesFkNCellRef(Long modelCodeId, Integer returnCellRef) {
		SdmxReturnModelInfoBean sdmxReturnModelInfoBean = null;
		SdmxReturnModelInfoEntity sdmxReturnModelInfoEntity = sdmxReturnModelInfoRepo.findActiveEntityByModelCodesFkNCellRef(new SdmxModelCodesEntity(modelCodeId), returnCellRef);
		if (sdmxReturnModelInfoEntity != null) {
			sdmxReturnModelInfoBean = new SdmxReturnModelInfoBean();
			SdmxReturnModelInfoHelper.convertEntityToBean(sdmxReturnModelInfoEntity, sdmxReturnModelInfoBean);
		}
		return sdmxReturnModelInfoBean;
	}

	/**
	 * @param modelCodeId
	 * @param returnCellRef
	 * @return
	 */
	public SdmxReturnModelInfoBean findByReturnSheetTemplateIdNCellRef(Long returnSheetInfoId, Integer returnCellRef) {
		SdmxReturnModelInfoBean sdmxReturnModelInfoBean = null;
		SdmxReturnModelInfoEntity sdmxReturnModelInfoEntity = sdmxReturnModelInfoRepo.findByReturnSheetTemplateIdNCellRef(new SdmxReturnSheetInfoEntity(returnSheetInfoId), returnCellRef);
		if (sdmxReturnModelInfoEntity != null) {
			sdmxReturnModelInfoBean = new SdmxReturnModelInfoBean();
			SdmxReturnModelInfoHelper.convertEntityToBean(sdmxReturnModelInfoEntity, sdmxReturnModelInfoBean);
		}
		return sdmxReturnModelInfoBean;
	}

	public SdmxReturnModelInfoBean findEntityByReturnTemplateNCellRef(Long returnTemplateIdFk, Integer returnCellRef, Long returnPreviewIdFk) {
		SdmxReturnModelInfoBean sdmxReturnModelInfoBean = null;
		SdmxReturnModelInfoEntity sdmxReturnModelInfoEntity = sdmxReturnModelInfoRepo.findEntityByReturnTemplateNCellRef(new ReturnTemplate(returnTemplateIdFk), returnCellRef, returnPreviewIdFk);
		if (sdmxReturnModelInfoEntity != null) {
			sdmxReturnModelInfoBean = new SdmxReturnModelInfoBean();
			SdmxReturnModelInfoHelper.convertEntityToBean(sdmxReturnModelInfoEntity, sdmxReturnModelInfoBean);
		}
		return sdmxReturnModelInfoBean;
	}

	/**
	 * @param modelCodesId
	 */
	@Transactional(readOnly = false)
	public void deleteEntityByBean(Long returnSheetInfoId) {
		SdmxReturnModelInfoEntity sdmxReturnModelInfoEntity = sdmxReturnModelInfoRepo.findByReturnModelInfoId(returnSheetInfoId);
		sdmxReturnModelInfoEntity.setIsActive(Boolean.FALSE);
		add(sdmxReturnModelInfoEntity);
	}

	/**
	 * @param returnTemplateIds
	 * @return
	 */
	public Map<String, List<Integer>> fetchReturnMappingCount(List<Long> returnTemplateIds) {
		Map<String, List<Integer>> returnValuesMap = new HashMap<>();
		List<SdmxReturnModelMappingBean> sdmxReturnModelMappingBeanList = sdmxReturnModelInfoRepo.fetchReturnMappingCount(returnTemplateIds);
		for (SdmxReturnModelMappingBean sdmxReturnModelMappingBean : sdmxReturnModelMappingBeanList) {
			List<Integer> returnCellRefList = null;
			if (returnValuesMap.containsKey(sdmxReturnModelMappingBean.getReturnTemplateId() + "~" + sdmxReturnModelMappingBean.getReturnPreviewIdFk() + "~" + sdmxReturnModelMappingBean.getEbrVersion())) {
				returnCellRefList = returnValuesMap.get(sdmxReturnModelMappingBean.getReturnTemplateId() + "~" + sdmxReturnModelMappingBean.getReturnPreviewIdFk() + "~" + sdmxReturnModelMappingBean.getEbrVersion());
				returnCellRefList.add(sdmxReturnModelMappingBean.getReturnCellRef());
			} else {
				returnCellRefList = new ArrayList<>();
				returnCellRefList.add(sdmxReturnModelMappingBean.getReturnCellRef());
			}
			returnValuesMap.put(sdmxReturnModelMappingBean.getReturnTemplateId() + "~" + sdmxReturnModelMappingBean.getReturnPreviewIdFk() + "~" + sdmxReturnModelMappingBean.getEbrVersion(), returnCellRefList);
		}
		return returnValuesMap;
	}

	/**
	 * @param returnTemplateId
	 * @return
	 */
	public List<SdmxReturnModelInfoEntity> findListByReturnTemplate(ReturnTemplate returnTemplateId, Long returnPreviewId) {
		return sdmxReturnModelInfoRepo.findListByReturnTemplate(returnTemplateId, returnPreviewId);
	}

	/**
	 * @param returnCellRef
	 * @param returnTemplateId
	 * @return
	 */
	public Boolean fetchIsMandatoryByReturnTemplateNCellRefId(Integer returnCellRef, Long returnTemplateId, Long returnPreviewId) {
		ReturnTemplate returnTemplate = new ReturnTemplate(returnTemplateId);
		return sdmxReturnModelInfoRepo.fetchIsMandatoryByReturnTemplateNCellRefId(returnCellRef, returnTemplate, returnPreviewId);

	}
}
