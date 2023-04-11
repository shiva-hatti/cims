/**
 * 
 */
package com.iris.sdmx.exceltohtml.bean;

import java.io.Serializable;

/**
 * @author apagaria
 *
 */
public class DimensionCodeListValueBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7429616150568330779L;

	/**
	 * 
	 */
	private String clValueCode;

	/**
	 * 
	 */
	private String dimConceptId;

	/**
	 * @return the clValueCode
	 */
	public String getClValueCode() {
		return clValueCode;
	}

	/**
	 * @param clValueCode the clValueCode to set
	 */
	public void setClValueCode(String clValueCode) {
		this.clValueCode = clValueCode;
	}

	/**
	 * @return the dimConceptId
	 */
	public String getDimConceptId() {
		return dimConceptId;
	}

	/**
	 * @param dimConceptId the dimConceptId to set
	 */
	public void setDimConceptId(String dimConceptId) {
		this.dimConceptId = dimConceptId;
	}

	/**
	 *
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clValueCode == null) ? 0 : clValueCode.hashCode());
		result = prime * result + ((dimConceptId == null) ? 0 : dimConceptId.hashCode());
		return result;
	}

	/**
	 *
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DimensionCodeListValueBean other = (DimensionCodeListValueBean) obj;
		if (clValueCode == null) {
			if (other.clValueCode != null)
				return false;
		} else if (!clValueCode.equals(other.clValueCode))
			return false;
		if (dimConceptId == null) {
			if (other.dimConceptId != null)
				return false;
		} else if (!dimConceptId.equals(other.dimConceptId))
			return false;
		return true;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "DimensionCodeListValueBean [clValueCode=" + clValueCode + ", dimConceptId=" + dimConceptId + "]";
	}
}
