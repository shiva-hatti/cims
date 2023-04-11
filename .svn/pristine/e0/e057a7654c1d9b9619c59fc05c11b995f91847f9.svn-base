package com.iris.dto;

import java.io.Serializable;
import java.util.List;

import com.iris.util.Validations;

public class OptionsUponReturnCode implements Serializable {
	private static final long serialVersionUID = 3899921646614178272L;
	private String returnCode;
	private List<DropDownOptionBean> dropDownOptionList;

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = Validations.trimInput(returnCode);
	}

	public List<DropDownOptionBean> getDropDownOptionList() {
		return dropDownOptionList;
	}

	public void setDropDownOptionList(List<DropDownOptionBean> dropDownOptionList) {
		this.dropDownOptionList = dropDownOptionList;
	}
}
