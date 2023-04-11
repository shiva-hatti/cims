/**
 * 
 */
package com.iris.sdmx.fusion.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author sajadhav
 *
 */
public class Attribute implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8941752045197626884L;

	private String id;

	private String urn;

	private String concept;

	private Boolean mandatory;

	private String attachmentLevel;

	private Representation representation;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the urn
	 */
	public String getUrn() {
		return urn;
	}

	/**
	 * @param urn the urn to set
	 */
	public void setUrn(String urn) {
		this.urn = urn;
	}

	/**
	 * @return the concept
	 */
	public String getConcept() {
		return concept;
	}

	/**
	 * @param concept the concept to set
	 */
	public void setConcept(String concept) {
		this.concept = concept;
	}

	/**
	 * @return the mandatory
	 */
	public Boolean getMandatory() {
		return mandatory;
	}

	/**
	 * @param mandatory the mandatory to set
	 */
	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	/**
	 * @return the attachmentLevel
	 */
	public String getAttachmentLevel() {
		return attachmentLevel;
	}

	/**
	 * @param attachmentLevel the attachmentLevel to set
	 */
	public void setAttachmentLevel(String attachmentLevel) {
		this.attachmentLevel = attachmentLevel;
	}

	/**
	 * @return the representation
	 */
	public Representation getRepresentation() {
		return representation;
	}

	/**
	 * @param representation the representation to set
	 */
	public void setRepresentation(Representation representation) {
		this.representation = representation;
	}

}
