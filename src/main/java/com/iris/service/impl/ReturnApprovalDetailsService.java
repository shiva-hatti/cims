package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.ReturnApprovalDetail;
import com.iris.repository.ReturnApprovalDetailsRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

@Service
public class ReturnApprovalDetailsService implements GenericService<ReturnApprovalDetail, Long> {

	@Autowired
	private ReturnApprovalDetailsRepo returnApprovalDetailRepo;

	private static final Logger LOGGER = LogManager.getLogger(ReturnApprovalDetailsService.class);

	@Override
	public ReturnApprovalDetail add(ReturnApprovalDetail entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(ReturnApprovalDetail entity) throws ServiceException {
		return false;
	}

	@Override
	public List<ReturnApprovalDetail> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public ReturnApprovalDetail getDataById(Long id) throws ServiceException {
		try {
			return returnApprovalDetailRepo.getDataByReturnApprovalDetailId(id);
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<ReturnApprovalDetail> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnApprovalDetail> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnApprovalDetail> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		try {
			List<Long> activityIds = null;
			List<Long> entityIds = null;
			List<String> returnCodes = null;
			List<Long> returnIds = null;
			Long userId = null;
			String langCode = null;

			for (String columnName : columnValueMap.keySet()) {
				if (columnValueMap.get(columnName) != null) {
					if (columnName.equalsIgnoreCase(ColumnConstants.ENTITYID.getConstantVal())) {
						entityIds = (List<Long>) columnValueMap.get(columnName);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.RETURN_CODE_LIST.getConstantVal())) {
						returnCodes = (List<String>) columnValueMap.get(columnName);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.RETURNID.getConstantVal())) {
						returnIds = (List<Long>) columnValueMap.get(columnName);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.USER_ID.getConstantVal())) {
						userId = (Long) columnValueMap.get(columnName);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.ACTIVITY_IDS.getConstantVal())) {
						activityIds = (List<Long>) columnValueMap.get(columnName);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.LANG_CODE.getConstantVal())) {
						langCode = (String) columnValueMap.get(columnName);
					}
				}
			}
			if (methodName.equalsIgnoreCase(MethodConstants.GET_PENDING_FOR_APPROVAL_DATA.getConstantVal())) {
				return returnApprovalDetailRepo.getPendignForApprovalData(activityIds, userId, entityIds, returnIds, langCode);
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_PENDING_FOR_APPROVAL_DATA_V2.getConstantVal())) {
				return returnApprovalDetailRepo.getPendignForApprovalDataV2(activityIds, userId, entityIds, returnCodes, langCode);
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<ReturnApprovalDetail> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<ReturnApprovalDetail> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(ReturnApprovalDetail bean) throws ServiceException {

	}

}
