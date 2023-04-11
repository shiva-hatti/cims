package com.iris.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.NbfcEntityProfileDetails;
import com.iris.repository.NbfcEntityProfileDetailsRepo;
import com.iris.service.GenericService;

@Service
public class NbfcEntityProfileDetailsService implements GenericService<NbfcEntityProfileDetails, Long> {

	@Autowired
	private NbfcEntityProfileDetailsRepo nbfcEntityProfileDetailsRepo;

	@Override
	public NbfcEntityProfileDetails add(NbfcEntityProfileDetails entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(NbfcEntityProfileDetails entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<NbfcEntityProfileDetails> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NbfcEntityProfileDetails getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return nbfcEntityProfileDetailsRepo.getNbfcEntityProfileDetails(id);
	}

	@Override
	public List<NbfcEntityProfileDetails> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NbfcEntityProfileDetails> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NbfcEntityProfileDetails> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NbfcEntityProfileDetails> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NbfcEntityProfileDetails> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(NbfcEntityProfileDetails bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
