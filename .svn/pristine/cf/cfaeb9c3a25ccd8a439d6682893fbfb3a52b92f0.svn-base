package com.iris.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.iris.util.Validations;

/**
 * This will use to store menu information
 * 
 * @author klahane
 *
 */
@Entity
@Table(name = "TBL_MENU")
public class Menu implements Serializable {

	private static final long serialVersionUID = -579172923825431785L;

	@Id
	@Column(name = "MENU_ID")
	private Long menuId;

	@Column(name = "MENU_DESC")
	private String menuDesc;

	@Column(name = "REDIRECTURL")
	private String redirectUrl;

	@Column(name = "ISACTIVE")
	private Boolean isActive;

	@Column(name = "APPLICABLE_FOR_DEPT")
	private Boolean isDept;

	@Column(name = "APPLICABLE_FOR_ENTITY")
	private Boolean isEntity;

	@Column(name = "APPLICABLE_FOR_AUDITOR")
	private Boolean isAuditor;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "menuIdFk")
	private Set<ActionMenuMapping> actionForCurntModule;

	@OneToMany(mappedBy = "menuIdFk")
	@OrderBy("languageIdFk")
	private Set<MenuLabel> menuLabelSet;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_MENU_ID")
	private Menu parentMenu;

	@Column(name = "ORDER_NO")
	private Float orderNo;

	@Column(name = "DEFAULT_NAME")
	private String defaultMenu;

	public Float getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Float orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * @return the menuId
	 */
	public Long getMenuId() {
		return menuId;
	}

	/**
	 * @param menuId the menuId to set
	 */
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	/**
	 * @return the menuDesc
	 */
	public String getMenuDesc() {
		return menuDesc;
	}

	/**
	 * @param menuDesc the menuDesc to set
	 */
	public void setMenuDesc(String menuDesc) {
		this.menuDesc = Validations.trimInput(menuDesc);
	}

	/**
	 * @return the redirectUrl
	 */
	public String getRedirectUrl() {
		return redirectUrl;
	}

	/**
	 * @param redirectUrl the redirectUrl to set
	 */
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = Validations.trimInput(redirectUrl);
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
	 * @return the actionForCurntModule
	 */
	public Set<ActionMenuMapping> getActionForCurntModule() {
		return actionForCurntModule;
	}

	/**
	 * @param actionForCurntModule the actionForCurntModule to set
	 */
	public void setActionForCurntModule(Set<ActionMenuMapping> actionForCurntModule) {
		this.actionForCurntModule = actionForCurntModule;
	}

	public Boolean getIsDept() {
		return isDept;
	}

	public void setIsDept(Boolean isDept) {
		this.isDept = isDept;
	}

	public Boolean getIsEntity() {
		return isEntity;
	}

	public void setIsEntity(Boolean isEntity) {
		this.isEntity = isEntity;
	}

	public Set<MenuLabel> getMenuLabelSet() {
		return menuLabelSet;
	}

	public void setMenuLabelSet(Set<MenuLabel> menuLabelSet) {
		this.menuLabelSet = menuLabelSet;
	}

	public Menu getParentMenu() {
		return parentMenu;
	}

	public void setParentMenu(Menu parentMenu) {
		this.parentMenu = parentMenu;
	}

	public Boolean getIsAuditor() {
		return isAuditor;
	}

	public void setIsAuditor(Boolean isAuditor) {
		this.isAuditor = isAuditor;
	}

	public String getDefaultMenu() {
		return defaultMenu;
	}

	public void setDefaultMenu(String defaultMenu) {
		this.defaultMenu = defaultMenu;
	}
}
