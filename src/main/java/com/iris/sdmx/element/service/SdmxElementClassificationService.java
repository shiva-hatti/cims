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

import com.iris.sdmx.element.bean.SdmxElementClassificationBean;
import com.iris.sdmx.element.entity.SdmxElementClassificationEntity;
import com.iris.sdmx.element.helper.SdmxElementClassificationHelper;
import com.iris.sdmx.element.repo.SdmxElementClassificationRepo;
import com.iris.exception.ServiceException;
import com.iris.service.GenericService;

/**
 * @author apagaria
 *
 */
@Service
@Transactional(readOnly = true)
public class SdmxElementClassificationService implements GenericService<SdmxElementClassificationEntity, Long> {

	/**
	 * 
	 */
	@Autowired
	private SdmxElementClassificationRepo sdmxElementClassificationRepo;

	@Override
	public SdmxElementClassificationEntity add(SdmxElementClassificationEntity entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(SdmxElementClassificationEntity entity) throws ServiceException {
		return false;
	}

	@Override
	public List<SdmxElementClassificationEntity> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public SdmxElementClassificationEntity getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxElementClassificationEntity> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxElementClassificationEntity> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxElementClassificationEntity> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxElementClassificationEntity> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxElementClassificationEntity> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(SdmxElementClassificationEntity bean) throws ServiceException {

	}

	/**
	 * 
	 * This method is used to fetch active classification data
	 * 
	 * @return
	 */
	public List<SdmxElementClassificationBean> findByActiveStatus(Boolean isActive) {
		List<SdmxElementClassificationBean> elementClassificationBeans = null;
		List<SdmxElementClassificationEntity> elementClassificationEntities = sdmxElementClassificationRepo.findByActiveStatus(isActive);
		if (!CollectionUtils.isEmpty(elementClassificationEntities)) {
			elementClassificationBeans = new ArrayList<>();
			for (SdmxElementClassificationEntity sdmxElementClassificationEntity : elementClassificationEntities) {
				SdmxElementClassificationBean sdmxElementClassificationBean = new SdmxElementClassificationBean();
				// convert entity to Bean
				SdmxElementClassificationHelper.convertEntityToBean(sdmxElementClassificationEntity, sdmxElementClassificationBean);
				elementClassificationBeans.add(sdmxElementClassificationBean);
			}
		}
		return elementClassificationBeans;
	}

	/**
	 * @param classificationId
	 * @return
	 */
	public Boolean isDataExistWithId(Long classificationId) {
		Boolean isRrecordExist = false;
		int recordCount = sdmxElementClassificationRepo.isDataExistWithId(classificationId);
		if (recordCount > 0) {
			isRrecordExist = true;
		}
		return isRrecordExist;
	}

	/**
	 * @param classificationName
	 * @return
	 */
	public Boolean isDataExistWithClassificationName(String classificationName) {
		Boolean isRrecordExist = false;
		int recordCount = sdmxElementClassificationRepo.isDataExistWithClassificationName(classificationName);
		if (recordCount > 0) {
			isRrecordExist = true;
		}
		return isRrecordExist;
	}

	/**
	 * @param classificationId
	 * @return
	 */
	public String findNameByClassificationId(Long classificationId) {
		return sdmxElementClassificationRepo.findNameByClassificationId(classificationId);
	}

}
