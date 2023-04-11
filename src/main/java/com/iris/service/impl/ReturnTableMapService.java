package com.iris.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.ReturnTableMap;
import com.iris.repository.ReturnTableMapRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ErrorConstants;

@Service
public class ReturnTableMapService implements GenericService<ReturnTableMap, Long> {

	@Autowired
	ReturnTableMapRepo returnTableMapRepo;

	@Override
	public ReturnTableMap add(ReturnTableMap returnTableMap) throws ServiceException {
		try {
			return returnTableMapRepo.save(returnTableMap);
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public boolean update(ReturnTableMap entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ReturnTableMap> getDataByIds(Long[] ids) throws ServiceException {

		return returnTableMapRepo.findByIdAndIsActiveTrue(ids);
	}

	@Override
	public ReturnTableMap getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReturnTableMap> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReturnTableMap> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReturnTableMap> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReturnTableMap> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReturnTableMap> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(ReturnTableMap bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	public Set<ReturnTableMap> getListOfTableCodes(String returnCode, Boolean isActive) {
		return returnTableMapRepo.getListOfTableCodes(returnCode, isActive);
	}

}
