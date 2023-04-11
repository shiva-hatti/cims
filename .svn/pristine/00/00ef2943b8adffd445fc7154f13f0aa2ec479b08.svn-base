/**
 * 
 */
package com.iris.sdmx.exceltohtml.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iris.exception.ServiceException;
import com.iris.sdmx.exceltohtml.entity.SdmxEleDimTypeMapEntity;
import com.iris.sdmx.exceltohtml.repo.SdmxEleDimTypeMapRepo;
import com.iris.service.GenericService;

/**
 * @author apagaria
 *
 */
@Service
@Transactional(readOnly = true)
public class SdmxEleDimTypeMapService implements GenericService<SdmxEleDimTypeMapEntity, Long> {

	@Autowired
	private SdmxEleDimTypeMapRepo sdmxEleDimTypeMapRepo;

	@Override
	@Transactional(readOnly = false)
	public SdmxEleDimTypeMapEntity add(SdmxEleDimTypeMapEntity entity) throws ServiceException {
		return sdmxEleDimTypeMapRepo.save(entity);
	}

	@Override
	public boolean update(SdmxEleDimTypeMapEntity entity) throws ServiceException {
		return false;
	}

	@Override
	public List<SdmxEleDimTypeMapEntity> getDataByIds(Long[] ids) throws ServiceException {
		return null;
	}

	@Override
	public SdmxEleDimTypeMapEntity getDataById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxEleDimTypeMapEntity> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxEleDimTypeMapEntity> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@Override
	public List<SdmxEleDimTypeMapEntity> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxEleDimTypeMapEntity> getActiveDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxEleDimTypeMapEntity> getAllDataFor(Class bean, Long id) throws ServiceException {
		return null;
	}

	@Override
	public void deleteData(SdmxEleDimTypeMapEntity bean) throws ServiceException {

	}

	/**
	 * @param eleDimHash
	 * @return
	 */
	public List<Long> findEntityByEleDimHash(String eleDimHash) {
		return sdmxEleDimTypeMapRepo.findEntityByEleDimHash(eleDimHash);
	}

	/**
	 * @param eleDimHash
	 * @return
	 */
	public Boolean checkEleDimHashExist(String eleDimHash) {
		boolean isEleDimHashExist = false;
		eleDimHash = sdmxEleDimTypeMapRepo.checkEntityExistByEleDimHash(eleDimHash);
		if (!StringUtils.isBlank(eleDimHash)) {
			isEleDimHashExist = true;
		}
		return isEleDimHashExist;
	}

	/**
	 * @param dsdCode
	 * @param elementVer
	 * @return
	 */
	public Integer fetchMaxGroupNumByEleNVersion(String dsdCode, String elementVer) {
		return sdmxEleDimTypeMapRepo.fetchMaxGroupNumByEleNVersion(dsdCode, elementVer);
	}

	/**
	 * @param entityList
	 * @return
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public List<SdmxEleDimTypeMapEntity> add(List<SdmxEleDimTypeMapEntity> entityList) throws ServiceException {
		return sdmxEleDimTypeMapRepo.saveAll(entityList);
	}

}
