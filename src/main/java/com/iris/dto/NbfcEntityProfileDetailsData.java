package com.iris.dto;

import java.io.Serializable;

/**
 * @author BHAVANA
 *
 */
public class NbfcEntityProfileDetailsData implements Serializable {

	private static final long serialVersionUID = 7280850399823582132L;
	private Long nbfcDetailsId;
	private String nbfcCinNumber;
	private Long dateInCorporatation;
	private String panNumber;
	private Long corIssueDate;
	private String crarAssetRatio;
	private String corNumber;

	public Long getNbfcDetailsId() {
		return nbfcDetailsId;
	}

	public void setNbfcDetailsId(Long nbfcDetailsId) {
		this.nbfcDetailsId = nbfcDetailsId;
	}

	public String getNbfcCinNumber() {
		return nbfcCinNumber;
	}

	public void setNbfcCinNumber(String nbfcCinNumber) {
		this.nbfcCinNumber = nbfcCinNumber;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public Long getDateInCorporatation() {
		return dateInCorporatation;
	}

	public void setDateInCorporatation(Long dateInCorporatation) {
		this.dateInCorporatation = dateInCorporatation;
	}

	public Long getCorIssueDate() {
		return corIssueDate;
	}

	public void setCorIssueDate(Long corIssueDate) {
		this.corIssueDate = corIssueDate;
	}

	public String getCrarAssetRatio() {
		return crarAssetRatio;
	}

	public void setCrarAssetRatio(String crarAssetRatio) {
		this.crarAssetRatio = crarAssetRatio;
	}

	public String getCorNumber() {
		return corNumber;
	}

	public void setCorNumber(String corNumber) {
		this.corNumber = corNumber;
	}

}
