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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.iris.util.AESV2;
import com.iris.util.Validations;
import com.iris.util.constant.GeneralConstants;

/**
 * This class represents users role wise assigned companies
 */
@Entity
@Table(name = "TBL_USER_ENTITY_ROLE")
public class UserEntityRole implements Serializable {

	private static final long serialVersionUID = 3142629929077640073L;
	private static Logger LOGGER = LogManager.getLogger(UserEntityRole.class);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ENTITY_ROLE_ID")
	private long userEntityRoleId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENTITY_ID_FK")
	private EntityBean entityBean;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_ROLE_MASTER_ID_FK")
	private UserRoleMaster userRoleMaster;

	@Column(name = "COMPANY_EMAIL")
	private String companyEmail;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Transient
	private String userName;

	@Transient
	private Long userId;

	@Transient
	private Long userRoleId;

	@Transient
	private UserRoleMaster userRoleMasters;

	@Transient
	private EntityBean entityBeans;

	public UserEntityRole(Long userId, Long userRoleId, String companyEmail) {
		this.userId = userId;
		this.userRoleId = userRoleId;
		this.companyEmail = companyEmail;
	}

	public UserEntityRole() {

	}

	public UserRoleMaster getUserRoleMasters() {
		return userRoleMasters;
	}

	public void setUserRoleMasters(UserRoleMaster userRoleMasters) {
		this.userRoleMasters = userRoleMasters;
	}

	public EntityBean getEntityBeans() {
		return entityBeans;
	}

	public void setEntityBeans(EntityBean entityBeans) {
		this.entityBeans = entityBeans;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	/**
	 * @return the userEntityRoleId
	 */
	public long getUserEntityRoleId() {
		return userEntityRoleId;
	}

	/**
	 * @param userEntityRoleId the userEntityRoleId to set
	 */
	public void setUserEntityRoleId(long userEntityRoleId) {
		this.userEntityRoleId = userEntityRoleId;
	}

	/**
	 * @return the entityBean
	 */
	public EntityBean getEntityBean() {
		return entityBean;
	}

	/**
	 * @param entityBean the entityBean to set
	 */
	public void setEntityBean(EntityBean entityBean) {
		this.entityBean = entityBean;
	}

	/**
	 * @return the userRoleMaster
	 */
	public UserRoleMaster getUserRoleMaster() {
		return userRoleMaster;
	}

	/**
	 * @param userRoleMaster the userRoleMaster to set
	 */
	public void setUserRoleMaster(UserRoleMaster userRoleMaster) {
		this.userRoleMaster = userRoleMaster;
	}

	public String getCompanyEmail() {
		if (!Validations.isEmpty(companyEmail)) {
			try {
				return AESV2.getInstance().decrypt(companyEmail);
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
				return companyEmail;
			}
		} else {
			return companyEmail;
		}
	}

	public void setCompanyEmail(String companyEmail) {
		if (!Validations.isEmpty(companyEmail)) {
			try {
				this.companyEmail = AESV2.getInstance().encrypt(Validations.trimInput(companyEmail));
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
				this.companyEmail = Validations.trimInput(companyEmail);
			}
		} else {
			this.companyEmail = Validations.trimInput(companyEmail);
		}
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}