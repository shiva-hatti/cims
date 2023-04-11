package com.iris.ebr.rbr.error.entity;

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

import com.iris.rbrToEbr.entity.EbrRbrFlowMaster;

/**
 * @author sdhone
 */
@Entity
@Table(name = "TBL_CTL_EBR_RBR_FLOW_ERROR")
public class CtlEbrRbrFlowError implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2021357728584988196L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ERROR_PK")
	private Long errorPk;

	@Column(name = "CONTROL_FK")
	private int controlFk;

	@Column(name = "JOB_ID")
	private String jobId;

	@Column(name = "ENTITY_CODE")
	private String entityCode;

	@Column(name = "AUDIT_STATUS")
	private int auditStatus;

	@Column(name = "FREQUENCY")
	private String frequency;

	@Column(name = "UPLOAD_ID")
	private int uploadId;

	@Column(name = "REPORTING_PERIOD")
	private Date reportingPeriod;

	@Column(name = "FLOW_ID_FK")
	private int flowIdfk;

	@Column(name = "FLOW_NAME")
	private String flowName;

	@Column(name = "TASK_NAME")
	private String taskName;

	@Column(name = "ELEMENT_CODE")
	private String elementCode;

	@Column(name = "ELEMENT_VERSION")
	private String elementVersion;

	@Column(name = "SRC_REPOSITORY")
	private String srcRepository;

	@Column(name = "TGT_REPOSITORY")
	private String tgtRepository;

	@Column(name = "SRC_RETURN_CODE")
	private String srcReturnCode;

	@Column(name = "SRC_ELEMENT_CODE")
	private String srcElementCode;

	@Column(name = "SRC_ELEMENT_VERSION")
	private String srcElementVersion;

	@Column(name = "TGT_RETURN_CODE")
	private String tgtReturnCode;

	@Column(name = "TGT_ELEMENT_CODE")
	private String tgtElementCode;

	@Column(name = "TGT_ELEMENT_VERSION")
	private String tgtElementVersion;

	@Column(name = "SRC_TABLE_NAME")
	private String srcTableName;

	@Column(name = "TGT_TABLE_NAME")
	private String tgtTableName;

	@Column(name = "ERROR_CODE")
	private String errorCode;

	@Column(name = "ERROR_MESSAGE")
	private String errorMessage;

	@Column(name = "ERROR_LOG_PATH")
	private String errorLogPath;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	/**
	 * @return the errorPk
	 */
	public Long getErrorPk() {
		return errorPk;
	}

	/**
	 * @param errorPk the errorPk to set
	 */
	public void setErrorPk(Long errorPk) {
		this.errorPk = errorPk;
	}

	/**
	 * @return the jobId
	 */
	public String getJobId() {
		return jobId;
	}

	/**
	 * @param jobId the jobId to set
	 */
	public void setJobId(String jobId) {
		this.jobId = jobId;
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
	 * @return the frequency
	 */
	public String getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	/**
	 * @return the uploadId
	 */
	public int getUploadId() {
		return uploadId;
	}

	/**
	 * @param uploadId the uploadId to set
	 */
	public void setUploadId(int uploadId) {
		this.uploadId = uploadId;
	}

	/**
	 * @return the reportingPeriod
	 */
	public Date getReportingPeriod() {
		return reportingPeriod;
	}

	/**
	 * @param reportingPeriod the reportingPeriod to set
	 */
	public void setReportingPeriod(Date reportingPeriod) {
		this.reportingPeriod = reportingPeriod;
	}

	/**
	 * @return the flowName
	 */
	public String getFlowName() {
		return flowName;
	}

	/**
	 * @param flowName the flowName to set
	 */
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	/**
	 * @return the taskName
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * @param taskName the taskName to set
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
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
	 * @return the elementVersion
	 */
	public String getElementVersion() {
		return elementVersion;
	}

	/**
	 * @param elementVersion the elementVersion to set
	 */
	public void setElementVersion(String elementVersion) {
		this.elementVersion = elementVersion;
	}

	/**
	 * @return the srcRepository
	 */
	public String getSrcRepository() {
		return srcRepository;
	}

	/**
	 * @param srcRepository the srcRepository to set
	 */
	public void setSrcRepository(String srcRepository) {
		this.srcRepository = srcRepository;
	}

	/**
	 * @return the tgtRepository
	 */
	public String getTgtRepository() {
		return tgtRepository;
	}

	/**
	 * @param tgtRepository the tgtRepository to set
	 */
	public void setTgtRepository(String tgtRepository) {
		this.tgtRepository = tgtRepository;
	}

	/**
	 * @return the srcReturnCode
	 */
	public String getSrcReturnCode() {
		return srcReturnCode;
	}

	/**
	 * @param srcReturnCode the srcReturnCode to set
	 */
	public void setSrcReturnCode(String srcReturnCode) {
		this.srcReturnCode = srcReturnCode;
	}

	/**
	 * @return the srcElementCode
	 */
	public String getSrcElementCode() {
		return srcElementCode;
	}

	/**
	 * @param srcElementCode the srcElementCode to set
	 */
	public void setSrcElementCode(String srcElementCode) {
		this.srcElementCode = srcElementCode;
	}

	/**
	 * @return the srcElementVersion
	 */
	public String getSrcElementVersion() {
		return srcElementVersion;
	}

	/**
	 * @param srcElementVersion the srcElementVersion to set
	 */
	public void setSrcElementVersion(String srcElementVersion) {
		this.srcElementVersion = srcElementVersion;
	}

	/**
	 * @return the tgtReturnCode
	 */
	public String getTgtReturnCode() {
		return tgtReturnCode;
	}

	/**
	 * @param tgtReturnCode the tgtReturnCode to set
	 */
	public void setTgtReturnCode(String tgtReturnCode) {
		this.tgtReturnCode = tgtReturnCode;
	}

	/**
	 * @return the tgtElementCode
	 */
	public String getTgtElementCode() {
		return tgtElementCode;
	}

	/**
	 * @param tgtElementCode the tgtElementCode to set
	 */
	public void setTgtElementCode(String tgtElementCode) {
		this.tgtElementCode = tgtElementCode;
	}

	/**
	 * @return the tgtElementVersion
	 */
	public String getTgtElementVersion() {
		return tgtElementVersion;
	}

	/**
	 * @param tgtElementVersion the tgtElementVersion to set
	 */
	public void setTgtElementVersion(String tgtElementVersion) {
		this.tgtElementVersion = tgtElementVersion;
	}

	/**
	 * @return the srcTableName
	 */
	public String getSrcTableName() {
		return srcTableName;
	}

	/**
	 * @param srcTableName the srcTableName to set
	 */
	public void setSrcTableName(String srcTableName) {
		this.srcTableName = srcTableName;
	}

	/**
	 * @return the tgtTableName
	 */
	public String getTgtTableName() {
		return tgtTableName;
	}

	/**
	 * @param tgtTableName the tgtTableName to set
	 */
	public void setTgtTableName(String tgtTableName) {
		this.tgtTableName = tgtTableName;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the errorLogPath
	 */
	public String getErrorLogPath() {
		return errorLogPath;
	}

	/**
	 * @param errorLogPath the errorLogPath to set
	 */
	public void setErrorLogPath(String errorLogPath) {
		this.errorLogPath = errorLogPath;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
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
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
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
	 * @return the controlFk
	 */
	public int getControlFk() {
		return controlFk;
	}

	/**
	 * @param controlFk the controlFk to set
	 */
	public void setControlFk(int controlFk) {
		this.controlFk = controlFk;
	}

	/**
	 * @return the flowIdfk
	 */
	public int getFlowIdfk() {
		return flowIdfk;
	}

	/**
	 * @param flowIdfk the flowIdfk to set
	 */
	public void setFlowIdfk(int flowIdfk) {
		this.flowIdfk = flowIdfk;
	}

	/**
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}
