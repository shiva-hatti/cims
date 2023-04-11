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
import com.iris.model.EntityBean;
import com.iris.model.Return;
import com.iris.model.ReturnPropertyValue;
import com.iris.model.UserMaster;

/**
 * @author vjadhav
 *
 */
@Entity
@Table(name = "TBL_EBR_DATA_CONVERSION_LOG")
@JsonInclude(Include.NON_NULL)
public class EbrDataConversionLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1734710352817027801L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AUDIT_LOG_ID")
	private Long auditLogId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_ID_FK")
	private Return returnIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENTITY_ID_FK")
	private EntityBean entityIdFk;

	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE")
	private Date endDate;

	@Column(name = "FINAL_TO_STAGE_JOB_NAME")
	private String finalToStageJobName;

	@Column(name = "FINAL_TO_STAGE_START_TIME")
	private Date finalToStageStartTime;

	@Column(name = "FINAL_TO_STAGE_END_TIME")
	private Date finalToStageEndTime;

	@Column(name = "FINAL_TO_STAGE_ERROR_LOG")
	private String finalToStageErrorlog;

	@Column(name = "STAGE_RBR_EBR_JOB_NAME")
	private String stageRbrEbrJobName;

	@Column(name = "STAGE_RBR_EBR_START_TIME")
	private Date stageRbrEbrStartTime;

	@Column(name = "STAGE_RBR_EBR_END_TIME")
	private Date stageRbrEbrEndTime;

	@Column(name = "STAGE_RBR_EBR_ERROR_LOG")
	private String stageRbrEbrErrorlog;

	@Column(name = "STAGE_EBR_RBR_JOB_NAME")
	private String stageEbrRbrJobName;

	@Column(name = "STAGE_EBR_RBR_START_TIME")
	private Date stageEbrRbrStartTime;

	@Column(name = "STAGE_EBR_RBR_END_TIME")
	private Date stageEbrRbrEndTime;

	@Column(name = "STAGE_EBR_RBR_ERROR_LOG")
	private String stageEbrRbrErrorlog;

	@Column(name = "STAGE_TO_FINAL_JOB_NAME")
	private String stageToFinalJobName;

	@Column(name = "STAGE_TO_FINAL_START_TIME")
	private Date stageToFinalStartTime;

	@Column(name = "STAGE_TO_FINAL_END_TIME")
	private Date stageToFinalEndTime;

	@Column(name = "STAGE_TO_FINAL_ERROR_LOG")
	private String stageToFinalErrorlog;

	@Column(name = "STATUS")
	private Long status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY_FK")
	private UserMaster createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	/**
	 * @return the returnIdFk
	 */
	public Return getReturnIdFk() {
		return returnIdFk;
	}

	/**
	 * @param returnIdFk the returnIdFk to set
	 */
	public void setReturnIdFk(Return returnIdFk) {
		this.returnIdFk = returnIdFk;
	}

	/**
	 * @return the entityIdFk
	 */
	public EntityBean getEntityIdFk() {
		return entityIdFk;
	}

	/**
	 * @param entityIdFk the entityIdFk to set
	 */
	public void setEntityIdFk(EntityBean entityIdFk) {
		this.entityIdFk = entityIdFk;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the finalToStageJobName
	 */
	public String getFinalToStageJobName() {
		return finalToStageJobName;
	}

	/**
	 * @param finalToStageJobName the finalToStageJobName to set
	 */
	public void setFinalToStageJobName(String finalToStageJobName) {
		this.finalToStageJobName = finalToStageJobName;
	}

	/**
	 * @return the finalToStageStartTime
	 */
	public Date getFinalToStageStartTime() {
		return finalToStageStartTime;
	}

	/**
	 * @param finalToStageStartTime the finalToStageStartTime to set
	 */
	public void setFinalToStageStartTime(Date finalToStageStartTime) {
		this.finalToStageStartTime = finalToStageStartTime;
	}

	/**
	 * @return the finalToStageEndTime
	 */
	public Date getFinalToStageEndTime() {
		return finalToStageEndTime;
	}

	/**
	 * @param finalToStageEndTime the finalToStageEndTime to set
	 */
	public void setFinalToStageEndTime(Date finalToStageEndTime) {
		this.finalToStageEndTime = finalToStageEndTime;
	}

	/**
	 * @return the finalToStageErrorlog
	 */
	public String getFinalToStageErrorlog() {
		return finalToStageErrorlog;
	}

	/**
	 * @param finalToStageErrorlog the finalToStageErrorlog to set
	 */
	public void setFinalToStageErrorlog(String finalToStageErrorlog) {
		this.finalToStageErrorlog = finalToStageErrorlog;
	}

	/**
	 * @return the stageRbrEbrJobName
	 */
	public String getStageRbrEbrJobName() {
		return stageRbrEbrJobName;
	}

	/**
	 * @param stageRbrEbrJobName the stageRbrEbrJobName to set
	 */
	public void setStageRbrEbrJobName(String stageRbrEbrJobName) {
		this.stageRbrEbrJobName = stageRbrEbrJobName;
	}

	/**
	 * @return the stageRbrEbrStartTime
	 */
	public Date getStageRbrEbrStartTime() {
		return stageRbrEbrStartTime;
	}

	/**
	 * @param stageRbrEbrStartTime the stageRbrEbrStartTime to set
	 */
	public void setStageRbrEbrStartTime(Date stageRbrEbrStartTime) {
		this.stageRbrEbrStartTime = stageRbrEbrStartTime;
	}

	/**
	 * @return the stageRbrEbrEndTime
	 */
	public Date getStageRbrEbrEndTime() {
		return stageRbrEbrEndTime;
	}

	/**
	 * @param stageRbrEbrEndTime the stageRbrEbrEndTime to set
	 */
	public void setStageRbrEbrEndTime(Date stageRbrEbrEndTime) {
		this.stageRbrEbrEndTime = stageRbrEbrEndTime;
	}

	/**
	 * @return the stageRbrEbrErrorlog
	 */
	public String getStageRbrEbrErrorlog() {
		return stageRbrEbrErrorlog;
	}

	/**
	 * @param stageRbrEbrErrorlog the stageRbrEbrErrorlog to set
	 */
	public void setStageRbrEbrErrorlog(String stageRbrEbrErrorlog) {
		this.stageRbrEbrErrorlog = stageRbrEbrErrorlog;
	}

	/**
	 * @return the stageEbrRbrJobName
	 */
	public String getStageEbrRbrJobName() {
		return stageEbrRbrJobName;
	}

	/**
	 * @param stageEbrRbrJobName the stageEbrRbrJobName to set
	 */
	public void setStageEbrRbrJobName(String stageEbrRbrJobName) {
		this.stageEbrRbrJobName = stageEbrRbrJobName;
	}

	/**
	 * @return the stageEbrRbrStartTime
	 */
	public Date getStageEbrRbrStartTime() {
		return stageEbrRbrStartTime;
	}

	/**
	 * @param stageEbrRbrStartTime the stageEbrRbrStartTime to set
	 */
	public void setStageEbrRbrStartTime(Date stageEbrRbrStartTime) {
		this.stageEbrRbrStartTime = stageEbrRbrStartTime;
	}

	/**
	 * @return the stageEbrRbrEndTime
	 */
	public Date getStageEbrRbrEndTime() {
		return stageEbrRbrEndTime;
	}

	/**
	 * @param stageEbrRbrEndTime the stageEbrRbrEndTime to set
	 */
	public void setStageEbrRbrEndTime(Date stageEbrRbrEndTime) {
		this.stageEbrRbrEndTime = stageEbrRbrEndTime;
	}

	/**
	 * @return the stageEbrRbrErrorlog
	 */
	public String getStageEbrRbrErrorlog() {
		return stageEbrRbrErrorlog;
	}

	/**
	 * @param stageEbrRbrErrorlog the stageEbrRbrErrorlog to set
	 */
	public void setStageEbrRbrErrorlog(String stageEbrRbrErrorlog) {
		this.stageEbrRbrErrorlog = stageEbrRbrErrorlog;
	}

	/**
	 * @return the stageToFinalJobName
	 */
	public String getStageToFinalJobName() {
		return stageToFinalJobName;
	}

	/**
	 * @param stageToFinalJobName the stageToFinalJobName to set
	 */
	public void setStageToFinalJobName(String stageToFinalJobName) {
		this.stageToFinalJobName = stageToFinalJobName;
	}

	/**
	 * @return the stageToFinalStartTime
	 */
	public Date getStageToFinalStartTime() {
		return stageToFinalStartTime;
	}

	/**
	 * @param stageToFinalStartTime the stageToFinalStartTime to set
	 */
	public void setStageToFinalStartTime(Date stageToFinalStartTime) {
		this.stageToFinalStartTime = stageToFinalStartTime;
	}

	/**
	 * @return the stageToFinalEndTime
	 */
	public Date getStageToFinalEndTime() {
		return stageToFinalEndTime;
	}

	/**
	 * @param stageToFinalEndTime the stageToFinalEndTime to set
	 */
	public void setStageToFinalEndTime(Date stageToFinalEndTime) {
		this.stageToFinalEndTime = stageToFinalEndTime;
	}

	/**
	 * @return the stageToFinalErrorlog
	 */
	public String getStageToFinalErrorlog() {
		return stageToFinalErrorlog;
	}

	/**
	 * @param stageToFinalErrorlog the stageToFinalErrorlog to set
	 */
	public void setStageToFinalErrorlog(String stageToFinalErrorlog) {
		this.stageToFinalErrorlog = stageToFinalErrorlog;
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
	 * @return the status
	 */
	public Long getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Long status) {
		this.status = status;
	}

	/**
	 * @return the auditLogId
	 */
	public Long getAuditLogId() {
		return auditLogId;
	}

	/**
	 * @param auditLogId the auditLogId to set
	 */
	public void setAuditLogId(Long auditLogId) {
		this.auditLogId = auditLogId;
	}

}
