package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.ReturnGroupMod;
import com.iris.repository.ReturnGroupModRepo;
import com.iris.service.GenericService;

@Service
public class ReturnGroupModServiceV2 implements GenericService<ReturnGroupMod, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReturnGroupModServiceV2.class);

	@Autowired
	private ReturnGroupModRepo returnGroupModRepo;

	@Override
	public ReturnGroupMod add(ReturnGroupMod returnGroupMapping) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(ReturnGroupMod returnGroupMappingBean) throws ServiceException {
		return false;
	}

	@Override
	public List<ReturnGroupMod> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public ReturnGroupMod getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnGroupMod> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnGroupMod> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnGroupMod> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnGroupMod> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnGroupMod> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(ReturnGroupMod bean) throws ServiceException {

	}

	public List<ReturnGroupMod> getReturnGroupModList(Long returnGroupId, String langCode, int pageCount) throws ServiceException {
		return returnGroupModRepo.getDataByReturnGroupId(returnGroupId, langCode, PageRequest.of(0, pageCount));
	}
}
