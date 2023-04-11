/**
 * 
 */
package com.iris.sdmx.exceltohtml.bean;

import java.io.Serializable;

import com.iris.util.Validations;

/**
 * @author apagaria
 *
 */
public class ProcessUploadInputBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1264243757740796633L;

	private String excelFilePath;

	private String htmlFilePath;

	private String fileName;

	private String returnCode;

	private String sheetInfoBean;

	private String sheetCellMappingJson;

	private String oldReturnTemplateId;

	private String oldReturnPreviewId;

	private String userSpecificFileName;

	private String agencyCode;

	/**
	 * @return the excelFilePath
	 */
	public String getExcelFilePath() {
		return excelFilePath;
	}

	/**
	 * @param excelFilePath the excelFilePath to set
	 */
	public void setExcelFilePath(String excelFilePath) {
		this.excelFilePath = excelFilePath;
	}

	/**
	 * @return the htmlFilePath
	 */
	public String getHtmlFilePath() {
		return htmlFilePath;
	}

	/**
	 * @param htmlFilePath the htmlFilePath to set
	 */
	public void setHtmlFilePath(String htmlFilePath) {
		this.htmlFilePath = Validations.trimInput(htmlFilePath);
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = Validations.trimInput(fileName);
	}

	/**
	 * @return the returnCode
	 */
	public String getReturnCode() {
		return returnCode;
	}

	/**
	 * @param returnCode the returnCode to set
	 */
	public void setReturnCode(String returnCode) {
		this.returnCode = Validations.trimInput(returnCode);
	}

	/**
	 * @return the sheetInfoBean
	 */
	public String getSheetInfoBean() {
		return sheetInfoBean;
	}

	/**
	 * @param sheetInfoBean the sheetInfoBean to set
	 */
	public void setSheetInfoBean(String sheetInfoBean) {
		this.sheetInfoBean = Validations.trimInput(sheetInfoBean);
	}

	/**
	 * @return the sheetCellMappingJson
	 */
	public String getSheetCellMappingJson() {
		return sheetCellMappingJson;
	}

	/**
	 * @param sheetCellMappingJson the sheetCellMappingJson to set
	 */
	public void setSheetCellMappingJson(String sheetCellMappingJson) {
		this.sheetCellMappingJson = sheetCellMappingJson;
	}

	/**
	 * @return the oldReturnTemplateId
	 */
	public String getOldReturnTemplateId() {
		return oldReturnTemplateId;
	}

	/**
	 * @param oldReturnTemplateId the oldReturnTemplateId to set
	 */
	public void setOldReturnTemplateId(String oldReturnTemplateId) {
		this.oldReturnTemplateId = oldReturnTemplateId;
	}

	/**
	 * @return the oldReturnPreviewId
	 */
	public String getOldReturnPreviewId() {
		return oldReturnPreviewId;
	}

	/**
	 * @param oldReturnPreviewId the oldReturnPreviewId to set
	 */
	public void setOldReturnPreviewId(String oldReturnPreviewId) {
		this.oldReturnPreviewId = oldReturnPreviewId;
	}

	/**
	 * @return the userSpecificFileName
	 */
	public String getUserSpecificFileName() {
		return userSpecificFileName;
	}

	/**
	 * @param userSpecificFileName the userSpecificFileName to set
	 */
	public void setUserSpecificFileName(String userSpecificFileName) {
		this.userSpecificFileName = userSpecificFileName;
	}

	/**
	 * @return the agencyCode
	 */
	public String getAgencyCode() {
		return agencyCode;
	}

	/**
	 * @param agencyCode the agencyCode to set
	 */
	public void setAgencyCode(String agencyCode) {
		this.agencyCode = agencyCode;
	}

	@Override
	public String toString() {
		return "ProcessUploadInputBean [excelFilePath=" + excelFilePath + ", htmlFilePath=" + htmlFilePath + ", fileName=" + fileName + ", returnCode=" + returnCode + ", sheetInfoBean=" + sheetInfoBean + ", sheetCellMappingJson=" + sheetCellMappingJson + ", oldReturnTemplateId=" + oldReturnTemplateId + ", oldReturnPreviewId=" + oldReturnPreviewId + ", userSpecificFileName=" + userSpecificFileName + ", agencyCode=" + agencyCode + "]";
	}
}
