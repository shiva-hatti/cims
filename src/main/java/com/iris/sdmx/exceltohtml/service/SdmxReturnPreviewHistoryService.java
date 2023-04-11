/**
 * 
 */
package com.iris.sdmx.exceltohtml.service;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iris.exception.ServiceException;
import com.iris.sdmx.exceltohtml.entity.SdmxReturnPreviewHistoryEntity;
import com.iris.sdmx.exceltohtml.repo.SdmxReturnPreviewHistoryRepo;
import com.iris.service.GenericService;

/**
 * @author apagaria
 *
 */
@Service
@Transactional(readOnly = true)
public class SdmxReturnPreviewHistoryService implements GenericService<SdmxReturnPreviewHistoryEntity, Long> {

	private static final Logger LOGGER = LogManager.getLogger(SdmxReturnPreviewHistoryService.class);

	@Autowired
	private SdmxReturnPreviewHistoryRepo sdmxReturnPreviewHistoryRepo;

	@Override
	@Transactional(readOnly = false)
	public SdmxReturnPreviewHistoryEntity add(SdmxReturnPreviewHistoryEntity entity) throws ServiceException {
		LOGGER.debug("SdmxReturnPreviewHistoryEntity save");
		return sdmxReturnPreviewHistoryRepo.save(entity);
	}

	@Override
	public boolean update(SdmxReturnPreviewHistoryEntity entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SdmxReturnPreviewHistoryEntity> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SdmxReturnPreviewHistoryEntity getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxReturnPreviewHistoryEntity> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxReturnPreviewHistoryEntity> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxReturnPreviewHistoryEntity> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxReturnPreviewHistoryEntity> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxReturnPreviewHistoryEntity> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(SdmxReturnPreviewHistoryEntity bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
