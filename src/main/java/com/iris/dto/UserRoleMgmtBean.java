package com.iris.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.iris.model.Platform;
import com.iris.model.Return;
import com.iris.model.RoleType;

public class UserRoleMgmtBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6879802998335218057L;
	private String oldRoleName;
	private String rollName;
	private String rollNameHindi;
	private Long portal;
	private Long userId;
	private Long roleType;
	private String[] selectedReturn;
	private String[] selectedEntity;
	private Long department;
	private String roleDesc;
	private String[] selectedMenu;
	private List<ReturnGroupMappingDto> returnInputList = new ArrayList<>();
	private List<RegulatorDto> returnRoleList = new ArrayList<>();
	private List<DropDownObject> entityInputList = new ArrayList<>();
	private List<Platform> portalInputList = new ArrayList<>();
	private List<RoleType> roleTypeInputList = new ArrayList<>();
	private List<DropDownObject> menuInputList = new ArrayList<>();
	private List<DropDownGroup> menuInputGroupList = new ArrayList<>();
	private List<DropDownGroup> entityInputGroupList = new ArrayList<>();
	private List<ReturnGroupMappingDto> returnOutList = new ArrayList<>();
	private List<DropDownObject> entityOutList = new ArrayList<>();
	private List<DropDownObject> menuOutList = new ArrayList<>();
	private boolean deptAdminFlag;
	private boolean deptFlag;
	private boolean entityFlag;
	private int editView;
	private Long createdByRole;
	private Long roleIdExisting;
	private String viewMode;
	private List<DropDownObject> languageInDropDown = new ArrayList<>();
	private String languageRoleMap;
	private Long currentLocale;
	private String languageLabel;
	private String roleTypeLabel;
	private String portalLabel;
	private Long langId;
	private Long loggedInUserId;
	private Long roleTypeId;
	private Long isActive;
	private List<DropDownObject> activityEntInDropDown = new ArrayList<>();
	private List<DropDownObject> activityDeptInDropDown = new ArrayList<>();
	private List<Long> uploadActivity;
	private List<Long> apprvRejectActivityDept;
	private List<Long> apprvRejectActivityEnt;
	private String[] selectedActivity;
	private String menuId;
	private String deptMenuOption;
	private String entMenuOption;
	private String autMenuOption;
	private Long roleId;
	private Long returnId;
	private List<DropDownObject> portalRoleList = new ArrayList<>();
	private String userName;
	private String langCode;
	private List<String> platFormCodeList = new ArrayList<>();
	private String portalCode;
	private String entityCode;
	private Boolean isActiveFilter;
	private String deptAdmin;
	private boolean approvalFlag;
	private List<Return> returnInputModifiedList = new ArrayList<>();
	private String languageLabelHindi;
	private Long createdByRoleType;
	private List<DropDownObject> templateList = new ArrayList<>();
	private String[] selectedTemplate;
	private String otherPortalJSON;
	private Integer status;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDeptAdmin() {
		return deptAdmin;
	}

	public void setDeptAdmin(String deptAdmin) {
		this.deptAdmin = deptAdmin;
	}

	public List<Long> getUploadActivity() {
		return uploadActivity;
	}

	public void setUploadActivity(List<Long> uploadActivity) {
		this.uploadActivity = uploadActivity;
	}

	public List<Long> getApprvRejectActivityDept() {
		return apprvRejectActivityDept;
	}

	public void setApprvRejectActivityDept(List<Long> apprvRejectActivityDept) {
		this.apprvRejectActivityDept = apprvRejectActivityDept;
	}

	public List<Long> getApprvRejectActivityEnt() {
		return apprvRejectActivityEnt;
	}

	public void setApprvRejectActivityEnt(List<Long> apprvRejectActivityEnt) {
		this.apprvRejectActivityEnt = apprvRejectActivityEnt;
	}

	public Long getIsActive() {
		return isActive;
	}

	public void setIsActive(Long isActive) {
		this.isActive = isActive;
	}

	public Long getCurrentLocale() {
		return currentLocale;
	}

	public void setCurrentLocale(Long currentLocale) {
		this.currentLocale = currentLocale;
	}

	public List<DropDownObject> getLanguageInDropDown() {
		return languageInDropDown;
	}

	public void setLanguageInDropDown(List<DropDownObject> languageInDropDown) {
		this.languageInDropDown = languageInDropDown;
	}

	public Long getRoleIdExisting() {
		return roleIdExisting;
	}

	public void setRoleIdExisting(Long roleIdExisting) {
		this.roleIdExisting = roleIdExisting;
	}

	public String getRollName() {
		return rollName;
	}

	public void setRollName(String rollName) {
		this.rollName = rollName;
	}

	public String[] getSelectedReturn() {
		return selectedReturn;
	}

	public void setSelectedReturn(String[] selectedReturn) {
		this.selectedReturn = selectedReturn;
	}

	public String[] getSelectedEntity() {
		return selectedEntity;
	}

	public void setSelectedEntity(String[] selectedEntity) {
		this.selectedEntity = selectedEntity;
	}

	public String getRollNameHindi() {
		return rollNameHindi;
	}

	public void setRollNameHindi(String rollNameHindi) {
		this.rollNameHindi = rollNameHindi;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleType() {
		return roleType;
	}

	public void setRoleType(Long roleType) {
		this.roleType = roleType;
	}

	public Long getDepartment() {
		return department;
	}

	public void setDepartment(Long department) {
		this.department = department;
	}

	public String[] getSelectedMenu() {
		return selectedMenu;
	}

	public void setSelectedMenu(String[] selectedMenu) {
		this.selectedMenu = selectedMenu;
	}

	public List<ReturnGroupMappingDto> getReturnInputList() {
		return returnInputList;
	}

	public void setReturnInputList(List<ReturnGroupMappingDto> returnInputList) {
		this.returnInputList = returnInputList;
	}

	public List<Platform> getPortalInputList() {
		return portalInputList;
	}

	public List<DropDownObject> getEntityInputList() {
		return entityInputList;
	}

	public void setEntityInputList(List<DropDownObject> entityInputList) {
		this.entityInputList = entityInputList;
	}

	public void setPortalInputList(List<Platform> portalInputList) {
		this.portalInputList = portalInputList;
	}

	public List<RoleType> getRoleTypeInputList() {
		return roleTypeInputList;
	}

	public void setRoleTypeInputList(List<RoleType> roleTypeInputList) {
		this.roleTypeInputList = roleTypeInputList;
	}

	public List<DropDownObject> getMenuInputList() {
		return menuInputList;
	}

	public void setMenuInputList(List<DropDownObject> menuInputList) {
		this.menuInputList = menuInputList;
	}

	public boolean isDeptAdminFlag() {
		return deptAdminFlag;
	}

	public void setDeptAdminFlag(boolean deptAdminFlag) {
		this.deptAdminFlag = deptAdminFlag;
	}

	public boolean isDeptFlag() {
		return deptFlag;
	}

	public void setDeptFlag(boolean deptFlag) {
		this.deptFlag = deptFlag;
	}

	public boolean isEntityFlag() {
		return entityFlag;
	}

	public void setEntityFlag(boolean entityFlag) {
		this.entityFlag = entityFlag;
	}

	public Long getPortal() {
		return portal;
	}

	public void setPortal(Long portal) {
		this.portal = portal;
	}

	public List<ReturnGroupMappingDto> getReturnOutList() {
		return returnOutList;
	}

	public void setReturnOutList(List<ReturnGroupMappingDto> returnOutList) {
		this.returnOutList = returnOutList;
	}

	public List<DropDownObject> getEntityOutList() {
		return entityOutList;
	}

	public void setEntityOutList(List<DropDownObject> entityOutList) {
		this.entityOutList = entityOutList;
	}

	public List<DropDownObject> getMenuOutList() {
		return menuOutList;
	}

	public void setMenuOutList(List<DropDownObject> menuOutList) {
		this.menuOutList = menuOutList;
	}

	public int getEditView() {
		return editView;
	}

	public void setEditView(int editView) {
		this.editView = editView;
	}

	public Long getCreatedByRole() {
		return createdByRole;
	}

	public void setCreatedByRole(Long createdByRole) {
		this.createdByRole = createdByRole;
	}

	public String getOldRoleName() {
		return oldRoleName;
	}

	public void setOldRoleName(String oldRoleName) {
		this.oldRoleName = oldRoleName;
	}

	public List<DropDownGroup> getMenuInputGroupList() {
		return menuInputGroupList;
	}

	public void setMenuInputGroupList(List<DropDownGroup> menuInputGroupList) {
		this.menuInputGroupList = menuInputGroupList;
	}

	public List<DropDownGroup> getEntityInputGroupList() {
		return entityInputGroupList;
	}

	public void setEntityInputGroupList(List<DropDownGroup> entityInputGroupList) {
		this.entityInputGroupList = entityInputGroupList;
	}

	@Override
	public String toString() {
		return "UserRoleMgmtBean [oldRoleName=" + oldRoleName + ", rollName=" + rollName + ", rollNameHindi=" + rollNameHindi + ", portal=" + portal + ", userId=" + userId + ", roleType=" + roleType + ", selectedReturn=" + Arrays.toString(selectedReturn) + ", selectedEntity=" + Arrays.toString(selectedEntity) + ", department=" + department + ", roleDesc=" + roleDesc + ", selectedMenu=" + Arrays.toString(selectedMenu) + ", returnInputList=" + returnInputList + ", entityInputList=" + entityInputList + ", portalInputList=" + portalInputList + ", roleTypeInputList=" + roleTypeInputList + ", menuInputList=" + menuInputList + ", menuInputGroupList=" + menuInputGroupList + ", entityInputGroupList=" + entityInputGroupList + ", returnOutList=" + returnOutList + ", entityOutList=" + entityOutList + ", menuOutList=" + menuOutList + ", deptAdminFlag=" + deptAdminFlag + ", deptFlag=" + deptFlag + ", entityFlag=" + entityFlag + ", editView=" + editView + ", createdByRole=" + createdByRole + ", roleIdExisting=" + roleIdExisting + ", languageInDropDown=" + languageInDropDown + ", currentLocale=" + currentLocale + "]";
	}

	public List<DropDownObject> getActivityEntInDropDown() {
		return activityEntInDropDown;
	}

	public void setActivityEntInDropDown(List<DropDownObject> activityEntInDropDown) {
		this.activityEntInDropDown = activityEntInDropDown;
	}

	public List<DropDownObject> getActivityDeptInDropDown() {
		return activityDeptInDropDown;
	}

	public void setActivityDeptInDropDown(List<DropDownObject> activityDeptInDropDown) {
		this.activityDeptInDropDown = activityDeptInDropDown;
	}

	public String[] getSelectedActivity() {
		return selectedActivity;
	}

	public void setSelectedActivity(String[] selectedActivity) {
		this.selectedActivity = selectedActivity;
	}

	public String getDeptMenuOption() {
		return deptMenuOption;
	}

	public void setDeptMenuOption(String deptMenuOption) {
		this.deptMenuOption = deptMenuOption;
	}

	public String getEntMenuOption() {
		return entMenuOption;
	}

	public void setEntMenuOption(String entMenuOption) {
		this.entMenuOption = entMenuOption;
	}

	/**
	 * @return the roleId
	 */
	public Long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the returnId
	 */
	public Long getReturnId() {
		return returnId;
	}

	/**
	 * @param returnId the returnId to set
	 */
	public void setReturnId(Long returnId) {
		this.returnId = returnId;
	}

	public List<DropDownObject> getPortalRoleList() {
		return portalRoleList;
	}

	public void setPortalRoleList(List<DropDownObject> portalRoleList) {
		this.portalRoleList = portalRoleList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<String> getPlatFormCodeList() {
		return platFormCodeList;
	}

	public void setPlatFormCodeList(List<String> platFormCodeList) {
		this.platFormCodeList = platFormCodeList;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public String getPortalCode() {
		return portalCode;
	}

	public void setPortalCode(String portalCode) {
		this.portalCode = portalCode;
	}

	public Boolean getIsActiveFilter() {
		return isActiveFilter;
	}

	public void setIsActiveFilter(Boolean isActiveFilter) {
		this.isActiveFilter = isActiveFilter;
	}

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	public String getAutMenuOption() {
		return autMenuOption;
	}

	public void setAutMenuOption(String autMenuOption) {
		this.autMenuOption = autMenuOption;
	}

	public String getViewMode() {
		return viewMode;
	}

	public void setViewMode(String viewMode) {
		this.viewMode = viewMode;
	}

	public String getLanguageRoleMap() {
		return languageRoleMap;
	}

	public void setLanguageRoleMap(String languageRoleMap) {
		this.languageRoleMap = languageRoleMap;
	}

	public String getLanguageLabel() {
		return languageLabel;
	}

	public void setLanguageLabel(String languageLabel) {
		this.languageLabel = languageLabel;
	}

	public String getRoleTypeLabel() {
		return roleTypeLabel;
	}

	public void setRoleTypeLabel(String roleTypeLabel) {
		this.roleTypeLabel = roleTypeLabel;
	}

	public String getPortalLabel() {
		return portalLabel;
	}

	public void setPortalLabel(String portalLabel) {
		this.portalLabel = portalLabel;
	}

	public Long getLangId() {
		return langId;
	}

	public void setLangId(Long langId) {
		this.langId = langId;
	}

	public Long getLoggedInUserId() {
		return loggedInUserId;
	}

	public void setLoggedInUserId(Long loggedInUserId) {
		this.loggedInUserId = loggedInUserId;
	}

	public Long getRoleTypeId() {
		return roleTypeId;
	}

	public boolean isApprovalFlag() {
		return approvalFlag;
	}

	public void setApprovalFlag(boolean approvalFlag) {
		this.approvalFlag = approvalFlag;
	}

	public void setRoleTypeId(Long roleTypeId) {
		this.roleTypeId = roleTypeId;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public List<Return> getReturnInputModifiedList() {
		return returnInputModifiedList;
	}

	public void setReturnInputModifiedList(List<Return> returnInputModifiedList) {
		this.returnInputModifiedList = returnInputModifiedList;
	}

	public String getLanguageLabelHindi() {
		return languageLabelHindi;
	}

	public void setLanguageLabelHindi(String languageLabelHindi) {
		this.languageLabelHindi = languageLabelHindi;
	}

	public Long getCreatedByRoleType() {
		return createdByRoleType;
	}

	public void setCreatedByRoleType(Long createdByRoleType) {
		this.createdByRoleType = createdByRoleType;
	}

	public List<DropDownObject> getTemplateList() {
		return templateList;
	}

	public void setTemplateList(List<DropDownObject> templateList) {
		this.templateList = templateList;
	}

	public String[] getSelectedTemplate() {
		return selectedTemplate;
	}

	public void setSelectedTemplate(String[] selectedTemplate) {
		this.selectedTemplate = selectedTemplate;
	}

	public String getOtherPortalJSON() {
		return otherPortalJSON;
	}

	public void setOtherPortalJSON(String otherPortalJSON) {
		this.otherPortalJSON = otherPortalJSON;
	}

	/**
	 * @return the returnRoleList
	 */
	public List<RegulatorDto> getReturnRoleList() {
		return returnRoleList;
	}

	/**
	 * @param returnRoleList the returnRoleList to set
	 */
	public void setReturnRoleList(List<RegulatorDto> returnRoleList) {
		this.returnRoleList = returnRoleList;
	}

}
