package com.iris.sdmx.dimesnsion.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
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

import com.google.gson.reflect.TypeToken;
import com.iris.caching.ObjectCache;
import com.iris.dto.ServiceResponse;
import com.iris.exception.ApplicationException;
import com.iris.exception.ServiceException;
import com.iris.model.UserMaster;
import com.iris.sdmx.agency.master.entity.AgencyMaster;
import com.iris.sdmx.agency.master.repo.SdmxAgencyMasterRepo;
import com.iris.sdmx.codelist.entity.CodeListMaster;
import com.iris.sdmx.codelist.repo.CodeListMasterRepo;
import com.iris.sdmx.dimesnsion.bean.DimensionMasterBean;
import com.iris.sdmx.dimesnsion.bean.DimensionModBean;
import com.iris.sdmx.dimesnsion.bean.DimensionRequestBean;
import com.iris.sdmx.dimesnsion.entity.DimensionMaster;
import com.iris.sdmx.dimesnsion.entity.DimensionMasterMod;
import com.iris.sdmx.dimesnsion.entity.DimensionType;
import com.iris.sdmx.dimesnsion.entity.Regex;
import com.iris.sdmx.dimesnsion.repo.DimensionModRepo;
import com.iris.sdmx.dimesnsion.repo.DimensionRepo;
import com.iris.sdmx.fusion.controller.FusionApiController;
import com.iris.sdmx.status.entity.ActionStatus;
import com.iris.sdmx.status.entity.AdminStatus;
import com.iris.sdmx.status.repo.ActionStatusRepo;
import com.iris.sdmx.status.repo.AdminStatusRepo;
import com.iris.sdmx.userMangement.bean.ApprovalInputBean;
import com.iris.service.GenericService;
import com.iris.util.JsonUtility;
import com.iris.util.Validations;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;

/**
 * @author vjadhav
 *
 */
@Service
@Transactional
public class DimensionApprovalService implements GenericService<DimensionMaster, Long> {

	private static final Logger LOGGER = LogManager.getLogger(DimensionApprovalService.class);

	@Autowired
	private AdminStatusRepo adminStatusRepo;

	@Autowired
	private ActionStatusRepo actionStatusRepo;

	@Autowired
	private DimensionModRepo dimensionModRepo;

	@Autowired
	private DimensionRepo dimensionRepo;

	@Autowired
	private CodeListMasterRepo codeListMasterRepo;

	@Autowired
	private FusionApiController fusionApiController;

	@Autowired
	private SdmxAgencyMasterRepo sdmxAgencyMasterRepo;

	@Override
	public DimensionMaster add(DimensionMaster entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(DimensionMaster entity) throws ServiceException {
		return false;
	}

	@Override
	public List<DimensionMaster> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public DimensionMaster getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<DimensionMaster> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<DimensionMaster> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<DimensionMaster> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<DimensionMaster> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<DimensionMaster> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(DimensionMaster bean) throws ServiceException {

	}

	public List<DimensionModBean> getAllRequestsByAdminStatus(Long adminStatusId) throws IllegalAccessException, InvocationTargetException {
		List<DimensionModBean> dimensionModBeanList = new ArrayList<>();
		DimensionModBean beanObj;
		List<DimensionMasterMod> dimensionMasterModList = new ArrayList<>();
		dimensionMasterModList = dimensionModRepo.findByAdminStatusId(adminStatusId.intValue());

		Map<Long, String> adminStatusMap = getAdminStatus();
		Map<Long, String> actionMap = getAction();
		if (!dimensionMasterModList.isEmpty()) {
			for (DimensionMasterMod entityobj : dimensionMasterModList) {
				beanObj = new DimensionModBean();
				BeanUtils.copyProperties(beanObj, entityobj);
				if (entityobj.getDimensionmaster() != null) {
					beanObj.setDimIdFk(entityobj.getDimensionmaster().getDimesnsionMasterId());
					beanObj.setAgencyCode(entityobj.getDimensionmaster().getAgencyMaster().getAgencyMasterCode());
					beanObj.setAgencyLabel(entityobj.getDimensionmaster().getAgencyMaster().getAgencyMasterLabel());
				}
				beanObj.setCreatedBy(entityobj.getCreatedBy().getUserId());
				beanObj.setCreatedByName(entityobj.getCreatedBy().getUserName());
				beanObj.setCreatedOn(entityobj.getCreatedOn());
				beanObj.setActionLabel(actionMap.get(Long.valueOf(entityobj.getActionId())));
				beanObj.setAdminStatusLabel(adminStatusMap.get(Long.valueOf(entityobj.getAdminStatusId())));
				dimensionModBeanList.add(beanObj);
			}
		}

		return dimensionModBeanList;
	}

	public List<DimensionModBean> getRequests(ApprovalInputBean requestInputBean) throws IllegalAccessException, InvocationTargetException {

		List<DimensionModBean> requestsList = new ArrayList<>();
		requestsList = getAllRequestsByAdminStatus(requestInputBean.getAdminStatusId());
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

	@org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
	public void approveRejectDimensionRecord(ApprovalInputBean requestInputBean) throws ApplicationException {
		Long actionId;
		int updatedRow;
		actionId = requestInputBean.getActionId();

		if (actionId == 1) {
			throw new ApplicationException(ErrorCode.E1573.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1573.toString()));

		}

		if (requestInputBean.getAdminStatusId() == 3) {
			updatedRow = dimensionModRepo.approveRejectRequest(requestInputBean.getAdminStatusId().intValue(), requestInputBean.getComments(), requestInputBean.getModTablePkId());

			dimensionRepo.setIsPending(requestInputBean.getMasterTablePkId(), Boolean.FALSE);

		} else if (requestInputBean.getAdminStatusId() == 4) {

			updatedRow = dimensionModRepo.approveRejectRequest(requestInputBean.getAdminStatusId().intValue(), requestInputBean.getComments(), requestInputBean.getModTablePkId());

			if (actionId == 2) {

				dimensionRepo.setIsPending(requestInputBean.getMasterTablePkId(), Boolean.FALSE);
				editDimension(requestInputBean);

			} else if (actionId == 4) {

				dimensionRepo.setIsPending(requestInputBean.getMasterTablePkId(), Boolean.FALSE);
				deleteDimension(requestInputBean);

			}
		}

	}

	@org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
	public void editDimension(ApprovalInputBean requestInputBean) throws ApplicationException {
		DimensionMaster dimensionMaster = dimensionRepo.findByDimesnsionMasterId(requestInputBean.getMasterTablePkId());
		DimensionMasterMod dimensionMasterMod = dimensionModRepo.findByDimMasterModId(requestInputBean.getModTablePkId());

		Type type = new TypeToken<DimensionMasterBean>() {
		}.getType();
		DimensionMasterBean dimensionMasterBean = new DimensionMasterBean();
		dimensionMasterBean = JsonUtility.getGsonObject().fromJson(dimensionMasterMod.getDimMasterJson(), type);

		DimensionMaster parentDimensionMaster = null;
		if (dimensionMasterBean.getParentDimensionBean() != null) {
			//parentDimensionMaster = dimensionRepo.findByDimensionCodeIgnoreCaseAndIsActive(dimensionMasterBean.getParentDimensionBean().getDimensionCode(), true);
			if (parentDimensionMaster == null) {
				throw new ApplicationException(ErrorCode.E0299.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0299.toString()));
			}
		}

		CodeListMaster codeListMaster = null;
		if (dimensionMasterBean.getCodeListMasterBean() != null) {
			codeListMaster = codeListMasterRepo.findByClCodeIgnoreCaseAndClVersionAndIsActive(dimensionMasterBean.getCodeListMasterBean().getClCode(), dimensionMasterBean.getCodeListMasterBean().getClVersion(), true);
		}

		Date approvedOn = new Date();

		UserMaster approvedBy = new UserMaster();
		approvedBy.setUserId(requestInputBean.getUserId());

		if (dimensionMaster != null) {

			if (dimensionMasterBean.getAgencyMasterCode() != null) {
				AgencyMaster agencyMaster = sdmxAgencyMasterRepo.findByAgencyCode(dimensionMasterBean.getAgencyMasterCode());
				if (agencyMaster != null && agencyMaster.getAgencyMasterId() != 0) {
					AgencyMaster agencyMaster2 = new AgencyMaster();
					agencyMaster2.setAgencyMasterId(agencyMaster.getAgencyMasterId());
					dimensionMaster.setAgencyMaster(agencyMaster2);
				}
			}

			dimensionMaster.setCodeListMaster(codeListMaster);
			dimensionMaster.setDimensionName(dimensionMasterBean.getDimensionName());
			dimensionMaster.setDimensionCode(dimensionMasterBean.getDimensionCode());
			dimensionMaster.setConceptVersion(dimensionMasterBean.getConceptVersion());
			DimensionType dimensionType = new DimensionType();
			dimensionType.setDimesnionTypeId(dimensionMasterBean.getDimensionTypeId());

			dimensionMaster.setDimensionType(dimensionType);
			dimensionMaster.setParentDimensionMaster(parentDimensionMaster);

			if (dimensionMasterBean.getRegEx() != null) {
				Regex regex = new Regex();
				regex.setRegexId(dimensionMasterBean.getRegEx().getRegexId());
				dimensionMaster.setMinLength(dimensionMasterBean.getMinLength());
				dimensionMaster.setMaxLength(dimensionMasterBean.getMaxLength());
				dimensionMaster.setRegex(regex);
			} else {
				dimensionMaster.setRegex(null);
			}

			dimensionMaster.setDimDesc(dimensionMasterBean.getDimensionDesc());
			dimensionMaster.setIsActive(dimensionMasterBean.getIsActive());
			dimensionMaster.setMinLength(dimensionMasterBean.getMinLength());
			dimensionMaster.setMaxLength(dimensionMasterBean.getMaxLength());
			dimensionMaster.setLastModifiedBy(dimensionMasterMod.getCreatedBy());
			dimensionMaster.setLastModifiedOn(dimensionMasterMod.getCreatedOn());
			dimensionMaster.setLastUpdatedOn(dimensionMasterMod.getCreatedOn());
			dimensionMaster.setLastApprovedBy(approvedBy);
			dimensionMaster.setLastApprovedOn(approvedOn);
			dimensionMaster.setIsCommon(dimensionMasterBean.getIsCommon());
			dimensionMaster.setIsMandatory(dimensionMasterBean.getIsMandatory());
			dimensionMaster.setDataType(dimensionMasterBean.getDataType());
			dimensionRepo.save(dimensionMaster);

			//ServiceResponse serviceResponse = fusionApiController.submitDimensionData(dimensionMasterMod.getCreatedBy().getUserId());
			ServiceResponse serviceResponse = fusionApiController.submitDimensionDataWithConceptVersion(dimensionMasterMod.getCreatedBy().getUserId(), dimensionMasterBean.getConceptVersion(), dimensionMasterBean.getAgencyMasterCode());
			if (!serviceResponse.isStatus()) {
				throw new ApplicationException(ErrorCode.E1413.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1413.toString()));
			}

		}
	}

	@org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
	public void deleteDimension(ApprovalInputBean requestInputBean) throws ApplicationException {
		DimensionMaster dimensionMaster = dimensionRepo.findByDimesnsionMasterId(requestInputBean.getMasterTablePkId());
		DimensionMasterMod dimensionMasterMod = dimensionModRepo.findByDimMasterModId(requestInputBean.getModTablePkId());
		Date approvedOn = new Date();

		UserMaster approvedBy = new UserMaster();
		approvedBy.setUserId(requestInputBean.getUserId());

		if (dimensionMaster != null) {
			dimensionMaster.setIsActive(Boolean.FALSE);
			dimensionMaster.setLastApprovedBy(approvedBy);
			dimensionMaster.setLastApprovedOn(approvedOn);
			dimensionRepo.save(dimensionMaster);

		}

		ServiceResponse serviceResponse = fusionApiController.submitDimensionDataWithConceptVersion(dimensionMasterMod.getCreatedBy().getUserId(), dimensionMaster != null ? dimensionMaster.getConceptVersion() : null, dimensionMaster != null ? dimensionMaster.getAgencyMaster().getAgencyMasterCode() : null);

		if (!serviceResponse.isStatus()) {
			throw new ApplicationException(ErrorCode.E1413.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1413.toString()));
		}

	}

	public boolean isDimensionPending(String dimCode, String agencyMasterCode, String conceptVersion) {
		DimensionMaster sdmxDimensionMasters = null;
		try {
			sdmxDimensionMasters = dimensionRepo.getDimensionByCodeAndAgency(dimCode, agencyMasterCode, conceptVersion);
			if (sdmxDimensionMasters != null) {
				if (sdmxDimensionMasters.getIsPending().equals(Boolean.TRUE)) {
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

	public boolean isDimensionArrayPending(List<DimensionMasterBean> dimensionRequestBeanList) {
		List<DimensionMaster> sdmxDimensionMastersList = new ArrayList<>();
		try {
			Map<String, List<String>> agencyVersionDmCodeMap = new HashMap<>();
			for (DimensionMasterBean obj : dimensionRequestBeanList) {
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
