package com.iris.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.iris.util.Validations;

/**
 * 
 * This is the Financial Year Format bean class with Hibernate mapping.
 * 
 * @author sgoswami
 * @date 23/10/2015
 * @version 1.0
 * 
 */

@Entity
@Table(name = "TBL_FINANCIAL_YEAR_FORMAT")
public class FinYrFormat implements Serializable {

	private static final long serialVersionUID = -7068721144291647524L;

	@Id
	@Column(name = "FINANCIAL_YEAR_FORMAT_ID")
	private Long finYrFormatId;

	@Column(name = "FINANCIAL_YEAR_FORMAT_NAME")
	private String finYrFormatName;

	@OneToMany(mappedBy = "finYrFormat")
	private Set<EntityBean> entitys;

	/**
	 * @return the entitys
	 */
	public Set<EntityBean> getEntitys() {
		return entitys;
	}

	/**
	 * @param entitys the entitys to set
	 */
	public void setEntitys(Set<EntityBean> entitys) {
		this.entitys = entitys;
	}

	/**
	 * @return the finYrFormatId
	 */
	public Long getFinYrFormatId() {
		return finYrFormatId;
	}

	/**
	 * @param finYrFormatId the finYrFormatId to set
	 */
	public void setFinYrFormatId(Long finYrFormatId) {
		this.finYrFormatId = finYrFormatId;
	}

	/**
	 * @return the finYrFormatName
	 */
	public String getFinYrFormatName() {
		return finYrFormatName;
	}

	/**
	 * @param finYrFormatName the finYrFormatName to set
	 */
	public void setFinYrFormatName(String finYrFormatName) {
		this.finYrFormatName = Validations.trimInput(finYrFormatName);
	}

}
