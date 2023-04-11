/**
 * 
 */
package com.iris.sdmx.status.helper;

import org.springframework.beans.BeanUtils;

import com.iris.sdmx.status.bean.AdminStatusBean;
import com.iris.sdmx.status.entity.AdminStatus;

/**
 * @author apagaria
 *
 */
public class AdminStatusHelper {

	private AdminStatusHelper() {

	}

	/**
	 * @param sdmxStatusEntity
	 * @param sdmxStatusBean
	 */
	public static void convertEntityToBean(AdminStatus adminStatus, AdminStatusBean adminStatusBean) {
		BeanUtils.copyProperties(adminStatus, adminStatusBean);
	}

}