package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.SapBoEBRPilotDetails;
import com.iris.repository.SapBoEBRPilotReportRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ErrorConstants;

/**
 * @author sajadhav
 * @version 1.0
 * @date 27/01/2021
 */
@Service
public class SapBoReportEBRPilotService implements GenericService<SapBoEBRPilotDetails, Integer> {

	@Autowired
	private SapBoEBRPilotReportRepo sapBoEBRPilotReportRepo;

	@Override
	public SapBoEBRPilotDetails add(SapBoEBRPilotDetails entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(SapBoEBRPilotDetails entity) throws ServiceException {
		return false;
	}

	@Override
	public List<SapBoEBRPilotDetails> getDataByIds(Integer[] ids) throws ServiceException {
		return null;
	}

	@Override
	public SapBoEBRPilotDetails getDataById(Integer id) throws ServiceException {
		return null;
	}

	@Override
	public List<SapBoEBRPilotDetails> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SapBoEBRPilotDetails> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SapBoEBRPilotDetails> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SapBoEBRPilotDetails> getActiveDataFor(Class bean, Integer id) throws ServiceException {
		return null;
	}

	@Override
	public List<SapBoEBRPilotDetails> getAllDataFor(Class bean, Integer id) throws ServiceException {
		try {
			return sapBoEBRPilotReportRepo.findAll();
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public void deleteData(SapBoEBRPilotDetails bean) throws ServiceException {
	}

}