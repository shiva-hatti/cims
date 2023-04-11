package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.Menu;
import com.iris.model.MenuRoleMap;
import com.iris.repository.MenuRoleMapRepo;
import com.iris.service.GenericService;

@Service
public class MenuRoleMapService implements GenericService<MenuRoleMap, Long> {

	@Autowired
	MenuRoleMapRepo repo;

	public List<MenuRoleMap> findByRole(Long rollId) {
		return repo.findByUserRoleIdFkUserRoleIdAndIsActiveTrueOrderByMenuIDFkParentMenuMenuIdAsc(rollId);
	}

	@Override
	public MenuRoleMap add(MenuRoleMap entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(MenuRoleMap entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<MenuRoleMap> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuRoleMap getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MenuRoleMap> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MenuRoleMap> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MenuRoleMap> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MenuRoleMap> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MenuRoleMap> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(MenuRoleMap bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	public List<Menu> getMenuForEntity() {
		return repo.getMenuForEntity();
	}

	public List<Menu> getMenuForAuditor() {
		return repo.getMenuForAuditor();
	}

	public List<Menu> getMenuFromJsonIds(List<Long> menuIds) {
		return repo.getMenuFromJsonIds(menuIds);
	}

	public List<Menu> getMenuForDept() {
		return repo.getMenuForDept();
	}

	public List<MenuRoleMap> getMenuForAllRoles(List<Long> userRoleIds) {
		return repo.getMenuForAllRoles(userRoleIds);
	}
}
