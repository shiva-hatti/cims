package com.iris.sdmx.menu.bean;

import java.io.Serializable;

public class MenuAccessInputBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5810356265546430077L;

	private Long roleTypeId;

	private Boolean isAdmin;

	private Boolean isMainDept;

	/**
	 * @return the roleTypeId
	 */
	public Long getRoleTypeId() {
		return roleTypeId;
	}

	/**
	 * @param roleTypeId the roleTypeId to set
	 */
	public void setRoleTypeId(Long roleTypeId) {
		this.roleTypeId = roleTypeId;
	}

	/**
	 * @return the isAdmin
	 */
	public Boolean getIsAdmin() {
		return isAdmin;
	}

	/**
	 * @param isAdmin the isAdmin to set
	 */
	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	/**
	 * @return the isMainDept
	 */
	public Boolean getIsMainDept() {
		return isMainDept;
	}

	/**
	 * @param isMainDept the isMainDept to set
	 */
	public void setIsMainDept(Boolean isMainDept) {
		this.isMainDept = isMainDept;
	}

}
