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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.iris.model.UserMaster;

/**
 * @author apagaria
 *
 */
@Entity
@Table(name = "TBL_SDMX_RETURN_MODEL_INFO")
@JsonInclude(Include.NON_NULL)
public class SdmxReturnModelInfoEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RETURN_MODEL_INFO_ID")
	private Long returnModelInfoId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_SHEET_INFO_ID_FK")
	private SdmxReturnSheetInfoEntity returnSheetInfoIdFk;

	@Column(name = "RETURN_CELL_REF")
	private Integer returnCellRef;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODEL_CODES_ID_FK")
	private SdmxModelCodesEntity modelCodesIdFk;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	private UserMaster createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "IS_MANDATORY")
	private Boolean isMandatory;

	@Column(name = "CELL_FORMULA")
	private String cellFormula;

	public SdmxReturnModelInfoEntity(Integer returnCellRef, String cellFormula) {

		this.returnCellRef = returnCellRef;
		this.cellFormula = cellFormula;
	}

	public SdmxReturnModelInfoEntity() {
	}

	/**
	 * @return the returnModelInfoId
	 */
	public Long getReturnModelInfoId() {
		return returnModelInfoId;
	}

	/**
	 * @param returnModelInfoId the returnModelInfoId to set
	 */
	public void setReturnModelInfoId(Long returnModelInfoId) {
		this.returnModelInfoId = returnModelInfoId;
	}

	/**
	 * @return the returnSheetInfoIdFk
	 */
	public SdmxReturnSheetInfoEntity getReturnSheetInfoIdFk() {
		return returnSheetInfoIdFk;
	}

	/**
	 * @param returnSheetInfoIdFk the returnSheetInfoIdFk to set
	 */
	public void setReturnSheetInfoIdFk(SdmxReturnSheetInfoEntity returnSheetInfoIdFk) {
		this.returnSheetInfoIdFk = returnSheetInfoIdFk;
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

	/**
	 * @return the modelCodesIdFk
	 */
	public SdmxModelCodesEntity getModelCodesIdFk() {
		return modelCodesIdFk;
	}

	/**
	 * @param modelCodesIdFk the modelCodesIdFk to set
	 */
	public void setModelCodesIdFk(SdmxModelCodesEntity modelCodesIdFk) {
		this.modelCodesIdFk = modelCodesIdFk;
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
	 * @return the isMandatory
	 */
	public Boolean getIsMandatory() {
		return isMandatory;
	}

	/**
	 * @param isMandatory the isMandatory to set
	 */
	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	/**
	 * @return the cellFormula
	 */
	public String getCellFormula() {
		return cellFormula;
	}

	/**
	 * @param cellFormula the cellFormula to set
	 */
	public void setCellFormula(String cellFormula) {
		this.cellFormula = cellFormula;
	}

}
