package com.iris.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author nsasane
 * @date 08/12/17
 */
public class DynamicTemplateBean implements Serializable {

	private static final long serialVersionUID = 4961289582262378072L;
	private List<TableBean> dynamicTableList;
	private List<HeaderBean> headerList;

	/**
	 * @return the dynamicTableList
	 */
	public List<TableBean> getDynamicTableList() {
		return dynamicTableList;
	}

	/**
	 * @param dynamicTableList the dynamicTableList to set
	 */
	public void setDynamicTableList(List<TableBean> dynamicTableList) {
		this.dynamicTableList = dynamicTableList;
	}

	public List<HeaderBean> getHeaderList() {
		return headerList;
	}

	public void setHeaderList(List<HeaderBean> headerList) {
		this.headerList = headerList;
	}

}