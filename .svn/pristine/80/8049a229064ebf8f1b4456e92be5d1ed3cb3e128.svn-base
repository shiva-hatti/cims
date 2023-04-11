package com.iris.dto;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.gson.Gson;

public class PrivilegeAccount {

	private String roleName;
	private String roleDescription;
	private String roleType;
	private String userRoleId;
	private String userId;
	private String cimsPortalId;
	private String methodType;

	private Map<String, String> availableMenuList;
	private Map<String, String> availableReturnsList;
	private Map<String, String> availableEntityAccessList;

	private Map<Long, String> allotedMenuList;
	private Map<Long, String> workflowActivityList;

	private Map<Long, String> allotedReturnsList;
	private Map<Long, String> allotedEntityAccessList;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(String userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCimsPortalId() {
		return cimsPortalId;
	}

	public void setCimsPortalId(String cimsPortalId) {
		this.cimsPortalId = cimsPortalId;
	}

	public String getMethodType() {
		return methodType;
	}

	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}

	public Map<String, String> getAvailableMenuList() {
		return availableMenuList;
	}

	public void setAvailableMenuList(Map<String, String> availableMenuList) {
		this.availableMenuList = availableMenuList;
	}

	public Map<String, String> getAvailableReturnsList() {
		return availableReturnsList;
	}

	public void setAvailableReturnsList(Map<String, String> availableReturnsList) {
		this.availableReturnsList = availableReturnsList;
	}

	public Map<String, String> getAvailableEntityAccessList() {
		return availableEntityAccessList;
	}

	public void setAvailableEntityAccessList(Map<String, String> availableEntityAccessList) {
		this.availableEntityAccessList = availableEntityAccessList;
	}

	public Map<Long, String> getAllotedMenuList() {
		return allotedMenuList;
	}

	public void setAllotedMenuList(Map<Long, String> allotedMenuList) {
		this.allotedMenuList = allotedMenuList;
	}

	public Map<Long, String> getAllotedReturnsList() {
		return allotedReturnsList;
	}

	public void setAllotedReturnsList(Map<Long, String> allotedReturnsList) {
		this.allotedReturnsList = allotedReturnsList;
	}

	public Map<Long, String> getAllotedEntityAccessList() {
		return allotedEntityAccessList;
	}

	public void setAllotedEntityAccessList(Map<Long, String> allotedEntityAccessList) {
		this.allotedEntityAccessList = allotedEntityAccessList;
	}

	public Map<Long, String> getWorkflowActivityList() {
		return workflowActivityList;
	}

	public void setWorkflowActivityList(Map<Long, String> workflowActivityList) {
		this.workflowActivityList = workflowActivityList;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

	public String toJson() {
		return new Gson().toJson(this);
	}

	public PrivilegeAccount() {
		super();
	}
}
