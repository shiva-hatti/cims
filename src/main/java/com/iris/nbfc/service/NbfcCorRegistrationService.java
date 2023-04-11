/**
 * 
 */
package com.iris.nbfc.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.nbfc.model.NbfcCorRegistrationBean;
import com.iris.nbfc.repository.NbfcCorRegistrationRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author Siddique
 *
 */

@Service
public class NbfcCorRegistrationService implements GenericService<NbfcCorRegistrationBean, Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(NbfcCorRegistrationService.class);

	@Autowired
	NbfcCorRegistrationRepo nbfcCorRegistrationRepo;

	@Override
	public NbfcCorRegistrationBean add(NbfcCorRegistrationBean entity) throws ServiceException {
		entity = nbfcCorRegistrationRepo.save(entity);
		return entity;
	}

	@Override
	public boolean update(NbfcCorRegistrationBean entity) throws ServiceException {

		try {
			nbfcCorRegistrationRepo.save(entity);
			return true;
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.toString(), e);
		}
	}

	@Override
	public List<NbfcCorRegistrationBean> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return nbfcCorRegistrationRepo.getDataByIds(ids);
	}

	@Override
	public NbfcCorRegistrationBean getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NbfcCorRegistrationBean> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {

		try {
			List<String> panNumber = null;
			if (methodName.equalsIgnoreCase(MethodConstants.GET_BORROWER_NAME_BY_PAN_NUMBER.getConstantVal())) {
				panNumber = columnValueMap.get(ColumnConstants.PAN_NUMBER.getConstantVal());
				return nbfcCorRegistrationRepo.fetchBorrowerNameData(panNumber.get(0));
			} else if (methodName.equalsIgnoreCase(MethodConstants.GET_BORROWER_NAME_BY_PAN_NUMBER_AND_REJECTED_STATUS.getConstantVal())) {
				panNumber = columnValueMap.get(ColumnConstants.PAN_NUMBER.getConstantVal());
				return nbfcCorRegistrationRepo.fetchBorrowerNameDataWithRejectedStatus(panNumber.get(0));
			}
			return Collections.emptyList();
		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<NbfcCorRegistrationBean> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NbfcCorRegistrationBean> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NbfcCorRegistrationBean> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NbfcCorRegistrationBean> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(NbfcCorRegistrationBean bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	public void updateAll(List<NbfcCorRegistrationBean> nsdlRecords) {
		nbfcCorRegistrationRepo.saveAll(nsdlRecords);

	}

}
