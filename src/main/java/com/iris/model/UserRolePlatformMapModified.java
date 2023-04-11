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
 * @author psawant
 * @version 1.0
 * @date 27/11/2019
 */
@Entity
@Table(name = "TBL_USER_ROLE_PLATFORM_MAP_MOD")
public class UserRolePlatformMapModified implements Serializable {

	private static final long serialVersionUID = 1617263341544152171L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ROLE_PLAT_MAP_MOD_ID")
	private Long userRolePlatMapModId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ROLE_ID_FK")
	private UserRole userRoleIdFk;

	@Column(name = "USER_ROLE_PLAT_MAPPING")
	private String userRolePlatMapping;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_MODIFIED_BY_FK")
	private UserMaster lastModifiedByFk;

	@Column(name = "LAST_MODIFIED_ON")
	private Date lastModifiedOn;

	public Long getUserRolePlatMapModId() {
		return userRolePlatMapModId;
	}

	public void setUserRolePlatMapModId(Long userRolePlatMapModId) {
		this.userRolePlatMapModId = userRolePlatMapModId;
	}

	public UserRole getUserRoleIdFk() {
		return userRoleIdFk;
	}

	public void setUserRoleIdFk(UserRole userRoleIdFk) {
		this.userRoleIdFk = userRoleIdFk;
	}

	public String getUserRolePlatMapping() {
		return userRolePlatMapping;
	}

	public void setUserRolePlatMapping(String userRolePlatMapping) {
		this.userRolePlatMapping = userRolePlatMapping;
	}

	public UserMaster getLastModifiedByFk() {
		return lastModifiedByFk;
	}

	public void setLastModifiedByFk(UserMaster lastModifiedByFk) {
		this.lastModifiedByFk = lastModifiedByFk;
	}

	public Date getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

}