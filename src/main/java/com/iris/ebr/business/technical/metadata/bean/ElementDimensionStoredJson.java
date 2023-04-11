/**
 * 
 */
package com.iris.ebr.business.technical.metadata.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author sajadhav
 *
 */

public class ElementDimensionStoredJson implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5540263941623331507L;

	private String dsdId;

	private String elementVersion;

	private List<DimCombination> dimCombination;

	private Boolean isActive;

	/**
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

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
	 * @return the dimCombination
	 */
	public List<DimCombination> getDimCombination() {
		return dimCombination;
	}

	/**
	 * @param dimCombination the dimCombination to set
	 */
	public void setDimCombination(List<DimCombination> dimCombination) {
		this.dimCombination = dimCombination;
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
		this.elementVersion = elementVersion;
	}

	@Override
	public String toString() {
		return "ElementDimensionStoredJson [dsdId=" + dsdId + ", elementVersion=" + elementVersion + ", dimCombination=" + dimCombination + ", isActive=" + isActive + "]";
	}

}
