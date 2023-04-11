package com.iris.dto;

import java.io.Serializable;
import java.util.List;

import com.iris.util.Validations;

/**
 * @author nsasane
 * @date 08/12/17
 */
public class ElementBean implements Serializable {

	private static final long serialVersionUID = 4936431905838959531L;

	private int eleId;
	private String eleLabel;
	private String eleTag;
	private String regex;
	private List<DropdownBean> dropDownList;
	private long maxLength;
	private long minLength;
	private String prePopulatedText;
	private Boolean isMandatory;
	private long fieldTypeId;
	private long dataTypeId;
	private String regXName;
	private Boolean isParent;
	private String parentId;
	private FileObjectBean fileObj;
	private Float eleOrder;
	private long rowPositionOnCsv;
	private long colPositionOnCsv;

	public long getColPositionOnCsv() {
		return colPositionOnCsv;
	}

	public void setColPositionOnCsv(long colPositionOnCsv) {
		this.colPositionOnCsv = colPositionOnCsv;
	}

	/**
	 * @return the rowPositionOnCsv
	 */
	public long getRowPositionOnCsv() {
		return rowPositionOnCsv;
	}

	/**
	 * @param rowPositionOnCsv the rowPositionOnCsv to set
	 */
	public void setRowPositionOnCsv(long rowPositionOnCsv) {
		this.rowPositionOnCsv = rowPositionOnCsv;
	}

	/**
	 * @return the eleId
	 */
	public int getEleId() {
		return eleId;
	}

	/**
	 * @param eleId the eleId to set
	 */
	public void setEleId(int eleId) {
		this.eleId = eleId;
	}

	/**
	 * @return the eleLabel
	 */
	public String getEleLabel() {
		return eleLabel;
	}

	/**
	 * @param eleLabel the eleLabel to set
	 */
	public void setEleLabel(String eleLabel) {
		this.eleLabel = Validations.trimInput(eleLabel);
	}

	/**
	 * @return the eleTag
	 */
	public String getEleTag() {
		return eleTag;
	}

	/**
	 * @param eleTag the eleTag to set
	 */
	public void setEleTag(String eleTag) {
		this.eleTag = Validations.trimInput(eleTag);
	}

	/**
	 * @return the regex
	 */
	public String getRegex() {
		return regex;
	}

	/**
	 * @param regex the regex to set
	 */
	public void setRegex(String regex) {
		this.regex = Validations.trimInput(regex);
	}

	/**
	 * @return the dropDownList
	 */
	public List<DropdownBean> getDropDownList() {
		return dropDownList;
	}

	/**
	 * @param dropDownList the dropDownList to set
	 */
	public void setDropDownList(List<DropdownBean> dropDownList) {
		this.dropDownList = dropDownList;
	}

	/**
	 * @return the prePopulatedText
	 */
	public String getPrePopulatedText() {
		return prePopulatedText;
	}

	/**
	 * @param prePopulatedText the prePopulatedText to set
	 */
	public void setPrePopulatedText(String prePopulatedText) {
		this.prePopulatedText = Validations.trimInput(prePopulatedText);
	}

	/**
	 * @return the fieldTypeId
	 */
	public long getFieldTypeId() {
		return fieldTypeId;
	}

	/**
	 * @param fieldTypeId the fieldTypeId to set
	 */
	public void setFieldTypeId(long fieldTypeId) {
		this.fieldTypeId = fieldTypeId;
	}

	/**
	 * @return the dataTypeId
	 */
	public long getDataTypeId() {
		return dataTypeId;
	}

	/**
	 * @param dataTypeId the dataTypeId to set
	 */
	public void setDataTypeId(long dataTypeId) {
		this.dataTypeId = dataTypeId;
	}

	/**
	 * @return the isMandatory
	 */
	public Boolean getIsMandatory() {
		return isMandatory;
	}

	/**
	 * @param isMandatory the isMandatory to set
	 */
	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	/**
	 * @return the maxLength
	 */
	public long getMaxLength() {
		return maxLength;
	}

	/**
	 * @param maxLength the maxLength to set
	 */
	public void setMaxLength(long maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * @return the minLength
	 */
	public long getMinLength() {
		return minLength;
	}

	/**
	 * @param minLength the minLength to set
	 */
	public void setMinLength(long minLength) {
		this.minLength = minLength;
	}

	/**
	 * @return the regXName
	 */
	public String getRegXName() {
		return regXName;
	}

	/**
	 * @param regXName the regXName to set
	 */
	public void setRegXName(String regXName) {
		this.regXName = Validations.trimInput(regXName);
	}

	public Boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = Validations.trimInput(parentId);
	}

	public FileObjectBean getFileObj() {
		return fileObj;
	}

	public void setFileObj(FileObjectBean fileObj) {
		this.fileObj = fileObj;
	}

	public Float getEleOrder() {
		return eleOrder;
	}

	public void setEleOrder(Float eleOrder) {
		this.eleOrder = eleOrder;
	}

}