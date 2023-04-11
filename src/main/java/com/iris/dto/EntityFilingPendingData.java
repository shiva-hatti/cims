package com.iris.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class EntityFilingPendingData implements Serializable {
	/**
	 * Author: Pradnya
	 */
	private static final long serialVersionUID = -7805036157028846276L;
	private String entityCode;
	private String returnCode;
	private String endDate;
	private Date dtEndDate;
	private String uniqueId;
	private long roleId;
	private String entityName;
	private String returnName;
	private String returnProperty;
	private Long entityId;
	private Long returnId;
	private List<String> toEmailIdList;
	private List<String> ccEmailIdList;
	private List<String> bccEmailIdList;
	private List<Long> mailSentHistoryIdFkList;

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public List<String> getToEmailIdList() {
		return toEmailIdList;
	}

	public void setToEmailIdList(List<String> toEmailIdList) {
		this.toEmailIdList = toEmailIdList;
	}

	public List<String> getCcEmailIdList() {
		return ccEmailIdList;
	}

	public void setCcEmailIdList(List<String> ccEmailIdList) {
		this.ccEmailIdList = ccEmailIdList;
	}

	public List<String> getBccEmailIdList() {
		return bccEmailIdList;
	}

	public void setBccEmailIdList(List<String> bccEmailIdList) {
		this.bccEmailIdList = bccEmailIdList;
	}

	public List<Long> getMailSentHistoryIdFkList() {
		return mailSentHistoryIdFkList;
	}

	public void setMailSentHistoryIdFkList(List<Long> mailSentHistoryIdFkList) {
		this.mailSentHistoryIdFkList = mailSentHistoryIdFkList;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getReturnName() {
		return returnName;
	}

	public void setReturnName(String returnName) {
		this.returnName = returnName;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public Long getReturnId() {
		return returnId;
	}

	public void setReturnId(Long returnId) {
		this.returnId = returnId;
	}

	public Date getDtEndDate() {
		return dtEndDate;
	}

	public void setDtEndDate(Date dtEndDate) {
		this.dtEndDate = dtEndDate;
	}

	public String getReturnProperty() {
		return returnProperty;
	}

	public void setReturnProperty(String returnProperty) {
		this.returnProperty = returnProperty;
	}
}
