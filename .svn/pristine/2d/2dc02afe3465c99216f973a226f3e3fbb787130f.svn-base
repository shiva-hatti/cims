package com.iris.sdmx.element.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iris.exception.ServiceException;
import com.iris.sdmx.element.bean.SdmxElementClassificationBean;
import com.iris.sdmx.element.bean.SdmxElementUsageBean;
import com.iris.sdmx.element.entity.SdmxElementClassificationEntity;
import com.iris.sdmx.element.entity.SdmxElementUsageEntity;
import com.iris.sdmx.element.helper.SdmxElementClassificationHelper;
import com.iris.sdmx.element.helper.SdmxElementUsageHelper;
import com.iris.sdmx.element.repo.SdmxElementClassificationRepo;
import com.iris.sdmx.element.repo.SdmxElementUsageRepo;
import com.iris.service.GenericService;

/**
 * @author vjadhav
 *
 */
@Service
@Transactional(readOnly = true)
public class SdmxElementUsageService implements GenericService<SdmxElementUsageEntity, Long> {

	@Autowired
	private SdmxElementUsageRepo sdmxElementUsageRepo;

	@Override
	public SdmxElementUsageEntity add(SdmxElementUsageEntity entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(SdmxElementUsageEntity entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SdmxElementUsageEntity> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SdmxElementUsageEntity getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxElementUsageEntity> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxElementUsageEntity> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxElementUsageEntity> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxElementUsageEntity> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxElementUsageEntity> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(SdmxElementUsageEntity bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	public List<SdmxElementUsageBean> findByActiveStatus(Boolean isActive) {
		List<SdmxElementUsageBean> sdmxElementUsageBeans = null;
		List<SdmxElementUsageEntity> sdmxElementUsageEntities = sdmxElementUsageRepo.findByActiveStatus(isActive);
		if (!CollectionUtils.isEmpty(sdmxElementUsageEntities)) {
			sdmxElementUsageBeans = new ArrayList<>();
			for (SdmxElementUsageEntity sdmxElementUsageEntity : sdmxElementUsageEntities) {
				SdmxElementUsageBean sdmxElementUsageBean = new SdmxElementUsageBean();
				// convert entity to Bean
				SdmxElementUsageHelper.convertEntityToBean(sdmxElementUsageEntity, sdmxElementUsageBean);
				sdmxElementUsageBeans.add(sdmxElementUsageBean);
			}
		}
		return sdmxElementUsageBeans;
	}

	public String findNameByUsageId(Long usageId) {
		return sdmxElementUsageRepo.findNameByUsageId(usageId);
	}

}
