package com.iris.sdmx.fusion.bean;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class MaintainableObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7027899773955950079L;

	private List<String> URN;

	/**
	 * @return the uRN
	 */
	@XmlElement(required = true, namespace = "")
	public List<String> getURN() {
		return URN;
	}

	/**
	 * @param uRN the uRN to set
	 */
	public void setURN(List<String> uRN) {
		URN = uRN;
	}

}
