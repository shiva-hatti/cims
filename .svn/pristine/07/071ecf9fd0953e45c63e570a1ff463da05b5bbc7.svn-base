package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.iris.util.Validations;

/**
 * It will store the menu's child action
 * 
 * @author klahane
 *
 */
@Entity
@Table(name = "TBL_ACTION_MENU_MAPPING")
public class ActionMenuMapping implements Serializable {

	private static final long serialVersionUID = -6675757656923278487L;

	@Id
	@Column(name = "ACTION_MENU_ID_PK")
	private Long actionMenuId;

	@Column(name = "ACTION_NAME")
	private String actionName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MENU_ID_FK")
	private Menu menuIdFk;

	/**
	 * @return the actionMenuId
	 */
	public Long getActionMenuId() {
		return actionMenuId;
	}

	/**
	 * @param actionMenuId the actionMenuId to set
	 */
	public void setActionMenuId(Long actionMenuId) {
		this.actionMenuId = actionMenuId;
	}

	/**
	 * @return the actionName
	 */
	public String getActionName() {
		return actionName;
	}

	/**
	 * @param actionName the actionName to set
	 */
	public void setActionName(String actionName) {
		this.actionName = Validations.trimInput(actionName);
	}

	/**
	 * @return the menuIdFk
	 */
	public Menu getMenuIdFk() {
		return menuIdFk;
	}

	/**
	 * @param menuIdFk the menuIdFk to set
	 */
	public void setMenuIdFk(Menu menuIdFk) {
		this.menuIdFk = menuIdFk;
	}

}
