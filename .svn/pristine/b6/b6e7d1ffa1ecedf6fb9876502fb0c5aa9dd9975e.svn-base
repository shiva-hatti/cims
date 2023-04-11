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
 * This is the Error Key bean class with Hibernate mapping.
 * 
 * @author pippar
 * @date 11/06/2015
 */
@Entity
@Table(name = "TBL_ERROR_KEY")
public class ErrorKey implements Serializable {

	private static final long serialVersionUID = 3372670773320844727L;

	@Id
	@Column(name = "ERROR_ID")
	private Long errorId;

	@Column(name = "ERROR_KEY")
	private String errorKey;

	@Column(name = "DEFAULT_NAME")
	private String defaultName;

	@Column(name = "ERROR_REASON")
	private String actionName;

	@Column(name = "ERROR_CODE")
	private String errorCode;

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public Long getErrorId() {
		return errorId;
	}

	public void setErrorId(Long errorId) {
		this.errorId = errorId;
	}

	public String getErrorKey() {
		return errorKey;
	}

	public void setErrorKey(String errorKey) {
		this.errorKey = Validations.trimInput(errorKey);
	}

	@OneToMany(mappedBy = "errorIdFk")
	@OrderBy("languageIdFk")
	private Set<ErrorKeyLabel> errorKeyLblSet;

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
	 * @return the actionName
	 */
	public String getActionName() {
		return actionName;
	}

	/**
	 * @param actionName the actionName to set
	 */
	public void setActionName(String actionName) {
		this.actionName = Validations.trimInput(actionName);
	}

	/**
	 * @return the errorKeyLblSet
	 */
	public Set<ErrorKeyLabel> getErrorKeyLblSet() {
		return errorKeyLblSet;
	}

	/**
	 * @param errorKeyLblSet the errorKeyLblSet to set
	 */
	public void setErrorKeyLblSet(Set<ErrorKeyLabel> errorKeyLblSet) {
		this.errorKeyLblSet = errorKeyLblSet;
	}

}