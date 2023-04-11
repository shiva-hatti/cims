package com.iris.sdmx.dataType;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_DATA_TYPE")
public class FieldDataType implements Serializable {

	/**
	 * @author sdhone
	 */
	private static final long serialVersionUID = -1207358208356416185L;

	@Id
	@Column(name = "DATA_TYPE_ID")
	private Long dataTypeId;

	@Column(name = "DATA_TYPE_NAME")
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