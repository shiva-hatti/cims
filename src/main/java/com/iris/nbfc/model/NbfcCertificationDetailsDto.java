package com.iris.nbfc.model;

import java.io.Serializable;

public class NbfcCertificationDetailsDto implements Serializable {
	private static final long serialVersionUID = 5717195175755935534L;

	private Long nbfcCetificationDetalId;
	private Long corRegIdFk;
	private Long nbfcUserIdFk;
	private String formPageJsonValue;
	private Long pageNo;
	private Long subPageNo;
	private Long startPageNo;
	private Long endPageNo;
	private int nbfcCompanyTypeOtherId;
	private int nbfcCompanyTypeId;
	private Long regStatusId;

	/**
	 * @return the nbfcCetificationDetalId
	 */
	public Long getNbfcCetificationDetalId() {
		return nbfcCetificationDetalId;
	}

	/**
	 * @param nbfcCetificationDetalId the nbfcCetificationDetalId to set
	 */
	public void setNbfcCetificationDetalId(Long nbfcCetificationDetalId) {
		this.nbfcCetificationDetalId = nbfcCetificationDetalId;
	}

	public Long getCorRegIdFk() {
		return corRegIdFk;
	}

	public void setCorRegIdFk(Long corRegIdFk) {
		this.corRegIdFk = corRegIdFk;
	}

	/**
	 * @return the nbfcUserIdFk
	 */
	public Long getNbfcUserIdFk() {
		return nbfcUserIdFk;
	}

	/**
	 * @param nbfcUserIdFk the nbfcUserIdFk to set
	 */
	public void setNbfcUserIdFk(Long nbfcUserIdFk) {
		this.nbfcUserIdFk = nbfcUserIdFk;
	}

	/**
	 * @return the formPageJsonValue
	 */
	public String getFormPageJsonValue() {
		return formPageJsonValue;
	}

	/**
	 * @param formPageJsonValue the formPageJsonValue to set
	 */
	public void setFormPageJsonValue(String formPageJsonValue) {
		this.formPageJsonValue = formPageJsonValue;
	}

	/**
	 * @return the pageNo
	 */
	public Long getPageNo() {
		return pageNo;
	}

	/**
	 * @param pageNo the pageNo to set
	 */
	public void setPageNo(Long pageNo) {
		this.pageNo = pageNo;
	}

	/**
	 * @return the subPageNo
	 */
	public Long getSubPageNo() {
		return subPageNo;
	}

	/**
	 * @param subPageNo the subPageNo to set
	 */
	public void setSubPageNo(Long subPageNo) {
		this.subPageNo = subPageNo;
	}

	/**
	 * @return the nbfcCompanyTypeOtherId
	 */
	public int getNbfcCompanyTypeOtherId() {
		return nbfcCompanyTypeOtherId;
	}

	/**
	 * @param nbfcCompanyTypeOtherId the nbfcCompanyTypeOtherId to set
	 */
	public void setNbfcCompanyTypeOtherId(int nbfcCompanyTypeOtherId) {
		this.nbfcCompanyTypeOtherId = nbfcCompanyTypeOtherId;
	}

	/**
	 * @return the startPageNo
	 */
	public Long getStartPageNo() {
		return startPageNo;
	}

	/**
	 * @param startPageNo the startPageNo to set
	 */
	public void setStartPageNo(Long startPageNo) {
		this.startPageNo = startPageNo;
	}

	/**
	 * @return the endPageNo
	 */
	public Long getEndPageNo() {
		return endPageNo;
	}

	/**
	 * @param endPageNo the endPageNo to set
	 */
	public void setEndPageNo(Long endPageNo) {
		this.endPageNo = endPageNo;
	}

	/**
	 * @return the nbfcCompanyTypeId
	 */
	public int getNbfcCompanyTypeId() {
		return nbfcCompanyTypeId;
	}

	/**
	 * @param nbfcCompanyTypeId the nbfcCompanyTypeId to set
	 */
	public void setNbfcCompanyTypeId(int nbfcCompanyTypeId) {
		this.nbfcCompanyTypeId = nbfcCompanyTypeId;
	}

	/**
	 * @return the regStatusId
	 */
	public Long getRegStatusId() {
		return regStatusId;
	}

	/**
	 * @param regStatusId the regStatusId to set
	 */
	public void setRegStatusId(Long regStatusId) {
		this.regStatusId = regStatusId;
	}
}
