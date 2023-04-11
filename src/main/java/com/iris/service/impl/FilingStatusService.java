/**
 * 
 */
package com.iris.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.model.FilingStatus;
import com.iris.repository.FilingStatusRepo;
import com.iris.sdmx.upload.history.dto.FilingStatusDto;
import com.iris.service.GenericService;

/**
 * @author Siddique
 *
 */
@Service
public class FilingStatusService implements GenericService<FilingStatus, Long> {

	@Autowired
	FilingStatusRepo filingStatusRepo;

	@Override
	public FilingStatus add(FilingStatus entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(FilingStatus entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<FilingStatus> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FilingStatus getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FilingStatus> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FilingStatus> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FilingStatus> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FilingStatus> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		List<FilingStatus> filingStatus = null;
		if (bean == null && id == null) {
			filingStatus = new ArrayList<>();
			filingStatus = filingStatusRepo.fetchAllFillingStatusData();
			return filingStatus;
		}
		return null;
	}

	@Override
	public List<FilingStatus> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return filingStatusRepo.fetchAllFilingStatus();
	}

	@Override
	public void deleteData(FilingStatus bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	public List<FilingStatusDto> prepareDataOfFilingStatus(List<FilingStatus> filingStatus) {
		List<FilingStatusDto> dtos = new ArrayList<>();
		FilingStatusDto filingStatusDto = null;
		for (FilingStatus obj : filingStatus) {
			filingStatusDto = new FilingStatusDto();
			filingStatusDto.setFilingStatusId(obj.getFilingStatusId());
			filingStatusDto.setStatus(obj.getStatus());
			dtos.add(filingStatusDto);
		}

		return dtos;
	}

}
