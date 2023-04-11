package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is the Return Type class with Hibernate mapping.
 * 
 */
@Entity
@Table(name = "TBL_RETURN_TYPE")
public class ReturnType implements Serializable {

	private static final long serialVersionUID = 7812783407610050761L;

	@Id
	@Column(name = "RETURN_TYPE_ID")
	private Long returnTypeId;

	@Column(name = "RETURN_TYPE_DESC")
	private String returnTypeDesc;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	/**
	 * @return the returnTypeId
	 */
	public Long getReturnTypeId() {
		return returnTypeId;
	}

	/**
	 * @param returnTypeId the returnTypeId to set
	 */
	public void setReturnTypeId(Long returnTypeId) {
		this.returnTypeId = returnTypeId;
	}

	/**
	 * @return the returnTypeDesc
	 */
	public String getReturnTypeDesc() {
		return returnTypeDesc;
	}

	/**
	 * @param returnTypeDesc the returnTypeDesc to set
	 */
	public void setReturnTypeDesc(String returnTypeDesc) {
		this.returnTypeDesc = returnTypeDesc;
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