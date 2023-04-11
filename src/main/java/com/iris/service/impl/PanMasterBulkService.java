/**
 * 
 */
package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.PanMasterBulk;
import com.iris.repository.PanMasterBulkRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author Siddique
 *
 */
@Service
public class PanMasterBulkService implements GenericService<PanMasterBulk, Long> {

	@Autowired
	private PanMasterBulkRepo panMasterBulkRepo;

	@Override
	public PanMasterBulk add(PanMasterBulk entity) throws ServiceException {
		// TODO Auto-generated method stub
		return panMasterBulkRepo.save(entity);
	}

	@Override
	public boolean update(PanMasterBulk entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<PanMasterBulk> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PanMasterBulk getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PanMasterBulk> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		try {
			if (methodName.equalsIgnoreCase(MethodConstants.GET_UNPROCESSED_DATA_AND_UPDATE_IS_PROCESSED_FLAG.getConstantVal())) {
				List<PanMasterBulk> panMasterBulkList = panMasterBulkRepo.getUnprocessedData();
				//				PanStatus panStatus = null;
				//				for(PanMasterBulk panMasterBulk : panMasterBulkList) {
				//					panStatus = new PanStatus();
				//					panStatus.setPanStatusId(2l);
				//					panMasterBulk.setStatusId(panStatus);
				//					panMasterBulkRepo.save(panMasterBulk);
				//				}

				return panMasterBulkList;
			}
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return null;
	}

	@Override
	public List<PanMasterBulk> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PanMasterBulk> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		try {
			String langCode = null;
			String entityCode = null;

			for (String columnName : columnValueMap.keySet()) {
				if (columnName.equalsIgnoreCase(ColumnConstants.ENT_CODE.getConstantVal())) {
					entityCode = (String) columnValueMap.get(columnName);
				} else if (columnName.equalsIgnoreCase(ColumnConstants.LANG_CODE.getConstantVal())) {
					langCode = (String) columnValueMap.get(columnName);
				}
			}

			if (methodName.equalsIgnoreCase(MethodConstants.GET_DATA_BY_ENTITY_CODE_AND_LANG_CODE.getConstantVal())) {
				return panMasterBulkRepo.getDataByEntityCodeAndLangCode(entityCode, langCode);
			}

		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
		return null;
	}

	@Override
	public List<PanMasterBulk> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PanMasterBulk> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(PanMasterBulk bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
