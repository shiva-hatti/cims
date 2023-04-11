package com.iris.dto;

import java.io.Serializable;

public class RoleMapHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -232661142044130242L;

	private String menuLabel;
	private String entityLabel;
	private String returnLabel;
	private String roleName;

	public RoleMapHistory() {
		super();
	}

	public RoleMapHistory(String menuLabel, String entityLabel, String returnLabel, String roleName) {
		super();
		this.menuLabel = menuLabel;
		this.entityLabel = entityLabel;
		this.returnLabel = returnLabel;
		this.roleName = roleName;
	}

	public String getMenuLabel() {
		return menuLabel;
	}

	public void setMenuLabel(String menuLabel) {
		this.menuLabel = menuLabel;
	}

	public String getEntityLabel() {
		return entityLabel;
	}

	public void setEntityLabel(String entityLabel) {
		this.entityLabel = entityLabel;
	}

	public String getReturnLabel() {
		return returnLabel;
	}

	public void setReturnLabel(String returnLabel) {
		this.returnLabel = returnLabel;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
