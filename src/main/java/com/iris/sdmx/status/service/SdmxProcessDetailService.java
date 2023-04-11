/**
 * 
 */
package com.iris.sdmx.status.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.sdmx.status.entity.SdmxProcessDetailEntity;
import com.iris.sdmx.status.repo.SdmxProcessDetailRepo;
import com.iris.service.GenericService;

/**
 * @author apagaria
 *
 */
@Service
public class SdmxProcessDetailService implements GenericService<SdmxProcessDetailEntity, Long> {

	/**
	 * 
	 */
	@Autowired
	public SdmxProcessDetailRepo sdmxProcessDetailRepo;

	/**
	 * @param moduleCode
	 * @return
	 */
	public SdmxProcessDetailEntity findProcessByProcessCode(String processCode) {
		return sdmxProcessDetailRepo.findProcessByProcessCode(processCode);
	}

	@Override
	public SdmxProcessDetailEntity add(SdmxProcessDetailEntity entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(SdmxProcessDetailEntity entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SdmxProcessDetailEntity> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SdmxProcessDetailEntity getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxProcessDetailEntity> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxProcessDetailEntity> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxProcessDetailEntity> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxProcessDetailEntity> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxProcessDetailEntity> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(SdmxProcessDetailEntity bean) throws ServiceException {
		// TODO Auto-generated method stub

	}
}