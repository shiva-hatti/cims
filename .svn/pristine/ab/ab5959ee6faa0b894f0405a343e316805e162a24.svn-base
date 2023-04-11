package com.iris.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ChartNode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2919701264253841950L;
	private String id;
	private String name;
	private String parent;
	private List<ChartNode> children = new ArrayList<>();
	private BigInteger value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ChartNode> getChildren() {
		return children;
	}

	public void setChildren(List<ChartNode> children) {
		this.children = children;
	}

	public BigInteger getValue() {
		return value;
	}

	public void setValue(BigInteger value) {
		this.value = value;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
