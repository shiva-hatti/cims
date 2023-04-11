package com.iris.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.Platform;
import com.iris.model.UserRolePlatFormMap;
import com.iris.repository.PortalRepo;
import com.iris.repository.UserRolePortalMapRepo;
import com.iris.service.GenericService;

@Service
public class PortalService implements GenericService<Platform, Long> {

	@Autowired
	private PortalRepo repo;

	@Override
	public Platform add(Platform entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(Platform entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Platform> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Platform getDataById(Long id) throws ServiceException {
		Optional<Platform> platformOptional = repo.findById(id);
		if (platformOptional.isPresent()) {
			return platformOptional.get();
		} else {
			return null;
		}
	}

	@Override
	public List<Platform> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Platform> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Platform> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Platform> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return repo.findByIsActiveTrue();
	}

	@Override
	public List<Platform> getAllDataFor(Class bean, Long id) throws ServiceException {
		return repo.findAll();
	}

	@Override
	public void deleteData(Platform bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	public Platform getDataByCode(String code) {
		return repo.getDataByCode(code);
	}

}
