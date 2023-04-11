/**
 * 
 */
package com.iris.sdmx.fusion.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author sajadhav
 *
 */
public class SubmissionResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2134439007727055977L;

	private SubmittedStructure submittedStructure;

	private StatusMessage statusMessage;

	/**
	 * @return the statusMessage
	 */
	@XmlElement(name = "StatusMessage", namespace = "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/registry")
	public StatusMessage getStatusMessage() {
		return statusMessage;
	}

	/**
	 * @param statusMessage the statusMessage to set
	 */
	public void setStatusMessage(StatusMessage statusMessage) {
		this.statusMessage = statusMessage;
	}

	/**
	 * @return the submittedStructure
	 */
	@XmlElement(name = "SubmittedStructure", namespace = "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/registry")
	public SubmittedStructure getSubmittedStructure() {
		return submittedStructure;
	}

	/**
	 * @param submittedStructure the submittedStructure to set
	 */
	public void setSubmittedStructure(SubmittedStructure submittedStructure) {
		this.submittedStructure = submittedStructure;
	}

}
