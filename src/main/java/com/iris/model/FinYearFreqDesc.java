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

/**
 * @author Snehal
 *
 */
@Entity
@Table(name = "TBL_FIN_YR_FREQUENCY_DESC")
public class FinYearFreqDesc implements Serializable {

	private static final long serialVersionUID = -534718712167736952L;

	@Id
	@Column(name = "FIN_YR_FREQUENCY_DESC_ID")
	private Long finYrFreqDescId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FORM_FREQUENCY_ID_FK")
	private Frequency formFrequency;

	@Column(name = "FIN_YR_FREQUENCY_DESC")
	private String finYrFreqDesc;

	public Long getFinYrFreqDescId() {
		return finYrFreqDescId;
	}

	public void setFinYrFreqDescId(Long finYrFreqDescId) {
		this.finYrFreqDescId = finYrFreqDescId;
	}

	/**
	 * @return the formFrequency
	 */
	public Frequency getFormFrequency() {
		return formFrequency;
	}

	/**
	 * @param formFrequency the formFrequency to set
	 */
	public void setFormFrequency(Frequency formFrequency) {
		this.formFrequency = formFrequency;
	}

	public String getFinYrFreqDesc() {
		return finYrFreqDesc;
	}

	public void setFinYrFreqDesc(String finYrFreqDesc) {
		this.finYrFreqDesc = Validations.trimInput(finYrFreqDesc);
	}

}
