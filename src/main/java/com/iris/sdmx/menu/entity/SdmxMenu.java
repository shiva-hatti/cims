package com.iris.sdmx.menu.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author vjadhav
 *
 */
@Entity
@Table(name = "TBL_SDMX_MENU")
@JsonInclude(Include.NON_NULL)
public class SdmxMenu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5728019580358373535L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SDMX_MENU_ID")
	private Long sdmxMenuId;

	@Column(name = "SDMX_MENU_URL")
	private String sdmxMenuUrl;

	@Column(name = "DEFAULT_NAME")
	private String defaultName;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_MENU")
	private SdmxMenu parentMenuId;

	@Column(name = "LEVEL")
	private int level;

	@Column(name = "ICON")
	private String icon;

	@Column(name = "ORDER_NO")
	private Float orderNo;

	@Column(name = "GROUP_ID")
	private int groupId;

	@Column(name = "MENU_LABEL_KEY")
	private String menuLabelKey;

	@Transient
	private String menuLabel;

	@Transient
	private Boolean addRight;

	@Transient
	private Boolean editRight;

	@Transient
	private Boolean viewRight;

	@Transient
	private Boolean addApproval;

	@Transient
	private Boolean editApproval;

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
	public SdmxMenu getParentMenuId() {
		return parentMenuId;
	}

	/**
	 * @param parentMenuId the parentMenuId to set
	 */
	public void setParentMenuId(SdmxMenu parentMenuId) {
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

	public SdmxMenu() {
	}

	public SdmxMenu(Long sdmxMenuId, String sdmxMenuUrl, String defaultName, SdmxMenu parentMenuId, int level, String icon, Float orderNo, int groupId, String menuLabelKey, Boolean addRight, Boolean editRight, Boolean viewRight, Boolean addApproval, Boolean editApproval) {
		super();
		this.sdmxMenuId = sdmxMenuId;
		this.sdmxMenuUrl = sdmxMenuUrl;
		this.defaultName = defaultName;
		this.parentMenuId = parentMenuId;
		this.level = level;
		this.icon = icon;
		this.orderNo = orderNo;
		this.groupId = groupId;
		this.menuLabelKey = menuLabelKey;
		this.addRight = addRight;
		this.editRight = editRight;
		this.viewRight = viewRight;
		this.addApproval = addApproval;
		this.editApproval = editApproval;

	}

	@Override
	public String toString() {
		return "SdmxMenu [sdmxMenuId=" + sdmxMenuId + ", sdmxMenuUrl=" + sdmxMenuUrl + ", defaultName=" + defaultName + ", isActive=" + isActive + ", parentMenuId=" + parentMenuId + ", level=" + level + ", icon=" + icon + ", orderNo=" + orderNo + ", groupId=" + groupId + ", menuLabelKey=" + menuLabelKey + ", menuLabel=" + menuLabel + "]";
	}

}
