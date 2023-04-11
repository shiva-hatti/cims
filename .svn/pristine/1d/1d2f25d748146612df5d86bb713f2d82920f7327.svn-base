package com.iris.nbfc.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.nbfc.model.NbfcCapitalType;
import com.iris.nbfc.repository.NbfcCapitalTypeRepo;
import com.iris.service.GenericService;

@Service
public class NbfcCapitalTypeService implements GenericService<NbfcCapitalType, Long> {

	@Autowired
	NbfcCapitalTypeRepo nbfcCapitalTypeRepo;

	@Override
	public NbfcCapitalType add(NbfcCapitalType entity) throws ServiceException {

		return null;
	}

	@Override
	public boolean update(NbfcCapitalType entity) throws ServiceException {

		return false;
	}

	@Override
	public List<NbfcCapitalType> getDataByIds(Long[] ids) throws ServiceException {

		return Collections.emptyList();
	}

	@Override
	public NbfcCapitalType getDataById(Long id) throws ServiceException {

		return null;
	}

	@Override
	public List<NbfcCapitalType> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {

		return Collections.emptyList();
	}

	@Override
	public List<NbfcCapitalType> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {

		return Collections.emptyList();
	}

	@Override
	public List<NbfcCapitalType> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {

		return Collections.emptyList();
	}

	@Override
	public List<NbfcCapitalType> getActiveDataFor(Class bean, Long id) throws ServiceException {

		if (bean.equals(NbfcCapitalType.class) && Objects.isNull(id)) {
			return nbfcCapitalTypeRepo.findByIsActiveTrue();
		}

		return Collections.emptyList();
	}

	@Override
	public List<NbfcCapitalType> getAllDataFor(Class bean, Long id) throws ServiceException {

		return Collections.emptyList();
	}

	@Override
	public void deleteData(NbfcCapitalType bean) throws ServiceException {

	}

}
