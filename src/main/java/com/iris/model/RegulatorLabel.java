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

import com.iris.util.Validations;

/**
 * @author psawant
 * @version 1.0
 * @date 12/07/2018
 */
@Entity
@Table(name = "TBL_REGULATOR_LABEL")
public class RegulatorLabel implements Serializable {

	private static final long serialVersionUID = -8580020820078836632L;

	@Id
	//	@SequenceGenerator(name = "REGULATOR_LABEL_ID_GENERATOR", sequenceName = "TBL_REGULATOR_LABEL_SEQ", allocationSize = 1)
	//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REGULATOR_LABEL_ID_GENERATOR")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "REGULATOR_LABEL_ID")
	private Long regulatorLabelId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REGULATOR_ID_FK")
	private Regulator regulatorIdFk;

	@Column(name = "REGULATOR_LABEL")
	private String regulatorLabel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LANGUAGE_ID_FK")
	private LanguageMaster langIdFk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_MODIFIED_BY_FK")
	private UserMaster lastModifiedByFk;

	@Column(name = "LAST_MODIFIED_ON")
	private Date lastModifiedOn;

	@Column(name = "LAST_UPDATED_ON")
	private Date lastUpdatedOn;

	/**
	 * @return the regulatorLabelId
	 */
	public Long getRegulatorLabelId() {
		return regulatorLabelId;
	}

	/**
	 * @param regulatorLabelId the regulatorLabelId to set
	 */
	public void setRegulatorLabelId(Long regulatorLabelId) {
		this.regulatorLabelId = regulatorLabelId;
	}

	/**
	 * @return the regulatorIdFk
	 */
	public Regulator getRegulatorIdFk() {
		return regulatorIdFk;
	}

	/**
	 * @param regulatorIdFk the regulatorIdFk to set
	 */
	public void setRegulatorIdFk(Regulator regulatorIdFk) {
		this.regulatorIdFk = regulatorIdFk;
	}

	/**
	 * @return the regulatorLabel
	 */
	public String getRegulatorLabel() {
		return regulatorLabel;
	}

	/**
	 * @param regulatorLabel the regulatorLabel to set
	 */
	public void setRegulatorLabel(String regulatorLabel) {
		this.regulatorLabel = Validations.trimInput(regulatorLabel);
	}

	/**
	 * @return the langIdFk
	 */
	public LanguageMaster getLangIdFk() {
		return langIdFk;
	}

	/**
	 * @param langIdFk the langIdFk to set
	 */
	public void setLangIdFk(LanguageMaster langIdFk) {
		this.langIdFk = langIdFk;
	}

	/**
	 * @return the lastModifiedByFk
	 */
	public UserMaster getLastModifiedByFk() {
		return lastModifiedByFk;
	}

	/**
	 * @param lastModifiedByFk the lastModifiedByFk to set
	 */
	public void setLastModifiedByFk(UserMaster lastModifiedByFk) {
		this.lastModifiedByFk = lastModifiedByFk;
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
	 * @return the lastUpdatedOn
	 */
	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	/**
	 * @param lastUpdatedOn the lastUpdatedOn to set
	 */
	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

}