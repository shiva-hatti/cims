/**
 * 
 */
package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.iris.exception.ServiceException;
import com.iris.model.BranchMasterDomestic;
import com.iris.repository.BranchMasterDomesticRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author Siddique H Khan
 *
 */

@Service
public class BranchMasterDomesticService implements GenericService<BranchMasterDomestic, Long> {

	@Autowired
	private BranchMasterDomesticRepo branchMasterDomesticRepo;

	@Override
	public BranchMasterDomestic add(BranchMasterDomestic entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(BranchMasterDomestic entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<BranchMasterDomestic> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BranchMasterDomestic getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BranchMasterDomestic> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BranchMasterDomestic> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BranchMasterDomestic> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		try {
			String branchWorkingCodes = null;

			if (columnValueMap != null) {
				for (String columnName : columnValueMap.keySet()) {
					if (columnValueMap.get(columnName) != null) {
						if (columnName.equalsIgnoreCase(ColumnConstants.BRANCH_WORKING_CODE.getConstantVal())) {
							branchWorkingCodes = (String) columnValueMap.get(columnName);
						}
					}
				}

				if (methodName.equalsIgnoreCase(MethodConstants.GET_DATA_BY_BRANCH_WORKING_CODE.getConstantVal())) {
					if (StringUtils.isNotBlank(branchWorkingCodes)) {
						return branchMasterDomesticRepo.getBranchMasterByBranchWorkingCode(branchWorkingCodes.toString());
					}
				}

			}
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}

		return null;
	}

	@Override
	public List<BranchMasterDomestic> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BranchMasterDomestic> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(BranchMasterDomestic bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	// Added By psahoo
	public List<BranchMasterDomestic> getAllDataForBranchDomestic(Class bean, Long id) throws ServiceException {

		try {
			if (bean.equals(BranchMasterDomestic.class) && id == null) {
				return branchMasterDomesticRepo.getBranchMasterList();
			}
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return null;
	}

}
