/**
 * 
 */
package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Siddique
 *
 */
@Entity
@Table(name = "TBL_ISIN_MASTER")
public class IsinMaster implements Serializable {

	private static final long serialVersionUID = -2798414849501379702L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "ISIN")
	private String isin;

	@Column(name = "NOMENCLATURE")
	private String nomenclature;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "ISSUED_BY")
	private String issuedBy;

	@Column(name = "ISSUED_DATE")
	private String issueDate;

	@Column(name = "MATURITY_DATE")
	private String maturityDate;

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public String getNomenclature() {
		return nomenclature;
	}

	public void setNomenclature(String nomenclature) {
		this.nomenclature = nomenclature;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public String getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(String maturityDate) {
		this.maturityDate = maturityDate;
	}
}
