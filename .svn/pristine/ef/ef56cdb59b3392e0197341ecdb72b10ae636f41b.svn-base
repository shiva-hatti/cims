/**
 * 
 */
package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.ETLConceptsInfo;
import com.iris.repository.ETLConceptInfoRepo;
import com.iris.service.GenericService;

/**
 * @author akhandagale
 *
 */
@Service
public class ETLConceptInfoService implements GenericService<ETLConceptsInfo, Long> {

	@Autowired
	private ETLConceptInfoRepo eTLConceptInfoRepo;

	@Override
	public ETLConceptsInfo add(ETLConceptsInfo entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(ETLConceptsInfo entity) throws ServiceException {
		return false;
	}

	@Override
	public List<ETLConceptsInfo> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public ETLConceptsInfo getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<ETLConceptsInfo> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ETLConceptsInfo> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ETLConceptsInfo> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ETLConceptsInfo> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<ETLConceptsInfo> getAllDataFor(Class bean, Long id) throws ServiceException {
		return eTLConceptInfoRepo.findAll();
	}

	@Override
	public void deleteData(ETLConceptsInfo bean) throws ServiceException {

	}

}
