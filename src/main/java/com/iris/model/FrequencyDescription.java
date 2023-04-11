package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.iris.util.Validations;

@Entity
@Table(name = "TBL_FIN_YR_FREQUENCY_DESC")
public class FrequencyDescription implements Serializable {

	private static final long serialVersionUID = -2912341079454368876L;

	@Id
	@Column(name = "FIN_YR_FREQUENCY_DESC_ID")
	private Long finYrFrquencyDescId;

	@Column(name = "FIN_YR_FREQUENCY_DESC")
	private String finYrFrquencyDesc;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FORM_FREQUENCY_ID_FK")
	private Frequency frequency;

	/**
	 * @return the finYrFrquencyDescId
	 */
	public Long getFinYrFrquencyDescId() {
		return finYrFrquencyDescId;
	}

	/**
	 * @param finYrFrquencyDescId the finYrFrquencyDescId to set
	 */
	public void setFinYrFrquencyDescId(Long finYrFrquencyDescId) {
		this.finYrFrquencyDescId = finYrFrquencyDescId;
	}

	/**
	 * @return the finYrFrquencyDesc
	 */
	public String getFinYrFrquencyDesc() {
		return finYrFrquencyDesc;
	}

	/**
	 * @param finYrFrquencyDesc the finYrFrquencyDesc to set
	 */
	public void setFinYrFrquencyDesc(String finYrFrquencyDesc) {
		this.finYrFrquencyDesc = Validations.trimInput(finYrFrquencyDesc);
	}

	/**
	 * @return the frequency
	 */
	public Frequency getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(Frequency frequency) {
		this.frequency = frequency;
	}

}
