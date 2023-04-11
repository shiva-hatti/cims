/**
 * 
 */
package com.iris.sdmx.exceltohtml.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author apagaria
 *
 */
public class DimensionDetailCategories implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6399084498706320242L;

	/**
	 * 
	 */
	private List<DimensionCodeListValueBean> closedDim;

	/**
	 * 
	 */
	private String dsdId;

	/**
	 * 
	 */
	private String elementVersion;

	/**
	 * 
	 */
	private List<DimensionCodeListValueBean> openDimension;

	/**
	 * 
	 */
	private List<DimensionCodeListValueBean> commonDimension;

	/**
	 * 
	 */
	private RegexDetails regexDetails;

	/**
	 * 
	 */
	private ModelOtherDetails modelOtherDetails;

	/**
	 * 
	 */
	private Integer groupNo;

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
		this.dsdId = dsdId;
	}

	/**
	 * @return the closedDim
	 */
	public List<DimensionCodeListValueBean> getClosedDim() {
		return closedDim;
	}

	/**
	 * @param closedDim the closedDim to set
	 */
	public void setClosedDim(List<DimensionCodeListValueBean> closedDim) {
		this.closedDim = closedDim;
	}

	/**
	 * @return the commonDimension
	 */
	public List<DimensionCodeListValueBean> getCommonDimension() {
		return commonDimension;
	}

	/**
	 * @param commonDimension the commonDimension to set
	 */
	public void setCommonDimension(List<DimensionCodeListValueBean> commonDimension) {
		this.commonDimension = commonDimension;
	}

	/**
	 * @return the openDimension
	 */
	public List<DimensionCodeListValueBean> getOpenDimension() {
		return openDimension;
	}

	/**
	 * @param openDimension the openDimension to set
	 */
	public void setOpenDimension(List<DimensionCodeListValueBean> openDimension) {
		this.openDimension = openDimension;
	}

	/**
	 * @return
	 */
	public String getElementVersion() {
		return elementVersion;
	}

	/**
	 * @param elementVersion
	 */
	public void setElementVersion(String elementVersion) {
		this.elementVersion = elementVersion;
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

	/**
	 * @return the groupNo
	 */
	public Integer getGroupNo() {
		return groupNo;
	}

	/**
	 * @param groupNo the groupNo to set
	 */
	public void setGroupNo(Integer groupNo) {
		this.groupNo = groupNo;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "DimensionDetailCategories [dsdId=" + dsdId + ", closedDim=" + closedDim + ", commonDimension=" + commonDimension + ", openDimension=" + openDimension + ", regexDetails=" + regexDetails + ", elementVersion=" + elementVersion + "]";
	}

}
