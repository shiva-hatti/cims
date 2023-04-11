package com.iris.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.RoleType;
import com.iris.repository.RoleTypeRepo;
import com.iris.service.GenericService;

@Service
public class RoleTypeService implements GenericService<RoleType, Long> {

	@Autowired
	RoleTypeRepo repo;

	@Autowired
	EntityManager em;

	@Override
	public RoleType add(RoleType entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(RoleType entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<RoleType> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RoleType getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		Optional<RoleType> roleTypeOptional = repo.findById(id);
		if (roleTypeOptional.isPresent()) {
			return roleTypeOptional.get();
		} else {
			return null;
		}
	}

	@Override
	public List<RoleType> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RoleType> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RoleType> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RoleType> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RoleType> getAllDataFor(Class bean, Long id) throws ServiceException {
		return repo.findAll();
	}

	public List<RoleType> findByIsActiveTrue() {
		return repo.findByIsActiveTrue();
	}

	@Override
	public void deleteData(RoleType bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	public List<RoleType> findByUserId(Long userId) {

		return repo.findByUserId(userId);
	}

}
