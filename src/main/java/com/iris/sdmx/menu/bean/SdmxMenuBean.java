package com.iris.sdmx.menu.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.iris.sdmx.menu.entity.SdmxMenu;

public class SdmxMenuBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1692964310045417486L;

	private Long sdmxMenuId;

	private String sdmxMenuUrl;

	private String defaultName;

	private Boolean isActive;

	private Long parentMenuId;

	private int level;

	private String icon;

	private Float orderNo;

	private int groupId;

	private String menuLabelKey;

	private String menuLabel;

	private Boolean addRight;

	private Boolean editRight;

	private Boolean viewRight;

	private Boolean addApproval;

	private Boolean editApproval;

	/**
	 * @return the addApproval
	 */
	public Boolean getAddApproval() {
		return addApproval;
	}

	/**
	 * @param addApproval the addApproval to set
	 */
	public void setAddApproval(Boolean addApproval) {
		this.addApproval = addApproval;
	}

	/**
	 * @return the editApproval
	 */
	public Boolean getEditApproval() {
		return editApproval;
	}

	/**
	 * @param editApproval the editApproval to set
	 */
	public void setEditApproval(Boolean editApproval) {
		this.editApproval = editApproval;
	}

	/**
	 * @return the orderNo
	 */
	public Float getOrderNo() {
		return orderNo;
	}

	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(Float orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * @return the sdmxMenuId
	 */
	public Long getSdmxMenuId() {
		return sdmxMenuId;
	}

	/**
	 * @param sdmxMenuId the sdmxMenuId to set
	 */
	public void setSdmxMenuId(Long sdmxMenuId) {
		this.sdmxMenuId = sdmxMenuId;
	}

	/**
	 * @return the sdmxMenuUrl
	 */
	public String getSdmxMenuUrl() {
		return sdmxMenuUrl;
	}

	/**
	 * @param sdmxMenuUrl the sdmxMenuUrl to set
	 */
	public void setSdmxMenuUrl(String sdmxMenuUrl) {
		this.sdmxMenuUrl = sdmxMenuUrl;
	}

	/**
	 * @return the defaultName
	 */
	public String getDefaultName() {
		return defaultName;
	}

	/**
	 * @param defaultName the defaultName to set
	 */
	public void setDefaultName(String defaultName) {
		this.defaultName = defaultName;
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

	/**
	 * @return the parentMenuId
	 */
	public Long getParentMenuId() {
		return parentMenuId;
	}

	/**
	 * @param parentMenuId the parentMenuId to set
	 */
	public void setParentMenuId(Long parentMenuId) {
		this.parentMenuId = parentMenuId;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return the groupId
	 */
	public int getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the menuLabelKey
	 */
	public String getMenuLabelKey() {
		return menuLabelKey;
	}

	/**
	 * @param menuLabelKey the menuLabelKey to set
	 */
	public void setMenuLabelKey(String menuLabelKey) {
		this.menuLabelKey = menuLabelKey;
	}

	/**
	 * @return the menuLabel
	 */
	public String getMenuLabel() {
		return menuLabel;
	}

	/**
	 * @param menuLabel the menuLabel to set
	 */
	public void setMenuLabel(String menuLabel) {
		this.menuLabel = menuLabel;
	}

	/**
	 * @return the addRight
	 */
	public Boolean getAddRight() {
		return addRight;
	}

	/**
	 * @param addRight the addRight to set
	 */
	public void setAddRight(Boolean addRight) {
		this.addRight = addRight;
	}

	/**
	 * @return the editRight
	 */
	public Boolean getEditRight() {
		return editRight;
	}

	/**
	 * @param editRight the editRight to set
	 */
	public void setEditRight(Boolean editRight) {
		this.editRight = editRight;
	}

	/**
	 * @return the viewRight
	 */
	public Boolean getViewRight() {
		return viewRight;
	}

	/**
	 * @param viewRight the viewRight to set
	 */
	public void setViewRight(Boolean viewRight) {
		this.viewRight = viewRight;
	}

	@Override
	public String toString() {
		return "SdmxMenuBean [sdmxMenuId=" + sdmxMenuId + ", sdmxMenuUrl=" + sdmxMenuUrl + ", defaultName=" + defaultName + ", isActive=" + isActive + ", parentMenuId=" + parentMenuId + ", level=" + level + ", icon=" + icon + ", orderNo=" + orderNo + ", groupId=" + groupId + ", menuLabelKey=" + menuLabelKey + ", menuLabel=" + menuLabel + "]";
	}

}
