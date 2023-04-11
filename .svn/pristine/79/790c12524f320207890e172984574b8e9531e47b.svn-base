package com.iris.sdmx.dataType.service;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.sdmx.dataType.FieldDataType;
import com.iris.sdmx.dataType.repo.DataTypeRepo;
import com.iris.service.GenericService;

@Service
public class DataTypeService implements GenericService<FieldDataType, Long> {

	private static final Logger LOGGER = LogManager.getLogger(DataTypeService.class);

	@Autowired
	DataTypeRepo dataTypeRepo;

	@Override
	public FieldDataType add(FieldDataType entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(FieldDataType entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<FieldDataType> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FieldDataType getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FieldDataType> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FieldDataType> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FieldDataType> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FieldDataType> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FieldDataType> getAllDataFor(Class bean, Long id) throws ServiceException {
		List<FieldDataType> dataTypes = null;
		try {
			dataTypes = dataTypeRepo.getAllFielddataType();
		} catch (Exception e) {
			LOGGER.error(e);
		}
		return dataTypes;
	}

	@Override
	public void deleteData(FieldDataType bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
