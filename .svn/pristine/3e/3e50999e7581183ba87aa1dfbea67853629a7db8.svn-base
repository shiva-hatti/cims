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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.iris.util.AESV2;
import com.iris.util.Validations;
import com.iris.util.constant.GeneralConstants;

/**
 * @author psheke
 * @date 03/12/2020
 */
@Entity
@Table(name = "TBL_USER_AUDITOR_ROLE")
public class UserAuditorRole implements Serializable {

	private static final long serialVersionUID = 3713522625830521758L;
	private static Logger LOGGER = LogManager.getLogger(UserAuditorRole.class);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_AUDIT_ROLE_ID")
	private long userAuditorRoleId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AUDIT_FIRM_ID_FK")
	private AuditFirmInfo auditFirm;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ROLE_MASTER_ID_FK")
	private UserRoleMaster userRoleMaster;

	@Column(name = "AUDITOR_EMAIL")
	private String auditorEmail;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	/**
	 * @return the userAuditorRoleId
	 */
	public long getUserAuditorRoleId() {
		return userAuditorRoleId;
	}

	/**
	 * @param userAuditorRoleId the userAuditorRoleId to set
	 */
	public void setUserAuditorRoleId(long userAuditorRoleId) {
		this.userAuditorRoleId = userAuditorRoleId;
	}

	/**
	 * @return the auditFirm
	 */
	public AuditFirmInfo getAuditFirm() {
		return auditFirm;
	}

	/**
	 * @param auditFirm the auditFirm to set
	 */
	public void setAuditFirm(AuditFirmInfo auditFirm) {
		this.auditFirm = auditFirm;
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

	/**
	 * @return the auditorEmail
	 */
	public String getAuditorEmail() {
		if (!Validations.isEmpty(auditorEmail)) {
			try {
				return AESV2.getInstance().decrypt(auditorEmail);
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
				return auditorEmail;
			}
		} else {
			return auditorEmail;
		}
	}

	/**
	 * @param auditorEmail the auditorEmail to set
	 */
	public void setAuditorEmail(String auditorEmail) {
		if (!Validations.isEmpty(auditorEmail)) {
			try {
				this.auditorEmail = AESV2.getInstance().encrypt(Validations.trimInput(auditorEmail));
			} catch (Exception e) {
				LOGGER.error(GeneralConstants.ERROR_MSG_BEAN.getConstantVal(), e);
				this.auditorEmail = Validations.trimInput(auditorEmail);
			}
		} else {
			this.auditorEmail = Validations.trimInput(auditorEmail);
		}
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
