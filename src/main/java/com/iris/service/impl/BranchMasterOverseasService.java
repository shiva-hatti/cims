package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.BranchMasterOverseas;
import com.iris.repository.BranchMasterOverseasRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

@Service
public class BranchMasterOverseasService implements GenericService<BranchMasterOverseas, Long> {

	@Autowired
	private BranchMasterOverseasRepo branchMasterOverseasRepo;

	@Override
	public BranchMasterOverseas add(BranchMasterOverseas entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(BranchMasterOverseas entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<BranchMasterOverseas> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BranchMasterOverseas getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BranchMasterOverseas> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BranchMasterOverseas> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BranchMasterOverseas> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		try {
			String countryCode = null;
			String entityCode = null;

			if (columnValueMap != null) {
				for (String columnName : columnValueMap.keySet()) {
					if (columnValueMap.get(columnName) != null) {
						if (columnName.equalsIgnoreCase(ColumnConstants.COUNTRY_CODE.getConstantVal())) {
							countryCode = (String) columnValueMap.get(columnName);
						} else if (columnName.equalsIgnoreCase(ColumnConstants.ENTITY_CODE.getConstantVal())) {
							entityCode = (String) columnValueMap.get(columnName);
						}
					}
				}

				if (methodName.equalsIgnoreCase(MethodConstants.GET_DATA_BY_COUNTRY_CODE.getConstantVal())) {
					if (StringUtils.isNotBlank(countryCode) && StringUtils.isNotBlank(entityCode)) {
						return branchMasterOverseasRepo.getBranchMasterByCountryCode(countryCode.toString(), entityCode.toString());
					}
				}

			}
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}

		return null;
	}

	@Override
	public List<BranchMasterOverseas> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BranchMasterOverseas> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(BranchMasterOverseas bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	//Added  By psahoo
	public List<BranchMasterOverseas> getAllDataForBranchOverseas(Class bean, String bankCode) throws ServiceException {

		try {
			if (bean.equals(BranchMasterOverseas.class) && bankCode != null) {
				return branchMasterOverseasRepo.getCountryBranchMasterListByEntityCode(bankCode);
			}
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return null;
	}

}
