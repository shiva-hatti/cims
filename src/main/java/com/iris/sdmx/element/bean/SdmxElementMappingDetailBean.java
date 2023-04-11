package com.iris.sdmx.element.bean;

import java.io.Serializable;
import java.util.List;

import com.iris.sdmx.dimesnsion.bean.DimensionBean;
import com.iris.sdmx.exceltohtml.bean.ModelMappingBean;

public class SdmxElementMappingDetailBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long elementId;

	private String elementCode;

	private String elementVersion;

	private String elementLabel;

	private String elementDesc;

	private String agencyLabel;

	private List<DimensionBean> dimensionBean;

	private List<ModelMappingBean> modelMappingBean;

	public Long getElementId() {
		return elementId;
	}

	public void setElementId(Long elementId) {
		this.elementId = elementId;
	}

	public String getElementCode() {
		return elementCode;
	}

	public void setElementCode(String elementCode) {
		this.elementCode = elementCode;
	}

	public String getElementVersion() {
		return elementVersion;
	}

	public void setElementVersion(String elementVersion) {
		this.elementVersion = elementVersion;
	}

	public List<DimensionBean> getDimensionBean() {
		return dimensionBean;
	}

	public void setDimensionBean(List<DimensionBean> dimensionBean) {
		this.dimensionBean = dimensionBean;
	}

	public List<ModelMappingBean> getModelMappingBean() {
		return modelMappingBean;
	}

	public void setModelMappingBean(List<ModelMappingBean> modelMappingBean) {
		this.modelMappingBean = modelMappingBean;
	}

	/**
	 * @return the elementLabel
	 */
	public String getElementLabel() {
		return elementLabel;
	}

	/**
	 * @param elementLabel the elementLabel to set
	 */
	public void setElementLabel(String elementLabel) {
		this.elementLabel = elementLabel;
	}

	/**
	 * @return the elementDesc
	 */
	public String getElementDesc() {
		return elementDesc;
	}

	/**
	 * @param elementDesc the elementDesc to set
	 */
	public void setElementDesc(String elementDesc) {
		this.elementDesc = elementDesc;
	}

	/**
	 * @return the agencyLabel
	 */
	public String getAgencyLabel() {
		return agencyLabel;
	}

	/**
	 * @param agencyLabel the agencyLabel to set
	 */
	public void setAgencyLabel(String agencyLabel) {
		this.agencyLabel = agencyLabel;
	}

}
