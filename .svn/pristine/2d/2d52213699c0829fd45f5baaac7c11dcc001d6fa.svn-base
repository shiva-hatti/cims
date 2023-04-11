package com.iris.nbfc.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.NSDLPanVerif;
import com.iris.nbfc.repository.NsdlPanVerificationRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author Siddique
 *
 */

@Service
public class NsdlPanVerficationService implements GenericService<NSDLPanVerif, Long> {

	@Autowired
	NsdlPanVerificationRepo nsdlPanVerificationRepo;

	@Override
	public NSDLPanVerif add(NSDLPanVerif entity) throws ServiceException {
		// TODO Auto-generated method stub
		return nsdlPanVerificationRepo.save(entity);
	}

	@Override
	public boolean update(NSDLPanVerif entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<NSDLPanVerif> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NSDLPanVerif getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NSDLPanVerif> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NSDLPanVerif> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NSDLPanVerif> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {

		try {
			Boolean subTaskStatus = false;
			String moduleName = null;
			Date schedulerLastRunTime = null;
			List<String> statusList = null;

			for (String columnName : columnValueMap.keySet()) {
				if (columnName.equalsIgnoreCase(ColumnConstants.MODULE_NAME.getConstantVal())) {
					moduleName = (String) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.SUB_TASK_STATUS.getConstantVal())) {
					subTaskStatus = (Boolean) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.SCHEDULER_LAST_RUNTIME.getConstantVal())) {
					schedulerLastRunTime = (Date) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.STATUS_LIST.getConstantVal())) {
					statusList = (List<String>) columnValueMap.get(columnName);
				}
			}
			if (methodName.equalsIgnoreCase(MethodConstants.GET_DATA_BY_SCHEDULER_LAST_RUNTIME.getConstantVal())) {
				return nsdlPanVerificationRepo.getDataBySchedulerLastRunTime(moduleName, subTaskStatus, schedulerLastRunTime, statusList);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_DATA_BY_WITHOUT_SCHEDULER_LAST_RUNTIME.getConstantVal())) {
				return nsdlPanVerificationRepo.getDataByWithoutSchedulerLastRunTime(moduleName, subTaskStatus, statusList);
			}

			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<NSDLPanVerif> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NSDLPanVerif> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(NSDLPanVerif bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	public void updateSubTaskStatus(List<Long> nsdlPanVarifyIdList, Date nsdlVeriDate) {

		nsdlPanVerificationRepo.updateSubTaskStatus(nsdlPanVarifyIdList, nsdlVeriDate);

	}

}
