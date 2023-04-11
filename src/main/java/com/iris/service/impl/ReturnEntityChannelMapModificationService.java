/**
 * 
 */
package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.ReturnEntityChannelMapModification;
import com.iris.repository.ReturnEntityChannelMapModificationRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author sikhan
 *
 */
@Service
public class ReturnEntityChannelMapModificationService implements GenericService<ReturnEntityChannelMapModification, Long> {

	@Autowired
	private ReturnEntityChannelMapModificationRepo returnEntityChannelMapModificationRepo;

	@Override
	public ReturnEntityChannelMapModification add(ReturnEntityChannelMapModification entity) throws ServiceException {
		try {
			return returnEntityChannelMapModificationRepo.save(entity);
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public boolean update(ReturnEntityChannelMapModification entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ReturnEntityChannelMapModification> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReturnEntityChannelMapModification getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReturnEntityChannelMapModification> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReturnEntityChannelMapModification> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReturnEntityChannelMapModification> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		try {
			Long entityId = null;
			Long returnId = null;
			for (String columnName : columnValueMap.keySet()) {
				if (columnName.equalsIgnoreCase(ColumnConstants.ENTITYID.getConstantVal())) {
					entityId = (Long) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.RETURNID.getConstantVal())) {
					returnId = (Long) columnValueMap.get(columnName);
				}
			}
			if (methodName.equalsIgnoreCase(MethodConstants.GET_MOD_HIST_BY_ENTITY_ID_AND_RETURN_ID.getConstantVal())) {
				Pageable pageable = PageRequest.of(0, 10);
				return returnEntityChannelMapModificationRepo.getModHistoryData(entityId, returnId, pageable);

			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<ReturnEntityChannelMapModification> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReturnEntityChannelMapModification> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(ReturnEntityChannelMapModification bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
