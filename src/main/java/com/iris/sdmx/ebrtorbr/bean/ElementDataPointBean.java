package com.iris.sdmx.ebrtorbr.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.iris.sdmx.upload.bean.ReturnDataPoint;

public class ElementDataPointBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4736809383563299110L;

	private String dsdCode;

	private Integer totalDataPoints;

	private Set<String> dataPoints;

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			if (this.getClass() != obj.getClass()) {
				return false;
			}
			ElementDataPointBean returnDataPointMap = (ElementDataPointBean) obj;
			if (returnDataPointMap.getDsdCode().equalsIgnoreCase(this.dsdCode)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (this.dsdCode != null) {
			return this.dsdCode.hashCode();
		}
		return 0;
	}

	public String getDsdCode() {
		return dsdCode;
	}

	public void setDsdCode(String dsdCode) {
		this.dsdCode = dsdCode;
	}

	public Integer getTotalDataPoints() {
		return totalDataPoints;
	}

	public void setTotalDataPoints(Integer totalDataPoints) {
		this.totalDataPoints = totalDataPoints;
	}

	public Set<String> getDataPoints() {
		return dataPoints;
	}

	public void setDataPoints(Set<String> dataPoints) {
		this.dataPoints = dataPoints;
	}

}
