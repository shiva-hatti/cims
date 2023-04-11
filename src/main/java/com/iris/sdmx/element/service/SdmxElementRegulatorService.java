package com.iris.sdmx.element.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iris.exception.ServiceException;
import com.iris.model.LanguageMaster;
import com.iris.model.Regulator;
import com.iris.model.UserMaster;
import com.iris.repository.RegulatorRepo;
import com.iris.sdmx.element.bean.SdmxElementRegulatorBean;
import com.iris.sdmx.element.entity.SdmxElementRegulatorEntity;
import com.iris.sdmx.element.helper.SdmxElementRegulatorHelper;
import com.iris.sdmx.element.repo.SdmxElementRegulatorRepo;
import com.iris.service.GenericService;
import com.iris.service.impl.LanguageMasterService;
import com.iris.service.impl.UserMasterService;

@Service
@Transactional(readOnly = true)
public class SdmxElementRegulatorService implements GenericService<SdmxElementRegulatorEntity, Long> {
	@Autowired
	private SdmxElementRegulatorRepo sdmxElementRegulatorRepo;

	@Autowired
	LanguageMasterService languageMasterService;

	@Autowired
	private UserMasterService userMasterService;

	@Autowired
	private RegulatorRepo regulatorRepo;

	@Override
	public SdmxElementRegulatorEntity add(SdmxElementRegulatorEntity entity) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(SdmxElementRegulatorEntity entity) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SdmxElementRegulatorEntity> getDataByIds(Long[] ids) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SdmxElementRegulatorEntity getDataById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxElementRegulatorEntity> getDataByColumnValue(Map<String, List<String>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxElementRegulatorEntity> getDataByColumnLongValue(Map<String, List<Long>> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SdmxElementRegulatorEntity> getDataByObject(Map<String, Object> columnValueMap, String methodName) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxElementRegulatorEntity> getActiveDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SdmxElementRegulatorEntity> getAllDataFor(Class bean, Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteData(SdmxElementRegulatorEntity bean) throws ServiceException {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * This method is used to fetch active classification data
	 * 
	 * @return
	 */
	public List<SdmxElementRegulatorBean> findByActiveStatus(String languageCode, Long userId) {

		List<SdmxElementRegulatorBean> elementRegulatorBeans = new ArrayList<>();
		LanguageMaster languageMaster = languageMasterService.getAllActiveLanguageWrapper(languageCode);

		Long deptIdFk;
		Regulator regulator;
		boolean isMainDept = false;
		List<SdmxElementRegulatorEntity> elementRegulatorEntities = new ArrayList<>();

		UserMaster userMaster = userMasterService.getDataById(userId);
		if (userMaster != null) {
			if (userMaster.getDepartmentIdFk() != null) {
				deptIdFk = userMaster.getDepartmentIdFk().getRegulatorId();
				regulator = regulatorRepo.findByRegulatorIdAndIsActiveTrue(deptIdFk);

				if (regulator != null) {
					isMainDept = regulator.getIsMaster();

					if (isMainDept) {
						elementRegulatorEntities = sdmxElementRegulatorRepo.findRegulatorLabelByIsactiveAndLanguageId(languageMaster.getLanguageId());
					} else {

						elementRegulatorEntities = sdmxElementRegulatorRepo.findByLanguageIdAndRegulatorId(languageMaster.getLanguageId(), deptIdFk);

					}

				}
			}

		}

		if (!CollectionUtils.isEmpty(elementRegulatorEntities)) {
			//elementRegulatorBeans = new ArrayList<>();
			for (SdmxElementRegulatorEntity sdmxElementRegulatorEntity : elementRegulatorEntities) {
				SdmxElementRegulatorBean sdmxElementRegulatorBean = new SdmxElementRegulatorBean();
				// convert entity to Bean
				SdmxElementRegulatorHelper.convertEntityToBean(sdmxElementRegulatorEntity, sdmxElementRegulatorBean);
				elementRegulatorBeans.add(sdmxElementRegulatorBean);
			}
		}
		return elementRegulatorBeans;
	}

}
