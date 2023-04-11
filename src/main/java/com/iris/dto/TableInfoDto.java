package com.iris.dto;

import java.io.Serializable;

public class TableInfoDto implements Serializable {

	private static final long serialVersionUID = -1490878673226490813L;

	private Long returnSecMapId;
	private String returnSecMapLabel;

	public Long getReturnSecMapId() {
		return returnSecMapId;
	}

	public void setReturnSecMapId(Long returnSecMapId) {
		this.returnSecMapId = returnSecMapId;
	}

	public String getReturnSecMapLabel() {
		return returnSecMapLabel;
	}

	public void setReturnSecMapLabel(String returnSecMapLabel) {
		this.returnSecMapLabel = returnSecMapLabel;
	}

}
