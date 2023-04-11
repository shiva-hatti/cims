/**
 * 
 */
package com.iris.sdmx.status.helper;

import org.springframework.beans.BeanUtils;

import com.iris.sdmx.status.bean.ActionStatusBean;
import com.iris.sdmx.status.entity.ActionStatus;

/**
 * @author apagaria
 *
 */
public class ActionStatusHelper {

	private ActionStatusHelper() {

	}

	/**
	 * @param sdmxStatusEntity
	 * @param sdmxStatusBean
	 */
	public static void convertEntityToBean(ActionStatus adminStatus, ActionStatusBean actionStatusBean) {
		BeanUtils.copyProperties(adminStatus, actionStatusBean);
	}

}