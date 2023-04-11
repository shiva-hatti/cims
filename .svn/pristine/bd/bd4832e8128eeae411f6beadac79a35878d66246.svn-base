package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.ReturnGroupLblMod;
import com.iris.repository.ReturnGroupLabelModRepo;
import com.iris.service.GenericService;

@Service
public class ReturnGroupLabelModServiceV2 implements GenericService<ReturnGroupLblMod, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReturnGroupLabelModServiceV2.class);

	@Autowired
	private ReturnGroupLabelModRepo returnGroupLabelModRepo;

	@Override
	public ReturnGroupLblMod add(ReturnGroupLblMod returnGroupLabelMapping) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(ReturnGroupLblMod returnGroupLabelMappingBean) throws ServiceException {
		return false;
	}

	@Override
	public List<ReturnGroupLblMod> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public ReturnGroupLblMod getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnGroupLblMod> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnGroupLblMod> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnGroupLblMod> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnGroupLblMod> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnGroupLblMod> getAllDataFor(Class bean, Long id) throws ServiceException {
		return returnGroupLabelModRepo.getDataByReturnGroupLabelId(id, PageRequest.of(0, 10));
	}

	@Override
	public void deleteData(ReturnGroupLblMod bean) throws ServiceException {

	}
}
