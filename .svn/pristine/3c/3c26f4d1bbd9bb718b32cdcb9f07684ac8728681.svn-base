package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.iris.util.Validations;

/**
 * This is the Role Type class with Hibernate mapping.
 * 
 * @author pippar
 * @date 18/06/2015
 */
@Entity
@Table(name = "TBL_ROLE_TYPE")
@JsonInclude(Include.NON_NULL)
public class RoleType implements Serializable {

	private static final long serialVersionUID = 7810783407610050761L;

	@Id
	@Column(name = "ROLE_TYPE_ID")
	private Long roleTypeId;

	@Column(name = "ROLE_TYPE_DESC")
	private String roleTypeDesc;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	public RoleType() {
		super();
	}

	public RoleType(Long roleTypeId, String roleTypeDesc, Boolean isActive) {
		super();
		this.roleTypeId = roleTypeId;
		this.roleTypeDesc = roleTypeDesc;
		this.isActive = isActive;

	}

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
	 * @return the roleTypeDesc
	 */
	public String getRoleTypeDesc() {
		return roleTypeDesc;
	}

	/**
	 * @param roleTypeDesc the roleTypeDesc to set
	 */
	public void setRoleTypeDesc(String roleTypeDesc) {
		this.roleTypeDesc = Validations.trimInput(roleTypeDesc);
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

}