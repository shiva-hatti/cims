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
@Table(name = "TBL_BUSINESS_VALIDATION_FAILED")
public class BusinessValidationFailed implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5353517195388537896L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BUSINESS_VALIDATION_FAILED_ID")
	private Long buisnesValidationFailedId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPLOAD_ID_FK")
	private ReturnsUploadDetails returnsUploadDetails;

	@Column(name = "VALIDATION_FAILED_JSON")
	private String validationFailedJSON;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Long getBuisnesValidationFailedId() {
		return buisnesValidationFailedId;
	}

	public void setBuisnesValidationFailedId(Long buisnesValidationFailedId) {
		this.buisnesValidationFailedId = buisnesValidationFailedId;
	}

	public ReturnsUploadDetails getReturnsUploadDetails() {
		return returnsUploadDetails;
	}

	public void setReturnsUploadDetails(ReturnsUploadDetails returnsUploadDetails) {
		this.returnsUploadDetails = returnsUploadDetails;
	}

	public String getValidationFailedJSON() {
		return validationFailedJSON;
	}

	public void setValidationFailedJSON(String validationFailedJSON) {
		this.validationFailedJSON = validationFailedJSON;
	}

}
