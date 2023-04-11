package com.iris.dto;

import java.io.Serializable;

import com.iris.util.Validations;

/**
 * @author nsasane
 * @date 08/12/17
 */
public class ColumnBean implements Serializable {

	private static final long serialVersionUID = 6326718470991840884L;
	private int colId;
	private String colTag;
	private String colLabel;
	private String level;
	private String child;
	private Float colOrder;
	private String regex;
	private long maxLength;
	private long minLength;
	private long fieldTypeId;
	private long dataTypeId;
	private String regXName;

	/**
	 * @return the colId
	 */
	public int getColId() {
		return colId;
	}

	/**
	 * @param colId the colId to set
	 */
	public void setColId(int colId) {
		this.colId = colId;
	}

	/**
	 * @return the colTag
	 */
	public String getColTag() {
		return colTag;
	}

	/**
	 * @param colTag the colTag to set
	 */
	public void setColTag(String colTag) {
		this.colTag = Validations.trimInput(colTag);
	}

	/**
	 * @return the colLabel
	 */
	public String getColLabel() {
		return colLabel;
	}

	/**
	 * @param colLabel the colLabel to set
	 */
	public void setColLabel(String colLabel) {
		this.colLabel = Validations.trimInput(colLabel);
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = Validations.trimInput(level);
	}

	public String getChild() {
		return child;
	}

	public void setChild(String child) {
		this.child = Validations.trimInput(child);
	}

	public Float getColOrder() {
		return colOrder;
	}

	public void setColOrder(Float colOrder) {
		this.colOrder = colOrder;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = Validations.trimInput(regex);
	}

	public long getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(long maxLength) {
		this.maxLength = maxLength;
	}

	public long getMinLength() {
		return minLength;
	}

	public void setMinLength(long minLength) {
		this.minLength = minLength;
	}

	public long getFieldTypeId() {
		return fieldTypeId;
	}

	public void setFieldTypeId(long fieldTypeId) {
		this.fieldTypeId = fieldTypeId;
	}

	public long getDataTypeId() {
		return dataTypeId;
	}

	public void setDataTypeId(long dataTypeId) {
		this.dataTypeId = dataTypeId;
	}

	public String getRegXName() {
		return regXName;
	}

	public void setRegXName(String regXName) {
		this.regXName = Validations.trimInput(regXName);
	}

}