/**
 * 
 */
package com.iris.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.EmailSentHistory;
import com.iris.repository.EmailSentHistoryRepo;
import com.iris.service.GenericService;

/**
 * @author sajadhav
 *
 */

@Service
public class EmailSentHistoryService implements GenericService<EmailSentHistory, Long> {

	@Autowired
	EmailSentHistoryRepo emailSentHistoryRepo;

	@Override
	public EmailSentHistory add(EmailSentHistory entity) throws ServiceException {
		// TODO Auto-generated method stub
		return emailSentHistoryRepo.save(entity);
	}

	@Override
	public boolean update(EmailSentHistory entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<EmailSentHistory> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return emailSentHistoryRepo.findAllById(Arrays.asList(ids));
	}

	@Override
	public EmailSentHistory getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EmailSentHistory> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EmailSentHistory> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EmailSentHistory> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EmailSentHistory> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EmailSentHistory> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(EmailSentHistory bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
