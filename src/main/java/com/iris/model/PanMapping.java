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

@Entity
@Table(name = "TBL_PAN_MAPPING")
public class PanMapping implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9194504616212424280L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PAN_MAPPING_ID")
	private Long panMappingId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DUMMY_PAN_REF_ID_FK")
	private PanMaster dummyPanId;

	@Column(name = "DUMMY_PAN")
	private String dummyPanNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACTUAL_PAN_REF_ID_FK")
	private PanMaster actualPanId;

	@Column(name = "ACTUAL_PAN")
	private String actualPanNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY_FK")
	private UserMaster createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REQUESTED_BY_ENTITY_ID_FK")
	private EntityBean requestedByEntityId;

	@Column(name = "COMMENT")
	private String comment;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APPROVED_BY_FK")
	private UserMaster approvedByFk;

	@Column(name = "APPROVED_ON")
	private Date approvedOn;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "BORROWER_NAME")
	private String borrowerName;

	@Column(name = "ENTITY_LIST_USED_FOR_FILING")
	private String entityListUsedForFilling;

	public void setPanMappingId(Long panMappingId) {
		this.panMappingId = panMappingId;
	}

	public String getDummyPanNumber() {
		return dummyPanNumber;
	}

	public void setDummyPanNumber(String dummyPanNumber) {
		this.dummyPanNumber = dummyPanNumber;
	}

	public String getActualPanNumber() {
		return actualPanNumber;
	}

	public void setActualPanNumber(String actualPanNumber) {
		this.actualPanNumber = actualPanNumber;
	}

	public UserMaster getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserMaster createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public EntityBean getRequestedByEntityId() {
		return requestedByEntityId;
	}

	public void setRequestedByEntityId(EntityBean requestedByEntityId) {
		this.requestedByEntityId = requestedByEntityId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public UserMaster getApprovedByFk() {
		return approvedByFk;
	}

	public void setApprovedByFk(UserMaster approvedByFk) {
		this.approvedByFk = approvedByFk;
	}

	public Date getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getEntityListUsedForFilling() {
		return entityListUsedForFilling;
	}

	public void setEntityListUsedForFilling(String entityListUsedForFilling) {
		this.entityListUsedForFilling = entityListUsedForFilling;
	}

	public Long getPanMappingId() {
		return panMappingId;
	}

	public PanMaster getDummyPanId() {
		return dummyPanId;
	}

	public void setDummyPanId(PanMaster dummyPanId) {
		this.dummyPanId = dummyPanId;
	}

	public PanMaster getActualPanId() {
		return actualPanId;
	}

	public void setActualPanId(PanMaster actualPanId) {
		this.actualPanId = actualPanId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBorrowerName() {
		return borrowerName;
	}

	public void setBorrowerName(String borrowerName) {
		this.borrowerName = borrowerName;
	}
}
