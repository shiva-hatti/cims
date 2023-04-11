package com.iris.nbfc.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.nbfc.model.NbfcDisplayMessages;
import com.iris.nbfc.repository.NbfcMessagesRepo;
import com.iris.service.GenericService;
import com.iris.util.constant.ColumnConstants;
import com.iris.util.constant.ErrorConstants;
import com.iris.util.constant.MethodConstants;

/**
 * @author pmohite
 */
@Service
public class NbfcMessagesService implements GenericService<NbfcDisplayMessages, Long> {

	@Autowired
	NbfcMessagesRepo nbfcMessagesRepo;

	@Override
	public NbfcDisplayMessages add(NbfcDisplayMessages entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(NbfcDisplayMessages entity) throws ServiceException {
		return false;
	}

	@Override
	public List<NbfcDisplayMessages> getDataByIds(Long[] ids) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public NbfcDisplayMessages getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<NbfcDisplayMessages> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public List<NbfcDisplayMessages> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public List<NbfcDisplayMessages> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		try {
			Integer companyType = null;
			if (methodName.equalsIgnoreCase(MethodConstants.GET_NBFC_MESSAGES_BY_COMPANY_TYPE.getConstantVal())) {
				companyType = (Integer) columnValueMap.get(ColumnConstants.COMPANY_TYPE.getConstantVal());
				return nbfcMessagesRepo.getActiveMessgaesDataBycompanyType(companyType);
			}
			return Collections.emptyList();

		} catch (Exception e) {
			throw new ServiceException(ErrorConstants.ERROR_MSG_SERVICE.getConstantVal(), e);
		}
	}

	@Override
	public List<NbfcDisplayMessages> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public List<NbfcDisplayMessages> getAllDataFor(Class bean, Long id) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public void deleteData(NbfcDisplayMessages bean) throws ServiceException {
	}
}
