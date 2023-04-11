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

import com.iris.sdmx.element.bean.SdmxElementDependencyTypeBean;
import com.iris.sdmx.element.entity.SdmxElementDependencyTypeEntity;
import com.iris.sdmx.element.helper.SdmxElementDependencyTypeHelper;
import com.iris.sdmx.element.repo.SdmxElementDependencyTypeRepo;
import com.iris.exception.ServiceException;
import com.iris.service.GenericService;

/**
 * @author apagaria
 *
 */
@Service
@Transactional(readOnly = true)
public class SdmxElementDependencyTypeService implements GenericService<SdmxElementDependencyTypeEntity, Long> {

	@Autowired
	private SdmxElementDependencyTypeRepo sdmxElementDependencyTypeRepo;

	@Override
	public SdmxElementDependencyTypeEntity add(SdmxElementDependencyTypeEntity entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(SdmxElementDependencyTypeEntity entity) throws ServiceException {
		return false;
	}

	@Override
	public List<SdmxElementDependencyTypeEntity> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public SdmxElementDependencyTypeEntity getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxElementDependencyTypeEntity> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxElementDependencyTypeEntity> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxElementDependencyTypeEntity> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxElementDependencyTypeEntity> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxElementDependencyTypeEntity> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(SdmxElementDependencyTypeEntity bean) throws ServiceException {

	}

	/**
	 * @param isActive
	 * @return
	 */
	public List<SdmxElementDependencyTypeBean> findByActiveStatus(Boolean isActive) {
		List<SdmxElementDependencyTypeBean> sdmxElementDependencyTypeBeans = null;
		List<SdmxElementDependencyTypeEntity> sdmxElementDependencyTypeEntities = sdmxElementDependencyTypeRepo.findByActiveStatus(isActive);
		if (!CollectionUtils.isEmpty(sdmxElementDependencyTypeEntities)) {
			sdmxElementDependencyTypeBeans = new ArrayList<>();
			for (SdmxElementDependencyTypeEntity sdmxElementDependencyTypeEntity : sdmxElementDependencyTypeEntities) {
				SdmxElementDependencyTypeBean sdmxElementDependencyTypeBean = new SdmxElementDependencyTypeBean();
				// convert entity to Bean
				SdmxElementDependencyTypeHelper.convertEntityToBean(sdmxElementDependencyTypeEntity, sdmxElementDependencyTypeBean);
				sdmxElementDependencyTypeBeans.add(sdmxElementDependencyTypeBean);
			}
		}
		return sdmxElementDependencyTypeBeans;
	}

	/**
	 * @param dependencyId
	 * @return
	 */
	public Boolean isDataExistWithId(Long dependencyId) {
		Boolean isRrecordExist = false;
		int recordCount = sdmxElementDependencyTypeRepo.isDataExistWithId(dependencyId);
		if (recordCount > 0) {
			isRrecordExist = true;
		}
		return isRrecordExist;
	}

	/**
	 * @param dependencyName
	 * @return
	 */
	public Boolean isDataExistWithDependencyName(String dependencyName) {
		Boolean isRrecordExist = false;
		int recordCount = sdmxElementDependencyTypeRepo.isDataExistWithDependencyTypeName(dependencyName);
		if (recordCount > 0) {
			isRrecordExist = true;
		}
		return isRrecordExist;
	}

	/**
	 * @param dependencyTypeId
	 * @return
	 */
	public String findNameByDependencyId(Long dependencyTypeId) {
		return sdmxElementDependencyTypeRepo.findNameByDependencyId(dependencyTypeId);
	}

}
