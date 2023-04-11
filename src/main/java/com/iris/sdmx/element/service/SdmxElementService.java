/**
 * 
 */
package com.iris.sdmx.element.service;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.reflect.TypeToken;
import com.iris.caching.ObjectCache;
import com.iris.exception.ApplicationException;
import com.iris.exception.ServiceException;
import com.iris.sdmx.agency.master.entity.AgencyMaster;
import com.iris.sdmx.agency.master.repo.SdmxAgencyMasterRepo;
import com.iris.sdmx.dimesnsion.bean.DimensionBean;
import com.iris.sdmx.dimesnsion.entity.DimensionMaster;
import com.iris.sdmx.dimesnsion.repo.DimensionRepo;
import com.iris.sdmx.element.bean.SdmxElementBean;
import com.iris.sdmx.element.bean.SdmxElementExistBean;
import com.iris.sdmx.element.bean.SdmxElementMappingDetailBean;
import com.iris.sdmx.element.bean.SdmxElementRequestMappingBean;
import com.iris.sdmx.element.entity.SdmxElementEntity;
import com.iris.sdmx.element.helper.SdmxElementHelper;
import com.iris.sdmx.element.repo.SdmxElementRepo;
import com.iris.sdmx.elementdimensionmapping.bean.DimCombination;
import com.iris.sdmx.elementdimensionmapping.entity.ElementDimension;
import com.iris.sdmx.elementdimensionmapping.repo.ElementDimensionRepo;
import com.iris.sdmx.exceltohtml.bean.ModelMappingBean;
import com.iris.sdmx.exceltohtml.bean.ModelOutputBean;
import com.iris.sdmx.exceltohtml.repo.SdmxModelCodesRepo;
import com.iris.service.GenericService;
import com.iris.util.JsonUtility;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;

/**
 * @author apagaria
 *
 */
@Service
@Transactional(readOnly = true)
public class SdmxElementService implements GenericService<SdmxElementEntity, Long> {

	@Autowired
	private SdmxElementRepo sdmxElementRepo;

	@Autowired
	private ElementDimensionRepo elementDimensionRepo;

	@Autowired
	private SdmxModelCodesRepo sdmxModelCodesRepo;

	@Autowired
	private DimensionRepo dimensionRepo;

	@Autowired
	private SdmxAgencyMasterRepo sdmxAgencyMasterRepo;

	private static final Logger LOGGER = LogManager.getLogger(SdmxElementService.class);

	@Override
	public SdmxElementEntity add(SdmxElementEntity entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(SdmxElementEntity entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SdmxElementEntity> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SdmxElementEntity getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxElementEntity> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxElementEntity> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxElementEntity> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxElementEntity> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxElementEntity> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(SdmxElementEntity bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	/**
	 * @param sdmxElementBean
	 */
	@Transactional(readOnly = false)
	public void addEntityByBean(SdmxElementBean sdmxElementBean) {
		SdmxElementEntity sdmxElementEntity = new SdmxElementEntity();
		SdmxElementHelper.convertBeanToEntity(sdmxElementBean, sdmxElementEntity, false);

		if (sdmxElementBean.getAgencyMasterCode() != null) {
			AgencyMaster agencyMaster = sdmxAgencyMasterRepo.findByAgencyCode(sdmxElementBean.getAgencyMasterCode());
			if (agencyMaster != null && agencyMaster.getAgencyMasterId() != 0) {
				AgencyMaster agencyMaster2 = new AgencyMaster();
				agencyMaster2.setAgencyMasterId(agencyMaster.getAgencyMasterId());
				sdmxElementEntity.setAgencyMaster(agencyMaster2);
			}
		} else {
			sdmxElementEntity.setAgencyMaster(null);
		}

		sdmxElementRepo.save(sdmxElementEntity);
	}

	/**
	 * @param sdmxElementBean
	 */
	@Transactional(readOnly = false)
	public void editEntityByBean(SdmxElementBean sdmxElementBean, Long elementId) {
		SdmxElementEntity sdmxElementEntity = sdmxElementRepo.findEntityByActiveStatus(elementId, Boolean.TRUE);

		SdmxElementHelper.convertBeanToEntity(sdmxElementBean, sdmxElementEntity, true);
		if (sdmxElementBean.getAgencyMasterCode() != null) {
			AgencyMaster agencyMaster = sdmxAgencyMasterRepo.findByAgencyCode(sdmxElementBean.getAgencyMasterCode());
			if (agencyMaster != null && agencyMaster.getAgencyMasterId() != 0) {
				AgencyMaster agencyMaster2 = new AgencyMaster();
				agencyMaster2.setAgencyMasterId(agencyMaster.getAgencyMasterId());
				agencyMaster2.setAgencyMasterCode(agencyMaster.getAgencyMasterCode());
				agencyMaster2.setAgencyMasterLabel(agencyMaster.getAgencyMasterLabel());
				sdmxElementEntity.setAgencyMaster(agencyMaster2);
			}
		} else {
			sdmxElementEntity.setAgencyMaster(null);
		}
		sdmxElementRepo.save(sdmxElementEntity);
	}

	public void deleteEntityById(Long elementId) {
		SdmxElementEntity sdmxElementEntity = sdmxElementRepo.findEntityByActiveStatus(elementId, Boolean.TRUE);
		sdmxElementEntity.setIsActive(Boolean.FALSE);
		sdmxElementRepo.save(sdmxElementEntity);
	}

	/**
	 * @param elementId
	 * @param isActive
	 * @return
	 */
	public Boolean findByElementIdAndActiveStatus(Long elementId, Boolean isActive) {
		Boolean isElementExist = false;
		if (sdmxElementRepo.findByElementIdAndActiveStatus(elementId, isActive) > 0) {
			isElementExist = true;
		}
		return isElementExist;
	}

	/**
	 * @param dsdCode
	 * @param elementVer
	 * @param isActive
	 * @return
	 */
	public Long findByDsdCodeAndVerAndIsActive(String dsdCode, String elementVer, Boolean isActive, String agencyMasterCode) {

		return sdmxElementRepo.findByDsdCodeAndVerAndIsActive(dsdCode, elementVer, isActive, agencyMasterCode);

	}

	/**
	 * @param dsdCode
	 * @param isActive
	 * @return
	 */
	public List<String> findVersionByDsdCode(String dsdCode, Boolean isActive, String agencyMasterCode) {
		List<String> elementVer = null;
		elementVer = sdmxElementRepo.findVersionsByDsdCode(dsdCode, isActive, agencyMasterCode);
		return elementVer;
	}

	/**
	 * @param isActive
	 * @return
	 */
	public List<SdmxElementBean> findEntitiesByActiveStatus(Boolean isActive, String agencyCode) {
		List<SdmxElementBean> sdmxElementBeans = null;
		List<SdmxElementEntity> sdmxElementEntities = null;

		if (agencyCode != null && agencyCode != "") {
			AgencyMaster agencyMaster = sdmxAgencyMasterRepo.findByAgencyCode(agencyCode);
			if (agencyMaster != null && agencyMaster.getAgencyMasterId() != 0) {
				sdmxElementEntities = new ArrayList<>();
				Instant entityQueryStart = Instant.now();
				sdmxElementEntities = sdmxElementRepo.findEntitiesByActiveStatusWithAgency(isActive, agencyCode);
				Instant entityQueryEnd = Instant.now();
				Duration entityQueryDuration = Duration.between(entityQueryStart, entityQueryEnd);
				LOGGER.debug("Entity Query time in seconds - " + entityQueryDuration.getSeconds());
			}
		} else {
			sdmxElementEntities = new ArrayList<>();
			Instant entityQueryStart = Instant.now();
			sdmxElementEntities = sdmxElementRepo.findEntitiesByActiveStatus(isActive);
			Instant entityQueryEnd = Instant.now();
			Duration entityQueryDuration = Duration.between(entityQueryStart, entityQueryEnd);
			LOGGER.debug("Entity Query 2 time in seconds - " + entityQueryDuration.getSeconds());
		}

		if (!CollectionUtils.isEmpty(sdmxElementEntities)) {
			sdmxElementBeans = new ArrayList<>();
			Instant convertorStart = Instant.now();
			SdmxElementHelper.convertEntityListToBeanList(sdmxElementEntities, sdmxElementBeans);
			Instant convertorEnd = Instant.now();
			Duration convertorDuration = Duration.between(convertorStart, convertorEnd);
			LOGGER.debug("Convertor time in seconds - " + convertorDuration.getSeconds());
		}
		return sdmxElementBeans;
	}

	/**
	 * @param isActive
	 * @return
	 */
	public List<SdmxElementBean> findEntitiesByActiveStatus(Boolean isActive, Boolean isPending) {
		return sdmxElementRepo.findEntitiesByActiveStatus(isActive, isPending);
	}

	/**
	 * @param isActive
	 * @param isPending
	 * @param agencyCode
	 * @return
	 */
	public List<SdmxElementBean> findEntitiesByActiveStatusNAgencyCode(Boolean isActive, Boolean isPending, String agencyCode) {
		return sdmxElementRepo.findEntitiesByActiveStatusNAgencyCode(isActive, isPending, agencyCode);
	}

	/**
	 * @param isActive
	 * @return
	 */
	public SdmxElementBean findEntityByActiveStatus(Long elementId, Boolean isActive) {
		SdmxElementBean sdmxElementBean = null;
		SdmxElementEntity sdmxElementEntity = sdmxElementRepo.findEntityByActiveStatus(elementId, isActive);
		if (sdmxElementEntity != null) {
			sdmxElementBean = new SdmxElementBean();
			SdmxElementHelper.convertEntityToBean(sdmxElementEntity, sdmxElementBean);
		}
		return sdmxElementBean;
	}

	/**
	 * @param elementId
	 * @param isActive
	 * @return
	 */
	public SdmxElementBean findEntityByActiveStatus(Long elementId, Boolean isActive, Boolean isPending) {
		SdmxElementBean sdmxElementBean = null;
		SdmxElementEntity sdmxElementEntity = sdmxElementRepo.findEntityByActiveStatus(elementId, isActive, isPending);
		if (sdmxElementEntity != null) {
			sdmxElementBean = new SdmxElementBean();
			SdmxElementHelper.convertEntityToBean(sdmxElementEntity, sdmxElementBean);
		}
		return sdmxElementBean;
	}

	public void delinkParentFromChild(Long elementId) {
		SdmxElementEntity sdmxElement = sdmxElementRepo.findEntityByActiveStatus(elementId, Boolean.TRUE);
		List<SdmxElementEntity> sdmxElementEntity = sdmxElementRepo.findByIsActiveAndParentElementIdFkElementId(Boolean.TRUE, elementId);
		if (sdmxElementEntity != null && sdmxElementEntity.size() > 0) {
			for (SdmxElementEntity elementEntity : sdmxElementEntity) {
				elementEntity.setParentElementIdFk(sdmxElement.getParentElementIdFk());
				sdmxElementRepo.save(elementEntity);
			}

		}

	}

	public SdmxElementExistBean chkElementExist(String dsdCode, String elementVer, boolean isActive, String agencyMasterCode) {

		SdmxElementEntity sdmxElementEntity = sdmxElementRepo.findByDsdCodeVersionIsActive(dsdCode, elementVer, isActive, agencyMasterCode);

		SdmxElementExistBean sdmxElementExistBean = new SdmxElementExistBean();

		if (sdmxElementEntity != null) {
			sdmxElementExistBean.setElementId(sdmxElementEntity.getElementId());
			sdmxElementExistBean.setIsActive(sdmxElementEntity.getIsActive());
			sdmxElementExistBean.setIsPending(sdmxElementEntity.getIsPending());

			return sdmxElementExistBean;
		} else {
			sdmxElementExistBean.setIsActive(Boolean.FALSE);
		}

		return sdmxElementExistBean;

	}

	public boolean isElementExist(String dsdCode, String elementVer, boolean isActive, String agencyMasterCode) {
		try {
			SdmxElementEntity sdmxElementEntity = sdmxElementRepo.findByDsdCodeVersionIsActive(dsdCode, elementVer, isActive, agencyMasterCode);
			if (sdmxElementEntity != null) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getErrorMessage(), e);
		}

	}

	public boolean isElementPending(String dsdCode, String elementVer, boolean isActive, String agencyMasterCode) {
		try {
			SdmxElementEntity sdmxElementEntity = sdmxElementRepo.findByDsdCodeVersionIsActive(dsdCode, elementVer, isActive, agencyMasterCode);
			if (sdmxElementEntity != null) {
				if (sdmxElementEntity.getIsPending().equals(Boolean.TRUE)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}

		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getErrorMessage(), e);
		}

	}

	@SuppressWarnings("serial")
	public List<SdmxElementMappingDetailBean> getMappingDetail(List<SdmxElementRequestMappingBean> requestBeanList) throws ApplicationException {
		List<DimCombination> dimCombinations = null;
		List<SdmxElementMappingDetailBean> mappingDetailOutput = new ArrayList<>();
		List<DimensionBean> dimensionBean;

		if (!CollectionUtils.isEmpty(requestBeanList)) {

			for (SdmxElementRequestMappingBean requestBeanObj : requestBeanList) {

				SdmxElementMappingDetailBean mappingDetailOutputObject = new SdmxElementMappingDetailBean();
				dimensionBean = new ArrayList<>();

				SdmxElementEntity elObject = sdmxElementRepo.findEntityByActiveStatus(requestBeanObj.getElementId(), true);
				if (elObject == null) {
					throw new ApplicationException(ErrorCode.E1415.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1415.toString()));
				}
				mappingDetailOutputObject.setElementId(requestBeanObj.getElementId());
				mappingDetailOutputObject.setElementCode(requestBeanObj.getElementCode());
				mappingDetailOutputObject.setElementVersion(requestBeanObj.getElementVersion());
				mappingDetailOutputObject.setElementLabel(elObject.getElementLabel());
				mappingDetailOutputObject.setElementDesc(elObject.getElementDesc());
				mappingDetailOutputObject.setAgencyLabel(elObject.getAgencyMaster().getAgencyMasterLabel());
				Long elementId = requestBeanObj.getElementId();
				ElementDimension elementDimension = elementDimensionRepo.findByIsActiveAndElementElementId(true, elementId);
				if (elementDimension != null) {
					String dimensionString = JsonUtility.extractResponseValueFromServiceResponseString(elementDimension.getElementDimensionJson(), "dimCombination");

					if (dimensionString != null) {
						Type listToken = new TypeToken<List<DimCombination>>() {
						}.getType();
						dimCombinations = JsonUtility.getGsonObject().fromJson(dimensionString, listToken);

						for (DimCombination dimCombination : dimCombinations) {
							DimensionBean dimObject = new DimensionBean();
							dimObject.setDimConceptId(dimCombination.getDimConceptId());
							dimObject.setAttachmentType(dimCombination.getAttachmentType());
							dimObject.setConceptVersion(dimCombination.getConceptVersion());
							DimensionMaster dimObj = dimensionRepo.getDimensionByCodeAndAgency(dimCombination.getDimConceptId(), elementDimension.getElement().getAgencyMaster().getAgencyMasterCode(), dimCombination.getConceptVersion());
							if (dimObj != null) {
								dimObject.setDimensionDesc(dimObj.getDimDesc());
								dimObject.setDimensionLabel(dimObj.getDimensionName());
							}
							dimensionBean.add(dimObject);
						}

					}

				}
				mappingDetailOutputObject.setDimensionBean(dimensionBean);
				List<ModelOutputBean> modelOutputBeanList = new ArrayList<>();
				modelOutputBeanList = sdmxModelCodesRepo.fetchElementModelMappingData(elementId);

				Collections.sort(modelOutputBeanList, Comparator.comparing(ModelOutputBean::getReturnName).thenComparing(ModelOutputBean::getReturnTemplateVersion).thenComparing(ModelOutputBean::getReturnPreviewId).thenComparing(ModelOutputBean::getSheetName).thenComparing(ModelOutputBean::getSectionName));

				Map<Long, List<ModelOutputBean>> modelMap = new HashMap<>();

				for (ModelOutputBean obj : modelOutputBeanList) {
					Long key = obj.getReturnPreviewId();
					if (modelMap.containsKey(key)) {
						List<ModelOutputBean> modelList = modelMap.get(key);
						modelList.add(obj);
					} else {
						List<ModelOutputBean> modelList = new ArrayList<>();
						modelList.add(obj);
						modelMap.put(key, modelList);
					}
				}

				Iterator<Entry<Long, List<ModelOutputBean>>> itr = modelMap.entrySet().iterator();
				List<ModelMappingBean> modelMappingList = new ArrayList<>();
				while (itr.hasNext()) {
					Map.Entry<Long, List<ModelOutputBean>> entry = itr.next();

					ModelMappingBean modelMappingBean = null;

					List<ModelOutputBean> modelDbList = entry.getValue();
					Map<String, ModelMappingBean> sheetSectionCellRefMap = new HashMap<>();
					for (ModelOutputBean modelDbObj : modelDbList) {
						String mapKey = modelDbObj.getSheetName() + "~" + modelDbObj.getSectionName();
						if (sheetSectionCellRefMap.containsKey(mapKey)) {
							modelMappingBean = sheetSectionCellRefMap.get(mapKey);
							List<Integer> cellRefList = modelMappingBean.getCellNo();
							Collections.sort(cellRefList);
							cellRefList.add(modelDbObj.getReturnCellRef());
						} else {
							modelMappingBean = new ModelMappingBean();
							modelMappingBean.setReturnCode(modelDbObj.getReturnCode());
							modelMappingBean.setReturnName(modelDbObj.getReturnName());
							modelMappingBean.setReturnTemplateVersion(modelDbObj.getReturnTemplateVersion());
							if (modelDbObj.getEbrVersion() == null) {
								modelMappingBean.setEbrVersion("");
							} else {
								modelMappingBean.setEbrVersion(modelDbObj.getEbrVersion());

							}
							modelMappingBean.setSheetName(modelDbObj.getSheetName());
							modelMappingBean.setSectionName(modelDbObj.getSectionName());
							List<Integer> cellRefList = new ArrayList<>();
							cellRefList.add(modelDbObj.getReturnCellRef());
							modelMappingBean.setCellNo(cellRefList);
							sheetSectionCellRefMap.put(mapKey, modelMappingBean);
						}
					}
					modelMappingList.add(modelMappingBean);
				}
				Collections.sort(modelMappingList, Comparator.comparing(ModelMappingBean::getReturnName).thenComparing(ModelMappingBean::getReturnTemplateVersion).thenComparing(ModelMappingBean::getEbrVersion).thenComparing(ModelMappingBean::getSheetName).thenComparing(ModelMappingBean::getSectionName));

				mappingDetailOutputObject.setModelMappingBean(modelMappingList);
				mappingDetailOutput.add(mappingDetailOutputObject);
			}
		}
		return mappingDetailOutput;
	}
}
