/**
 * 
 */
package com.iris.sdmx.exceltohtml.helper;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.iris.model.UserMaster;
import com.iris.sdmx.exceltohtml.bean.SdmxReturnModelInfoBean;
import com.iris.sdmx.exceltohtml.entity.SdmxModelCodesEntity;
import com.iris.sdmx.exceltohtml.entity.SdmxReturnModelInfoEntity;
import com.iris.sdmx.exceltohtml.entity.SdmxReturnSheetInfoEntity;

/**
 * @author apagaria
 *
 */
public class SdmxReturnModelInfoHelper {

	private SdmxReturnModelInfoHelper() {
	}

	/**
	 * @param sdmxModelCodesEntity
	 * @param sdmxModelCodesBean
	 */
	public static void convertEntityToBean(SdmxReturnModelInfoEntity sdmxReturnModelInfoEntity, SdmxReturnModelInfoBean sdmxReturnModelInfoBean) {
		BeanUtils.copyProperties(sdmxReturnModelInfoEntity, sdmxReturnModelInfoBean);

		if (sdmxReturnModelInfoEntity.getReturnSheetInfoIdFk().getReturnSheetInfoId() != null) {
			sdmxReturnModelInfoBean.setReturnSheetInfoId(sdmxReturnModelInfoEntity.getReturnSheetInfoIdFk().getReturnSheetInfoId());
		}
		// Model Info
		if (sdmxReturnModelInfoEntity.getModelCodesIdFk() != null) {
			SdmxModelCodesEntity sdmxModelCodesEntity = sdmxReturnModelInfoEntity.getModelCodesIdFk();
			sdmxReturnModelInfoBean.setModelCodesIdFk(sdmxModelCodesEntity.getModelCodesId());
			sdmxReturnModelInfoBean.setModelCode(sdmxModelCodesEntity.getModelCode());
			sdmxReturnModelInfoBean.setModelDim(sdmxModelCodesEntity.getModelDim());
			sdmxReturnModelInfoBean.setElementIdFk(sdmxModelCodesEntity.getElementIdFk().getElementId());
		}

		// Return Sheet Info
		if (sdmxReturnModelInfoEntity.getReturnSheetInfoIdFk() != null) {
			SdmxReturnSheetInfoEntity sdmxReturnSheetInfoEntity = sdmxReturnModelInfoEntity.getReturnSheetInfoIdFk();
			sdmxReturnModelInfoBean.setReturnSheetIdFk(sdmxReturnSheetInfoEntity.getReturnSheetInfoId());
			sdmxReturnModelInfoBean.setSectionCode(sdmxReturnSheetInfoEntity.getSectionCode());
			sdmxReturnModelInfoBean.setSectionName(sdmxReturnSheetInfoEntity.getSectionName());
			sdmxReturnModelInfoBean.setSheetCode(sdmxReturnSheetInfoEntity.getSheetCode());
			sdmxReturnModelInfoBean.setSheetName(sdmxReturnSheetInfoEntity.getSheetName());
			sdmxReturnModelInfoBean.setReturnTemplateId(sdmxReturnSheetInfoEntity.getReturnTemplateIdFk().getReturnTemplateId());
		}

		// Created By
		if (sdmxReturnModelInfoEntity.getCreatedBy() != null) {
			sdmxReturnModelInfoBean.setCreatedBy(sdmxReturnModelInfoEntity.getCreatedBy().getUserId());
			sdmxReturnModelInfoBean.setCreatedByName(sdmxReturnModelInfoEntity.getCreatedBy().getUserName());
		}
	}

	/**
	 * @param sdmxModelCodesBean
	 * @param sdmxModelCodesEntity
	 * @param userId
	 */
	public static void convertBeanToEntity(SdmxReturnModelInfoBean sdmxReturnModelInfoBean, SdmxReturnModelInfoEntity sdmxReturnModelInfoEntity, Long userId) {
		BeanUtils.copyProperties(sdmxReturnModelInfoBean, sdmxReturnModelInfoEntity);

		// User Master Created By
		if (userId != null) {
			UserMaster userMaster = new UserMaster();
			userMaster.setUserId(userId);
			sdmxReturnModelInfoEntity.setCreatedBy(userMaster);
			// Created On
			sdmxReturnModelInfoEntity.setCreatedOn(new Date());
		}

		// Return Sheet Info entity
		if (sdmxReturnModelInfoBean.getReturnSheetIdFk() != null) {
			sdmxReturnModelInfoEntity.setReturnSheetInfoIdFk(new SdmxReturnSheetInfoEntity(sdmxReturnModelInfoBean.getReturnSheetIdFk()));
		}

		// SdmxModelCodesEntity
		if (sdmxReturnModelInfoBean.getModelCodesIdFk() != null) {
			sdmxReturnModelInfoEntity.setModelCodesIdFk(new SdmxModelCodesEntity(sdmxReturnModelInfoBean.getModelCodesIdFk()));
		}

		sdmxReturnModelInfoEntity.setIsActive(true);
	}
}
