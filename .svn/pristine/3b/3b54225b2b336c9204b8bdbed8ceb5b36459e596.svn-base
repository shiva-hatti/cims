package com.iris.nbfc.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.nbfc.model.NbfcCompanyType;
import com.iris.nbfc.repository.NbfcCompanyTypeRepo;
import com.iris.service.GenericService;

@Service
public class NbfcCompanyTypeService implements GenericService<NbfcCompanyType, Long> {

	@Autowired
	NbfcCompanyTypeRepo nbfcCompanyTypeRepo;

	@Override
	public NbfcCompanyType add(NbfcCompanyType entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(NbfcCompanyType entity) throws ServiceException {
		return false;
	}

	@Override
	public List<NbfcCompanyType> getDataByIds(Long[] ids) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public NbfcCompanyType getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<NbfcCompanyType> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public List<NbfcCompanyType> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public List<NbfcCompanyType> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public List<NbfcCompanyType> getActiveDataFor(Class bean, Long id) throws ServiceException {
		if (bean.equals(NbfcCompanyType.class) && Objects.isNull(id)) {
			return nbfcCompanyTypeRepo.findByIsActiveTrue();
		}
		return Collections.emptyList();
	}

	@Override
	public List<NbfcCompanyType> getAllDataFor(Class bean, Long id) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public void deleteData(NbfcCompanyType bean) throws ServiceException {
	}
}