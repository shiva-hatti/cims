package com.iris.model;

public class UserAuditorRoleInfo {

	private long userAuditorRoleId;
	private long auditFirmId;
	private long userRoleMasterId;
	private String auditorEmail;
	private boolean isActive;

	public long getUserAuditorRoleId() {
		return userAuditorRoleId;
	}

	public void setUserAuditorRoleId(long userAuditorRoleId) {
		this.userAuditorRoleId = userAuditorRoleId;
	}

	public long getAuditFirmId() {
		return auditFirmId;
	}

	public void setAuditFirmId(long auditFirmId) {
		this.auditFirmId = auditFirmId;
	}

	public long getUserRoleMasterId() {
		return userRoleMasterId;
	}

	public void setUserRoleMasterId(long userRoleMasterId) {
		this.userRoleMasterId = userRoleMasterId;
	}

	public String getAuditorEmail() {
		return auditorEmail;
	}

	public void setAuditorEmail(String auditorEmail) {
		this.auditorEmail = auditorEmail;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
