/**
 * 
 */
package com.iris.sdmx.element.helper;

import org.springframework.beans.BeanUtils;

import com.iris.sdmx.element.bean.SdmxElementSectorBean;
import com.iris.sdmx.element.entity.SdmxElementSectorEntity;

/**
 * @author apagaria
 *
 */
public class SdmxElementSectorHelper {

	private SdmxElementSectorHelper() {

	}

	/**
	 * @param sdmxElementSectorEntity
	 * @param sdmxElementSectorBean
	 */
	public static void convertEntityToBean(SdmxElementSectorEntity sdmxElementSectorEntity, SdmxElementSectorBean sdmxElementSectorBean) {
		BeanUtils.copyProperties(sdmxElementSectorEntity, sdmxElementSectorBean);
		// Removing extra data from user master for created By field
		if (sdmxElementSectorEntity.getCreatedBy() != null && sdmxElementSectorEntity.getCreatedBy().getUserId() != null) {
			sdmxElementSectorBean.setCreatedBy(sdmxElementSectorEntity.getCreatedBy().getUserId());
		}
		// Removing extra data from user master for modify By field
		if (sdmxElementSectorEntity.getModifyBy() != null && sdmxElementSectorEntity.getModifyBy().getUserId() != null) {
			sdmxElementSectorBean.setModifyBy(sdmxElementSectorEntity.getModifyBy().getUserId());
		}
	}

}