package com.iris.sdmx.agency.master.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iris.exception.ServiceException;
import com.iris.sdmx.agency.master.bean.SdmxAgencyMasterBean;
import com.iris.sdmx.agency.master.entity.AgencyMaster;
import com.iris.sdmx.agency.master.repo.SdmxAgencyMasterRepo;
import com.iris.service.GenericService;

@Service
public class SdmxAgencyMasterService implements GenericService<AgencyMaster, Long> {

	private static final Logger LOGGER = LogManager.getLogger(SdmxAgencyMasterService.class);

	@Autowired
	SdmxAgencyMasterRepo sdmxAgencyMasterRepo;

	@Override
	public AgencyMaster add(AgencyMaster entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(AgencyMaster entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<AgencyMaster> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AgencyMaster getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AgencyMaster> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AgencyMaster> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AgencyMaster> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<AgencyMaster> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<AgencyMaster> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(AgencyMaster bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	public List<SdmxAgencyMasterBean> getActiveRecord() throws Exception {

		List<SdmxAgencyMasterBean> sdmxAgencyMasterBeans = null;
		LOGGER.info("Fetching Data Start for SDMX Group Master ");

		List<AgencyMaster> agencyMasters = sdmxAgencyMasterRepo.findByIsActiveTrue();

		if (!CollectionUtils.isEmpty(agencyMasters)) {
			LOGGER.info(" Group Master Data With Size  " + agencyMasters.size());
			sdmxAgencyMasterBeans = new ArrayList<>();

			for (AgencyMaster agencyMaster : agencyMasters) {
				SdmxAgencyMasterBean sdmxAgencyMasterBean = new SdmxAgencyMasterBean();
				sdmxAgencyMasterBean.setAgencyMasterId(agencyMaster.getAgencyMasterId());
				sdmxAgencyMasterBean.setAgencyMasterCode(agencyMaster.getAgencyMasterCode());
				sdmxAgencyMasterBean.setAgencyMasterLabel(agencyMaster.getAgencyMasterLabel());
				sdmxAgencyMasterBeans.add(sdmxAgencyMasterBean);
			}

			LOGGER.info(" Returning  Data size   " + sdmxAgencyMasterBeans.size());
			Collections.sort(sdmxAgencyMasterBeans);
			return sdmxAgencyMasterBeans;
		}

		return sdmxAgencyMasterBeans;

	}

	/**
	 * @param agencyCode
	 * @param isActive
	 * @return
	 */
	public String findAgencyNameByAgencyCode(String agencyCode, Boolean isActive) {
		return sdmxAgencyMasterRepo.findAgencyNameByAgencyCode(agencyCode, isActive);
	}

	/**
	 * @param isActive
	 * @return
	 */
	public List<SdmxAgencyMasterBean> findAgencyNameByStatus(Boolean isActive) {
		return sdmxAgencyMasterRepo.findAgencyNameByStatus(isActive);
	}

	/**
	 * @param agencyCode
	 * @param isActive
	 * @return
	 */
	public Long findAgencyIdByAgencyCode(String agencyCode, Boolean isActive) {
		return sdmxAgencyMasterRepo.findAgencyIdByAgencyCode(agencyCode, isActive);
	}

}
