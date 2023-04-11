/**
 * 
 */
package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.DynamicEleCol;
import com.iris.repository.DynamicEleColRepo;
import com.iris.service.GenericService;

/**
 * @author Siddique
 *
 */

@Service
public class DynamicEleColService implements GenericService<DynamicEleCol, Long> {

	@Autowired
	DynamicEleColRepo dynamicEleColRepo;

	@Override
	public DynamicEleCol add(DynamicEleCol entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(DynamicEleCol entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<DynamicEleCol> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DynamicEleCol getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DynamicEleCol> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DynamicEleCol> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DynamicEleCol> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DynamicEleCol> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DynamicEleCol> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(DynamicEleCol bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	public String getDataByElementDefName(String val) throws ServiceException {

		return dynamicEleColRepo.getDyanmicUrl(val);
	}

}
