/**
 * 
 */
package com.iris.sdmx.element.helper;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.google.gson.Gson;
import com.iris.model.UserMaster;
import com.iris.sdmx.element.bean.SdmxElementTempBean;
import com.iris.sdmx.element.entity.SdmxElementEntity;
import com.iris.sdmx.element.entity.SdmxElementTempEntity;
import com.iris.sdmx.status.entity.ActionStatus;
import com.iris.sdmx.status.entity.AdminStatus;

/**
 * @author apagaria
 *
 */
public class SdmxElementTempHelper {

	private SdmxElementTempHelper() {

	}

	/**
	 * @param sdmxElementSectorEntity
	 * @param sdmxElementSectorBean
	 */
	public static void convertEntityToBean(SdmxElementTempEntity sdmxElementTempEntity, SdmxElementTempBean sdmxElementTempBean) {
		BeanUtils.copyProperties(sdmxElementTempEntity, sdmxElementTempBean);
		// Removing extra data from user master for created By field
		if (sdmxElementTempEntity.getCreatedBy() != null && sdmxElementTempEntity.getCreatedBy().getUserId() != null) {
			sdmxElementTempBean.setCreatedBy(sdmxElementTempEntity.getCreatedBy().getUserId());
		}

		// Action Status
		if (sdmxElementTempEntity.getActionStatusFk() != null) {
			sdmxElementTempBean.setActionStatusId(sdmxElementTempEntity.getActionStatusFk().getActionId());
		}

		// Admin Status
		if (sdmxElementTempEntity.getSdmxStatusEntity() != null) {
			sdmxElementTempBean.setActionStatusId(sdmxElementTempEntity.getSdmxStatusEntity().getAdminStatusId());
		}
	}

	public static void convertBeanToEntity(SdmxElementTempBean sdmxElementTempBean, SdmxElementTempEntity sdmxElementTempEntity) {
		BeanUtils.copyProperties(sdmxElementTempBean, sdmxElementTempEntity);

		// Created By
		if (sdmxElementTempBean.getCreatedBy() != null) {
			UserMaster userMaster = new UserMaster();
			userMaster.setUserId(sdmxElementTempBean.getCreatedBy());
			sdmxElementTempEntity.setCreatedBy(userMaster);
			// Created On
			sdmxElementTempEntity.setCreatedOn(new Date());
		}

		// Status
		if (sdmxElementTempBean.getStatusId() != null) {
			AdminStatus sdmxStatusEntity = new AdminStatus();
			sdmxStatusEntity.setAdminStatusId(sdmxElementTempBean.getStatusId());
			sdmxElementTempEntity.setSdmxStatusEntity(sdmxStatusEntity);
		}

		// Sdmx Element Entity
		if (sdmxElementTempBean.getSdmxElementEntity() != null) {
			sdmxElementTempEntity.setSdmxElementEntity(new Gson().toJson(sdmxElementTempBean.getSdmxElementEntity()));
		}

		// Action Status
		if (sdmxElementTempBean.getActionStatusId() != null) {
			ActionStatus actionStatus = new ActionStatus();
			actionStatus.setActionId(sdmxElementTempBean.getActionStatusId());
			sdmxElementTempEntity.setActionStatusFk(actionStatus);
		}

		// Element Id Fk
		if (sdmxElementTempBean.getElementIdFk() != null) {
			SdmxElementEntity elementIdFk = new SdmxElementEntity();
			elementIdFk.setElementId(sdmxElementTempBean.getElementIdFk());
			sdmxElementTempEntity.setElementIdFk(elementIdFk);
		}

	}

}