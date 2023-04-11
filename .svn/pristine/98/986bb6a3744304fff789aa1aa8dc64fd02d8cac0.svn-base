package com.iris.rbrToEbr.entity;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.iris.model.UserMaster;

/**
 * @author vjadhav
 *
 */
@Entity
@Table(name = "TBL_CTL_EBR_ELEMENTS")
@JsonInclude(Include.NON_NULL)
public class CtlEbrElementEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6477980045211664576L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CONTROL_EBR_ELEMENT_PK")
	private Long controlEbrElementId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTROL_FK")
	private EbrRbrFlow controlFk;

	@Column(name = "ENTITY_CODE")
	private String entityCode;

	@Column(name = "ELEMENT_CODE")
	private String elementCode;

	@Column(name = "AUDIT_STATUS")
	private int auditStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	private UserMaster createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFIED_BY")
	private UserMaster modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	/**
	 * @return the controlEbrElementId
	 */
	public Long getControlEbrElementId() {
		return controlEbrElementId;
	}

	/**
	 * @param controlEbrElementId the controlEbrElementId to set
	 */
	public void setControlEbrElementId(Long controlEbrElementId) {
		this.controlEbrElementId = controlEbrElementId;
	}

	/**
	 * @return the controlFk
	 */
	public EbrRbrFlow getControlFk() {
		return controlFk;
	}

	/**
	 * @param controlFk the controlFk to set
	 */
	public void setControlFk(EbrRbrFlow controlFk) {
		this.controlFk = controlFk;
	}

	/**
	 * @return the entityCode
	 */
	public String getEntityCode() {
		return entityCode;
	}

	/**
	 * @param entityCode the entityCode to set
	 */
	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	/**
	 * @return the elementCode
	 */
	public String getElementCode() {
		return elementCode;
	}

	/**
	 * @param elementCode the elementCode to set
	 */
	public void setElementCode(String elementCode) {
		this.elementCode = elementCode;
	}

	/**
	 * @return the auditStatus
	 */
	public int getAuditStatus() {
		return auditStatus;
	}

	/**
	 * @param auditStatus the auditStatus to set
	 */
	public void setAuditStatus(int auditStatus) {
		this.auditStatus = auditStatus;
	}

	/**
	 * @return the createdBy
	 */
	public UserMaster getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(UserMaster createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the modifiedBy
	 */
	public UserMaster getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(UserMaster modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the modifiedDate
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "CtlEbrElementEntity [controlEbrElementId=" + controlEbrElementId + ", controlFk=" + controlFk + ", entityCode=" + entityCode + ", elementCode=" + elementCode + ", auditStatus=" + auditStatus + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate + "]";
	}

}
