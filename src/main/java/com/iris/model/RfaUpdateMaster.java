package com.iris.model;

import java.io.Serializable;
import java.math.BigInteger;
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

import com.iris.util.Validations;

@Entity
@Table(name = "TBL_RFA_UPDATE_MASTER")
public class RfaUpdateMaster implements Serializable {

	private static final long serialVersionUID = 1140399731700695259L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RFA_UPDATE_MASTER_ID")
	private BigInteger rfaUpdateMasterId;

	@ManyToOne
	@JoinColumn(name = "ENTITY_ID_FK")
	private EntityBean entityIdFk;

	@Column(name = "BORROWER_ID")
	private Long borrowerId;

	@Column(name = "BORROWER_NAME")
	private String borrowerName;

	@Column(name = "BORROWER_PAN")
	private String borrowerPan;

	@Column(name = "DATE_OF_RFA_CLASSIFICATION")
	private Date dateOfRfaClassification;

	@Column(name = "STATUS_OF_RFA")
	private String statusOfRfa;

	@Column(name = "DATE_OF_FRAUD_CLASSIFICATION_REMOVAL")
	private Date dateOfFraudClassificationRemoval;

	@Column(name = "JSON_ENCODE")
	private String jsonEncode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY_FK")
	private UserMaster createdByFk;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	public BigInteger getRfaUpdateMasterId() {
		return rfaUpdateMasterId;
	}

	public void setRfaUpdateMasterId(BigInteger rfaUpdateMasterId) {
		this.rfaUpdateMasterId = rfaUpdateMasterId;
	}

	public EntityBean getEntityIdFk() {
		return entityIdFk;
	}

	public void setEntityIdFk(EntityBean entityIdFk) {
		this.entityIdFk = entityIdFk;
	}

	public String getBorrowerName() {
		return borrowerName;
	}

	public void setBorrowerName(String borrowerName) {
		this.borrowerName = Validations.trimInput(borrowerName);
	}

	public Date getDateOfRfaClassification() {
		return dateOfRfaClassification;
	}

	public void setDateOfRfaClassification(Date dateOfRfaClassification) {
		this.dateOfRfaClassification = dateOfRfaClassification;
	}

	public String getStatusOfRfa() {
		return statusOfRfa;
	}

	public void setStatusOfRfa(String statusOfRfa) {
		this.statusOfRfa = Validations.trimInput(statusOfRfa);
	}

	public Date getDateOfFraudClassificationRemoval() {
		return dateOfFraudClassificationRemoval;
	}

	public void setDateOfFraudClassificationRemoval(Date dateOfFraudClassificationRemoval) {
		this.dateOfFraudClassificationRemoval = dateOfFraudClassificationRemoval;
	}

	public String getJsonEncode() {
		return jsonEncode;
	}

	public void setJsonEncode(String jsonEncode) {
		this.jsonEncode = Validations.trimInput(jsonEncode);
	}

	public UserMaster getCreatedByFk() {
		return createdByFk;
	}

	public void setCreatedByFk(UserMaster createdByFk) {
		this.createdByFk = createdByFk;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Long getBorrowerId() {
		return borrowerId;
	}

	public void setBorrowerId(Long borrowerId) {
		this.borrowerId = borrowerId;
	}

	public String getBorrowerPan() {
		return borrowerPan;
	}

	public void setBorrowerPan(String borrowerPan) {
		this.borrowerPan = Validations.trimInput(borrowerPan);
	}

}