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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * This class is bean for AuditLog
 * 
 * @author pjuwar
 */
@Entity
@Table(name = "TBL_AUDIT_LOG")
public class AuditLog implements Serializable {

	private static final long serialVersionUID = -6305709526119023051L;

	@Id
	//	@SequenceGenerator(name = "AUDIT_LOG_ID_GENERATOR", sequenceName = "TBL_AUDIT_LOG_SEQ", allocationSize = 1)
	//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUDIT_LOG_ID_GENERATOR")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AUDIT_LOG_ID_PK")
	private long auditLogIdPk;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LOG_TIME_STAMP")
	private Date logTimeStamp;

	@Column(name = "LOG_LEVEL")
	private String loglevel;

	@Column(name = "LOG_CLASS_NAME")
	private String logClassName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID_FK")
	private UserMaster userIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MENU_ID_FK")
	private Menu menuIdFk;

	@Column(name = "IP_ADDRESS")
	private String ipAddress;

	@Column(name = "IS_PAGE_ACCESSED")
	private Boolean isPageAccessed;

	@Column(name = "LOG_MESSAGE")
	private String logMessage;

	@Column(name = "ROLE_ID")
	private Long roleId;

	@Transient
	private String refStartDate;

	@Transient
	private String refEndDate;

	@Transient
	private String entityCode;

	@Transient
	private String activityIds;

	@Transient
	private String menuIds;

	@Transient
	private String sessionDateFormat;

	@Transient
	private Boolean selectSpecificFlag;

	@Transient
	private Integer activityTypeId;

	@Transient
	private AuditLogMsgDetails auditLogMsgDetails;

	public long getAuditLogIdPk() {
		return auditLogIdPk;
	}

	public void setAuditLogIdPk(long auditLogIdPk) {
		this.auditLogIdPk = auditLogIdPk;
	}

	public Date getLogTimeStamp() {
		return logTimeStamp;
	}

	public void setLogTimeStamp(Date logTimeStamp) {
		this.logTimeStamp = logTimeStamp;
	}

	public String getLoglevel() {
		return loglevel;
	}

	public void setLoglevel(String loglevel) {
		this.loglevel = loglevel;
	}

	public String getLogClassName() {
		return logClassName;
	}

	public void setLogClassName(String logClassName) {
		this.logClassName = logClassName;
	}

	public UserMaster getUserIdFk() {
		return userIdFk;
	}

	public void setUserIdFk(UserMaster userIdFk) {
		this.userIdFk = userIdFk;
	}

	public Menu getMenuIdFk() {
		return menuIdFk;
	}

	public void setMenuIdFk(Menu menuIdFk) {
		this.menuIdFk = menuIdFk;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Boolean getIsPageAccessed() {
		return isPageAccessed;
	}

	public void setIsPageAccessed(Boolean isPageAccessed) {
		this.isPageAccessed = isPageAccessed;
	}

	public String getLogMessage() {
		return logMessage;
	}

	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRefStartDate() {
		return refStartDate;
	}

	public void setRefStartDate(String refStartDate) {
		this.refStartDate = refStartDate;
	}

	public String getRefEndDate() {
		return refEndDate;
	}

	public void setRefEndDate(String refEndDate) {
		this.refEndDate = refEndDate;
	}

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	public String getActivityIds() {
		return activityIds;
	}

	public void setActivityIds(String activityIds) {
		this.activityIds = activityIds;
	}

	public String getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

	public String getSessionDateFormat() {
		return sessionDateFormat;
	}

	public void setSessionDateFormat(String sessionDateFormat) {
		this.sessionDateFormat = sessionDateFormat;
	}

	public Boolean getSelectSpecificFlag() {
		return selectSpecificFlag;
	}

	public void setSelectSpecificFlag(Boolean selectSpecificFlag) {
		this.selectSpecificFlag = selectSpecificFlag;
	}

	public AuditLogMsgDetails getAuditLogMsgDetails() {
		return auditLogMsgDetails;
	}

	public void setAuditLogMsgDetails(AuditLogMsgDetails auditLogMsgDetails) {
		this.auditLogMsgDetails = auditLogMsgDetails;
	}

	public Integer getActivityTypeId() {
		return activityTypeId;
	}

	public void setActivityTypeId(Integer activityTypeId) {
		this.activityTypeId = activityTypeId;
	}

}
