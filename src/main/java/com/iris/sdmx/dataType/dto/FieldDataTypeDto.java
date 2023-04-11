package com.iris.sdmx.dataType.dto;

import java.io.Serializable;

public class FieldDataTypeDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -155452184382042446L;

	private Long dataTypeId;
	private String dataTypeName;

	public Long getDataTypeId() {
		return dataTypeId;
	}

	public void setDataTypeId(Long dataTypeId) {
		this.dataTypeId = dataTypeId;
	}

	public String getDataTypeName() {
		return dataTypeName;
	}

	public void setDataTypeName(String dataTypeName) {
		this.dataTypeName = dataTypeName;
	}

}
