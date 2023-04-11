/**
 * 
 */
package com.iris.dto;

import java.io.Serializable;
import java.util.List;

import com.iris.sdmx.exceltohtml.bean.FormulaValidationBean;

/**
 * @author apagaria
 *
 */
public class DataTemplateUploadValidationDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7718112722708535294L;

	private String errorCode;

	private String errorMessage;

	private Integer cellNo;

	private String elementCode;

	private String elementVersion;

	private String dimensionCode;

	private String dimensionVersion;

	private String dimensionType;

	private String clValue;

	private List<FormulaValidationBean> formulaValidationBeanList;

	/**
	 * @return the formulaValidationBeanList
	 */
	public List<FormulaValidationBean> getFormulaValidationBeanList() {
		return formulaValidationBeanList;
	}

	/**
	 * @param formulaValidationBeanList the formulaValidationBeanList to set
	 */
	public void setFormulaValidationBeanList(List<FormulaValidationBean> formulaValidationBeanList) {
		this.formulaValidationBeanList = formulaValidationBeanList;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the cellNo
	 */
	public Integer getCellNo() {
		return cellNo;
	}

	/**
	 * @param cellNo the cellNo to set
	 */
	public void setCellNo(Integer cellNo) {
		this.cellNo = cellNo;
	}

	/**
	 * @return the elementCode
	 */
	public String getElementCode() {
		return elementCode;
	}

	/**
	 * @param elementCode the elementCode to set
	 */
	public void setElementCode(String elementCode) {
		this.elementCode = elementCode;
	}

	/**
	 * @return the elementVersion
	 */
	public String getElementVersion() {
		return elementVersion;
	}

	/**
	 * @param elementVersion the elementVersion to set
	 */
	public void setElementVersion(String elementVersion) {
		this.elementVersion = elementVersion;
	}

	/**
	 * @return the dimensionCode
	 */
	public String getDimensionCode() {
		return dimensionCode;
	}

	/**
	 * @param dimensionCode the dimensionCode to set
	 */
	public void setDimensionCode(String dimensionCode) {
		this.dimensionCode = dimensionCode;
	}

	/**
	 * @return the dimensionVersion
	 */
	public String getDimensionVersion() {
		return dimensionVersion;
	}

	/**
	 * @param dimensionVersion the dimensionVersion to set
	 */
	public void setDimensionVersion(String dimensionVersion) {
		this.dimensionVersion = dimensionVersion;
	}

	/**
	 * @return the dimensionType
	 */
	public String getDimensionType() {
		return dimensionType;
	}

	/**
	 * @param dimensionType the dimensionType to set
	 */
	public void setDimensionType(String dimensionType) {
		this.dimensionType = dimensionType;
	}

	/**
	 * @return the clValue
	 */
	public String getClValue() {
		return clValue;
	}

	/**
	 * @param clValue the clValue to set
	 */
	public void setClValue(String clValue) {
		this.clValue = clValue;
	}

}
