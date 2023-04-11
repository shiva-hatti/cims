/**
 * 
 */
package com.iris.dto;

/**
 * @author Siddique
 *
 */
public class PanMasterDto {

	private Long id;
	private Long panIdFk;
	private String panNumber;
	private String borrowerName;
	private String borrowerAlternateName;
	private String borrowerTitle;
	private Long borrowerMobile;
	private String rbiGenerated;
	private String institutionType;
	private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPanIdFk() {
		return panIdFk;
	}

	public void setPanIdFk(Long panIdFk) {
		this.panIdFk = panIdFk;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getBorrowerName() {
		return borrowerName;
	}

	public void setBorrowerName(String borrowerName) {
		this.borrowerName = borrowerName;
	}

	public String getBorrowerAlternateName() {
		return borrowerAlternateName;
	}

	public void setBorrowerAlternateName(String borrowerAlternateName) {
		this.borrowerAlternateName = borrowerAlternateName;
	}

	public String getBorrowerTitle() {
		return borrowerTitle;
	}

	public void setBorrowerTitle(String borrowerTitle) {
		this.borrowerTitle = borrowerTitle;
	}

	public Long getBorrowerMobile() {
		return borrowerMobile;
	}

	public void setBorrowerMobile(Long borrowerMobile) {
		this.borrowerMobile = borrowerMobile;
	}

	public String getRbiGenerated() {
		return rbiGenerated;
	}

	public void setRbiGenerated(String rbiGenerated) {
		this.rbiGenerated = rbiGenerated;
	}

	public String getInstitutionType() {
		return institutionType;
	}

	public void setInstitutionType(String institutionType) {
		this.institutionType = institutionType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
