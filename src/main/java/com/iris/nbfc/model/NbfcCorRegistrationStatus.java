/**
 * 
 */
package com.iris.nbfc.model;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.iris.model.UserMaster;
import com.iris.sdmx.status.entity.AdminStatus;

/**
 * @author Shivabasava Hatti
 *
 */
@Entity
@Table(name = "TBL_NBFC_COR_REG_STATUS")
public class NbfcCorRegistrationStatus implements Serializable {

	private static final long serialVersionUID = 6249528712588787207L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NBFC_COR_REG_STATUS_ID")
	private Long nbfcCorRegStatusId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COR_REG_ID_FK")
	private NbfcCorRegistrationBean corRegIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID_FK")
	private UserMaster userIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ADMIN_STATUS_ID_FK")
	private AdminStatus adminStatusIdFk;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SUBMITTED_ON")
	private Date submittedOn;

	/**
	 * @return the nbfcCorRegStatusId
	 */
	public Long getNbfcCorRegStatusId() {
		return nbfcCorRegStatusId;
	}

	/**
	 * @param nbfcCorRegStatusId the nbfcCorRegStatusId to set
	 */
	public void setNbfcCorRegStatusId(Long nbfcCorRegStatusId) {
		this.nbfcCorRegStatusId = nbfcCorRegStatusId;
	}

	public NbfcCorRegistrationBean getCorRegIdFk() {
		return corRegIdFk;
	}

	public void setCorRegIdFk(NbfcCorRegistrationBean corRegIdFk) {
		this.corRegIdFk = corRegIdFk;
	}

	/**
	 * @return the userIdFk
	 */
	public UserMaster getUserIdFk() {
		return userIdFk;
	}

	/**
	 * @param userIdFk the userIdFk to set
	 */
	public void setUserIdFk(UserMaster userIdFk) {
		this.userIdFk = userIdFk;
	}

	/**
	 * @return the adminStatusIdFk
	 */
	public AdminStatus getAdminStatusIdFk() {
		return adminStatusIdFk;
	}

	/**
	 * @param adminStatusIdFk the adminStatusIdFk to set
	 */
	public void setAdminStatusIdFk(AdminStatus adminStatusIdFk) {
		this.adminStatusIdFk = adminStatusIdFk;
	}

	/**
	 * @return the submittedOn
	 */
	public Date getSubmittedOn() {
		return submittedOn;
	}

	/**
	 * @param submittedOn the submittedOn to set
	 */
	public void setSubmittedOn(Date submittedOn) {
		this.submittedOn = submittedOn;
	}
}
