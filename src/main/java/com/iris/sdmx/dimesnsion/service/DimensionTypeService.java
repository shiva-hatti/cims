package com.iris.sdmx.dimesnsion.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.iris.exception.ServiceException;
import com.iris.sdmx.dimesnsion.entity.DimensionType;
import com.iris.sdmx.dimesnsion.repo.DimensionTypeRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ErrorConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class DimensionTypeService implements GenericService<DimensionType, Long> {

	static final Logger LOGGER = LogManager.getLogger(DimensionTypeService.class);

	@Autowired
	DimensionTypeRepo dimensionTypeRepo;

	@Override
	public DimensionType add(DimensionType entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(DimensionType entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<DimensionType> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DimensionType getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DimensionType> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DimensionType> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DimensionType> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DimensionType> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DimensionType> getAllDataFor(Class bean, Long id) throws ServiceException {
		try {
			if (bean == null && id == null) {
				return dimensionTypeRepo.findByIsActiveTrue();
			} else {
				return null;
			}
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal());
		}
	}

	@Override
	public void deleteData(DimensionType bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
