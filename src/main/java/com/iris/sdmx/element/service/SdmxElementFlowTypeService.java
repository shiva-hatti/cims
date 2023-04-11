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

import com.iris.sdmx.element.bean.SdmxElementFlowTypeBean;
import com.iris.sdmx.element.entity.SdmxElementFlowTypeEntity;
import com.iris.sdmx.element.helper.SdmxElementFlowTypeHelper;
import com.iris.sdmx.element.repo.SdmxElementFlowTypeRepo;
import com.iris.exception.ServiceException;
import com.iris.service.GenericService;

/**
 * @author apagaria
 *
 */
@Service
@Transactional(readOnly = true)
public class SdmxElementFlowTypeService implements GenericService<SdmxElementFlowTypeEntity, Long> {

	@Autowired
	private SdmxElementFlowTypeRepo sdmxElementFlowTypeRepo;

	@Override
	public SdmxElementFlowTypeEntity add(SdmxElementFlowTypeEntity entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(SdmxElementFlowTypeEntity entity) throws ServiceException {
		return false;
	}

	@Override
	public List<SdmxElementFlowTypeEntity> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public SdmxElementFlowTypeEntity getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxElementFlowTypeEntity> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxElementFlowTypeEntity> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxElementFlowTypeEntity> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxElementFlowTypeEntity> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxElementFlowTypeEntity> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(SdmxElementFlowTypeEntity bean) throws ServiceException {

	}

	/**
	 * @param isActive
	 * @return
	 */
	public List<SdmxElementFlowTypeBean> findByActiveStatus(Boolean isActive) {
		List<SdmxElementFlowTypeBean> sdmxElementFlowTypeBeans = null;
		List<SdmxElementFlowTypeEntity> sdmxElementFlowTypeEntities = sdmxElementFlowTypeRepo.findByActiveStatus(isActive);
		if (!CollectionUtils.isEmpty(sdmxElementFlowTypeEntities)) {
			sdmxElementFlowTypeBeans = new ArrayList<>();
			for (SdmxElementFlowTypeEntity sdmxElementFlowTypeEntity : sdmxElementFlowTypeEntities) {
				SdmxElementFlowTypeBean sdmxElementFlowTypeBean = new SdmxElementFlowTypeBean();
				// convert entity to Bean
				SdmxElementFlowTypeHelper.convertEntityToBean(sdmxElementFlowTypeEntity, sdmxElementFlowTypeBean);
				sdmxElementFlowTypeBeans.add(sdmxElementFlowTypeBean);
			}
		}
		return sdmxElementFlowTypeBeans;

	}

	/**
	 * @param flowTypeId
	 * @return
	 */
	public Boolean isDataExistWithId(Long flowTypeId) {
		Boolean isRrecordExist = false;
		int recordCount = sdmxElementFlowTypeRepo.isDataExistWithId(flowTypeId);
		if (recordCount > 0) {
			isRrecordExist = true;
		}
		return isRrecordExist;
	}

	/**
	 * @param flowTypeName
	 * @return
	 */
	public Boolean isDataExistWithFlowTypeName(String flowTypeName) {
		Boolean isRrecordExist = false;
		int recordCount = sdmxElementFlowTypeRepo.isDataExistWithName(flowTypeName);
		if (recordCount > 0) {
			isRrecordExist = true;
		}
		return isRrecordExist;
	}

	/**
	 * @param flowTypeId
	 * @return
	 */
	public String findNameByFlowTypeId(Long flowTypeId) {
		return sdmxElementFlowTypeRepo.findNameByFlowTypeId(flowTypeId);
	}

}
