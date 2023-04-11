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
@Table(name = "HIVE_DIM_BANK")
public class HiveDimBank implements Serializable {

	private static final long serialVersionUID = -6896668142939695617L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bank_sk")
	private Integer bankSk;

	@Column(name = "bank_working_code")
	private String bankWorkingCode;

	@Column(name = "bank_name")
	private String bankName;

	@Column(name = "op_level1")
	private String opLevel1;

	@Column(name = "op_level2")
	private String opLevel2;

	@Column(name = "op_level3")
	private String opLevel3;

	@Column(name = "op_level4")
	private String opLevel4;

	@Column(name = "op_level5")
	private String opLevel5;

	@Column(name = "active_flag")
	private Boolean activeFlag;

	@Column(name = "start_effective_date")
	private Date startEffectiveDate;

	@Column(name = "end_effective_date")
	private Date endEffectiveDate;

	public Integer getBankSk() {
		return bankSk;
	}

	public void setBankSk(Integer bankSk) {
		this.bankSk = bankSk;
	}

	public String getBankWorkingCode() {
		return bankWorkingCode;
	}

	public void setBankWorkingCode(String bankWorkingCode) {
		this.bankWorkingCode = Validations.trimInput(bankWorkingCode);
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = Validations.trimInput(bankName);
	}

	public String getOpLevel1() {
		return opLevel1;
	}

	public void setOpLevel1(String opLevel1) {
		this.opLevel1 = Validations.trimInput(opLevel1);
	}

	public String getOpLevel2() {
		return opLevel2;
	}

	public void setOpLevel2(String opLevel2) {
		this.opLevel2 = Validations.trimInput(opLevel2);
	}

	public String getOpLevel3() {
		return opLevel3;
	}

	public void setOpLevel3(String opLevel3) {
		this.opLevel3 = Validations.trimInput(opLevel3);
	}

	public String getOpLevel4() {
		return opLevel4;
	}

	public void setOpLevel4(String opLevel4) {
		this.opLevel4 = Validations.trimInput(opLevel4);
	}

	public String getOpLevel5() {
		return opLevel5;
	}

	public void setOpLevel5(String opLevel5) {
		this.opLevel5 = Validations.trimInput(opLevel5);
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