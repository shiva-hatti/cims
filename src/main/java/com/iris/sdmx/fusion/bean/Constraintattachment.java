package com.iris.sdmx.fusion.bean;

import java.util.List;

/**
 * @author apagaria
 *
 */
public class Constraintattachment {

	private List<String> dataStructures;

	/**
	 * @return the dataStructures
	 */
	public List<String> getDataStructures() {
		return dataStructures;
	}

	/**
	 * @param dataStructures the dataStructures to set
	 */
	public void setDataStructures(List<String> dataStructures) {
		this.dataStructures = dataStructures;
	}

	@Override
	public String toString() {
		return "Constraintattachment [dataStructures=" + dataStructures + "]";
	}

}