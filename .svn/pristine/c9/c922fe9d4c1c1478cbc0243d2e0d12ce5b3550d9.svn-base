package com.iris.dto;

import java.io.Serializable;
import java.util.List;
import com.iris.util.Validations;

public class DropDownOptionBean implements Serializable {

	private static final long serialVersionUID = 6282723385805498095L;
	private String dropDownTypeName;
	private String dropDownTypeCode;
	private List<Option> optionList;

	public String getDropDownTypeName() {
		return dropDownTypeName;
	}

	public void setDropDownTypeName(String dropDownTypeName) {
		this.dropDownTypeName = Validations.trimInput(dropDownTypeName);
	}

	public String getDropDownTypeCode() {
		return dropDownTypeCode;
	}

	public void setDropDownTypeCode(String dropDownTypeCode) {
		this.dropDownTypeCode = Validations.trimInput(dropDownTypeCode);
	}

	public List<Option> getOptionList() {
		return optionList;
	}

	public void setOptionList(List<Option> optionList) {
		this.optionList = optionList;
	}

}
