/**
 * 
 */
package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.StateMaster;
import com.iris.repository.StateMasterRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ErrorConstants;

/**
 * @author Siddique H Khan
 *
 */
@Service
public class StateMasterService implements GenericService<StateMaster, Long> {

	@Autowired
	StateMasterRepo stateMasterRepo;

	@Override
	public StateMaster add(StateMaster entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(StateMaster entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<StateMaster> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StateMaster getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StateMaster> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StateMaster> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StateMaster> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StateMaster> getActiveDataFor(Class bean, Long id) throws ServiceException {
		try {
			if (bean.equals(StateMaster.class) && id == null) {
				return stateMasterRepo.getActiveStateData();
			}

		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}

		return null;
	}

	@Override
	public List<StateMaster> getAllDataFor(Class bean, Long id) throws ServiceException {

		return null;
	}

	@Override
	public void deleteData(StateMaster bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
