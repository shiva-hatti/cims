package com.iris.nbfc.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.nbfc.model.NbfcShareHolder;
import com.iris.nbfc.repository.NbfcShareHolderRepo;
import com.iris.service.GenericService;

@Service
public class NbfcShareHolderService implements GenericService<NbfcShareHolder, Long> {

	@Autowired
	NbfcShareHolderRepo nbfcShareHolderRepo;

	@Override
	public NbfcShareHolder add(NbfcShareHolder entity) throws ServiceException {

		return null;
	}

	@Override
	public boolean update(NbfcShareHolder entity) throws ServiceException {

		return false;
	}

	@Override
	public List<NbfcShareHolder> getDataByIds(Long[] ids) throws ServiceException {

		return Collections.emptyList();
	}

	@Override
	public NbfcShareHolder getDataById(Long id) throws ServiceException {

		return null;
	}

	@Override
	public List<NbfcShareHolder> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {

		return Collections.emptyList();
	}

	@Override
	public List<NbfcShareHolder> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {

		return Collections.emptyList();
	}

	@Override
	public List<NbfcShareHolder> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {

		return Collections.emptyList();
	}

	@Override
	public List<NbfcShareHolder> getActiveDataFor(Class bean, Long id) throws ServiceException {

		if (bean.equals(NbfcShareHolder.class) && Objects.isNull(id)) {
			return nbfcShareHolderRepo.findByIsActiveTrue();
		}

		return Collections.emptyList();
	}

	@Override
	public List<NbfcShareHolder> getAllDataFor(Class bean, Long id) throws ServiceException {

		return Collections.emptyList();
	}

	@Override
	public void deleteData(NbfcShareHolder bean) throws ServiceException {

	}

}
