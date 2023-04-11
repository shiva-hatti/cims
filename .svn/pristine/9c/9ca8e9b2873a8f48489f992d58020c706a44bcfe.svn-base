package com.iris.service.impl;

import static org.hamcrest.CoreMatchers.isA;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.Return;
import com.iris.repository.ReturnRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

@Service
public class ReturnServiceV2 implements GenericService<Return, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReturnServiceV2.class);

	@Autowired
	ReturnRepo returnRepo;

	@Override
	public Return add(Return entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(Return entity) throws ServiceException {
		return false;
	}

	@Override
	public List<Return> getDataByIds(Long[] ids) throws ServiceException {
		return new ArrayList<>();
	}

	@Override
	public List<Return> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<Return> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<Return> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<Return> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(Return bean) throws ServiceException {
	}

	@Override
	public Return getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<Return> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		try {
			Boolean isActive = false;
			String langCode = null;
			Long returnGroupId = null;
			Long roleId = null;
			Long regulatorId = null;
			Long languageId = null;

			for (String columnName : columnValueMap.keySet()) {
				if (columnName.equalsIgnoreCase(ColumnConstants.IS_ACTIVE.getConstantVal())) {
					isActive = (Boolean) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.LANG_CODE.getConstantVal())) {
					langCode = (String) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.RETURN_GROUP_ID.getConstantVal())) {
					returnGroupId = (Long) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.ROLEID.getConstantVal())) {
					roleId = (Long) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.REGULATORID.getConstantVal())) {
					regulatorId = (Long) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.LANG_ID.getConstantVal())) {
					languageId = (Long) columnValueMap.get(columnName);
				}
			}
			if (methodName.equalsIgnoreCase(MethodConstants.GET_RETURN_LIST_WITH_OUT_RETURN_GROUP.getConstantVal())) {
				return returnRepo.getReturnListWithoutReturnGroup(isActive, langCode);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_RETURN_LIST_MAPPED_TO_RETURN_GROUP.getConstantVal())) {
				return returnRepo.getReturnListForReturnGroup(isActive, langCode, returnGroupId);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_MAPPED_RETURN_LIST_BY_REGULATOR_ID.getConstantVal())) {
				LOGGER.debug("com.iris.service.impl.ReturnServiceV2.getDataByObject(Map<String, Object>, String) - GET_MAPPED_RETURN_LIST_BY_REGULATOR_ID  isActive - " + isActive + " , languageId - " + languageId + " , regulatorId - " + regulatorId + " ,role Id - " + roleId);
				return returnRepo.getReturnListMappedRegulator(isActive, languageId, regulatorId, roleId);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_UNMAPPED_RETURN_LIST_BY_REGULATOR_ID.getConstantVal())) {
				return returnRepo.getReturnListUnMappedRegulator(isActive, langCode, roleId);
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

}
