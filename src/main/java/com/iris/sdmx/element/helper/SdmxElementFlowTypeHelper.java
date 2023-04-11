/**
 * 
 */
package com.iris.sdmx.element.helper;

import org.springframework.beans.BeanUtils;

import com.iris.sdmx.element.bean.SdmxElementFlowTypeBean;
import com.iris.sdmx.element.entity.SdmxElementFlowTypeEntity;

/**
 * @author apagaria
 *
 */
public class SdmxElementFlowTypeHelper {

	private SdmxElementFlowTypeHelper() {

	}

	/**
	 * @param sdmxElementFlowTypeEntity
	 * @param sdmxElementFlowTypeBean
	 */
	public static void convertEntityToBean(SdmxElementFlowTypeEntity sdmxElementFlowTypeEntity, SdmxElementFlowTypeBean sdmxElementFlowTypeBean) {
		BeanUtils.copyProperties(sdmxElementFlowTypeEntity, sdmxElementFlowTypeBean);
		// Removing extra data from user master for created By field
		if (sdmxElementFlowTypeEntity.getCreatedBy() != null && sdmxElementFlowTypeEntity.getCreatedBy().getUserId() != null) {
			sdmxElementFlowTypeBean.setCreatedBy(sdmxElementFlowTypeEntity.getCreatedBy().getUserId());
		}
		// Removing extra data from user master for modify By field
		if (sdmxElementFlowTypeEntity.getModifyBy() != null && sdmxElementFlowTypeEntity.getModifyBy().getUserId() != null) {
			sdmxElementFlowTypeBean.setModifyBy(sdmxElementFlowTypeEntity.getModifyBy().getUserId());
		}
	}

}