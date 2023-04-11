package com.iris.sdmx.fusion.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author sajadhav
 */
@XmlRootElement(name = "RegistryInterface")
public class RegistryInterface implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8103035410843378125L;
	private Header header;
	private SubmitStructureResponse submitStructureResponse;

	/**
	 * @return the submitStructureResponse
	 */
	@XmlElement(name = "SubmitStructureResponse")
	public SubmitStructureResponse getSubmitStructureResponse() {
		return submitStructureResponse;
	}

	/**
	 * @param submitStructureResponse the submitStructureResponse to set
	 */
	public void setSubmitStructureResponse(SubmitStructureResponse submitStructureResponse) {
		this.submitStructureResponse = submitStructureResponse;
	}

	/**
	 * @return the header
	 */
	@XmlElement(name = "Header")
	public Header getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(Header header) {
		this.header = header;
	}

}