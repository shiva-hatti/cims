package com.iris.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.ActivityApplicableMenu;
import com.iris.repository.ActivityApplicableMenuRepo;
import com.iris.service.GenericService;

@Service
public class ActivityApplicableMenuServie implements GenericService<ActivityApplicableMenu, Long> {

	@Autowired
	private ActivityApplicableMenuRepo repo;

	@Override
	public ActivityApplicableMenu add(ActivityApplicableMenu entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(ActivityApplicableMenu entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ActivityApplicableMenu> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActivityApplicableMenu getDataById(Long id) throws ServiceException {
		Optional<ActivityApplicableMenu> activityAppliOptional = repo.findById(id);
		if (activityAppliOptional.isPresent()) {
			return activityAppliOptional.get();
		} else {
			return null;
		}
	}

	@Override
	public List<ActivityApplicableMenu> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ActivityApplicableMenu> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ActivityApplicableMenu> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ActivityApplicableMenu> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ActivityApplicableMenu> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(ActivityApplicableMenu bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	public List<ActivityApplicableMenu> getActivityApplicableMenu(Boolean isDept) {
		if (isDept) {
			return repo.findByActivityIdFkIsApplicableForDeptTrue();
		}
		return repo.findByActivityIdFkIsApplicableForEntityTrue();
	}

}
