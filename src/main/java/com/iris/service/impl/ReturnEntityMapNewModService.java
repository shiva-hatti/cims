package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.EntityBean;
import com.iris.model.ReturnEntityMappingNewMod;
import com.iris.repository.EntityRepo;
import com.iris.repository.ReturnEntityMapNewModRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

@Service
public class ReturnEntityMapNewModService implements GenericService<ReturnEntityMappingNewMod, Long> {

	@Autowired
	ReturnEntityMapNewModRepo returnEntityMapNewModRepo;

	@Autowired
	EntityRepo entityRepo;

	static final Logger logger = LogManager.getLogger(ReturnEntityMapNewModService.class);

	@Override
	public ReturnEntityMappingNewMod add(ReturnEntityMappingNewMod entity) throws ServiceException {
		try {
			return returnEntityMapNewModRepo.save(entity);
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.DEFAULT_ERROR.getErrorMessage(), e);
		}
	}

	@Override
	public boolean update(ReturnEntityMappingNewMod entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ReturnEntityMappingNewMod> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReturnEntityMappingNewMod getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReturnEntityMappingNewMod> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReturnEntityMappingNewMod> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReturnEntityMappingNewMod> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {

		try {
			Long entityId = null;
			Integer totalRecordCount = null;
			for (String columnName : columnValueMap.keySet()) {
				if (columnName.equalsIgnoreCase(ColumnConstants.ENTITYID.getConstantVal())) {
					entityId = (Long) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.TOTAL_RECORD_COUNT.getConstantVal())) {
					totalRecordCount = (Integer) columnValueMap.get(columnName);
				}
			}

			if (methodName.equalsIgnoreCase(MethodConstants.GET_ENT_RETURN_MAPPING_MOD_HISTORY_BY_ENTITY_ID.getConstantVal())) {
				return returnEntityMapNewModRepo.getEntityReturnModHistoryByEntityId(entityId, PageRequest.of(0, totalRecordCount));
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<ReturnEntityMappingNewMod> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReturnEntityMappingNewMod> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(ReturnEntityMappingNewMod bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@org.springframework.transaction.annotation.Transactional(rollbackFor = ServiceException.class)
	public boolean updateEntityReturnMappingAndSaveModificationHistory(EntityBean entityBean, ReturnEntityMappingNewMod returnEntityMappingNewMod) {
		try {
			entityRepo.save(entityBean);
			if (returnEntityMappingNewMod != null) {
				returnEntityMapNewModRepo.save(returnEntityMappingNewMod);
			}
			return true;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	public boolean addListObject(List<ReturnEntityMappingNewMod> returbEntityMappingNewMods) throws ServiceException {
		try {
			for (ReturnEntityMappingNewMod returnEntityMappingNewMod : returbEntityMappingNewMods) {
				returnEntityMapNewModRepo.save(returnEntityMappingNewMod);
			}
			return true;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.DEFAULT_ERROR.getErrorMessage(), e);
		}
	}
}
