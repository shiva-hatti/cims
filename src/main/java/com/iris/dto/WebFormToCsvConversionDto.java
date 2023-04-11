/**
 * 
 */
package com.iris.dto;

import java.io.Serializable;

/**
 * @author Siddique
 *
 */
public class WebFormToCsvConversionDto implements Serializable {

	private static final long serialVersionUID = -9008902108626530004L;

	private DynamicFormBean dynamicFormBean;
	private String webFormjson;
	private Long returnTemplateId;

	/**
	 * @return the dynamicFormBean
	 */
	public DynamicFormBean getDynamicFormBean() {
		return dynamicFormBean;
	}

	/**
	 * @param dynamicFormBean the dynamicFormBean to set
	 */
	public void setDynamicFormBean(DynamicFormBean dynamicFormBean) {
		this.dynamicFormBean = dynamicFormBean;
	}

	/**
	 * 
	 * /**
	 * 
	 * @return the returnTemplateId
	 */
	public Long getReturnTemplateId() {
		return returnTemplateId;
	}

	/**
	 * @param returnTemplateId the returnTemplateId to set
	 */
	public void setReturnTemplateId(Long returnTemplateId) {
		this.returnTemplateId = returnTemplateId;
	}

	/**
	 * @return the webFormjson
	 */
	public String getWebFormjson() {
		return webFormjson;
	}

	/**
	 * @param webFormjson the webFormjson to set
	 */
	public void setWebFormjson(String webFormjson) {
		this.webFormjson = webFormjson;
	}

}
