/**
 * 
 */
package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.UserMaster;
import com.iris.service.GenericService;
import com.iris.repository.UserAuthorizationRepo;

/**
 * @author sajadhav
 *
 */
@Service
public class UserAuthorizationService implements GenericService<UserMaster, Long> {

	@Autowired
	private UserAuthorizationRepo userAuthorizationRepo;

	public List<UserMaster> getDataByRefId(long userId) {
		// TODO Auto-generated method stub
		return userAuthorizationRepo.getDataByRefId(userId);
	}

	@Override
	public UserMaster add(UserMaster entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(UserMaster entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<UserMaster> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserMaster getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserMaster> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserMaster> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserMaster> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserMaster> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserMaster> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(UserMaster bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
