/**
 * 
 */
package com.iris.sdmx.status.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iris.exception.ServiceException;
import com.iris.sdmx.status.bean.ActionStatusBean;
import com.iris.sdmx.status.entity.ActionStatus;
import com.iris.sdmx.status.helper.ActionStatusHelper;
import com.iris.sdmx.status.repo.ActionStatusRepo;
import com.iris.service.GenericService;

/**
 * @author apagaria
 *
 */
@Service
@Transactional(readOnly = true)
public class ActionStatusService implements GenericService<ActionStatus, Long> {

	@Autowired
	private ActionStatusRepo actionStatusRepo;

	@Override
	public ActionStatus add(ActionStatus entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(ActionStatus entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ActionStatus> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionStatus getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ActionStatus> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ActionStatus> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ActionStatus> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ActionStatus> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ActionStatus> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(ActionStatus bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	/**
	 * @param isActive
	 * @return
	 */
	public List<ActionStatusBean> findByActiveStatus(Boolean isActive) {
		List<ActionStatusBean> actionStatusBeans = null;
		List<ActionStatus> actionStatusList = actionStatusRepo.findByActiveStatus(isActive);
		if (!CollectionUtils.isEmpty(actionStatusList)) {
			actionStatusBeans = new ArrayList<>();
			for (ActionStatus actionStatus : actionStatusList) {
				ActionStatusBean actionStatusBean = new ActionStatusBean();
				// convert entity to Bean
				ActionStatusHelper.convertEntityToBean(actionStatus, actionStatusBean);
				actionStatusBeans.add(actionStatusBean);
			}
		}
		return actionStatusBeans;
	}

	/**
	 * @param actionName
	 * @return
	 */
	public Long findIdByActionName(String actionName) {
		Long actionId = 0L;
		ActionStatus actionStatus = actionStatusRepo.findByStatusActionName(actionName);
		if (actionStatus != null) {
			actionId = actionStatus.getActionId();
		}
		return actionId;
	}

}
