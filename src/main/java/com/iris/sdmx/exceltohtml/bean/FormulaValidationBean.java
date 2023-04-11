package com.iris.sdmx.exceltohtml.bean;

import java.io.Serializable;

public class FormulaValidationBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1429265348527035745L;

	private String dmId;

	private String errorMsgCode;

	private String cellRef;

	private Integer sheetRow;

	private String errorMessage;

	/**
	 * @return the dmId
	 */
	public String getDmId() {
		return dmId;
	}

	/**
	 * @param dmId the dmId to set
	 */
	public void setDmId(String dmId) {
		this.dmId = dmId;
	}

	/**
	 * @return the errorMsgCode
	 */
	public String getErrorMsgCode() {
		return errorMsgCode;
	}

	/**
	 * @param errorMsgCode the errorMsgCode to set
	 */
	public void setErrorMsgCode(String errorMsgCode) {
		this.errorMsgCode = errorMsgCode;
	}

	/**
	 * @return the cellRef
	 */
	public String getCellRef() {
		return cellRef;
	}

	/**
	 * @param cellRef the cellRef to set
	 */
	public void setCellRef(String cellRef) {
		this.cellRef = cellRef;
	}

	/**
	 * @return the sheetRow
	 */
	public Integer getSheetRow() {
		return sheetRow;
	}

	/**
	 * @param sheetRow the sheetRow to set
	 */
	public void setSheetRow(Integer sheetRow) {
		this.sheetRow = sheetRow;
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

}
