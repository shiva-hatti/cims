/**
 * 
 */
package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.CensusLocationMaster;
import com.iris.repository.CensusLocationMasterRepo;
import com.iris.service.GenericService;

/**
 * @author Siddique H Khan
 *
 */
@Service
public class CensusLocationMasterService implements GenericService<CensusLocationMaster, Long> {

	@Autowired
	private CensusLocationMasterRepo censusLocationMasterRepo;

	@Override
	public CensusLocationMaster add(CensusLocationMaster entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(CensusLocationMaster entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<CensusLocationMaster> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CensusLocationMaster getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CensusLocationMaster> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CensusLocationMaster> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CensusLocationMaster> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CensusLocationMaster> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CensusLocationMaster> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(CensusLocationMaster bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
