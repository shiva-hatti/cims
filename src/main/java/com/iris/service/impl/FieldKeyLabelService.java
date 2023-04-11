/**
 * 
 */
package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.FieldKeyLabel;
import com.iris.repository.FieldKeyLabelRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author sajadhav
 *
 */

@Service
public class FieldKeyLabelService implements GenericService<FieldKeyLabel, Long> {

	@Autowired
	private FieldKeyLabelRepo fieldKeyLabelRepo;

	@Override
	public FieldKeyLabel add(FieldKeyLabel entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(FieldKeyLabel entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<FieldKeyLabel> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FieldKeyLabel getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FieldKeyLabel> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
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
				return fieldKeyLabelRepo.getDataByLabelKeyAndLangCode(labelKeys, languageCode);
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<FieldKeyLabel> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FieldKeyLabel> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FieldKeyLabel> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FieldKeyLabel> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(FieldKeyLabel bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
