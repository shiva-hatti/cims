/**
 * 
 */
package com.iris.nbfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Shivabasava
 *
 */
@Entity
@Table(name = "TBL_NBFC_DIRECTOR_TYPE")
public class NbfcDirectorType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DIRECTOR_TYPE_ID")
	private Long directorTypeId;

	@Column(name = "DIRECTOR_TYPE")
	private String directorType;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "DIRECTOR_TYPE_BIL")
	private String directorTypeBil;

	/**
	 * @return the directorTypeId
	 */
	public Long getDirectorTypeId() {
		return directorTypeId;
	}

	/**
	 * @param directorTypeId the directorTypeId to set
	 */
	public void setDirectorTypeId(Long directorTypeId) {
		this.directorTypeId = directorTypeId;
	}

	/**
	 * @return the directorType
	 */
	public String getDirectorType() {
		return directorType;
	}

	/**
	 * @param directorType the directorType to set
	 */
	public void setDirectorType(String directorType) {
		this.directorType = directorType;
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
	 * @return the directorTypeBil
	 */
	public String getDirectorTypeBil() {
		return directorTypeBil;
	}

	/**
	 * @param directorTypeBil the directorTypeBil to set
	 */
	public void setDirectorTypeBil(String directorTypeBil) {
		this.directorTypeBil = directorTypeBil;
	}
}
