/**
 * 
 */
package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.ReturnFileFormatMap;
import com.iris.repository.ReturnFileFormatMapRepo;
import com.iris.service.GenericService;

/**
 * @author Siddique
 *
 */
@Service
public class ReturnFileFormatMapService implements GenericService<ReturnFileFormatMap, Long> {

	@Autowired
	private ReturnFileFormatMapRepo returnFileFormatMapRepo;

	@Override
	public ReturnFileFormatMap add(ReturnFileFormatMap entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(ReturnFileFormatMap entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ReturnFileFormatMap> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReturnFileFormatMap getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return returnFileFormatMapRepo.getDataByid(id);
	}

	@Override
	public List<ReturnFileFormatMap> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReturnFileFormatMap> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReturnFileFormatMap> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReturnFileFormatMap> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReturnFileFormatMap> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(ReturnFileFormatMap bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
