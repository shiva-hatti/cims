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
 * @author psheke
 * @date 18/12/2020
 */
@Entity
@Table(name = "TBL_ENTITY_AUDITOR_MAPPING_MOD")
public class EntityAuditorMapMod implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -357967153035667060L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENTITY_AUDITOR_MAP_ID_FK")
	private EntityAuditorMapInfo entityAuditorMapModIdFk;

	@Column(name = "MODIFICATION_JSON")
	private String entityAuditorMappingModJson;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_MODIFIED_BY_FK")
	private UserMaster lastModifiedBy;

	@Column(name = "LAST_MODIFIED_ON")
	private Date lastModifiedOn;

	@Id
	@Column(name = "ENTITY_AUDITOR_MODIFICATION_HISTORY_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long entityAuditorMapModHistId;

	/**
	 * @return the entityAuditorMapModHistId
	 */
	public Long getEntityAuditorMapModHistId() {
		return entityAuditorMapModHistId;
	}

	/**
	 * @param entityAuditorMapModHistId the entityAuditorMapModHistId to set
	 */
	public void setEntityAuditorMapModHistId(Long entityAuditorMapModHistId) {
		this.entityAuditorMapModHistId = entityAuditorMapModHistId;
	}

	/**
	 * @return the entityAuditorMapModIdFk
	 */
	public EntityAuditorMapInfo getEntityAuditorMapModIdFk() {
		return entityAuditorMapModIdFk;
	}

	/**
	 * @param entityAuditorMapModIdFk the entityAuditorMapModIdFk to set
	 */
	public void setEntityAuditorMapModIdFk(EntityAuditorMapInfo entityAuditorMapModIdFk) {
		this.entityAuditorMapModIdFk = entityAuditorMapModIdFk;
	}

	/**
	 * @return the entityAuditorMappingModJson
	 */
	public String getEntityAuditorMappingModJson() {
		return entityAuditorMappingModJson;
	}

	/**
	 * @param entityAuditorMappingModJson the entityAuditorMappingModJson to set
	 */
	public void setEntityAuditorMappingModJson(String entityAuditorMappingModJson) {
		this.entityAuditorMappingModJson = entityAuditorMappingModJson;
	}

	/**
	 * @return the lastModifiedBy
	 */
	public UserMaster getLastModifiedBy() {
		return lastModifiedBy;
	}

	/**
	 * @param lastModifiedBy the lastModifiedBy to set
	 */
	public void setLastModifiedBy(UserMaster lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	/**
	 * @return the lastModifiedOn
	 */
	public Date getLastModifiedOn() {
		return lastModifiedOn;
	}

	/**
	 * @param lastModifiedOn the lastModifiedOn to set
	 */
	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

}
