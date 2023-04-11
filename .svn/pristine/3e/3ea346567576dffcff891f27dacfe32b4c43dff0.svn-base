package com.iris.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.CityMaster;
import com.iris.model.PincodeMaster;
import com.iris.repository.CityMasterRepo;
import com.iris.repository.PincodeMasterRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

@Service
public class PincodeMasterService implements GenericService<PincodeMaster, Long> {

	@Autowired
	PincodeMasterRepo pincodeMasterRepo;

	@Override
	public PincodeMaster add(PincodeMaster entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(PincodeMaster entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<PincodeMaster> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PincodeMaster getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PincodeMaster> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		try {
			List<String> cityIdList = null;
			if (methodName.equalsIgnoreCase(MethodConstants.GET_PINCODE_LIST_BY_CITY_ID.getConstantVal())) {
				cityIdList = columnValueMap.get(ColumnConstants.CITY_ID.getConstantVal());
				return pincodeMasterRepo.getpincodeListByCity(Long.parseLong(cityIdList.get(0)));
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_PINCODE_LIST_BY_CITY_NAME.getConstantVal())) {
				cityIdList = columnValueMap.get(ColumnConstants.CITY_NAME.getConstantVal());
				return pincodeMasterRepo.getpincodeListByCityName(cityIdList.get(0));
			}
			return Collections.emptyList();
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<PincodeMaster> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PincodeMaster> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	//	@Override
	//	public List<PincodeMaster> getActiveDataFor(Class bean, Long id) throws ServiceException {
	//		// TODO Auto-generated method stub
	//		return null;
	//	}

	@Override
	public List<PincodeMaster> getActiveDataFor(Class bean, Long id) throws ServiceException {
		try {
			if (bean.equals(PincodeMaster.class) && id == null) {
				return pincodeMasterRepo.findAllActiveData();
			}
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return null;
	}

	@Override
	public List<PincodeMaster> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(PincodeMaster bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
