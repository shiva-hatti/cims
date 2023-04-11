/**
 * 
 */
package com.iris.sdmx.ebrvalidation.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author sajadhav
 *
 */
public class CustFusionErrorType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6259164830750869899L;

	private String type;

	private List<CustFusionErrorDataSet> dataSets;

	@Override
	public boolean equals(Object obj) {

		if (obj == null) {
			return false;
		}

		if (!(obj instanceof CustFusionErrorType)) {
			return false;
		}

		CustFusionErrorType errorType = (CustFusionErrorType) obj;
		return this.getType().equals(errorType.getType());
	}

	@Override
	public int hashCode() {
		return this.getType().hashCode();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<CustFusionErrorDataSet> getDataSets() {
		return dataSets;
	}

	public void setDataSets(List<CustFusionErrorDataSet> dataSets) {
		this.dataSets = dataSets;
	}

}
