/**
 * 
 */
package com.iris.sdmx.exceltohtml.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author apagaria
 *
 */
public class SdmxReturnSheetInfoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Long returnPreviewTypeId;

	private Long returnTemplateId;

	private String returnName;

	private String returnCode;

	private Long returnId;

	private String returnVersion;

	private String sheetCode;

	private String sheetName;

	private Map<String, String> sectionInfo;

	private Long createdBy;

	private String createdByName;

	private Date createdOn;

	private Long returnPreviewIdFk;

	/**
	 * @return the returnPreviewTypeId
	 */
	public Long getReturnPreviewTypeId() {
		return returnPreviewTypeId;
	}

	/**
	 * @param returnPreviewTypeId the returnPreviewTypeId to set
	 */
	public void setReturnPreviewTypeId(Long returnPreviewTypeId) {
		this.returnPreviewTypeId = returnPreviewTypeId;
	}

	/**
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
	 * @return the returnName
	 */
	public String getReturnName() {
		return returnName;
	}

	/**
	 * @param returnName the returnName to set
	 */
	public void setReturnName(String returnName) {
		this.returnName = returnName;
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
		this.returnCode = returnCode;
	}

	/**
	 * @return the returnId
	 */
	public Long getReturnId() {
		return returnId;
	}

	/**
	 * @param returnId the returnId to set
	 */
	public void setReturnId(Long returnId) {
		this.returnId = returnId;
	}

	/**
	 * @return the returnVersion
	 */
	public String getReturnVersion() {
		return returnVersion;
	}

	/**
	 * @param returnVersion the returnVersion to set
	 */
	public void setReturnVersion(String returnVersion) {
		this.returnVersion = returnVersion;
	}

	/**
	 * @return the sheetCode
	 */
	public String getSheetCode() {
		return sheetCode;
	}

	/**
	 * @param sheetCode the sheetCode to set
	 */
	public void setSheetCode(String sheetCode) {
		this.sheetCode = sheetCode;
	}

	/**
	 * @return the sheetName
	 */
	public String getSheetName() {
		return sheetName;
	}

	/**
	 * @param sheetName the sheetName to set
	 */
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	/**
	 * @return the sectionInfo
	 */
	public Map<String, String> getSectionInfo() {
		return sectionInfo;
	}

	/**
	 * @param sectionInfo the sectionInfo to set
	 */
	public void setSectionInfo(Map<String, String> sectionInfo) {
		this.sectionInfo = sectionInfo;
	}

	/**
	 * @return the createdBy
	 */
	public Long getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdByName
	 */
	public String getCreatedByName() {
		return createdByName;
	}

	/**
	 * @param createdByName the createdByName to set
	 */
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the returnPreviewIdFk
	 */
	public Long getReturnPreviewIdFk() {
		return returnPreviewIdFk;
	}

	/**
	 * @param returnPreviewIdFk the returnPreviewIdFk to set
	 */
	public void setReturnPreviewIdFk(Long returnPreviewIdFk) {
		this.returnPreviewIdFk = returnPreviewIdFk;
	}

}
