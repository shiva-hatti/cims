/**
 * 
 */
package com.iris.sdmx.status.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.sdmx.status.entity.SdmxModuleDetailEntity;
import com.iris.sdmx.status.entity.SdmxModuleStatus;
import com.iris.sdmx.status.repo.SdmxModuleStatusRepo;
import com.iris.service.GenericService;

/**
 * @author apagaria
 *
 */
@Service
public class SdmxModuleStatusService implements GenericService<SdmxModuleStatus, Long> {

	/**
	 * 
	 */
	@Autowired
	private SdmxModuleStatusRepo sdmxModuleStatusRepo;

	@Override
	public SdmxModuleStatus add(SdmxModuleStatus entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(SdmxModuleStatus entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SdmxModuleStatus> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SdmxModuleStatus getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxModuleStatus> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxModuleStatus> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxModuleStatus> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxModuleStatus> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxModuleStatus> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(SdmxModuleStatus bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	/**
	 * @param moduleId
	 * @param isActive
	 * @return
	 */
	public Map<String, SdmxModuleStatus> findModuleStatusByModuleIdNActive(SdmxModuleDetailEntity moduleDetailIdFk, Boolean isActive) {
		Map<String, SdmxModuleStatus> sdmxModuleStatusMap = null;
		List<SdmxModuleStatus> sdmxModuleStatusList = sdmxModuleStatusRepo.findModuleStatusByModuleIdNActive(moduleDetailIdFk, isActive);
		if (!CollectionUtils.isEmpty(sdmxModuleStatusList)) {
			sdmxModuleStatusMap = new HashMap<String, SdmxModuleStatus>();
			for (SdmxModuleStatus sdmxModuleStatus : sdmxModuleStatusList) {
				sdmxModuleStatusMap.put(sdmxModuleStatus.getModuleStatusCode(), sdmxModuleStatus);
			}
		}
		return sdmxModuleStatusMap;
	}

}