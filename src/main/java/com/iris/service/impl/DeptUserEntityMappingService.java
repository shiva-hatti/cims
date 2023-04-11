package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.DeptUserEntityMapping;
import com.iris.repository.DeptUserEntityMappingRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

@Service
public class DeptUserEntityMappingService implements GenericService<DeptUserEntityMapping, Long> {

	@Autowired
	private DeptUserEntityMappingRepo DeptUserEntityMapping2Repo;

	@Override
	public DeptUserEntityMapping add(DeptUserEntityMapping entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(DeptUserEntityMapping entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<DeptUserEntityMapping> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeptUserEntityMapping getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DeptUserEntityMapping> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DeptUserEntityMapping> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DeptUserEntityMapping> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		try {
			Long userId = null;
			for (String columnName : columnValueMap.keySet()) {
				if (columnName.equalsIgnoreCase(ColumnConstants.USER_ID.getConstantVal())) {
					userId = (Long) columnValueMap.get(columnName);
				}
			}
			if (methodName.equalsIgnoreCase(MethodConstants.GET_USER_ROLE_ENT_DATA_BY_ROLE_ID.getConstantVal())) {
				return DeptUserEntityMapping2Repo.fetchAllActiveDataByUserId(userId);
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<DeptUserEntityMapping> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DeptUserEntityMapping> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(DeptUserEntityMapping bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
