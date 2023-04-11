package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This bean represents the formula category type for NON-XBRL returns
 * 
 * @author apagaria
 *
 */
@Entity
@Table(name = "TBL_FORMULA_CATEGORY_TYPE")
public class FormulaCategoryType implements Serializable {

	private static final long serialVersionUID = -6305709526119023051L;

	/**
	 * 
	 */
	@Id
	@Column(name = "FORMULA_CATEGORY_TYPE_ID")
	private Long formulaCategoryTypeId;

	/**
	 * 
	 */
	@Column(name = "FORMULA_CATEGORY_CODE")
	private String formulaCategoryCode;

	/**
	 * 
	 */
	@Column(name = "FORMULA_CATEGORY_NAME")
	private String formulaCategoryName;

	/**
	 * 
	 */
	@Column(name = "FORMULA_CATEGORY_DESCRIPTION")
	private String formulaCategoryDescription;

	/**
	 * 
	 */
	@Column(name = "IS_ROW_SPECIFIC")
	private Boolean isRowSpecific;

	/**
	 * 
	 */
	@Column(name = "IS_COLUMN_SPECIFIC")
	private Boolean isColumnSpecific;

	/**
	 * 
	 */
	@Column(name = "NO_OF_TABLE_INCLUDE")
	private int noOfTablesInclude;

	/**
	 * @return the formulaCategoryTypeId
	 */
	public Long getFormulaCategoryTypeId() {
		return formulaCategoryTypeId;
	}

	/**
	 * @param formulaCategoryTypeId the formulaCategoryTypeId to set
	 */
	public void setFormulaCategoryTypeId(Long formulaCategoryTypeId) {
		this.formulaCategoryTypeId = formulaCategoryTypeId;
	}

	/**
	 * @return the formulaCategoryCode
	 */
	public String getFormulaCategoryCode() {
		return formulaCategoryCode;
	}

	/**
	 * @param formulaCategoryCode the formulaCategoryCode to set
	 */
	public void setFormulaCategoryCode(String formulaCategoryCode) {
		this.formulaCategoryCode = formulaCategoryCode;
	}

	/**
	 * @return the formulaCategoryName
	 */
	public String getFormulaCategoryName() {
		return formulaCategoryName;
	}

	/**
	 * @param formulaCategoryName the formulaCategoryName to set
	 */
	public void setFormulaCategoryName(String formulaCategoryName) {
		this.formulaCategoryName = formulaCategoryName;
	}

	/**
	 * @return the formulaCategoryDescription
	 */
	public String getFormulaCategoryDescription() {
		return formulaCategoryDescription;
	}

	/**
	 * @param formulaCategoryDescription the formulaCategoryDescription to set
	 */
	public void setFormulaCategoryDescription(String formulaCategoryDescription) {
		this.formulaCategoryDescription = formulaCategoryDescription;
	}

	/**
	 * @return the isRowSpecific
	 */
	public Boolean getIsRowSpecific() {
		return isRowSpecific;
	}

	/**
	 * @param isRowSpecific the isRowSpecific to set
	 */
	public void setIsRowSpecific(Boolean isRowSpecific) {
		this.isRowSpecific = isRowSpecific;
	}

	/**
	 * @return the isColumnSpecific
	 */
	public Boolean getIsColumnSpecific() {
		return isColumnSpecific;
	}

	/**
	 * @param isColumnSpecific the isColumnSpecific to set
	 */
	public void setIsColumnSpecific(Boolean isColumnSpecific) {
		this.isColumnSpecific = isColumnSpecific;
	}

	/**
	 * @return the noOfTablesInclude
	 */
	public int getNoOfTablesInclude() {
		return noOfTablesInclude;
	}

	/**
	 * @param noOfTablesInclude the noOfTablesInclude to set
	 */
	public void setNoOfTablesInclude(int noOfTablesInclude) {
		this.noOfTablesInclude = noOfTablesInclude;
	}

	@Override
	public String toString() {
		return "FormulaCategoryType [formulaCategoryTypeId=" + formulaCategoryTypeId + ", formulaCategoryCode=" + formulaCategoryCode + ", formulaCategoryName=" + formulaCategoryName + ", formulaCategoryDescription=" + formulaCategoryDescription + ", isRowSpecific=" + isRowSpecific + ", isColumnSpecific=" + isColumnSpecific + ", noOfTablesInclude=" + noOfTablesInclude + "]";
	}
}