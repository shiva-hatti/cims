package com.iris.dto;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class DropDownObject implements Serializable, Comparable<DropDownObject> {

	private static final long serialVersionUID = 7047297338309122468L;

	private Long key;
	private String display;
	private String selected = StringUtils.EMPTY;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	@Override
	public int compareTo(DropDownObject o) {
		return this.key.compareTo(o.getKey());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DropDownObject other = (DropDownObject) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

}