package com.iris.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iris.util.Validations;

/**
 * @author bthakare
 * @date 26-08-2019
 */

@Entity
@Table(name = "TBL_RETURN_PROPERTY_VAL")
public class ReturnPropertyValue implements Serializable {
	private static final long serialVersionUID = -8617397764124967672L;

	@Id
	@Column(name = "RETURN_PROP_VAL_ID")
	private Integer returnProprtyValId;

	@Column(name = "RETURN_PROP_VALUE")
	private String returnProValue;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RETURN_PROPERTY_FK")
	@JsonIgnore
	private ReturnProperty returnProprtyIdFK;

	public Integer getReturnProprtyValId() {
		return returnProprtyValId;
	}

	public void setReturnProprtyValId(Integer returnProprtyValId) {
		this.returnProprtyValId = returnProprtyValId;
	}

	public String getReturnProValue() {
		return returnProValue;
	}

	public void setReturnProValue(String returnProValue) {
		this.returnProValue = Validations.trimInput(returnProValue);
	}

	public ReturnProperty getReturnProprtyIdFK() {
		return returnProprtyIdFK;
	}

	public void setReturnProprtyIdFK(ReturnProperty returnProprtyIdFK) {
		this.returnProprtyIdFK = returnProprtyIdFK;
	}
}
