package com.iris.nbfc.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.nbfc.model.NbfcRegionalOfficeMaster;
import com.iris.nbfc.repository.NbfcRegionalOfficeRepo;
import com.iris.service.GenericService;

@Service
public class NbfcRegionalOfficeService implements GenericService<NbfcRegionalOfficeMaster, Long> {

	@Autowired
	NbfcRegionalOfficeRepo nbfcRegionalOfficeRepo;

	@Override
	public NbfcRegionalOfficeMaster add(NbfcRegionalOfficeMaster entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(NbfcRegionalOfficeMaster entity) throws ServiceException {
		return false;
	}

	@Override
	public List<NbfcRegionalOfficeMaster> getDataByIds(Long[] ids) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public NbfcRegionalOfficeMaster getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<NbfcRegionalOfficeMaster> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public List<NbfcRegionalOfficeMaster> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public List<NbfcRegionalOfficeMaster> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public List<NbfcRegionalOfficeMaster> getActiveDataFor(Class bean, Long id) throws ServiceException {
		if (bean.equals(NbfcRegionalOfficeMaster.class) && Objects.isNull(id)) {
			return nbfcRegionalOfficeRepo.findByIsActiveTrue();
		}
		return Collections.emptyList();
	}

	@Override
	public List<NbfcRegionalOfficeMaster> getAllDataFor(Class bean, Long id) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public void deleteData(NbfcRegionalOfficeMaster bean) throws ServiceException {
	}
}