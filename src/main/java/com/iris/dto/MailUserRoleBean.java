package com.iris.dto;

import java.io.Serializable;
import java.util.List;

public class MailUserRoleBean implements Serializable {

	private static final long serialVersionUID = -2955000219121757484L;

	private List<Long> roleList;
	private List<Long> entityId;
	private List<Long> auditorId;
	private Boolean audtFlag = false;

	/**
	 * @return the roleList
	 */
	public List<Long> getRoleList() {
		return roleList;
	}

	/**
	 * @param roleList the roleList to set
	 */
	public void setRoleList(List<Long> roleList) {
		this.roleList = roleList;
	}

	/**
	 * @return the entityId
	 */
	public List<Long> getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(List<Long> entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return the auditorId
	 */
	public List<Long> getAuditorId() {
		return auditorId;
	}

	/**
	 * @param auditorId the auditorId to set
	 */
	public void setAuditorId(List<Long> auditorId) {
		this.auditorId = auditorId;
	}

	/**
	 * @return the audtFlag
	 */
	public Boolean getAudtFlag() {
		return audtFlag;
	}

	/**
	 * @param audtFlag the audtFlag to set
	 */
	public void setAudtFlag(Boolean audtFlag) {
		this.audtFlag = audtFlag;
	}

}
