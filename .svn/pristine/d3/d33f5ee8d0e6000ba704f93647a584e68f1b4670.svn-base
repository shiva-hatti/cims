package com.iris.dto;

import java.io.Serializable;

import com.iris.util.Validations;

public class CompanyDto implements Serializable {

	private static final long serialVersionUID = -5333835560017356728L;

	private Long companyId;

	private String companyName;

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = Validations.trimInput(companyName);
	}

}
