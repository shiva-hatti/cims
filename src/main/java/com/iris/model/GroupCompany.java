package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.iris.util.Validations;

@Entity
@Table(name = "TBL_GROUP_COMPANY")
public class GroupCompany implements Serializable {

	private static final long serialVersionUID = -8557152255792775910L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GROUP_COMPANY_ID")
	private Long companyId;

	@Column(name = "GROUP_COMPANY_NAME")
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
