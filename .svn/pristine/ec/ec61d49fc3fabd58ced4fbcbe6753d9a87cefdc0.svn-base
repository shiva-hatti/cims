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

/**
 * @author pradnya
 */
@Entity
@Table(name = "TBL_DEPT_USER_ENTITY_MAPPING")
public class DeptUserEntityMapping implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ENTITY_MAP_ID")
	private Long deptUserEntMapId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID_FK")
	private UserMaster userIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENTITY_ID_FK")
	private EntityBean entity;

	@Column(name = "IS_ACTIVE")
	private boolean isActive;

	public DeptUserEntityMapping() {

	}

	public Long getDeptUserEntMapId() {
		return deptUserEntMapId;
	}

	public void setDeptUserEntMapId(Long deptUserEntMapId) {
		this.deptUserEntMapId = deptUserEntMapId;
	}

	public UserMaster getUserIdFk() {
		return userIdFk;
	}

	public void setUserIdFk(UserMaster userIdFk) {
		this.userIdFk = userIdFk;
	}

	public EntityBean getEntity() {
		return entity;
	}

	public void setEntity(EntityBean entity) {
		this.entity = entity;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}