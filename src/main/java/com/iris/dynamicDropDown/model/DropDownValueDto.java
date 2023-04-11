package com.iris.dynamicDropDown.model;

import java.io.Serializable;
import java.util.List;

import com.iris.util.Validations;

public class DropDownValueDto implements Serializable {
	private static final long serialVersionUID = 6532438437725520190L;

	private String returnCode;
	private List<String> elrTagList;
	private List<String> conceptList;

	/**
	 * @return the returnCode
	 */
	public String getReturnCode() {
		return returnCode;
	}

	/**
	 * @param returnCode the returnCode to set
	 */
	public void setReturnCode(String returnCode) {
		this.returnCode = Validations.trimInput(returnCode);
	}

	/**
	 * @return the elrTagList
	 */
	public List<String> getElrTagList() {
		return elrTagList;
	}

	/**
	 * @param elrTagList the elrTagList to set
	 */
	public void setElrTagList(List<String> elrTagList) {
		this.elrTagList = elrTagList;
	}

	/**
	 * @return the conceptList
	 */
	public List<String> getConceptList() {
		return conceptList;
	}

	/**
	 * @param conceptList the conceptList to set
	 */
	public void setConceptList(List<String> conceptList) {
		this.conceptList = conceptList;
	}
}
