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
import com.iris.sdmx.status.bean.AdminStatusBean;
import com.iris.sdmx.status.entity.AdminStatus;
import com.iris.sdmx.status.helper.AdminStatusHelper;
import com.iris.sdmx.status.repo.AdminStatusRepo;
import com.iris.service.GenericService;

/**
 * @author apagaria
 *
 */
@Service
@Transactional(readOnly = true)
public class AdminStatusService implements GenericService<AdminStatus, Long> {

	@Autowired
	private AdminStatusRepo adminStatusRepo;

	@Override
	public AdminStatus add(AdminStatus entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(AdminStatus entity) throws ServiceException {
		return false;
	}

	@Override
	public List<AdminStatus> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public AdminStatus getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<AdminStatus> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<AdminStatus> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<AdminStatus> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<AdminStatus> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<AdminStatus> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(AdminStatus bean) throws ServiceException {
	}

	/**
	 * @param isActive
	 * @return
	 */
	public List<AdminStatusBean> findByActiveStatus(Boolean isActive) {
		List<AdminStatusBean> adminStatusBeans = null;
		List<AdminStatus> adminStatusList = adminStatusRepo.findByActiveStatus(isActive);
		if (!CollectionUtils.isEmpty(adminStatusList)) {
			adminStatusBeans = new ArrayList<>();
			for (AdminStatus adminStatus : adminStatusList) {
				AdminStatusBean adminStatusBean = new AdminStatusBean();
				// convert entity to Bean
				AdminStatusHelper.convertEntityToBean(adminStatus, adminStatusBean);
				adminStatusBeans.add(adminStatusBean);
			}
		}
		return adminStatusBeans;
	}

	/**
	 * @param statusTechCode
	 * @return
	 */
	public Long findIdByStatusTechCode(String statusTechCode) {
		Long statusCodeId = 0L;
		AdminStatus adminStatus = adminStatusRepo.findByStatusTechCode(statusTechCode);
		if (adminStatus != null) {
			statusCodeId = adminStatus.getAdminStatusId();
		}
		return statusCodeId;
	}

}
