package com.iris.sdmx.fusion.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SubmitStructureResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5553325613888828246L;

	private SubmissionResult submissionResult;

	/**
	 * @return the submissionResult
	 */
	@XmlElement(name = "SubmissionResult", namespace = "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/registry")
	public SubmissionResult getSubmissionResult() {
		return submissionResult;
	}

	/**
	 * @param submissionResult the submissionResult to set
	 */
	public void setSubmissionResult(SubmissionResult submissionResult) {
		this.submissionResult = submissionResult;
	}

}
