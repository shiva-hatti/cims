package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.SapBoDetails;
import com.iris.repository.SapBoReportRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ErrorConstants;

/**
 * @author psawant
 * @version 1.0
 * @date 10/09/2020
 */
@Service
public class SapBoReportService implements GenericService<SapBoDetails, Integer> {

	@Autowired
	private SapBoReportRepo sapBoReportRepo;

	@Override
	public SapBoDetails add(SapBoDetails entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(SapBoDetails entity) throws ServiceException {
		try {
			sapBoReportRepo.save(entity);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<SapBoDetails> getDataByIds(Integer[] ids) throws ServiceException {
		return null;
	}

	@Override
	public SapBoDetails getDataById(Integer id) throws ServiceException {
		try {
			return sapBoReportRepo.findDataById(id);
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<SapBoDetails> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SapBoDetails> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SapBoDetails> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SapBoDetails> getActiveDataFor(Class bean, Integer id) throws ServiceException {
		return null;
	}

	@Override
	public List<SapBoDetails> getAllDataFor(Class bean, Integer id) throws ServiceException {
		try {
			return sapBoReportRepo.findAll();
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public void deleteData(SapBoDetails bean) throws ServiceException {
	}

}