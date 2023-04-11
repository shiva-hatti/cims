package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.WorkFlowMasterBean;
import com.iris.repository.WorkFlowMasterRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

@Service
public class WorkflowMasterService implements GenericService<WorkFlowMasterBean, Long> {

	@Autowired
	WorkFlowMasterRepo workFlowMasterRepo;

	@Override
	public WorkFlowMasterBean add(WorkFlowMasterBean entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(WorkFlowMasterBean entity) throws ServiceException {
		return false;
	}

	@Override
	public List<WorkFlowMasterBean> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public WorkFlowMasterBean getDataById(Long id) throws ServiceException {
		return workFlowMasterRepo.getWorkflowDataById(id);
	}

	@Override
	public List<WorkFlowMasterBean> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<WorkFlowMasterBean> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<WorkFlowMasterBean> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		try {
			List<Long> workflowIds = null;
			for (String columnName : columnValueMap.keySet()) {
				if (columnName.equalsIgnoreCase(ColumnConstants.WORKFLOW_MASTER_IDS.getConstantVal())) {
					workflowIds = (List<Long>) columnValueMap.get(columnName);
				}
			}
			if (methodName.equalsIgnoreCase(MethodConstants.GET_WORKFLOW_BY_RETURN_ID_AND_CHANNEL_ID.getConstantVal())) {
				return workFlowMasterRepo.findByWorkflowIdIn(workflowIds);
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<WorkFlowMasterBean> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<WorkFlowMasterBean> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(WorkFlowMasterBean bean) throws ServiceException {

	}

}
