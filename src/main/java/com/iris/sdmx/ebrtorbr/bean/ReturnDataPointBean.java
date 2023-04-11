/**
 * 
 */
package com.iris.sdmx.ebrtorbr.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author sajadhav
 *
 */
public class ReturnDataPointBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5555287211199495938L;

	private Integer totalDataPoints;

	private List<ElementDataPointBean> dataPointList;

	public Integer getTotalDataPoints() {
		return totalDataPoints;
	}

	public void setTotalDataPoints(Integer totalDataPoints) {
		this.totalDataPoints = totalDataPoints;
	}

	public List<ElementDataPointBean> getDataPointList() {
		return dataPointList;
	}

	public void setDataPointList(List<ElementDataPointBean> dataPointList) {
		this.dataPointList = dataPointList;
	}
}
