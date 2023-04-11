/**
 * 
 */
package com.iris.sdmx.exceltohtml.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iris.exception.ServiceException;
import com.iris.sdmx.exceltohtml.entity.SdmxReturnFormulaMappingEntity;
import com.iris.sdmx.exceltohtml.repo.SdmxReturnFormulaMappingRepo;
import com.iris.service.GenericService;

/**
 * @author apagaria
 *
 */
@Service
@Transactional(readOnly = true)
public class SdmxReturnFormulaMappingService implements GenericService<SdmxReturnFormulaMappingEntity, Long> {

	@Autowired
	private SdmxReturnFormulaMappingRepo sdmxReturnFormulaMappingRepo;

	@Override
	@Transactional(readOnly = false)
	public SdmxReturnFormulaMappingEntity add(SdmxReturnFormulaMappingEntity entity) throws ServiceException {
		return sdmxReturnFormulaMappingRepo.save(entity);
	}

	@Override
	public boolean update(SdmxReturnFormulaMappingEntity entity) throws ServiceException {
		return false;
	}

	@Override
	public List<SdmxReturnFormulaMappingEntity> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public SdmxReturnFormulaMappingEntity getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxReturnFormulaMappingEntity> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxReturnFormulaMappingEntity> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxReturnFormulaMappingEntity> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxReturnFormulaMappingEntity> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxReturnFormulaMappingEntity> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(SdmxReturnFormulaMappingEntity bean) throws ServiceException {

	}
}
