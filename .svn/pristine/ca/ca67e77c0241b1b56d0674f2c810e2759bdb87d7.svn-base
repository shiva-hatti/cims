package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_INSTITUTION_TYPE")
public class InstitutionType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5064796237211173987L;

	@Id
	@Column(name = "INSTITUTION_TYPE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long instTypeId;

	@Column(name = "INSTITUTION_TYPE")
	private String instTypeName;

	public Long getInstTypeId() {
		return instTypeId;
	}

	public void setInstTypeId(Long instTypeId) {
		this.instTypeId = instTypeId;
	}

	public String getInstTypeName() {
		return instTypeName;
	}

	public void setInstTypeName(String instTypeName) {
		this.instTypeName = instTypeName;
	}

}
