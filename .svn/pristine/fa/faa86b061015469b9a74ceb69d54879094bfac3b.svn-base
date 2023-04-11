package com.iris.nbfc.model;

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

/**
 * @author pmohite
 */
@Entity
@Table(name = "TBL_NBFC_CERTIFICATION_DETAILS")
public class NbfcCertificationDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NBFC_CERTIFICATION_DETAILS_ID")
	private Long nbfcCerDetalId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COR_REG_ID_FK")
	private NbfcCorRegistrationBean corRegIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID_FK")
	private UserMaster userIdFk;

	@Column(name = "FORM_PAGE_JSON_VALUE")
	private String formPageJsonValue;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NBFC_PAGE_MASTER_ID_FK")
	private NbfcPageMaster nbfcPageMaster;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NBFC_SUB_PAGE_MASTER_ID_FK")
	private NbfcSubPageMaster nbfcSubPageMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NBFC_COMPANY_TYPE_OTHER_ID_FK")
	private NbfcCompanyTypeOther nbfcCompanyTypeOtherIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NBFC_COMPANY_TYPE_ID_FK")
	private NbfcCompanyType nbfcCompanyTypeIdFk;

	/**
	 * @return the nbfcCerDetalId
	 */
	public Long getNbfcCerDetalId() {
		return nbfcCerDetalId;
	}

	/**
	 * @param nbfcCerDetalId the nbfcCerDetalId to set
	 */
	public void setNbfcCerDetalId(Long nbfcCerDetalId) {
		this.nbfcCerDetalId = nbfcCerDetalId;
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
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the updatedOn
	 */
	public Date getUpdatedOn() {
		return updatedOn;
	}

	/**
	 * @param updatedOn the updatedOn to set
	 */
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	/**
	 * @return the formPageJsonValue
	 */
	public String getFormPageJsonValue() {
		return formPageJsonValue;
	}

	/**
	 * @param formPageJsonValue the formPageJsonValue to set
	 */
	public void setFormPageJsonValue(String formPageJsonValue) {
		this.formPageJsonValue = formPageJsonValue;
	}

	/**
	 * @return the nbfcPageMaster
	 */
	public NbfcPageMaster getNbfcPageMaster() {
		return nbfcPageMaster;
	}

	/**
	 * @param nbfcPageMaster the nbfcPageMaster to set
	 */
	public void setNbfcPageMaster(NbfcPageMaster nbfcPageMaster) {
		this.nbfcPageMaster = nbfcPageMaster;
	}

	/**
	 * @return the nbfcSubPageMaster
	 */
	public NbfcSubPageMaster getNbfcSubPageMaster() {
		return nbfcSubPageMaster;
	}

	/**
	 * @param nbfcSubPageMaster the nbfcSubPageMaster to set
	 */
	public void setNbfcSubPageMaster(NbfcSubPageMaster nbfcSubPageMaster) {
		this.nbfcSubPageMaster = nbfcSubPageMaster;
	}

	/**
	 * @return the nbfcCompanyTypeOtherIdFk
	 */
	public NbfcCompanyTypeOther getNbfcCompanyTypeOtherIdFk() {
		return nbfcCompanyTypeOtherIdFk;
	}

	/**
	 * @param nbfcCompanyTypeOtherIdFk the nbfcCompanyTypeOtherIdFk to set
	 */
	public void setNbfcCompanyTypeOtherIdFk(NbfcCompanyTypeOther nbfcCompanyTypeOtherIdFk) {
		this.nbfcCompanyTypeOtherIdFk = nbfcCompanyTypeOtherIdFk;
	}

	/**
	 * @return the nbfcCompanyTypeIdFk
	 */
	public NbfcCompanyType getNbfcCompanyTypeIdFk() {
		return nbfcCompanyTypeIdFk;
	}

	/**
	 * @param nbfcCompanyTypeIdFk the nbfcCompanyTypeIdFk to set
	 */
	public void setNbfcCompanyTypeIdFk(NbfcCompanyType nbfcCompanyTypeIdFk) {
		this.nbfcCompanyTypeIdFk = nbfcCompanyTypeIdFk;
	}

}
