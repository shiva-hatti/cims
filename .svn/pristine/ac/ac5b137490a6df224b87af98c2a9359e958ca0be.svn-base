package com.iris.nbfc.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.nbfc.model.NbfcCorRegistrationStatus;
import com.iris.nbfc.repository.NbfcCorRegistrationStatusRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;

/**
 * @author Shivabasava Hatti
 *
 */
@Service
public class NbfcCorRegistrationStatusService implements GenericService<NbfcCorRegistrationStatus, Long> {

	@Autowired
	NbfcCorRegistrationStatusRepo nbfcCorRegistrationStatusRepo;

	@Override
	public NbfcCorRegistrationStatus add(NbfcCorRegistrationStatus entity) throws ServiceException {
		entity = nbfcCorRegistrationStatusRepo.save(entity);
		return entity;
	}

	@Override
	public boolean update(NbfcCorRegistrationStatus entity) throws ServiceException {
		return false;
	}

	@Override
	public List<NbfcCorRegistrationStatus> getDataByIds(Long[] ids) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public NbfcCorRegistrationStatus getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<NbfcCorRegistrationStatus> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {

		return Collections.emptyList();
	}

	@Override
	public List<NbfcCorRegistrationStatus> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		List<NbfcCorRegistrationStatus> nbfcCorRegistrationStatusList = new ArrayList<>();
		List<Long> nbfcEntityIdList = null;
		List<Long> userIdList = null;
		try {
			nbfcEntityIdList = columnValueMap.get(ColumnConstants.ENTITY_ID_LIST.getConstantVal());
			userIdList = columnValueMap.get(ColumnConstants.USER_ID.getConstantVal());
			nbfcCorRegistrationStatusList = nbfcCorRegistrationStatusRepo.getCorRegistrationStatusByUserIdAndNbfcEntityId(nbfcEntityIdList.get(0), userIdList.get(0));
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return nbfcCorRegistrationStatusList;
	}

	@Override
	public List<NbfcCorRegistrationStatus> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return Collections.emptyList();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<NbfcCorRegistrationStatus> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return Collections.emptyList();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<NbfcCorRegistrationStatus> getAllDataFor(Class bean, Long id) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public void deleteData(NbfcCorRegistrationStatus bean) throws ServiceException {
	}

	public void deleteDetails(Map<String, List<Long>> columnValueMap) throws ServiceException {

	}
}
