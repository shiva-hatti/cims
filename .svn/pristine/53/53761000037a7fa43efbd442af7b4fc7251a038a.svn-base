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
@Table(name = "HIVE_DIM_INDIAN_AGENT")
public class HiveDimIndianAgent implements Serializable {

	private static final long serialVersionUID = 6010268338148052436L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "indian_agent_sk")
	private Integer indianAgentSk;

	@Column(name = "indian_agent_code")
	private String indianAgentCode;

	@Column(name = "indian_agent_name")
	private String indianAgentName;

	@Column(name = "active_flag")
	private Boolean activeFlag;

	@Column(name = "start_effective_date")
	private Date startEffectiveDate;

	@Column(name = "end_effective_date")
	private Date endEffectiveDate;

	public Integer getIndianAgentSk() {
		return indianAgentSk;
	}

	public void setIndianAgentSk(Integer indianAgentSk) {
		this.indianAgentSk = indianAgentSk;
	}

	public String getIndianAgentCode() {
		return indianAgentCode;
	}

	public void setIndianAgentCode(String indianAgentCode) {
		this.indianAgentCode = Validations.trimInput(indianAgentCode);
	}

	public String getIndianAgentName() {
		return indianAgentName;
	}

	public void setIndianAgentName(String indianAgentName) {
		this.indianAgentName = Validations.trimInput(indianAgentName);
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