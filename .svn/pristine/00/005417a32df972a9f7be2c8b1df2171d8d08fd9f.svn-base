package com.iris.sdmx.elementdimensionmapping.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iris.caching.ObjectCache;
import com.iris.dto.ServiceResponse;
import com.iris.exception.ApplicationException;
import com.iris.exception.ServiceException;
import com.iris.model.UserMaster;
import com.iris.sdmx.agency.master.repo.SdmxAgencyMasterRepo;
import com.iris.sdmx.dimesnsion.bean.DimensionMasterBean;
import com.iris.sdmx.dimesnsion.entity.DimensionMaster;
import com.iris.sdmx.dimesnsion.repo.DimensionRepo;
import com.iris.sdmx.element.entity.SdmxElementEntity;
import com.iris.sdmx.element.repo.SdmxElementRepo;
import com.iris.sdmx.elementdimensionmapping.bean.AttachmentBean;
import com.iris.sdmx.elementdimensionmapping.bean.DimCombination;
import com.iris.sdmx.elementdimensionmapping.bean.ElementDimensionBean;
import com.iris.sdmx.elementdimensionmapping.bean.ElementDimensionModBean;
import com.iris.sdmx.elementdimensionmapping.bean.ElementDimensionStoredJson;
import com.iris.sdmx.elementdimensionmapping.entity.ElementDimension;
import com.iris.sdmx.elementdimensionmapping.entity.ElementDimensionMod;
import com.iris.sdmx.elementdimensionmapping.repo.ElementDimensionModRepo;
import com.iris.sdmx.elementdimensionmapping.repo.ElementDimensionRepo;
import com.iris.sdmx.fusion.controller.FusionApiController;
import com.iris.sdmx.status.entity.ActionStatus;
import com.iris.sdmx.status.entity.AdminStatus;
import com.iris.sdmx.status.repo.ActionStatusRepo;
import com.iris.sdmx.status.repo.AdminStatusRepo;
import com.iris.sdmx.userMangement.bean.ApprovalInputBean;
import com.iris.service.GenericService;
import com.iris.util.JsonUtility;
import com.iris.util.UtilMaster;
import com.iris.util.Validations;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;

/**
 * @author vjadhav
 *
 */
@Service
@Transactional
public class ElementDimApprovalService implements GenericService<ElementDimension, Long> {

	private static final Logger LOGGER = LogManager.getLogger(ElementDimApprovalService.class);

	@Autowired
	private SdmxElementRepo sdmxElementRepo;

	@Autowired
	private ElementDimensionModRepo elementDimensionModRepo;

	@Autowired
	private AdminStatusRepo adminStatusRepo;

	@Autowired
	private ActionStatusRepo actionStatusRepo;

	@Autowired
	private ElementDimensionRepo elementDimensionRepo;

	@Autowired
	private FusionApiController fusionApiController;

	@Autowired
	private DimensionRepo dimensionRepo;

	@Autowired
	private SdmxAgencyMasterRepo sdmxAgencyMasterRepo;

	@Override
	public ElementDimension add(ElementDimension entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(ElementDimension entity) throws ServiceException {
		return false;
	}

	@Override
	public List<ElementDimension> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public ElementDimension getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<ElementDimension> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ElementDimension> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ElementDimension> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ElementDimension> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<ElementDimension> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(ElementDimension bean) throws ServiceException {

	}

	public List<ElementDimensionModBean> getAllRequestsByAdminStatus(ApprovalInputBean requestInputBean) throws IllegalAccessException, InvocationTargetException {

		List<ElementDimensionModBean> elementDimModBeanList = new ArrayList<>();
		ElementDimensionModBean beanObj;
		List<ElementDimensionMod> elementDimensionModEntityList = new ArrayList<>();
		Map<Long, String> adminStatusMap = getAdminStatus();
		Map<Long, String> actionMap = getAction();

		elementDimensionModEntityList = elementDimensionModRepo.findByAdminStatusId(requestInputBean.getAdminStatusId().intValue());
		if (!elementDimensionModEntityList.isEmpty()) {
			for (ElementDimensionMod entityObj : elementDimensionModEntityList) {
				beanObj = new ElementDimensionModBean();
				BeanUtils.copyProperties(beanObj, entityObj);
				if (entityObj.getElementDimension() != null) {
					beanObj.setElementDimensionIdFk(entityObj.getElementDimension().getElementDimensionId());
				}
				beanObj.setElementIdFk(entityObj.getElement().getElementId());
				beanObj.setAgencyCode(entityObj.getElement().getAgencyMaster().getAgencyMasterCode());
				beanObj.setAgencyLabel(entityObj.getElement().getAgencyMaster().getAgencyMasterLabel());
				beanObj.setCreatedBy(entityObj.getCreatedBy().getUserId());
				beanObj.setCreatedByName(entityObj.getCreatedBy().getUserName());
				beanObj.setCreatedOn(entityObj.getCreatedOn());
				beanObj.setActionStatusLabel(actionMap.get(Long.valueOf(entityObj.getActionId())));
				beanObj.setAdminStatusLabel(adminStatusMap.get(Long.valueOf(entityObj.getAdminStatusId())));

				elementDimModBeanList.add(beanObj);
			}
		}

		return elementDimModBeanList;
	}

	public List<ElementDimensionModBean> getRequests(ApprovalInputBean requestInputBean) throws IllegalAccessException, InvocationTargetException {
		boolean isDeptAdmin;
		boolean isMainDept;
		Long elementId;
		List<ElementDimensionModBean> requestsList = new ArrayList<>();
		List<ElementDimensionModBean> rggAdminRequestList = new ArrayList<>();
		List<ElementDimensionModBean> deptAdminRequestList = new ArrayList<>();
		requestsList = getAllRequestsByAdminStatus(requestInputBean);
		isDeptAdmin = requestInputBean.getIsDeptAdmin();
		isMainDept = requestInputBean.getIsMainDept();

		if (isDeptAdmin) {
			if (isMainDept) { // RGG admin
				for (ElementDimensionModBean reqObj : requestsList) {
					if (reqObj.getActionId() == 1) {
						rggAdminRequestList.add(reqObj);
					} else if (reqObj.getActionId() == 2 || reqObj.getActionId() == 4) {
						elementId = reqObj.getElementIdFk();
						SdmxElementEntity sdmxElementBean = sdmxElementRepo.findByElementId(elementId);
						if (sdmxElementBean != null) {
							if (sdmxElementBean.getRegulatorOwnerIdFk().getRegulatorCode().equalsIgnoreCase(requestInputBean.getDeptCode())) {
								rggAdminRequestList.add(reqObj);
							}
						}
					}
				}

				return rggAdminRequestList;

			} else { // DEPT admin
				for (ElementDimensionModBean reqObj : requestsList) {
					if (reqObj.getActionId() == 2 || reqObj.getActionId() == 4) {
						elementId = reqObj.getElementIdFk();
						SdmxElementEntity sdmxElementBean = sdmxElementRepo.findByElementId(elementId);
						if (sdmxElementBean != null) {
							if (sdmxElementBean.getRegulatorOwnerIdFk().getRegulatorCode().equalsIgnoreCase(requestInputBean.getDeptCode())) {
								deptAdminRequestList.add(reqObj);
							}
						}
					}

				}

				return deptAdminRequestList;
			}
		}

		return requestsList;
	}

	public Map<Long, String> getAdminStatus() {
		List<AdminStatus> adminStatusList = new ArrayList<>();
		adminStatusList = adminStatusRepo.findByActiveStatus(Boolean.TRUE);
		Map<Long, String> adminStatusMap = adminStatusList.stream().collect(Collectors.toMap(AdminStatus::getAdminStatusId, AdminStatus::getStatus));
		return adminStatusMap;
	}

	public Map<Long, String> getAction() {
		List<ActionStatus> actionStatusList = new ArrayList<>();
		actionStatusList = actionStatusRepo.findAll();
		Map<Long, String> actionStatusMap = actionStatusList.stream().collect(Collectors.toMap(ActionStatus::getActionId, ActionStatus::getActionName));
		return actionStatusMap;

	}

	public void approveRejectElementDimensionRecord(ApprovalInputBean requestInputBean) throws ApplicationException {
		Long actionId;
		Long elementDimensionId;

		if (requestInputBean.getAdminStatusId() == 3) {
			actionId = requestInputBean.getActionId();

			int updatedRow = elementDimensionModRepo.approveRejectRequest(requestInputBean.getAdminStatusId().intValue(), requestInputBean.getComments(), requestInputBean.getModTablePkId());

			if (actionId == 2 || actionId == 4) {
				elementDimensionId = requestInputBean.getMasterTablePkId();

				elementDimensionRepo.setIsPending(elementDimensionId, Boolean.FALSE);

			}

		} else if (requestInputBean.getAdminStatusId() == 4) {
			actionId = requestInputBean.getActionId();
			elementDimensionId = requestInputBean.getMasterTablePkId();
			int updatedRow = elementDimensionModRepo.approveRejectRequest(requestInputBean.getAdminStatusId().intValue(), requestInputBean.getComments(), requestInputBean.getModTablePkId());

			if (actionId == 1) {
				addElementDimension(requestInputBean);

			} else if (actionId == 2) {

				elementDimensionRepo.setIsPending(elementDimensionId, Boolean.FALSE);
				editElementDimension(requestInputBean);

			} else if (actionId == 4) {

				elementDimensionRepo.setIsPending(elementDimensionId, Boolean.FALSE);
				deleteElementDimension(requestInputBean);

			}

		}

	}

	public void addElementDimension(ApprovalInputBean requestInputBean) throws ApplicationException {
		ElementDimensionMod elementDimensionMod = elementDimensionModRepo.findByElementDimensionModId(requestInputBean.getModTablePkId());
		String elementDimensionJson = elementDimensionMod.getElementDimensionJson();

		ElementDimensionStoredJson elementDimensionAddJson = JsonUtility.getGsonObject().fromJson(elementDimensionJson, ElementDimensionStoredJson.class);

		SdmxElementEntity sdmxElement = sdmxElementRepo.findEntityByActiveStatus(requestInputBean.getElementId(), true);

		if (UtilMaster.isEmpty(sdmxElement)) {
			// SDMX element not found
			throw new ApplicationException(ErrorCode.E1415.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1415.toString()));
		}

		ElementDimension dbElementDimension = elementDimensionRepo.findByElementDsdCodeAndElementElementVer(sdmxElement.getDsdCode(), sdmxElement.getElementVer(), sdmxElement.getAgencyMaster().getAgencyMasterCode());

		if (dbElementDimension != null && dbElementDimension.getIsActive().equals(Boolean.TRUE)) {
			throw new ApplicationException(ErrorCode.E1414.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1414.toString()));
		}

		ElementDimensionBean elementDimensionBean = new ElementDimensionBean();
		elementDimensionBean.setElementId(sdmxElement.getElementId());
		elementDimensionBean.setElementLabel(sdmxElement.getElementLabel());
		elementDimensionBean.setElementVersion(sdmxElement.getElementVer());
		elementDimensionBean.setElementDesc(sdmxElement.getElementDesc());
		elementDimensionBean.setDsdCode(sdmxElement.getDsdCode());
		elementDimensionBean.setAgencyMasterCode(sdmxElement.getAgencyMaster().getAgencyMasterCode());
		List<DimensionMasterBean> dimensionmasterBeans = new ArrayList<>();
		List<DimCombination> dimCombinationList = elementDimensionAddJson.getDimCombination();
		for (DimCombination obj : dimCombinationList) {
			DimensionMasterBean dimensionMasterBean = new DimensionMasterBean();
			dimensionMasterBean.setDimensionCode(obj.getDimConceptId());
			AttachmentBean attachmentBean = new AttachmentBean();
			if (obj.getAttachmentType() != null) {
				attachmentBean.setAttachmentCode(obj.getAttachmentType());
			} else {
				attachmentBean.setAttachmentCode("");
			}
			dimensionMasterBean.setAttachmentBean(attachmentBean);
			dimensionMasterBean.setConceptVersion(obj.getConceptVersion());
			dimensionmasterBeans.add(dimensionMasterBean);

		}

		elementDimensionBean.setDimensionmasterBeans(dimensionmasterBeans);

		UserMaster approvedBy = new UserMaster();
		approvedBy.setUserId(requestInputBean.getUserId());

		Date approvedOn = new Date();

		if (!UtilMaster.isEmpty(dbElementDimension) && dbElementDimension != null) {
			dbElementDimension.setElement(sdmxElement);
			dbElementDimension.setElementDimensionJson(elementDimensionJson);
			dbElementDimension.setCreatedBy(elementDimensionMod.getCreatedBy());
			dbElementDimension.setCreatedOn(elementDimensionMod.getCreatedOn());
			dbElementDimension.setLastUpdatedOn(approvedOn);
			dbElementDimension.setLastApprovedBy(approvedBy);
			dbElementDimension.setLastApprovedOn(approvedOn);
			dbElementDimension.setIsActive(true);
			dbElementDimension.setIsPending(Boolean.FALSE);
			elementDimensionRepo.save(dbElementDimension);

			ElementDimensionMod elementDimMod = elementDimensionModRepo.findByElementDimensionModId(requestInputBean.getModTablePkId());
			ElementDimension elementDimensionMaster = new ElementDimension();
			elementDimensionMaster.setElementDimensionId(dbElementDimension.getElementDimensionId());
			elementDimMod.setElementDimension(elementDimensionMaster);
			elementDimensionModRepo.save(elementDimMod);
		} else {
			ElementDimension elementDimension = new ElementDimension();
			elementDimension.setElement(sdmxElement);
			elementDimension.setElementDimensionJson(elementDimensionJson);
			elementDimension.setCreatedBy(elementDimensionMod.getCreatedBy());
			elementDimension.setCreatedOn(elementDimensionMod.getCreatedOn());
			elementDimension.setLastUpdatedOn(approvedOn);
			elementDimension.setLastApprovedBy(approvedBy);
			elementDimension.setLastApprovedOn(approvedOn);
			elementDimension.setIsActive(true);
			elementDimension.setIsPending(Boolean.FALSE);
			elementDimensionRepo.save(elementDimension);

			elementDimensionModRepo.setElementDimensionId(requestInputBean.getModTablePkId(), elementDimension.getElementDimensionId());

		}

		ServiceResponse serviceResponse = fusionApiController.submitElementDimensionData(elementDimensionBean);

		if (!serviceResponse.isStatus()) {
			throw new ApplicationException(ErrorCode.E1413.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1413.toString()));
		}

	}

	public void editElementDimension(ApprovalInputBean requestInputBean) throws ApplicationException {
		ElementDimensionMod elementDimensionMod = elementDimensionModRepo.findByElementDimensionModId(requestInputBean.getModTablePkId());
		String elementDimensionJson = elementDimensionMod.getElementDimensionJson();
		ElementDimensionStoredJson elementDimensionAddJson = JsonUtility.getGsonObject().fromJson(elementDimensionJson, ElementDimensionStoredJson.class);

		SdmxElementEntity sdmxElement = sdmxElementRepo.findEntityByActiveStatus(requestInputBean.getElementId(), true);
		if (UtilMaster.isEmpty(sdmxElement)) {
			// SDMX element not found
			throw new ApplicationException(ErrorCode.E1415.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1415.toString()));
		}

		ElementDimension dbElementDimension = elementDimensionRepo.findByElementDsdCodeAndElementElementVer(sdmxElement.getDsdCode(), sdmxElement.getElementVer(), sdmxElement.getAgencyMaster().getAgencyMasterCode());
		if (UtilMaster.isEmpty(dbElementDimension)) {
			throw new ApplicationException(ErrorCode.E0152.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0152.toString()));
		}

		ElementDimensionBean elementDimensionBean = new ElementDimensionBean();
		elementDimensionBean.setElementId(sdmxElement.getElementId());
		elementDimensionBean.setElementLabel(sdmxElement.getElementLabel());
		elementDimensionBean.setElementVersion(sdmxElement.getElementVer());
		elementDimensionBean.setElementDesc(sdmxElement.getElementDesc());
		elementDimensionBean.setDsdCode(sdmxElement.getDsdCode());
		elementDimensionBean.setAgencyMasterCode(sdmxElement.getAgencyMaster().getAgencyMasterCode());
		List<DimensionMasterBean> dimensionmasterBeans = new ArrayList<>();
		List<DimCombination> dimCombinationList = elementDimensionAddJson.getDimCombination();
		for (DimCombination obj : dimCombinationList) {
			DimensionMasterBean dimensionMasterBean = new DimensionMasterBean();
			dimensionMasterBean.setDimensionCode(obj.getDimConceptId());
			AttachmentBean attachmentBean = new AttachmentBean();
			if (obj.getAttachmentType() != null) {
				attachmentBean.setAttachmentCode(obj.getAttachmentType());
			} else {
				attachmentBean.setAttachmentCode("");
			}
			dimensionMasterBean.setAttachmentBean(attachmentBean);
			dimensionMasterBean.setConceptVersion(obj.getConceptVersion());
			dimensionmasterBeans.add(dimensionMasterBean);

		}

		elementDimensionBean.setDimensionmasterBeans(dimensionmasterBeans);

		UserMaster approvedBy = new UserMaster();
		approvedBy.setUserId(requestInputBean.getUserId());

		Date approvedOn = new Date();

		dbElementDimension.setElementDimensionJson(elementDimensionJson);
		dbElementDimension.setLastModifiedBy(elementDimensionMod.getCreatedBy());
		dbElementDimension.setLastModifiedOn(elementDimensionMod.getCreatedOn());
		dbElementDimension.setLastUpdatedOn(approvedOn);
		dbElementDimension.setLastApprovedBy(approvedBy);
		dbElementDimension.setLastApprovedOn(approvedOn);
		dbElementDimension.setIsPending(Boolean.FALSE);
		elementDimensionRepo.save(dbElementDimension);

		ServiceResponse serviceResponse = fusionApiController.submitElementDimensionData(elementDimensionBean);

		if (!serviceResponse.isStatus()) {
			throw new ApplicationException(ErrorCode.E1413.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1413.toString()));
		}
	}

	public void deleteElementDimension(ApprovalInputBean requestInputBean) throws ApplicationException {
		ElementDimensionMod elementDimensionMod = elementDimensionModRepo.findByElementDimensionModId(requestInputBean.getModTablePkId());
		String elementDimensionJson = elementDimensionMod.getElementDimensionJson();
		ElementDimensionStoredJson elementDimensionAddJson = JsonUtility.getGsonObject().fromJson(elementDimensionJson, ElementDimensionStoredJson.class);

		SdmxElementEntity sdmxElement = sdmxElementRepo.findEntityByActiveStatus(requestInputBean.getElementId(), true);
		if (UtilMaster.isEmpty(sdmxElement)) {
			// SDMX element not found
			throw new ApplicationException(ErrorCode.E1415.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1415.toString()));
		}

		ElementDimension dbElementDimension = elementDimensionRepo.findByElementDsdCodeAndElementElementVer(sdmxElement.getDsdCode(), sdmxElement.getElementVer(), sdmxElement.getAgencyMaster().getAgencyMasterCode());
		if (UtilMaster.isEmpty(dbElementDimension)) {
			throw new ApplicationException(ErrorCode.E0152.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0152.toString()));
		}

		UserMaster approvedBy = new UserMaster();
		approvedBy.setUserId(requestInputBean.getUserId());

		Date approvedOn = new Date();

		dbElementDimension.setIsActive(false);
		dbElementDimension.setLastModifiedBy(elementDimensionMod.getCreatedBy());
		dbElementDimension.setLastModifiedOn(elementDimensionMod.getCreatedOn());
		dbElementDimension.setLastUpdatedOn(approvedOn);
		dbElementDimension.setLastApprovedBy(approvedBy);
		dbElementDimension.setLastApprovedOn(approvedOn);
		dbElementDimension.setIsPending(Boolean.FALSE);
		elementDimensionRepo.save(dbElementDimension);

		ServiceResponse serviceResponse = fusionApiController.deleteElementDimensionData(sdmxElement.getDsdCode(), sdmxElement.getElementVer(), elementDimensionMod.getCreatedBy().getUserId(), sdmxElement.getAgencyMaster().getAgencyMasterCode());

		if (!serviceResponse.isStatus()) {
			throw new ApplicationException(ErrorCode.E1413.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1413.toString()));
		}

	}

	public boolean isElementDimensionPending(String dsdCode, String elementVersion, String agencyCode) throws ServiceException {
		try {
			ElementDimension elementDimension = elementDimensionRepo.findByElementDsdCodeAndElementElementVer(dsdCode, elementVersion, agencyCode);

			if (elementDimension != null && elementDimension.getIsActive().equals(Boolean.TRUE)) {
				if (elementDimension.getIsPending().equals(Boolean.TRUE)) {
					return true;
				} else {
					return false;
				}
			}

			return false;

		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getErrorMessage(), e);
		}
	}

	public boolean isDimensionListPending(List<DimensionMasterBean> dimensionRequestBeanList, String agencyMasterCode, String version) throws ServiceException {
		List<DimensionMaster> sdmxDimensionMastersList = new ArrayList<>();

		try {
			Map<String, List<String>> agencyVersionDmCodeMap = new HashMap<>();
			for (DimensionMasterBean obj : dimensionRequestBeanList) {
				obj.setAgencyMasterCode(agencyMasterCode);
				if (obj.getDimensionCode().equals("OBS_VALUE") || obj.getDimensionCode().equals("TIME_PERIOD")) {
					obj.setConceptVersion("1.0");
				}
				String key = obj.getAgencyMasterCode() + "~~" + obj.getConceptVersion();
				if (agencyVersionDmCodeMap.containsKey(key)) {
					List<String> dimCodeList = agencyVersionDmCodeMap.get(key);
					dimCodeList.add(obj.getDimensionCode());
				} else {
					List<String> dimCodeList = new ArrayList<>();
					dimCodeList.add(obj.getDimensionCode());
					agencyVersionDmCodeMap.put(key, dimCodeList);
				}
			}

			Iterator<Entry<String, List<String>>> it = agencyVersionDmCodeMap.entrySet().iterator();

			while (it.hasNext()) {
				Map.Entry<String, List<String>> set = (Map.Entry<String, List<String>>) it.next();
				String conceptVersion = set.getKey().split("~~")[1];
				String agencyCode = set.getKey().split("~~")[0];
				Long agencyId = sdmxAgencyMasterRepo.findByAgencyCode(agencyCode).getAgencyMasterId();
				sdmxDimensionMastersList = dimensionRepo.findPendingDimensions(set.getValue(), agencyId, conceptVersion, true);
				if (!Validations.isEmpty(sdmxDimensionMastersList)) {
					return true;
				}
			}
			return false;

		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getErrorMessage(), e);
		}
	}
}
