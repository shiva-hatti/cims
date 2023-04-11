/**
 * 
 */
package com.iris.sdmx.element.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iris.sdmx.element.bean.SdmxElementNatureBean;
import com.iris.sdmx.element.entity.SdmxElementNatureEntity;
import com.iris.sdmx.element.helper.SdmxElementNatureHelper;
import com.iris.sdmx.element.repo.SdmxElementNatureRepo;
import com.iris.exception.ServiceException;
import com.iris.service.GenericService;

/**
 * @author apagaria
 *
 */
@Service
@Transactional(readOnly = true)
public class SdmxElementNatureService implements GenericService<SdmxElementNatureEntity, Long> {

	@Autowired
	private SdmxElementNatureRepo sdmxElementNatureRepo;

	@Override
	public SdmxElementNatureEntity add(SdmxElementNatureEntity entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(SdmxElementNatureEntity entity) throws ServiceException {
		return false;
	}

	@Override
	public List<SdmxElementNatureEntity> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public SdmxElementNatureEntity getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxElementNatureEntity> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxElementNatureEntity> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxElementNatureEntity> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxElementNatureEntity> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxElementNatureEntity> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(SdmxElementNatureEntity bean) throws ServiceException {

	}

	/**
	 * @param isActive
	 * @return
	 */
	public List<SdmxElementNatureBean> findByActiveStatus(Boolean isActive) {
		List<SdmxElementNatureBean> sdmxElementNatureBeans = null;
		List<SdmxElementNatureEntity> sdmxElementNatureEntities = sdmxElementNatureRepo.findByActiveStatus(isActive);
		if (!CollectionUtils.isEmpty(sdmxElementNatureEntities)) {
			sdmxElementNatureBeans = new ArrayList<>();
			for (SdmxElementNatureEntity sdmxElementNatureEntity : sdmxElementNatureEntities) {
				SdmxElementNatureBean sdmxElementNatureBean = new SdmxElementNatureBean();
				// convert entity to Bean
				SdmxElementNatureHelper.convertEntityToBean(sdmxElementNatureEntity, sdmxElementNatureBean);
				sdmxElementNatureBeans.add(sdmxElementNatureBean);
			}
		}
		return sdmxElementNatureBeans;
	}

	/**
	 * @param natureId
	 * @return
	 */
	public Boolean isDataExistWithId(Long natureId) {
		Boolean isRrecordExist = false;
		int recordCount = sdmxElementNatureRepo.isDataExistWithId(natureId);
		if (recordCount > 0) {
			isRrecordExist = true;
		}
		return isRrecordExist;
	}

	/**
	 * @param natureName
	 * @return
	 */
	public Boolean isDataExistWithNatureName(String natureName) {
		Boolean isRrecordExist = false;
		int recordCount = sdmxElementNatureRepo.isDataExistWithNatureName(natureName);
		if (recordCount > 0) {
			isRrecordExist = true;
		}
		return isRrecordExist;
	}

	/**
	 * @param natureId
	 * @return
	 */
	public String findNameByNatureId(Long natureId) {
		return sdmxElementNatureRepo.findNameByNatureId(natureId);
	}

}
