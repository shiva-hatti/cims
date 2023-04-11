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

import com.iris.sdmx.element.bean.SdmxElementSectorBean;
import com.iris.sdmx.element.entity.SdmxElementSectorEntity;
import com.iris.sdmx.element.helper.SdmxElementSectorHelper;
import com.iris.sdmx.element.repo.SdmxElementSectorRepo;
import com.iris.exception.ServiceException;
import com.iris.service.GenericService;

/**
 * @author apagaria
 *
 */
@Service
@Transactional(readOnly = true)
public class SdmxElementSectorService implements GenericService<SdmxElementSectorEntity, Long> {

	@Autowired
	private SdmxElementSectorRepo sdmxElementSectorRepo;

	@Override
	public SdmxElementSectorEntity add(SdmxElementSectorEntity entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(SdmxElementSectorEntity entity) throws ServiceException {
		return false;
	}

	@Override
	public List<SdmxElementSectorEntity> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public SdmxElementSectorEntity getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxElementSectorEntity> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxElementSectorEntity> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxElementSectorEntity> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxElementSectorEntity> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxElementSectorEntity> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(SdmxElementSectorEntity bean) throws ServiceException {

	}

	/**
	 * @param isActive
	 * @return
	 */
	public List<SdmxElementSectorBean> findByActiveStatus(Boolean isActive) {
		List<SdmxElementSectorBean> sdmxElementNatureBeans = null;
		List<SdmxElementSectorEntity> sdmxElementSectorEntities = sdmxElementSectorRepo.findByActiveStatus(isActive);
		if (!CollectionUtils.isEmpty(sdmxElementSectorEntities)) {
			sdmxElementNatureBeans = new ArrayList<>();
			for (SdmxElementSectorEntity sdmxElementSectorEntity : sdmxElementSectorEntities) {
				SdmxElementSectorBean sdmxElementSectorBean = new SdmxElementSectorBean();
				// convert entity to Bean
				SdmxElementSectorHelper.convertEntityToBean(sdmxElementSectorEntity, sdmxElementSectorBean);
				sdmxElementNatureBeans.add(sdmxElementSectorBean);
			}
		}
		return sdmxElementNatureBeans;
	}

	/**
	 * @param sectorId
	 * @return
	 */
	public Boolean isDataExistWithId(Long sectorId) {
		Boolean isRrecordExist = false;
		int recordCount = sdmxElementSectorRepo.isDataExistWithId(sectorId);
		if (recordCount > 0) {
			isRrecordExist = true;
		}
		return isRrecordExist;
	}

	/**
	 * @param sectorName
	 * @return
	 */
	public Boolean isDataExistWithSectorName(String sectorName) {
		Boolean isRrecordExist = false;
		int recordCount = sdmxElementSectorRepo.isDataExistWithSectorName(sectorName);
		if (recordCount > 0) {
			isRrecordExist = true;
		}
		return isRrecordExist;
	}

	/**
	 * @param sectorId
	 * @return
	 */
	public String findNameBySectorId(Long sectorId) {
		return sdmxElementSectorRepo.findNameBySectorId(sectorId);
	}

}
