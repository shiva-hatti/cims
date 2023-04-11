package com.iris.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author psheke
 * @date 11/11/2020
 */
public class CrossValStatusDet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1564108313859526706L;

	private List<ReturnDet> returnList;

	private List<CrossValidationStatus> crossValStatusList;

	/**
	 * @return the crossValStatusList
	 */
	public List<CrossValidationStatus> getCrossValStatusList() {
		return crossValStatusList;
	}

	/**
	 * @param crossValStatusList the crossValStatusList to set
	 */
	public void setCrossValStatusList(List<CrossValidationStatus> crossValStatusList) {
		this.crossValStatusList = crossValStatusList;
	}

	/**
	 * @return the returnList
	 */
	public List<ReturnDet> getReturnList() {
		return returnList;
	}

	/**
	 * @param returnList the returnList to set
	 */
	public void setReturnList(List<ReturnDet> returnList) {
		this.returnList = returnList;
	}

}
