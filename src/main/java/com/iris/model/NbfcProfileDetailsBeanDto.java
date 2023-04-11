package com.iris.model;

import java.io.Serializable;
import com.iris.util.UtilMaster;

public class NbfcProfileDetailsBeanDto implements Serializable {

	private static final long serialVersionUID = 1778840879673729094L;
	private String pageJson;
	private Integer pageNumber;
	private String entityCode;
	private Long userId;

	public String getPageJson() {
		return pageJson;
	}

	public void setPageJson(String pageJson) {
		this.pageJson = UtilMaster.trimInput(pageJson);
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = UtilMaster.trimInput(entityCode);
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
