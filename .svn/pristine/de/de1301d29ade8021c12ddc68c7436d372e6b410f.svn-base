package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.iris.util.Validations;

/**
 * @author psawant
 * @version 1.0
 * @date 17/10/2017
 */
@Entity
@Table(name = "TBL_DYNAMIC_FIELD_TYPE")
public class DynamicFieldType implements Serializable {

	private static final long serialVersionUID = -4648338829083580933L;

	@Id
	@Column(name = "FIELD_TYPE_ID")
	private Long fieldTypeId;

	@Column(name = "FIELD_TYPE_NAME")
	private String fieldTypeName;

	/**
	 * @return the fieldTypeId
	 */
	public Long getFieldTypeId() {
		return fieldTypeId;
	}

	/**
	 * @param fieldTypeId the fieldTypeId to set
	 */
	public void setFieldTypeId(Long fieldTypeId) {
		this.fieldTypeId = fieldTypeId;
	}

	/**
	 * @return the fieldTypeName
	 */
	public String getFieldTypeName() {
		return fieldTypeName;
	}

	/**
	 * @param fieldTypeName the fieldTypeName to set
	 */
	public void setFieldTypeName(String fieldTypeName) {
		this.fieldTypeName = Validations.trimInput(fieldTypeName);
	}

}