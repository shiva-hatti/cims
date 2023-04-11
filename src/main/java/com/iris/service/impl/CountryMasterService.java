/**
 * 
 */
package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.CountryMaster;
import com.iris.repository.CountryMasterRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ErrorConstants;

/**
 * @author Siddique H Khan
 *
 */
@Service
public class CountryMasterService implements GenericService<CountryMaster, Long> {

	@Autowired
	CountryMasterRepo countryMasterRepo;

	@Override
	public CountryMaster add(CountryMaster entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(CountryMaster entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<CountryMaster> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CountryMaster getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CountryMaster> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CountryMaster> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CountryMaster> getActiveDataFor(Class bean, Long id) throws ServiceException {
		try {
			if (bean.equals(CountryMaster.class) && id == null) {
				return countryMasterRepo.findAllActiveData();
			}
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return null;
	}

	@Override
	public List<CountryMaster> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(CountryMaster bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<CountryMaster> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

}
