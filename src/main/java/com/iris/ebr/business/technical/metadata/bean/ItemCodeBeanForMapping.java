package com.iris.ebr.business.technical.metadata.bean;

import java.io.Serializable;

public class ItemCodeBeanForMapping implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8283940712803328950L;

	private String tableColumn;
	private String temSheetNo;
	private String itemCode;
	private String colName;
	private int cellRef;

	public int getCellRef() {
		return cellRef;
	}

	public void setCellRef(int cellRef) {
		this.cellRef = cellRef;
	}

	public String getTableColumn() {
		return tableColumn;
	}

	public void setTableColumn(String tableColumn) {
		this.tableColumn = tableColumn;
	}

	public String getTemSheetNo() {
		return temSheetNo;
	}

	public void setTemSheetNo(String temSheetNo) {
		this.temSheetNo = temSheetNo;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

}
