package com.iris.sdmx.fusion.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class SubmittedStructure implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9134051879183154451L;

	private MaintainableObject maintainableObject;

	private String action;

	/**
	 * @return the action
	 */
	@XmlAttribute
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the maintainableObject
	 */
	@XmlElement(name = "MaintainableObject", namespace = "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/registry")
	public MaintainableObject getMaintainableObject() {
		return maintainableObject;
	}

	/**
	 * @param maintainableObject the maintainableObject to set
	 */
	public void setMaintainableObject(MaintainableObject maintainableObject) {
		this.maintainableObject = maintainableObject;
	}

}
