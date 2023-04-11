package com.iris.nbfc.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.nbfc.model.NbfcTypeCompany;
import com.iris.nbfc.repository.NbfcTypeCompanyRepo;
import com.iris.service.GenericService;

@Service
public class NbfcTypeCompanyService implements GenericService<NbfcTypeCompany, Long> {

	@Autowired
	NbfcTypeCompanyRepo nbfcTypeCompanyRepo;

	@Override
	public NbfcTypeCompany add(NbfcTypeCompany entity) throws ServiceException {

		return null;
	}

	@Override
	public boolean update(NbfcTypeCompany entity) throws ServiceException {

		return false;
	}

	@Override
	public List<NbfcTypeCompany> getDataByIds(Long[] ids) throws ServiceException {

		return Collections.emptyList();
	}

	@Override
	public NbfcTypeCompany getDataById(Long id) throws ServiceException {

		return null;
	}

	@Override
	public List<NbfcTypeCompany> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {

		return Collections.emptyList();
	}

	@Override
	public List<NbfcTypeCompany> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {

		return Collections.emptyList();
	}

	@Override
	public List<NbfcTypeCompany> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {

		return Collections.emptyList();
	}

	@Override
	public List<NbfcTypeCompany> getActiveDataFor(Class bean, Long id) throws ServiceException {
		if (bean.equals(NbfcTypeCompany.class) && Objects.isNull(id)) {
			return nbfcTypeCompanyRepo.findByIsActiveTrue();
		}
		return Collections.emptyList();
	}

	@Override
	public List<NbfcTypeCompany> getAllDataFor(Class bean, Long id) throws ServiceException {

		return Collections.emptyList();
	}

	@Override
	public void deleteData(NbfcTypeCompany bean) throws ServiceException {

	}

}
