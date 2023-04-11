package com.iris.nbfc.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.nbfc.model.NbfcRocMaster;
import com.iris.nbfc.repository.NbfcRocMasterRepo;
import com.iris.service.GenericService;

@Service
public class NbfcRocService implements GenericService<NbfcRocMaster, Long> {

	@Autowired
	NbfcRocMasterRepo nbfcRocMasterRepo;

	@Override
	public NbfcRocMaster add(NbfcRocMaster entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(NbfcRocMaster entity) throws ServiceException {
		return false;
	}

	@Override
	public List<NbfcRocMaster> getDataByIds(Long[] ids) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public NbfcRocMaster getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<NbfcRocMaster> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public List<NbfcRocMaster> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public List<NbfcRocMaster> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public List<NbfcRocMaster> getActiveDataFor(Class bean, Long id) throws ServiceException {
		if (bean.equals(NbfcRocMaster.class) && Objects.isNull(id)) {
			return nbfcRocMasterRepo.findByIsActiveTrue();
		}
		return Collections.emptyList();
	}

	@Override
	public List<NbfcRocMaster> getAllDataFor(Class bean, Long id) throws ServiceException {
		return Collections.emptyList();
	}

	@Override
	public void deleteData(NbfcRocMaster bean) throws ServiceException {
	}
}