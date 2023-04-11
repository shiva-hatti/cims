/**
 * 
 */
package com.iris.sdmx.exceltohtml.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.iris.model.UserMaster;
import com.iris.sdmx.dimesnsion.entity.Regex;
import com.iris.sdmx.element.entity.SdmxElementEntity;
import com.iris.util.Validations;

/**
 * @author apagaria
 *
 */
@Entity
@Table(name = "TBL_SDMX_MODEL_CODES")
@JsonInclude(Include.NON_NULL)
public class SdmxModelCodesEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MODEL_CODES_ID")
	private Long modelCodesId;

	@Column(name = "MODEL_DIM")
	private String modelDim;

	@Column(name = "MODEL_DIM_HASH")
	private String modelDimHash;

	@Column(name = "MODEL_CODE")
	private String modelCode;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ELEMENT_ID_FK")
	private SdmxElementEntity elementIdFk;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	private UserMaster createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REGEX_ID_FK")
	private Regex regexIdFk;

	@Transient
	private Integer returnCellRef;

	/**
	 * 
	 */
	public SdmxModelCodesEntity() {
	}

	public SdmxModelCodesEntity(SdmxElementEntity elementIdFk) {
		this.elementIdFk = elementIdFk;
	}

	public SdmxModelCodesEntity(String modelDimHash, String modelCode, String dsdCode, String elementVersion) {
		this.modelCode = modelCode;
		this.modelDimHash = modelDimHash;

		elementIdFk = new SdmxElementEntity();
		elementIdFk.setDsdCode(dsdCode);
		elementIdFk.setElementVer(elementVersion);
	}

	public SdmxModelCodesEntity(String modelDim, String modelCode, Integer returnCellRef) {

		this.modelDim = modelDim;
		this.modelCode = modelCode;
		this.returnCellRef = returnCellRef;
	}

	/**
	 * @param modelCodesId
	 */
	public SdmxModelCodesEntity(Long modelCodesId) {
		this.modelCodesId = modelCodesId;
	}

	/**
	 * @return the modelCodesId
	 */
	public Long getModelCodesId() {
		return modelCodesId;
	}

	/**
	 * @param modelCodesId the modelCodesId to set
	 */
	public void setModelCodesId(Long modelCodesId) {
		this.modelCodesId = modelCodesId;
	}

	/**
	 * @return the modelDim
	 */
	public String getModelDim() {
		return modelDim;
	}

	/**
	 * @param modelDim the modelDim to set
	 */
	public void setModelDim(String modelDim) {
		this.modelDim = Validations.trimInput(modelDim);
	}

	/**
	 * @return the modelDimHash
	 */
	public String getModelDimHash() {
		return modelDimHash;
	}

	/**
	 * @param modelDimHash the modelDimHash to set
	 */
	public void setModelDimHash(String modelDimHash) {
		this.modelDimHash = modelDimHash;
	}

	/**
	 * @return the modelCode
	 */
	public String getModelCode() {
		return modelCode;
	}

	/**
	 * @param modelCode the modelCode to set
	 */
	public void setModelCode(String modelCode) {
		this.modelCode = Validations.trimInput(modelCode);
	}

	/**
	 * @return the elementIdFk
	 */
	public SdmxElementEntity getElementIdFk() {
		return elementIdFk;
	}

	/**
	 * @param elementIdFk the elementIdFk to set
	 */
	public void setElementIdFk(SdmxElementEntity elementIdFk) {
		this.elementIdFk = elementIdFk;
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
	 * @return the createdBy
	 */
	public UserMaster getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(UserMaster createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the regexIdFk
	 */
	public Regex getRegexIdFk() {
		return regexIdFk;
	}

	/**
	 * @param regexIdFk the regexIdFk to set
	 */
	public void setRegexIdFk(Regex regexIdFk) {
		this.regexIdFk = regexIdFk;
	}

	/**
	 * @return the returnCellRef
	 */
	public Integer getReturnCellRef() {
		return returnCellRef;
	}

	/**
	 * @param returnCellRef the returnCellRef to set
	 */
	public void setReturnCellRef(Integer returnCellRef) {
		this.returnCellRef = returnCellRef;
	}

}
