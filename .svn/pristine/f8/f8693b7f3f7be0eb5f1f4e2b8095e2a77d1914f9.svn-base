/**
 * 
 */
package com.iris.sdmx.element.helper;

import org.springframework.beans.BeanUtils;

import com.iris.sdmx.element.bean.SdmxElementDependencyTypeBean;
import com.iris.sdmx.element.entity.SdmxElementDependencyTypeEntity;

/**
 * @author apagaria
 *
 */
public class SdmxElementDependencyTypeHelper {

	private SdmxElementDependencyTypeHelper() {

	}

	/**
	 * @param sdmxElementDependencyTypeEntity
	 * @param sdmxElementDependencyTypeBean
	 */
	public static void convertEntityToBean(SdmxElementDependencyTypeEntity sdmxElementDependencyTypeEntity, SdmxElementDependencyTypeBean sdmxElementDependencyTypeBean) {
		BeanUtils.copyProperties(sdmxElementDependencyTypeEntity, sdmxElementDependencyTypeBean);
		// Removing extra data from user master for created By field
		if (sdmxElementDependencyTypeEntity.getCreatedBy() != null && sdmxElementDependencyTypeEntity.getCreatedBy().getUserId() != null) {
			sdmxElementDependencyTypeBean.setCreatedBy(sdmxElementDependencyTypeEntity.getCreatedBy().getUserId());
		}
		// Removing extra data from user master for modify By field
		if (sdmxElementDependencyTypeEntity.getModifyBy() != null && sdmxElementDependencyTypeEntity.getModifyBy().getUserId() != null) {
			sdmxElementDependencyTypeBean.setModifyBy(sdmxElementDependencyTypeEntity.getModifyBy().getUserId());
		}
	}

}