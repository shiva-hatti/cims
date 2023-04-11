/**
 * 
 */
package com.iris.sdmx.upload.bean;

import java.io.Serializable;
import java.util.List;

import com.iris.sdmx.exceltohtml.bean.DimensionDetailCategories;

/**
 * @author sajadhav
 *
 */
public class HashValueDimensionBean implements Serializable {

	private static final long serialVersionUID = -6358080902545295834L;

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
	private List<DimensionDetailCategories> dimensionDetailsCategoriesGroupWise;

	@Override
	public boolean equals(Object obj) {

		if (obj == null) {
			return false;
		}

		if (obj instanceof HashValueDimensionBean) {
			HashValueDimensionBean hashValueDimensionBean = (HashValueDimensionBean) obj;

			if (this.getDsdId().equalsIgnoreCase(hashValueDimensionBean.getDsdId()) && this.getElementVersion().equalsIgnoreCase(hashValueDimensionBean.getElementVersion())) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		return (this.dsdId + "" + this.elementVersion).hashCode();
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

	/**
	 * @return the dimensionDetailsCategoriesGroupWise
	 */
	public List<DimensionDetailCategories> getDimensionDetailsCategoriesGroupWise() {
		return dimensionDetailsCategoriesGroupWise;
	}

	/**
	 * @param dimensionDetailsCategoriesGroupWise the dimensionDetailsCategoriesGroupWise to set
	 */
	public void setDimensionDetailsCategoriesGroupWise(List<DimensionDetailCategories> dimensionDetailsCategoriesGroupWise) {
		this.dimensionDetailsCategoriesGroupWise = dimensionDetailsCategoriesGroupWise;
	}

}
