package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.Menu;
import com.iris.model.UserRoleActivityMap;
import com.iris.model.WorkFlowActivity;
import com.iris.repository.UserRoleActivityMapRepo;
import com.iris.service.GenericService;

@Service
public class UserRoleActivityMapService implements GenericService<UserRoleActivityMap, Long> {
	@Autowired
	UserRoleActivityMapRepo repo;

	@Override
	public UserRoleActivityMap add(UserRoleActivityMap entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(UserRoleActivityMap entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<UserRoleActivityMap> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserRoleActivityMap getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserRoleActivityMap> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserRoleActivityMap> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserRoleActivityMap> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserRoleActivityMap> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserRoleActivityMap> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(UserRoleActivityMap bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	public List<UserRoleActivityMap> findByRoleUserRoleId(Long roleId) {
		return repo.findByRoleUserRoleIdAndIsActiveTrue(roleId);
	}

	public void cancelUserRoleActivityMapping(Long roleId) {
		repo.cancelUserRoleActivityMapping(roleId);
	}

	public List<WorkFlowActivity> getActivityFromJsonIds(List<Long> activityIds) {
		return repo.getActivityFromJsonIds(activityIds);
	}
}
