/**
 * 
 */
package com.iris.sdmx.element.entity;

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
import com.iris.sdmx.status.entity.ActionStatus;
import com.iris.sdmx.status.entity.AdminStatus;

/**
 * @author apagaria
 *
 */
@Entity
@Table(name = "TBL_SDMX_ELEMENT_MOD")
@JsonInclude(Include.NON_NULL)
public class SdmxElementTempEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ELEMENT_TEMP_ID")
	private Long elementTempId;

	@Column(name = "DSD_CODE")
	private String dsdCode;

	@Column(name = "ELEMENT_JSON")
	private String sdmxElementEntity;

	@Column(name = "ELEMENT_VER")
	private String elementVer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	private UserMaster createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_FK")
	private AdminStatus sdmxStatusEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACTION_STATUS_FK")
	private ActionStatus actionStatusFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ELEMENT_ID_FK")
	private SdmxElementEntity elementIdFk;

	@Column(name = "REVIEW_COMMENT")
	private String comment;

	/**
	 * @return the elementTempId
	 */
	public Long getElementTempId() {
		return elementTempId;
	}

	/**
	 * @param elementTempId the elementTempId to set
	 */
	public void setElementTempId(Long elementTempId) {
		this.elementTempId = elementTempId;
	}

	/**
	 * @return the dsdCode
	 */
	public String getDsdCode() {
		return dsdCode;
	}

	/**
	 * @param dsdCode the dsdCode to set
	 */
	public void setDsdCode(String dsdCode) {
		this.dsdCode = dsdCode;
	}

	/**
	 * @return the sdmxElementEntity
	 */
	public String getSdmxElementEntity() {
		return sdmxElementEntity;
	}

	/**
	 * @param sdmxElementEntity the sdmxElementEntity to set
	 */
	public void setSdmxElementEntity(String sdmxElementEntity) {
		this.sdmxElementEntity = sdmxElementEntity;
	}

	/**
	 * @return the elementVer
	 */
	public String getElementVer() {
		return elementVer;
	}

	/**
	 * @param elementVer the elementVer to set
	 */
	public void setElementVer(String elementVer) {
		this.elementVer = elementVer;
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

	/**
	 * @return the sdmxStatusEntity
	 */
	public AdminStatus getSdmxStatusEntity() {
		return sdmxStatusEntity;
	}

	/**
	 * @param sdmxStatusEntity the sdmxStatusEntity to set
	 */
	public void setSdmxStatusEntity(AdminStatus sdmxStatusEntity) {
		this.sdmxStatusEntity = sdmxStatusEntity;
	}

	/**
	 * @return the actionStatusFk
	 */
	public ActionStatus getActionStatusFk() {
		return actionStatusFk;
	}

	/**
	 * @param actionStatusFk the actionStatusFk to set
	 */
	public void setActionStatusFk(ActionStatus actionStatusFk) {
		this.actionStatusFk = actionStatusFk;
	}

	/**
	 * @return the elementIdFk
	 */
	public SdmxElementEntity getElementIdFk() {
		return elementIdFk;
	}

	/**
	 * @param elementIdFk the elementIdFk to set
	 */
	public void setElementIdFk(SdmxElementEntity elementIdFk) {
		this.elementIdFk = elementIdFk;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "SdmxElementTempEntity [elementTempId=" + elementTempId + ", dsdCode=" + dsdCode + ", sdmxElementEntity=" + sdmxElementEntity + ", elementVer=" + elementVer + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", sdmxStatusEntity=" + sdmxStatusEntity + ", actionStatusFk=" + actionStatusFk + "]";
	}

}
