package com.iris.dto;

import java.io.Serializable;
import java.util.Map;

import com.iris.util.Validations;

public class DynamicFormBean implements Serializable {

	private static final long serialVersionUID = -8791331004155142659L;
	private DynamicTemplateBean jsonFormData;
	private String structureData;
	private Map<String, String> structureLabel;
	private String disclaimerContent;
	private String isActive;
	private String isCheckShow;

	/**
	 * @return the jsonFormData
	 */
	public DynamicTemplateBean getJsonFormData() {
		return jsonFormData;
	}

	/**
	 * @param jsonFormData the jsonFormData to set
	 */
	public void setJsonFormData(DynamicTemplateBean jsonFormData) {
		this.jsonFormData = jsonFormData;
	}

	/**
	 * @return the structureLabel
	 */
	public Map<String, String> getStructureLabel() {
		return structureLabel;
	}

	/**
	 * @param structureLabel the structureLabel to set
	 */
	public void setStructureLabel(Map<String, String> structureLabel) {
		this.structureLabel = structureLabel;
	}

	/**
	 * @return the structureData
	 */
	public String getStructureData() {
		return structureData;
	}

	/**
	 * @param structureData the structureData to set
	 */
	public void setStructureData(String structureData) {
		this.structureData = Validations.trimInput(structureData);
	}

	public String getDisclaimerContent() {
		return disclaimerContent;
	}

	public void setDisclaimerContent(String disclaimerContent) {
		this.disclaimerContent = Validations.trimInput(disclaimerContent);
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = Validations.trimInput(isActive);
	}

	public String getIsCheckShow() {
		return isCheckShow;
	}

	public void setIsCheckShow(String isCheckShow) {
		this.isCheckShow = Validations.trimInput(isCheckShow);
	}

}