package com.iris.dto;

import java.io.Serializable;
import java.util.List;

public class HeaderInfoDto implements Serializable {

	private static final long serialVersionUID = 329813554599556276L;

	private Integer dynamicHeaderId;
	private String dynamicHeaderLabel;
	private List<TableInfoDto> tableInfoDtoList;

	public Integer getDynamicHeaderId() {
		return dynamicHeaderId;
	}

	public void setDynamicHeaderId(Integer dynamicHeaderId) {
		this.dynamicHeaderId = dynamicHeaderId;
	}

	public String getDynamicHeaderLabel() {
		return dynamicHeaderLabel;
	}

	public void setDynamicHeaderLabel(String dynamicHeaderLabel) {
		this.dynamicHeaderLabel = dynamicHeaderLabel;
	}

	public List<TableInfoDto> getTableInfoDtoList() {
		return tableInfoDtoList;
	}

	public void setTableInfoDtoList(List<TableInfoDto> tableInfoDtoList) {
		this.tableInfoDtoList = tableInfoDtoList;
	}

}
