package com.iris.sdmx.upload.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * @author vjadhav
 *
 */
import org.springframework.transaction.annotation.Transactional;

import com.iris.exception.ServiceException;
import com.iris.model.FilingStatus;
import com.iris.sdmx.upload.bean.ElementAuditBean;
import com.iris.sdmx.upload.entity.ElementAudit;
import com.iris.sdmx.upload.helper.SdmxElementAuditHelper;
import com.iris.sdmx.upload.repo.ElementAuditRepo;
import com.iris.service.GenericService;

/**
 * @author apagaria
 *
 */
@Service
@Transactional(readOnly = true)
public class SdmxElementAuditService implements GenericService<ElementAudit, Long> {

	/**
	 * 
	 */
	private static final Logger LOGGER = LogManager.getLogger(SdmxElementAuditService.class);

	/**
	 * 
	 */
	@Autowired
	private ElementAuditRepo elementAuditRepo;

	@Override
	public ElementAudit add(ElementAudit entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(ElementAudit entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ElementAudit> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElementAudit getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElementAudit> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElementAudit> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElementAudit> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ElementAudit> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ElementAudit> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(ElementAudit bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	/**
	 * @param status
	 * @return
	 */
	public List<ElementAuditBean> fetchSdmxAuditRecords(Integer statusId, Long fileDetailsId, String jobProcessingId) {
		LOGGER.debug("Start fetchSdmxAuditRecords with job processing id - " + jobProcessingId);
		List<ElementAuditBean> elementAuditBeanList = null;
		// Filing Status
		FilingStatus status = new FilingStatus(statusId);

		List<ElementAudit> elementAuditEntityList = elementAuditRepo.fetchElementAuditRecords(status, fileDetailsId);

		if (!CollectionUtils.isEmpty(elementAuditEntityList)) {
			LOGGER.debug("fetchSdmxAuditRecords with job processing id - " + jobProcessingId + " ,EBR file audit list size - " + elementAuditEntityList.size());
			elementAuditBeanList = new ArrayList<>();
			for (ElementAudit elementAudit : elementAuditEntityList) {
				ElementAuditBean elementAuditBean = new ElementAuditBean();
				SdmxElementAuditHelper.convertElementAuditEntityToBean(elementAudit, elementAuditBean);
				elementAuditBeanList.add(elementAuditBean);
			}
		} else {
			LOGGER.debug("fetchSdmxAuditRecords empty with job processing id - " + jobProcessingId);
		}
		LOGGER.debug("End fetchSdmxAuditRecords with job processing id - " + jobProcessingId);
		return elementAuditBeanList;
	}

	/**
	 * @param statusId
	 * @param fileDetailsId
	 * @param jobProcessingId
	 * @return
	 */
	public List<ElementAuditBean> fetchSdmxAuditRecordsWithStatus(Integer statusId, Integer recordToProcess, String jobProcessingId) {
		LOGGER.debug("Start fetchSdmxAuditRecords with job processing id - " + jobProcessingId);
		List<ElementAuditBean> elementAuditBeanList = null;
		// Filing Status
		List<ElementAudit> elementAuditEntityList = elementAuditRepo.fetchElementAuditRecordsWithStatus(statusId);

		if (!CollectionUtils.isEmpty(elementAuditEntityList)) {
			LOGGER.debug("fetchSdmxAuditRecords with job processing id - " + jobProcessingId + " ,EBR file audit list size - " + elementAuditEntityList.size());
			elementAuditBeanList = new ArrayList<>();
			for (ElementAudit elementAudit : elementAuditEntityList) {
				ElementAuditBean elementAuditBean = new ElementAuditBean();
				SdmxElementAuditHelper.convertElementAuditEntityToBean(elementAudit, elementAuditBean);
				elementAuditBeanList.add(elementAuditBean);
			}
		} else {
			LOGGER.debug("fetchSdmxAuditRecords empty with job processing id - " + jobProcessingId);
		}
		LOGGER.debug("End fetchSdmxAuditRecords with job processing id - " + jobProcessingId);
		return elementAuditBeanList;
	}

	/**
	 * @param elementAuditId
	 * @param statusId
	 * @param jobProcessingId
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void updateStatusOfSdmxElementAuditRecords(List<Long> elementAuditId, Integer statusId, String jobProcessingId) {
		LOGGER.debug("Start updateStatusOfSdmxElementAuditRecords with job processing id - " + jobProcessingId);
		FilingStatus status = new FilingStatus(statusId);
		elementAuditRepo.updateStatusOfSdmxElementAuditRecords(elementAuditId, status);
		LOGGER.debug("End updateStatusOfSdmxElementAuditRecords with job processing id - " + jobProcessingId);
	}

}
