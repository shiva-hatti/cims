package com.iris.model;

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

@Entity
@Table(name = "TBL_RETURN_RETURN_TYPE_MAPPING")
public class ReturnReturnTypeMapping implements Serializable {

	private static final long serialVersionUID = 4489176595989337528L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RETURN_RETURN_TYPE_ID")
	private Long returnReturnTypeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_ID")
	private Return returnIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_TYPE_ID")
	private ReturnType returnTypeIdFk;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	/**
	 * @return the returnReturnTypeId
	 */
	public Long getReturnReturnTypeId() {
		return returnReturnTypeId;
	}

	/**
	 * @param returnReturnTypeId the returnReturnTypeId to set
	 */
	public void setReturnReturnTypeId(Long returnReturnTypeId) {
		this.returnReturnTypeId = returnReturnTypeId;
	}

	/**
	 * @return the returnIdFk
	 */
	public Return getReturnIdFk() {
		return returnIdFk;
	}

	/**
	 * @param returnIdFk the returnIdFk to set
	 */
	public void setReturnIdFk(Return returnIdFk) {
		this.returnIdFk = returnIdFk;
	}

	/**
	 * @return the returnTypeIdFk
	 */
	public ReturnType getReturnTypeIdFk() {
		return returnTypeIdFk;
	}

	/**
	 * @param returnTypeIdFk the returnTypeIdFk to set
	 */
	public void setReturnTypeIdFk(ReturnType returnTypeIdFk) {
		this.returnTypeIdFk = returnTypeIdFk;
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