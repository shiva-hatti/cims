package com.iris.sdmx.upload.bean;

import java.io.Serializable;
import java.util.List;

public class RepDateReturnDataPoint implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2308360572142316794L;

	private String endDate;

	private List<ReturnDataPoint> returnList;

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<ReturnDataPoint> getReturnList() {
		return returnList;
	}

	public void setReturnList(List<ReturnDataPoint> returnList) {
		this.returnList = returnList;
	}

}
