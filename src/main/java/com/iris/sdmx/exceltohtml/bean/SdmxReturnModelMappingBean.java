/**
 * 
 */
package com.iris.sdmx.exceltohtml.bean;

import java.io.Serializable;

/**
 * @author apagaria
 *
 */
public class SdmxReturnModelMappingBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8166108562134347100L;

	private Long returnTemplateId;

	private Integer returnCellRef;

	private Long returnId;

	private String modelCode;

	private boolean isMandatory;

	private String dsdCode;

	private String elementVersion;

	private Long returnPreviewIdFk;

	private String ebrVersion;

	/**
	 * @param returnTemplateId
	 * @param returnCellRef
	 */
	public SdmxReturnModelMappingBean(Long returnTemplateId, Integer returnCellRef, Long returnPreviewIdFk, String ebrVersion) {
		this.returnTemplateId = returnTemplateId;
		this.returnCellRef = returnCellRef;
		this.returnPreviewIdFk = returnPreviewIdFk;
		this.ebrVersion = ebrVersion;
	}

	public SdmxReturnModelMappingBean(Long returnId, Integer returnCellRef, String modelCode, boolean isMandatory, String dsdCode, String elementVersion) {
		this.returnId = returnId;
		this.returnCellRef = returnCellRef;
		this.modelCode = modelCode;
		this.isMandatory = isMandatory;
		this.dsdCode = dsdCode;
		this.elementVersion = elementVersion;
	}

	public SdmxReturnModelMappingBean() {

	}

	public Long getReturnId() {
		return returnId;
	}

	public void setReturnId(Long returnId) {
		this.returnId = returnId;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public boolean isMandatory() {
		return isMandatory;
	}

	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public String getDsdCode() {
		return dsdCode;
	}

	public void setDsdCode(String dsdCode) {
		this.dsdCode = dsdCode;
	}

	public String getElementVersion() {
		return elementVersion;
	}

	public void setElementVersion(String elementVersion) {
		this.elementVersion = elementVersion;
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
	 * @return the returnCellRef
	 */
	public Integer getReturnCellRef() {
		return returnCellRef;
	}

	/**
	 * @param returnCellRef the returnCellRef to set
	 */
	public void setReturnCellRef(Integer returnCellRef) {
		this.returnCellRef = returnCellRef;
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
