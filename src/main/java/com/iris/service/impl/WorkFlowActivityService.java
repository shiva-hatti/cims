package com.iris.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.WorkFlowActivity;
import com.iris.repository.WorkFlowActivityRepo;
import com.iris.service.GenericService;

@Service
public class WorkFlowActivityService implements GenericService<WorkFlowActivity, Long> {

	@Autowired
	WorkFlowActivityRepo repo;

	@Override
	public WorkFlowActivity add(WorkFlowActivity entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(WorkFlowActivity entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<WorkFlowActivity> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorkFlowActivity getDataById(Long id) throws ServiceException {
		Optional<WorkFlowActivity> workFlowActivityOptional = repo.findById(id);
		if (workFlowActivityOptional.isPresent()) {
			return workFlowActivityOptional.get();
		} else {
			return null;
		}
	}

	@Override
	public List<WorkFlowActivity> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WorkFlowActivity> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WorkFlowActivity> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WorkFlowActivity> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return repo.findByIsActiveTrue();
	}

	@Override
	public List<WorkFlowActivity> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(WorkFlowActivity bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	public List<WorkFlowActivity> getWorkFlowisDept(Boolean isDept) {
		if (isDept) {
			return repo.findByIsApplicableForDeptTrue();
		}
		return repo.findByIsApplicableForEntityTrue();

	}

}
