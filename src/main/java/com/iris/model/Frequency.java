package com.iris.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.iris.util.Validations;

/**
 * This is the Frequency bean class with Hibernate mapping.
 * 
 * @author psawant
 * @date 06/10/2015 version 1.0
 */

@Entity
@Table(name = "TBL_FREQUENCY")
public class Frequency implements Serializable {

	private static final long serialVersionUID = 1976143648453851502L;

	@Id
	@Column(name = "FREQUENCY_ID")
	private Long frequencyId;

	@Column(name = "FREQUENCY_NAME")
	private String frequencyName;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@OneToMany(mappedBy = "formFrequency", fetch = FetchType.LAZY)
	@OrderBy("finYrFreqDescId")
	private Set<FinYearFreqDesc> finYearFreqDesc;

	@Transient
	private Long roleIdKey;

	@OneToMany(mappedBy = "frequency", fetch = FetchType.LAZY)
	@OrderBy("finYrFrquencyDescId")
	private List<FrequencyDescription> freqDesc;

	@Transient
	private String freqLbl;

	@Column(name = "FREQUENCY_CODE")
	private String frequencyCode;

	/**
	 * @return the freqDesc
	 */
	public List<FrequencyDescription> getFreqDesc() {
		return freqDesc;
	}

	/**
	 * @param freqDesc the freqDesc to set
	 */
	public void setFreqDesc(List<FrequencyDescription> freqDesc) {
		this.freqDesc = freqDesc;
	}

	/**
	 * @return the frequencyId
	 */
	public Long getFrequencyId() {
		return frequencyId;
	}

	/**
	 * @param frequencyId the frequencyId to set
	 */
	public void setFrequencyId(Long frequencyId) {
		this.frequencyId = frequencyId;
	}

	/**
	 * @return the frequencyName
	 */
	public String getFrequencyName() {
		return frequencyName;
	}

	/**
	 * @param frequencyName the frequencyName to set
	 */
	public void setFrequencyName(String frequencyName) {
		this.frequencyName = Validations.trimInput(frequencyName);
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = Validations.trimInput(description);
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

	/**
	 * @return the roleIdKey
	 */
	public Long getRoleIdKey() {
		return roleIdKey;
	}

	/**
	 * @param roleIdKey the roleIdKey to set
	 */
	public void setRoleIdKey(Long roleIdKey) {
		this.roleIdKey = roleIdKey;
	}

	/**
	 * @return the finYearFreqDesc
	 */
	public Set<FinYearFreqDesc> getFinYearFreqDesc() {
		return finYearFreqDesc;
	}

	/**
	 * @param finYearFreqDesc the finYearFreqDesc to set
	 */
	public void setFinYearFreqDesc(Set<FinYearFreqDesc> finYearFreqDesc) {
		this.finYearFreqDesc = finYearFreqDesc;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Frequency other = (Frequency) obj;
		if (frequencyId == null) {
			if (other.frequencyId != null) {
				return false;
			}
		} else if (!frequencyId.equals(other.frequencyId)) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		return frequencyId.intValue();
	}

	/**
	 * @return the freqLbl
	 */
	public String getFreqLbl() {
		return freqLbl;
	}

	/**
	 * @param freqLbl the freqLbl to set
	 */
	public void setFreqLbl(String freqLbl) {
		this.freqLbl = Validations.trimInput(freqLbl);
	}

	/**
	 * @return the frequencyCode
	 */
	public String getFrequencyCode() {
		return frequencyCode;
	}

	/**
	 * @param frequencyCode the frequencyCode to set
	 */
	public void setFrequencyCode(String frequencyCode) {
		this.frequencyCode = frequencyCode;
	}
}
