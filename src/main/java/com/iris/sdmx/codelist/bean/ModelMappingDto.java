package com.iris.sdmx.codelist.bean;

import java.io.Serializable;
import java.util.List;

public class ModelMappingDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8877545416210468695L;

	private String returnName;
	private String returnCode;
	private List<Integer> cellNo;
	private String sheetName;
	private String tableName;
	private String templateVersion;
	private String ebrVersion;
	private List<String> cellNoData;

	public List<Integer> getCellNo() {
		return cellNo;
	}

	public void setCellNo(List<Integer> cellNo) {
		this.cellNo = cellNo;
	}

	public String getReturnName() {
		return returnName;
	}

	public void setReturnName(String returnName) {
		this.returnName = returnName;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	/**
	 * @return the cellNoData
	 */
	public List<String> getCellNoData() {
		return cellNoData;
	}

	/**
	 * @param cellNoData the cellNoData to set
	 */
	public void setCellNoData(List<String> cellNoData) {
		this.cellNoData = cellNoData;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the templateVersion
	 */
	public String getTemplateVersion() {
		return templateVersion;
	}

	/**
	 * @param templateVersion the templateVersion to set
	 */
	public void setTemplateVersion(String templateVersion) {
		this.templateVersion = templateVersion;
	}

	/**
	 * @return the ebrVersion
	 */
	public String getEbrVersion() {
		return ebrVersion;
	}

	/**
	 * @param ebrVersion the ebrVersion to set
	 */
	public void setEbrVersion(String ebrVersion) {
		this.ebrVersion = ebrVersion;
	}

}
