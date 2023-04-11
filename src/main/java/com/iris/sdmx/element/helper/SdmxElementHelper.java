/**
 * 
 */
package com.iris.sdmx.element.helper;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.iris.model.Regulator;
import com.iris.model.UserMaster;
import com.iris.sdmx.agency.master.bean.SdmxAgencyMasterBean;
import com.iris.sdmx.element.bean.SdmxAddElementInputBean;
import com.iris.sdmx.element.bean.SdmxElementBean;
import com.iris.sdmx.element.bean.SdmxElementBeanForTemp;
import com.iris.sdmx.element.entity.SdmxElementClassificationEntity;
import com.iris.sdmx.element.entity.SdmxElementDependencyTypeEntity;
import com.iris.sdmx.element.entity.SdmxElementEntity;
import com.iris.sdmx.element.entity.SdmxElementFlowTypeEntity;
import com.iris.sdmx.element.entity.SdmxElementFrequencyEntity;
import com.iris.sdmx.element.entity.SdmxElementNatureEntity;
import com.iris.sdmx.element.entity.SdmxElementSectorEntity;
import com.iris.sdmx.element.entity.SdmxElementUsageEntity;

/**
 * @author apagaria
 *
 */
public class SdmxElementHelper {

	private SdmxElementHelper() {

	}

	/**
	 * @param sdmxElementSectorEntity
	 * @param sdmxElementSectorBean
	 */
	public static void convertEntityToBean(SdmxElementEntity sdmxElementEntity, SdmxElementBean sdmxElementBean) {
		BeanUtils.copyProperties(sdmxElementEntity, sdmxElementBean);
		// Removing extra data from user master for created By field
		if (sdmxElementEntity.getCreatedBy() != null && sdmxElementEntity.getCreatedBy().getUserId() != null) {
			sdmxElementBean.setCreatedBy(sdmxElementEntity.getCreatedBy().getUserId());
			sdmxElementBean.setCreatedByUserName(sdmxElementEntity.getCreatedBy().getUserName());
		}

		// Removing extra data from user master for modify By field
		if (sdmxElementEntity.getModifyBy() != null && sdmxElementEntity.getModifyBy().getUserId() != null) {
			sdmxElementBean.setModifyBy(sdmxElementEntity.getModifyBy().getUserId());
			sdmxElementBean.setModifyByUserName(sdmxElementEntity.getModifyBy().getUserName());
		}

		if (sdmxElementEntity.getAgencyMaster() != null && sdmxElementEntity.getAgencyMaster().getAgencyMasterId() != 0) {
			SdmxAgencyMasterBean sdmxAgencyMasterBean = new SdmxAgencyMasterBean();
			sdmxAgencyMasterBean.setAgencyMasterCode(sdmxElementEntity.getAgencyMaster().getAgencyMasterCode());
			sdmxAgencyMasterBean.setAgencyMasterId(sdmxElementEntity.getAgencyMaster().getAgencyMasterId());
			sdmxAgencyMasterBean.setAgencyMasterLabel(sdmxElementEntity.getAgencyMaster().getAgencyMasterLabel());
			sdmxElementBean.setSdmxAgencyMasterBean(sdmxAgencyMasterBean);
		} else {
			sdmxElementBean.setSdmxAgencyMasterBean(null);
		}

		// Classification
		if (sdmxElementEntity.getClassificationIdFk() != null) {
			sdmxElementBean.setClassificationTypeId(sdmxElementEntity.getClassificationIdFk().getClassificationId());
			sdmxElementBean.setClassificationName(sdmxElementEntity.getClassificationIdFk().getClassificationName());
		}

		// Dependency Type
		if (sdmxElementEntity.getDependencyTypeIdFk() != null) {
			sdmxElementBean.setDependencyTypeId(sdmxElementEntity.getDependencyTypeIdFk().getDependencyTypeId());
			sdmxElementBean.setDependencyTypeName(sdmxElementEntity.getDependencyTypeIdFk().getDependencyTypeName());
		}

		// Nature
		if (sdmxElementEntity.getNatureIdFk() != null) {
			sdmxElementBean.setNatureTypeId(sdmxElementEntity.getNatureIdFk().getNatureId());
			sdmxElementBean.setNatureTypeName(sdmxElementEntity.getNatureIdFk().getNatureName());
		}

		// Flow Type
		if (sdmxElementEntity.getFlowTypeIdFk() != null) {
			sdmxElementBean.setFlowTypeId(sdmxElementEntity.getFlowTypeIdFk().getFlowTypeId());
			sdmxElementBean.setFlowTypeName(sdmxElementEntity.getFlowTypeIdFk().getFlowTypeName());
		}

		// Sector
		if (sdmxElementEntity.getSectorIdFk() != null) {
			sdmxElementBean.setSectorTypeId(sdmxElementEntity.getSectorIdFk().getSectorId());
			sdmxElementBean.setSectorTypeName(sdmxElementEntity.getSectorIdFk().getSectorName());
		}

		// Usage
		if (sdmxElementEntity.getUsageIdFk() != null) {
			sdmxElementBean.setUsageId(sdmxElementEntity.getUsageIdFk().getUsageId());
			sdmxElementBean.setUsageName(sdmxElementEntity.getUsageIdFk().getUsageName());
		}

		// Frequency
		if (sdmxElementEntity.getFrequencyIdFk() != null) {
			sdmxElementBean.setFrequencyId(sdmxElementEntity.getFrequencyIdFk().getFrequencyId());
			sdmxElementBean.setFrequencyName(sdmxElementEntity.getFrequencyIdFk().getFrequencyName());
		}
		// Parent Element
		if (sdmxElementEntity.getParentElementIdFk() != null) {
			sdmxElementBean.setParentElementDsdCode(sdmxElementEntity.getParentElementIdFk().getDsdCode());
			sdmxElementBean.setElementParentId(sdmxElementEntity.getParentElementIdFk().getElementId());
			sdmxElementBean.setParentElementVer(sdmxElementEntity.getParentElementIdFk().getElementVer());
		}

		// Owner Department
		if (sdmxElementEntity.getRegulatorOwnerIdFk() != null) {
			sdmxElementBean.setOwnerDepartmentId(sdmxElementEntity.getRegulatorOwnerIdFk().getRegulatorId());
			sdmxElementBean.setOwnerDepartmentCode(sdmxElementEntity.getRegulatorOwnerIdFk().getRegulatorCode());
			sdmxElementBean.setOwnerDepartmentName(sdmxElementEntity.getRegulatorOwnerIdFk().getRegulatorName());
		}

	}

	public static void convertInputBeanToEntity(SdmxAddElementInputBean sdmxAddElementInputBean, SdmxElementBeanForTemp sdmxElementBeanForTemp) {
		BeanUtils.copyProperties(sdmxAddElementInputBean, sdmxElementBeanForTemp);
	}

	public static void convertBeanToEntity(SdmxElementBean sdmxElementBean, SdmxElementEntity sdmxElementEntity, boolean isForEdit) {
		if (!isForEdit) {
			BeanUtils.copyProperties(sdmxElementBean, sdmxElementEntity);
		}

		// Created By
		if (sdmxElementBean.getCreatedBy() != null) {
			UserMaster userMaster = new UserMaster();
			userMaster.setUserId(sdmxElementBean.getCreatedBy());
			if (!isForEdit) {
				sdmxElementEntity.setCreatedBy(userMaster);
				sdmxElementEntity.setCreatedOn(new Date());
				sdmxElementEntity.setLastUpdatedOn(sdmxElementEntity.getCreatedOn());
			} else {
				sdmxElementEntity.setModifyBy(userMaster);
				sdmxElementEntity.setModifyOn(new Date());
				sdmxElementEntity.setLastUpdatedOn(sdmxElementEntity.getModifyOn());
			}

		}

		// Classification Fk
		if (sdmxElementBean.getClassificationTypeId() != null) {
			SdmxElementClassificationEntity sdmxElementClassificationEntity = new SdmxElementClassificationEntity();
			sdmxElementClassificationEntity.setClassificationId(sdmxElementBean.getClassificationTypeId());
			sdmxElementEntity.setClassificationIdFk(sdmxElementClassificationEntity);
		}

		// Dependency Type
		if (sdmxElementBean.getDependencyTypeId() != null) {
			SdmxElementDependencyTypeEntity sdmxElementDependencyTypeEntity = new SdmxElementDependencyTypeEntity();
			sdmxElementDependencyTypeEntity.setDependencyTypeId(sdmxElementBean.getDependencyTypeId());
			sdmxElementEntity.setDependencyTypeIdFk(sdmxElementDependencyTypeEntity);
		}

		// Flow Type
		if (sdmxElementBean.getFlowTypeId() != null) {
			SdmxElementFlowTypeEntity sdmxElementFlowTypeEntity = new SdmxElementFlowTypeEntity();
			sdmxElementFlowTypeEntity.setFlowTypeId(sdmxElementBean.getFlowTypeId());
			sdmxElementEntity.setFlowTypeIdFk(sdmxElementFlowTypeEntity);
		}

		// Nature Type
		if (sdmxElementBean.getNatureTypeId() != null) {
			SdmxElementNatureEntity sdmxElementNatureEntity = new SdmxElementNatureEntity();
			sdmxElementNatureEntity.setNatureId(sdmxElementBean.getNatureTypeId());
			sdmxElementEntity.setNatureIdFk(sdmxElementNatureEntity);
		}

		// Sector
		if (sdmxElementBean.getSectorTypeId() != null) {
			SdmxElementSectorEntity sdmxElementSectorEntity = new SdmxElementSectorEntity();
			sdmxElementSectorEntity.setSectorId(sdmxElementBean.getSectorTypeId());
			sdmxElementEntity.setSectorIdFk(sdmxElementSectorEntity);
		} else {
			sdmxElementEntity.setSectorIdFk(null);
		}

		// Regulator
		if (sdmxElementBean.getOwnerDepartmentId() != null) {
			Regulator regulator = new Regulator();
			regulator.setRegulatorId(sdmxElementBean.getOwnerDepartmentId());
			sdmxElementEntity.setRegulatorOwnerIdFk(regulator);
		}

		// Usage 
		if (sdmxElementBean.getUsageId() != null) {
			SdmxElementUsageEntity sdmxElementUsageEntity = new SdmxElementUsageEntity();
			sdmxElementUsageEntity.setUsageId(sdmxElementBean.getUsageId());
			sdmxElementEntity.setUsageIdFk(sdmxElementUsageEntity);
		}

		// Frequency 
		if (sdmxElementBean.getFrequencyId() != null) {
			SdmxElementFrequencyEntity sdmxElementFrequencyEntity = new SdmxElementFrequencyEntity();
			sdmxElementFrequencyEntity.setFrequencyId(sdmxElementBean.getFrequencyId());
			sdmxElementEntity.setFrequencyIdFk(sdmxElementFrequencyEntity);
		}

		// Parent Element
		if (sdmxElementBean.getElementParentId() != null) {
			SdmxElementEntity parentElementEntity = new SdmxElementEntity();
			parentElementEntity.setElementId(sdmxElementBean.getElementParentId());
			sdmxElementEntity.setParentElementIdFk(parentElementEntity);
		} else {

			sdmxElementEntity.setParentElementIdFk(null);
		}
		if (isForEdit) {
			sdmxElementEntity.setElementDesc(sdmxElementBean.getElementDesc());
			sdmxElementEntity.setElementLabel(sdmxElementBean.getElementLabel());
			//newly Added
			sdmxElementEntity.setElementVer(sdmxElementBean.getElementVer());
		} else {
			sdmxElementEntity.setIsActive(Boolean.TRUE);
			sdmxElementEntity.setIsPending(Boolean.FALSE);
		}
	}

	/**
	 * @param sdmxElementEntities
	 * @param sdmxElementBeans
	 */
	public static void convertEntityListToBeanList(List<SdmxElementEntity> sdmxElementEntities, List<SdmxElementBean> sdmxElementBeans) {
		for (SdmxElementEntity sdmxElementEntity : sdmxElementEntities) {
			SdmxElementBean sdmxElementBean = new SdmxElementBean();
			convertEntityToBean(sdmxElementEntity, sdmxElementBean);
			sdmxElementBeans.add(sdmxElementBean);
		}
	}

	public static void copyEditedData(SdmxElementBeanForTemp sdmxElementBeanForTemp, SdmxElementBean sdmxElementBean) {

		if (sdmxElementBeanForTemp != null) {
			// Classification
			if (sdmxElementBeanForTemp.getClassificationTypeId() != null && !sdmxElementBeanForTemp.getClassificationTypeId().equals(sdmxElementBean.getClassificationTypeId())) {
				sdmxElementBean.setClassificationTypeId(sdmxElementBeanForTemp.getClassificationTypeId());
				sdmxElementBean.setClassificationName(sdmxElementBeanForTemp.getClassificationName());
			}

			if (sdmxElementBeanForTemp.getAgencyMasterCode() != null) {
				sdmxElementBean.setAgencyMasterCode(sdmxElementBeanForTemp.getAgencyMasterCode());
			} else {
				sdmxElementBean.setAgencyMasterCode(null);
			}

			// Dependency
			if (sdmxElementBeanForTemp.getDependencyTypeId() != null && !sdmxElementBeanForTemp.getDependencyTypeId().equals(sdmxElementBean.getDependencyTypeId())) {
				sdmxElementBean.setDependencyTypeId(sdmxElementBeanForTemp.getDependencyTypeId());
				sdmxElementBean.setDependencyTypeName(sdmxElementBeanForTemp.getDependencyTypeName());
			}

			// Flow Type
			if (sdmxElementBeanForTemp.getFlowTypeId() != null && !sdmxElementBeanForTemp.getFlowTypeId().equals(sdmxElementBean.getFlowTypeId())) {
				sdmxElementBean.setFlowTypeId(sdmxElementBeanForTemp.getFlowTypeId());
				sdmxElementBean.setFlowTypeName(sdmxElementBeanForTemp.getFlowTypeName());
			}

			// Sector
			if (sdmxElementBeanForTemp.getSectorTypeId() != null && !sdmxElementBeanForTemp.getSectorTypeId().equals(sdmxElementBean.getSectorTypeId())) {
				sdmxElementBean.setSectorTypeId(sdmxElementBeanForTemp.getSectorTypeId());
				sdmxElementBean.setSectorTypeName(sdmxElementBeanForTemp.getSectorTypeName());
			}

			// Nature
			if (sdmxElementBeanForTemp.getNatureTypeId() != null && !sdmxElementBeanForTemp.getNatureTypeId().equals(sdmxElementBean.getNatureTypeId())) {
				sdmxElementBean.setNatureTypeId(sdmxElementBeanForTemp.getNatureTypeId());
				sdmxElementBean.setNatureTypeName(sdmxElementBeanForTemp.getNatureTypeName());
			}

			// Usage
			if (sdmxElementBeanForTemp.getUsageId() != null && !sdmxElementBeanForTemp.getUsageId().equals(sdmxElementBean.getUsageId())) {
				sdmxElementBean.setUsageId(sdmxElementBeanForTemp.getUsageId());
				sdmxElementBean.setUsageName(sdmxElementBeanForTemp.getUsageName());
			}

			// Frequency
			if (sdmxElementBeanForTemp.getFrequencyId() != null && !sdmxElementBeanForTemp.getFrequencyId().equals(sdmxElementBean.getFrequencyId())) {
				sdmxElementBean.setFrequencyId(sdmxElementBeanForTemp.getFrequencyId());
				sdmxElementBean.setFrequencyName(sdmxElementBeanForTemp.getFrequencyName());
			}

			// Element Label
			if (!StringUtils.isBlank(sdmxElementBeanForTemp.getElementLabel()) && !sdmxElementBeanForTemp.getElementLabel().equals(sdmxElementBean.getElementLabel())) {
				sdmxElementBean.setElementLabel(sdmxElementBeanForTemp.getElementLabel());
			}

			// Element Description
			if (!StringUtils.isBlank(sdmxElementBeanForTemp.getElementDesc()) && !sdmxElementBeanForTemp.getElementDesc().equals(sdmxElementBean.getElementDesc())) {
				sdmxElementBean.setElementDesc(sdmxElementBeanForTemp.getElementDesc());
			}

			// Element Owner
			if (sdmxElementBeanForTemp.getOwnerDepartmentCode() != null && !sdmxElementBeanForTemp.getOwnerDepartmentCode().equals(sdmxElementBean.getOwnerDepartmentCode())) {
				sdmxElementBean.setOwnerDepartmentCode(sdmxElementBeanForTemp.getOwnerDepartmentCode());
				sdmxElementBean.setOwnerDepartmentId(sdmxElementBeanForTemp.getOwnerDepartmentId());
				sdmxElementBean.setOwnerDepartmentName(sdmxElementBeanForTemp.getOwnerDepartmentName());
			}

			// Parent Element DSD Code and Version
			/*	if ((!StringUtils.isBlank(sdmxElementBeanForTemp.getParentElementDsdCode()) && !sdmxElementBeanForTemp
						.getParentElementDsdCode().equals(sdmxElementBean.getParentElementDsdCode()))
						|| (!StringUtils.isBlank(sdmxElementBeanForTemp.getParentElementVer()) && !sdmxElementBeanForTemp
								.getParentElementVer().equals(sdmxElementBean.getParentElementVer()))) {*/
			sdmxElementBean.setParentElementDsdCode(sdmxElementBeanForTemp.getParentElementDsdCode());
			sdmxElementBean.setElementParentId(sdmxElementBeanForTemp.getElementParentId());
			sdmxElementBean.setParentElementVer(sdmxElementBeanForTemp.getParentElementVer());
			/* } */

			// Copy DSD code and other info to temp
			//Element version
			//newly added
			if (!StringUtils.isBlank(sdmxElementBeanForTemp.getElementVer())) {
				sdmxElementBean.setElementVer(sdmxElementBeanForTemp.getElementVer());

			}
			BeanUtils.copyProperties(sdmxElementBean, sdmxElementBeanForTemp);
		}

	}

}