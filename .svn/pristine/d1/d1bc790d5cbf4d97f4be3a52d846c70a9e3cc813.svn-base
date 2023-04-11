package com.iris.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.iris.dto.CategoryDto;
import com.iris.dto.EntityDto;
import com.iris.dto.RegulatorDto;
import com.iris.dto.SubCategoryDto;
import com.iris.dto.UserDetailsDto;
import com.iris.dto.UserDto;
import com.iris.dto.UserRoleDto;
import com.iris.exception.ServiceException;
import com.iris.model.EntityBean;
import com.iris.model.UserMaster;
import com.iris.model.UserRoleMaster;
import com.iris.repository.UserMasterRepo;
import com.iris.service.GenericService;
import com.iris.util.JsonUtility;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;

@Service
public class UserMasterService implements GenericService<UserMaster, Long> {

	@Autowired
	UserMasterRepo userMasterRepo;

	@Autowired
	private EntityManager em;

	@Override
	public UserMaster add(UserMaster entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(UserMaster entity) throws ServiceException {
		return false;
	}

	@Override
	public List<UserMaster> getDataByIds(Long[] ids) throws ServiceException {
		try {
			return userMasterRepo.findByUserIdIn(ids);
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}

	}

	@Override
	public List<UserMaster> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		try {
			List<String> emailIds = null;
			List<String> userNameList = null;

			for (String columnName : columnValueMap.keySet()) {
				if (columnValueMap.get(columnName) != null && columnValueMap.get(columnName).size() > 0) {
					if (columnName.equalsIgnoreCase(ColumnConstants.EMAIL_ID.getConstantVal())) {
						emailIds = columnValueMap.get(columnName);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.USER_NAME.getConstantVal())) {
						userNameList = columnValueMap.get(columnName);
					}
				}
			}
			if (methodName.equalsIgnoreCase(MethodConstants.GET_USER_DATA_BY_EMAIL_ID.getConstantVal())) {
				return userMasterRepo.getDataByPrimaryEmailInAndIsActiveTrue(emailIds);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_USER_DATA_BY_USER_NAME.getConstantVal())) {
				return userMasterRepo.getDataByUserName(userNameList);
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<UserMaster> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return Arrays.asList(userMasterRepo.findByUserIdAndIsActiveTrue(id));
	}

	@Override
	public List<UserMaster> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(UserMaster bean) throws ServiceException {

	}

	@Override
	public List<UserMaster> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public UserMaster getDataById(Long id) throws ServiceException {
		try {
			return userMasterRepo.findByUserIdAndIsActiveTrue(id);
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	/**
	 * @param userId
	 * @return
	 */
	public String getUserNameByUserId(Long userId) {
		try {
			return userMasterRepo.getUserNameByUserId(userId);
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<UserMaster> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		try {
			List<Long> userIdList = null;
			List<Boolean> isActiveList = null;

			for (String columnName : columnValueMap.keySet()) {
				if (columnValueMap.get(columnName) != null) {
					if (columnName.equalsIgnoreCase(ColumnConstants.USER_ID.getConstantVal())) {
						userIdList = (List<Long>) columnValueMap.get(columnName);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.IS_ACTIVE.getConstantVal())) {
						isActiveList = (List<Boolean>) columnValueMap.get(columnName);
					}
				}
			}
			if (methodName.equalsIgnoreCase(MethodConstants.GET_USER_DATA_BY_USER_ID_ACITVE_FLAG.getConstantVal())) {
				return userMasterRepo.findByUserIdInAndIsActiveIn(userIdList, isActiveList);
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	public EntityBean getEntityOfEntityRoleType(Long userId, Long roleId, UserMaster userObj) {

		if (userObj == null) {
			return null;
		}

		if (userObj.getRoleType().getRoleTypeId().equals(GeneralConstants.ENTITY_ROLE_TYPE_ID.getConstantLongVal())) {
			UserRoleMaster userRoleMaster = userObj.getUsrRoleMstrSet().stream().filter(userRole -> userRole.getUserRole().getUserRoleId().equals(roleId)).findAny().orElse(null);
			if (userRoleMaster != null) {
				if (!CollectionUtils.isEmpty(userRoleMaster.getUserEntityRole())) {
					// Entity user always mapped to only one entity..hence getting first record from
					return userRoleMaster.getUserEntityRole().iterator().next().getEntityBean();
				}
			}
		}

		return null;
	}

	public UserDto getLightWeightUserDto(Long userId) {
		return userMasterRepo.getLighWeightUserDto(userId);
	}

	public UserDetailsDto getUserWithEntityDetails(UserDto userDto) {
		UserDetailsDto userDetailsDto = getUserDetails(userDto);
		String sql = "";
		if (userDetailsDto.getRoleTypeId().equals(2)) {
			sql = getEntityDetailsForEntityUser(userDetailsDto);
		} else if (userDetailsDto.getRoleTypeId().equals(1)) {
			sql = getEntityDetailsForRegulatorUser(userDetailsDto);
		}

		Query query = em.createNativeQuery(sql, Tuple.class);

		List<Tuple> result = query.getResultList();

		for (Tuple tuple : result) {
			EntityDto entityDto = getEntityDto(tuple);
			if (userDetailsDto.getEntityDtos() != null) {
				userDetailsDto.getEntityDtos().add(entityDto);
			} else {
				List<EntityDto> entityDtos = new ArrayList<>();
				entityDtos.add(entityDto);
				userDetailsDto.setEntityDtos(entityDtos);
			}
		}

		return userDetailsDto;
	}

	private EntityDto getEntityDto(Tuple tuple) {
		EntityDto entityDto = new EntityDto();
		entityDto.setEntityId(Long.parseLong(tuple.get(6) + ""));
		entityDto.setEntityCode(tuple.get(7) + "");
		entityDto.setIfscCode(tuple.get(8) + "");
		entityDto.setEntityName(tuple.get(9) + "");

		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setCategoryId(Integer.parseInt(tuple.get(10) + ""));
		categoryDto.setCategoryName(tuple.get(11) + "");
		categoryDto.setCategoryCode(tuple.get(12) + "");

		SubCategoryDto subCategoryDto = new SubCategoryDto();
		subCategoryDto.setSubCategoryId(Integer.parseInt(tuple.get(13) + ""));
		subCategoryDto.setSubCategoryName(tuple.get(14) + "");
		subCategoryDto.setSubCategoryCode(tuple.get(15) + "");

		entityDto.setCategoryDto(categoryDto);
		entityDto.setSubCategoryDto(subCategoryDto);

		return entityDto;
	}

	private String getEntityDetailsForEntityUser(UserDetailsDto userDetailsDto) {
		String sql = "select distinct usr.USER_ID, usr.USER_NAME, usr.FIRST_NAME, usr.LAST_NAME,  usr.ROLE_TYPE_FK, " + "\"ENTITY_USER\", ent.ENTITY_ID , ent.ENTITY_CODE , ent.IFSC_CODE , entLabel.ENTITY_NAME_LABEL ,  " + " cat.CATEGORY_ID, catLabel.CATEGORY_LABEL , cat.CATEGORY_CODE , subCat.SUB_CATEGORY_ID, subCatLabel.SUB_CATEGORY_LABEL ," + " subCat.SUB_CATEGORY_CODE from TBL_USER_MASTER usr, TBL_USER_ROLE_MASTER roleMas," + " TBL_USER_ROLE rle, TBL_USER_ROLE_LABEL roleLabel, TBL_USER_ENTITY_ROLE entRole, TBL_ENTITY ent," + " TBL_ENTITY_LABEL entLabel,  TBL_CATEGORY cat, TBL_CATEGORY_LABEL catLabel, TBL_SUB_CATEGORY subCat, " + "TBL_SUB_CATEGORY_LABEL subCatLabel where usr.USER_ID =  " + userDetailsDto.getUserId() + " and usr.IS_ACTIVE = 1 and roleMas.IS_ACTIVE = 1" + " and rle.IS_ACTIVE = 1 and entRole.IS_ACTIVE = 1 and ent.IS_ACTIVE = 1 and entLabel.LANGUAGE_ID_FK = 15 " + "and cat.IS_ACTIVE = 1 and subCat.IS_ACTIVE = 1 and roleLabel.LANG_ID_FK = 15 and catLabel.LANG_ID_FK = 15 " + " and subCatLabel.LANG_ID_FK = 15 and usr.USER_ID = roleMas.USER_MASTER_ID_FK and " + "roleMas.USER_ROLE_ID_FK = rle.USER_ROLE_ID and rle.USER_ROLE_ID = roleLabel.USER_ROLE_ID_FK" + " and entRole.USER_ROLE_MASTER_ID_FK = roleMas.USER_ROLE_MASTER_ID and ent.ENTITY_ID = entRole.ENTITY_ID_FK " + "and ent.ENTITY_ID = entLabel.ENTITY_ID_FK and ent.CATEGORY_ID_FK = cat.CATEGORY_ID and catLabel.CATEGORY_ID_FK = cat.CATEGORY_ID " + "and ent.SUB_CATEGORY_ID_FK = subCat.SUB_CATEGORY_ID and subCatLabel.SUB_CATEGORY_ID_FK = subCat.SUB_CATEGORY_ID ";
		return sql;
	}

	private String getEntityDetailsForRegulatorUser(UserDetailsDto userDetailsDto) {
		String sql = "select usr.USER_ID, usr.USER_NAME, usr.FIRST_NAME, usr.LAST_NAME,  usr.ROLE_TYPE_FK, regLabel.REGULATOR_LABEL, ent.ENTITY_ID , " + "ent.ENTITY_CODE , ent.IFSC_CODE , entLabel.ENTITY_NAME_LABEL ,  cat.CATEGORY_ID, catLabel.CATEGORY_LABEL , cat.CATEGORY_CODE , subCat.SUB_CATEGORY_ID, subCatLabel.SUB_CATEGORY_LABEL , " + "subCat.SUB_CATEGORY_CODE from TBL_USER_MASTER usr, TBL_DEPT_USER_ENTITY_MAPPING map, TBL_ENTITY ent, TBL_ENTITY_LABEL entLabel,  TBL_CATEGORY cat, " + "  TBL_CATEGORY_LABEL catLabel, TBL_SUB_CATEGORY subCat, TBL_SUB_CATEGORY_LABEL subCatLabel, TBL_REGULATOR reg, TBL_REGULATOR_LABEL regLabel where usr.USER_ID =  " + userDetailsDto.getUserId() + "" + "  and usr.IS_ACTIVE = 1 and ent.IS_ACTIVE = 1 and map.IS_ACTIVE = 1 and entLabel.LANGUAGE_ID_FK = 15 " + "and cat.IS_ACTIVE = 1 and subCat.IS_ACTIVE = 1 and catLabel.LANG_ID_FK = 15  and subCatLabel.LANG_ID_FK = 15 and regLabel.LANGUAGE_ID_FK = 15 " + " and reg.IS_ACTIVE = 1 and ent.ENTITY_ID = entLabel.ENTITY_ID_FK and ent.CATEGORY_ID_FK = cat.CATEGORY_ID and catLabel.CATEGORY_ID_FK = cat.CATEGORY_ID " + " and ent.SUB_CATEGORY_ID_FK = subCat.SUB_CATEGORY_ID and subCatLabel.SUB_CATEGORY_ID_FK = subCat.SUB_CATEGORY_ID  and map.USER_ID_FK = usr.USER_ID " + " and map.ENTITY_ID_FK = ent.ENTITY_ID " + " and usr.DEPARTMENT_ID_FK = reg.REGULATOR_ID and usr.DEPARTMENT_ID_FK = regLabel.REGULATOR_ID_FK";
		return sql;
	}

	public UserDetailsDto getUserDetails(UserDto userDto) {

		String sql = "SELECT mas.userId, roleType.roleTypeId, userRole.userRoleId, userRoleLabel.userRoleLabel, mas.departmentIdFk.regulatorId " + " " + " FROM UserMaster mas left join RoleType roleType  on mas.roleType.roleTypeId = roleType.roleTypeId " + "left join" + " UserRoleMaster roleMas on roleMas.userMaster.userId = mas.userId left join UserRole userRole " + "on userRole.userRoleId =  roleMas.userRole.userRoleId left join UserRoleLabel userRoleLabel " + "on userRoleLabel.userRoleIdFk.userRoleId =  userRole.userRoleId where mas.userId =:userId" + " and roleType.isActive = 1  and roleMas.isActive = 1 and userRole.isActive = 1 " + "and mas.isActive = 1 and userRoleLabel.langIdFk = 15";

		Query query = em.createQuery(sql, Tuple.class);
		query.setParameter("userId", userDto.getUserId());
		//		List<Long> roleIds = new ArrayList<>();
		//		if(userDto.getRoleId()!= null) {
		//			roleIds.add(userDto.getRoleId());
		//		}else {
		//			roleIds.addAll(userDto.getRoleIds());
		//		}
		//		
		//		query.setParameter("userRoleIds", roleIds);

		List<Tuple> result = query.getResultList();

		UserDetailsDto userDetailsDto = null;

		for (Tuple tuple : result) {
			if (userDetailsDto != null) {
				UserRoleDto userRoleDto = getUserRoleDtoObject(tuple);
				if (userRoleDto != null) {
					if (userDetailsDto.getUserRoleDtos() != null) {
						userDetailsDto.getUserRoleDtos().add(userRoleDto);
					} else {
						List<UserRoleDto> userRoleDtos = new ArrayList<>();
						userRoleDtos.add(userRoleDto);
					}
				}
			} else {
				userDetailsDto = new UserDetailsDto();

				if (tuple.get(0) != null) {
					userDetailsDto.setUserId(Long.parseLong(tuple.get(0) + ""));
				}

				if (tuple.get(1) != null) {
					userDetailsDto.setRoleTypeId(Integer.parseInt(tuple.get(1) + ""));
				}

				if (tuple.get(4) != null) {
					RegulatorDto regulatorDto = new RegulatorDto();
					regulatorDto.setRegulatorId(Integer.parseInt(tuple.get(4) + ""));
					userDetailsDto.setRegulatorDto(regulatorDto);
				}

				UserRoleDto userRoleDto = getUserRoleDtoObject(tuple);
				if (userRoleDto != null) {
					List<UserRoleDto> userRoleDtos = new ArrayList<>();
					userRoleDtos.add(userRoleDto);
				}
			}
		}

		return userDetailsDto;
	}

	private UserRoleDto getUserRoleDtoObject(Tuple tuple) {

		if (tuple.get(2) != null) {
			UserRoleDto userRoleDto = new UserRoleDto();
			userRoleDto.setUserRoleId(Long.parseLong(tuple.get(2) + ""));

			if (tuple.get(3) != null) {
				userRoleDto.setRoleDesc(tuple.get(3) + "");
			}

			return userRoleDto;
		}

		return null;
	}

	/**
	 * @param roleTypeId
	 * @param userIdsSet
	 * @return
	 */
	public Boolean isRegulatorForRolesCreatedByUsers(Long roleTypeId, Set<Long> userIdsSet) {
		Boolean isRegulatorUserCreatedRole = false;
		userIdsSet = userMasterRepo.isRegulatorForRolesCreatedByUsers(roleTypeId, userIdsSet);
		if (!CollectionUtils.isEmpty(userIdsSet)) {
			isRegulatorUserCreatedRole = true;
		}
		return isRegulatorUserCreatedRole;

	}

}
