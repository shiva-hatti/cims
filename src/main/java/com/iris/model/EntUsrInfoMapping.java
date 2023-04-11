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
import javax.persistence.Transient;

@Entity
@Table(name = "TBL_ENT_USR_INFO_MAPPING")
public class EntUsrInfoMapping implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2672447395521935744L;

	@Id
	// @SequenceGenerator(name = "ENT_USR_INFO_MAP_ID_GENERATOR", sequenceName = "TBL_ENT_USR_INFO_MAPPING_SEQ", allocationSize = 1)
	// @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENT_USR_INFO_MAP_ID_GENERATOR")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ENT_USR_INFO_MAP_ID")
	private Long entUsrInfoMapId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENTITY_ID_FK")
	private EntityBean entityIdFk;

	@Column(name = "MAX_FILER_ADMIN_BANK")
	private Integer maxAdminBank;

	@Column(name = "MAX_FILER_CHECKER_BANK")
	private Integer maxCheckerBank;

	@Column(name = "MAX_FILER_MAKER_BANK")
	private Integer maxMakerBank;

	@Column(name = "MAX_FILER_SYSTEM_BANK")
	private Integer maxSystemBank;

	@Column(name = "MAX_TOTAL_USER_BANK")
	private Integer maxUserBank;

	@Transient
	private Long entModId;

	/**
	 * @return the entUsrInfoMapId
	 */
	public Long getEntUsrInfoMapId() {
		return entUsrInfoMapId;
	}

	/**
	 * @param entUsrInfoMapId the entUsrInfoMapId to set
	 */
	public void setEntUsrInfoMapId(Long entUsrInfoMapId) {
		this.entUsrInfoMapId = entUsrInfoMapId;
	}

	/**
	 * @return the maxAdminBank
	 */
	public Integer getMaxAdminBank() {
		return maxAdminBank;
	}

	/**
	 * @param maxAdminBank the maxAdminBank to set
	 */
	public void setMaxAdminBank(Integer maxAdminBank) {
		this.maxAdminBank = maxAdminBank;
	}

	/**
	 * @return the maxCheckerBank
	 */
	public Integer getMaxCheckerBank() {
		return maxCheckerBank;
	}

	/**
	 * @param maxCheckerBank the maxCheckerBank to set
	 */
	public void setMaxCheckerBank(Integer maxCheckerBank) {
		this.maxCheckerBank = maxCheckerBank;
	}

	/**
	 * @return the maxMakerBank
	 */
	public Integer getMaxMakerBank() {
		return maxMakerBank;
	}

	/**
	 * @param maxMakerBank the maxMakerBank to set
	 */
	public void setMaxMakerBank(Integer maxMakerBank) {
		this.maxMakerBank = maxMakerBank;
	}

	/**
	 * @return the maxSystemBank
	 */
	public Integer getMaxSystemBank() {
		return maxSystemBank;
	}

	/**
	 * @param maxSystemBank the maxSystemBank to set
	 */
	public void setMaxSystemBank(Integer maxSystemBank) {
		this.maxSystemBank = maxSystemBank;
	}

	/**
	 * @return the entityIdFk
	 */
	public EntityBean getEntityIdFk() {
		return entityIdFk;
	}

	/**
	 * @param entityIdFk the entityIdFk to set
	 */
	public void setEntityIdFk(EntityBean entityIdFk) {
		this.entityIdFk = entityIdFk;
	}

	/**
	 * @return the entModId
	 */
	public Long getEntModId() {
		return entModId;
	}

	/**
	 * @param entModId the entModId to set
	 */
	public void setEntModId(Long entModId) {
		this.entModId = entModId;
	}

	/**
	 * @return the maxUserBank
	 */
	public Integer getMaxUserBank() {
		return maxUserBank;
	}

	/**
	 * @param maxUserBank the maxUserBank to set
	 */
	public void setMaxUserBank(Integer maxUserBank) {
		this.maxUserBank = maxUserBank;
	}

}
