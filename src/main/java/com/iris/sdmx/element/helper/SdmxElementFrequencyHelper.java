package com.iris.sdmx.element.helper;

import org.springframework.beans.BeanUtils;

import com.iris.sdmx.element.bean.SdmxElementFrequencyBean;
import com.iris.sdmx.element.entity.SdmxElementFrequencyEntity;

public class SdmxElementFrequencyHelper {

	/**
	 * @param sdmxElementClassificationEntity
	 * @param sdmxElementClassificationBean
	 */
	public static void convertEntityToBean(SdmxElementFrequencyEntity sdmxElementFrequencyEntity, SdmxElementFrequencyBean sdmxElementFrequencyBean) {
		BeanUtils.copyProperties(sdmxElementFrequencyEntity, sdmxElementFrequencyBean);
		// Removing extra data from user master for created By field
		if (sdmxElementFrequencyEntity.getCreatedBy() != null && sdmxElementFrequencyEntity.getCreatedBy().getUserId() != null) {
			sdmxElementFrequencyBean.setCreatedBy(sdmxElementFrequencyEntity.getCreatedBy().getUserId());
		}
		// Removing extra data from user master for modify By field
		if (sdmxElementFrequencyEntity.getModifyBy() != null && sdmxElementFrequencyEntity.getModifyBy().getUserId() != null) {
			sdmxElementFrequencyBean.setModifyBy(sdmxElementFrequencyEntity.getModifyBy().getUserId());
		}
	}
}
