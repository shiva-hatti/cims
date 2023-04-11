package com.iris.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.dateutility.enums.DateConstants;
import com.iris.dateutility.util.DateManip;
import com.iris.exception.ServiceException;
import com.iris.model.UnlockingRequest;
import com.iris.repository.UnlockRequestRepository;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

@Service
public class UnlockRequestServiceV2 implements GenericService<UnlockingRequest, Long> {
	@Autowired
	private UnlockRequestRepository unlockingRequestRepo;

	@Override
	public UnlockingRequest add(UnlockingRequest entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(UnlockingRequest entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<UnlockingRequest> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UnlockingRequest getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UnlockingRequest> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UnlockingRequest> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UnlockingRequest> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		try {
			Long langId = null;
			Long regulatorId = null;
			String entityCode = null;
			String langCode = null;
			Long roleId = null;
			Long entityId = null;
			List<Long> returnList = null;
			Long userId = null;
			List<Integer> unlockStatusIdList = null;
			List<Long> entityList = null;
			Date startDate = null;
			Date endDate = null;
			List<Long> subCatIdList = null;
			Long subCategoryId = null;
			List<String> subCatCodeList = null;
			List<String> categoryCodeList = null;
			List<String> entityCodeList = null;

			for (String columnName : columnValueMap.keySet()) {
				if (columnName.equalsIgnoreCase(ColumnConstants.LANG_ID.getConstantVal())) {
					langId = (Long) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.REGULATORID.getConstantVal())) {
					regulatorId = (Long) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.ENT_CODE.getConstantVal())) {
					entityCode = (String) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.ROLEID.getConstantVal())) {
					roleId = (Long) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.LANG_CODE.getConstantVal())) {
					langCode = (String) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.ENTITYID.getConstantVal())) {
					entityId = (Long) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.RETURN_ID_LIST.getConstantVal())) {
					returnList = (List<Long>) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.USER_ID.getConstantVal())) {
					userId = (Long) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.STARTDATE.getConstantVal())) {
					startDate = DateManip.convertStringToDate(columnValueMap.get(columnName).toString(), DateConstants.YYYY_MM_DD.getDateConstants());
				} else if (columnName.equalsIgnoreCase(ColumnConstants.ENDDATE.getConstantVal())) {
					endDate = DateManip.convertStringToDate(columnValueMap.get(columnName).toString(), DateConstants.YYYY_MM_DD.getDateConstants());
				} else if (columnName.equalsIgnoreCase(ColumnConstants.UNLOCK_STATUS_ID_LIST.getConstantVal())) {
					unlockStatusIdList = (List<Integer>) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.ENTITY_ID_LIST.getConstantVal())) {
					entityList = (List<Long>) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.SUB_CATEGORY_ID.getConstantVal())) {
					if (columnValueMap.get(columnName) != null && columnValueMap.get(columnName) != "") {
						subCategoryId = Long.parseLong(columnValueMap.get(columnName) + "");
					} else {
						subCategoryId = null;
					}
				} else if (columnName.equalsIgnoreCase(ColumnConstants.SUB_CATEGORY_ID_LIST.getConstantVal())) {
					subCatIdList = (List<Long>) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.SUB_CATEGORY_CODE_LIST.getConstantVal())) {
					subCatCodeList = (List<String>) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.CATEGORY_CODE_LIST.getConstantVal())) {
					categoryCodeList = (List<String>) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.ENTITY_CODE_LIST.getConstantVal())) {
					entityCodeList = (List<String>) columnValueMap.get(columnName);
				}
			}
			if (methodName.equalsIgnoreCase(MethodConstants.GET_PENDING_UNLOCK_REQUEST.getConstantVal())) {
				return unlockingRequestRepo.getDataByLangIdAndRegId(langId, regulatorId, userId);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_PENDING_UNLOCK_REQUEST_FOR_APPROVAL.getConstantVal())) {
				if (subCatCodeList == null) {
					return unlockingRequestRepo.getDataByLangIdAndRegIdEntityCode(langId, userId, returnList, entityCodeList, startDate, endDate);
				} else {
					return unlockingRequestRepo.getDataByLangIdAndRegIdEntitySubCatCode(langId, userId, returnList, entityCodeList, startDate, endDate, subCatCodeList);
				}
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_UNLOCK_REQUEST_LIST_FOR_REG_USER.getConstantVal())) {
				return unlockingRequestRepo.getDataByEntCodeAndLangCodeAndRoleId(entityCode, roleId, langCode);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_UNLOCK_REQUEST_DATA.getConstantVal())) {
				if (subCatCodeList == null) {
					if (regulatorId == null) {
						return unlockingRequestRepo.getUnlockRequestDataByLangCodeAndEntityCode(langCode, returnList, entityCodeList);
					} else {
						return unlockingRequestRepo.getUnlockRequestDataByLangCodeAndEntityCode(langCode, userId, returnList, entityCodeList, startDate, endDate, unlockStatusIdList);
					}
				} else {
					return unlockingRequestRepo.getUnlockRequestDataByLangCodeAndEntityCodeSubCatCode(langCode, userId, returnList, entityCodeList, startDate, endDate, subCatCodeList, unlockStatusIdList);
				}
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<UnlockingRequest> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UnlockingRequest> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(UnlockingRequest bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
