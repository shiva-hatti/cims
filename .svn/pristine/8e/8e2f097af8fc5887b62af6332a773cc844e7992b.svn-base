package com.iris.formula.gen;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.iris.model.AutoCalculationFormula;
import com.iris.model.ReturnTemplate;
import com.iris.model.UserMaster;

@Entity
@Table(name = "TBL_AUTO_CAL_VERSION_MAP")
public class AutoCalVesrion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2350715759651691461L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AUTO_CAL_VERSION_ID")
	private Long autoCalVesrionId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_TEMPLATE_ID_FK")
	private ReturnTemplate returnTemplateFk;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AUTO_CAL_ID_FK")
	private AutoCalculationFormula autoFormula;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY_FK")
	private UserMaster createdByFk;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFIED_BY_FK")
	private UserMaster modifiedByFk;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "MODIFIED_ON")
	private Date updatedOn;

	public Long getAutoCalVesrionId() {
		return autoCalVesrionId;
	}

	public void setAutoCalVesrionId(Long autoCalVesrionId) {
		this.autoCalVesrionId = autoCalVesrionId;
	}

	public ReturnTemplate getReturnTemplateFk() {
		return returnTemplateFk;
	}

	public void setReturnTemplateFk(ReturnTemplate returnTemplateFk) {
		this.returnTemplateFk = returnTemplateFk;
	}

	public AutoCalculationFormula getAutoFormula() {
		return autoFormula;
	}

	public void setAutoFormula(AutoCalculationFormula autoFormula) {
		this.autoFormula = autoFormula;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public UserMaster getCreatedByFk() {
		return createdByFk;
	}

	public void setCreatedByFk(UserMaster createdByFk) {
		this.createdByFk = createdByFk;
	}

	public UserMaster getModifiedByFk() {
		return modifiedByFk;
	}

	public void setModifiedByFk(UserMaster modifiedByFk) {
		this.modifiedByFk = modifiedByFk;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

}
