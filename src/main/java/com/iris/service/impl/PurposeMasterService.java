package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.Currency;
import com.iris.model.PurposeMasterBean;
import com.iris.repository.PurposeMasterRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ErrorConstants;

/**
 * @author bthakare
 *
 */
@Service
public class PurposeMasterService implements GenericService<PurposeMasterBean, Long> {

	@Autowired
	PurposeMasterRepo purposeMasterRepo;

	@Override
	public PurposeMasterBean add(PurposeMasterBean entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(PurposeMasterBean entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<PurposeMasterBean> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PurposeMasterBean getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PurposeMasterBean> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PurposeMasterBean> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PurposeMasterBean> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PurposeMasterBean> getActiveDataFor(Class bean, Long id) throws ServiceException {
		try {
			if (bean.equals(PurposeMasterBean.class) && id == null) {
				return purposeMasterRepo.findByIsActiveTrue();
			}
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return null;
	}

	@Override
	public List<PurposeMasterBean> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(PurposeMasterBean bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
