package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.iris.util.Validations;

/**
 * This is the bean to store values from filing status table
 * 
 * @author Suman Kumar
 * @version 1.0
 */
@Entity
@Table(name = "TBL_FILING_STATUS")
public class FilingStatus implements Serializable {

	private static final long serialVersionUID = 7725691992072097958L;

	@Id
	@Column(name = "FILING_STATUS_ID")
	private Integer filingStatusId;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "FOR_WORKFLOW")
	private Boolean forWorkFlow;

	/**
	 * 
	 */
	public FilingStatus() {
	}

	/**
	 * @param filingStatusId
	 */
	public FilingStatus(Integer filingStatusId) {
		this.filingStatusId = filingStatusId;
	}

	/**
	 * @return the filingStatusId
	 */
	public Integer getFilingStatusId() {
		return filingStatusId;
	}

	/**
	 * @param filingStatusId the filingStatusId to set
	 */
	public void setFilingStatusId(Integer filingStatusId) {
		this.filingStatusId = filingStatusId;
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
		this.status = Validations.trimInput(status);
	}

	/**
	 * @return the forWorkFlow
	 */
	public Boolean getForWorkFlow() {
		return forWorkFlow;
	}

	/**
	 * @param forWorkFlow the forWorkFlow to set
	 */
	public void setForWorkFlow(Boolean forWorkFlow) {
		this.forWorkFlow = forWorkFlow;
	}
}
