package com.iris.dto;

import java.io.Serializable;
import java.math.BigInteger;

public class FraudMasterDto implements Serializable {

	private static final long serialVersionUID = 8605522663486252179L;

	private BigInteger fraudMasterId;
	private String fraudCode;
	private String jsonEncode;
	private Long returnId;
	private Long entityId;
	private Long createdBy;
	private String createdOn;
	private Long uploadId;
	private String dataPopulatedHive;

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
		this.fraudCode = fraudCode;
	}

	public String getJsonEncode() {
		return jsonEncode;
	}

	public void setJsonEncode(String jsonEncode) {
		this.jsonEncode = jsonEncode;
	}

	public Long getReturnId() {
		return returnId;
	}

	public void setReturnId(Long returnId) {
		this.returnId = returnId;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUploadId() {
		return uploadId;
	}

	public void setUploadId(Long uploadId) {
		this.uploadId = uploadId;
	}

	public String getDataPopulatedHive() {
		return dataPopulatedHive;
	}

	public void setDataPopulatedHive(String dataPopulatedHive) {
		this.dataPopulatedHive = dataPopulatedHive;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
}