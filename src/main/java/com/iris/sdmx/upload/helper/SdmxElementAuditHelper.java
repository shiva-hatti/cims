/**
 * 
 */
package com.iris.sdmx.upload.helper;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.google.common.reflect.TypeToken;
import com.iris.sdmx.upload.bean.ElementAuditBean;
import com.iris.sdmx.upload.bean.RepDateReturnDataPoint;
import com.iris.sdmx.upload.entity.ElementAudit;
import com.iris.util.JsonUtility;

/**
 * @author apagaria
 *
 */
public class SdmxElementAuditHelper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5404975705721983289L;

	/**
	 * @param elementAudit
	 * @param elementAuditBean
	 */
	public static void convertElementAuditEntityToBean(ElementAudit elementAudit, ElementAuditBean elementAuditBean) {
		// Copy same properties
		BeanUtils.copyProperties(elementAudit, elementAuditBean);

		if (!StringUtils.isEmpty(elementAudit.getEleReturnRef())) {
			String eleReturnRefJsonStr = elementAudit.getEleReturnRef();
			Type typeToken = new TypeToken<List<RepDateReturnDataPoint>>() {
				private static final long serialVersionUID = 1L;
			}.getType();
			List<RepDateReturnDataPoint> repDateReturnDataPointMapList = JsonUtility.getGsonObject().fromJson(eleReturnRefJsonStr, typeToken);
			List<String> endDateList = null;
			if (!CollectionUtils.isEmpty(repDateReturnDataPointMapList)) {
				endDateList = new ArrayList<>();
				for (RepDateReturnDataPoint repDateReturnDataPoint : repDateReturnDataPointMapList) {
					endDateList.add(repDateReturnDataPoint.getEndDate());
				}
				elementAuditBean.setEndDateList(endDateList);
			}
		}

	}
}
