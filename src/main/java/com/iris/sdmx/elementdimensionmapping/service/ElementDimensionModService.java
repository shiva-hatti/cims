/**
 * 
 */
package com.iris.sdmx.elementdimensionmapping.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.sdmx.dimesnsion.repo.DimensionModRepo;
import com.iris.sdmx.elementdimensionmapping.entity.ElementDimensionMod;
import com.iris.service.GenericService;

/**
 * @author sajadhav
 *
 */
@Service
public class ElementDimensionModService implements GenericService<ElementDimensionMod, Long> {

	@Autowired
	private DimensionModRepo dimensionModRepo;

	@Override
	public ElementDimensionMod add(ElementDimensionMod entity) throws ServiceException {
		return null;
	}

	@Override
	public boolean update(ElementDimensionMod entity) throws ServiceException {
		return false;
	}

	@Override
	public List<ElementDimensionMod> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public ElementDimensionMod getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<ElementDimensionMod> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ElementDimensionMod> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ElementDimensionMod> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<ElementDimensionMod> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<ElementDimensionMod> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(ElementDimensionMod bean) throws ServiceException {

	}

}
