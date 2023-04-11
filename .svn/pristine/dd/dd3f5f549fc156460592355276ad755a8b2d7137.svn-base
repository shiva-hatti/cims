package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.dto.ErrorCodeMessageBean;
import com.iris.exception.ServiceException;
import com.iris.model.ErrorVersionChannelMapping;
import com.iris.model.ReturnTemplate;
import com.iris.repository.ErrorVersionChannelMappingRepository;
import com.iris.service.GenericService;

@Service
public class ErrorVersionChannelMappingService implements GenericService<ErrorVersionChannelMapping, Long> {

	@Autowired
	private ErrorVersionChannelMappingRepository errorVersionChannelMappingRepository;

	static final Logger LOGGER = LogManager.getLogger(ErrorVersionChannelMappingService.class);

	@Override
	public ErrorVersionChannelMapping add(ErrorVersionChannelMapping entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(ErrorVersionChannelMapping entity) throws ServiceException {
		return false;
	}

	@Override
	public List<ErrorVersionChannelMapping> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public ErrorVersionChannelMapping getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<ErrorVersionChannelMapping> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ErrorVersionChannelMapping> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ErrorVersionChannelMapping> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ErrorVersionChannelMapping> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<ErrorVersionChannelMapping> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(ErrorVersionChannelMapping bean) throws ServiceException {
	}

	public List<ErrorVersionChannelMapping> getErrorVersionChannelMappingRecordForFileByStatus(Boolean isActiveForFileBasedFiling, ReturnTemplate returnTemplate) {
		return errorVersionChannelMappingRepository.getErrorVersionChannelMappingRecordForFileByStatus(isActiveForFileBasedFiling, returnTemplate);
	}

	public List<ErrorVersionChannelMapping> getErrorVersionChannelMappingRecordForWebFormBasedByStatus(Boolean isActiveForWebFormBasedFiling, ReturnTemplate returnTemplate) {
		return errorVersionChannelMappingRepository.getErrorVersionChannelMappingRecordForWebFormBasedByStatus(isActiveForWebFormBasedFiling, returnTemplate);
	}

	public List<ErrorCodeMessageBean> getErrorsRecordForWebFormBasedOnReturnId(Long returnTemplateId, String langCode) {
		return errorVersionChannelMappingRepository.getErrorsRecordForWebFormBasedOnReturnId(returnTemplateId, langCode);
	}

}