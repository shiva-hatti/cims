/**
 * 
 */
package com.iris.dto;

import java.io.Serializable;

/**
 * @author sanjayv
 *
 */
public class UserRoleEntityMappingDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long userRoleEntMapId;

	private Long entity;

	private Long userRole;

	private boolean isActive;

	public Long getUserRoleEntMapId() {
		return userRoleEntMapId;
	}

	public void setUserRoleEntMapId(Long userRoleEntMapId) {
		this.userRoleEntMapId = userRoleEntMapId;
	}

	public Long getEntity() {
		return entity;
	}

	public void setEntity(Long entity) {
		this.entity = entity;
	}

	public Long getUserRole() {
		return userRole;
	}

	public void setUserRole(Long userRole) {
		this.userRole = userRole;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
