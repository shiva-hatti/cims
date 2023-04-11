package com.iris.sdmx.element.helper;

import org.springframework.beans.BeanUtils;

import com.iris.sdmx.element.bean.SdmxElementUsageBean;
import com.iris.sdmx.element.entity.SdmxElementUsageEntity;

public class SdmxElementUsageHelper {

	/**
	 * @param SdmxElementUsageEntity
	 * @param SdmxElementUsageBean
	 */
	public static void convertEntityToBean(SdmxElementUsageEntity sdmxElementUsageEntity, SdmxElementUsageBean sdmxElementUsageBean) {
		BeanUtils.copyProperties(sdmxElementUsageEntity, sdmxElementUsageBean);
		// Removing extra data from user master for created By field
		if (sdmxElementUsageEntity.getCreatedBy() != null && sdmxElementUsageEntity.getCreatedBy().getUserId() != null) {
			sdmxElementUsageBean.setCreatedBy(sdmxElementUsageEntity.getCreatedBy().getUserId());
		}
		// Removing extra data from user master for modify By field
		if (sdmxElementUsageEntity.getModifyBy() != null && sdmxElementUsageEntity.getModifyBy().getUserId() != null) {
			sdmxElementUsageBean.setModifyBy(sdmxElementUsageEntity.getModifyBy().getUserId());
		}
	}
}
