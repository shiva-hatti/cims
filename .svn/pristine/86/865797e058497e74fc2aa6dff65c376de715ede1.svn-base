/**
 * 
 */
package com.iris.sdmx.exceltohtml.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.iris.exception.ServiceException;
import com.iris.sdmx.element.entity.SdmxElementEntity;
import com.iris.sdmx.exceltohtml.bean.DimensionCodeListValueBean;
import com.iris.sdmx.exceltohtml.bean.DimensionDetailCategories;
import com.iris.sdmx.exceltohtml.bean.SdmxEleDimTypeMapBean;
import com.iris.sdmx.exceltohtml.bean.SdmxModelCodesBean;
import com.iris.sdmx.exceltohtml.entity.SdmxModelCodesEntity;
import com.iris.sdmx.exceltohtml.entity.SdmxReturnModelInfoEntity;
import com.iris.sdmx.exceltohtml.helper.SdmxModelCodesHelper;
import com.iris.sdmx.exceltohtml.repo.SdmxModelCodesRepo;
import com.iris.sdmx.model.code.data.SdmxDataModelCodeBean;
import com.iris.service.GenericService;

/**
 * @author apagaria
 *
 */
@Service
@Transactional(readOnly = true)
public class SdmxModelCodesService implements GenericService<SdmxModelCodesEntity, Long> {

	private static final Logger LOGGER = LogManager.getLogger(SdmxModelCodesService.class);

	@Autowired
	private SdmxModelCodesRepo sdmxModelCodesRepo;

	@Autowired
	private SdmxModelCodesHelper sdmxModelCodesHelper;

	@Override
	@Transactional(readOnly = false)
	public SdmxModelCodesEntity add(SdmxModelCodesEntity entity) throws ServiceException {
		return sdmxModelCodesRepo.save(entity);
	}

	@Override
	public boolean update(SdmxModelCodesEntity entity) throws ServiceException {
		return false;
	}

	@Override
	public List<SdmxModelCodesEntity> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public SdmxModelCodesEntity getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxModelCodesEntity> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxModelCodesEntity> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxModelCodesEntity> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxModelCodesEntity> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxModelCodesEntity> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(SdmxModelCodesEntity bean) throws ServiceException {

	}

	/**
	 * @param sdmxModelCodesBean
	 * @param userId
	 */
	@Transactional(readOnly = false)
	public Long saveEntityByBean(SdmxModelCodesBean sdmxModelCodesBean, Long userId) {
		SdmxModelCodesEntity sdmxModelCodesEntity = new SdmxModelCodesEntity();
		SdmxModelCodesHelper.convertBeanToEntity(sdmxModelCodesBean, sdmxModelCodesEntity, userId);
		// sdmxModelCodesEntity.setModelDimHash(new
		// String(Base64.encodeBase64(sdmxModelCodesEntity.getModelDim().getBytes())));
		add(sdmxModelCodesEntity);
		return sdmxModelCodesEntity.getModelCodesId();
	}

	/**
	 * @param modelCode
	 * @return
	 */
	public SdmxModelCodesBean getBeanByModelCode(String modelCode) {
		SdmxModelCodesBean sdmxModelCodesBean = null;
		SdmxModelCodesEntity sdmxModelCodesEntity = sdmxModelCodesRepo.findByModelCode(modelCode);
		if (sdmxModelCodesEntity != null) {
			sdmxModelCodesBean = new SdmxModelCodesBean();
			SdmxModelCodesHelper.convertEntityToBean(sdmxModelCodesEntity, sdmxModelCodesBean);
		}
		return sdmxModelCodesBean;
	}

	/**
	 * @param modelDim
	 * @return
	 */
	public SdmxModelCodesBean findEntityByModelDim(String modelDim) {
		SdmxModelCodesBean sdmxModelCodesBean = null;
		SdmxModelCodesEntity sdmxModelCodesEntity = sdmxModelCodesRepo.findEntityByModelDim(modelDim);
		if (sdmxModelCodesEntity != null) {
			sdmxModelCodesBean = new SdmxModelCodesBean();
			SdmxModelCodesHelper.convertEntityToBean(sdmxModelCodesEntity, sdmxModelCodesBean);
		}
		return sdmxModelCodesBean;
	}

	/**
	 * @param modelDim
	 * @return
	 */
	public List<SdmxModelCodesBean> findEntityByModelDimNElementId(String modelDim, Long elementIdFk) {
		List<SdmxModelCodesBean> sdmxModelCodesBeanList = null;
		SdmxElementEntity sdmxElementEntity = new SdmxElementEntity(elementIdFk);
		List<SdmxModelCodesEntity> sdmxModelCodesEntityList = sdmxModelCodesRepo.findEntityByModelDimNElementId(modelDim, sdmxElementEntity);
		if (!CollectionUtils.isEmpty(sdmxModelCodesEntityList)) {
			sdmxModelCodesBeanList = new ArrayList<>();
			for (SdmxModelCodesEntity sdmxModelCodesEntity : sdmxModelCodesEntityList) {
				SdmxModelCodesBean sdmxModelCodesBean = new SdmxModelCodesBean();
				SdmxModelCodesHelper.convertEntityToBean(sdmxModelCodesEntity, sdmxModelCodesBean);
				sdmxModelCodesBeanList.add(sdmxModelCodesBean);
			}
		}
		return sdmxModelCodesBeanList;
	}

	/**
	 * @param modelCodesId
	 */
	@Transactional(readOnly = false)
	public void deleteEntityByBean(Long modelCodesId) {
		SdmxModelCodesEntity sdmxModelCodesEntity = sdmxModelCodesRepo.findByModelCodesId(modelCodesId);
		sdmxModelCodesEntity.setIsActive(Boolean.FALSE);
		add(sdmxModelCodesEntity);
	}

	/**
	 * @param returnCellRef
	 * @param returnTemplateId
	 * @return
	 */
	public SdmxModelCodesBean findByReturnCellReffNReturnTemplate(Integer returnCellRef, Long returnTemplateId, Long returnPreviewId) {
		SdmxModelCodesBean sdmxModelCodesBean = null;
		// ReturnTemplate returnTemplateFk = new ReturnTemplate(returnTemplateId);
		// SdmxModelCodesEntity sdmxModelCodesEntity
		sdmxModelCodesBean = sdmxModelCodesRepo.findByReturnCellReffNReturnTemplate(returnCellRef, returnTemplateId, returnPreviewId);
		/*
		 * if (sdmxModelCodesEntity != null) { sdmxModelCodesBean = new
		 * SdmxModelCodesBean();
		 * SdmxModelCodesHelper.convertEntityToBean(sdmxModelCodesEntity,
		 * sdmxModelCodesBean); }
		 */
		return sdmxModelCodesBean;
	}

	/**
	 * @param modelCodeHash
	 * @return
	 */
	public SdmxModelCodesBean findEntityByModelCodeHash(String modelCodeHash) {
		SdmxModelCodesBean sdmxModelCodesBean = null;
		SdmxModelCodesEntity sdmxModelCodesEntity = sdmxModelCodesRepo.findEntityByModelCodeHash(modelCodeHash);
		if (sdmxModelCodesEntity != null) {
			sdmxModelCodesBean = new SdmxModelCodesBean();
			SdmxModelCodesHelper.convertEntityToBean(sdmxModelCodesEntity, sdmxModelCodesBean);
		}
		return sdmxModelCodesBean;
	}

	/**
	 * @param dmModelCode
	 * @return
	 */
	public SdmxModelCodesBean findEntityByDmModelCodes(String dmModelCode) {
		SdmxModelCodesBean sdmxModelCodesBean = null;
		List<SdmxModelCodesEntity> sdmxModelCodesEntity = sdmxModelCodesRepo.findEntityByModelCode(dmModelCode);
		if (!CollectionUtils.isEmpty(sdmxModelCodesEntity)) {
			sdmxModelCodesBean = new SdmxModelCodesBean();
			SdmxModelCodesHelper.convertEntityToBean(sdmxModelCodesEntity.get(0), sdmxModelCodesBean);
		}
		return sdmxModelCodesBean;
	}

	/**
	 * @param sdmxModelCodesBean
	 * @param userId
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void deleteDataByBean(SdmxModelCodesBean sdmxModelCodesBean, Long userId) throws ServiceException {
		sdmxModelCodesBean.setIsActive(false);
		SdmxModelCodesEntity sdmxModelCodesEntity = new SdmxModelCodesEntity();
		SdmxModelCodesHelper.convertBeanToEntity(sdmxModelCodesBean, sdmxModelCodesEntity, userId);
		add(sdmxModelCodesEntity);
	}

	/**
	 * @param dmModelCode
	 * @return
	 */
	public List<String> findDmModelCodes(String dmModelCode) {
		return sdmxModelCodesRepo.findDmModelCodes(dmModelCode);
	}

	/**
	 * @return
	 */
	public String findMaxDMIModelCodes(String modelCodeStr) {
		return sdmxModelCodesRepo.findMaxDMIModelCodes(modelCodeStr);
	}

	public String findMaxDMModelCodes(String modelCodeStr) {
		return sdmxModelCodesRepo.findMaxDMModelCodes(modelCodeStr);
	}

	public List<SdmxDataModelCodeBean> prepareDataForSDMXDataModel(List<SdmxReturnModelInfoEntity> sdmxDataModelCodeBeanList) {
		List<SdmxDataModelCodeBean> beans = new ArrayList<>();
		for (SdmxReturnModelInfoEntity entity : sdmxDataModelCodeBeanList) {
			SdmxDataModelCodeBean bean = new SdmxDataModelCodeBean();
			bean.setModelCode(entity.getModelCodesIdFk().getModelCode());
			bean.setReturnCellRef(entity.getReturnCellRef());
			bean.setSheetCode(entity.getReturnSheetInfoIdFk().getSheetCode());
			bean.setSectionCode(entity.getReturnSheetInfoIdFk().getSectionCode());
			bean.setDependencyTypeName(entity.getModelCodesIdFk().getElementIdFk().getDependencyTypeIdFk().getDependencyTypeName());
			bean.setDepartmentName(entity.getModelCodesIdFk().getElementIdFk().getRegulatorOwnerIdFk().getRegulatorName());
			// bean.setReturnCode(entity.getReturnSheetInfoIdFk().getReturnTemplateIdFk().getReturnObj().getReturnCode());
			bean.setReturnCode(entity.getReturnSheetInfoIdFk().getReturnTemplateIdFk().getReturnObj().getReturnCode());
			bean.setModelCodesId(entity.getModelCodesIdFk().getModelCodesId());
			bean.setElementId(entity.getModelCodesIdFk().getElementIdFk().getElementId());
			if (entity.getCreatedOn() != null) {
				bean.setLastUpdateOnInLong(entity.getCreatedOn().getTime());
				bean.setLastUpdatedOn(null);
			}

			beans.add(bean);
		}
		return beans;
	}

	/**
	 * @param jobProcessingId
	 */
	@Transactional(readOnly = false)
	public void deleteUnusedModelCodes(String jobProcessId) {

		// Fetch unused model code id's
		LOGGER.debug("Fetch Unused model codes with Job Processing ID : " + jobProcessId);
		List<Long> unUsedModelCodeIdList = sdmxModelCodesRepo.fetchdModelCodeIds();
		LOGGER.debug("Fetch Unused model codes received with Job Processing ID : " + jobProcessId);

		// Delete unused model code
		LOGGER.debug("Delete Unused model codes with Job Processing ID : " + jobProcessId);
		if (!CollectionUtils.isEmpty(unUsedModelCodeIdList)) {
			LOGGER.debug("Job Processing ID : " + jobProcessId + " Unused model codes count = " + unUsedModelCodeIdList.size());
			for (Long modelCodeId : unUsedModelCodeIdList) {
				deleteUnmappdedModelCodes(modelCodeId);
			}
		}
		LOGGER.debug("Delete Unused model codes complete with Job Processing ID : " + jobProcessId);
	}

	@Transactional(readOnly = false)
	public void modelGroupMapping(String jobProcessId) {
		// Fetch unused model code id's
		LOGGER.debug("Fetch model code list with Job Processing ID : " + jobProcessId);
		List<String> modelCodeList = sdmxModelCodesRepo.fetchModelCodesJson();
		LOGGER.debug("Fetch model code list received with Job Processing ID : " + jobProcessId);

		if (!CollectionUtils.isEmpty(modelCodeList)) {
			LOGGER.debug("Model code list received with Job Processing ID : " + jobProcessId + ", Size = " + modelCodeList.size());
			Gson gson = new Gson();
			for (String modelCodeHash : modelCodeList) {
				DimensionDetailCategories sdmxReturnModelHashBean = gson.fromJson(new String(Base64.decodeBase64(modelCodeHash)), DimensionDetailCategories.class);
				SdmxEleDimTypeMapBean sdmxEleDimTypeMapBean = new SdmxEleDimTypeMapBean();
				sdmxEleDimTypeMapBean.setDsdCode(sdmxReturnModelHashBean.getDsdId());
				sdmxEleDimTypeMapBean.setElementVer(sdmxReturnModelHashBean.getElementVersion());

				// Close Dimension
				List<DimensionCodeListValueBean> dimensionCodeListValueBeanList = sdmxReturnModelHashBean.getClosedDim();
				if (!CollectionUtils.isEmpty(dimensionCodeListValueBeanList)) {
					List<String> closedDimList = new ArrayList<>();
					for (DimensionCodeListValueBean dimensionCodeListValueBean : dimensionCodeListValueBeanList) {
						closedDimList.add(dimensionCodeListValueBean.getDimConceptId());
					}
					sdmxEleDimTypeMapBean.setClosedDim(closedDimList);
				}

				// Open Dimension
				List<DimensionCodeListValueBean> openDimensionCodeListValueBeanList = sdmxReturnModelHashBean.getOpenDimension();
				if (!CollectionUtils.isEmpty(openDimensionCodeListValueBeanList)) {
					List<String> openDimList = new ArrayList<>();
					for (DimensionCodeListValueBean dimensionCodeListValueBean : openDimensionCodeListValueBeanList) {
						openDimList.add(dimensionCodeListValueBean.getDimConceptId());
					}
					sdmxEleDimTypeMapBean.setOpenDim(openDimList);
				}
				sdmxModelCodesHelper.saveDMIDGrouping(sdmxEleDimTypeMapBean, gson);
			}

		}

	}

	@Transactional(readOnly = false)
	public void deleteUnmappdedModelCodes(Long modelCodeId) {
		sdmxModelCodesRepo.deleteUnmappdedModelCodes(modelCodeId);
	}

}
