package com.iris.rbrToEbr.service;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.rbrToEbr.entity.CtlEbrElementEntity;
import com.iris.rbrToEbr.repo.CtlEbrElementRepo;
import com.iris.service.GenericService;

@Service
public class CtlEbrElementService implements GenericService<CtlEbrElementEntity, Long> {

	@Autowired
	private CtlEbrElementRepo ctlEbrElementRepo;

	private static final Logger LOGGER = LogManager.getLogger(CtlEbrElementService.class);

	@Override
	public CtlEbrElementEntity add(CtlEbrElementEntity entity) throws ServiceException {
		return ctlEbrElementRepo.save(entity);
	}

	@Override
	public boolean update(CtlEbrElementEntity entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<CtlEbrElementEntity> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CtlEbrElementEntity getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CtlEbrElementEntity> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CtlEbrElementEntity> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CtlEbrElementEntity> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CtlEbrElementEntity> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CtlEbrElementEntity> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(CtlEbrElementEntity bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
