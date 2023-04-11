/**
 * 
 */
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.iris.model.EntityBean;
import com.iris.model.Return;
import com.iris.model.UserMaster;

/**
 * @author sikhan
 *
 */
@Entity
@Table(name = "TBL_RETURN_ENTITY_CHANNEL_MAP_MOD")
public class ReturnEntityChannelMapModification implements Serializable {

	private static final long serialVersionUID = -2861322655200032409L;

	@Id
	//@SequenceGenerator(name = "ENTITY_ID_GENERATOR", sequenceName = "TBL_ENTITY_SEQ", allocationSize = 1)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENTITY_ID_GENERATOR")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ENT_RET_CHNL_MAP_ID")
	private Long entityReturnChannelMapId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_ID_FK")
	private Return returnIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENTITY_ID_FK")
	private EntityBean entityIdFk;

	@Column(name = "ENT_RETURN_CHANNEL_MAP_JSON_DATA")
	private String entReturnChannelMapJsonData;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFIED_BY_FK")
	private UserMaster modifiedByFk;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

	/**
	 * @return the entityReturnChannelMapId
	 */
	public Long getEntityReturnChannelMapId() {
		return entityReturnChannelMapId;
	}

	/**
	 * @param entityReturnChannelMapId the entityReturnChannelMapId to set
	 */
	public void setEntityReturnChannelMapId(Long entityReturnChannelMapId) {
		this.entityReturnChannelMapId = entityReturnChannelMapId;
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
	 * @return the entReturnChannelMapJsonData
	 */
	public String getEntReturnChannelMapJsonData() {
		return entReturnChannelMapJsonData;
	}

	/**
	 * @param entReturnChannelMapJsonData the entReturnChannelMapJsonData to set
	 */
	public void setEntReturnChannelMapJsonData(String entReturnChannelMapJsonData) {
		this.entReturnChannelMapJsonData = entReturnChannelMapJsonData;
	}

	/**
	 * @return the modifiedByFk
	 */
	public UserMaster getModifiedByFk() {
		return modifiedByFk;
	}

	/**
	 * @param modifiedByFk the modifiedByFk to set
	 */
	public void setModifiedByFk(UserMaster modifiedByFk) {
		this.modifiedByFk = modifiedByFk;
	}

	/**
	 * @return the modifiedOn
	 */
	public Date getModifiedOn() {
		return modifiedOn;
	}

	/**
	 * @param modifiedOn the modifiedOn to set
	 */
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

}
