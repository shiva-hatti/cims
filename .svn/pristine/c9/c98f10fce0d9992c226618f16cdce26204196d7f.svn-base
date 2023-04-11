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
 * @author sajadhav
 * 
 */
@Entity
@Table(name = "TBL_MODULE_APPROVAL_DEPTWISE")
public class ModuleApprovalDeptWise implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2282361645524432423L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MODULE_APPROVAL_DEPTWISE_ID")
	private Long modApprovalDeptWiseId;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MENU_ID_FK")
	private Menu menuIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLE_TYPE_ID_FK")
	private RoleType roleTypeIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEPARTMENT_ID_FK")
	private Regulator deptIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_MODIFIED_BY_FK")
	private UserMaster lastModifiedBy;

	@Column(name = "LAST_MODIFIED_ON")
	private Date lastModifiedOn;

	/**
	 * @return the modApprovalDeptWiseId
	 */
	public Long getModApprovalDeptWiseId() {
		return modApprovalDeptWiseId;
	}

	/**
	 * @param modApprovalDeptWiseId the modApprovalDeptWiseId to set
	 */
	public void setModApprovalDeptWiseId(Long modApprovalDeptWiseId) {
		this.modApprovalDeptWiseId = modApprovalDeptWiseId;
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

	/**
	 * @return the menuIdFk
	 */
	public Menu getMenuIdFk() {
		return menuIdFk;
	}

	/**
	 * @param menuIdFk the menuIdFk to set
	 */
	public void setMenuIdFk(Menu menuIdFk) {
		this.menuIdFk = menuIdFk;
	}

	/**
	 * @return the roleTypeIdFk
	 */
	public RoleType getRoleTypeIdFk() {
		return roleTypeIdFk;
	}

	/**
	 * @param roleTypeIdFk the roleTypeIdFk to set
	 */
	public void setRoleTypeIdFk(RoleType roleTypeIdFk) {
		this.roleTypeIdFk = roleTypeIdFk;
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

	/**
	 * @return the deptIdFk
	 */
	public Regulator getDeptIdFk() {
		return deptIdFk;
	}

	/**
	 * @param deptIdFk the deptIdFk to set
	 */
	public void setDeptIdFk(Regulator deptIdFk) {
		this.deptIdFk = deptIdFk;
	}

}
