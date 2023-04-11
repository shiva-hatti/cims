/**
 * 
 */
package com.iris.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author sajadhav
 *
 */
@Entity
@Table(name = "TBL_FILING_PAN")
public class FilingPan implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2127566139624442093L;

	@Id
	@Column(name = "FILING_PAN_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer filingPanId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPLOAD_ID_FK")
	private ReturnsUploadDetails returnsUploadDetails;

	@Column(name = "COMPANY_PAN")
	private String companyPan;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	/**
	 * @return the returnsUploadDetails
	 */
	public ReturnsUploadDetails getReturnsUploadDetails() {
		return returnsUploadDetails;
	}

	/**
	 * @param returnsUploadDetails the returnsUploadDetails to set
	 */
	public void setReturnsUploadDetails(ReturnsUploadDetails returnsUploadDetails) {
		this.returnsUploadDetails = returnsUploadDetails;
	}

	/**
	 * @return the filingPanId
	 */
	public Integer getFilingPanId() {
		return filingPanId;
	}

	/**
	 * @param filingPanId the filingPanId to set
	 */
	public void setFilingPanId(Integer filingPanId) {
		this.filingPanId = filingPanId;
	}

	/**
	 * @return the companyPan
	 */
	public String getCompanyPan() {
		return companyPan;
	}

	/**
	 * @param companyPan the companyPan to set
	 */
	public void setCompanyPan(String companyPan) {
		this.companyPan = companyPan;
	}

	/**
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

}
