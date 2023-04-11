package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.EmailSetting;
import com.iris.repository.EmailRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ErrorConstants;

@Service
public class EmailService implements GenericService<EmailSetting, Long> {

	@Autowired
	EmailRepo emailRepo;

	@Override
	public EmailSetting add(EmailSetting entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(EmailSetting entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<EmailSetting> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EmailSetting getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EmailSetting> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EmailSetting> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EmailSetting> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EmailSetting> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EmailSetting> getAllDataFor(Class bean, Long id) throws ServiceException {
		try {
			if (bean == null && id == null) {
				return emailRepo.findAll();
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}

	}

	@Override
	public void deleteData(EmailSetting bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
