package com.iris.dto;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author sdhone
 * 
 */
@JsonInclude(Include.NON_NULL)
public class OwnerReturn implements Serializable {

	private static final long serialVersionUID = -3832927154600939183L;

	private Long returnId;
	private String returnCode;
	private String returnName;
	private String currentMaxVersion;

	public OwnerReturn(Long returnId) {
		super();
		this.returnId = returnId;
	}

	public OwnerReturn(Long returnId, String returnCode) {
		super();
		this.returnId = returnId;
		this.returnCode = returnCode;
	}

	public OwnerReturn(Long returnId, String returnCode, String returnName) {
		super();
		this.returnId = returnId;
		this.returnCode = returnCode;
		this.returnName = returnName;
	}

	public OwnerReturn() {
		super();
	}

	public Long getReturnId() {
		return returnId;
	}

	public void setReturnId(Long returnId) {
		this.returnId = returnId;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnName() {
		return returnName;
	}

	public void setReturnName(String returnName) {
		this.returnName = returnName;
	}

	public String getCurrentMaxVersion() {
		return currentMaxVersion;
	}

	public void setCurrentMaxVersion(String currentMaxVersion) {
		this.currentMaxVersion = currentMaxVersion;
	}

	@Override
	public int hashCode() {
		return Objects.hash(returnCode, returnId, returnName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OwnerReturn other = (OwnerReturn) obj;
		return Objects.equals(returnCode, other.returnCode) && Objects.equals(returnId, other.returnId) && Objects.equals(returnName, other.returnName);
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "OwnerReturn [returnCode=" + returnCode + "]";
	}

}