/**
 * 
 */
package com.iris.sdmx.exceltohtml.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author apagaria
 *
 */
@Entity
@Table(name = "TBL_SDMX_RETURN_FORMULA")
@JsonInclude(Include.NON_NULL)
public class SdmxReturnFormulaMappingEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RETURN_FORMULA_ID")
	private Long returnFormulaId;

	@Column(name = "RETURN_CODE")
	private String returnCode;

	/**
	 * 
	 */
	@Column(name = "RBR_VERSION")
	private String rbrVersion;

	/**
	 * 
	 */
	@Column(name = "EBR_VERSION")
	private String ebrVersion;

	/**
	 * 
	 */
	@Column(name = "FORMULA_JSON")
	private String formulaJson;

	/**
	 * 
	 */
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	/**
	 * 
	 */
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	/**
	 * 
	 */
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	/**
	 * @return the returnFormulaId
	 */
	public Long getReturnFormulaId() {
		return returnFormulaId;
	}

	/**
	 * @param returnFormulaId the returnFormulaId to set
	 */
	public void setReturnFormulaId(Long returnFormulaId) {
		this.returnFormulaId = returnFormulaId;
	}

	/**
	 * @return the returnCode
	 */
	public String getReturnCode() {
		return returnCode;
	}

	/**
	 * @param returnCode the returnCode to set
	 */
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	/**
	 * @return the rbrVersion
	 */
	public String getRbrVersion() {
		return rbrVersion;
	}

	/**
	 * @param rbrVersion the rbrVersion to set
	 */
	public void setRbrVersion(String rbrVersion) {
		this.rbrVersion = rbrVersion;
	}

	/**
	 * @return the ebrVersion
	 */
	public String getEbrVersion() {
		return ebrVersion;
	}

	/**
	 * @param ebrVersion the ebrVersion to set
	 */
	public void setEbrVersion(String ebrVersion) {
		this.ebrVersion = ebrVersion;
	}

	/**
	 * @return the formulaJson
	 */
	public String getFormulaJson() {
		return formulaJson;
	}

	/**
	 * @param formulaJson the formulaJson to set
	 */
	public void setFormulaJson(String formulaJson) {
		this.formulaJson = formulaJson;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the modifiedDate
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "SdmxReturnFormulaMappingEntity [returnFormulaId=" + returnFormulaId + ", returnCode=" + returnCode + ", rbrVersion=" + rbrVersion + ", ebrVersion=" + ebrVersion + ", formulaJson=" + formulaJson + ", createdDate=" + createdDate + ", modifiedDate=" + modifiedDate + ", isActive=" + isActive + "]";
	}
}
