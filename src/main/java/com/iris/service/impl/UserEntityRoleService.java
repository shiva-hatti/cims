package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.UserEntityRole;
import com.iris.repository.UserEntityRoleRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

@Service
public class UserEntityRoleService implements GenericService<UserEntityRole, Long> {

	@Autowired
	private UserEntityRoleRepo userEntityRoleRepo;

	@Override
	public UserEntityRole add(UserEntityRole entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(UserEntityRole entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<UserEntityRole> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserEntityRole> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		try {
			List<String> emailIds = null;
			List<String> entityCodeList = null;

			for (String columnName : columnValueMap.keySet()) {
				if (columnValueMap.get(columnName) != null && columnValueMap.get(columnName).size() > 0) {
					if (columnName.equalsIgnoreCase(ColumnConstants.EMAIL_ID.getConstantVal())) {
						emailIds = columnValueMap.get(columnName);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.ENTITY_CODE.getConstantVal())) {
						entityCodeList = columnValueMap.get(columnName);
					}
				}
			}
			if (methodName.equalsIgnoreCase(MethodConstants.GET_USER_DATA_BY_EMAIL_ID.getConstantVal())) {
				return userEntityRoleRepo.getDataByPrimaryEmailInAndIsActiveTrue(emailIds, entityCodeList);
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}

	}

	@Override
	public List<UserEntityRole> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<UserEntityRole> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserEntityRole> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(UserEntityRole bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public UserEntityRole getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserEntityRole> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		try {

			String entityCode = null;
			Long entityId = null;
			Long roleMasterId = null;
			for (String columnName : columnValueMap.keySet()) {
				if (columnName.equalsIgnoreCase(ColumnConstants.ENT_CODE.getConstantVal())) {
					entityCode = (String) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.ENTITYID.getConstantVal())) {
					entityId = (Long) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.USER_ROLE_MASTER_ID.getConstantVal())) {
					roleMasterId = (Long) columnValueMap.get(columnName);
				}
			}

			if (methodName.equalsIgnoreCase("getUserEntityRoleByUserEntityRoleId")) {
				Long userRoleMasterId = (Long) columnValueMap.get(ColumnConstants.USER_ROLE_MASTER_ID.getConstantVal());
				return userEntityRoleRepo.getUserEntityRoleByUserRoleMasterId(userRoleMasterId);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_ENTITY_ROLE_DATA.getConstantVal())) {
				return userEntityRoleRepo.getEntityByEntityCode(entityCode);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_DATA_BY_ENTITY_ID_AND_ROLE_MASTER_ID.getConstantVal())) {
				return userEntityRoleRepo.getEntityByEntityIdAndUserRoleMasterId(entityId, roleMasterId);
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	public List<UserEntityRole> getActiveEntities(List<Long> activeEntityBeanList) {
		try {
			return userEntityRoleRepo.getActiveEntities(activeEntityBeanList);
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}
}
