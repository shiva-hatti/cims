package com.iris.ebr.business.technical.metadata.bean;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class SDMXReturnModelBean implements Serializable {

	private static final long serialVersionUID = -3005517265360230186L;

	private String dsdId;
	private List<CodeListDimension> closedDim;
	private List<CommonDimension> commonDimension;
	private List<InputDimension> openDimension;
	private String elementVersion = "1.0";
	private ModelOtherDetails modelOtherDetails;

	/**
	 * @return the dsdId
	 */
	public String getDsdId() {
		return dsdId;
	}

	/**
	 * @param dsdId the dsdId to set
	 */
	public void setDsdId(String dsdId) {
		this.dsdId = StringUtils.trim(dsdId);
	}

	/**
	 * @return the codeListDim
	 */
	public List<CodeListDimension> getClosedDim() {
		return closedDim;
	}

	/**
	 * @param codeListDim the codeListDim to set
	 */
	public void setClosedDim(List<CodeListDimension> closedDim) {
		this.closedDim = closedDim;
	}

	/**
	 * @return the commonDimension
	 */
	public List<CommonDimension> getCommonDimension() {
		return commonDimension;
	}

	/**
	 * @param commonDimension the commonDimension to set
	 */
	public void setCommonDimension(List<CommonDimension> commonDimension) {
		this.commonDimension = commonDimension;
	}

	/**
	 * @return the inputDimension
	 */
	public List<InputDimension> getOpenDimension() {
		return openDimension;
	}

	/**
	 * @param inputDimension the inputDimension to set
	 */
	public void setOpenDimension(List<InputDimension> openDimension) {
		this.openDimension = openDimension;
	}

	/**
	 * @return the elementVersion
	 */
	public String getElementVersion() {
		return elementVersion;
	}

	/**
	 * @param elementVersion the elementVersion to set
	 */
	public void setElementVersion(String elementVersion) {
		this.elementVersion = StringUtils.trim(elementVersion);
	}

	/**
	 * @return the modelOtherDetails
	 */
	public ModelOtherDetails getModelOtherDetails() {
		return modelOtherDetails;
	}

	/**
	 * @param modelOtherDetails the modelOtherDetails to set
	 */
	public void setModelOtherDetails(ModelOtherDetails modelOtherDetails) {
		this.modelOtherDetails = modelOtherDetails;
	}

}
