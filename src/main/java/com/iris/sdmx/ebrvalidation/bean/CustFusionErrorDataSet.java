/**
 * 
 */
package com.iris.sdmx.ebrvalidation.bean;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author sajadhav
 *
 */
@JsonInclude(Include.NON_NULL)
public class CustFusionErrorDataSet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6012613237909454032L;

	private String dsd;

	private String obsCount;

	private List<CustFusionError> errors;

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof CustFusionErrorDataSet)) {
			return false;
		}

		CustFusionErrorDataSet fusionErrorDataSet = (CustFusionErrorDataSet) obj;
		return this.getDsd().equals(fusionErrorDataSet.getDsd());
	}

	@Override
	public int hashCode() {
		return this.getDsd().hashCode();
	}

	public String getDsd() {
		return dsd;
	}

	public void setDsd(String dsd) {
		this.dsd = dsd;
	}

	public String getObsCount() {
		return obsCount;
	}

	public void setObsCount(String obsCount) {
		this.obsCount = obsCount;
	}

	public List<CustFusionError> getErrors() {
		return errors;
	}

	public void setErrors(List<CustFusionError> errors) {
		this.errors = errors;
	}

}
