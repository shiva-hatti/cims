package com.iris.dto;

import java.io.Serializable;
import java.util.List;

public class DataBlock implements Serializable {

	private static final long serialVersionUID = 5019821983795211969L;
	private List<DataBean> data;

	/**
	 * @return the data
	 */
	public List<DataBean> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<DataBean> data) {
		this.data = data;
	}

}