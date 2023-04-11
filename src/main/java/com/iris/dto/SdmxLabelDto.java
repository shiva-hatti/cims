package com.iris.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SdmxLabelDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -930828326640726931L;

	List<ErrorLableDto> errorlabelDto = new ArrayList<>();

	List<CommonLableDto> commonlabelDto = new ArrayList<>();

	List<FieldLableDto> fildlabelDto = new ArrayList<>();

	public List<ErrorLableDto> getErrorlabelDto() {
		return errorlabelDto;
	}

	public void setErrorlabelDto(List<ErrorLableDto> errorlabelDto) {
		this.errorlabelDto = errorlabelDto;
	}

	public List<CommonLableDto> getCommonlabelDto() {
		return commonlabelDto;
	}

	public void setCommonlabelDto(List<CommonLableDto> commonlabelDto) {
		this.commonlabelDto = commonlabelDto;
	}

	public List<FieldLableDto> getFildlabelDto() {
		return fildlabelDto;
	}

	public void setFildlabelDto(List<FieldLableDto> fildlabelDto) {
		this.fildlabelDto = fildlabelDto;
	}

}
