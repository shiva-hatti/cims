package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.ProcessingTime;
import com.iris.model.UploadChannel;
import com.iris.repository.ChannelRepo;
import com.iris.repository.ProcessingTimeRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ErrorConstants;

@Service
public class ProcessingTimeService implements GenericService<ProcessingTime, Long> {

	@Autowired
	ProcessingTimeRepo processingTimeRepo;

	@Override
	public ProcessingTime add(ProcessingTime processingTime) throws ServiceException {
		try {
			return processingTimeRepo.save(processingTime);
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public boolean update(ProcessingTime entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ProcessingTime> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessingTime getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProcessingTime> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProcessingTime> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProcessingTime> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProcessingTime> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProcessingTime> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(ProcessingTime bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
