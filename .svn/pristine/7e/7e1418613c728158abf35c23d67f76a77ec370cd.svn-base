package com.iris.service.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.UserRoleMaster;
import com.iris.repository.UserRoleMasterRepo;
import com.iris.service.GenericService;
import com.iris.util.Validations;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;

@Service
public class UserRoleMasterService implements GenericService<UserRoleMaster, Long> {

	@Autowired
	UserRoleMasterRepo userRoleMasterRepo;

	@Autowired
	DataSource datasource;

	@Override
	public UserRoleMaster add(UserRoleMaster userEntityMap) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(UserRoleMaster userEntityMap) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<UserRoleMaster> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserRoleMaster> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<UserRoleMaster> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserRoleMaster> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(UserRoleMaster userRoleMaster) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<UserRoleMaster> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		try {
			List<Long> roleIds = null;

			for (String columnName : columnValueMap.keySet()) {
				if (columnValueMap.get(columnName) != null && columnValueMap.get(columnName).size() > 0) {
					if (columnName.equalsIgnoreCase("roleId")) {
						roleIds = columnValueMap.get(columnName);
					}
				}
			}
			if (methodName.equalsIgnoreCase("getUserDataByRoleIds")) {
				return userRoleMasterRepo.findByUserRoleIds(roleIds);
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public UserRoleMaster getDataById(Long id) throws ServiceException {
		Optional<UserRoleMaster> userRoleMasterOptional = userRoleMasterRepo.findById(id);
		if (userRoleMasterOptional.isPresent()) {
			return userRoleMasterOptional.get();
		} else {
			return null;
		}
	}

	public List<UserRoleMaster> findByUserMasterUserId(Long userId) {
		return userRoleMasterRepo.findByUserMasterUserId(userId);

	}

	@Override
	public List<UserRoleMaster> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		try {
			if (methodName.equalsIgnoreCase("findByUserRoleIdInAndUserIdIn")) {
				Long roleId = (Long) columnValueMap.get(ColumnConstants.ROLEID.getConstantVal());
				Long userId = (Long) columnValueMap.get(ColumnConstants.USER_ID.getConstantVal());
				return userRoleMasterRepo.findByUserRoleIdInAndUserIdIn(roleId, userId);
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	public boolean updateUserRoleMaster(Long id, Long[] userRoleMstId) throws SQLException {
		try (Connection con = datasource.getConnection(); CallableStatement stmt = con.prepareCall(GeneralConstants.SP_UPDATE_USER_ROLE_MASTER.getConstantVal());) {

			String array = null;
			if (userRoleMstId != null) {
				array = Validations.convertArrayToString(userRoleMstId);
			}

			stmt.setLong(1, id);
			stmt.setString(2, array);

			stmt.executeQuery();

			return true;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}

	}

}
