package com.iris.sdmx.exceltohtml.bean;

import java.io.Serializable;
import java.util.List;

import com.iris.util.Validations;

public class SDMXReturnModelBean implements Serializable {

	private static final long serialVersionUID = -3005517265360230186L;

	private String dsdId;
	private List<CodeListDimension> closedDim;
	private List<CommonDimension> commonDimension;
	private List<InputDimension> openDimension;
	private RegexDetails regexDetails;
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
		this.dsdId = Validations.trimInput(dsdId);
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
		this.elementVersion = Validations.trimInput(elementVersion);
	}

	/**
	 * @return the regexDetails
	 */
	public RegexDetails getRegexDetails() {
		return regexDetails;
	}

	/**
	 * @param regexDetails the regexDetails to set
	 */
	public void setRegexDetails(RegexDetails regexDetails) {
		this.regexDetails = regexDetails;
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
