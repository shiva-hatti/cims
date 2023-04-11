/**
 * 
 */
package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.EmailAlert;
import com.iris.repository.PrepareSendMailRepo;
import com.iris.service.GenericService;

/**
 * @author sajadhav
 *
 */
@Service
public class PrepareSendMailService implements GenericService<EmailAlert, Long> {

	@Autowired
	PrepareSendMailRepo prepareSendMailRepo;

	@Override
	public EmailAlert add(EmailAlert entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(EmailAlert entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<EmailAlert> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EmailAlert getDataById(Long id) throws ServiceException {
		return prepareSendMailRepo.getDataById(id);
	}

	@Override
	public List<EmailAlert> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EmailAlert> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EmailAlert> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EmailAlert> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EmailAlert> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(EmailAlert bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
