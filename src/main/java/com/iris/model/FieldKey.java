package com.iris.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.iris.util.Validations;

/**
 * This is the Field Key bean class with Hibernate mapping.
 * 
 * @author pippar
 * @date 11/06/2015
 */

@Entity
@Table(name = "TBL_FIELD_KEY")
public class FieldKey implements Serializable {

	private static final long serialVersionUID = 746537541873097573L;

	@Id
	@Column(name = "FIELD_ID")
	private Long fieldId;

	@Column(name = "FIELD_KEY")
	private String fieldKey;

	@Column(name = "DEFAULT_NAME")
	private String defaultName;

	public Long getFieldId() {
		return fieldId;
	}

	public void setFieldId(Long fieldId) {
		this.fieldId = fieldId;
	}

	public String getFieldKey() {
		return fieldKey;
	}

	public void setFieldKey(String fieldKey) {
		this.fieldKey = Validations.trimInput(fieldKey);
	}

	@OneToMany(mappedBy = "fieldIdFk")
	@OrderBy("languageIdFk")
	private Set<FieldKeyLabel> fieldKeyLblSet;

	/**
	 * @return the defaultName
	 */
	public String getDefaultName() {
		return defaultName;
	}

	/**
	 * @param defaultName the defaultName to set
	 */
	public void setDefaultName(String defaultName) {
		this.defaultName = Validations.trimInput(defaultName);
	}

	/**
	 * @return the fieldKeyLblSet
	 */
	public Set<FieldKeyLabel> getFieldKeyLblSet() {
		return fieldKeyLblSet;
	}

	/**
	 * @param fieldKeyLblSet the fieldKeyLblSet to set
	 */
	public void setFieldKeyLblSet(Set<FieldKeyLabel> fieldKeyLblSet) {
		this.fieldKeyLblSet = fieldKeyLblSet;
	}

}