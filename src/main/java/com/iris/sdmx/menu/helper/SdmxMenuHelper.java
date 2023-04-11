package com.iris.sdmx.menu.helper;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.iris.sdmx.menu.bean.SdmxActionMenuMappingBean;
import com.iris.sdmx.menu.bean.SdmxMenuBean;
import com.iris.sdmx.menu.entity.SdmxActionMenuMapping;
import com.iris.sdmx.menu.entity.SdmxMenu;

/**
 * @author vjadhav
 *
 */
@Component
public class SdmxMenuHelper {

	/**
	 * 
	 */

	private static final Logger LOGGER = LogManager.getLogger(SdmxMenuHelper.class);

	public void convertEntityToBean(SdmxMenu sdmxMenu, SdmxMenuBean sdmxMenuBean, Map<String, String> fieldKeyLabelMap, String jobProcessId) {
		LOGGER.info("END - convert bean to entity request received with Job Processing ID : " + jobProcessId);

		try {
			if (sdmxMenu != null) {

				if (sdmxMenu.getSdmxMenuId() != null) {
					sdmxMenuBean.setSdmxMenuId(sdmxMenu.getSdmxMenuId());
				}

				if (sdmxMenu.getLevel() == 1) {
					sdmxMenuBean.setParentMenuId(0L);
				} else {
					if (sdmxMenu.getParentMenuId() != null) {
						sdmxMenuBean.setParentMenuId(sdmxMenu.getParentMenuId().getSdmxMenuId());
					}
				}

				if (sdmxMenu.getDefaultName() != null) {
					sdmxMenuBean.setDefaultName(sdmxMenu.getDefaultName());
				}

				if (sdmxMenu.getSdmxMenuUrl() != null) {
					sdmxMenuBean.setSdmxMenuUrl(sdmxMenu.getSdmxMenuUrl());
				}

				if (sdmxMenu.getIcon() != null) {
					sdmxMenuBean.setIcon(sdmxMenu.getIcon());
				}

				if (sdmxMenu.getMenuLabelKey() != null) {
					sdmxMenuBean.setMenuLabelKey(sdmxMenu.getMenuLabelKey());
					if (!fieldKeyLabelMap.isEmpty()) {
						if (fieldKeyLabelMap.get(sdmxMenu.getMenuLabelKey()) != null) {
							sdmxMenuBean.setMenuLabel(fieldKeyLabelMap.get(sdmxMenu.getMenuLabelKey()));

						}
					}
				}

				if (sdmxMenu.getViewRight() != null) {
					sdmxMenuBean.setViewRight(sdmxMenu.getViewRight());
				}
				if (sdmxMenu.getAddRight() != null) {
					sdmxMenuBean.setAddRight(sdmxMenu.getAddRight());
				}
				if (sdmxMenu.getEditRight() != null) {
					sdmxMenuBean.setEditRight(sdmxMenu.getEditRight());
				}
				if (sdmxMenu.getAddApproval() != null) {
					sdmxMenuBean.setAddApproval(sdmxMenu.getAddApproval());
				}
				if (sdmxMenu.getEditApproval() != null) {
					sdmxMenuBean.setEditApproval(sdmxMenu.getEditApproval());
				}
				sdmxMenuBean.setLevel(sdmxMenu.getLevel());
				sdmxMenuBean.setOrderNo(sdmxMenu.getOrderNo());
				sdmxMenuBean.setGroupId(sdmxMenu.getGroupId());

			}

		} catch (Exception e) {
			LOGGER.error("ERROR occurred while converting entity to bean with " + jobProcessId + " JobProcessingId :", e);
		}
		LOGGER.info("END - convert bean to entity request completed with Job Processing ID : " + jobProcessId);

	}

	public void convertEntityToBean(SdmxActionMenuMapping sdmxActionMenuMapping, SdmxActionMenuMappingBean sdmxActionMenuMappingBean, String jobProcessId) {
		LOGGER.info("END - convert bean to entity request received with Job Processing ID : " + jobProcessId);

		try {
			if (sdmxActionMenuMapping != null) {

				if (sdmxActionMenuMapping.getSdmxActionMenuId() != null) {
					sdmxActionMenuMappingBean.setSdmxActionMenuId(sdmxActionMenuMapping.getSdmxActionMenuId());
				}

				if (sdmxActionMenuMapping.getActionName() != null) {
					sdmxActionMenuMappingBean.setActionName(sdmxActionMenuMapping.getActionName());
				}

				if (sdmxActionMenuMapping.getSdmxMenuIdFk() != null) {
					sdmxActionMenuMappingBean.setSdmxMenuIdFk(sdmxActionMenuMapping.getSdmxMenuIdFk().getSdmxMenuId());
				}
			}

		} catch (Exception e) {
			LOGGER.error("ERROR occurred while converting entity to bean with " + jobProcessId + " JobProcessingId :", e);
		}
		LOGGER.info("END - convert bean to entity request completed with Job Processing ID : " + jobProcessId);

	}
}
