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

import com.iris.sdmx.status.entity.AdminStatus;
import com.iris.util.Validations;

@Entity
@Table(name = "TBL_FRAUD_MASTER")
public class FraudMaster implements Serializable {

	private static final long serialVersionUID = 8605522663486252179L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "FRAUD_MASTER_ID")
	private BigInteger fraudMasterId;

	@Column(name = "FRAUD_CODE")
	private String fraudCode;

	@Column(name = "JSON_ENCODE")
	private String jsonEncode;

	@ManyToOne
	@JoinColumn(name = "RETURN_ID_FK")
	private Return returnIdFk;

	@ManyToOne
	@JoinColumn(name = "ENTITY_ID_FK")
	private EntityBean entityIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY_FK")
	private UserMaster createdByFk;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ADMIN_STATUS_ID_FK")
	private AdminStatus adminStatusIdFk;

	@Column(name = "COMMENTS")
	private String comments;

	@Column(name = "REMARK")
	private String remark;

	@Column(name = "SUPPORTING_DOCUMENT")
	private String supportingDocument;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REVIEWED_BY_FK")
	private UserMaster reviewedByFk;

	@Column(name = "REVIEWED_ON")
	private Date reviewedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPLOAD_ID_FK")
	private ReturnsUploadDetails returnsUploadDetails;

	@Column(name = "DATA_POPULATED_HIVE")
	private String dataPopulatedHive;

	@Column(name = "ACTIVITY_TYPE")
	private String activityType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFIED_BY_FK")
	private UserMaster modifiedByFk;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

	@Column(name = "SOURCE_FLAG")
	private String sourceFlag;

	public BigInteger getFraudMasterId() {
		return fraudMasterId;
	}

	public void setFraudMasterId(BigInteger fraudMasterId) {
		this.fraudMasterId = fraudMasterId;
	}

	public String getFraudCode() {
		return fraudCode;
	}

	public void setFraudCode(String fraudCode) {
		this.fraudCode = Validations.trimInput(fraudCode);
	}

	public String getJsonEncode() {
		return jsonEncode;
	}

	public void setJsonEncode(String jsonEncode) {
		this.jsonEncode = Validations.trimInput(jsonEncode);
	}

	public Return getReturnIdFk() {
		return returnIdFk;
	}

	public void setReturnIdFk(Return returnIdFk) {
		this.returnIdFk = returnIdFk;
	}

	public EntityBean getEntityIdFk() {
		return entityIdFk;
	}

	public void setEntityIdFk(EntityBean entityIdFk) {
		this.entityIdFk = entityIdFk;
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

	public AdminStatus getAdminStatusIdFk() {
		return adminStatusIdFk;
	}

	public void setAdminStatusIdFk(AdminStatus adminStatusIdFk) {
		this.adminStatusIdFk = adminStatusIdFk;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = Validations.trimInput(comments);
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = Validations.trimInput(remark);
	}

	public String getSupportingDocument() {
		return supportingDocument;
	}

	public void setSupportingDocument(String supportingDocument) {
		this.supportingDocument = Validations.trimInput(supportingDocument);
	}

	public UserMaster getReviewedByFk() {
		return reviewedByFk;
	}

	public void setReviewedByFk(UserMaster reviewedByFk) {
		this.reviewedByFk = reviewedByFk;
	}

	public Date getReviewedOn() {
		return reviewedOn;
	}

	public void setReviewedOn(Date reviewedOn) {
		this.reviewedOn = reviewedOn;
	}

	public ReturnsUploadDetails getReturnsUploadDetails() {
		return returnsUploadDetails;
	}

	public void setReturnsUploadDetails(ReturnsUploadDetails returnsUploadDetails) {
		this.returnsUploadDetails = returnsUploadDetails;
	}

	public String getDataPopulatedHive() {
		return dataPopulatedHive;
	}

	public void setDataPopulatedHive(String dataPopulatedHive) {
		this.dataPopulatedHive = Validations.trimInput(dataPopulatedHive);
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = Validations.trimInput(activityType);
	}

	public UserMaster getModifiedByFk() {
		return modifiedByFk;
	}

	public void setModifiedByFk(UserMaster modifiedByFk) {
		this.modifiedByFk = modifiedByFk;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getSourceFlag() {
		return sourceFlag;
	}

	public void setSourceFlag(String sourceFlag) {
		this.sourceFlag = Validations.trimInput(sourceFlag);
	}

}