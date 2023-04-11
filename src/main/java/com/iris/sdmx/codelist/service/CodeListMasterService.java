/**
 * 
 */
package com.iris.sdmx.codelist.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iris.caching.ObjectCache;
import com.iris.dto.ReturnDto;
import com.iris.dto.ServiceResponse;
import com.iris.exception.ApplicationException;
import com.iris.exception.ServiceException;
import com.iris.model.UserMaster;
import com.iris.sdmx.agency.master.entity.AgencyMaster;
import com.iris.sdmx.agency.master.repo.SdmxAgencyMasterRepo;
import com.iris.sdmx.codelist.bean.CodeListDeleteOutputBean;
import com.iris.sdmx.codelist.bean.CodeListMasterBean;
import com.iris.sdmx.codelist.bean.CodeListValuesBean;
import com.iris.sdmx.codelist.bean.CodelListMasterRequestBean;
import com.iris.sdmx.codelist.bean.ModelMappingDto;
import com.iris.sdmx.codelist.entity.CodeListMaster;
import com.iris.sdmx.codelist.entity.CodeListMasterMod;
import com.iris.sdmx.codelist.entity.CodeListValues;
import com.iris.sdmx.codelist.repo.CodeListMasterModRepo;
import com.iris.sdmx.codelist.repo.CodeListMasterRepo;
import com.iris.sdmx.codelist.repo.CodeListValueRepo;
import com.iris.sdmx.dimesnsion.bean.DimensionMasterBean;
import com.iris.sdmx.dimesnsion.entity.DimensionMaster;
import com.iris.sdmx.dimesnsion.repo.DimensionRepo;
import com.iris.sdmx.dimesnsion.service.DimensionService;
import com.iris.sdmx.exceltohtml.repo.SdmxModelCodesRepo;
import com.iris.sdmx.exceltohtml.repo.SdmxReturnModelInfoRepo;
import com.iris.sdmx.fusion.controller.FusionApiController;
import com.iris.sdmx.fusion.service.FusionApiService;
import com.iris.sdmx.status.service.AdminStatusService;
import com.iris.sdmx.util.SDMXConstants;
import com.iris.service.GenericService;
import com.iris.util.JsonUtility;
import com.iris.util.Validations;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorCode;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author sajadhav
 *
 */
@Service
public class CodeListMasterService implements GenericService<CodeListMaster, Long> {

	private static final Logger LOGGER = LogManager.getLogger(CodeListMasterService.class);

	@Autowired
	private CodeListMasterRepo codeListMasterRepo;

	@Autowired
	private CodeListMasterModRepo codeListMasterModRepo;

	@Autowired
	private FusionApiService fusionApiService;

	@Autowired
	private FusionApiController fusionAPIController;

	@Autowired
	private DimensionRepo dimeRepo;

	@Autowired
	private SdmxModelCodesRepo sdmxModelCodesRepo;

	@Autowired
	private CodeListValueRepo codeListValueRepo;

	@Autowired
	private SdmxReturnModelInfoRepo sdmxReturnModelInfoRepo;

	@Autowired
	EntityManager entityManager;

	@Autowired
	private AdminStatusService adminStatusService;

	@Autowired
	private SdmxAgencyMasterRepo sdmxAgencyMasterRepo;

	@Autowired
	private DimensionService dimensionService;

	private static final Object lock1 = new Object();

	private final static String NOT_EXIST = "NOT-EXIST";
	private final static String PENDING = "PENDING";

	@Override
	public CodeListMaster add(CodeListMaster entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(CodeListMaster entity) throws ServiceException {
		return false;
	}

	@Override
	public List<CodeListMaster> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public CodeListMaster getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<CodeListMaster> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<CodeListMaster> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<CodeListMaster> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {

		try {
			String clCode = null;
			String clVersion = null;
			for (String columnName : columnValueMap.keySet()) {
				if (columnValueMap.get(columnName) != null && columnName.equalsIgnoreCase(ColumnConstants.CL_CODE.getConstantVal())) {
					clCode = (String) columnValueMap.get(columnName);
				} else if (columnValueMap.get(columnName) != null && columnName.equalsIgnoreCase(ColumnConstants.CL_VERSION.getConstantVal())) {
					clVersion = (String) columnValueMap.get(columnName);
				}
			}

			if (methodName.equalsIgnoreCase(MethodConstants.GET_CODE_LIST_MASTER_RECORD_BY_CL_CODE_CL_VERSION.getConstantVal())) {
				return Arrays.asList(codeListMasterRepo.findByClCodeIgnoreCaseAndClVersionAndIsActive(clCode, clVersion, true));
			}
		} catch (Exception e) {
			throw new ServiceException("Exception : ", e);
		}
		return null;
	}

	public boolean isCodeListExist(String clCode, String clVersion, String agencyMasterCode, boolean isActive) throws ServiceException {
		try {
			CodeListMaster codeListmaster = codeListMasterRepo.findByCodeVersionAndAgency(clCode, clVersion, agencyMasterCode, true);
			return !ObjectUtils.isEmpty(codeListmaster);
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getErrorMessage(), e);
		}
	}

	@Override
	public List<CodeListMaster> getActiveDataFor(Class bean, Long id) throws ServiceException {
		try {
			if (bean == null && id == null) {
				return codeListMasterRepo.findByIsActiveTrueOrderByClCodeAsc();
			} else {
				return Collections.emptyList();
			}
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getErrorMessage(), e);
		}
	}

	@Override
	public List<CodeListMaster> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(CodeListMaster bean) throws ServiceException {

	}

	@Transactional(rollbackOn = Exception.class)
	public boolean addCodeListMasterData(CodeListMasterBean codeListMasterBean, boolean isApproval) throws ApplicationException {

		CodeListMaster codeListmaster = codeListMasterRepo.findByCodeVersionAndAgency(codeListMasterBean.getClCode(), codeListMasterBean.getClVersion(), codeListMasterBean.getAgencyMasterCode(), true);

		if (codeListmaster != null) {
			throw new ApplicationException(ErrorCode.E1171.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1171.toString()));
		}

		CodeListMasterMod pendingCodeListRecord = codeListMasterModRepo.findByClCodeAndClVersionAndAdminStatusId(codeListMasterBean.getClCode(), codeListMasterBean.getClVersion(), GeneralConstants.PENDING_ADMIN_STATUS_ID.getConstantIntVal());

		if (pendingCodeListRecord != null) {
			throw new ApplicationException(ErrorCode.E0267.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0267.toString()));
		}

		Date createdOn = new Date();
		UserMaster createdBy = new UserMaster();
		createdBy.setUserId(codeListMasterBean.getUserId());

		CodeListMaster codeListMaster = new CodeListMaster();
		codeListMaster.setClCode(codeListMasterBean.getClCode().toUpperCase());
		codeListMaster.setClLable(codeListMasterBean.getClLable());
		codeListMaster.setClVersion(codeListMasterBean.getClVersion());
		codeListMaster.setClDesc(codeListMasterBean.getClDesc());
		codeListMaster.setIsActive(codeListMasterBean.getIsActive());

		if (codeListMasterBean.getAgencyMasterCode() != null) {
			AgencyMaster agencyMaster = sdmxAgencyMasterRepo.findByAgencyCode(codeListMasterBean.getAgencyMasterCode());
			if (agencyMaster != null && agencyMaster.getAgencyMasterId() != 0) {
				AgencyMaster agencyMaster2 = new AgencyMaster();
				agencyMaster2.setAgencyMasterId(agencyMaster.getAgencyMasterId());
				codeListMaster.setAgencyMaster(agencyMaster2);
			}
		}

		List<CodeListValuesBean> codeListValuesBeanList = codeListMasterBean.getCodeListValues();
		codeListValuesBeanList.sort((CodeListValuesBean v1, CodeListValuesBean v2) -> Boolean.compare(v2.getIsParent(), v1.getIsParent()));

		List<CodeListValues> codeListValuesList = new ArrayList<>();

		Map<String, CodeListValues> parentCodeListValueMap = new HashMap<String, CodeListValues>();

		for (CodeListValuesBean codeListValueBean : codeListValuesBeanList) {
			CodeListValues codeListValues = new CodeListValues();
			codeListValues.setClValueCode(codeListValueBean.getClValueCode().toUpperCase());
			codeListValues.setClValueLable(codeListValueBean.getClValueLable());
			codeListValues.setClValueDesc(codeListValueBean.getClValueDesc());
			codeListValues.setIsActive(codeListValueBean.getIsActive());
			codeListValues.setCreatedBy(createdBy);
			codeListValues.setCreatedOn(createdOn);
			codeListValues.setLastUpdatedOn(new Date());

			if (codeListValueBean.getIsParent().equals(Boolean.TRUE)) {
				parentCodeListValueMap.put(codeListValueBean.getClValueCode(), codeListValues);
			}

			codeListValues.setParentCodeListValues(parentCodeListValueMap.get(codeListValueBean.getParentClValueCode()));

			codeListValues.setCodeListMaster(codeListMaster);

			codeListValuesList.add(codeListValues);
		}
		codeListMaster.setCodeListValues(codeListValuesList);

		CodeListMasterMod codeListMasterMod = new CodeListMasterMod();
		codeListMasterMod.setActive(true);

		codeListMasterMod.setClMasterJson(JsonUtility.getGsonObject().toJson(codeListMasterBean));
		codeListMaster.setLastUpdatedOn(createdOn);
		codeListMaster.setCreatedBy(createdBy);
		codeListMaster.setCreatedOn(createdOn);

		codeListMasterMod.setClCode(codeListMasterBean.getClCode().toUpperCase());
		codeListMasterMod.setClVersion(codeListMasterBean.getClVersion());
		codeListMasterMod.setCreatedBy(createdBy);
		codeListMasterMod.setCreatedOn(createdOn);
		codeListMasterMod.setLastUpdatedOn(createdOn);
		codeListMasterMod.setActionId(GeneralConstants.ACTIONID_ADDITION.getConstantIntVal());

		if (isApproval) {
			// TO do : hard coded needs to ve removed
			codeListMasterMod.setAdminStatusId(adminStatusService.findIdByStatusTechCode(SDMXConstants.StatusTechCode.PENDING_FOR_APPROVAL.getCode()).intValue());
		} else {
			codeListMasterMod.setAdminStatusId(adminStatusService.findIdByStatusTechCode(SDMXConstants.StatusTechCode.APPROVED.getCode()).intValue());

		}

		codeListMasterMod = codeListMasterModRepo.save(codeListMasterMod);

		if (!isApproval && codeListMasterMod.getClMasterModId() != null) {
			codeListMaster.setIsPending(Boolean.FALSE);
			codeListMasterRepo.save(codeListMaster);
			if (codeListMaster.getClId() != null) {
				codeListMasterModRepo.updateClIdFk(codeListMasterMod.getClMasterModId(), codeListMaster.getClId());

			}
			//			ServiceResponse serviceResponse = fusionAPIController.submitCodeListData(codeListMasterBean);
			boolean success = fusionApiService.submitCodeListData(codeListMasterBean);

			if (!success) {
				throw new ApplicationException(ErrorCode.E1413.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1413.toString()));
			}
		}
		return true;
	}

	@Transactional(rollbackOn = Exception.class)
	public boolean performActiononOnAddedRecord(CodeListMasterBean codeListMasterBean) throws ApplicationException {
		CodeListMasterMod codeListMasterMod = codeListMasterModRepo.findByClCodeAndClVersionAndAdminStatusId(codeListMasterBean.getClCode(), codeListMasterBean.getClVersion(), 1);

		UserMaster userMaster = new UserMaster();
		userMaster.setUserId(codeListMasterBean.getUserId());

		Date approveRejectedOn = new Date();

		if (codeListMasterMod == null) {
			throw new ApplicationException(ErrorCode.E0660.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString()));
		}

		codeListMasterMod.setComments(codeListMasterBean.getComment());
		codeListMasterMod.setLastApprovedBy(userMaster);
		codeListMasterMod.setLastApprovedOn(approveRejectedOn);
		codeListMasterMod.setLastUpdatedOn(approveRejectedOn);

		if (!codeListMasterBean.getIsApproved().equals(Boolean.TRUE)) {
			codeListMasterMod.setAdminStatusId(GeneralConstants.REJECTED_ADMIN_STATUS_ID.getConstantIntVal());
			codeListMasterModRepo.save(codeListMasterMod);
		} else {
			codeListMasterMod.setAdminStatusId(GeneralConstants.APPROVED_ADMIN_STATUS_ID.getConstantIntVal());

			String jsonString = codeListMasterMod.getClMasterJson();
			Type listToken = new TypeToken<CodeListMasterBean>() {
			}.getType();
			Gson gson = new Gson();
			CodeListMasterBean jsonCodeListMasterBean = gson.fromJson(jsonString, listToken);

			CodeListMaster codeListMaster = new CodeListMaster();
			codeListMaster.setClCode(jsonCodeListMasterBean.getClCode().toUpperCase());
			codeListMaster.setClLable(jsonCodeListMasterBean.getClLable());
			codeListMaster.setClVersion(jsonCodeListMasterBean.getClVersion());
			codeListMaster.setClDesc(jsonCodeListMasterBean.getClDesc());
			codeListMaster.setIsActive(jsonCodeListMasterBean.getIsActive());
			codeListMaster.setLastUpdatedOn(approveRejectedOn);
			codeListMaster.setCreatedBy(userMaster);
			codeListMaster.setCreatedOn(approveRejectedOn);
			codeListMaster.setLastApprovedBy(userMaster);
			codeListMaster.setLastApprovedOn(approveRejectedOn);

			List<CodeListValuesBean> codeListValuesBeanList = jsonCodeListMasterBean.getCodeListValues();
			codeListValuesBeanList.sort((CodeListValuesBean v1, CodeListValuesBean v2) -> Boolean.compare(v2.getIsParent(), v1.getIsParent()));

			List<CodeListValues> codeListValuesList = new ArrayList<>();

			Map<String, CodeListValues> parentCodeListValueMap = new HashMap<String, CodeListValues>();

			for (CodeListValuesBean codeListValueBean : codeListValuesBeanList) {
				CodeListValues codeListValues = new CodeListValues();
				codeListValues.setClValueCode(codeListValueBean.getClValueCode().toUpperCase());
				codeListValues.setClValueLable(codeListValueBean.getClValueLable());
				codeListValues.setClValueDesc(codeListValueBean.getClValueDesc());
				codeListValues.setIsActive(codeListValueBean.getIsActive());
				codeListValues.setCreatedBy(codeListMasterMod.getCreatedBy());
				codeListValues.setCreatedOn(codeListMasterMod.getCreatedOn());
				codeListValues.setLastUpdatedOn(codeListMasterMod.getLastUpdatedOn());

				if (codeListValueBean.getIsParent().equals(Boolean.TRUE)) {
					parentCodeListValueMap.put(codeListValueBean.getClValueCode(), codeListValues);
				}

				codeListValues.setParentCodeListValues(parentCodeListValueMap.get(codeListValueBean.getParentClValueCode()));

				codeListValues.setCodeListMaster(codeListMaster);

				codeListValuesList.add(codeListValues);
			}
			codeListMaster.setCodeListValues(codeListValuesList);

			codeListMasterMod = codeListMasterModRepo.save(codeListMasterMod);

			if (codeListMasterMod.getClMasterModId() != null) {
				codeListMasterRepo.save(codeListMaster);
			}
		}
		return true;
	}

	@Transactional(rollbackOn = Exception.class)
	public boolean editCodeListMasterData(CodeListMasterBean codeListMasterBean, boolean isApprovalRequired) throws ApplicationException {
		CodeListMaster codeListMaster = codeListMasterRepo.findByClId(codeListMasterBean.getClId());

		if (codeListMaster == null) {
			throw new ApplicationException(ErrorCode.E0660.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString()));
		} else {
			CodeListMasterMod pendingCodeListRecord = codeListMasterModRepo.findByClCodeAndClVersionAndAdminStatusId(codeListMasterBean.getClCode(), codeListMasterBean.getClVersion(), GeneralConstants.PENDING_ADMIN_STATUS_ID.getConstantIntVal());

			if (pendingCodeListRecord != null) {
				throw new ApplicationException(ErrorCode.E0267.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0267.toString()));
			}

			Date creationDate = new Date();

			UserMaster createdBy = new UserMaster();
			createdBy.setUserId(codeListMasterBean.getUserId());

			// Create modification object
			CodeListMasterMod codeListMasterMod = new CodeListMasterMod();
			codeListMasterMod.setCodeListMaster(codeListMaster);
			codeListMasterMod.setActive(true);
			codeListMasterBean.setUserId(null);
			codeListMasterBean.setRoleId(null);
			codeListMasterBean.setIsApproved(null);
			codeListMasterBean.setLangCode(null);
			codeListMasterMod.setClMasterJson(JsonUtility.getGsonObject().toJson(codeListMasterBean));
			codeListMasterMod.setCreatedBy(createdBy);
			codeListMasterMod.setCreatedOn(creationDate);
			codeListMasterMod.setLastUpdatedOn(creationDate);
			codeListMasterMod.setActionId(2);
			codeListMasterMod.setClCode(codeListMasterBean.getClCode().toUpperCase());
			codeListMasterMod.setClVersion(codeListMasterBean.getClVersion());

			if (isApprovalRequired) {
				codeListMasterMod.setAdminStatusId(adminStatusService.findIdByStatusTechCode(SDMXConstants.StatusTechCode.PENDING_FOR_APPROVAL.getCode()).intValue());

				codeListMasterRepo.setIsPending(codeListMasterBean.getClId(), Boolean.TRUE);
			} else {
				codeListMasterMod.setAdminStatusId(adminStatusService.findIdByStatusTechCode(SDMXConstants.StatusTechCode.APPROVED.getCode()).intValue());
			}

			codeListMasterMod = codeListMasterModRepo.save(codeListMasterMod);

			if (!isApprovalRequired && codeListMasterMod.getClMasterModId() != null) {
				codeListMaster.setClCode(codeListMasterBean.getClCode().toUpperCase());
				codeListMaster.setClLable(codeListMasterBean.getClLable());
				codeListMaster.setClVersion(codeListMasterBean.getClVersion());
				codeListMaster.setClDesc(codeListMasterBean.getClDesc());
				codeListMaster.setIsActive(codeListMasterBean.getIsActive());
				codeListMaster.setLastUpdatedOn(creationDate);
				codeListMaster.setLastModifiedBy(createdBy);
				codeListMaster.setLastModifiedOn(creationDate);
				codeListMaster.setIsPending(Boolean.FALSE);

				List<CodeListValuesBean> codeListValuesBeanList = codeListMasterBean.getCodeListValues();
				codeListValuesBeanList.sort((CodeListValuesBean v1, CodeListValuesBean v2) -> Boolean.compare(v2.getIsParent(), v1.getIsParent()));

				Map<String, CodeListValues> parentCodeListValueMap = new HashMap<String, CodeListValues>();

				for (CodeListValuesBean codeListValueBean : codeListValuesBeanList) {
					CodeListValues dbCodeListValue = codeListMaster.getCodeListValues().stream().filter(f -> f.getClValueId() != null && f.getClValueId().equals(codeListValueBean.getClValueId())).findAny().orElse(null);

					if (dbCodeListValue != null) {
						dbCodeListValue.setClValueCode(codeListValueBean.getClValueCode().toUpperCase());
						dbCodeListValue.setClValueLable(codeListValueBean.getClValueLable());
						dbCodeListValue.setClValueDesc(codeListValueBean.getClValueDesc());
						dbCodeListValue.setIsActive(codeListValueBean.getIsActive());
						dbCodeListValue.setLastUpdatedOn(new Date());

						if (codeListValueBean.getIsParent().equals(Boolean.TRUE)) {
							parentCodeListValueMap.put(codeListValueBean.getClValueCode(), dbCodeListValue);
						}

						dbCodeListValue.setParentCodeListValues(parentCodeListValueMap.get(codeListValueBean.getParentClValueCode()));
					} else {
						CodeListValues codeListValues = new CodeListValues();
						codeListValues.setClValueCode(codeListValueBean.getClValueCode().toUpperCase());
						codeListValues.setClValueLable(codeListValueBean.getClValueLable());
						codeListValues.setClValueDesc(codeListValueBean.getClValueDesc());
						codeListValues.setIsActive(codeListValueBean.getIsActive());
						codeListValues.setLastUpdatedOn(creationDate);
						codeListValues.setCreatedBy(createdBy);
						codeListValues.setCreatedOn(creationDate);

						if (codeListValueBean.getIsParent().equals(Boolean.TRUE)) {
							parentCodeListValueMap.put(codeListValueBean.getClValueCode(), codeListValues);
						}

						codeListValues.setParentCodeListValues(parentCodeListValueMap.get(codeListValueBean.getParentClValueCode()));

						codeListValues.setCodeListMaster(codeListMaster);

						codeListMaster.getCodeListValues().add(codeListValues);
					}
				}
				codeListMasterRepo.save(codeListMaster);

				ServiceResponse serviceResponse = fusionAPIController.submitCodeListData(codeListMasterBean);

				if (!serviceResponse.isStatus()) {
					throw new ApplicationException(ErrorCode.E1413.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1413.toString()));
				}
			}
			return true;
		}
	}

	@Transactional(rollbackOn = Exception.class)
	public boolean performActionOnEditedData(CodeListMasterBean codeListMasterBean) {
		try {
			CodeListMasterMod codeListMasterMod = codeListMasterModRepo.findByClCodeAndClVersionAndAdminStatusId(codeListMasterBean.getClCode(), codeListMasterBean.getClVersion(), 1);

			UserMaster userMaster = new UserMaster();
			userMaster.setUserId(codeListMasterBean.getUserId());

			Date approveRejectedOn = new Date();

			if (codeListMasterMod == null) {
				throw new ApplicationException(ErrorCode.E0660.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString()));
			}

			codeListMasterMod.setComments(codeListMasterBean.getComment());
			codeListMasterMod.setLastApprovedBy(userMaster);
			codeListMasterMod.setLastApprovedOn(approveRejectedOn);
			codeListMasterMod.setLastUpdatedOn(approveRejectedOn);

			if (!codeListMasterBean.getIsApproved().equals(Boolean.TRUE)) {
				codeListMasterMod.setAdminStatusId(GeneralConstants.REJECTED_ADMIN_STATUS_ID.getConstantIntVal());
				codeListMasterModRepo.save(codeListMasterMod);
			} else {
				codeListMasterMod.setAdminStatusId(GeneralConstants.APPROVED_ADMIN_STATUS_ID.getConstantIntVal());

				String jsonString = codeListMasterMod.getClMasterJson();
				Type listToken = new TypeToken<CodeListMasterBean>() {
				}.getType();
				Gson gson = new Gson();
				CodeListMasterBean jsonCodeListMasterBean = gson.fromJson(jsonString, listToken);

				CodeListMaster codeListMaster = codeListMasterRepo.findByClId(jsonCodeListMasterBean.getClId());
				Date creationDate = new Date();

				UserMaster createdBy = new UserMaster();
				createdBy.setUserId(codeListMasterBean.getUserId());

				if (codeListMaster != null) {
					// Create modification object
					codeListMaster.setClCode(jsonCodeListMasterBean.getClCode().toUpperCase());
					codeListMaster.setClLable(jsonCodeListMasterBean.getClLable());
					codeListMaster.setClVersion(jsonCodeListMasterBean.getClVersion());
					codeListMaster.setClDesc(jsonCodeListMasterBean.getClDesc());
					codeListMaster.setIsActive(jsonCodeListMasterBean.getIsActive());
					codeListMaster.setLastUpdatedOn(creationDate);
					codeListMaster.setLastModifiedBy(createdBy);
					codeListMaster.setLastModifiedOn(creationDate);

					List<CodeListValuesBean> codeListValuesBeanList = jsonCodeListMasterBean.getCodeListValues();
					codeListValuesBeanList.sort((CodeListValuesBean v1, CodeListValuesBean v2) -> Boolean.compare(v2.getIsParent(), v1.getIsParent()));

					Map<String, CodeListValues> parentCodeListValueMap = new HashMap<String, CodeListValues>();

					for (CodeListValuesBean codeListValueBean : codeListValuesBeanList) {
						CodeListValues dbCodeListValue = codeListMaster.getCodeListValues().stream().filter(f -> f.getClValueId().equals(codeListValueBean.getClValueId())).findAny().orElse(null);

						if (dbCodeListValue != null) {
							dbCodeListValue.setClValueCode(codeListValueBean.getClValueCode().toUpperCase());
							dbCodeListValue.setClValueLable(codeListValueBean.getClValueLable());
							dbCodeListValue.setClValueDesc(codeListValueBean.getClValueDesc());
							dbCodeListValue.setIsActive(codeListValueBean.getIsActive());
							dbCodeListValue.setLastUpdatedOn(new Date());

							if (codeListValueBean.getIsParent().equals(Boolean.TRUE)) {
								parentCodeListValueMap.put(codeListValueBean.getClValueCode(), dbCodeListValue);
							}

							dbCodeListValue.setParentCodeListValues(parentCodeListValueMap.get(codeListValueBean.getParentClValueCode()));
						} else {
							CodeListValues codeListValues = new CodeListValues();
							codeListValues.setClValueCode(codeListValueBean.getClValueCode().toUpperCase());
							codeListValues.setClValueLable(codeListValueBean.getClValueLable());
							codeListValues.setClValueDesc(codeListValueBean.getClValueDesc());
							codeListValues.setIsActive(codeListValueBean.getIsActive());
							codeListValues.setLastUpdatedOn(creationDate);
							codeListValues.setCreatedBy(createdBy);
							codeListValues.setCreatedOn(creationDate);

							if (codeListValueBean.getIsParent().equals(Boolean.TRUE)) {
								parentCodeListValueMap.put(codeListValueBean.getClValueCode(), codeListValues);
							}

							codeListValues.setParentCodeListValues(parentCodeListValueMap.get(codeListValueBean.getParentClValueCode()));

							codeListValues.setCodeListMaster(codeListMaster);

							codeListMaster.getCodeListValues().add(codeListValues);
						}
					}

					codeListMasterModRepo.save(codeListMasterMod);
					codeListMasterRepo.save(codeListMaster);
					return true;
				} else {
					throw new ServiceException("Code list not exist");
				}
			}
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getErrorMessage(), e);
		}
		return false;
	}

	@Transactional(rollbackOn = Exception.class)
	public boolean performActionOnDeletedData(CodeListMasterBean codeListMasterBean) {
		try {
			CodeListMasterMod codeListMasterMod = codeListMasterModRepo.findByClCodeAndClVersionAndAdminStatusId(codeListMasterBean.getClCode(), codeListMasterBean.getClVersion(), 1);

			UserMaster userMaster = new UserMaster();
			userMaster.setUserId(codeListMasterBean.getUserId());

			Date approveRejectedOn = new Date();

			if (codeListMasterMod == null) {
				throw new ApplicationException(ErrorCode.E0660.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E0660.toString()));
			}

			codeListMasterMod.setComments(codeListMasterBean.getComment());
			codeListMasterMod.setLastApprovedBy(userMaster);
			codeListMasterMod.setLastApprovedOn(approveRejectedOn);
			codeListMasterMod.setLastUpdatedOn(approveRejectedOn);
			codeListMasterMod.setLastModifiedBy(userMaster);
			codeListMasterMod.setLastModifiedOn(approveRejectedOn);

			if (!codeListMasterBean.getIsApproved().equals(Boolean.TRUE)) {
				codeListMasterMod.setAdminStatusId(GeneralConstants.REJECTED_ADMIN_STATUS_ID.getConstantIntVal());
				codeListMasterModRepo.save(codeListMasterMod);
			} else {
				codeListMasterMod.setAdminStatusId(GeneralConstants.APPROVED_ADMIN_STATUS_ID.getConstantIntVal());

				String jsonString = codeListMasterMod.getClMasterJson();
				Type listToken = new TypeToken<CodeListMasterBean>() {
				}.getType();
				Gson gson = new Gson();
				CodeListMasterBean jsonCodeListMasterBean = gson.fromJson(jsonString, listToken);

				CodeListMaster codeListMaster = codeListMasterRepo.findByClCodeIgnoreCaseAndClVersionAndIsActive(jsonCodeListMasterBean.getClCode(), jsonCodeListMasterBean.getClVersion(), jsonCodeListMasterBean.getIsActive());

				UserMaster createdBy = new UserMaster();
				createdBy.setUserId(codeListMasterBean.getUserId());

				Date creationDate = new Date();

				if (codeListMaster != null) {
					// Create modification object
					codeListMasterMod.setAdminStatusId(2);
					codeListMasterModRepo.save(codeListMasterMod);

					codeListMaster.setIsActive(false);
					codeListMaster.setLastModifiedBy(createdBy);
					codeListMaster.setLastModifiedOn(creationDate);
					codeListMaster.setLastUpdatedOn(creationDate);
					codeListMaster.setLastApprovedBy(createdBy);
					codeListMaster.setLastApprovedOn(creationDate);

					codeListMasterRepo.save(codeListMaster);
					return true;
				} else {
					throw new ApplicationException("EOOO2", "Code List not exist");
				}
			}
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getErrorMessage(), e);
		}
		return false;
	}

	@Transactional(rollbackOn = Exception.class)
	public Map<String, List<String>> deleteCodeListMasterData(CodeListMasterBean inputCodeListMasterBeans, boolean isApprovalRequired) throws ApplicationException, Exception {
		Map<String, List<String>> failedMap = new HashMap<>();

		UserMaster createdBy = new UserMaster();
		createdBy.setUserId(inputCodeListMasterBeans.getUserId());

		List<String> clCodeList = inputCodeListMasterBeans.getCodeListMasterBeans().stream().map(f -> f.getClCode()).collect(Collectors.toList());

		List<CodeListMasterMod> codeListmasterMods = codeListMasterModRepo.findByClCodeInAndAdminStatusId(clCodeList, 1);

		for (CodeListMasterBean codeListMasterBean : inputCodeListMasterBeans.getCodeListMasterBeans()) {
			CodeListMasterMod codeMistMasterMod = codeListmasterMods.stream().filter(f -> f.getClCode().equalsIgnoreCase(codeListMasterBean.getClCode()) && f.getClVersion().equalsIgnoreCase(codeListMasterBean.getClVersion())).findAny().orElse(null);
			if (codeMistMasterMod != null) {
				if (failedMap.containsKey(NOT_EXIST)) {
					List<String> codeList = failedMap.get(NOT_EXIST);
					codeList.add(codeListMasterBean.getClCode());
					failedMap.put(NOT_EXIST, codeList);
				} else {
					failedMap.put(NOT_EXIST, Arrays.asList(codeListMasterBean.getClCode()));
				}
			}
		}

		List<CodeListMaster> codeListMasterList = codeListMasterRepo.findByClCodeIn(clCodeList);
		Map<String, CodeListMaster> codeListMasterMap = new HashMap<>();

		for (CodeListMasterBean codeListMasterBean : inputCodeListMasterBeans.getCodeListMasterBeans()) {
			CodeListMaster codeListMaster = codeListMasterList.stream().filter(f -> f.getClCode().equalsIgnoreCase(codeListMasterBean.getClCode()) && f.getAgencyMaster().getAgencyMasterCode().equals(codeListMasterBean.getAgencyMasterCode()) && f.getClVersion().equalsIgnoreCase(codeListMasterBean.getClVersion()) && f.getIsActive().equals(codeListMasterBean.getIsActive())).findAny().orElse(null);
			if (codeListMaster == null) {
				if (failedMap.containsKey(PENDING)) {
					List<String> codeList = failedMap.get(PENDING);
					codeList.add(codeListMasterBean.getClCode());
					failedMap.put(PENDING, codeList);
				} else {
					failedMap.put(PENDING, Arrays.asList(codeListMasterBean.getClCode()));
				}
			} else {
				codeListMasterMap.put(codeListMasterBean.getClCode() + "~" + codeListMaster.getClVersion() + "~" + codeListMaster.getAgencyMaster().getAgencyMasterCode(), codeListMaster);
			}
		}

		if (failedMap == null || failedMap.size() != 0) {
			return failedMap;
		}

		for (CodeListMasterBean codeListMasterBaean : inputCodeListMasterBeans.getCodeListMasterBeans()) {
			Date creationDate = new Date();

			if (codeListMasterMap.containsKey(codeListMasterBaean.getClCode() + "~" + codeListMasterBaean.getClVersion() + "~" + codeListMasterBaean.getAgencyMasterCode())) {
				CodeListMaster codeListMaster = codeListMasterMap.get(codeListMasterBaean.getClCode() + "~" + codeListMasterBaean.getClVersion() + "~" + codeListMasterBaean.getAgencyMasterCode());
				// Create modification object
				//json to be stored in mod table
				codeListMasterBaean.setClId(codeListMaster.getClId());
				codeListMasterBaean.setClDesc(codeListMaster.getClDesc());
				codeListMasterBaean.setClLable(codeListMaster.getClLable());
				List<CodeListValuesBean> codeListValuesBeanList = new ArrayList<>();
				for (CodeListValues entityObj : codeListMaster.getCodeListValues()) {
					CodeListValuesBean codeListValuesBean = new CodeListValuesBean();
					codeListValuesBean.setClValueId(entityObj.getClValueId());
					codeListValuesBean.setClValueCode(entityObj.getClValueCode());
					codeListValuesBean.setClValueDesc(entityObj.getClValueDesc());
					codeListValuesBean.setClValueLable(entityObj.getClValueLable());
					codeListValuesBean.setIsActive(entityObj.getIsActive());
					if (entityObj.getParentCodeListValues() != null) {
						codeListValuesBean.setParentClValueCode(entityObj.getParentCodeListValues().getClValueCode());
					} else {
						codeListValuesBean.setParentClValueCode("");
					}
					codeListValuesBeanList.add(codeListValuesBean);
				}
				codeListMasterBaean.setCodeListValues(codeListValuesBeanList);
				//json to be stored in mod table
				CodeListMasterMod codeListMasterMod = new CodeListMasterMod();
				codeListMasterMod.setCodeListMaster(codeListMasterMap.get(codeListMasterBaean.getClCode() + "~" + codeListMasterBaean.getClVersion()));
				codeListMasterMod.setActive(true);
				codeListMasterMod.setActive(true);
				codeListMasterBaean.setUserId(null);
				codeListMasterBaean.setRoleId(null);
				codeListMasterBaean.setIsApproved(null);
				codeListMasterBaean.setLangCode(null);
				codeListMasterMod.setClMasterJson(JsonUtility.getGsonObject().toJson(codeListMasterBaean));
				codeListMasterMod.setCreatedBy(createdBy);
				codeListMasterMod.setCreatedOn(creationDate);
				codeListMasterMod.setLastUpdatedOn(creationDate);
				codeListMasterMod.setActionId(4);
				codeListMasterMod.setClCode(codeListMasterBaean.getClCode());
				codeListMasterMod.setClVersion(codeListMasterBaean.getClVersion());
				codeListMasterMod.setCodeListMaster(codeListMaster);
				if (isApprovalRequired) {
					codeListMasterMod.setAdminStatusId(adminStatusService.findIdByStatusTechCode(SDMXConstants.StatusTechCode.PENDING_FOR_APPROVAL.getCode()).intValue());

					codeListMasterRepo.setIsPending(codeListMaster.getClId(), Boolean.TRUE);
				} else {
					codeListMasterMod.setAdminStatusId(adminStatusService.findIdByStatusTechCode(SDMXConstants.StatusTechCode.APPROVED.getCode()).intValue());
				}

				codeListMasterMod = codeListMasterModRepo.save(codeListMasterMod);

				if (!isApprovalRequired && codeListMasterMod.getClMasterModId() != null) {
					codeListMaster.setIsActive(false);
					codeListMaster.setIsPending(Boolean.FALSE);
					codeListMasterRepo.save(codeListMaster);

					ServiceResponse serviceResponse = fusionAPIController.deleteCodeListData(codeListMasterMod.getClCode(), codeListMasterMod.getClVersion(), inputCodeListMasterBeans.getUserId(), codeListMasterBaean.getAgencyMasterCode());

					if (!serviceResponse.isStatus()) {
						throw new ApplicationException(ErrorCode.E1413.toString(), ObjectCache.getErrorCodeKey(ErrorCode.E1413.toString()));
					}
				}
			}
		}
		return failedMap;
	}

	public List<CodeListMaster> prepareCodeListMasterResponseList(List<CodeListMaster> codeListMasterList, boolean fetchClMasterRecord) {

		List<CodeListMaster> codeListMasterReponseList = new ArrayList<>();

		codeListMasterList.forEach(f -> {
			CodeListMaster codeListMaster = new CodeListMaster();
			BeanUtils.copyProperties(f, codeListMaster);

			//			if (f.getCreatedBy() != null) {
			//				UserMaster createdBy = new UserMaster();
			//				createdBy.setUserId(f.getCreatedBy().getUserId());
			//				createdBy.setUserName(f.getCreatedBy().getUserName());
			//				codeListMaster.setCreatedBy(createdBy);
			//			}
			codeListMaster.setCreatedBy(null);

			//			if(f.getCreatedOn()!= null) {
			//				codeListMaster.setCreatedOnInLong(f.getCreatedOn().getTime());
			//				codeListMaster.setCreatedOn(null);
			//			}

			//			if (f.getLastModifiedBy() != null) {
			//				UserMaster lastModifiedBy = new UserMaster();
			//				lastModifiedBy.setUserId(f.getLastModifiedBy().getUserId());
			//				lastModifiedBy.setUserName(f.getLastModifiedBy().getUserName());
			//				codeListMaster.setLastModifiedBy(lastModifiedBy);
			//			}

			codeListMaster.setLastModifiedBy(null);

			//			if(f.getLastModifiedOn()!= null) {
			//				codeListMaster.setLastModifiedOnInLong(f.getLastModifiedOn().getTime());
			//				codeListMaster.setLastModifiedOn(null);
			//			}

			//			if (f.getLastApprovedBy() != null) {
			//				UserMaster lastApprovedBy = new UserMaster();
			//				lastApprovedBy.setUserId(f.getLastApprovedBy().getUserId());
			//				lastApprovedBy.setUserName(f.getLastApprovedBy().getUserName());
			//				codeListMaster.setLastApprovedBy(lastApprovedBy);
			//			}
			codeListMaster.setLastApprovedBy(null);

			//			if(f.getLastApprovedOn()!= null) {
			//				codeListMaster.setLastApprovedOnInLong(f.getLastApprovedOn().getTime());
			//				codeListMaster.setLastApprovedOn(null);
			//			}
			//			
			if (f.getLastUpdatedOn() != null) {
				codeListMaster.setLastUpdatedOnInLong(f.getLastUpdatedOn().getTime());
				codeListMaster.setLastUpdatedOn(null);
			}

			if (f.getAgencyMaster() != null && f.getAgencyMaster().getAgencyMasterId() != 0) {
				AgencyMaster agencyMaster = new AgencyMaster();
				agencyMaster.setAgencyMasterCode(f.getAgencyMaster().getAgencyMasterCode());
				agencyMaster.setAgencyMasterId(f.getAgencyMaster().getAgencyMasterId());
				agencyMaster.setAgencyMasterLabel(f.getAgencyMaster().getAgencyMasterLabel());
				codeListMaster.setAgencyMaster(agencyMaster);
			} else {
				codeListMaster.setAgencyMaster(null);
			}

			List<CodeListValues> codeListValuesList = new ArrayList<>();

			if (!fetchClMasterRecord) {
				f.getCodeListValues().forEach(k -> {
					try {
						if (!k.getIsActive().equals(Boolean.FALSE)) {
							CodeListValues codeListValues = new CodeListValues();
							BeanUtils.copyProperties(k, codeListValues);

							//							if (k.getCreatedBy() != null) {
							//								UserMaster createdBy = new UserMaster();
							//								createdBy.setUserId(k.getCreatedBy().getUserId());
							//								createdBy.setUserName(k.getCreatedBy().getUserName());
							//								codeListValues.setCreatedBy(createdBy);
							//							}
							codeListValues.setCreatedBy(null);

							//							if(k.getCreatedOn()!= null) {
							//								codeListValues.setCreatedOnInLong(k.getCreatedOn().getTime());
							//								codeListValues.setCreatedOn(null);
							//							}

							//							if (k.getLastModifiedBy() != null) {
							//								UserMaster lastModifiedBy = new UserMaster();
							//								lastModifiedBy.setUserId(k.getLastModifiedBy().getUserId());
							//								lastModifiedBy.setUserName(k.getLastModifiedBy().getUserName());
							//								codeListValues.setLastModifiedBy(lastModifiedBy);
							//							}
							codeListValues.setLastModifiedBy(null);

							//							if(k.getLastModifiedOn()!= null) {
							//								codeListValues.setLastModifiedOnInLong(k.getLastModifiedOn().getTime());
							//								codeListValues.setLastModifiedOn(null);
							//							}

							if (k.getLastUpdatedOn() != null) {
								codeListValues.setLastUpdatedOnInLong(k.getLastUpdatedOn().getTime());
								codeListValues.setLastUpdatedOn(null);
							}

							if (k.getParentCodeListValues() != null) {
								CodeListValues parentCodelistValues = new CodeListValues();
								parentCodelistValues.setClValueId(k.getParentCodeListValues().getClValueId());
								parentCodelistValues.setClValueCode(k.getParentCodeListValues().getClValueCode());
								parentCodelistValues.setClValueLable(k.getParentCodeListValues().getClValueLable());
								parentCodelistValues.setIsActive(k.getParentCodeListValues().getIsActive());
								codeListValues.setParentCodeListValues(parentCodelistValues);
							} else {
								codeListValues.setParentCodeListValues(null);
							}

							codeListValues.setCodeListMaster(null);
							codeListValuesList.add(codeListValues);
						}
					} catch (Exception e) {
						LOGGER.error("Exception :", e);
					}

				});

			}

			if (!codeListValuesList.isEmpty()) {
				codeListMaster.setCodeListValues(codeListValuesList);
			} else {
				codeListMaster.setCodeListValues(null);
			}
			codeListMasterReponseList.add(codeListMaster);
		});

		if (!fetchClMasterRecord) {
			return formatCodeListResponseMasterList(codeListMasterReponseList);
		} else {
			return codeListMasterReponseList;
		}
	}

	private List<CodeListMaster> formatCodeListResponseMasterList(List<CodeListMaster> codeListMasterReponseList) {
		synchronized (lock1) {
			List<CodeListMaster> responseList = new ArrayList<>();
			CopyOnWriteArrayList<CodeListValues> serverSideList = null;

			for (CodeListMaster codeListMaster : codeListMasterReponseList) {
				List<CodeListValues> finalList = null;
				CodeListMaster newCodeListMaster = new CodeListMaster();
				newCodeListMaster.setClCode(codeListMaster.getClCode());
				newCodeListMaster.setClLable(codeListMaster.getClLable());
				newCodeListMaster.setClDesc(codeListMaster.getClDesc());
				newCodeListMaster.setClId(codeListMaster.getClId());
				newCodeListMaster.setClVersion(codeListMaster.getClVersion());
				newCodeListMaster.setLastUpdatedOnInLong(codeListMaster.getLastUpdatedOnInLong());

				if (codeListMaster.getLastUpdatedOn() != null) {
					newCodeListMaster.setLastUpdatedOn(codeListMaster.getLastUpdatedOn());
				}

				if (codeListMaster.getAgencyMaster() != null && codeListMaster.getAgencyMaster().getAgencyMasterId() != 0) {
					AgencyMaster agencyMaster = new AgencyMaster();
					agencyMaster.setAgencyMasterCode(codeListMaster.getAgencyMaster().getAgencyMasterCode());
					agencyMaster.setAgencyMasterId(codeListMaster.getAgencyMaster().getAgencyMasterId());
					agencyMaster.setAgencyMasterLabel(codeListMaster.getAgencyMaster().getAgencyMasterLabel());
					newCodeListMaster.setAgencyMaster(agencyMaster);
				} else {
					newCodeListMaster.setAgencyMaster(null);
				}

				if (codeListMaster.getCodeListValues() != null) {
					serverSideList = new CopyOnWriteArrayList<>(codeListMaster.getCodeListValues());
					finalList = serverSideList.stream().filter(f -> f.getParentCodeListValues() == null).collect(Collectors.toList());
					serverSideList.removeAll(finalList);

					for (CodeListValues finalCodeVal : finalList) {
						if (!CollectionUtils.isEmpty(serverSideList)) {
							List<CodeListValues> findCopyEleList = serverSideList.stream().filter(f -> f.getParentCodeListValues().getClValueId().equals(finalCodeVal.getClValueId())).collect(Collectors.toList());
							if (!CollectionUtils.isEmpty(findCopyEleList)) {
								serverSideList.removeAll(findCopyEleList);
								finalCodeVal.setChildCodeListValues(findCopyEleList);
								checkForInnerElement(findCopyEleList, serverSideList);
							}
						}
					}
				}

				newCodeListMaster.setCodeListValues(finalList);
				responseList.add(newCodeListMaster);
			}
			return responseList;
		}
	}

	//	public static void main(String[] args) {
	//		
	//		String str = "{\"clId\":230,\"clCode\":\"CL_PERTY\",\"clLable\":\"Type of perpetrator\",\"clVersion\":\"1.0\",\"clDesc\":\"Type of perpetrator\",\"isActive\":true,\"codeListValues\":[{\"clValueId\":7621,\"clValueCode\":\"N_A\",\"clValueLable\":\"Not applicable\",\"clValueDesc\":\"Not applicable\",\"isActive\":true,\"childCodeListValues\":[]},{\"clValueId\":7930,\"clValueCode\":\"N_A1\",\"clValueLable\":\"N_A1\",\"clValueDesc\":\"Not applicable\",\"isActive\":true,\"parentCodeListValues\":{\"clValueId\":7621,\"clValueCode\":\"N_A\",\"clValueLable\":\"Not applicable\",\"isActive\":true,\"childCodeListValues\":[]},\"childCodeListValues\":[]},{\"clValueId\":7933,\"clValueCode\":\"N_A1-1\",\"clValueLable\":\"N_A1-1\",\"clValueDesc\":\"Not applicable\",\"isActive\":true,\"parentCodeListValues\":{\"clValueId\":7930,\"clValueCode\":\"N_A1\",\"clValueLable\":\"N_A1\",\"isActive\":true,\"childCodeListValues\":[]},\"childCodeListValues\":[]},{\"clValueId\":6305,\"clValueCode\":\"OTH\",\"clValueLable\":\"Others\",\"clValueDesc\":\"Others\",\"isActive\":true,\"childCodeListValues\":[]},{\"clValueId\":7931,\"clValueCode\":\"OTH1\",\"clValueLable\":\"OTH1\",\"clValueDesc\":\"Not applicable\",\"isActive\":true,\"parentCodeListValues\":{\"clValueId\":6305,\"clValueCode\":\"OTH\",\"clValueLable\":\"Others\",\"isActive\":true,\"childCodeListValues\":[]},\"childCodeListValues\":[]},{\"clValueId\":6304,\"clValueCode\":\"PRIPAL\",\"clValueLable\":\"Principal\",\"clValueDesc\":\"Principal\",\"isActive\":true,\"childCodeListValues\":[]},{\"clValueId\":7932,\"clValueCode\":\"PRIPAL1\",\"clValueLable\":\"PRIPAL1\",\"clValueDesc\":\"Not applicable\",\"isActive\":true,\"parentCodeListValues\":{\"clValueId\":6304,\"clValueCode\":\"PRIPAL\",\"clValueLable\":\"Principal\",\"isActive\":true,\"childCodeListValues\":[]},\"childCodeListValues\":[]}],\"lastUpdatedOnInLong\":1595850270000}";
	//		
	//		CodeListMaster codeListMasterBean = JsonUtility.getGsonObject().fromJson(str, CodeListMaster.class);
	//		
	//		CopyOnWriteArrayList<CodeListValues> serverSideList = new CopyOnWriteArrayList<>(codeListMasterBean.getCodeListValues());
	//		
	//		List<CodeListValues> finalList = new ArrayList<>();
	//		
	//		finalList = serverSideList.stream().filter(f -> f.getParentCodeListValues() == null).collect(Collectors.toList());
	//		serverSideList.removeAll(finalList);
	//		
	//		for (CodeListValues finalCodeVal : finalList) {
	//			if(!CollectionUtils.isEmpty(serverSideList)) {
	//				List<CodeListValues> findCopyEleList = serverSideList.stream().filter(f -> f.getParentCodeListValues().getClValueId().equals(finalCodeVal.getClValueId())).collect(Collectors.toList());
	//				if(!CollectionUtils.isEmpty(findCopyEleList)) {
	//					serverSideList.removeAll(findCopyEleList);
	//					finalCodeVal.setChildCodeListValues(findCopyEleList);
	//					checkForInnerElement(findCopyEleList, serverSideList);
	//				}
	//			}
	//		}
	//		
	//		System.out.println(JsonUtility.getGsonObject().toJson(finalList));
	//	}

	private static void checkForInnerElement(List<CodeListValues> findCopyEleList, CopyOnWriteArrayList<CodeListValues> copyList) {
		for (CodeListValues finalCodeVal : findCopyEleList) {
			if (!CollectionUtils.isEmpty(copyList)) {
				List<CodeListValues> eleList = copyList.stream().filter(f -> f.getParentCodeListValues().getClValueId().equals(finalCodeVal.getClValueId())).collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(eleList)) {
					copyList.removeAll(eleList);
					finalCodeVal.setChildCodeListValues(eleList);
					checkForInnerElement(eleList, copyList);
				}
			}
		}
	}

	//This Method Check Multiple Dimension which is Already Mapped with CodeListMaster 
	public List<CodeListMasterBean> isCodeListMappedWithDimension(CodelListMasterRequestBean codeListMasterRequestBean) {
		List<CodeListMasterBean> codeListMasterBeans = null;
		List<DimensionMasterBean> dimensionMasterBeans = null;
		try {
			if (codeListMasterRequestBean != null) {
				codeListMasterBeans = new ArrayList<>();
				for (CodelListMasterRequestBean codelListMasterRequestBean : codeListMasterRequestBean.getCodelListMasterRequestBean()) {
					List<DimensionMaster> dimensionMasters = dimeRepo.checkCodeListMasterMappWithDim(codelListMasterRequestBean.getClCode(), codelListMasterRequestBean.getClVersion(), codelListMasterRequestBean.getAgencyMasterCode());
					CodeListMasterBean codeListMasterBean = null;
					if (!Validations.isEmpty(dimensionMasters)) {
						codeListMasterBean = new CodeListMasterBean();
						dimensionMasterBeans = new ArrayList<>();
						codeListMasterBean.setClCode(dimensionMasters.get(0).getCodeListMaster().getClCode());
						codeListMasterBean.setClVersion(dimensionMasters.get(0).getCodeListMaster().getClVersion());
						codeListMasterBean.setClLable(dimensionMasters.get(0).getCodeListMaster().getClLable());
						for (DimensionMaster dimensionMaster : dimensionMasters) {
							DimensionMasterBean dimensionMasterBean = new DimensionMasterBean();
							dimensionMasterBean.setDimensionCode(dimensionMaster.getDimensionCode());
							dimensionMasterBean.setDimensionName(dimensionMaster.getDimensionName());
							dimensionMasterBean.setDimensionDesc(dimensionMaster.getDimDesc());
							dimensionMasterBean.setConceptVersion(dimensionMaster.getConceptVersion());
							dimensionMasterBeans.add(dimensionMasterBean);
						}
						codeListMasterBean.setDimensionMasterBeans(dimensionMasterBeans);
					}

					if (codeListMasterBean != null) {
						codeListMasterBeans.add(codeListMasterBean);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getErrorMessage(), e);
		}

		if (codeListMasterBeans != null && codeListMasterBeans.size() > 0) {
			return codeListMasterBeans;
		} else {
			return null;
		}

	}

	//This Method Check Multiple Dimension which is Already Mapped with CodeListMaster 
	public List<CodeListDeleteOutputBean> isCodeListValuesMappedWithDimension(CodelListMasterRequestBean codeListMasterRequestBean) {
		List<CodeListDeleteOutputBean> codeListDeleteOutputBeans = null;
		try {
			CodeListDeleteOutputBean obj3 = new CodeListDeleteOutputBean();
			List<String> dimComboObjList = new ArrayList<>();
			CodeListMaster codeListMaster = codeListMasterRepo.findByCodeVersionAndAgency(codeListMasterRequestBean.getClCode(), codeListMasterRequestBean.getClVersion(), codeListMasterRequestBean.getAgencyMasterCode(), true);

			if (codeListMaster == null) {
				return codeListDeleteOutputBeans;
			}

			List<DimensionMaster> dimensionMasters = dimeRepo.getAllDimensionUsingClIDAndAgencyCode(codeListMaster, codeListMasterRequestBean.getAgencyMasterCode());

			if (CollectionUtils.isEmpty(dimensionMasters)) {
				return codeListDeleteOutputBeans;
			}

			for (DimensionMaster obj : dimensionMasters) {
				dimComboObjList.add(obj.getDimensionCode() + "~~" + codeListMasterRequestBean.getAgencyMasterCode() + "~~" + obj.getConceptVersion());
			}

			List<DimensionMasterBean> dimensionMasterBeans = dimensionService.searchDimensionCode(dimComboObjList);
			if (dimensionMasterBeans.get(0).getModelMapping() == null || CollectionUtils.isEmpty(dimensionMasterBeans)) {
				return codeListDeleteOutputBeans;
			} else {
				codeListDeleteOutputBeans = new ArrayList<>();
			}

			List<ModelMappingDto> modelMappingDtos = new ArrayList<>();
			for (DimensionMasterBean dimObj : dimensionMasterBeans) {
				List<ReturnDto> returnDtos = dimObj.getModelMapping();
				for (ReturnDto returnDto : returnDtos) {
					ModelMappingDto modelMappingDto = new ModelMappingDto();
					modelMappingDto.setReturnCode(returnDto.getReturnCode());
					modelMappingDto.setReturnName(returnDto.getReturnName());
					modelMappingDto.setSheetName(returnDto.getSheetName());
					modelMappingDto.setTableName(returnDto.getTableName());
					modelMappingDto.setTemplateVersion(returnDto.getTemplateVersion());
					modelMappingDto.setEbrVersion(returnDto.getEbrVersion());
					modelMappingDto.setCellNoData(returnDto.getCelReferences());
					modelMappingDtos.add(modelMappingDto);
				}
			}
			obj3.setModelMapping(modelMappingDtos);
			codeListDeleteOutputBeans.add(obj3);
			return codeListDeleteOutputBeans;
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getErrorMessage(), e);
		}
		return codeListDeleteOutputBeans;

	}

}
