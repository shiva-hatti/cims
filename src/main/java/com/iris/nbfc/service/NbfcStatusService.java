package com.iris.nbfc.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.nbfc.model.NbfcStatus;
import com.iris.nbfc.repository.NbfcStatusRepo;
import com.iris.service.GenericService;

@Service
public class NbfcStatusService implements GenericService<NbfcStatus, Long> {

	@Autowired
	NbfcStatusRepo nbfcStatusRepo;

	@Override
	public NbfcStatus add(NbfcStatus entity) throws ServiceException {

		return null;
	}

	@Override
	public boolean update(NbfcStatus entity) throws ServiceException {

		return false;
	}

	@Override
	public List<NbfcStatus> getDataByIds(Long[] ids) throws ServiceException {

		return Collections.emptyList();
	}

	@Override
	public NbfcStatus getDataById(Long id) throws ServiceException {

		return null;
	}

	@Override
	public List<NbfcStatus> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {

		return Collections.emptyList();
	}

	@Override
	public List<NbfcStatus> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {

		return Collections.emptyList();
	}

	@Override
	public List<NbfcStatus> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {

		return Collections.emptyList();
	}

	@Override
	public List<NbfcStatus> getActiveDataFor(Class bean, Long id) throws ServiceException {

		if (bean.equals(NbfcStatus.class) && Objects.isNull(id)) {
			return nbfcStatusRepo.findByIsActiveTrue();
		}

		return Collections.emptyList();
	}

	@Override
	public List<NbfcStatus> getAllDataFor(Class bean, Long id) throws ServiceException {

		return Collections.emptyList();
	}

	@Override
	public void deleteData(NbfcStatus bean) throws ServiceException {

	}

}
