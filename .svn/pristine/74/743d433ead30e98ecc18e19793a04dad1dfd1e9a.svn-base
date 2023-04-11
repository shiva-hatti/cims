package com.iris.rbrToEbr.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.rbrToEbr.entity.EbrRbrFlow;
import com.iris.rbrToEbr.repo.EbrRbrFlowRepo;
import com.iris.service.GenericService;

@Service
public class CtlEbrRbrFlowService implements GenericService<EbrRbrFlow, Long> {

	@Autowired
	private EbrRbrFlowRepo ebrRbrFlowRepo;

	private static final Logger LOGGER = LogManager.getLogger(CtlEbrRbrFlowService.class);

	@Override
	public EbrRbrFlow add(EbrRbrFlow entity) throws ServiceException {
		return ebrRbrFlowRepo.save(entity);
	}

	@Override
	public boolean update(EbrRbrFlow entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<EbrRbrFlow> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EbrRbrFlow getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EbrRbrFlow> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EbrRbrFlow> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EbrRbrFlow> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<EbrRbrFlow> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<EbrRbrFlow> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(EbrRbrFlow bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	/**
	 * @param returnCode
	 * @param entityCode
	 * @param reportingDate
	 * @param auditStatus
	 * @return
	 */
	public Integer checkRecordExistWithStatusNEntityCode(String entityCode, Date reportingDate, int auditStatus) {
		return ebrRbrFlowRepo.checkRecordExistWithStatusNEntityCode(entityCode, reportingDate, auditStatus);

	}

}
