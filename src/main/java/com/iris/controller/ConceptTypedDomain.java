package com.iris.controller;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.json.simple.JSONObject;

import com.iris.dynamicDropDown.model.DropDownType;
import com.iris.model.Return;

/**
 * @author BHAVANA
 *
 */
@Entity
@Table(name = "TBL_CONCEPT_TYPED_DOMAIN")
public class ConceptTypedDomain implements Serializable {

	private static final long serialVersionUID = 4256770020412988654L;

	@Id
	@Column(name = "CONCEPT_TYPED_DOMAIN_ID")
	private Long conceptTypeDomainId;

	@Column(name = "TYPED_DOMAIN")
	private String typedDomain;

	@Column(name = "CONCEPT")
	private String concept;

	@Column(name = "EXPLICIT_MEMBER")
	private String explicitMember;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DROPDOWN_TYPE_ID_FK")
	private DropDownType dropDownTypeIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_ID_FK")
	private Return returnIdFk;

	@Column(name = "API_URL_DETAILS")
	private String apiUrlDetails;

	@Transient
	private Long returnId;

	@Transient
	private Long rowNum;

	@Transient
	private JSONObject jsonApiDetails;

	@Column(name = "ELR_TAG")
	private String elrTag;

	public Long getConceptTypeDomainId() {
		return conceptTypeDomainId;
	}

	public void setConceptTypeDomainId(Long conceptTypeDomainId) {
		this.conceptTypeDomainId = conceptTypeDomainId;
	}

	public String getTypedDomain() {
		return typedDomain;
	}

	public void setTypedDomain(String typedDomain) {
		this.typedDomain = typedDomain;
	}

	public DropDownType getDropDownTypeIdFk() {
		return dropDownTypeIdFk;
	}

	public void setDropDownTypeIdFk(DropDownType dropDownTypeIdFk) {
		this.dropDownTypeIdFk = dropDownTypeIdFk;
	}

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Return getReturnIdFk() {
		return returnIdFk;
	}

	public void setReturnIdFk(Return returnIdFk) {
		this.returnIdFk = returnIdFk;
	}

	public String getApiUrlDetails() {
		return apiUrlDetails;
	}

	public void setApiUrlDetails(String apiUrlDetails) {
		this.apiUrlDetails = apiUrlDetails;
	}

	public String getExplicitMember() {
		return explicitMember;
	}

	public void setExplicitMember(String explicitMember) {
		this.explicitMember = explicitMember;
	}

	/**
	 * @return the returnId
	 */
	public Long getReturnId() {
		return returnId;
	}

	/**
	 * @param returnId the returnId to set
	 */
	public void setReturnId(Long returnId) {
		this.returnId = returnId;
	}

	/**
	 * @return the rowNum
	 */
	public Long getRowNum() {
		return rowNum;
	}

	/**
	 * @param rowNum the rowNum to set
	 */
	public void setRowNum(Long rowNum) {
		this.rowNum = rowNum;
	}

	public JSONObject getJsonApiDetails() {
		return jsonApiDetails;
	}

	public void setJsonApiDetails(JSONObject jsonApiDetails) {
		this.jsonApiDetails = jsonApiDetails;
	}

	public String getElrTag() {
		return elrTag;
	}

	public void setElrTag(String elrTag) {
		this.elrTag = elrTag;
	}

}
