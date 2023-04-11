/**
 * 
 */
package com.iris.sdmx.fusion.bean;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author sajadhav
 *
 */
@JsonInclude(Include.NON_DEFAULT)
public class CodeListFusion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4085377771591585675L;

	private String id;

	private String urn;

	private List<FusionDecription> names;

	private List<FusionDecription> descriptions;

	private String agencyId;

	private String version;

	private boolean isFinal;

	private boolean isPartial;

	private String validityType;

	private String parentCode;

	private List<CodeListFusion> items;

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
	 * @return the names
	 */
	public List<FusionDecription> getNames() {
		return names;
	}

	/**
	 * @param names the names to set
	 */
	public void setNames(List<FusionDecription> names) {
		this.names = names;
	}

	/**
	 * @return the descriptions
	 */
	public List<FusionDecription> getDescriptions() {
		return descriptions;
	}

	/**
	 * @param descriptions the descriptions to set
	 */
	public void setDescriptions(List<FusionDecription> descriptions) {
		this.descriptions = descriptions;
	}

	/**
	 * @return the agencyId
	 */
	public String getAgencyId() {
		return agencyId;
	}

	/**
	 * @param agencyId the agencyId to set
	 */
	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the isFinal
	 */
	public boolean isFinal() {
		return isFinal;
	}

	/**
	 * @param isFinal the isFinal to set
	 */
	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	/**
	 * @return the isPartial
	 */
	public boolean isPartial() {
		return isPartial;
	}

	/**
	 * @param isPartial the isPartial to set
	 */
	public void setPartial(boolean isPartial) {
		this.isPartial = isPartial;
	}

	/**
	 * @return the validityType
	 */
	public String getValidityType() {
		return validityType;
	}

	/**
	 * @param validityType the validityType to set
	 */
	public void setValidityType(String validityType) {
		this.validityType = validityType;
	}

	/**
	 * @return the parentCode
	 */
	public String getParentCode() {
		return parentCode;
	}

	/**
	 * @param parentCode the parentCode to set
	 */
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	/**
	 * @return the items
	 */
	public List<CodeListFusion> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<CodeListFusion> items) {
		this.items = items;
	}

}
