
package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Bhavana Thakare
 *
 */
@Entity
@Table(name = "TBL_CUSTODIAN_DETAILS")
public class CustodianDetails implements Serializable {
	private static final long serialVersionUID = -4842132418050544228L;

	@Id
	@Column(name = "CUSTODIAN_DETAILS_ID")
	private Long custodianDetaiksId;

	@Column(name = "CLIENTS_NAME")
	private String clientName;

	@Column(name = "CBS_CODE")
	private String cbsCode;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	public Long getCustodianDetaiksId() {
		return custodianDetaiksId;
	}

	public void setCustodianDetaiksId(Long custodianDetaiksId) {
		this.custodianDetaiksId = custodianDetaiksId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getCbsCode() {
		return cbsCode;
	}

	public void setCbsCode(String cbsCode) {
		this.cbsCode = cbsCode;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}
