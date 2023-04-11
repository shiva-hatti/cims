package com.iris.sdmx.dimesnsion.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class DimensionTypeDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2240707174603501746L;

	private Long dimensionTypeId;
	private String dimensionTypeName;
	private Date lastUpdatedOn;

	public Long getDimensionTypeId() {
		return dimensionTypeId;
	}

	public void setDimensionTypeId(Long dimensionTypeId) {
		this.dimensionTypeId = dimensionTypeId;
	}

	public String getDimensionTypeName() {
		return dimensionTypeName;
	}

	public void setDimensionTypeName(String dimensionTypeName) {
		this.dimensionTypeName = dimensionTypeName;
	}

	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

}
