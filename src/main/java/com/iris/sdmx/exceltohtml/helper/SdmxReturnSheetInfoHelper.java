/**
 * 
 */
package com.iris.sdmx.exceltohtml.helper;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.iris.model.ReturnTemplate;
import com.iris.model.UserMaster;
import com.iris.sdmx.exceltohtml.bean.SdmxReturnSheetInfoBean;
import com.iris.sdmx.exceltohtml.entity.SdmxReturnSheetInfoEntity;

/**
 * @author apagaria
 *
 */
public class SdmxReturnSheetInfoHelper {

	private SdmxReturnSheetInfoHelper() {
	}

	/**
	 * @param sdmxReturnSheetInfoEntity
	 * @param sdmxReturnSheetInfoBean
	 */
	public static void convertEntityToBean(SdmxReturnSheetInfoEntity sdmxReturnSheetInfoEntity, SdmxReturnSheetInfoBean sdmxReturnSheetInfoBean) {
		BeanUtils.copyProperties(sdmxReturnSheetInfoEntity, sdmxReturnSheetInfoBean);

		// Return Template
		if (sdmxReturnSheetInfoEntity.getReturnTemplateIdFk() != null) {
			sdmxReturnSheetInfoBean.setReturnTemplateId(sdmxReturnSheetInfoEntity.getReturnTemplateIdFk().getReturnTemplateId());
			sdmxReturnSheetInfoBean.setReturnVersion(sdmxReturnSheetInfoEntity.getReturnTemplateIdFk().getVersionNumber());
			sdmxReturnSheetInfoBean.setReturnCode(sdmxReturnSheetInfoEntity.getReturnTemplateIdFk().getReturnObj().getReturnCode());
			sdmxReturnSheetInfoBean.setReturnName(sdmxReturnSheetInfoEntity.getReturnTemplateIdFk().getReturnObj().getReturnName());
		}

		// Created By
		if (sdmxReturnSheetInfoEntity.getCreatedBy() != null) {
			sdmxReturnSheetInfoBean.setCreatedBy(sdmxReturnSheetInfoEntity.getCreatedBy().getUserId());
			sdmxReturnSheetInfoBean.setCreatedByName(sdmxReturnSheetInfoEntity.getCreatedBy().getUserName());
		}
	}

	/**
	 * @param sdmxReturnSheetInfoBean
	 * @param sdmxReturnSheetInfoEntity
	 */
	public static void convertBeanToEntity(SdmxReturnSheetInfoBean sdmxReturnSheetInfoBean, SdmxReturnSheetInfoEntity sdmxReturnSheetInfoEntity, Long returnTemplateId, Long userId) {
		BeanUtils.copyProperties(sdmxReturnSheetInfoBean, sdmxReturnSheetInfoEntity);

		// User Master Created By
		if (userId != null) {
			UserMaster userMaster = new UserMaster();
			userMaster.setUserId(userId);
			sdmxReturnSheetInfoEntity.setCreatedBy(userMaster);
			// Created On
			sdmxReturnSheetInfoEntity.setCreatedOn(new Date());
		}

		// Return Template id
		if (sdmxReturnSheetInfoBean.getReturnTemplateId() != null) {
			ReturnTemplate returnTemplate = new ReturnTemplate();
			returnTemplate.setReturnTemplateId(sdmxReturnSheetInfoBean.getReturnTemplateId());
			sdmxReturnSheetInfoEntity.setReturnTemplateIdFk(returnTemplate);
		}

		// Return Template
		if (returnTemplateId != null) {
			ReturnTemplate returnTemplate = new ReturnTemplate();
			returnTemplate.setReturnTemplateId(returnTemplateId);
			sdmxReturnSheetInfoEntity.setReturnTemplateIdFk(returnTemplate);
		}

		if (sdmxReturnSheetInfoBean.getReturnPreviewIdFk() != null) {
			sdmxReturnSheetInfoEntity.setReturnPreviewIdFk(sdmxReturnSheetInfoBean.getReturnPreviewIdFk());
		}

	}
}
