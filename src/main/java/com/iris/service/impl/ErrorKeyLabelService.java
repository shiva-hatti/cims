/**
 * 
 */
package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.ErrorKeyLabel;
import com.iris.repository.ErrorKeyLabelRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author sajadhav
 *
 */

@Service
public class ErrorKeyLabelService implements GenericService<ErrorKeyLabel, Long> {

	@Autowired
	private ErrorKeyLabelRepo errorKeyLabelRepo;

	@Override
	public ErrorKeyLabel add(ErrorKeyLabel entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(ErrorKeyLabel entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ErrorKeyLabel> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ErrorKeyLabel getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ErrorKeyLabel> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		try {
			List<String> labelKeys = null;
			List<String> languageCode = null;

			for (String columnName : columnValueMap.keySet()) {
				if (columnValueMap.get(columnName) != null && columnValueMap.get(columnName).size() > 0) {
					if (columnName.equalsIgnoreCase(ColumnConstants.LABEL_KEY.getConstantVal())) {
						labelKeys = columnValueMap.get(columnName);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.LANGUAGE_CODE.getConstantVal())) {
						languageCode = columnValueMap.get(columnName);
					}
				}
			}
			if (methodName.equalsIgnoreCase(MethodConstants.GET_LABEL_DATA_BY_KEY_AND_LANG.getConstantVal())) {
				return errorKeyLabelRepo.getDataByLabelKeyAndLangCode(labelKeys, languageCode);
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<ErrorKeyLabel> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ErrorKeyLabel> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ErrorKeyLabel> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ErrorKeyLabel> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(ErrorKeyLabel bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
