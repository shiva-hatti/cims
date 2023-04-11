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
 * this class is bean class for MenuRoleMap Management module
 * 
 * @author nsasane date 31/08/15 version 1.0
 */

@Entity
@Table(name = "TBL_MENU_ROLE_MAP")
public class MenuRoleMap implements Serializable {

	private static final long serialVersionUID = -4167862181529537516L;

	@Id
	//	@SequenceGenerator(name = "MENU_ROLE_ID_GENERATOR", sequenceName = "TBL_MENU_ROLE_MAP_SEQ", allocationSize = 1)
	//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MENU_ROLE_ID_GENERATOR")

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MENU_ROLE_ID")
	private Long menuRoleId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ROLE_ID_FK")
	private UserRole userRoleIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MENU_ID_FK")
	private Menu menuIDFk;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFIED_BY_FK")
	private UserMaster modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

	/**
	 * @return the menuRoleId
	 */
	public Long getMenuRoleId() {
		return menuRoleId;
	}

	/**
	 * @param menuRoleId the menuRoleId to set
	 */
	public void setMenuRoleId(Long menuRoleId) {
		this.menuRoleId = menuRoleId;
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
	 * @return the menuIDFk
	 */
	public Menu getMenuIDFk() {
		return menuIDFk;
	}

	/**
	 * @param menuIDFk the menuIDFk to set
	 */
	public void setMenuIDFk(Menu menuIDFk) {
		this.menuIDFk = menuIDFk;
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

}
