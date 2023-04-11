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
 * This is the ReturnRegulatorMapping bean class with Hibernate mapping.
 * 
 * @author pmohite
 */
@Entity
@Table(name = "TBL_RETURN_REGULATOR_MAP_MOD")
public class ReturnRegulatorMapMod implements Serializable {

	private static final long serialVersionUID = 8262415709009662785L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RETURN_REGULATOR_MOD_ID")
	private Long returnRegulatorMapModId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REGULATOR_ID_FK")
	private Regulator regulatorIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFIED_BY_FK")
	private UserMaster updatedBy;

	@Column(name = "MODIFIED_ON")
	private Date updatedOn;

	@Column(name = "RETURNS_MAPPED")
	private String returnMapped;

	public Long getReturnRegulatorMapModId() {
		return returnRegulatorMapModId;
	}

	public void setReturnRegulatorMapModId(Long returnRegulatorMapModId) {
		this.returnRegulatorMapModId = returnRegulatorMapModId;
	}

	public Regulator getRegulatorIdFk() {
		return regulatorIdFk;
	}

	public void setRegulatorIdFk(Regulator regulatorIdFk) {
		this.regulatorIdFk = regulatorIdFk;
	}

	public UserMaster getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(UserMaster updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getReturnMapped() {
		return returnMapped;
	}

	public void setReturnMapped(String returnMapped) {
		this.returnMapped = returnMapped;
	}

}