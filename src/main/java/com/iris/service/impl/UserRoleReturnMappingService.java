package com.iris.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.iris.dto.LoggedInUserDeptReturnDto;
import com.iris.dto.OwnerReturn;
import com.iris.dto.ReturnByRoleInputDto;
import com.iris.dto.ReturnByRoleOutputDto;
import com.iris.dto.ReturnEntityMapDto;
import com.iris.dto.ReturnEntityQueryOutputDto;
import com.iris.dto.UserDetailsDto;
import com.iris.exception.ServiceException;
import com.iris.model.EntityBean;
import com.iris.model.Return;
import com.iris.model.UserRoleEntityMapping;
import com.iris.model.UserRoleReturnMapping;
import com.iris.repository.UserRoleEntityMappingRepo;
import com.iris.repository.UserRoleReturnMappingRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author Siddique
 */
@Service
public class UserRoleReturnMappingService implements GenericService<UserRoleReturnMapping, Long> {

	@Autowired
	private UserRoleReturnMappingRepo userRoleReturnMappingRepo;

	@Autowired
	private UserRoleEntityMappingRepo userRoleEntitynMappingRepo;

	@Autowired
	private EntityManager entityManager;

	@Override
	public UserRoleReturnMapping add(UserRoleReturnMapping entity) throws ServiceException {
		return userRoleReturnMappingRepo.save(entity);
	}

	@Override
	public boolean update(UserRoleReturnMapping entity) throws ServiceException {
		return false;
	}

	@Override
	public List<UserRoleReturnMapping> getDataByIds(Long[] ids) throws ServiceException {
		return userRoleReturnMappingRepo.getReturnListByRoleId(ids);
	}

	@Override
	public UserRoleReturnMapping getDataById(Long id) throws ServiceException {
		return userRoleReturnMappingRepo.getDataByRoleReturnId(id);
	}

	@Override
	public List<UserRoleReturnMapping> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<UserRoleReturnMapping> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	public List<UserRoleReturnMapping> findByRoleIdFkUserRoleIdAndIsActiveTrue(Long userRoleId) {
		return userRoleReturnMappingRepo.findByRoleIdFkUserRoleIdAndIsActiveTrue(userRoleId);
	}

	@Override
	public List<UserRoleReturnMapping> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		try {
			Long roleId = null;
			Boolean isActive = false;
			String entCode = null;
			Long returnId = null;

			for (String columnName : columnValueMap.keySet()) {
				if (columnName.equalsIgnoreCase(ColumnConstants.ROLEID.getConstantVal())) {
					roleId = (Long) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.IS_ACTIVE.getConstantVal())) {
					isActive = (Boolean) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.ENT_CODE.getConstantVal())) {
					entCode = (String) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.RETURNID.getConstantVal())) {
					returnId = (Long) columnValueMap.get(columnName);
				}
			}
			if (methodName.equalsIgnoreCase(MethodConstants.GET_USER_ROLE_RETURN_DATA_BY_ROLE_ID_ACTIVE_FLAG.getConstantVal())) {
				return userRoleReturnMappingRepo.getUserRoleReturnDataByRoleIdAndActiveFlag(roleId, isActive);
			} else if (methodName.equalsIgnoreCase("getUserRoleReturnMappingByUserId")) {
				return userRoleReturnMappingRepo.findByRoleIdFkUserRoleIdAndIsActiveTrue(roleId);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_USER_ROLE_RETURN_DATA_BY_ROLE_ID.getConstantVal())) {
				return userRoleReturnMappingRepo.getUserRoleReturnMappingByRoleId(roleId);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_USER_ROLE_RETURN_MAPP_DATA_BY_ROLE_ID.getConstantVal())) {
				return userRoleReturnMappingRepo.findByRoleIdFkUserRoleId(roleId);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_UNMAPPED_RETURN_LIST_BY_REGULATOR_ID.getConstantVal())) {
				if (isActive == null) {
					return userRoleReturnMappingRepo.getUnMappedReturnListByRegulatorId(roleId);
				} else {
					return userRoleReturnMappingRepo.getUnMappedReturnListByRegulatorIdAndIsActive(roleId, isActive);
				}
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_RETURN_LIST_BY_ROLE_ID.getConstantVal())) {
				return userRoleReturnMappingRepo.getReturnDataByRoleId(roleId, entCode);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_USER_ROLE_RETURN_MAPPING_BY_RETURN_ID.getConstantVal())) {
				return userRoleReturnMappingRepo.getUserRolesOnReturnId(returnId);
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<UserRoleReturnMapping> getActiveDataFor(Class bean, Long id) throws ServiceException {
		List<UserRoleReturnMapping> userRoleReturnMappingList = null;
		try {
			if (bean.isInstance(UserRoleReturnMapping.class) && id == null) {
				userRoleReturnMappingList = userRoleReturnMappingRepo.getAllActiveData();
			}
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return userRoleReturnMappingList;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<UserRoleReturnMapping> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(UserRoleReturnMapping bean) throws ServiceException {
	}

	public Map<Long, Map<Return, Boolean>> getRoleRetunMap(Long[] ids) {
		try {
			List<UserRoleReturnMapping> userRoleReturnMappings = userRoleReturnMappingRepo.getLightWeightUserRoleReturnByRoleId(ids);
			if (!CollectionUtils.isEmpty(userRoleReturnMappings)) {
				Map<Long, Map<Return, Boolean>> userRoleReturnMap = new HashMap<>();
				userRoleReturnMappings.forEach(f -> {
					if (userRoleReturnMap.get(f.getRoleIdFk().getUserRoleId()) != null) {
						userRoleReturnMap.get(f.getRoleIdFk().getUserRoleId());
						userRoleReturnMap.get(f.getRoleIdFk().getUserRoleId()).put(f.getReturnIdFk(), f.getIsActive());
					} else {
						Map<Return, Boolean> returnMap = new HashMap<>();
						returnMap.put(f.getReturnIdFk(), f.getIsActive());
						userRoleReturnMap.put(f.getRoleIdFk().getUserRoleId(), returnMap);
					}
				});
				return userRoleReturnMap;
			}
			return new HashMap<>();
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	public Map<Long, Map<EntityBean, Boolean>> getRoleEntityMap(Long[] roleIds) {
		try {
			List<UserRoleEntityMapping> userRoleEntityMappings = userRoleEntitynMappingRepo.getLightWeightUserRoleEntityByRoleId(roleIds);
			if (!CollectionUtils.isEmpty(userRoleEntityMappings)) {
				Map<Long, Map<EntityBean, Boolean>> userRoleEntityMap = new HashMap<>();
				userRoleEntityMappings.forEach(f -> {
					if (userRoleEntityMap.get(f.getUserRole().getUserRoleId()) != null) {
						userRoleEntityMap.get(f.getUserRole().getUserRoleId());
						userRoleEntityMap.get(f.getUserRole().getUserRoleId()).put(f.getEntity(), f.isActive());
					} else {
						Map<EntityBean, Boolean> entityMap = new HashMap<>();
						entityMap.put(f.getEntity(), f.isActive());
						userRoleEntityMap.put(f.getUserRole().getUserRoleId(), entityMap);
					}
				});
				return userRoleEntityMap;
			}
			return new HashMap<>();
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	/**
	 * @param roleIdList
	 * @return
	 */
	public List<ReturnEntityQueryOutputDto> fetchReturnByRoleIds(List<Long> roleIdList, UserDetailsDto userDetailsDto, ReturnEntityMapDto returnChannelMapReqDto, List<String> entityCodeList) {
		String returnQuerySql = "SELECT new com.iris.dto.ReturnEntityQueryOutputDto(urrm.returnIdFk.returnId,urrm.returnIdFk.returnCode, " + "returnLabel.returnLabel, " + "remn.entity.entityCode, remn.entity.entityName, " + "urrm.returnIdFk.frequency.frequencyCode, urrm.returnIdFk.frequency.frequencyName, fieldKeyLabel.fieldKeyLable, " + "remn.returnObj.returnGroupMapIdFk.returnGroupMapId,rglm.groupLabel, " + "remn.entity.category.categoryCode, remn.entity.category.categoryName," + "remn.entity.subCategory.subCategoryCode, remn.entity.subCategory.subCategoryName, remn.uploadChannel, remn.webChannel, remn.emailChannel, remn.apiChannel, remn.stsChannel, returnRegMapping.regulatorIdFk.regulatorId, returnRegMapping.regulatorIdFk.regulatorCode, returnRegMapping.regulatorIdFk.regulatorName, urrm.returnIdFk.isNonXbrl, urrm.returnIdFk.isApplicableForDynamicWebForm) " + "FROM UserRoleReturnMapping urrm, ReturnEntityMappingNew remn, " + "ReturnLabel returnLabel, ReturnGroupLabelMapping rglm , ReturnRegulatorMapping returnRegMapping, FieldKey fieldKey, FieldKeyLabel fieldKeyLabel " + "WHERE urrm.roleIdFk.userRoleId IN(:roleIdList) " + "and urrm.returnIdFk.returnId = remn.returnObj.returnId " + " and urrm.isActive=:isActive " + "and remn.returnObj.returnId=returnLabel.returnIdFk.returnId " + "and returnLabel.langIdFk.languageCode=:langCode " + "and remn.isActive=:isActive " + "and remn.returnObj.isActive=:isActive " + "and remn.returnObj.returnGroupMapIdFk.isActive=:isActive " + "and remn.returnObj.frequency.isActive=:isActive " + "and remn.returnObj.returnGroupMapIdFk.returnGroupMapId=rglm.returnGroupMapIdFk.returnGroupMapId " + "and rglm.langIdFk.languageCode=:langCode and returnRegMapping.returnIdFk.returnId = remn.returnObj.returnId " + " and returnRegMapping.isActive =:isActive and returnRegMapping.regulatorIdFk.isActive =:isActive";
		returnQuerySql = returnQuerySql + " and urrm.returnIdFk.frequency.frequencyName = fieldKey.fieldKey " + " and fieldKey.fieldId = fieldKeyLabel.fieldIdFk.fieldId " + " and fieldKeyLabel.languageIdFk.languageCode=:langCode ";

		returnQuerySql = returnQuerySql + " and remn.entity.entityCode IN(:entityCodeList) ";

		if (returnChannelMapReqDto.getIsChanelToConsider() != null && returnChannelMapReqDto.getIsChanelToConsider()) {

			// Upload Channel
			if (returnChannelMapReqDto.getUploadChannel() != null && returnChannelMapReqDto.getUploadChannel()) {
				returnQuerySql = returnQuerySql + " and remn.uploadChannel=true ";
			}

			// Web Channel
			if (returnChannelMapReqDto.getWebChannel() != null && returnChannelMapReqDto.getWebChannel()) {
				returnQuerySql = returnQuerySql + " and remn.webChannel=true ";
			}

			// Email Channel
			if (returnChannelMapReqDto.getEmailChannel() != null && returnChannelMapReqDto.getEmailChannel()) {
				returnQuerySql = returnQuerySql + " and remn.emailChannel=true ";
			}

			// Api Channel
			if (returnChannelMapReqDto.getApiChannel() != null && returnChannelMapReqDto.getApiChannel()) {
				returnQuerySql = returnQuerySql + " and remn.apiChannel=true ";
			}

			// Sts Channel
			if (returnChannelMapReqDto.getStsChannel() != null && returnChannelMapReqDto.getStsChannel()) {
				returnQuerySql = returnQuerySql + " and remn.stsChannel=true ";
			}
		}

		returnQuerySql = returnQuerySql + " ORDER BY returnLabel.returnLabel ASC";

		Query query = entityManager.createQuery(returnQuerySql, ReturnEntityQueryOutputDto.class);
		query.setParameter("roleIdList", roleIdList);
		query.setParameter("entityCodeList", entityCodeList);
		query.setParameter("langCode", returnChannelMapReqDto.getLangCode());
		query.setParameter("isActive", returnChannelMapReqDto.getIsActive());
		return query.getResultList();
	}

	/**
	 * @param roleIdList
	 * @return
	 */
	public List<ReturnByRoleOutputDto> fetchReturnByRoleId(List<Long> roleIdList, ReturnByRoleInputDto returnByRoleInputDto) {
		String returnQuerySql = "SELECT DISTINCT new com.iris.dto.ReturnByRoleOutputDto(urrm.returnIdFk.returnCode, " + "returnLabel.returnLabel, " + "urrm.returnIdFk.frequency.frequencyCode, urrm.returnIdFk.frequency.frequencyName, fieldKeyLabel.fieldKeyLable, " + "rglm.returnGroupLabelMapId, rglm.groupLabel, " + "returnRegMapping.regulatorIdFk.regulatorCode, returnRegMapping.regulatorIdFk.regulatorName, urrm.returnIdFk.isNonXbrl, urrm.returnIdFk.isApplicableForDynamicWebForm) " + "FROM UserRoleReturnMapping urrm, " + "ReturnLabel returnLabel, ReturnGroupLabelMapping rglm , ReturnRegulatorMapping returnRegMapping, " + " FieldKey fieldKey, FieldKeyLabel fieldKeyLabel " + "WHERE urrm.roleIdFk.userRoleId IN(:roleIdList) " + " and rglm.langIdFk.languageCode=:langCode " + " and returnLabel.langIdFk.languageCode=:langCode " + " and urrm.isActive=:isActive " + " and urrm.returnIdFk.isActive=:isActive " + " and urrm.returnIdFk.frequency.isActive =: isActive " + " and urrm.returnIdFk.returnId = returnLabel.returnIdFk.returnId" + " and urrm.returnIdFk.returnId = returnRegMapping.returnIdFk.returnId" + " and urrm.returnIdFk.returnGroupMapIdFk.returnGroupMapId=rglm.returnGroupMapIdFk.returnGroupMapId " + " and rglm.returnGroupMapIdFk.isActive =:isActive " + " and urrm.returnIdFk.frequency.frequencyName = fieldKey.fieldKey " + " and fieldKey.fieldId = fieldKeyLabel.fieldIdFk.fieldId " + " and fieldKeyLabel.languageIdFk.languageCode=:langCode "

																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		+ " and returnRegMapping.isActive =:isActive and returnRegMapping.regulatorIdFk.isActive =:isActive ";

		returnQuerySql = returnQuerySql + " ORDER BY returnLabel.returnLabel ASC";

		Query query = entityManager.createQuery(returnQuerySql, ReturnByRoleOutputDto.class);
		query.setParameter("roleIdList", roleIdList);
		query.setParameter("langCode", returnByRoleInputDto.getLangCode());
		query.setParameter("isActive", returnByRoleInputDto.getIsActive());
		return query.getResultList();
	}

	/**
	 * @param roleIdList
	 * @return
	 */
	public List<LoggedInUserDeptReturnDto> fetchReturnOwnedByLoggedInUserDepartment(ReturnByRoleInputDto returnByRoleInputDto) {
		String returnQuerySql = "SELECT DISTINCT new com.iris.dto.LoggedInUserDeptReturnDto(ret.returnId, ret.returnCode, retlabel.returnLabel, " + " ret.frequency.frequencyCode, ret.frequency.frequencyName, grpMap.returnGroupMapId, grpLabelMap.groupLabel, " + " reg.regulatorCode, reg.regulatorName) FROM Return ret, " + " ReturnLabel retlabel, Regulator reg, ReturnRegulatorMapping map, ReturnGroupMapping grpMap, ReturnGroupLabelMapping  grpLabelMap, " + " UserMaster mas WHERE" + " mas.userId =:userId and ret.isActive =:isActive and reg.isActive =:isActive and map.isActive =:isActive " + " and grpMap.isActive =:isActive and mas.isActive =:isActive " + " and mas.departmentIdFk.regulatorId = reg.regulatorId" + " and retlabel.langIdFk.languageCode =:langCode and grpLabelMap.langIdFk.languageCode =:langCode" + " and retlabel.returnIdFk.returnId = ret.returnId and ret.returnId = map.returnIdFk.returnId " + " and reg.regulatorId = map.regulatorIdFk.regulatorId" + " and grpMap.returnGroupMapId = ret.returnGroupMapIdFk.returnGroupMapId " + " and grpLabelMap.returnGroupMapIdFk.returnGroupMapId = grpMap.returnGroupMapId ORDER BY retlabel.returnLabel ASC ";

		Query query = entityManager.createQuery(returnQuerySql, LoggedInUserDeptReturnDto.class);
		query.setParameter("langCode", returnByRoleInputDto.getLangCode());
		query.setParameter("isActive", returnByRoleInputDto.getIsActive());
		query.setParameter("userId", returnByRoleInputDto.getUserId());
		return query.getResultList();
	}

	/**
	 * @param roleIdSet
	 * @param isActive
	 * @return
	 */
	public Set<OwnerReturn> getUserRoleReturnDataByRoleIdAndActiveFlag(Set<Long> roleIdSet, Boolean isActive) {
		return userRoleReturnMappingRepo.getUserRoleReturnDataByRoleIdAndActiveFlag(roleIdSet, isActive);
	}

}