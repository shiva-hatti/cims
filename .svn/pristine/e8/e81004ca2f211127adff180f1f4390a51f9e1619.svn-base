package com.iris.sdmx.element.helper;

import org.springframework.beans.BeanUtils;

import com.iris.sdmx.element.bean.SdmxElementRegulatorBean;
import com.iris.sdmx.element.entity.SdmxElementRegulatorEntity;

public class SdmxElementRegulatorHelper {

	private SdmxElementRegulatorHelper() {

	}

	public static void convertEntityToBean(SdmxElementRegulatorEntity sdmxElementRegulatorEntity, SdmxElementRegulatorBean sdmxElementRegulatorBean) {

		BeanUtils.copyProperties(sdmxElementRegulatorEntity, sdmxElementRegulatorBean);

		// Removing extra data from user master for created By field
		if (sdmxElementRegulatorEntity.getCreatedBy() != null && sdmxElementRegulatorEntity.getCreatedBy().getUserId() != null) {
			sdmxElementRegulatorBean.setCreatedBy(sdmxElementRegulatorEntity.getCreatedBy().getUserId());
		}
		// Removing extra data from user master for modify By field
		if (sdmxElementRegulatorEntity.getModifyBy() != null && sdmxElementRegulatorEntity.getModifyBy().getUserId() != null) {
			sdmxElementRegulatorBean.setModifyBy(sdmxElementRegulatorEntity.getModifyBy().getUserId());
		}
	}
}
