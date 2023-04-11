package com.iris.sdmx.element.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iris.exception.ServiceException;
import com.iris.sdmx.element.bean.SdmxElementFrequencyBean;
import com.iris.sdmx.element.entity.SdmxElementFrequencyEntity;
import com.iris.sdmx.element.helper.SdmxElementFrequencyHelper;
import com.iris.sdmx.element.repo.SdmxElementFrequencyRepo;
import com.iris.service.GenericService;

/**
 * @author vjadhav
 *
 */
@Service
@Transactional(readOnly = true)
public class SdmxElementFrequencyService implements GenericService<SdmxElementFrequencyEntity, Long> {

	@Autowired
	private SdmxElementFrequencyRepo sdmxElementFrequencyRepo;

	@Override
	public SdmxElementFrequencyEntity add(SdmxElementFrequencyEntity entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(SdmxElementFrequencyEntity entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SdmxElementFrequencyEntity> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SdmxElementFrequencyEntity getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxElementFrequencyEntity> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxElementFrequencyEntity> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxElementFrequencyEntity> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxElementFrequencyEntity> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxElementFrequencyEntity> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(SdmxElementFrequencyEntity bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	public List<SdmxElementFrequencyBean> findByActiveStatus(Boolean isActive) {
		List<SdmxElementFrequencyBean> sdmxElementFrequencyBeans = null;
		List<SdmxElementFrequencyEntity> sdmxElementFrequencyEntities = sdmxElementFrequencyRepo.findByActiveStatus(isActive);
		if (!CollectionUtils.isEmpty(sdmxElementFrequencyEntities)) {
			sdmxElementFrequencyBeans = new ArrayList<>();
			for (SdmxElementFrequencyEntity sdmxElementFrequencyEntity : sdmxElementFrequencyEntities) {
				SdmxElementFrequencyBean sdmxElementFrequencyBean = new SdmxElementFrequencyBean();
				// convert entity to Bean
				SdmxElementFrequencyHelper.convertEntityToBean(sdmxElementFrequencyEntity, sdmxElementFrequencyBean);
				sdmxElementFrequencyBeans.add(sdmxElementFrequencyBean);
			}
		}
		return sdmxElementFrequencyBeans;
	}

	public String findNameByFrequencyId(Long frequencyId) {
		return sdmxElementFrequencyRepo.findNameByFrequencyId(frequencyId);
	}

}
