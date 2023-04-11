package com.iris.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.iris.util.Validations;

@Entity
@Table(name = "HIVE_DIM_PRIMARY_DEALER")
public class HiveDimPrimaryDealer implements Serializable {

	private static final long serialVersionUID = -5958735093618176720L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "primary_dealer_sk")
	private Integer primaryDealerSk;

	@Column(name = "primary_dealer_code")
	private String primaryDealerCode;

	@Column(name = "primary_dealer_name")
	private String primaryDealerName;

	@Column(name = "active_flag")
	private Boolean activeFlag;

	@Column(name = "start_effective_date")
	private Date startEffectiveDate;

	@Column(name = "end_effective_date")
	private Date endEffectiveDate;

	public Integer getPrimaryDealerSk() {
		return primaryDealerSk;
	}

	public void setPrimaryDealerSk(Integer primaryDealerSk) {
		this.primaryDealerSk = primaryDealerSk;
	}

	public String getPrimaryDealerCode() {
		return primaryDealerCode;
	}

	public void setPrimaryDealerCode(String primaryDealerCode) {
		this.primaryDealerCode = Validations.trimInput(primaryDealerCode);
	}

	public String getPrimaryDealerName() {
		return primaryDealerName;
	}

	public void setPrimaryDealerName(String primaryDealerName) {
		this.primaryDealerName = Validations.trimInput(primaryDealerName);
	}

	public Boolean getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(Boolean activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Date getStartEffectiveDate() {
		return startEffectiveDate;
	}

	public void setStartEffectiveDate(Date startEffectiveDate) {
		this.startEffectiveDate = startEffectiveDate;
	}

	public Date getEndEffectiveDate() {
		return endEffectiveDate;
	}

	public void setEndEffectiveDate(Date endEffectiveDate) {
		this.endEffectiveDate = endEffectiveDate;
	}

}