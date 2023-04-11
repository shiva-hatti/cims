package com.iris.service.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;
import com.iris.dto.OwnerReturn;
import com.iris.exception.ServiceException;
import com.iris.model.Return;
import com.iris.model.ReturnRegulatorMapMod;
import com.iris.model.ReturnRegulatorMapping;
import com.iris.repository.ReturnRegulatorMapModRepo;
import com.iris.repository.ReturnRegulatorMappingRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.GeneralConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author pmohite
 *
 */

@Service
public class ReturnRegulatorMappingService implements GenericService<ReturnRegulatorMapping, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReturnRegulatorMappingService.class);

	@Autowired
	private ReturnRegulatorMappingRepo returnRegulatorMappingRepo;

	@Autowired
	DataSource datasource;

	@Autowired
	private ReturnServiceV2 returnServiceV2;

	@Autowired
	private ReturnRegulatorMapModRepo returnRegulatorMapModRepo;

	@Override
	public ReturnRegulatorMapping add(ReturnRegulatorMapping returnRegulatorMapping) throws ServiceException {
		try (Connection con = datasource.getConnection(); CallableStatement stmt = con.prepareCall(GeneralConstants.SP_RETURN_REGULATOR_MAPPING.getConstantVal());) {
			if (returnRegulatorMapping != null) {
				if (returnRegulatorMapping.getReturnIdArray() != null) {
					saveReturnRegulatorMapModHistory(returnRegulatorMapping);
					returnRegulatorMappingRepo.updateReturnRegulatorMappingByRegulatoIdAndReturnIds(returnRegulatorMapping.getRegulatorIdFk().getRegulatorId(), returnRegulatorMapping.getReturnIdArray(), new Date(), returnRegulatorMapping.getCreatedBy().getUserId());
					Set<String> returnIdArray = returnRegulatorMapping.getReturnEmailIdStringMap().keySet();
					for (String returnString : returnIdArray) {
						if (returnRegulatorMapping != null) {
							stmt.setLong(1, returnRegulatorMapping.getRegulatorIdFk().getRegulatorId());
							stmt.setString(2, returnString);
							stmt.setString(3, returnRegulatorMapping.getReturnEmailIdStringMap().get(returnString));
							stmt.setLong(4, returnRegulatorMapping.getCreatedBy().getUserId());
							stmt.registerOutParameter(5, Types.INTEGER);
							stmt.executeQuery();
							int number = stmt.getInt(5);
							if (number > 0) {
								returnRegulatorMapping = null;
							} else {
								LOGGER.debug("SP_RETURN_REGULATOR_MAPPING Procedure executed successfully.");
							}
						}
					}
				} else {
					saveReturnRegulatorMapModHistory(returnRegulatorMapping);
					returnRegulatorMappingRepo.updateReturnRegulatorMappingByRegulatoId(returnRegulatorMapping.getRegulatorIdFk().getRegulatorId(), new Date(), returnRegulatorMapping.getCreatedBy().getUserId());
				}
			} else {
				returnRegulatorMapping = null;
			}
		} catch (SQLException e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return returnRegulatorMapping;
	}

	private void saveReturnRegulatorMapModHistory(ReturnRegulatorMapping returnRegulatorMapping) {
		LOGGER.debug("ReturnRegulatorMapMod save method start.");
		Map<String, Object> columneMap = new HashMap<>();
		columneMap.put(ColumnConstants.IS_ACTIVE.getConstantVal(), Boolean.TRUE);
		columneMap.put(ColumnConstants.ROLEID.getConstantVal(), returnRegulatorMapping.getRoleId());
		columneMap.put(ColumnConstants.REGULATORID.getConstantVal(), returnRegulatorMapping.getRegulatorIdFk().getRegulatorId());
		columneMap.put(ColumnConstants.LANG_CODE.getConstantVal(), returnRegulatorMapping.getLangCode());
		List<Return> returnList = returnServiceV2.getDataByObject(columneMap, MethodConstants.GET_MAPPED_RETURN_LIST_BY_REGULATOR_ID.getConstantVal());
		ReturnRegulatorMapMod returnRegulatorMapMod = new ReturnRegulatorMapMod();
		returnRegulatorMapMod.setReturnMapped(new Gson().toJson(returnList));
		returnRegulatorMapMod.setRegulatorIdFk(returnRegulatorMapping.getRegulatorIdFk());
		returnRegulatorMapMod.setUpdatedOn(new Date());
		returnRegulatorMapMod.setUpdatedBy(returnRegulatorMapping.getCreatedBy());
		returnRegulatorMapModRepo.save(returnRegulatorMapMod);
		LOGGER.debug("ReturnRegulatorMapMod save method end.");
	}

	@Override
	public boolean update(ReturnRegulatorMapping entity) throws ServiceException {
		return false;
	}

	@Override
	public List<ReturnRegulatorMapping> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public ReturnRegulatorMapping getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnRegulatorMapping> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public List<ReturnRegulatorMapping> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public List<ReturnRegulatorMapping> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {

		try {
			Long roleId = null;
			Boolean isActive = false;
			Long regulatorId = null;
			String returnCode = null;

			for (String columnName : columnValueMap.keySet()) {
				if (columnName.equalsIgnoreCase(ColumnConstants.ROLEID.getConstantVal())) {
					roleId = (Long) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.IS_ACTIVE.getConstantVal())) {
					isActive = (Boolean) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.REGULATORID.getConstantVal())) {
					regulatorId = (Long) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.RETURN_CODE.getConstantVal())) {
					returnCode = (String) columnValueMap.get(columnName);
				}
			}
			if (methodName.equalsIgnoreCase(MethodConstants.GET_MAPPED_RETURN_LIST_BY_REGULATOR_ID.getConstantVal())) {
				if (isActive == null) {
					return returnRegulatorMappingRepo.getMappedReturnListByRegulatorId(roleId, regulatorId);
				} else {
					return returnRegulatorMappingRepo.getMappedReturnListByRegulatorIdAndIsActiveStatus(roleId, regulatorId, isActive);
				}
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_DATA_BY_RETURN_CODE.getConstantVal())) {
				return returnRegulatorMappingRepo.getMappedDataByReturnCode(returnCode);
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReturnRegulatorMapping> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return Collections.emptyList();
	}

	public List<ReturnRegulatorMapping> getMappedReturnListByRegulatorIdAndIsActiveStatus(Long roleId, Long regulatorId, Boolean isActive) {
		return returnRegulatorMappingRepo.getMappedReturnListByRegulatorIdAndIsActiveStatus(roleId, regulatorId, isActive);
	}

	public Set<OwnerReturn> getMappedReturnListByRegulatorIdAndIsActiveStatus(Set<Long> roleIdSet, Long regulatorId, Boolean isActive) {
		return returnRegulatorMappingRepo.getMappedReturnListByRegulatorIdAndIsActiveStatus(roleIdSet, regulatorId, isActive);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReturnRegulatorMapping> getAllDataFor(Class bean, Long id) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public void deleteData(ReturnRegulatorMapping bean) throws ServiceException {
	}

	public List<ReturnRegulatorMapping> findByReturnIdFkReturnIdAndIsActiveTrue(Long returnId) {
		try {
			return returnRegulatorMappingRepo.findByReturnIdFkReturnIdAndIsActiveTrue(returnId);
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	public List<Return> getReturnList(Map<String, Object> columnValueMap, String methodName) {
		return returnServiceV2.getDataByObject(columnValueMap, methodName);
	}
}
