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

/**
 * @author nsasane date 31/08/15 version 1.0
 */
@Entity
@Table(name = "TBL_MENU_ROLE_MAP_MOD")
public class MenuRoleMapModified implements Serializable {

	private static final long serialVersionUID = 9101280045798912608L;

	@Id
	// @SequenceGenerator(name = "MENU_ROLE_MOD_ID_GENERATOR", sequenceName = "TBL_MENU_ROLE_MAP_MOD_SEQ", allocationSize = 1)
	// @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MENU_ROLE_MOD_ID_GENERATOR")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MENU_ROLE_MOD_ID")
	private Long menuRoleModId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MENU_ROLE_ID_FK")
	private MenuRoleMap menuRoleIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ROLE_ID_FK")
	private UserRole userRoleIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MENU_ID_FK")
	private Menu menuIDFK;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFIED_BY_FK")
	private UserMaster modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

	@Column(name = "GROUP_ID")
	private Long groupId;

	/**
	 * @return the menuRoleModId
	 */
	public Long getMenuRoleModId() {
		return menuRoleModId;
	}

	/**
	 * @param menuRoleModId the menuRoleModId to set
	 */
	public void setMenuRoleModId(Long menuRoleModId) {
		this.menuRoleModId = menuRoleModId;
	}

	/**
	 * @return the menuRoleIdFk
	 */
	public MenuRoleMap getMenuRoleIdFk() {
		return menuRoleIdFk;
	}

	/**
	 * @param menuRoleIdFk the menuRoleIdFk to set
	 */
	public void setMenuRoleIdFk(MenuRoleMap menuRoleIdFk) {
		this.menuRoleIdFk = menuRoleIdFk;
	}

	/**
	 * @return the userRoleIdFk
	 */
	public UserRole getUserRoleIdFk() {
		return userRoleIdFk;
	}

	/**
	 * @param userRoleIdFk the userRoleIdFk to set
	 */
	public void setUserRoleIdFk(UserRole userRoleIdFk) {
		this.userRoleIdFk = userRoleIdFk;
	}

	/**
	 * @return the menuIDFK
	 */
	public Menu getMenuIDFK() {
		return menuIDFK;
	}

	/**
	 * @param menuIDFK the menuIDFK to set
	 */
	public void setMenuIDFK(Menu menuIDFK) {
		this.menuIDFK = menuIDFK;
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
	 * @return the modifiedBy
	 */
	public UserMaster getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(UserMaster modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the modifiedOn
	 */
	public Date getModifiedOn() {
		return modifiedOn;
	}

	/**
	 * @param modifiedOn the modifiedOn to set
	 */
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	/**
	 * @return the groupId
	 */
	public Long getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

}