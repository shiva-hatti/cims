/**
 * 
 */
package com.iris.sdmx.element.helper;

import org.springframework.beans.BeanUtils;

import com.iris.sdmx.element.bean.SdmxElementClassificationBean;
import com.iris.sdmx.element.entity.SdmxElementClassificationEntity;

/**
 * @author apagaria
 *
 */
public class SdmxElementClassificationHelper {

	private SdmxElementClassificationHelper() {

	}

	/**
	 * @param sdmxElementClassificationEntity
	 * @param sdmxElementClassificationBean
	 */
	public static void convertEntityToBean(SdmxElementClassificationEntity sdmxElementClassificationEntity, SdmxElementClassificationBean sdmxElementClassificationBean) {
		BeanUtils.copyProperties(sdmxElementClassificationEntity, sdmxElementClassificationBean);
		// Removing extra data from user master for created By field
		if (sdmxElementClassificationEntity.getCreatedBy() != null && sdmxElementClassificationEntity.getCreatedBy().getUserId() != null) {
			sdmxElementClassificationBean.setCreatedBy(sdmxElementClassificationEntity.getCreatedBy().getUserId());
		}
		// Removing extra data from user master for modify By field
		if (sdmxElementClassificationEntity.getModifyBy() != null && sdmxElementClassificationEntity.getModifyBy().getUserId() != null) {
			sdmxElementClassificationBean.setModifyBy(sdmxElementClassificationEntity.getModifyBy().getUserId());
		}
	}

}
