/**
 * 
 */
package com.iris.sdmx.fusion.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.sdmx.fusion.entity.FusionProperties;
import com.iris.sdmx.fusion.repo.FusionPropertiesRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ErrorConstants;

/**
 * @author sajadhav
 *
 */
@Service
public class FusionPropertiesService implements GenericService<FusionProperties, Long> {

	@Autowired
	private FusionPropertiesRepo fusionPropertiesRepo;

	@Override
	public FusionProperties add(FusionProperties entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(FusionProperties entity) throws ServiceException {
		return false;
	}

	@Override
	public List<FusionProperties> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public FusionProperties getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<FusionProperties> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<FusionProperties> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FusionProperties> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<FusionProperties> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FusionProperties> getAllDataFor(Class bean, Long id) throws ServiceException {
		try {
			if (bean == null || id == null) {
				return fusionPropertiesRepo.findAll();
			}
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return null;
	}

	@Override
	public void deleteData(FusionProperties bean) throws ServiceException {

	}

}
