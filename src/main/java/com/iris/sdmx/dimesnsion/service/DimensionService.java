/**
 * 
 */
package com.iris.sdmx.dimesnsion.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omg.CORBA.DynamicImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.iris.dto.ReturnDto;
import com.iris.exception.ApplicationException;
import com.iris.exception.ServiceException;
import com.iris.model.UserMaster;
import com.iris.sdmx.agency.master.bean.SdmxAgencyMasterBean;
import com.iris.sdmx.agency.master.entity.AgencyMaster;
import com.iris.sdmx.agency.master.repo.SdmxAgencyMasterRepo;
import com.iris.sdmx.codelist.bean.CodeListMasterBean;
import com.iris.sdmx.codelist.entity.CodeListMaster;
import com.iris.sdmx.codelist.repo.CodeListMasterRepo;
import com.iris.sdmx.dimesnsion.bean.DimensionMasterBean;
import com.iris.sdmx.dimesnsion.bean.RegexBean;
import com.iris.sdmx.dimesnsion.entity.DimensionMaster;
import com.iris.sdmx.dimesnsion.entity.DimensionMasterMod;
import com.iris.sdmx.dimesnsion.entity.DimensionType;
import com.iris.sdmx.dimesnsion.entity.Regex;
import com.iris.sdmx.dimesnsion.repo.DimensionModRepo;
import com.iris.sdmx.dimesnsion.repo.DimensionRepo;
import com.iris.sdmx.element.bean.SdmxElementBean;
import com.iris.sdmx.elementdimensionmapping.bean.ElementDimensionStoredJson;
import com.iris.sdmx.exceltohtml.bean.DimensionDetailCategories;
import com.iris.sdmx.status.service.AdminStatusService;
import com.iris.sdmx.util.SDMXConstants;
import com.iris.service.GenericService;
import com.iris.util.JsonUtility;
import com.iris.util.Validations;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author sajadhav
 *
 */
@Service
public class DimensionService implements GenericService<DimensionMaster, Long> {

	private static final Logger LOGGER = LogManager.getLogger(DimensionService.class);

	@Autowired
	private DimensionRepo dimensionRepo;

	@Autowired
	private CodeListMasterRepo codeListMasterRepo;

	@Autowired
	private DimensionModRepo dimensionModRepo;

	@Autowired
	EntityManager entityManager;

	@Autowired
	private AdminStatusService adminStatusService;

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
		try {
			Boolean isActive = null;
			Long parentDimensionId = null;
			for (String columnName : columnValueMap.keySet()) {
				if (columnValueMap.get(columnName) != null && columnName.equalsIgnoreCase(ColumnConstants.IS_ACTIVE.getConstantVal())) {
					isActive = (Boolean) columnValueMap.get(columnName);
				} else if (columnValueMap.get(columnName) != null && columnName.equalsIgnoreCase(ColumnConstants.PARENT_DIMENSION_ID.getConstantVal())) {
					parentDimensionId = (Long) columnValueMap.get(columnName);
				}
			}

			if (methodName.equalsIgnoreCase(MethodConstants.GET_DIM_MASTER_RECORD_BY_PARENT_ID_IS_ACTIVE.getConstantVal())) {
				return dimensionRepo.findByIsActiveAndParentDimensionMasterDimesnsionMasterId(isActive, parentDimensionId);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_DIM_MASTER_RECORD_BY_IS_ACTIVE.getConstantVal())) {
				return dimensionRepo.findByIsActive(isActive);
			}
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getErrorMessage(), e);
		}
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

	public List<DimensionMasterBean> prepareDimensionMasterResponseList(List<DimensionMaster> dimensionMasterList) {
		List<DimensionMasterBean> dimensionMasterBeans = new ArrayList<>();

		for (DimensionMaster dimensionMaster : dimensionMasterList) {
			if (dimensionMaster.getIsActive().equals(Boolean.FALSE))
				continue;
			DimensionMasterBean dimensionMasterBean = new DimensionMasterBean();
			dimensionMasterBean.setDimensionCode(dimensionMaster.getDimensionCode());
			dimensionMasterBean.setDimensionDesc(dimensionMaster.getDimDesc());
			dimensionMasterBean.setDimensionId(dimensionMaster.getDimesnsionMasterId());
			dimensionMasterBean.setDimensionName(dimensionMaster.getDimensionName());
			dimensionMasterBean.setDimensionType(dimensionMaster.getDimensionType().getDimesnsionTypeName());
			dimensionMasterBean.setDimensionTypeId(dimensionMaster.getDimensionType().getDimesnionTypeId());
			dimensionMasterBean.setConceptVersion(dimensionMaster.getConceptVersion());
			if (!CollectionUtils.isEmpty(dimensionMaster.getDimensionMasterList())) {
				prepareInnerDimensionObject(dimensionMasterBean, dimensionMaster);
			}

			if (dimensionMaster.getCodeListMaster() != null) {
				CodeListMasterBean codeListMasterBean = new CodeListMasterBean();
				codeListMasterBean.setClCode(dimensionMaster.getCodeListMaster().getClCode());
				codeListMasterBean.setClLable(dimensionMaster.getCodeListMaster().getClLable());
				codeListMasterBean.setClVersion(dimensionMaster.getCodeListMaster().getClVersion());
				dimensionMasterBean.setCodeListMasterBean(codeListMasterBean);
			}

			if (dimensionMaster.getAgencyMaster() != null && dimensionMaster.getAgencyMaster().getAgencyMasterId() != 0) {
				SdmxAgencyMasterBean sdmxAgencyMasterBean = new SdmxAgencyMasterBean();

				sdmxAgencyMasterBean.setAgencyMasterCode(dimensionMaster.getAgencyMaster().getAgencyMasterCode());
				sdmxAgencyMasterBean.setAgencyMasterId(dimensionMaster.getAgencyMaster().getAgencyMasterId());
				sdmxAgencyMasterBean.setAgencyMasterLabel(dimensionMaster.getAgencyMaster().getAgencyMasterLabel());
				dimensionMasterBean.setSdmxAgencyMasterBean(sdmxAgencyMasterBean);
			}

			if (dimensionMaster.getRegex() != null) {
				RegexBean regExBean = new RegexBean();
				regExBean.setRegex(dimensionMaster.getRegex().getRegex());
				regExBean.setRegexName(dimensionMaster.getRegex().getRegexName());
				regExBean.setRegexId(dimensionMaster.getRegex().getRegexId());
				dimensionMasterBean.setRegEx(regExBean);
			}

			dimensionMasterBean.setMinLength(dimensionMaster.getMinLength());
			dimensionMasterBean.setMaxLength(dimensionMaster.getMaxLength());
			dimensionMasterBean.setIsCommon(dimensionMaster.getIsCommon());
			dimensionMasterBean.setIsMandatory(dimensionMaster.getIsMandatory());
			dimensionMasterBean.setDataType(dimensionMaster.getDataType());
			if (dimensionMaster.getLastUpdatedOn() != null) {
				dimensionMasterBean.setLastUpdatedOn(dimensionMaster.getLastUpdatedOn());
				dimensionMasterBean.setLastUpdatedOnInLong(dimensionMaster.getLastUpdatedOn().getTime());
			}

			dimensionMasterBeans.add(dimensionMasterBean);
		}

		return dimensionMasterBeans;
	}

	private void prepareInnerDimensionObject(DimensionMasterBean dimensionMasterBean, DimensionMaster dimensionMaster) {
		List<DimensionMasterBean> dimensionMasterBeans = new ArrayList<>();

		for (DimensionMaster dimen : dimensionMaster.getDimensionMasterList()) {
			if (dimen.getIsActive().equals(Boolean.FALSE))
				continue;
			DimensionMasterBean dimensionMasBean = new DimensionMasterBean();
			dimensionMasBean.setDimensionCode(dimen.getDimensionCode());
			dimensionMasBean.setDimensionDesc(dimen.getDimDesc());
			dimensionMasBean.setDimensionId(dimen.getDimesnsionMasterId());
			dimensionMasBean.setDimensionName(dimen.getDimensionName());
			dimensionMasBean.setDimensionType(dimen.getDimensionType().getDimesnsionTypeName());
			dimensionMasBean.setDimensionTypeId(dimen.getDimensionType().getDimesnionTypeId());
			dimensionMasBean.setConceptVersion(dimen.getConceptVersion());
			if (!CollectionUtils.isEmpty(dimen.getDimensionMasterList())) {
				prepareInnerDimensionObject(dimensionMasBean, dimen);
			}

			if (dimen.getCodeListMaster() != null) {
				CodeListMasterBean codeListMasterBean = new CodeListMasterBean();
				codeListMasterBean.setClCode(dimen.getCodeListMaster().getClCode());
				codeListMasterBean.setClLable(dimen.getCodeListMaster().getClLable());
				codeListMasterBean.setClVersion(dimen.getCodeListMaster().getClVersion());
				dimensionMasBean.setCodeListMasterBean(codeListMasterBean);
			}

			if (dimen.getAgencyMaster() != null && dimen.getAgencyMaster().getAgencyMasterId() != 0) {
				SdmxAgencyMasterBean sdmxAgencyMasterBean = new SdmxAgencyMasterBean();

				sdmxAgencyMasterBean.setAgencyMasterCode(dimen.getAgencyMaster().getAgencyMasterCode());
				sdmxAgencyMasterBean.setAgencyMasterId(dimen.getAgencyMaster().getAgencyMasterId());
				sdmxAgencyMasterBean.setAgencyMasterLabel(dimen.getAgencyMaster().getAgencyMasterLabel());
				dimensionMasBean.setSdmxAgencyMasterBean(sdmxAgencyMasterBean);
			}

			if (dimen.getRegex() != null) {
				RegexBean regExBean = new RegexBean();
				regExBean.setRegex(dimen.getRegex().getRegex());
				regExBean.setRegexName(dimen.getRegex().getRegexName());
				regExBean.setRegexId(dimen.getRegex().getRegexId());
				dimensionMasBean.setRegEx(regExBean);
			}

			dimensionMasBean.setMinLength(dimen.getMinLength());
			dimensionMasBean.setMaxLength(dimen.getMaxLength());
			dimensionMasBean.setIsCommon(dimen.getIsCommon());
			dimensionMasBean.setIsMandatory(dimen.getIsMandatory());
			dimensionMasBean.setDataType(dimen.getDataType());
			if (dimen.getLastUpdatedOn() != null) {
				dimensionMasBean.setLastUpdatedOn(null);
				dimensionMasBean.setLastUpdatedOnInLong(dimen.getLastUpdatedOn().getTime());
			}

			//dimensionMasBean.setLastUpdatedOn(dimen.getLastUpdatedOn());

			dimensionMasterBeans.add(dimensionMasBean);
		}

		dimensionMasterBean.setDimesnionMasterBeans(dimensionMasterBeans);
	}

	@Transactional(rollbackOn = Exception.class)
	public boolean addDimensionData(DimensionMasterBean inputDimensionMasterBean, boolean isApprovalRequired) throws ApplicationException {

		DimensionMaster dimensionMasterBean = dimensionRepo.getDimensionByCodeAndAgency(inputDimensionMasterBean.getDimensionCode(), inputDimensionMasterBean.getAgencyMasterCode(), inputDimensionMasterBean.getConceptVersion());
		DimensionMaster parentDimensionMaster = null;
		if (inputDimensionMasterBean.getParentDimensionBean() != null) {
			parentDimensionMaster = dimensionRepo.getDimensionByCodeAndAgency(inputDimensionMasterBean.getParentDimensionBean().getDimensionCode(), inputDimensionMasterBean.getParentDimensionBean().getAgencyMasterCode(), inputDimensionMasterBean.getParentDimensionBean().getConceptVersion());
			if (parentDimensionMaster == null) {
				throw new ApplicationException("E004", "Parent dimension record not present");
			}
		}

		if (dimensionMasterBean != null) {
			throw new ApplicationException("E004", "Same dim record present");
		}

		Date createdOn = new Date();
		UserMaster createdBy = new UserMaster();
		createdBy.setUserId(inputDimensionMasterBean.getUserId());

		DimensionMasterMod dimensionMasterMod = new DimensionMasterMod();
		dimensionMasterMod.setDimCode(inputDimensionMasterBean.getDimensionCode().toUpperCase());
		dimensionMasterMod.setIsActive(true);
		dimensionMasterMod.setActionId(1);
		dimensionMasterMod.setConceptVersion(inputDimensionMasterBean.getConceptVersion());
		dimensionMasterMod.setAgencyIdFk(sdmxAgencyMasterRepo.findByAgencyCode(inputDimensionMasterBean.getAgencyMasterCode()));
		dimensionMasterMod.setDimMasterJson(JsonUtility.getGsonObject().toJson(inputDimensionMasterBean));

		if (isApprovalRequired) {
			dimensionMasterMod.setAdminStatusId(adminStatusService.findIdByStatusTechCode(SDMXConstants.StatusTechCode.PENDING_FOR_APPROVAL.getCode()).intValue());

		} else {
			dimensionMasterMod.setAdminStatusId(adminStatusService.findIdByStatusTechCode(SDMXConstants.StatusTechCode.APPROVED.getCode()).intValue());
		}

		dimensionMasterMod.setCreatedBy(createdBy);
		dimensionMasterMod.setCreatedOn(createdOn);
		dimensionMasterMod.setLastUpdatedOn(createdOn);

		dimensionMasterMod = dimensionModRepo.save(dimensionMasterMod);

		if (!isApprovalRequired && dimensionMasterMod.getDimMasterModId() != null) {
			DimensionMaster dimensionMaster = new DimensionMaster();

			if (inputDimensionMasterBean.getCodeListMasterBean() != null) {
				CodeListMaster codeListMaster = codeListMasterRepo.findByClCodeIgnoreCaseAndClVersionAndIsActive(inputDimensionMasterBean.getCodeListMasterBean().getClCode(), inputDimensionMasterBean.getCodeListMasterBean().getClVersion(), true);
				dimensionMaster.setCodeListMaster(codeListMaster);
			}

			dimensionMaster.setDimensionName(inputDimensionMasterBean.getDimensionName());
			dimensionMaster.setDimensionCode(inputDimensionMasterBean.getDimensionCode().toUpperCase());
			dimensionMaster.setConceptVersion(inputDimensionMasterBean.getConceptVersion());
			DimensionType dimensionType = new DimensionType();
			dimensionType.setDimesnionTypeId(inputDimensionMasterBean.getDimensionTypeId());

			if (inputDimensionMasterBean.getAgencyMasterCode() != null) {
				AgencyMaster agencyMaster = sdmxAgencyMasterRepo.findByAgencyCode(inputDimensionMasterBean.getAgencyMasterCode());
				if (agencyMaster != null && agencyMaster.getAgencyMasterId() != 0) {
					AgencyMaster agencyMaster2 = new AgencyMaster();
					agencyMaster2.setAgencyMasterId(agencyMaster.getAgencyMasterId());
					dimensionMaster.setAgencyMaster(agencyMaster2);
				}
			}

			dimensionMaster.setDimensionType(dimensionType);
			dimensionMaster.setParentDimensionMaster(parentDimensionMaster);

			if (inputDimensionMasterBean.getRegEx() != null) {
				Regex regex = new Regex();
				regex.setRegexId(inputDimensionMasterBean.getRegEx().getRegexId());
				dimensionMaster.setRegex(regex);
			}

			dimensionMaster.setMinLength(inputDimensionMasterBean.getMinLength());
			dimensionMaster.setMaxLength(inputDimensionMasterBean.getMaxLength());
			dimensionMaster.setDimDesc(inputDimensionMasterBean.getDimensionDesc());
			dimensionMaster.setIsActive(inputDimensionMasterBean.getIsActive());

			dimensionMaster.setCreatedBy(createdBy);
			dimensionMaster.setCreatedOn(createdOn);
			dimensionMaster.setLastUpdatedOn(createdOn);
			dimensionMaster.setIsCommon(inputDimensionMasterBean.getIsCommon());
			dimensionMaster.setIsMandatory(inputDimensionMasterBean.getIsMandatory());
			dimensionMaster.setDataType(inputDimensionMasterBean.getDataType());
			dimensionMaster.setIsPending(Boolean.FALSE);
			dimensionRepo.save(dimensionMaster);
			if (dimensionMaster.getDimesnsionMasterId() != null) {
				dimensionModRepo.updateDimIdFk(dimensionMasterMod.getDimMasterModId(), dimensionMaster.getDimesnsionMasterId());

			}
		}

		return true;
	}

	@Transactional(rollbackOn = Exception.class)
	public boolean editDimensionData(DimensionMasterBean dimensionMasterBean, boolean isApprovalRequired) {

		try {

			DimensionMaster dimensionMaster = dimensionRepo.getDimensionByCodeAndAgency(dimensionMasterBean.getDimensionCode(), dimensionMasterBean.getAgencyMasterCode(), dimensionMasterBean.getConceptVersion());

			DimensionMaster parentDimensionMaster = null;
			if (dimensionMasterBean.getParentDimensionBean() != null) {
				parentDimensionMaster = dimensionRepo.getDimensionByCodeAndAgency(dimensionMasterBean.getParentDimensionBean().getDimensionCode(), dimensionMasterBean.getParentDimensionBean().getAgencyMasterCode(), dimensionMasterBean.getParentDimensionBean().getConceptVersion());
				if (parentDimensionMaster == null) {
					throw new ApplicationException("E004", "Parent dimension record not present");
				}
			}

			CodeListMaster codeListMaster = null;
			if (dimensionMasterBean.getCodeListMasterBean() != null) {
				codeListMaster = codeListMasterRepo.findByClCodeIgnoreCaseAndClVersionAndIsActive(dimensionMasterBean.getCodeListMasterBean().getClCode(), dimensionMasterBean.getCodeListMasterBean().getClVersion(), true);
			}

			Date createdOn = new Date();

			UserMaster createdBy = new UserMaster();
			createdBy.setUserId(dimensionMasterBean.getUserId());

			if (dimensionMaster != null) {
				DimensionMasterMod dimensionMasterMod = new DimensionMasterMod();
				dimensionMasterMod.setDimensionmaster(dimensionMaster);
				dimensionMasterMod.setIsActive(true);
				dimensionMasterMod.setDimMasterJson(JsonUtility.getGsonObject().toJson(dimensionMasterBean));
				dimensionMasterMod.setDimCode(dimensionMaster.getDimensionCode().toUpperCase());
				dimensionMasterMod.setCreatedBy(createdBy);
				dimensionMasterMod.setCreatedOn(createdOn);
				dimensionMasterMod.setLastUpdatedOn(createdOn);
				dimensionMasterMod.setActionId(2);
				dimensionMasterMod.setConceptVersion(dimensionMasterBean.getConceptVersion());
				dimensionMasterMod.setAgencyIdFk(sdmxAgencyMasterRepo.findByAgencyCode(dimensionMasterBean.getAgencyMasterCode()));

				if (isApprovalRequired) {
					dimensionMasterMod.setAdminStatusId(adminStatusService.findIdByStatusTechCode(SDMXConstants.StatusTechCode.PENDING_FOR_APPROVAL.getCode()).intValue());
					dimensionRepo.setIsPending(dimensionMaster.getDimesnsionMasterId(), Boolean.TRUE);
				} else {
					dimensionMasterMod.setAdminStatusId(adminStatusService.findIdByStatusTechCode(SDMXConstants.StatusTechCode.APPROVED.getCode()).intValue());
				}

				dimensionModRepo.save(dimensionMasterMod);

				if (!isApprovalRequired && dimensionMasterMod.getDimMasterModId() != null) {

					dimensionMaster.setCodeListMaster(codeListMaster);
					dimensionMaster.setDimensionName(dimensionMasterBean.getDimensionName());
					dimensionMaster.setDimensionCode(dimensionMasterBean.getDimensionCode().toUpperCase());
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
					dimensionMaster.setLastModifiedBy(createdBy);
					dimensionMaster.setLastModifiedOn(createdOn);
					dimensionMaster.setLastUpdatedOn(createdOn);
					dimensionMaster.setIsCommon(dimensionMasterBean.getIsCommon());
					dimensionMaster.setIsMandatory(dimensionMasterBean.getIsMandatory());
					dimensionMaster.setDataType(dimensionMasterBean.getDataType());
					dimensionMaster.setIsPending(Boolean.FALSE);
					dimensionRepo.save(dimensionMaster);
				}
				return true;
			} else {
				throw new ServiceException("Code list not exist");
			}
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getErrorMessage(), e);
			return false;
		}
	}

	@Transactional(rollbackOn = Exception.class)
	public boolean deleteDimensionData(DimensionMasterBean inputDimensionMasterBean, boolean isApprovalRequired) throws ApplicationException {
		List<String> commingRequest = null;
		List<DimensionMaster> dimensionMasterList = new ArrayList<DimensionMaster>();
		Map<String, List<String>> agencyVersionDmCodeMap = new HashMap<>();
		if (!CollectionUtils.isEmpty(inputDimensionMasterBean.getDimesnionMasterBeans())) {
			commingRequest = new ArrayList<String>();
			for (DimensionMasterBean obj : inputDimensionMasterBean.getDimesnionMasterBeans()) {
				commingRequest.add(obj.getDimensionCode() + "~~" + obj.getAgencyMasterCode() + "~~" + obj.getConceptVersion());

				String key = obj.getAgencyMasterCode() + "~~" + obj.getConceptVersion();
				if (agencyVersionDmCodeMap.containsKey(key)) {
					List<String> dmCodeList = agencyVersionDmCodeMap.get(key);
					dmCodeList.add(obj.getDimensionCode());
				} else {
					List<String> dmCodeList = new ArrayList<>();
					dmCodeList.add(obj.getDimensionCode());
					agencyVersionDmCodeMap.put(key, dmCodeList);
				}
			}
		}
		List<DimensionMaster> dimensionList;
		Iterator<Entry<String, List<String>>> it = agencyVersionDmCodeMap.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<String, List<String>> set = (Map.Entry<String, List<String>>) it.next();
			//System.out.println(set.getKey() + " = " + set.getValue());
			String conceptVersion = set.getKey().split("~~")[1];
			String agencyCode = set.getKey().split("~~")[0];
			Long agencyId = sdmxAgencyMasterRepo.findByAgencyCode(agencyCode).getAgencyMasterId();
			List<DimensionMasterMod> dimensionMasterMods = dimensionModRepo.findByDimCodeInAndAdminStatusId(set.getValue(), conceptVersion, agencyId, 1);
			if (!Validations.isEmpty(dimensionMasterMods)) {
				return false;
			}

			dimensionList = new ArrayList<>();
			dimensionList = dimensionRepo.findByDimensionCodeInAndIsActive(set.getValue(), agencyId, conceptVersion, true);
			if (!dimensionList.isEmpty()) {
				dimensionMasterList.addAll(dimensionList);
			}
		}

		UserMaster createdBy = new UserMaster();
		createdBy.setUserId(inputDimensionMasterBean.getUserId());
		List<String> dmCodeList = inputDimensionMasterBean.getDimesnionMasterBeans().stream().map(f -> f.getDimensionCode()).collect(Collectors.toList());

		if (CollectionUtils.isEmpty(commingRequest)) {
			return false;
		} else {
			if (!CollectionUtils.isEmpty(dimensionMasterList)) {
				for (DimensionMaster dim : dimensionMasterList) {
					boolean recordFound = false;
					innerBreak: for (String str2 : commingRequest) {
						if ((dim.getDimensionCode() + "~~" + dim.getAgencyMaster().getAgencyMasterCode() + "~~" + dim.getConceptVersion()).equals(str2)) {
							recordFound = true;
							break innerBreak;
						}
					}
					if (!recordFound) {
						dimensionMasterList.remove(dim);
					}
				}
			} else {
				return false;
			}
		}

		Map<String, DimensionMaster> dimensionMasterMap = new HashMap<>();

		for (DimensionMaster dimensionMaster : dimensionMasterList) {
			dimensionMasterMap.put(dimensionMaster.getDimensionCode() + "~~" + dimensionMaster.getAgencyMaster().getAgencyMasterCode() + "~~" + dimensionMaster.getConceptVersion(), dimensionMaster);
		}

		for (DimensionMasterBean dimensionMasterBean : inputDimensionMasterBean.getDimesnionMasterBeans()) {
			Date creationDate = new Date();

			List<DimensionMaster> dimensionMasters = dimensionRepo.getSdmxDimensionMasterUsingDimCode(dimensionMasterBean.getDimensionCode(), dimensionMasterBean.getAgencyMasterCode(), dimensionMasterBean.getConceptVersion());
			if (dimensionMasters != null && dimensionMasters.size() > 0) {
				long dimensionId = dimensionMasters.get(0).getDimesnsionMasterId();
				List<DimensionMaster> dimMaster = dimensionRepo.findByIsActiveAndParentDimensionMasterDimesnsionMasterId(true, dimensionId);
				if (dimMaster != null && dimMaster.size() > 0) {
					for (DimensionMaster dimensionMaster : dimMaster) {
						dimensionMaster.setParentDimensionMaster(dimensionMasters.get(0).getParentDimensionMaster());
						AgencyMaster agencyMaster = new AgencyMaster();
						agencyMaster = sdmxAgencyMasterRepo.findByAgencyCode(dimensionMasterBean.getAgencyMasterCode());
						dimensionMaster.setAgencyMaster(agencyMaster);
						dimensionRepo.save(dimensionMaster);
					}
				}
			}

			if (dimensionMasterMap.containsKey(dimensionMasterBean.getDimensionCode() + "~~" + dimensionMasterBean.getAgencyMasterCode() + "~~" + dimensionMasterBean.getConceptVersion())) {
				DimensionMaster dimensionMaster = dimensionMasterMap.get(dimensionMasterBean.getDimensionCode() + "~~" + dimensionMasterBean.getAgencyMasterCode() + "~~" + dimensionMasterBean.getConceptVersion());
				// Create modification object

				//new dimMaster json to be stored in mod table
				DimensionMaster parentDimensionMaster = null;
				if (dimensionMaster.getParentDimensionMaster() != null) {
					parentDimensionMaster = dimensionRepo.findByDimensionCodeIgnoreCaseAndIsActive(dimensionMaster.getParentDimensionMaster().getDimensionCode(), dimensionMaster.getParentDimensionMaster().getAgencyMaster().getAgencyMasterId(), dimensionMaster.getParentDimensionMaster().getConceptVersion(), true);
					if (parentDimensionMaster == null) {
						throw new ApplicationException("E004", "Parent dimension record not present");
					}
				}

				CodeListMaster codeListMaster = null;
				if (dimensionMaster.getCodeListMaster() != null) {
					codeListMaster = codeListMasterRepo.findByClCodeIgnoreCaseAndClVersionAndIsActive(dimensionMaster.getCodeListMaster().getClCode(), dimensionMaster.getCodeListMaster().getClVersion(), true);
				}

				CodeListMasterBean codeListMasterBean = new CodeListMasterBean();
				if (codeListMaster != null) {
					codeListMasterBean.setClCode(codeListMaster.getClCode());
					codeListMasterBean.setClVersion(codeListMaster.getClVersion());
					dimensionMasterBean.setCodeListMasterBean(codeListMasterBean);
				}

				dimensionMasterBean.setDimensionName(dimensionMaster.getDimensionName());
				dimensionMasterBean.setDimensionDesc(dimensionMaster.getDimDesc());
				dimensionMasterBean.setDimensionTypeId(dimensionMaster.getDimensionType().getDimesnionTypeId());
				dimensionMasterBean.setConceptVersion(dimensionMaster.getConceptVersion());
				dimensionMasterBean.setAgencyMasterCode(dimensionMaster.getAgencyMaster().getAgencyMasterCode());
				DimensionMasterBean parentDimBean = new DimensionMasterBean();
				if (parentDimensionMaster != null) {
					parentDimBean.setDimensionCode(parentDimensionMaster.getDimensionCode());
					dimensionMasterBean.setParentDimensionBean(parentDimBean);
				}

				RegexBean regEx = new RegexBean();
				if (dimensionMaster.getRegex() != null) {
					regEx.setRegexId(dimensionMaster.getRegex().getRegexId());
					dimensionMasterBean.setRegEx(regEx);
					dimensionMasterBean.setMinLength(dimensionMaster.getMinLength());
					dimensionMasterBean.setMaxLength(dimensionMaster.getMaxLength());
				} else {
					dimensionMasterBean.setRegEx(null);
					dimensionMasterBean.setMinLength(null);
					dimensionMasterBean.setMaxLength(null);
				}
				dimensionMasterBean.setIsCommon(dimensionMaster.getIsCommon());
				dimensionMasterBean.setIsMandatory(dimensionMaster.getIsMandatory());
				dimensionMasterBean.setDataType(dimensionMaster.getDataType());
				//new dimMaster json
				DimensionMasterMod dimensionMasterMod = new DimensionMasterMod();
				dimensionMasterMod.setDimensionmaster(dimensionMasterMap.get(dimensionMasterBean.getDimensionCode() + "~~" + dimensionMasterBean.getAgencyMasterCode() + "~~" + dimensionMasterBean.getConceptVersion()));
				dimensionMasterMod.setIsActive(true);

				dimensionMasterMod.setDimMasterJson(JsonUtility.getGsonObject().toJson(dimensionMasterBean));

				dimensionMasterMod.setCreatedBy(createdBy);
				dimensionMasterMod.setCreatedOn(creationDate);
				dimensionMasterMod.setLastUpdatedOn(creationDate);
				dimensionMasterMod.setActionId(4);
				dimensionMasterMod.setDimCode(dimensionMasterBean.getDimensionCode().toUpperCase());
				AgencyMaster agencyMaster = new AgencyMaster();
				agencyMaster.setAgencyMasterId(sdmxAgencyMasterRepo.findByAgencyCode(dimensionMaster.getAgencyMaster().getAgencyMasterCode()).getAgencyMasterId());
				dimensionMasterMod.setAgencyIdFk(agencyMaster);
				dimensionMasterMod.setConceptVersion(dimensionMaster.getConceptVersion());
				// Siddhart need to look into this
				if (isApprovalRequired) {
					dimensionMasterMod.setAdminStatusId(adminStatusService.findIdByStatusTechCode(SDMXConstants.StatusTechCode.PENDING_FOR_APPROVAL.getCode()).intValue());
					//dimensionRepo.setIsPending(dimensionMaster.getDimesnsionMasterId(), Boolean.TRUE);
					DimensionMaster dimensionEntity = dimensionRepo.findByDimesnsionMasterId(dimensionMaster.getDimesnsionMasterId());
					if (dimensionEntity.getIsActive().equals(Boolean.TRUE)) {
						dimensionEntity.setIsPending(Boolean.TRUE);
						dimensionRepo.save(dimensionEntity);
					}

				} else {
					dimensionMasterMod.setAdminStatusId(adminStatusService.findIdByStatusTechCode(SDMXConstants.StatusTechCode.APPROVED.getCode()).intValue());
				}

				dimensionMasterMod = dimensionModRepo.save(dimensionMasterMod);

				if (!isApprovalRequired && dimensionMasterMod.getDimMasterModId() != null) {
					dimensionMaster.setIsActive(false);
					dimensionMaster.setIsPending(Boolean.FALSE);
					AgencyMaster agency = new AgencyMaster();
					agency = sdmxAgencyMasterRepo.findByAgencyCode(dimensionMasterBean.getAgencyMasterCode());
					dimensionMaster.setAgencyMaster(agency);
					dimensionRepo.save(dimensionMaster);
				}
			}
		}

		return true;
	}

	public Boolean getSdmxDimensionMasterUsingDimCode(String dimCode, String agencyMasterCode, String conceptVersion) throws ServiceException {
		boolean found = false;
		List<DimensionMaster> sdmxDimensionMasters = null;
		try {
			sdmxDimensionMasters = dimensionRepo.getSdmxDimensionMasterUsingDimCode(dimCode, agencyMasterCode, conceptVersion);
			if (!Validations.isEmpty(sdmxDimensionMasters)) {
				found = true;
			}
			return found;
		} catch (Exception e) {
			LOGGER.error("Exception : ", e);
		}
		return found;
	}

	public List<DimensionMaster> getCommonOrMandatoryDimension() throws ServiceException {

		List<DimensionMaster> sdmxDimensionMasters = null;
		try {
			sdmxDimensionMasters = dimensionRepo.getCommonOrMandatoryDimension();

		} catch (Exception e) {
			LOGGER.error("Exception : ", e);
		}
		return sdmxDimensionMasters;
	}

	public List<DimensionMasterBean> prepareDimensionCommonData(List<DimensionMaster> dimensionMasters) {
		List<DimensionMasterBean> dimensionMasterBeans = null;
		if (!Validations.isEmpty(dimensionMasters)) {
			dimensionMasterBeans = new ArrayList<>();
			for (DimensionMaster dimen : dimensionMasters) {

				if (dimen.getIsActive().equals(Boolean.FALSE))
					continue;
				DimensionMasterBean dimensionMasBean = new DimensionMasterBean();

				if (dimen.getAgencyMaster() != null && dimen.getAgencyMaster().getAgencyMasterId() != 0) {
					SdmxAgencyMasterBean sdmxAgencyMasterBean = new SdmxAgencyMasterBean();

					sdmxAgencyMasterBean.setAgencyMasterCode(dimen.getAgencyMaster().getAgencyMasterCode());
					sdmxAgencyMasterBean.setAgencyMasterId(dimen.getAgencyMaster().getAgencyMasterId());
					sdmxAgencyMasterBean.setAgencyMasterLabel(dimen.getAgencyMaster().getAgencyMasterLabel());
					dimensionMasBean.setSdmxAgencyMasterBean(sdmxAgencyMasterBean);
					dimensionMasBean.setAgencyMasterCode(dimen.getAgencyMaster().getAgencyMasterCode());
				}

				dimensionMasBean.setDimensionCode(dimen.getDimensionCode());
				dimensionMasBean.setDimensionDesc(dimen.getDimDesc());
				dimensionMasBean.setDimensionId(dimen.getDimesnsionMasterId());
				dimensionMasBean.setDimensionName(dimen.getDimensionName());
				dimensionMasBean.setDimensionType(dimen.getDimensionType().getDimesnsionTypeName());
				dimensionMasBean.setDimensionTypeId(dimen.getDimensionType().getDimesnionTypeId());
				dimensionMasBean.setConceptVersion(dimen.getConceptVersion());
				if (dimen.getCodeListMaster() != null) {
					CodeListMasterBean codeListMasterBean = new CodeListMasterBean();
					codeListMasterBean.setClCode(dimen.getCodeListMaster().getClCode());
					codeListMasterBean.setClLable(dimen.getCodeListMaster().getClLable());
					codeListMasterBean.setClVersion(dimen.getCodeListMaster().getClVersion());
					dimensionMasBean.setCodeListMasterBean(codeListMasterBean);
				}

				if (dimen.getRegex() != null) {
					RegexBean regExBean = new RegexBean();
					regExBean.setRegex(dimen.getRegex().getRegex());
					regExBean.setRegexName(dimen.getRegex().getRegexName());
					regExBean.setRegexId(dimen.getRegex().getRegexId());
					dimensionMasBean.setRegEx(regExBean);
				}

				dimensionMasBean.setMinLength(dimen.getMinLength());
				dimensionMasBean.setMaxLength(dimen.getMaxLength());
				dimensionMasBean.setIsCommon(dimen.getIsCommon());
				dimensionMasBean.setIsMandatory(dimen.getIsMandatory());
				dimensionMasBean.setDataType(dimen.getDataType());

				dimensionMasterBeans.add(dimensionMasBean);
			}
		}
		return dimensionMasterBeans;
	}

	public List<DimensionMasterBean> searchDimensionCode(List<String> dimCodeList) {
		StringBuilder sb = new StringBuilder();

		sb.append("select ele.ELEMENT_ID, ele.DSD_CODE, ele.ELEMENT_LABEL, ele.ELEMENT_VER, " + " eleDim.ELEMENT_DIMENSIONS , ele.ELEMENT_DESC ,agm.AGENCY_MASTER_CODE " + " from TBL_SDMX_ELEMENT_DIMENSIONS eleDim, " + " TBL_SDMX_ELEMENT ele , TBL_SDMX_AGENCY_MASTER agm  where ele.ELEMENT_ID = eleDim.ELEMENT_ID_FK and ele.AGENCY_MASTER_ID_FK = agm.SDMX_AGENCY_MASTER_ID and agm.IS_ACTIVE = 1 and eleDim.IS_ACTIVE = 1 and ele.IS_ACTIVE = 1 and");
		int i = 0;
		for (String dimCode : dimCodeList) {
			String dimCodeArray[] = dimCode.split("~~");
			if (i > 0) {
				sb.append("or eleDim.ELEMENT_DIMENSIONS -> '$.dimCombination[*].dimConceptId' like '%" + dimCodeArray[0] + "%'"

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																				+ " and eleDim.ELEMENT_DIMENSIONS -> '$.dimCombination[*].conceptVersion' like '%"
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																				+ dimCodeArray[2]
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																				+ "%' ");
			} else {
				sb.append("(eleDim.ELEMENT_DIMENSIONS -> '$.dimCombination[*].dimConceptId' like '%" + dimCodeArray[0] + "%' " + " and eleDim.ELEMENT_DIMENSIONS -> '$.dimCombination[*].conceptVersion' like '%" + dimCodeArray[2] + "%' ");
			}
			i++;
		}
		if (i > 0) {
			sb.append(")");
		}

		List<Tuple> elementDimensions = entityManager.createNativeQuery(sb.toString(), Tuple.class).getResultList();

		String jsonString = "";
		Long count = null;
		Long closeDimenCount = null;
		Long commonDimensionCount = null;
		Long openDimenCount = null;

		Map<String, Set<String>> dimensionElementMap = new HashMap<>();

		for (String dimCode : dimCodeList) {
			String dimCodeArray[] = dimCode.split("~~");
			dimensionElementMap.put(dimCodeArray[0], new HashSet<>());

			for (Tuple tuple : elementDimensions) {
				if (tuple.get(4) != null && tuple.get(1) != null && tuple.get(3) != null) {
					jsonString = tuple.get(4).toString();

					ElementDimensionStoredJson elementDimensionStoredJson = JsonUtility.getGsonObject().fromJson(jsonString, ElementDimensionStoredJson.class);

					count = elementDimensionStoredJson.getDimCombination().stream().filter(f -> f.getDimConceptId().equalsIgnoreCase(dimCodeArray[0])).count();

					if (count > 0) {
						dimensionElementMap.get(dimCodeArray[0]).add(tuple.get(1).toString() + "~" + tuple.get(3).toString() + "~" + tuple.get(2).toString() + "~" + tuple.get(5).toString() + "~" + tuple.get(6).toString());
					}
				}
			}
		}

		sb = new StringBuilder();

		sb.append("select cod.ELEMENT_ID_FK, info.RETURN_CELL_REF, sheetInfo.RETURN_SHEET_INFO_ID, " + " ret.RETURN_CODE, ret.RETURN_NAME, cod.MODEL_DIM , sheetInfo.SHEET_NAME , sheetInfo.SECTION_NAME , agm1.AGENCY_MASTER_CODE , temp.VERSION_NUMBER , srp.EBR_VERSION " + " from TBL_SDMX_MODEL_CODES cod, TBL_SDMX_RETURN_MODEL_INFO info, " + " TBL_SDMX_RETURN_SHEET_INFO sheetInfo,  " + " TBL_RETURN_TEMPLATE temp, TBL_RETURN ret , TBL_SDMX_AGENCY_MASTER agm1 , TBL_SDMX_ELEMENT tse , TBL_SDMX_RETURN_PREVIEW srp where " +

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		" cod.MODEL_CODES_ID =  info.MODEL_CODES_ID_FK "
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		+ " and sheetInfo.RETURN_PREVIEW_ID_FK = srp.RETURN_PREVIEW_TYPE_ID  "
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		+ " and info.RETURN_SHEET_INFO_ID_FK = sheetInfo.RETURN_SHEET_INFO_ID "
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		+ " and srp.RETURN_TEMPLATE_ID_FK = temp.RETURN_TEMPLATE_ID "
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		+ " and temp.RETURN_ID_FK = ret.RETURN_ID and  cod.IS_ACTIVE = 1 "
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		+ " and tse.ELEMENT_ID = cod.ELEMENT_ID_FK "
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		+ " and agm1.SDMX_AGENCY_MASTER_ID = tse.AGENCY_MASTER_ID_FK  "
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		+ " and info.IS_ACTIVE = 1 and ret.IS_ACTIVE = 1 and temp.IS_ACTIVE = 1 and (");

		i = 0;
		for (String dimCode : dimCodeList) {
			String dimCodeArray[] = dimCode.split("~~");
			if (i != 0) {
				sb.append("or");
			}
			sb.append("(");
			sb.append("cod.MODEL_DIM -> '$.closedDim[*].dimConceptId' like '%" + dimCodeArray[0] + "%' or ");
			sb.append("cod.MODEL_DIM -> '$.openDimension[*].dimConceptId' like '%" + dimCodeArray[0] + "%' or ");
			sb.append("cod.MODEL_DIM -> '$.commonDimension[*].dimConceptId' like '%" + dimCodeArray[0] + "%' ");
			sb.append(")");
			i++;
		}
		sb.append(") order by ret.RETURN_CODE , sheetInfo.SECTION_NAME ");

		List<Tuple> modelMap = entityManager.createNativeQuery(sb.toString(), Tuple.class).getResultList();
		Map<String, List<String>> retunCodeCellReference = new HashMap<>();

		Map<String, List<ReturnDto>> dimensionModelMap = new HashMap<>();

		for (String dimCode : dimCodeList) {
			String dimCodeArray[] = dimCode.split("~~");
			for (Tuple tuple : modelMap) {
				if (tuple.get(5) != null && tuple.get(1) != null && tuple.get(3) != null && tuple.get(4) != null) {
					jsonString = tuple.get(5).toString();

					DimensionDetailCategories dimensionDetailCategory = JsonUtility.getGsonObject().fromJson(jsonString, DimensionDetailCategories.class);

					if (dimCodeArray[0] != null) {

						openDimenCount = ((null != dimensionDetailCategory.getOpenDimension()) ? dimensionDetailCategory.getOpenDimension().stream().filter(f -> f.getDimConceptId().equalsIgnoreCase(dimCodeArray[0])).count() : 0);
						closeDimenCount = ((null != dimensionDetailCategory.getClosedDim()) ? dimensionDetailCategory.getClosedDim().stream().filter(f -> f.getDimConceptId().equalsIgnoreCase(dimCodeArray[0])).count() : 0);
						commonDimensionCount = ((null != dimensionDetailCategory.getCommonDimension()) ? dimensionDetailCategory.getCommonDimension().stream().filter(f -> f.getDimConceptId().equalsIgnoreCase(dimCodeArray[0])).count() : 0);

					}

					if (openDimenCount > 0 || closeDimenCount > 0 || commonDimensionCount > 0) {
						ReturnDto returnDto = new ReturnDto();
						String tempTableName = tuple.get(7).toString();

						returnDto.setReturnCode(tuple.get(3) != null ? tuple.get(3).toString() : "");
						returnDto.setReturnName(tuple.get(4) != null ? tuple.get(4).toString() : "");
						returnDto.setSheetName(tuple.get(6) != null ? tuple.get(6).toString() : "");
						returnDto.setTableName(tuple.get(7) != null ? tuple.get(7).toString() : "");
						returnDto.setAgencyMasterCode(tuple.get(8) != null ? tuple.get(8).toString() : "");
						returnDto.setTemplateVersion(tuple.get(9) != null ? tuple.get(9).toString() : "");
						returnDto.setEbrVersion(tuple.get(10) != null ? tuple.get(10).toString() : "");

						List<String> cellRefList = new ArrayList<>();
						cellRefList.add(tuple.get(1).toString());

						returnDto.setCelReferences(cellRefList);

						if (dimensionModelMap.get(dimCodeArray[0]) != null) {

							List<ReturnDto> returnDtos = dimensionModelMap.get(dimCodeArray[0]);
							ReturnDto finalReturnDto = null;
							if (!Validations.isEmpty(returnDtos)) {
								List<ReturnDto> finalReturnDtoList = new ArrayList<>();
								for (ReturnDto reDto : returnDtos) {
									if (reDto.getReturnCode().equals(tuple.get(3).toString())) {
										finalReturnDtoList.add(reDto);
									}
								}

								if (finalReturnDtoList.size() > 0) {
									finalReturnDto = finalReturnDtoList.get(finalReturnDtoList.size() - 1);
								}
							}
							//ReturnDto filteredReturnDto = returnDtos.stream().filter(f -> f.getReturnCode().equalsIgnoreCase(tuple.get(3).toString())).findAny().orElse(null);

							if (finalReturnDto != null && finalReturnDto.getTableName().equals(tempTableName)) {
								finalReturnDto.getCelReferences().add(tuple.get(1).toString());
							} else {
								List<ReturnDto> returnDtoList = dimensionModelMap.get(dimCodeArray[0]);
								brealDimList: for (String dimCode1 : dimCodeList) {
									returnDtoList.add(returnDto);
									if (dimCode1.equals(dimCodeArray[0] + "~~" + returnDto.getAgencyMasterCode())) {
										dimensionModelMap.put(dimCodeArray[0], returnDtoList);
										break brealDimList;
									}
								}

							}
						} else {
							List<ReturnDto> returnDtoList = new ArrayList<>();
							brealDimList: for (String dimCode1 : dimCodeList) {
								returnDtoList.add(returnDto);
								if (dimCode1.equals(dimCodeArray[0] + "~~" + returnDto.getAgencyMasterCode())) {
									dimensionModelMap.put(dimCodeArray[0], returnDtoList);
									break brealDimList;
								}
							}
						}
					} else {
						dimensionModelMap.put(dimCodeArray[0], null);
					}
				}
			}
		}

		List<DimensionMasterBean> dimensionMasterBeans = new ArrayList<>();

		for (Map.Entry<String, Set<String>> obj : dimensionElementMap.entrySet()) {
			DimensionMasterBean dimensionMasterBean = new DimensionMasterBean();
			dimensionMasterBean.setDimensionCode(obj.getKey().split("~~")[0]);

			List<SdmxElementBean> sdmxElementsBeans = new ArrayList<>();

			for (String str : obj.getValue()) {
				SdmxElementBean sdmxElementBean = new SdmxElementBean();
				sdmxElementBean.setDsdCode(str.split("~")[0]);
				sdmxElementBean.setElementVer(str.split("~")[1]);
				sdmxElementBean.setElementLabel(str.split("~")[2]);
				sdmxElementBean.setElementDesc(str.split("~")[3]);
				sdmxElementBean.setAgencyMasterCode(str.split("~")[4]);

				brealDimList: for (String dimCode : dimCodeList) {
					String dimCodeArray[] = dimCode.split("~~");
					String comparingStr = dimCodeArray[0] + "~~" + dimCodeArray[1];
					if (comparingStr.equals(obj.getKey() + "~~" + str.split("~")[4])) {
						sdmxElementsBeans.add(sdmxElementBean);
						break brealDimList;

					}
				}

			}
			if (!CollectionUtils.isEmpty(sdmxElementsBeans)) {
				dimensionMasterBean.setElements(sdmxElementsBeans);
			}

			dimensionMasterBeans.add(dimensionMasterBean);
		}

		for (DimensionMasterBean dimensionMasterBean : dimensionMasterBeans) {
			if (dimensionModelMap.get(dimensionMasterBean.getDimensionCode()) != null) {
				dimensionMasterBean.setModelMapping(dimensionModelMap.get(dimensionMasterBean.getDimensionCode()));
			}
		}

		return dimensionMasterBeans;
	}

	public List<DimensionMaster> getByIsActiveAndConceptVersion(boolean isActive, String conceptVersion, String agencyCode) {
		List<DimensionMaster> dimensionMasterList = new ArrayList<>();
		dimensionMasterList = dimensionRepo.getByIsActiveAndConceptVersion(isActive, conceptVersion, agencyCode);
		return dimensionMasterList;
	}
}
