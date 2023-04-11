package com.iris.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.Holiday;
import com.iris.repository.HolidayRepository;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

@Service
public class HolidayService implements GenericService<Holiday, Long> {

	@Autowired
	HolidayRepository holidayRepo;

	@Override
	public Holiday add(Holiday entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(Holiday entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Holiday> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Holiday getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Holiday> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Holiday> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Holiday> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		try {
			Date startYear = null;
			Date endYear = null;

			for (String columnName : columnValueMap.keySet()) {
				if (columnValueMap.get(columnName) != null) {
					if (columnName.equalsIgnoreCase(ColumnConstants.FROM_YEAR.getConstantVal())) {
						startYear = (Date) columnValueMap.get(columnName);
					} else if (columnName.equalsIgnoreCase(ColumnConstants.TO_YEAR.getConstantVal())) {
						endYear = (Date) columnValueMap.get(columnName);
					}
				}
			}
			if (methodName.equalsIgnoreCase(MethodConstants.GET_ACTIVE_HOLIDAY_BY_YEAR.getConstantVal())) {
				return holidayRepo.findActiveHoliday(startYear, endYear);
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<Holiday> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Holiday> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(Holiday bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
