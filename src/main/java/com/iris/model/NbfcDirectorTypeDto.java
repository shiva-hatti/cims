package com.iris.model;

import java.io.Serializable;

public class NbfcDirectorTypeDto implements Serializable {

	private static final long serialVersionUID = 6235078389402679317L;
	private String directorName;
	private String directorNo;
	private String directorType;
	private String directorPan;
	private String remark;
	private Long rowNum;

	public String getDirectorName() {
		return directorName;
	}

	public void setDirectorName(String directorName) {
		this.directorName = directorName;
	}

	public String getDirectorNo() {
		return directorNo;
	}

	public void setDirectorNo(String directorNo) {
		this.directorNo = directorNo;
	}

	public String getDirectorType() {
		return directorType;
	}

	public void setDirectorType(String directorType) {
		this.directorType = directorType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDirectorPan() {
		return directorPan;
	}

	public void setDirectorPan(String directorPan) {
		this.directorPan = directorPan;
	}

	public Long getRowNum() {
		return rowNum;
	}

	public void setRowNum(Long rowNum) {
		this.rowNum = rowNum;
	}

}
